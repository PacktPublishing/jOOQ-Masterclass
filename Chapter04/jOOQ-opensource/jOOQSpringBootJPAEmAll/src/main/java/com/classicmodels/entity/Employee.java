package com.classicmodels.entity;

import com.classicmodels.pojo.EmployeeCntr;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;

@Entity
@JsonInclude(Include.NON_EMPTY)
@SqlResultSetMapping(name = "EmployeeDtoMapping",
        classes = @ConstructorResult(targetClass = EmployeeCntr.class,
                columns = {
                    @ColumnResult(name = "firstName"),
                    @ColumnResult(name = "lastName"),
                    @ColumnResult(name = "salary"),
                    @ColumnResult(name = "leastSalary")}))

@SqlResultSetMapping(
        name = "EmployeeOfficeEntityMapping",
        entities = {
            @EntityResult(
                    entityClass = Employee.class,
                    fields = {
                        @FieldResult(name = "employeeNumber", column = "employee_number"),
                        @FieldResult(name = "lastName", column = "last_name"),
                        @FieldResult(name = "firstName", column = "first_name"),
                        @FieldResult(name = "extension", column = "extension"),
                        @FieldResult(name = "email", column = "email"),
                        @FieldResult(name = "jobTitle", column = "job_title"),
                        @FieldResult(name = "salary", column = "salary"),
                        @FieldResult(name = "reports", column = "reports_to"),
                        @FieldResult(name = "office", column = "first_name")
                    }
            ),
            @EntityResult(
                    entityClass = Office.class,
                    fields = {
                        @FieldResult(name = "officeCode", column = "office_code"),
                        @FieldResult(name = "city", column = "city"),
                        @FieldResult(name = "phone", column = "phone"),
                        @FieldResult(name = "addressLineFirst", column = "address_line_first"),
                        @FieldResult(name = "addressLineSecond", column = "address_line_second"),
                        @FieldResult(name = "state", column = "state"),
                        @FieldResult(name = "country", column = "country"),
                        @FieldResult(name = "postalCode", column = "postal_code"),
                        @FieldResult(name = "territory", column = "territory")})})

public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long employeeNumber;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 10, nullable = false)
    private String extension;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private int salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reports_to")
    private Employee reports;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "salesRepEmployeeNumber", orphanRemoval = true)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "employee", orphanRemoval = true)
    private List<Sale> sales = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_code")
    private Office office;

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Employee getReports() {
        return reports;
    }

    public void setReports(Employee reports) {
        this.reports = reports;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public int hashCode() {
        return 2021;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return employeeNumber != null && employeeNumber.equals(((Employee) obj).employeeNumber);
    }

    @Override
    public String toString() {
        return "Employee{" + "lastName=" + lastName
                + ", firstName=" + firstName + ", jobTitle=" + jobTitle + '}';
    }

}
