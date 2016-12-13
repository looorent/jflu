package be.looorent.jflu.store;

/**
 * An exception that occurred when replaying events from the database.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class ReplayException extends Exception {

    ReplayException(Exception e) {
        super(e);
    }

}
