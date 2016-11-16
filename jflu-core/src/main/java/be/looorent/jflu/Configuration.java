package be.looorent.jflu;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class Configuration {

    private static final String EMITTER = "flu.emitter";
    private static Configuration instance;

    private String emitter;

    public Configuration() {
        emitter = System.getProperty(EMITTER);
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getEmitter() {
        return emitter;
    }
}
