# jdbc_library_system — Library Database (Java + MySQL + XML)

An academic Java project demonstrating **relational schema design**, **SQL DDL/DML**, and **XML-to-database ingestion** using **JDBC** against a **MySQL** backend. The program builds (and/or refreshes) a small library database (books, authors, publishers, members, borrowing activity, and library inventory), loads activity records from an XML file, and executes a sequence of inserts/updates/deletes consistent with typical library checkout/check-in workflows.

---


## Functionality Summary

### `App.java` (main program)

`App` is the primary driver that:

* Connects to a MySQL database using **JDBC** (`com.mysql.cj.jdbc.Driver`).
* Defines and executes SQL to create a normalized schema representing a library system, including tables such as:

  * `phone`, `author`, `publisher`, `book`, `member`, `library`
  * relationship tables like `authorHas`, `publisherHas`, `writtenBy`, `publishedBy`, `borrowedBy`, `locatedAt`
* Performs DML operations (**INSERT/UPDATE/DELETE/SELECT**) to populate and manipulate data.
* Parses `activity.xml` using a DOM parser to extract borrowing events and apply them to the database (e.g., checkout/check-in logic).
* Produces deterministic, console-readable output for the executed operations.

### `Lab4_xml.java` (XML parsing utility)

A smaller utility class that demonstrates DOM-based parsing of `activity.xml`, iterating through `<Borrowed_by>` elements and printing fields such as member IDs, ISBNs, and related values.

### `activity.xml`

A sample XML dataset containing `<Borrowed_by>` records (borrowing activity) used by the program to drive database updates.

---

## Prerequisites

* **Java JDK** (8+ recommended)
* **MySQL** (or access to a MySQL server)
* **MySQL Connector/J** (JDBC driver jar)

If you are running on a university/department server, ensure you can reach the host used in your JDBC URL.

---

## Database Configuration

In `App.java`, the connection is configured similarly to:

```java
String url = "url_to_sql_database";
con = DriverManager.getConnection(url, "username", "id_number");
```

Update these values to match your environment:

* **Host/DB**: change `url_to_sql_database` to your MySQL host and database name
* **Username/Password**: replace the placeholder credentials with valid credentials

> Tip: If you run locally, you’ll commonly use something like `jdbc:mysql://localhost:3306/<dbname>`.

---

## How to Run

### 1) Ensure the XML file path is correct

`App.java` references an XML path like:

```java
File file = new File("./src/activity.xml");
```

You have two options:

* **Option A (recommended):** create a `src/` directory and place `activity.xml` inside it.
* **Option B:** edit the path in `App.java` to point to the repo location (e.g., `./activity.xml`).

### 2) Compile

Download the MySQL Connector/J jar (e.g., `mysql-connector-j-8.x.x.jar`) and compile with it on your classpath.

**macOS/Linux**:

```bash
javac -cp .:mysql-connector-j-8.x.x.jar App.java Lab4_xml.java
```

**Windows (PowerShell/cmd)**:

```bat
javac -cp .;mysql-connector-j-8.x.x.jar App.java Lab4_xml.java
```

### 3) Run

**macOS/Linux**:

```bash
java -cp .:mysql-connector-j-8.x.x.jar App
```

**Windows**:

```bat
java -cp .;mysql-connector-j-8.x.x.jar App
```

The program will:

* connect to MySQL
* create/refresh the library schema
* parse `activity.xml`
* apply the corresponding SQL operations
* print progress/results to stdout

---

## Notes

* The program is structured as an **academic demonstration** of SQL + JDBC + XML parsing. It prioritizes clarity of schema and query logic over production hardening.
* If you encounter `Communications link failure`, verify:

  * the MySQL host is reachable
  * the port is open (default `3306`)
  * credentials are correct
* If tables already exist and the program re-creates them, ensure your MySQL user has sufficient privileges.

---

