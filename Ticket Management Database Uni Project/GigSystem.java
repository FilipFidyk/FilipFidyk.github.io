import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Vector;

public class GigSystem {

    public static void main(String[] args) {

        // You should only need to fetch the connection details once
        // You might need to change this to either getSocketConnection() or getPortConnection() - see below
        Connection conn = getSocketConnection();

        boolean repeatMenu = true;

        while(repeatMenu){
            System.out.println("_________________________");
            System.out.println("________GigSystem________");
            System.out.println("_________________________");

            
            System.out.println("q: Quit");

            String menuChoice = readEntry("Please choose an option: ");

            if(menuChoice.length() == 0){
                //Nothing was typed (user just pressed enter) so start the loop again
                continue;
            }
            char option = menuChoice.charAt(0);

            /**
             * If you are going to implement a menu, you must read input before you call the actual methods
             * Do not read input from any of the actual task methods
             */
            switch(option){
                case '1':
                    boolean loop = true ;
                    while (loop){
                        String gigID = readEntry("Please enter the gig ID: ");
                        try{
                            String[][] result = task1(conn, Integer.parseInt(gigID));
                            if (result == null){
                                System.out.println("No results");
                            }
                            else{
                                printTable(result);
                            }
                            loop = false;
                        } catch (Exception e){
                            System.out.println("Input needs to be a string.");
                        }
                    }
                    break;

                case '2':
                    break;
                case '3':
                    break;
                case '4':
                    break;
                case '5':
                    break;
                case '6':
                    break;
                case '7':
                    break;
                case '8':
                    break;
                case 'q':
                    repeatMenu = false;
                    break;
                default: 
                    System.out.println("Invalid option");
            }
        }
    }

    /*
     * You should not change the names, input parameters or return types of any of the predefined methods in GigSystem.java
     * You may add extra methods if you wish (and you may overload the existing methods - as long as the original version is implemented)
     */

