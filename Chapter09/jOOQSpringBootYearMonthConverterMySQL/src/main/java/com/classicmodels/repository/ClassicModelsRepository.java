package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_ARR_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.tables.Department.DEPARTMENT;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.DataType;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.INTEGER;
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
    public void insertDepartment() {

        // converter is not used (we insert '202010' directly)
        ctx.insertInto(DEPARTMENT)
                .values(null, "HR", "-int 8799", 2231, 4, "{\"HR\": [\"hiring\", \"interview\"]}",
                        202010)
                .execute();

        // use converter explicitly by passing to it a valid YearMonth value
        ctx.insertInto(DEPARTMENT)
                .values(null, "HR", "-int 8799", 2231, 4, "{\"HR\": [\"hiring\", \"interview\"]}",
                        INTEGER_YEARMONTH_CONVERTER.to(YearMonth.of(2020, 10)))
                .execute();

        // convert via data type       
        ctx.insertInto(DEPARTMENT)
                .values(null, "HR", "-int 8799", 2231, 4, "{\"HR\": [\"hiring\", \"interview\"]}",
                        val(YearMonth.of(2020, 10), YEARMONTH))
                .execute();

        // define the converter locally
        Converter<Integer, YearMonth> converter = Converter.ofNullable(Integer.class, YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(t / 100, t % 100);
                },
                (YearMonth u) -> {
                    return (u.getYear() * 100) + u.getMonth().getValue();
                });

        // use the above 'converter'
        ctx.insertInto(DEPARTMENT)
                .values(null, "HR", "-int 8799", 2231, 4, "{\"HR\": [\"hiring\", \"interview\"]}",
                        converter.to(YearMonth.of(2020, 10)))
                .execute();

        // declare data type locally
        DataType<YearMonth> yearmonth = INTEGER.asConvertedDataType(YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(t / 100, t % 100);
                },
                (YearMonth u) -> {
                    return (u.getYear() * 100) + u.getMonth().getValue();
                });
        
        // convert via the above data type       
        ctx.insertInto(DEPARTMENT)
                .values(null, "HR", "-int 8799", 2231, 4, "{\"HR\": [\"hiring\", \"interview\"]}",
                        val(YearMonth.of(2020, 10), yearmonth))
                .execute();
    }

    public void findHRDepartment() {

        // fetch any DEPARTMENT.OPEN_DATE and convert it via data type's convert()
        Integer resultInt = ctx.select(DEPARTMENT.OPEN_DATE)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetchAny(DEPARTMENT.OPEN_DATE);

        System.out.println("Converter is not used, so fetch as Integer: " + resultInt);
        YearMonth resultYM = YEARMONTH.convert(resultInt); // there is a convert() for array and one for List as well
        System.out.println("Use data type's convert() method: " + resultYM);

        // fetch an array of DEPARTMENT.OPEN_DATE and convert via INTEGER_YEARMONTH_ARR_CONVERTER
        Integer[] resultArrInt = ctx.select(DEPARTMENT.OPEN_DATE)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetchArray(DEPARTMENT.OPEN_DATE);

        YearMonth[] resultArrYM1 = INTEGER_YEARMONTH_ARR_CONVERTER.from(resultArrInt);
        System.out.println("Use converter for arrays: " + Arrays.toString(resultArrYM1));

        // fetch an array of DEPARTMENT.OPEN_DATE and convert in fetchArray()
        YearMonth[] resultArrYM2 = ctx.select(DEPARTMENT.OPEN_DATE)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetchArray(DEPARTMENT.OPEN_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Convert in fetchArray(): " + Arrays.toString(resultArrYM2));

        // fetch a List of DEPARTMENT.OPEN_DATE and convertin fetch()
        List<YearMonth> resultListYM = ctx.select(DEPARTMENT.OPEN_DATE)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(DEPARTMENT.OPEN_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Convert in fetchArray(): " + resultListYM);

        // coercing
        ctx.select(DEPARTMENT.OPEN_DATE.coerce(YEARMONTH))
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch();

        // casting
        ctx.select(DEPARTMENT.OPEN_DATE.cast(YEARMONTH))
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch();

        // new field
        ctx.select(field(DEPARTMENT.OPEN_DATE.getName(), YEARMONTH))
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch();
    }
}