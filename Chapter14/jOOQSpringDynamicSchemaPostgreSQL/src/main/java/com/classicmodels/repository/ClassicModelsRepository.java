package com.classicmodels.repository;

import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.schema;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void ddlFromJavaSchema() {

       // ctx.setSchema("classicmodels").execute();
        System.out.println("ddddd:" + schema("public").getTable("office"));

        Queries ddl = ctx.ddl(schema("public").getCatalog());

        System.out.println("Queries:\n" + ddl.queries().length);
        for (Query query : ddl.queries()) {
            System.out.println("Query:" + query);
        }
    }

    @Transactional
    public void generateSchema() {

        ctx.dropDatabaseIfExists("classicmodels_g").execute();      // or, ctx.dropDatabase()
        ctx.createSchemaIfNotExists("classicmodels_g").execute();   // or, ctx.createSchema()     

        ctx.setSchema("classicmodels_g").execute();

        ctx.dropTableIfExists("employee_g").execute();
        ctx.createTable("employee_g")
                .column("employee_number_g", SQLDataType.BIGINT.nullable(false))
                .column("last_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("first_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("job_title_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("salary_g", SQLDataType.INTEGER.nullable(false))
                .constraints(
                        constraint("employee_g_pk").primaryKey("employee_number_g")
                ).execute();

        ctx.dropTableIfExists("customer_g").execute();
        ctx.createTable("customer_g")
                .column("customer_number_g", SQLDataType.BIGINT.nullable(false).identity(true))
                .column("customer_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("sales_rep_employee_number_g", SQLDataType.BIGINT)
                .column("credit_limit_g", SQLDataType.DECIMAL(10, 2))
                .constraints(
                        constraint("customer_g_pk").primaryKey("customer_number_g"),
                        constraint("customer_name_g_uk").unique("customer_name_g"),
                        constraint("customer_employee_fk").foreignKey("sales_rep_employee_number_g")
                                .references("employee_g", "employee_number_g")
                                .onUpdateCascade()
                ).execute();

    }

}
