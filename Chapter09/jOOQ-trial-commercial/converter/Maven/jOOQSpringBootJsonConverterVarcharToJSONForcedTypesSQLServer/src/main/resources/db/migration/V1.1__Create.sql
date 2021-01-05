/*
*********************************************************************
http://www.mysqltutorial.org
*********************************************************************
Name: MySQL Sample Database classicmodels
Link: http://www.mysqltutorial.org/mysql-sample-database.aspx
*********************************************************************

This is a modified version of the original schema for Microsoft Server SQL
*/

/* START */

/* USER-DEFINED FUNCTIONS */

CREATE OR ALTER FUNCTION netPriceEach(
    @quantity INT,
    @list_price DEC(10,2),
    @discount DEC(4,2)
)
RETURNS DEC(10,2)
AS 
BEGIN
    RETURN @quantity * @list_price * (1 - @discount);
END;
GO

CREATE OR ALTER FUNCTION top_three_sales_per_employee(@employee_nr BIGINT)
RETURNS @out_table TABLE (
  sales FLOAT
)
AS BEGIN
  INSERT @out_table
  SELECT 
      TOP 3 [classicmodels].[dbo].[sale].[sale] [sales] 
    FROM 
      [classicmodels].[dbo].[sale] 
    WHERE 
      @employee_nr = [classicmodels].[dbo].[sale].[employee_number] 
    ORDER BY 
      [classicmodels].[dbo].[sale].[sale] DESC
    RETURN
END;	
GO

IF OBJECT_ID('payment', 'U') IS NOT NULL 
  DROP TABLE payment;  
IF OBJECT_ID('bank_transaction', 'U') IS NOT NULL 
  DROP TABLE bank_transaction;  
IF OBJECT_ID('orderdetail', 'U') IS NOT NULL 
  DROP TABLE orderdetail;
IF OBJECT_ID('order', 'U') IS NOT NULL 
  DROP TABLE [order];
IF OBJECT_ID('product', 'U') IS NOT NULL 
  DROP TABLE product;
IF OBJECT_ID('productline', 'U') IS NOT NULL 
  DROP TABLE productline;
IF OBJECT_ID('top3product', 'U') IS NOT NULL 
  DROP TABLE top3product;
IF OBJECT_ID('productlinedetail', 'U') IS NOT NULL 
  DROP TABLE productlinedetail;
IF OBJECT_ID('office_has_manager', 'U') IS NOT NULL 
  DROP TABLE office_has_manager;
IF OBJECT_ID('manager', 'U') IS NOT NULL 
  DROP TABLE manager;
IF OBJECT_ID('customerdetail', 'U') IS NOT NULL 
  DROP TABLE customerdetail;
IF OBJECT_ID('customer', 'U') IS NOT NULL 
  DROP TABLE customer;
IF OBJECT_ID('sale', 'U') IS NOT NULL 
  DROP TABLE sale;
IF OBJECT_ID('employee', 'U') IS NOT NULL 
  DROP TABLE employee;
  IF OBJECT_ID('department', 'U') IS NOT NULL 
  DROP TABLE department;
IF OBJECT_ID('office', 'U') IS NOT NULL 
  DROP TABLE office;

/*Table structure for table `office` */

CREATE TABLE office (
  [office_code] varchar(10) NOT NULL,
  [city] varchar(50),
  [phone] varchar(50) NOT NULL,
  [address_line_first] varchar(50) NOT NULL,
  [address_line_second] varchar(50) DEFAULT NULL,
  [state] varchar(50) DEFAULT NULL,
  [country] varchar(50),
  [postal_code] varchar(15) NOT NULL,
  [territory] varchar(10) NOT NULL,
  [location] [geometry] DEFAULT NULL,
  PRIMARY KEY ([office_code])
) ;

/*Table structure for table `employee` */

CREATE TABLE employee (
  [employee_number] bigint NOT NULL,
  [last_name] varchar(50) NOT NULL,
  [first_name] varchar(50) NOT NULL,
  [extension] varchar(10) NOT NULL,
  [email] varchar(100) NOT NULL,
  [office_code] varchar(10) NOT NULL,
  [salary] int NOT NULL,
  [reports_to] bigint DEFAULT NULL,
  [job_title] varchar(50) NOT NULL,
  [employee_of_year] varchar(50) DEFAULT NULL,
  PRIMARY KEY ([employee_number])
,
  CONSTRAINT [employees_ibfk_1] FOREIGN KEY ([reports_to]) REFERENCES employee ([employee_number]),
  CONSTRAINT [employees_ibfk_2] FOREIGN KEY ([office_code]) REFERENCES office ([office_code])
) ;

