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
  IF OBJECT_ID('token', 'U') IS NOT NULL 
  DROP TABLE token;
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
  CONSTRAINT [office_pk] PRIMARY KEY ([office_code]),
  CONSTRAINT [office_postal_code_uk] UNIQUE ([postal_code])
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
  [monthly_bonus] varchar(500) DEFAULT NULL,
  CONSTRAINT [employee_pk] PRIMARY KEY ([employee_number])
,
  CONSTRAINT [employee_employee_fk] FOREIGN KEY ([reports_to]) REFERENCES employee ([employee_number]),
  CONSTRAINT [employee_office_fk] FOREIGN KEY ([office_code]) REFERENCES office ([office_code])
) ;

DROP SEQUENCE IF EXISTS employee_seq;
GO

CREATE SEQUENCE employee_seq START WITH 100000 INCREMENT BY 10 MINVALUE 100000 MAXVALUE 10000000000;
GO

/*Table structure for table `department` */

CREATE TABLE department (
  [department_id] bigint NOT NULL IDENTITY,  
  [name] varchar(50) NOT NULL,
  [phone] varchar(50) NOT NULL,
  [code] smallint DEFAULT 1,
  [office_code] varchar(10) NOT NULL,
  [topic] varchar(100) DEFAULT NULL,  
  [dep_net_ipv4] varchar(16) DEFAULT NULL, 
  CONSTRAINT [department_pk] PRIMARY KEY ([department_id]),
  CONSTRAINT [department_code_uk] UNIQUE ([code])
,
  CONSTRAINT [department_office_fk] FOREIGN KEY ([office_code]) REFERENCES office ([office_code])
) ;

/*Table structure for table `sale` */
DROP SEQUENCE IF EXISTS sale_seq;
GO

CREATE TABLE sale (
  [sale_id] bigint NOT NULL IDENTITY,  
  [fiscal_year] int NOT NULL,  
  [sale] float NOT NULL,  
  [employee_number] bigint DEFAULT NULL,  
  [hot] bit DEFAULT 0,  
  [rate] varchar(10) DEFAULT NULL,
  [vat] varchar(10) DEFAULT NULL,
  [trend] varchar(10) DEFAULT NULL,  
  CONSTRAINT [sale_pk] PRIMARY KEY ([sale_id])
,    
  CONSTRAINT [sale_employee_fk] FOREIGN KEY ([employee_number]) REFERENCES employee ([employee_number]) ON UPDATE CASCADE,
  CONSTRAINT [enum_rate_check] CHECK ([rate] IN('SILVER', 'GOLD', 'PLATINUM')),
  CONSTRAINT [enum_vat_check] CHECK ([vat] IN('NONE', 'MIN', 'MAX'))
) ;

CREATE TABLE [token] (
  [token_id] bigint NOT NULL IDENTITY,
  [sale_id] bigint NOT NULL,
  [amount] float NOT NULL,    
  [updated_on] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT [token_pk] PRIMARY KEY ([token_id])
 ,  
  CONSTRAINT [token_sale_fk] FOREIGN KEY ([sale_id]) REFERENCES sale ([sale_id]) ON DELETE CASCADE ON UPDATE CASCADE
) ;

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
  CONSTRAINT [customer_pk] PRIMARY KEY ([customer_number]),
  CONSTRAINT [customer_name_uk] UNIQUE ([customer_name])
 ,
  CONSTRAINT [customer_employee_fk] FOREIGN KEY ([sales_rep_employee_number]) REFERENCES employee ([employee_number]) ON UPDATE CASCADE
) ;

