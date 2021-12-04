package me.catmi.module.modules.hud;

import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.module.ModuleManager;
import me.catmi.settings.Setting;
import me.catmi.util.Wrapper;
import me.catmi.util.font.FontUtils;
import me.catmi.util.font.OGCFONT;

import java.awt.*;

public class ArmorWarning extends Module {
    public ArmorWarning() {
        super("DurabiltyWarning", Category.HUD);
        setDrawn(false);
    }

    private Setting.Boolean rainbow;
    Setting.Integer x;
    Setting.Integer y;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    Setting.Integer threshold;
    ClickGuiModule mod = ((ClickGuiModule) ModuleManager.getModuleByName("ClickGui"));


    Color c;

    public void setup() {
        threshold = this.registerInteger("Percent", "Percent",50, 0, 100);
        x = this.registerInteger("X", "X",255, 0, 960);
        y = this.registerInteger("Y", "Y",255, 0, 530);
        red = this.registerInteger("Red","Red", 255, 0, 255);
        green = this.registerInteger("Green", "Green",255, 0, 255);
        blue = this.registerInteger("Blue", "Blue",255, 0, 255);
        rainbow = this.registerBoolean("Rainbow", "Rainbow",false);


    }

    @Override
    public void onRender() {
        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
        int rgb = Color.HSBtoRGB(hue[0], 1, 1);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        if (this.shouldMend(0) || this.shouldMend(1) || this.shouldMend(2) || this.shouldMend(3)) {
            final String text = "Armor Durability Is Below " + this.threshold.getValue() + "%";
            final int divider = getScale();
            if (rainbow.getValue()) {
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), text, x.getValue(),
                        y.getValue(), new Color(r, g, b).getRGB());
            } else {
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), text, x.getValue(),
                        y.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());

            }
        }
    }

    private boolean shouldMend(final int i) {
        return ArmorWarning.mc.player.inventory.armorInventory.get(i).getMaxDamage()
                != 0 && 100 * ArmorWarning.mc.player.inventory.armorInventory.get(i).getItemDamage()
                / ArmorWarning.mc.player.inventory.armorInventory.get(i).getMaxDamage()
                > reverseNumber(this.threshold.getValue(), 1, 100);
    }

    public static int reverseNumber(final int num, final int min, final int max) {
        return max + min - num;
    }

    public static int getScale() {
        int scaleFactor = 0;
        int scale = Wrapper.getMinecraft().gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        while (scaleFactor < scale && Wrapper.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (scaleFactor == 0) {
            scaleFactor = 1;
        }
        return scaleFactor;
    }
}