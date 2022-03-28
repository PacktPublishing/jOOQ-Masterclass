package com.classicmodels.repository;

import com.classicmodels.pojo.ManagerEvaluation;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchManagerEvaluation() {

        List<ManagerEvaluation> result = ctx.configuration()
                .derive(new RecordMapperProvider() {

                    @Override
                    public <R extends org.jooq.Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {

                        if (type == ManagerEvaluation.class) {
                            return (R record) -> {
                                String[] e = record.getValue(MANAGER.MANAGER_EVALUATION).split(",");

                                return (E) new ManagerEvaluation(Integer.valueOf(e[0].trim()), Integer.valueOf(e[1].trim()),
                                        Integer.valueOf(e[2].trim()), Integer.valueOf(e[3].trim()));
                            };
                        }

                        // another record mapper
                        // if (type == SomeOtherType.class) { }
                        
                        // Fall back to jOOQ's DefaultRecordMapper
                        return new DefaultRecordMapper(recordType, type);
                    }
                })
                .dsl()
                .select(MANAGER.MANAGER_EVALUATION)
                .from(MANAGER)
                .fetchInto(ManagerEvaluation.class);

        System.out.println("Result:\n" + result);
    }
}
