package be.looorent.jflu.entity;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityHelper {

    public static final boolean isAudited(Object entity) {
        return entity.getClass().isAnnotationPresent(Tracked.class);
    }
}
