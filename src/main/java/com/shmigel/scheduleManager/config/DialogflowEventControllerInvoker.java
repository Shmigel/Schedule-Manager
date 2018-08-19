package com.shmigel.scheduleManager.config;

import com.shmigel.scheduleManager.controller.BaseController;
import com.shmigel.scheduleManager.controller.TestController;
import com.shmigel.scheduleManager.model.dialogflow.Request;
import com.shmigel.scheduleManager.model.dialogflow.Response;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class DialogflowEventControllerInvoker {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private TestController testController;

    @Autowired
    private BaseController baseController;

    @Autowired
    private Set<MethodWrapper> methodWrappers;

    public Response invokeProperMethod(Request request) {
        Iterator<MethodWrapper> iterator = methodWrappers.iterator();
        Response response = null;
        while (iterator.hasNext()) {
            MethodWrapper wrapper = iterator.next();
            if (request.getQueryResult().getAction().equals(wrapper.getAction())) {
                Method method = wrapper.getMethod();
                Object[] parameters = new Object[1];
                System.out.println(method.getDeclaringClass());
                if (method.getParameterCount() == 1) {
                     response = (Response) ReflectionUtils.invokeMethod(method, beanFactory.getBean(method.getDeclaringClass()),
                             request.getQueryResult().getParameters());
                } else if(method.getParameterCount() == 0) {
                     response = (Response) ReflectionUtils.invokeMethod(method, beanFactory.getBean(method.getDeclaringClass()));
                } else {
                    throw new RuntimeException("Can't find proper parameters");
                }

            }
        }
        return response;
    }

}
