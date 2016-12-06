package be.looorent.jflu;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_LONG_FOR_INTS;
import static java.lang.System.getenv;

/**
 * Overall Jflu configuration.
 * If no 'JFLU_EMITTER' environment variable is set, '[not emitter set]' will be used as the default emitter.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class Configuration {

    private static final String EMITTER_VARIABLE = "JFLU_EMITTER";
    private static final String DEFAULT_EMITTER = "[no emitter set]";
    private static Configuration instance;

    private String emitter;

    public Configuration() {
        emitter = getenv(EMITTER_VARIABLE);
        if (emitter == null || emitter.isEmpty()) {
            emitter = DEFAULT_EMITTER;
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getEmitter() {
        return emitter;
    }

    /**
     * @return a default service that (un)marshall object from/to JSON.
     */
    public ObjectMapper getDefaultJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(FIELD, ANY);
        mapper.setVisibility(CREATOR, ANY);
        mapper.setVisibility(GETTER, NONE);
        mapper.configure(USE_LONG_FOR_INTS, true);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
