package com.classicmodels.repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import static jooq.generated.Sequences.DEPARTMENT_DEPARTMENT_ID_SEQ;
import static jooq.generated.tables.Department.DEPARTMENT;
import jooq.generated.tables.pojos.Department;
import jooq.generated.tables.records.DepartmentRecord;
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

        // workaround to avoid defining a converter
        /*
        ctx.insertInto(DEPARTMENT)
                .set(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT_DEPARTMENT_ID_SEQ.nextval())
                .set(DEPARTMENT.NAME, "HR")
                .set(DEPARTMENT.PHONE, "-int 8799")
                .set(DEPARTMENT.CODE, (short) 2231)
                .set(DEPARTMENT.OFFICE_CODE, "4")
                .set(DEPARTMENT.TOPIC, new String[]{"hiring", "interiew"})                
                //.set(DEPARTMENT.DEP_NET_IPV4, InetAddress.getByName("128.10.124.12"))
                .set(DEPARTMENT.DEP_NET_IPV4, field("?::inet", String.class, "128.10.124.12"))
                .execute();
        */
        
        // use the InetConverter
        ctx.insertInto(DEPARTMENT)
                .values(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval(),
                        "HR", "-int 8799", (short) 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"))
                .execute();
        
        // use the InetConverter and Department POJO
        Integer nextId1 = ctx.select(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval()).fetchOneInto(Integer.class);
        Department department = new Department(nextId1,
                        "HR", "-int 8799", (short) 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"));
        ctx.newRecord(DEPARTMENT, department).insert();        
        
        // use the InetConverter and DepartmentRecord
        Integer nextId2 = ctx.select(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval()).fetchOneInto(Integer.class);
        DepartmentRecord departmentr = new DepartmentRecord(nextId2,
                        "HR", "-int 8799", (short) 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"));
        ctx.insertInto(DEPARTMENT).values(departmentr.valuesRow().fields()).execute();
    }

    public void fetchDepInet() {

        // workaround to avoid defining a converter
        /*
        List<String> inets = ctx.select(DEPARTMENT.DEP_NET_IPV4)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(DEPARTMENT.DEP_NET_IPV4, String.class);
        */
        
        // use the InetConverter
        List<InetAddress> inets = ctx.select(DEPARTMENT.DEP_NET_IPV4)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(DEPARTMENT.DEP_NET_IPV4);

        System.out.println("Inet addrs: " + inets);
    }
}
