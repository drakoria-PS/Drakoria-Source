package com.skyfire.net.packet.impl;

import com.skyfire.model.Flag;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.entity.impl.player.Player;

public class ItemColorCustomization implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int itemId = packet.readUnsignedShort();
		int size = packet.readUnsignedByte();
		
		System.out.println(itemId);
		
		switch(itemId) {
		case 14019:
			
			int[] colors = new int[size];
			
			for(int i = 0; i < size; i++) {
				colors[i] = packet.readInt();
			}
			
			player.setMaxCapeColors(colors);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendInterfaceRemoval();
			
			break;
			
		}
		
	}

}
