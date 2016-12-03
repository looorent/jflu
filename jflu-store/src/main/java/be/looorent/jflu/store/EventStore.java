package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.*;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.*;
import static be.looorent.jflu.subscriber.RabbitMQPropertyName.readPropertiesFromEnvironment;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventStore {

    private final static Logger LOG = LoggerFactory.getLogger(EventStore.class);

    public static void main(String... args) throws SQLException, LiquibaseException {
        migrateDatabase();
        listenToQueue();
    }

    private static void migrateDatabase() throws SQLException, LiquibaseException {
        LOG.info("Migrating database...");
        try (Connection connection = createDatabaseConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection)
            );
            Liquibase liquibase = new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        }
        LOG.info("Migrating database: Done.");
    }

    private static void listenToQueue() {
        LOG.info("Starting listener...");
        RabbitMQConfiguration configuration = new RabbitMQConfiguration(readPropertiesFromEnvironment());
        RabbitMQQueueListener queueListener = new RabbitMQQueueListener(configuration);
        RabbitMQSubscriptionRepository repository = new RabbitMQSubscriptionRepository(configuration);
        EventListener listener = new EventListener();
        listener.start("be.looorent.jflu.store", new SubscriptionScanner(), repository, queueListener);
        LOG.info("Starting listener: Done.");
    }
}