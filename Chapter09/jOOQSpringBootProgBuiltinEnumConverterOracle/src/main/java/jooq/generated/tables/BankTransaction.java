/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import jooq.generated.Keys;
import jooq.generated.System;
import jooq.generated.tables.records.BankTransactionRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
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
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.14.4",
        "schema version:1.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankTransaction extends TableImpl<BankTransactionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>SYSTEM.BANK_TRANSACTION</code>
     */
    public static final BankTransaction BANK_TRANSACTION = new BankTransaction();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BankTransactionRecord> getRecordType() {
        return BankTransactionRecord.class;
    }

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.TRANSACTION_ID</code>.
     */
    public final TableField<BankTransactionRecord, Long> TRANSACTION_ID = createField(DSL.name("TRANSACTION_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.BANK_NAME</code>.
     */
    public final TableField<BankTransactionRecord, String> BANK_NAME = createField(DSL.name("BANK_NAME"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.BANK_IBAN</code>.
     */
    public final TableField<BankTransactionRecord, String> BANK_IBAN = createField(DSL.name("BANK_IBAN"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.TRANSFER_AMOUNT</code>.
     */
    public final TableField<BankTransactionRecord, BigDecimal> TRANSFER_AMOUNT = createField(DSL.name("TRANSFER_AMOUNT"), SQLDataType.NUMERIC(10, 2).nullable(false), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.CACHING_DATE</code>.
     */
    public final TableField<BankTransactionRecord, LocalDateTime> CACHING_DATE = createField(DSL.name("CACHING_DATE"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("SYSTIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.CUSTOMER_NUMBER</code>.
     */
    public final TableField<BankTransactionRecord, Long> CUSTOMER_NUMBER = createField(DSL.name("CUSTOMER_NUMBER"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYSTEM.BANK_TRANSACTION.CHECK_NUMBER</code>.
     */
    public final TableField<BankTransactionRecord, String> CHECK_NUMBER = createField(DSL.name("CHECK_NUMBER"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    private BankTransaction(Name alias, Table<BankTransactionRecord> aliased) {
        this(alias, aliased, null);
    }

    private BankTransaction(Name alias, Table<BankTransactionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>SYSTEM.BANK_TRANSACTION</code> table reference
     */
    public BankTransaction(String alias) {
        this(DSL.name(alias), BANK_TRANSACTION);
    }

    /**
     * Create an aliased <code>SYSTEM.BANK_TRANSACTION</code> table reference
     */
    public BankTransaction(Name alias) {
        this(alias, BANK_TRANSACTION);
    }

    /**
     * Create a <code>SYSTEM.BANK_TRANSACTION</code> table reference
     */
    public BankTransaction() {
        this(DSL.name("BANK_TRANSACTION"), null);
    }

    public <O extends Record> BankTransaction(Table<O> child, ForeignKey<O, BankTransactionRecord> key) {
        super(child, key, BANK_TRANSACTION);
    }

    @Override
    public Schema getSchema() {
        return System.SYSTEM;
    }

    @Override
    public UniqueKey<BankTransactionRecord> getPrimaryKey() {
        return Keys.SYS_C0013912;
    }

    @Override
    public List<UniqueKey<BankTransactionRecord>> getKeys() {
        return Arrays.<UniqueKey<BankTransactionRecord>>asList(Keys.SYS_C0013912);
    }

    @Override
    public List<ForeignKey<BankTransactionRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<BankTransactionRecord, ?>>asList(Keys.BANK_TRANSACTION_IBFK_1);
    }

    public Payment payment() {
        return new Payment(this, Keys.BANK_TRANSACTION_IBFK_1);
    }

    @Override
    public BankTransaction as(String alias) {
        return new BankTransaction(DSL.name(alias), this);
    }

    @Override
    public BankTransaction as(Name alias) {
        return new BankTransaction(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public BankTransaction rename(String name) {
        return new BankTransaction(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public BankTransaction rename(Name name) {
        return new BankTransaction(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, String, BigDecimal, LocalDateTime, Long, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
