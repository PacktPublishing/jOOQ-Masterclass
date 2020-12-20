/*
*********************************************************************
http://www.mysqltutorial.org
*********************************************************************
Name: MySQL Sample Database classicmodels
Link: http://www.mysqltutorial.org/mysql-sample-database.aspx
*********************************************************************

This is a modified version of the original schema for Oracle
*/

/* START */

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "PAYMENT" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "BANK_TRANSACTION" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "ORDERDETAIL" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "ORDER" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "PRODUCT" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "PRODUCTLINE" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "TOP3PRODUCT" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "PRODUCTLINEDETAIL" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "OFFICE_HAS_MANAGER" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "MANAGER" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "CUSTOMER" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "CUSTOMERDETAIL" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "SALE" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "EMPLOYEE" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "DEPARTMENT" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE "OFFICE" CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
COMMIT;

/*Table structure for table `office` */

CREATE TABLE office (
  office_code varchar2(10) NOT NULL,
  city varchar2(50),
  phone varchar2(50) NOT NULL,
  address_line_first varchar2(50) NOT NULL,
  address_line_second varchar2(50) DEFAULT NULL,
  state varchar2(50) DEFAULT NULL,
  country varchar2(50),
  postal_code varchar2(15) NOT NULL,
  territory varchar2(10) NOT NULL,
  PRIMARY KEY (office_code)
) ;

/*Table structure for table `employee` */

BEGIN
   EXECUTE IMMEDIATE 'CREATE TYPE employeeOfYearArr AS VARRAY(100) OF INTEGER;';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE employee (
  employee_number number(10) NOT NULL,
  last_name varchar2(50) NOT NULL,
  first_name varchar2(50) NOT NULL,
  extension varchar2(10) NOT NULL,
  email varchar2(100) NOT NULL,
  office_code varchar2(10) NOT NULL,
  salary int NOT NULL,
  reports_to number(10) DEFAULT NULL,
  job_title varchar2(50) NOT NULL,
  employee_of_year employeeOfYearArr DEFAULT NULL,
  PRIMARY KEY (employee_number)
 ,
  CONSTRAINT employees_ibfk_1 FOREIGN KEY (reports_to) REFERENCES employee (employee_number),
  CONSTRAINT employees_ibfk_2 FOREIGN KEY (office_code) REFERENCES office (office_code)
) ;

CREATE INDEX reports_to ON employee (reports_to);
CREATE INDEX office_code ON employee (office_code);

/*Table structure for table `sale` */

CREATE TABLE sale (
  sale_id number(20) NOT NULL, 
  fiscal_year int NOT NULL, 
  sale float NOT NULL,    
  employee_number number(10) DEFAULT NULL,  
  hot number(1,0) DEFAULT 0,
  PRIMARY KEY (sale_id)
,  
  CONSTRAINT sales_ibfk_1 FOREIGN KEY (employee_number) REFERENCES employee (employee_number)
) ;

CREATE INDEX employee_number ON sale (employee_number);

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "SALE_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE sale_seq START WITH 1000000 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER sale_seq_tr
 BEFORE INSERT ON sale FOR EACH ROW
 WHEN (NEW.sale_id IS NULL)
BEGIN
 SELECT sale_seq.NEXTVAL INTO :NEW.sale_id FROM DUAL;
END;
/

/*Table structure for table `customer` */

