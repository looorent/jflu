package be.looorent.jflu.entity;

import be.looorent.jflu.EventData;

import java.io.Serializable;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityData implements EventData {

    private Serializable id;
    private String entityName;
    private EntityActionName actionName;
    private Object userMetadata;
    private Object associations;

}
