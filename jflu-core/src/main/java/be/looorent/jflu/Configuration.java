package be.looorent.jflu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.*;

/**
 * Overall Jflu configuration.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class Configuration {

    private static final String EMITTER_VARIABLE = "JFLU_EMITTER";
    private static Configuration instance;

    private String emitter;

    public Configuration() {
        emitter = System.getenv(EMITTER_VARIABLE);
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
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
