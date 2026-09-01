package com.skyfire.model.input.impl;

import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.model.input.EnterAmount;
import com.skyfire.util.Misc;
import com.skyfire.world.entity.impl.player.Player;

public class BuyShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > 80000000)
			amount = 80000000;
		player.getPacketSender().sendInterfaceRemoval();
		int cost = ItemDefinition.forId(18016).getValue() * amount;
		long moneyAmount = player.getInventory().getAmount(995);
		long canBeBought = moneyAmount / (ItemDefinition.forId(18016).getValue());
		if(canBeBought >= amount)
			canBeBought = amount;
		if(canBeBought == 0) {
			player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
			return;
		}
		cost = ItemDefinition.forId(18016).getValue() * (int) canBeBought;
		if(player.getInventory().getAmount(995) < cost) {
			player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
			return;
		}
		player.getInventory().delete(995, (int) cost);
		player.getInventory().add(18016, (int) canBeBought);
		player.getPacketSender().sendMessage("You've bought "+canBeBought+" Spirit Shards for "+Misc.insertCommasToNumber(""+(int)cost)+" coins.");
	}

}
