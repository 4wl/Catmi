package me.catmi.module.modules.render;

import me.catmi.event.events.TransformSideFirstPersonEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module{
	public ViewModel(){super("ViewModel", Category.Render);}

	public Setting.Boolean cancelEating;
	Setting.Double xRight;
	Setting.Double yRight;
	Setting.Double zRight;
	Setting.Double xLeft;
	Setting.Double yLeft;
	Setting.Double zLeft;

	public void setup(){
		cancelEating = registerBoolean("No Eat", "NoEat", false);
		xLeft = registerDouble("Left X", "LeftX", 0.0, -2.0, 2.0);
		yLeft = registerDouble("Left Y", "LeftY", 0.2, -2.0, 2.0);
		zLeft = registerDouble("Left Z", "LeftZ", -1.2, -2.0, 2.0);
		xRight = registerDouble("Right X", "RightX", 0.0, -2.0, 2.0);
		yRight = registerDouble("Right Y", "RightY", 0.2, -2.0, 2.0);
		zRight = registerDouble("Right Z", "RightZ", -1.2, -2.0, 2.0);
	}

	@EventHandler
	private final Listener<TransformSideFirstPersonEvent> eventListener = new Listener<>(event -> {
		if (event.getHandSide() == EnumHandSide.RIGHT){
			GlStateManager.translate(xRight.getValue(), yRight.getValue(), zRight.getValue());
		} else if (event.getHandSide() == EnumHandSide.LEFT){
			GlStateManager.translate(xLeft.getValue(), yLeft.getValue(), zLeft.getValue());
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}
}
