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

USE classicmodels;

DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `productline`;

/*Table structure for table `productline` */

CREATE TABLE `productline` (
  `product_line` varchar(50) NOT NULL,
  `text_description` varchar(4000) DEFAULT NULL,
  `html_description` mediumtext,
  `image` mediumblob,
  PRIMARY KEY (`product_line`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `product` */

CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) NOT NULL,
  `product_line` varchar(50) NOT NULL,
  `product_scale` varchar(10) NOT NULL,
  `product_vendor` varchar(50) NOT NULL,
  `product_description` text NOT NULL,
  `quantity_in_stock` smallint NOT NULL,
  `buy_price` decimal(10,2) NOT NULL,
  `msrp` decimal(10,2) NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `product_line` (`product_line`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`product_line`) REFERENCES `productline` (`product_line`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* END */