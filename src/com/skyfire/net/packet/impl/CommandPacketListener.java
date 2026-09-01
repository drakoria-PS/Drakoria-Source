package com.skyfire.net.packet.impl;

import com.motivoters.motivote.service.MotivoteRS;
import com.ruse.world.content.grandLottery.GrandLottery;
import com.skyfire.SkyFireLauncher;
import com.skyfire.GameSettings;
import com.skyfire.engine.GameEngine;
import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.engine.task.impl.BonusExperienceTask;
import com.skyfire.engine.task.impl.PlayerDeathTask;
import com.skyfire.model.Animation;
import com.skyfire.model.Flag;
import com.skyfire.model.GameMode;
import com.skyfire.model.GameObject;
import com.skyfire.model.Graphic;
import com.skyfire.model.Item;
import com.skyfire.model.Locations;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.container.impl.Bank;
import com.skyfire.model.container.impl.Equipment;
import com.skyfire.model.container.impl.Shop.ShopManager;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.model.definitions.NPCDrops;
import com.skyfire.model.definitions.NpcDefinition;
import com.skyfire.model.definitions.WeaponAnimations;
import com.skyfire.model.definitions.WeaponInterfaces;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.net.security.ConnectionHandler;
import com.skyfire.util.CommandTypes;
import com.skyfire.util.Misc;
import com.skyfire.util.NameUtils;
import com.skyfire.util.RandomUtility;
import com.skyfire.util.StaffCommand;
import com.skyfire.util.banning.BanHammer;
import com.skyfire.util.banning.BanType;
import com.skyfire.world.World;
import com.skyfire.world.content.Achievements;
import com.skyfire.world.content.BonusManager;
import com.skyfire.world.content.CustomObjects;
import com.skyfire.world.content.EvilTrees;
import com.skyfire.world.content.Gambling;
import com.skyfire.world.content.ItemBonuses;
import com.skyfire.world.content.KillsTracker;
import com.skyfire.world.content.Lottery;
import com.skyfire.world.content.MonsterDrops;
import com.skyfire.world.content.PlayerDropLog;
import com.skyfire.world.content.PlayerLogs;
import com.skyfire.world.content.PlayerPanel;
import com.skyfire.world.content.PlayerPunishment;
import com.skyfire.world.content.ProfileViewing;
import com.skyfire.world.content.ShootingStar;
import com.skyfire.world.content.TrioBosses;
import com.skyfire.world.content.TriviaBot;
import com.skyfire.world.content.VotingContest;
import com.skyfire.world.content.WellOfGoodwill;
import com.skyfire.world.content.WellOfWealth;
import com.skyfire.world.content.Achievements.AchievementData;
import com.skyfire.world.content.PlayerPunishment.Jail;
import com.skyfire.world.content.ShootingStar.CrashedStar;
import com.skyfire.world.content.clan.ClanChatManager;
import com.skyfire.world.content.combat.CombatFactory;
import com.skyfire.world.content.combat.DesolaceFormulas;
import com.skyfire.world.content.combat.strategy.CombatStrategies;
import com.skyfire.world.content.combat.strategy.impl.zulrah.GreenZulrah;
import com.skyfire.world.content.combat.weapon.CombatSpecial;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.content.droppreview.KBD;
import com.skyfire.world.content.droppreview.SLASHBASH;
import com.skyfire.world.content.global.GlobalBossHandler;
import com.skyfire.world.content.grandexchange.GrandExchange;
import com.skyfire.world.content.grandexchange.GrandExchangeOffers;
import com.skyfire.world.content.minigames.impl.FreeForAll;
import com.skyfire.world.content.skill.SkillManager;
import com.skyfire.world.content.skill.impl.construction.Construction;
import com.skyfire.world.content.skill.impl.herblore.Decanting;
import com.skyfire.world.content.skill.impl.herblore.decanting.PotionDecanting;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.content.transportation.TeleportType;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;
import com.skyfire.world.entity.impl.player.PlayerHandler;
import com.skyfire.world.entity.impl.player.PlayerSaving;

