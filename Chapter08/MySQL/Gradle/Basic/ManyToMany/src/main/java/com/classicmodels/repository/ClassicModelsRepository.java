package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleBManager;
import com.classicmodels.pojo.SimpleBOffice;
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
import static org.jooq.impl.DSL.lateral;
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

    public List<SimpleBManager> fetchManyToManyUnidirectional() {

        ResultSet rs = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("officeCode"), field("city"), field("state"))
                .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))))
                .orderBy(MANAGER.MANAGER_ID)
                .fetchResultSet();

        List<SimpleBManager> result = Collections.emptyList();
        Map<Long, SimpleBManager> temp = new HashMap<>();

        try (rs) {
            while (rs.next()) {
                long managerId = rs.getLong("manager_id");

                temp.putIfAbsent(managerId,
                        new SimpleBManager(managerId, rs.getString("manager_name")));

                SimpleBOffice office = new SimpleBOffice(
                        rs.getString("officeCode"), rs.getString("state"), rs.getString("city"));

                temp.get(managerId).getOffices().add(office);
            }

            result = new ArrayList<>(temp.values());

        } catch (SQLException ex) {
            System.out.println("ex=" + ex);
        }

        // trivial display 
        System.out.println("===== Unidirectional =======");
        for (SimpleBManager sm : result) {

            System.out.println("\nManager:");
            System.out.println("===========================");
            System.out.println(sm);
            System.out.println(sm.getOffices());
        }

        return result;
    }

    public List<SimpleBManager> fetchManyToManyBidirectional() {

        ResultSet rs = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("officeCode"), field("city"), field("state"))
                .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))))
                .orderBy(MANAGER.MANAGER_ID)
                .fetchResultSet();

        List<SimpleBManager> result = Collections.emptyList();
        Map<Long, SimpleBManager> tempSM = new HashMap<>();
        Map<String, SimpleBOffice> tempSO = new HashMap<>();

        try (rs) {
            while (rs.next()) {
                long managerId = rs.getLong("manager_id");
                String officeCode = rs.getString("officeCode");

                SimpleBManager manager = tempSM.putIfAbsent(managerId,
                        new SimpleBManager(managerId, rs.getString("manager_name")));

                tempSO.putIfAbsent(officeCode, new SimpleBOffice(
                        rs.getString("officeCode"), rs.getString("state"), rs.getString("city")));

                if (manager != null) {
                    manager.getOffices().add(tempSO.get(officeCode));
                    tempSO.get(officeCode).getManagers().add(manager);
                } else {
                    SimpleBManager managerFromMap = tempSM.get(managerId);
                    managerFromMap.getOffices().add(tempSO.get(officeCode));
                    tempSO.get(officeCode).getManagers().add(managerFromMap);
                }
            }

            result = new ArrayList<>(tempSM.values());

        } catch (SQLException ex) {
            System.out.println("ex=" + ex);
        }

        // trivial display 
        System.out.println("\n\n===== Bidirectional =======");
        for (SimpleBManager sm : result) {

            System.out.println("\nManager:");
            System.out.println("===========================");
            System.out.println(sm);
            System.out.println(sm.getOffices());

            for (SimpleBOffice so : sm.getOffices()) {

                System.out.println("\nOffice:");
                System.out.println("---------------------------");
                System.out.println(so);
                System.out.println(so.getManagers());
            }
        }

        return result;
    }
}
