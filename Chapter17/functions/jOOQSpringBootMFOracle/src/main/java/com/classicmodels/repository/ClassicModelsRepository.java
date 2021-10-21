package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.udt.EvaluationCriteria.EVALUATION_CRITERIA;
import static jooq.generated.udt.EvaluationCriteria.improve;
import static jooq.generated.udt.EvaluationCriteria.score;
import jooq.generated.udt.records.EvaluationCriteriaRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.inline;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeMemberFunction() {
        
        // EvaluationCriteriaRecord ecr = new EvaluationCriteriaRecord();        
        // ecr.attach(ctx.configuration());
        
        // EvaluationCriteriaRecord ecr = EVALUATION_CRITERIA.newRecord();
        // ecr.attach(ctx.configuration());
        
        EvaluationCriteriaRecord ecr = ctx.newRecord(EVALUATION_CRITERIA);        
        ecr.setCommunicationAbility(58);
        ecr.setEthics(30);
        ecr.setPerformance(26);
        ecr.setEmployeeInput(59);
                
        BigDecimal result = ecr.score();
        System.out.println("Result: " + result);
        
        EvaluationCriteriaRecord newEcr = ecr.improve(10);        
        
        System.out.println("Communication Ability: " + ecr.getCommunicationAbility());
        System.out.println("Ethics: " + ecr.getEthics());
        System.out.println("Performance: " + ecr.getPerformance());
        System.out.println("Employee Input: " + ecr.getEmployeeInput());
        System.out.println("Communication Ability (new): " + newEcr.getCommunicationAbility());
        System.out.println("Ethics (new): " + newEcr.getEthics());
        System.out.println("Performance (new): " + newEcr.getPerformance());
        System.out.println("Employee Input (new): " + newEcr.getEmployeeInput());
                
        ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .from(MANAGER)
                .where(score(MANAGER.MANAGER_EVALUATION).lt(newEcr.score()))
                .fetch();        
        
        ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME, score(MANAGER.MANAGER_EVALUATION))
                .from(MANAGER)
                .where(score(MANAGER.MANAGER_EVALUATION)
                        .gt(BigDecimal.valueOf(57)))
                .fetch();
        
        ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .from(MANAGER)
                .where(score(improve(MANAGER.MANAGER_EVALUATION, inline(10)))
                        .gt(BigDecimal.valueOf(57)))
                .fetch();
        
        ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .from(MANAGER)
                .where(score(improve(MANAGER.MANAGER_EVALUATION, inline(10)))
                        .gt(score(ecr.improve(5))))
                .fetch();
    }        

}





