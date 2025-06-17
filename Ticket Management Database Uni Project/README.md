# Github README update
SQL project for a databases university module, 4 database alterations and 4 queries done via JDBC. All SQL written by me but my Java code functions named tasks 1 through 8 the rest was a provided schedule. Heavy use of persistent stored modules as minimal java was required.

This was the original report file, below there is an explanation of the code across schema.sql and GigSystem.java. This project was written November/December 2024 and i'm writing this update June 2025, i don not remember what the first 4 tasks were exactly.
# Task Implementations

<!-- For each of the tasks below, please write around 100-200 words explaining how your solution works (describing the behaviour of your SQL statements/queries) -->
## Business Rules
    With the exceptions of business rules 7, 8, 10 and 13, the rest are implemented through triggers. If the all business rules are followed the triggers don't do anything but if a rule is violated an exception is raised which then is caught be a try catch statement and handled accordingly. Business rule 13 is handled by the use of integer data type when handling duration and assuming the seconds part of timestamps is ignored.

    7, 8 and 10 are checked through Java and differing uses of SQL. For 7 JDBC makes a call to the database and uses a bespoke SQL function to ensure the table returns one entry, either true or false. That value is simply returned in the bespoke Java verifyStartTime function. For 10 it is a similar concept but the truth value is derived in the select clause. The where clause ensures up to one value and thus if it exists it is returned by the bespoke Java verifyGigLength function.
    Finally for rule 8, JDBC requests a table of act start times and end times for the gig and it is ordered in terms of start time. Then the gaps or breaks are calculated procedurally using the "Duration.between" function. If there are no violating gaps the bespoke function "verifyEmptyIntervals" returns true and false otherwise

## Task 1
    REgular SQL query on natural joing of ACT_GIG and ACT to have access to the necessary fields, namely actname, ontime and duration. Correct formatting is handled completely by the "to_char" function.
## Task 2
    Task 2 is broken down in to the following steps:
        1. find venueid from venuename
        2. Turn auto commit off
        3. create gig entry
        4. create gig_ticket entry
        5. create act_gig entries
        6. verify outstanding business rules
        7. Commit successful new gig and reenable auto commit

    Steps 1 through 5 just use SQL to find the necessary data and adding the entries with appropriate use of "?" and setting it via JDBC. Correct triggers take place when certain commands are called on the tables and if any illegal states of genuine errors take place an exception is thrown. When an exception is thrown all changes are rolled back and auto commit is turned back on. The outstanding business rules refer to the ones that aren't handled solely by SQL and the database. If they are also satisfied the changed are committed and auto commit is re-enabled   
## Task 3
    Task 3 is broken down in to the following steps:
        1. Find the ticket type
        2. If exists add the ticket

    This task doesn't bother with auto commit as there is only one change taking place so if it gets rejected, the database is still in a consistent state. The ticket type is found by getting all ticket types for the gig from the database and iteratively checking if the necessary ticket type is in there. Should have been done by expanding the query to just return the tuple with the expected ticket type but this was a very simple section. 
    
    If all works then the ticket is added to the ticket table and a trigger checks if the number of tickets for that gig doesn't exceed venue capacity
## Task 4
    Task 4 is broken down in to the following steps:
        1. Finding the actid for the given act
        2. Checking if we are removing a headline act
        3. Set auto commit off
        3. Removing the act appearances
            3.1. moving later acts back 
        4. Verifying outstanding business rules
        5. Commiting or rolling back
        6. Returning either new line-up or affected customers 

    Steps 2,3 and 4 have the possibility of the gig being cancelled and the output being affected customers as opposed to the new line-up. The "returnUsers" boolean variable keeps track of if at any point that is the case due to a business rule violation or the headline act being removed. 
    Step 2 uses a nested query that returns the last act of a gig and if that has the same actid as the one in step 1, "returnUsers" is turned true. Step 3 is a simple delete statement with the actid and gig id but more interestingly the moving back of later acts is done by a trigger. This caused an unexpected issue that other triggers were called when the later acts were being updated and causing exceptions. That was due to intermediate states that did violate the business rule and so all other triggers other than moving the acts had to disables for a short while. Surprisingly the business rules trigger did not need to be triggered as they focus on relations between tuples and if there weren't errors before there won't be any after a deletion. Then the non-trigger rules have to be verified same as in task 2 but the gigdatetime field of the gig being altered is necessary for the "verifyStartTime" function. At the end either the new line up is returned using "task1" or the affected users which is a simple query
    If there are exceptions when querying for data changes are rolled back, return nothing and assume nothing happened. If an exception happens during deletes or updates we assume it exception that was raised by the business rules and gigs are cancelled. Obviously these assumptions aren't ideal but it was decided to avoid exhaustively handling all cases of errors due to time. 
## Task 5
    Uses two commontable expressions:
        1. actGigTotals - for the sum of all act costs per gig
        2. ticketInfo - for the cheapest ticket type that wasn't 0 and tickets already sold per gig

    The actual query gets pulls from a table that finds the venueID per gig and the attributes in the CTEs. Then is just calculates the number to cover act and venue fees minus the tickets already sold. To avoid paying the same act twice "DISTINCT ON" is used un the ActGigTotals CTE. 
## Task 6
    Uses three common table expression:
        1. headActsPerGig - returns a table with the headline acts for every gig and what year it was
        2. ticketsPerActPerYear - select explanatory, how many tickets an act sold across its gigs per year
        3. totals - sums the tickets that each act sold

        The actual query sets up table that is the union of ticketsPerActPerYear and totals joins that with totals using actname. from that the actname, the year (which includes the "Total" by making the column text) and the number, then it is ordered by the total number of tickets sold per act and then the year value in string representation.
## Task 7
    Uses three common table expression:
        1.headActs - returns years when an act headlined a gig
        2.customeryears - counts the number of different years that an act headlined
            note : those two could be combined into one CTE
        3.Selects customers, headline act they visited and over how many years. Uses in a inner select statement which finds which customer went to which headline acts and what year

        The actual query does a left outer join on actYears and customerYears in the order to make sure all acts even ones with without viewers are selected and from that actname and the customer are selected, if there is no customer through the use of coalesce we display '[None]'   
## Task 8
    Uses one common table expression
        1. avgTicketPrice - finds the average ticket price of gigs that are going ahead 

    The actual query  the venuename, actname and the venue and act costs divided by the average ticket price. The costs are obtained by a cross join of venue and act tables.  