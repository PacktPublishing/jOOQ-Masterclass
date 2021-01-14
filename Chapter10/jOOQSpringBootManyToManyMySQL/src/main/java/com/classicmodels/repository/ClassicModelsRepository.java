package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // bidirectional many-to-many
    public List<SimpleManager> fetchManyToMany() {

        ResultSet rs = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("officeCode"), field("city"), field("state"))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .fetchResultSet();

        List<SimpleManager> result = Collections.emptyList();
        Map<Long, SimpleManager> tempSM = new HashMap<>();
        Map<String, SimpleOffice> tempSO = new HashMap<>();

        try {
            while (rs.next()) {
                long managerId = rs.getLong("manager_id");
                String officeCode = rs.getString("officeCode");

                SimpleManager manager = tempSM.putIfAbsent(managerId,
                        new SimpleManager(managerId, rs.getString("manager_name")));
                
                tempSO.putIfAbsent(officeCode, new SimpleOffice(
                        rs.getString("officeCode"), rs.getString("state"), rs.getString("city")));               

                if (manager != null) {
                    manager.getOffices().add(tempSO.get(officeCode));
                    tempSO.get(officeCode).getManagers().add(manager);
                } else {
                    SimpleManager managerFromMap = tempSM.get(managerId);
                    managerFromMap.getOffices().add(tempSO.get(officeCode));
                    tempSO.get(officeCode).getManagers().add(managerFromMap);
                }
            }

            result = new ArrayList<>(tempSM.values());

        } catch (SQLException ex) {
            System.out.println("ex="+ex);
        }
                 
        // trivial display 
        for (SimpleManager sm : result) {

            System.out.println("\nManager:");
            System.out.println("===========================");
            System.out.println(sm);
            System.out.println(sm.getOffices());

            for (SimpleOffice so : sm.getOffices()) {

                System.out.println("\nOffice:");
                System.out.println("---------------------------");
                System.out.println(so);
                System.out.println(so.getManagers());
            }
        }
        
        return result;
    }
}
