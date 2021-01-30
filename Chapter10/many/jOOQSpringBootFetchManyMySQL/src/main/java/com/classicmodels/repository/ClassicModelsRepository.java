package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import org.jooq.Results;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {
    
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

  public void q() {
      
      Results results = ctx.select().from(EMPLOYEE).fetchMany()
              
              
              fetchMany("sp_help 'author'");
      
      System.out.println("R="+results);
  }
}
