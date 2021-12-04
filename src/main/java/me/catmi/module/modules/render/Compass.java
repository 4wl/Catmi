package me.catmi.module.modules.render;

import me.catmi.module.Module;
import me.catmi.module.modules.hud.ClickGuiModule;
import me.catmi.module.modules.hud.HUD;
import me.catmi.settings.Setting;
import me.catmi.util.CMColor;
import me.catmi.util.Wrapper;
import me.catmi.util.font.FontUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class Compass
        extends Module {
    Setting.Double scale;
    Setting.Integer x;
    Setting.Integer y;
    private static final double HALF_PI = 1.5707963267948966;
    ScaledResolution resolution = new ScaledResolution(mc);

    public Compass() {
        super("Compass", Category.Render);
    }

    @Override
    public void setup() {
        this.scale = this.registerDouble("Radius","Radius", 3.0, 1.0, 5.0);
        this.x = this.registerInteger("X","X",100,0,2000);
        this.y = this.registerInteger("Y","Y",100,0,2000);
    }

    @Override
    public void onRender() {
        double centerX = (double)this.resolution.getScaledWidth() * 1.11;
        double centerY = this.resolution.getScaledHeight_double() * 1.8;
        for (Direction dir : Direction.values()) {
            double rad = Compass.getPosOnCompass(dir);
            FontUtils.drawStringWithShadow2(HUD.customFont.getValue(), dir.name(), (int) (x.getValue() + this.getX(rad)), (int) (y.getValue() + this.getY(rad)), dir == Direction.N ? new CMColor(255, 0, 0, 255).getRGB() : new CMColor(255, 255, 255, 255).getRGB());
        }
    }

    private double getX(double rad) {
        return Math.sin(rad) * (this.scale.getValue() * 10.0);
    }

    private double getY(double rad) {
        double epicPitch = MathHelper.clamp((float)(Wrapper.getRenderEntity().rotationPitch + 30.0f), (float)-90.0f, (float)90.0f);
        double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (this.scale.getValue() * 10.0);
    }

    private static double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees((float)Wrapper.getRenderEntity().rotationYaw));
        int index = dir.ordinal();
        return yaw + (double)index * 1.5707963267948966;
    }

    private static enum Direction {
        N,
        W,
        S,
        E;

    }

}
