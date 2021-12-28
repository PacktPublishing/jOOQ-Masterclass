package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import jooq.generated.tables.records.OrderdetailRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.SelectConditionStep;
import org.jooq.SelectField;
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSeekStep1;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {
    
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;        
    }
    
    public String getSQLOfQuantityOrderedGroupedByOrderLineNumber() {
        
        /* Fluent SQL */
        /* return */
        DSL.select(ORDERDETAIL.ORDER_LINE_NUMBER, 
                sum(ORDERDETAIL.QUANTITY_ORDERED).as("itemsCount"),
                sum(ORDERDETAIL.PRICE_EACH.mul(ORDERDETAIL.QUANTITY_ORDERED)).as("total"))
                .from(ORDERDETAIL)
                .where((val(20).lt(ORDERDETAIL.QUANTITY_ORDERED)))
                .groupBy(ORDERDETAIL.ORDER_LINE_NUMBER)
                .orderBy(ORDERDETAIL.ORDER_LINE_NUMBER)
                .getSQL();

        /* Identify column expression */        
        Field<Integer> tc1 = ORDERDETAIL.ORDER_LINE_NUMBER;  // table column expression 
        // TableField<OrderdetailRecord,Integer> tfc1 = ORDERDETAIL.ORDER_LINE_NUMBER; // or, as a TableField
        Field<Integer> tc2 = ORDERDETAIL.QUANTITY_ORDERED; // table column expression
        // TableField<OrderdetailRecord,Integer> tfc2 = ORDERDETAIL.QUANTITY_ORDERED; // or, as a TableField
        Field<BigDecimal> tc3 = ORDERDETAIL.PRICE_EACH;    // table column expression
        // TableField<OrderdetailRecord,BigDecimal> tfc3 = ORDERDETAIL.PRICE_EACH; // or, as a TableField
        Field<Integer> uc1 = val(20);                   // Unnamed column expression     

        /* return */
        DSL.select(tc1, sum(tc2).as("itemsCount"),
                sum(tc3.mul(tc2)).as("total"))
                .from(ORDERDETAIL)
                .where(uc1.lt(tc2))
                .groupBy(tc1)
                .orderBy(tc1)
                .getSQL();

        Field<BigDecimal> f1 = sum(tc2);                // aggregate function expression        
        Field<BigDecimal> m1 = tc3.mul(tc2);            // arithmetic expression
        Field<BigDecimal> f2 = sum(m1);                 // aggregate function expression
        Field<BigDecimal> a1 = f1.as("itemsCount");     // alias expression
        Field<BigDecimal> a2 = f2.as("total");          // alias expression                
        
        /* return */
        DSL.select(tc1, a1, a2)
                .from(ORDERDETAIL)
                .where(uc1.lt(tc2))
                .groupBy(tc1)
                .orderBy(tc1)
                .getSQL();
        
        /* Identify tables */
        // Table<?> t1 = ORDERDETAIL;              // non type-safe table expression
        Table<OrderdetailRecord> t1 = ORDERDETAIL; // type-safe table expression       
                
        /* return */        
        DSL.select(tc1, a1, a2)        
                .from(t1)
                .where(uc1.lt(tc2))
                .groupBy(tc1)
                .orderBy(tc1)
                .getSQL();
        
        // you could even do this, but there is no more type-safety
        Collection<? extends SelectField> sf = List.of(tc1, a1, a2);
        // return ctx.select(sf) ...
        
        /* Identify conditions */
        
        Condition c1 = uc1.lt(tc2); // condition
        
        /* return */
        DSL.select(tc1, a1, a2)
                .from(t1)
                .where(c1)
                .groupBy(tc1)
                .orderBy(tc1)
                .getSQL();
        
        /* Identify query-steps */
        
        SelectSelectStep s1 = DSL.select(tc1, a1, a2);
        SelectJoinStep s2 = s1.from(t1);
        SelectConditionStep s3 = s2.where(c1);
        SelectHavingStep s4 = s3.groupBy(tc1);
        SelectSeekStep1 s5 = s4.orderBy(tc1);   
        
        SelectSelectStep<Record3<Integer, BigDecimal, BigDecimal>> s1ts = DSL.select(tc1, a1, a2);
        SelectJoinStep<Record3<Integer, BigDecimal, BigDecimal>> s2ts = s1ts.from(t1);
        SelectConditionStep<Record3<Integer, BigDecimal, BigDecimal>> s3ts = s2ts.where(c1);
        SelectHavingStep<Record3<Integer, BigDecimal, BigDecimal>> s4ts = s3ts.groupBy(tc1);
        SelectSeekStep1<Record3<Integer, BigDecimal, BigDecimal>, Integer> s5ts = s4ts.orderBy(tc1);
        
        // execute the query
        ctx.fetch(s5); // s5ts
        
        return s5ts.getSQL(); // s5.getSQL();               
    }

}