package be.looorent.jflu.entity;

import java.io.Serializable;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public interface Auditable {

    Serializable createUserMetadataOnCreate();
    Serializable createUserMetadataOnUpdate();
    Serializable createUserMetadataOnDestroy();

}
