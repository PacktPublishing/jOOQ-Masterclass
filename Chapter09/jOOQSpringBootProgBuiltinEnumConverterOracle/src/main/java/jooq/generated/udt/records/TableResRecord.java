/*
 * This file is generated by jOOQ.
 */
package jooq.generated.udt.records;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Generated;

import jooq.generated.System;

import org.jooq.Configuration;
import org.jooq.impl.ArrayRecordImpl;


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
public class TableResRecord extends ArrayRecordImpl<TableResObjRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #TableResRecord()} constructor instead
     */
    @java.lang.Deprecated
    public TableResRecord(Configuration configuration) {
        super(System.SYSTEM, "TABLE_RES", jooq.generated.udt.TableResObj.TABLE_RES_OBJ.getDataType(), configuration);
    }

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #TableResRecord()} constructor instead
     */
    @java.lang.Deprecated
    public TableResRecord(Configuration configuration, TableResObjRecord... array) {
        this(configuration);
        set(array);
    }

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #TableResRecord()} constructor instead
     */
    @java.lang.Deprecated
    public TableResRecord(Configuration configuration, List<? extends TableResObjRecord> list) {
        this(configuration);
        setList(list);
    }

    /**
     * Create a new <code>SYSTEM.TABLE_RES</code> record
     */
    public TableResRecord() {
        super(System.SYSTEM, "TABLE_RES", jooq.generated.udt.TableResObj.TABLE_RES_OBJ.getDataType());
    }

    /**
     * Create a new <code>SYSTEM.TABLE_RES</code> record
     */
    public TableResRecord(TableResObjRecord... array) {
        this();
        if (array != null)
            addAll(Arrays.asList(array));
    }

    /**
     * Create a new <code>SYSTEM.TABLE_RES</code> record
     */
    public TableResRecord(Collection<? extends TableResObjRecord> collection) {
        this();
        addAll(collection);
    }
}
