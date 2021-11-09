USE `db1`;

insert  into `productline`(`product_line`,`code`) values 

('Classic Cars',599302),

('Motorcycles',599302),

('Planes',433823),

('Ships',433823),

('Trains',123333),

('Trucks and Buses',569331),

('Vintage Cars',223113) ON DUPLICATE KEY UPDATE product_line=product_line;

USE `db2`;

insert  into `product`(`product_id`,`product_name`,`product_line`,`quantity_in_stock`) values 

(1,'1969 Harley Davidson Ultimate Chopper','Motorcycles', 7933),

(2,'1952 Alpine Renault 1300','Classic Cars', 7305),

(3,'1996 Moto Guzzi 1100i','Motorcycles', 6625),

(4,'2003 Harley-Davidson Eagle Drag Bike','Motorcycles', 5582),

(5,'1972 Alfa Romeo GTA','Classic Cars', 3252),

(6,'1962 LanciaA Delta 16V','Classic Cars', 6791),

(9,'1958 Setra Bus','Trucks and Buses', 1579),

(15,'1957 Chevy Pickup','Trucks and Buses', 6125),

(19,'1937 Lincoln Berline','Vintage Cars', 8693),

(20,'1936 Mercedes-Benz 500K Special Roadster','Vintage Cars', 8635),

(22,'1980s Black Hawk Helicopter','Planes', 5330),

(26,'1998 Chrysler Plymouth Prowler','Classic Cars', 4724),

(28,'1964 Mercedes Tour Bus','Trucks and Buses', 8258) ON DUPLICATE KEY UPDATE product_id=product_id;