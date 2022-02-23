# Chapter 9 - CRUD, transactions, locking

In this chapter, we cover a must-know mix of fundamental notions about CRUD operations, transactions, and locking. These three topics are heavily exploited in almost any database application. In a common scenario, an application has a significant number of CRUD operations that are executed in explicitly demarcated logical transactions and, in certain cases, they also need to explicitly control the concurrent access to data in order to prevent race conditions, lost updates, and other SQL phenomena (or SQL anomalies). 
