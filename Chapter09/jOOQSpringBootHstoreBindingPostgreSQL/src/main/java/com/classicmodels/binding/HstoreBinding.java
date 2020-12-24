package com.classicmodels.binding;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.conf.ParamType;
import static org.jooq.impl.DSL.inline;

public class HstoreBinding implements Binding<Object, Map<String, String>> {

    private final HstoreConverter converter = new HstoreConverter();

    @Override
    public final Converter<Object, Map<String, String>> converter() {
        return converter;
    }

    @Override
    public void sql(BindingSQLContext<Map<String, String>> ctx) throws SQLException {
       
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::hstore");
        } else {
            ctx.render().sql("?::hstore");
        }
    }

    @Override
    public void register(BindingRegisterContext<Map<String, String>> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<Map<String, String>> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }    

    @Override
    public void get(BindingGetResultSetContext<Map<String, String>> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Map<String, String>> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Map<String, String>> bgsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void set(BindingSetSQLOutputContext<Map<String, String>> bsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}