package be.looorent.jflu.publisher.rabbitmq.quarkus;

public final class ProducerBuildConfiguration$accessor {

    @SuppressWarnings("unchecked")
    public static boolean get_enabled(Object __instance) {
        return ((ProducerBuildConfiguration)__instance).enabled;
    }
    @SuppressWarnings("unchecked")
    public static void set_enabled(Object __instance, boolean enabled) {
        ((ProducerBuildConfiguration)__instance).enabled = enabled;
    }

    private ProducerBuildConfiguration$accessor() {}
    public static Object construct() {
        return new ProducerBuildConfiguration();
    }
}
