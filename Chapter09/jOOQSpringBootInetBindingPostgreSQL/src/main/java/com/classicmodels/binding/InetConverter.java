package com.classicmodels.binding;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.jooq.Converter;

public class InetConverter implements Converter<Object, InetAddress> {

    @Override
    public InetAddress from(Object t) {

        if (t != null) {

            try {
                return InetAddress.getByName(String.valueOf(t));
            } catch (UnknownHostException ex) {
                throw new RuntimeException("Invalid inet addr", ex);
            }

        }
        return null;
    }

    @Override
    public Object to(InetAddress u) {

        if (u != null) {
            return u.getHostAddress();
        }

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
