package com.classicmodels.repository;

import com.classicmodels.mapper.CustomerRecordMapper;
import com.classicmodels.pojo.FlatProductline;
import com.classicmodels.pojo.MaxHeap;
import com.classicmodels.pojo.SimpleCustomer;
import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.RecordMapper;
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

                    List<Integer> tax = List.of(1, 2, 3, 4, 5);
                    
                    @Override
                    public Double map(Record2<BigDecimal, Integer> record) {
                                                
                        double total = record.get(ORDERDETAIL.PRICE_EACH).doubleValue()
                                * record.get(ORDERDETAIL.QUANTITY_ORDERED);                                              
                        
                        return total - tax.get((int) (total % 5));
                    }
                });
        
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
        
        List<SimpleCustomer> result1 = ctx.selectFrom(CUSTOMER)
              //.fetchInto(SimpleCustomer.class);      // using jOOQ default mapper
                .fetch(new CustomerRecordMapper());    // use our mapper
        System.out.println("Example 2.1\n" + result1);
        
        List<FlatProductline> r = ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchInto(FlatProductline.class);
        System.out.println("Example 2.2\n" + r);
    }
}
