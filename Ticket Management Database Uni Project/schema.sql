/*Put your CREATE TABLE statements (and any other schema related definitions) here*/
DROP TABLE IF EXISTS VENUE CASCADE;
CREATE TABLE VENUE(
    venueID SERIAL PRIMARY KEY,
    venuename VARCHAR(100) UNIQUE NOT NULL,
    hirecost INTEGER CHECK(hirecost >= 0) NOT NULL,
    capacity INTEGER NOT NULL
);

DROP TABLE IF EXISTS ACT CASCADE;
CREATE TABLE ACT(
    actID SERIAL PRIMARY KEY,
    actname VARCHAR(100) UNIQUE NOT NULL,
    genre VARCHAR(10) NOT NULL,
    standardfee INTEGER CHECK(standardfee >= 0) NOT NULL
);

DROP TABLE IF EXISTS GIG CASCADE;
CREATE TABLE GIG(
    gigID SERIAL UNIQUE,
    venueID INTEGER REFERENCES VENUE(venueID),
    gigtitle VARCHAR(100) NOT NULL,
    gigdatetime TIMESTAMP CHECK (                               -- BUSINESS RULE 12
        EXTRACT(HOUR FROM gigdatetime) >= 9 
        AND EXTRACT(HOUR FROM gigdatetime) < 24),
    gigstatus VARCHAR(1) CHECK(gigstatus IN ('C','G')),
    PRIMARY KEY (gigID, venueID, gigdatetime, gigstatus)        -- BUSINESS RULE 14 - gigs can be at the same time and place if only one is going ahead
);
--Composite Key becuse the combination of the used attributes have to be unique as a whole
--gig status is included as cancelled gigs need to be ignored

DROP TABLE IF EXISTS GIG_TICKET CASCADE;
CREATE TABLE GIG_TICKET(
    gigID SERIAL REFERENCES GIG(gigID),
    pricetype VARCHAR(2),
    price INTEGER CHECK(price >= 0) NOT NULL,
    PRIMARY KEY(gigID, pricetype)
);
--Composite Key because every gig can have multiple ticket types

DROP TABLE IF EXISTS TICKET CASCADE;
CREATE TABLE TICKET(
    ticketID SERIAL,
    gigID INTEGER REFERENCES GIG(gigID) NOT NULL,
    pricetype VARCHAR(2) NOT NULL,
    cost INTEGER CHECK(cost >= 0) NOT NULL,
    customername VARCHAR(100) NOT NULL,
    customeremail VARCHAR(100),
    PRIMARY KEY(ticketID, customeremail)
);

DROP TABLE IF EXISTS ACT_GIG CASCADE;
CREATE TABLE ACT_GIG(
    actID INTEGER REFERENCES ACT(actID),
    gigID INTEGER REFERENCES GIG(gigID) NOT NULL,
    actgigfee INTEGER CHECK(actgigfee >= 0) NOT NULL,
    ontime TIMESTAMP,
    duration INTEGER CHECK(duration >= 15 AND duration <= 90) NOT NULL,  -- BUSINESS RULE 4
    PRIMARY KEY(actID, ontime)                                           -- BUSINESS RULE 2 - acts can't be in two places at the same time so enforce uniqueness between the two
);
--Composite key because uniqueness is required across acts, gigs and times

--=====================================================================================================================
-- Ensure the act_gig.ontime isn't before the planned time, i.e gig.gigdatetime
CREATE OR REPLACE FUNCTION validate_timestamps() RETURNS TRIGGER AS $$
BEGIN

    IF NEW.ontime < (SELECT gigdatetime FROM GIG WHERE gigID = NEW.gigID) THEN
        RAISE EXCEPTION 'GIG_ACT.ontime must be the same or later than GIG.gigdatetime';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_timestamps
BEFORE INSERT OR UPDATE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION validate_timestamps();


--=====================================================================================================================
--Adult tickets can't be free, ticket table
CREATE OR REPLACE FUNCTION validate_adult_ticket() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.pricetype IN ('A') AND NEW.cost = 0  THEN
        RAISE EXCEPTION 'TICKET.cost cannot be 0 for an adult ticket';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_adult_ticket
BEFORE INSERT OR UPDATE ON TICKET
FOR EACH ROW
EXECUTE FUNCTION validate_adult_ticket();


--=====================================================================================================================
--Adult tickets can't be free, gig_ticket table
CREATE OR REPLACE FUNCTION validate_adult_gig_ticket() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.pricetype IN ('A') AND NEW.price = 0  THEN
        RAISE EXCEPTION 'GIG_TICKET.price cannot be 0 for an adult ticket';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_adult_gig_ticket
BEFORE INSERT OR UPDATE ON GIG_TICKET
FOR EACH ROW
EXECUTE FUNCTION validate_adult_gig_ticket();


