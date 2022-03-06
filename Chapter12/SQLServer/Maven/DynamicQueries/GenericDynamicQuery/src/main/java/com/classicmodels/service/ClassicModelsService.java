package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.Record;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.ProductRecord;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {

        List<ProductRecord> rs1 = classicModelsRepository.select1(PRODUCT,
                List.of(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE, PRODUCT.MSRP),
                PRODUCT.BUY_PRICE.gt(BigDecimal.valueOf(50)), PRODUCT.MSRP.gt(BigDecimal.valueOf(80)));
        System.out.println("EXAMPLE 1\n" + rs1);

        List<Record> rs2 = classicModelsRepository.select2(table("product"),
                List.of(field("product_line"), field("product_name"), field("buy_price"), field("msrp")),
                field("buy_price").gt(50), field("msrp").gt(80));
        System.out.println("EXAMPLE 2\n" + rs2);

        int ri1 = classicModelsRepository.insert1(PRODUCT,
                Map.of(PRODUCT.PRODUCT_LINE, "Classic Cars", PRODUCT.CODE, 599302,
                        PRODUCT.PRODUCT_NAME, "1972 Porsche 914"));
        System.out.println("EXAMPLE 3\n" + ri1);

        int ri2 = classicModelsRepository.insert2(table("product"),
                Map.of(field("product_line"), "Classic Cars", field("code"), 599302,
                        field("product_name"), "1972 Porsche 914"));
        System.out.println("EXAMPLE 4\n" + ri2);

        int ru1 = classicModelsRepository.update1(SALE,
                Map.of(SALE.TREND, "UP", SALE.HOT, true), SALE.TREND.eq("CONSTANT"));
        System.out.println("EXAMPLE 5\n" + ru1);

        int ru2 = classicModelsRepository.update2(table("sale"),
                Map.of(field("trend"), "CONSTANT", field("hot"), false), field("trend").eq("UP"));
        System.out.println("EXAMPLE 6\n" + ru2);

        int rd1 = classicModelsRepository.delete(SALE, SALE.TREND.eq("UP"));
        System.out.println("EXAMPLE 7\n" + rd1);

        int rd2 = classicModelsRepository.delete(table("sale"), field("trend").eq("CONSTANT"));
        System.out.println("EXAMPLE 8\n" + rd2);

        // More examples
        List<Record> r1 = classicModelsRepository.fetchOfficeEmployeeFields1(
                List.of(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.STATE),
                List.of(EMPLOYEE.EMAIL, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME));
        System.out.println("EXAMPLE 9\n" + r1);

        List<Record> r2 = classicModelsRepository.fetchOfficeEmployeeFields2(
                List.of(field("city"), field("country"), field("state")),
                List.of(field("email"), field("first_name"), field("last_name")));
        System.out.println("EXAMPLE 10\n" + r2);

        List<Record> r3 = classicModelsRepository.allFieldsOfTables(100, CUSTOMER, CUSTOMERDETAIL);
        System.out.println("EXAMPLE 11\n" + r3);
    }
}
