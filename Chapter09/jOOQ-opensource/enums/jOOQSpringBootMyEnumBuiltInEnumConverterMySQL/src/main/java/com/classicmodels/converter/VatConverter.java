package com.classicmodels.converter;

import com.classicmodels.enums.VatType;
import org.jooq.DataType;
import org.jooq.impl.EnumConverter;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class VatConverter extends EnumConverter<String, VatType> {

    public final static VatConverter VAT_CONVERTER
            = new VatConverter();

    public final static DataType<VatType> VATTYPE
            = VARCHAR.asConvertedDataType(VAT_CONVERTER);

    public VatConverter() {
        super(String.class, VatType.class);
    }
}
