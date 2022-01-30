package com.classicmodels.repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
        // workaround to avoid defining a binding/converter
        // to test this, you need to disable the current binding
        InetAddress addr = InetAddress.getByName("128.10.124.12");
        
        // non type-safe (don't do this!)
        ctx.insertInto(DEPARTMENT)
                .values(100, "HR", "-int 8799",
                        2231, "4", new String[]{"hiring", "interiew"},
                        //InetAddress.getByName("128.10.124.12")) - this will not work                        
                        field("?::inet", String.class, addr.getHostName()),
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0) 
                .onDuplicateKeyIgnore()
                .execute();
        
        // type-safe (don't do this!)
        ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, DEPARTMENT.PHONE,
                DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE, DEPARTMENT.TOPIC, DEPARTMENT.DEP_NET_IPV4)
                .values("HR", "-int 8799",
                         2231, "4", new String[]{"hiring", "interiew"},
                        //InetAddress.getByName("128.10.124.12")) - this will not work                        
                        field("?::inet", String.class, addr.getHostName())) // to test this, you need to disable the converter
                .onDuplicateKeyIgnore()
                .execute();        
        */
        
        // non type-safe, use the InetConverter
        ctx.insertInto(DEPARTMENT)
                .values(100, "HR", "-int 8799", 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"),
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe, use the InetConverter
        ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, DEPARTMENT.PHONE,
                DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE, DEPARTMENT.TOPIC, DEPARTMENT.DEP_NET_IPV4)
                .values("HR", "-int 8799", 2231, "4", new String[]{"hiring", "interiew"},
                        InetAddress.getByName("128.10.124.12"))
                .onDuplicateKeyIgnore()
                .execute();

        // use the InetConverter and Department POJO                 
        Department department = new Department(null,
                "HR", "-int 8799", 
                ThreadLocalRandom.current().nextInt(10000, 20000), // random code
                "4", new String[]{"hiring", "interiew"},
                InetAddress.getByName("128.10.124.12"),
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        ctx.newRecord(DEPARTMENT, department).insert();        

        // use the InetConverter and DepartmentRecord
        DepartmentRecord departmentr = new DepartmentRecord(
                ThreadLocalRandom.current().nextInt(10000, 20000), // random PK
                "HR", "-int 8799", 
                ThreadLocalRandom.current().nextInt(10000, 20000), // random code
                "4", new String[]{"hiring", "interiew"},
                InetAddress.getByName("128.10.124.12"),
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        ctx.insertInto(DEPARTMENT).values(departmentr.valuesRow().fields()).execute();
    }

    public void fetchDepInet() {

        /*
        // workaround to avoid defining a binding/converter
        // to test this, you need to disable the binding
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
