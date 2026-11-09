/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.BiomeCommands;
import com.sk89q.worldedit.command.BrushCommands;
import com.sk89q.worldedit.command.ChunkCommands;
import com.sk89q.worldedit.command.ClipboardCommands;
import com.sk89q.worldedit.command.GeneralCommands;
import com.sk89q.worldedit.command.GenerationCommands;
import com.sk89q.worldedit.command.HistoryCommands;
import com.sk89q.worldedit.command.NavigationCommands;
import com.sk89q.worldedit.command.RegionCommands;
import com.sk89q.worldedit.command.SchematicCommands;
import com.sk89q.worldedit.command.ScriptingCommands;
import com.sk89q.worldedit.command.SelectionCommands;
import com.sk89q.worldedit.command.SnapshotCommands;
import com.sk89q.worldedit.command.SnapshotUtilCommands;
import com.sk89q.worldedit.command.SuperPickaxeCommands;
import com.sk89q.worldedit.command.ToolCommands;
import com.sk89q.worldedit.command.ToolUtilCommands;
import com.sk89q.worldedit.command.UtilityCommands;
import com.sk89q.worldedit.command.WorldEditCommands;
import com.sk89q.worldedit.command.argument.ReplaceParser;
import com.sk89q.worldedit.command.argument.TreeGeneratorParser;
import com.sk89q.worldedit.command.composition.ApplyCommand;
import com.sk89q.worldedit.command.composition.DeformCommand;
import com.sk89q.worldedit.command.composition.PaintCommand;
import com.sk89q.worldedit.command.composition.SelectionCommand;
import com.sk89q.worldedit.command.composition.ShapedBrushCommand;
import com.sk89q.worldedit.event.platform.CommandEvent;
import com.sk89q.worldedit.event.platform.CommandSuggestionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.PlatformManager;
import com.sk89q.worldedit.function.factory.Deform;
import com.sk89q.worldedit.internal.command.ActorAuthorizer;
import com.sk89q.worldedit.internal.command.CommandLoggingHandler;
import com.sk89q.worldedit.internal.command.UserCommandCompleter;
import com.sk89q.worldedit.internal.command.WorldEditBinding;
import com.sk89q.worldedit.internal.command.WorldEditExceptionConverter;
import com.sk89q.worldedit.session.request.Request;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.InvalidUsageException;
import com.sk89q.worldedit.util.command.composition.LegacyCommandAdapter;
import com.sk89q.worldedit.util.command.composition.ProvidedValue;
import com.sk89q.worldedit.util.command.fluent.CommandGraph;
import com.sk89q.worldedit.util.command.parametric.ExceptionConverter;
import com.sk89q.worldedit.util.command.parametric.LegacyCommandsHandler;
import com.sk89q.worldedit.util.command.parametric.ParametricBuilder;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.util.formatting.ColorCodeBuilder;
import com.sk89q.worldedit.util.formatting.component.CommandUsageBox;
import com.sk89q.worldedit.util.logging.DynamicStreamHandler;
import com.sk89q.worldedit.util.logging.LogFormat;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class CommandManager {
    public static final Pattern COMMAND_CLEAN_PATTERN = Pattern.compile("^[/]+");
    private static final Logger log = Logger.getLogger(CommandManager.class.getCanonicalName());
    private static final Logger commandLog = Logger.getLogger(CommandManager.class.getCanonicalName() + ".CommandLog");
    private static final Pattern numberFormatExceptionPattern = Pattern.compile("^For input string: \"(.*)\"$");
    private final WorldEdit worldEdit;
    private final PlatformManager platformManager;
    private final Dispatcher dispatcher;
    private final DynamicStreamHandler dynamicHandler = new DynamicStreamHandler();
    private final ExceptionConverter exceptionConverter;

    CommandManager(WorldEdit worldEdit, PlatformManager platformManager) {
        Preconditions.checkNotNull((Object)worldEdit);
        Preconditions.checkNotNull((Object)platformManager);
        this.worldEdit = worldEdit;
        this.platformManager = platformManager;
        this.exceptionConverter = new WorldEditExceptionConverter(worldEdit);
        worldEdit.getEventBus().register(this);
        commandLog.addHandler(this.dynamicHandler);
        this.dynamicHandler.setFormatter(new LogFormat());
        ParametricBuilder builder = new ParametricBuilder();
        builder.setAuthorizer(new ActorAuthorizer());
        builder.setDefaultCompleter(new UserCommandCompleter(platformManager));
        builder.addBinding(new WorldEditBinding(worldEdit), new Type[0]);
        builder.addExceptionConverter(this.exceptionConverter);
        builder.addInvokeListener(new LegacyCommandsHandler());
        builder.addInvokeListener(new CommandLoggingHandler(worldEdit, commandLog));
        this.dispatcher = new CommandGraph().builder(builder).commands().registerMethods(new BiomeCommands(worldEdit)).registerMethods(new ChunkCommands(worldEdit)).registerMethods(new ClipboardCommands(worldEdit)).registerMethods(new GeneralCommands(worldEdit)).registerMethods(new GenerationCommands(worldEdit)).registerMethods(new HistoryCommands(worldEdit)).registerMethods(new NavigationCommands(worldEdit)).registerMethods(new RegionCommands(worldEdit)).registerMethods(new ScriptingCommands(worldEdit)).registerMethods(new SelectionCommands(worldEdit)).registerMethods(new SnapshotUtilCommands(worldEdit)).registerMethods(new ToolUtilCommands(worldEdit)).registerMethods(new ToolCommands(worldEdit)).registerMethods(new UtilityCommands(worldEdit)).register(LegacyCommandAdapter.adapt(new SelectionCommand(new ApplyCommand(new ReplaceParser(), "Set all blocks within selection"), "worldedit.region.set")), "/set").group("worldedit", "we").describeAs("WorldEdit commands").registerMethods(new WorldEditCommands(worldEdit)).parent().group("schematic", "schem", "/schematic", "/schem").describeAs("Schematic commands for saving/loading areas").registerMethods(new SchematicCommands(worldEdit)).parent().group("snapshot", "snap").describeAs("Schematic commands for saving/loading areas").registerMethods(new SnapshotCommands(worldEdit)).parent().group("brush", "br").describeAs("Brushing commands").registerMethods(new BrushCommands(worldEdit)).register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(new DeformCommand(), "worldedit.brush.deform")), "deform").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(new ApplyCommand(new ReplaceParser(), "Set all blocks within region"), "worldedit.brush.set")), "set").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(new PaintCommand(), "worldedit.brush.paint")), "paint").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(new ApplyCommand(), "worldedit.brush.apply")), "apply").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(new PaintCommand(new TreeGeneratorParser("treeType")), "worldedit.brush.forest")), "forest").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(ProvidedValue.create(new Deform("y-=1", Deform.Mode.RAW_COORD), "Raise one block"), "worldedit.brush.raise")), "raise").register(LegacyCommandAdapter.adapt(new ShapedBrushCommand(ProvidedValue.create(new Deform("y+=1", Deform.Mode.RAW_COORD), "Lower one block"), "worldedit.brush.lower")), "lower").parent().group("superpickaxe", "pickaxe", "sp").describeAs("Super-pickaxe commands").registerMethods(new SuperPickaxeCommands(worldEdit)).parent().group("tool").describeAs("Bind functions to held items").registerMethods(new ToolCommands(worldEdit)).parent().graph().getDispatcher();
    }

    public ExceptionConverter getExceptionConverter() {
        return this.exceptionConverter;
    }

    void register(Platform platform) {
        log.log(Level.FINE, "Registering commands with " + platform.getClass().getCanonicalName());
        LocalConfiguration config = platform.getConfiguration();
        boolean logging = config.logCommands;
        String path = config.logFile;
        if (!logging || path.isEmpty()) {
            this.dynamicHandler.setHandler(null);
            commandLog.setLevel(Level.OFF);
        } else {
            File file = new File(config.getWorkingDirectory(), path);
            commandLog.setLevel(Level.ALL);
            log.log(Level.INFO, "Logging WorldEdit commands to " + file.getAbsolutePath());
            try {
                this.dynamicHandler.setHandler(new FileHandler(file.getAbsolutePath(), true));
            }
            catch (IOException e) {
                log.log(Level.WARNING, "Could not use command log file " + path + ": " + e.getMessage());
            }
        }
        platform.registerCommands(this.dispatcher);
    }

    void unregister() {
        this.dynamicHandler.setHandler(null);
    }

    public String[] commandDetection(String[] split) {
        String searchCmd;
        if (split[0].matches("^[^/].*\\.js$")) {
            String[] newSplit = new String[split.length + 1];
            System.arraycopy(split, 0, newSplit, 1, split.length);
            newSplit[0] = "cs";
            newSplit[1] = newSplit[1];
            split = newSplit;
        }
        if (!this.dispatcher.contains(searchCmd = split[0].toLowerCase())) {
            if (this.worldEdit.getConfiguration().noDoubleSlash && this.dispatcher.contains("/" + searchCmd)) {
                split[0] = "/" + split[0];
            } else if (searchCmd.length() >= 2 && searchCmd.charAt(0) == '/' && this.dispatcher.contains(searchCmd.substring(1))) {
                split[0] = split[0].substring(1);
            }
        }
        return split;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    public void handleCommand(CommandEvent event) {
        Request.reset();
        Actor actor = this.platformManager.createProxyActor(event.getActor());
        Object[] split = this.commandDetection(event.getArguments().split(" "));
        if (!this.dispatcher.contains(split[0])) {
            return;
        }
        LocalSession session = this.worldEdit.getSessionManager().get(actor);
        LocalConfiguration config = this.worldEdit.getConfiguration();
        CommandLocals locals = new CommandLocals();
        locals.put(Actor.class, actor);
        locals.put("arguments", event.getArguments());
        long start = System.currentTimeMillis();
        try {
            this.dispatcher.call(Joiner.on((String)" ").join(split), locals, new String[0]);
        }
        catch (CommandPermissionsException e) {
            actor.printError("You are not permitted to do that. Are you in the right mode?");
        }
        catch (InvalidUsageException e) {
            String message;
            if (e.isFullHelpSuggested()) {
                actor.printRaw(ColorCodeBuilder.asColorCodes(new CommandUsageBox(e.getCommand(), e.getCommandUsed("/", ""), locals)));
                message = e.getMessage();
                if (message != null) {
                    actor.printError(message);
                }
            } else {
                message = e.getMessage();
                actor.printError(message != null ? message : "The command was not used properly (no more help available).");
                actor.printError("Usage: " + e.getSimpleUsageString("/"));
            }
        }
        catch (WrappedCommandException e) {
            Throwable t = e.getCause();
            actor.printError("Please report this error: [See console]");
            actor.printRaw(t.getClass().getName() + ": " + t.getMessage());
            log.log(Level.SEVERE, "An unexpected error while handling a WorldEdit command", t);
        }
        catch (CommandException e) {
            String message = e.getMessage();
            if (message != null) {
                actor.printError(e.getMessage());
            } else {
                actor.printError("An unknown error has occurred! Please see console.");
                log.log(Level.SEVERE, "An unknown error occurred", e);
            }
        }
        finally {
            EditSession editSession = locals.get(EditSession.class);
            if (editSession != null) {
                session.remember(editSession);
                editSession.flushQueue();
                if (config.profile) {
                    long time = System.currentTimeMillis() - start;
                    int changed = editSession.getBlockChangeCount();
                    if (time > 0L) {
                        double throughput = (double)changed / ((double)time / 1000.0);
                        actor.printDebug((double)time / 1000.0 + "s elapsed (history: " + changed + " changed; " + Math.round(throughput) + " blocks/sec).");
                    } else {
                        actor.printDebug((double)time / 1000.0 + "s elapsed.");
                    }
                }
                this.worldEdit.flushBlockBag(actor, editSession);
            }
        }
        event.setCancelled(true);
    }

    @Subscribe
    public void handleCommandSuggestion(CommandSuggestionEvent event) {
        try {
            CommandLocals locals = new CommandLocals();
            locals.put(Actor.class, event.getActor());
            locals.put("arguments", event.getArguments());
            event.setSuggestions(this.dispatcher.getSuggestions(event.getArguments(), locals));
        }
        catch (CommandException e) {
            event.getActor().printError(e.getMessage());
        }
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    public static Logger getLogger() {
        return commandLog;
    }
}

