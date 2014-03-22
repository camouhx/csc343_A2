DROP TABLE country CASCADE;
DROP TABLE club CASCADE;
DROP TABLE player CASCADE;
DROP TABLE stadium CASCADE;
DROP TABLE match CASCADE;
DROP TABLE ticket CASCADE;
DROP TABLE competes CASCADE;
DROP TABLE appearance CASCADE;

-- The country table contains all the national teams participating in World Cup 2014 along with their coaches.
-- 'cid' is the id of the national team.
-- 'coach' is the name of the coach.
CREATE TABLE country (
    cid INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    coach VARCHAR(20) NOT NULL);
    
-- The club table contains information about the clubs into which players participating in World Cup 2014, played during season 2013-2014.
-- 'fcid' is the id of the football club.
-- 'name' is the name of the football club.
-- 'coach' is the name of the coach of the football club.
CREATE TABLE club (
    fcid INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    coach VARCHAR(20) NOT NULL);

-- The player table contains all the football players participating in World Cup 2014.
-- 'position' can take the following values: ST, CF, LF, RF, CM, LM, RM, LCM, RCM, LWM, RWM, CB, LB, RB, LCB, RCB, LWB, RWB, GK.
-- 'goals' denotes the number of goals that the player has scored (>= 0).
-- 'value' is the monetary value of the player in millions dollars (> 0).
CREATE TABLE player (
    pid INTEGER PRIMARY KEY,
    fname VARCHAR(20) NOT NULL,
    lname VARCHAR(20) NOT NULL,
    position VARCHAR(3) NOT NULL,
    goals INTEGER NOT NULL,
    value INTEGER NOT NULL,
    cid INTEGER REFERENCES country(cid) ON DELETE RESTRICT,
    fcid INTEGER REFERENCES club(fcid) ON DELETE RESTRICT);

-- The stadium table provides information about the stadiums of the country where the World Cup 2014 takes place (i.e. Brazil).
-- 'sid' is the id of the stadium.
-- 'capacity' denotes the maximum number of fans that can attend a match held in the particular stadium.
-- 'city' denotes the name of the city where the stadium is located.
CREATE TABLE stadium (
    sid INTEGER PRIMARY KEY,
    capacity INTEGER NOT NULL,
    city VARCHAR(20) NOT NULL);
    
-- The match table contains information about the matches being held. 
-- 'mid' is the id of current match. 
-- 'date' attribute is of the form dd/mm, where (00 <= dd <= 30) and (06 <= mm <= 07).
-- 'time' attribute is of the form hh:mm, where (00 <= hh <= 23) and (00 <= mm <= 59).
CREATE TABLE match (
    mid INTEGER PRIMARY KEY,
    date CHAR(5) NOT NULL,
    time CHAR(5) NOT NULL,
    sid INTEGER REFERENCES stadium(sid) ON DELETE RESTRICT);
    
-- The ticket table contains information about tickets being purchased by fans.
-- 'tid' referes to the ticket id.
-- 'dateIssued' is the date which the ticket was purchased (has the same format as 'date' attribute from 'match' table).
-- 'timeIssued' is the time which the ticket was purchased (has the same format as 'time' attribute from 'match' table).
CREATE TABLE ticket (
    tid INTEGER PRIMARY KEY,
    dateIssued char(5) NOT NULL, 
    timeIssued char(5) NOT NULL,
    mid INTEGER REFERENCES match(mid) ON DELETE RESTRICT);
    
-- The competes table contains information that represents "who plays against whom" in a match.
-- 'country1' refers to the cid of the country that plays as a host in the current match.
-- 'country2' refers to the cid of the country that plays as a visitor in the current match.
-- 'goals1' refers to the number of goals that 'country1' scored (>= 0).
-- 'goals2' refers to the number of goals that 'country2' scored (>= 0).
CREATE TABLE competes (
    mid INTEGER references match(mid) ON DELETE RESTRICT,
    country1 INTEGER REFERENCES country(cid) ON DELETE RESTRICT,
    country2 INTEGER REFERENCES country(cid) ON DELETE RESTRICT, 
    goals1 INTEGER NOT NULL,
    goals2 INTEGER NOT NULL);
    
-- The appearance table keeps track of the number of minutes that each player played in a match in which he appeared (he actually was
--    used by his country's coach).
-- 'minutes' attribute is an integer value belonging in the range (0, 90]
CREATE TABLE appearance(
    mid INTEGER REFERENCES match(mid) ON DELETE RESTRICT,
    pid INTEGER REFERENCES player(pid) ON DELETE RESTRICT,
    minutes INTEGER NOT NULL);
