package me.catmi.module.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import me.catmi.Catmi;
import me.catmi.mixin.misc.IMinecraft;
import me.catmi.module.Module;
import me.catmi.module.ModuleManager;
import me.catmi.settings.Setting;
import me.catmi.util.ColourHolder;
import me.catmi.util.RainbowUtil;
import me.catmi.util.TickRate;
import me.catmi.util.font.FontUtils;
import me.catmi.util.render.Animation;
import me.catmi.util.render.RenderUtil2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class  HUD2
        extends Module {
    int y;
    Setting.Boolean welcomer;
    Setting.Boolean server;
    Setting.Boolean ping;
    Setting.Boolean ArmorHud;
    Setting.Boolean fps;
    Setting.Boolean watermark;
    Setting.Boolean arraylistOutline;
    Setting.Boolean coordinates;
    Setting.Boolean tps;
    Setting.Boolean time1;
    Setting.Boolean arraylist;
    Setting.Integer animS;
    Setting.Integer rainbowSpeed;
    public static Setting.Boolean rainbow;
    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;
    static final RenderItem itemRender;
    Setting.Mode mode;
    String coords;
    final String time = new SimpleDateFormat("h:mm a").format(new Date());

    public HUD2() {
        super("HUD2", Module.Category.HUD);
        this.setDrawn(false);
    }

    @Override
    public void setup() {
        this.watermark = this.registerBoolean("Watermark", "Watermark", true);
        this.welcomer = this.registerBoolean("Welcomer", "Welcomer", true);
        this.server = this.registerBoolean("Server", "Server", true);
        this.ping = this.registerBoolean("Ping", "Ping", true);
        this.time1 = this.registerBoolean("Time", "Time", true);
        this.tps = this.registerBoolean("Tps", "Tps", true);
        this.fps = this.registerBoolean("Fps", "Fps", true);
        this.coordinates = this.registerBoolean("Coords", "Coords", true);
        this.ArmorHud = this.registerBoolean("ArmorHud", "ArmorHud", true);
        this.arraylist = this.registerBoolean("ArrayList", "ArrayList", true);
        ArrayList<String> modes = new ArrayList<String>();
        modes.add("Top");
        this.mode = this.registerMode("Mode", "Mode", modes, "Bottom");
        this.arraylistOutline = this.registerBoolean("ArraylistOutline", "ArraylistOutline", true);
        this.animS = this.registerInteger("AnimSpeed", "AnimSpeed", 1, 0, 10);
        red = this.registerInteger("Red", "Red", 255, 0, 255);
        green = this.registerInteger("Green", "Green", 255, 0, 255);
        blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.rainbowSpeed = this.registerInteger("RainbowSpeed", "RainbowSpeed", 1, 1, 25);
    }

    @Override
    public void onRender() {
        String text;
        int modCount = 0;
        int[] counter1 = new int[]{1};
        ScaledResolution resolution = new ScaledResolution(mc);
        if (this.arraylist.getValue()) {
            int[] counter = new int[]{1};
            ArrayList<Module> modules = new ArrayList<Module>(ModuleManager.getModules());
            modules.sort(Comparator.comparing(m -> -FontUtils.getStringWidth(HUD.customFont.getValue(), m.getName() + m.getHudInfo())));
            for (int i2 = 0; i2 < modules.size(); ++i2) {
                Module module = modules.get(i2);
                if (!module.isEnabled() || !module.isDrawn()) continue;
                int x = resolution.getScaledWidth();
                int y = 3 + modCount * 10;
                int lWidth = FontUtils.getStringWidth(HUD.customFont.getValue(), module.getName() + (Object)ChatFormatting.GRAY + module.getHudInfo());
                if (module.animPos < (float)lWidth && module.isEnabled()) {
                    module.animPos = Animation.moveTowards(module.animPos, lWidth + 1, 0.05f + (float)(this.animS.getValue() / 30), 0.5f);
                } else if (module.animPos > 1.5f && !module.isEnabled()) {
                    module.animPos = Animation.moveTowards(module.animPos, -1.5f, 0.05f + (float)(this.animS.getValue() / 30), 0.5f);
                } else if (module.animPos <= 1.5f && !module.isEnabled()) {
                    module.animPos = -1.0f;
                }
                if (module.animPos > (float)lWidth && module.isEnabled()) {
                    module.animPos = lWidth;
                }
                x = (int)((float)x - module.animPos);
                if (this.arraylistOutline.getValue()) {
                    RenderUtil2.drawBorderedRect(x - 6, y - 3, resolution.getScaledWidth(), y + FontUtils.getFontHeight(false), 1.0, new Color(20, 20, 20, 200).getRGB(), 0);
                    RenderUtil2.drawBorderedRect(x - 3, y - 3, resolution.getScaledWidth(),y+FontUtils.getFontHeight(false),1.0,new Color(20,20,20,200).getRGB(),0);
                    RenderUtil2.drawGradient(resolution.getScaledWidth() - 2, y - 4, resolution.getScaledWidth(), y + FontUtils.getFontHeight(false), rainbow.getValue() ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB(), rainbow.getValue() ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
                }
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), module.getName() + (Object)ChatFormatting.GRAY + module.getHudInfo(), x - 3, y, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
                ++modCount;
                counter[0] = counter[0] + 1;
            }
        }
        if (this.ArmorHud.getValue()) {
            GlStateManager.enableTexture2D();
            int i3 = resolution.getScaledWidth() / 2;
            int iteration = 0;
            int y = resolution.getScaledHeight() - 55 - (HUD2.mc.player.isInWater() ? 10 : 0);
            for (ItemStack is : HUD2.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) continue;
                int x = i3 - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                HUD2.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(HUD2.mc.fontRenderer, is, x, y, "");
                HUD2.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), s, x + 19 - 2 - Catmi.fontRenderer.getStringWidth(s), y + 9, 16777215);
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), dmg + "", x + 8 - Catmi.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        int posY = resolution.getScaledHeight() - this.y - Catmi.fontRenderer.getHeight() + 10;
        int posY2 = 2;
        int x2 = resolution.getScaledWidth();
        if (this.watermark.getValue()) {
            if (this.watermark.getValue()) {
                text = Catmi.MODNAME + " " + "1.2.4";
                FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), text, 2, posY2, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
            }
        }
        if (this.server.getValue() && HUD2.mc.player != null && !mc.isSingleplayer()) {
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), "\u00a7rServer ip \u00a7f" + (HUD2.mc).getCurrentServerData().serverIP + "", x2-35  , posY, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());

            counter1[0] = counter1[0] + 1;
            posY -= 10;
        }
        if (this.ping.getValue()) {
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), "\u00a7rPing \u00a7f" + this.getPing() + "", x2-40, posY, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
            counter1[0] = counter1[0] + 1;
            posY -= 10;
        }
        if (this.time1.getValue()) {
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), this.time, 2, 10, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
        }
        if (this.tps.getValue()) {
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), "\u00a7rTps \u00a7f" + TickRate.TPS + "", x2-40, posY, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
            counter1[0] = counter1[0] + 1;
            posY -= 10;
        }
        if (this.fps.getValue()) {
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), "\u00a7rFps \u00a7f" + Minecraft.getDebugFPS() + "", x2-32, posY, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
            counter1[0] = counter1[0] + 1;
        }
        if (this.coordinates.getValue()) {
            this.y = HUD2.mc.currentScreen instanceof GuiChat ? 15 : 2;
            this.coords = HUD2.mc.player.dimension == -1 ? (Object)ChatFormatting.GRAY + "Xyz " + (Object)ChatFormatting.WHITE + HUD2.mc.player.getPosition().getX() + ", " + HUD2.mc.player.getPosition().getY() + ", " + HUD2.mc.player.getPosition().getZ() + (Object)ChatFormatting.GRAY + " [" + (Object)ChatFormatting.WHITE + HUD2.mc.player.getPosition().getX() * 8 + ", " + HUD2.mc.player.getPosition().getZ() * 8 + (Object)ChatFormatting.GRAY + "]" : (Object)ChatFormatting.GRAY + "XYZ " + (Object)ChatFormatting.WHITE + HUD2.mc.player.getPosition().getX() + ", " + HUD2.mc.player.getPosition().getY() + ", " + Math.floor(HUD2.mc.player.getPosition().getZ()) + (Object)ChatFormatting.GRAY + " [" + (Object)ChatFormatting.WHITE + HUD2.mc.player.getPosition().getX() / 8 + ", " + HUD2.mc.player.getPosition().getZ() / 8 + (Object)ChatFormatting.GRAY + "]";
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), this.coords, 0, resolution.getScaledHeight() - this.y - Catmi.fontRenderer.getHeight(), new Color(255, 255, 255, 255).getRGB());
        }
        if (this.welcomer.getValue()) {
            text = "Welcome " + HUD2.mc.player.getName() + " You Looks Fat Today :^)";
            this.drawCentredString(text, resolution.getScaledWidth() / 2, rainbow.getValue() ? RainbowUtil.rainbow(counter1[0] * RainbowOffset.offset.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB());
            counter1[0] = counter1[0] + 1;
        }
    }

    public int getPing() {
        int p;
        if (HUD2.mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(HUD2.mc.player.getName()) == null) {
            p = -1;
        } else {
            HUD2.mc.player.getName();
            p = Objects.requireNonNull(mc.getConnection().getPlayerInfo(HUD2.mc.player.getName())).getResponseTime();
        }
        return p;
    }

    private void drawCentredString(String text, int x, int color) {
        FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), text, x - FontUtils.getStringWidth(HUD.customFont.getValue(), text) / 2, 2, color);
    }

    static {
        itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
