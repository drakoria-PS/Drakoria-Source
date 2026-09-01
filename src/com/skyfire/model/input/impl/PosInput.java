package com.skyfire.model.input.impl;

import java.sql.PreparedStatement;

import com.skyfire.GameSettings;
import com.skyfire.model.input.Input;
import com.skyfire.util.NameUtils;
import com.skyfire.world.entity.impl.player.Player;

import mysql.MySQLController;
import mysql.MySQLDatabase;
import mysql.MySQLController.Database;

public class PosInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendClientRightClickRemoval();
		player.getPlayerOwnedShopManager().updateFilter(syntax);
		
	}
}
