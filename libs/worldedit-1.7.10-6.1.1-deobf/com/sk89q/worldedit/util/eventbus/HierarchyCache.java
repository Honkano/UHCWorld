/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

class HierarchyCache {
    private final Map<Class<?>, Set<Class<?>>> cache = new WeakHashMap();

    HierarchyCache() {
    }

    public Set<Class<?>> get(Class<?> concreteClass) {
        Set<Class<?>> ret = this.cache.get(concreteClass);
        if (ret == null) {
            ret = this.build(concreteClass);
            this.cache.put(concreteClass, ret);
        }
        return ret;
    }

    protected Set<Class<?>> build(Class<?> concreteClass) {
        LinkedList parents = Lists.newLinkedList();
        HashSet classes = Sets.newHashSet();
        parents.add(concreteClass);
        while (!parents.isEmpty()) {
            Class clazz = (Class)parents.remove(0);
            classes.add(clazz);
            Class parent = clazz.getSuperclass();
            if (parent != null) {
                parents.add(parent);
            }
            Collections.addAll(parents, clazz.getInterfaces());
        }
        return classes;
    }
}

