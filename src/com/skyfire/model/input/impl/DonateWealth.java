package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.WellOfWealth;
import com.skyfire.world.entity.impl.player.Player;

public class DonateWealth extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		WellOfWealth.donate(player, amount);
	}

}
