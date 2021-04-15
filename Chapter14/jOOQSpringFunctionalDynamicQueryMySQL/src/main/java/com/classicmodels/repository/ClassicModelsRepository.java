package com.classicmodels.repository;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.Sale;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.util.SaleFunction;
import jooq.generated.tables.records.SaleRecord;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Evolving from 0 to functional implementation */
    // Day 1 (writing a filter based on *fiscalYear*)
    public List<SaleRecord> filterSaleByFiscalYear(int fiscalYear) {

        return ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.eq(fiscalYear))
                .fetch();
    }

    // Day 2 (writing another filter based on *trend*)
    public List<SaleRecord> filterSaleByTrend(String trend) {

        return ctx.selectFrom(SALE)
                .where(SALE.TREND.eq(trend))
                .fetch();
    }

    // Day 3 (writing a filter based on *fiscalYear* and *trend*, this can become an issue if more filters are needed)
    public List<SaleRecord> filterSaleByFiscalYearAndTrend(int fiscalYear, String trend) {

        return ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.eq(fiscalYear)
                        .and(SALE.TREND.eq(trend)))
                .fetch();
    }

    // Day 4 (relying on Collection<Condition>)
    public List<SaleRecord> filterSaleBy1(Collection<Condition> cf) {

        return ctx.selectFrom(SALE)
                .where(cf)
                .fetch();
    }

    // Day 5 (writing an interface for more flexibility and type-safety)
    public List<SaleRecord> filterSaleBy2(SaleFunction<Sale, Condition> sf) {

        return ctx.selectFrom(SALE)
                .where(sf.apply(SALE))
                .fetch();
    }

    // Day 6 (wait, Java already has java.util.function.Function<T, R>)
    public List<SaleRecord> filterSaleBy3(Function<Sale, Condition> f) {

        return ctx.selectFrom(SALE)
                .where(f.apply(SALE))
                .fetch();
    }

    // Day 7 (allow the code to apply multiple conditions)
    public List<SaleRecord> filterSaleBy4(Function<Sale, Condition>... ff) {

        return ctx.selectFrom(SALE)
                .where(Stream.of(ff).map(f -> f.apply(SALE)).collect(toList()))
                .fetch();
    }

    // Day 8 (add genericity)
    public <T extends Table<R>, R extends Record> List<R>
            filterBy5(T t, Function<T, Condition>... ff) {

        return ctx.selectFrom(t)
                .where(Stream.of(ff).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }

    // Day 9 (select certain fields (TableField), add Supplier to defer creation)
    public <T extends Table<R>, R extends Record> List<Record>
            filterBy6(T t, Supplier<Collection<TableField<R, ?>>> select, Function<T, Condition>... ff) {

        return ctx.select(select.get())
                .from(t)
                .where(Stream.of(ff).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }

    // Still day 9 (select certain fields (SelectField), add Supplier to defer creation)
    public <T extends Table<R>, R extends Record> List<Record>
            filterBy7(T t, Supplier<Collection<SelectField<?>>> select, Function<T, Condition>... ff) {

        return ctx.select(select.get())
                .from(t)
                .where(Stream.of(ff).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }   
}
