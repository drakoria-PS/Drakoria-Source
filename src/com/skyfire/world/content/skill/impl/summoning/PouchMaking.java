package com.skyfire.world.content.skill.impl.summoning;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Graphic;
import com.skyfire.model.Item;
import com.skyfire.model.Skill;
import com.skyfire.model.input.impl.EnterAmountToInfuse;
import com.skyfire.world.content.Achievements;
import com.skyfire.world.content.Achievements.AchievementData;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.entity.impl.player.Player;

public class PouchMaking {

	private static final int SHARD_ID = 18016;
	private static final int POUCH_ID = 12155;

	public static boolean pouchInterface(Player p, int buttonId) {
	

		return false;
	}

	private static boolean hasRequirements(final Player player, final Pouch pouch) {
		return false;
		
	}

	public static void infusePouches(final Player player, final int amount) {}
}
