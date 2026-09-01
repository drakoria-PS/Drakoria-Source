package com.skyfire.model.input.impl;

import com.skyfire.model.container.impl.Bank.BankSearchAttributes;
import com.skyfire.model.input.Input;
import com.skyfire.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if(searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
