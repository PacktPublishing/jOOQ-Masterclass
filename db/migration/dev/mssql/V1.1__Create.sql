/* START */

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO 

/* DROP FUNCTIONS AND PROCEDURES */

IF OBJECT_ID('split_part') IS NOT NULL 
  DROP FUNCTION [split_part];   
IF OBJECT_ID('sale_price') IS NOT NULL 
  DROP FUNCTION [sale_price];   
IF OBJECT_ID('top_three_sales_per_employee') IS NOT NULL 
  DROP FUNCTION [top_three_sales_per_employee]; 
IF OBJECT_ID('product_of_product_line') IS NOT NULL 
  DROP FUNCTION [product_of_product_line];     
IF OBJECT_ID('get_product') IS NOT NULL 
  DROP PROCEDURE [get_product];      
IF OBJECT_ID('refresh_top3_product') IS NOT NULL 
  DROP PROCEDURE [refresh_top3_product];  
IF OBJECT_ID('get_emps_in_office') IS NOT NULL 
  DROP PROCEDURE [get_emps_in_office];  
IF OBJECT_ID('customer_master') IS NOT NULL 
  DROP VIEW [customer_master];  
IF OBJECT_ID('office_master') IS NOT NULL 
  DROP VIEW [office_master];    
IF OBJECT_ID('product_master') IS NOT NULL 
  DROP VIEW [product_master];  
  GO

/* USER-DEFINED FUNCTIONS */

CREATE OR ALTER FUNCTION [split_part] (@tstr VARCHAR(2000), @sstr VARCHAR(1), @occ INT)
   RETURNS VARCHAR(1024)
AS BEGIN
  DECLARE @cpos INT, @pos INT, @cnt INT, @ret VARCHAR(1000), @tstrlen INT, @tocc INT
	SET @tstrlen = LEN (@tstr)
	SET @tocc = DATALENGTH(@tstr)-DATALENGTH(REPLACE(@tstr,@sstr,''))
	iF @tstrlen = 0
		RETURN(@ret)
	ELSE
	BEGIN
		SET @pos = CHARINDEX(@sstr, @tstr,  1)
		IF @pos = 0
			RETURN(@ret)
		ELSE
		BEGIN
			SET @cnt = 1		
			IF @occ = 1 
				SET @ret = LEFT(@tstr, @pos -1) 
			ELSE
			BEGIN
				WHILE (@cnt < @occ)
				BEGIN
					SET @cpos = CHARINDEX(@sstr, @tstr, @pos + 1)
					SET @cnt = @cnt + 1
					IF @cpos =0
						SET @ret= SUBSTRING (@tstr, @pos +1, @tstrlen) 
					ELSE
						SET @ret = SUBSTRING(@tstr, @pos+1, @cpos-@pos-1)
					SET @pos = @cpos	
				END
					IF (@cnt > @tocc+1)
					SET @ret = ''
			END
		END
	END
	RETURN(@ret)
END
GO

CREATE OR ALTER FUNCTION [sale_price](
    @quantity INT, @list_price REAL, @fraction_of_price REAL)
RETURNS REAL
AS BEGIN
  RETURN (@list_price - (@list_price * @fraction_of_price)) * @quantity;
END;
GO

CREATE OR ALTER FUNCTION [top_three_sales_per_employee](@employee_nr BIGINT)
  RETURNS @out_table TABLE (
    [sales] FLOAT
  )
AS BEGIN
  INSERT @out_table
  SELECT 
    TOP 3 [sale].[sale] [sales] 
  FROM 
    [sale] 
  WHERE 
    @employee_nr = [sale].[employee_number] 
  ORDER BY 
    [sale].[sale] DESC
	
  RETURN
END;	
GO

CREATE OR ALTER FUNCTION [product_of_product_line](@p_line_in VARCHAR(50))
  RETURNS @out_table TABLE (p_id BIGINT, p_name VARCHAR(70), p_line VARCHAR(50)) 
AS BEGIN
  INSERT @out_table
    SELECT 
      [product].[product_id] AS "p_id",
      [product].[product_name] AS "p_name",
      [product].[product_line] AS "p_line"
    FROM 
      [product] 
    WHERE 
      @p_line_in = [product].[product_line];  
	  
  RETURN	  
END; 
GO

