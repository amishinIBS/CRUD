package ru.ibs.crud.core.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ibs.crud.core.params.ComplexFilter;
import ru.ibs.crud.core.params.Filter;
import ru.ibs.crud.core.params.FilterItem;

import java.io.IOException;

public class ParamDeserializer extends JsonDeserializer<Filter> {
    @Override
    public Filter deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);

        String cmx = node.toString();
        if (isComplex(node)) {
            return mapper.readValue(cmx, ComplexFilter.class);
        } else {
            return mapper.readValue(cmx, FilterItem.class);
        }
    }

    private boolean isComplex(JsonNode node) {
        String operator = node.get("operator").asText();
        return operator.equalsIgnoreCase("AND") || operator.equalsIgnoreCase("OR");
    }
}
