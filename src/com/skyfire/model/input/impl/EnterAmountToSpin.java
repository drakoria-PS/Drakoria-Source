package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.skill.impl.crafting.Flax;
import com.skyfire.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		Flax.spinFlax(player, amount);
	}

}
