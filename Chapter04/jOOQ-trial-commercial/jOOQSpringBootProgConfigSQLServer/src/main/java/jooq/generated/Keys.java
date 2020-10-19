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
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>SYSTEM</code> schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5",
        "schema version:1.1"
    },
    date = "2020-10-19T14:04:39.558Z",
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CustomerRecord> SYS_C0011260 = UniqueKeys0.SYS_C0011260;
    public static final UniqueKey<CustomerdetailRecord> SYS_C0011266 = UniqueKeys0.SYS_C0011266;
    public static final UniqueKey<EmployeeRecord> SYS_C0011247 = UniqueKeys0.SYS_C0011247;
    public static final UniqueKey<ManagerRecord> SYS_C0011270 = UniqueKeys0.SYS_C0011270;
    public static final UniqueKey<OfficeRecord> SYS_C0011238 = UniqueKeys0.SYS_C0011238;
    public static final UniqueKey<OfficeHasManagerRecord> OFFICES_MANAGERS_PKEY = UniqueKeys0.OFFICES_MANAGERS_PKEY;
    public static final UniqueKey<OrderRecord> SYS_C0011292 = UniqueKeys0.SYS_C0011292;
    public static final UniqueKey<OrderdetailRecord> SYS_C0011299 = UniqueKeys0.SYS_C0011299;
    public static final UniqueKey<PaymentRecord> SYS_C0011306 = UniqueKeys0.SYS_C0011306;
    public static final UniqueKey<ProductRecord> SYS_C0011285 = UniqueKeys0.SYS_C0011285;
    public static final UniqueKey<ProductlineRecord> SYS_C0011275 = UniqueKeys0.SYS_C0011275;
    public static final UniqueKey<SaleRecord> SYS_C0011253 = UniqueKeys0.SYS_C0011253;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<CustomerRecord, EmployeeRecord> CUSTOMERS_IBFK_1 = ForeignKeys0.CUSTOMERS_IBFK_1;
    public static final ForeignKey<CustomerdetailRecord, CustomerRecord> CUSTOMERS_DETAILS_IBFK_1 = ForeignKeys0.CUSTOMERS_DETAILS_IBFK_1;
    public static final ForeignKey<EmployeeRecord, OfficeRecord> EMPLOYEES_IBFK_2 = ForeignKeys0.EMPLOYEES_IBFK_2;
    public static final ForeignKey<EmployeeRecord, EmployeeRecord> EMPLOYEES_IBFK_1 = ForeignKeys0.EMPLOYEES_IBFK_1;
    public static final ForeignKey<OfficeHasManagerRecord, OfficeRecord> SYS_C0011272 = ForeignKeys0.SYS_C0011272;
    public static final ForeignKey<OfficeHasManagerRecord, ManagerRecord> SYS_C0011273 = ForeignKeys0.SYS_C0011273;
    public static final ForeignKey<OrderRecord, CustomerRecord> ORDERS_IBFK_1 = ForeignKeys0.ORDERS_IBFK_1;
    public static final ForeignKey<OrderdetailRecord, OrderRecord> ORDERDETAILS_IBFK_1 = ForeignKeys0.ORDERDETAILS_IBFK_1;
    public static final ForeignKey<OrderdetailRecord, ProductRecord> ORDERDETAILS_IBFK_2 = ForeignKeys0.ORDERDETAILS_IBFK_2;
    public static final ForeignKey<PaymentRecord, CustomerRecord> PAYMENTS_IBFK_1 = ForeignKeys0.PAYMENTS_IBFK_1;
    public static final ForeignKey<ProductRecord, ProductlineRecord> PRODUCTS_IBFK_1 = ForeignKeys0.PRODUCTS_IBFK_1;
    public static final ForeignKey<SaleRecord, EmployeeRecord> SALES_IBFK_1 = ForeignKeys0.SALES_IBFK_1;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<CustomerRecord> SYS_C0011260 = Internal.createUniqueKey(Customer.CUSTOMER, "SYS_C0011260", new TableField[] { Customer.CUSTOMER.CUSTOMER_NUMBER }, true);
        public static final UniqueKey<CustomerdetailRecord> SYS_C0011266 = Internal.createUniqueKey(Customerdetail.CUSTOMERDETAIL, "SYS_C0011266", new TableField[] { Customerdetail.CUSTOMERDETAIL.CUSTOMER_NUMBER }, true);
        public static final UniqueKey<EmployeeRecord> SYS_C0011247 = Internal.createUniqueKey(Employee.EMPLOYEE, "SYS_C0011247", new TableField[] { Employee.EMPLOYEE.EMPLOYEE_NUMBER }, true);
        public static final UniqueKey<ManagerRecord> SYS_C0011270 = Internal.createUniqueKey(Manager.MANAGER, "SYS_C0011270", new TableField[] { Manager.MANAGER.MANAGER_ID }, true);
        public static final UniqueKey<OfficeRecord> SYS_C0011238 = Internal.createUniqueKey(Office.OFFICE, "SYS_C0011238", new TableField[] { Office.OFFICE.OFFICE_CODE }, true);
        public static final UniqueKey<OfficeHasManagerRecord> OFFICES_MANAGERS_PKEY = Internal.createUniqueKey(OfficeHasManager.OFFICE_HAS_MANAGER, "OFFICES_MANAGERS_PKEY", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE, OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID }, true);
        public static final UniqueKey<OrderRecord> SYS_C0011292 = Internal.createUniqueKey(Order.ORDER, "SYS_C0011292", new TableField[] { Order.ORDER.ORDER_ID }, true);
        public static final UniqueKey<OrderdetailRecord> SYS_C0011299 = Internal.createUniqueKey(Orderdetail.ORDERDETAIL, "SYS_C0011299", new TableField[] { Orderdetail.ORDERDETAIL.ORDER_ID, Orderdetail.ORDERDETAIL.PRODUCT_ID }, true);
        public static final UniqueKey<PaymentRecord> SYS_C0011306 = Internal.createUniqueKey(Payment.PAYMENT, "SYS_C0011306", new TableField[] { Payment.PAYMENT.CUSTOMER_NUMBER, Payment.PAYMENT.CHECK_NUMBER }, true);
        public static final UniqueKey<ProductRecord> SYS_C0011285 = Internal.createUniqueKey(Product.PRODUCT, "SYS_C0011285", new TableField[] { Product.PRODUCT.PRODUCT_ID }, true);
        public static final UniqueKey<ProductlineRecord> SYS_C0011275 = Internal.createUniqueKey(Productline.PRODUCTLINE, "SYS_C0011275", new TableField[] { Productline.PRODUCTLINE.PRODUCT_LINE }, true);
        public static final UniqueKey<SaleRecord> SYS_C0011253 = Internal.createUniqueKey(Sale.SALE, "SYS_C0011253", new TableField[] { Sale.SALE.SALE_ID }, true);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<CustomerRecord, EmployeeRecord> CUSTOMERS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011247, Customer.CUSTOMER, "CUSTOMERS_IBFK_1", new TableField[] { Customer.CUSTOMER.SALES_REP_EMPLOYEE_NUMBER }, true);
        public static final ForeignKey<CustomerdetailRecord, CustomerRecord> CUSTOMERS_DETAILS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011260, Customerdetail.CUSTOMERDETAIL, "CUSTOMERS_DETAILS_IBFK_1", new TableField[] { Customerdetail.CUSTOMERDETAIL.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<EmployeeRecord, OfficeRecord> EMPLOYEES_IBFK_2 = Internal.createForeignKey(Keys.SYS_C0011238, Employee.EMPLOYEE, "EMPLOYEES_IBFK_2", new TableField[] { Employee.EMPLOYEE.OFFICE_CODE }, true);
        public static final ForeignKey<EmployeeRecord, EmployeeRecord> EMPLOYEES_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011247, Employee.EMPLOYEE, "EMPLOYEES_IBFK_1", new TableField[] { Employee.EMPLOYEE.REPORTS_TO }, true);
        public static final ForeignKey<OfficeHasManagerRecord, OfficeRecord> SYS_C0011272 = Internal.createForeignKey(Keys.SYS_C0011238, OfficeHasManager.OFFICE_HAS_MANAGER, "SYS_C0011272", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE }, true);
        public static final ForeignKey<OfficeHasManagerRecord, ManagerRecord> SYS_C0011273 = Internal.createForeignKey(Keys.SYS_C0011270, OfficeHasManager.OFFICE_HAS_MANAGER, "SYS_C0011273", new TableField[] { OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID }, true);
        public static final ForeignKey<OrderRecord, CustomerRecord> ORDERS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011260, Order.ORDER, "ORDERS_IBFK_1", new TableField[] { Order.ORDER.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<OrderdetailRecord, OrderRecord> ORDERDETAILS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011292, Orderdetail.ORDERDETAIL, "ORDERDETAILS_IBFK_1", new TableField[] { Orderdetail.ORDERDETAIL.ORDER_ID }, true);
        public static final ForeignKey<OrderdetailRecord, ProductRecord> ORDERDETAILS_IBFK_2 = Internal.createForeignKey(Keys.SYS_C0011285, Orderdetail.ORDERDETAIL, "ORDERDETAILS_IBFK_2", new TableField[] { Orderdetail.ORDERDETAIL.PRODUCT_ID }, true);
        public static final ForeignKey<PaymentRecord, CustomerRecord> PAYMENTS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011260, Payment.PAYMENT, "PAYMENTS_IBFK_1", new TableField[] { Payment.PAYMENT.CUSTOMER_NUMBER }, true);
        public static final ForeignKey<ProductRecord, ProductlineRecord> PRODUCTS_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011275, Product.PRODUCT, "PRODUCTS_IBFK_1", new TableField[] { Product.PRODUCT.PRODUCT_LINE }, true);
        public static final ForeignKey<SaleRecord, EmployeeRecord> SALES_IBFK_1 = Internal.createForeignKey(Keys.SYS_C0011247, Sale.SALE, "SALES_IBFK_1", new TableField[] { Sale.SALE.EMPLOYEE_NUMBER }, true);
    }
}
