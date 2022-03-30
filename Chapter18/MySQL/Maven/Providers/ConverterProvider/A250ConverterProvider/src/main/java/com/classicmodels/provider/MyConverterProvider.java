package com.classicmodels.provider;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.jooq.Converter;
import org.jooq.ConverterProvider;
import org.jooq.JSON;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DefaultConverterProvider;

public class MyConverterProvider implements ConverterProvider {

    final ConverterProvider delegate = new DefaultConverterProvider();

    @Override
    public <T, U> Converter<T, U> provide(Class<T> tType, Class<U> uType) {

        if (tType == JSON.class) {
            return Converter.ofNullable(tType, uType,
                    t -> {
                        try {
                            return new JSONDeserializer<U>().deserialize(((JSON) t).data());
                        } catch (Exception e) {
                            throw new DataTypeException("JSON mapping error", e);
                        }
                    },
                    u -> {
                        try {
                            return (T) JSON.valueOf(new JSONSerializer().serialize(u));
                        } catch (Exception e) {
                            throw new DataTypeException("JSON mapping error", e);
                        }
                    }
            );
            
        } // Delegate all other type pairs to jOOQ's default
        else {
            return delegate.provide(tType, uType);
        }
    }
}
