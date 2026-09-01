package com.skyfire.net.packet.impl;

import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.content.MoneyPouch;
import com.skyfire.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();
		if(player.getTrading().inTrade() || player.getDueling().inDuelScreen) {
			player.getPacketSender().sendMessage("You cannot withdraw money at the moment.");
		} else {
			MoneyPouch.withdrawMoney(player, amount);
		}
	}

}
