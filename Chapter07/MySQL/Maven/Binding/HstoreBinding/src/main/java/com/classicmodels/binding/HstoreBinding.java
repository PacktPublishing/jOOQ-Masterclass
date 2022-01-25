package com.classicmodels.binding;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

    // Rendering a bind variable for the binding context's value and casting it to the HSTORE type
    @Override    
    public void sql(BindingSQLContext<Map<String, String>> ctx) throws SQLException {
       
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::hstore");
        } else {
            ctx.render().sql("?::hstore");
        }
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<Map<String, String>> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    // Converting the Map<String, String> to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<Map<String, String>> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }    

    // Getting a String value from a JDBC ResultSet and converting that to a Map<String, String>
    @Override
    public void get(BindingGetResultSetContext<Map<String, String>> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to Map<String, String>
    @Override
    public void get(BindingGetStatementContext<Map<String, String>> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<Map<String, String>> bgsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet."); 
    }
    
    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<Map<String, String>> bsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet."); 
    }
}