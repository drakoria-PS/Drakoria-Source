package com.skyfire.model.input.impl;

import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.content.skill.impl.crafting.Tanning;
import com.skyfire.world.entity.impl.player.Player;

public class EnterAmountOfHidesToTan extends EnterAmount {

	private int button;
	public EnterAmountOfHidesToTan(int button) {
		this.button = button;
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		Tanning.tanHide(player, button, amount);
	}

}
