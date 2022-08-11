package be.looorent.jflu.store;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.subscriber.EventConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.createDatabaseConnection;
import static java.time.LocalDateTime.now;

/**
 * Uses native SQL queries to store {@link Event} into a PostgreSQL table.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class EventStoreConsumer implements EventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(EventStoreConsumer.class);
    private final Connection connection;
    private final ObjectMapper jsonMapper;

    public EventStoreConsumer() throws SQLException {
        connection = createDatabaseConnection();
        jsonMapper = Configuration.getInstance().getDefaultJsonMapper();
    }

    public void store(Event event) {
        LOG.debug("Storing event with id : {}", event.getId());
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events (uuid, emitter, name, kind, timestamp, stored_at, data) VALUES (?, ?, ?, ?, ?, ?, ?);");
            statement.setObject(1, createUniqueId(event));
            statement.setString(2, event.getEmitter());
            statement.setString(3, event.getName());
            statement.setString(4, event.getKind().name().toLowerCase());
            statement.setTimestamp(5, Timestamp.valueOf(event.getTimestamp()));
            statement.setTimestamp(6, Timestamp.valueOf(now()));
            statement.setObject(7, createData(event));
            statement.execute();
            LOG.debug("Event stored.");
        } catch (SQLException | JsonProcessingException e) {
            LOG.error("An error occurred when storing event with id : {}", event.getId(), e);
            throw new RuntimeException(e);
        }
    }

    private PGobject createData(Event event) throws SQLException, JsonProcessingException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(jsonMapper.writeValueAsString(event.getData()));
        return jsonObject;
    }

    private PGobject createUniqueId(Event event) throws SQLException {
        PGobject id = new PGobject();
        id.setType("uuid");
        id.setValue(event.getId().toString());
        return id;
    }
}
