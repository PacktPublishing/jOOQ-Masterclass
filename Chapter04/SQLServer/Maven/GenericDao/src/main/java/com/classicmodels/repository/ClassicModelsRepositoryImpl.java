package com.classicmodels.repository;

import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Repository;

@Repository
public abstract class ClassicModelsRepositoryImpl<R extends UpdatableRecord<R>, P, T>
        implements ClassicModelsRepository<R, P, T> {

    private final DSLContext ctx;
    private final Table<R> table;
    private final Class<P> type;
    private final RecordMapper<R, P> mapper;

    protected ClassicModelsRepositoryImpl(Table<R> table, Class<P> type, DSLContext ctx) {
        this.table = table;
        this.type = type;
        this.ctx = ctx;
        this.mapper = ctx.configuration().recordMapperProvider()
                .provide(table.recordType(), this.type);
    }

    @Override
    public List<P> fetchAll() {
        
        return ctx.selectFrom(table).fetch(mapper);
    }

    @Override
    public void deleteById(T id) {
        
        Field<?>[] pk = pk();

        if (pk != null) {
            ctx.delete(table)
                    .where(equal(pk, id))                    
                    .execute();
        }
    }

    private Field<?>[] pk() {
        UniqueKey<?> key = table.getPrimaryKey();
        return key == null ? null : key.getFieldsArray();
    }

    @SuppressWarnings("unchecked")
    private Condition equal(Field<?>[] pk, T id) {
        if (pk.length == 1) {
            return ((Field<Object>) pk[0])
                    .equal(pk[0].getDataType().convert(id));
        } else {
            return row(pk).equal((Record) id);
        }
    }
}