-- USER-DEFINED AGG FUNCTION
EXEC sp_configure 'show advanced options', 1
GO  
RECONFIGURE;
GO  
EXEC sp_configure 'clr strict security', 0;
GO  
RECONFIGURE;
GO  

DROP ASSEMBLY IF EXISTS [StringUtilities];
GO
CREATE ASSEMBLY [StringUtilities] FROM 'C:\SBPBP\GitHub\Up-and-Running-with-jOOQ\Chapter15\SQLServer\StringUtilities.dll'  
GO  
  
CREATE AGGREGATE [concatenate](@input NVARCHAR(4000))  
RETURNS NVARCHAR(4000)  
EXTERNAL NAME [StringUtilities].[Microsoft.Samples.SqlServer.Concatenate];  
GO  

/* USER-DEFINED PROCEDURES */

CREATE PROCEDURE [get_product](@pid BIGINT)
AS BEGIN
  SELECT * FROM [product] WHERE [product].[product_id] = @pid;
END;
GO

CREATE OR ALTER PROCEDURE [refresh_top3_product](@p_line_in VARCHAR(50))
AS BEGIN
  DELETE FROM [top3product]; 
  INSERT INTO [top3product]([top3product].[product_id], [top3product].[product_name])        
    SELECT [t].[product_id], [t].[product_name] FROM (
      SELECT TOP 3 [orderdetail].[product_id], [product].[product_name], max([orderdetail].[quantity_ordered]) AS qo
      FROM [orderdetail]
      JOIN [product]
      ON [orderdetail].[product_id] = [product].[product_id]
      AND @p_line_in = [product].[product_line]
      GROUP BY [orderdetail].[product_id], [product].[product_name]) AS t
      ORDER BY [t].[qo];         
END;
GO

CREATE PROCEDURE [get_emps_in_office](@in_office_code VARCHAR(10))
AS BEGIN
  SELECT [office].[city], [office].[country], [office].[internal_budget]
  FROM [office]
  WHERE [office].[office_code]=@in_office_code;

  SELECT [employee].[employee_number], [employee].[first_name], [employee].[last_name]
  FROM [employee]
  WHERE [employee].[office_code]=@in_office_code;
END;
GO

/* DROP AND CREATE TABLES */

IF OBJECT_ID('payment', 'U') IS NOT NULL 
  DROP TABLE [payment];  
IF OBJECT_ID('bank_transaction', 'U') IS NOT NULL 
  DROP TABLE [bank_transaction];  
IF OBJECT_ID('orderdetail', 'U') IS NOT NULL 
  DROP TABLE [orderdetail];
IF OBJECT_ID('order', 'U') IS NOT NULL 
  DROP TABLE [order];
IF OBJECT_ID('product', 'U') IS NOT NULL 
  DROP TABLE [product];
IF OBJECT_ID('productline', 'U') IS NOT NULL 
  DROP TABLE [productline];
IF OBJECT_ID('top3product', 'U') IS NOT NULL 
  DROP TABLE [top3product];
IF OBJECT_ID('productlinedetail', 'U') IS NOT NULL 
  DROP TABLE [productlinedetail];
IF OBJECT_ID('office_has_manager', 'U') IS NOT NULL 
  DROP TABLE [office_has_manager];
IF OBJECT_ID('manager', 'U') IS NOT NULL 
  DROP TABLE [manager];
IF OBJECT_ID('customerdetail', 'U') IS NOT NULL 
  DROP TABLE [customerdetail];
IF OBJECT_ID('customer', 'U') IS NOT NULL 
  DROP TABLE [customer];
IF OBJECT_ID('sale', 'U') IS NOT NULL 
  DROP TABLE [sale];
IF OBJECT_ID('daily_activity', 'U') IS NOT NULL 
  DROP TABLE [daily_activity];
IF OBJECT_ID('token', 'U') IS NOT NULL 
  DROP TABLE [token];
IF OBJECT_ID('employee', 'U') IS NOT NULL 
  DROP TABLE [employee];
IF OBJECT_ID('employee_status', 'U') IS NOT NULL 
  DROP TABLE [employee_status];
  IF OBJECT_ID('department', 'U') IS NOT NULL 
  DROP TABLE [department];
IF OBJECT_ID('office', 'U') IS NOT NULL 
  DROP TABLE [office];
IF OBJECT_ID('office_flights', 'U') IS NOT NULL 
  DROP TABLE [office_flights];  
  
