package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.GroupField;
import org.jooq.SelectField;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SortField;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void useCte() {

        Result<Record> result = cte(
                "t",
                select(EMPLOYEE.EMPLOYEE_NUMBER.as("data_val"),
                        rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER).as("data_seq"),
                        EMPLOYEE.EMPLOYEE_NUMBER.minus(
                                rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER)).as("absent_data_grp"))
                        .from(EMPLOYEE),
                new Field[]{
                    field(name("absent_data_grp")), count(),
                    min(field(name("data_val"))).as("start_data_val")
                },
                null,
                new GroupField[]{field(name("absent_data_grp"))},
                null);

        System.out.println("Result :\n" + result);
    }

    private Result<Record> cte(String cteName, Select select, SelectField<?>[] fields,
            Condition condition, GroupField[] groupBy, SortField<?>[] orderBy) {

        var cte = ctx.with(cteName).as(select);

        var cteSelect = fields == null ? cte.select() : cte.select(fields)
                .from(table(name(cteName)));

        if (condition != null) {
            cteSelect.where(condition);
        }

        if (groupBy != null) {
            cteSelect.groupBy(groupBy);
        }

        if (orderBy != null) {
            cteSelect.orderBy(orderBy);
        }

        return cteSelect.fetch();
    }

    // using a dynamic number of CTEs    
    private void ctes(List<CommonTableExpression<?>> ctes) {

        ctx.with(ctes)
                .select()
                .from()
                .fetch();
    }

    private void ctes(CommonTableExpression<?> cte1, CommonTableExpression<?> cte2,
            CommonTableExpression<?> cte3 //, ...
    ) {

        ctx.with(cte1, cte2, cte3)
                .select()
                .from()
                .fetch();
    }
}