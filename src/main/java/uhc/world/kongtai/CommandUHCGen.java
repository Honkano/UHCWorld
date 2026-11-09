package uhc.world.kongtai;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.LinkedList;
import java.util.Queue;

public class CommandUHCGen extends CommandBase {

    private static Queue<int[]> chunkQueue = new LinkedList<>();
    private static boolean isRunning = false;
    private static int totalChunks = 0;
    private static int currentChunks = 0;
    private static int tickCounter = 0;

    // 每 tick 加载 5 个，防止客户端爆炸
    private static final int CHUNKS_PER_TICK = 5;

    @Override
    public String getCommandName() { return "uhcgen"; }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/uhcgen tp | /uhcgen <半径> | /uhcgen scan | /uhcgen stop | /uhcgen status";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("§c用法: /uhcgen tp | /uhcgen 5000 | /uhcgen scan | /uhcgen stop | /uhcgen status"));
            return;
        }

        if (args[0].equalsIgnoreCase("tp")) {
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                if (player.dimension != 100) {
                    WorldServer uhcWorld = player.mcServer.worldServerForDimension(100);
                    if (uhcWorld == null) {
                        sender.addChatMessage(new ChatComponentText("§c[UHC] 维度未加载！"));
                        return;
                    }
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            uhcWorld.getChunkProvider().provideChunk(x, z);
                        }
                    }
                    int highestY = uhcWorld.getTopSolidOrLiquidBlock(0, 0);
                    final double safeY = highestY + 1.0;

                    player.mcServer.getConfigurationManager().transferPlayerToDimension(
                        player, 100, new net.minecraft.world.Teleporter(uhcWorld) {
                            @Override
                            public void placeInPortal(net.minecraft.entity.Entity entity, double x, double y, double z, float yaw) {
                                entity.setLocationAndAngles(0.5, safeY, 0.5, entity.rotationYaw, entity.rotationPitch);
                                entity.motionX = 0; entity.motionY = 0; entity.motionZ = 0;
                            }
                        }
                    );
                    player.setPositionAndUpdate(0.5, safeY, 0.5);
                    player.addChatMessage(new ChatComponentText("§a[UHC] 已安全传送到 UHC 造图维度！"));
                } else {
                    int highestY = player.worldObj.getTopSolidOrLiquidBlock(0, 0);
                    player.setPositionAndUpdate(0.5, highestY + 1, 0.5);
                    sender.addChatMessage(new ChatComponentText("§a[UHC] 已重置到 UHC 地面！"));
                }
            }
            return;
        }

        if (args[0].equalsIgnoreCase("status")) {
            if (isRunning) {
                float progress = (float) currentChunks / totalChunks;
                sender.addChatMessage(new ChatComponentText("§e[UHC] 跑图进行中！"));
                sender.addChatMessage(new ChatComponentText("§e进度: §f" + currentChunks + " / " + totalChunks + " §7(" + (int)(progress * 100) + "%)"));
            } else {
                sender.addChatMessage(new ChatComponentText("§e[UHC] 当前无跑图任务。"));
            }
            return;
        }

        // 🎯 2. 扫描脚下区块矿物（全维度通用版）
        if (args[0].equalsIgnoreCase("scan")) {
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                
                // ⭐ 核心修改：直接获取玩家当前所在的世界，不再限制维度 100
                WorldServer world = (WorldServer) player.worldObj;
                if (world == null) return;

                net.minecraft.world.chunk.Chunk chunk = world.getChunkFromBlockCoords((int) player.posX, (int) player.posZ);
                int coal = 0, iron = 0, gold = 0, diamond = 0, lapis = 0, redstone = 0;
                int emerald = 0; // 顺便把绿宝石也加上，满足你的好奇心

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            Block block = chunk.getBlock(x, y, z);
                            if (block == Blocks.coal_ore) coal++;
                            else if (block == Blocks.iron_ore) iron++;
                            else if (block == Blocks.gold_ore) gold++;
                            else if (block == Blocks.diamond_ore) diamond++;
                            else if (block == Blocks.lapis_ore) lapis++;
                            else if (block == Blocks.redstone_ore) redstone++;
                            else if (block == Blocks.emerald_ore) emerald++;
                        }
                    }
                }
                
                // 获取世界名字，方便区分
                String worldName = world.provider.getDimensionName();
                
                sender.addChatMessage(new ChatComponentText("§e==== 区块矿物扫描 [" + worldName + "] ===="));
                sender.addChatMessage(new ChatComponentText("§7煤: §f" + coal + " | §7铁: §f" + iron + " | §7红石: §f" + redstone));
                sender.addChatMessage(new ChatComponentText("§7金: §f" + gold + " | §7青金石: §f" + lapis + " | §c钻石: §f" + diamond + " | §a绿宝石: §f" + emerald));
            }
            return;
        }
        
        if (args[0].equalsIgnoreCase("stop")) {
            if (isRunning) {
                isRunning = false;
                chunkQueue.clear();
                sender.addChatMessage(new ChatComponentText("§c[UHC] 已强制终止跑图任务。"));
                printLog("§c[UHC] 跑图已被人为终止，共 " + currentChunks + " 个区块");
            }
            return;
        }

        try {
            int radius = Integer.parseInt(args[0]);
            startPreGen(sender, radius);
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("§c请输入正确的数字半径！"));
        }
    }

    public static void startPreGen(ICommandSender sender, int radius) {
        int minChunk = (-radius) >> 4;
        int maxChunk = radius >> 4;
        chunkQueue.clear();
        for (int x = minChunk; x <= maxChunk; x++) {
            for (int z = minChunk; z <= maxChunk; z++) {
                chunkQueue.add(new int[]{x, z});
            }
        }
        totalChunks = chunkQueue.size();
        currentChunks = 0;
        isRunning = true;
        tickCounter = 0;

        if (sender != null) sender.addChatMessage(new ChatComponentText("§a[UHC] 开始预加载，区块数: " + totalChunks));
        printLog("§a[UHC] ===== 跑图任务启动 =====");
        printLog("§a[UHC] 目标区块: " + totalChunks + " | 每tick: " + CHUNKS_PER_TICK);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isRunning) return;

        WorldServer world = net.minecraftforge.common.DimensionManager.getWorld(100);
        if (world == null) {
            isRunning = false;
            printLog("§c[UHC] 维度丢失，跑图终止！");
            return;
        }

        int processed = 0;
        try {
            while (processed < CHUNKS_PER_TICK && !chunkQueue.isEmpty()) {
                int[] coords = chunkQueue.poll();
                // ⭐ 核心：强制生成区块，绝不返回 null
                world.getChunkProvider().provideChunk(coords[0], coords[1]);
                processed++;
                currentChunks++;
            }
        } catch (Exception e) {
            printLog("§c[UHC] 跑图异常: " + e.getMessage());
            e.printStackTrace();
            isRunning = false;
            return;
        }

        tickCounter++;

        // ⭐ 每 20 tick（1 秒）刷新一次进度条，绝对肉眼可见！
        if (tickCounter % 20 == 0 || chunkQueue.isEmpty()) {
            float progress = (float) currentChunks / totalChunks;
            int percent = (int) (progress * 100);
            int filled = percent / 5;
            StringBuilder bar = new StringBuilder("§e[UHC] [");
            for (int i = 0; i < 20; i++) {
                bar.append(i < filled ? "§a█" : "§7░");
            }
            bar.append("§e] §f").append(percent).append("% §7(").append(currentChunks).append("/").append(totalChunks).append(")");
            printLog(bar.toString());
        }

        if (chunkQueue.isEmpty()) {
            isRunning = false;
            printLog("§a[UHC] ========================");
            printLog("§a[UHC] ✔ 预加载完成！区块总数: " + totalChunks);
            printLog("§a[UHC] ========================");
            if (MinecraftServer.getServer() != null) {
                MinecraftServer.getServer().getConfigurationManager()
                    .sendChatMsg(new ChatComponentText("§a[UHC] ✔ 跑图完成！共 " + totalChunks + " 个区块"));
            }
        }
    }

    // ⭐ 双管齐下输出：FMLLog 输出到日志，System.out 输出到控制台
    private static void printLog(String msg) {
        String cleanMsg = msg.replaceAll("§.", "");
        FMLLog.info("[UHC] " + cleanMsg);
        System.out.println("[UHC] " + cleanMsg);
    }
}