CREATE INDEX [reports_to] ON employee ([reports_to]);
CREATE INDEX [office_code] ON employee ([office_code]);

/*Table structure for table `department` */

CREATE TABLE department (
  [department_id] bigint NOT NULL IDENTITY,  
  [name] varchar(50) NOT NULL,
  [phone] varchar(50) NOT NULL,
  [code] smallint DEFAULT 1,
  [office_code] varchar(10) NOT NULL,
  [topic] varchar(100) NOT NULL,  
  [dep_net_ipv4] varchar(16) DEFAULT NULL,
  PRIMARY KEY ([department_id])
,
  CONSTRAINT [department_ibfk_1] FOREIGN KEY ([office_code]) REFERENCES office ([office_code])
) ;

CREATE INDEX [department_id] ON department ([department_id]);

/*Table structure for table `sale` */
CREATE TABLE sale (
  [sale_id] bigint NOT NULL IDENTITY,  
  [fiscal_year] int NOT NULL,  
  [sale] float NOT NULL,  
  [employee_number] bigint DEFAULT NULL,  
  [hot] bit DEFAULT 0,  
  [rate] varchar(10) DEFAULT NULL CHECK ([rate] IN('SILVER', 'GOLD', 'PLATINUM')),
  [vat] varchar(10) DEFAULT NULL CHECK ([vat] IN('NONE', 'MIN', 'MAX')),
  [trend] varchar(10) DEFAULT NULL,
  PRIMARY KEY ([sale_id])
,    
  CONSTRAINT [sales_ibfk_1] FOREIGN KEY ([employee_number]) REFERENCES employee ([employee_number])
) ;

CREATE INDEX [employee_number] ON sale ([employee_number]);

/*Table structure for table `customer` */

CREATE TABLE customer (
  [customer_number] bigint NOT NULL IDENTITY,
  [customer_name] varchar(50) NOT NULL,
  [contact_last_name] varchar(50) NOT NULL,
  [contact_first_name] varchar(50) NOT NULL,
  [phone] varchar(50) NOT NULL,
  [sales_rep_employee_number] bigint DEFAULT NULL,
  [credit_limit] decimal(10,2) DEFAULT NULL,
  [first_buy_date] int DEFAULT NULL,
  PRIMARY KEY ([customer_number])
 ,
  CONSTRAINT [customers_ibfk_1] FOREIGN KEY ([sales_rep_employee_number]) REFERENCES employee ([employee_number])
) ;

CREATE INDEX [sales_rep_employee_number] ON customer ([sales_rep_employee_number]);

/* Table structure for table `customerdetail` */
CREATE TABLE customerdetail (
  [customer_number] bigint NOT NULL,
  [address_line_first] varchar(50) NOT NULL,
  [address_line_second] varchar(50) DEFAULT NULL,
  [city] varchar(50),
  [state] varchar(50) DEFAULT NULL,
  [postal_code] varchar(15) DEFAULT NULL,
  [country] varchar(50),
PRIMARY KEY ([customer_number])
 ,
 CONSTRAINT [customers_details_ibfk_1] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
) ; 

/*Table structure for table `manager` */

CREATE TABLE manager (
  [manager_id] bigint NOT NULL IDENTITY,
  [manager_name] varchar(50) NOT NULL,
  [manager_detail] nvarchar(4000) CHECK(ISJSON(manager_detail) = 1),
  [manager_evaluation] varchar(200) DEFAULT NULL, 
  PRIMARY KEY ([manager_id])
) ;

/*Table structure for table `office_has_manager` */

CREATE TABLE office_has_manager (
  [offices_office_code] varchar(10) REFERENCES office ([office_code]) ON UPDATE NO ACTION ON DELETE NO ACTION,
  [managers_manager_id] bigint REFERENCES manager ([manager_id]) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT [offices_managers_pkey] PRIMARY KEY ([offices_office_code], [managers_manager_id]) 
);

CREATE INDEX [idx_offices_has_managers_id] ON office_has_manager([managers_manager_id], [offices_office_code]);

/*Table structure for table `productline` */

CREATE TABLE productline (
  [product_line] varchar(50) NOT NULL,
  [code] bigint NOT NULL,
  [text_description] varchar(4000) DEFAULT NULL,
  [html_description] varchar(max),
  [image] varbinary(max),
  [created_on] date DEFAULT GETDATE(),
  PRIMARY KEY ([product_line],[code]),
  CONSTRAINT [unique_product_line] UNIQUE([product_line])
);

