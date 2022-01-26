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
    public void insertManagerWithDetail() throws JsonProcessingException {

        JsonNode managerDetail = new ObjectMapper().readTree(
                "{\"skills\":[\"management\", \"business\"], \"education\":\"PhD\"}");

        ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME, MANAGER.MANAGER_DETAIL)
                .values("Mark Kyerol", managerDetail)
                .execute();
    }

    public void fetchDetailOfManager() {

        List<JsonNode> managerDetail = ctx.select(MANAGER.MANAGER_DETAIL)
                .from(MANAGER)
                .where(MANAGER.MANAGER_NAME.eq("Mark Kyerol"))
                .fetch(MANAGER.MANAGER_DETAIL);

        System.out.println("Mark Kyerol's details: " + managerDetail);
    }
}