package com.classicmodels.converter;

import org.jooq.Converter;
import org.jooq.JSON;

public class JsonConverter implements Converter<String, JSON> {

    @Override
    public JSON from(String t) {

        if (t != null) {

            return JSON.valueOf(t);
        }

        return null;
    }

    @Override
    public String to(JSON u) {

        if (u != null) {

            return u.data();
        }

        return null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<JSON> toType() {
        return JSON.class;
    }

}
