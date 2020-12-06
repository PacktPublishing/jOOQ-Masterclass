/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.records;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jooq.generated.tables.Payment;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PaymentRecord extends UpdatableRecordImpl<PaymentRecord> implements Record5<Long, String, LocalDateTime, BigDecimal, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>payment.customer_number</code>.
     */
    public void setCustomerNumber(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>payment.customer_number</code>.
     */
    @NotNull
    public Long getCustomerNumber() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>payment.check_number</code>.
     */
    public void setCheckNumber(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>payment.check_number</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getCheckNumber() {
        return (String) get(1);
    }

    /**
     * Setter for <code>payment.payment_date</code>.
     */
    public void setPaymentDate(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>payment.payment_date</code>.
     */
    @NotNull
    public LocalDateTime getPaymentDate() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>payment.invoice_amount</code>.
     */
    public void setInvoiceAmount(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>payment.invoice_amount</code>.
     */
    @NotNull
    public BigDecimal getInvoiceAmount() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>payment.caching_date</code>.
     */
    public void setCachingDate(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>payment.caching_date</code>.
     */
    public LocalDateTime getCachingDate() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, LocalDateTime, BigDecimal, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, LocalDateTime, BigDecimal, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Payment.PAYMENT.CUSTOMER_NUMBER;
    }

    @Override
    public Field<String> field2() {
        return Payment.PAYMENT.CHECK_NUMBER;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Payment.PAYMENT.PAYMENT_DATE;
    }

    @Override
    public Field<BigDecimal> field4() {
        return Payment.PAYMENT.INVOICE_AMOUNT;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Payment.PAYMENT.CACHING_DATE;
    }

    @Override
    public Long component1() {
        return getCustomerNumber();
    }

    @Override
    public String component2() {
        return getCheckNumber();
    }

    @Override
    public LocalDateTime component3() {
        return getPaymentDate();
    }

    @Override
    public BigDecimal component4() {
        return getInvoiceAmount();
    }

    @Override
    public LocalDateTime component5() {
        return getCachingDate();
    }

    @Override
    public Long value1() {
        return getCustomerNumber();
    }

    @Override
    public String value2() {
        return getCheckNumber();
    }

    @Override
    public LocalDateTime value3() {
        return getPaymentDate();
    }

    @Override
    public BigDecimal value4() {
        return getInvoiceAmount();
    }

    @Override
    public LocalDateTime value5() {
        return getCachingDate();
    }

    @Override
    public PaymentRecord value1(Long value) {
        setCustomerNumber(value);
        return this;
    }

    @Override
    public PaymentRecord value2(String value) {
        setCheckNumber(value);
        return this;
    }

    @Override
    public PaymentRecord value3(LocalDateTime value) {
        setPaymentDate(value);
        return this;
    }

    @Override
    public PaymentRecord value4(BigDecimal value) {
        setInvoiceAmount(value);
        return this;
    }

    @Override
    public PaymentRecord value5(LocalDateTime value) {
        setCachingDate(value);
        return this;
    }

    @Override
    public PaymentRecord values(Long value1, String value2, LocalDateTime value3, BigDecimal value4, LocalDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PaymentRecord
     */
    public PaymentRecord() {
        super(Payment.PAYMENT);
    }

    /**
     * Create a detached, initialised PaymentRecord
     */
    public PaymentRecord(Long customerNumber, String checkNumber, LocalDateTime paymentDate, BigDecimal invoiceAmount, LocalDateTime cachingDate) {
        super(Payment.PAYMENT);

        setCustomerNumber(customerNumber);
        setCheckNumber(checkNumber);
        setPaymentDate(paymentDate);
        setInvoiceAmount(invoiceAmount);
        setCachingDate(cachingDate);
    }
}
