package com.shmigel.scheduleManager.dialogflow.controller;

import com.shmigel.scheduleManager.dialogflow.model.request.MethodWrapper;
import com.shmigel.scheduleManager.dialogflow.model.request.Request;
import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
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
     * If method has one parameter, he's suggesting that it's parameter of the request,
     * and send requests' parameter as argue,
     * If method has more than one parameter he, by now, can't fill they, and throwing {@link RuntimeException},
     * If has not parameter just invoking the method
     *
     * @param request given request
     * @return TextResponse of invoked method if any
     */
    public DialogflowResponse invokeProperMethod(Request request) {
        Iterator<MethodWrapper> iterator = methodWrappers.iterator();
        DialogflowResponse response = new DialogflowResponse();
        while (iterator.hasNext()) {
            MethodWrapper wrapper = iterator.next();
            if (request.getQueryResult().getAction().equals(wrapper.getAction())) {
                Method method = wrapper.getMethod();
                Class<?> re = method.getReturnType();
                logger.debug("Find proper method: {} for processing request", method);
                if (method.getParameterCount() == 1) {
                     response = (DialogflowResponse) ReflectionUtils.invokeMethod(method,
                             beanFactory.getBean(method.getDeclaringClass()),
                             request.getQueryResult().getParameters());
                } else if(method.getParameterCount() == 0) {
                     response = (DialogflowResponse) ReflectionUtils.invokeMethod(method,
                             beanFactory.getBean(method.getDeclaringClass()));
                } else {
                    throw new RuntimeException("Can't fill methods' parameter");
                }

            }
        }
        return response;
    }

}