CREATE TABLE customer (
  customer_number number(10) NOT NULL,
  customer_name varchar2(50) NOT NULL,
  contact_last_name varchar2(50) NOT NULL,
  contact_first_name varchar2(50) NOT NULL,
  phone varchar2(50) NOT NULL,
  sales_rep_employee_number number(10) DEFAULT NULL,
  credit_limit number(10,2) DEFAULT NULL,
  PRIMARY KEY (customer_number)
 ,
  CONSTRAINT customers_ibfk_1 FOREIGN KEY (sales_rep_employee_number) REFERENCES employee (employee_number)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "CUSTOMER_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE customer_seq START WITH 1000000 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER customer_seq_tr
 BEFORE INSERT ON customer FOR EACH ROW
 WHEN (NEW.customer_number IS NULL)
BEGIN
 SELECT customer_seq.NEXTVAL INTO :NEW.customer_number FROM DUAL;
END;
/

CREATE INDEX sales_rep_employee_number ON customer (sales_rep_employee_number);

/*Table structure for table `customerdetail` */

CREATE TABLE customerdetail (
  customer_number number(10) NOT NULL,
  address_line_first varchar2(50) NOT NULL,
  address_line_second varchar2(50) DEFAULT NULL,
  city varchar2(50),
  state varchar2(50) DEFAULT NULL,
  postal_code varchar2(15) DEFAULT NULL,
  country varchar2(50),
  PRIMARY KEY (customer_number)
 ,
  CONSTRAINT customers_details_ibfk_1 FOREIGN KEY (customer_number) REFERENCES customer (customer_number)
) ; 

/* Table structure for table `department` */

BEGIN
   EXECUTE IMMEDIATE 'CREATE TYPE topicArr AS VARRAY(100) OF VARCHAR2(100 CHAR);';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE department (
  department_id number(10) NOT NULL,
  name varchar(50) NOT NULL,
  phone varchar(50) NOT NULL,
  code number(5) DEFAULT 1,
  office_code varchar(10) NOT NULL,
  topic topicArr NOT NULL,
  PRIMARY KEY (department_id)
,
  CONSTRAINT department_ibfk_1 FOREIGN KEY (office_code) REFERENCES office (office_code)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "DEPARTMENT_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE department_seq START WITH 10 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER department_seq_tr
 BEFORE INSERT ON department FOR EACH ROW
 WHEN (NEW.department_id IS NULL)
BEGIN
 SELECT department_seq.NEXTVAL INTO :NEW.department_id FROM DUAL;
END;
/

/*Table structure for table `manager` */

CREATE TABLE manager (
  manager_id number(10) NOT NULL,
  manager_name varchar2(50) NOT NULL,
  PRIMARY KEY (manager_id)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "MANAGER_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE manager_seq START WITH 1000000 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER manager_seq_tr
 BEFORE INSERT ON manager FOR EACH ROW
 WHEN (NEW.manager_id IS NULL)
BEGIN
 SELECT manager_seq.NEXTVAL INTO :NEW.manager_id FROM DUAL;
END;
/

/*Table structure for table `office_has_manager` */

CREATE TABLE office_has_manager (
  offices_office_code varchar2(10) REFERENCES office (office_code),
  managers_manager_id number(10) REFERENCES manager (manager_id),
  CONSTRAINT offices_managers_pkey PRIMARY KEY (offices_office_code, managers_manager_id) 
);

CREATE INDEX idx_offices_has_managers_id ON office_has_manager(managers_manager_id, offices_office_code);

/*Table structure for table `productline` */

CREATE TABLE productline (
  product_line varchar2(50) NOT NULL,
  code number(10) NOT NULL,
  text_description varchar2(4000) DEFAULT NULL,
  html_description clob,
  image blob,
  created_on date DEFAULT SYSDATE NOT NULL,
  PRIMARY KEY (product_line, code),
  CONSTRAINT unique_product_line UNIQUE(product_line)
) ;

/*Table structure for table `productdetail` */

CREATE TABLE productlinedetail (
  product_line varchar2(50) NOT NULL,
  code number(10) NOT NULL,
  line_capacity varchar2(20) NOT NULL,
  line_type number(1) DEFAULT 0,
  PRIMARY KEY (product_line,code),  
  CONSTRAINT unique_product_line_detail UNIQUE(product_line),
  CONSTRAINT productlinedetail_ibfk_1 FOREIGN KEY (product_line,code) REFERENCES productline (product_line,code),
  CONSTRAINT productlinedetail_ibfk_2 FOREIGN KEY (product_line) REFERENCES productline (product_line)
) ;

/*Table structure for table `product` */

CREATE TABLE product (
  product_id number(10) NOT NULL,
  product_name varchar2(70) DEFAULT NULL,
  product_line varchar2(50) DEFAULT NULL,
  product_scale varchar2(10) DEFAULT NULL,
  product_vendor varchar2(50) DEFAULT NULL,
  product_description clob DEFAULT NULL,
  quantity_in_stock number(5) DEFAULT 0,
  buy_price number(10,2) DEFAULT 0.0,
  msrp number(10,2) DEFAULT 0.0,
  PRIMARY KEY (product_id)
 ,
  CONSTRAINT products_ibfk_1 FOREIGN KEY (product_line) REFERENCES productline (product_line)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "PRODUCT_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE product_seq START WITH 1000000 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER product_seq_tr
 BEFORE INSERT ON product FOR EACH ROW
 WHEN (NEW.product_id IS NULL)
BEGIN
 SELECT product_seq.NEXTVAL INTO :NEW.product_id FROM DUAL;
END;
/

CREATE INDEX product_line ON product (product_line);

/*Table structure for table `order` */

CREATE TABLE "ORDER" (
  order_id number(10) NOT NULL,
  order_date date NOT NULL,
  required_date date NOT NULL,
  shipped_date date DEFAULT NULL,
  status varchar2(15) NOT NULL,
  comments clob,
  customer_number number(10) NOT NULL,
  PRIMARY KEY (order_id)
 ,
  CONSTRAINT orders_ibfk_1 FOREIGN KEY (customer_number) REFERENCES customer (customer_number)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "ORDER_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE order_seq START WITH 1000000 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER order_seq_tr
 BEFORE INSERT ON "ORDER" FOR EACH ROW
 WHEN (NEW.order_id IS NULL)
BEGIN
 SELECT order_seq.NEXTVAL INTO :NEW.order_id FROM DUAL;
END;
/

CREATE INDEX customer_number ON "ORDER" (customer_number);

/*Table structure for table `orderdetail` */

CREATE TABLE orderdetail (
  order_id number(10) NOT NULL,
  product_id number(10) NOT NULL,
  quantity_ordered number(10) NOT NULL,
  price_each number(10,2) NOT NULL,
  order_line_number number(5) NOT NULL,
  PRIMARY KEY (order_id,product_id)
 ,
  CONSTRAINT orderdetails_ibfk_1 FOREIGN KEY (order_id) REFERENCES "ORDER" (order_id),
  CONSTRAINT orderdetails_ibfk_2 FOREIGN KEY (product_id) REFERENCES product (product_id)
) ;

CREATE INDEX product_id ON orderdetail (product_id);

/*Table structure for table `top3product` */

CREATE TABLE top3product (  
  product_id number(10) NOT NULL,
  product_name varchar2(70) DEFAULT NULL,  
  PRIMARY KEY (product_id),  
  CONSTRAINT top3product_ibfk_1 FOREIGN KEY (product_id) REFERENCES product (product_id)
) ;

/*Table structure for table `payment` */

CREATE TABLE payment (
  customer_number number(10) NOT NULL,
  check_number varchar2(50) NOT NULL,
  payment_date timestamp NOT NULL,
  invoice_amount number(10,2) NOT NULL,
  caching_date timestamp DEFAULT NULL,
  PRIMARY KEY (customer_number,check_number),
  CONSTRAINT unique_check_number UNIQUE (check_number),
  CONSTRAINT payments_ibfk_1 FOREIGN KEY (customer_number) REFERENCES customer (customer_number)
) ;

/* Table structure for table 'bank_transaction' */

CREATE TABLE bank_transaction (
  transaction_id number(10) NOT NULL,
  bank_name varchar2(50) NOT NULL,
  bank_iban varchar2(50) NOT NULL,  
  transfer_amount number(10,2) NOT NULL,
  caching_date timestamp DEFAULT SYSTIMESTAMP,
  customer_number number(10) NOT NULL,
  check_number varchar2(50) NOT NULL, 
  PRIMARY KEY (transaction_id),  
  CONSTRAINT bank_transaction_ibfk_1 FOREIGN KEY (customer_number,check_number) REFERENCES payment (customer_number,check_number)
) ;

-- Generate ID using sequence and trigger
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE "BANK_TRANSACTION_SEQ"';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE bank_transaction_seq START WITH 10 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER bank_transaction_seq_tr
 BEFORE INSERT ON bank_transaction FOR EACH ROW
 WHEN (NEW.transaction_id IS NULL)
BEGIN
 SELECT bank_transaction_seq.NEXTVAL INTO :NEW.transaction_id FROM DUAL;
END;
/

COMMIT;

/* USER-DEFINED FUNCTIONS */

CREATE OR REPLACE FUNCTION get_total_sales(
    in_year PLS_INTEGER
) 
RETURN NUMBER
IS
    l_total_sales NUMBER := 0;
BEGIN
    -- get total sales
    SELECT SUM(PRICE_EACH * QUANTITY_ORDERED)
    INTO l_total_sales
    FROM ORDERDETAIL
    INNER JOIN "ORDER" USING (ORDER_ID)
    WHERE STATUS = 'Shipped'
    GROUP BY EXTRACT(YEAR FROM ORDER_DATE)
    HAVING EXTRACT(YEAR FROM ORDER_DATE) = in_year;
    
    -- return the total sales
    RETURN l_total_sales;
END;
/

-- Create Object of your table
BEGIN
   EXECUTE IMMEDIATE 'CREATE TYPE TABLE_RES_OBJ AS OBJECT (SALES FLOAT);';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

--Create a type of your object 
BEGIN
   EXECUTE IMMEDIATE 'CREATE TYPE TABLE_RES AS TABLE OF TABLE_RES_OBJ;';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE OR REPLACE NONEDITIONABLE FUNCTION top_three_sales_per_employee (
    employee_nr IN NUMBER
) RETURN TABLE_RES IS
    table_result TABLE_RES;
BEGIN
    SELECT
        TABLE_RES_OBJ("SYSTEM"."SALE"."SALE") "sales"
    BULK COLLECT
    INTO table_result
    FROM
        "SYSTEM"."SALE"
    WHERE
        employee_nr = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
    ORDER BY
        "SYSTEM"."SALE"."SALE" DESC
    FETCH NEXT 3 ROWS ONLY;

    RETURN table_result;
END;
/
/* END */