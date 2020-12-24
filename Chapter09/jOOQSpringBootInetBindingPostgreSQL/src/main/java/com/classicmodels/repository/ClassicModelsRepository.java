package com.classicmodels.repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import static jooq.generated.Sequences.DEPARTMENT_DEPARTMENT_ID_SEQ;
import static jooq.generated.tables.Department.DEPARTMENT;
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
    public void insertDepInet() throws UnknownHostException {

        ctx.insertInto(DEPARTMENT)
                .values(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval(),
                        "HR", "-int 8799", 2231, 4, new String[]{"hiring", "interiew"},
                        202010, InetAddress.getByName("128.10.124.12"))
                .execute();
    }

    public void fetchDepInet() {

        List<InetAddress> inets = ctx.select(DEPARTMENT.DEP_NET_IPV4)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(DEPARTMENT.DEP_NET_IPV4);

        System.out.println("Inet addrs: " + inets);
    }
}
