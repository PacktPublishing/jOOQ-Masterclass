/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables.daos;


import java.util.List;

import javax.annotation.processing.Generated;

import jooq.generated.tables.OfficeHasManager;
import jooq.generated.tables.pojos.JooqOfficeHasManager;
import jooq.generated.tables.records.OfficeHasManagerRecord;

import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.14.4",
        "schema version:1.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class OfficeHasManagerRepository extends DAOImpl<OfficeHasManagerRecord, JooqOfficeHasManager, Record2<String, Long>> {

    /**
     * Create a new OfficeHasManagerRepository without any configuration
     */
    public OfficeHasManagerRepository() {
        super(OfficeHasManager.OFFICE_HAS_MANAGER, JooqOfficeHasManager.class);
    }

    /**
     * Create a new OfficeHasManagerRepository with an attached configuration
     */
    @Autowired
    public OfficeHasManagerRepository(Configuration configuration) {
        super(OfficeHasManager.OFFICE_HAS_MANAGER, JooqOfficeHasManager.class, configuration);
    }

    @Override
    public Record2<String, Long> getId(JooqOfficeHasManager object) {
        return compositeKeyRecord(object.getOfficesOfficeCode(), object.getManagersManagerId());
    }

    /**
     * Fetch records that have <code>OFFICES_OFFICE_CODE BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<JooqOfficeHasManager> fetchRangeOfOfficesOfficeCode(String lowerInclusive, String upperInclusive) {
        return fetchRange(OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>OFFICES_OFFICE_CODE IN (values)</code>
     */
    public List<JooqOfficeHasManager> fetchByOfficesOfficeCode(String... values) {
        return fetch(OfficeHasManager.OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE, values);
    }

    /**
     * Fetch records that have <code>MANAGERS_MANAGER_ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<JooqOfficeHasManager> fetchRangeOfManagersManagerId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>MANAGERS_MANAGER_ID IN (values)</code>
     */
    public List<JooqOfficeHasManager> fetchByManagersManagerId(Long... values) {
        return fetch(OfficeHasManager.OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID, values);
    }
}
