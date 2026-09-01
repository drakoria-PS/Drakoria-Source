package com.skyfire.net.packet.impl;

import com.skyfire.model.Skill;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.world.entity.impl.player.Player;

public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();
		Skill skill = Skill.forPrestigeId(prestigeId);
		if(skill == null) {
			return;
		}
		if(player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close all interfaces before doing this.");
			return;
		}
		player.getSkillManager().resetSkill(skill, true);
	}

}
