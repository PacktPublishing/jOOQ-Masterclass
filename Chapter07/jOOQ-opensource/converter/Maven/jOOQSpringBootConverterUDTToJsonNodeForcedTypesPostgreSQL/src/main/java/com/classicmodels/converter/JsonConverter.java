package com.classicmodels.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jooq.generated.udt.records.EvaluationCriteriaRecord;
import org.jooq.Converter;

public class JsonConverter implements Converter<EvaluationCriteriaRecord, JsonNode> {

    @Override
    public JsonNode from(EvaluationCriteriaRecord t) {

        if (t != null) {

            try {
                return new ObjectMapper().readTree(
                        "{"
                        + "\"ca\":" + t.getCommunicationAbility() + ", "
                        + "\"et\":" + t.getEthics() + ", "
                        + "\"pr\":" + t.getPerformance() + ", "
                        + "\"ei\":" + t.getEmployeeInput()
                        + "}"
                );
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Json corrupted", ex);
            }
        }

        return null;
    }

    @Override
    public EvaluationCriteriaRecord to(JsonNode u) {

        if (u != null) {

            return new EvaluationCriteriaRecord(u.get("ca").asInt(),
                    u.get("et").asInt(), u.get("pr").asInt(), u.get("ei").asInt());
        }

        return null;
    }

    @Override
    public Class<EvaluationCriteriaRecord> fromType() {
        return EvaluationCriteriaRecord.class;
    }

    @Override
    public Class<JsonNode> toType() {
        return JsonNode.class;
    }

}
