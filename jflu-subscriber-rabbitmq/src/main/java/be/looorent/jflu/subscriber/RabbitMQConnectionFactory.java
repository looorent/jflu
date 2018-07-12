package be.looorent.jflu.subscriber;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static be.looorent.jflu.subscriber.RabbitMQPropertyName.*;
import static java.lang.Thread.sleep;
import static java.util.Optional.ofNullable;

/**
 * Attempt to connect RabbitMQ and wait if needed.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class RabbitMQConnectionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConnectionFactory.class);
    private static final int MAXIMUM_CONNECTION_ATTEMPTS = 30;
    private static final int CONNECTION_ATTEMPT_INTERVAL_IN_MS = 1000;
    private static final boolean DEFAULT_WAIT_FOR_CONNECTION = true;

    Connection connect(Properties properties) {
        boolean waitForConnection = ofNullable(WAIT_FOR_CONNECTION.readFrom(properties)).map(Boolean::parseBoolean).orElse(DEFAULT_WAIT_FOR_CONNECTION);
        ConnectionFactory factory = createFactory(properties);
        LOG.info("Waiting for a RabbitMQ connection? -> {}", waitForConnection);
        if (waitForConnection) {
            return waitForConnection(factory);
        } else {
            return connect(factory);
        }
    }

    private Connection connect(ConnectionFactory factory) {
        try {
            return factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection waitForConnection(ConnectionFactory factory) {
        Connection connection = null;
        int attempt = 0;
        while (connection == null) {
            attempt++;
            try {
                connection = connect(factory);
            } catch (RuntimeException e) {
                waitOrStop(attempt, e);
            }
        }
        return connection;
    }

    protected ConnectionFactory createFactory(Properties properties) {
        LOG.debug("Creating RabbitMQ connection factory with properties: {}", properties);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USERNAME.readFrom(properties));
        factory.setPassword(PASSWORD.readFrom(properties));
        factory.setVirtualHost(VIRTUAL_HOST.readFrom(properties));
        factory.setHost(HOST.readFrom(properties));
        factory.setPort(Integer.parseInt(PORT.readFrom(properties)));
        factory.setExceptionHandler(handleChannelExceptions());
        return factory;
    }

    private DefaultExceptionHandler handleChannelExceptions() {
        return new DefaultExceptionHandler() {
            @Override
            public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
                super.handleConsumerException(channel, exception, consumer, consumerTag, methodName);
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                } else {
                    throw new RuntimeException(exception);
                }
            }
        };
    }

    private void waitOrStop(int attempt, RuntimeException error) {
        if (attempt == MAXIMUM_CONNECTION_ATTEMPTS) {
            LOG.error("The maximum of attempts has been reached ({}). Stopping the consumer.", MAXIMUM_CONNECTION_ATTEMPTS);
            throw new RuntimeException(error);
        } else {
            wait(attempt);
        }
    }

    private void wait(int attempt) {
        try {
            LOG.error("RabbitMQ is not ready yet, waiting {} ms (attempt {})", CONNECTION_ATTEMPT_INTERVAL_IN_MS, attempt);
            sleep(CONNECTION_ATTEMPT_INTERVAL_IN_MS);
        } catch (InterruptedException e1) {
            LOG.error("Error when waiting for a connection", e1);
            throw new RuntimeException(e1);
        }
    }
}
