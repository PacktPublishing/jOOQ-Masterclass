package com.classicmodels.repository;

import java.util.List;
import jooq.generated.embeddables.pojos.OfficeMasterPk;
import jooq.generated.embeddables.records.OfficeMasterPkRecord;
import static jooq.generated.tables.CustomerMaster.CUSTOMER_MASTER;
import static jooq.generated.tables.OfficeMaster.OFFICE_MASTER;
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

    public void selectFromCustomerOfficeMaster() {

        var result1 = ctx.select(OFFICE_MASTER.OFFICE_CODE, OFFICE_MASTER.PHONE)
                .from(OFFICE_MASTER)
                .where(OFFICE_MASTER.OFFICE_MASTER_PK.in(
                        new OfficeMasterPkRecord("USA", "MA", "Boston"),
                        new OfficeMasterPkRecord("USA", "CA", "San Francisco")))
                .fetch();

        System.out.println("Result 1:\n" + result1);

        List<OfficeMasterPk> result2 = ctx.select(OFFICE_MASTER.OFFICE_MASTER_PK)
                .from(OFFICE_MASTER)
                .where(OFFICE_MASTER.OFFICE_CODE.eq("1"))
                .fetchInto(OfficeMasterPk.class);

        System.out.println("Result 2:\n" + result2);

        var result3 = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, CUSTOMER_MASTER.CREDIT_LIMIT,
                CUSTOMER_MASTER.CUSTOMER_OFFICE_MASTER_FK)
                .from(CUSTOMER_MASTER)
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 3:\n" + result3);
    }

    @Transactional
    public void joinCustomerAndOfficeaMasterViews() {

        var result = ctx.select(CUSTOMER_MASTER.CUSTOMER_NAME, CUSTOMER_MASTER.CREDIT_LIMIT,
                OFFICE_MASTER.OFFICE_CODE, OFFICE_MASTER.PHONE)
                .from(CUSTOMER_MASTER)
                .innerJoin(OFFICE_MASTER)
                .on(OFFICE_MASTER.OFFICE_MASTER_PK.eq(CUSTOMER_MASTER.CUSTOMER_OFFICE_MASTER_FK))
                .orderBy(CUSTOMER_MASTER.CUSTOMER_NAME)
                .fetch();

        System.out.println("Result 4:\n" + result);
    }

    @Transactional
    public void updateOffice() {

        ctx.update(OFFICE_MASTER)
                .set(OFFICE_MASTER.PHONE, "+16179821809")
                .where(OFFICE_MASTER.OFFICE_MASTER_PK.eq(new OfficeMasterPkRecord("USA", "MA", "Boston")))
                .execute();

        OfficeMasterPk om = new OfficeMasterPk("USA", "CA", "San Francisco");
        OfficeMasterPkRecord omr = new OfficeMasterPkRecord();
        omr.from(om);

        ctx.update(OFFICE_MASTER)
                .set(OFFICE_MASTER.PHONE, "1-877-247-3852")
                .where(OFFICE_MASTER.OFFICE_MASTER_PK.eq(omr))
                .execute();

        // in the same manner, you can execute DELETE/INSERT
    }
}
