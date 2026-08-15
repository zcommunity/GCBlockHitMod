package com.greencloud.mod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class BlockHitCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "blockhit";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/blockhit [toggle | chance <min> <max> | delay <min> <max> | hold <min> <max>]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        BlockHitHandler handler = GreenCloudMod.blockHitHandler;

        if (args.length == 0) {
            sendMessage(sender, EnumChatFormatting.GOLD + "=== GC BlockHit Settings ===");
            sendMessage(sender, EnumChatFormatting.YELLOW + "Estado: " + (handler.enabled ? EnumChatFormatting.GREEN + "Activado" : EnumChatFormatting.RED + "Desactivado"));
            sendMessage(sender, EnumChatFormatting.YELLOW + "Chance: " + handler.chanceMin + " - " + handler.chanceMax);
            sendMessage(sender, EnumChatFormatting.YELLOW + "Delay: " + handler.delayMin + " - " + handler.delayMax);
            sendMessage(sender, EnumChatFormatting.YELLOW + "Hold: " + handler.holdMin + " - " + handler.holdMax);
            return;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("toggle")) {
            handler.enabled = !handler.enabled;
            if (!handler.enabled) handler.terminateBlock();
            sendMessage(sender, EnumChatFormatting.GREEN + "BlockHit " + (handler.enabled ? "activado." : "desactivado."));
        } else if (sub.equals("chance") && args.length >= 3) {
            handler.chanceMin = Double.parseDouble(args[1]);
            handler.chanceMax = Double.parseDouble(args[2]);
            sendMessage(sender, EnumChatFormatting.GREEN + "Chance: " + handler.chanceMin + " - " + handler.chanceMax);
        } else if (sub.equals("delay") && args.length >= 3) {
            handler.delayMin = Double.parseDouble(args[1]);
            handler.delayMax = Double.parseDouble(args[2]);
            sendMessage(sender, EnumChatFormatting.GREEN + "Delay: " + handler.delayMin + " - " + handler.delayMax);
        } else if (sub.equals("hold") && args.length >= 3) {
            handler.holdMin = Double.parseDouble(args[1]);
            handler.holdMax = Double.parseDouble(args[2]);
            sendMessage(sender, EnumChatFormatting.GREEN + "Hold: " + handler.holdMin + " - " + handler.holdMax);
        } else {
            sendMessage(sender, EnumChatFormatting.RED + "Uso: " + getCommandUsage(sender));
        }
    }

    private void sendMessage(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(text));
    }
                                                  }
