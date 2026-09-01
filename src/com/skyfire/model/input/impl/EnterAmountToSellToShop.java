package com.skyfire.model.input.impl;

import com.skyfire.model.container.impl.Shop;
import com.skyfire.model.input.EnterAmount;
import com.skyfire.world.entity.impl.player.Player;

public class EnterAmountToSellToShop extends EnterAmount {

	public EnterAmountToSellToShop(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(player.isShopping() && getItem() > 0 && getSlot() >= 0) {
			Shop shop = player.getShop();
			if(shop != null) {
				if(getSlot() >= player.getInventory().getItems().length || player.getInventory().getItems()[getSlot()].getId() != getItem())
					return;
				player.getShop().setPlayer(player).forSlot(getSlot()).copy().setAmount(amount).copy();
				shop.sellItem(player, getSlot(), amount);
			} else
				player.getPacketSender().sendInterfaceRemoval();
		} else
			player.getPacketSender().sendInterfaceRemoval();
		
	}

}
