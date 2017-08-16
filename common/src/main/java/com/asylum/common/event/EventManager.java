package com.asylum.common.event;

import com.asylum.common.event.events.IEvent;
import com.asylum.common.event.events.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by James on 8/15/2017.
 */
public class EventManager {
    private static EventManager instance;

    private ArrayList<Object> eventHandlers;

    private EventManager(){
        eventHandlers= new ArrayList<>();
    }

    public static EventManager getInstance(){
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void registerHandler(Object obj){
        eventHandlers.add(obj);
    }


    public boolean dispatchEvent(IEvent event) {
        for (Object obj : eventHandlers) {
            Class clazz = obj.getClass();
            //This data should really be cached for speed reasons,
            //unless you think your users might do something insane with ASM.
            for (Method method : clazz.getMethods()){
                if (method.isAnnotationPresent(SubscribeEvent.class)){
                    //Not sure if I like equivilency.  Might want to switch to some form of instanceof
                    if (method.getParameterCount() == 1 && event.getClass() == method.getParameters()[0].getType()){
                        try {
                            method.invoke(obj,event);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return event.isCanceled();
    }
}
