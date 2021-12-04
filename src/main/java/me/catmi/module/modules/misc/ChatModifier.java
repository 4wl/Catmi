package me.catmi.module.modules.misc;


import me.catmi.event.events.PacketEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.command.Command;
import me.catmi.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatModifier extends Module{
	public ChatModifier(){
		super("ChatModifier", Category.Misc);
	}

	public Setting.Boolean clearBkg;
	Setting.Boolean chattimestamps;
	Setting.Mode format;
	Setting.Mode color;
	Setting.Mode decoration;
	Setting.Boolean space;
	Setting.Boolean greentext;

	public void setup(){

		ArrayList<String> formats = new ArrayList<>();
		formats.add("H24:mm");
		formats.add("H12:mm");
		formats.add("H12:mm a");
		formats.add("H24:mm:ss");
		formats.add("H12:mm:ss");
		formats.add("H12:mm:ss a");
		ArrayList<String> deco = new ArrayList<>(); deco.add("< >"); deco.add("[ ]"); deco.add("{}"); deco.add(" ");
		ArrayList<String> colors = new ArrayList<>();
		for (ChatFormatting cf : ChatFormatting.values()){
			colors.add(cf.getName());
		}

		clearBkg = registerBoolean("Clear Chat", "ClearChat", false);
		greentext = registerBoolean("Green Text", "GreenText", false);
		chattimestamps = registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
		format = registerMode("Format", "Format", formats, "H24:mm");
		decoration = registerMode("Deco", "Deco", deco, "[ ]");
		color = registerMode("Color", "Colors", colors, ChatFormatting.GRAY.getName());
		space = registerBoolean("Space", "Space", false);

	}

	@EventHandler
	private final Listener<ClientChatReceivedEvent> chatReceivedEventListener = new Listener<>(event -> {
		//Chat Time Stamps
		if (chattimestamps.getValue()){
			String decoLeft = decoration.getValue().equalsIgnoreCase(" ") ? "" : decoration.getValue().split(" ")[0];
			String decoRight = decoration.getValue().equalsIgnoreCase(" ") ? "" : decoration.getValue().split(" ")[1];
			if (space.getValue()) decoRight += " ";
			String dateFormat = format.getValue().replace("H24", "k").replace("H12", "h");
			String date = new SimpleDateFormat(dateFormat).format(new Date());
			TextComponentString time = new TextComponentString(ChatFormatting.getByName(color.getValue()) + decoLeft + date + decoRight + ChatFormatting.RESET);
			event.setMessage(time.appendSibling(event.getMessage()));
	}
	});

	@EventHandler
	private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
		if (greentext.getValue()){
			if (event.getPacket() instanceof CPacketChatMessage){
				if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(Command.getPrefix()))
					return;
				String message = ((CPacketChatMessage) event.getPacket()).getMessage();
				String prefix = "";
				prefix = ">";
				String s = prefix + message;
				if (s.length() > 255) return;
				((CPacketChatMessage) event.getPacket()).message = s;
			}
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}
}
