/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxBrushRadiusException;
import com.sk89q.worldedit.MaxRadiusException;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.UnknownDirectionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import com.sk89q.worldedit.event.platform.InputType;
import com.sk89q.worldedit.event.platform.Interaction;
import com.sk89q.worldedit.event.platform.PlayerInputEvent;
import com.sk89q.worldedit.extension.factory.BlockFactory;
import com.sk89q.worldedit.extension.factory.ItemFactory;
import com.sk89q.worldedit.extension.factory.MaskFactory;
import com.sk89q.worldedit.extension.factory.PatternFactory;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.PlatformManager;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.pattern.Patterns;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.scripting.CraftScriptContext;
import com.sk89q.worldedit.scripting.RhinoCraftScriptEngine;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.session.request.Request;
import com.sk89q.worldedit.util.eventbus.EventBus;
import com.sk89q.worldedit.util.io.file.FileSelectionAbortedException;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.util.io.file.FilenameResolutionException;
import com.sk89q.worldedit.util.io.file.InvalidFilenameException;
import com.sk89q.worldedit.util.logging.WorldEditPrefixHandler;
import com.sk89q.worldedit.world.registry.BundledBlockData;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;

public class WorldEdit {
    public static final Logger logger = Logger.getLogger(WorldEdit.class.getCanonicalName());
    private static final WorldEdit instance = new WorldEdit();
    private static String version;
    private final EventBus eventBus = new EventBus();
    private final PlatformManager platformManager = new PlatformManager(this);
    private final EditSessionFactory editSessionFactory = new EditSessionFactory.EditSessionFactoryImpl(this.eventBus);
    private final SessionManager sessions = new SessionManager(this);
    private final BlockFactory blockFactory = new BlockFactory(this);
    private final ItemFactory itemFactory = new ItemFactory(this);
    private final MaskFactory maskFactory = new MaskFactory(this);
    private final PatternFactory patternFactory = new PatternFactory(this);

    private WorldEdit() {
    }

    public static WorldEdit getInstance() {
        return instance;
    }

