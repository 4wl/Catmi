package me.catmi.module.modules.misc;

import me.catmi.event.events.PacketEvent;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG extends Module{
	public static AutoGG INSTANCE;

	public AutoGG(){
		super("AutoGG", Category.Misc);
		INSTANCE = this;
	}

	static List<String> AutoGgMessages = new ArrayList<>();
	private ConcurrentHashMap targetedPlayers = null;
	int index = -1;

	@EventHandler
	private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
		if (mc.player != null){
			if (this.targetedPlayers == null){
				this.targetedPlayers = new ConcurrentHashMap();
			}

			if (event.getPacket() instanceof CPacketUseEntity){
				CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();
				if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)){
					Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
					if (targetEntity instanceof EntityPlayer){
						this.addTargetedPlayer(targetEntity.getName());
					}
				}
			}
		}
	});
	@EventHandler
	private final Listener<LivingDeathEvent> livingDeathEventListener = new Listener<>(event -> {
		if (mc.player != null){
			if (this.targetedPlayers == null){
				this.targetedPlayers = new ConcurrentHashMap();
			}

			EntityLivingBase entity = event.getEntityLiving();
			if (entity != null){
				if (entity instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer) entity;
					if (player.getHealth() <= 0.0F){
						String name = player.getName();
						if (this.shouldAnnounce(name)){
							this.doAnnounce(name);
						}

					}
				}
			}
		}
	});

	public void onEnable(){
		this.targetedPlayers = new ConcurrentHashMap();
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		this.targetedPlayers = null;
		Catmi.EVENT_BUS.unsubscribe(this);
	}

	public void onUpdate(){
		if (this.targetedPlayers == null){
			this.targetedPlayers = new ConcurrentHashMap();
		}

		Iterator var1 = mc.world.getLoadedEntityList().iterator();

		while (var1.hasNext()){
			Entity entity = (Entity) var1.next();
			if (entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) entity;
				if (player.getHealth() <= 0.0F){
					String name = player.getName();
					if (this.shouldAnnounce(name)){
						this.doAnnounce(name);
						break;
					}
				}
			}
		}

		targetedPlayers.forEach((namex, timeout) -> {
			if ((int) timeout <= 0){
				this.targetedPlayers.remove(namex);
			} else{
				this.targetedPlayers.put(namex, (int) timeout - 1);
			}

		});
	}

	private boolean shouldAnnounce(String name){
		return this.targetedPlayers.containsKey(name);
	}

	private void doAnnounce(String name){
		targetedPlayers.remove(name);
		if (index >= (AutoGgMessages.size() - 1)) index = -1;
		index++;
		String message;
		if (AutoGgMessages.size() > 0)
			message = AutoGgMessages.get(index);
		else
			message = "\u1d0f\u1d0d\u0262 \u1d1c \u1d05\u026a\u1d07 \u1d07\u1d22\u1d18\u1d22";

		String messageSanitized = message.replaceAll("ยง", "").replace("{name}", name);
		if (messageSanitized.length() > 255){
			messageSanitized = messageSanitized.substring(0, 255);
		}

		mc.player.connection.sendPacket(new CPacketChatMessage(messageSanitized));
	}

	public void addTargetedPlayer(String name){
		if (!Objects.equals(name, mc.player.getName())){
			if (this.targetedPlayers == null){
				this.targetedPlayers = new ConcurrentHashMap();
			}

			targetedPlayers.put(name, 20);
		}
	}


	public static void addAutoGgMessage(String s){
		AutoGgMessages.add(s);
	}

	public static List<String> getAutoGgMessages(){
		return AutoGgMessages;
	}
}
