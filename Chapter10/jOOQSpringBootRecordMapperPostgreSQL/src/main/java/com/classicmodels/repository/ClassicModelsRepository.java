package com.classicmodels.repository;

import com.classicmodels.pojo.MaxHeap;
import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.RecordMapper;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void recordMapperArbitraryObjects() {

        List<Double> result1 = ctx.select(ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                .from(ORDERDETAIL)
                .fetch(new RecordMapper<Record2<BigDecimal, Integer>, Double>() {

                    @Override
                    public Double map(Record2<BigDecimal, Integer> record) {
                        return record.get(ORDERDETAIL.PRICE_EACH).doubleValue()
                                * record.get(ORDERDETAIL.QUANTITY_ORDERED);
                    }
                });

        // or, as a lambda expression
        /*
        List<Double> result1 = ctx.select(ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                .from(ORDERDETAIL)
                .fetch((Record2<BigDecimal, Integer> record) 
                   -> record.get(ORDERDETAIL.PRICE_EACH).doubleValue()
                        * record.get(ORDERDETAIL.QUANTITY_ORDERED));
         */
        System.out.println("Example 1.1\n: " + result1);

        List<MaxHeap> result2 = ctx.select(EMPLOYEE.EMAIL, EMPLOYEE.MONTHLY_BONUS)
                .from(EMPLOYEE)
                .where(EMPLOYEE.MONTHLY_BONUS.isNotNull())
                .fetch((Record2<String, Integer[]> record) -> {
                    MaxHeap<Integer> heap = new MaxHeap(record.get(EMPLOYEE.EMAIL));

                    for(Integer i: record.get(EMPLOYEE.MONTHLY_BONUS)) {
                        heap.add(i);
                    }
                    
                    return heap;
                });

        System.out.println("Example 1.2\n");
        for (MaxHeap heap : result2) {
            heap.printHeap();
        }
    }

    public void recordMapperPojos() {

    }
}
