package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.BrokerSubscriptionConfiguration;
import be.looorent.jflu.subscriber.BrokerSubscriptionEnvironmentConfigurationProvider;
import be.looorent.jflu.subscriber.EventListener;
import be.looorent.jflu.subscriber.SubscriptionScanner;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.createDatabaseConnection;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventStore {

    private static final Logger LOG = LoggerFactory.getLogger(EventStore.class);
    private static final String PROJECTOR_ROOT_PACKAGE = "be.looorent.jflu.store";
    private static final String CHANGELOG_LOCATION = "db/changelog.xml";

    public static void main(String... args) throws SQLException, LiquibaseException {
        migrateDatabase();
        listenToQueue();
    }

    private static final void migrateDatabase() throws SQLException, LiquibaseException {
        LOG.info("Migrating database...");
        try (Connection connection = createDatabaseConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection)
            );
            new Liquibase(CHANGELOG_LOCATION, new ClassLoaderResourceAccessor(), database).update("");
        }
        LOG.info("Migrating database: Done.");
    }

    private static final void listenToQueue() {
        LOG.info("Starting listener...");
        BrokerSubscriptionConfiguration configuration = new BrokerSubscriptionEnvironmentConfigurationProvider().createSubscriptionConfiguration();
        new EventListener().start(PROJECTOR_ROOT_PACKAGE,
                new SubscriptionScanner(),
                configuration.getSubscriptionRepository(),
                configuration.getQueueListener());
        LOG.info("Starting listener: Done.");
    }
}