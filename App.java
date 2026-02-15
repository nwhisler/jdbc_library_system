import java.io.File;
import java.sql.*;
import java.util.*;
import java.lang.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class App {

      public static void main(String args[]){
    
        String member;
        String ISBN;
        String libraryNode;
        String checkedInValue;
        String checkedOutValue;
        boolean contains;
        boolean checkedIn;
        boolean checkedOut;
        Connection con = null;
        ArrayList<String> books = new ArrayList<>();
        HashMap<String, String> booksCheckedOut = new HashMap<>();
    
        try {
          Statement stmt;
          ResultSet rs;
    
          // Register the JDBC driver for MySQL.
          Class.forName("com.mysql.cj.jdbc.Driver");
    
          // Define URL of database server for
          // database named 'user' on the faure.
          String url =
                "url";
    
          // Get a connection to the database for a
          // user named 'user' with the password
          // 123456789.
          con = DriverManager.getConnection(
                            url,"username", "id_number");
    
          // Display URL and connection information
          System.out.println("URL: " + url);
          System.out.println("Connection: " + con);
          System.out.println();
    
          // Get a Statement object
          stmt = con.createStatement();

        try{
            String dropTables = "DROP TABLE IF EXISTS locatedAt," 
                    + "library,"
                    + "borrowedBy,"
                    + "member,"
                    + "publishedBy,"
                    + "writtenBy,"
                    + "book," 
                    + "publisherHas,"
                    + "publisher," 
                    + "authorHas,"
                    + "author,"
                    + "phone;";
            stmt.executeUpdate(dropTables);
        }catch(Exception e){
            System.out.print(e);
        }//end catch  
        
        try {
            String phoneTable = "CREATE TABLE phone ("

            +   "PNumber         VARCHAR(12)         NOT NULL,"
            +   "NumberType      ENUM('o','h','c')   NOT NULL,"
            +   "PRIMARY KEY (PNumber)"     
            
            + ");";  
            stmt.executeUpdate(phoneTable);          
        }
        catch(Exception e) {
            System.out.print(e); 
        }
        try {
            String authorTable = " CREATE TABLE author ("

            +   "authorID        INT                 NOT NULL,"
            +   "first_name      VARCHAR(14)         NOT NULL,"
            +   "last_name       VARCHAR(16)         NOT NULL,"
            +   "PRIMARY KEY (authorID)"
            +   ");";
            stmt.executeUpdate(authorTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String authorHasTable = " CREATE TABLE authorHas ("

            +   "authorID        INT                 NOT NULL,"
            +   "PNumber         VARCHAR(12)         NOT NULL,"
            +   "FOREIGN KEY (authorID)        REFERENCES author (authorID)          ON DELETE CASCADE,"
            +   "FOREIGN KEY (PNumber)         REFERENCES phone (PNumber)            ON DELETE CASCADE"
            
            
            + ");";
            stmt.executeUpdate(authorHasTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publisherTable = " CREATE TABLE publisher ("
 
            +  "publisherID     INT                 NOT NULL,"
            +  "publisherName   VARCHAR(25)         NOT NULL,"
            +  "PRIMARY KEY (publisherID)"
            
            +  ");";
            stmt.executeUpdate(publisherTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publisherHasTable = " CREATE TABLE publisherHas ("
 
            +     "publisherID        INT              NOT NULL,"
            +     "PNumber         VARCHAR(12)         NOT NULL,"
            +     "FOREIGN KEY (publisherID)     REFERENCES publisher (publisherID)    ON DELETE CASCADE,"
            +     "FOREIGN KEY (PNumber)         REFERENCES phone (PNumber)            ON DELETE CASCADE"
             
            + ");";
            stmt.executeUpdate(publisherHasTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String bookTable = " CREATE TABLE book ("

            +    "ISBN            VARCHAR(16)         NOT NULL,"
            +    "title           VARCHAR(35)         NOT NULL,"
            +    "yearPublished   INT                 NOT NULL,"
            +    "PRIMARY KEY (ISBN)"
            
            + ");";
            stmt.executeUpdate(bookTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String writtenByTable = " CREATE TABLE writtenBy ("

            +    "ISBN            VARCHAR(16)         NOT NULL,"
            +    "authorID        INT                 NOT NULL,"
            +    "FOREIGN KEY (ISBN)            REFERENCES book (ISBN)                ON DELETE CASCADE,"
            +    "FOREIGN KEY (authorID)        REFERENCES author (authorID)          ON DELETE CASCADE"
            
            + ");";
            stmt.executeUpdate(writtenByTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publishedByTable = " CREATE TABLE publishedBy ("

            +    "ISBN            VARCHAR(16)         NOT NULL,"
            +    "publisherID        INT              NOT NULL,"
            +    "FOREIGN KEY (ISBN)            REFERENCES book (ISBN)                ON DELETE CASCADE,"
            +    "FOREIGN KEY (publisherID)     REFERENCES publisher (publisherID)    ON DELETE CASCADE"
            
            + ");";
            stmt.executeUpdate(publishedByTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String memberTable = " CREATE TABLE member ("

            +    "memberID        INT                 NOT NULL,"
            +    "first_name      VARCHAR(14)         NOT NULL,"
            +    "last_name       VARCHAR(16)         NOT NULL,"
            +    "DOB             VARCHAR(10)         NOT NULL,"
            +    "PRIMARY KEY (memberID)"       
            
            + ");";
            stmt.executeUpdate(memberTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String borrowedByTable = " CREATE TABLE borrowedBy ("

            +    "memberID        INT                 NOT NULL,"
            +    "ISBN            VARCHAR(16)         NOT NULL,"
            +    "checkoutDate    VARCHAR(10),"         
            +    "checkinDate     VARCHAR(10),"         
            +    "FOREIGN KEY (memberID)        REFERENCES member (memberID)          ON DELETE CASCADE,"
            +    "FOREIGN KEY (ISBN)            REFERENCES book (ISBN)                ON DELETE CASCADE"      
            
            + ");";
            stmt.executeUpdate(borrowedByTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String libraryTable = " CREATE TABLE library ("

            +    "name            varchar(16)         NOT NULL,"
            +    "street          varchar(25)         NOT NULL,"
            +    "city            varchar(25)         NOT NULL,"
            +    "state           varchar(5)          NOT NULL,"
            +    "PRIMARY KEY (name)"
            
            + ");";
            stmt.executeUpdate(libraryTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String locatedAtTable = "CREATE TABLE locatedAt ("

            +    "name            varchar(16)         NOT NULL,"
            +    "ISBN            varchar(16)         NOT NULL,"
            +    "numberOfCopies  INT                 NOT NULL,"
            +    "shelf           INT                 NOT NULL,"
            +    "floor           INT                 NOT NULL,"
            +    "availableCopies INT                 NOT NULL,"
            +    "FOREIGN KEY (name)            REFERENCES library (name)             ON DELETE CASCADE,"
            +    "FOREIGN KEY (ISBN)            REFERENCES book (ISBN)                ON DELETE CASCADE"   
            
            + ");";
            stmt.executeUpdate(locatedAtTable);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String phoneValues = "INSERT INTO `phone` VALUES"
            +"('970-555-1000', 'c'),"
            +"('970-555-1010', 'h'),"
            +"('970-555-1100', 'o'),"
            +"('303-555-1200', 'h'),"
            +"('303-555-1210', 'c'),"
            +"('970-555-1400', 'c'),"
            +"('970-555-1600', 'h'),"
            +"('970-555-1800', 'c'),"
            +"('970-555-1900', 'h'),"
            +"('970-555-2000', 'h'),"
            +"('970-555-2010', 'c'),"
            +"('970-555-2001', 'h'),"
            +"('970-555-2020', 'c'),"
            +"('970-555-2300', 'o'),"
            +"('970-555-2401', 'c'),"
            +"('970-555-2400', 'h'),"
            +"('970-555-2402', 'c'),"
            +"('970-555-2403', 'o'),"
            +"('970-555-5000', 'o'),"
            +"('970-555-5010', 'o'),"
            +"('970-555-5020', 'o'),"
            +"('970-555-5030', 'o'),"
            +"('970-555-5040', 'o'),"
            +"('970-555-5050', 'o'),"
            +"('970-555-5060', 'o'),"
            +"('970-555-5070', 'c'),"
            +"('970-555-5080', 'o');";
            stmt.executeUpdate(phoneValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String authorValues = "INSERT INTO `author` VALUES"
            +"(101,'Bobby','Ewing'),"     
            +"(102,'Red','Skelton'),"
            +"(201,'Thomas','Magnum'),"
            +"(202,'Julie','Barnes'),"
            +"(203,'Roger','Ramjet'),"
            +"(204,'Mickey','Hart'),"
            +"(205,'Grace','Slick'),"
            +"(206,'Perry','Mason'),"
            +"(207,'Dickey','Betts'),"
            +"(208,'Waco','Kid'),"
            +"(209,'Roger','Thornhill'),"
            +"(210,'Scottie','Ferguson'),"
            +"(301,'Barbara','Wright'),"
            +"(302,'John','Crichton'),"
            +"(303,'Aeon','Flux'),"
            +"(304,'Robert','Crawley');";
            stmt.executeUpdate(authorValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String authorHasValues = "INSERT INTO `authorHas` VALUES" 
            +"(101, '970-555-1000'),"
            +"(101, '970-555-1010'),"
            +"(201, '303-555-1200'),"
            +"(202, '303-555-1200'),"
            +"(202, '303-555-1210'),"
            +"(203, '970-555-1400'),"
            +"(205, '970-555-1600'),"
            +"(206, '970-555-1600'),"
            +"(207, '970-555-1800'),"
            +"(208, '970-555-1900'),"
            +"(209, '970-555-2000'),"
            +"(209, '970-555-2010'),"
            +"(210, '970-555-2001'),"
            +"(301, '970-555-2020'),"
            +"(302, '970-555-2300'),"
            +"(303, '970-555-2400'),"
            +"(303, '970-555-2401'),"
            +"(303, '970-555-2403'),"
            +"(304, '970-555-2400'),"
            +"(304, '970-555-2402'),"
            +"(304, '970-555-2403');";
            stmt.executeUpdate(authorHasValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publisherValues = "INSERT INTO `publisher` VALUES"
            +"(10000, 'Wiley'),"
            +"(10001, 'McGraw-Hill'),"
            +"(10002, 'Coyote Publishing'),"
            +"(10003, 'Amazon'),"
            +"(10004, 'Jerrys Ice Cream'),"
            +"(10005, 'Buy a Boat'),"
            +"(10006, 'Flagstaff Publishing'),"
            +"(10007, 'Ram West');";
            stmt.executeUpdate(publisherValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publisherHasValues = "INSERT INTO `publisherHas` VALUES" 
            +"(10000, '970-555-5000'),"
            +"(10001, '970-555-5010'),"
            +"(10002, '970-555-5020'),"
            +"(10003, '970-555-5030'),"
            +"(10004, '970-555-5040'),"
            +"(10005, '970-555-5050'),"
            +"(10006, '970-555-5060'),"
            +"(10006, '970-555-5070'),"
            +"(10007, '970-555-5080');";
            stmt.executeUpdate(publisherHasValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String bookValues = "INSERT INTO `book` VALUES" 
            +"('96-42103-10001', 'How to Grow Tomatoes', 1963),"
            +"('96-42103-10002', 'Mr. Smith Goes to Washington', 2010),"
            +"('96-42103-10003', 'Studying is Your Friend', 1955),"
            +"('96-42103-10004', 'To Have and To Cherish', 2011),"
            +"('96-42103-10005', 'Hal Finds a Home', 2001),"
            +"('96-42103-10006', 'Last Train to Clarksville', 1999),"
            +"('96-42103-10007', 'How to Digitally Sign', 2003),"
            +"('96-42103-10008', 'Sam Needs a Friend', 2013),"
            +"('96-42103-10009', 'Downton Abbey', 2005),"
            +"('96-42103-10011', 'Database Theory', 2010),"
            +"('96-42103-10022', 'Challenging Puzzles', 1988),"
            +"('96-42103-10033', 'American Football', 2011),"
            +"('96-42103-10034', 'European Football', 2015),"
            +"('96-42103-10040', 'Where to Start', 2012),"
            +"('96-42103-10054', 'Lacey Discovers Herself', 2013),"
            +"('96-42103-10068', 'Mr. Ed', 2009),"
            +"('96-42103-10081', 'Escape from Gilligans Island', 2009),"
            +"('96-42103-10093', 'Fixing Computers', 2010),"
            +"('96-42103-10109', 'Red Burn', 2011),"
            +"('96-42103-10206', 'Taks McGrill', 2000),"
            +"('96-42103-10300', 'Eating Healthy', 1999),"
            +"('96-42103-10401', 'How to Grow Cucumbers', 1945),"
            +"('96-42103-10502', 'Gardening Tips', 1973),"
            +"('96-42103-10604', 'Using the Library', 1993),"
            +"('96-42103-10709', 'Too Many Cooks', 1983),"
            +"('96-42103-10800', 'Green is Your Friend', 2000),"
            +"('96-42103-10907', 'Cubs Win!', 2005),"
            +"('96-42103-11003', 'Missing Tomorrow', 2005),"
            +"('96-42103-11604', 'Eating in the Fort', 1993),"
            +"('96-42103-11709', 'Green Eggs', 1983),"
            +"('96-42103-11800', 'Blue is Your Friend', 2010);";
            stmt.executeUpdate(bookValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String writtenByValues = "INSERT INTO `writtenBy` VALUES"
            +"('96-42103-10001', 101),"
            +"('96-42103-10001', 208),"
            +"('96-42103-10002', 102),"
            +"('96-42103-10003', 203),"
            +"('96-42103-10003', 201),"
            +"('96-42103-10004', 209),"
            +"('96-42103-10004', 302),"
            +"('96-42103-10004', 304),"
            +"('96-42103-10005', 204),"
            +"('96-42103-10006', 101),"
            +"('96-42103-10007', 102),"
            +"('96-42103-10007', 302),"
            +"('96-42103-10008', 209),"
            +"('96-42103-10008', 210),"
            +"('96-42103-10008', 301),"
            +"('96-42103-10009', 304),"
            +"('96-42103-10011', 207),"
            +"('96-42103-10011', 208),"
            +"('96-42103-10022', 203),"
            +"('96-42103-10033', 202),"
            +"('96-42103-10034', 202),"
            +"('96-42103-10040', 205),"
            +"('96-42103-10040', 203),"
            +"('96-42103-10054', 201),"
            +"('96-42103-10054', 202),"
            +"('96-42103-10068', 210),"
            +"('96-42103-10081', 101),"
            +"('96-42103-10081', 204),"
            +"('96-42103-10093', 102),"
            +"('96-42103-10109', 304),"
            +"('96-42103-10109', 208),"
            +"('96-42103-10206', 210),"
            +"('96-42103-10206', 301),"
            +"('96-42103-10300', 207),"
            +"('96-42103-10300', 208),"
            +"('96-42103-10401', 101),"
            +"('96-42103-10401', 203),"
            +"('96-42103-10502', 201),"
            +"('96-42103-10502', 202),"
            +"('96-42103-10604', 201),"
            +"('96-42103-10709', 303),"
            +"('96-42103-10709', 201),"
            +"('96-42103-10800', 301),"
            +"('96-42103-10800', 302),"
            +"('96-42103-10907', 102),"
            +"('96-42103-10907', 301),"
            +"('96-42103-11003', 204),"
            +"('96-42103-11003', 205),"
            +"('96-42103-11003', 206),"
            +"('96-42103-11003', 207);";
            stmt.executeUpdate(writtenByValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String publishedByValues = "INSERT INTO `publishedBy` VALUES"
            +"('96-42103-10001', 10006),"
            +"('96-42103-10002', 10007),"
            +"('96-42103-10003', 10000),"
            +"('96-42103-10004', 10003),"
            +"('96-42103-10005', 10003),"
            +"('96-42103-10006', 10004),"
            +"('96-42103-10007', 10004),"
            +"('96-42103-10008', 10005),"
            +"('96-42103-10009', 10002),"
            +"('96-42103-10011', 10001),"
            +"('96-42103-10022', 10001),"
            +"('96-42103-10033', 10006),"
            +"('96-42103-10034', 10003),"
            +"('96-42103-10040', 10003),"
            +"('96-42103-10054', 10002),"
            +"('96-42103-10068', 10001),"
            +"('96-42103-10081', 10000),"
            +"('96-42103-10093', 10000),"
            +"('96-42103-10109', 10000),"
            +"('96-42103-10206', 10003),"
            +"('96-42103-10300', 10004),"
            +"('96-42103-10401', 10006),"
            +"('96-42103-10502', 10007),"
            +"('96-42103-10604', 10003),"
            +"('96-42103-10709', 10006),"
            +"('96-42103-10800', 10006),"
            +"('96-42103-10907', 10002),"
            +"('96-42103-11003', 10001);";
            stmt.executeUpdate(publishedByValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String memberValues = "INSERT INTO `member` VALUES" 
            +"(1000, 'Wiley', 'Coyote', '9/10/1961'),"
            +"(1100, 'Bugs', 'Bunny', '6/24/1990'),"
            +"(1200, 'Olive', 'Oyl', '7/19/1989'),"
            +"(1300, 'Alice', 'Wonderland', '7/19/1989'),"
            +"(1400, 'Roger', 'Ramjet', '1/13/1985'),"
            +"(1500, 'Larry', 'Lujack', '6/6/1940'),"
            +"(1600, 'Thomas', 'Tankengine', '4/1/1991'),"
            +"(1700, 'Amber', 'Corwin', '12/1/1970'),"
            +"(1800, 'Dworkin', 'Barimen', '12/2/1950'),"
            +"(1900, 'Bel', 'Garion', '01/01/1983'),"
            +"(2000, 'Pol', 'Gara', '01/10/1963'),"
            +"(2001, 'Art', 'Clark', '12/6/2001'),"
            +"(2002, 'Edith', 'Crawley', '09/4/1962'),"
            +"(2003, 'John', 'Bates', '12/12/2001'),"
            +"(2004, 'Thomas', 'Barrow', '05/05/2005'),"
            +"(2005, 'John', 'Watson', '02/22/1975'),"
            +"(2006, 'Jim', 'Moriarty', '03/12/1967'),"
            +"(2007, 'Walter', 'White', '05/14/1983'),"
            +"(2008, 'Skyler', 'White', '04/12/1983'),"
            +"(2009, 'Dexter', 'Morgan', '07/11/1994'),"
            +"(2010, 'Rita', 'Bennett', '11/12/2001'),"
            +"(2011, 'Sun', 'Kwon', '10/19/1988'),"
            +"(2012, 'John', 'Locke', '01/01/2000'),"
            +"(2013, 'Clair', 'Littleton', '02/01/2001'),"
            +"(2015, 'Claire', 'Bennet', '03/11/2001'),"
            +"(2016, 'Mohinder', 'Suresh', '04/15/1993'),"
            +"(2017, 'Chloe', 'Sullivan', '05/16/2007'),"
            +"(2018, 'Martha', 'Kent', '02/13/1954'),"
            +"(2019, 'Paige', 'Matthews', '03/16/1977'),"
            +"(2020, 'Leo', 'Wyatt', '10/01/2000'),"
            +"(2021, 'Buffy', 'Summers', '05/26/2000'),"
            +"(2022, 'Xander', 'Harris', '06/23/2001');";
            stmt.executeUpdate(memberValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String borrowedByValues = "INSERT INTO `borrowedBy` VALUES"
            +"(1100, '96-42103-10604', '4/13/2016', '4/17/2016'),"
            +"(1100, '96-42103-10604', '3/9/2016', '3/10/2016'),"
            +"(1100, '96-42103-10003', '3/23/2016', '3/24/2016'),"
            +"(1100, '96-42103-10401', '5/14/2016', '5/16/2016'),"
            +"(1100, '96-42103-10001', '4/27/2016', '4/30/2016'),"
            +"(1100, '96-42103-10040', '5/16/2016', '5/16/2016'),"
            +"(1100, '96-42103-10709', '5/18/2016', '5/21/2016'),"
            +"(1100, '96-42103-10081', '3/15/2016', '3/17/2016'),"
            +"(1200, '96-42103-10081', '4/10/2016', '4/16/2016'),"
            +"(1200, '96-42103-10093', '3/27/2016', '3/29/2016'),"
            +"(1200, '96-42103-10109', '4/15/2016', '4/18/2016'),"
            +"(1300, '96-42103-10022', '3/9/2016', '3/10/2016'),"
            +"(1300, '96-42103-10206', '5/11/2016', '5/14/2016'),"
            +"(1300, '96-42103-10007', '5/11/2016', '5/12/2016'),"
            +"(1300, '96-42103-10001', '3/28/2016', '4/4/2016'),"
            +"(1300, '96-42103-10008', '4/20/2016', '4/26/2016'),"
            +"(1400, '96-42103-10033', '3/26/2016', '3/29/2016'),"
            +"(1400, '96-42103-10800', '3/4/2016', '3/6/2016'),"
            +"(1400, '96-42103-10008', '5/24/2016', '5/29/2016'),"
            +"(1400, '96-42103-10007', '5/6/2016', '5/11/2016'),"
            +"(1400, '96-42103-11003', '3/5/2016', '3/10/2016'),"
            +"(1400, '96-42103-10022', '3/4/2016', '3/7/2016'),"
            +"(1500, '96-42103-10004', '5/14/2016', '5/16/2016'),"
            +"(1500, '96-42103-10800', '4/2/2016', '4/5/2016'),"
            +"(1600, '96-42103-10206', '5/21/2016', '5/27/2016'),"
            +"(1600, '96-42103-10002', '5/4/2016', '5/9/2016'),"
            +"(1600, '96-42103-10011', '3/20/2016', '3/20/2016'),"
            +"(1600, '96-42103-10800', '5/10/2016', '5/13/2016'),"
            +"(1600, '96-42103-10300', '3/22/2016', '3/24/2016'),"
            +"(1700, '96-42103-10040', '5/18/2016', '5/21/2016'),"
            +"(1700, '96-42103-10604', '4/17/2016', '4/18/2016'),"
            +"(1700, '96-42103-11003', '4/17/2016', '4/20/2016'),"
            +"(1700, '96-42103-10004', '4/5/2016', '4/11/2016'),"
            +"(1700, '96-42103-10081', '3/5/2016', '3/8/2016'),"
            +"(1700, '96-42103-10800', '4/22/2016', '4/28/2016'),"
            +"(1800, '96-42103-10011', '4/25/2016', '4/26/2016'),"
            +"(1900, '96-42103-10008', '5/1/2016', '5/1/2016'),"
            +"(2000, '96-42103-10093', '4/12/2016', '4/12/2016'),"
            +"(2000, '96-42103-10005', '5/17/2016', '5/21/2016'),"
            +"(2000, '96-42103-10109', '5/9/2016', '5/11/2016'),"
            +"(2000, '96-42103-10006', '5/8/2016', '5/11/2016'),"
            +"(2001, '96-42103-10040', '4/22/2016' , '4/25/2016'),"
            +"(2002, '96-42103-10040', '5/22/2016', '5/27/2016'),"
            +"(2002, '96-42103-10008', '4/21/2016', '4/25/2016'),"
            +"(2002, '96-42103-10081', '3/4/2016', '3/6/2016'),"
            +"(2002, '96-42103-10006', '4/27/2016', '5/2/2016'),"
            +"(2003, '96-42103-10040', '4/9/2016', '4/14/2016'),"
            +"(2003, '96-42103-10033', '3/14/2016', '3/15/2016'),"
            +"(2003, '96-42103-10068', '4/12/2016', '4/16/2016'),"
            +"(2004, '96-42103-10800', '5/13/2016', '5/17/2016'),"
            +"(2004, '96-42103-10040', '4/27/2016', '5/2/2016'),"
            +"(2004, '96-42103-10007', '4/21/2016', '4/23/2016'),"
            +"(2004, '96-42103-10907', '5/9/2016', '5/10/2016'),"
            +"(2004, '96-42103-10800', '5/24/2016', '5/28/2016'),"
            +"(2005, '96-42103-10011', '4/20/2016', '4/22/2016'),"
            +"(2006, '96-42103-10800', '4/4/2016', '4/4/2016'),"
            +"(2007, '96-42103-10709', '3/13/2016', '3/17/2016'),"
            +"(2007, '96-42103-10033', '3/19/2016', '3/24/2016'),"
            +"(2007, '96-42103-10068', '4/19/2016', '4/20/2016'),"
            +"(2008, '96-42103-10502', '3/12/2016', '3/14/2016'),"
            +"(2008, '96-42103-11003', '4/4/2016', '4/8/2016'),"
            +"(2009, '96-42103-10022', '3/24/2016', '3/30/2016'),"
            +"(2009, '96-42103-10709', '4/12/2016', '4/15/2016'),"
            +"(2009, '96-42103-10022', '4/10/2016', '4/10/2016'),"
            +"(2009, '96-42103-10009', '3/20/2016', '3/22/2016'),"
            +"(2009, '96-42103-10033', '4/30/2016', '5/2/2016'),"
            +"(2009, '96-42103-10007', '3/7/2016', '3/13/2016'),"
            +"(2009, '96-42103-10002', '4/12/2016', '4/13/2016'),"
            +"(2009, '96-42103-10709', '4/30/2016', '5/3/2016'),"
            +"(2009, '96-42103-10054', '4/29/2016', '5/2/2016'),"
            +"(2011, '96-42103-10093', '4/20/2016', '4/20/2016'),"
            +"(2011, '96-42103-10011', '3/9/2016', '3/11/2016'),"
            +"(2011, '96-42103-10081', '3/15/2016', '3/20/2016'),"
            +"(2011, '96-42103-10800', '5/29/2016', NULL),"
            +"(2011, '96-42103-11003', '3/6/2016', '3/8/2016'),"
            +"(2011, '96-42103-10040', '3/10/2016', '3/16/2016'),"
            +"(2012, '96-42103-10009', '3/28/2016', '3/29/2016'),"
            +"(2012, '96-42103-10006', '3/24/2016', '3/28/2016'),"
            +"(2012, '96-42103-11003', '4/4/2016', '4/9/2016'),"
            +"(2012, '96-42103-10008', '4/26/2016', '4/28/2016'),"
            +"(2012, '96-42103-10002', '5/27/2016', NULL),"
            +"(2013, '96-42103-10008', '4/19/2016', '4/23/2016'),"
            +"(2013, '96-42103-10054', '3/26/2016', '3/29/2016'),"
            +"(2013, '96-42103-10033', '5/17/2016', '5/21/2016'),"
            +"(2015, '96-42103-10009', '3/15/2016', '3/19/2016'),"
            +"(2016, '96-42103-10033', '4/28/2016', '4/30/2016'),"
            +"(2016, '96-42103-10907', '4/22/2016', '4/28/2016'),"
            +"(2016, '96-42103-10206', '5/9/2016', '5/14/2016'),"
            +"(2016, '96-42103-10907', '3/6/2016', '3/8/2016'),"
            +"(2016, '96-42103-10907', '3/21/2016', '3/22/2016'),"
            +"(2016, '96-42103-10001', '4/7/2016', '4/8/2016'),"
            +"(2016, '96-42103-10206', '5/15/2016', '5/15/2016'),"
            +"(2017, '96-42103-10709', '4/20/2016', '4/25/2016'),"
            +"(2017, '96-42103-10300', '5/20/2016', '5/26/2016'),"
            +"(2017, '96-42103-10093', '3/19/2016', '3/23/2016'),"
            +"(2017, '96-42103-10300', '5/1/2016', '5/5/2016'),"
            +"(2017, '96-42103-10004', '3/14/2016', '3/16/2016'),"
            +"(2018, '96-42103-10033', '3/5/2016', '3/7/2016'),"
            +"(2018, '96-42103-10004', '3/25/2016', '3/27/2016'),"
            +"(2018, '96-42103-10907', '3/16/2016', '3/20/2016'),"
            +"(2018, '96-42103-10008', '5/10/2016', '5/10/2016'),"
            +"(2018, '96-42103-10033', '4/1/2016', '4/7/2016'),"
            +"(2018, '96-42103-10300', '4/5/2016', '4/10/2016'),"
            +"(2018, '96-42103-10206', '3/5/2016', '3/11/2016'),"
            +"(2018, '96-42103-10709', '4/20/2016', '4/25/2016'),"
            +"(2018, '96-42103-10206', '5/12/2016', '5/17/2016'),"
            +"(2019, '96-42103-10009', '3/11/2016', '3/12/2016'),"
            +"(2020, '96-42103-10022', '4/9/2016', '4/10/2016'),"
            +"(2020, '96-42103-10206', '5/13/2016', '5/18/2016'),"
            +"(2020, '96-42103-10093', '4/8/2016', '4/12/2016'),"
            +"(2020, '96-42103-10401', '5/29/2016', NULL),"
            +"(2021, '96-42103-10033', '3/14/2016', '3/16/2016'),"
            +"(2021, '96-42103-10001', '5/21/2016', '5/26/2016'),"
            +"(2021, '96-42103-10054', '4/29/2016', '4/30/2016'),"
            +"(2022, '96-42103-10009', '3/25/2016', '3/29/2016'),"
            +"(2022, '96-42103-10011', '5/3/2016', '5/6/2016'),"
            +"(2022, '96-42103-10109', '4/16/2016', '4/21/2016'),"
            +"(2022, '96-42103-11003', '6/1/2016', NULL),"
            +"(2022, '96-42103-10081', '3/26/2016', '3/30/2016'),"
            +"(2022, '96-42103-10093', '3/27/2016', '3/29/2016'),"
            +"(2022, '96-42103-10502', '3/26/2016', '4/1/2016'),"
            +"(2022, '96-42103-10003', '5/6/2016', '5/9/2016');";
            stmt.executeUpdate(borrowedByValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String libraryValues = "INSERT INTO `library` VALUES"

            +"('Main', '42 South Main', 'Fort Collins', 'CO'),"
            +"('South Park', '4000 South College', 'Fort Collins', 'CO');";
            stmt.executeUpdate(libraryValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        try {
            String locatedAtValues = "INSERT INTO `locatedAt` VALUES"

            +"('Main', '96-42103-10001', 3, 1, 1, 3),"
            +"('Main', '96-42103-10002', 1, 1, 1, 0),"
            +"('Main', '96-42103-10003', 1, 1, 1, 1),"
            +"('Main', '96-42103-10004', 2, 2, 1, 2),"
            +"('Main', '96-42103-10005', 1, 2, 1, 1),"
            +"('Main', '96-42103-10006', 1, 2, 1, 1),"
            +"('Main', '96-42103-10007', 2, 3, 1, 2),"
            +"('Main', '96-42103-10008', 2, 3, 1, 2),"
            +"('Main', '96-42103-10009', 2, 3, 1, 2),"
            +"('Main', '96-42103-10011', 2, 4, 1, 2),"
            +"('Main', '96-42103-10022', 3, 4, 1, 3),"
            +"('Main', '96-42103-10033', 3, 4, 1, 3),"
            +"('Main', '96-42103-10034', 1, 5, 2, 1),"
            +"('Main', '96-42103-10040', 2, 5, 2, 2),"
            +"('Main', '96-42103-10054', 2, 5, 2, 2),"
            +"('Main', '96-42103-10068', 1, 6, 2, 1),"
            +"('Main', '96-42103-10081', 1, 6, 2, 1),"
            +"('Main', '96-42103-10093', 1, 6, 2, 1),"
            +"('Main', '96-42103-10109', 1, 6, 2, 1),"
            +"('Main', '96-42103-10206', 1, 7, 2, 1),"
            +"('Main', '96-42103-10300', 2, 7, 2, 2),"
            +"('Main', '96-42103-10401', 2, 7, 2, 1),"
            +"('Main', '96-42103-10502', 1, 7, 2, 1),"
            +"('Main', '96-42103-10604', 2, 8, 2, 2),"
            +"('Main', '96-42103-10709', 3, 8, 2, 3),"
            +"('Main', '96-42103-10800', 2, 8, 2, 1),"
            +"('Main', '96-42103-10907', 7, 8, 2, 7),"
            +"('Main', '96-42103-11003', 3, 8, 2, 2),"
            +"('South Park', '96-42103-10001', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10002', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10003', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10004', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10005', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10006', 1, 8, 3, 1),"
            +"('South Park', '96-42103-10007', 3, 7, 3, 3),"
            +"('South Park', '96-42103-10008', 3, 7, 3, 3),"
            +"('South Park', '96-42103-10009', 1, 7, 3, 1),"
            +"('South Park', '96-42103-10011', 1, 7, 3, 1),"
            +"('South Park', '96-42103-10022', 2, 7, 3, 2),"
            +"('South Park', '96-42103-10033', 3, 7, 3, 3),"
            +"('South Park', '96-42103-10040', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10054', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10068', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10081', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10093', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10109', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10206', 1, 4, 2, 1),"
            +"('South Park', '96-42103-10401', 1, 3, 2, 1),"
            +"('South Park', '96-42103-10502', 1, 3, 2, 1),"
            +"('South Park', '96-42103-11604', 2, 3, 2, 2),"
            +"('South Park', '96-42103-11709', 1, 3, 2, 1),"
            +"('South Park', '96-42103-11800', 2, 3, 2, 2),"
            +"('South Park', '96-42103-10907', 7, 3, 2, 7),"
            +"('South Park', '96-42103-11003', 2, 3, 2, 2);";
            stmt.executeUpdate(locatedAtValues);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        try{
            String update = "ALTER TABLE borrowedBy"
                        + " ADD library varchar(16);";
            stmt.executeUpdate(update);
        }catch(Exception e){
            System.out.print(e);
        }//end catch
        try{
            rs = stmt.executeQuery("SELECT ISBN FROM book");
            while (rs.next()) {
              books.add(rs.getString("ISBN"));
          }
          }catch(Exception e){
            System.out.print(e);
            System.out.println(
                      "No book table to query");
          }//end catch
          try{
            rs = stmt.executeQuery("SELECT memberId, ISBN FROM borrowedBy WHERE checkInDate is NULL;");
            while (rs.next()) {
              booksCheckedOut.put(rs.getString("ISBN"), rs.getString("memberId"));
          }
          }catch(Exception e){
            System.out.print(e);
            System.out.println(
                      "No book table to query");
          }//end catch

		try {
			File file = new File("./src/activity.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Borrowed_by");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element sectionNode = (Element) fstNode;
					
					NodeList memberIdElementList = sectionNode.getElementsByTagName("MemberID");
					Element memberIdElmnt = (Element) memberIdElementList.item(0);
					NodeList memberIdNodeList = memberIdElmnt.getChildNodes();
                    member = ((Node) memberIdNodeList.item(0)).getNodeValue().trim();

					NodeList secnoElementList = sectionNode.getElementsByTagName("ISBN");
					Element secnoElmnt = (Element) secnoElementList.item(0);
					NodeList secno = secnoElmnt.getChildNodes();
                    ISBN = ((Node) secno.item(0)).getNodeValue().trim();

                    if(books.contains(ISBN)) {
                        contains = true;
                    }
                    else {
                        contains = false;
                    }

					NodeList thirdElementList = sectionNode.getElementsByTagName("Library");
					Element thirdElmnt = (Element) thirdElementList.item(0);
					NodeList third = thirdElmnt.getChildNodes();
                    libraryNode = ((Node) third.item(0)).getNodeValue().trim();

					NodeList codateElementList = sectionNode.getElementsByTagName("Checkout_date");
					Element codElmnt = (Element) codateElementList.item(0);
					NodeList cod = codElmnt.getChildNodes();
                    checkedOutValue = ((Node) cod.item(0)).getNodeValue().trim();

					NodeList cidateElementList = sectionNode.getElementsByTagName("Checkin_date");
					Element cidElmnt = (Element) cidateElementList.item(0);
					NodeList cid = cidElmnt.getChildNodes();
                    checkedInValue = ((Node) cid.item(0)).getNodeValue().trim();

                    if(!checkedInValue.equals("N/A") && !checkedOutValue.equals("N/A")) {
                        checkedIn = true;
                        checkedOut = true;
                    }

                    else if(checkedInValue.equals("N/A") && !checkedOutValue.equals("N/A")) {
                        checkedIn = false;
                        checkedOut = true;
                    }

                    else {
                        checkedIn = true;
                        checkedOut = false;
                    }

                    if(contains) {

                        Integer availableCopies = null;
                        String update;

                        try {
                            String request = String.format("SELECT availableCopies FROM locatedAt WHERE name = \'%s\' AND ISBN = \'%s\';", libraryNode, ISBN);
                            rs = stmt.executeQuery(request);
                            rs.next();
                            availableCopies = Integer.parseInt(rs.getString("availableCopies"));

                        }
                        catch(SQLException e) {
                            availableCopies = 0;
                        }      

                        if(!booksCheckedOut.containsKey(ISBN) && availableCopies > 0) { 
                            if(checkedIn && checkedOut) {
                                update = String.format("INSERT into borrowedBy values(%s, \'%s\', \'%s\', \'%s\', \'%s\')", member, ISBN, checkedOutValue, checkedInValue, libraryNode);
                                System.out.println(String.format("(%s, \'%s\', \'%s\', \'%s\', \'%s\') added to borrowedBy. Instance details below.", member, ISBN, checkedOutValue, checkedInValue, libraryNode));
                            }
                            else if (!checkedIn && checkedOut){
                                update = String.format("INSERT into borrowedBy values(%s, \'%s\', \'%s\', Null, \'%s\')", member, ISBN, checkedOutValue, libraryNode);
                                System.out.println(String.format("(%s, \'%s\', \'%s\', Null, \'%s\') added to borrowedBy. Instance details below.", member, ISBN, checkedOutValue, libraryNode));
                            }
                            else {
                                System.out.println("Member: " + member);
                                System.out.println("ISBN: " + ISBN);
                                System.out.println("Library: " + libraryNode);
                                System.out.println("CheckOut: " + checkedOutValue);
                                System.out.println("CheckIn: " + checkedInValue);
                                System.out.println("Not added.");
                                System.out.println("Book never checked out.");
                                System.out.println();
                                continue; 
                            }
                            try {
                                stmt.executeUpdate(update);
                                System.out.println("Member: " + member);
                                System.out.println("ISBN: " + ISBN);
                                System.out.println("Library: " + libraryNode);
                                System.out.println("CheckOut: " + checkedOutValue);
                                System.out.println("CheckIn: " + checkedInValue);
                                System.out.println("Added.");
                                if(!checkedIn && checkedOut){
                                    update = String.format("UPDATE locatedAt SET availableCopies = %s WHERE name = \'%s\' AND ISBN = \'%s\';", Integer.toString(availableCopies - 1), libraryNode, ISBN);
                                    System.out.println(String.format("Updated locatedAt availableCopies to %s for name \'%s\' and ISBN \'%s\';", Integer.toString(availableCopies - 1), libraryNode, ISBN));
                                    stmt.executeUpdate(update);
                                    booksCheckedOut.put(ISBN, member);
                                }
                                System.out.println();
                            }
                            catch(SQLException e) {
                                System.out.println(e);
                            }
                        }
                        else {
                            if((checkedIn && !checkedOut) && booksCheckedOut.get(ISBN).equals(member)) {
                                try {
                                    update = String.format("UPDATE borrowedBy SET checkinDate = \'%s\', library = \'%s\' WHERE ISBN = \'%s\' AND memberID = %s;", checkedInValue, libraryNode, ISBN, member);
                                    System.out.println(String.format("Updated borrowedBy checkinDate to \'%s\' and library to \'%s\' for ISBN \'%s\' and memberID %s. Instance details below.", checkedInValue, libraryNode, ISBN, member));
                                    stmt.executeUpdate(update);
                                    System.out.println("Member: " + member);
                                    System.out.println("ISBN: " + ISBN);
                                    System.out.println("Library: " + libraryNode);
                                    System.out.println("CheckOut: " + checkedOutValue);
                                    System.out.println("CheckIn: " + checkedInValue);
                                    System.out.println("Added.");
                                    update = String.format("UPDATE locatedAt SET availableCopies = %s WHERE name = \'%s\' AND ISBN = \'%s\';", Integer.toString(availableCopies + 1), libraryNode, ISBN);
                                    System.out.println(String.format("Updated locatedAt availableCopies to %s for name \'%s\' and ISBN \'%s\';", Integer.toString(availableCopies + 1), libraryNode, ISBN));
                                    System.out.println();
                                    stmt.executeUpdate(update);
                                    booksCheckedOut.remove(ISBN);   
                                } 
                                catch(SQLException e) {
                                    System.out.println(e);
                                }                                              
                            }
                            else {
                                if(availableCopies <= 0) {
                                    System.out.println("Member: " + member);
                                    System.out.println("ISBN: " + ISBN);
                                    System.out.println("Library: " + libraryNode);
                                    System.out.println("CheckOut: " + checkedOutValue);
                                    System.out.println("CheckIn: " + checkedInValue);
                                    System.out.println("Not added.");
                                    System.out.println("No copies available.");
                                    System.out.println();  
                                } 
                                else {
                                    update = String.format("INSERT into borrowedBy values(%s, \'%s\', \'%s\', Null, \'%s\')", member, ISBN, checkedOutValue, libraryNode);
                                    System.out.println(String.format("(%s, \'%s\', \'%s\', Null, \'%s\') added to borrowedBy. Instance details below.", member, ISBN, checkedOutValue, libraryNode));
                                    try {
                                        stmt.executeUpdate(update);
                                        update = String.format("UPDATE locatedAt SET availableCopies = %s WHERE name = \'%s\' AND ISBN = \'%s\';", Integer.toString(availableCopies - 1), libraryNode, ISBN);
                                        stmt.executeUpdate(update);
                                        System.out.println("Member: " + member);
                                        System.out.println("ISBN: " + ISBN);
                                        System.out.println("Library: " + libraryNode);
                                        System.out.println("CheckOut: " + checkedOutValue);
                                        System.out.println("CheckIn: " + checkedInValue);
                                        System.out.println("Added.");
                                        System.out.println(String.format("Updated locatedAt availableCopies to %s for name \'%s\' and ISBN \'%s\'.", Integer.toString(availableCopies - 1), libraryNode, ISBN));
                                        System.out.println();
                                        booksCheckedOut.put(ISBN, member);
                                    }
                                    catch(SQLException e) {
                                        System.out.println(e);
                                    }  
                                }                           
                            }
                        }
                    }

                    else if(!contains) {
                        System.out.println("Member: " + member);
                        System.out.println("ISBN: " + ISBN);
                        System.out.println("Library: " + libraryNode);
                        System.out.println("CheckOut: " + checkedOutValue);
                        System.out.println("CheckIn: " + checkedInValue);
                        System.out.println("Not added.");
                        System.out.println("ISBN: " + ISBN + " not in list of books.");
                        System.out.println();
                    }

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    
          con.close();
        }catch( Exception e ) {
          e.printStackTrace();
    
        }//end catch
    
      }//end main
    
    }//end class
