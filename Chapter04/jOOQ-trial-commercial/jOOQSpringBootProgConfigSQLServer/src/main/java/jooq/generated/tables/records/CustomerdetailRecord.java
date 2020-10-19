/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.records;


import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jooq.generated.tables.Customerdetail;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class CustomerdetailRecord extends UpdatableRecordImpl<CustomerdetailRecord> implements Record7<Long, String, String, String, String, String, String> {

    private static final long serialVersionUID = 391108517;

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.CUSTOMER_NUMBER</code>.
     */
    public void setCustomerNumber(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.CUSTOMER_NUMBER</code>.
     */
    @NotNull
    public Long getCustomerNumber() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.ADDRESS_LINE_FIRST</code>.
     */
    public void setAddressLineFirst(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.ADDRESS_LINE_FIRST</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getAddressLineFirst() {
        return (String) get(1);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.ADDRESS_LINE_SECOND</code>.
     */
    public void setAddressLineSecond(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.ADDRESS_LINE_SECOND</code>.
     */
    @Size(max = 50)
    public String getAddressLineSecond() {
        return (String) get(2);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.CITY</code>.
     */
    public void setCity(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.CITY</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getCity() {
        return (String) get(3);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.STATE</code>.
     */
    public void setState(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.STATE</code>.
     */
    @Size(max = 50)
    public String getState() {
        return (String) get(4);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.POSTAL_CODE</code>.
     */
    public void setPostalCode(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.POSTAL_CODE</code>.
     */
    @Size(max = 15)
    public String getPostalCode() {
        return (String) get(5);
    }

    /**
     * Setter for <code>SYSTEM.CUSTOMERDETAIL.COUNTRY</code>.
     */
    public void setCountry(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>SYSTEM.CUSTOMERDETAIL.COUNTRY</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getCountry() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, String, String, String, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, String, String, String, String, String, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Customerdetail.CUSTOMERDETAIL.CUSTOMER_NUMBER;
    }

    @Override
    public Field<String> field2() {
        return Customerdetail.CUSTOMERDETAIL.ADDRESS_LINE_FIRST;
    }

    @Override
    public Field<String> field3() {
        return Customerdetail.CUSTOMERDETAIL.ADDRESS_LINE_SECOND;
    }

    @Override
    public Field<String> field4() {
        return Customerdetail.CUSTOMERDETAIL.CITY;
    }

    @Override
    public Field<String> field5() {
        return Customerdetail.CUSTOMERDETAIL.STATE;
    }

    @Override
    public Field<String> field6() {
        return Customerdetail.CUSTOMERDETAIL.POSTAL_CODE;
    }

    @Override
    public Field<String> field7() {
        return Customerdetail.CUSTOMERDETAIL.COUNTRY;
    }

    @Override
    public Long component1() {
        return getCustomerNumber();
    }

    @Override
    public String component2() {
        return getAddressLineFirst();
    }

    @Override
    public String component3() {
        return getAddressLineSecond();
    }

    @Override
    public String component4() {
        return getCity();
    }

    @Override
    public String component5() {
        return getState();
    }

    @Override
    public String component6() {
        return getPostalCode();
    }

    @Override
    public String component7() {
        return getCountry();
    }

    @Override
    public Long value1() {
        return getCustomerNumber();
    }

    @Override
    public String value2() {
        return getAddressLineFirst();
    }

    @Override
    public String value3() {
        return getAddressLineSecond();
    }

    @Override
    public String value4() {
        return getCity();
    }

    @Override
    public String value5() {
        return getState();
    }

    @Override
    public String value6() {
        return getPostalCode();
    }

    @Override
    public String value7() {
        return getCountry();
    }

    @Override
    public CustomerdetailRecord value1(Long value) {
        setCustomerNumber(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value2(String value) {
        setAddressLineFirst(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value3(String value) {
        setAddressLineSecond(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value4(String value) {
        setCity(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value5(String value) {
        setState(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value6(String value) {
        setPostalCode(value);
        return this;
    }

    @Override
    public CustomerdetailRecord value7(String value) {
        setCountry(value);
        return this;
    }

    @Override
    public CustomerdetailRecord values(Long value1, String value2, String value3, String value4, String value5, String value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CustomerdetailRecord
     */
    public CustomerdetailRecord() {
        super(Customerdetail.CUSTOMERDETAIL);
    }

    /**
     * Create a detached, initialised CustomerdetailRecord
     */
    public CustomerdetailRecord(Long customerNumber, String addressLineFirst, String addressLineSecond, String city, String state, String postalCode, String country) {
        super(Customerdetail.CUSTOMERDETAIL);

        set(0, customerNumber);
        set(1, addressLineFirst);
        set(2, addressLineSecond);
        set(3, city);
        set(4, state);
        set(5, postalCode);
        set(6, country);
    }
}
