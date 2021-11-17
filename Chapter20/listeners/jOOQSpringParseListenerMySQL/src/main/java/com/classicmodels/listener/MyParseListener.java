package com.classicmodels.listener;

import org.jooq.Field;
import org.jooq.ParseContext;
import org.jooq.impl.CustomField;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.covarPop;
import static org.jooq.impl.DSL.regrCount;
import static org.jooq.impl.DSL.sum;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLSERVER;
import org.jooq.impl.DefaultParseListener;
import org.jooq.impl.SQLDataType;

public class MyParseListener extends DefaultParseListener {

    @Override
    public Field parseField(ParseContext ctx) {

        if (ctx.parseFunctionNameIf("REGR_SXY")) {

            ctx.parse('(');
            Field x = ctx.parseField();
            ctx.parse(',');
            Field y = ctx.parseField();
            ctx.parse(')');

            return CustomField.of("", SQLDataType.DOUBLE, f -> {
                switch (f.family()) {
                    case SQLSERVER, MYSQL ->
                        f.visit(sum(x.mul(y)).minus(sum(x).mul(sum(y).div(count())))); // (SUM(X*Y)-SUM(X) * SUM(Y)/COUNT(*))
                    case ORACLE, POSTGRES ->
                        f.visit(regrCount(x, y).mul(covarPop(x, y))); // REGR_COUNT(expr1, expr2) * COVAR_POP(expr1, expr2)                    
                }
            });
        }

        return null;
    }

    /*  DE SCOS DUPA CE AM SCRIS SI AM MAI TESTAT
    @Override
    public Field<?> parseField(ParseContext ctx) {
                     
       if(ctx.parseNameIf().unqualifiedName().getName()[0].equals("product_line")) {
           return CustomField.of("", SQLDataType.VARCHAR,c -> {
                switch (c.family()) {
                    case MARIADB:
                    case MYSQL:
                        c.visit(field(name("ccccc")));
                        break;
                    default:
                        c.visit(field("qqqqq"));
                        break;
            }});                
       }
                
       return null;
    }*/
}
