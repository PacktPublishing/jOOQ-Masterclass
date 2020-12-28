/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.records;


import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jooq.generated.tables.Department;
import jooq.generated.udt.records.TopicarrRecord;

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
        "jOOQ version:3.14.4",
        "schema version:1.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DepartmentRecord extends UpdatableRecordImpl<DepartmentRecord> implements Record7<Long, String, String, Integer, String, TopicarrRecord, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.DEPARTMENT_ID</code>.
     */
    public void setDepartmentId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.DEPARTMENT_ID</code>.
     */
    @NotNull
    public Long getDepartmentId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.NAME</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.PHONE</code>.
     */
    public void setPhone(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.PHONE</code>.
     */
    @NotNull
    @Size(max = 50)
    public String getPhone() {
        return (String) get(2);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.CODE</code>.
     */
    public void setCode(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.CODE</code>.
     */
    public Integer getCode() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.OFFICE_CODE</code>.
     */
    public void setOfficeCode(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.OFFICE_CODE</code>.
     */
    @NotNull
    @Size(max = 10)
    public String getOfficeCode() {
        return (String) get(4);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.TOPIC</code>.
     */
    public void setTopic(TopicarrRecord value) {
        set(5, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.TOPIC</code>.
     */
    @NotNull
    public TopicarrRecord getTopic() {
        return (TopicarrRecord) get(5);
    }

    /**
     * Setter for <code>SYSTEM.DEPARTMENT.DEP_NET_IPV4</code>.
     */
    public void setDepNetIpv4(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>SYSTEM.DEPARTMENT.DEP_NET_IPV4</code>.
     */
    @Size(max = 16)
    public String getDepNetIpv4() {
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
    public Row7<Long, String, String, Integer, String, TopicarrRecord, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, String, String, Integer, String, TopicarrRecord, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Department.DEPARTMENT.DEPARTMENT_ID;
    }

    @Override
    public Field<String> field2() {
        return Department.DEPARTMENT.NAME;
    }

    @Override
    public Field<String> field3() {
        return Department.DEPARTMENT.PHONE;
    }

    @Override
    public Field<Integer> field4() {
        return Department.DEPARTMENT.CODE;
    }

    @Override
    public Field<String> field5() {
        return Department.DEPARTMENT.OFFICE_CODE;
    }

    @Override
    public Field<TopicarrRecord> field6() {
        return Department.DEPARTMENT.TOPIC;
    }

    @Override
    public Field<String> field7() {
        return Department.DEPARTMENT.DEP_NET_IPV4;
    }

    @Override
    public Long component1() {
        return getDepartmentId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getPhone();
    }

    @Override
    public Integer component4() {
        return getCode();
    }

    @Override
    public String component5() {
        return getOfficeCode();
    }

    @Override
    public TopicarrRecord component6() {
        return getTopic();
    }

    @Override
    public String component7() {
        return getDepNetIpv4();
    }

    @Override
    public Long value1() {
        return getDepartmentId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getPhone();
    }

    @Override
    public Integer value4() {
        return getCode();
    }

    @Override
    public String value5() {
        return getOfficeCode();
    }

    @Override
    public TopicarrRecord value6() {
        return getTopic();
    }

    @Override
    public String value7() {
        return getDepNetIpv4();
    }

    @Override
    public DepartmentRecord value1(Long value) {
        setDepartmentId(value);
        return this;
    }

    @Override
    public DepartmentRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public DepartmentRecord value3(String value) {
        setPhone(value);
        return this;
    }

    @Override
    public DepartmentRecord value4(Integer value) {
        setCode(value);
        return this;
    }

    @Override
    public DepartmentRecord value5(String value) {
        setOfficeCode(value);
        return this;
    }

    @Override
    public DepartmentRecord value6(TopicarrRecord value) {
        setTopic(value);
        return this;
    }

    @Override
    public DepartmentRecord value7(String value) {
        setDepNetIpv4(value);
        return this;
    }

    @Override
    public DepartmentRecord values(Long value1, String value2, String value3, Integer value4, String value5, TopicarrRecord value6, String value7) {
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
     * Create a detached DepartmentRecord
     */
    public DepartmentRecord() {
        super(Department.DEPARTMENT);
    }

    /**
     * Create a detached, initialised DepartmentRecord
     */
    public DepartmentRecord(Long departmentId, String name, String phone, Integer code, String officeCode, TopicarrRecord topic, String depNetIpv4) {
        super(Department.DEPARTMENT);

        setDepartmentId(departmentId);
        setName(name);
        setPhone(phone);
        setCode(code);
        setOfficeCode(officeCode);
        setTopic(topic);
        setDepNetIpv4(depNetIpv4);
    }
}
