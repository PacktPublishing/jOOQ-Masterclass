/* START */

DROP TABLE IF EXISTS "payment" CASCADE;
DROP TABLE IF EXISTS "bank_transaction" CASCADE;
DROP TABLE IF EXISTS "orderdetail" CASCADE;
DROP TABLE IF EXISTS "order" CASCADE;
DROP TABLE IF EXISTS "product" CASCADE;
DROP TABLE IF EXISTS "productline" CASCADE;
DROP TABLE IF EXISTS "top3product" CASCADE;
DROP TABLE IF EXISTS "productlinedetail" CASCADE;
DROP TABLE IF EXISTS "office_has_manager" CASCADE;
DROP TABLE IF EXISTS "manager" CASCADE;
DROP TABLE IF EXISTS "customer" CASCADE;
DROP TABLE IF EXISTS "customerdetail" CASCADE;
DROP TABLE IF EXISTS "sale" CASCADE;
DROP TABLE IF EXISTS "daily_activity" CASCADE;
DROP TABLE IF EXISTS "token" CASCADE;
DROP TABLE IF EXISTS "employee" CASCADE;
DROP TABLE IF EXISTS "employee_status" CASCADE;
DROP TABLE IF EXISTS "department" CASCADE;
DROP TABLE IF EXISTS "office" CASCADE;
DROP TABLE IF EXISTS "office_flights" CASCADE;

DROP SEQUENCE IF EXISTS "manager_seq";
DROP SEQUENCE IF EXISTS "product_seq";
DROP SEQUENCE IF EXISTS "order_seq";
DROP SEQUENCE IF EXISTS "sale_seq";
DROP SEQUENCE IF EXISTS "customer_seq";
DROP SEQUENCE IF EXISTS "employee_seq";
DROP SEQUENCE IF EXISTS "token_seq";

DROP TYPE IF EXISTS "rate_type";
DROP TYPE IF EXISTS "vat_type";
DROP TYPE IF EXISTS "evaluation_criteria";

DROP DOMAIN IF EXISTS "postal_code";

DROP FUNCTION IF EXISTS "make_array";
DROP FUNCTION IF EXISTS "dup";
DROP FUNCTION IF EXISTS "get_avg_sale";
DROP FUNCTION IF EXISTS "get_salary_stat";
DROP FUNCTION IF EXISTS "swap";
DROP FUNCTION IF EXISTS "new_salary";
DROP FUNCTION IF EXISTS "get_customer";
DROP FUNCTION IF EXISTS "get_offices_multiple";
DROP FUNCTION IF EXISTS "employee_office_arr";
DROP FUNCTION IF EXISTS "sale_price";
DROP FUNCTION IF EXISTS "top_three_sales_per_employee";
DROP FUNCTION IF EXISTS "product_of_product_line";
DROP FUNCTION IF EXISTS "update_msrp";

DROP VIEW IF EXISTS "customer_master";
DROP VIEW IF EXISTS "office_master";
DROP VIEW IF EXISTS "product_master";

CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- TABLE OFFICE

CREATE DOMAIN "postal_code" AS VARCHAR(15)
CHECK(
   VALUE ~ '^\d{5}$'
OR VALUE ~ '^[A-Z]{2}[0-9]{3}[A-Z]{2}$'
);

CREATE TABLE "office" (
  "office_code"         VARCHAR(10) NOT NULL,
  "city"                VARCHAR(50) DEFAULT NULL,
  "phone"               VARCHAR(50) NOT NULL,
  "address_line_first"  VARCHAR(50) NOT NULL,
  "address_line_second" VARCHAR(50) DEFAULT NULL,
  "state"               VARCHAR(50) DEFAULT NULL,
  "country"             VARCHAR(50) DEFAULT NULL,
  "postal_code"         postal_code NOT NULL,
  "territory"           VARCHAR(10) NOT NULL,
  "location"            POINT       DEFAULT NULL,
  "internal_budget"     INT         NOT NULL, 
  CONSTRAINT "office_pk" PRIMARY KEY ("office_code"),
  CONSTRAINT "office_postal_code_uk" UNIQUE ("postal_code")
);

-- TABLE DEPARTMENT

