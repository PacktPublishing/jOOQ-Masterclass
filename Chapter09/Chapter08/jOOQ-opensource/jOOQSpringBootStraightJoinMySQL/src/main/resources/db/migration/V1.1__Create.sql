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

USE `classicmodels`;

DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `orderdetail`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `productline`;
DROP TABLE IF EXISTS `productlinedetail`;
DROP TABLE IF EXISTS `office_has_manager`;
DROP TABLE IF EXISTS `manager`;
DROP TABLE IF EXISTS `customerdetail`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `sale`;
DROP TABLE IF EXISTS `employee`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `office`;

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
  PRIMARY KEY (`office_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `department` (
  `department_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `code` smallint DEFAULT 1,
  `office_code` varchar(10) NOT NULL,
  `topic` json NOT NULL,
  PRIMARY KEY (`department_id`),
  KEY `office_code` (`office_code`),
  CONSTRAINT `department_ibfk_1` FOREIGN KEY (`office_code`) REFERENCES `office` (`office_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `employee` */

CREATE TABLE `employee` (
  `employee_number` bigint NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `office_code` varchar(10) NOT NULL,
  `salary` int NOT NULL,
  `reports_to` bigint DEFAULT NULL,
  `job_title` varchar(50) NOT NULL,
  PRIMARY KEY (`employee_number`),
  KEY `reports_to` (`reports_to`),
  KEY `office_code` (`office_code`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`reports_to`) REFERENCES `employee` (`employee_number`),
  CONSTRAINT `employees_ibfk_2` FOREIGN KEY (`office_code`) REFERENCES `office` (`office_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `sale` */

CREATE TABLE `sale` (
  `sale_id` bigint NOT NULL AUTO_INCREMENT, 
  `fiscal_year` int NOT NULL,  
  `sale` float NOT NULL,  
  `employee_number` bigint DEFAULT NULL,  
  `hot` boolean DEFAULT FALSE,  
  PRIMARY KEY (`sale_id`),  
  KEY `employee_number` (`employee_number`),  
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`employee_number`) REFERENCES `employee` (`employee_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `customer` */

CREATE TABLE `customer` (
  `customer_number` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `contact_last_name` varchar(50) NOT NULL,
  `contact_first_name` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `sales_rep_employee_number` bigint DEFAULT NULL,
  `credit_limit` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`customer_number`),
  KEY `sales_rep_employee_number` (`sales_rep_employee_number`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`sales_rep_employee_number`) REFERENCES `employee` (`employee_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `customerdetail` */

CREATE TABLE `customerdetail` (
  `customer_number` bigint NOT NULL,
  `address_line_first` varchar(50) NOT NULL,
  `address_line_second` varchar(50) DEFAULT NULL,
  `city` varchar(50),
  `state` varchar(50) DEFAULT NULL,
  `postal_code` varchar(15) DEFAULT NULL,
  `country` varchar(50),
  PRIMARY KEY (`customer_number`),
  KEY `customer_number` (`customer_number`),
  CONSTRAINT `customers_details_ibfk_1` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `manager` */

CREATE TABLE `manager` (
  `manager_id` bigint NOT NULL AUTO_INCREMENT,
  `manager_name` varchar(50) NOT NULL,
  PRIMARY KEY (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `office_has_manager` */

CREATE TABLE `office_has_manager` (
  `offices_office_code` varchar(10) NOT NULL,
  `managers_manager_id` bigint NOT NULL,
  PRIMARY KEY (`offices_office_code`, `managers_manager_id`),
  INDEX `fk_offices_has_managers_managers1_idx` (`managers_manager_id` ASC) VISIBLE,
  INDEX `fk_offices_has_managers_offices_idx` (`offices_office_code` ASC) VISIBLE,
  CONSTRAINT `fk_offices_has_managers_offices`
    FOREIGN KEY (`offices_office_code`)
    REFERENCES `office` (`office_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offices_has_managers_managers1`
    FOREIGN KEY (`managers_manager_id`)
    REFERENCES `manager` (`manager_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `productline` */

CREATE TABLE `productline` (
  `product_line` varchar(50) NOT NULL,
  `code` bigint NOT NULL,
  `text_description` varchar(4000) DEFAULT NULL,
  `html_description` mediumtext DEFAULT NULL,
  `image` mediumblob DEFAULT NULL,
  `created_on` date DEFAULT (CURRENT_DATE),
  PRIMARY KEY (`product_line`,`code`),
  CONSTRAINT unique_product_line UNIQUE(product_line)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `productdetail` */

CREATE TABLE `productlinedetail` (
  `product_line` varchar(50) NOT NULL,
  `code` bigint NOT NULL,
  `line_capacity` varchar(20) NOT NULL,
  `line_type` int DEFAULT 0,
  PRIMARY KEY (`product_line`,`code`),  
  CONSTRAINT unique_product_line_detail UNIQUE(product_line),
  CONSTRAINT `productlinedetail_ibfk_1` FOREIGN KEY (`product_line`,`code`) REFERENCES `productline` (`product_line`,`code`),
  CONSTRAINT `productlinedetail_ibfk_2` FOREIGN KEY (`product_line`) REFERENCES `productline` (`product_line`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


/*Table structure for table `product` */

CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) DEFAULT NULL,
  `product_line` varchar(50) DEFAULT NULL,
  `product_scale` varchar(10) DEFAULT NULL,
  `product_vendor` varchar(50) DEFAULT NULL,
  `product_description` text DEFAULT NULL,
  `quantity_in_stock` smallint DEFAULT 0,
  `buy_price` decimal(10,2) DEFAULT 0.0,
  `msrp` decimal(10,2) DEFAULT 0.0,
  PRIMARY KEY (`product_id`),
  KEY `product_line` (`product_line`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`product_line`) REFERENCES `productline` (`product_line`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `order` */

CREATE TABLE `order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` date NOT NULL,
  `required_date` date NOT NULL,
  `shipped_date` date DEFAULT NULL,
  `status` varchar(15) NOT NULL,
  `comments` text,
  `customer_number` bigint NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `customer_number` (`customer_number`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `orderdetail` */

CREATE TABLE `orderdetail` (
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity_ordered` int NOT NULL,
  `price_each` decimal(10,2) NOT NULL,
  `order_line_number` smallint NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
  CONSTRAINT `orderdetails_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `payment` */

CREATE TABLE `payment` (
  `customer_number` bigint NOT NULL,
  `check_number` varchar(50) NOT NULL,
  `payment_date` timestamp NOT NULL,
  `invoice_amount` decimal(10,2) NOT NULL,
  `caching_date` timestamp DEFAULT NULL,
  PRIMARY KEY (`customer_number`,`check_number`),
  CONSTRAINT `unique_check_number` UNIQUE (`check_number`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`customer_number`) REFERENCES `customer` (`customer_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* USER-DEFINED FUNCTIONS */
DELIMITER $$

CREATE FUNCTION CustomerLevel(
	credit DECIMAL(10,2)
) 
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE customerLevel VARCHAR(20);

    IF credit > 50000 THEN
		SET customerLevel = 'PLATINUM';
    ELSEIF (credit >= 50000 AND 
			credit <= 100000) THEN
        SET customerLevel = 'GOLD';
    ELSEIF credit < 10000 THEN
        SET customerLevel = 'SILVER';
    END IF;
	-- return the customer level
	RETURN (customerLevel);
END$$
DELIMITER ;

/* END */