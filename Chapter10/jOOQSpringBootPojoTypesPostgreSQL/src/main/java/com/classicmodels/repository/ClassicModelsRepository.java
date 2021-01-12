package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import com.classicmodels.pojo.ImmutableCustomer;
import com.classicmodels.pojo.ImmutableDepartment;
import com.classicmodels.pojo.ImmutableManager;
import com.classicmodels.pojo.ImmutableOffice;
import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.pojo.SimpleDepartment;
import com.classicmodels.pojo.SimpleEmployee;
import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.pojo.ICustomer;
import com.classicmodels.pojo.JpaCustomer;
import com.classicmodels.pojo.JpaDepartment;
import com.classicmodels.pojo.JpaManager;
import com.classicmodels.pojo.JpaOffice;
import com.classicmodels.pojo.RecordCustomer;
import com.classicmodels.pojo.RecordDepartment;
import com.classicmodels.pojo.RecordManager;
import com.classicmodels.pojo.RecordOffice;
import com.classicmodels.pojo.SimpleCustomerdetail;
import com.classicmodels.pojo.SimpleDepartmentDetail;
import com.classicmodels.pojo.SimpleManagerStatus;
import com.classicmodels.pojo.SimpleOrder;
import java.util.Map;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Order.ORDER;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // jOOQ generated POJOs almost like those from Example 1.3 via <pojos>true</pojos> 
    public void fetchSimplePojoExamples() {

        List<SimpleCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH).as("ym"))
                .from(CUSTOMER)
                .fetchInto(SimpleCustomer.class);
        System.out.println("Example 1.1\n" + result1);

        // CUSTOMER.FIRST_BUY_DATE is ignored (it needs the proper alias), so POJOs's "ym"  field is set to null
        // CUSTOMER.PHONE is ignored
        List<SimpleCustomer> result2 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH))
                .from(CUSTOMER)
                .fetchInto(SimpleCustomer.class);
        System.out.println("Example 1.2\n" + result2);

        // having a proper constructor, we can omit aliases
        List<SimpleDepartment> result3 = ctx.select(
                DEPARTMENT.NAME, DEPARTMENT.CODE, DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchInto(SimpleDepartment.class);
        System.out.println("Example 1.3\n" + result3);

        List<SimpleEmployee> result4 = ctx.select(EMPLOYEE.FIRST_NAME.as("fn"), EMPLOYEE.LAST_NAME.as("ln"),
                concat(EMPLOYEE.employee().FIRST_NAME, val(" "), EMPLOYEE.employee().LAST_NAME).as("boss"))
                .from(EMPLOYEE)
                .fetchInto(SimpleEmployee.class);
        System.out.println("Example 1.4\n" + result4);

        // fetch UDT
        List<SimpleManager> result5 = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetchInto(SimpleManager.class);
        System.out.println("Example 1.5\n" + result5);

        // fetch embeddable
        List<SimpleOffice> result6 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetchInto(SimpleOffice.class);
        System.out.println("Example 1.6\n" + result6);

        // fetch embeddable containing UDT
        // by default, a REST controller produces the following JSON:
        /*
        [
            {
                "managerId": 1,
                "ms": {
                    "managerName": "Joana Nimar",
                    "managerEvaluation": {
                        "communicationAbility": 67,
                        "ethics": 34,
                        "performance": 33,
                        "employeeInput": 66
                  }
                }
            }, ...
        ]
         */
        List<SimpleManagerStatus> result7 = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_STATUS)
                .from(MANAGER)
                .fetchInto(SimpleManagerStatus.class);
        System.out.println("Example 1.7\n" + result7);

        // fetch embeddable containing array
        // by default, a REST controller produces the following JSON:
        /*
        [
            {
                "departmentId": "1",
                "departmentDetail": {
                    "name": "Advertising",
                    "phone": "-int 4782",
                    "topic": [
                        "publicity",
                        "promotion"
                    ]
                }
            }, ...
        ]
         */
        List<SimpleDepartmentDetail> result8 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.DEPARTMENT_DETAIL)
                .from(DEPARTMENT)
                .fetchInto(SimpleDepartmentDetail.class);
        System.out.println("Example 1.8\n" + result8);

        Map<SimpleCustomer, SimpleCustomerdetail> result9 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH).as("ym"),
                CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchMap(SimpleCustomer.class, SimpleCustomerdetail.class);
        System.out.println("Example 1.9\n" + result9);

        Map<SimpleCustomer, List<SimpleOrder>> result10 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH).as("ym"),
                ORDER.ORDER_DATE, ORDER.STATUS)
                .from(CUSTOMER)
                .join(ORDER)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                .fetchGroups(SimpleCustomer.class, SimpleOrder.class);
        System.out.println("Example 1.10\n" + result10);
    }

    // This kind of POJOs are generated by jOOQ via <immutablePojos>true</immutablePojos> 
    // This will generate "immutable" POJOs for tables, UDTs, embeddable types, and so on
    public void fetchImmutablePojoExamples() {

        // require an exact match between the fetched fields and POJO's fields
        List<ImmutableCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH).as("ym"))
                .from(CUSTOMER)
                .fetchInto(ImmutableCustomer.class);
        System.out.println("Example 2.1\n" + result1);

        // DEPARTMENT.PHONE, DEPARTMENT.OFFICE_CODE - are ignored
        // since DEPARTMENT.CODE is not fetched the POJO's "code" field is set to null
        List<ImmutableDepartment> result2 = ctx.select(
                DEPARTMENT.NAME, DEPARTMENT.PHONE, DEPARTMENT.OFFICE_CODE, DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchInto(ImmutableDepartment.class);
        System.out.println("Example 2.2\n" + result2);

        // UDT
        List<ImmutableManager> result3 = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetchInto(ImmutableManager.class);
        System.out.println("Example 2.3\n" + result3);

        // embeddable type
        List<ImmutableOffice> result4 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetchInto(ImmutableOffice.class);
        System.out.println("Example 2.4\n" + result4);
    }

    // This kind of POJOs are generated by jOOQ via <jpaAnnotations>true</jpaAnnotations>
    public void fetchJpaLikePojoExamples() {

        List<JpaCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH))
                .from(CUSTOMER)
                .fetchInto(JpaCustomer.class);
        System.out.println("Example 3.1\n" + result1);

        List<JpaDepartment> result2 = ctx.select(
                DEPARTMENT.NAME, DEPARTMENT.CODE, DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchInto(JpaDepartment.class);
        System.out.println("Example 3.2\n" + result2);

        // UDT
        List<JpaManager> result3 = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetchInto(JpaManager.class);
        System.out.println("Example 3.3\n" + result3);

        // embeddable type
        List<JpaOffice> result4 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetchInto(JpaOffice.class);
        System.out.println("Example 3.4\n" + result4);
    }

    // This kind of POJOs (JDK 14 records) are generated by jOOQ via <pojosAsJavaRecordClasses>true</pojosAsJavaRecordClasses>
    public void fetchJavaRecordPojoExamples() {

        List<RecordCustomer> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH))
                .from(CUSTOMER)
                .fetchInto(RecordCustomer.class);
        System.out.println("Example 4.1\n" + result1);

        List<RecordDepartment> result2 = ctx.select(
                DEPARTMENT.NAME, DEPARTMENT.CODE, DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchInto(RecordDepartment.class);
        System.out.println("Example 4.2\n" + result2);

        // UDT
        List<RecordManager> result3 = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetchInto(RecordManager.class);
        System.out.println("Example 4.3\n" + result3);

        // embeddable type
        List<RecordOffice> result4 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetchInto(RecordOffice.class);
        System.out.println("Example 4.4\n" + result4);
    }

    // This kind of interfaces are generated by jOOQ via <interfaces>true</interfaces>
    // If POJOs are generated as well then they will implement these interfaces.
    public void fetchProxyablePojoExamples() {

        List<ICustomer> result1 = ctx.select(CUSTOMER.CUSTOMER_NAME)
                .from(CUSTOMER)
                .fetchInto(ICustomer.class);
        System.out.println("Example 5.1\n" + result1);
    }
}
