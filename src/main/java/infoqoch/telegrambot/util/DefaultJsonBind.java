package infoqoch.telegrambot.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import infoqoch.telegrambot.bot.entity.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class DefaultJsonBind implements JsonBind {
    private final ObjectMapper objectMapper;

    private DefaultJsonBind(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        simpleModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        simpleModule.addSerializer(Instant.class, new CustomEpochSerializer());
        simpleModule.addDeserializer(Instant.class, new CustomEpochDeserializer());
        objectMapper.registerModule(simpleModule);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 바인딩되지 않는 필드에 대해 무시하고 예외를 던지지 않는다.
        this.objectMapper = objectMapper;
    }

    public static DefaultJsonBind getInstance(){
        return new DefaultJsonBind();
    }

    @Override
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends Response> T toObject(String target, Class generic) {
        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(Response.class, generic);
            return objectMapper.readValue(target, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends Response<List<E>>, E> T toList(String target, Class<E> generic) {
        try {
            JavaType list = objectMapper.getTypeFactory().constructCollectionType(List.class, generic);
            JavaType type = objectMapper.getTypeFactory().constructParametricType(Response.class, list);
            final T result = objectMapper.readValue(target, type);
            if(result.emptyResult()){
                result.setResult(Collections.emptyList());
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return LocalDateTime.parse(jsonParser.getText(), formatter);
        }
    }
    private class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(formatter.format(value));
        }
    }
    private class CustomEpochDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
            return Instant.ofEpochSecond(Long.valueOf(jsonParser.getText()));
        }
    }
    private class CustomEpochSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.valueOf(value.getEpochSecond()));
        }
    }
}
