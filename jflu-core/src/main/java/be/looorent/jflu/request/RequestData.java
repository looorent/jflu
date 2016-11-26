package be.looorent.jflu.request;

import be.looorent.jflu.EventData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RequestData implements EventData {

    private final String requestId;
    private final String controllerName;
    private final String actionName;
    private final String path;
    private final int responseCode;
    private final String userAgent;
    private final int duration;
    private final Map<String, String[]> parameters;

    @JsonCreator
    public RequestData(@JsonProperty("requestId") String requestId,
                       @JsonProperty("controllerName") String controllerName,
                       @JsonProperty("actionName") String actionName,
                       @JsonProperty("path") String path,
                       @JsonProperty("responseCode") int responseCode,
                       @JsonProperty("userAgent") String userAgent,
                       @JsonProperty("duration") int duration,
                       @JsonProperty("parameters") Map<String, String[]> parameters) {
        this.requestId = requestId;
        this.controllerName = controllerName;
        this.actionName = actionName;
        this.path = path;
        this.responseCode = responseCode;
        this.userAgent = userAgent;
        this.duration = duration;
        this.parameters = parameters;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getControllerName() {
        return controllerName;
    }

    public String getActionName() {
        return actionName;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public int getDuration() {
        return duration;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }
}
