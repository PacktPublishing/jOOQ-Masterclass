package com.classicmodels.generator;

import java.time.LocalDate;
import java.util.List;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.TableDefinition;

public class CustomJavaGenerator extends JavaGenerator {

    @Override
    protected void generateDaoClassFooter(TableDefinition table, JavaWriter out) {

        final String pType = getStrategy().getFullJavaClassName(table, Mode.POJO);

        // add a method common to all DAOs
        out.tab(1).javadoc("Fetch the number of records limited by <code>value</code>");
        out.tab(1).println("public %s<%s> findLimitedTo(%s value) {", List.class, pType, Integer.class);
        out.tab(2).println("return ctx().selectFrom(%s)", getStrategy().getFullJavaIdentifier(table));
        out.tab(3).println(".limit(value)");
        out.tab(3).println(".fetch(mapper());");
        out.tab(1).println("}");

        // add a method specific to Order DAO
        if (table.getName().equals("order")) {            
            out.println();
            out.tab(1).javadoc("Fetch orders having status <code>statusVal</code> and order date after <code>orderDateVal</code>");
            out.tab(1).println("public %s<%s> findOrderByStatusAndOrderDate(%s statusVal, %s orderDateVal) {",
                     List.class, pType, String.class, LocalDate.class);
            out.tab(2).println("return ctx().selectFrom(%s)", getStrategy().getFullJavaIdentifier(table));
            out.tab(3).println(".where(%s.eq(statusVal))", getStrategy().getFullJavaIdentifier(table.getColumn("status")));
            out.tab(3).println(".and(%s.ge(orderDateVal))", getStrategy().getFullJavaIdentifier(table.getColumn("order_date")));
            out.tab(3).println(".fetch(mapper());");
            out.tab(1).println("}");
        }
    }
}