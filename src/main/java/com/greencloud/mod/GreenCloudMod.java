package com.greencloud.mod;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = GreenCloudMod.MODID, name = GreenCloudMod.NAME, version = GreenCloudMod.VERSION, clientSideOnly = true)
public class GreenCloudMod {
    public static final String MODID = "gcblockhit";
    public static final String NAME = "GC BlockHit";
    public static final String VERSION = "1.0";

    public static BlockHitHandler blockHitHandler = new BlockHitHandler();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(blockHitHandler);
        ClientCommandHandler.instance.registerCommand(new BlockHitCommand());
    }
}
