package com.shmigel.scheduleManager.dialogflow.controller;

import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.shmigel.scheduleManager.dialogflow.model.request.MethodWrapper;

public class DialogflowEventControllerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BeanFactory beanFactory;

    private Set<MethodWrapper> methodWrappers = new HashSet<>();

    /**
     * Register methods that might process dialogflow request
     *
     * Methods should be annotated {@link EventMapping}
     * and locate in classes annotated {@link EventController}
     *
     * Each proper method saving in methodWrappers
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(EventController.class);
        beansWithAnnotation.forEach((i, j) -> {
            Method[] methods = beanFactory.getBean(i).getClass().getMethods();
            for (Method method : methods) {
                EventMapping annotation = method.getAnnotation(EventMapping.class);
                if (annotation != null) {
                    methodWrappers.add(new MethodWrapper(method, annotation.value()));
                }
            }
        });
        methodWrappers.forEach(System.out::println);
    }

    public Set<MethodWrapper> getMethodWrappers() {
        return methodWrappers;
    }
}