--=====================================================================================================================
--Check for inappropriate overlap between acts at the same gig - BUSINESS RULE 1
CREATE OR REPLACE FUNCTION validate_no_overlap() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM ACT_GIG
        WHERE 
            (
                ((NEW.ontime < ontime + INTERVAL '1 minute' * duration) AND                                         --new start time isn't during a previous gig
                 (NEW.ontime >= ontime)) OR                                                                          
                ((NEW.ontime + INTERVAL '1 minute' * NEW.duration > ontime) AND                                     --new end time isn't during a later gig
                 (NEW.ontime + INTERVAL '1 minute' * NEW.duration <= ontime + INTERVAL '1 minute' * duration))      --an act doesn't wrap around another act
                ) AND
            NEW.gigID = gigID                                                                                       --Only matters at the same gig
    )
    THEN 
        RAISE EXCEPTION 'acts cannot overlap at the same gig';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_no_overlap
BEFORE INSERT OR UPDATE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION validate_no_overlap();


--=====================================================================================================================
--Check for 60 minute break for acts between different gigs - BUSINESS RULE 5
CREATE OR REPLACE FUNCTION validate_act_break_between_gigs() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM ACT_GIG
        WHERE
            (   --Check either start or end is during another act break or an act wraps around another
                (NEW.ontime + (INTERVAL '1 minute' * NEW.duration) + (INTERVAL '1 minute' * 60) > ontime AND
                 NEW.ontime + (INTERVAL '1 minute' * NEW.duration) + (INTERVAL '1 minute' * 60) <= ontime + (INTERVAL '1 minute' * duration) + (INTERVAL '1 minute' * 60)) OR    --new end time + 60 > existing start time or
                (NEW.ontime < ontime + (INTERVAL '1 minute' * NEW.duration) + (INTERVAL '1 minute' * 60) AND
                 NEW.ontime >= ontime) OR      
                (NEW.ontime < ontime AND NEW.ontime + (INTERVAL '1 minute' * NEW.duration) > ontime + (INTERVAL '1 minute' * duration))
            )   AND    
            (NEW.actID = actID) AND
            (NEW.gigID <> gigID)                                                                               
    )                                                                             
    THEN 
        RAISE EXCEPTION 'acts cannot be within 60 minutes of each other at different gigs';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_act_break_between_gigs
BEFORE INSERT OR UPDATE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION validate_act_break_between_gigs();


--=====================================================================================================================
--Check for 180 minute break for venues between different gigs - BUSINESS RULE 6
CREATE OR REPLACE FUNCTION validate_venue_break_between_gigs() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS(
        SELECT 1 
        FROM (SELECT venueID, GIG.gigID, gigdatetime, max(ontime + INTERVAL '1 minute' * duration) AS endtime 
             FROM GIG JOIN ACT_GIG ON GIG.gigID = ACT_GIG.gigID
             GROUP BY venueID, gigdatetime, GIG.gigID
             ORDER BY venueID, gigdatetime, GIG.gigID)                                          --returns the venue ID, and end time of final act of every gig
        WHERE
            (   
                (NEW.gigdatetime < endtime + (INTERVAL '1 minute' * 180) AND                                      --new.start during different gig + break
                 NEW.gigdatetime >= gigdatetime) OR                        
                (NEW.gigdatetime + (INTERVAL '1 minute' * (180+60)) > gigdatetime AND                             --new.end during different gig + break
                 NEW.gigdatetime + (INTERVAL '1 minute' * (180+60)) <= endtime) OR          
                (NEW.gigdatetime < gigdatetime AND NEW.gigdatetime + (INTERVAL '1 minute' * 180) > endtime)      --new.start before a gig and new.end after that gig    
            ) AND                                                                                   
            NEW.venueID = venueID AND                                                                                --at same venue      
            NEW.gigID <> gigID                                              
    )
    THEN 
        RAISE EXCEPTION 'gigs cannot be within 180 minutes of each other at the same venue';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_venue_break_between_gigs
BEFORE INSERT OR UPDATE ON GIG
FOR EACH ROW
EXECUTE FUNCTION validate_venue_break_between_gigs();


--=====================================================================================================================
--Reject tickets for gigs when capacity has been reached - BUSINESS RULE 9
CREATE OR REPLACE FUNCTION validate_tickets_venue_capacity() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        WITH GIG_TOTALS AS (
            SELECT *
            FROM (SELECT gigID, count(*) AS total FROM TICKET GROUP BY gigID)
            NATURAL JOIN GIG
        )
        SELECT 1
        FROM GIG_TOTALS g JOIN VENUE v ON g.venueID = v.venueID
        WHERE 
            NEW.gigID = g.gigID AND
            g.venueID = v.venueID AND
            g.total = v.capacity
    )
    THEN
        RAISE EXCEPTION 'Ticket number per gig cannot exceed venue capacity';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_tickets_venue_capacity
BEFORE INSERT OR UPDATE ON TICKET
FOR EACH ROW
EXECUTE FUNCTION validate_tickets_venue_capacity();


