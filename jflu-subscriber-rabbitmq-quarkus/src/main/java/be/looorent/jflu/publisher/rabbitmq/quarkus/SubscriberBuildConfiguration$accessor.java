package be.looorent.jflu.publisher.rabbitmq.quarkus;

public class SubscriberBuildConfiguration$accessor {

    @SuppressWarnings("unchecked")
    public static boolean get_enabled(Object __instance) {
        return ((SubscriberBuildConfiguration)__instance).enabled;
    }
    @SuppressWarnings("unchecked")
    public static void set_enabled(Object __instance, boolean enabled) {
        ((SubscriberBuildConfiguration)__instance).enabled = enabled;
    }

    private SubscriberBuildConfiguration$accessor() {}
    public static Object construct() {
        return new SubscriberBuildConfiguration();
    }
}
