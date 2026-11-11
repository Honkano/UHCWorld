/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleDescription
implements Description {
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private List<String> permissions = new ArrayList<String>();
    private String description;
    private String help;
    private String overrideUsage;

    @Override
    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public SimpleDescription setParameters(List<Parameter> parameters) {
        this.parameters = Collections.unmodifiableList(parameters);
        return this;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public SimpleDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getHelp() {
        return this.help;
    }

    public SimpleDescription setHelp(String help) {
        this.help = help;
        return this;
    }

    @Override
    public List<String> getPermissions() {
        return this.permissions;
    }

    public SimpleDescription setPermissions(List<String> permissions) {
        this.permissions = Collections.unmodifiableList(permissions);
        return this;
    }

    public SimpleDescription overrideUsage(String usage) {
        this.overrideUsage = usage;
        return this;
    }

    @Override
    public String getUsage() {
        if (this.overrideUsage != null) {
            return this.overrideUsage;
        }
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Parameter parameter : this.parameters) {
            if (!first) {
                builder.append(" ");
            }
            builder.append(parameter);
            first = false;
        }
        return builder.toString();
    }

    public String toString() {
        return this.getUsage();
    }
}

