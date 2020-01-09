package be.looorent.jflu.publisher.rabbitmq.quarkus;

import static java.util.Optional.ofNullable;

public final class ProducerBuildConfiguration$$accessor {

    @SuppressWarnings("unchecked")
    public static boolean get_enabled(Object __instance) {
        return ((ProducerBuildConfiguration)__instance).enabled;
    }
    @SuppressWarnings("unchecked")
    public static void set_enabled(Object __instance, boolean enabled) {
        ((ProducerBuildConfiguration)__instance).enabled = enabled;
    }

    @SuppressWarnings("unchecked")
    public static Object get_waitForConnection(Object __instance) {
        return ((ProducerBuildConfiguration)__instance).waitForConnection;
    }
    @SuppressWarnings("unchecked")
    public static void set_waitForConnection(Object __instance, Object waitForConnection) {
        ((ProducerBuildConfiguration)__instance).waitForConnection = (Boolean) waitForConnection;
    }

    @SuppressWarnings("unchecked")
    public static Object get_emitter(Object __instance) {
        return ((ProducerBuildConfiguration)__instance).emitter;
    }
    @SuppressWarnings("unchecked")
    public static void set_emitter(Object __instance, Object emitter) {
        ((ProducerBuildConfiguration)__instance).emitter = (String) emitter;
    }

    private ProducerBuildConfiguration$$accessor() {}
    public static Object construct() {
        return new ProducerBuildConfiguration();
    }
}
