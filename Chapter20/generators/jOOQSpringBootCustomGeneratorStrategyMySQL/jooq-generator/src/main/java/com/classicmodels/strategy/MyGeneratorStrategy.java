package com.classicmodels.strategy;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public class MyGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {

        if (definition.getQualifiedName().equals("classicmodels.employee") 
                && mode.equals(Mode.DEFAULT)) {
            return "reportsTo";
        }

        return super.getJavaMethodName(definition, mode);
    }
}
