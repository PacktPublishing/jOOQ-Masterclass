/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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
public class JooqProductPrivs implements Serializable {

    private static final long serialVersionUID = 1L;

    private String     product;
    private String     userid;
    private String     attribute;
    private String     scope;
    private BigDecimal numericValue;
    private String     charValue;
    private LocalDate  dateValue;
    private String     longValue;

    public JooqProductPrivs() {}

    public JooqProductPrivs(JooqProductPrivs value) {
        this.product = value.product;
        this.userid = value.userid;
        this.attribute = value.attribute;
        this.scope = value.scope;
        this.numericValue = value.numericValue;
        this.charValue = value.charValue;
        this.dateValue = value.dateValue;
        this.longValue = value.longValue;
    }

    public JooqProductPrivs(
        String     product,
        String     userid,
        String     attribute,
        String     scope,
        BigDecimal numericValue,
        String     charValue,
        LocalDate  dateValue,
        String     longValue
    ) {
        this.product = product;
        this.userid = userid;
        this.attribute = attribute;
        this.scope = scope;
        this.numericValue = numericValue;
        this.charValue = charValue;
        this.dateValue = dateValue;
        this.longValue = longValue;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.PRODUCT</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getProduct() {
        return this.product;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.PRODUCT</code>.
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.USERID</code>.
     */
    @Size(max = 128)
    public String getUserid() {
        return this.userid;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.USERID</code>.
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.ATTRIBUTE</code>.
     */
    @Size(max = 240)
    public String getAttribute() {
        return this.attribute;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.ATTRIBUTE</code>.
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.SCOPE</code>.
     */
    @Size(max = 240)
    public String getScope() {
        return this.scope;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.SCOPE</code>.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.NUMERIC_VALUE</code>.
     */
    public BigDecimal getNumericValue() {
        return this.numericValue;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.NUMERIC_VALUE</code>.
     */
    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.CHAR_VALUE</code>.
     */
    @Size(max = 240)
    public String getCharValue() {
        return this.charValue;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.CHAR_VALUE</code>.
     */
    public void setCharValue(String charValue) {
        this.charValue = charValue;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.DATE_VALUE</code>.
     */
    public LocalDate getDateValue() {
        return this.dateValue;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.DATE_VALUE</code>.
     */
    public void setDateValue(LocalDate dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * Getter for <code>SYSTEM.PRODUCT_PRIVS.LONG_VALUE</code>.
     */
    public String getLongValue() {
        return this.longValue;
    }

    /**
     * Setter for <code>SYSTEM.PRODUCT_PRIVS.LONG_VALUE</code>.
     */
    public void setLongValue(String longValue) {
        this.longValue = longValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JooqProductPrivs (");

        sb.append(product);
        sb.append(", ").append(userid);
        sb.append(", ").append(attribute);
        sb.append(", ").append(scope);
        sb.append(", ").append(numericValue);
        sb.append(", ").append(charValue);
        sb.append(", ").append(dateValue);
        sb.append(", ").append(longValue);

        sb.append(")");
        return sb.toString();
    }
}
