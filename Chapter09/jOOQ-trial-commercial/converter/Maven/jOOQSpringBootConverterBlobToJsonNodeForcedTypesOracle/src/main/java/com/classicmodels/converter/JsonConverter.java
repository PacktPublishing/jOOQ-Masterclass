package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Converter;
import org.jooq.JSONB;

public class JsonConverter implements Converter<JSONB, JsonNode> {

    @Override
    public JsonNode from(JSONB t) {

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
    public JSONB to(JsonNode u) {

        if (u != null) {

            return JSONB.valueOf(u.toString());
        }

        return null;
    }

    @Override
    public Class<JSONB> fromType() {
        return JSONB.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
