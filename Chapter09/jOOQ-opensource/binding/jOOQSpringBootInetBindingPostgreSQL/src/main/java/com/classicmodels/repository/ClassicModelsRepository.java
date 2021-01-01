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

        /*
        // workaround to avoid defining a converter (but, don't do this!)
        InetAddress addr = InetAddress.getByName("128.10.124.12");
        
        // non type-safe (don't do this!)
        ctx.insertInto(DEPARTMENT)
                .values(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval(), "HR", "-int 8799",
                        (short) 2231, "4", new String[]{"hiring", "interiew"},
                        //InetAddress.getByName("128.10.124.12")) - this will not work                        
                        field("?::inet", String.class, addr.getHostName())) // to test this, you need to disable the converter
                .execute();
        
        // type-safe (don't do this!)
        ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, DEPARTMENT.PHONE,
                DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE, DEPARTMENT.TOPIC, DEPARTMENT.DEP_NET_IPV4)
                .values("HR", "-int 8799",
                        (short) 2231, "4", new String[]{"hiring", "interiew"},
                        //InetAddress.getByName("128.10.124.12")) - this will not work                        
                        field("?::inet", String.class, addr.getHostName())) // to test this, you need to disable the converter
                .execute();
         */
        
        // non type-safe, use the InetConverter
        ctx.insertInto(DEPARTMENT)
                .values(DEPARTMENT_DEPARTMENT_ID_SEQ.nextval(),
                        "HR", "-int 8799", (short) 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"))
                .execute();

        // type-safe, use the InetConverter
        ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, DEPARTMENT.PHONE,
                DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE, DEPARTMENT.TOPIC, DEPARTMENT.DEP_NET_IPV4)
                .values("HR", "-int 8799", (short) 2231, "4", new String[]{"hiring", "interiew"},
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

        /*
        // workaround to avoid defining a converter (but, don't do this!)
        // to test this, you need to disable the converter
        List<InetAddress> inets = ctx.select(DEPARTMENT.DEP_NET_IPV4)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(rs -> {
                    try {
                        return InetAddress.getByName(String.valueOf(rs.getValue(DEPARTMENT.DEP_NET_IPV4)));
                    } catch (UnknownHostException ex) {
                        // handle exception
                    }
                    return null;
                });
        
        System.out.println("Inets: " + inets);
         */
        
        // use the InetConverter
        List<InetAddress> inets = ctx.select(DEPARTMENT.DEP_NET_IPV4)
                .from(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("HR"))
                .fetch(DEPARTMENT.DEP_NET_IPV4);

        System.out.println("Inet addrs: " + inets);
    }
}
