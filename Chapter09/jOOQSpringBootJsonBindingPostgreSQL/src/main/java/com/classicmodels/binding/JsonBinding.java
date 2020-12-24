package com.classicmodels.binding;

import com.fasterxml.jackson.databind.JsonNode;
import java.sql.SQLException;
import java.sql.Types;
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
import org.jooq.JSON;
import org.jooq.conf.ParamType;
import static org.jooq.impl.DSL.inline;

public class JsonBinding implements Binding<JSON, JsonNode> {

    private final JsonConverter converter = new JsonConverter();

    @Override
    public final Converter<JSON, JsonNode> converter() {
        return converter;
    }

    @Override
    public void sql(BindingSQLContext<JsonNode> ctx) throws SQLException {
       
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::json");
        } else {
            ctx.render().sql("?::json");
        }
    }

    @Override
    public void register(BindingRegisterContext<JsonNode> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<JsonNode> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }    

    @Override
    public void get(BindingGetResultSetContext<JsonNode> ctx) throws SQLException {
        ctx.convert(converter()).value(JSON.valueOf(ctx.resultSet().getString(ctx.index())));
    }

    @Override
    public void get(BindingGetStatementContext<JsonNode> ctx) throws SQLException {
        ctx.convert(converter()).value(JSON.valueOf(ctx.statement().getString(ctx.index())));
    }

    @Override
    public void get(BindingGetSQLInputContext<JsonNode> bgsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void set(BindingSetSQLOutputContext<JsonNode> bsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}