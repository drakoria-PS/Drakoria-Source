package com.skyfire.world.content.skill.impl.summoning;

import com.skyfire.model.Item;

public enum FamiliarData {

STEEL_TITAN(-1, -1, -1, -1, -1, -1, -1, 
		new Item(-1),
new Item(-1), new Item(-1), new Item(-1, -1));

	private FamiliarData(int pouchId, int levelRequired, double createExperience,
			int npcId, double summonExperience, int summoningPointsRequired, int scroll, Item... recipe) {
		this.pouchId = pouchId;
		this.levelRequired = levelRequired;
		this.createExperience = createExperience;
		this.npcId = npcId;
		this.summonExperience = summonExperience;
		this.summoningPointsRequired = summoningPointsRequired;
		this.scroll = scroll;
		this.recipe = recipe;
	}

	public final int pouchId;

	final int levelRequired;

	final double createExperience;

	final int npcId;

	final double summonExperience;

	final int summoningPointsRequired;
	
	final Item[] recipe;

	final int scroll;

	public static FamiliarData forId(int pouchId) {
		for (FamiliarData familiar : FamiliarData.values()) {
			if (familiar.getPouchId() == pouchId) {
				return familiar;
			}
		}
		return null;
	}

	public static FamiliarData forNPCId(int npcId) {
		for (FamiliarData familiar : FamiliarData.values()) {
			if (familiar.npcId == npcId)
				return familiar;
		}
		return null;
	}

	public int getPouchId() {
		return pouchId;
	}
}
