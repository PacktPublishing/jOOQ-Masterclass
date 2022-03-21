package com.classicmodels.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static jooq.generated.Routines.dup;
import static jooq.generated.Routines.makeArray;
import jooq.generated.routines.Dup;
import jooq.generated.routines.MakeArray;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.val;
import org.postgresql.jdbc.PgArray;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executePolymorphicFunction() throws SQLException {

        // Calling make_array()
        // EXECUTION 1
        MakeArray ma = new MakeArray();
        ma.set__1(1);
        ma.set__2(2);

        ma.execute(ctx.configuration());

        PgArray arr = (PgArray) ma.getReturnValue();
        ResultSet rs = arr.getResultSet();
        List<Integer> result = new ArrayList();
        while (rs.next()) {
            result.add(rs.getInt(2));
        }

        System.out.println("Return: " + ma.getReturnValue());
        System.out.println("List: " + result);

        // EXECUTION 2
        ctx.select(makeArray(1, 2).as("ia"), makeArray("a", "b").as("ta")).fetch();

        // Calling dup()
        // EXECUTION 1
        Dup dup = new Dup();
        dup.setF1(10);
        dup.execute(ctx.configuration());

        System.out.println(dup.getF2());
        System.out.println(dup.getF3());

        // EXECUTION 2
        ctx.select(val(dup(ctx.configuration(), 10).getF2())).fetch();
        ctx.fetchValue(val(dup(ctx.configuration(), 10).getF2()));
    }
}
