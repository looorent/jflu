package be.looorent.jflu.manual;

import be.looorent.jflu.EventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualData extends HashMap<String, Object> implements EventData {

    ManualData() {
        super();
    }

    ManualData(Map<String, Object> data) {
        super(data);
    }
}
