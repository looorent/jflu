package be.looorent.jflu.store;

import be.looorent.jflu.*;
import be.looorent.jflu.publisher.EventPublisher;
import be.looorent.jflu.publisher.PublishingException;
import be.looorent.jflu.publisher.RabbitMQEventTopicPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static be.looorent.jflu.EventStatus.REPLAYED;
import static be.looorent.jflu.publisher.RabbitMQPropertyName.readPropertiesFromEnvironment;
import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.createDatabaseConnection;
import static com.google.common.collect.Lists.partition;
import static java.lang.Long.parseLong;
import static java.time.LocalDateTime.ofInstant;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.stream.Collectors.joining;

/**
 * This service can be used to read all events from the database
 * and send them to the exchange with {@code status=replay}
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class ReplayService {

    private static final Logger LOG = LoggerFactory.getLogger(ReplayService.class);
    private static final int BATCH_SIZE = 5;

    private final ObjectMapper jsonMapper;
    private final long firstEventId;
    private final EventPublisher eventPublisher;


    public ReplayService(long firstEventId,
                         EventPublisher eventPublisher) {
        if (eventPublisher == null) {
            throw new IllegalArgumentException("eventPublisher must not be null");
        }
        this.eventPublisher = eventPublisher;
        this.firstEventId = firstEventId;
        jsonMapper = Configuration.getInstance().getDefaultJsonMapper();
    }

    public void replay() throws ReplayException {
        LOG.info("Database connection...");
        try (Connection connection = createDatabaseConnection()) {
            LOG.info("Database connection: done.");
            List<Long> ids = findOrderedEventIds(connection);

            long firstId = min(ids);
            long lastId = max(ids);
            LOG.info("Replaying {} events to replay with ids [{}, {}]", ids.size(), firstId, lastId);

            for (List<Long> batchOfIds : partition(ids, BATCH_SIZE)) {
                List<Event> events = readEventsFrom(connection, batchOfIds);
                for (Event event : events) {
                    publish(event);
                }
            }
        } catch (SQLException e) {
            LOG.error("An database operation failed", e);
            throw new ReplayException(e);
        } catch (PublishingException e) {
            LOG.error("An error occurred when publishing an event", e);
            throw new ReplayException(e);
        } catch (IOException e) {
            LOG.error("An event cannot be unmarshalled", e);
            throw new ReplayException(e);
        }
    }

    private void publish(Event event) throws PublishingException {
        LOG.trace("Publish event with id = {}", event.getId());
        eventPublisher.publish(event);
    }

    private List<Event> readEventsFrom(Connection connection, List<Long> storedIds) throws SQLException, IOException {
        String ids = storedIds.stream()
                .map(String::valueOf)
                .collect(joining(","));
        try (PreparedStatement query = connection.prepareStatement("SELECT uuid, name, emitter, timestamp, kind, data::text FROM events WHERE id IN ("+ids+") ORDER BY id ASC")) {
            List<Event> events = new ArrayList<>(storedIds.size());
            ResultSet result = query.getResultSet();
            while (result.next()) {
                events.add(createEventFrom(result));
            }
            return events;
        }
    }

    private Event createEventFrom(ResultSet result) throws SQLException, IOException {
        EventMetadata metadata = new EventMetadata(
                UUID.fromString(result.getString(1)),
                result.getString(2),
                result.getString(3),
                ofInstant(result.getDate(4).toInstant(), ZoneId.systemDefault()),
                EventKind.valueOf(result.getString(5)),
                REPLAYED
                );
        EventData data = jsonMapper.readValue(result.getString(6), EventData.class);
        return new Event(metadata, data);
    }

    private List<Long> findOrderedEventIds(Connection connection) throws SQLException {
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement query = connection.prepareStatement("SELECT id FROM events WHERE id >= ? ORDER BY id ASC")) {
            query.setLong(1, firstEventId);
            ResultSet result = query.getResultSet();
            while (result.next()) {
                ids.add(result.getLong(1));
            }
        }
        return ids;
    }

    public static final void main(String... args) throws ReplayException {
        long firstEventId = args.length > 0 && args[0] != null && !args[0].isEmpty() ? parseLong(args[0]) : 0;
        new ReplayService(firstEventId, new RabbitMQEventTopicPublisher(readPropertiesFromEnvironment())).replay();
    }
}
