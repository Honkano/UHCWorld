/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.Parameter;
import java.util.List;

public interface Description {
    public List<Parameter> getParameters();

    public String getDescription();

    public String getHelp();

    public String getUsage();

    public List<String> getPermissions();
}

