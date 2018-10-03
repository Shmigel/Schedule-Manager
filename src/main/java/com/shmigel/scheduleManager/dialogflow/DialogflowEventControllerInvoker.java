package com.shmigel.scheduleManager.dialogflow;

import com.shmigel.scheduleManager.dialogflow.model.MethodWrapper;
import com.shmigel.scheduleManager.dialogflow.model.Request;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class DialogflowEventControllerInvoker {

    private Logger logger = LoggerFactory.getLogger(DialogflowEventControllerInvoker.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private Set<MethodWrapper> methodWrappers;

    /**
     * Try to find method to process given request by comparing methods' annotation and request action parameter.
     *
     * If method has one parameter, he's suggesting that it's parameters of the request,
     * and send requests' parameters as argue,
     * If method has more than one parameter he, by now, can't fill they, and throwing {@link RuntimeException},
     * If has not parameters just invoking the method
     *
     * @param request given request
     * @return Response of invoked method if any
     */
    public Response invokeProperMethod(Request request) {
        Iterator<MethodWrapper> iterator = methodWrappers.iterator();
        Response response = Response.getUnknownAnswer();
        while (iterator.hasNext()) {
            MethodWrapper wrapper = iterator.next();
            if (request.getQueryResult().getAction().equals(wrapper.getAction())) {
                Method method = wrapper.getMethod();
                logger.debug("Find proper method: {} for processing request", method);
                if (method.getParameterCount() == 1) {
                     response = (Response) ReflectionUtils.invokeMethod(method,
                             beanFactory.getBean(method.getDeclaringClass()),
                             request.getQueryResult().getParameters());
                } else if(method.getParameterCount() == 0) {
                     response = (Response) ReflectionUtils.invokeMethod(method,
                             beanFactory.getBean(method.getDeclaringClass()));
                } else {
                    throw new RuntimeException("Can't fill methods' parameters");
                }

            }
        }
        return response;
    }

}
