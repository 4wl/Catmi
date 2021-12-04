package me.catmi.module.modules.misc;

import me.catmi.event.events.PacketEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.command.Command;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

public class ChatSuffix extends Module{
	public ChatSuffix(){
		super("ChatSuffix", Category.Misc);
	}


	Setting.Mode Separator;

	public void setup(){

		ArrayList<String> Separators = new ArrayList<>();
		Separators.add(">>");
		Separators.add("<<");
		Separators.add("|");

		Separator = registerMode("Separator", "Separator", Separators, "|");


	}

	@EventHandler
	private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
		if (event.getPacket() instanceof CPacketChatMessage){
			if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(Command.getPrefix()))
				return;
			String Separator2 = null;
			if (Separator.getValue().equalsIgnoreCase(">>")){
				Separator2 = " \u300b";
			}
			if (Separator.getValue().equalsIgnoreCase("<<")){
				Separator2 = " \u300a";
			}
			else if (Separator.getValue().equalsIgnoreCase("|")){
				Separator2 = " \u23D0 ";
			}
			String old = ((CPacketChatMessage) event.getPacket()).getMessage();
			String suffix = Separator2 + toUnicode(Catmi.MODNAME);
			String s = old + suffix;
			if (s.length() > 255) return;
			((CPacketChatMessage) event.getPacket()).message = s;
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}

	public String toUnicode(String s){
		return s.toLowerCase()
				.replace("a", "\u1d00")
				.replace("b", "\u0299")
				.replace("c", "\u1d04")
				.replace("d", "\u1d05")
				.replace("e", "\u1d07")
				.replace("f", "\ua730")
				.replace("g", "\u0262")
				.replace("h", "\u029c")
				.replace("i", "\u026a")
				.replace("j", "\u1d0a")
				.replace("k", "\u1d0b")
				.replace("l", "\u029f")
				.replace("m", "\u1d0d")
				.replace("n", "\u0274")
				.replace("o", "\u1d0f")
				.replace("p", "\u1d18")
				.replace("q", "\u01eb")
				.replace("r", "\u0280")
				.replace("s", "\ua731")
				.replace("t", "\u1d1b")
				.replace("u", "\u1d1c")
				.replace("v", "\u1d20")
				.replace("w", "\u1d21")
				.replace("x", "\u02e3")
				.replace("y", "\u028f")
				.replace("z", "\u1d22");
	}
}
