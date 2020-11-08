package com.classicmodels.generator;

import java.util.List;
import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;
import org.jooq.tools.StringUtils;

public class CustomStrategy extends DefaultGeneratorStrategy {

    public static final String DAO_CLASS_PREFIX = "JooqGen";
    public static final String DAO_CLASS_SUFFIX = "RepositoryImpl";
    public static final String DAO_INTERFACE_SUFFIX = "Repository";

    /* each DAO class is named FooRepositoryImpl */
    @Override
    public String getJavaClassName(final Definition definition, final Mode mode) {

        if (mode.equals(Mode.DAO)) {
            // get the default name of a DAO class (e.g., CustomerDao)            
            String name = super.getJavaClassName(definition, mode); 
            
            // remove the default 'Dao' suffix
            name = name.substring(0, name.length() - 3); 

            // CustomerDao -> JooqGenCustomerRepositoryImpl            
            return (DAO_CLASS_PREFIX + name + DAO_CLASS_SUFFIX);
        } else {
            return super.getJavaClassName(definition, mode);
        }
    }

    /* each DAO class implements an interface named FooRepository */
    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {

        if (mode.equals(Mode.DAO)) {

            // get the default interfaces
            List<String> interfaces = super.getJavaClassImplements(definition, mode);

            // assume PASCAL names the default           
            interfaces.add(DAO_CLASS_PREFIX + StringUtils.toCamelCase(definition.getName()) + DAO_INTERFACE_SUFFIX);

            return interfaces;
        }

        return super.getJavaClassImplements(definition, mode);
    }

}