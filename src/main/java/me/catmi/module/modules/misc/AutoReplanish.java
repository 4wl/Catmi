package me.catmi.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import javafx.util.Pair;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AutoReplanish extends Module {
    public AutoReplanish(){super("AutoReplanish", Category.Misc);
        this.delayStep = 0;}
    Setting.Integer threshold;
    Setting.Integer tickDelay;
    private int delayStep;

    public void setup() {
        threshold = registerInteger("Threshold","Threshold",4,0,20);
        tickDelay = registerInteger("tickDelay","tickDelay",4,0,20);

    }

    private static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 35);
    }

    private static Map<Integer, ItemStack> getHotbar() {
        return getInventorySlots(36, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)AutoReplanish.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }

    @Override
    public void onUpdate() {
        if (AutoReplanish.mc.player == null) {
            return;
        }
        if (AutoReplanish.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.delayStep < this.tickDelay.getValue()) {
            ++this.delayStep;
            return;
        }
        this.delayStep = 0;
        final Pair<Integer, Integer> slots = this.findReplenishableHotbarSlot();
        if (slots == null) {
            return;
        }
        final int inventorySlot = slots.getKey();
        final int hotbarSlot = slots.getValue();
        AutoReplanish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
        AutoReplanish.mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
        AutoReplanish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
    }

    private Pair<Integer, Integer> findReplenishableHotbarSlot() {
        Pair<Integer, Integer> returnPair = null;
        for (final Map.Entry<Integer, ItemStack> hotbarSlot : getHotbar().entrySet()) {
            final ItemStack stack = hotbarSlot.getValue();
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.AIR) {
                    continue;
                }
                if (!stack.isStackable()) {
                    continue;
                }
                if (stack.getCount() >= stack.getMaxStackSize()) {
                    continue;
                }
                if (stack.getCount() > this.threshold.getValue()) {
                    continue;
                }
                final int inventorySlot = this.findCompatibleInventorySlot(stack);
                if (inventorySlot == -1) {
                    continue;
                }
                returnPair = new Pair<Integer, Integer>(inventorySlot, hotbarSlot.getKey());
            }
        }
        return returnPair;
    }

    private int findCompatibleInventorySlot(final ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (final Map.Entry<Integer, ItemStack> entry : getInventory().entrySet()) {
            final ItemStack inventoryStack = entry.getValue();
            if (!inventoryStack.isEmpty()) {
                if (inventoryStack.getItem() == Items.AIR) {
                    continue;
                }
                if (!this.isCompatibleStacks(hotbarStack, inventoryStack)) {
                    continue;
                }
                final int currentStackSize = ((ItemStack)AutoReplanish.mc.player.inventoryContainer.getInventory().get((int)entry.getKey())).getCount();
                if (smallestStackSize <= currentStackSize) {
                    continue;
                }
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }
        return inventorySlot;
    }

    private boolean isCompatibleStacks(final ItemStack stack1, final ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof ItemBlock && stack2.getItem() instanceof ItemBlock) {
            final Block block1 = ((ItemBlock)stack1.getItem()).getBlock();
            ((ItemBlock)stack2.getItem()).getBlock();
        }
        return stack1.getDisplayName().equals(stack2.getDisplayName()) && stack1.getItemDamage() == stack2.getItemDamage();
    }

    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + "NCP" + ChatFormatting.GRAY + "]";
    }
}