package uhc.world.kongtai;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

public class CommandUHCGen extends CommandBase {

    @Override
    public String getCommandName() {
        return "uhcgen";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/uhcgen tp [维度ID] | /uhcgen scan | /uhcgen info";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("§c用法: /uhcgen tp [维度ID] | /uhcgen scan | /uhcgen info"));
            return;
        }

        // ================= 1. 传送功能 =================
        if (args[0].equalsIgnoreCase("tp")) {
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                int targetDim = 100; // 默认传送到 100（平原）
                
                // 如果输入了维度参数，比如 /uhcgen tp 110
                if (args.length > 1) {
                    try {
                        targetDim = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(new ChatComponentText("§c维度格式错误！用法: /uhcgen tp 101"));
                        return;
                    }
                }
                
                if (player.dimension != targetDim) {
                    WorldServer uhcWorld = player.mcServer.worldServerForDimension(targetDim);
                    if (uhcWorld == null) {
                        sender.addChatMessage(new ChatComponentText("§c[UHC] 目标维度未加载或不存在！"));
                        return;
                    }
                    
                    // 预加载出生点附近的 5x5 区块，防止传送过去掉入虚空
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            uhcWorld.getChunkProvider().provideChunk(x, z);
                        }
                    }
                    
                    int highestY = uhcWorld.getTopSolidOrLiquidBlock(0, 0);
                    final double safeY = highestY + 1.0;
                    final int finalTargetDim = targetDim;

                    player.mcServer.getConfigurationManager()
                        .transferPlayerToDimension(player, finalTargetDim, new net.minecraft.world.Teleporter(uhcWorld) {
                            @Override
                            public void placeInPortal(net.minecraft.entity.Entity entity, double x, double y, double z, float yaw) {
                                // 强制在 0,0 生成
                                entity.setLocationAndAngles(0.5, safeY, 0.5, entity.rotationYaw, entity.rotationPitch);
                                entity.motionX = 0;
                                entity.motionY = 0;
                                entity.motionZ = 0;
                            }
                        });
                    player.setPositionAndUpdate(0.5, safeY, 0.5);
                    sender.addChatMessage(new ChatComponentText("§a[UHC] 已安全传送到 UHC 造图维度 [" + finalTargetDim + "]"));
                } else {
                    // 如果已经在目标维度，直接重置回 0,0
                    int highestY = player.worldObj.getTopSolidOrLiquidBlock(0, 0);
                    player.setPositionAndUpdate(0.5, highestY + 1, 0.5);
                    sender.addChatMessage(new ChatComponentText("§a[UHC] 已重置到当前维度中心点！"));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("§c只有玩家才能执行传送！"));
            }
            return;
        }

        // ================= 2. 维度列表提示 =================
        if (args[0].equalsIgnoreCase("info")) {
            sender.addChatMessage(new ChatComponentText("§e==== UHC 维度对照表 ===="));
            sender.addChatMessage(new ChatComponentText("§b100: 平原 §7| §a101: 森林 §7| §2102: 黑森林"));
            sender.addChatMessage(new ChatComponentText("§e103: 沙漠 §7| §c104: 平顶山 §7| §6105: 热带草原"));
            sender.addChatMessage(new ChatComponentText("§d106: 蘑菇岛 §7| §8107: 极端山丘 §7| §2108: 丛林"));
            sender.addChatMessage(new ChatComponentText("§2109: 沼泽 §7| §f110: 冰原/雪地 §7| §3111: 冷针叶林"));
            sender.addChatMessage(new ChatComponentText("§d112: 花海"));
            sender.addChatMessage(new ChatComponentText("§a用法: /uhcgen tp <ID> 快速传送"));
            return;
        }

        // ================= 3. 扫描矿物 =================
        if (args[0].equalsIgnoreCase("scan")) {
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                WorldServer world = (WorldServer) player.worldObj;
                if (world == null) return;

                net.minecraft.world.chunk.Chunk chunk = world
                    .getChunkFromBlockCoords((int) player.posX, (int) player.posZ);
                int coal = 0, iron = 0, gold = 0, diamond = 0, lapis = 0, redstone = 0;
                int emerald = 0;

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

                String worldName = world.provider.getDimensionName();
                sender.addChatMessage(new ChatComponentText("§e==== 区块矿物扫描 [" + worldName + "] ===="));
                sender.addChatMessage(new ChatComponentText("§7煤: §f" + coal + " | §7铁: §f" + iron + " | §7红石: §f" + redstone));
                sender.addChatMessage(new ChatComponentText("§7金: §f" + gold + " | §7青金石: §f" + lapis + " | §c钻石: §f" + diamond + " | §a绿宝石: §f" + emerald));
            }
            return;
        }

        // 如果输入了不认识的指令
        sender.addChatMessage(new ChatComponentText("§c未知指令！用法: /uhcgen tp [维度ID] | /uhcgen scan | /uhcgen info"));
    }
}