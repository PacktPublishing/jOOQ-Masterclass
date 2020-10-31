/*
 * This file is generated by jOOQ.
 */
package jooq.generated.master.dbo.tables;


import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import jooq.generated.master.dbo.Dbo;
import jooq.generated.master.dbo.tables.records.SptMonitorRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row11;
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
public class SptMonitor extends TableImpl<SptMonitorRecord> {

    private static final long serialVersionUID = -1639221783;

    /**
     * The reference instance of <code>master.dbo.spt_monitor</code>
     */
    public static final SptMonitor SPT_MONITOR = new SptMonitor();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SptMonitorRecord> getRecordType() {
        return SptMonitorRecord.class;
    }

    /**
     * The column <code>master.dbo.spt_monitor.lastrun</code>.
     */
    public final TableField<SptMonitorRecord, LocalDateTime> LASTRUN = createField(DSL.name("lastrun"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.cpu_busy</code>.
     */
    public final TableField<SptMonitorRecord, Integer> CPU_BUSY = createField(DSL.name("cpu_busy"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.io_busy</code>.
     */
    public final TableField<SptMonitorRecord, Integer> IO_BUSY = createField(DSL.name("io_busy"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.idle</code>.
     */
    public final TableField<SptMonitorRecord, Integer> IDLE = createField(DSL.name("idle"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.pack_received</code>.
     */
    public final TableField<SptMonitorRecord, Integer> PACK_RECEIVED = createField(DSL.name("pack_received"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.pack_sent</code>.
     */
    public final TableField<SptMonitorRecord, Integer> PACK_SENT = createField(DSL.name("pack_sent"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.connections</code>.
     */
    public final TableField<SptMonitorRecord, Integer> CONNECTIONS = createField(DSL.name("connections"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.pack_errors</code>.
     */
    public final TableField<SptMonitorRecord, Integer> PACK_ERRORS = createField(DSL.name("pack_errors"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.total_read</code>.
     */
    public final TableField<SptMonitorRecord, Integer> TOTAL_READ = createField(DSL.name("total_read"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.total_write</code>.
     */
    public final TableField<SptMonitorRecord, Integer> TOTAL_WRITE = createField(DSL.name("total_write"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>master.dbo.spt_monitor.total_errors</code>.
     */
    public final TableField<SptMonitorRecord, Integer> TOTAL_ERRORS = createField(DSL.name("total_errors"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>master.dbo.spt_monitor</code> table reference
     */
    public SptMonitor() {
        this(DSL.name("spt_monitor"), null);
    }

    /**
     * Create an aliased <code>master.dbo.spt_monitor</code> table reference
     */
    public SptMonitor(String alias) {
        this(DSL.name(alias), SPT_MONITOR);
    }

    /**
     * Create an aliased <code>master.dbo.spt_monitor</code> table reference
     */
    public SptMonitor(Name alias) {
        this(alias, SPT_MONITOR);
    }

    private SptMonitor(Name alias, Table<SptMonitorRecord> aliased) {
        this(alias, aliased, null);
    }

    private SptMonitor(Name alias, Table<SptMonitorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> SptMonitor(Table<O> child, ForeignKey<O, SptMonitorRecord> key) {
        super(child, key, SPT_MONITOR);
    }

    @Override
    public Schema getSchema() {
        return Dbo.DBO;
    }

    @Override
    public SptMonitor as(String alias) {
        return new SptMonitor(DSL.name(alias), this);
    }

    @Override
    public SptMonitor as(Name alias) {
        return new SptMonitor(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SptMonitor rename(String name) {
        return new SptMonitor(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SptMonitor rename(Name name) {
        return new SptMonitor(name, null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<LocalDateTime, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }
}