DROP SEQUENCE IF EXISTS [employee_seq];
DROP SEQUENCE IF EXISTS [product_uid_seq];

-- TABLE OFFICE

CREATE TABLE [office] (
  [office_code]         VARCHAR(10) NOT NULL,
  [city]                VARCHAR(50) DEFAULT NULL,
  [phone]               VARCHAR(50) NOT NULL,
  [address_line_first]  VARCHAR(50) NOT NULL,
  [address_line_second] VARCHAR(50) DEFAULT NULL,
  [state]               VARCHAR(50) DEFAULT NULL,
  [country]             VARCHAR(50) DEFAULT NULL,
  [postal_code]         VARCHAR(15) NOT NULL,
  [territory]           VARCHAR(10) NOT NULL,
  [location]            GEOMETRY    DEFAULT NULL,
  [internal_budget]     INT         NOT NULL,
  CONSTRAINT [office_pk] PRIMARY KEY ([office_code]),
  CONSTRAINT [office_postal_code_uk] UNIQUE ([postal_code])
);

-- TABLE EMPLOYEE

CREATE TABLE [employee] (
  [employee_number]  BIGINT       NOT NULL,
  [last_name]        VARCHAR(50)  NOT NULL,
  [first_name]       VARCHAR(50)  NOT NULL,
  [extension]        VARCHAR(10)  NOT NULL,
  [email]            VARCHAR(100) NOT NULL,
  [office_code]      VARCHAR(10)  NOT NULL,
  [salary]           INT          NOT NULL,
  [commission]       INT          DEFAULT NULL,
  [reports_to]       BIGINT       DEFAULT NULL,
  [job_title]        VARCHAR(50)  NOT NULL,
  [employee_of_year] VARCHAR(500) DEFAULT NULL,
  [monthly_bonus]    VARCHAR(500) DEFAULT NULL,
  CONSTRAINT [employee_pk] PRIMARY KEY ([employee_number]),
  CONSTRAINT [employee_employee_fk] FOREIGN KEY ([reports_to]) REFERENCES [employee] ([employee_number]),
  CONSTRAINT [employee_office_fk] FOREIGN KEY ([office_code]) REFERENCES [office] ([office_code])
);

-- this sequence is not used automatically
CREATE SEQUENCE [employee_seq] START WITH 100000 INCREMENT BY 10 MINVALUE 100000 MAXVALUE 10000000000;
GO

-- TABLE EMPLOYEE_STATUS

CREATE TABLE [employee_status] (
  [id]              BIGINT      NOT NULL IDENTITY,
  [employee_number] BIGINT      NOT NULL,  
  [status]          VARCHAR(50) NOT NULL,  
  [acquired_date]   DATE        NOT NULL,
  CONSTRAINT [id_pk] PRIMARY KEY ([id]),  
  CONSTRAINT [employee_status_employee_fk] FOREIGN KEY ([employee_number]) REFERENCES [employee] ([employee_number])
);

-- TABLE DEPARTMENT

CREATE TABLE [department] (
  [department_id]       BIGINT       NOT NULL IDENTITY,  
  [name]                VARCHAR(50)  NOT NULL,
  [phone]               VARCHAR(50)  NOT NULL,
  [code]                INT          DEFAULT 1,
  [office_code]         VARCHAR(10)  NOT NULL,
  [topic]               VARCHAR(500) DEFAULT NULL,  
  [dep_net_ipv4]        VARCHAR(16)  DEFAULT NULL, -- or, binary(4)
  [local_budget]        FLOAT        DEFAULT NULL,
  [profit]              FLOAT        DEFAULT NULL,
  [forecast_profit]     FLOAT        DEFAULT NULL,
  [cash]                FLOAT        DEFAULT NULL,
  [accounts_receivable] FLOAT        DEFAULT NULL,
  [inventories]         FLOAT        DEFAULT NULL,
  [accounts_payable]    FLOAT        DEFAULT NULL,
  [st_borrowing]        FLOAT        DEFAULT NULL,
  [accrued_liabilities] FLOAT        DEFAULT NULL,
  CONSTRAINT [department_pk] PRIMARY KEY ([department_id]),
  CONSTRAINT [department_code_uk] UNIQUE ([code]),
  CONSTRAINT [department_office_fk] FOREIGN KEY ([office_code]) REFERENCES [office] ([office_code])
);

-- TABLE SALE

