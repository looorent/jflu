package be.looorent.jflu.publisher;

public final class ProducerBuildConfiguration$$accessor {


    @SuppressWarnings("unchecked")
    public static boolean get_waitForConnection(Object __instance) {
        return ((ProducerBuildConfiguration)__instance).waitForConnection;
    }
    @SuppressWarnings("unchecked")
    public static void set_waitForConnection(Object __instance, boolean waitForConnection) {
        ((ProducerBuildConfiguration)__instance).waitForConnection = waitForConnection;
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
