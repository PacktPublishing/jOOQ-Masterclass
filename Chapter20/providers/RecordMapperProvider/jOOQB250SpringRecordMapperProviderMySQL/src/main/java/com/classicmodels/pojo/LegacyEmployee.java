package com.classicmodels.pojo;

public final class LegacyEmployee {

    private final Long employeeNumber;
    private final String lastName;
    private final String firstName;
    private final String extension;
    private final String email;
    private final String officeCode;
    private final Integer salary;
    private final Integer commission;
    private final Long reportsTo;
    private final String jobTitle;
    private final String employeeOfYear;
    private final String monthlyBonus;

    private LegacyEmployee(EmployeeBuilder builder) {
        this.employeeNumber = builder.employeeNumber;
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.extension = builder.extension;
        this.email = builder.email;
        this.officeCode = builder.officeCode;
        this.salary = builder.salary;
        this.commission = builder.commission;
        this.reportsTo = builder.reportsTo;
        this.jobTitle = builder.jobTitle;
        this.employeeOfYear = builder.employeeOfYear;
        this.monthlyBonus = builder.monthlyBonus;
    }

    public static EmployeeBuilder getBuilder(String lastName, String firstName) {
        return new LegacyEmployee.EmployeeBuilder(lastName, firstName);
    }

    public static final class EmployeeBuilder {

        private Long employeeNumber;
        private String lastName;
        private String firstName;
        private String extension;
        private String email;
        private String officeCode;
        private Integer salary;
        private Integer commission;
        private Long reportsTo;
        private String jobTitle;
        private String employeeOfYear;
        private String monthlyBonus;

        public EmployeeBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public EmployeeBuilder employeeNumber(Long employeeNumber) {
            this.employeeNumber = employeeNumber;
            return this;
        }

        public EmployeeBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public EmployeeBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public EmployeeBuilder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public EmployeeBuilder email(String email) {
            this.email = email;
            return this;
        }

        public EmployeeBuilder officeCode(String officeCode) {
            this.officeCode = officeCode;
            return this;
        }

        public EmployeeBuilder salary(Integer salary) {
            this.salary = salary;
            return this;
        }

        public EmployeeBuilder commission(Integer commission) {
            this.commission = commission;
            return this;
        }

        public EmployeeBuilder reportsTo(Long reportsTo) {
            this.reportsTo = reportsTo;
            return this;
        }

        public EmployeeBuilder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public EmployeeBuilder employeeOfYear(String employeeOfYear) {
            this.employeeOfYear = employeeOfYear;
            return this;
        }

        public EmployeeBuilder monthlyBonus(String monthlyBonus) {
            this.monthlyBonus = monthlyBonus;
            return this;
        }

        public LegacyEmployee build() {
            return new LegacyEmployee(this);
        }
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getExtension() {
        return extension;
    }

    public String getEmail() {
        return email;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public Integer getSalary() {
        return salary;
    }

    public Integer getCommission() {
        return commission;
    }

    public Long getReportsTo() {
        return reportsTo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmployeeOfYear() {
        return employeeOfYear;
    }

    public String getMonthlyBonus() {
        return monthlyBonus;
    }

    @Override
    public String toString() {
        return "EmployeeBuilder{" + "employeeNumber=" + employeeNumber
                + ", lastName=" + lastName + ", firstName=" + firstName
                + ", extension=" + extension + ", email=" + email
                + ", officeCode=" + officeCode + ", salary=" + salary
                + ", commission=" + commission + ", reportsTo=" + reportsTo
                + ", jobTitle=" + jobTitle + ", employeeOfYear=" + employeeOfYear
                + ", monthlyBonus=" + monthlyBonus + '}';
    }
}
