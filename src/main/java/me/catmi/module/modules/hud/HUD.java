package me.catmi.module.modules.hud;

import me.catmi.players.friends.Friends;
import me.catmi.settings.Setting;
import me.catmi.util.font.FontUtils;
import me.catmi.util.render.RenderUtil2;
import me.catmi.util.world.TpsUtils;
import me.catmi.util.CMColor;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.module.ModuleManager;
import me.catmi.module.modules.combat.AutoCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class HUD extends Module {
	public HUD() {
		super("HUD", Category.HUD);
		setDrawn(false);

		resource = new ResourceLocation("minecraft:inventory_viewer.png");
	}

	public static Setting.Boolean customFont;
	Setting.Boolean PotionEffects;
	Setting.Boolean Watermark;
	Setting.Boolean Welcomer;
	Setting.Boolean Inventory;
	Setting.Integer inventoryX;
	Setting.Integer inventoryY;
	Setting.Boolean CatmiInfo;
	Setting.Mode Type;
	Setting.Boolean ArrayList;
	Setting.Integer animS;
	Setting.Integer rainbowSpeed;
	Setting.Boolean arraylistOutline;
	Setting.Boolean ArmorHud;
	Setting.Integer potionx;
	Setting.Integer potiony;
	Setting.Integer welcomex;
	Setting.Integer welcomey;
	Setting.Integer infox;
	Setting.Integer infoy;
	Setting.Boolean sortUp;
	Setting.Boolean right;
	Setting.Boolean psortUp;
	Setting.Boolean pright;
	Setting.Integer arrayx;
	Setting.Integer arrayy;
	Setting.ColorSetting color;
	private BlockPos[] surroundOffset;
	ResourceLocation resource;
	int sort;
	int modCount;
	int count;
	DecimalFormat format1 = new DecimalFormat("0");
	DecimalFormat format2 = new DecimalFormat("00");

	private static final RenderItem itemRender = Minecraft.getMinecraft()
			.getRenderItem();
	int totems;
	
	private static CMColor col;

	public void setup() {
		ArrayList<String> Modes = new ArrayList<>();
		Modes.add("PvP");
		Modes.add("Combat");
		Type = registerMode("Info Type", "InfoType", Modes, "PvP");
		infox = registerInteger("Information X", "InformationX", 0, 0, 1000);
		infoy = registerInteger("Information Y", "InformationY",  0, 0, 1000);
		CatmiInfo = registerBoolean("Information", "Information", false);
		ArmorHud = registerBoolean("Armor Hud", "ArmorHud", false);
		ArrayList = registerBoolean("ArrayList", "ArrayList",  false);
		sortUp = registerBoolean("Array Sort Up", "ArraySortUp", false);
		right = registerBoolean("Array Right", "ArrayRight", false);
		arrayx = registerInteger("Array X", "ArrayX", 0, 0, 1000);
		arrayy = registerInteger("Array Y", "ArrayY",0 , 0 ,1000);
		Inventory = registerBoolean("Inventory", "Inventory", false);
		inventoryX = registerInteger("Inventory X", "InventoryX", 0,0,1000);
		inventoryY = registerInteger("Inventory Y", "InventoryY", 12,0,1000);
		PotionEffects = registerBoolean("Potion Effects", "PotionEffects",false);
		potionx = registerInteger("Potion X", "PotionX", 0, 0, 1000);
		potiony = registerInteger("Potion Y", "PotionY", 0, 0, 1000);
		psortUp = registerBoolean("Potion Sort Up", "PotionSortUp", false);
		pright = registerBoolean("Potion Right", "PotionRight", false);
		Watermark = registerBoolean("Watermark", "Watermark", false);
		Welcomer = registerBoolean("Welcomer", "Welcomer", false);
		welcomex = registerInteger("Welcomer X", "WelcomerX", 0, 0, 1000);
		welcomey = registerInteger("Welcomer Y", "WelcomerY", 0, 0, 1000);
		customFont = registerBoolean("Custom Font", "CustomFont", false);
		color=registerColor("Color","Color");
	}

	public void onRender() {
		CMColor c=color.getValue();
		if (PotionEffects.getValue()){
			count = 0;
			try {
				mc.player.getActivePotionEffects().forEach(effect -> {
					String name = I18n.format(effect.getPotion().getName());
					double duration = effect.getDuration() / TpsUtils.getTickRate();
					int amplifier = effect.getAmplifier() + 1;
					double p1 = duration % 60;
					double p2 = duration / 60;
					double p3 = p2 % 60;
					String minutes = format1.format(p3);
					String seconds = format2.format(p1);
					String s = name + " " + amplifier + ChatFormatting.GRAY + " " + minutes + ":" + seconds;
					if (psortUp.getValue()) {
						if (pright.getValue()) {
							FontUtils.drawStringWithShadow(customFont.getValue(), s, potionx.getValue() - FontUtils.getStringWidth(customFont.getValue(),s),potiony.getValue() + (count * 10), c);
						} else {
							FontUtils.drawStringWithShadow(customFont.getValue(), s, potionx.getValue(), potiony.getValue() + (count * 10), c);
						}
						count++;
					} else {
						if (pright.getValue()) {
							FontUtils.drawStringWithShadow(customFont.getValue(), s, potionx.getValue() - FontUtils.getStringWidth(customFont.getValue(),s),  potiony.getValue() + (count * -10), c);
						} else {
							FontUtils.drawStringWithShadow(customFont.getValue(), s, potionx.getValue(), potiony.getValue() + (count * -10), c);
						}
						count++;
					}
				});
			} catch(NullPointerException e){e.printStackTrace();}
		}

		if (Watermark.getValue()) {
			FontUtils.drawStringWithShadow(customFont.getValue(), "Catmi " + Catmi.MODVER, 0, 0, c);
		}

		if (Welcomer.getValue()) {
			FontUtils.drawStringWithShadow(customFont.getValue(), "Hello " + mc.player.getName() + " You Looks Fat Today:^)", welcomex.getValue(), welcomey.getValue(), c);
		}

		if (Inventory.getValue()) {
			drawInventory(inventoryX.getValue(), inventoryY.getValue());
		}

		CMColor on = new CMColor(255, 0, 160);
		CMColor off = new CMColor(255, 255, 255);
		if (CatmiInfo.getValue()) {
			if (Type.getValue().equalsIgnoreCase("PvP")) {
				totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
				if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) totems++;

				EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
						.filter(entity -> entity instanceof EntityEnderCrystal)
						.filter(e -> mc.player.getDistance(e) <= AutoCrystal.range.getValue())
						.map(entity -> (EntityEnderCrystal) entity)
						.min(Comparator.comparing(cl -> mc.player.getDistance(cl)))
						.orElse(null);
				EntityOtherPlayerMP players = mc.world.loadedEntityList.stream()
						.filter(entity -> entity instanceof EntityOtherPlayerMP)
						.filter(entity -> !Friends.isFriend(entity.getName()))
						.filter(e -> mc.player.getDistance(e) <= AutoCrystal.placeRange.getValue())
						.map(entity -> (EntityOtherPlayerMP) entity)
						.min(Comparator.comparing(cl -> mc.player.getDistance(cl)))
						.orElse(null);
				final AutoCrystal a = (AutoCrystal) ModuleManager.getModuleByName("Autocrystal");
				this.surroundOffset = new BlockPos[]{new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
				final List<EntityPlayer> entities = new ArrayList<EntityPlayer>(mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
				if (Type.getValue().equalsIgnoreCase("PvP")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Catmi1.2", infox.getValue(), infoy.getValue(), c);
					if (players != null && mc.player.getDistance(players) <= AutoCrystal.range.getValue()) {
						FontUtils.drawStringWithShadow(customFont.getValue(), "HTR", infox.getValue(), infoy.getValue() + 10, on);
					} else {
						FontUtils.drawStringWithShadow(customFont.getValue(), "HTR", infox.getValue(), infoy.getValue() + 10, off);
					}
					if (players != null && mc.player.getDistance(players) <= AutoCrystal.placeRange.getValue()) {
						FontUtils.drawStringWithShadow(customFont.getValue(), "PLR", infox.getValue(), infoy.getValue() + 20, on);
					} else {
						FontUtils.drawStringWithShadow(customFont.getValue(), "PLR", infox.getValue(), infoy.getValue() + 20, off);
					}
					if (totems > 0 && ModuleManager.isModuleEnabled("AutoTotem")) {
						FontUtils.drawStringWithShadow(customFont.getValue(), totems + "", infox.getValue(), infoy.getValue() + 30, on);
					} else {
						FontUtils.drawStringWithShadow(customFont.getValue(), totems + "", infox.getValue(), infoy.getValue() + 30, off);
					}

					if (getPing() > 100) {
						FontUtils.drawStringWithShadow(customFont.getValue(), "PING " + getPing(), infox.getValue(), infoy.getValue() + 40, off);
					} else {
						FontUtils.drawStringWithShadow(customFont.getValue(), "PING " + getPing(), infox.getValue(), infoy.getValue() + 40, on);

					}
					for (final EntityPlayer e : entities) {
						int i = 0;
						for (final BlockPos add : this.surroundOffset) {
							++i;
							final BlockPos o = new BlockPos(e.getPositionVector().x, e.getPositionVector().y, e.getPositionVector().z).add(add.getX(), add.getY(), add.getZ());
							if (mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN) {
								if (i == 1 && a.canPlaceCrystal(o.north(1).down())) {
									FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", infox.getValue(), infoy.getValue() + 50, on);
								}
								if (i == 2 && a.canPlaceCrystal(o.east(1).down())) {
									FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", infox.getValue(), infoy.getValue() + 50, on);
								}
								if (i == 3 && a.canPlaceCrystal(o.south(1).down())) {
									FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", infox.getValue(), infoy.getValue() + 50, on);
								}
								if (i == 4 && a.canPlaceCrystal(o.west(1).down())) {
									FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", infox.getValue(), infoy.getValue() + 50, on);
								}
							} else
								FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", infox.getValue(), infoy.getValue() + 50, off);
						}
					}
				}
			} else if (Type.getValue().equalsIgnoreCase("Combat")) {
				FontUtils.drawStringWithShadow(customFont.getValue(), "Catmi1.2.4", infox.getValue(), infoy.getValue(), c);
				FontUtils.drawStringWithShadow(customFont.getValue(), "PvPInfo", infox.getValue(), infoy.getValue() + 10, c);
				FontUtils.drawStringWithShadow(customFont.getValue(), "<PvP>", infox.getValue(), infoy.getValue() + 20, c);
				if (ModuleManager.isModuleEnabled("AutoCrystal")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "CA: True", infox.getValue(), infoy.getValue() + 30, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "CA: False", infox.getValue(), infoy.getValue() + 30, off);
				}
				if (ModuleManager.isModuleEnabled("KillAura")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Aura: True", infox.getValue(), infoy.getValue() + 40, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Aura: False", infox.getValue(), infoy.getValue() + 40, off);
				}
				if (ModuleManager.isModuleEnabled("AutoFeetPlace")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Sur: True", infox.getValue(), infoy.getValue() + 50, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Sur: False", infox.getValue(), infoy.getValue() + 50, off);
				}
				if (ModuleManager.isModuleEnabled("AutoTrap")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Trap: True", infox.getValue(), infoy.getValue() + 60, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Trap: False", infox.getValue(), infoy.getValue() + 60, off);
				}
				if (ModuleManager.isModuleEnabled("Offhand")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Offhand: True", infox.getValue(), infoy.getValue() + 70, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "Offhand: False", infox.getValue(), infoy.getValue() + 70, off);
				}
				FontUtils.drawStringWithShadow(customFont.getValue(), "<qwq>", infox.getValue(), infoy.getValue() + 80, c);
				if (totems > 0 && ModuleManager.isModuleEnabled("AutoTotem")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), totems + " Tot", infox.getValue(), infoy.getValue() + 90, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), totems + " Tot", infox.getValue(), infoy.getValue() + 90, off);
				}
				if (getPing() > 100) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "PING [" + getPing() + " ms]", infox.getValue(), infoy.getValue() + 100, off);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "PING [" + getPing() + "]", infox.getValue(), infoy.getValue() + 100, on);

				}
				if (ModuleManager.isModuleEnabled("ClickGUI")) {
					FontUtils.drawStringWithShadow(customFont.getValue(), "hehe", infox.getValue(), infoy.getValue() + 110, on);
				} else {
					FontUtils.drawStringWithShadow(customFont.getValue(), "awa", infox.getValue(), infoy.getValue() + 1100, off);
				}
			}
		}

		if (ArrayList.getValue()) {

			}

			if (ArmorHud.getValue()) {

				GlStateManager.enableTexture2D();

				ScaledResolution resolution = new ScaledResolution(mc);
				int i = resolution.getScaledWidth() / 2;
				int iteration = 0;
				int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
				for (ItemStack is : mc.player.inventory.armorInventory) {
					iteration++;
					if (is.isEmpty()) continue;
					int x = i - 90 + (9 - iteration) * 20 + 2;
					GlStateManager.enableDepth();

					itemRender.zLevel = 200F;
					itemRender.renderItemAndEffectIntoGUI(is, x, y);
					itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
					itemRender.zLevel = 0F;

					GlStateManager.enableTexture2D();
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();

					String s = is.getCount() > 1 ? is.getCount() + "" : "";
					mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, new CMColor(255,255,255).getRGB());
						float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
						float red = 1 - green;
						int dmg = 100 - (int) (red * 100);
						FontUtils.drawStringWithShadow(customFont.getValue(), dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, new CMColor((int) (red * 255), (int) (green * 255), 0));
				}

				GlStateManager.enableDepth();
				GlStateManager.disableLighting();
			}

		}

		public void drawInventory ( int x, int y){
			if (Inventory.getValue()) {
				GlStateManager.enableAlpha();
				mc.renderEngine.bindTexture(resource);
				GlStateManager.color(1, 1, 1, 1);
				mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 162, 54);
				GlStateManager.disableAlpha();

				GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
				NonNullList<ItemStack> items = Minecraft.getMinecraft().player.inventory.mainInventory;
				for (int size = items.size(), item = 9; item < size; ++item) {
					final int slotX = x + 1 + item % 9 * 18;
					final int slotY = y + 1 + (item / 9 - 1) * 18;
					RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotX, slotY);
					mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotX, slotY);
					RenderHelper.disableStandardItemLighting();
				}
			}
		}

		public int getPing () {
			int p = -1;
			if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
				p = -1;
			} else {
				p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
			}
			return p;
		}
}
