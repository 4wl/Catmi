package me.catmi.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    public static Vec3d getEyesPos() {
        return new Vec3d( Wrapper.getMinecraft().player.posX,  Wrapper.getMinecraft().player.posY +  Wrapper.getMinecraft().player.getEyeHeight(),  Wrapper.getMinecraft().player.posZ);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0D / Math.PI;
        yaw = yaw * 180.0D / Math.PI;
        yaw += 90.0D;
        return new double[] { yaw, pitch };
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new float[] {  Wrapper.getMinecraft().player.rotationYaw +
                MathHelper.wrapDegrees(yaw -  Wrapper.getMinecraft().player.rotationYaw),  Wrapper.getMinecraft().player.rotationPitch +
                MathHelper.wrapDegrees(pitch -  Wrapper.getMinecraft().player.rotationPitch) };
    }

    public static void faceYawAndPitch(float yaw, float pitch) {
        Wrapper.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch,  Wrapper.getMinecraft().player.onGround));
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = getLegitRotations(vec);
        Wrapper.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1],  Wrapper.getMinecraft().player.onGround));
    }

    public static void faceEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle( Wrapper.getMinecraft().player.getPositionEyes( Wrapper.getMinecraft().getRenderPartialTicks()), entity.getPositionEyes( Wrapper.getMinecraft().getRenderPartialTicks()));
        faceYawAndPitch(angle[0], angle[1]);
    }

    public static float[] getAngle(Entity entity) {
        return MathUtil.calcAngle( Wrapper.getMinecraft().player.getPositionEyes( Wrapper.getMinecraft().getRenderPartialTicks()), entity.getPositionEyes( Wrapper.getMinecraft().getRenderPartialTicks()));
    }

    public static int getDirection4D() {
        return MathHelper.floor(( Wrapper.getMinecraft().player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
    }

    public static String getDirection4D(boolean northRed) {
        int dirnumber = getDirection4D();
        if (dirnumber == 0)
            return "South (+Z)";
        if (dirnumber == 1)
            return "West (-X)";
        if (dirnumber == 2)
            return (northRed ? "\u00a7c" : "") + "North (-Z)";
        if (dirnumber == 3)
            return "East (+X)";
        return "Loading...";
    }
}
