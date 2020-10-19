/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.records;


import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jooq.generated.tables.Productline;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5",
        "schema version:1.1"
    },
    date = "2020-10-19T14:04:39.558Z",
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProductlineRecord extends UpdatableRecordImpl<ProductlineRecord> implements Record4<String, String, String, byte[]> {

    private static final long serialVersionUID = 719795457;

    /**
     * Setter for <code>SYSTEM.PRODUCTLINE.PRODUCT_LINE</code>.
     */
    public void setProductLine(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>SYSTEM.PRODUCTLINE.PRODUCT_LINE</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getProductLine() {
        return (String) get(0);
    }

    /**
     * Setter for <code>SYSTEM.PRODUCTLINE.TEXT_DESCRIPTION</code>.
     */
    public void setTextDescription(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>SYSTEM.PRODUCTLINE.TEXT_DESCRIPTION</code>.
     */
    @Size(max = 4000)
    public String getTextDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>SYSTEM.PRODUCTLINE.HTML_DESCRIPTION</code>.
     */
    public void setHtmlDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>SYSTEM.PRODUCTLINE.HTML_DESCRIPTION</code>.
     */
    public String getHtmlDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>SYSTEM.PRODUCTLINE.IMAGE</code>.
     */
    public void setImage(byte[] value) {
        set(3, value);
    }

    /**
     * Getter for <code>SYSTEM.PRODUCTLINE.IMAGE</code>.
     */
    public byte[] getImage() {
        return (byte[]) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, byte[]> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, byte[]> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Productline.PRODUCTLINE.PRODUCT_LINE;
    }

    @Override
    public Field<String> field2() {
        return Productline.PRODUCTLINE.TEXT_DESCRIPTION;
    }

    @Override
    public Field<String> field3() {
        return Productline.PRODUCTLINE.HTML_DESCRIPTION;
    }

    @Override
    public Field<byte[]> field4() {
        return Productline.PRODUCTLINE.IMAGE;
    }

    @Override
    public String component1() {
        return getProductLine();
    }

    @Override
    public String component2() {
        return getTextDescription();
    }

    @Override
    public String component3() {
        return getHtmlDescription();
    }

    @Override
    public byte[] component4() {
        return getImage();
    }

    @Override
    public String value1() {
        return getProductLine();
    }

    @Override
    public String value2() {
        return getTextDescription();
    }

    @Override
    public String value3() {
        return getHtmlDescription();
    }

    @Override
    public byte[] value4() {
        return getImage();
    }

    @Override
    public ProductlineRecord value1(String value) {
        setProductLine(value);
        return this;
    }

    @Override
    public ProductlineRecord value2(String value) {
        setTextDescription(value);
        return this;
    }

    @Override
    public ProductlineRecord value3(String value) {
        setHtmlDescription(value);
        return this;
    }

    @Override
    public ProductlineRecord value4(byte[] value) {
        setImage(value);
        return this;
    }

    @Override
    public ProductlineRecord values(String value1, String value2, String value3, byte[] value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProductlineRecord
     */
    public ProductlineRecord() {
        super(Productline.PRODUCTLINE);
    }

    /**
     * Create a detached, initialised ProductlineRecord
     */
    public ProductlineRecord(String productLine, String textDescription, String htmlDescription, byte[] image) {
        super(Productline.PRODUCTLINE);

        set(0, productLine);
        set(1, textDescription);
        set(2, htmlDescription);
        set(3, image);
    }
}