    public static String[][] task1(Connection conn, int gigID){
        //Prepare the statement that will get the actnames, starttimes and endtimes given the gigID

        try{
            PreparedStatement query = conn.prepareStatement(
                "SELECT actname, to_char(ontime,'HH24:MI'), to_char((ontime+INTERVAL '1 minute' * duration), 'HH24:MI') AS offtime " + 
                "FROM ACT_GIG NATURAL JOIN ACT " + 
                "WHERE gigID = ? ORDER BY ontime;");
            try {
                query.setInt(1, gigID);
            } catch (SQLException e) {
                query.setNull(1, Types.INTEGER);
            }
            ResultSet rs = query.executeQuery(); 
            return convertResultToStrings(rs);
        } catch (SQLException e){
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void task2(Connection conn, String venue, String gigTitle, LocalDateTime gigStart, int adultTicketPrice, ActPerformanceDetails[] actDetails){
        //Find venueID
        ResultSet venueRS;
        int javaVenueID;
        
        try{    
            PreparedStatement findVenueID = conn.prepareStatement("SELECT venueID FROM VENUE WHERE venuename = ?");
            try {
                findVenueID.setString(1, venue);
            } catch (SQLException e) {
                //If there is a problem setting the name then we can't find the venue ID and everything won't work
                e.printStackTrace();
                return;
            }
            venueRS = findVenueID.executeQuery();
            venueRS.next();
            javaVenueID = venueRS.getInt("venueID");
        } catch (SQLException e){
            //if we can't get venue id for some SQL reason return
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return;
        }
        
        //turn off auto commit
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            return;
        }
        
        //Create GIG entry 
        ResultSet gigID;
        try {
            PreparedStatement gigInsert = conn.prepareStatement("INSERT INTO GIG (gigID, venueID, gigtitle, gigdatetime, gigstatus) VALUES (DEFAULT,?,?,?,?) RETURNING gigID");
            try {
                gigInsert.setInt(1, javaVenueID);
            } catch (Exception e) {
                gigInsert.setNull(1, Types.INTEGER);
            }

            try {
                gigInsert.setString(2, gigTitle);
            } catch (Exception e) {
                gigInsert.setNull(2, Types.VARCHAR);
            }

            try {
                gigInsert.setObject(3, gigStart);
            } catch (Exception e) {
                gigInsert.setNull(3, Types.TIMESTAMP);
            }

            try {
                gigInsert.setString(4, "G");
            } catch (Exception e) {
                gigInsert.setNull(4, Types.VARCHAR);
            }

            //The insert also returns the new gigID
            gigInsert.execute();
            gigID = gigInsert.getResultSet();
            gigID.next();
        } catch (SQLException e) {
            //If any errors occur rollback any changes and turn Autocommit back on
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            try{
                conn.rollback();
                conn.setAutoCommit(true);     
            } catch (SQLException f) {
                System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                f.printStackTrace();
            }
            return;
        }

        //Create GIG_TICKET entry
        try {
            PreparedStatement gigTicket = conn.prepareStatement("INSERT INTO GIG_TICKET VALUES (?,?,?)");
            try {
                gigTicket.setInt(1, gigID.getInt("gigID"));
            } catch (SQLException e) {
                gigTicket.setNull(1, Types.INTEGER);
            }

            try {
                gigTicket.setString(2, "A");
            } catch (SQLException e) {
                gigTicket.setNull(2, Types.VARCHAR);
            }

            try {
                gigTicket.setInt(3, adultTicketPrice);
            } catch (SQLException e) {
                gigTicket.setNull(3, Types.INTEGER);
            }

            gigTicket.executeUpdate();
        } catch (SQLException e) {
            //If any errors occur rollback any changes and turn Autocommit back on
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            try{
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException f) {
                System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                f.printStackTrace();
            }
            return;
        }

        //Add ACT_GIG entries
        try {
            PreparedStatement actGig = conn.prepareStatement("INSERT INTO ACT_GIG VALUES (?,?,?,?,?)");
            for (int i = 0; i < actDetails.length;i++){
                try {
                    actGig.setInt(1, actDetails[i].getActID());
                } catch (SQLException e) {
                    actGig.setNull(1, Types.INTEGER);
                }
                try {
                    actGig.setInt(2, gigID.getInt("gigID"));
                } catch (SQLException e) {
                    actGig.setNull(2, Types.INTEGER);
                }
                try {
                    actGig.setInt(3, actDetails[i].getFee());
                } catch (SQLException e) {
                    actGig.setNull(3, Types.INTEGER);
                }
                try {
                    actGig.setObject(4, actDetails[i].getOnTime());
                } catch (SQLException e) {
                    actGig.setNull(4, Types.TIMESTAMP);
                }
                try {
                    actGig.setInt(5, actDetails[i].getDuration());
                } catch (SQLException e) {
                    actGig.setNull(5, Types.INTEGER);
                }

                actGig.executeUpdate();
            }
        } catch (SQLException e) {
            //If any errors occur rollback any changes and turn Autocommit back on
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            try{
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException f) {
                System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                f.printStackTrace();
            }
            return;
        }

        //Verify business rules that aren't triggers
        try{
            if (verifyStartTime(conn, gigID.getInt("gigID"), gigStart) && verifyEmptyIntervals(conn, gigID.getInt("gigID")) && verifyGigLength(conn, gigID.getInt("gigID"))){
                conn.commit();
            }
            else{
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        }
    }

    public static void task3(Connection conn, int gigid, String name, String email, String ticketType){
        ResultSet ticketTypes, size;
        boolean found = false;

        //Find if the correct gig and ticket type exist
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT pricetype FROM GIG_TICKET WHERE gigID = ?");
            PreparedStatement psSize = conn.prepareStatement("SELECT count(*) AS s FROM GIG_TICKET WHERE gigID = ?");
            ps.setInt(1, gigid);
            psSize.setInt(1, gigid);

            ticketTypes = ps.executeQuery();
            size = psSize.executeQuery();
            ticketTypes.next();
            size.next();
            
            if (size.getInt("s") == 0){
                return;
            }

            //Iterate through the resultset to find the ticket type
            for (int i = 0; i < size.getInt("s"); i++){
                if (ticketTypes.getString("pricetype").equals(ticketType)){
                    found = true;
                    break;
                }
                ticketTypes.next();
            }

        } catch (SQLException e) {
            //Any errors dont do anything and leave
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return;
        }

        //Ticket type didn't exist, leave early
        if (!found){
            return;
        }

        //Gig and ticket type exist so we add the new ticket to TICKET 
        try {
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO TICKET (gigID, pricetype, cost, customername, customeremail) VALUES (?,?,?,?,?)");

            try {
                ps2.setInt(1, gigid);
            } catch (Exception e) {
                ps2.setNull(1, Types.INTEGER);
            }

            try {
                ps2.setString(2, ticketType);
            } catch (Exception e) {
                ps2.setNull(2, Types.VARCHAR);
            }

            try {
                ps2.setInt(3, ticketTypes.getInt("price"));
            } catch (Exception e) {
                ps2.setNull(3, Types.TIMESTAMP);
            }

            try {
                ps2.setString(4, name);
            } catch (Exception e) {
                ps2.setNull(4, Types.VARCHAR);
            }

            try {
                ps2.setString(5, email);
            } catch (Exception e) {
                ps2.setNull(4, Types.VARCHAR);
            }

            ps2.executeUpdate();
        } catch (SQLException e) {
            //Any errors dont do anything and leave
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return;
        }

    }

    public static String[][] task4(Connection conn, int gigID, String actName){
        //Find actid 
        int actID;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT actID FROM act WHERE actname = ?");

            try {
                ps.setString(1, actName);
            } catch (SQLException e) {
                ps.setNull(1, Types.VARCHAR);
            }

            ResultSet rsActID = ps.executeQuery();
            rsActID.next();
            actID = rsActID.getInt("actID");
        } catch (SQLException e) {
            //SQL Error or act doesn't exist so no changes
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return new String[0][0];
        }
        
        //Check if we are removing a headline act
        boolean returnUsers = false;
        try{
            PreparedStatement hActPS = conn.prepareStatement("SELECT count (*) FROM ACT_GIG ag WHERE gigid = ? AND actid = ? AND ontime = (SELECT MAX(ontime) FROM ACT_GIG WHERE gigID = ag.gigID)");
            try {
                hActPS.setInt(1, gigID);
            } catch (SQLException e) {
                hActPS.setNull(1, Types.INTEGER);
            }
            try {
                hActPS.setInt(2, actID);
            } catch (SQLException e) {
                hActPS.setNull(2, Types.INTEGER);
            }
            ResultSet hActRS = hActPS.executeQuery();
            hActRS.next();
            if (hActRS.getInt("count") == 1){
                returnUsers = true;
            }
        } catch (SQLException e){
            //if an error occurs, something major will be caught later and if it is about the headline act just assume we can't find it
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        }

        //Remove act appearances - turn of validity triggers to allow intermediate steps during the readjusting trigger
        try {
            PreparedStatement triggerWorkaround = conn.prepareStatement("ALTER TABLE ACT_GIG DISABLE TRIGGER ALL");
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM ACT_GIG WHERE gigID = ? AND actID = ?");

            try {
                ps2.setInt(1, gigID);
            } catch (SQLException e) {
                ps2.setNull(1, Types.INTEGER);
            }
            try {
                ps2.setInt(2, actID);
            } catch (SQLException e) {
                ps2.setNull(1, Types.INTEGER);
            }

            //Turn auto commit off before executing the delete
            conn.setAutoCommit(false);
            triggerWorkaround.execute();
            triggerWorkaround = conn.prepareStatement("ALTER TABLE ACT_GIG ENABLE TRIGGER trigger_move_later_acts_back");
            triggerWorkaround.execute();
            ps2.executeUpdate();
            triggerWorkaround = conn.prepareStatement("ALTER TABLE ACT_GIG ENABLE TRIGGER ALL");
            triggerWorkaround.execute();
            conn.commit();
        } catch (SQLException e) {
            //Error here means a problem with the database, rollback if the delete happened
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();

            try{
                conn.rollback();
                conn.setAutoCommit(true);
            } catch(SQLException f){
                System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                f.printStackTrace();
            }
            return new String[0][0];
        }

        //get the gigdatetime for the gig - for business rules which arent SQL triggers
        try {
            PreparedStatement ps3 = conn.prepareStatement("SELECT gigdatetime FROM GIG WHERE gigID = ?");
            LocalDateTime gigStart;

            try {
                ps3.setInt(1, gigID);
            } catch (SQLException e) {
                ps3.setNull(1, Types.INTEGER);
            }

            ResultSet rsGigDateTime = ps3.executeQuery();
            rsGigDateTime.next();
            gigStart = rsGigDateTime.getObject("gigdatetime", LocalDateTime.class);
            
            if (!(verifyStartTime(conn, gigID, gigStart) && verifyEmptyIntervals(conn, gigID) && verifyGigLength(conn, gigID))){
                returnUsers = true;
            }

            conn.commit();
        } catch (SQLException e) {
            //We're still validating the db state so any errors rollback any changes
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException f) {
                System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                f.printStackTrace();
            }
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        }

