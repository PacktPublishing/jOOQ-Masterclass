package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jooq.generated.embeddables.records.OfficeFullAddressRecord;
import org.jooq.Converter;

public class JsonConverter implements Converter<OfficeFullAddressRecord, JsonNode> {

    public static final JsonConverter JSON_CONVERTER = new JsonConverter();

    @Override
    public JsonNode from(OfficeFullAddressRecord t) {

        if (t != null) {

            try {
                return new ObjectMapper().readTree(
                        "{ \"city\":\"" + t.getCity() + "\", \"address\":\"" + t.getAddressLineFirst()
                        + "\", \"state\":\"" + t.getState() + "\", \"country\":\"" + t.getCountry()
                        + "\", \"territory\":\"" + t.getTerritory() + "\" }"
                );
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Json corrupted", ex);
            }
        }

        return null;
    }

    @Override
    public OfficeFullAddressRecord to(JsonNode u) {

        if (u != null) {

            return new OfficeFullAddressRecord(
                    u.path("city").textValue(),
                    u.path("address").textValue(),
                    u.path("state").textValue(),
                    u.path("country").textValue(),
                    u.path("territory").textValue()
            );
        }

        return null;
    }

    @Override
    public Class<OfficeFullAddressRecord> fromType() {
        return OfficeFullAddressRecord.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
