package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Converter;

public class JsonConverter implements Converter<String, JsonNode> {

    @Override
    public JsonNode from(String t) {

        if (t != null) {

            try {
                return new ObjectMapper().readTree(t);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Json corrupted", ex);
            }

        }

        return null;
    }

    @Override
    public String to(JsonNode u) {

        if (u != null) {

            return u.toString();
        }

        return null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
