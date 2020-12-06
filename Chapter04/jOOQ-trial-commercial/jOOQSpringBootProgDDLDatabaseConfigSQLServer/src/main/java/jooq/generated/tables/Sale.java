/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables;


import java.util.Arrays;
import java.util.List;

import jooq.generated.DefaultSchema;
import jooq.generated.Indexes;
import jooq.generated.Keys;
import jooq.generated.tables.records.SaleRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sale extends TableImpl<SaleRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>sale</code>
     */
    public static final Sale SALE = new Sale();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SaleRecord> getRecordType() {
        return SaleRecord.class;
    }

    /**
     * The column <code>sale.sale_id</code>.
     */
    public final TableField<SaleRecord, Long> SALE_ID = createField(DSL.name("sale_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>sale.fiscal_year</code>.
     */
    public final TableField<SaleRecord, Integer> FISCAL_YEAR = createField(DSL.name("fiscal_year"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sale.sale</code>.
     */
    public final TableField<SaleRecord, Double> SALE_ = createField(DSL.name("sale"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>sale.employee_number</code>.
     */
    public final TableField<SaleRecord, Long> EMPLOYEE_NUMBER = createField(DSL.name("employee_number"), SQLDataType.BIGINT.defaultValue(DSL.field("NULL", SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>sale.hot</code>.
     */
    public final TableField<SaleRecord, Boolean> HOT = createField(DSL.name("hot"), SQLDataType.BOOLEAN.defaultValue(DSL.field("0", SQLDataType.BOOLEAN)), this, "");

    private Sale(Name alias, Table<SaleRecord> aliased) {
        this(alias, aliased, null);
    }

    private Sale(Name alias, Table<SaleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>sale</code> table reference
     */
    public Sale(String alias) {
        this(DSL.name(alias), SALE);
    }

    /**
     * Create an aliased <code>sale</code> table reference
     */
    public Sale(Name alias) {
        this(alias, SALE);
    }

    /**
     * Create a <code>sale</code> table reference
     */
    public Sale() {
        this(DSL.name("sale"), null);
    }

    public <O extends Record> Sale(Table<O> child, ForeignKey<O, SaleRecord> key) {
        super(child, key, SALE);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EMPLOYEE_NUMBER);
    }

    @Override
    public Identity<SaleRecord, Long> getIdentity() {
        return (Identity<SaleRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<SaleRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_35;
    }

    @Override
    public List<UniqueKey<SaleRecord>> getKeys() {
        return Arrays.<UniqueKey<SaleRecord>>asList(Keys.CONSTRAINT_35);
    }

    @Override
    public List<ForeignKey<SaleRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SaleRecord, ?>>asList(Keys.SALES_IBFK_1);
    }

    public Employee employee() {
        return new Employee(this, Keys.SALES_IBFK_1);
    }

    @Override
    public Sale as(String alias) {
        return new Sale(DSL.name(alias), this);
    }

    @Override
    public Sale as(Name alias) {
        return new Sale(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Sale rename(String name) {
        return new Sale(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Sale rename(Name name) {
        return new Sale(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Double, Long, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
