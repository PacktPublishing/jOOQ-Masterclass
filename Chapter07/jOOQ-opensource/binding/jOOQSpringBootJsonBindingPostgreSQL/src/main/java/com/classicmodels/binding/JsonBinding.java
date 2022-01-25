package com.classicmodels.binding;

import com.fasterxml.jackson.databind.JsonNode;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

    // Rendering a bind variable for the binding context's value and casting it to the JSON type
    @Override
    public void sql(BindingSQLContext<JsonNode> ctx) throws SQLException {

        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::json");
        } else {
            ctx.render().sql("?::json");
        }
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<JsonNode> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    // Converting the JsonNode to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<JsonNode> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsonNode
    @Override
    public void get(BindingGetResultSetContext<JsonNode> ctx) throws SQLException {
        ctx.convert(converter()).value(JSON.valueOf(ctx.resultSet().getString(ctx.index())));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to JsonNode
    @Override
    public void get(BindingGetStatementContext<JsonNode> ctx) throws SQLException {
        ctx.convert(converter()).value(JSON.valueOf(ctx.statement().getString(ctx.index())));
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<JsonNode> bgsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<JsonNode> bsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }
}