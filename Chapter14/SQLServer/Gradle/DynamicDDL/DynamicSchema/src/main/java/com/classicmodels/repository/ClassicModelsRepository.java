package com.classicmodels.repository;

import static jooq.generated.Classicmodels.CLASSICMODELS;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.length;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sequence;
import static org.jooq.impl.DSL.table;
import org.jooq.impl.SQLDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void ddlFromJavaSchema() {

        // MySQL DDL to PostgreSQL DDL
        Queries ddl = DSL.using(SQLDialect.ORACLE).ddl(CLASSICMODELS);

        System.out.println("Queries:\n" + ddl.queries().length);
        for (Query query : ddl.queries()) {
            System.out.println("Query:" + query);
        }
    }
    
    @Transactional
    public void createSchema() {
       
        ctx.createDatabaseIfNotExists("classicmodels_g").execute();                 
        ctx.setCatalog("classicmodels_g").execute();
    }

    @Transactional
    public void createSequence() {                                      

        ctx.dropTableIfExists("employee_g").execute(); // this is needed to avoid sequence reference        
        
        ctx.dropSequenceIfExists("employee_seq").execute();
        ctx.createSequenceIfNotExists("employee_seq").startWith(100000).incrementBy(10)
                .minvalue(100000).maxvalue(10000000).execute();     
    }

    @Transactional
    public void populateSchema() {

        ctx.dropTableIfExists("employee_g").execute();
        ctx.createTable("employee_g")
                .column("employee_number_g", SQLDataType.DECIMAL_INTEGER
                        .nullable(false).default_(sequence(name("employee_seq")).nextval()))
                .column("last_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("first_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("job_title_g", SQLDataType.VARCHAR(50).nullable(false).defaultValue("Sales Rep"))
                .column("salary_g", SQLDataType.INTEGER.nullable(false).defaultValue(0))
                .constraints(
                        constraint("employee_g_pk").primaryKey("employee_number_g")
                ).execute();

        ctx.dropTableIfExists("customer_g").execute();
        ctx.dropTableIfExists("customer_renamed_g").execute();
        ctx.createTable("customer_g")
                .column("customer_number_g", SQLDataType.BIGINT.nullable(false).identity(true))
                .column("customer_name_g", SQLDataType.VARCHAR(50).nullable(false))
                .column("phone_g", VARCHAR(50).nullable(false))
                .column("sales_rep_employee_number_g", SQLDataType.DECIMAL_INTEGER)
                .column("credit_limit_g", SQLDataType.DECIMAL(10, 2))
                .constraints(
                        constraint("customer_g_pk").primaryKey("customer_number_g"),
                        constraint("customer_employee_fk").foreignKey("sales_rep_employee_number_g")
                                .references("employee_g", "employee_number_g")
                                .onUpdateCascade()
                ).execute();
    }

    @Transactional
    public void alterSchema() {
        
        ctx.alterTable("customer_g").add(constraint("customer_name_g_uk")
                .unique("customer_name_g")).execute();

        ctx.alterTable("customer_g").add(constraint("customer_name_len_g_ck")
                .check(length(field("customer_name_g", VARCHAR)).gt(10))).execute();

        ctx.alterTable("customer_g").alter("phone_g").default_("000-000-000").execute();

        ctx.alterTable("customer_g").dropUnique("customer_name_g_uk").execute();
        ctx.alterTable("customer_g").dropForeignKey("customer_employee_fk").execute();

        ctx.alterTable("customer_g").renameTo("customer_renamed_g").execute();
        ctx.alterTable("customer_renamed_g").renameColumn("credit_limit_g").to("credit_limit_renamed_g").execute();
    }

    @Transactional
    public void createDropIndexes() {
        ctx.createIndexIfNotExists("phone_idx").on("customer_renamed_g", "phone_g").execute();
        ctx.createIndexIfNotExists("cc_idx").on("customer_renamed_g", "customer_name_g", "credit_limit_renamed_g").execute();
        ctx.createIndexIfNotExists("emp_idx").on(table("employee_g"), field("last_name_g").asc(), field("first_name_g").desc()).execute();

        ctx.dropIndexIfExists("cc_idx").on("customer_renamed_g").execute();
    }

    @Transactional
    public void createTableFromAnotherTable() {

        ctx.dropTableIfExists("office_all_g").execute();
        ctx.createTable("office_all_g").as(
                select().from(OFFICE))
                .withData().execute();   // withData() is default, so you skip it
                                         // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("office_all_g")));

        ctx.dropTableIfExists("office_some_g").execute();
        ctx.createTable("office_some_g").as(
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE))
                .withData().execute();   // withData() is default, so you skip it
                                         // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("office_some_g")));
    }

    @Transactional
    public void createTempTable1() {

        ctx.dropTemporaryTableIfExists("##employee_t").execute();
        ctx.createTable("##employee_t")
                .column("employee_number_t", SQLDataType.BIGINT.nullable(false).identity(true))
                .column("last_name_t", SQLDataType.VARCHAR(50).nullable(false))
                .column("first_name_t", SQLDataType.VARCHAR(50).nullable(false))
                .column("job_title_t", SQLDataType.VARCHAR(50).nullable(false).defaultValue("Sales Rep"))
                .column("salary_t", SQLDataType.INTEGER.nullable(false).defaultValue(0))
                .constraints(
                        constraint("employee_t_pk").primaryKey("employee_number_t")
                ).execute();

        ctx.insertInto(table("##employee_t"), field("last_name_t"), field("first_name_t"),
                field("job_title_t"), field("salary_t"))
                .values("John", "Malon", default_(), 75000)
                .values("Yen", "Right", "VP", 110000)
                .execute();

        System.out.println(ctx.fetch(table("##employee_t")));
    }

    @Transactional
    public void createTempTable2() {

        ctx.dropTemporaryTableIfExists("##top3product_t").execute();
        ctx.createTable("##top3product_t").as(
                select().from(TOP3PRODUCT)
        // or, select(TOP3PRODUCT.PRODUCT_ID, TOP3PRODUCT.PRODUCT_NAME).from(TOP3PRODUCT)
        ).withData().execute();   // withData() is default, so you skip it
        // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("##top3product_t")));
    }
    
    @Transactional
    public void createView() {

        ctx.dropViewIfExists("product_view");
        ctx.createOrReplaceView("product_view").as(
                select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)).execute();

        System.out.println(ctx.fetch(table("product_view")));
    }
}
