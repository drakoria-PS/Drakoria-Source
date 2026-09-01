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


public class BonesOnAltar {

	public static void openInterface(Player player, int itemId) {
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		player.setInputHandling(new EnterAmountOfBonesToSacrifice());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(itemId).getName()).sendInterfaceModel(1746, itemId, 150) .sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to offer?");
	}

	
	
	
	
	
	public static void offerBonesd(final Player player, final int amount) {
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
					if(player.getInventory().contains(13653) && player.getInventory().getAmount(13653) <= 9) {
						player.getPacketSender().sendMessage("@red@[ALERT] @blu@You need atleast 10 energy fragments, go collect or buy more!");
						return;
					}
					player.getSkillManager().addExperience(Skill.WARDING, 5000);
					player.getInventory().delete(13653, 10);
					player.performAnimation(new Animation(544));
					
					
					//ramd
					if (Misc.getRandom(1000) == 37) {
						player.getInventory().add(6914, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Master wand");
					}
					if (Misc.getRandom(90) == 29) {
						player.getInventory().add(6922, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Infinity gloves");
					}
					if (Misc.getRandom(90) == 24) {
						player.getInventory().add(6918, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Infinity hat");
					}
					
					
					if (Misc.getRandom(250) == 149) {
						player.getInventory().add(6924, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Infinity bottoms");
					}
					
					
					
					
					if (Misc.getRandom(90) == 49) {
						player.getInventory().add(6920, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Infinity boots");
					}
					
					
					if (Misc.getRandom(23) == 22) {
						player.getInventory().add(995, 100000).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded 100k coins");
					}
					
					

					
					if (Misc.getRandom(150) == 65) {
						player.getInventory().add(6916, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Infinity robe top");
					}
					
					
					
					
					
					if (Misc.getRandom(5000) == 445) {
						player.getInventory().add(12284, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Toxic staff of dead");
					}
					
					
					
					if (Misc.getRandom(5000) == 333) {
						player.getInventory().add(14005, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Staff of light");
					}
					
					
					
					
					
					
					
					

					if (Misc.getRandom(8000) == 766) {
						player.getInventory().add(18963, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Animal Helm");
					}
					
					
					
					

					if (Misc.getRandom(8000) == 776) {
						player.getInventory().add(18964, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Animal Body");
					}
					
					
					
					
					

					if (Misc.getRandom(8000) == 433) {
						player.getInventory().add(18972, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Animal Legs");
					}
					
					
					

					if (Misc.getRandom(8000) == 436) {
						player.getInventory().add(18965, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Animal Long Bow");
					}
						
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					if (Misc.getRandom(5000) == 222) {
						player.getInventory().add(13216, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage hat");
					}
					
					if (Misc.getRandom(15000) == 55) {
						player.getInventory().add(3661, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a @red@Draconian Staff");
					}
					
					
					
					if (Misc.getRandom(5000) == 29) {
						player.getInventory().add(13218, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage bottoms");
					}
					
					
					
					if (Misc.getRandom(5000) == 210) {
						player.getInventory().add(13217, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage top");
					}
					
					if (Misc.getRandom(5000) == 211) {
						player.getInventory().add(13215, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic staff");
					}
					
					if (Misc.getRandom(5000) == 212) {
						player.getInventory().add(6532, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic TokHaar-Kal cape");
					}
					
					
					if (Misc.getRandom(5) == 5) {
						player.getInventory().add(995, 5000).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awarded 5k coins");
					}
					
					
					
					
					
					
					
					
					
					
					
					if (Misc.getRandom(10) == 3) {
						player.getInventory().add(555, 50+Misc.getRandom(150)).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awarded some runes");
					}
					
					if (Misc.getRandom(10) == 2) {
						player.getInventory().add(565, 50+Misc.getRandom(50)).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awarded some runes");
					}
					
					
					if (Misc.getRandom(10) == 1) {
						player.getInventory().add(560, 50+Misc.getRandom(50)).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awarded some runes");
					}
					
					
					
					
					

					if (Misc.getRandom(2) == 1) {
						player.getInventory().add(532, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awared a big bone");
					}
					if (Misc.getRandom(3500) == 1) {
						player.getInventory().add(2092, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awared a Pet Centaur");
					}
					if (Misc.getRandom(15000) == 1) {
						player.getInventory().add(14742, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						player.getPacketSender().sendMessage("@mag@This Item is Super Rare! Sell to Thessilia at home for reward!");
						
						player.getPacketSender().sendMessage("@cya@You were awared a Lilly of the valley!");
						player.getPacketSender().sendMessage("@mag@This Item is Super Rare! Sell to Thessilia at home for reward!");
						
					}
					
					if (Misc.getRandom(2) == 1) {
						player.getInventory().add(526, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awared a bones");
					}
					
					if (Misc.getRandom(2000) == 999) {
						player.getInventory().add(18335, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were arcane stream necklace coins! WOW");
					}
					if (Misc.getRandom(3000) == 3) {
						player.getInventory().add(18979, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a colorful scarf! WOW");
					}
					if (Misc.getRandom(25) == 1) {
						player.getInventory().add(18830, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@red@You were awared a bones");
					}
					
					
					if (Misc.getRandom(5000) == 999) {
						player.getInventory().add(995, 1000000+Misc.getRandom(50)).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded 1m coins! WOW");
					}
				
				
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void offerBones(final Player player, final int amount) {
		final int boneId = player.getSelectedSkillingItem();
		player.getSkillManager().stopSkilling();
		final BonesData currentBone = BonesData.forId(boneId);
		if(currentBone == null)
			return;
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
					player.getInteractingObject().performGraphic(new Graphic(624));
				}
				
				 
				
				
				
				
				if(currentBone == BonesData.BIG_BONES)
					Achievements.finishAchievement(player, AchievementData.BURY_A_BIG_BONE);
				else if(currentBone == BonesData.FROSTDRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
					Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
				}
				amountSacrificed++;
				player.getInventory().delete(boneId, 1);
				player.performAnimation(new Animation(713));
				player.getSkillManager().addExperience(Skill.PRAYER, (int) (currentBone.getBuryingXP() * 1.5));
			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("@red@[ALERT] @blu@You have pleased the gods with your "+(amountSacrificed == 1 ? "sacrifice" : "sacrifices")+".");
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}






	public static void offerBonesdf(final Player player, final int amount) {
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
					player.getInteractingObject().performGraphic(new Graphic(999));
				}
				
				if(currentBone == 13653) {
					if(player.getInventory().getFreeSlots() < 1) {
						player.getPacketSender().sendMessage("@red@[ALERT] @blu@You need at least 1 free inventory space to loot this.");
						return;
					}
					if(player.getInventory().contains(13653) && player.getInventory().getAmount(13653) <= 300) {
						player.getPacketSender().sendMessage("@red@[ALERT] @blu@You need atleast 300 energy fragments, go collect or buy more!");
						return;
					}
					player.getSkillManager().addExperience(Skill.WARDING, 20000);
					player.getInventory().delete(13653, 300);
					player.performAnimation(new Animation(10502));
					
					
					//ramd
					if (Misc.getRandom(5000) == 4190) {
						player.getInventory().add(15241, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@Blasta cannon! Congrats");
					}
					
					if (Misc.getRandom(8) == 2) {
						player.getInventory().add(15243, 1).refreshItems();
						//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
						
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@Blasta cannonball");
					}
					if (Misc.getRandom(5) == 1) {
						player.getInventory().add(7775, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@1 million token");
					}
					if (Misc.getRandom(50) == 8) {
						player.getInventory().add(7776, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@10 million token");
					}
					if (Misc.getRandom(100) == 9) {
						player.getInventory().add(7774, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@100 million token");
					}
					
					player.getInventory().add(995, 10000).refreshItems();
					
					if (Misc.getRandom(15) == 3) {
						player.getInventory().add(12158, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@500k token");
					}
					if (Misc.getRandom(5) == 1) {
						player.getInventory().add(12159, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@250k token");
					}
					
					if (Misc.getRandom(50) == 28) {
						player.getInventory().add(12160, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@5m token");
					}
					if (Misc.getRandom(5000) == 428) {
						player.getInventory().add(10498, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@Ava's alerter");
					}
					
					
					if (Misc.getRandom(15000) == 6444) {
						player.getInventory().add(15398, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@strange ring");
					}
					if (Misc.getRandom(15000) == 4278) {
						player.getInventory().add(15399, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@strange ring");
					}
					if (Misc.getRandom(15000) == 9674) {
						player.getInventory().add(15400, 1).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@strange ring");
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					if (Misc.getRandom(10) == 0) {
						player.getInventory().add(2360, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 1) {
						player.getInventory().add(2362, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 2) {
						player.getInventory().add(2364, 10).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 3) {
						player.getInventory().add(1514, 10).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					if (Misc.getRandom(10) == 4) {
						player.getInventory().add(1516, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 5) {
						player.getInventory().add(384, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					if (Misc.getRandom(10) == 6) {
						player.getInventory().add(7945, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					if (Misc.getRandom(10) == 0) {
						player.getInventory().add(1518, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 0) {
						player.getInventory().add(1624, 5).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					if (Misc.getRandom(10) == 1) {
						player.getInventory().add(1622, 5).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					if (Misc.getRandom(10) == 2) {
						player.getInventory().add(1620, 5).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(10) == 3) {
						player.getInventory().add(1618, 5).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					
					if (Misc.getRandom(10) == 5) {
						player.getInventory().add(1778, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(15) == 15) {
						player.getInventory().add(1512, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(15) == 14) {
						player.getInventory().add(1522, 15).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					
					if (Misc.getRandom(25) == 24) {
						player.getInventory().add(100, 10).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					
					if (Misc.getRandom(25) == 23) {
						player.getInventory().add(3005, 10).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(25) == 22) {
						player.getInventory().add(112, 10).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded @bla@Skilling supplies");
					}
					
					if (Misc.getRandom(50) == 1) {
						player.getInventory().add(13209, 2).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@Corrupt crystal");
					}
					if (Misc.getRandom(50) == 49) {
						player.getInventory().add(6199, 2).refreshItems();
						player.getPacketSender().sendMessage("@cya@You were awarded a @bla@Mystery box");
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				
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