    public PlatformManager getPlatformManager() {
        return this.platformManager;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public BlockFactory getBlockFactory() {
        return this.blockFactory;
    }

    public ItemFactory getItemFactory() {
        return this.itemFactory;
    }

    public MaskFactory getMaskFactory() {
        return this.maskFactory;
    }

    public PatternFactory getPatternFactory() {
        return this.patternFactory;
    }

    public SessionManager getSessionManager() {
        return this.sessions;
    }

    @Deprecated
    public LocalSession getSession(String player) {
        return this.sessions.findByName(player);
    }

    @Deprecated
    public LocalSession getSession(Player player) {
        return this.sessions.get(player);
    }

    @Deprecated
    public void removeSession(Player player) {
        this.sessions.remove(player);
    }

    @Deprecated
    public void clearSessions() {
        this.sessions.clear();
    }

    @Deprecated
    public boolean hasSession(Player player) {
        return this.sessions.contains(player);
    }

    @Deprecated
    public BaseBlock getBlock(Player player, String arg, boolean allAllowed) throws WorldEditException {
        return this.getBlock(player, arg, allAllowed, false);
    }

    @Deprecated
    public BaseBlock getBlock(Player player, String arg, boolean allAllowed, boolean allowNoData) throws WorldEditException {
        ParserContext context = new ParserContext();
        context.setActor(player);
        context.setWorld(player.getWorld());
        context.setSession(this.getSession(player));
        context.setRestricted(!allAllowed);
        context.setPreferringWildcard(allowNoData);
        return (BaseBlock)this.getBlockFactory().parseFromInput(arg, context);
    }

    @Deprecated
    public BaseBlock getBlock(Player player, String id) throws WorldEditException {
        return this.getBlock(player, id, false);
    }

    @Deprecated
    public Set<BaseBlock> getBlocks(Player player, String list, boolean allAllowed, boolean allowNoData) throws WorldEditException {
        String[] items = list.split(",");
        HashSet<BaseBlock> blocks = new HashSet<BaseBlock>();
        for (String id : items) {
            blocks.add(this.getBlock(player, id, allAllowed, allowNoData));
        }
        return blocks;
    }

    @Deprecated
    public Set<BaseBlock> getBlocks(Player player, String list, boolean allAllowed) throws WorldEditException {
        return this.getBlocks(player, list, allAllowed, false);
    }

    @Deprecated
    public Set<BaseBlock> getBlocks(Player player, String list) throws WorldEditException {
        return this.getBlocks(player, list, false);
    }

    @Deprecated
    public Set<Integer> getBlockIDs(Player player, String list, boolean allBlocksAllowed) throws WorldEditException {
        String[] items = list.split(",");
        HashSet<Integer> blocks = new HashSet<Integer>();
        for (String s : items) {
            blocks.add(this.getBlock(player, s, allBlocksAllowed).getType());
        }
        return blocks;
    }

    @Deprecated
    public Pattern getBlockPattern(Player player, String input) throws WorldEditException {
        ParserContext context = new ParserContext();
        context.setActor(player);
        context.setWorld(player.getWorld());
        context.setSession(this.getSession(player));
        return Patterns.wrap((com.sk89q.worldedit.function.pattern.Pattern)this.getPatternFactory().parseFromInput(input, context));
    }

    @Deprecated
    public Mask getBlockMask(Player player, LocalSession session, String input) throws WorldEditException {
        ParserContext context = new ParserContext();
        context.setActor(player);
        context.setWorld(player.getWorld());
        context.setSession(session);
        return Masks.wrap((com.sk89q.worldedit.function.mask.Mask)this.getMaskFactory().parseFromInput(input, context));
    }

    public File getSafeSaveFile(Player player, File dir, String filename, String defaultExt, String ... extensions) throws FilenameException {
        return this.getSafeFile(player, dir, filename, defaultExt, extensions, true);
    }

    public File getSafeOpenFile(Player player, File dir, String filename, String defaultExt, String ... extensions) throws FilenameException {
        return this.getSafeFile(player, dir, filename, defaultExt, extensions, false);
    }

    private File getSafeFile(Player player, File dir, String filename, String defaultExt, String[] extensions, boolean isSave) throws FilenameException {
        File f;
        if (extensions != null && extensions.length == 1 && extensions[0] == null) {
            extensions = null;
        }
        if (filename.equals("#")) {
            f = isSave ? player.openFileSaveDialog(extensions) : player.openFileOpenDialog(extensions);
            if (f == null) {
                throw new FileSelectionAbortedException("No file selected");
            }
        } else {
            if (defaultExt != null && filename.lastIndexOf(46) == -1) {
                filename = filename + "." + defaultExt;
            }
            if (!filename.matches("^[A-Za-z0-9_\\- \\./\\\\'\\$@~!%\\^\\*\\(\\)\\[\\]\\+\\{\\},\\?]+\\.[A-Za-z0-9]+$")) {
                throw new InvalidFilenameException(filename, "Invalid characters or extension missing");
            }
            f = new File(dir, filename);
        }
        try {
            String filePath = f.getCanonicalPath();
            String dirPath = dir.getCanonicalPath();
            if (!filePath.substring(0, dirPath.length()).equals(dirPath) && !this.getConfiguration().allowSymlinks) {
                throw new FilenameResolutionException(filename, "Path is outside allowable root");
            }
            return f;
        }
        catch (IOException e) {
            throw new FilenameResolutionException(filename, "Failed to resolve path");
        }
    }

    public void checkMaxRadius(double radius) throws MaxRadiusException {
        if (this.getConfiguration().maxRadius > 0 && radius > (double)this.getConfiguration().maxRadius) {
            throw new MaxRadiusException();
        }
    }

    public void checkMaxBrushRadius(double radius) throws MaxBrushRadiusException {
        if (this.getConfiguration().maxBrushRadius > 0 && radius > (double)this.getConfiguration().maxBrushRadius) {
            throw new MaxBrushRadiusException();
        }
    }

    public File getWorkingDirectoryFile(String path) {
        File f = new File(path);
        if (f.isAbsolute()) {
            return f;
        }
        return new File(this.getConfiguration().getWorkingDirectory(), path);
    }

    public Vector getDirection(Player player, String dirStr) throws UnknownDirectionException {
        dirStr = dirStr.toLowerCase();
        PlayerDirection dir = this.getPlayerDirection(player, dirStr);
        switch (dir) {
            case WEST: 
            case EAST: 
            case SOUTH: 
            case NORTH: 
            case UP: 
            case DOWN: {
                return dir.vector();
            }
        }
        throw new UnknownDirectionException(dir.name());
    }

    private PlayerDirection getPlayerDirection(Player player, String dirStr) throws UnknownDirectionException {
        PlayerDirection dir;
        switch (dirStr.charAt(0)) {
            case 'w': {
                dir = PlayerDirection.WEST;
                break;
            }
            case 'e': {
                dir = PlayerDirection.EAST;
                break;
            }
            case 's': {
                if (dirStr.indexOf(119) > 0) {
                    return PlayerDirection.SOUTH_WEST;
                }
                if (dirStr.indexOf(101) > 0) {
                    return PlayerDirection.SOUTH_EAST;
                }
                dir = PlayerDirection.SOUTH;
                break;
            }
            case 'n': {
                if (dirStr.indexOf(119) > 0) {
                    return PlayerDirection.NORTH_WEST;
                }
                if (dirStr.indexOf(101) > 0) {
                    return PlayerDirection.NORTH_EAST;
                }
                dir = PlayerDirection.NORTH;
                break;
            }
            case 'u': {
                dir = PlayerDirection.UP;
                break;
            }
            case 'd': {
                dir = PlayerDirection.DOWN;
                break;
            }
            case 'f': 
            case 'm': {
                dir = player.getCardinalDirection(0);
                break;
            }
            case 'b': {
                dir = player.getCardinalDirection(180);
                break;
            }
            case 'l': {
                dir = player.getCardinalDirection(-90);
                break;
            }
            case 'r': {
                dir = player.getCardinalDirection(90);
                break;
            }
            default: {
                throw new UnknownDirectionException(dirStr);
            }
        }
        return dir;
    }

    public Vector getDiagonalDirection(Player player, String dirStr) throws UnknownDirectionException {
        return this.getPlayerDirection(player, dirStr.toLowerCase()).vector();
    }

    public CuboidClipboard.FlipDirection getFlipDirection(Player player, String dirStr) throws UnknownDirectionException {
        PlayerDirection dir = this.getPlayerDirection(player, dirStr);
        switch (dir) {
            case WEST: 
            case EAST: {
                return CuboidClipboard.FlipDirection.WEST_EAST;
            }
            case SOUTH: 
            case NORTH: {
                return CuboidClipboard.FlipDirection.NORTH_SOUTH;
            }
            case UP: 
            case DOWN: {
                return CuboidClipboard.FlipDirection.UP_DOWN;
            }
        }
        throw new UnknownDirectionException(dir.name());
    }

    public void flushBlockBag(Actor actor, EditSession editSession) {
        Map<Integer, Integer> missingBlocks;
        BlockBag blockBag = editSession.getBlockBag();
        if (blockBag != null) {
            blockBag.flushChanges();
        }
        if (!(missingBlocks = editSession.popMissingBlocks()).isEmpty()) {
            StringBuilder str = new StringBuilder();
            str.append("Missing these blocks: ");
            int size = missingBlocks.size();
            int i = 0;
            for (Integer id : missingBlocks.keySet()) {
                BlockType type = BlockType.fromID(id);
                str.append(type != null ? type.getName() + " (" + id + ")" : id.toString());
                str.append(" [Amt: ").append(missingBlocks.get(id)).append("]");
                if (++i == size) continue;
                str.append(", ");
            }
            actor.printError(str.toString());
        }
    }

    public boolean handleArmSwing(Player player) {
        PlayerInputEvent event = new PlayerInputEvent(player, InputType.PRIMARY);
        this.getEventBus().post(event);
        return event.isCancelled();
    }

    public boolean handleRightClick(Player player) {
        PlayerInputEvent event = new PlayerInputEvent(player, InputType.SECONDARY);
        this.getEventBus().post(event);
        return event.isCancelled();
    }

    public boolean handleBlockRightClick(Player player, WorldVector clicked) {
        BlockInteractEvent event = new BlockInteractEvent(player, clicked.toLocation(), Interaction.OPEN);
        this.getEventBus().post(event);
        return event.isCancelled();
    }

    public boolean handleBlockLeftClick(Player player, WorldVector clicked) {
        BlockInteractEvent event = new BlockInteractEvent(player, clicked.toLocation(), Interaction.HIT);
        this.getEventBus().post(event);
        return event.isCancelled();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void runScript(Player player, File f, String[] args) throws WorldEditException {
        String script;
        Request.reset();
        String filename = f.getPath();
        int index = filename.lastIndexOf(".");
        String ext = filename.substring(index + 1, filename.length());
        if (!ext.equalsIgnoreCase("js")) {
            player.printError("Only .js scripts are currently supported");
            return;
        }
        try {
            InputStream file;
            if (!f.exists()) {
                file = WorldEdit.class.getResourceAsStream("craftscripts/" + filename);
                if (file == null) {
                    player.printError("Script does not exist: " + filename);
                    return;
                }
            } else {
                file = new FileInputStream(f);
            }
            DataInputStream in = new DataInputStream(file);
            byte[] data = new byte[in.available()];
            in.readFully(data);
            in.close();
            script = new String(data, 0, data.length, "utf-8");
        }
        catch (IOException e) {
            player.printError("Script read error: " + e.getMessage());
            return;
        }
        LocalSession session = this.getSessionManager().get(player);
        CraftScriptContext scriptContext = new CraftScriptContext(this, this.getServer(), this.getConfiguration(), session, player, args);
        RhinoCraftScriptEngine engine = null;
        try {
            engine = new RhinoCraftScriptEngine();
        }
        catch (NoClassDefFoundError e) {
            player.printError("Failed to find an installed script engine.");
            player.printError("Please see http://wiki.sk89q.com/wiki/WorldEdit/Installation");
            return;
        }
        engine.setTimeLimit(this.getConfiguration().scriptTimeout);
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("argv", args);
        vars.put("context", scriptContext);
        vars.put("player", player);
        try {
            engine.evaluate(script, filename, vars);
        }
        catch (ScriptException e) {
            player.printError("Failed to execute:");
            player.printRaw(e.getMessage());
            logger.log(Level.WARNING, "Failed to execute script", e);
        }
        catch (NumberFormatException e) {
            throw e;
        }
        catch (WorldEditException e) {
            throw e;
        }
        catch (Throwable e) {
            player.printError("Failed to execute (see console):");
            player.printRaw(e.getClass().getCanonicalName());
            logger.log(Level.WARNING, "Failed to execute script", e);
        }
        finally {
            for (EditSession editSession : scriptContext.getEditSessions()) {
                editSession.flushQueue();
                session.remember(editSession);
            }
        }
    }

    public LocalConfiguration getConfiguration() {
        return this.getPlatformManager().getConfiguration();
    }

    public ServerInterface getServer() {
        return this.getPlatformManager().getServerInterface();
    }

    public EditSessionFactory getEditSessionFactory() {
        return this.editSessionFactory;
    }

    @Deprecated
    public void setEditSessionFactory(EditSessionFactory factory) {
        Preconditions.checkNotNull((Object)factory);
        logger.severe("Got request to set EditSessionFactory of type " + factory.getClass().getName() + " from " + factory.getClass().getPackage().getName() + " but EditSessionFactories have been removed in favor of extending EditSession's extents.\n\n" + "This may mean that any block logger / intercepters addons/plugins/mods that you have installed will not " + "intercept WorldEdit's changes! Please notify the maintainer of the other addon about this.");
    }

    public static String getVersion() {
        if (version != null) {
            return version;
        }
        Package p = WorldEdit.class.getPackage();
        if (p == null) {
            p = Package.getPackage("com.sk89q.worldedit");
        }
        if (p == null) {
            version = "(unknown)";
        } else {
            version = p.getImplementationVersion();
            if (version == null) {
                version = "(unknown)";
            }
        }
        return version;
    }

    @Deprecated
    public static void setVersion(String version) {
    }

    static {
        WorldEditPrefixHandler.register("com.sk89q.worldedit");
        WorldEdit.getVersion();
        BundledBlockData.getInstance();
    }
}

