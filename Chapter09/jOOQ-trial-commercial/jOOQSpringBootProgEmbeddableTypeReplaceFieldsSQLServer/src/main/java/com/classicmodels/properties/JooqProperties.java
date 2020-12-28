package com.classicmodels.properties;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.jooq")
public class JooqProperties {

    @NotEmpty
    private String withName;
    @NotEmpty
    private String withSchemaVersionProvider;
    @NotEmpty
    private String withIncludes;
    @NotEmpty
    private String withExcludes;
    @NotEmpty
    private String withInputCatalog;
    @NotEmpty
    private String withInputSchema;
    @NotEmpty
    private String withPackageName;
    @NotEmpty
    private String withDirectory;
    @NotEmpty
    private String withCatalog;
    @NotEmpty
    private String withSchema;
    @NotEmpty
    private String withEmbeddableName;
    @NotEmpty
    private String withComment;
    @NotEmpty
    private String withTables;

    public String getWithName() {
        return withName;
    }

    public void setWithName(String withName) {
        this.withName = withName;
    }

    public String getWithSchemaVersionProvider() {
        return withSchemaVersionProvider;
    }

    public void setWithSchemaVersionProvider(String withSchemaVersionProvider) {
        this.withSchemaVersionProvider = withSchemaVersionProvider;
    }

    public String getWithIncludes() {
        return withIncludes;
    }

    public void setWithIncludes(String withIncludes) {
        this.withIncludes = withIncludes;
    }

    public String getWithExcludes() {
        return withExcludes;
    }

    public void setWithExcludes(String withExcludes) {
        this.withExcludes = withExcludes;
    }

    public String getWithInputCatalog() {
        return withInputCatalog;
    }

    public void setWithInputCatalog(String withInputCatalog) {
        this.withInputCatalog = withInputCatalog;
    }   
    
    public String getWithInputSchema() {
        return withInputSchema;
    }

    public void setWithInputSchema(String withInputSchema) {
        this.withInputSchema = withInputSchema;
    }

    public String getWithPackageName() {
        return withPackageName;
    }

    public void setWithPackageName(String withPackageName) {
        this.withPackageName = withPackageName;
    }

    public String getWithDirectory() {
        return withDirectory;
    }

    public void setWithDirectory(String withDirectory) {
        this.withDirectory = withDirectory;
    }

    public String getWithCatalog() {
        return withCatalog;
    }

    public void setWithCatalog(String withCatalog) {
        this.withCatalog = withCatalog;
    }

    public String getWithSchema() {
        return withSchema;
    }

    public void setWithSchema(String withSchema) {
        this.withSchema = withSchema;
    }

    public String getWithEmbeddableName() {
        return withEmbeddableName;
    }

    public void setWithEmbeddableName(String withEmbeddableName) {
        this.withEmbeddableName = withEmbeddableName;
    }

    public String getWithComment() {
        return withComment;
    }

    public void setWithComment(String withComment) {
        this.withComment = withComment;
    }

    public String getWithTables() {
        return withTables;
    }

    public void setWithTables(String withTables) {
        this.withTables = withTables;
    }
}