CREATE TABLE [sale] (
  [sale_id]         BIGINT      NOT NULL IDENTITY,  -- in PostgreSQL/Oracle this PK is a sequence
  [fiscal_year]     INT         NOT NULL,  
  [sale]            FLOAT       NOT NULL,  
  [employee_number] BIGINT      DEFAULT NULL,  
  [hot]             BIT         DEFAULT 0 CHECK ([hot] IN(1, 0)),  
  [rate]            VARCHAR(10) DEFAULT NULL CHECK ([rate] IN('SILVER', 'GOLD', 'PLATINUM')),
  [vat]             VARCHAR(10) DEFAULT NULL CHECK ([vat] IN('NONE', 'MIN', 'MAX')),
  [fiscal_month]    INT         NOT NULL,
  [revenue_growth]  FLOAT       NOT NULL,
  [trend]           VARCHAR(10) DEFAULT NULL,  
  CONSTRAINT [sale_pk] PRIMARY KEY ([sale_id]),    
  CONSTRAINT [sale_employee_fk] FOREIGN KEY ([employee_number]) REFERENCES [employee] ([employee_number]) ON UPDATE CASCADE
);

-- TABLE DAILY_ACTIVITY

CREATE TABLE [daily_activity] (
  [day_id]     BIGINT NOT NULL IDENTITY, 
  [day_date]   DATE   NOT NULL,
  [sales]      FLOAT  NOT NULL,  
  [visitors]   FLOAT  NOT NULL,    
  [conversion] FLOAT  NOT NULL,
  CONSTRAINT [daily_activity_pk] PRIMARY KEY ([day_id])
);

-- TABLE TOKEN

CREATE TABLE [token] (
  [token_id]   BIGINT   NOT NULL IDENTITY, -- in PostgreSQL/Oracle this PK is a sequence
  [sale_id]    BIGINT   NOT NULL,
  [amount]     FLOAT    NOT NULL,    
  [updated_on] DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT [token_pk] PRIMARY KEY ([token_id]),  
  CONSTRAINT [token_sale_fk] FOREIGN KEY ([sale_id]) REFERENCES [sale] ([sale_id]) ON DELETE CASCADE ON UPDATE CASCADE
) ;

-- TABLE CUSTOMER

CREATE TABLE [customer] (
  [customer_number]           BIGINT        NOT NULL IDENTITY, -- in PostgreSQL/Oracle this PK is a sequence
  [customer_name]             VARCHAR(50)   NOT NULL,
  [contact_last_name]         VARCHAR(50)   NOT NULL,
  [contact_first_name]        VARCHAR(50)   NOT NULL,
  [phone]                     VARCHAR(50)   NOT NULL,
  [sales_rep_employee_number] BIGINT        DEFAULT NULL,
  [credit_limit]              DECIMAL(10,2) DEFAULT NULL,
  [first_buy_date]            INT           DEFAULT NULL,
  CONSTRAINT [customer_pk] PRIMARY KEY ([customer_number]),
  CONSTRAINT [customer_name_uk] UNIQUE ([customer_name]),
  CONSTRAINT [customer_employee_fk] FOREIGN KEY ([sales_rep_employee_number]) REFERENCES [employee] ([employee_number]) ON UPDATE CASCADE
);

-- TABLE CUSTOMERDETAIL

