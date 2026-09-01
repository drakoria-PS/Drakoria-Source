package com.skyfire.engine.task.impl;


import com.skyfire.engine.task.Task;
import com.skyfire.model.Item;
import com.skyfire.world.content.skill.impl.summoning.FamiliarData;
import com.skyfire.world.content.skill.impl.summoning.BossPets.BossPet;
import com.skyfire.world.entity.impl.player.Player;

/**
 * Familiar spawn on login
 * @author Gabriel Hannason
 */
public class FamiliarSpawnTask extends Task {

	public FamiliarSpawnTask(Player player) {
		super(2, player, false);
		this.player = player;
	}

	private Player player;
	public int familiarId;
	public int deathTimer;
	public Item[] validItems;

	public FamiliarSpawnTask setFamiliarId(int familiarId) {
		this.familiarId = familiarId;
		return this;
	}

	public FamiliarSpawnTask setDeathTimer(int deathTimer) {
		this.deathTimer = deathTimer;
		return this;
	}
	
	public FamiliarSpawnTask setValidItems(Item[] validItems) {
		this.validItems = validItems;
		return this;
	}
	
	@Override
	public void execute() {
		
	}
}
