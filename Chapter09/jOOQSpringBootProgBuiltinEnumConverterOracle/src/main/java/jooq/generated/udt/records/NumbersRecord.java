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
import org.jooq.impl.SQLDataType;


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
public class NumbersRecord extends ArrayRecordImpl<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #NumbersRecord()} constructor instead
     */
    @java.lang.Deprecated
    public NumbersRecord(Configuration configuration) {
        super(System.SYSTEM, "NUMBERS", SQLDataType.BIGINT, configuration);
    }

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #NumbersRecord()} constructor instead
     */
    @java.lang.Deprecated
    public NumbersRecord(Configuration configuration, Long... array) {
        this(configuration);
        set(array);
    }

    /**
     * @deprecated - 3.4.0 - [#3126] - Use the {@link #NumbersRecord()} constructor instead
     */
    @java.lang.Deprecated
    public NumbersRecord(Configuration configuration, List<? extends Long> list) {
        this(configuration);
        setList(list);
    }

    /**
     * Create a new <code>SYSTEM.NUMBERS</code> record
     */
    public NumbersRecord() {
        super(System.SYSTEM, "NUMBERS", SQLDataType.BIGINT);
    }

    /**
     * Create a new <code>SYSTEM.NUMBERS</code> record
     */
    public NumbersRecord(Long... array) {
        this();
        if (array != null)
            addAll(Arrays.asList(array));
    }

    /**
     * Create a new <code>SYSTEM.NUMBERS</code> record
     */
    public NumbersRecord(Collection<? extends Long> collection) {
        this();
        addAll(collection);
    }
}