import mysql.MySQLController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import mysql.impl.Voting;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

	public static int config;
	private static final File CONTESTERS_FILE_LOCATION = new File("./data/saves/voting/votes.txt");
	public static final List<String> CONTESTERS = new ArrayList<String>();
	@Override
	public void handleMessage(Player player, Packet packet) {
		String command = Misc.readString(packet.getBuffer());
		String[] parts = command.toLowerCase().split(" ");
		if (command.contains("\r") || command.contains("\n")) {
			return;
		}
		try {
			switch (player.getRights()) {
			case PLAYER:
				playerCommands(player, parts, command);
				break;
			case MODERATOR:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				break;
			case ADMINISTRATOR:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				break;
			case ONYX:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				//helperCommands(player, parts, command);
				//moderatorCommands(player, parts, command);
				//administratorCommands(player, parts, command);
				//ownerCommands(player, parts, command);
				//developerCommands(player, parts, command);
				break;
			case DEVELOPER:// so wait, let me do it im pretty fast, correct me
							// if im wrong
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				break;
			case SUPPORT:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				break;
			case ZENYTE:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				//helperCommands(player, parts, command);
				//moderatorCommands(player, parts, command);
				//administratorCommands(player, parts, command);
				//ownerCommands(player, parts, command);
			break;
			case SAPPHIRE:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				break;
			case EMERALD:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				break;
			case RUBY:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			case DIAMOND:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			case DRAGONSTONE:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				uberDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			default:
				break;
			}
		} catch (Exception exception) {
			// exception.printStackTrace();

			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendConsoleMessage("Error executing that command.");
			} else {
				player.getPacketSender().sendMessage("Error executing that command.");
			}

		}
	}

	private final static MotivoteRS motivote = new MotivoteRS("RuneUnity", "257ddcadb6e64d66d81462a483bbc2e4"); // replace
																												// API
																												// key
																												// and
																												// subdomain
					public static int VOTES = 0;																							// with
																												// yours.

	private static void playerCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equalsIgnoreCase("pos")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.DUEL_ARENA) {
			player.getPacketSender().sendMessage("You can't open the player shops right now!");
		} else {
				player.getPlayerOwnedShopManager().options();
				return;
			}
		}
		
		if (command[0].equalsIgnoreCase("lottery")) {
			GrandLottery.open(player);
		}
		if (command[0].equals("sponsorzone2")) {
			if (player.getAmountDonated() >= 500) {
				TeleportHandler.teleportPlayer(player, new Position(2974, 2845,4),
						player.getSpellbook().getTeleportType());
		}
	}
		if (command[0].equalsIgnoreCase("checkgold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable())
						gold += item.getDefinition().getValue();
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable())
						gold += item.getDefinition().getValue();
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable())
							gold += item.getDefinition().getValue();
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else
				player.getPacketSender().sendMessage("Can not find player online.");
		}

		
		if (command[0].equals("staff")) {
			player.getOnlineStaff();
			player.getPA().sendMessage("@mag@<shad=0>Online Owners: <img=4>" + player.onlineOwners);
			player.getPA().sendMessage("@cya@<shad=0>Online Admins: <img=2>" + player.onlineAdmins);
			player.getPA().sendMessage("@mag@<shad=0>Online Developers: <img=4>" + player.onlineDevelopers);
			player.getPA().sendMessage("@cya@<shad=0>Online Mods: <img=1>" + player.onlineMods);
			player.getPA().sendMessage("@mag@<shad=0>Online Server Support: <img=10>" + player.onlineServerSupport);
			}
		
		
		if (command[0].equalsIgnoreCase("lottery")) {
			GrandLottery.open(player);
		}
		
		
		
		
		  if (command[0].equalsIgnoreCase("dropsim")) {
	            player.getDropSimulator().open();
	        }
		
		
		
		if (command[0].equals("dailyboss")) {
		if (player.freeDediBoss > System.currentTimeMillis()) {
			player.sendMessage("@cya@<shad=0>You need to wait a full 24 hours before using this command!");
			return;
		}
		TeleportHandler.teleportPlayer(player, new Position(1675, 5599, 0),
				player.getSpellbook().getTeleportType());
		NPC n = new NPC(1001, new Position(1695, 5600, 0));
		World.register(n);
		n.setSpawnedFor(player);
		player.freeDediBoss = System.currentTimeMillis() + ((60_000 * 60) * 24);
		player.sendMessage("This is your only entry to this boss today.");
		player.sendMessage("You cannot get back here once you kill it for 24hours!");
		player.sendMessage("The boss has 500,000 HP proceed follow the path to begin!");
		return;
		}
		if (command[0].equals("dailyrewardex")) {

		if (player.freeDTD > System.currentTimeMillis()) {
			player.sendMessage("@gr2@<shad=0>You need to wait a full 24 hours before using this command!");
			return;
		}

		final int DTD = 6199, QUANTITY = 5;
		boolean space = player.getInventory().getFreeSlots() > 0 || player.getInventory().contains(DTD);

		if (space)
			player.getInventory().add(DTD, QUANTITY);
		else
			player.getBank().add(new Item(DTD, QUANTITY));

		player.freeDTD = System.currentTimeMillis() + ((60_000 * 60) * 24);
		player.sendMessage("@gr2@<shad=0>You have sucessfully claimed your 5 free Mystery box!");
		player.sendMessage("@gr2@<shad=0>they have been added to your " + (space ? "inventory" : "bank") + ".");
		player.sendMessage("@gr2@<shad=0>You can use this command again in another 24hours!");
		World.sendMessage("@mag@<shad=0>[DAILY] " + player.getUsername()+ " just claimed their :: dailyreward! Goodluck");
		
		return;
		}
		if (command[0].equalsIgnoreCase("whatdrops")) {
			try {
				boolean isItem = false;
				int itemId = 0;
				String target = wholeCommand.substring(command[0].length() + 1);
				player.getPacketSender().sendMessage("Finding what drops \"" + target + "\".");

				for (int i = 0; i < ItemDefinition.getDefinitions().length; i++) {
					if (ItemDefinition.forId(i) == null || ItemDefinition.forId(i).getName() == null) {
						continue;
					}
					if (ItemDefinition.forId(i).getName().toLowerCase().equalsIgnoreCase(target)) {
						isItem = true;
						itemId = i;
						break;
					}
				}
				if (!isItem) {
					player.getPacketSender().sendMessage("No item named \"" + target + "\" was found.");
					return;
				} /*
					 * else { player.getPacketSender().sendMessage("Found the item"); }
					 */

				for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
					if (NpcDefinition.forId(i) == null || NpcDefinition.forId(i).getName() == null
							|| NPCDrops.forId(i) == null || NPCDrops.forId(i).getDropList() == null
							|| NPCDrops.forId(i).getDropList().length <= 0) {
						// System.out.println("fuck sake");
						continue;
					}

					for (int q = 0; q < NPCDrops.forId(i).getDropList().length; q++) {
						if (ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getItem().getId()) == null
								|| ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getItem().getId())
										.getName() == null) {
							// System.out.println("what the hell");
							continue;
						}
						// System.out.println(ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getId()).getName()+"
						// VS "+ItemDefinition.forId(itemId).getName());

						if (ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getId()).getName()
								.equalsIgnoreCase(ItemDefinition.forId(itemId).getName())) {
							player.getPacketSender()
									.sendMessage(NpcDefinition.forId(i).getName() + " drops " + target + ".");
							continue;
						}
					}
				}

				/*
				 * for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) { if
				 * (NpcDefinition.forId(i) == null || NpcDefinition.forId(i).getName() == null
				 * || NPCDrops.forId(i) == null || NPCDrops.forId(i).getDropList() == null ||
				 * NPCDrops.forId(i).getDropList().length >= 0) { continue; }
				 * 
				 * for (int q = 0; q < NPCDrops.forId(i).getDropList().length; q++) { if
				 * (ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getItem().getId())
				 * == null ||
				 * ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getItem().getId()).
				 * getName() == null) { continue; } if
				 * (ItemDefinition.forId(NPCDrops.forId(i).getDropList()[q].getItem().getId()).
				 * getName().toLowerCase().equalsIgnoreCase(target)) {
				 * player.getPacketSender().sendMessage(NpcDefinition.forId(i).getName()
				 * +" drops "+target+"."); continue; } } }
				 */

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (command[0].equalsIgnoreCase("time") || command[0].equalsIgnoreCase("date")
				|| command[0].equalsIgnoreCase("clock")) {
			int month = 1 + Misc.getMonth();
			String mo = Integer.toString(month);
			String dd = Integer.toString(Misc.getDayOfMonth());
			String weekend = "";

			if (Misc.getDayOfMonth() < 10) {
				dd = "0" + dd;
			}

			if (month < 10) {
				mo = "0" + mo;
			}

			if (Misc.isWeekend()) {
				weekend = ". It's the weekend";
			}

			player.getPacketSender().sendMessage("@cya@<shad=0>[Time] <shad=-1>@bla@[" + mo + "/" + dd + "] (MM/DD) @ "
					+ Misc.getCurrentServerTime() + " (24:00) in New York City, USA" + weekend + ".");
		}
		
		
		if (command[0].equalsIgnoreCase("referral") || command[0].equalsIgnoreCase("12referred") || command[0].equalsIgnoreCase("refer") || command[0].equalsIgnoreCase("referredby")) {
			String ref = wholeCommand.substring(command[0].length()+1);
			//String reff = command[1];
			Player player2 = World.getPlayerByName(ref);
			if(player2 == null) {
				player.getPacketSender().sendMessage(ref+" returned errors. Check name? They must be online to complete a referral.");
				return;
			} else if(player.getUsername().equals(player2.getUsername())) {
				player.getPacketSender().sendMessage("Uhm... You can't refer yourself.");
				return;
			} else if(player.getHostAddress().equals(player2.getHostAddress())){ 
				World.sendMessage("<img=10> @mag@<shad=0> [Refs] "+player.getUsername()+" and "+player2.getUsername()+" tried to reffer eachother on the same IP!");
				World.sendStaffMessage("<img=10> @mag@<shad=0> [Refs] "+player.getUsername()+"'s IP: "+player.getHostAddress()+" | "+player2.getUsername()+"'s IP: "+player2.getHostAddress());
				World.sendMessage("<img=10> @red@<shad=0>Self reffering is against the rules. This action has been logged.");
				//player2.getPacketSender().sendMessage("<img=10> @red@<shad=0>Self reffering is against the rules. This action has been logged.");
				PlayerLogs.log("1 - refboosts", player.getUsername()+" tried to set their refferer to "+player2.getUsername()+" on the same IP! IP: "+player.getHostAddress());
				return;
			} else if(player.gotReffered() == true) {
				player2.getPacketSender().sendMessage(player.getUsername()+"  just tried to set their refferrer to you, but failed as they were already reffered.");
				player.getPacketSender().sendMessage(player2.getUsername()+" could not be set as your refferrer, because you have already been reffered.");
				return;
			} else if(player.getInventory().getFreeSlots() == 0 && player2.getInventory().getFreeSlots() == 0) {
				player.getPacketSender().sendMessage("Both you and "+player2.getUsername()+" need 1 free slot to finish the referral.");
				player2.getPacketSender().sendMessage(player.getUsername()+" tried to reffer you, but it failed because you both need a free inventory space.");
				return;
			} else {
			player.getPacketSender().sendMessage(player2.getUsername()+" was found! Do referral method!");
			BonusExperienceTask.addBonusXp(player, 60);
			
			BonusExperienceTask.addBonusXp(player2, 30);
			player.setReffered(true);
			player.getPacketSender().sendMessage("You have successfully been reffered by "+player2.getUsername());
			player2.getPacketSender().sendMessage(player.getUsername()+" has succesfully been reffered by you.");
			World.sendMessage("<img=10><shad=0> @mag@[Ref] @bla@"+player.getUsername()+" has set their refferrer to "+player2.getUsername());
			PlayerLogs.log("1 - realreferrals", player.getUsername()+" set their refferrer to "+player2.getUsername()+" successfully.");
			}
		}
		
		
		
		
		
		
		
		
		
		if (command[0].equalsIgnoreCase("totalvotes")) {
			
			World.sendMessage("Currentlty " + VOTES);
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if (command[0].equalsIgnoreCase("reward")) {
			new Thread() {
				public void run() {
					try {
						int id = Integer.parseInt(command[1]);
						String playerName = player.getUsername();
						final String request = com.everythingrs.vote.Vote.validate("yl47gos48090yl2ejs40wwmipu2jd57xwtcza4fnus7b3ayvibgpze3n46taic818hhfc1sjor", playerName, id);
						String[][] errorMessage = {
								{ "error_invalid", "There was an error processing your request." },
								{ "error_non_existent_server", "This server is not registered at EverythingRS." },
								{ "error_invalid_reward", "The reward you're trying to claim doesn't exist" },
								{ "error_non_existant_rewards", "This server does not have any rewards set up yet." },
								{ "error_non_existant_player", "There is not record of user " + playerName + " make sure to vote first" },
								{ "not_enough", "You do not have enough vote points to receive this item" } };
						for (String[] message : errorMessage) {
							if (request.equalsIgnoreCase(message[0])) {
								player.getPacketSender().sendMessage(message[1]);
								return;
							}
						}
						if (request.startsWith("complete")) {
							BufferedWriter writer = new BufferedWriter(new FileWriter(CONTESTERS_FILE_LOCATION, true));
				    		writer.write(""+getName()+"");
				    		writer.newLine();
				    		writer.close();
							int item = Integer.valueOf(request.split("_")[1]);
							int amount = Integer.valueOf(request.split("_")[2]);
							String itemName = request.split("_")[3];
							int remainingPoints = Integer.valueOf(request.split("_")[4]);
							player.getInventory().add(new Item(item, amount));
							World.sendMessage("<img=10><col=008FB2>" +player.getUsername() + " <col=008FB2>has just voted! Type ::vote to get your rewards!");
							player.getSkillManager().addExperience(Skill.LUCK, 5000 + Misc.getRandom(30));
							player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
						player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
					
							player.getPacketSender().sendMessage("You have received the item " + itemName + ". You have " + remainingPoints
									+ " points left.");
						

						}
					} catch (Exception e) {
						player.getPacketSender().sendMessage("Our API services are currently offline. We are working on bringing it back up");
						e.printStackTrace();
					}
				}
			}.start();
		}
		if(command[0].equalsIgnoreCase("irontitle")) {
			if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				String title = "Hardcore Ironman";
				player.setTitle("@or2@" + title);
				title = "Hardcore Ironman";
				player.getPacketSender().sendMessage("@red@Your title switched to @dre@Hardcore Ironman");
				player.getPacketSender().sendMessage("You recieve a new title...");
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				return;
			}
		if(player.getGameMode() == GameMode.IRONMAN) {
			String title = "Ironman";
			player.setTitle("@or2@" + title);
			title = "Ironman";
			player.getPacketSender().sendMessage("@red@Your title switched to @dre@Ironman");
			player.getPacketSender().sendMessage("You recieve a new title...");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
				return;
		}
			
			
		player.getPacketSender().sendMessage("@red@You must be some sort of Ironman to claim your title!");
		return;
			}
		
		
		if(command[0].equalsIgnoreCase("ffa")) {
			FreeForAll.enterLobby(player);
		}
		if (command[0].equalsIgnoreCase("npckills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKills(player) + " NPC kills.");
		}
		if (command[0].equalsIgnoreCase("tdkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(8349, player) + " Tormented Demon kills.");
		}
		if (command[0].equalsIgnoreCase("dustkills")) {
			player.getPacketSender().sendMessage("@blu@You currently have " + KillsTracker.getTotalKillsForNpc(1624, player) + " Dust Devil kills.");
		}
		if (command[0].equalsIgnoreCase("thermykills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(499, player) + " Thermy kills.");
		}
		if (command[0].equalsIgnoreCase("skotizokills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(7286, player) + " Skotizo kills.");
		}
		if (command[0].equalsIgnoreCase("kbdkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(50, player) + " KBD kills.");
		}
		if (command[0].equalsIgnoreCase("kqkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(1160, player) + " KQ kills.");
		}
		if (command[0].equalsIgnoreCase("slashkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(2060, player) + " Slash Bash kills.");
		}
		if (command[0].equalsIgnoreCase("cerbkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(1999, player) + " Cerberus kills.");
		}
		if (command[0].equalsIgnoreCase("phoenixkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(8549, player) + " Phoenix kills.");
		}
		if (command[0].equalsIgnoreCase("shamankills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(6766, player) + " Shaman kills.");
		}
		if (command[0].equalsIgnoreCase("dwolfkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(4413, player) + " Dire Wolf kills.");
		}
		if (command[0].equalsIgnoreCase("glacorkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(1382, player) + " Glacor kills.");
		}
		if (command[0].equalsIgnoreCase("yetikills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(130, player) + " Yeti kills.");
		}
		if (command[0].equalsIgnoreCase("cyrisuskills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(433, player) + " Cyrisus kills.");
		}
		if (command[0].equalsIgnoreCase("frostkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(51, player) + " Frost Dragon kills.");
		}
		if (command[0].equalsIgnoreCase("nazakills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(509, player) + " Nazastarool kills.");
		}
		if (command[0].equalsIgnoreCase("abbadonkills")) {
			player.getPacketSender().sendMessage("@red@You currently have " + KillsTracker.getTotalKillsForNpc(6303, player) + " Abbadon kills.");
		}
		if (command[0].equalsIgnoreCase("claim")) {
			if (player.getInventory().getFreeSlots() < 15) {
				player.getPacketSender().sendMessage("You need atleast 15 free slots to claim your donation!");
				return;
			}
			new Thread() {
				public void run() {
					try {
						com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("yl47gos48090yl2ejs40wwmipu2jd57xwtcza4fnus7b3ayvibgpze3n46taic818hhfc1sjor", 
								player.getUsername());
						if (donations.length == 0) {
							player.getPacketSender().sendMessage("You currently don't have any items waiting. You must donate first!");
							return;
						}
						if (donations[0].message != null) {
							player.getPacketSender().sendMessage(donations[0].message);
							return;
						}
						for (com.everythingrs.donate.Donation donate : donations) {
							player.getInventory().add(new Item(donate.product_id, donate.product_amount));
						}
						World.sendStaffMessage("[LOG] You got a new donation from " + player.getUsername() + ", YAY! ");
						
						player.getPacketSender().sendMessage("Thank you for donating!");
						World.sendMessage("<img=10><col=D800FF>" +player.getUsername() + " <col=D800FF>has just donated! Thank you ! Type ::donate!");
						player.getSkillManager().addExperience(Skill.LUCK, 10000 + Misc.getRandom(30));
						player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
					player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
				
						//World.sendMessage("<img=10> <col=008FB2>Thank you PLAYERNAME for Donating! <3");

					} catch (Exception e) {
						player.getPacketSender().sendMessage("Api Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}	
				}
			}.start();
		}
				if(command[0].equals("players")) {
		      player.getPacketSender().sendMessage("There are currently " + World.getPlayers().size() * 1 + " players online!");
		}
		if (command[0].equals("headlessbeast") || command[0].equals("headlessbeast")) {
			TeleportHandler.teleportPlayer(player, new Position(2766, 2799),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("edgeville")) {
			TeleportHandler.teleportPlayer(player, new Position(3087, 3503),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("abyzou")) {
			TeleportHandler.teleportPlayer(player, new Position(3105, 5534),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("falador")) {
			TeleportHandler.teleportPlayer(player, new Position(2966, 3381),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("camelot")) {
			TeleportHandler.teleportPlayer(player, new Position(2731, 3485),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("lumbridge")) {
			TeleportHandler.teleportPlayer(player, new Position(3236, 3218),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("draynor")) {
			TeleportHandler.teleportPlayer(player, new Position(3094, 3248),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("portsarim")) {
			TeleportHandler.teleportPlayer(player, new Position(3041, 3252),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("rimmington")) {
			TeleportHandler.teleportPlayer(player, new Position(2957, 3215),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("alkharid")) {
			TeleportHandler.teleportPlayer(player, new Position(3292, 3185),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("ardougne")) {
			TeleportHandler.teleportPlayer(player, new Position(2662, 3305),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("yanille")) {
			TeleportHandler.teleportPlayer(player, new Position(2606, 3093),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("direwolf")) {
			TeleportHandler.teleportPlayer(player, new Position(3110, 9518),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("direwolf2")) {
			TeleportHandler.teleportPlayer(player, new Position(3110, 9518, 4),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("direwolf3")) {
			TeleportHandler.teleportPlayer(player, new Position(3110, 9518, 8),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("triobros")) {
			TeleportHandler.teleportPlayer(player, new Position(2522, 3019, 0),
					player.getSpellbook().getTeleportType());
		}
		
		//if (command[0].equals("voteboss")) {
		//	Voting.handleVotingBoss(player);
		//}
		if (command[0].equalsIgnoreCase("redeem")) {
			String auth = command[1];
			boolean success = motivote.redeemVote(auth);

			if (success) {
				player.getInventory().add(995, 1500000); // replace 995,
				player.getInventory().add(19670, 1);
				player.getPacketSender().sendMessage("Auth redeemed, thanks for voting!");
			} else {
				player.getPacketSender().sendMessage("Invalid auth supplied, please try again later.");
			}
		}
		if (command[0].equalsIgnoreCase("bork")) {
			TeleportHandler.teleportPlayer(player, new Position(3104, 5537), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("cyrisus")) {
			TeleportHandler.teleportPlayer(player, new Position(2914, 4446, 4), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("abbadon")) {
			TeleportHandler.teleportPlayer(player, new Position(2496, 5175, 0), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("yeti")) {
			TeleportHandler.teleportPlayer(player, new Position(2835, 3816, 0), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("123451ge")) {
			GrandExchange.open(player);
		}
		if (command[0].equals("tree")) {
			player.getPacketSender().sendMessage("<img=4> <shad=1><col=FF9933> The Evil Tree has sprouted at: "
					+ EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
		}
		if (wholeCommand.startsWith("profile")) {
			final String[] s = wholeCommand.split(" ");
			if (s.length < 2) {
				ProfileViewing.view(player, player);
				return;
			}
			final String name = wholeCommand.substring(8);
			final Player other = World.getPlayerByName(name);
			if (other == null) {
				player.sendMessage("Player not online: " + name);
				return;
			}
			ProfileViewing.view(player, other);
		}

		if (command[0].equals("decant")) {
			// PotionDecanting.decantPotions(player);
			Decanting.startDecanting(player);
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}

		if (wholeCommand.startsWith("droplog")) {
			final String[] s = wholeCommand.split(" ");
			if (s.length < 2) {
				PlayerDropLog.sendDropLog(player, player);
				return;
			}
			final String name = wholeCommand.substring(8);
			final Player other = World.getPlayerByName(name);
			if (other == null) {
				player.sendMessage("Player not found: " + name);
				return;
			}
			PlayerDropLog.sendDropLog(player, other);
		} else if (wholeCommand.startsWith("drop") && !wholeCommand.startsWith("droplog")) {
			final String[] s = wholeCommand.split(" ");
			if (s.length < 2) {
				player.sendMessage("Enter npc name!");
				return;
			}
			final String name = wholeCommand.substring(5).toLowerCase();

			final int id = NpcDefinition.forName(name).getId();
			if (id == -1) {
				player.sendMessage("Npc not found: " + name);
				return;
			}
			MonsterDrops.sendNpcDrop(player, id, name);
		}
		if (command[0].equalsIgnoreCase("answer")) {
			String triviaAnswer = wholeCommand.substring(7);
			if (TriviaBot.acceptingQuestion()) {
				TriviaBot.attemptAnswer(player, triviaAnswer);
			} else {

			}
		}
		
		//if (command[0].equals("zul")) {
		//	if (player.getLastZulrah().elapsed(600000)) {
		//	GreenZulrah.spawn(player, 1, 5000);
		//	player.moveTo(new Position(2268, 3070, player.getIndex() * 4));
		//	player.getLastZulrah().reset();
		//} else {
		//	player.getPacketSender().sendMessage("You can only teleport here every 10 minutes!");
		//}
		//}
		if (wholeCommand.equalsIgnoreCase("rules") || wholeCommand.equalsIgnoreCase("gamerules")) {
			player.getPacketSender().sendString(1, "http://skyfire317.com/forums/showthread.php?42-SkyFire317-Official-Rules");
			player.getPacketSender().sendMessage("Attempting to open: Skyfire Official Rules, if doesnt work try ::thread 42");
			//Achievements.finishAchievement(player, AchievementData.OPEN_UP_THE_FORUMS);
			
			
			
			
		}
		
		if (wholeCommand.equalsIgnoreCase("prices") || wholeCommand.equalsIgnoreCase("gameprices")) {
			player.getPacketSender().sendString(1, "http://skyfire317.com/forums/showthread.php?52-SkyFire317-Price-Guide&p=135");
			player.getPacketSender().sendMessage("Attempting to open: Skyfire Official Price Guide, if doesnt work try ::thread 52");
			//Achievements.finishAchievement(player, AchievementData.OPEN_UP_THE_FORUMS);
			
			
			
			
		}
		if (command[0].equalsIgnoreCase("gamble") || command[0].equalsIgnoreCase("dice")|| command[0].equalsIgnoreCase("gamblezone")||command[0].equalsIgnoreCase("dicezone")|| command[0].equalsIgnoreCase("dz")) {
			long gold = 0;
			for (Item item : player.getInventory().getItems()) {
				if (item != null && item.getId() > 0 && item.tradeable()) {
					gold += item.getDefinition().getValue();
				}
			}
			for (Item item : player.getEquipment().getItems()) {
				if (item != null && item.getId() > 0 && item.tradeable()) {
					gold += item.getDefinition().getValue();
				}
			}
			for (int i = 0; i < 9; i++) {
				for (Item item : player.getBank(i).getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
			}
			gold += player.getMoneyInPouch();
			World.sendMessage("<col=6EFF00><shad=1>[" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
					+ "><col=6EFF00><shad=1></shad></col>" + "<col=6EFF00><shad=1>"+player.getUsername() +"]</shad></col>" +"<col=6EFF00><shad=1> has teleported to ::gamble with " +Misc.insertCommasToNumber(String.valueOf(gold)) + " coins");
			TeleportHandler.teleportPlayer(player, new Position(2734, 3470, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@red@Video evidence is required to file a report.");
			player.getPacketSender().sendMessage("@red@Only Staff + Ranked Players in 'Dice' Can Middleman! No MM = No Refunds.");
		}
		if (wholeCommand.equalsIgnoreCase("forum") || wholeCommand.equalsIgnoreCase("forums")) {
			player.getPacketSender().sendString(1, "http://Skyfire317.com/forums");
			player.getPacketSender().sendMessage("Attempting to open: Skyfire Forums");
			//Achievements.finishAchievement(player, AchievementData.OPEN_UP_THE_FORUMS);
			
			
			
			
		}
		if (command[0].equals("zombie") || command[0].equals("zombies")) {
			TeleportHandler.teleportPlayer(player, new Position(3503, 3563),
					player.getSpellbook().getTeleportType());
		}
		
		
		if (wholeCommand.equalsIgnoreCase("news") || wholeCommand.equalsIgnoreCase("new")) {
			player.getPacketSender().sendString(1, "http://skyfire317.com/forums/forumdisplay.php?2-News-amp-Announcements");
			player.getPacketSender().sendMessage("Make Sure To Read It All!");
		}
		if (wholeCommand.equalsIgnoreCase("benefits") || wholeCommand.equalsIgnoreCase("benefit")) {
			player.getPacketSender().sendString(1, "http://skyfire317.com/forums/showthread.php?13-Donator-Benefits!");
			player.getPacketSender().sendMessage("Make Sure To Read It All!");
		}
	
		if (wholeCommand.equalsIgnoreCase("discord") || wholeCommand.equalsIgnoreCase("disc")) {
			player.getPacketSender().sendString(1, "http://discord.gg/sWub5nc");
			player.getPacketSender().sendMessage("attempting to open Discord");
		}

		/*
		 * Sql commands start
		 */

		if (command[0].equalsIgnoreCase("auth")) {
			if (!GameSettings.MYSQL_ENABLED) {
				player.getPacketSender().sendMessage("Unable to claim because voting is toggled off by Skyfire");
				return;
			}
			if (player.getLastSql().elapsed(7000)) {
				String auth = wholeCommand.substring(5);

				if (player.getInventory().getFreeSlots() < 4) {
					player.getPacketSender().sendMessage("You need atleast 4 free slots to open your reward!");
					return;
				}
				new Thread().start();
			} else {
				player.getPacketSender().sendMessage("Causing dcs, will be back soon");

			}
			player.getLastSql().reset();

		}
		
	
	

		/*
		 * End of sql commands
		 */

		if (command[0].equalsIgnoreCase("thread")) {
			String threadId = wholeCommand.substring(7);
			player.getPacketSender().sendMessage("Opening forums thread id: " + threadId);
			player.getPacketSender().sendString(1, "http://skyfire317.com/forums/showthread.php?" + threadId);
		}
		if (command[0].equalsIgnoreCase("train")) {
			TeleportHandler.teleportPlayer(player, new Position(2679, 3714), player.getSpellbook().getTeleportType());

		}

		if (command[0].equalsIgnoreCase("commands")) {

			player.getPacketSender().sendMessage(":: thread (#) - opens a forums thread");
			player.getPacketSender().sendMessage(":: help - contacts staff for help");
			player.getPacketSender().sendMessage(":: home - teleports you to home area");
			player.getPacketSender().sendMessage(":: gamble - teleports you to the gamble area");
			player.getPacketSender().sendMessage(":: vote - opens vote page");
			player.getPacketSender().sendMessage(":: setemail - sets an email on your account");
			player.getPacketSender().sendMessage(":; donate - opens donate page");
			//player.getPacketSender().sendMessage(":: auth (code) - claims voting auth");
			player.getPacketSender().sendMessage(":: claim - claims a donation");
			player.getPacketSender().sendMessage(":: train - teleports you to rock crabs");
			player.getPacketSender().sendMessage(":: attacks - shows your max hits");
			player.getPacketSender().sendMessage(":: empty - empties your entire inventory");
			player.getPacketSender().sendMessage(":: answer (answer) - answers the trivia");
			player.getPacketSender().sendMessage(":; skull - skulls your player");
			player.getPacketSender().sendMessage(":; drop (npc name) - opens drop list of npc");
			player.getPacketSender().sendMessage(":; bosscommands - shows you the list of boss commands");
			player.getPacketSender().sendMessage(":; irontitle - claims your ironman title");

		}
		if (command[0].equalsIgnoreCase("bosscommands")) {

			player.getPacketSender().sendMessage(":: cyrisus");
			player.getPacketSender().sendMessage(":: direwolf");
			player.getPacketSender().sendMessage(":: abbadon");
			player.getPacketSender().sendMessage(":: triobros");

		}

		if (command[0].equalsIgnoreCase("setemail")) {
			String email = wholeCommand.substring(9);
			player.setEmailAddress(email);
			player.getPacketSender().sendMessage("You set your account's email adress to: [" + email + "] ");
			// Achievements.finishAchievement(player,
			// AchievementData.SET_AN_EMAIL_ADDRESS);
			PlayerPanel.refreshPanel(player);
		}

		if (command[0].equalsIgnoreCase("changepassword")) {
			String syntax = wholeCommand.substring(15);
			if (syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
				player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
				return;
			}
			if (syntax.contains("_")) {
				player.getPacketSender().sendMessage("Your password can not contain underscores.");
				return;
			}
			player.setPassword(syntax);
			player.getPacketSender().sendMessage("Your new password is: [" + syntax + "] Write it down!");

		}

		if (command[0].equalsIgnoreCase("home2")) {
			TeleportHandler.teleportPlayer(player, new Position(3208, 3426), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("home")) {
			TeleportHandler.teleportPlayer(player, new Position(2319, 3673), player.getSpellbook().getTeleportType());
		}
		
		if (command[0].equalsIgnoreCase("duel")) {
			if(player.getSummoning().getFamiliar() != null) {
				player.getPacketSender().sendMessage("You must dismiss your familiar before teleporting to the arena!");
		} else {
				TeleportHandler.teleportPlayer(player, new Position(3364, 3267), player.getSpellbook().getTeleportType());
		}
		}
		if (wholeCommand.equalsIgnoreCase("donate") || wholeCommand.equalsIgnoreCase("store")) {
			player.getPacketSender().sendString(1, "http://skyfire317.everythingrs.com/services/store");
			player.getPacketSender().sendMessage("Attempting to open: Skyfire Donation Page");
		
			World.sendStaffMessage("[LOG] - " + player.getUsername() + " is attempting to open :: donate");
		
		}
		if (command[0].equalsIgnoreCase("maxhit")) {
			int attack = DesolaceFormulas.getMeleeAttack(player) / 10;
			int range = DesolaceFormulas.getRangedAttack(player) / 10;
			int magic = DesolaceFormulas.getMagicAttack(player) / 10;
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
					+ range + "@bla@, magic attack: @or2@" + magic);
		}
		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}
		if (command[0].equals("vote")) {
			World.sendStaffMessage("[LOG] - " + player.getUsername() + " is attempting to ::vote ");
			
			player.getPacketSender().sendString(1, "http://skyfire317.everythingrs.com/services/vote");
			player.getPacketSender().sendMessage("Attempting to open Skyfire voting");
		}
		if (command[0].equals("help")) {
			if (player.getLastYell().elapsed(30000)) {
				World.sendStaffMessage("<col=FF0066><img=10> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
						+ " has requested help. Please help them!");
				player.getLastYell().reset();
				player.getPacketSender()
						.sendMessage("<col=663300>Your help request has been received. Please be patient.");
			} else {
				player.getPacketSender().sendMessage("")
						.sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage(
								"<col=663300>If it's an emergency, please private message a staff member directly instead.");
			}
		}
		if (command[0].equals("empty")) {
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
			player.getSkillManager().stopSkilling();
			player.getInventory().resetItems().refreshItems();
		}

		if (command[0].equalsIgnoreCase("[cn]")) {
			if (player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
		}
	}

	private static void superDonator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dzone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.DRAGONSTONE
					|| player.getRights() == PlayerRights.DIAMOND
					|| player.getRights() == PlayerRights.RUBY
					|| player.getRights() == PlayerRights.EMERALD || player.getRights() == PlayerRights.SAPPHIRE)
				TeleportHandler.teleportPlayer(player, new Position(3363, 9638),
						player.getSpellbook().getTeleportType());
		}
	}

	private static void uberDonator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dragonzone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.DRAGONSTONE)
				TeleportHandler.teleportPlayer(player, new Position(2408, 4724), player.getSpellbook().getTeleportType());
		}
	}
	

	
	private static void legendaryDonator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("title")) {}
		if (command[0].equals("diamondzone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.DRAGONSTONE
					|| player.getRights() == PlayerRights.DIAMOND)
				TeleportHandler.teleportPlayer(player, new Position(2313, 9810),
						player.getSpellbook().getTeleportType());
		}
	}

	private static void extremeDonator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("bank")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
					|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER
					|| player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.getBank(player.getCurrentBankTab()).open();
		}

		if (command[0].equals("rubyzone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.DRAGONSTONE
					|| player.getRights() == PlayerRights.DIAMOND
					|| player.getRights() == PlayerRights.RUBY)
				TeleportHandler.teleportPlayer(player, new Position(3429, 2919),
						player.getSpellbook().getTeleportType());
		}
	}

	private static final String[] admin = { "admin", "administrator", "a d m i n" };
	private static final String[] mod = { "mod", "moderator", "m o d" };
	
	private static void youtuberCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1].replaceAll("18199", "1"));
			int amount = (command.length == 2 ? 1
					: Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
							.replaceAll("b", "000000000")));
			if (amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if (command[0].equals("bank")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
					|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER
					|| player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set your " + skill.getName() + " level to " + level);
		}
	}
	private static void memberCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dzone")) {
			TeleportHandler.teleportPlayer(player, new Position(3363, 9638), player.getSpellbook().getTeleportType());
		}

		if (wholeCommand.toLowerCase().startsWith("yell")) {
			if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			int delay = player.getRights().getYellDelay();
			if (!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage(
						"You must wait at least " + delay + " seconds between every yell-message you send.");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());

			player.getLastYell().reset();
			// if (player.getUsername().equalsIgnoreCase("levi")) {
			// World.sendMessage("" + player.getRights().getYellPrefix() +
			// "<img=" + player.getRights().ordinal()
			// + ">@red@ [DEVELOPER] @bla@" + player.getUsername() + ":" +
			// yellMessage);
			// return;
			// }

			if (player.getRights() == PlayerRights.ADMINISTRATOR && player.getUsername().equalsIgnoreCase("")) {
				World.sendMessage("<col=DAFF08><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=DAFF08><shad=1></shad></col>"  + "<col=DAFF08><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=DAFF08><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.ONYX) {
				World.sendMessage("<col=121010><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=121010><shad=1></shad></col>"  + "<col=121010><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=121010><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.DEVELOPER) {
				World.sendMessage("<col=DDA205><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=DDA205><shad=1></shad></col>"  + "<col=DDA205><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=DDA205><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.SUPPORT) {
				World.sendMessage("<col=00B8FF><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=00B8FF><shad=1></shad></col>"  + "<col=00B8FF><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=00B8FF><shad=1>" + yellMessage);
				return;
			}

			if (player.getRights() == PlayerRights.MODERATOR) {
				World.sendMessage("<col=ADBABE><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=ADBABE><shad=1></shad></col>" + "<col=ADBABE><shad=1>"+player.getUsername() +"]</shad></col>" +":<col=ADBABE><shad=1>"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.ADMINISTRATOR) {
				World.sendMessage("<col=EBFF00><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=EBFF00><shad=1></shad></col>" + "<col=EBFF00><shad=1>"+player.getUsername() +"]</shad></col>" +":<col=EBFF00><shad=1>"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.DRAGONSTONE) {
				World.sendMessage("<col=9673D7><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=9673D7><shad=1></shad></col>"  + "<col=9673D7><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=9673D7><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.DIAMOND) {
				World.sendMessage("<col=FFFFFF><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=FFFFFF><shad=1></shad></col>" + "<col=FFFFFF><shad=1>"+player.getUsername() +"]</shad></col>" +":<col=FFFFFF><shad=1>"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.RUBY) {
				World.sendMessage("<col=EB2A2A><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=EB2A2A><shad=1></shad></col>" + "<col=EB2A2A><shad=1>"+player.getUsername() +"]</shad></col>" +":<col=EB2A2A><shad=1>"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.EMERALD) {
				World.sendMessage("<col=24CF31><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=24CF31><shad=1></shad></col>@bla@"  + "<col=24CF31><shad=1>"+ player.getUsername() + "]: <col=24CF31><shad=1>"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.SAPPHIRE) {
				World.sendMessage("<col=2188FF><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=2188FF><shad=1></shad></col> <col=2188FF><shad=1>" + player.getUsername() + "]<col=2188FF><shad=1>:"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.ZENYTE) {
				World.sendMessage("<col=DDA205><shad=1>["+player.getNpcKills()+"][" + player.getRights().getYellPrefix() + "<img=" + player.getRights().ordinal()
						+ "><col=DDA205><shad=1></shad></col>"  + "<col=DDA205><shad=1>"+player.getUsername() +"]</shad></col>"+ ":<col=DDA205><shad=1>" + yellMessage);
				return;
			}
			// TO-DO

		}

	}

	private static void helperCommands(final Player player, String[] command, String wholeCommand) {
		
if (wholeCommand.equals("afk")) {
			
			player.getPacketSender().sendMessage("Your now afk type ::back to return");
			player.forceChat("I AM AFK NOW!!");
			player.performAnimation(new Animation(1353));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			World.sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
			+ ": I am now AFK, please don't message me; I won't reply.");
			
			
			//World.sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
					//+ ": I am now away, please don't message me; I won't reply.");
		}
		if (wholeCommand.equals("back")) {
			
			//player.getPacketSender().sendMessage("Your now afk type ::bank to return");
			player.forceChat("I AM BACK!");
			player.performAnimation(new Animation(-1));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getMovementQueue().reset();
			World.sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
			+ ": I'm back from being AFK! You can messege me now!");
		
			
			
		}
		if (command[0].equalsIgnoreCase("lotteryentrycost")) {
			GrandLottery.setEntryCost(player, 1);
		}
		if (command[0].equalsIgnoreCase("endround")) {
			GrandLottery.endRound();
		}
		if (command[0].equalsIgnoreCase("setprizes")) {
			GrandLottery.set1stPrize(player, 1, 1);
			GrandLottery.set2ndPrize(player, 1, 1);
			GrandLottery.set3rdPrize(player, 1, 1);
			GrandLottery.setStaticPrize(player, 2, 1);
			GrandLottery.setSpecialPrize(player, 2, 1, 1);
			GrandLottery.addTime(player, 1);

		}
		
		
		if(command[0].contains("warn")) {
			player.getPacketSender().sendMessage("@mag@You have given a warning point!");
			//player.getPacketSender().sendConsoleMessage("Player couldn't be found on Skyfire.");
			String plr = wholeCommand.substring(5);
			Player playr2 = World.getPlayerByName(plr);
			if(playr2 != null) {
				playr2.getPointsHandler().setwarningPoints(1, true);
			
				playr2.getPA().sendMessage("@blu@You have received [1] warning point!");
				playr2.getPA().sendMessage("@red@[AUTO-BAN] If you reach [3] Warning Points you will be Auto Banned.");
				//playr2.getPA().sendMessage("@red@[AUTO-BAN] You currently have "+player.getPointsHandler().getWarningPoints()+" Warning Points.");
		      
				playr2.getPointsHandler().refreshPanel();
				} else
				player.getPacketSender().sendMessage("Could not find player: "+plr);
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Skyfire.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}

		if (command[0].equals("bank")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
					|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER
					|| player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.getBank(player.getCurrentBankTab()).open();
		}

		if (command[0].equalsIgnoreCase("jail")) {
			Player player2 = World
					.getPlayerByName(wholeCommand.substring(command[0].length() + 1, wholeCommand.length()));
			if (player2 != null) {
				if (Jail.isJailed(player2)) {
					player.getPacketSender().sendMessage("That player is already jailed!");
					return;
				}
				if (Jail.jailPlayer(player2) && player2.getDueling().duelingStatus == 0) {
					player2.getSkillManager().stopSkilling();
					PlayerLogs.log(player.getUsername(),
							player.getUsername() + " just jailed " + player2.getUsername() + "!");
					player.getPacketSender().sendMessage("Jailed player: " + player2.getUsername());
					player2.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
					World.sendMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
							+ " just jailed " + player2.getUsername());
					player2.performAnimation(new Animation(1994));
					player.performGraphic(new Graphic(730));
				} else {
					if (player2.getDueling().duelingStatus > 0) {
						player.getPacketSender().sendMessage(
								"That player is currently in a stake. Please wait before jailing them, or kick then jail.");
						return;
					} else {
						player.getPacketSender().sendMessage("This shouldn't happen, message Crimson. Error: JAIL45");
					}
				}
			} else {
				player.getPacketSender().sendMessage("Could not find that player online.");
			}
		}

		if (command[0].equalsIgnoreCase("mmz")) {
			TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);

		}

		if (command[0].equals("remindvote")) {
			 World.sendMessage("<img=10> <col=008FB2>Remember to collect "
			 		+ "s by using the ::vote command every 12 hours!");
		}
		if (command[0].equalsIgnoreCase("unjail")) {
			Player player2 = World
					.getPlayerByName(wholeCommand.substring(command[0].length() + 1, wholeCommand.length()));
			if (player2 != null) {
				Jail.unjail(player2);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just unjailed " + player2.getUsername() + "!");
				player.getPacketSender().sendMessage("Unjailed player: " + player2.getUsername() + "");
				player2.getPacketSender().sendMessage("You have been unjailed by " + player.getUsername() + ".");
				World.sendMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just unjailed " + player2.getUsername());
				player2.performAnimation(new Animation(1993));
				player.performGraphic(new Graphic(730));
			} else {
				player.getPacketSender().sendMessage("Could not find \""
						+ wholeCommand.substring(command[0].length() + 1, wholeCommand.length()) + "\" online.");
			}
		}
		if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getRights().isStaff()) {
							TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
						}
					}
				}
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);
			}
		}
		if (command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
		}
		if (command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);

			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR) {
					if (player2.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
						player.sendMessage("you can't teleport to someone who is in dungeonnering");
						return;
					}
				}

				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (command.length >= 3 && command[2] != null) {
				player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
			}
			Player playerToMove = World.getPlayerByName(player2);
			if (playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender()
						.sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender()
						.sendConsoleMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
			}
		}
		if (command[0].equalsIgnoreCase("mute")) 
			StaffCommand.parse(CommandTypes.MUTE, player, wholeCommand);
		if (command[0].equalsIgnoreCase("unmute")) 
			StaffCommand.parse(CommandTypes.UN_MUTE, player, wholeCommand);	
	}
	
	private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equalsIgnoreCase("ban"))
			StaffCommand.parse(CommandTypes.BAN, player, wholeCommand);

		if (command[0].equalsIgnoreCase("unban")) 
			StaffCommand.parse(CommandTypes.UN_BAN, player, wholeCommand);
		
		if (command[0].equalsIgnoreCase("ipmute")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(10));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
				return;
			} else {
				if (PlayerPunishment.IPMuted(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already IPMuted. Command logs written.");
					return;
				}
				final String mutedIP = player2.getHostAddress();
				PlayerPunishment.addMutedIP(mutedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + " was successfully IPMuted. Command logs written.");
				World.sendStaffMessage("<col=FF0066><img=10> [PUNISHMENTS]<col=6600FF> " + player.getUsername() + " has IPmuted " + player2.getUsername() + ". Don't forget to post logs!");
				player2.getPacketSender().sendMessage("You have been IPMuted by " + player.getUsername() + ".");
				}
		}

		if (command[0].equalsIgnoreCase("unipmute"))
			player.getPacketSender().sendConsoleMessage("Unipmutes can only be handled manually.");

		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3) {
				z = Integer.valueOf(command[3]);
			}
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
		}

		//if (command[0].equals("sql")) {
			//MySQLController.toggle();
			//if (player.getRights() == PlayerRights.DEVELOPER) {
			//	player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);
			//} else {
			//	player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED + ".");
			//}
		//}
		if (command[0].equalsIgnoreCase("cpuban")) {
			final String bannedUserName = Misc.formatPlayerName(command[1]);
			final Player player2 = Optional.ofNullable(PlayerHandler.getPlayerForName(bannedUserName)).orElse(Misc.accessPlayer(bannedUserName));
			final String serial = player2.getSerialNumber();
		
			if (player2 != null && player2.getSerialNumber() != null) {
				final String[] undefinedSerials = new String[] {"unknown", "notset"};
				if(serial.isEmpty() || Arrays.stream(undefinedSerials).anyMatch(keyword -> serial.toLowerCase().contains(keyword))) {
					player.sendMessage("@red@CPU Ban attempt has been blocked, serial of "+player2.getUsername()+" is unknown.");
				} else {
					World.deregister(player2);
					ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
					player.getPacketSender()
					.sendConsoleMessage("CPU["+serial+"] of Player["+player2.getUsername()+"] has been banned from connecting.");
				}
			}
		}

		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("ipban")) {
			final String bannedUserName = Misc.formatPlayerName(wholeCommand.substring(command[0].length()+1));

			player.sendMessage("Attempting to ban "+bannedUserName);
			Player player2 = PlayerHandler.getPlayerForName(bannedUserName);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online, started searching offline files.");
				BanHammer.ban(BanType.IP, player, bannedUserName);
			} else {
				if (PlayerPunishment.IPBanned(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already banned. Command logs written.");
					return;
				}
				final String bannedIP = player2.getHostAddress();
				PlayerPunishment.addBannedIP(bannedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + "'s IP was successfully banned. Command logs written.");
				World.sendStaffMessage("<col=FF0066><img=10> [PUNISHMENTS]<col=6600FF> " + player.getUsername() + " has IPbanned " + player2.getUsername() + ". Don't forget to post logs!");
				for (Player playersToBan : World.getPlayers()) {
					if (playersToBan == null) {
						continue;
					}
					if (Objects.equals(playersToBan.getHostAddress(), bannedIP)) {
						PlayerLogs.log(player.getUsername(),
								"" + player.getUsername() + " just IPBanned " + playersToBan.getUsername() + "!");
						World.deregister(playersToBan);
						if (!Objects.equals(player2.getUsername(), playersToBan.getUsername())) {
							player.getPacketSender().sendConsoleMessage("Player " + playersToBan.getUsername()
									+ " was successfully IPBanned. Command logs written.");
						}
					}
				}
			}
		}
		if (command[0].equalsIgnoreCase("unipmute")) {
			player.getPacketSender().sendConsoleMessage("Unipmutes can only be handled manually.");
		}
		if (command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender()
							.sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				} else {
					player.getPacketSender().sendConsoleMessage(
							"You can not teleport that player at the moment. Maybe you or they are in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					player.getPacketSender().sendConsoleMessage("Moving player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You've been moved to " + player.getUsername());
					player2.moveTo(player.getPosition().copy());
				} else {
					player.getPacketSender()
							.sendConsoleMessage("Failed to move player to your coords. Are you or them in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Skyfire.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}
	}

	private static void administratorCommands(final Player player, String[] command, String wholeCommand) {

		if(command[0].equalsIgnoreCase("ffa-pure")) {
			FreeForAll.startEvent("pure");
		}
		if(command[0].equalsIgnoreCase("ffa-brid")) {
			FreeForAll.startEvent("brid");
		}
		if(command[0].equalsIgnoreCase("ffa-dharok")) {
			FreeForAll.startEvent("dharok");
		}

		if (command[0].equalsIgnoreCase("donamount")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {

				player.getPacketSender().sendMessage("Player has donated: " + target.getAmountDonated() + " Dollars.");
			}
		}


		if (command[0].equals("reset")) {
			for (Skill skill : Skill.values()) {
				int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
			}
			player.getPacketSender().sendConsoleMessage("Your skill levels have now been reset.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("kill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}
		if (command[0].equals("emptyitem")) {
			if (player.getInterfaceId() > 0
					|| player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int item = Integer.parseInt(command[1]);
			int itemAmount = player.getInventory().getAmount(item);
			Item itemToDelete = new Item(item, itemAmount);
			player.getInventory().delete(itemToDelete).refreshItems();
		}
		if (command[0].equals("gold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else {
				player.getPacketSender().sendMessage("Can not find player online.");
			}
		}
		if (command[0].equalsIgnoreCase("cashineco")) {
			int gold = 0, plrLoops = 0;
			for (Player p : World.getPlayers()) {
				if (p != null) {
					for (Item item : p.getInventory().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable())
							gold += item.getDefinition().getValue();
					}
					for (Item item : p.getEquipment().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable())
							gold += item.getDefinition().getValue();
					}
					for (int i = 0; i < 9; i++) {
						for (Item item : player.getBank(i).getItems()) {
							if (item != null && item.getId() > 0 && item.tradeable())
								gold += item.getDefinition().getValue();
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage(
					"Total gold in economy right now: \"" + Misc.insertCommasToNumber(String.valueOf(gold)) + "\", went through " + plrLoops + " players.");
		}
		
		

		if (command[0].equals("getid")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}}
		}

		

	private static void ownerCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equalsIgnoreCase("mypos")) {
			player.getPacketSender().sendMessage(
					"X: " + player.getPosition().getX()
							+ " Y: " + player.getPosition().getY()
							+ " H: " + player.getPosition().getZ()
			);
			return;
		}

		if (command[0].equalsIgnoreCase("givedon")) {

			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SAPPHIRE);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(25);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		
		if (command[0].equalsIgnoreCase("emptypouch")) {
			String name = wholeCommand.substring(11);
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is offline");
			} else {
				target.setMoneyInPouch(0);
			}

		}
		if (command[0].equals("setlev")) {
			String name = wholeCommand.substring(8);
            Player target = World.getPlayerByName(name);
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			target.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set his " + skill.getName() + " level to " + level);
		}
		if (command[0].equalsIgnoreCase("givedon1")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.EMERALD);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(50);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon2")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.RUBY);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(100);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		
		if (command[0].contains("pure")) {
			int[][] data = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
					{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
					{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
					{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
			}
			BonusManager.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().resetItems();
			player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000).add(4154, 5000)
					.add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000).add(2435, 1000);
			player.getSkillManager().newSkillManager();
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85)
					.setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70)
					.setMaxLevel(Skill.CONSTITUTION, 850);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
		}
		if (command[0].equalsIgnoreCase("givedon3")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DIAMOND);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(250);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon4")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DRAGONSTONE);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(500);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("tsql")) {
			MySQLController.toggle();
			player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);

		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ZENYTE);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equals("doublexp")) {
			GameSettings.BONUS_EXP = !GameSettings.BONUS_EXP;
			player.getPacketSender()
					.sendMessage("Double XP is now " + (GameSettings.BONUS_EXP ? "enabled" : "disabled") + ".");
		}
		if (wholeCommand.equals("sfs")) {
			Lottery.restartLottery();
		}
		if (command[0].equals("giveitem")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			String rss = command[3];
			if (command.length > 4) {
				rss += " " + command[4];
			}
			if (command.length > 5) {
				rss += " " + command[5];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				player.getPacketSender().sendConsoleMessage("Gave player gold.");
				target.getInventory().add(item, amount);
			}
		}
		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			if (time > 0) {
				SkyFireLauncher.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						WellOfWealth.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						SkyFireLauncher.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
		if (command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length() + 1);
			Player playr2 = World.getPlayerByName(plr);
			if (playr2 != null) {
				player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
						+ playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
			}
		}
	}

	private static void developerCommands(Player player, String command[], String wholeCommand) {
		if (command[0].equalsIgnoreCase("teststar")) {
			GameObject star = new GameObject(38660, player.getPosition());
			CustomObjects.spawnGlobalObject(star);
		}

	     if (command[0].contains("host")) {
	            String plr = wholeCommand.substring(command[0].length() + 1);
	            Player playr2 = World.getPlayerByName(plr);
	            if (playr2 != null) {
	                player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
	                        + playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
	            } else {
	                player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
	            }
	        }

		if (command[0].equalsIgnoreCase("void")) {
			int[][] VOID_ARMOUR = { { Equipment.BODY_SLOT, 19785 }, { Equipment.LEG_SLOT, 19786 },
					{ Equipment.HANDS_SLOT, 8842 } };
			for (int i = 0; i < VOID_ARMOUR.length; i++) {
				player.getEquipment().set(VOID_ARMOUR[i][0], new Item(VOID_ARMOUR[i][1]));
			}
			int index = Integer.parseInt(command[1]);
			switch (index) {
			case 1:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(11665));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19111));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(11732));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(18349));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13262));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15220));
				break;
			case 2:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(11664));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(10499));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(11732));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(18357));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13740));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15019));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(9244, 500));
				break;
			case 3:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(11663));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(2413));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(6920));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(18335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(14006));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13738));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15018));
				break;
			}
			BonusManager.update(player);

			//WeaponAnimations.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getEquipment().refreshItems();
		}
		if (command[0].equalsIgnoreCase("crim")) {
			int index = Integer.parseInt(command[1]);
			switch (index) {
			case 1:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(9788));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19709));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(20000));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(19335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(16403));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13964));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(773));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(2583));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(2585));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(14484));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(11212, 1000000));
				break;
			case 2:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(9788));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19709));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(20000));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(19335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(20171));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13964));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(773));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(2583));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(2585));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(14484));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(11212, 1000000));
				break;
			case 3:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(14484));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(13999, 1000000));
				break;
			}
			BonusManager.update(player);
			//WeaponAnimations.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getEquipment().refreshItems();
		}
		if (command[0].equalsIgnoreCase("kilik")) {
			int index = Integer.parseInt(command[1]);
			switch (index) {
			case 1:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(14008));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14019));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(20000));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(19335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(13999));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13742));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15220));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(14009));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(14010));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462));
				break;
			case 2:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(14014));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14019));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(20002));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(19335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(21777));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13738));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15018));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(14015));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(14016));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462));
				break;
			case 3:
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(14011));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14019));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(20001));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(19335));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(20171));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(18361));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15019));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(14012));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(14013));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(11212, 1000000));
				break;
			}
			BonusManager.update(player);
			//WeaponAnimations.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getEquipment().refreshItems();
		}
		if (wholeCommand.contains("potup")) {
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 118);
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 118);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 118);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 114);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 110);
			player.setHasVengeance(true);
			player.getPacketSender().sendMessage("<shad=330099>You now have Vengeance's effect.");
		}
		
		if(command[0].equalsIgnoreCase("spawnvoteboss")) {
		//	NPC npc = new NPC(6305, new Position(3101, 5537, 0));
			//World.register(npc);
		}
		if(command[0].equalsIgnoreCase("wwdespawn")) {
			GlobalBossHandler.getBosses().forEach(boss -> {
				System.out.println("Found boss -> "+boss.getDefinition().getName());
				GlobalBossHandler.onDeath(boss);
			});
		}
		if(wholeCommand.equals("voterewarder")) {
			VotingContest.restartContest();
		}
		if (command[0].equalsIgnoreCase("title")) {
			
		}
		if (command[0].equalsIgnoreCase("sstar")) {
			CustomObjects.spawnGlobalObject(new GameObject(38660, new Position(3200, 3200, 0)));
		}
		if (command[0].equals("checkbank")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(10));
			if (plr != null) {
				player.getPacketSender().sendConsoleMessage("Loading bank..");
				for (Bank b : player.getBanks()) {
					if (b != null) {
						b.resetItems();
					}
				}
				for (int i = 0; i < plr.getBanks().length; i++) {
					for (Item it : plr.getBank(i).getItems()) {
						if (it != null) {
							player.getBank(i).add(it, false);
						}
					}
				}
				player.getBank(0).open();
			} else {
				player.getPacketSender().sendConsoleMessage("Player is offline!");
			}
		}

		if (command[0].equals("antibot")) {
			AntiBotting.sendPrompt(player);
		}

		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ZENYTE
						);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equalsIgnoreCase("kbd")) {
			SLASHBASH.startPreview(player);

		}

		if (command[0].equalsIgnoreCase("spec")) {
			player.setSpecialPercentage(1000);
			CombatSpecial.updateBar(player);
		}

		if (command[0].equalsIgnoreCase("double")) {
			String event = command[1];

		}

		if (command[0].equals("givedpoints")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.getPointsHandler().incrementDonationPoints(amount);
				target.getPointsHandler().refreshPanel();

				// player.refreshPanel(target);
			}
		}

		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);
			int amount = (command.length == 2 ? 1
					: Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
							.replaceAll("b", "000000000")));
			if (amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if (command[0].equals("bank")) {
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("mark")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set your " + skill.getName() + " level to " + level);
		}
		if (command[0].equals("dzoneon")) {
			if (GameSettings.DZONEON = false) {
				GameSettings.DZONEON = true;
				World.sendMessage(
						"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
			}
			GameSettings.DZONEON = false;
			World.sendMessage(
					"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
		}

		if (command[0].equals("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equals(""
				+ "cpubans")) {
			ConnectionHandler.reloadUUIDBans();
			player.getPacketSender().sendConsoleMessage("UUID bans reloaded!");
		}
		if (command[0].equals("reloadnpcs")) {
			NpcDefinition.parseNpcs().load();
			World.sendMessage("@red@NPC Definitions Reloaded.");
		}
		if (command[0].equals("reloadcombat")) {
			CombatStrategies.init();
			World.sendMessage("@red@Combat Strategies have been reloaded");
		}
        if (command[0].equals("reloadall")) {
            NpcDefinition.parseNpcs().load();
            World.sendMessage("@red@NPC Definitions Reloaded.");
            CombatStrategies.init();
            World.sendMessage("@red@Combat Strategies have been reloaded");
            ShopManager.parseShops().load();
            World.sendMessage("@red@Shops have been reloaded");
            NPCDrops.parseDrops().load();
            World.sendMessage("@red@Drops have been reloaded");
            ItemDefinition.init();
            World.sendMessage("@red@Item Definitions have been reloaded");
            WeaponInterfaces.parseInterfaces();
            World.sendMessage("@red@Weapon Interfaces have been reloaded");
            PlayerPunishment.reloadIPBans();
            player.getPacketSender().sendConsoleMessage("IP bans reloaded!");
            PlayerPunishment.reloadIPMutes();
            player.getPacketSender().sendConsoleMessage("IP mutes reloaded!");
            PlayerPunishment.reloadBans();
            player.getPacketSender().sendConsoleMessage("Banned accounts reloaded!");
        }
		if (command[0].equals("reloadipbans")) {
			PlayerPunishment.reloadIPBans();
			player.getPacketSender().sendConsoleMessage("IP bans reloaded!");
		}
		if (command[0].equals("reloadipmutes")) {
			PlayerPunishment.reloadIPMutes();
			player.getPacketSender().sendConsoleMessage("IP mutes reloaded!");
		}
		if (command[0].equals("reloadbans")) {
			PlayerPunishment.reloadBans();
			player.getPacketSender().sendConsoleMessage("Banned accounts reloaded!");
		}
		// if (command[0].equalsIgnoreCase("cpuban2")) {
		// String serial = wholeCommand.substring(8);
		// ConnectionHandler.banComputer("cpuban2", serial);
		// player.getPacketSender()
		// .sendConsoleMessage("" + serial + " cpu was successfully banned.
		// Command logs written.");
		// }
		if (command[0].equalsIgnoreCase("ipban2")) {
			String ip = wholeCommand.substring(7);
			PlayerPunishment.addBannedIP(ip);
			player.getPacketSender().sendConsoleMessage("" + ip + " IP was successfully banned. Command logs written.");
		}
		if (command[0].equals("scc")) {
			/*
			 * PlayerPunishment.addBannedIP("46.16.33.9");
			 * ConnectionHandler.banComputer("Kustoms", -527305299);
			 * player.getPacketSender().sendMessage("Banned Kustoms.");
			 */
			/*
			 * for(GrandExchangeOffer of : GrandExchangeOffers.getOffers()) {
			 * if(of != null) { if(of.getId() == 34) { //
			 * if(of.getOwner().toLowerCase().contains("eliyahu") ||
			 * of.getOwner().toLowerCase().contains("matt")) {
			 * 
			 * player.getPacketSender().sendConsoleMessage("FOUND IT! Owner: "
			 * +of.getOwner()+", amount: "+of.getAmount()+", finished: "
			 * +of.getAmountFinished()); //
			 * GrandExchangeOffers.getOffers().remove(of); //} } } }
			 */
			/*
			 * Player cc = World.getPlayerByName("Thresh"); if(cc != null) {
			 * //cc.getPointsHandler().setPrestigePoints(50, true);
			 * //cc.getPointsHandler().refreshPanel();
			 * //player.getPacketSender().sendConsoleMessage("Did");
			 * cc.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
			 * 15000).updateSkill(Skill.CONSTITUTION);
			 * cc.getSkillManager().setCurrentLevel(Skill.PRAYER,
			 * 15000).updateSkill(Skill.PRAYER); }
			 */
			// player.getSkillManager().addExperience(Skill.CONSTITUTION,
			// 200000000);
			// player.getSkillManager().setExperience(Skill.ATTACK, 1000000000);
			System.out.println("Seri: " + player.getSerialNumber());
		}
		if (command[0].equals("memory")) {
			// ManagementFactory.getMemoryMXBean().gc();
			/*
			 * MemoryUsage heapMemoryUsage =
			 * ManagementFactory.getMemoryMXBean().getHeapMemoryUsage(); long mb
			 * = (heapMemoryUsage.getUsed() / 1000);
			 */
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender()
					.sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
		}
		if (command[0].equals("star")) {
			ShootingStar.despawn(true);
			player.getPacketSender().sendConsoleMessage("star method called.");
		}
		if (command[0].equals("stree")) {
			EvilTrees.despawn(true);
			player.getPacketSender().sendConsoleMessage("tree method called.");
		}
		if (command[0].equals("save")) {
			player.save();
		}
		if (command[0].equals("saveall")) {
			World.savePlayers();
		}
		if (command[0].equals("v1")) {
			World.sendMessage(
					"<img=10> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
		}

		if(command[0].equalsIgnoreCase("master")) {
			player.getSkillManager().setExperience(Skill.ATTACK, 2000000000);
			player.getSkillManager().setExperience(Skill.STRENGTH, 2000000000);
			player.getSkillManager().setExperience(Skill.DEFENCE, 2000000000);
			player.getSkillManager().setExperience(Skill.CONSTITUTION, 2000000000);
			player.getSkillManager().setExperience(Skill.PRAYER, 2000000000);
			player.getSkillManager().setExperience(Skill.RANGED, 2000000000);
			player.getSkillManager().setExperience(Skill.MAGIC, 2000000000);
			
			player.getSkillManager().setExperience(Skill.SLAYER, 2000000000);
			player.getSkillManager().setExperience(Skill.LUCK, 2000000000);
			player.getSkillManager().setExperience(Skill.PVMING, 2000000000);

			
			}
		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}

		if (command[0].equals("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			// npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
			/*
			 * TaskManager.submit(new Task(5) {
			 * 
			 * @Override protected void execute() { npc.moveTo(new
			 * Position(npc.getPosition().getX() + 2, npc.getPosition().getY() +
			 * 2)); player.getPacketSender().sendEntityHintRemoval(false);
			 * stop(); }
			 * 
			 * });
			 */
			// npc.getMovementCoordinator().setCoordinator(new
			// Coordinator().setCoordinate(true).setRadius(5));
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("fillinv")) {
			for (int i = 0; i < 28; i++) {
				int it = RandomUtility.getRandom(10000);
				player.getInventory().add(it, 1);
			}
		}
		if (command[0].equals("playnpc")) {
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
					new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}

		if (command[0].equals("swi")) {
			int id = Integer.parseInt(command[1]);
			boolean vis = Boolean.parseBoolean(command[2]);
			player.sendParallellInterfaceVisibility(id, vis);
			player.getPacketSender().sendMessage("Done.");
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}
		if (command[0].equals("checkbank")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(10));
			if (plr != null) {
				player.getPacketSender().sendConsoleMessage("Loading bank..");
				for (Bank b : player.getBanks()) {
					if (b != null) {
						b.resetItems();
					}
				}
				for (int i = 0; i < plr.getBanks().length; i++) {
					for (Item it : plr.getBank(i).getItems()) {
						if (it != null) {
							player.getBank(i).add(it, false);
						}
					}
				}
				player.getBank(0).open();
			} else {
				player.getPacketSender().sendConsoleMessage("Player is offline!");
			}
		}
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equals("checkequip")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(11));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}

	public static void setServerkills(int points, boolean add) {
		if(add)
			CommandPacketListener.VOTES += points;
		else
			CommandPacketListener.VOTES = points;
	}
}
