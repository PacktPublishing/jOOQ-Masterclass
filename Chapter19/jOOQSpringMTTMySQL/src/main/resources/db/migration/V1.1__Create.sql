USE `development`;
DROP TABLE IF EXISTS `product_dev`;
CREATE TABLE `product_dev` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) DEFAULT NULL,
  `quantity_in_stock` int DEFAULT 0,
  CONSTRAINT `product_pk` PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE DATABASE IF NOT EXISTS stage;
USE `stage`;
DROP TABLE IF EXISTS `product_stage`;

CREATE TABLE `product_stage` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) DEFAULT NULL,
  `quantity_in_stock` int DEFAULT 0,
  CONSTRAINT `product_pk` PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE DATABASE IF NOT EXISTS test;
USE `test`;
DROP TABLE IF EXISTS `product_test`;

CREATE TABLE `product_test` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(70) DEFAULT NULL,
  `quantity_in_stock` int DEFAULT 0,
  CONSTRAINT `product_pk` PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

USE `development`;