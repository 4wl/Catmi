package me.catmi.clickgui.buttons;

import me.catmi.settings.Setting;
import me.catmi.util.font.FontUtils;
import me.catmi.clickgui.frame.Buttons;
import me.catmi.clickgui.frame.Component;
import me.catmi.clickgui.frame.Renderer;
import me.catmi.module.Module;
import me.catmi.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ModeComponent extends Component {
	private boolean hovered;
	private final Buttons parent;
	private final Setting.Mode set;
	private int offset;
	private int x;
	private int y;
	private final Module mod;
	private int modeIndex;
	
	public ModeComponent(final Setting.Mode set, final Buttons button, final Module mod, final int offset){
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}
	
	@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}
	
	@Override
	public void renderComponent(){
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, Renderer.getTransColor(hovered));
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
		FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
	}
	
	@Override
	public void updateComponent(final int mouseX, final int mouseY){
		this.hovered = this.isMouseOnButton(mouseX, mouseY);
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button){
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open){
			final int maxIndex = this.set.getModes().size() - 1;
			this.modeIndex++;
			if (this.modeIndex > maxIndex){
				this.modeIndex = 0;
			}
			this.set.setValue(this.set.getModes().get(this.modeIndex));
		}
	}
	
	public boolean isMouseOnButton(final int x, final int y){
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16;
	}
}
