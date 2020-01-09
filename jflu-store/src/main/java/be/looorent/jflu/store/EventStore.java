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

import java.sql.Connection;
import java.sql.SQLException;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.createDatabaseConnection;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EventStore {

    private static final Logger LOG = LoggerFactory.getLogger(EventStore.class);
    private static final String CHANGELOG_LOCATION = "db/changelog.xml";

    public static void main(String... args) throws SQLException, LiquibaseException, BrokerException {
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

    private static final void listenToQueue() throws BrokerException, SQLException {
        LOG.info("Starting listener...");
        BrokerSubscriptionConfiguration configuration = new BrokerSubscriptionEnvironmentConfigurationProvider().createSubscriptionConfiguration();

        SubscriptionRepository repository = registerRepository();
        configuration.getQueueListener().listen(repository);
        LOG.info("Starting listener: Done.");
    }

    private static SubscriptionRepository registerRepository() throws SQLException {
        SubscriptionRepository repository = new SubscriptionRepository();
        repository.register(createOverallSubscription());
        return repository;
    }

    private static Subscription createOverallSubscription() throws SQLException {
        EventStoreConsumer consumer = new EventStoreConsumer();
        return new Subscription(new SubscriptionQuery("", EventMappingKind.ALL, "", EventMappingStatus.NEW), "EventStore", consumer::store);
    }
}