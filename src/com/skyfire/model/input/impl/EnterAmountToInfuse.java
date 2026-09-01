package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.skill.impl.summoning.PouchMaking;
import com.skyfire.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getInterfaceId() != 63471) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		PouchMaking.infusePouches(player, amount);
	}

}
