package com.classicmodels.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.DSLContext;
import org.jooq.JSON;
import static org.jooq.JSON.json;
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

        ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME, MANAGER.MANAGER_DETAIL)
                .values("Mark Kyerol", json("{\"skills\":[\"management\", \"business\"], \"education\":\"PhD\"}"))
                .execute();
    }

    public void fetchDetailOfManager() {

        List<JSON> managerDetail = ctx.select(MANAGER.MANAGER_DETAIL)
                .from(MANAGER)
                .where(MANAGER.MANAGER_NAME.eq("Mark Kyerol"))
                .fetch(MANAGER.MANAGER_DETAIL);

        System.out.println("Mark Kyerol's details: " + managerDetail);
    }
}