        // act(s) was/were removed but fixes violated business rules so find tickets of affected customers
        // and change gig status
        if (returnUsers){
            try {
                PreparedStatement ps4 = conn.prepareStatement("SELECT DISTINCT customername, customeremail FROM TICKET WHERE gigID = ?");
                PreparedStatement ps5 = conn.prepareStatement("UPDATE GIG SET gigstatus = 'C' WHERE gigID = ?");

                try {
                    ps4.setInt(1, gigID);
                    ps5.setInt(1, gigID);
                } catch (SQLException e) {
                    ps4.setNull(1, Types.INTEGER);
                    ps5.setNull(1, Types.INTEGER);
                }

                ResultSet customers = ps4.executeQuery();
                ps5.executeUpdate();
                conn.commit();
                conn.setAutoCommit(true);                   //All went well turn auto commit back on
                return convertResultToStrings(customers);
            } catch (SQLException e) {
                //Any error that interrupts the clean up of the delete rollback changes
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException f) {
                    System.err.format("SQL State: %s\n%s\n", f.getSQLState(), f.getMessage());
                    f.printStackTrace();
                }
                System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
                e.printStackTrace();
            }
        }
        //If an act is removed without broken rules return the gig line up via task 1
        return task1(conn, gigID);
    }

    public static String[][] task5(Connection conn){
        try {
            PreparedStatement ps = conn.prepareStatement(   
                "WITH ActGigTotals AS ( "+
                    "SELECT gigID, sum(actgigfee) AS sumFee " +
                    "FROM (SELECT DISTINCT ON (actID, gigID, actgigfee) * FROM ACT_GIG) " +
                    "GROUP BY gigID), " +
                "ticketInfo AS ( " +
                    "SELECT gt.gigID, min(price) as minTicket, count(ticketid) as ticketNum " +
                    "FROM GIG_TICKET gt LEFT OUTER JOIN TICKET t on gt.gigid = t.gigid " +
                    "GROUP BY gt.gigID " +
                    "HAVING min(price) > 0) " +
                "SELECT gigID, (hirecost+sumFee)/minTicket - ticketNum " +
                "FROM ( " +
                    "SELECT gigID, hirecost, sumFee, minTicket, ticketNum " +
                    "FROM GIG NATURAL JOIN VENUE NATURAL JOIN ActGigTotals NATURAL JOIN ticketInfo) " +
                "ORDER BY gigid");
            
            ResultSet rs = ps.executeQuery();
            return convertResultToStrings(rs);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return new String[0][0];
        }
    }

    public static String[][] task6(Connection conn){
        try {
            PreparedStatement ps = conn.prepareStatement(   
                "WITH headActsPerGig AS ( " +
                    "SELECT ag.gigID, actname, extract(year FROM max(ontime)) AS year " +
                    "FROM ACT_GIG ag NATURAL JOIN ACT NATURAL JOIN GIG " +
                    "WHERE gigstatus = 'G' AND ontime = ( " +
                        "SELECT MAX(ontime) " +
                        "FROM ACT_GIG " +
                        "WHERE gigID = ag.gigID " +
                    ") " +
                    "GROUP BY ag.gigid, actname), " +
                "ticketsPerActPerYear AS ( " +
                    "SELECT actname, cast(year AS TEXT), count(*) AS num " +
                    "FROM headActsPerGig NATURAL JOIN TICKET " +
                    "GROUP BY actname, year " +
                "), " +
                "totals AS ( " +
                    "SELECT actname, 'Total' AS year, sum(num) AS num " +
                    "FROM ticketsPerActPerYear " +
                    "GROUP BY actname) " +
                "SELECT tp.actname, tp.year, tp.num " +
                "FROM (SELECT * FROM ticketsPerActPerYear UNION ALL SELECT * FROM totals) tp " +
                    "JOIN totals t ON tp.actname = t.actname " +
                "ORDER BY t.num, tp.year");
            
            ResultSet rs = ps.executeQuery();
            return convertResultToStrings(rs);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return new String[0][0];
        }
    }

    public static String[][] task7(Connection conn){
        try {
            PreparedStatement ps = conn.prepareStatement(   
                "WITH headActs AS ( " +
                    "SELECT actname, extract(year from ontime) AS year " +                                     
                    "FROM ACT_GIG ag NATURAL JOIN ACT NATURAL JOIN GIG " +
                    "WHERE gigstatus = 'G' AND ontime = ( " +
                        "SELECT max(ontime) " +
                        "FROM ACT_GIG " +
                        "WHERE gigID = ag.gigID " +
                    ") " +
                "),  " +
                "actYears AS ( " +
                    "SELECT actname, count(DISTINCT year) AS total_years " +
                    "FROM headActs " +
                    "GROUP BY actname " +
                "), " +
                "customerYears AS ( " +
                    "SELECT customername, actname, count(DISTINCT year) AS attended_years " +
                    "FROM ( " +
                        "SELECT customername, actname, extract(year FROM ontime) AS year " +
                        "FROM TICKET NATURAL JOIN ACT_GIG ag NATURAL JOIN ACT NATURAL JOIN GIG " +
                        "WHERE (actname, extract(year FROM ontime)) IN (SELECT actname, year FROM headActs) AND gigstatus = 'G' AND ontime = ( " +
                            "SELECT max(ontime) " +
                            "FROM ACT_GIG " +
                            "WHERE gigID = ag.gigID " +
                        ")) " +
                    "GROUP BY customername, actname " +
                ") " +
                "SELECT a.actname, coalesce(c.customername, '[None]') AS customername " +
                "FROM actYears a " +
                "LEFT OUTER JOIN customerYears c " +
                    "ON a.actname = c.actname AND c.attended_years = a.total_years");
            
            ResultSet rs = ps.executeQuery();
            return convertResultToStrings(rs);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return new String[0][0];
        }
    }

    public static String[][] task8(Connection conn){
        try {
            PreparedStatement ps = conn.prepareStatement(   
                "WITH avgTicketPrice AS( " +
                    "SELECT avg(cost) " +
                    "FROM TICKET NATURAL JOIN GIG " +
                    "WHERE gigstatus = 'G' " +
                ") " +
                "SELECT venuename, actname, (hirecost+standardfee)/(SELECT avg FROM avgTicketPrice)::INTEGER AS ticks " +
                "FROM (VENUE CROSS JOIN ACT) " +
                "WHERE (hirecost+standardfee)/(SELECT avg FROM avgTicketPrice) <= capacity " +
                "ORDER BY venuename, ticks DESC");
            ResultSet rs = ps.executeQuery();
            return convertResultToStrings(rs);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return new String[0][0];
        }
    }

    //Verifies whether an act at a gig starts at the gig start time using an SQL function
    private static boolean verifyStartTime(Connection conn, int gigID, LocalDateTime gigStart){
        ResultSet rs;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT validate_first_actgig_ontime(?, ?) AS val");
            ps.setInt(1, gigID);
            ps.setObject(2, gigStart);

            rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean("val");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return false;
        }

    } 

    //Iterates through the result set checking if the gaps between acts follows business rule 7 
    private static boolean verifyEmptyIntervals(Connection conn, int gigID){
        ResultSet rs, rsSize;
        int tableSize;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT ontime , ontime+INTERVAL '1 minute' * duration as endtime FROM ACT_GIG WHERE gigid = ? ORDER BY ontime");
            PreparedStatement psSize = conn.prepareStatement("SELECT count(*) AS size FROM ACT_GIG WHERE gigid = ?");
            ps.setInt(1, gigID);
            psSize.setInt(1, gigID);

            rs = ps.executeQuery();
            rsSize = psSize.executeQuery();
            rs.next();
            rsSize.next();
            tableSize = rsSize.getInt("size");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        try{
            for (int i = 0; i<tableSize-1;i++){
                Timestamp endtime = rs.getTimestamp("endtime");
                rs.next();
                long diff = Duration.between(rs.getTimestamp("ontime").toLocalDateTime(), endtime.toLocalDateTime()).toMinutes();

                if (diff != 0 && (diff > -10 || diff < -30)){
                    System.out.println(i + " : " + diff);
                    return false;
                }
            }
        } catch (SQLException e){
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true; //no errors therefore valid gaps or empty table, still return true as no gaps are vacuously true
    }

    //Checks that a chosen gig is over an hour long with a query that returns true or false
    public static boolean verifyGigLength(Connection conn, int gigID){
        ResultSet rs, rsSize;

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT CASE WHEN max_time - min_time > INTERVAL '1 hour' THEN TRUE ELSE FALSE END AS val " +                               
                "FROM (SELECT gigID, MIN(ontime) AS min_time, MAX(ontime + INTERVAL '1 minute' * duration) AS max_time " +
                       "FROM ACT_GIG " +
                       "WHERE gigID = ? " +
                       "GROUP BY gigID)");
            PreparedStatement psSize = conn.prepareStatement("SELECT count(*) FROM ACT_GIG WHERE gigID = ?");
            ps.setInt(1, gigID);
            psSize.setInt(1, gigID);
            
            rsSize = psSize.executeQuery();
            rsSize.next();

            if (rsSize.getInt("count") == 0){
                return false;
            }

            rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean("val");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Prompts the user for input
     * @param prompt Prompt for user input
     * @return the text the user typed
     */
    private static String readEntry(String prompt) {
        
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while(c != '\n' && c != -1) {
                buffer.append((char)c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }

    }
     
    /**
    * Gets the connection to the database using the Postgres driver, connecting via unix sockets
    * @return A JDBC Connection object
    */
    public static Connection getSocketConnection(){
        Properties props = new Properties();
        props.setProperty("socketFactory", "org.newsclub.net.unix.AFUNIXSocketFactory$FactoryArg");
        props.setProperty("socketFactoryArg",System.getenv("HOME") + "/cs258-postgres/postgres/tmp/.s.PGSQL.5432");
        Connection conn;
        try{
          conn = DriverManager.getConnection("jdbc:postgresql://localhost/cwk", props);
          return conn;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the connection to the database using the Postgres driver, connecting via TCP/IP port
     * @return A JDBC Connection object
     */
    public static Connection getPortConnection() {
        
        String user = "postgres";
        String passwrd = "password";
        Connection conn;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException x) {
            System.out.println("Driver could not be loaded");
        }

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/cwk?user="+ user +"&password=" + passwrd);
            return conn;
        } catch(SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            System.out.println("Error retrieving connection");
            return null;
        }
    }

    /**
     * Iterates through a ResultSet and converts to a 2D Array of Strings
     * @param rs JDBC ResultSet
     * @return 2D Array of Strings
     */
     public static String[][] convertResultToStrings(ResultSet rs) {
        List<String[]> output = new ArrayList<>();
        String[][] out = null;
        try {
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                String[] thisRow = new String[columns];
                for (int i = 0; i < columns; i++) {
                    thisRow[i] = rs.getString(i + 1);
                }
                output.add(thisRow);
            }
            out = new String[output.size()][columns];
            for (int i = 0; i < output.size(); i++) {
                out[i] = output.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void printTable(String[][] out){
        int numCols = out[0].length;
        int w = 20;
        int widths[] = new int[numCols];
        for(int i = 0; i < numCols; i++){
            widths[i] = w;
        }
        printTable(out,widths);
    }

    public static void printTable(String[][] out, int[] widths){
        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[i].length; j++){
                System.out.format("%"+widths[j]+"s",out[i][j]);
                if(j < out[i].length - 1){
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }

}
