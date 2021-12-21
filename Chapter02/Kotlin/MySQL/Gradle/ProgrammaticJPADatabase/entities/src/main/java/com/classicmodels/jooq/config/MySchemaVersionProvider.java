package com.classicmodels.jooq.config;

public class MySchemaVersionProvider implements org.jooq.meta.SchemaVersionProvider {

    public String version(org.jooq.meta.SchemaDefinition schema) {

        return schema.getName() + "_1";
    }
}
