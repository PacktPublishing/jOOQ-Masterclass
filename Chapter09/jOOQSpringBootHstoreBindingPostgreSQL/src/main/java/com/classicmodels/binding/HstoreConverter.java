package com.classicmodels.binding;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import org.jooq.Converter;

public class HstoreConverter implements Converter<Object, Map<String, String>> {

    @Override
    public Map<String, String> from(Object t) {

        if (t != null) {

            
        }
        return null;
    }

    @Override
    public Object to(Map<String, String> u) {

        return null;
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<InetAddress> toType() {
        return InetAddress.class;
    }

}
