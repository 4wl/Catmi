package me.catmi.module.modules.render;

import me.catmi.module.Module;
import me.catmi.util.Wrapper;
import me.catmi.util.wing.GuiEditWings;

public class Wings extends Module {
    public Wings(){
        super("Wings", Category.Render);
    }
    public void onEnable(){
        mc.displayGuiScreen(new GuiEditWings(Wrapper.mod));
        disable();
    }
}
