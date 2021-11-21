package com.classicmodels.provider;

import com.classicmodels.pojo.LegacyEmployee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;

public class MyRecordMapperProvider implements RecordMapperProvider {

    @Override
    public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {

        if (type == LegacyEmployee.class) {
            return (R record) -> {

                return (E) LegacyEmployee.getBuilder(record.getValue(EMPLOYEE.LAST_NAME), record.getValue(EMPLOYEE.FIRST_NAME))
                        .employeeNumber(record.getValue(EMPLOYEE.EMPLOYEE_NUMBER))
                        .extension(record.getValue(EMPLOYEE.EXTENSION))
                        .email(record.getValue(EMPLOYEE.EMAIL))
                        .officeCode(record.getValue(EMPLOYEE.OFFICE_CODE))
                        .salary(record.getValue(EMPLOYEE.SALARY))
                        .commission(record.getValue(EMPLOYEE.COMMISSION))
                        .reportsTo(record.getValue(EMPLOYEE.REPORTS_TO))
                        .jobTitle(record.getValue(EMPLOYEE.JOB_TITLE))
                        .employeeOfYear(record.getValue(EMPLOYEE.EMPLOYEE_OF_YEAR))
                        .monthlyBonus(record.getValue(EMPLOYEE.MONTHLY_BONUS))
                        .build();
            };
        }
        
        // another record mapper
        // if (type == SomeOtherType.class) { }

        // Fall back to jOOQ's DefaultRecordMapper
        return new DefaultRecordMapper(recordType, type);
    }
}
