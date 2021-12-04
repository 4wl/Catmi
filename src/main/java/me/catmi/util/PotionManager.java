package me.catmi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.catmi.module.ModuleManager;
import me.catmi.module.modules.hud.HUD3;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionManager {
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<>();

    public void onLogout() {
        this.potions.clear();
    }

    public void updatePlayer() {
        PotionList list = new PotionList();
        for (PotionEffect effect : Wrapper.getMinecraft().player.getActivePotionEffects())
            list.addEffect(effect);
        this.potions.put(Wrapper.getMinecraft().player, list);
    }

    public void update() {
        updatePlayer();
        if (ModuleManager.isModuleEnabled("HUD3")) {
            ArrayList<EntityPlayer> removeList = new ArrayList<>();
            for (Map.Entry<EntityPlayer, PotionList> potionEntry : this.potions.entrySet()) {
                boolean notFound = true;
                for (EntityPlayer player : Wrapper.getMinecraft().world.playerEntities) {
                    if (this.potions.get(player) == null) {
                        PotionList list = new PotionList();
                        for (PotionEffect effect : player.getActivePotionEffects())
                            list.addEffect(effect);
                        this.potions.put(player, list);
                        notFound = false;
                    }
                    if (((EntityPlayer)potionEntry.getKey()).equals(player))
                        notFound = false;
                }
                if (notFound)
                    removeList.add(potionEntry.getKey());
            }
            for (EntityPlayer player : removeList)
                this.potions.remove(player);
        }
    }

    public List<PotionEffect> getOwnPotions() {
        return getPlayerPotions((EntityPlayer)Wrapper.getMinecraft().player);
    }

    public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
        PotionList list = this.potions.get(player);
        List<PotionEffect> potions = new ArrayList<>();
        if (list != null)
            potions = list.getEffects();
        return potions;
    }

    public void onTotemPop(EntityPlayer player) {
        PotionList list = new PotionList();
        this.potions.put(player, list);
    }

    public PotionEffect[] getImportantPotions(EntityPlayer player) {
        PotionEffect[] array = new PotionEffect[3];
        for (PotionEffect effect : getPlayerPotions(player)) {
            Potion potion = effect.getPotion();
            switch (I18n.format(potion.getName(), new Object[0]).toLowerCase()) {
                case "strength":
                    array[0] = effect;
                case "weakness":
                    array[1] = effect;
                case "speed":
                    array[2] = effect;
            }
        }
        return array;
    }

    public String getPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        return I18n.format((String)potion.getName(), (Object[])new Object[0]) + " " + (effect.getAmplifier() + 1) + " " + "\u00a7f" + Potion.getPotionDurationString((PotionEffect)effect, (float)1.0f);
    }

    public String getColoredPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        switch (I18n.format((String)potion.getName(), (Object[])new Object[0])) {
            case "Jump Boost":
            case "Speed": {
                return "\u00a7b" + this.getPotionString(effect);
            }
            case "Resistance":
            case "Strength": {
                return "\u00a7c" + this.getPotionString(effect);
            }
            case "Wither":
            case "Slowness":
            case "Weakness": {
                return "\u00a70" + this.getPotionString(effect);
            }
            case "Absorption": {
                return "\u00a79" + this.getPotionString(effect);
            }
            case "Haste":
            case "Fire Resistance": {
                return "\u00a76" + this.getPotionString(effect);
            }
            case "Regeneration": {
                return "\u00a7d" + this.getPotionString(effect);
            }
            case "Night Vision":
            case "Poison": {
                return "\u00a7a" + this.getPotionString(effect);
            }
        }
        return "\u00a7f" + this.getPotionString(effect);
    }



    public static class PotionList {
        private List<PotionEffect> effects = new ArrayList<>();

        public void addEffect(PotionEffect effect) {
            if (effect != null)
                this.effects.add(effect);
        }

        public List<PotionEffect> getEffects() {
            return this.effects;
        }
    }
}
