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
    private String withInputSchema;    
    @NotEmpty
    private String withScripts;
    @NotEmpty
    private String withSort;
    @NotEmpty
    private String withUnqualifiedSchema;
    @NotEmpty
    private String withDefaultNameCase;
    @NotEmpty
    private String withPackageName;
    @NotEmpty
    private String withDirectory;

    public String getWithName() {
        return withName;
    }

    public void setWithName(String withName) {
        this.withName = withName;
    }

    public String getWithInputSchema() {
        return withInputSchema;
    }

    public void setWithInputSchema(String withInputSchema) {
        this.withInputSchema = withInputSchema;
    }

    public String getWithScripts() {
        return withScripts;
    }

    public void setWithScripts(String withScripts) {
        this.withScripts = withScripts;
    }

    public String getWithSort() {
        return withSort;
    }

    public void setWithSort(String withSort) {
        this.withSort = withSort;
    }

    public String getWithUnqualifiedSchema() {
        return withUnqualifiedSchema;
    }

    public void setWithUnqualifiedSchema(String withUnqualifiedSchema) {
        this.withUnqualifiedSchema = withUnqualifiedSchema;
    }

    public String getWithDefaultNameCase() {
        return withDefaultNameCase;
    }

    public void setWithDefaultNameCase(String withDefaultNameCase) {
        this.withDefaultNameCase = withDefaultNameCase;
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
}