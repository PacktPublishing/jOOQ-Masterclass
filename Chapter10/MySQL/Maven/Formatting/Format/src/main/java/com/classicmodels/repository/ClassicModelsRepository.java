package com.classicmodels.repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import org.jooq.TXTFormat;
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.DECIMAL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String formatResultAsText() {

        // Result<Record3<String, Long, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetch();

        System.out.println("EXAMPLE 1.1:\n" + result.format());
        System.out.println("EXAMPLE 1.2:\n" + result.format(5)); // format first 5 records
        System.out.println("EXAMPLE 1.3:\n" + result.format(result.size())); // format all

        TXTFormat txtFormat = new TXTFormat()
                .maxRows(25)
                .minColWidth(20);
        // try out more options
        System.out.println("EXAMPLE 1.4:\n" + result.format(txtFormat));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("result.txt"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()
                    .fetch()
                    .format(bw, txtFormat); // or, new TXTFormat().maxRows(25).minColWidth(20)
        } catch (IOException ex) {
            // handle exception
        }

        return result.format(txtFormat);
    }

    public String formatResultAsJSON() {
        
        // <Record3<String, Long, String>
        var oneResult = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetchAny();
                  
        // both output the same result
        System.out.println("EXAMPLE 2.1.1:\n" + oneResult.formatJSON()); // or, JSONFormat.DEFAULT_FOR_RESULTS               
        // System.out.println("EXAMPLE 2.1.1:\n" + oneResult.formatJSON(new JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT)));
        System.out.println("EXAMPLE 2.2.1:\n" + oneResult.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS));

        // Result<Record3<String, Long, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetch();

        System.out.println("EXAMPLE 2.1.2:\n" + result.formatJSON()); // or, JSONFormat.DEFAULT_FOR_RESULTS               
        System.out.println("EXAMPLE 2.2.2:\n" + result.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("resultArray.json"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()                    
                    .fetch()
                    .formatJSON(bw, JSONFormat.DEFAULT_FOR_RECORDS);
        } catch (IOException ex) {
            // handle exception
        }
        
        JSONFormat jsonFormat = new JSONFormat()
                .indent(4)      // defaults to 2
                .header(false)  // default to true
                .newline("\r")  // "\n" is default
                .recordFormat(JSONFormat.RecordFormat.OBJECT); // defaults to ARRAY                                

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("resultObject.json"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()                    
                    .fetch()
                    .formatJSON(bw, jsonFormat);
        } catch (IOException ex) {
            // handle exception
        }               

        return result.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
    }

    public String formatResultAsXML() {

        // <Record3<String, Long, String>
        var oneResult = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetchAny();
        
        // both output the same result 
        System.out.println("EXAMPLE 3.1.1:\n" + oneResult.formatXML()); // or, XMLFormat.DEFAULT_FOR_RESULTS               
        // System.out.println("EXAMPLE 3.1.1:\n" + oneResult.formatXML(new XMLFormat().recordFormat(XMLFormat.RecordFormat.COLUMN_NAME_ELEMENTS))); 
        System.out.println("EXAMPLE 3.2.1:\n" + oneResult.formatXML(XMLFormat.DEFAULT_FOR_RECORDS));
        
        // Result<Record3<String, Long, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetch();

        System.out.println("EXAMPLE 3.1.2:\n" + result.formatXML()); // or, XMLFormat.DEFAULT_FOR_RESULTS               
        System.out.println("EXAMPLE 3.2.2:\n" + result.formatXML(XMLFormat.DEFAULT_FOR_RECORDS));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("resultDefault.xml"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()
                    .fetch()
                    .formatXML(bw, XMLFormat.DEFAULT_FOR_RECORDS);
        } catch (IOException ex) {
            // handle exception
        }

        XMLFormat xmlFormat = new XMLFormat()
                .indent(4)      // defaults to 2
                .header(false)  // default to true
                .newline("\r")  // "\n" is default
                .recordFormat(XMLFormat.RecordFormat.COLUMN_NAME_ELEMENTS); // defaults to VALUE_ELEMENTS_WITH_FIELD_ATTRIBUTE
        
        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("resultColumnNameElements.xml"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()
                    .fetch()
                    .formatXML(bw, xmlFormat);
        } catch (IOException ex) {
            // handle exception
        }           

        return result.formatXML(XMLFormat.DEFAULT_FOR_RECORDS);
    }

    public String formatResultAsHTML() {

        // Result<Record3<String, Long, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCT_LINE,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetch();

        System.out.println("EXAMPLE 4:\n" + result.formatHTML());

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("result.html"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCTLINE.PRODUCT_LINE,
                    PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .from(PRODUCTLINE)
                    .join(PRODUCT)
                    .onKey()
                    .fetch()
                    .formatHTML(bw);
        } catch (IOException ex) {
            // handle exception
        }

        return result.formatHTML();
    }

    public String formatResultAsCSV() {

        // Result<Record3<String, String, Long, String>>
        var result = ctx.select(OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME)
                .from(OFFICE)
                .leftJoin(DEPARTMENT)
                .onKey()
                .fetch();

        System.out.println("EXAMPLE 5.1:\n" + result.formatCSV());
        System.out.println("EXAMPLE 5.2:\n" + result.formatCSV('\t', "N/A"));
        System.out.println("EXAMPLE 5.3:\n" + result.formatCSV(false, ';', "N/A")); // no header        

        CSVFormat csvFormat = new CSVFormat()
                .delimiter("|")
                .nullString("{null}");
        // try out more options
        System.out.println("EXAMPLE 5.4:\n" + result.formatCSV(csvFormat));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("result.csv"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(OFFICE.CITY, OFFICE.COUNTRY,
                    DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME)
                    .from(OFFICE)
                    .leftJoin(DEPARTMENT)
                    .onKey()
                    .fetch()
                    .formatCSV(bw, csvFormat);
        } catch (IOException ex) {
            // handle exception
        }

        return result.formatCSV(csvFormat);
    }

    public String formatResultAs1Chart() {

        var result = ctx.select(SALE.FISCAL_YEAR, max(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        ChartFormat cf = new ChartFormat()
                .showLegends(true, true)
                .display(ChartFormat.Display.DEFAULT)
                .categoryAsText(true)
                .type(ChartFormat.Type.AREA)
                .shades('*')
                .numericFormat(decimalFormat);

        System.out.println("EXAMPLE 6.1:\n" + result.formatChart());
        System.out.println("EXAMPLE 6.2:\n" + result.formatChart(cf));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("result1Chart.txt"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(SALE.FISCAL_YEAR, max(SALE.SALE_))
                    .from(SALE)
                    .groupBy(SALE.FISCAL_YEAR)
                    .fetch()
                    .formatChart(bw, cf);
        } catch (IOException ex) {
            // handle exception
        }

        return result.formatChart(cf);
    }

    public String formatResultAs2Chart() {

        var result = ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE, field("avg_price"), PRODUCT.MSRP)
                .from(PRODUCT, lateral(select(
                        avg(ORDERDETAIL.PRICE_EACH).as("avg_price")).from(ORDERDETAIL)
                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))
                .limit(5)
                .fetch();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        ChartFormat cf = new ChartFormat()
                .showLegends(true, true)              // show legends  
                .display(ChartFormat.Display.DEFAULT) // try also, HUNDRED_PERCENT_STACKED
                .categoryAsText(true)                 // category as text
                .type(ChartFormat.Type.AREA)          // area chart type
                .shades('a', 'b', 'c')                // shades of PRODUCT.BUY_PRICE, PRODUCT.MSRP, avg(ORDERDETAIL.PRICE_EACH)
                .values(1, 2, 3)                      // value source column numbers
                .numericFormat(decimalFormat);        // numeric format

        System.out.println("EXAMPLE 7.1:\n" + result.formatChart());
        System.out.println("EXAMPLE 7.2:\n" + result.formatChart(cf));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("result2Chart.txt"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE, field("avg_price"), PRODUCT.MSRP)
                    .from(PRODUCT, lateral(select(
                            avg(ORDERDETAIL.PRICE_EACH).as("avg_price")).from(ORDERDETAIL)
                            .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))
                    .limit(5)
                    .fetch()
                    .formatChart(bw, cf);
        } catch (IOException ex) {
            // handle exception
        }

        return result.formatChart(cf);
    }

    @Transactional
    public String formatResultAsInserts() {

        var result = ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE, field("avg_price"), PRODUCT.MSRP)
                .from(PRODUCT, lateral(select(
                        avg(ORDERDETAIL.PRICE_EACH).as("avg_price")).from(ORDERDETAIL)
                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))
                .limit(5)
                .fetch();

        ctx.dropTemporaryTableIfExists("product_stats").execute();
        ctx.createTemporaryTableIfNotExists("product_stats")
                .column("product_id", BIGINT)
                .column("buy_price", DECIMAL)
                .column("avg_price", DECIMAL)
                .column("msrp", DECIMAL)
                .constraints(
                        primaryKey("product_id")
                ).execute();

        String inserts = result.formatInsert(table("product_stats"));
        System.out.println("EXAMPLE 8.1 (to insert):\n" + inserts);

        ctx.execute(inserts);

        System.out.println("EXAMPLE 8.2 (after insert):\n"
                + (ctx.selectFrom(table("product_stats")).fetch()));

        // fetch -> format -> export to file        
        try ( BufferedWriter bw = Files.newBufferedWriter(
                Paths.get("resultInserts.txt"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE, field("avg_price"), PRODUCT.MSRP)
                    .from(PRODUCT, lateral(select(
                            avg(ORDERDETAIL.PRICE_EACH).as("avg_price")).from(ORDERDETAIL)
                            .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))
                    .limit(5)
                    .fetch()
                    .formatInsert(bw, table("product_stats"));
        } catch (IOException ex) {
            // handle exception
        }

        return inserts;
    }
}
