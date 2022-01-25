package com.classicmodels.binding;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.jooq.Converter;
import org.jooq.DataType;
import static org.jooq.impl.SQLDataType.BLOB;

public class PointConverter implements Converter<Object, Point2D> {

    public static final Converter<Object, Point2D> POINT_CONVERTER
            = new PointConverter();

    public static final Converter<Object[], Point2D[]> POINT_ARR_CONVERTER
            = POINT_CONVERTER.forArrays();

    public static final DataType<Point2D> POINT
            = BLOB.asConvertedDataType(POINT_CONVERTER);

    @Override
    public Point2D from(Object t) {

        if (t != null) {

            try {
                Blob blob = (Blob) t;
                Geometry geom = getGeometryFromInputStream(blob.getBinaryStream());

                return new Point2D.Double(geom.getCoordinate().x, geom.getCoordinate().y);
            } catch (IOException | ParseException | SQLException ex) {
                throw new RuntimeException("Cannot determine the geometry", ex);
            }
        }

        return null;
    }

    @Override
    public Object to(Point2D u) {

        if (u != null) {

            return "POINT(" + u.getX() + " " + u.getY() + ")";
        }

        return null;
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Point2D> toType() {
        return Point2D.class;
    }

    // Read a geometry (POINT, POLYGON, MULTIPOINT, and so on) from an InputStream
    private Geometry getGeometryFromInputStream(InputStream inputStream)
            throws IOException, ParseException {

        Geometry dbGeometry = null;

        if (inputStream != null) {

            // convert the stream to a byte[] array
            // so it can be passed to the WKBReader
            byte[] buffer = new byte[255];

            int bytesRead = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] geometryAsBytes = baos.toByteArray();

            if (geometryAsBytes.length < 5) {
                throw new IOException("Invalid geometry inputStream - less than five bytes");
            }

            // first four bytes of the geometry are the SRID,
            // followed by the actual WKB.  Determine the SRID here
            byte[] sridBytes = new byte[4];
            System.arraycopy(geometryAsBytes, 0, sridBytes, 0, 4);
            boolean bigEndian = (geometryAsBytes[4] == 0x00);

            int srid = 0;
            if (bigEndian) {
                for (int i = 0; i < sridBytes.length; i++) {
                    srid = (srid << 8) + (sridBytes[i] & 0xff);
                }
            } else {
                for (int i = 0; i < sridBytes.length; i++) {
                    srid += (sridBytes[i] & 0xff) << (8 * i);
                }
            }

            // use the JTS WKBReader for WKB parsing
            WKBReader wkbReader = new WKBReader();

            // copy the byte array, removing the first four SRID bytes
            byte[] wkb = new byte[geometryAsBytes.length - 4];
            System.arraycopy(geometryAsBytes, 4, wkb, 0, wkb.length);
            dbGeometry = wkbReader.read(wkb);
            dbGeometry.setSRID(srid);
        }

        return dbGeometry;
    }
}
