package be.looorent.jflu.publisher.rabbitmq.quarkus;

import java.util.OptionalInt;

public final class ProducerRuntimeConfiguration$$accessor {
    @SuppressWarnings("unchecked")
    public static Object get_username(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).username;
    }
    @SuppressWarnings("unchecked")
    public static void set_username(Object __instance, Object username) {
        ((ProducerRuntimeConfiguration)__instance).username = (java.util.Optional<String>) username;
    }
    @SuppressWarnings("unchecked")
    public static Object get_password(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).password;
    }
    @SuppressWarnings("unchecked")
    public static void set_password(Object __instance, Object password) {
        ((ProducerRuntimeConfiguration)__instance).password = (java.util.Optional<String>) password;
    }
    @SuppressWarnings("unchecked")
    public static Object get_host(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).host;
    }
    @SuppressWarnings("unchecked")
    public static void set_host(Object __instance, Object host) {
        ((ProducerRuntimeConfiguration)__instance).host = (String) host;
    }
    @SuppressWarnings("unchecked")
    public static Object get_port(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).port;
    }
    @SuppressWarnings("unchecked")
    public static void set_port(Object __instance, Object port) {
        ((ProducerRuntimeConfiguration)__instance).port = (OptionalInt) port;
    }
    @SuppressWarnings("unchecked")
    public static void set_virtualHost(Object __instance, Object virtualHost) {
        ((ProducerRuntimeConfiguration)__instance).virtualHost = (java.util.Optional<String>) virtualHost;
    }
    @SuppressWarnings("unchecked")
    public static Object get_virtualHost(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).virtualHost;
    }
    @SuppressWarnings("unchecked")
    public static Object get_exchangeName(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).exchangeName;
    }
    @SuppressWarnings("unchecked")
    public static void set_exchangeName(Object __instance, Object exchangeName) {
        ((ProducerRuntimeConfiguration)__instance).exchangeName = (String) exchangeName;
    }
    @SuppressWarnings("unchecked")
    public static boolean get_exchangeDurable(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).exchangeDurable;
    }
    @SuppressWarnings("unchecked")
    public static void set_exchangeDurable(Object __instance, boolean exchangeDurable) {
        ((ProducerRuntimeConfiguration)__instance).exchangeDurable = exchangeDurable;
    }

    public static Object get_emitter(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).emitter;
    }

    public static void set_emitter(Object __instance, Object emitter) {
        ((ProducerRuntimeConfiguration)__instance).emitter = (String) emitter;
    }

    public static boolean get_waitForConnection(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).waitForConnection;
    }

    public static void set_waitForConnection(Object __instance, boolean waitForConnection) {
        ((ProducerRuntimeConfiguration)__instance).waitForConnection = waitForConnection;
    }

    public static boolean get_useSsl(Object __instance) {
        return ((ProducerRuntimeConfiguration)__instance).useSsl;
    }

    public static void set_useSsl(Object __instance, boolean useSsl) {
        ((ProducerRuntimeConfiguration)__instance).useSsl = useSsl;
    }

    private ProducerRuntimeConfiguration$$accessor() { }
    public static Object construct() {
        return new ProducerRuntimeConfiguration();
    }
}
