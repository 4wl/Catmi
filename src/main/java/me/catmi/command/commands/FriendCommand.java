package me.catmi.command.commands;

import me.catmi.Catmi;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.command.Command;
import me.catmi.players.friends.Friends;

public class FriendCommand extends Command{
	@Override
	public String[] getAlias(){
		return new String[]{"friend", "friends", "f"};
	}

	@Override
	public String getSyntax(){
		return "friend <add | del> <Name>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		if (args[0].equalsIgnoreCase("add")){
			if (Friends.isFriend(args[1])){
				Command.sendClientMessage(args[1] + ChatFormatting.GRAY + " is already a friend!");
				return;
			}
			if (!Friends.isFriend(args[1])){
				Catmi.getInstance().friends.addFriend(args[1]);
				Command.sendClientMessage("Added " + args[1] + " to friends list");
			}
		}
		if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")){
			if (!Friends.isFriend(args[1])){
				Command.sendClientMessage(args[1] + " is not a friend!");
				return;
			}
			if (Friends.isFriend(args[1])){
				Catmi.getInstance().friends.delFriend(args[1]);
				Command.sendClientMessage("Removed " + args[1] + " from friends list");
			}
		}
	}
}
