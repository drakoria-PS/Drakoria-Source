package com.skyfire.engine.task.impl;


import com.skyfire.engine.task.Task;
import com.skyfire.model.CombatIcon;
import com.skyfire.model.Graphic;
import com.skyfire.model.Hit;
import com.skyfire.model.Hitmask;
import com.skyfire.model.Locations.Location;
import com.skyfire.util.RandomUtility;
import com.skyfire.world.entity.impl.player.Player;

/**
 * Barrows
 * @author Gabriel Hannason
 */
public class CeilingCollapseTask extends Task {

	public CeilingCollapseTask(Player player) {
		super(9, player, false);
		this.player = player;
	}

	private Player player;

	@Override
	public void execute() {
		if(player == null || !player.isRegistered() || player.getLocation() != Location.BARROWS || player.getLocation() == Location.BARROWS && player.getPosition().getY() < 8000) {
			player.getPacketSender().sendCameraNeutrality();
			stop();
			return;
		}
		player.performGraphic(new Graphic(60));
		player.getPacketSender().sendMessage("Some rocks fall from the ceiling and hit you.");
		player.forceChat("Ouch!");
		player.dealDamage(new Hit(30 + RandomUtility.getRandom(20), Hitmask.RED, CombatIcon.BLOCK));
	}
}