/*Table structure for table `productdetail` */

CREATE TABLE productlinedetail (
  [product_line] varchar(50) NOT NULL,
  [code] bigint NOT NULL,
  [line_capacity] varchar(20) NOT NULL,
  [line_type] int DEFAULT 0,
  PRIMARY KEY ([product_line],[code]),  
  CONSTRAINT [unique_product_line_detail] UNIQUE([product_line]),
  CONSTRAINT [productlinedetail_ibfk_1] FOREIGN KEY ([product_line],[code]) REFERENCES productline ([product_line],[code]),
  CONSTRAINT [productlinedetail_ibfk_2] FOREIGN KEY ([product_line]) REFERENCES productline ([product_line])
);

/*Table structure for table `product` */

CREATE TABLE product (
  [product_id] bigint NOT NULL IDENTITY,
  [product_name] varchar(70) DEFAULT NULL,
  [product_line] varchar(50) DEFAULT NULL,
  [product_scale] varchar(10) DEFAULT NULL,
  [product_vendor] varchar(50) DEFAULT NULL,
  [product_description] varchar(max) DEFAULT NULL,
  [quantity_in_stock] smallint DEFAULT 0,
  [buy_price] decimal(10,2) DEFAULT 0.0,
  [specs] varchar(max) DEFAULT NULL,
  [msrp] decimal(10,2) DEFAULT 0.0,
  PRIMARY KEY ([product_id])
 ,
  CONSTRAINT [products_ibfk_1] FOREIGN KEY ([product_line]) REFERENCES productline ([product_line])
) ;

CREATE INDEX [product_line] ON product ([product_line]);

/*Table structure for table `order` */

CREATE TABLE [order] (
  [order_id] bigint NOT NULL IDENTITY,
  [order_date] date NOT NULL,
  [required_date] date NOT NULL,
  [shipped_date] date DEFAULT NULL,
  [status] varchar(15) NOT NULL,
  [comments] varchar(max),
  [customer_number] bigint NOT NULL,
  PRIMARY KEY ([order_id])
 ,
  CONSTRAINT [orders_ibfk_1] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
) ;

CREATE INDEX [customer_number] ON [order] ([customer_number]);

/*Table structure for table `orderdetail` */

CREATE TABLE orderdetail (
  [order_id] bigint NOT NULL,
  [product_id] bigint NOT NULL,
  [quantity_ordered] int NOT NULL,
  [price_each] decimal(10,2) NOT NULL,
  [order_line_number] smallint NOT NULL,
  PRIMARY KEY ([order_id],[product_id])
 ,
  CONSTRAINT [orderdetails_ibfk_1] FOREIGN KEY ([order_id]) REFERENCES [order] ([order_id]),
  CONSTRAINT [orderdetails_ibfk_2] FOREIGN KEY ([product_id]) REFERENCES product ([product_id])
) ;

CREATE INDEX [product_id] ON orderdetail ([product_id]);

/*Table structure for table `top3product` */

CREATE TABLE top3product (  
  product_id bigint NOT NULL,
  product_name varchar(70) DEFAULT NULL,  
  PRIMARY KEY (product_id),  
  CONSTRAINT top3product_ibfk_1 FOREIGN KEY (product_id) REFERENCES product (product_id)
) ;

/*Table structure for table `payment` */

CREATE TABLE payment (
  [customer_number] bigint NOT NULL,
  [check_number] varchar(50) NOT NULL,
  [payment_date] datetime NOT NULL,
  [invoice_amount] decimal(10,2) NOT NULL,
  [caching_date] datetime DEFAULT NULL,
  PRIMARY KEY ([customer_number],[check_number]),
  CONSTRAINT [unique_check_number] UNIQUE([check_number]),
  CONSTRAINT [payments_ibfk_1] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
) ;

/* Table structure for table 'bank_transaction' */

CREATE TABLE bank_transaction (
  [transaction_id] bigint NOT NULL IDENTITY,
  [bank_name] varchar(50) NOT NULL,
  [bank_iban] varchar(50) NOT NULL,  
  [transfer_amount] decimal(10,2) NOT NULL,
  [caching_date] datetime DEFAULT GETDATE(),
  [customer_number] bigint NOT NULL,
  [check_number] varchar(50) NOT NULL, 
  PRIMARY KEY ([transaction_id]),  
  CONSTRAINT [bank_transaction_ibfk_1] FOREIGN KEY ([customer_number],[check_number]) REFERENCES payment ([customer_number],[check_number])
) ;

/* END */