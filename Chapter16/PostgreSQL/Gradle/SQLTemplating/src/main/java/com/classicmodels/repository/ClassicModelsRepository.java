package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.resultQuery;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.update;
import static org.jooq.impl.DSL.val;
import org.postgresql.util.HStoreConverter;
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
    public void hstoreSamples() {

        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE, PRODUCT.CODE, PRODUCT.SPECS)
                .values("2002 Masserati Levante", "Classic Cars", 599302L,
                        field("?::hstore", String.class,
                                HStoreConverter.toString(Map.of("Length (in)", "197", "Width (in)", "77.5", "Height (in)",
                                        "66.1", "Engine", "Twin Turbo Premium Unleaded V-6"))))
                .execute();

        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME,
                field("{0} -> {1}", String.class, PRODUCT.SPECS, val("Engine")).as("engine"))
                .from(PRODUCT)
                .where(field("{0} -> {1}", String.class, PRODUCT.SPECS, val("Length (in)")).eq("197"))
                .fetch();

        ctx.update(PRODUCT)
                .set(PRODUCT.SPECS,
                        (field("{0} || {1}::hstore",
                                Record.class, PRODUCT.SPECS,
                                val("\"Autonomy(h)\" => \"128\""))))
                .execute();

        ctx.update(PRODUCT)
                .set(PRODUCT.SPECS,
                        (field("delete({0}, {1})",
                                Record.class, PRODUCT.SPECS,
                                val("Engine"))))
                .execute();

        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .where(field("{0} @> {1}::hstore", Boolean.class,
                        PRODUCT.SPECS, val("\"Length (in)\"=>\"197\"")))
                .fetch();

        ctx.select(field("skeys({0})", PRODUCT.SPECS).as("keys"),
                field("avals({0})", PRODUCT.SPECS).as("values"))
                .from(PRODUCT)
                .fetch();

        ctx.select(PRODUCT.PRODUCT_NAME,
                field("hstore_to_json ({0}) json", PRODUCT.SPECS))
                .from(PRODUCT)
                .fetch();

        List<Map<String, String>> specs = ctx.select(PRODUCT.SPECS.coerce(String.class))
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("2002 Masserati Levante"))
                .fetch(rs -> {
                    return HStoreConverter.fromString(rs.getValue(PRODUCT.SPECS).toString());
                });

        System.out.println("Product specs: " + specs);
    }

    @Transactional
    public void moreSamples() {

        ctx.fetch("""
                  SELECT first_name, last_name 
                  FROM employee WHERE salary > ? AND job_title = ?
                  """, 5000, "Sales Rep");

        ctx.resultQuery("""
                        SELECT first_name, last_name 
                        FROM employee WHERE salary > ? AND job_title = ?
                        """, 5000, "Sales Rep")
                .fetch();

        ctx.query("""
                  UPDATE product SET quantity_in_stock = ? 
                      WHERE product_id = ?
                  """, 0, 2)
                .execute();
        
        // ctx.queries(query(""), query(""), query("")).executeBatch();

        Field<String> pswd = field("crypt({0}, {1})",
                 String.class, val("pass"), val("md5"));
        System.out.println("Pass:" + ctx.select(pswd).fetchOne(pswd));

        Result<Record1<BigDecimal>> msrps1 = ctx.resultQuery(
                "with \"updatedMsrp\" as ({0}) {1}",
                update(PRODUCT).set(PRODUCT.MSRP, PRODUCT.MSRP.plus(PRODUCT.MSRP.mul(0.25))).returning(PRODUCT.MSRP),
                select().from(name("updatedMsrp")))
                .coerce(PRODUCT.MSRP)
                .fetch();

        Result<Record1<BigDecimal>> msrps2 = ctx.resultQuery(
                "with \"updatedMsrp\" as ({0}) {1}",
                resultQuery("""
                            update 
                                "public"."product" 
                              set 
                                "msrp" = (
                                  "public"."product"."msrp" + (
                                    "public"."product"."msrp" * 0.25
                                  )
                                ) returning "public"."product"."msrp"
                            """),
                resultQuery("""
                            select 
                              * 
                            from 
                              "updatedMsrp"
                            """))
                .coerce(PRODUCT.MSRP)
                .fetch();

        Result<Record> result = ctx.fetch("{0} returning *",
                ctx.insertInto(TOP3PRODUCT)
                        .select(select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME).from(PRODUCT).limit(3))
                        .onDuplicateKeyIgnore());

        // jOOQ 3.11 and before        
        List<ProductRecord> products1 = ctx.configuration().derive(new Settings()
                .withRenderSchema(Boolean.FALSE)).dsl()
                .select(PRODUCT.fields())
                .from("({0}) AS {1}",
                        sql("select * from product"),
                        name(PRODUCT.getName()))
                .fetchInto(PRODUCT);

        // jOOQ 3.12+       
        List<ProductRecord> products2 = ctx.resultQuery("select * from product")
                .coerce(PRODUCT)
                .fetch();

        List<Record2<String, BigDecimal>> productsNamePrice
                = ctx.resultQuery("select product_name, buy_price from product")
                        .coerce(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .fetch();                        
    }
}
