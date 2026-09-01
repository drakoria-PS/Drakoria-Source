package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.skill.impl.prayer.BonesOnAltar;
import com.skyfire.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
	}

}
