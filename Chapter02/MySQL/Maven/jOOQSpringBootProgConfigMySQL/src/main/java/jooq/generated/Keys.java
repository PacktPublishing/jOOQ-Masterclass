/*
 * This file is generated by jOOQ.
 */
package jooq.generated;


import javax.annotation.processing.Generated;

import jooq.generated.tables.Customer;
import jooq.generated.tables.Customerdetail;
import jooq.generated.tables.Employee;
import jooq.generated.tables.Manager;
import jooq.generated.tables.Office;
import jooq.generated.tables.OfficeHasManager;
import jooq.generated.tables.Order;
import jooq.generated.tables.Orderdetail;
import jooq.generated.tables.Payment;
import jooq.generated.tables.Product;
import jooq.generated.tables.Productline;
import jooq.generated.tables.Sale;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.ManagerRecord;
import jooq.generated.tables.records.OfficeHasManagerRecord;
import jooq.generated.tables.records.OfficeRecord;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.OrderdetailRecord;
import jooq.generated.tables.records.PaymentRecord;
import jooq.generated.tables.records.ProductRecord;
import jooq.generated.tables.records.ProductlineRecord;
import jooq.generated.tables.records.SaleRecord;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>classicmodels</code> schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.4",
        "schema version:1.1"
    },
    date = "2020-10-19T13:27:12.864Z",
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<ManagerRecord, Long> IDENTITY_MANAGER = Identities0.IDENTITY_MANAGER;
    public static final Identity<OrderRecord, Long> IDENTITY_ORDER = Identities0.IDENTITY_ORDER;
    public static final Identity<ProductRecord, Long> IDENTITY_PRODUCT = Identities0.IDENTITY_PRODUCT;
    public static final Identity<SaleRecord, Long> IDENTITY_SALE = Identities0.IDENTITY_SALE;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CustomerRecord> KEY_CUSTOMER_PRIMARY = UniqueKeys0.KEY_CUSTOMER_PRIMARY;
    public static final UniqueKey<CustomerdetailRecord> KEY_CUSTOMERDETAIL_PRIMARY = UniqueKeys0.KEY_CUSTOMERDETAIL_PRIMARY;
    public static final UniqueKey<EmployeeRecord> KEY_EMPLOYEE_PRIMARY = UniqueKeys0.KEY_EMPLOYEE_PRIMARY;
    public static final UniqueKey<ManagerRecord> KEY_MANAGER_PRIMARY = UniqueKeys0.KEY_MANAGER_PRIMARY;
    public static final UniqueKey<OfficeRecord> KEY_OFFICE_PRIMARY = UniqueKeys0.KEY_OFFICE_PRIMARY;
    public static final UniqueKey<OfficeHasManagerRecord> KEY_OFFICE_HAS_MANAGER_PRIMARY = UniqueKeys0.KEY_OFFICE_HAS_MANAGER_PRIMARY;
    public static final UniqueKey<OrderRecord> KEY_ORDER_PRIMARY = UniqueKeys0.KEY_ORDER_PRIMARY;
    public static final UniqueKey<OrderdetailRecord> KEY_ORDERDETAIL_PRIMARY = UniqueKeys0.KEY_ORDERDETAIL_PRIMARY;
    public static final UniqueKey<PaymentRecord> KEY_PAYMENT_PRIMARY = UniqueKeys0.KEY_PAYMENT_PRIMARY;
    public static final UniqueKey<ProductRecord> KEY_PRODUCT_PRIMARY = UniqueKeys0.KEY_PRODUCT_PRIMARY;
    public static final UniqueKey<ProductlineRecord> KEY_PRODUCTLINE_PRIMARY = UniqueKeys0.KEY_PRODUCTLINE_PRIMARY;
    public static final UniqueKey<SaleRecord> KEY_SALE_PRIMARY = UniqueKeys0.KEY_SALE_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<CustomerRecord, EmployeeRecord> CUSTOMERS_IBFK_1 = ForeignKeys0.CUSTOMERS_IBFK_1;
    public static final ForeignKey<CustomerdetailRecord, CustomerRecord> CUSTOMERS_DETAILS_IBFK_1 = ForeignKeys0.CUSTOMERS_DETAILS_IBFK_1;
    public static final ForeignKey<EmployeeRecord, OfficeRecord> EMPLOYEES_IBFK_2 = ForeignKeys0.EMPLOYEES_IBFK_2;
    public static final ForeignKey<EmployeeRecord, EmployeeRecord> EMPLOYEES_IBFK_1 = ForeignKeys0.EMPLOYEES_IBFK_1;
    public static final ForeignKey<OfficeHasManagerRecord, OfficeRecord> FK_OFFICES_HAS_MANAGERS_OFFICES = ForeignKeys0.FK_OFFICES_HAS_MANAGERS_OFFICES;
    public static final ForeignKey<OfficeHasManagerRecord, ManagerRecord> FK_OFFICES_HAS_MANAGERS_MANAGERS1 = ForeignKeys0.FK_OFFICES_HAS_MANAGERS_MANAGERS1;
    public static final ForeignKey<OrderRecord, CustomerRecord> ORDERS_IBFK_1 = ForeignKeys0.ORDERS_IBFK_1;
    public static final ForeignKey<OrderdetailRecord, OrderRecord> ORDERDETAILS_IBFK_1 = ForeignKeys0.ORDERDETAILS_IBFK_1;
    public static final ForeignKey<OrderdetailRecord, ProductRecord> ORDERDETAILS_IBFK_2 = ForeignKeys0.ORDERDETAILS_IBFK_2;
    public static final ForeignKey<PaymentRecord, CustomerRecord> PAYMENTS_IBFK_1 = ForeignKeys0.PAYMENTS_IBFK_1;
    public static final ForeignKey<ProductRecord, ProductlineRecord> PRODUCTS_IBFK_1 = ForeignKeys0.PRODUCTS_IBFK_1;
    public static final ForeignKey<SaleRecord, EmployeeRecord> SALES_IBFK_1 = ForeignKeys0.SALES_IBFK_1;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<ManagerRecord, Long> IDENTITY_MANAGER = Internal.createIdentity(Manager.MANAGER, Manager.MANAGER.MANAGER_ID);
        public static Identity<OrderRecord, Long> IDENTITY_ORDER = Internal.createIdentity(Order.ORDER, Order.ORDER.ORDER_ID);
        public static Identity<ProductRecord, Long> IDENTITY_PRODUCT = Internal.createIdentity(Product.PRODUCT, Product.PRODUCT.PRODUCT_ID);
        public static Identity<SaleRecord, Long> IDENTITY_SALE = Internal.createIdentity(Sale.SALE, Sale.SALE.SALE_ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<CustomerRecord> KEY_CUSTOMER_PRIMARY = Internal.createUniqueKey(Customer.CUSTOMER, "KEY_customer_PRIMARY", new TableField[] { Customer.CUSTOMER.CUSTOMER_NUMBER }, true);
        public static final UniqueKey<CustomerdetailRecord> KEY_CUSTOMERDETAIL_PRIMARY = Internal.createUniqueKey(Customerdetail.CUSTOMERDETAIL, "KEY_customerdetail_PRIMARY", new TableField[] { Customerdetail.CUSTOMERDETAIL.CUSTOMER_NUMBER }, true);
        public static final UniqueKey<EmployeeRecord> KEY_EMPLOYEE_PRIMARY = Internal.createUniqueKey(Employee.EMPLOYEE, "KEY_employee_PRIMARY", new TableField[] { Employee.EMPLOYEE.EMPLOYEE_NUMBER }, true);
        public static final UniqueKey<ManagerRecord> KEY_MANAGER_PRIMARY = Internal.createUniqueKey(Manager.MANAGER, "KEY_manager_PRIMARY", new TableField[] { Manager.MANAGER.MANAGER_ID }, true);
        public static final UniqueKey<OfficeRecord> KEY_OFFICE_PRIMARY = Internal.createUniqueKey(Office.OFFICE, "KEY_office_PRIMARY", new TableField[] { Office.OFFICE.OFFICE_CODE }, true);
        public static final UniqueKey<OfficeHasManagerRecord> KEY_OFFICE_HAS_MANAGER_PRIMARY = Internal.createUniqueKey(OfficeHasManager.OFFICE_HAS_MANAGER, "KEY_office_has_manager_PRIMARY", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE, OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID }, true);
        public static final UniqueKey<OrderRecord> KEY_ORDER_PRIMARY = Internal.createUniqueKey(Order.ORDER, "KEY_order_PRIMARY", new TableField[] { Order.ORDER.ORDER_ID }, true);
        public static final UniqueKey<OrderdetailRecord> KEY_ORDERDETAIL_PRIMARY = Internal.createUniqueKey(Orderdetail.ORDERDETAIL, "KEY_orderdetail_PRIMARY", new TableField[] { Orderdetail.ORDERDETAIL.ORDER_ID, Orderdetail.ORDERDETAIL.PRODUCT_ID }, true);
        public static final UniqueKey<PaymentRecord> KEY_PAYMENT_PRIMARY = Internal.createUniqueKey(Payment.PAYMENT, "KEY_payment_PRIMARY", new TableField[] { Payment.PAYMENT.CUSTOMER_NUMBER, Payment.PAYMENT.CHECK_NUMBER }, true);
        public static final UniqueKey<ProductRecord> KEY_PRODUCT_PRIMARY = Internal.createUniqueKey(Product.PRODUCT, "KEY_product_PRIMARY", new TableField[] { Product.PRODUCT.PRODUCT_ID }, true);
        public static final UniqueKey<ProductlineRecord> KEY_PRODUCTLINE_PRIMARY = Internal.createUniqueKey(Productline.PRODUCTLINE, "KEY_productline_PRIMARY", new TableField[] { Productline.PRODUCTLINE.PRODUCT_LINE }, true);
        public static final UniqueKey<SaleRecord> KEY_SALE_PRIMARY = Internal.createUniqueKey(Sale.SALE, "KEY_sale_PRIMARY", new TableField[] { Sale.SALE.SALE_ID }, true);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<CustomerRecord, EmployeeRecord> CUSTOMERS_IBFK_1 = Internal.createForeignKey(Keys.KEY_EMPLOYEE_PRIMARY, Customer.CUSTOMER, "customers_ibfk_1", new TableField[] { Customer.CUSTOMER.SALES_REP_EMPLOYEE_NUMBER }, true);
        public static final ForeignKey<CustomerdetailRecord, CustomerRecord> CUSTOMERS_DETAILS_IBFK_1 = Internal.createForeignKey(Keys.KEY_CUSTOMER_PRIMARY, Customerdetail.CUSTOMERDETAIL, "customers_details_ibfk_1", new TableField[] { Customerdetail.CUSTOMERDETAIL.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<EmployeeRecord, OfficeRecord> EMPLOYEES_IBFK_2 = Internal.createForeignKey(Keys.KEY_OFFICE_PRIMARY, Employee.EMPLOYEE, "employees_ibfk_2", new TableField[] { Employee.EMPLOYEE.OFFICE_CODE }, true);
        public static final ForeignKey<EmployeeRecord, EmployeeRecord> EMPLOYEES_IBFK_1 = Internal.createForeignKey(Keys.KEY_EMPLOYEE_PRIMARY, Employee.EMPLOYEE, "employees_ibfk_1", new TableField[] { Employee.EMPLOYEE.REPORTS_TO }, true);
        public static final ForeignKey<OfficeHasManagerRecord, OfficeRecord> FK_OFFICES_HAS_MANAGERS_OFFICES = Internal.createForeignKey(Keys.KEY_OFFICE_PRIMARY, OfficeHasManager.OFFICE_HAS_MANAGER, "fk_offices_has_managers_offices", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE }, true);
        public static final ForeignKey<OfficeHasManagerRecord, ManagerRecord> FK_OFFICES_HAS_MANAGERS_MANAGERS1 = Internal.createForeignKey(Keys.KEY_MANAGER_PRIMARY, OfficeHasManager.OFFICE_HAS_MANAGER, "fk_offices_has_managers_managers1", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID }, true);
        public static final ForeignKey<OrderRecord, CustomerRecord> ORDERS_IBFK_1 = Internal.createForeignKey(Keys.KEY_CUSTOMER_PRIMARY, Order.ORDER, "orders_ibfk_1", new TableField[] { Order.ORDER.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<OrderdetailRecord, OrderRecord> ORDERDETAILS_IBFK_1 = Internal.createForeignKey(Keys.KEY_ORDER_PRIMARY, Orderdetail.ORDERDETAIL, "orderdetails_ibfk_1", new TableField[] { Orderdetail.ORDERDETAIL.ORDER_ID }, true);
        public static final ForeignKey<OrderdetailRecord, ProductRecord> ORDERDETAILS_IBFK_2 = Internal.createForeignKey(Keys.KEY_PRODUCT_PRIMARY, Orderdetail.ORDERDETAIL, "orderdetails_ibfk_2", new TableField[] { Orderdetail.ORDERDETAIL.PRODUCT_ID }, true);
        public static final ForeignKey<PaymentRecord, CustomerRecord> PAYMENTS_IBFK_1 = Internal.createForeignKey(Keys.KEY_CUSTOMER_PRIMARY, Payment.PAYMENT, "payments_ibfk_1", new TableField[] { Payment.PAYMENT.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<ProductRecord, ProductlineRecord> PRODUCTS_IBFK_1 = Internal.createForeignKey(Keys.KEY_PRODUCTLINE_PRIMARY, Product.PRODUCT, "products_ibfk_1", new TableField[] { Product.PRODUCT.PRODUCT_LINE }, true);
        public static final ForeignKey<SaleRecord, EmployeeRecord> SALES_IBFK_1 = Internal.createForeignKey(Keys.KEY_EMPLOYEE_PRIMARY, Sale.SALE, "sales_ibfk_1", new TableField[] { Sale.SALE.EMPLOYEE_NUMBER }, true);
    }
}