# Good to know

The database schema (`classicmodels`) is available for four dialects: MySQL, PostgreSQL, SQL Server, and Oracle. Depending on dialect, the corresponding schema may contain things (e.g., data types) that are specific to that dialect. Most of the applications (80%+) are available for all these dialects, and they may differ from each other depending on dialect particularities. Also, most applications uses the schema from the `db/migration/dev` folder, but some applications (detailed in the book) may use a smaller (reduced) version of this schema.

## The database schema ER Diagram (generated for MySQL via DBeaver)
![](https://github.com/PacktPublishing/Up-and-Running-with-jOOQ/blob/master/db/ER%20Diagram.png)

## What you need
You'll need:

- MySQL (8.0), PostgreSQL (13), SQL Server (2017), Oracle (18c).
- an IDE (e.g., NetBeans, Eclipse, and so on)
- Java 17
- jOOQ OSS (this is downloaded automatically as a dependency)
- jOOQ Trial Version (this shoud be installed as [here](https://github.com/PacktPublishing/Up-and-Running-with-jOOQ/blob/master/Install_jOOQ_Trial.mp4); this is needed especially for applications that uses SQL Server and Oracle, but sometimes for MySQL and PostgreSQL as well)
- A databse viewer (e.g., the specific database viewer, or a general one such as DBeaver, SharpDevelop, and so on)
- Basic knowledge of SQL and database usage :)

For MySQL, the database is created automatically by the application, while for PostgreSQL, SQL Server and Oracle you have to do it manually (or, via a script).

It is recommended to run each application from a fresh database. This way you avoid errors that may occur (for instance, application A deletes a row selected by application B for update).
