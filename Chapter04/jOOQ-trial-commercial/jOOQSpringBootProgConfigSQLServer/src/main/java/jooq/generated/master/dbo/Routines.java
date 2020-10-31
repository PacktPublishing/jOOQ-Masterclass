/*
 * This file is generated by jOOQ.
 */
package jooq.generated.master.dbo;


import javax.annotation.processing.Generated;

import jooq.generated.master.dbo.routines.SpMscleanupmergepublisher;
import jooq.generated.master.dbo.routines.SpMsreplStartup;

import org.jooq.Configuration;


/**
 * Convenience access to all stored procedures and functions in dbo
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
public class Routines {

    /**
     * Call <code>master.dbo.sp_MScleanupmergepublisher</code>
     */
    public static Integer spMscleanupmergepublisher(Configuration configuration) {
        SpMscleanupmergepublisher p = new SpMscleanupmergepublisher();

        p.execute(configuration);
        return p.getReturnValue();
    }

    /**
     * Call <code>master.dbo.sp_MSrepl_startup</code>
     */
    public static Integer spMsreplStartup(Configuration configuration) {
        SpMsreplStartup p = new SpMsreplStartup();

        p.execute(configuration);
        return p.getReturnValue();
    }
}
