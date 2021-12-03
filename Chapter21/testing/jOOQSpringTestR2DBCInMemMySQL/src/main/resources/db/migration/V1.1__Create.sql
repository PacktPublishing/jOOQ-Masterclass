/*
*********************************************************************
http://www.mysqltutorial.org
*********************************************************************
Name: MySQL Sample Database classicmodels
Link: http://www.mysqltutorial.org/mysql-sample-database.aspx
*********************************************************************

This is a modified version of the original schema for MySQL
*/

/* START */

DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `bank_transaction`;
DROP TABLE IF EXISTS `orderdetail`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `productline`;
DROP TABLE IF EXISTS `top3product`;
DROP TABLE IF EXISTS `productlinedetail`;
DROP TABLE IF EXISTS `office_has_manager`;
DROP TABLE IF EXISTS `manager`;
DROP TABLE IF EXISTS `customerdetail`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `sale`;
DROP TABLE IF EXISTS `daily_activity`;
DROP TABLE IF EXISTS `token`;
DROP TABLE IF EXISTS `employee`;
DROP TABLE IF EXISTS `employee_status`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `office`;
DROP TABLE IF EXISTS `office_flights`;

/*Table structure for table `office` */

CREATE TABLE `office` (
  `office_code` varchar(10) NOT NULL,
  `city` varchar(50),
  `phone` varchar(50) NOT NULL,
  `address_line_first` varchar(50) NOT NULL,
  `address_line_second` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `country` varchar(50),
  `postal_code` varchar(15) NOT NULL,
  `territory` varchar(10) NOT NULL,
  -- `location` point DEFAULT NULL, suppressed for H2 compatibilty
  `internal_budget` int NOT NULL,
  CONSTRAINT `office_pk` PRIMARY KEY (`office_code`),
  CONSTRAINT `office_postal_code_uk` UNIQUE (`postal_code`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

CREATE TABLE `department` (
  `department_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `code` smallint DEFAULT 1,
  `office_code` varchar(10) NOT NULL,
  `topic` varchar(100) DEFAULT NULL,  
  `dep_net_ipv4` varchar(16) DEFAULT NULL,
  `local_budget` float DEFAULT NULL,
  `profit` float DEFAULT NULL,
  `forecast_profit` float DEFAULT NULL,
  `cash` float DEFAULT NULL,
  `accounts_receivable` float DEFAULT NULL,
  `inventories` float DEFAULT NULL,
  `accounts_payable` float DEFAULT NULL,
  `st_borrowing` float DEFAULT NULL,
  `accrued_liabilities` float DEFAULT NULL,
  CONSTRAINT `department_pk` PRIMARY KEY (`department_id`),  
  CONSTRAINT `department_code_uk` UNIQUE (`code`),
  CONSTRAINT `department_office_fk` FOREIGN KEY (`office_code`) REFERENCES `office` (`office_code`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `employee` */

CREATE TABLE `employee` (
  `employee_number` bigint NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `office_code` varchar(10) NOT NULL,
  `salary` int NOT NULL,
  `commission` int DEFAULT NULL,
  `reports_to` bigint DEFAULT NULL,
  `job_title` varchar(50) NOT NULL, 
  `employee_of_year` varchar(50) DEFAULT NULL,
  `monthly_bonus` varchar(500) DEFAULT NULL,
  CONSTRAINT `employee_pk` PRIMARY KEY (`employee_number`),
  CONSTRAINT `employee_employee_fk` FOREIGN KEY (`reports_to`) REFERENCES `employee` (`employee_number`),
  CONSTRAINT `employee_office_fk` FOREIGN KEY (`office_code`) REFERENCES `office` (`office_code`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `employee_status` */

CREATE TABLE `employee_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_number` bigint NOT NULL,  
  `status` varchar(50) NOT NULL,  
  `acquired_date` date NOT NULL,
  CONSTRAINT `id_pk` PRIMARY KEY (`id`),  
  CONSTRAINT `employee_status_employee_fk` FOREIGN KEY (`employee_number`) REFERENCES `employee` (`employee_number`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `sale` */

CREATE TABLE `sale` (
  `sale_id` bigint NOT NULL AUTO_INCREMENT, 
  `fiscal_year` int NOT NULL,  
  `sale` float NOT NULL,  
  `employee_number` bigint DEFAULT NULL,  
  `hot` boolean DEFAULT FALSE,  
  `rate` enum ('SILVER', 'GOLD', 'PLATINUM') DEFAULT NULL,
  `vat` enum ('NONE', 'MIN', 'MAX') DEFAULT NULL,
  `fiscal_month` int NOT NULL,
  `revenue_growth` float NOT NULL, 
  `trend` varchar(10) DEFAULT NULL,
  CONSTRAINT `sale_pk` PRIMARY KEY (`sale_id`),    
  CONSTRAINT `sale_employee_fk` FOREIGN KEY (`employee_number`) REFERENCES `employee` (`employee_number`) ON UPDATE CASCADE
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `daily_activity` */

CREATE TABLE `daily_activity` (
  `day_id` bigint NOT NULL AUTO_INCREMENT, 
  `day_date` date NOT NULL,
  `sales` float NOT NULL,  
  `visitors` float NOT NULL,    
  `conversion` float NOT NULL,
  CONSTRAINT `daily_activity_pk` PRIMARY KEY (`day_id`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `token` */

CREATE TABLE `token` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,    
  `sale_id` bigint NOT NULL,
  `amount` float NOT NULL,   
  `updated_on` timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT `token_pk` PRIMARY KEY (`token_id`),  
  CONSTRAINT `token_sale_fk` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`) ON DELETE CASCADE ON UPDATE CASCADE
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `customer` */

CREATE TABLE `customer` (
  `customer_number` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `contact_last_name` varchar(50) NOT NULL,
  `contact_first_name` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `sales_rep_employee_number` bigint DEFAULT NULL,
  `credit_limit` decimal(10,2) DEFAULT NULL,
  `first_buy_date` int DEFAULT NULL,
  CONSTRAINT `customer_pk` PRIMARY KEY (`customer_number`), 
  CONSTRAINT `customer_name_uk` UNIQUE (`customer_name`),
  CONSTRAINT `customer_employee_fk` FOREIGN KEY (`sales_rep_employee_number`) REFERENCES `employee` (`employee_number`) ON UPDATE CASCADE
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `customerdetail` */

CREATE TABLE `customerdetail` (
  `customer_number` bigint NOT NULL,
  `address_line_first` varchar(50) NOT NULL,
  `address_line_second` varchar(50) DEFAULT NULL,
  `city` varchar(50),
  `state` varchar(50) DEFAULT NULL,
  `postal_code` varchar(15) DEFAULT NULL,
  `country` varchar(50),
  CONSTRAINT `customerdetail_pk` PRIMARY KEY (`customer_number`),  
  CONSTRAINT customerdetail_address_line_first_uk UNIQUE (address_line_first),
  CONSTRAINT `customerdetail_customer_fk` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)  
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `manager` */

CREATE TABLE `manager` (
  `manager_id` bigint NOT NULL AUTO_INCREMENT,
  `manager_name` varchar(50) NOT NULL,
  `manager_detail` json DEFAULT NULL,
  `manager_evaluation` varchar(200) DEFAULT NULL, 
  CONSTRAINT `manager_pk` PRIMARY KEY (`manager_id`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1;  suppressed for H2 compatibilty

/*Table structure for table `office_has_manager` */

CREATE TABLE `office_has_manager` (
  `offices_office_code` varchar(10) NOT NULL,
  `managers_manager_id` bigint NOT NULL,
  CONSTRAINT `office_manager_uk` UNIQUE (`offices_office_code`, `managers_manager_id`),
  CONSTRAINT `office_fk` FOREIGN KEY (`offices_office_code`) REFERENCES office (`office_code`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `manager_fk` FOREIGN KEY (`managers_manager_id`) REFERENCES manager (`manager_id`) ON UPDATE NO ACTION ON DELETE NO ACTION  
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `productline` */

CREATE TABLE `productline` (
  `product_line` varchar(50) NOT NULL,
  `code` bigint NOT NULL,
  `text_description` varchar(4000) DEFAULT NULL,
  `html_description` mediumtext DEFAULT NULL,
  `image` mediumblob DEFAULT NULL,
  `created_on` date DEFAULT (CURRENT_DATE),
  CONSTRAINT `productline_pk` PRIMARY KEY (`product_line`,`code`),
  CONSTRAINT productline_uk UNIQUE(product_line)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `productdetail` */

CREATE TABLE `productlinedetail` (
  `product_line` varchar(50) NOT NULL,
  `code` bigint NOT NULL,
  `line_capacity` varchar(20) NOT NULL,
  `line_type` int DEFAULT 0,
  CONSTRAINT `productlinedetail_pk` PRIMARY KEY (`product_line`,`code`),  
  CONSTRAINT `productlinedetail_uk` UNIQUE(product_line),
  CONSTRAINT `productlinedetail_productline_fk` FOREIGN KEY (`product_line`,`code`) REFERENCES `productline` (`product_line`,`code`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `product` */

CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) DEFAULT NULL,
  `product_line` varchar(50) DEFAULT NULL,
  `code` bigint NOT NULL,
  `product_scale` varchar(10) DEFAULT NULL,
  `product_vendor` varchar(50) DEFAULT NULL,
  `product_description` text DEFAULT NULL,
  `quantity_in_stock` int DEFAULT 0,
  `buy_price` decimal(10,2) NOT NULL DEFAULT 0.0,
  `msrp` decimal(10,2) NOT NULL DEFAULT 0.0,
  `specs` mediumtext DEFAULT NULL,
  `product_uid` bigint DEFAULT 10,
  CONSTRAINT `product_pk` PRIMARY KEY (`product_id`),  
  CONSTRAINT `product_productline_fk` FOREIGN KEY (`product_line`,`code`) REFERENCES `productline` (`product_line`,`code`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1;  suppressed for H2 compatibilty

CREATE TABLE sequences (sequence_name VARCHAR(50),currval INT);
INSERT INTO sequences (sequence_name,currval) VALUES ('product_uid_seq',0);

/*Table structure for table `order` */

CREATE TABLE `order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` date NOT NULL,
  `required_date` date NOT NULL,
  `shipped_date` date DEFAULT NULL,
  `status` varchar(15) NOT NULL,
  `comments` text,
  `customer_number` bigint NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  CONSTRAINT `order_pk` PRIMARY KEY (`order_id`),
  CONSTRAINT `order_customer_fk` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `orderdetail` */

CREATE TABLE `orderdetail` (
  `orderdetail_id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity_ordered` int NOT NULL,
  `price_each` decimal(10,2) NOT NULL,
  `order_line_number` smallint NOT NULL,  
  CONSTRAINT `orderdetail_pk` PRIMARY KEY (`orderdetail_id`),
  CONSTRAINT `orderdetail_uk` UNIQUE KEY (`order_id`, `product_id`),
  CONSTRAINT `orderdetail_order_fk` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
  CONSTRAINT `orderdetail_product_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `top3product` */

CREATE TABLE `top3product` (  
  `product_id` bigint NOT NULL,
  `product_name` varchar(70) DEFAULT NULL,  
  CONSTRAINT `top3product_pk` PRIMARY KEY (`product_id`),
  CONSTRAINT `top3product_product_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `payment` */

CREATE TABLE `payment` (
  `customer_number` bigint NOT NULL,
  `check_number` varchar(50) NOT NULL,
  `payment_date` timestamp NOT NULL DEFAULT NOW(),
  `invoice_amount` decimal(10,2) NOT NULL,
  `caching_date` timestamp DEFAULT NULL,  
  `version` int NOT NULL DEFAULT 0,
  `modified` timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT `payment_pk` PRIMARY KEY (`customer_number`,`check_number`),
  CONSTRAINT `check_number_uk` UNIQUE (`check_number`),
  CONSTRAINT `payment_customer_fk` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `bank_transaction` */

CREATE TABLE `bank_transaction` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(50) NOT NULL,
  `bank_iban` varchar(50) NOT NULL,  
  `transfer_amount` decimal(10,2) NOT NULL,
  `caching_date` timestamp NOT NULL DEFAULT NOW(),
  `customer_number` bigint NOT NULL,
  `check_number` varchar(50) NOT NULL, 
  `card_type` varchar(50) NOT NULL, 
  `status` varchar(50) NOT NULL DEFAULT 'SUCCESS',   
  CONSTRAINT `bank_transaction_pk` PRIMARY KEY (`transaction_id`),    
  CONSTRAINT `bank_transaction_customer_fk` FOREIGN KEY (`customer_number`,`check_number`) REFERENCES `payment` (`customer_number`,`check_number`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty

/*Table structure for table `office_flights` */

CREATE TABLE `office_flights` (  
  `depart_town` varchar(32) NOT NULL,
  `arrival_town` varchar(32) NOT NULL,
  `distance_km` integer NOT NULL,
  CONSTRAINT `office_flights_pk` PRIMARY KEY (`depart_town`, `arrival_town`)
); -- ENGINE=InnoDB DEFAULT CHARSET=latin1; suppressed for H2 compatibilty
