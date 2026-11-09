/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sk89q.worldedit.util.eventbus.EventHandler;
import com.sk89q.worldedit.util.eventbus.MethodEventHandler;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.util.eventbus.SubscriberFindingStrategy;
import java.lang.reflect.Method;

class AnnotatedSubscriberFinder
implements SubscriberFindingStrategy {
    AnnotatedSubscriberFinder() {
    }

    @Override
    public Multimap<Class<?>, EventHandler> findAllSubscribers(Object listener) {
        HashMultimap methodsInListener = HashMultimap.create();
        for (Class<?> clazz = listener.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getMethods()) {
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                method.setAccessible(true);
                if (annotation == null) continue;
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods " + "must require a single argument.");
                }
                Class<?> eventType = parameterTypes[0];
                MethodEventHandler handler = new MethodEventHandler(annotation.priority(), listener, method);
                methodsInListener.put(eventType, (Object)handler);
            }
        }
        return methodsInListener;
    }
}

