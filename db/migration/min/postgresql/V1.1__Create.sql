/* START */

/* A minimalist version of our schema */

    drop table if exists "order";
    drop table if exists customer;
    drop table if exists customerdetail;
    drop table if exists employee;
    drop table if exists manager;
    drop table if exists office;
    drop table if exists office_has_manager;
    drop table if exists orderdetail;
    drop table if exists payment;
    drop table if exists product;
    drop table if exists productline;
    drop table if exists sale;

    create table "order" (
       order_id serial not null,
        comments text,
        order_date date not null,
        required_date date not null,
        shipped_date date,
        status varchar(50) not null,
        customer_number bigint,
        primary key (order_id)
    );
    
    create table customer (
       customer_number bigint not null,
        contact_first_name varchar(50) not null,
        contact_last_name varchar(50) not null,
        credit_limit float,
        customer_name varchar(50) not null,
        phone varchar(50) not null,
        sales_rep_employee_number bigint,
        primary key (customer_number)
    );
    
    create table customerdetail (
       customer_number bigint not null,
        address_line_first varchar(50) not null,
        address_line_second varchar(50),
        city varchar(50) not null,
        country varchar(50) not null,
        postal_code varchar(15),
        state varchar(50),
        primary key (customer_number)
    );
    
    create table employee (
       employee_number bigint not null,
        email varchar(100) not null,
        extension varchar(10) not null,
        first_name varchar(50) not null,
        job_title varchar(50) not null,
        last_name varchar(50) not null,
        salary integer not null,
        office_code varchar(10),
        reports_to bigint,
        primary key (employee_number)
    );
    
    create table manager (
       manager_id serial not null,
        manager_name varchar(50) not null,
        primary key (manager_id)
    );
    
    create table office (
       office_code varchar(10) not null,
        address_line_first varchar(50) not null,
        address_line_second varchar(50),
        city varchar(50) not null,
        country varchar(50) not null,
        phone varchar(50) not null,
        postal_code varchar(15) not null,
        state varchar(50),
        territory varchar(10) not null,
        primary key (office_code)
    );
    
    create table office_has_manager (
       offices_office_code varchar(10) not null,
        managers_manager_id bigint not null,
        primary key (offices_office_code, managers_manager_id)
    );
    
    create table orderdetail (
       order_id bigint not null,
        product_id bigint not null,
        order_line_number smallint not null,
        price_each float not null,
        quantity_ordered integer not null,
        primary key (order_id, product_id)
    );
    
    create table payment (
       check_number varchar(50) not null,
        customer_number bigint not null,
        caching_date date,
        invoice_amount float not null,
        payment_date date not null,
        primary key (check_number, customer_number)
    );
    
    create table product (
       product_id serial not null,
        buy_price float not null,
        msrp float not null,
        product_description text not null,
        product_name varchar(70) not null,
        product_scale varchar(10) not null,
        product_vendor varchar(50) not null,
        quantity_in_stock smallint not null,
        product_line varchar(50),
        primary key (product_id)
    );
    
    create table productline (
       product_line varchar(50) not null,
        html_description text,
        image bytea,
        text_description varchar(4000),
        primary key (product_line)
    );
    
    create table sale (
       sale_id serial not null,
        fiscal_year integer not null,
        sale float not null,
        employee_number bigint,
        primary key (sale_id)
    );
    
    alter table "order" 
       add constraint FKro9uh5sg5ig4ei75p2h20lbby 
       foreign key (customer_number) 
       references customer (customer_number);
    
    alter table customer 
       add constraint FKfwit3pchrvpph3tvj4tufuiny 
       foreign key (sales_rep_employee_number) 
       references employee (employee_number);
    
    alter table customerdetail 
       add constraint FKpincwfvtd0nvcjwwy3d8kj5lb 
       foreign key (customer_number) 
       references customer (customer_number);
    
    alter table employee 
       add constraint FKnxm4u35m7qqbnqbvtb8bj2tk9 
       foreign key (office_code) 
       references office (office_code);
    
    alter table employee 
       add constraint FKghecv11ypswk5w7mmcof2dscg 
       foreign key (reports_to) 
       references employee (employee_number);
    
    alter table office_has_manager 
       add constraint FK628qu6nufx7jwjohq6uvc1em1 
       foreign key (managers_manager_id) 
       references manager (manager_id);
    
    alter table office_has_manager 
       add constraint FKhbn3wp02ixbhw84c3vsm7x5cs 
       foreign key (offices_office_code) 
       references office (office_code);
    
    alter table orderdetail 
       add constraint FK3qd9bud6u1ggm33opobeopnea 
       foreign key (order_id) 
       references "order" (order_id);
    
    alter table orderdetail 
       add constraint FKdubukg3j0fymgci0idnd72k0 
       foreign key (product_id) 
       references product (product_id);
    
    alter table payment 
       add constraint FK4yvong4nch792htadwtaex6dr 
       foreign key (customer_number) 
       references customer (customer_number);
    
    alter table product 
       add constraint FK71ytjxwdqw3950xadqx4e6dj3 
       foreign key (product_line) 
       references productline (product_line);
    
    alter table sale 
       add constraint FKgph5i8g999x8104dkjfysyw7h 
       foreign key (employee_number) 
       references employee (employee_number);

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