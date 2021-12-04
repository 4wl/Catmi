package me.catmi.module.modules.misc;

import com.mojang.authlib.GameProfile;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayer
        extends Module {
    private List<Integer> fakePlayerIdList = null;
    Setting.Mode mode;
    private static final String[][] fakePlayerInfo = new String[][]{{"66666666-6666-6666-6666-666666666600", "nigga0", "0", "0"}};

    public FakePlayer() {
        super("FakePlayer", Category.Misc);
    }

    @Override
    public void setup() {
        ArrayList<String> modes = new ArrayList<String>();
        modes.add("Single");
        modes.add("Multi");
        this.mode = this.registerMode("Mode", "Mode", modes, "Single");
    }

    @Override
    protected void onEnable() {
        if (FakePlayer.mc.player == null || FakePlayer.mc.world == null) {
            this.disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        int entityId = -101;
        for (String[] data : fakePlayerInfo) {
            if (this.mode.getValue().equals("Single")) {
                this.addFakePlayer(data[0], data[1], entityId, 0, 0);
            }
            if (this.mode.getValue().equals("Multi")) {
                this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            }
            --entityId;
        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString(uuid), name));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        fakePlayer.posX += (double)offsetX;
        fakePlayer.posZ += (double)offsetZ;
        FakePlayer.mc.world.addEntityToWorld(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }

    @Override
    public void onUpdate() {
        if (this.fakePlayerIdList == null || this.fakePlayerIdList.isEmpty()) {
            this.disable();
        }
    }

    /*
     * Could not resolve type clashes
     */
    @Override
    protected void onDisable() {
        if (FakePlayer.mc.player == null || FakePlayer.mc.world == null) {
            return;
        }
        if (this.fakePlayerIdList != null) {
            for (int id : this.fakePlayerIdList) {
                FakePlayer.mc.world.removeEntityFromWorld(id);
            }
        }
    }
}