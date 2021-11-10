package com.classicmodels.provider;

import com.classicmodels.pojo.ManagerEvaluation;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;

public class MyRecordMapperProvider implements RecordMapperProvider {

    @Override
    public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {

        if(type == ManagerEvaluation.class) {
            return (R record) -> {
                String[] e = record.getValue(MANAGER.MANAGER_EVALUATION).split(",");
                
                return (E) new ManagerEvaluation(Integer.valueOf(e[0].trim()), Integer.valueOf(e[1].trim()),
                        Integer.valueOf(e[2].trim()),Integer.valueOf(e[3].trim()));
            };
        }
        
        // Fall back to jOOQ's DefaultRecordMapper
        return new DefaultRecordMapper(recordType, type);
    }
}
