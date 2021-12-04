package me.catmi.module.modules.movement;

import it.unimi.dsi.fastutil.bytes.AbstractByte2BooleanMap;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.TupleIntJsonSerializable;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LongJump extends Module {

    private Setting.Boolean jumped;
    private Setting.Boolean boostable;
    private Setting.Integer speed;
    private Setting.Boolean packet;


    public LongJump(){super("LongJump",Category.Movement);}

public void setup(){

        jumped = registerBoolean("Jumped","Jumped",30,1,100);
        speed = registerInteger("Speed","Speed",30,1,100);


    }

    private Setting.Boolean registerBoolean(String jumped, String jumped1, int i, int i1, int i2) {

    return null;
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;


        {
            if (mc.player.onGround || mc.player.capabilities.isFlying)
            {


                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;

                AbstractByte2BooleanMap.BasicEntry packet = null;
                if (packet.getBooleanValue())
                {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                }

                return;
            }

            if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f)) return;
            double yaw = getDirection();


        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event)
    {
        if (mc.player == null || mc.world == null) return;


        {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            event.setX(0);
            event.setY(0);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event)
    {
        if ((mc.player != null && mc.world != null) && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f))
        {

        }
    }

    private double getDirection()
    {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f) rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f) forward = -0.5f;
        else if (mc.player.moveForward > 0f) forward = 0.5f;

        if (mc.player.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (mc.player.moveStrafing < 0f) rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
    }
}
