DROP TABLE IF EXISTS product;
CREATE TABLE product (
  product_id SERIAL NOT NULL,
  product_name varchar(70) DEFAULT NULL,
  quantity_in_stock int DEFAULT 0,
  CONSTRAINT product_pk PRIMARY KEY (product_id)
)