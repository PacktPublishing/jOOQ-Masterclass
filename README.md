


# jOOQ Masterclass 

<a href="https://www.packtpub.com/product/jooq-masterclass/9781800566897?utm_source=github&utm_medium=repository&utm_campaign="><img src="https://static.packt-cdn.com/products/9781800566897/cover/smaller" alt="jOOQ Masterclass " height="256px" align="right"></a>

This is the code repository for [jOOQ Masterclass ](https://www.packtpub.com/product/jooq-masterclass/9781800566897?utm_source=github&utm_medium=repository&utm_campaign=), published by Packt.

**A practical guide for Java developers to write SQL queries for complex database interactions**

## What is this book about?
jOOQ Masterclass will help you write the coolest SQL statements without working with JDBC or handling ORM complexity and performance issues. This practical guide to jOOQ provides a hands-on approach with a complete and versatile set of solutions for implementing the persistence layer to serve the most stressful environments.

This book covers the following exciting features:
* Enable the jOOQ Code Generator in any combination of Java and Kotlin, Maven and Gradle
* Generate jOOQ artifacts directly from database schema, or without touching the real database
* Use jOOQ DSL to write and execute a wide range of queries for different databases
* Understand jOOQ type-safe queries, CRUD operations, converters, bindings, and mappers
* Implement advanced SQL concepts such as stored procedures, derived tables, CTEs, window functions, and database views
* Implement jOOQ multi-tenancy, tuning, jOOQ SPI, logging, and testing	

If you feel this book is for you, get your [copy](https://www.amazon.com/dp/1800566891) today!

<a href="https://www.packtpub.com/?utm_source=github&utm_medium=banner&utm_campaign=GitHubBanner"><img src="https://raw.githubusercontent.com/PacktPublishing/GitHub/master/GitHub.png" 
alt="https://www.packtpub.com/" border="5" /></a>

## Instructions and Navigations
All of the code is organized into folders. For example, Chapter02.

The code will look like the following:
```
public List<Office> findOfficesInTerritory(
                    String territory) {
  List<Office> result = ctx.selectFrom(table("office"))
    .where(field("territory").eq(territory))
    .fetchInto(Office.class);
  return result;
}
```

**Following is what you need for this book:**
This book is for Java developers who write applications that interact with databases via SQL. No prior experience with jOOQ is assumed.

With the following software and hardware list you can run all code files present in the book (Chapter 1-19).
### Software and Hardware List
| Chapter | Software required | OS required |
| -------- | ------------------------------------ | ----------------------------------- |
| 1 | MySql | Windows and Linux |
| 1 | Postgres | Windows and Linux |
| 1 | SQL Server | Windows and Linux |
| 1 | Oracle | Windows and Linux |


We also provide a PDF file that has color images of the screenshots/diagrams used in this book. [Click here to download it](https://packt.link/a1q9L).

### Related products
* The Complete Coding Interview Guide in Java  [[Packt]](https://www.packtpub.com/product/the-complete-coding-interview-guide-in-java/9781839212062?utm_source=github&utm_medium=repository&utm_campaign=) [[Amazon]](https://www.amazon.com/dp/1839212063)

* Designing Hexagonal Architecture with Java  [[Packt]](https://www.packtpub.com/product/designing-hexagonal-architecture-with-java-and-quarkus/9781801816489?utm_source=github&utm_medium=repository&utm_campaign=) [[Amazon]](https://www.amazon.com/dp/1801816484)


## Get to Know the Author
**Anghel Leonard**
 is a chief technology strategist and independent consultant with 20+ years of experience in the Java ecosystem. In his daily work, he is focused on architecting and developing Java-distributed applications that empower robust architectures, clean code, and high performance. He is also passionate about coaching, mentoring, and technical leadership. He is the author of several books, videos, and dozens of articles related to Java technologies.


## Other books by the authors
[Java Coding Problems ](https://www.packtpub.com/product/java-coding-problems/9781789801415?utm_source=github&utm_medium=repository&utm_campaign=)

[Data Stream Development with Apache Spark, Kafka, and Spring Boot [Video] ](https://www.packtpub.com/product/data-stream-development-with-apache-spark-kafka-and-spring-boot-video/9781789539585?utm_source=github&utm_medium=repository&utm_campaign=)

[The Complete Coding Interview Guide in Java ](https://www.packtpub.com/product/the-complete-coding-interview-guide-in-java/9781839212062?utm_source=github&utm_medium=repository&utm_campaign=)

## Link to the readme

* https://github.com/PacktPublishing/jOOQ-Masterclass/blob/master/README.md
### Download a free PDF

 <i>If you have already purchased a print or Kindle version of this book, you can get a DRM-free PDF version at no cost.<br>Simply click on the link to claim your free PDF.</i>
<p align="center"> <a href="https://packt.link/free-ebook/9781800566897">https://packt.link/free-ebook/9781800566897 </a> </p>