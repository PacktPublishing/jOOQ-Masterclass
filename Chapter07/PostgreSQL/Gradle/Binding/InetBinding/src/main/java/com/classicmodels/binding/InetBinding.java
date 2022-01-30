package com.classicmodels.binding;

import java.net.InetAddress;
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
import org.jooq.conf.ParamType;
import static org.jooq.impl.DSL.inline;

public class InetBinding implements Binding<Object, InetAddress> {

    private final InetConverter converter = new InetConverter();

    @Override
    public final Converter<Object, InetAddress> converter() {
        return converter;
    }

    // Rendering a bind variable for the binding context's value and casting it to the INET type
    @Override
    public void sql(BindingSQLContext<InetAddress> ctx) throws SQLException {
       
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::inet");
        } else {
            ctx.render().sql("?::inet");
        }
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<InetAddress> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    // Converting the InetAddress to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<InetAddress> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }    

    // Getting a String value from a JDBC ResultSet and converting that to a InetAddress
    @Override
    public void get(BindingGetResultSetContext<InetAddress> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to InetAddress
    @Override
    public void get(BindingGetStatementContext<InetAddress> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<InetAddress> bgsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet."); 
    }
    
    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<InetAddress> bsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet."); 
    }
}