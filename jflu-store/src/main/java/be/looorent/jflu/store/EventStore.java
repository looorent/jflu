package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.BrokerSubscriptionConfiguration;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import static be.looorent.jflu.store.EventStoreDatabaseConfiguration.createDatabaseConnection;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventStore {

    private static final Logger LOG = LoggerFactory.getLogger(EventStore.class);
    private static final String BROKER_IMPLEMENTATION_PROPERTY = "BROKER_SUBSCRIPTION_IMPLEMENTATION";
    private static final String BROKER_FACTORY_METHOD_NAME = "createFromSystemProperties";
    private static final String PROJECTOR_ROOT_PACKAGE = "be.looorent.jflu.store";

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
            new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database).update("");
        }
        LOG.info("Migrating database: Done.");
    }

    private static void listenToQueue() {
        LOG.info("Starting listener...");
        BrokerSubscriptionConfiguration configuration = createSubscriptionConfiguration();
        new EventListener().start(PROJECTOR_ROOT_PACKAGE,
                new SubscriptionScanner(),
                configuration.getSubscriptionRepository(),
                configuration.getQueueListener());
        LOG.info("Starting listener: Done.");
    }

    private static BrokerSubscriptionConfiguration createSubscriptionConfiguration() {
        String configurationClassName = System.getenv(BROKER_IMPLEMENTATION_PROPERTY);
        try {
            LOG.debug("Instanciating Broker configuration based on environment property: {}={}", BROKER_IMPLEMENTATION_PROPERTY, configurationClassName);
            Class<? extends BrokerSubscriptionConfiguration> configurationType = (Class<? extends BrokerSubscriptionConfiguration>) Class.forName(configurationClassName);
            Method method = configurationType.getMethod(BROKER_FACTORY_METHOD_NAME, String.class);
            return (BrokerSubscriptionConfiguration) method.invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            LOG.error("Class does not exist to create an instance of BrokerSubcriptionConfiguration: {}. Did you define a static method called {} ? Is this class available to the classpath?",
                    configurationClassName,
                    BROKER_FACTORY_METHOD_NAME,
                    e);
            throw new RuntimeException(e);
        }
    }
}