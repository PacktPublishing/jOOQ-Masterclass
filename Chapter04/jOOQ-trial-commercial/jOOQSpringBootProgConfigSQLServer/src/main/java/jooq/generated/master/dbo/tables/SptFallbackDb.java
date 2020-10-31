/*
 * This file is generated by jOOQ.
 */
package jooq.generated.master.dbo.tables;


import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import jooq.generated.master.dbo.Dbo;
import jooq.generated.master.dbo.tables.records.SptFallbackDbRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5",
        "schema version:1.1"
    },
    date = "2020-10-31T06:19:36.439Z",
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SptFallbackDb extends TableImpl<SptFallbackDbRecord> {

    private static final long serialVersionUID = 865871638;

    /**
     * The reference instance of <code>master.dbo.spt_fallback_db</code>
     */
    public static final SptFallbackDb SPT_FALLBACK_DB = new SptFallbackDb();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SptFallbackDbRecord> getRecordType() {
        return SptFallbackDbRecord.class;
    }

    /**
     * The column <code>master.dbo.spt_fallback_db.xserver_name</code>.
     */
    public final TableField<SptFallbackDbRecord, String> XSERVER_NAME = createField(DSL.name("xserver_name"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.xdttm_ins</code>.
     */
    public final TableField<SptFallbackDbRecord, LocalDateTime> XDTTM_INS = createField(DSL.name("xdttm_ins"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.xdttm_last_ins_upd</code>.
     */
    public final TableField<SptFallbackDbRecord, LocalDateTime> XDTTM_LAST_INS_UPD = createField(DSL.name("xdttm_last_ins_upd"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.xfallback_dbid</code>.
     */
    public final TableField<SptFallbackDbRecord, Short> XFALLBACK_DBID = createField(DSL.name("xfallback_dbid"), org.jooq.impl.SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.name</code>.
     */
    public final TableField<SptFallbackDbRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.dbid</code>.
     */
    public final TableField<SptFallbackDbRecord, Short> DBID = createField(DSL.name("dbid"), org.jooq.impl.SQLDataType.SMALLINT.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.status</code>.
     */
    public final TableField<SptFallbackDbRecord, Short> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.SMALLINT.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_fallback_db.version</code>.
     */
    public final TableField<SptFallbackDbRecord, Short> VERSION = createField(DSL.name("version"), org.jooq.impl.SQLDataType.SMALLINT.nullable(false), this, "");

    /**
     * Create a <code>master.dbo.spt_fallback_db</code> table reference
     */
    public SptFallbackDb() {
        this(DSL.name("spt_fallback_db"), null);
    }

    /**
     * Create an aliased <code>master.dbo.spt_fallback_db</code> table reference
     */
    public SptFallbackDb(String alias) {
        this(DSL.name(alias), SPT_FALLBACK_DB);
    }

    /**
     * Create an aliased <code>master.dbo.spt_fallback_db</code> table reference
     */
    public SptFallbackDb(Name alias) {
        this(alias, SPT_FALLBACK_DB);
    }

    private SptFallbackDb(Name alias, Table<SptFallbackDbRecord> aliased) {
        this(alias, aliased, null);
    }

    private SptFallbackDb(Name alias, Table<SptFallbackDbRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> SptFallbackDb(Table<O> child, ForeignKey<O, SptFallbackDbRecord> key) {
        super(child, key, SPT_FALLBACK_DB);
    }

    @Override
    public Schema getSchema() {
        return Dbo.DBO;
    }

    @Override
    public SptFallbackDb as(String alias) {
        return new SptFallbackDb(DSL.name(alias), this);
    }

    @Override
    public SptFallbackDb as(Name alias) {
        return new SptFallbackDb(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SptFallbackDb rename(String name) {
        return new SptFallbackDb(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SptFallbackDb rename(Name name) {
        return new SptFallbackDb(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<String, LocalDateTime, LocalDateTime, Short, String, Short, Short, Short> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
