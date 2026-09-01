package com.skyfire.world.content.skill.impl.thieving;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Item;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.util.RandomUtility;
import com.skyfire.world.content.Achievements;
import com.skyfire.world.content.Achievements.AchievementData;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.entity.impl.player.Player;

public class Stalls {
    public static void stealFromStall(Player player, int lvlReq, int xp, int reward, int amount) {
        player.getSkillManager().stopSkilling();
        player.getPacketSender().sendInterfaceRemoval();
        if (!player.getClickDelay().elapsed(2000))
            return;
        if (player.getInventory().getFreeSlots() <= 0) {
            player.getPacketSender().sendMessage("You need some more inventory space to do this.");
            return;
        }
        if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
            player.getPacketSender().sendMessage("You cannot do that right now.");
            return;
        }
        if (player.getRights() == PlayerRights.EMERALD || player.getRights() == PlayerRights.SUPPORT) {
			player.getInventory().add(995, 5000);
		}
		if (player.getRights() == PlayerRights.RUBY || player.getRights() == PlayerRights.MODERATOR) {
			player.getInventory().add(995, 7500);
		}
		if (player.getRights() == PlayerRights.DIAMOND || player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.getInventory().add(995, 10000);
		}
		if (player.getRights() == PlayerRights.DRAGONSTONE) {
			player.getInventory().add(995, 15000);
		}
		
		if (player.getLocation() == Location.DONATOR_ZONE) {
			Item item = new Item(reward);
			int value = item.getDefinition().getValue();		
			player.getInventory().add(995, value);

		} else {
			player.getInventory().add(reward, 1);

		}
		if(reward == 15009)
			Achievements.finishAchievement(player, AchievementData.STEAL_A_RING);
		else if(reward == 11998) {
			Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS);
			Achievements.doProgress(player, AchievementData.STEAL_5000_SCIMITARS);
		}
        if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlReq) {
            player.getPacketSender()
                    .sendMessage("You need a Thieving level of at least " + lvlReq + " to steal from this stall.");
            return;
        }
        player.setCurrentTask(new Task(2, player, true) {

            @Override
            public void execute() {
                if (player.getInventory().getFreeSlots() <= 0) {
                    player.getPacketSender().sendMessage("You do not have any free inventory space left.");
                    this.stop();
                    return;
                }
                player.performAnimation(new Animation(881));
                player.getInventory().add(reward, 1);
                player.getInventory().refreshItems();
                player.getSkillManager().addExperience(Skill.THIEVING, xp);
                player.getClickDelay().reset();
               // Achievements.doProgress(player, Achievement.THIEVING_101);
               // Achievements.doProgress(player, Achievement.THIEVES_GUILD);
                //Achievements.doProgress(player, Achievement.KLEPTOMANIAC);
            }

            @Override
            public void stop() {
                setEventRunning(false);
                player.performAnimation(new Animation(65535));
            }
        });
        TaskManager.submit(player.getCurrentTask());
    }
}


