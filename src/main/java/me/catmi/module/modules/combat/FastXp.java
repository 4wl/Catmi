package me.catmi.module.modules.combat;

import me.catmi.module.Module;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class FastXp extends Module {

    public FastXp() {

        super("FastXp", Category.Combat);
    }

    public void onUpdata() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 1) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
            mc.player.stopActiveHand();

        }
    }
}
