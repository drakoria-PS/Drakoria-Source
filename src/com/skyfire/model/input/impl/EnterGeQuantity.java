package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.grandexchange.GrandExchange;
import com.skyfire.world.entity.impl.player.Player;

public class EnterGeQuantity extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		GrandExchange.setQuantity(player, amount);
	}

}
