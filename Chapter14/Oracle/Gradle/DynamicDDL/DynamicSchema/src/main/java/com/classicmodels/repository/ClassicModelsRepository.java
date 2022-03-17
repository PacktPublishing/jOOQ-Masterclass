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

        // Oracle DDL to MySQL DDL
        Queries ddl = DSL.using(SQLDialect.MYSQL).ddl(CLASSICMODELS);

        System.out.println("Queries:\n" + ddl.queries().length);
        for (Query query : ddl.queries()) {
            System.out.println("Query:" + query);
        }
    }
    
    @Transactional
    public void setSchema() {

        ctx.setSchema("CLASSICMODELS").execute();
    }

    @Transactional
    public void createSequence() {        

        ctx.dropSequenceIfExists("EMPLOYEE_SEQ");
        ctx.createSequenceIfNotExists("EMPLOYEE_SEQ").startWith(100000).incrementBy(10)
                .minvalue(100000).maxvalue(10000000).execute();
    }

    @Transactional
    public void populateSchema() {
        
        ctx.dropTableIfExists("CUSTOMER_G").execute();
        ctx.dropTableIfExists("CUSTOMER_RENAMED_G").execute();
        ctx.dropTableIfExists("EMPLOYEE_G").execute();                
                
        ctx.createTable("EMPLOYEE_G")
                .column("EMPLOYEE_NUMBER_G", SQLDataType.DECIMAL_INTEGER
                        .nullable(false).default_(sequence(name("EMPLOYEE_SEQ")).nextval()))
                .column("LAST_NAME_G", SQLDataType.VARCHAR(50).nullable(false))
                .column("FIRST_NAME_G", SQLDataType.VARCHAR(50).nullable(false))
                .column("JOB_TITLE_G", SQLDataType.VARCHAR(50).nullable(false).defaultValue("Sales Rep"))
                .column("SALARY_G", SQLDataType.INTEGER.nullable(false).defaultValue(0))
                .constraints(
                        constraint("EMPLOYEE_G_PK").primaryKey("EMPLOYEE_NUMBER_G")
                ).execute();
                
        ctx.createTable("CUSTOMER_G")
                .column("CUSTOMER_NUMBER_G", SQLDataType.BIGINT.nullable(false).identity(true))
                .column("CUSTOMER_NAME_G", SQLDataType.VARCHAR(50).nullable(false))
                .column("PHONE_G", VARCHAR(50).nullable(false))
                .column("SALES_REP_EMPLOYEE_NUMBER_G", SQLDataType.DECIMAL_INTEGER)
                .column("CREDIT_LIMIT_G", SQLDataType.DECIMAL(10, 2))
                .constraints(
                        constraint("CUSTOMER_G_PK").primaryKey("CUSTOMER_NUMBER_G"),
                        constraint("CUSTOMER_EMPLOYEE_G_FK").foreignKey("SALES_REP_EMPLOYEE_NUMBER_G")
                                .references("EMPLOYEE_G", "EMPLOYEE_NUMBER_G")                                                        
                ).execute();
    }

    @Transactional
    public void alterSchema() {
                
        ctx.alterTable("CUSTOMER_G").add(constraint("CUSTOMER_NAME_G_UK")
                .unique("CUSTOMER_NAME_G")).execute();
        
        ctx.alterTable("CUSTOMER_G").add(constraint("CUSTOMER_NAME_LEN_G_CK")
                .check(length(field("CUSTOMER_NAME_G", VARCHAR)).gt(10))).execute();

        ctx.alterTable("CUSTOMER_G").alter("PHONE_G").default_("000-000-000").execute();                
        
        ctx.alterTable("CUSTOMER_G").dropPrimaryKey("CUSTOMER_G_PK").execute();
        ctx.alterTable("CUSTOMER_G").dropUnique("CUSTOMER_NAME_G_UK").execute();
        ctx.alterTable("CUSTOMER_G").dropForeignKey("CUSTOMER_EMPLOYEE_G_FK").execute();        
        ctx.alterTable("CUSTOMER_G").drop(constraint("CUSTOMER_NAME_LEN_G_CK")).execute();
        
        ctx.alterTable("CUSTOMER_G").renameTo("CUSTOMER_RENAMED_G").execute();
        ctx.alterTable("CUSTOMER_RENAMED_G").renameColumn("CREDIT_LIMIT_G").to("CREDIT_LIMIT_RENAMED_G").execute();
    }
           
    @Transactional
    public void createDropIndexes() {
        ctx.createIndexIfNotExists("PHONE_IDX").on("CUSTOMER_RENAMED_G", "PHONE_G").execute();
        ctx.createIndexIfNotExists("CC_IDX").on("CUSTOMER_RENAMED_G", "CUSTOMER_NAME_G", "CREDIT_LIMIT_RENAMED_G").execute();
        ctx.createIndexIfNotExists("EMP_IDX").on(table("EMPLOYEE_G"), field("LAST_NAME_G").asc(), field("FIRST_NAME_G").desc()).execute();

        ctx.dropIndexIfExists("CC_IDX").on("CUSTOMER_RENAMED_G").execute();
    }

    @Transactional
    public void createTableFromAnotherTable() {

        ctx.dropTableIfExists("OFFICE_ALL_G").execute();
        ctx.createTable("OFFICE_ALL_G").as(
                select().from(OFFICE))
                .withData().execute();   // withData() is default, so you skip it
        // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("OFFICE_ALL_G")));

        ctx.dropTableIfExists("OFFICE_SOME_G").execute();
        ctx.createTable("OFFICE_SOME_G").as(
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE))
                .withData().execute();   // withData() is default, so you skip it
        // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("OFFICE_SOME_G")));
    }

    @Transactional
    public void createTempTable1() {

        ctx.dropTemporaryTableIfExists("EMPLOYEE_T").execute();
        ctx.createGlobalTemporaryTable("EMPLOYEE_T") // or, createTemporaryTable (local temporary table)
                .column("EMPLOYEE_NUMBER_T", SQLDataType.BIGINT.nullable(false).identity(true))
                .column("LAST_NAME_T", SQLDataType.VARCHAR(50).nullable(false))
                .column("FIRST_NAME_T", SQLDataType.VARCHAR(50).nullable(false))
                .column("JOB_TITLE_T", SQLDataType.VARCHAR(50).nullable(false).defaultValue("Sales Rep"))
                .column("SALARY_T", SQLDataType.INTEGER.nullable(false).defaultValue(0))
                .constraints(
                        constraint("EMPLOYEE_T_PK").primaryKey("EMPLOYEE_NUMBER_T")
                ).execute();

        ctx.insertInto(table("EMPLOYEE_T"), field("LAST_NAME_T"), field("FIRST_NAME_T"),
                field("JOB_TITLE_T"), field("SALARY_T"))
                .values("Mark", "Joiop", "PM", 75000)
                .values("Yen", "Right", "VP", 110000)
                .execute();

        System.out.println(ctx.fetch(table("EMPLOYEE_T")));
    }

    @Transactional
    public void createTempTable2() {

        ctx.dropTemporaryTableIfExists("TOP3PRODUCT_T").execute();
        ctx.createGlobalTemporaryTable("TOP3PRODUCT_T").as( // or, createTemporaryTable (local temporary table)
                select().from(TOP3PRODUCT)
        // or, select(TOP3PRODUCT.PRODUCT_ID, TOP3PRODUCT.PRODUCT_NAME).from(TOP3PRODUCT)
        ).withData().execute();   // withData() is default, so you skip it
        // withNoData(), if you want an empty table

        System.out.println(ctx.fetch(table("TOP3PRODUCT_T")));
    }
    
    @Transactional
    public void createView() {

        ctx.dropViewIfExists("PRODUCT_VIEW");
        ctx.createOrReplaceView("PRODUCT_VIEW").as(
                select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT)).execute();

        System.out.println(ctx.fetch(table("PRODUCT_VIEW")));
    }
}
