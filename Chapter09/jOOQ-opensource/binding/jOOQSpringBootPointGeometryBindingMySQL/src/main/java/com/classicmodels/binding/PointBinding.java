package com.classicmodels.binding;

import java.awt.geom.Point2D;
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

public class PointBinding implements Binding<Object, Point2D> {

    private final PointConverter converter = new PointConverter();

    @Override
    public final Converter<Object, Point2D> converter() {
        return converter;
    }

    // Rendering a bind variable for the binding context's value and casting it to the POINT type
    @Override
    public void sql(BindingSQLContext<Point2D> ctx) throws SQLException {

        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().sql("ST_PointFromText(")
                    .visit(inline(ctx.convert(converter()).value())).sql(")");
        } else {
            ctx.render().sql("ST_PointFromText(?)");
        }
    }

    // Registering BLOB types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<Point2D> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.BLOB);
    }

    // Converting the Point2D to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<Point2D> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    // Getting a Blob value from a JDBC ResultSet and converting that to a Point2D
    @Override
    public void get(BindingGetResultSetContext<Point2D> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getBlob(ctx.index())); // or, getBinaryStream()
    }

    // Getting a Blob value from a JDBC CallableStatement and converting that to Point2D
    @Override
    public void get(BindingGetStatementContext<Point2D> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getBlob(ctx.index()));
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<Point2D> bgsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<Point2D> bsqlc) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }
}