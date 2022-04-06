# Give yourself the chance to learn jOOQ and SQL

https://twitter.com/anghelleonard/status/1504041764025511937
![](https://github.com/PacktPublishing/Up-and-Running-with-jOOQ/blob/master/db/Learn%20SQL%20Quiz.png)
https://twitter.com/anghelleonard/status/1504041764025511937

# Good to know before running the code

The database schema (`classicmodels`) is available for four dialects: MySQL, PostgreSQL, SQL Server, and Oracle. Depending on dialect, the corresponding schema may contain things (e.g., data types) that are specific to that dialect. Most of the applications (80%+) are available for all these dialects, and they may differ from each other depending on dialect particularities. Also, most applications uses the schema from the `db/migration/dev` folder, but some applications (detailed in the book) may use a smaller (reduced) version of this schema available in separate folders. While all applications are available for Maven, most of all are available for Gradle as well.

## The database schema ER Diagram (generated for MySQL via DBeaver)
![](https://github.com/PacktPublishing/Up-and-Running-with-jOOQ/blob/master/db/ER%20Diagram.png)

## What you need
You'll need:

- MySQL (8.0); database name: `classicmodels`, port: `3306`, username: `root`, password: `root` 
- PostgreSQL (13); database name: `classicmodels`, port: `5432`, username: `postgres`, password: `root`
- SQL Server (2017); database name: `classicmodels`, port: `1433`, username: `sa`, password: `root`
- Oracle (18c); database: `jdbc:oracle:thin:@localhost:1521:xe`, username: `CLASSICMODELS`, password: `root`
- an IDE (e.g., IntelliJ IDEA, NetBeans, Eclipse, and so on)
- Java 17
- jOOQ OSS (this is downloaded automatically as a dependency)
- jOOQ Trial Version (this shoud be installed as [here](https://github.com/PacktPublishing/Up-and-Running-with-jOOQ/blob/master/Install_jOOQ_Trial.mp4); this is needed especially for applications that uses SQL Server and Oracle, but sometimes for MySQL and PostgreSQL as well)
- A databse viewer (e.g., the specific database viewer, or a general one such as DBeaver, SharpDevelop, and so on)
- Basic knowledge of SQL and database usage :)

For MySQL, the database is created automatically by the application, while for PostgreSQL, SQL Server and Oracle you have to do it manually (or, via a script). It is recommended to run each application from a fresh database. This way you avoid errors that may occur (for instance, application `A` deletes a row selected by application `B` for update).
