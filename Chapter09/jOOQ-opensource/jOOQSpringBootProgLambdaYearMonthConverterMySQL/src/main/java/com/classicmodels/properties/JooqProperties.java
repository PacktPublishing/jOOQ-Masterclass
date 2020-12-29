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
    private String withInputSchema;
    @NotEmpty
    private String withPackageName;
    @NotEmpty
    private String withDirectory;
    @NotEmpty
    private String withUserType;
    @NotEmpty
    private String withFrom;
    @NotEmpty
    private String withTo;
    @NotEmpty
    private String withIncludeExpression;
    @NotEmpty
    private String withIncludeTypes;

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

    public String getWithInputSchema() {
        return withInputSchema;
    }

    public void setWithInputSchema(String withInputSchema) {
        this.withInputSchema = withInputSchema;
    }

    public String getWithUserType() {
        return withUserType;
    }

    public void setWithUserType(String withUserType) {
        this.withUserType = withUserType;
    }
    
    public String getWithIncludeExpression() {
        return withIncludeExpression;
    }

    public void setWithIncludeExpression(String withIncludeExpression) {
        this.withIncludeExpression = withIncludeExpression;
    }

    public String getWithIncludeTypes() {
        return withIncludeTypes;
    }

    public void setWithIncludeTypes(String withIncludeTypes) {
        this.withIncludeTypes = withIncludeTypes;
    }    

    public String getWithFrom() {
        return withFrom;
    }

    public void setWithFrom(String withFrom) {
        this.withFrom = withFrom;
    }

    public String getWithTo() {
        return withTo;
    }

    public void setWithTo(String withTo) {
        this.withTo = withTo;
    }        
}