package be.looorent.jflu.request;

import be.looorent.jflu.EventData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * When an event represents an HTTP request, it can use this class as {@link EventData} implementation.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RequestData implements EventData {

    private final UUID requestId;
    private final String controllerName;
    private final String actionName;
    private final String path;
    private final int responseCode;
    private final String userAgent;
    private final int duration;
    private final Map<String, List<String>> parameters;
    private final Object userMetadata;

    @JsonCreator
    public RequestData(@JsonProperty("requestId") UUID requestId,
                       @JsonProperty("controllerName") String controllerName,
                       @JsonProperty("actionName") String actionName,
                       @JsonProperty("path") String path,
                       @JsonProperty("responseCode") int responseCode,
                       @JsonProperty("userAgent") String userAgent,
                       @JsonProperty("duration") int duration,
                       @JsonProperty("params") Map<String, List<String>> parameters,
                       @JsonProperty("userMetadata") Object userMetadata) {
        this.requestId = requestId;
        this.controllerName = controllerName;
        this.actionName = actionName;
        this.path = path;
        this.responseCode = responseCode;
        this.userAgent = userAgent;
        this.duration = duration;
        this.parameters = parameters;
        this.userMetadata = userMetadata;
    }

    public UUID getRequestId() {
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

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public Object getUserMetadata() {
        return userMetadata;
    }
}
