package com.shmigel.scheduleManager.dialogflow.controller;

import com.shmigel.scheduleManager.dialogflow.model.request.MethodWrapper;
import com.shmigel.scheduleManager.dialogflow.model.request.Request;
import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DialogflowEventControllerInvoker {

    private Logger logger = LoggerFactory.getLogger(DialogflowEventControllerInvoker.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private Set<MethodWrapper> methodWrappers;

    /**
     * Trying to find method in order to process given request by comparing methods' annotation and request action parameter.
     *
     * If method has one parameter, he's suggesting that it's parameter of the request,
     * and send requests' parameter as argue,
     * If method has more than one parameter then we try to fulfill them by request parameters,
     * comparing names of method parameters and names of request parameters,
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
                Parameter[] parameters = method.getParameters();
                logger.debug("Find proper method: {} for processing request", method);
                if (method.getParameterCount() == 1 &&
                        parameters[0].getType().getName().equals(Map.class.getName())) {
                    response = (DialogflowResponse) ReflectionUtils.invokeMethod(method,
                            beanFactory.getBean(method.getDeclaringClass()),
                            request.getQueryResult().getParameters());
                } else if(method.getParameterCount() != 0) {
                    response = (DialogflowResponse) ReflectionUtils.invokeMethod(method,
                            beanFactory.getBean(method.getDeclaringClass()),
                            matchMethodParameters(method, request.getQueryResult().getParameters()));
                } else if(method.getParameterCount() == 0) {
                     response = (DialogflowResponse) ReflectionUtils.invokeMethod(method,
                             beanFactory.getBean(method.getDeclaringClass()));
                } else {
                    throw new RuntimeException("Can't fill method parameters");
                }

            }
        }
        return response;
    }

    private String[] matchMethodParameters(Method method,
                                                         Map<String, String> givenParameters) {
        List<String> fitParameters = new ArrayList<>();
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);

        for (String name: parameterNames) {
            if (givenParameters.containsKey(name)) {
                fitParameters.add(givenParameters.get(name));
            } else
                throw new RuntimeException("Can't fill method parameters");
        }
        //java
        String[] array = new String[fitParameters.size()];
        return fitParameters.toArray(array);
    }

}
