package me.catmi.module.modules.hud;

import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import me.catmi.util.ColorUtil;
import me.catmi.util.MathUtil;
import me.catmi.util.render.RenderUtil3;
import me.catmi.util.world.EntityUtil;
import me.catmi.util.world.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HUD3
        extends Module {
    Setting.Boolean renderingUp;
    Setting.Boolean potionIcons;
    Setting.Boolean shadow;
    Setting.Boolean watermark;
    Setting.Boolean modeVer;
    Setting.Boolean arrayList;
    Setting.Boolean serverBrand;
    Setting.Boolean ping;
    Setting.Boolean tps;
    Setting.Boolean fps;
    Setting.Boolean coords;
    Setting.Boolean direction;
    Setting.Boolean speed;
    Setting.Boolean potions;
    Setting.Boolean textRadar;
    Setting.Boolean armor;
    Setting.Boolean percent;
    Setting.Boolean totems;
    Setting.Boolean greeter;
    Setting.Boolean time;
    Setting.Boolean lag;
    Setting.Boolean hitMarkers;
    Setting.Integer hudRed;
    Setting.Integer hudGreen;
    Setting.Integer hudBlue;
    Setting.Boolean grayNess;
    public Setting.Integer respondTime;


    @Override
    public void setup() {
        this.renderingUp = registerBoolean("RenderingUp","RenderingUp",false);
        this.potionIcons = registerBoolean("PotionIcons","PotionIcons",true);
        this.shadow = registerBoolean("Shadow","Shadow",true);
        this.watermark = registerBoolean("WaterMark","WaterMark",true);
        this.modeVer = registerBoolean("ModVer","ModVer",true);
        this.arrayList = registerBoolean("ArrayList","ArrayList",true);
        this.serverBrand = registerBoolean("ServerBrand","ServerBrand",true);
        this.ping = registerBoolean("Ping","Ping",true);
        this.tps = registerBoolean("Tps","Tps",true);
        this.fps = registerBoolean("Fps","Fps",true);
        this.coords = registerBoolean("Coords","Coords",true);
        this.direction = registerBoolean("Direction","Direction",true);
        this.speed = registerBoolean("Speed","Speed",true);
        this.potions = registerBoolean("Potions","Potions",true);
        this.textRadar = registerBoolean("TextRadar","TextRadar",false);
        this.armor = registerBoolean("Armor","Armor",true);
        this.percent = registerBoolean("Percent","Percent",true);
        this.totems = registerBoolean("Totems","Totems",true);
        this.greeter = registerBoolean("Greeter","Greeter",true);
        this.time = registerBoolean("Time","Time",false);
        this.lag = registerBoolean("Lag","Lag",true);
        this.hitMarkers = registerBoolean("HitMarkers","HitMarkers",true);
        this.hudRed = registerInteger("Red","Red",255,0,255);
        this.hudGreen = registerInteger("Green","Green",255,0,255);
        this.hudBlue = registerInteger("Blue","Blue",255,0,255);
        this.grayNess = registerBoolean("FutureSkid","FutureSkid",false);
        this.respondTime = registerInteger("ServerTime","ServerTime",500,0,1000);
    }
    private static HUD3 INSTANCE = new HUD3();
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    private final Timer timer = new Timer();

    public HUD3() {
        super("HUD3", Module.Category.HUD);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static HUD3 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUD3();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    public static boolean fullNullCheck() {
        return (mc.player == null || mc.world == null);
    }

    @Override
    public void onRender() {
        String text;
        Object text2;
        int j;
        int i;
        String fpsText;
        String text3;
        if (HUD3.fullNullCheck()) {
            return;
        }
        int width = Catmi.fontRenderer2.scaledWidth;
        int height = Catmi.fontRenderer2.scaledHeight;
        this.color = ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue());
        String grayString = this.grayNess.getValue() != false ? "\u00a77" : "";
        if (this.watermark.getValue()) {
                Catmi.fontRenderer2.drawString("Catmi" + (this.modeVer.getValue() != false ? " v1.2.5" : ""), 2.0f, 2.0f, this.color, true);
        }
        int n = this.renderingUp.getValue() != false ? 0 : (j = HUD.mc.currentScreen instanceof GuiChat ? 14 : 0);
        if (this.arrayList.getValue()) {
            Module module;
            if (this.renderingUp.getValue()) {
            } else {
            }
        }
        int n2 = i = HUD.mc.currentScreen instanceof GuiChat ? 14 : 0;
        if (this.renderingUp.getValue()) {
            if (this.serverBrand.getValue()) {
                text2 = grayString + "Server brand " + "\u00a7f" + Catmi.serverManager.getServerBrand();
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), height - 2 - (i += 10), this.color, true);
            }
            if (this.potions.getValue()) {
                for (PotionEffect effect : Catmi.potionManager.getOwnPotions()) {
                    text = Catmi.potionManager.getColoredPotionString(effect);
                    Catmi.fontRenderer2.drawString(text, width - (Catmi.fontRenderer2.getStringWidth(text) + 2), height - 2 - (i += 10), this.color, true);
                }
            }
            if (this.speed.getValue()) {
                text2 = grayString + "Speed " + "\u00a7f" + Catmi.speedManager.getSpeedKpH() + " km/h";
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), height - 2 - (i += 10), this.color, true);
            }
            if (this.time.getValue()) {
                text2 = grayString + "Time " + "\u00a7f" + new SimpleDateFormat("h:mm a").format(new Date());
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), height - 2 - (i += 10), this.color, true);
            }
            if (this.tps.getValue()) {
                text2 = grayString + "TPS " + "\u00a7f" + Catmi.serverManager.getTPS();
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), height - 2 - (i += 10), this.color, true);
            }
            fpsText = grayString + "FPS " + "\u00a7f" + Minecraft.debugFPS;
            text3 = grayString + "Ping " + "\u00a7f" + Catmi.serverManager.getPing();
            if (Catmi.fontRenderer2.getStringWidth(text3) > Catmi.fontRenderer2.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    Catmi.fontRenderer2.drawString(text3, width - (Catmi.fontRenderer2.getStringWidth(text3) + 2), height - 2 - (i += 10), this.color, true);
                }
                if (this.fps.getValue()) {
                    Catmi.fontRenderer2.drawString(fpsText, width - (Catmi.fontRenderer2.getStringWidth(fpsText) + 2), height - 2 - (i += 10), this.color, true);
                }
            } else {
                if (this.fps.getValue()) {
                    Catmi.fontRenderer2.drawString(fpsText, width - (Catmi.fontRenderer2.getStringWidth(fpsText) + 2), height - 2 - (i += 10), this.color, true);
                }
                if (this.ping.getValue()) {
                    Catmi.fontRenderer2.drawString(text3, width - (Catmi.fontRenderer2.getStringWidth(text3) + 2), height - 2 - (i += 10), this.color, true);
                }
            }
        } else {
            if (this.serverBrand.getValue()) {
                text2 = grayString + "Server brand " + "\u00a7r" + Catmi.serverManager.getServerBrand();
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), 2 + i++ * 10, this.color, true);
            }
            if (this.potions.getValue()) {
                for (PotionEffect effect : Catmi.potionManager.getOwnPotions()) {
                    text = Catmi.potionManager.getColoredPotionString(effect);
                    Catmi.fontRenderer2.drawString(text, width - (Catmi.fontRenderer2.getStringWidth(text) + 2), 2 + i++ * 10, this.color, true);
                }
            }
            if (this.speed.getValue()) {
                text2 = grayString + "Speed " + "\u00a7r" + Catmi.speedManager.getSpeedKpH() + " km/h";
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), 2 + i++ * 10, this.color, true);
            }
            if (this.time.getValue()) {
                text2 = grayString + "Time " + "\u00a7r" + new SimpleDateFormat("h:mm a").format(new Date());
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), 2 + i++ * 10, this.color, true);
            }
            if (this.tps.getValue()) {
                text2 = grayString + "TPS " + "\u00a7r" + Catmi.serverManager.getTPS();
                Catmi.fontRenderer2.drawString((String)text2, width - (Catmi.fontRenderer2.getStringWidth((String)text2) + 2), 2 + i++ * 10, this.color, true);
            }
            fpsText = grayString + "FPS " + "\u00a7r" + Minecraft.debugFPS;
            text3 = grayString + "Ping " + "\u00a7r" + Catmi.serverManager.getPing();
            if (Catmi.fontRenderer2.getStringWidth(text3) > Catmi.fontRenderer2.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    Catmi.fontRenderer2.drawString(text3, width - (Catmi.fontRenderer2.getStringWidth(text3) + 2), 2 + i++ * 10, this.color, true);
                }
                if (this.fps.getValue()) {
                    Catmi.fontRenderer2.drawString(fpsText, width - (Catmi.fontRenderer2.getStringWidth(fpsText) + 2), 2 + i++ * 10, this.color, true);
                }
            } else {
                if (this.fps.getValue()) {
                    Catmi.fontRenderer2.drawString(fpsText, width - (Catmi.fontRenderer2.getStringWidth(fpsText) + 2), 2 + i++ * 10, this.color, true);
                }
                if (this.ping.getValue()) {
                    Catmi.fontRenderer2.drawString(text3, width - (Catmi.fontRenderer2.getStringWidth(text3) + 2), 2 + i++ * 10, this.color, true);
                }
            }
        }
        boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        int posX = (int)HUD.mc.player.posX;
        int posY = (int)HUD.mc.player.posY;
        int posZ = (int)HUD.mc.player.posZ;
        float nether = !inHell ? 0.125f : 8.0f;
        int hposX = (int)(HUD.mc.player.posX * (double)nether);
        int hposZ = (int)(HUD.mc.player.posZ * (double)nether);
        if (this.renderingUp.getValue()) {
        } else {
        }
        i = HUD.mc.currentScreen instanceof GuiChat ? 14 : 0;
        String coordinates = "\u00a7rXYZ \u00a7f" + posX + ", " + posY + ", " + posZ + " " + "\u00a7r" + "[" + "\u00a7f" + hposX + ", " + hposZ + "\u00a7r" + "]";
        String text4 = (this.direction.getValue() != false ? Catmi.rotationManager.getDirection4D(false) + " " : "") + (this.coords.getValue() != false ? coordinates : "") + "";
        Catmi.fontRenderer2.drawString(text4, 2.0f, height - (i += 10), this.color, true);
        if (this.armor.getValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue()) {
            this.renderGreeter();
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }



    public void renderGreeter() {
        int width = Catmi.fontRenderer2.scaledWidth;
        String text = "";
        if (this.greeter.getValue()) {
                text = text + "Welcome " + HUD.mc.player.getDisplayNameString();
        }
        Catmi.fontRenderer2.drawString(text, (float)width / 2.0f - (float)Catmi.fontRenderer2.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
    }

    public void renderLag() {
        int width = Catmi.fontRenderer2.scaledWidth;
        if (Catmi.serverManager.isServerNotResponding()) {
            String text = (this.lag.getValue() ? "\u00a77" : "\u00a7c") + "Server not responding: " + MathUtil.round((float)Catmi.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            Catmi.fontRenderer2.drawString(text, (float)width / 2.0f - (float)Catmi.fontRenderer2.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    public void renderTotemHUD() {
        int width = Catmi.fontRenderer2.scaledWidth;
        int height = Catmi.fontRenderer2.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            boolean iteration = false;
            int y = height - 55 - (HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil3.itemRender.zLevel = 200.0f;
            RenderUtil3.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil3.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, totem, x, y, "");
            RenderUtil3.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            Catmi.fontRenderer2.drawStringWithShadow(totems + "", x + 19 - 2 - Catmi.fontRenderer2.getStringWidth(totems + ""), y + 9, 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public void renderArmorHUD(boolean percent) {
        int width = Catmi.fontRenderer2.scaledWidth;
        int height = Catmi.fontRenderer2.scaledHeight;
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil3.itemRender.zLevel = 200.0f;
            RenderUtil3.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil3.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
            RenderUtil3.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            Catmi.fontRenderer2.drawStringWithShadow(s, x + 19 - 2 - Catmi.fontRenderer2.getStringWidth(s), y + 9, 16777215);
            if (!percent) continue;
            int dmg = 0;
            int itemDurability = is.getMaxDamage() - is.getItemDamage();
            float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
            float red = 1.0f - green;
            dmg = percent ? 100 - (int)(red * 100.0f) : itemDurability;
            Catmi.fontRenderer2.drawStringWithShadow(dmg + "", x + 8 - Catmi.fontRenderer2.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    public void drawHitMarkers() {
    }



    public static enum WaterMark {
        NONE,
        PHOBOS,
        EARTH;

    }

    public static enum LagNotify {
        NONE,
        RED,
        GRAY;

    }

    public static enum Greeter {
        NONE,
        NAME,
        TIME,
        LONG,
        CUSTOM;

    }

}
