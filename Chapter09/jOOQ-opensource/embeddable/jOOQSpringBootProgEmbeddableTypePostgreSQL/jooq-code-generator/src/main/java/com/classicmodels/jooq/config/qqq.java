/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classicmodels.jooq.config;

import com.classicmodels.properties.JooqProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author leopr
 */
@org.springframework.context.annotation.Configuration
public class qqq {
    
    @Bean(name = "jooqProperties")
    public JooqProperties jooqProperties() {
        System.out.println("2222222222222222222222222222222222222222222222222222");
        return new JooqProperties();
    }
    
}