CREATE TABLE "department" (
  "department_id"       SERIAL      NOT NULL,
  "name"                VARCHAR(50) NOT NULL,
  "phone"               VARCHAR(50) NOT NULL,
  "code"                INT         NOT NULL,
  "office_code"         VARCHAR(10) NOT NULL,
  "topic"               TEXT[]      DEFAULT NULL,  
  "dep_net_ipv4"        INET        DEFAULT NULL,
  "local_budget"        FLOAT       DEFAULT NULL,
  "profit"              FLOAT       DEFAULT NULL,
  "forecast_profit"     FLOAT       DEFAULT NULL,
  "cash"                FLOAT       DEFAULT NULL,
  "accounts_receivable" FLOAT       DEFAULT NULL,
  "inventories"         FLOAT       DEFAULT NULL,
  "accounts_payable"    FLOAT       DEFAULT NULL,
  "st_borrowing"        FLOAT       DEFAULT NULL,
  "accrued_liabilities" FLOAT       DEFAULT NULL,
  CONSTRAINT "department_pk" PRIMARY KEY ("department_id"),
  CONSTRAINT "department_code_uk" UNIQUE ("code"),
  CONSTRAINT "department_office_fk" FOREIGN KEY ("office_code") REFERENCES "office" ("office_code")
);

ALTER SEQUENCE "department_department_id_seq" RESTART WITH 20;

-- TABLE EMPLOYEE

CREATE TABLE "employee" (
  "employee_number"  BIGINT       NOT NULL,
  "last_name"        VARCHAR(50)  NOT NULL,
  "first_name"       VARCHAR(50)  NOT NULL,
  "extension"        VARCHAR(10)  NOT NULL,
  "email"            VARCHAR(100) NOT NULL,
  "office_code"      VARCHAR(10)  NOT NULL,
  "salary"           INT          NOT NULL,
  "commission"       INT          DEFAULT NULL,
  "reports_to"       BIGINT       DEFAULT NULL,
  "job_title"        VARCHAR(50)  NOT NULL,
  "employee_of_year" INT[]        DEFAULT NULL,
  "monthly_bonus"    INT[]        DEFAULT NULL,
  CONSTRAINT "employee_pk" PRIMARY KEY ("employee_number"),
  CONSTRAINT "employee_employee_fk" FOREIGN KEY ("reports_to") REFERENCES "employee" ("employee_number"),
  CONSTRAINT "employees_office_fk" FOREIGN KEY ("office_code") REFERENCES "office" ("office_code")
);

-- this sequence is not used automatically
CREATE SEQUENCE "employee_seq" START 100000 INCREMENT 10 MINVALUE 100000 MAXVALUE 10000000 OWNED BY "employee"."employee_number";

-- TABLE EMPLOYEE_STATUS

CREATE TABLE "employee_status" (
  "id"              SERIAL      NOT NULL,
  "employee_number" BIGINT      NOT NULL,  
  "status"          VARCHAR(50) NOT NULL,  
  "acquired_date"   DATE        NOT NULL,
  CONSTRAINT "id_pk" PRIMARY KEY ("id"),  
  CONSTRAINT "employee_status_employee_fk" FOREIGN KEY ("employee_number") REFERENCES "employee" ("employee_number")
);

-- TABLE SALE

CREATE SEQUENCE "sale_seq" START 1000000;

CREATE TYPE "rate_type" AS enum('SILVER', 'GOLD', 'PLATINUM');
CREATE TYPE "vat_type" AS enum('NONE', 'MIN', 'MAX');

CREATE TABLE "sale" (
  "sale_id"         BIGINT      NOT NULL DEFAULT NEXTVAL ('"sale_seq"'),  
  "fiscal_year"     INT         NOT NULL,  
  "sale"            FLOAT       NOT NULL,  
  "employee_number" BIGINT      DEFAULT NULL,  
  "hot"             BOOLEAN     DEFAULT FALSE,
  "rate"            rate_type   DEFAULT NULL,
  "vat"             vat_type    DEFAULT NULL,
  "fiscal_month"    INT         NOT NULL,
  "revenue_growth"  FLOAT       NOT NULL,
  "trend"           VARCHAR(10) DEFAULT NULL,
  CONSTRAINT "sale_pk" PRIMARY KEY ("sale_id"),  
  CONSTRAINT "sale_employee_fk" FOREIGN KEY ("employee_number") REFERENCES "employee" ("employee_number") ON UPDATE CASCADE
);

-- TABLE DAILY_ACTIVITY

