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
  employee_number number(20) DEFAULT NULL,  
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

CREATE SEQUENCE sale_seq START WITH 1 INCREMENT BY 1;

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

CREATE SEQUENCE manager_seq START WITH 1 INCREMENT BY 1;

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
  text_description varchar2(4000) DEFAULT NULL,
  html_description clob,
  image blob,
  PRIMARY KEY (product_line)
) ;

/*Table structure for table `product` */

CREATE TABLE product (
  product_id number(10) NOT NULL,
  product_name varchar2(70) NOT NULL,
  product_line varchar2(50) NOT NULL,
  product_scale varchar2(10) NOT NULL,
  product_vendor varchar2(50) NOT NULL,
  product_description clob NOT NULL,
  quantity_in_stock number(5) NOT NULL,
  buy_price number(10,2) NOT NULL,
  msrp number(10,2) NOT NULL,
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

CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;

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

CREATE SEQUENCE order_seq START WITH 1 INCREMENT BY 1;

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

/*Table structure for table `payment` */

CREATE TABLE payment (
  customer_number number(10) NOT NULL,
  check_number varchar2(50) NOT NULL,
  payment_date timestamp NOT NULL,
  invoice_amount number(10,2) NOT NULL,
  caching_date timestamp DEFAULT NULL,
  PRIMARY KEY (customer_number,check_number),
  CONSTRAINT payments_ibfk_1 FOREIGN KEY (customer_number) REFERENCES customer (customer_number)
) ;

COMMIT;
/* END */
