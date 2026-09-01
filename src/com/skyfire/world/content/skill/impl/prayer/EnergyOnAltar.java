package com.skyfire.world.content.skill.impl.prayer;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Graphic;
import com.skyfire.model.Skill;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.model.input.impl.EnterAmountOfBonesToSacrifice;
import com.skyfire.util.Misc;
import com.skyfire.world.content.Achievements;
import com.skyfire.world.content.Achievements.AchievementData;
import com.skyfire.world.entity.impl.player.Player;


public class EnergyOnAltar {

	public static void openInterface(Player player, int itemId) {
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		player.setInputHandling(new EnterAmountOfBonesToSacrifice());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(itemId).getName()).sendInterfaceModel(1746, itemId, 150) .sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to offer?");
	}

	public static void offerBones(final Player player, final int amount) {
		final int boneId = 13653;
		player.getSkillManager().stopSkilling();
		final int currentBone = 13653;
		
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(2, player, true) {
			int amountSacrificed = 0;
			@Override
			public void execute() {
				if(amountSacrificed >= amount) {
					stop();
					return;
				}
				if(!player.getInventory().contains(boneId)) {
					player.getPacketSender().sendMessage("@red@[ALERT] @blu@You have run out of "+ItemDefinition.forId(boneId).getName()+".");
					stop();
					return;
				}
				if(player.getInteractingObject() != null) {
					player.setPositionToFace(player.getInteractingObject().getPosition().copy());
					player.getInteractingObject().performGraphic(new Graphic(744));
				}
				
				if(currentBone == 13653) {
					if(player.getInventory().getFreeSlots() < 1) {
						player.getPacketSender().sendMessage("@red@[ALERT] @blu@You need at least 1 free inventory space to loot this.");
						return;
					}
					if(player.getInventory().contains(13653) && player.getInventory().getAmount(13653) <= 60) {
						player.getPacketSender().sendMessage("@red@[ALERT] @blu@You need atleast 60 energy fragments, go collect or buy more!");
						return;
					}
					player.getSkillManager().addExperience(Skill.WARDING, 7000);
					player.getInventory().delete(13653, 60);
					player.performAnimation(new Animation(544));
					
					
					//ramd
					if (Misc.getRandom(10000) == 3) {
						player.getInventory().add(20510, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Staff of Power");
					}
					if (Misc.getRandom(5000) == 4) {
						player.getInventory().add(4708, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Blessed ahrims hood");
					}
					if (Misc.getRandom(5000) == 5) {
						player.getInventory().add(4712, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Blessed ahrims top");
					}
					if (Misc.getRandom(5000) == 6) {
						player.getInventory().add(4714, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Blessed ahrims robe");
					}
					
					if (Misc.getRandom(5000) == 6) {
						player.getInventory().add(6070, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Cloak of power");
					}
					if (Misc.getRandom(25) == 7) {
						player.getInventory().add(7775, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@1 million token");
					}
					if (Misc.getRandom(150) == 8) {
						player.getInventory().add(7776, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@10 million token");
					}
					if (Misc.getRandom(3500) == 9) {
						player.getInventory().add(7774, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@100 million token");
					}
					if (Misc.getRandom(5) == 0) {
						player.getInventory().add(2, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@cannonball");
					}
					if (Misc.getRandom(800) == 17) {
						player.getInventory().add(14499, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Dagon'hai hat");
					}
					if (Misc.getRandom(800) == 16) {
						player.getInventory().add(14497, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Dagon'hai robe top");
					}
					if (Misc.getRandom(800) == 15) {
						player.getInventory().add(14501, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Dagon'hai robe bottoms");
					}
					
					if (Misc.getRandom(25609) == 18) {
						player.getInventory().add(5609, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Chiken! Its worth 2b in General store!");
					}
					
					
					if (Misc.getRandom(30) == 4) {
						player.getInventory().add(12158, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@500k token");
					}
					if (Misc.getRandom(15) == 5) {
						player.getInventory().add(12159, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@250k token");
					}
					
					if (Misc.getRandom(500) == 28) {
						player.getInventory().add(12160, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@5m token");
					}
					
					if (Misc.getRandom(6) == 1) {
						player.getInventory().add(15270, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@raw rocktail! Yuck");
					}
					if (Misc.getRandom(55) == 54) {
						player.getInventory().add(15335, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@sip of a Overload potion");
					}
					if (Misc.getRandom(100) == 1) {
						player.getInventory().add(13209, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Corrupt crystal");
					}
					if (Misc.getRandom(100) == 99) {
						player.getInventory().add(6199, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Mystery box");
					}
					if (Misc.getRandom(10) == 0) {
						player.getInventory().add(6685, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Saradomin brew (4)");
					}
				
						player.getInventory().add(995, 1500).refreshItems();
				
					
					
					
			}
				
			
			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("@red@[ALERT] @blu@You stopped warding.");
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
