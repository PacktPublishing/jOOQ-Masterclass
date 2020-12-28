/*
 * This file is generated by jOOQ.
 */
package jooq.generated.udt.pojos;


import java.io.Serializable;

import javax.annotation.processing.Generated;


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
public class TableResObj implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double sales;

    public TableResObj() {}

    public TableResObj(TableResObj value) {
        this.sales = value.sales;
    }

    public TableResObj(
        Double sales
    ) {
        this.sales = sales;
    }

    /**
     * Getter for <code>SYSTEM.TABLE_RES_OBJ.SALES</code>.
     */
    public Double getSales() {
        return this.sales;
    }

    /**
     * Setter for <code>SYSTEM.TABLE_RES_OBJ.SALES</code>.
     */
    public void setSales(Double sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TableResObj (");

        sb.append(sales);

        sb.append(")");
        return sb.toString();
    }
}
