package be.looorent.jflu.subscriber;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles RabbitMQ Consumer Exceptions and allow the definition of a custom exception handler
 * for {@link ConsumptionException}s
 *
 * @author Logan Clement {@literal <logan@commuty.net>}
 */
public class RabbitMQExceptionHandler extends DefaultExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQExceptionHandler.class);
    private static final String CONSUMPTION_EXCEPTION_HANDLER_IMPLEMENTATION_PROPERTY = "CONSUMPTION_EXCEPTION_HANDLER_IMPLEMENTATION";

    private ConsumptionExceptionHandler consumptionExceptionHandler;

    RabbitMQExceptionHandler() {
        consumptionExceptionHandler = initConsumptionExceptionHandler();
    }

    @Override
    public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
        super.handleConsumerException(channel, exception, consumer, consumerTag, methodName);
        if (exception instanceof ConsumptionException) {
            handleConsumptionException((ConsumptionException) exception);
        } else if (exception instanceof RuntimeException) {
            throw (RuntimeException) exception;
        } else {
            throw new RuntimeException(exception);
        }
    }

    private void handleConsumptionException(ConsumptionException exception) {
        if (consumptionExceptionHandler != null) {
            consumptionExceptionHandler.handle(exception);
        } else {
            throw new RuntimeException(exception);
        }
    }

    private ConsumptionExceptionHandler initConsumptionExceptionHandler() {
        String consumptionExceptionHandlerClassName = readConsumptionExceptionHandlerClassName();
        if (consumptionExceptionHandlerClassName != null && !consumptionExceptionHandlerClassName.isEmpty()) {
            try {
                Class<?> handlerClass = Class.forName(consumptionExceptionHandlerClassName);
                return handlerClass.asSubclass(ConsumptionExceptionHandler.class).newInstance();
            } catch (ClassNotFoundException e) {
                LOG.error("{} property present but class {} not found in the classpath",
                        CONSUMPTION_EXCEPTION_HANDLER_IMPLEMENTATION_PROPERTY,
                        consumptionExceptionHandlerClassName);
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                LOG.error("class {} found but does not extends {}",
                        consumptionExceptionHandlerClassName,
                        ConsumptionExceptionHandler.class.getSimpleName());
                throw new RuntimeException(e);
            } catch (IllegalAccessException | InstantiationException e) {
                LOG.error("Unable to instantiate class {}",
                        consumptionExceptionHandlerClassName);
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    protected String readConsumptionExceptionHandlerClassName() {
        return System.getenv(CONSUMPTION_EXCEPTION_HANDLER_IMPLEMENTATION_PROPERTY);
    }
}
