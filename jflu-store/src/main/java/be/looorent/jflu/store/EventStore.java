package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.RabbitMQConfiguration;
import be.looorent.jflu.subscriber.RabbitMQListener;
import be.looorent.jflu.subscriber.RabbitMQSubscriptionRepository;
import be.looorent.jflu.subscriber.SubscriptionScanner;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

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
        RabbitMQListener listener = new RabbitMQListener(configuration);
        RabbitMQSubscriptionRepository repository = new RabbitMQSubscriptionRepository(configuration);
        listener.start("be.looorent.jflu.store", new SubscriptionScanner(), repository);
        LOG.info("Starting listener: Done.");
    }
}