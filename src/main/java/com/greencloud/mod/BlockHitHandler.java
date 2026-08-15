package com.greencloud.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.security.SecureRandom;

public class BlockHitHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final SecureRandom random = new SecureRandom();

    public boolean enabled = true;
    public double chanceMin = 80.0;
    public double chanceMax = 100.0;
    public double delayMin = 0.0;
    public double delayMax = 50.0;
    public double holdMin = 40.0;
    public double holdMax = 150.0;

    private long blockTriggerTime = -1;
    private long releaseTriggerTime = -1;
    private boolean isBlockingActive = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!enabled || event.phase != TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null) return;

        handleTimings();

        if (mc.currentScreen != null || !Mouse.isButtonDown(0)) {
            if (blockTriggerTime != -1L) blockTriggerTime = -1;
            return;
        }

        if (shouldAttemptBlock()) {
            scheduleBlock();
        }
    }

    private void handleTimings() {
        long now = System.currentTimeMillis();

        if (blockTriggerTime != -1L && now >= blockTriggerTime) {
            activateBlock();
            blockTriggerTime = -1;

            double holdTime = (holdMax > holdMin) ? (holdMin + random.nextDouble() * (holdMax - holdMin)) : holdMin;
            releaseTriggerTime = now + (long) holdTime;
        }

        if (releaseTriggerTime != -1L && now >= releaseTriggerTime) {
            terminateBlock();
        }
    }

    private boolean shouldAttemptBlock() {
        EntityPlayer player = mc.thePlayer;
        if (player == null || player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemSword)) return false;
        if (getTargetEntity() == null) return false;
        if (player.swingProgressInt != 1) return false;

        double actualChance = (chanceMax > chanceMin) ? (chanceMin + random.nextDouble() * (chanceMax - chanceMin)) : chanceMin;
        return (random.nextDouble() * 100) <= actualChance;
    }

    private void scheduleBlock() {
        if (blockTriggerTime != -1L) return;
        double waitMs = (delayMax > delayMin) ? (delayMin + random.nextDouble() * (delayMax - delayMin)) : delayMin;
        blockTriggerTime = System.currentTimeMillis() + (long) waitMs;
    }

    private void activateBlock() {
        if (isBlockingActive) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        isBlockingActive = true;
    }

    public void terminateBlock() {
        if (!isBlockingActive) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        isBlockingActive = false;
        releaseTriggerTime = -1;
        blockTriggerTime = -1;
    }

    private EntityLivingBase getTargetEntity() {
        MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) return null;
        return (mop.entityHit instanceof EntityLivingBase && !mop.entityHit.isDead) ? (EntityLivingBase) mop.entityHit : null;
    }
          }
