package be.looorent.jflu.subscriber.rabbitmq

import be.looorent.jflu.Event
import be.looorent.jflu.subscriber.ConsumptionException
import be.looorent.jflu.subscriber.rabbitmq.ConsumptionExceptionHandler
import be.looorent.jflu.subscriber.rabbitmq.RabbitMQExceptionHandler
import com.rabbitmq.client.Channel
import spock.lang.Specification

/**
 * @author Logan Clement {@literal <logan@commuty.net>}
 */
class RabbitMQExceptionHandlerTest extends Specification {

    def "loading an exception handler without a consumption exception handler"() {
        given: "an exception handler"
        RabbitMQExceptionHandler handler = new RabbitMQExceptionHandler(null)

        when: "an exception of type 'ConsumptionException' is raised"
        ConsumptionException exception = new ConsumptionException(new Event(null, null), new Exception())
        handler.handleConsumerException(Mock(Channel), exception, null, null, null)

        then: "an exception of type 'RuntimeException' is thrown"
        thrown RuntimeException
    }

    def "loading an exception handler with a reflective custom consumption exception handler"() {
        given: "an exception handler with a consumption exception class defined"
        RabbitMQExceptionHandler handler = new RabbitMQExceptionHandler(null) {
            @Override
            protected String readConsumptionExceptionHandlerClassName() {
                return "${DummyConsumptionExceptionHandler.class.name}"
            }
        }

        when: "an exception of type 'ConsumptionException' is raised"
        ConsumptionException exception = new ConsumptionException(new Event(null, null), new Exception())
        handler.handleConsumerException(Mock(Channel), exception, null, null, null)

        then: "an exception of type DummyException is thrown"
        thrown DummyException
    }


    def "loading an exception handler with a manual consumption exception handler"() {
        given: "an exception handler with a consumption exception class defined"
        RabbitMQExceptionHandler handler = new RabbitMQExceptionHandler(new DummyConsumptionExceptionHandler())

        when: "an exception of type 'ConsumptionException' is raised"
        ConsumptionException exception = new ConsumptionException(new Event(null, null), new Exception())
        handler.handleConsumerException(Mock(Channel), exception, null, null, null)

        then: "an exception of type DummyException is thrown"
        thrown DummyException
    }


    static class DummyException extends Exception {}

    static class DummyConsumptionExceptionHandler implements ConsumptionExceptionHandler {
        @Override
        void handle(ConsumptionException exception) {
            throw new DummyException()
        }
    }
}