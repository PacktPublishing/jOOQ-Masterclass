package com.classicmodels.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.InsertQuery;
import org.jooq.SelectField;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UpdateQuery;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public <R extends Record> List<R> select1(
            Table<R> table, Collection<TableField<R, ?>> fields, Condition... conditions) {

        SelectQuery sq = ctx.selectQuery(table);

        sq.addSelect(fields);
        sq.addConditions(conditions);

        return sq.fetch();
    }

    public <R extends Record> List<R> select2(
            Table<R> table, Collection<SelectField<?>> fields, Condition... conditions) {

        SelectQuery sq = ctx.selectQuery(table);

        sq.addSelect(fields);
        sq.addConditions(conditions);

        return sq.fetch();
    }

    @Transactional
    public <R extends Record> int insert1(Table<R> table, Map<TableField<R, ?>, ?> values) {

        InsertQuery iq = ctx.insertQuery(table);

        iq.addValues(values);

        return iq.execute();
    }

    @Transactional
    public <R extends Record> int insert2(Table<R> table, Map<SelectField<?>, ?> values) {

        InsertQuery iq = ctx.insertQuery(table);

        iq.addValues(values);

        return iq.execute();
    }

    @Transactional
    public <R extends Record> int update1(Table<R> table,
            Map<TableField<R, ?>, ?> values, Condition... conditions) {

        UpdateQuery uq = ctx.updateQuery(table);

        uq.addValues(values);
        uq.addConditions(conditions);

        return uq.execute();
    }

    @Transactional
    public <R extends Record> int update2(Table<R> table,
            Map<SelectField<?>, ?> values, Condition... conditions) {

        UpdateQuery uq = ctx.updateQuery(table);

        uq.addValues(values);
        uq.addConditions(conditions);

        return uq.execute();
    }

    @Transactional
    public <R extends Record> int delete(Table<R> table, Condition... conditions) {

        DeleteQuery dq = ctx.deleteQuery(table);

        dq.addConditions(conditions);

        return dq.execute();
    }  
    
    // More examples
    public List<Record> fetchOfficeEmployeeFields1(List<TableField<OfficeRecord, ?>> officeFields,
            List<TableField<EmployeeRecord, ?>> employeeFields) {

        SelectQuery select = ctx.select().getQuery();

        if (!officeFields.isEmpty()) {
            select.addFrom(OFFICE);
            select.addSelect(officeFields);
        }

        if (!employeeFields.isEmpty()) {
            select.addFrom(EMPLOYEE);
            select.addSelect(employeeFields);
        }

        return select.fetch();
    }

    public List<Record> fetchOfficeEmployeeFields2(List<SelectField<?>> officeFields,
            List<SelectField<?>> employeeFields) {

        SelectQuery select = ctx.select().getQuery();

        if (!officeFields.isEmpty()) {
            select.addFrom(table("office"));
            select.addSelect(officeFields);
        }

        if (!employeeFields.isEmpty()) {
            select.addFrom(table("employee"));
            select.addSelect(employeeFields);
        }

        return select.fetch();
    }

    public <R extends Record> List<R> allFieldsOfTables(int l, Table<?>... tables) {

        SelectQuery select = ctx.select().limit(l).getQuery();

        select.addFrom(tables);
        for (Table<?> table : tables) {
            select.addSelect(table.fields());
        }

        return select.fetch();
    }
    
}