package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jooq.Converter;

public class JsonConverter implements Converter<LocalDateTime, JsonNode> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public JsonNode from(LocalDateTime t) {

        if (t != null) {

            try {
                return new ObjectMapper().readTree("{\"Timestamp\":" + "\""
                        + t.format(DATE_TIME_FORMATTER) + "\"}");
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Json corrupted", ex);
            }
        }

        return null;
    }

    @Override
    public LocalDateTime to(JsonNode u) {

        if (u != null) {
            
            return LocalDateTime.parse(u.path("Timestamp").textValue(), DATE_TIME_FORMATTER);
        }

        return null;
    }

    @Override
    public Class<LocalDateTime> fromType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
