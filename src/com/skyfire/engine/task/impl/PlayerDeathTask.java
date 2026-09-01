package com.skyfire.engine.task.impl;

import java.util.ArrayList;

import java.util.concurrent.CopyOnWriteArrayList;

import com.skyfire.GameLoader;
import com.skyfire.GameSettings;
import com.skyfire.engine.task.Task;
import com.skyfire.model.Animation;
import com.skyfire.model.Flag;
import com.skyfire.model.GameMode;
import com.skyfire.model.GroundItem;
import com.skyfire.model.Item;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.util.Misc;
import com.skyfire.util.RandomUtility;
import com.skyfire.world.World;
import com.skyfire.world.content.ItemsKeptOnDeath;
import com.skyfire.world.content.minigames.impl.FreeForAll;
import com.skyfire.world.content.skill.SkillManager;
import com.skyfire.world.entity.impl.GroundItemManager;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;

/**
 * Represents a player's death task, through which the process of dying is handled,
 * the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

	/**
	 * The PlayerDeathTask constructor.
	 * @param player	The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	private Player player;
	private int ticks = 5;
	private boolean dropItems = true;
	Position oldPosition;
	Location loc;
	ArrayList<Item> itemsToKeep = null; 
	NPC death;

	@Override
	public void execute() {
		if(player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				player.getMovementQueue().setLockMovement(true).reset();
				break;
			case 3:
				if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
					//player.getPacketSender().sendMessage("Orrrh dear, you died on a hardcore!");
					World.sendMessage("<col=C3032D>[DEATH]: "+player.getUsername()+" has died on Hardcore with total Lvl " + player.getSkillManager().getTotalLevel()+ ", and combat Lvl: " + player.getSkillManager().getCombatLevel());
					GameMode.set(player, GameMode.IRONMAN, true);
					String title = "Fallen Ironman";
					player.setTitle("@or2@" + title);
					title = "Fallen Ironman";
					player.getPacketSender().sendMessage("You recieve a new title...");
					player.getUpdateFlag().flag(Flag.APPEARANCE);}
				player.performAnimation(new Animation(0x900));
				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				this.death = getDeathNpc(player);
				break;
			case 1:
				this.oldPosition = player.getPosition().copy();
				this.loc = player.getLocation();
				if (player.inFFA) {
					Player killer = player.getCombatBuilder().getKiller(true);
					FreeForAll.leaveGame(player);
					killer.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, killer.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
					killer.setSpecialPercentage(100);
				}
				if(loc != Location.DUNGEONEERING && !player.inFFA && loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA && loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT && loc != Location.SOULWARS && loc != Location.FIGHT_PITS && loc != Location.FIGHT_PITS_WAIT_ROOM && loc != Location.FIGHT_CAVES && loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD) {
					Player killer = player.getCombatBuilder().getKiller(true);
					if(player.getUsername().equalsIgnoreCase("Thon") || player.getRights().equals(PlayerRights.DEVELOPER))
						dropItems = false;
					if(loc == Location.WILDERNESS) {
						if(killer != null && ( killer.getRights().equals(PlayerRights.ZENYTE) || killer.getRights().equals(PlayerRights.DEVELOPER)))
							dropItems = false;
					}
					if(loc != Location.WILDERNESS) {
						dropItems = false;
					}
					if(killer != null) {
						if(killer.getRights().equals(PlayerRights.ZENYTE) || killer.getRights().equals(PlayerRights.DEVELOPER)) {
							dropItems = false;
						}
					}
					boolean spawnItems = loc != Location.NOMAD && !(loc == Location.GODWARS_DUNGEON && player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
					if(dropItems) {
						itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
						final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
						playerItems.addAll(player.getInventory().getValidItems());
						playerItems.addAll(player.getEquipment().getValidItems());
						final Position position = player.getPosition();
						for (Item item : playerItems) {
							if(!item.tradeable() || itemsToKeep.contains(item)) {
								if(!itemsToKeep.contains(item)) {
									itemsToKeep.add(item);
								}
								continue;
							}
							if(spawnItems) {
								if(item != null && item.getId() > 0 && item.getAmount() > 0) {
									GroundItemManager.spawnGroundItem((killer != null && killer.getGameMode() == GameMode.NORMAL ? killer : player), new GroundItem(item, position, killer != null ? killer.getUsername() : player.getUsername(), player.getHostAddress(), false, 150, true, 150));
								}
							}
						}
						if (killer != null) {
							killer.getPlayerKillingAttributes().add(player);
							killer.getPlayerKillingAttributes()
									.setPlayerKills(killer.getPlayerKillingAttributes().getPlayerKills()
											+ GameLoader.getDay() == GameLoader.TUESDAY ? 2 : 1);
							player.getPlayerKillingAttributes()
									.setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
							player.getPlayerKillingAttributes().setPlayerKillStreak(0);
							player.getPointsHandler().refreshPanel();
						}
						player.getInventory().resetItems().refreshItems();
						player.getEquipment().resetItems().refreshItems();
					}
				} else
					dropItems = false;
				player.getPacketSender().sendInterfaceRemoval();
				player.setEntityInteraction(null);
				player.getMovementQueue().setFollowCharacter(null);
				player.getCombatBuilder().cooldown(false);
				player.setTeleporting(false);
				player.setWalkToTask(null);
				player.getSkillManager().stopSkilling();
				break;
				
			case 0:
				if(dropItems) {
					if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						GameMode.set(player, player.getGameMode(), true);
						//player.getPacketSender().sendMessage("Orrrh dear, you died on a hardcore!");
						//World.sendMessage("<col=FF0000>[HARDCORE DEATH]: "+player.getUsername()+" has just died on Hardcore Ironman. GG!");
						//GameMode.set(player, GameMode.IRONMAN, true);		
					} else { 
						if(itemsToKeep != null) {
							for(Item it : itemsToKeep) {
								player.getInventory().add(it.getId(), 1);
							}
							itemsToKeep.clear();
						}
					}
				}
				if(death != null) {
					World.deregister(death);
				}
				player.restart();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				loc.onDeath(player);
				if(loc != Location.DUNGEONEERING) {
					if(player.getPosition().equals(oldPosition))
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				}
				player = null;
				oldPosition = null;
				stop();
				break;
			}
			
			ticks--;
		} catch(Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if(player != null) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
				
			
			}	
		}
	}


	public static NPC getDeathNpc(Player player) {
		NPC death = new NPC(2579, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
		World.register(death);
		death.setEntityInteraction(player);
		death.performAnimation(new Animation(401));
		if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			death.forceChat(randomHCDeath(player.getUsername()));
		} else {
		death.forceChat(randomDeath(player.getUsername()));
		}
		return death;
	}
	public static String randomHCDeath(String name) {
		switch (RandomUtility.getRandom(8)) {
		case 0:
			return "HCIM DEATH!! HAHAHAHAHA " + Misc.formatText(name)
					+ "IS MINE";
		case 1:
			return "Another HCIM dies, I've come to eat you!";
		case 2:
			return "I eat HCIM for fun! Everyday! Now you!";
		}
		return "";
	}

	public static String randomDeath(String name) {
		switch (RandomUtility.getRandom(8)) {
		case 0:
			return "Today there is no escape, " + Misc.formatText(name)
					+ "...";
		case 1:
			return "Clearly you were not that good!";
		case 2:
			return "I've come here to claim you as mine!";
		case 3:
			return "Move out of my way, " + Misc.formatText(name)
					+ " comes with me!";
		case 4:
			return "Your time on Skyfire is over, " + Misc.formatText(name)
					+ "!";
		case 5:
			return "Now is the time you die, " + Misc.formatText(name)
					+ "!";
		case 6:
			return "I claim " + Misc.formatText(name) + " as my slave!";
		case 7:
			return "" + Misc.formatText(name) + " is mine! Forever!";
		case 8:
			return "Let me escort you back to Skyfire, "
					+ Misc.formatText(name) + "!";
		case 9:
			return "I have come for you, " + Misc.formatText(name)
					+ "! And I waited long!";
		}
		return "";
	}

}
