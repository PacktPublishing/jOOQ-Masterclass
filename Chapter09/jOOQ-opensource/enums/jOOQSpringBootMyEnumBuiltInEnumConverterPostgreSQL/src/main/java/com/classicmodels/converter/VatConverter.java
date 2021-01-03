package com.classicmodels.converter;

import com.classicmodels.enums.VatType;
import org.jooq.DataType;
import org.jooq.impl.EnumConverter;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class VatConverter extends EnumConverter<Object, VatType> {

    public final static VatConverter VAT_CONVERTER
            = new VatConverter();

    public final static DataType<VatType> VATTYPE
            = VARCHAR.asConvertedDataType(VAT_CONVERTER);

    public VatConverter() {
        super(Object.class, VatType.class);
    }
}
