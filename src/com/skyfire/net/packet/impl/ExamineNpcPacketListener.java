package com.skyfire.net.packet.impl;

import com.skyfire.model.PlayerRights;
import com.skyfire.model.definitions.NpcDefinition;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.content.MonsterDrops;
import com.skyfire.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if(npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if(npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
			MonsterDrops.sendNpcDrop(player, npcDef.getId(), npcDef.getName().toLowerCase());
			
			if(player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendMessage("This NPC's ID is: " + npcDef.getId());
			}
			
	}
   }

}


