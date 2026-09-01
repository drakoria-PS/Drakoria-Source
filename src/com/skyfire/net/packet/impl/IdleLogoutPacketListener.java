package com.skyfire.net.packet.impl;

import com.skyfire.model.PlayerRights;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.entity.impl.player.Player;

//CALLED EVERY 3 MINUTES OF INACTIVITY

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR  || player.getRights() == PlayerRights.DEVELOPER)
			return;
		/*if(player.logout() && (player.getSkillManager().getSkillAttributes().getCurrentTask() == null || !player.getSkillManager().getSkillAttributes().getCurrentTask().isRunning())) {
			World.getPlayers().remove(player);
		}*/
		player.setInactive(true);
	}
}
