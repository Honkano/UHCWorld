/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.eventbus;

import com.google.common.collect.Multimap;
import com.sk89q.worldedit.util.eventbus.EventHandler;

interface SubscriberFindingStrategy {
    public Multimap<Class<?>, EventHandler> findAllSubscribers(Object var1);
}