CREATE TABLE [customerdetail] (
  [customer_number]     BIGINT      NOT NULL,
  [address_line_first]  VARCHAR(50) NOT NULL,
  [address_line_second] VARCHAR(50) DEFAULT NULL,
  [city]                VARCHAR(50) DEFAULT NULL,
  [state]               VARCHAR(50) DEFAULT NULL,
  [postal_code]         VARCHAR(15) DEFAULT NULL,
  [country]             VARCHAR(50) DEFAULT NULL,
  CONSTRAINT [customerdetail_pk] PRIMARY KEY ([customer_number]),
  CONSTRAINT [customerdetail_address_line_first_uk] UNIQUE ([address_line_first]),
  CONSTRAINT [customerdetail_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES [customer] ([customer_number])
); 

-- TABLE MANAGER

CREATE TABLE [manager] (
  [manager_id]         BIGINT         NOT NULL IDENTITY, -- in PostgreSQL/Oracle this PK is a sequence
  [manager_name]       VARCHAR(50)    NOT NULL DEFAULT '"anonymous"',
  [manager_detail]     NVARCHAR(4000) DEFAULT NULL, -- or, NVARCHAR(max) up to 2GB
  [manager_evaluation] VARCHAR(500)   DEFAULT NULL, 
  CONSTRAINT [manager_pk] PRIMARY KEY ([manager_id]),
  CONSTRAINT ENSURE_JSON CHECK(ISJSON([manager_detail]) = 1)
);

-- TABLE OFFICE_HAS_MANAGER

CREATE TABLE [office_has_manager] (
  [offices_office_code] VARCHAR(10) NOT NULL,
  [managers_manager_id] BIGINT      NOT NULL,
  CONSTRAINT [office_manager_uk] UNIQUE ([offices_office_code], [managers_manager_id]),
  CONSTRAINT [office_fk] FOREIGN KEY ([offices_office_code]) REFERENCES [office] ([office_code]) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT [manager_fk] FOREIGN KEY ([managers_manager_id]) REFERENCES [manager] ([manager_id]) ON UPDATE NO ACTION ON DELETE NO ACTION  
);

-- TABLE PRODUCTLINE

CREATE TABLE [productline] (
  [product_line]     VARCHAR(50)    NOT NULL,
  [code]             BIGINT         NOT NULL,
  [text_description] VARCHAR(4000)  DEFAULT NULL,
  [html_description] XML            DEFAULT NULL,
  [image]            VARBINARY(max) DEFAULT NULL,
  [created_on]       DATE           DEFAULT GETDATE(),
  CONSTRAINT [productline_pk] PRIMARY KEY ([product_line],[code]),
  CONSTRAINT [productline_uk] UNIQUE([product_line])
);

-- TABLE PRODUCTLINEDETAIL

CREATE TABLE [productlinedetail] (
  [product_line]  VARCHAR(50) NOT NULL,
  [code]          BIGINT      NOT NULL,
  [line_capacity] VARCHAR(20) NOT NULL,
  [line_type]     INT         DEFAULT 0,
  CONSTRAINT [productlinedetail_uk] UNIQUE([product_line]),
  CONSTRAINT [productlinedetail_pk] PRIMARY KEY ([product_line],[code]),    
  CONSTRAINT [productlinedetail_productline_fk] FOREIGN KEY ([product_line],[code]) REFERENCES [productline] ([product_line],[code])  
);

-- TABLE PRODUCT

CREATE SEQUENCE [product_uid_seq] START WITH 10 INCREMENT BY 10;
GO

CREATE TABLE [product] (
  [product_id]          BIGINT        NOT NULL IDENTITY, -- in PostgreSQL/Oracle this PK is a sequence
  [product_name]        VARCHAR(70)   DEFAULT NULL,
  [product_line]        VARCHAR(50)   DEFAULT NULL,
  [code]                BIGINT        NOT NULL,
  [product_scale]       VARCHAR(10)   DEFAULT NULL,
  [product_vendor]      VARCHAR(50)   DEFAULT NULL,
  [product_description] VARCHAR(max)  DEFAULT NULL,
  [quantity_in_stock]   INT           DEFAULT 0,
  [buy_price]           DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  [specs]               VARCHAR(max)  DEFAULT NULL,
  [msrp]                DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  [product_uid]         BIGINT        NOT NULL DEFAULT (NEXT VALUE FOR [product_uid_seq]),
  CONSTRAINT [product_pk] PRIMARY KEY ([product_id]),
  CONSTRAINT [product_productline_fk] FOREIGN KEY ([product_line],[code]) REFERENCES [productline] ([product_line],[code])
);

-- TABLE ORDER

CREATE TABLE [order] (
  [order_id]        BIGINT        NOT NULL IDENTITY, -- in PostgreSQL/Oracle this PK is a sequence
  [order_date]      DATE          NOT NULL,
  [required_date]   DATE          NOT NULL,
  [shipped_date]    DATE          DEFAULT NULL,
  [status]          VARCHAR(15)   NOT NULL,
  [comments]        VARCHAR(max)  DEFAULT NULL,
  [customer_number] BIGINT        NOT NULL,
  [amount]          DECIMAL(10,2) NOT NULL,
  CONSTRAINT [order_pk] PRIMARY KEY ([order_id]),
  CONSTRAINT [order_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES [customer] ([customer_number])
);

-- TABLE ORDERDETAIL

CREATE TABLE [orderdetail] (
  [orderdetail_id]    BIGINT        NOT NULL IDENTITY,
  [order_id]          BIGINT        NOT NULL,
  [product_id]        BIGINT        NOT NULL,
  [quantity_ordered]  INT           NOT NULL,
  [price_each]        DECIMAL(10,2) NOT NULL,
  [order_line_number] INT           NOT NULL,
  CONSTRAINT [orderdetail_pk] PRIMARY KEY ([orderdetail_id]),
  CONSTRAINT [orderdetail_uk] UNIQUE ([order_id], [product_id]),
  CONSTRAINT [orderdetail_order_fk] FOREIGN KEY ([order_id]) REFERENCES [order] ([order_id]),
  CONSTRAINT [orderdetail_product_fk] FOREIGN KEY ([product_id]) REFERENCES [product] ([product_id])
);

-- TABLE TOP3PRODUCT

CREATE TABLE [top3product] (  
  [product_id]   BIGINT      NOT NULL,
  [product_name] VARCHAR(70) DEFAULT NULL,  
  CONSTRAINT [top3product_pk] PRIMARY KEY ([product_id])
);

-- TABLE PAYMENT

CREATE TABLE [payment] (
  [customer_number] BIGINT        NOT NULL,
  [check_number]    VARCHAR(50)   NOT NULL,
  [payment_date]    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [invoice_amount]  DECIMAL(10,2) NOT NULL,
  [caching_date]    DATETIME      DEFAULT NULL,
  [version]         INT           NOT NULL DEFAULT 0,
  [modified]        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT [payment_pk] PRIMARY KEY ([customer_number],[check_number]),
  CONSTRAINT [check_number_uk] UNIQUE([check_number]),
  CONSTRAINT [payment_customer_fk] FOREIGN KEY ([customer_number]) REFERENCES [customer] ([customer_number])
);

-- TABLE BANK_TRANSACTION

CREATE TABLE [bank_transaction] (
  [transaction_id]  BIGINT        NOT NULL IDENTITY,
  [bank_name]       VARCHAR(50)   NOT NULL,
  [bank_iban]       VARCHAR(50)   NOT NULL,  
  [transfer_amount] DECIMAL(10,2) NOT NULL,
  [caching_date]    DATETIME      DEFAULT GETDATE(),
  [customer_number] BIGINT        NOT NULL,
  [check_number]    VARCHAR(50)   NOT NULL, 
  [card_type]       VARCHAR(50)   NOT NULL,
  [status]          VARCHAR(50)   NOT NULL DEFAULT 'SUCCESS',
  CONSTRAINT [bank_transaction_pk] PRIMARY KEY ([transaction_id]),  
  CONSTRAINT [bank_transaction_customer_fk] FOREIGN KEY ([customer_number],[check_number]) REFERENCES [payment] ([customer_number],[check_number])
);

-- TABLE OFFICE_FLIGHTS

CREATE TABLE [office_flights] (  
  [depart_town]  VARCHAR(32) NOT NULL,
  [arrival_town] VARCHAR(32) NOT NULL,
  [distance_km]  INT         NOT NULL,
  CONSTRAINT [office_flights_pk] PRIMARY KEY ([depart_town], [arrival_town])  
);

GO

/* USER-DEFINED VIEWS */

CREATE VIEW [customer_master] AS
SELECT [customer].[customer_name],
       [customer].[credit_limit],
       [customerdetail].[city],
       [customerdetail].[country],
       [customerdetail].[address_line_first],
       [customerdetail].[postal_code],
       [customerdetail].[state]
FROM [customer]
JOIN [customerdetail] ON [customerdetail].[customer_number] = [customer].[customer_number]
WHERE [customer].[first_buy_date] IS NOT NULL;
GO

CREATE VIEW [office_master] AS
SELECT [office].[office_code],
       [office].[city],
       [office].[country],
       [office].[state],
       [office].[phone],
	   [office].[postal_code]
FROM [office]
WHERE [office].[city] IS NOT NULL;
GO

CREATE VIEW [product_master] AS
SELECT [product].[product_line],
       [product].[product_name],
       [product].[product_scale]       
FROM [product];
GO

/* END */

/*
*********************************************************************
http://www.mysqltutorial.org
*********************************************************************
Name: MySQL Sample Database classicmodels
Link: http://www.mysqltutorial.org/mysql-sample-database.aspx
*********************************************************************

This is a modified version of the original schema for MySQL
*/