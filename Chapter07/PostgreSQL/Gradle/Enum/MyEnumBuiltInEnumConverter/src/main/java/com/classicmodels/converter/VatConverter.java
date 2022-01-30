package com.classicmodels.converter;

import org.jooq.DataType;
import org.jooq.impl.EnumConverter;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class VatConverter extends
        EnumConverter<jooq.generated.enums.VatType, com.classicmodels.enums.VatType> {

    public final static VatConverter VAT_CONVERTER
            = new VatConverter();

    public final static DataType<com.classicmodels.enums.VatType> VATTYPE
            = VARCHAR.asEnumDataType(jooq.generated.enums.VatType.class).asConvertedDataType(VAT_CONVERTER);

    public VatConverter() {
        super(jooq.generated.enums.VatType.class, com.classicmodels.enums.VatType.class);
    }
}