--=====================================================================================================================
--Ensure acts finish at the right time - BUSINESS RULE 11
CREATE OR REPLACE FUNCTION validate_act_finish_time() RETURNS TRIGGER AS $$
BEGIN
    CASE 
        WHEN 'Rock' in (SELECT genre FROM ACT WHERE NEW.actID = ACT.actID) THEN 
            IF (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::TIME > TIME '23:00:00' OR (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::DATE > NEW.ontime::DATE THEN
                RAISE EXCEPTION 'Rock acts cannot play past 11pm';
            END IF;
        WHEN 'Pop' in (SELECT genre FROM ACT WHERE NEW.actID = ACT.actID) THEN 
            IF (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::TIME > TIME '23:00:00' OR (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::DATE > NEW.ontime::DATE THEN
                RAISE EXCEPTION 'Pop acts cannot play past 11pm';
            END IF;
        ELSE
            IF (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::TIME > TIME '01:00:00' AND (NEW.ontime + INTERVAL '1 minute' * NEW.duration)::DATE > NEW.ontime::DATE THEN
                RAISE EXCEPTION 'No act can play past 1am the next day';
            END IF;
    END CASE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_act_finish_time
BEFORE INSERT OR UPDATE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION validate_act_finish_time();


--=====================================================================================================================
--Ensure rules for acts performing twice at the same gig - BUSINESS RULE 3
CREATE OR REPLACE FUNCTION validate_same_act_per_gig() RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS(
    SELECT 1
    FROM (SELECT * FROM ACT_GIG WHERE NEW.gigID = ACT_GIG.gigID AND NEW.actID = ACT_GIG.actID)
    WHERE
        (NEW.ontime < ontime + INTERVAL '1 minute' * (duration+30) AND
         NEW.ontime <= ontime) OR                                                                                   --new start time is within 30 mins of an existing end time 
        (NEW.ontime + INTERVAL '1 minute' * (NEW.duration+30) > ontime AND                                          
         NEW.ontime + INTERVAL '1 minute' * (NEW.duration+30) <= ontime + INTERVAL '1 minute' * duration) AND       --new end time is within 30 mins of existing start time
        (NEW.ontime < ontime AND NEW.ontime + INTERVAL '1 minute' * NEW.duration > ontime + INTERVAL '1 minute' * duration)
    ) 
    THEN
        RAISE EXCEPTION 'An act cannot perform again within 30 mins';
    ELSIF EXISTS (SELECT * FROM ACT_GIG WHERE NEW.gigID = gigID AND NEW.actID = actID AND NEW.actgigfee <> actgigfee) THEN
        RAISE EXCEPTION 'Non first appearance of an act at ACT_GIG gig needs its fee to be the same as the first';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_same_act_per_gig
BEFORE INSERT OR UPDATE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION validate_same_act_per_gig();

--=====================================================================================================================
--Check if the earliest act_GIG ontime attribute is the corresponding gigs gigdatetime
CREATE OR REPLACE FUNCTION validate_first_actgig_ontime(inGigID INT, inGigDateTime TIMESTAMP WITHOUT TIME ZONE) RETURNS BOOLEAN AS $$
BEGIN
    IF inGigDateTime IN (
        SELECT ontime
        FROM ACT_GIG
        WHERE gigID = inGigID
    )
    THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;

--=====================================================================================================================
--Check that the gig lasts at least 60 minutes
CREATE OR REPLACE FUNCTION validate_60_mins_gig(inGigID INT) RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS(
        SELECT ontime + INTERVAL '1 minute' * duration AS endtime
        FROM ACT_GIG NATURAL JOIN GIG
        WHERE gigID = inGigID AND (ontime + INTERVAL '1 minute' * duration) >= gigdatetime + INTERVAL '1 hour');

END;
$$ LANGUAGE plpgsql;

--=====================================================================================================================
--Move all later acts back by duration
CREATE OR REPLACE FUNCTION move_later_acts_back() RETURNS TRIGGER AS $$
BEGIN

    UPDATE ACT_GIG SET ontime = ontime - (INTERVAL '1 minute' * OLD.duration) WHERE ontime > OLD.ontime AND gigID = OLD.gigID;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_move_later_acts_back
AFTER DELETE ON ACT_GIG
FOR EACH ROW
EXECUTE FUNCTION move_later_acts_back();

--=====================================================================================================================
--Move all later acts back by duration
CREATE OR REPLACE FUNCTION nullify_cancelled_gig_tickets() RETURNS TRIGGER AS $$
BEGIN
    UPDATE TICKET SET cost = 0
    WHERE (gigID, 'C') in (SELECT gigID, gigstatus FROM GIG);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_nullify_cancelled_gig_tickets
AFTER UPDATE ON GIG
FOR EACH ROW
EXECUTE FUNCTION nullify_cancelled_gig_tickets();
