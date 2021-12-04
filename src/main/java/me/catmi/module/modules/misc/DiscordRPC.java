package me.catmi.module.modules.misc;

        import com.mojang.realmsclient.gui.ChatFormatting;
        import me.catmi.CatmiRPC;
        import me.catmi.command.Command;
        import me.catmi.module.Module;

        public class DiscordRPC extends Module {
        public DiscordRPC() {
        super("DiscordRPC", Category.Misc);
        }
        public void onEnable() {
        CatmiRPC.init();
        if(mc.player != null)
        Command.sendClientMessage(ChatFormatting.WHITE + "Discord RPC " + ChatFormatting.GREEN + "started!");
        }

        public void onDisable() {
        CatmiRPC.shutdown();
        Command.sendClientMessage(ChatFormatting.WHITE + "Discord RPC " + ChatFormatting.RED + "shutdown!");
        }
        }
