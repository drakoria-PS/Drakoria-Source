package com.skyfire.net.packet.impl;

import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.World;
import com.skyfire.world.content.ProfileViewing;
import com.skyfire.world.entity.impl.player.Player;

public class ViewProfilePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0) {
			return;
		}
		Player other = World.getPlayerByIndex(index);
		if (other == null) {
			return;
		}
		if (other.getPosition().getDistance(player.getPosition()) > 5) {
			player.sendMessage("The other player is too far away from you.");
			return;
		}
		ProfileViewing.view(player, other);
	}
}
