package be.looorent.jflu.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class EventSerializer {

    static class IdDeserializer extends JsonDeserializer<Object> {

        private static final Logger LOG = LoggerFactory.getLogger(IdDeserializer.class);

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext ctxt) {
            try {
                return parser.readValueAs(Long.class);
            }
            catch (Exception e) {
                try {
                    return parser.readValueAs(String.class);
                }
                catch (Exception ex) {
                    LOG.error("Impossible to parse Id", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
