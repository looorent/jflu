package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.RabbitMQConfiguration;
import be.looorent.jflu.subscriber.RabbitMQListener;
import be.looorent.jflu.subscriber.RabbitMQSubscriptionRepository;
import be.looorent.jflu.subscriber.SubscriptionScanner;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import java.sql.*;
import java.util.Properties;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.*;
import static be.looorent.jflu.subscriber.RabbitMQPropertyName.readPropertiesFromEnvironment;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventStore {

    public static void main(String... args) throws SQLException, LiquibaseException {
        migrateDatabase();
        listenToQueue();
    }

    private static void migrateDatabase() throws SQLException, LiquibaseException {
        try (Connection connection = createDatabaseConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection)
            );
            Liquibase liquibase = new Liquibase("/db/changelog.xml", new FileSystemResourceAccessor(), database);
            liquibase.update("");
        }
    }

    private static void listenToQueue() {
        RabbitMQConfiguration configuration = new RabbitMQConfiguration(readPropertiesFromEnvironment());
        RabbitMQListener listener = new RabbitMQListener(configuration);
        RabbitMQSubscriptionRepository repository = new RabbitMQSubscriptionRepository(configuration);
        listener.start("be.looorent.jflu.store", new SubscriptionScanner(), repository);
    }
}