package com.skyfire.world.content;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Graphic;
import com.skyfire.model.GraphicHeight;
import com.skyfire.model.GroundItem;
import com.skyfire.model.Item;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.entity.impl.GroundItemManager;
import com.skyfire.world.entity.impl.player.Player;

/*
 * Special Key Event for 2017 (Promotional Event)
 * @author Cade www.runeunity.org
 * 
 */

public class KeysEvent {
	
	//Item ids that will be dropped
	public static int pvmKey = 1543;
	
	//useless, just needed to write down object id
	public static int chest = 6420;

	public static int rareLoots[] = {15606, 15608, 15610, 15612, 15614, 15616, 15618, 15620, 15622, 7671, 7673, 10840, 989, 989, 989, 989, 19335, 6739, 15259, 11720, 11722, 11724, 11726};
	
	public static int ultraLoots[] = {9470, 4084, 9921, 9922, 9923, 9924, 9925, 15352, 5607, 4565, 19670, 20072};
	
	public static int amazingLoots[] = {19028, 19053, 19051, 14484, 20555, 13047, 6500, 2091};
	
	public static int commonLoots[] = {4151, 6585, 11732, 536, 2577, 2581, 6328, 6916, 6918, 6920, 6922, 6924, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4753, 4755, 4757, 4759, 4747, 4749, 4745, 4751, 10025, 6570};
	/*
	 * Start methods below
	 */
	
	
	/*
	 * Grabs a random item from aray
	 */
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	
	/*
	 * Opening the chest with suspense, give reward
	 */
	public static void openChest(Player player) {
		/*if(!p.getClickDelay().elapsed(1000)) 
			return;*/
		if (player.getInventory().contains(1543)) {   
		
			TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(6387));
				player.getPacketSender().sendMessage("You insert the bloody key, and the chest opens...");
				player.getInventory().delete(1543, 1);
				giveReward(player);
				player.getSkillManager().addExperience(Skill.LUCK, 2000 + Misc.getRandom(1000));
				player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
				player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));

				this.stop();
			}
		});
      } else {
		  
    	  player.getPacketSender().sendMessage("You require a @red@Bloody Key@bla@ to open this chest!");
    	  return;
      }
	 
	}
	
	public static void giveReward(Player player) {
		if (Misc.getRandom(20) == 5) { //Rare Item
			int rareDrops = getRandomItem(rareLoots);
			player.getInventory().add(rareDrops, 1);
			World.sendMessage("@or3@[Blood Chest]@bla@ "+player.getUsername()+ " has received a @red@Rare@bla@ from the chest!");
		} else if (Misc.getRandom(225) == 147) {//Ultra Rare items
			World.sendMessage("@or3@[Blood Chest]@bla@ "+player.getUsername()+ " has received an @red@Ultra Rare@bla@ from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
			player.getInventory().add(ultraDrops, 1);
		} else if (Misc.getRandom(400) == 388) {//Amazing items
			World.sendMessage("@or3@[Blood Chest]@bla@ "+player.getUsername()+ " has received a @mag@Legendary Reward @bla@from the chest!!");
			int amazingDrops = getRandomItem(amazingLoots);
			player.getInventory().add(amazingDrops, 1);
		} else {//Common items
			int commonDrops = getRandomItem(commonLoots);
			player.getInventory().add(commonDrops, 1);
		}
			
	}
	
	/*
	 * Handles Skotizo Boss
	 */
	public static void handleSkotizo(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Phoenix Boss
	 */
	public static void handlePhoenix(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Thermo Boss
	 */
	public static void handleThermo(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have receivedved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Slash Bash Boss
	 */
	public static void handleSlashBash(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles KBD Boss
	 */
	public static void handleKBD(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Cerb Boss
	 */
	public static void handleCerb(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Bork Boss
	 */
	public static void handleBork(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Glacor Boss
	 */
	public static void handleGlacor(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Shaman Boss
	 */
	public static void handleShaman(Player player, Position pos) {
		if (Misc.getRandom(50) == 1) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Green Dragon
	 */
	public static void handleGreenDragon(Player player, Position pos) {
		if (Misc.getRandom(195) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Blue Dragon
	 */
	public static void handleBlueDragon(Player player, Position pos) {
		if (Misc.getRandom(195) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Abby Demon
	 */
	public static void handleAbbyDemon(Player player, Position pos) {
		if (Misc.getRandom(195) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}

}
