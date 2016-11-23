package be.looorent.jflu.request;

import be.looorent.jflu.publisher.EventPublisher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RequestListener extends HandlerInterceptorAdapter {


    private final EventPublisher publisher;
    private final RequestEventFactory factory;

    public RequestListener(EventPublisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("publisher must not be null");
        }

        this.publisher = publisher;
        this.factory = new RequestEventFactory();
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        publisher.publish(factory.createEvent("requestId",
                "controllerName",
                "actionName",
                request.getRemoteAddr(),
                response.getStatus(),
                request.getHeader("User-Agent"),
                0,
                request.getParameterMap()));
    }
}
