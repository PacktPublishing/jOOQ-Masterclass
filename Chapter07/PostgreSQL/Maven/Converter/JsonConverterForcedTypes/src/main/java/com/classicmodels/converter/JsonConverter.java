package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Converter;
import org.jooq.JSON;

public class JsonConverter implements Converter<JSON, JsonNode> {

    @Override
    public JsonNode from(JSON t) {

        if (t != null) {

            try {
                return new ObjectMapper().readTree(t.data());
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Json corrupted", ex);
            }

        }

        return null;
    }

    @Override
    public JSON to(JsonNode u) {

        if (u != null) {

            return JSON.valueOf(u.toString());
        }

        return null;
    }

    @Override
    public Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
