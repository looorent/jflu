package be.looorent.jflu.springmvc;

import be.looorent.jflu.publisher.EventPublisher;
import be.looorent.jflu.request.RequestEventFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * If registered, this interceptor listens for HTTP requests
 * to Spring controllers and publish an {@link be.looorent.jflu.Event}
 * representing this request.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final String JFLU_START_TIME = "jfluStartTime";
    private static final String JFLU_REQUEST_ID = "jfluRequestId";

    private final EventPublisher publisher;
    private final RequestEventFactory factory;

    public RequestInterceptor(EventPublisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("publisher must not be null");
        }

        this.publisher = publisher;
        this.factory = new RequestEventFactory();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        request.setAttribute(JFLU_START_TIME, currentTimeMillis());
        request.setAttribute(JFLU_REQUEST_ID, randomUUID());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        HandlerMethod method = (HandlerMethod) handler;
        int duration = (int) (currentTimeMillis() - (Long) request.getAttribute(JFLU_START_TIME));
        UUID requestId = (UUID) request.getAttribute(JFLU_REQUEST_ID);

        publisher.publish(factory.createEvent(requestId,
                method.getMethod().getDeclaringClass().getSimpleName(),
                method.getMethod().getName(),
                request.getRequestURL().toString(),
                response.getStatus(),
                request.getHeader(USER_AGENT),
                duration,
                extractParametersFrom(request),
                null));
    }

    private Map<String, List<String>> extractParametersFrom(HttpServletRequest request) {
        return request.getParameterMap()
                .entrySet()
                .stream()
                .filter(entry -> !JFLU_REQUEST_ID.equals(entry.getKey()) && !JFLU_START_TIME.equals(entry.getKey()))
                .collect(toMap(Map.Entry::getKey, entry -> Arrays.asList(entry.getValue())));
    }
}