CREATE TABLE "daily_activity" (
  "day_id"     SERIAL NOT NULL, 
  "day_date"   DATE   NOT NULL,
  "sales"      FLOAT  NOT NULL,  
  "visitors"   FLOAT  NOT NULL,    
  "conversion" FLOAT  NOT NULL,
  CONSTRAINT "daily_activity_pk" PRIMARY KEY ("day_id")
);

-- TABLE TOKEN

CREATE SEQUENCE "token_seq" START 1000000;

CREATE TABLE "token" (
  "token_id"   BIGINT    NOT NULL DEFAULT NEXTVAL ('"token_seq"'),    
  "sale_id"    BIGINT    NOT NULL,
  "amount"     FLOAT     NOT NULL,   
  "updated_on" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT "token_pk" PRIMARY KEY ("token_id"),  
  CONSTRAINT "token_sale_fk" FOREIGN KEY ("sale_id") REFERENCES "sale" ("sale_id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- TABLE CUSTOMER

CREATE SEQUENCE "customer_seq" START 1000000;

CREATE TABLE "customer" (
  "customer_number"           BIGINT        NOT NULL DEFAULT NEXTVAL ('"customer_seq"'),
  "customer_name"             VARCHAR(50)   NOT NULL,
  "contact_last_name"         VARCHAR(50)   NOT NULL,
  "contact_first_name"        VARCHAR(50)   NOT NULL,
  "phone"                     VARCHAR(50)   NOT NULL,  
  "sales_rep_employee_number" BIGINT        DEFAULT NULL,
  "credit_limit"              DECIMAL(10,2) DEFAULT NULL,
  "first_buy_date"            INT           DEFAULT NULL,
  CONSTRAINT "customer_pk" PRIMARY KEY ("customer_number"),
  CONSTRAINT "customer_name_uk" UNIQUE ("customer_name"),
  CONSTRAINT "customer_employee_fk" FOREIGN KEY ("sales_rep_employee_number") REFERENCES "employee" ("employee_number") ON UPDATE CASCADE
);

-- TABLE CUSTOMERDETAIL

CREATE TABLE "customerdetail" (
  "customer_number"     BIGINT      NOT NULL,
  "address_line_first"  VARCHAR(50) NOT NULL,
  "address_line_second" VARCHAR(50) DEFAULT NULL,
  "city"                VARCHAR(50) DEFAULT NULL,
  "state"               VARCHAR(50) DEFAULT NULL,
  "postal_code"         VARCHAR(15) DEFAULT NULL,
  "country"             VARCHAR(50) DEFAULT NULL,
  CONSTRAINT "customerdetail_pk" PRIMARY KEY ("customer_number"),
  CONSTRAINT "customer_address_line_first_uk" UNIQUE ("address_line_first"),  
  CONSTRAINT "customerdetail_customer_fk" FOREIGN KEY ("customer_number") REFERENCES "customer" ("customer_number")  
);

-- TABLE MANAGER

CREATE TYPE "evaluation_criteria" AS ("communication_ability" INT, "ethics" INT, "performance" INT, "employee_input" INT);

CREATE SEQUENCE "manager_seq" START 1000000;

CREATE TABLE "manager" (
  "manager_id"         BIGINT              NOT NULL DEFAULT NEXTVAL ('"manager_seq"'),
  "manager_name"       VARCHAR(50)         NOT NULL DEFAULT '"anonymous"',
  "manager_detail"     JSON                DEFAULT NULL,
  "manager_evaluation" evaluation_criteria DEFAULT NULL, 
  CONSTRAINT "manager_pk" PRIMARY KEY ("manager_id")
);

-- TABLE OFFICE_HAS_MANAGER

CREATE TABLE "office_has_manager" (
  "offices_office_code" VARCHAR(10) NOT NULL,
  "managers_manager_id" BIGINT      NOT NULL,
  CONSTRAINT "office_manager_uk" UNIQUE ("offices_office_code", "managers_manager_id"),
  CONSTRAINT "office_fk" FOREIGN KEY ("offices_office_code") REFERENCES "office" ("office_code") ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "manager_fk" FOREIGN KEY ("managers_manager_id") REFERENCES "manager" ("manager_id") ON UPDATE NO ACTION ON DELETE NO ACTION  
);
  
-- TABLE PRODUCTLINE

CREATE TABLE "productline" (
  "product_line"     VARCHAR(50)   NOT NULL,
  "code"             BIGINT        NOT NULL,
  "text_description" VARCHAR(4000) DEFAULT NULL,
  "html_description" XML           DEFAULT NULL,
  "image"            BYTEA         DEFAULT NULL,
  "created_on"       DATE          NOT NULL DEFAULT NOW(),
  CONSTRAINT "productline_pk" PRIMARY KEY ("product_line", "code"),
  CONSTRAINT "productline_uk" UNIQUE("product_line")
);

-- TABLE PRODUCTDETAIL

CREATE TABLE "productlinedetail" (
  "product_line"  VARCHAR(50) NOT NULL,
  "code"          BIGINT      NOT NULL,
  "line_capacity" VARCHAR(20) NOT NULL,
  "line_type"     INT         DEFAULT 0,
  CONSTRAINT "productlinedetail_pk" PRIMARY KEY ("product_line","code"),  
  CONSTRAINT "productlinedetail_uk" UNIQUE("product_line"),
  CONSTRAINT "productlinedetail_productline_fk" FOREIGN KEY ("product_line","code") REFERENCES "productline" ("product_line","code")
);

-- TABLE PRODUCT

CREATE SEQUENCE "product_seq" START 1000000;

CREATE TABLE "product" (
  "product_id"          BIGINT        NOT NULL DEFAULT NEXTVAL ('"product_seq"'),
  "product_name"        VARCHAR(70)   DEFAULT NULL,
  "product_line"        VARCHAR(50)   DEFAULT NULL,
  "code"                BIGINT        NOT NULL,
  "product_scale"       VARCHAR(10)   DEFAULT NULL,
  "product_vendor"      VARCHAR(50)   DEFAULT NULL,
  "product_description" TEXT          DEFAULT NULL,
  "quantity_in_stock"   INT           DEFAULT 0,
  "buy_price"           DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  "msrp"                DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  "specs"               HSTORE        DEFAULT NULL,
  "product_uid"         BIGINT        GENERATED BY DEFAULT AS IDENTITY (START WITH 10 INCREMENT BY 10),
  CONSTRAINT "product_pk" PRIMARY KEY ("product_id"),
  CONSTRAINT "product_productline_fk" FOREIGN KEY ("product_line","code") REFERENCES "productline" ("product_line","code")
);

-- TABLE ORDER

CREATE SEQUENCE "order_seq" START 1000000;

CREATE TABLE "order" (
  "order_id"        BIGINT        NOT NULL DEFAULT NEXTVAL ('"order_seq"'),
  "order_date"      DATE          NOT NULL,
  "required_date"   DATE          NOT NULL,
  "shipped_date"    DATE          DEFAULT NULL,
  "status"          VARCHAR(15)   NOT NULL,
  "comments"        TEXT          DEFAULT NULL,    
  "customer_number" BIGINT        NOT NULL,
  "amount"          DECIMAL(10,2) NOT NULL,
  CONSTRAINT "order_pk" PRIMARY KEY ("order_id"),
  CONSTRAINT "order_customer_fk" FOREIGN KEY ("customer_number") REFERENCES "customer" ("customer_number")
);

-- TABLE ORDERDETAIL

CREATE TABLE "orderdetail" (
  "orderdetail_id"    SERIAL        NOT NULL, 
  "order_id"          BIGINT        NOT NULL,
  "product_id"        BIGINT        NOT NULL,
  "quantity_ordered"  INT           NOT NULL,
  "price_each"        DECIMAL(10,2) NOT NULL,
  "order_line_number" INT           NOT NULL,
  CONSTRAINT "orderdetail_pk" PRIMARY KEY ("orderdetail_id"),
  CONSTRAINT "orderdetail_uk" UNIQUE ("order_id", "product_id"),
  CONSTRAINT "orderdetail_order_fk" FOREIGN KEY ("order_id") REFERENCES "order" ("order_id"),
  CONSTRAINT "orderdetail_product_fk" FOREIGN KEY ("product_id") REFERENCES "product" ("product_id")
);

-- TABLE TOP3PRODUCT

CREATE TABLE "top3product" (  
  "product_id"   BIGINT      NOT NULL,
  "product_name" VARCHAR(70) DEFAULT NULL,  
  CONSTRAINT "top3product_pk" PRIMARY KEY ("product_id")
);

-- TABLE PAYMENT

CREATE TABLE "payment" (
  "customer_number" BIGINT        NOT NULL,
  "check_number"    VARCHAR(50)   NOT NULL,
  "payment_date"    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "invoice_amount"  DECIMAL(10,2) NOT NULL,
  "caching_date"    TIMESTAMP     DEFAULT NULL,
  "version"         INT           NOT NULL DEFAULT 0,
  "modified"        TIMESTAMP     NOT NULL DEFAULT NOW(),
  CONSTRAINT "payment_pk" PRIMARY KEY ("customer_number","check_number"),
  CONSTRAINT "check_number_uk" UNIQUE("check_number"),
  CONSTRAINT "payment_customer_fk" FOREIGN KEY ("customer_number") REFERENCES "customer" ("customer_number")
);

-- TABLE BANK_TRANSACTION

CREATE TABLE "bank_transaction" (
  "transaction_id"  SERIAL        NOT NULL,
  "bank_name"       VARCHAR(50)   NOT NULL,
  "bank_iban"       VARCHAR(50)   NOT NULL,  
  "transfer_amount" DECIMAL(10,2) NOT NULL,
  "caching_date"    TIMESTAMP     NOT NULL DEFAULT NOW(),
  "customer_number" BIGINT        NOT NULL,
  "check_number"    VARCHAR(50)   NOT NULL, 
  "card_type"       VARCHAR(50)   NOT NULL, 
  "status"          VARCHAR(50)   NOT NULL DEFAULT 'SUCCESS',   
  CONSTRAINT "bank_transaction_pk" PRIMARY KEY ("transaction_id"),    
  CONSTRAINT "bank_transaction_customer_fk" FOREIGN KEY ("customer_number","check_number") REFERENCES "payment" ("customer_number","check_number")
);

ALTER SEQUENCE "bank_transaction_transaction_id_seq" RESTART WITH 100;

-- TABLE OFFICE_FLIGHTS

CREATE TABLE "office_flights" (  
  "depart_town"  VARCHAR(32) NOT NULL,
  "arrival_town" VARCHAR(32) NOT NULL,
  "distance_km"  INT     NOT NULL,
  CONSTRAINT "office_flights_pk" PRIMARY KEY ("depart_town", "arrival_town")
);

/* USER-DEFINED FUNCTIONS */

CREATE FUNCTION "make_array"(anyelement, anyelement) RETURNS anyarray 
AS $$
  SELECT ARRAY[$1, $2];
$$ LANGUAGE sql;

CREATE FUNCTION "dup" (IN "f1" anyelement, OUT "f2" anyelement, OUT "f3" anyarray)
  AS 'select $1, array[$1,$1]' 
  LANGUAGE sql;

CREATE OR REPLACE FUNCTION "get_avg_sale"(IN "len_from" INT, IN "len_to" INT) 
    RETURNS INT 
	LANGUAGE plpgsql 
AS $$ 
  DECLARE "avg_count" INT; 
  BEGIN 
    SELECT avg("sale"."sale") 
    INTO "avg_count"
    FROM "sale" 
    WHERE "sale"."sale" BETWEEN "len_from" AND "len_to"; 
   
    RETURN "avg_count"; 
  END; 
$$;

CREATE OR REPLACE FUNCTION "get_salary_stat"(
    OUT "min_sal" INT, OUT "max_sal" INT, OUT "avg_sal" NUMERIC) 
  LANGUAGE plpgsql
AS $$
  BEGIN  
    SELECT MIN("public"."employee"."salary"),
           MAX("public"."employee"."salary"),
           AVG("public"."employee"."salary")::NUMERIC(7,2)
      INTO "min_sal", "max_sal", "avg_sal"
      FROM "public"."employee";
  END;
$$;

CREATE OR REPLACE FUNCTION "swap"(
    INOUT "x" INT, INOUT "y" INT) RETURNS RECORD
  LANGUAGE plpgsql	
AS $$
  BEGIN
    SELECT "x","y" INTO "y","x";
  END; 
$$;

CREATE OR REPLACE FUNCTION "new_salary"(IN "salary" INT, IN "bonus" INT DEFAULT 50, IN "penalty" INT DEFAULT 0)
  RETURNS INT
  LANGUAGE sql
AS $$
  SELECT $1 + $2 - $3;
$$;

CREATE OR REPLACE FUNCTION "get_customer"(IN "cl" INT) RETURNS REFCURSOR 
AS $$
  DECLARE
    "cur" REFCURSOR;                                                   
  BEGIN
    OPEN "cur" FOR SELECT * FROM "customer" WHERE "credit_limit" > "cl" ORDER BY "customer_name";   
    RETURN "cur";                                    
  END;
$$ LANGUAGE plpgsql;
	
CREATE OR REPLACE FUNCTION "get_offices_multiple"() RETURNS SETOF REFCURSOR 
AS $$
  DECLARE
    "ref1" REFCURSOR;           
    "ref2" REFCURSOR;                             
  BEGIN
    OPEN "ref1" FOR SELECT "public"."office"."city", "public"."office"."country" 
                    FROM "public"."office" WHERE "public"."office"."internal_budget" < 100000;  
    RETURN NEXT "ref1"; 
 
    OPEN "ref2" FOR SELECT "public"."office"."city", "public"."office"."country"
                    FROM "public"."office" WHERE "public"."office"."internal_budget" > 100000;  
    RETURN NEXT "ref2";                                                 
  END;
$$ LANGUAGE plpgsql;	
	  
CREATE OR REPLACE FUNCTION "employee_office_arr"(VARCHAR(10))
  RETURNS BIGINT[] 
AS $$
  SELECT ARRAY(SELECT "public"."employee"."employee_number"
    FROM "public"."employee" WHERE "public"."employee"."office_code" = $1)
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION "department_topic_arr"(IN "id" BIGINT)
  RETURNS text[] 
AS $$
  SELECT "public"."department"."topic"
    FROM "public"."department" WHERE "public"."department"."department_id" = "id"
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION "sale_price"(
    "quantity" INT, "list_price" REAL, "fraction_of_price"	REAL)
  RETURNS REAL LANGUAGE plpgsql 
AS $$ 
  BEGIN
    RETURN ("list_price" - ("list_price" * "fraction_of_price")) * "quantity";
  END;
$$;

CREATE OR REPLACE FUNCTION "top_three_sales_per_employee"(IN "employee_nr" BIGINT)
  RETURNS TABLE("sales" FLOAT) LANGUAGE plpgsql 
AS $$ 
  BEGIN
    RETURN QUERY
    SELECT 
      "public"."sale"."sale" AS "sales" 
    FROM 
      "public"."sale" 
    WHERE 
      employee_nr = "public"."sale"."employee_number" 
    ORDER BY
      "public"."sale"."sale" DESC
    LIMIT 3;     
  END; 
$$;

CREATE OR REPLACE FUNCTION "product_of_product_line"(IN "p_line_in" VARCHAR)
  RETURNS TABLE("p_id" BIGINT, "p_name" VARCHAR, "p_line" VARCHAR) LANGUAGE plpgsql 
AS $$ 
  BEGIN
    RETURN QUERY
    SELECT 
      "public"."product"."product_id" AS "p_id",
      "public"."product"."product_name" AS "p_name",
	  "public"."product"."product_line" AS "p_line"
    FROM 
      "public"."product" 
    WHERE 
      "p_line_in" = "public"."product"."product_line";     
  END; 
$$;

CREATE OR REPLACE FUNCTION "update_msrp" (IN "id" BIGINT, IN "debit" INT) 
  RETURNS REAL 
AS $$
  UPDATE "public"."product"
    SET "msrp" = "public"."product"."msrp" - "debit"
    WHERE "public"."product"."product_id" = "id"
    RETURNING "public"."product"."msrp";
$$ LANGUAGE sql;

/* USER-DEFINED VIEWS */

CREATE OR REPLACE VIEW "customer_master" AS
SELECT "customer"."customer_name",
       "customer"."credit_limit",
       "customerdetail"."city",
       "customerdetail"."country",
       "customerdetail"."address_line_first",
       "customerdetail"."postal_code",
       "customerdetail"."state"
FROM "customer"
JOIN "customerdetail" ON "customerdetail"."customer_number" = "customer"."customer_number"
WHERE "customer"."first_buy_date" IS NOT NULL;

CREATE OR REPLACE VIEW "office_master" AS
SELECT "office"."office_code",
       "office"."city",
       "office"."country",
       "office"."state",
       "office"."phone",
       "office"."postal_code"
FROM "office"
WHERE "office"."city" IS NOT NULL;

CREATE OR REPLACE VIEW "product_master" AS
SELECT "product"."product_line",
       "product"."product_name",
       "product"."product_scale"       
FROM "product";

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