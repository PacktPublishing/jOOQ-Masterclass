package com.classicmodels.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Day 1
    public List<Sale> filterByFiscalYear(int fiscalYear) {

        return ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.eq(fiscalYear))
                .fetchInto(Sale.class);
    }

    // Day 2
    public List<Sale> filterByTrend(String trend) {

        return ctx.selectFrom(SALE)
                .where(SALE.TREND.eq(trend))
                .fetchInto(Sale.class);
    }

    // Day 3
    public List<Sale> filterByFiscalYearAndTrend(int fiscalYear, String trend) {

        return ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.eq(fiscalYear)
                        .and(SALE.TREND.eq(trend)))
                .fetchInto(Sale.class);
    }

    // Day 4
    public List<Sale> filterByz(SalePredicate sp) {

        return ctx.selectFrom(SALE)
                .where(sp.apply(SALE))
                .fetchInto(Sale.class);
    }

    // Day 5
    public List<Sale> filterByf(Function<Sale, Condition> sp) {

        return ctx.selectFrom(SALE)
                .where(sp.apply(SALE))
                .fetchInto(Sale.class);
    }

    // Day 6
    public List<Sale> filterByf(Function<Sale, Condition>... sp) {

        return ctx.selectFrom(SALE)
                .where(Arrays.stream(sp)
                        .map(f -> f.apply(SALE))
                        .collect(Collectors.toList()))
                .fetchInto(Sale.class);
    }

    // Day 7
    public <T extends Table, R extends Record> List<R>
            filterByfd(T t, Function<T, Condition>... sp) {

        return ctx.selectFrom(t)
                .where(Stream.of(sp).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }

    // Day 8
    public <T extends Table> List<Record>
            filterBydfd(T t, Supplier<Collection<SelectField<?>>> select, Function<T, Condition>... sp) {

        return ctx.select(select.get())
                .from(t)
                .where(Stream.of(sp).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }

    // ccc
    public <T extends Table, R extends Record> List<Record>
            filterBydfdddd(T t, Supplier<Collection<TableField<R, ?>>> select, Function<T, Condition>... sp) {

        return ctx.select(select.get())
                .from(t)
                .where(Stream.of(sp).map(f -> f.apply(t)).collect(toList()))
                .fetch();
    }

    // Day 9
    public <T extends Table> List<Record>
            filterBydfds(T t, Supplier<Collection<SelectField<?>>> select,
                    Supplier<Collection<Condition>> sp) {

        return ctx.select(select.get())
                .from(t)
                .where(sp.get())
                .fetch();
    }

}
