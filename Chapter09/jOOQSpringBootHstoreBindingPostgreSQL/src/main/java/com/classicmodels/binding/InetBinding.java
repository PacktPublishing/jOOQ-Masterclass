package com.classicmodels.binding;

import java.net.InetAddress;
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
import org.jooq.conf.ParamType;
import static org.jooq.impl.DSL.inline;

public class InetBinding implements Binding<Object, InetAddress> {

    private final HstoreConverter converter = new HstoreConverter();

    @Override
    public final Converter<Object, InetAddress> converter() {
        return converter;
    }

    @Override
    public void sql(BindingSQLContext<InetAddress> ctx) throws SQLException {
       
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(inline(ctx.convert(converter()).value())).sql("::inet");
        } else {
            ctx.render().sql("?::inet");
        }
    }

    @Override
    public void register(BindingRegisterContext<InetAddress> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<InetAddress> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }    

    @Override
    public void get(BindingGetResultSetContext<InetAddress> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<InetAddress> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<InetAddress> bgsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void set(BindingSetSQLOutputContext<InetAddress> bsqlc) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}