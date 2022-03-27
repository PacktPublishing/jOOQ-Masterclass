USE `db1`;
DROP TABLE IF EXISTS `productline`;
CREATE TABLE `productline` (
  `product_line` varchar(50) NOT NULL,
  `code` bigint NOT NULL,  
  CONSTRAINT `productline_pk` PRIMARY KEY (`product_line`)  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE DATABASE IF NOT EXISTS db2;
USE `db2`;
DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_line` varchar(50) DEFAULT NULL,
  `product_name` varchar(70) DEFAULT NULL,
  `quantity_in_stock` int DEFAULT 0,
  CONSTRAINT `product_pk` PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;