/* Table structure for table `customerdetail` */
CREATE TABLE customerdetail (
  [customer_number] bigint NOT NULL,
  [address_line_first] varchar(50) NOT NULL,
  [address_line_second] varchar(50) DEFAULT NULL,
  [city] varchar(50),
  [state] varchar(50) DEFAULT NULL,
  [postal_code] varchar(15) DEFAULT NULL,
  [country] varchar(50),
  CONSTRAINT [customerdetail_pk] PRIMARY KEY ([customer_number]),
  CONSTRAINT [customerdetail_address_line_first_uk] UNIQUE ([address_line_first])
  ,
  CONSTRAINT [customerdetail_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
) ; 

/*Table structure for table `manager` */

CREATE TABLE manager (
  [manager_id] bigint NOT NULL IDENTITY,
  [manager_name] varchar(50) NOT NULL,
  [manager_detail] nvarchar(4000),
  [manager_evaluation] varchar(200) DEFAULT NULL, 
  CONSTRAINT [manager_pk] PRIMARY KEY ([manager_id]),
  CONSTRAINT ENSURE_JSON CHECK(ISJSON([manager_detail]) = 1)
) ;

/*Table structure for table `office_has_manager` */

CREATE TABLE office_has_manager (
  [offices_office_code] varchar(10) NOT NULL,
  [managers_manager_id] bigint NOT NULL,
  CONSTRAINT office_manager_uk UNIQUE (offices_office_code, managers_manager_id),
  CONSTRAINT [office_fk] FOREIGN KEY ([offices_office_code]) REFERENCES office ([office_code]) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT [manager_fk] FOREIGN KEY ([managers_manager_id]) REFERENCES manager ([manager_id]) ON UPDATE NO ACTION ON DELETE NO ACTION  
);

/*Table structure for table `productline` */

CREATE TABLE productline (
  [product_line] varchar(50) NOT NULL,
  [code] bigint NOT NULL,
  [text_description] varchar(4000) DEFAULT NULL,
  [html_description] xml,
  [image] varbinary(max),
  [created_on] date DEFAULT GETDATE(),
  CONSTRAINT [productline_pk] PRIMARY KEY ([product_line],[code]),
  CONSTRAINT [productline_uk] UNIQUE([product_line])
);

/*Table structure for table `productdetail` */

CREATE TABLE productlinedetail (
  [product_line] varchar(50) NOT NULL,
  [code] bigint NOT NULL,
  [line_capacity] varchar(20) NOT NULL,
  [line_type] int DEFAULT 0,
  CONSTRAINT [productlinedetail_uk] UNIQUE([product_line]),
  CONSTRAINT [productlinedetail_pk] PRIMARY KEY ([product_line],[code]),    
  CONSTRAINT [productlinedetail_productline_fk] FOREIGN KEY ([product_line],[code]) REFERENCES productline ([product_line],[code])  
);

/*Table structure for table `product` */

CREATE SEQUENCE product_uid_seq START WITH 10 INCREMENT BY 10;
GO

CREATE TABLE product (
  [product_id] bigint NOT NULL IDENTITY,
  [product_name] varchar(70) DEFAULT NULL,
  [product_line] varchar(50) DEFAULT NULL,
  [code] bigint NOT NULL,
  [product_scale] varchar(10) DEFAULT NULL,
  [product_vendor] varchar(50) DEFAULT NULL,
  [product_description] varchar(max) DEFAULT NULL,
  [quantity_in_stock] smallint DEFAULT 0,
  [buy_price] decimal(10,2) DEFAULT 0.0,
  [specs] varchar(max) DEFAULT NULL,
  [msrp] decimal(10,2) DEFAULT 0.0,
  [product_uid] bigint NOT NULL DEFAULT (NEXT VALUE FOR product_uid_seq),
  CONSTRAINT [product_pk] PRIMARY KEY ([product_id])
 ,
  CONSTRAINT [product_productline_fk] FOREIGN KEY ([product_line],[code]) REFERENCES productline ([product_line],[code])
) ;

/*Table structure for table `order` */

CREATE TABLE [order] (
  [order_id] bigint NOT NULL IDENTITY,
  [order_date] date NOT NULL,
  [required_date] date NOT NULL,
  [shipped_date] date DEFAULT NULL,
  [status] varchar(15) NOT NULL,
  [comments] varchar(max),
  [customer_number] bigint NOT NULL,
  CONSTRAINT [order_pk] PRIMARY KEY ([order_id])
 ,
  CONSTRAINT [order_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
) ;

/*Table structure for table `orderdetail` */

CREATE TABLE orderdetail (
  [orderdetail_id] bigint NOT NULL IDENTITY,
  [order_id] bigint NOT NULL,
  [product_id] bigint NOT NULL,
  [quantity_ordered] int NOT NULL,
  [price_each] decimal(10,2) NOT NULL,
  [order_line_number] smallint NOT NULL,
  CONSTRAINT [orderdetail_pk] PRIMARY KEY ([orderdetail_id])
 ,
  CONSTRAINT [orderdetail_order_fk] FOREIGN KEY ([order_id]) REFERENCES [order] ([order_id]),
  CONSTRAINT [orderdetail_product_fk] FOREIGN KEY ([product_id]) REFERENCES product ([product_id])
) ;

/*Table structure for table `top3product` */

CREATE TABLE top3product (  
  product_id bigint NOT NULL,
  product_name varchar(70) DEFAULT NULL,  
  CONSTRAINT [top3product_pk] PRIMARY KEY (product_id),  
  CONSTRAINT [top3product_product_fk] FOREIGN KEY (product_id) REFERENCES product (product_id)
) ;

/*Table structure for table `payment` */

CREATE TABLE payment (
  [customer_number] bigint NOT NULL,
  [check_number] varchar(50) NOT NULL,
  [payment_date] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [invoice_amount] decimal(10,2) NOT NULL,
  [caching_date] datetime DEFAULT NULL,
  [version] int NOT NULL DEFAULT 0,
  [modified] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT [payment_pk] PRIMARY KEY ([customer_number],[check_number]),
  CONSTRAINT [check_number_uk] UNIQUE([check_number]),
  CONSTRAINT [payment_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES customer ([customer_number])
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
  [status] varchar(50) NOT NULL DEFAULT 'SUCCESS',
  CONSTRAINT [bank_transaction_pk] PRIMARY KEY ([transaction_id]),  
  CONSTRAINT [bank_transaction_customer_fk] FOREIGN KEY ([customer_number],[check_number]) REFERENCES payment ([customer_number],[check_number])
) ;
GO

-- VIEWS
CREATE VIEW customer_master AS
SELECT [classicmodels].[dbo].[customer].[customer_name],
       [classicmodels].[dbo].[customer].[credit_limit],
       [classicmodels].[dbo].[customerdetail].[city],
       [classicmodels].[dbo].[customerdetail].[country],
       [classicmodels].[dbo].[customerdetail].[address_line_first],
       [classicmodels].[dbo].[customerdetail].[postal_code],
       [classicmodels].[dbo].[customerdetail].[state]
FROM [classicmodels].[dbo].[customer]
JOIN [classicmodels].[dbo].[customerdetail] ON [classicmodels].[dbo].[customerdetail].[customer_number] = [classicmodels].[dbo].[customer].[customer_number]
WHERE [classicmodels].[dbo].[customer].[first_buy_date] IS NOT NULL;
GO

CREATE VIEW office_master AS
SELECT [classicmodels].[dbo].[office].[office_code],
       [classicmodels].[dbo].[office].[city],
       [classicmodels].[dbo].[office].[country],
       [classicmodels].[dbo].[office].[state],
       [classicmodels].[dbo].[office].[phone],
	   [classicmodels].[dbo].[office].[postal_code]
FROM [classicmodels].[dbo].[office]
WHERE [classicmodels].[dbo].[office].[city] IS NOT NULL;

CREATE OR REPLACE VIEW product_master AS
SELECT [classicmodels].[dbo].[product].[product_line],
       [classicmodels].[dbo].[product].[product_name],
       [classicmodels].[dbo].[product].[product_scale]       
FROM [classicmodels].[dbo].[product];
/* END */