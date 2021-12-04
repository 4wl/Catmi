package me.catmi.module;

import me.catmi.event.events.RenderEvent;
import me.catmi.util.render.CatmiTessellator;
import me.catmi.module.modules.combat.*;
import me.catmi.module.modules.exploits.*;
import me.catmi.module.modules.hud.*;
import me.catmi.module.modules.misc.*;
import me.catmi.module.modules.movement.*;
import me.catmi.module.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
	public static ArrayList<Module> modules;
	public List<Module> sortedModules = new ArrayList<>();

	public ModuleManager(){
		modules = new ArrayList<>();
		//Combat
		addMod(new AutoArmor());
		addMod(new AutoClicker());
		addMod(new AutoCrystal());
		addMod(new AutoFeetPlace());
		addMod(new AutoTotem());
		addMod(new AutoTrap());
		addMod(new FastXp());
		addMod(new AutoWeb());
		addMod(new FastBow());
		addMod(new HoleFill());
		addMod(new KillAura());
		addMod(new OffhandCrystal());
		addMod(new OffhandGap());
		addMod(new SelfTrap());
		//Exploits
		addMod(new CoordExploit());
		addMod(new FastBreak());
		addMod(new LiquidInteract());
		addMod(new NoInteract());
		addMod(new NoSwing());
		addMod(new PortalGodMode());
		//Movement
		addMod(new HoleTP());
		addMod(new PlayerTweaks());
		addMod(new ReverseStep());
		addMod(new LongJump());
		addMod(new Speed());
		addMod(new IceSpeed());
		addMod(new Sprint());
		addMod(new Step());
		addMod(new Strafe());
		addMod(new Speed2());
		addMod(new FakeLag());
		//Misc
		addMod(new AutoGG());
		addMod(new AutoReply());
		addMod(new AutoTool());
		addMod(new ChatModifier());
		addMod(new ChatSuffix());
		addMod(new FastPlace());
		addMod(new MCF());
		addMod(new MultiTask());
		addMod(new NoEntityTrace());
		addMod(new NoKick());
		addMod(new PvPInfo());
		addMod(new Live());
		addMod(new AutoReplanish());
		addMod(new FakePlayer());
		addMod(new AntiWeb());
		addMod(new AntiVoid());
		addMod(new DiscordRPC());
		//Render
		addMod(new BlockHighlight());
		addMod(new CapesModule());
		addMod(new EntityESP());
		addMod(new Freecam());
		addMod(new Fullbright());
		addMod(new HitSpheres());
		addMod(new HoleESP());
		addMod(new LogoutSpots());
		addMod(new MobOwner());
		addMod(new Nametags());
		addMod(new NoRender());
		addMod(new RenderTweaks());
		addMod(new ShulkerViewer());
		addMod(new Tracers());
		addMod(new ViewModel());
		addMod(new VoidESP());
		addMod(new Wings());
		addMod(new KeyBoard());
		addMod(new Compass());
		addMod(new TargetHUD());
		addMod(new ESP());
		//HUD
		addMod(new ClickGuiModule());
		addMod(new ColorMain());
		addMod(new HUD());
		addMod(new Notifications());
		addMod(new TextRadar());
		addMod(new HUD2());
		addMod(new RainbowOffset());
		addMod(new HUD3());
		addMod(new ArmorWarning());
	}

	public static void addMod(Module m){
		modules.add(m);
	}

	public static void onUpdate() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
	}

	public static void onRender() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
	}

	public static void onWorldRender(RenderWorldLastEvent event) {

		Minecraft.getMinecraft().profiler.startSection("gamesense");
		Minecraft.getMinecraft().profiler.startSection("setup");

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();
		GlStateManager.glLineWidth(1f);

		Vec3d renderPos = getInterpolatedPos(Minecraft.getMinecraft().player, event.getPartialTicks());

		RenderEvent e = new RenderEvent(CatmiTessellator.INSTANCE, renderPos, event.getPartialTicks());
		e.resetTranslation();
		Minecraft.getMinecraft().profiler.endSection();

		modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
			Minecraft.getMinecraft().profiler.startSection(module.getName());
			module.onWorldRender(e);
			Minecraft.getMinecraft().profiler.endSection();
		});

		Minecraft.getMinecraft().profiler.startSection("release");

		GlStateManager.glLineWidth(1f);
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();
		CatmiTessellator.releaseGL();

		Minecraft.getMinecraft().profiler.endSection();
		Minecraft.getMinecraft().profiler.endSection();
	}



	public static ArrayList<Module> getModules() {
		return modules;
	}

	public static ArrayList<Module> getModulesInCategory(Module.Category c){
		ArrayList<Module> list = (ArrayList<Module>) getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
		return list;
	}

	public static void onBind(int key) {
		if (key == 0 || key == Keyboard.KEY_NONE) return;
		modules.forEach(module -> {
			if(module.getBind() == key){
				module.toggle();
			}
		});
	}

	public static Module getModuleByName(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m;
	}

	public static boolean isModuleEnabled(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m.isEnabled();
	}

	public static boolean isModuleEnabled(Module m){
		return m.isEnabled();
	}

	public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3d(
				(entity.posX - entity.lastTickPosX) * x,
				(entity.posY - entity.lastTickPosY) * y,
				(entity.posZ - entity.lastTickPosZ) * z
		);
	}
}