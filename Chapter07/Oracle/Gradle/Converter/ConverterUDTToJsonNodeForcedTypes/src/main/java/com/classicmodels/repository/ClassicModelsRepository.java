package com.classicmodels.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertManagerEvaluation() throws JsonProcessingException {

        JsonNode managerEvaluation = new ObjectMapper().readTree(
                "{\"ca\": 345, \"et\": 422, \"pr\": 455, \"ei\":500 }");

        ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                .values("Mark Joy", managerEvaluation)
                .execute();
    }

    public void fetchManagerEvaluation() {

        List<JsonNode> managerEvaluation = ctx.select(MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetch(MANAGER.MANAGER_EVALUATION);

        System.out.println("Manager evaluation:" + managerEvaluation);
    }
}
