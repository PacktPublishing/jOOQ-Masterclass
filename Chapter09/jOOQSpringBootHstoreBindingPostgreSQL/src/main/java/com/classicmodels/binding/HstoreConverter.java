package com.classicmodels.binding;

import java.util.Map;
import org.jooq.Converter;
import org.postgresql.util.HStoreConverter;

public class HstoreConverter implements Converter<Object, Map<String, String>> {

    @Override
    public Map<String, String> from(Object t) {

        if (t != null) {

            return HStoreConverter.fromString(t.toString());
        }

        return null;
    }

    @Override
    public Object to(Map<String, String> u) {

        if (u != null) {

            return HStoreConverter.toString(u);
        }

        return null;
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Map<String, String>> toType() {
        return (Class<Map<String, String>>) (Class) Map.class;
    }

}
