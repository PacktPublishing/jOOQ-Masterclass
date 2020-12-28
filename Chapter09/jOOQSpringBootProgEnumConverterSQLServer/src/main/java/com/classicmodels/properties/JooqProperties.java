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
    private String withUserType1;
    @NotEmpty
    private String withConverter1;
    @NotEmpty
    private String withIncludeExpression1;
    @NotEmpty
    private String withIncludeTypes1;
    @NotEmpty
    private String withUserType2;
    @NotEmpty
    private String withConverter2;
    @NotEmpty
    private String withIncludeExpression2;
    @NotEmpty
    private String withIncludeTypes2;
    @NotEmpty
    private String withUserType3;
    @NotEmpty
    private String withConverter3;
    @NotEmpty
    private String withIncludeExpression3;
    @NotEmpty
    private String withIncludeTypes3;

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

    public String getWithUserType1() {
        return withUserType1;
    }

    public void setWithUserType1(String withUserType1) {
        this.withUserType1 = withUserType1;
    }

    public String getWithConverter1() {
        return withConverter1;
    }

    public void setWithConverter1(String withConverter1) {
        this.withConverter1 = withConverter1;
    }

    public String getWithIncludeExpression1() {
        return withIncludeExpression1;
    }

    public void setWithIncludeExpression1(String withIncludeExpression1) {
        this.withIncludeExpression1 = withIncludeExpression1;
    }

    public String getWithIncludeTypes1() {
        return withIncludeTypes1;
    }

    public void setWithIncludeTypes1(String withIncludeTypes1) {
        this.withIncludeTypes1 = withIncludeTypes1;
    }

    public String getWithUserType2() {
        return withUserType2;
    }

    public void setWithUserType2(String withUserType2) {
        this.withUserType2 = withUserType2;
    }

    public String getWithConverter2() {
        return withConverter2;
    }

    public void setWithConverter2(String withConverter2) {
        this.withConverter2 = withConverter2;
    }

    public String getWithIncludeExpression2() {
        return withIncludeExpression2;
    }

    public void setWithIncludeExpression2(String withIncludeExpression2) {
        this.withIncludeExpression2 = withIncludeExpression2;
    }

    public String getWithIncludeTypes2() {
        return withIncludeTypes2;
    }

    public void setWithIncludeTypes2(String withIncludeTypes2) {
        this.withIncludeTypes2 = withIncludeTypes2;
    }

    public String getWithUserType3() {
        return withUserType3;
    }

    public void setWithUserType3(String withUserType3) {
        this.withUserType3 = withUserType3;
    }

    public String getWithConverter3() {
        return withConverter3;
    }

    public void setWithConverter3(String withConverter3) {
        this.withConverter3 = withConverter3;
    }

    public String getWithIncludeExpression3() {
        return withIncludeExpression3;
    }

    public void setWithIncludeExpression3(String withIncludeExpression3) {
        this.withIncludeExpression3 = withIncludeExpression3;
    }

    public String getWithIncludeTypes3() {
        return withIncludeTypes3;
    }

    public void setWithIncludeTypes3(String withIncludeTypes3) {
        this.withIncludeTypes3 = withIncludeTypes3;
    }
}