package com.skyfire.net.packet.impl;

import java.util.Arrays;

import com.google.common.collect.Multiset.Entry;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.GameMode;
import com.skyfire.model.GameObject;
import com.skyfire.model.Graphic;
import com.skyfire.model.Item;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.container.impl.Equipment;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.util.Misc;
import com.skyfire.util.RandomUtility;
import com.skyfire.world.World;
import com.skyfire.world.content.ChristmasPresent;
import com.skyfire.world.content.ClueScrolls;
import com.skyfire.world.content.Consumables;
import com.skyfire.world.content.Digging;
import com.skyfire.world.content.Effigies;
import com.skyfire.world.content.ExperienceLamps;
import com.skyfire.world.content.Gambling;
import com.skyfire.world.content.MoneyPouch;
import com.skyfire.world.content.PetBox;
import com.skyfire.world.content.Sounds;
import com.skyfire.world.content.StarterTasks;
import com.skyfire.world.content.TrioBosses;
import com.skyfire.world.content.Sounds.Sound;
import com.skyfire.world.content.books.TestBook;
import com.skyfire.world.content.combat.range.DwarfMultiCannon;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.content.itemopening.CharmBox;
import com.skyfire.world.content.itemopening.DonationBox;
import com.skyfire.world.content.itemopening.MemberScrolls;
import com.skyfire.world.content.itemopening.RUBoxes;
import com.skyfire.world.content.scratchcards.ScratchCard;
import com.skyfire.world.content.skill.impl.construction.Construction;
import com.skyfire.world.content.skill.impl.dungeoneering.ItemBinding;
import com.skyfire.world.content.skill.impl.herblore.Herblore;
import com.skyfire.world.content.skill.impl.herblore.IngridientsBook;
import com.skyfire.world.content.skill.impl.hunter.BoxTrap;
import com.skyfire.world.content.skill.impl.hunter.Hunter;
import com.skyfire.world.content.skill.impl.hunter.JarData;
import com.skyfire.world.content.skill.impl.hunter.PuroPuro;
import com.skyfire.world.content.skill.impl.hunter.SnareTrap;
import com.skyfire.world.content.skill.impl.hunter.Trap.TrapState;
import com.skyfire.world.content.skill.impl.prayer.Prayer;
import com.skyfire.world.content.skill.impl.runecrafting.Runecrafting;
import com.skyfire.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.skyfire.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.skyfire.world.content.skill.impl.slayer.SlayerDialogues;
import com.skyfire.world.content.skill.impl.slayer.SlayerTasks;
import com.skyfire.world.content.skill.impl.summoning.CharmingImp;
import com.skyfire.world.content.skill.impl.summoning.SummoningData;
import com.skyfire.world.content.skill.impl.woodcutting.BirdNests;
import com.skyfire.world.content.transportation.JewelryTeleporting;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.content.transportation.TeleportType;
import com.skyfire.world.content.treasuretrails.ClueScroll;
import com.skyfire.world.content.treasuretrails.CoordinateScrolls;
import com.skyfire.world.content.treasuretrails.DiggingScrolls;
import com.skyfire.world.content.treasuretrails.MapScrolls;
import com.skyfire.world.content.treasuretrails.Puzzle;
import com.skyfire.world.content.treasuretrails.SearchScrolls;
import com.skyfire.world.entity.impl.player.Player;


public class ItemActionPacketListener implements PacketListener {
	
	public static boolean drinkSuperOverload(final Player player, int slot, int replacePotion) {
return true;
	}
	
	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
	public static boolean checkReqs(Player player, Location targetLocation) {
		if(player.getConstitution() <= 0)
			return false;
		if(player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if(player.getLocation() != null && !player.getLocation().canTeleport(player))
			return false;
		if(player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	private static void firstAction(final Player player,  Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();
		
		Location targetLocation = player.getLocation();
		
		if(interfaceId == 38274) {
			Construction.handleItemClick(itemId, player);
			return;
		}
		
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		player.setInteractingItem(player.getInventory().getItems()[slot]);
		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		if (Consumables.isFood(player, itemId, slot))
			return;
		if(Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if(BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if(MemberScrolls.handleScroll(player, itemId))
			return;
		if(ClueScroll.handleCasket(player, itemId) || SearchScrolls.loadClueInterface(player, itemId) || MapScrolls.loadClueInterface(player, itemId) || Puzzle.loadClueInterface(player, itemId) || CoordinateScrolls.loadClueInterface(player, itemId) || DiggingScrolls.loadClueInterface(player, itemId))
			return;
		if(Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if(ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}


	
	
		switch(itemId) {
		

		case 455:
			new ScratchCard(player).display();
			break;
		
		
		case 12158:
			
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(12158) && player.getInventory().getAmount(12158) >= 1) {
				player.getInventory().delete(12158, 1);
				player.getSkillManager().addExperience(Skill.LUCK, 200 + Misc.getRandom(125));
				player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
				
					player.getInventory().add(995, 100000);
					player.getClickDelay().reset();
				player.getPacketSender().sendMessage("@dre@You smash the token and claim 100k!");
				} else {
					player.getPacketSender().sendMessage("How?");
					return;
				
				}
			break;
		
		
		
		
		case 12159:
			
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(12159) && player.getInventory().getAmount(12159) >= 1) {
				player.getInventory().delete(12159, 1);
				player.getSkillManager().addExperience(Skill.LUCK, 200 + Misc.getRandom(125));
				player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
		
					player.getInventory().add(995, 250000);
					player.getClickDelay().reset();
				player.getPacketSender().sendMessage("@dre@You smash the token and claim 250k!");
				} else {
					player.getPacketSender().sendMessage("How?");
					return;
				
				}
			break;
		
		
		
	case 12160:
			
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(12160) && player.getInventory().getAmount(12160) >= 1) {
				player.getInventory().delete(12160, 1);
				player.getSkillManager().addExperience(Skill.LUCK, 200 + Misc.getRandom(125));
				player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
		
					player.getInventory().add(995, 500000);
					player.getClickDelay().reset();
				player.getPacketSender().sendMessage("@dre@You smash the token and claim 500k!");
				} else {
					player.getPacketSender().sendMessage("How?");
					return;
				
				}
			break;
		
		
		
	case 12163:
		
		if(player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
			return;
		}
		if(player.getInventory().contains(12163) && player.getInventory().getAmount(12163) >= 1) {
			player.getInventory().delete(12163, 1);
			player.getSkillManager().addExperience(Skill.LUCK, 200 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
	
				player.getInventory().add(995, 1000000);
				player.getClickDelay().reset();
			player.getPacketSender().sendMessage("@dre@You smash the token and claim 1m!");
			} else {
				player.getPacketSender().sendMessage("How?");
				return;
			
			}
		break;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		case 995:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			
			
			
			if(player.getInventory().contains(995) && player.getInventory().getAmount(995) >= 9999999) {
				player.getInventory().delete(995, 10000000);
				player.getInventory().add(7776, 1);
				player.getClickDelay().reset();
				//player.getInventory().delete(995, 1000000);
					//player.getPacketSender().sendMessage("You do not have enouph coins!");
			} else {
				player.getPacketSender().sendMessage("You do not have 10m coins in your inventory!");
				
				return;
	
			}
		
		
		
			break;
		
		
		
		
		
		
		
		
		case 7774:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7774) && player.getInventory().getAmount(7774) >= 1) {
				
			player.getInventory().delete(7774, 1);
			
				player.getInventory().add(995, 100000000);
				player.getClickDelay().reset();
			
			player.getPacketSender().sendMessage("You open the scroll and find gold!");
			} else {
				player.getPacketSender().sendMessage("You dont have a token");
				
			}
			break;
		case 7776:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 1) {
				
			player.getInventory().delete(7776, 1);
			
				player.getInventory().add(995, 10000000);
				player.getClickDelay().reset();
			player.getPacketSender().sendMessage("You open the scroll and find gold!");
			
			}	else {
				player.getPacketSender().sendMessage("You dont have a token");
			}
			break;
		case 7775:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7775) && player.getInventory().getAmount(7775) >= 1) {
				
			player.getInventory().delete(7775, 1);
			
				player.getInventory().add(995, 1000000);
				player.getClickDelay().reset();
			player.getPacketSender().sendMessage("You open the scroll and find gold!");
			
			}	else {
				player.getPacketSender().sendMessage("You dont have a token");
			}
			
			break;
		case 13663:
			if(player.getInterfaceId() > 0) {
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
				return;
			}
			player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
			player.getPacketSender().sendString(38006, "Choose stat to reset!").sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.").sendString(38090, "Which skill would you like to reset?");
			player.getPacketSender().sendInterface(38000);
			break;

		case 19670:
			if(player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(70);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 8007: //Varrock
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
			
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8007, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(3210, 3424, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			 break;
		case 8008: //Lumbridge
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8008, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(3222, 3218, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			
			 break;
		case 8009: //Falador
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8009, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2964, 3378, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			
			 break;
		case 8010: //Camelot
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
			
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8010, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2757, 3477, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
			
			 break;
		case 8011: //Ardy
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8011, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2662, 3305, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
			 break;
		case 8012: //Watchtower Tele
			if (player.inFFALobby){
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8012, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2728, 3349, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
			 break;
		case 8013: //Home Tele
			 TeleportHandler.teleportPlayer(player, new Position(3087, 3491), TeleportType.NORMAL);
			 break;
		case 13598: //Runecrafting Tele
			 TeleportHandler.teleportPlayer(player, new Position(2595, 4772), TeleportType.NORMAL);
			 break;
		case 13599: //Air Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2845, 4832), TeleportType.NORMAL);
			 break;
		case 13600: //Mind Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2796, 4818), TeleportType.NORMAL);
			 break;
		case 13601: //Water Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2713, 4836), TeleportType.NORMAL);
			 break;
		case 13602: //Earth Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2660, 4839), TeleportType.NORMAL);
			 break;
		case 13603: //Fire Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2584, 4836), TeleportType.NORMAL);
			 break;
		case 13604: //Body Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2527, 4833), TeleportType.NORMAL);
			 break;
		case 13605: //Cosmic Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2162, 4833), TeleportType.NORMAL);
			 break;
		case 13606: //Chaos Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2269,4843), TeleportType.NORMAL);
			 break;
		case 13607: //Nature Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2398, 4841), TeleportType.NORMAL);
			 break;
		case 13608: //Law Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2464, 4834), TeleportType.NORMAL);
			 break;
		case 13609: //Death Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2207, 4836), TeleportType.NORMAL);
			 break;
		case 18809: //Rimmington Tele
			 TeleportHandler.teleportPlayer(player, new Position(2957, 3214), TeleportType.NORMAL);
			 break;
		case 18811: //Pollnivneach Tele
			 TeleportHandler.teleportPlayer(player, new Position(3359,2910), TeleportType.NORMAL);
			 break;
		case 18812: //Rellekka Tele
			 TeleportHandler.teleportPlayer(player, new Position(2659, 3661), TeleportType.NORMAL);
			 break;
		case 18814: //Yanielle Tele
			 TeleportHandler.teleportPlayer(player, new Position(2606, 3093), TeleportType.NORMAL);
			 break;
		case 6542:
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			ChristmasPresent.openBox(player);
			break;
			
			
		case 10025:
			player.getSkillManager().addExperience(Skill.LUCK, 2500 + Misc.getRandom(1000));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			CharmBox.open(player);
			break;
			
			
		case 1959:
			TrioBosses.eatPumpkin(player);
			break;
		case 2677:
		case 2678:
		case 2679:
		case 2680:
		case 2681:
		case 2682:
		case 2683:
		case 2684:
		case 2685:
			ClueScrolls.giveHint(player, itemId);
			break;
		case 7956:
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
				return;
			}
			if (player.getRights() == PlayerRights.SAPPHIRE) {
				if (Misc.getRandom(15) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.EMERALD || player.getRights() == PlayerRights.SUPPORT) {
				if (Misc.getRandom(12) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.RUBY || player.getRights() == PlayerRights.MODERATOR) {
				if (Misc.getRandom(9) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.DIAMOND  || player.getRights() == PlayerRights.ADMINISTRATOR) {
				if (Misc.getRandom(6) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.DRAGONSTONE || player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.ONYX || player.getRights() == PlayerRights.ZENYTE) {
				if (Misc.getRandom(3) == 2) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.PLAYER) {
				player.getInventory().delete(7956, 1);
			}
			
			int[] rewards = 		{537, 989, 2572, 6585, 13047, 4151, 989, 19055, 15220, 15020, 15019, 15018, 11730, 11283, 11235, 15486, 15332, 13879, 13883, 11230, 11230, 11230, 11230, 15243, 15243, 15243, 15243, 11212, 11212, 11212, 11212, 2, 2, 2, 2, 565, 560, 995, 995, 995, 2435, 2435, 15273, 15273, 15273, 15273, 1514, 1514, 384, 384, 384, 454, 452, 1631, 1619, 3025, 15332, 386};
			int[] rewardsAmount = 	{25, 5, 1, 1, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 20, 65, 50, 30, 15, 5, 10, 15, 20, 20, 20, 20, 20, 100, 250, 500, 1000, 2500, 5000, 5000000, 5000000, 5000000, 10, 20, 100, 50, 25, 250, 100, 250, 100, 250, 1000, 250, 10, 1, 1, 10, 1, 150};
			int rewardPos = Misc.getRandom(rewards.length-1);
			player.getInventory().add(rewards[rewardPos], (int)((rewardsAmount[rewardPos]*0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
			player.getSkillManager().addExperience(Skill.LUCK, 5000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
			
			//Donation Box
		case 6183:
			player.getSkillManager().addExperience(Skill.LUCK, 15000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			DonationBox.open(player);
			break;
			case 15369:
			RUBoxes.openCommonBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 500 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 15370:
			RUBoxes.openUncommonBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 15371:
			RUBoxes.openRareBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 1500 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 15372:
			RUBoxes.openExtremeBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 2000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 15373:
			RUBoxes.openLegendaryBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 2500 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 19934:
			PetBox.OpenPetBox(player);
			player.getSkillManager().addExperience(Skill.LUCK, 5000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
			//Clue Scroll
		case 2714:
			player.getSkillManager().addExperience(Skill.LUCK, 200 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			ClueScrolls.addClueRewards(player);
			break;
		case 621:
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(621, 1);
			player.getPointsHandler().incrementTriviaPoints(10);
			player.getPacketSender().sendMessage("You have claimed 10 Trivia Points");
			break;
			
		case 13208:
			if(!player.getClickDelay().elapsed(2000)) {
				player.getPacketSender().sendMessage("@bla@You need to wait before you can heal more");
				
				return;
			}
			if (!player.getCorruptBandagesLoading().getContents().isEmpty()) {
				for (Entry<Integer> crystal : player.getCorruptBandagesLoading().getContents().entrySet()) {
					player.getCorruptBandagesLoading().getContents().remove(crystal.getElement());
					player.setConstitution(player.getConstitution() + 170);
					if(player.getConstitution() > 1190)
						player.setConstitution(1190);
					Sounds.sendSound(player, Sound.EAT_FOOD);
					player.performAnimation(new Animation(829));
					player.performGraphic(new Graphic(482));
					player.getPacketSender().sendMessage("@red@The Corrupt bandage has healed you 15 Hitpoints");
					player.getClickDelay().reset();
					
					return;
				}
			} else {
				player.getPacketSender().sendMessage("@red@You have no crystals in your Bandage!");
			}
			break;
			
		
				
		case 15387:
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(15387, 1);
			rewards = new int[] {1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442, 347, 247};
			player.getInventory().add(rewards[RandomUtility.getRandom(rewards.length-1)], 1);
			break;
		case 407:
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(407, 1);
			if (RandomUtility.getRandom(3) < 3) {
				player.getInventory().add(409, 1);
			} else if(RandomUtility.getRandom(4) < 4) {
				player.getInventory().add(411, 1);
			} else 
				player.getInventory().add(413, 1);
			break;
		case 405:
			
			player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(405, 1);
			if (RandomUtility.getRandom(1) < 1) {
				int coins = RandomUtility.getRandom(30000);
				player.getInventory().add(995, coins);
				player.getPacketSender().sendMessage("The casket contained "+coins+" coins!");
			} else
				player.getPacketSender().sendMessage("The casket was empty.");
			break;
		case 15084:
			Gambling.rollDice(player);
			break;
		case 6307:
			player.setDialogueActionId(79);
			DialogueManager.start(player, 0);
			break;
		case 299:
			Gambling.plantSeed(player);
			player.getSkillManager().addExperience(Skill.LUCK, 100 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			break;
		case 4155:
			if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				return;
			}
			DialogueManager.start(player, SlayerDialogues.dialogue(player));
			break;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
			if(!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if(player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] {10350, 10348, 10346, 10352} : 
				itemId == 11860 ? new int[]{10334, 10330, 10332, 10336} : 
					itemId == 11862 ? new int[]{10342, 10338, 10340, 10344} : 
						itemId == 11848 ? new int[]{4716, 4720, 4722, 4718} : 
							itemId == 11856 ? new int[]{4753, 4757, 4759, 4755} : 
								itemId == 11850 ? new int[]{4724, 4728, 4730, 4726} : 
									itemId == 11854 ? new int[]{4745, 4749, 4751, 4747} : 
										itemId == 11852 ? new int[]{4732, 4734, 4736, 4738} : 
											itemId == 11846 ? new int[]{4708, 4712, 4714, 4710} :
												new int[]{itemId};

											if(player.getInventory().getFreeSlots() < items.length) {
												player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
												return;
											}
											player.getInventory().delete(itemId, 1);
											for(int i : items) {
												player.getInventory().add(i, 1);
											}
											player.getSkillManager().addExperience(Skill.LUCK, 1000 + Misc.getRandom(125));
											player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
										player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
											
											player.getPacketSender().sendMessage("You open the set and find items inside.");
											player.getClickDelay().reset();
											break;
		case 952:
		
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
				return;
			}
			if(player.getLocation() != Location.WILDERNESS) {
				if(!player.getClickDelay().elapsed(1300)) {
					return;
			} 
				if(!player.getClickDelay().elapsed(1300))
					return;
				Digging.dig(player);
				player.getMovementQueue().reset();
				player.getPacketSender().sendMessage("You start digging in the mud...");
				player.performAnimation(new Animation(830));
				player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 1500 + Misc.getRandom(125));
				player.getClickDelay().reset();
				player.getInventory().add(995, 2000).refreshItems();
				
				
				player.getPacketSender().sendMessage("You find some coins...");
				if (Misc.getRandom(5) == 1) {
					//player.getInventory().add(1215, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@red@You found nothing but got some extra XP!");
				}
				if (Misc.getRandom(10) == 1) {
					player.getInventory().add(1215, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@red@You found a dragon dagger, and some extra XP!");
				}
				if (Misc.getRandom(10) == 1) {
					player.getInventory().add(1213, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a rune dagger");
				}
				if (Misc.getRandom(5) == 1) {
					player.getInventory().add(1969, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a spinach roll! Yuuck");
				}
				if (Misc.getRandom(13) == 1) {
					player.getInventory().add(1965, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a cabbage! Yuucky!");
				}
				if (Misc.getRandom(35) == 1) {
					player.getInventory().add(4587, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a dragon scimitar!");
				}
				if (Misc.getRandom(27) == 1) {
					player.getInventory().add(995, 120000).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found some extra gold while digging");
				}
				if (Misc.getRandom(1000) == 1) {
					player.getInventory().add(995, 1000000).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You got super lucky while digging, and got 1m coins!");
				}
				if (Misc.getRandom(100000) == 1) {
					player.getInventory().add(995, 2000000000).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a cabbage! Yuucky!");
				}
				if (Misc.getRandom(3000) == 1) {
					player.getInventory().add(14018, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@mag@You found a Ornate katana!");
				}
				if (Misc.getRandom(35) == 1) {
					player.getInventory().add(995, 100000).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@red@You found 100k while digging");
				}
				if (Misc.getRandom(2500) == 1) {
					player.getInventory().add(18778, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@mag@You found a Starved ancient effegy! Amazing luck!");
				}
				return;
			} else {
				//player.getPacketSender().sendMessage("@blu@Your currently in the wilderness");
				if(!player.getClickDelay().elapsed(1300))
					return;
				Digging.dig(player);
				player.getMovementQueue().reset();
				player.getPacketSender().sendMessage("You start digging in the wild...");
				player.performAnimation(new Animation(830));
				player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 2500 + Misc.getRandom(100));
				player.getClickDelay().reset();
				player.getInventory().add(995, 4000).refreshItems();
				
				
				player.getPacketSender().sendMessage("@dre@You find some coins...");
				if (Misc.getRandom(5) == 1) {
					//player.getInventory().add(1215, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(100));
					
					player.getPacketSender().sendMessage("@red@You found nothing but got some extra XP!");
				}
				
				if (Misc.getRandom(10) == 1) {
					player.getInventory().add(1127, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 350 + Misc.getRandom(100));
					
					player.getPacketSender().sendMessage("@red@You found a rune platebody, and some extra XP!");
				}
				if (Misc.getRandom(10) == 1) {
					player.getInventory().add(1123, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a adamant  platebody!");
				}
				if (Misc.getRandom(15) == 1) {
					player.getInventory().add(1126, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a black  platebody!");
				}
				if (Misc.getRandom(1000) == 1) {
					player.getInventory().add(14018, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@mag@You found a Ornate katana!");
				}
				if (Misc.getRandom(5) == 1) {
					player.getInventory().add(553, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a muddy skull! Yet you still pick it up.");
				}
				if (Misc.getRandom(95000) == 1) {
					player.getInventory().add(995, 2000000000).refreshItems();
					
					player.getPacketSender().sendMessage("@mag@You found a 2b coins! Thats very rare...!");
				}
				if (Misc.getRandom(13) == 1) {
					player.getInventory().add(10976, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a long bone! Strange...");
				}
				if (Misc.getRandom(35) == 1) {
					player.getInventory().add(6335, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found a green tribal mask!");
				}
				if (Misc.getRandom(27) == 1) {
					player.getInventory().add(995, 120000).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You found some extra muddy gold while digging");
				}
				
				if (Misc.getRandom(2500) == 1) {
					player.getInventory().add(18888, 1).refreshItems();
					
					player.getPacketSender().sendMessage("@mag@You found Red wings, dropped by a pker long ago! Awesome");
				}
				if (Misc.getRandom(1000) == 1) {
					player.getInventory().add(995, 2000000).refreshItems();
					
					player.getPacketSender().sendMessage("@red@You got super lucky while digging in wild, and got 2m coins!");
				}
				if (Misc.getRandom(35) == 1) {
					player.getInventory().add(995, 125000).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@red@You found 125k while digging");
				}
				if (Misc.getRandom(100) == 1) {
					player.getInventory().add(6563, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@dre@You found a mystic mud staff while digging, decent!");
				}
				if (Misc.getRandom(1500) == 1) {
					player.getInventory().add(2090, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@mag@You found a @red@Pet Monkey@mag@ while digging! Congrats");
				}
				if (Misc.getRandom(1500) == 1) {
					player.getInventory().add(18778, 1).refreshItems();
					player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(125));
					
					player.getPacketSender().sendMessage("@mag@You found a Starved ancient effegy! Amazing luck!");
				}
				
	
			
			}
		
			
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:			
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;
		case 600:
			TestBook.readBook(player, 0, false);
			break;
		case 6199:
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
				return;
			}
			int rewards2[][] = {
					{386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753, 10828, 1215,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 4587,4587, 4091,4587, 4091,4587,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024, 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204, 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544, 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common, 0
					{386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15501,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753, 11133, 10828, 6199, 3751, 3753, 11884, 10589,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 6739, 6739, 2577, 2581, 15332, 15332, 15332, 11732, 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751, 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 4730, 4151, 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140, 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1
					{386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,386,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,15273,3749,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753,3749, 3753, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739, 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848,  2577, 2581, 2572, 15501, 6920, 6922, 11335, 15243, 6585, 4151, 11696, 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730, 11283, 18349, 18351, 18353, 18355, 18357, 18359, 2527,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091,4587, 4091, 12601, 15486, 15018, 15019, 15020, 15220} //Rare, 2
			};
			double numGen = Math.random();
			/** Chances
			 *  50% chance of Common Items - cheap gear, high-end consumables
			 *  40% chance of Uncommon Items - various high-end coin-bought gear
			 *  10% chance of Rare Items - Highest-end coin-bought gear, some voting-point/pk-point equipment
			 */
			int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2;
			rewardPos = RandomUtility.getRandom(rewards2[rewardGrade].length-1);
			player.getInventory().delete(6199, 1);
			player.getSkillManager().addExperience(Skill.LUCK, 1500 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().add(rewards2[rewardGrade][rewardPos], 1).refreshItems();
			break;
		case 15501:
			
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
				return;
			}
			int superiorRewards[][] = {
					
					{11133, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 2581, 2577, 2577, 2577, 2581, 2581, 1249, 3204, 1305, 4151, 6585, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889}, //Uncommon, 0
					{6739, 15259, 15332, 2579, 6920, 6922, 11882, 4151, 6585, 6570, 11884}, //Rare, 1
					{6570, 15018, 15019, 15020, 15220, 11730, 11718, 11720, 11722, 11724, 11726, 11283, 18349, 18353, 13896, 18357, 10551, 4151, 2577, }, //Epic, 2
					{11235, 17273, 11696, 11698, 11700, 20072, 15486, 19336, 19337, 19338, 19339, 19340} //Legendary, 3
			};
			double superiorNumGen = Math.random();
			/** Chances
			 *  40% chance of Uncommon Items - various high-end coin-bought gear
			 *  30% chance of Rare Items - Highest-end coin-bought gear, Some poor voting-point/pk-point equipment
			 *  20% chance of Epic Items -Better voting-point/pk-point equipment
			 *  10% chance of Legendary Items - Only top-notch voting-point/pk-point equipment
			 */
			int superiorRewardGrade = superiorNumGen >= 0.60 ? 0 : superiorNumGen >= 0.30 ? 1 : superiorNumGen >= 0.10 ? 2 : 3;
			int superiorRewardPos = RandomUtility.getRandom(superiorRewards[superiorRewardGrade].length-1);
			player.getInventory().delete(15501, 1);
			player.getSkillManager().addExperience(Skill.LUCK, 3500 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().add(superiorRewards[superiorRewardGrade][superiorRewardPos], 1).refreshItems();
			break;
		case 11882:
			player.getSkillManager().addExperience(Skill.LUCK, 100 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11884:
			player.getSkillManager().addExperience(Skill.LUCK, 100 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11906:
			player.getSkillManager().addExperience(Skill.LUCK, 100 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			player.getInventory().delete(11906, 1);
			player.getInventory().add(7394, 1).refreshItems();
			player.getInventory().add(7390, 1).refreshItems();
			player.getInventory().add(7386, 1).refreshItems();
			break;
		case 15262:
			player.getSkillManager().addExperience(Skill.LUCK, 100 + Misc.getRandom(125));
			player.getSkillManager().setCurrentLevel(Skill.LUCK, (int) (player.getSkillManager().getCurrentLevel(Skill.LUCK) + (player.getSkillManager().getMaxLevel(Skill.LUCK) * 0.25)));
		player.getSkillManager().setCurrentLevel(Skill.LUCK, player.getSkillManager().getMaxLevel(Skill.LUCK));
			
			if(!player.getClickDelay().elapsed(1000))
				return;
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		switch(itemId) {
		case 6500:
			if(player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
				player.getPacketSender().sendMessage("You cannot configure this right now.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 101);
			player.setDialogueActionId(60);
			break;
		case 12927:
		case 12926:
			player.getBlowpipeLoading().handleUnloadBlowpipe();
			break;
		case 3962:
			player.getDragonRageLoading().handleUnloadDragonrage();
			break;
		case 896:
			player.getMinigunLoading().handleUnloadMinigun();
			break;
		case 13208:
	
			player.getCorruptBandagesLoading().handleUnloadCorruptBandages();
			
			//player.getClickDelay().reset();
			break;
	
		case 11724:
			if(player.getInventory().contains(6530)){
				player.getInventory().delete(6530, 250);
				player.getInventory().add(897, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Chestplate@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
				} else {
					player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
				}
				break;
		case 11726:
			if(player.getInventory().contains(6530)){
				player.getInventory().delete(6530, 250);
				player.getInventory().add(894, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Tassets@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
				} else {	
					player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
				}
				break;
		case 894:
			if(player.getInventory().contains(6530)){
				player.getInventory().delete(6530, 500);
				player.getInventory().add(895, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
				} else {
					player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
				}
				break;
		case 895:
			if(player.getInventory().contains(6530)){
				player.getInventory().delete(6530, 1000);
				player.getInventory().add(896, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Tassets@la@ to tier 3!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
				} else {
					player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
				}
				break;
		case 897:
			if(player.getInventory().contains(6530)){
				player.getInventory().delete(6530, 500);
				player.getInventory().add(898, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
				} else {
					player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
				}
				break;
		case 7774:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7774) && player.getInventory().getAmount(7774) >= 10) {
			player.getInventory().delete(7774, 10);
			
				player.getInventory().add(995, 1000000000);
				player.getClickDelay().reset();
			player.getPacketSender().sendMessage("You exchange 10 tickets for 1,000,000,000");
			} else {
				player.getPacketSender().sendMessage("You need 10 100m tokens to do this");
				return;
			}
			
			break;
		case 7776:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 10) {
				player.getInventory().delete(7776, 10);
				
					player.getInventory().add(995, 100000000);
					player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You exchange 10 tickets for 100,000,000");
				} else {
					player.getPacketSender().sendMessage("You need 10 10m tokens to do this");
					return;
				}
			break;
		case 7775:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7775) && player.getInventory().getAmount(7775) >= 10) {
				player.getInventory().delete(7775, 10);
				
					player.getInventory().add(995, 10000000);
					player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You exchange 10 tickets for 10,000,000");
				} else {
					player.getPacketSender().sendMessage("You need 10 1m tokens to do this");
					return;
				
				}
			break;
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		case 898:
			if(player.getInventory().contains(6530)){
			player.getInventory().delete(6530, 1000);
			player.getInventory().add(899, 1);
			World.sendMessage("<img=10> <col=008FB2>[Upgrade]"+player.getUsername()+" has just upgraded his @red@Bandos Chestplate@la@ to tier 3!");
			player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
			}
			break;
			
			
			
			
		case 1712:
		case 1710:
		case 1708:
		case 1706:
		case 11118:
		case 11120:
		case 11122:
		case 11124:
			JewelryTeleporting.rub(player, itemId);
			break;
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getSlayer().handleSlayerRingTP(itemId);
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;
		case 995:
			MoneyPouch.depositMoney(player, player.getInventory().getAmount(995));
			break;
		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if(JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if(ItemBinding.isBindable(itemId)) {
			ItemBinding.bindItem(player, itemId);
			return;
		}
		switch(itemId) {
		
		case 995:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			
			
			
			if(player.getInventory().contains(995) && player.getInventory().getAmount(995) >= 99999999) {
				player.getInventory().delete(995, 100000000);
				player.getInventory().add(7774, 1);
				player.getClickDelay().reset();
				//player.getInventory().delete(995, 1000000);
					//player.getPacketSender().sendMessage("You do not have enouph coins!");
			} else {
				player.getPacketSender().sendMessage("You do not have 100m coins in your inventory!");
				
				return;
	
			}
		
		
		
			break;
		case 7775:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.getInventory().contains(7775) && player.getInventory().getAmount(7775) >= 10) {
				
			player.getInventory().delete(7775, 10);
			
				player.getInventory().add(995, 10000000);
				player.getClickDelay().reset();
			
				
		player.getPacketSender().sendMessage("You open the scroll and find gold!");
			} else {
				player.getPacketSender().sendMessage("You dont have enouph token");
				
				
			}
		
		
		
		break;
		
		
		
		
		
		
		
		
		
		case 15330:
			if (!drinkSuperOverload(player, slot, 15330))
				
				return;
			player.getPacketSender().sendInterfaceRemoval();
			player.getCombatBuilder().incrementAttackTimer(1).cooldown(false);
			player.getCombatBuilder().setDistanceTask(null);
			player.setCastSpell(null);
			player.getFoodTimer().reset();
			player.getPotionTimer().reset();
			player.setOverloadPotionTimer(100000);
			String potion = ItemDefinition.forId(itemId).getName();
			if (potion.contains("ummer pi")) {
				player.getPacketSender().sendMessage("You eat your " + potion + ".");
			} else {
				player.getPacketSender().sendMessage("You drink some of your " + potion + ".");
			}
			if (potion.endsWith("(4)")) {
				player.getPacketSender().sendMessage("You have 3 doses of potion left.");
			} else if (potion.endsWith("(3)")) {
				player.getPacketSender().sendMessage("You have 2 doses of potion left.");
			} else if (potion.endsWith("(2)")) {
				player.getPacketSender().sendMessage("You have 1 dose of potion left.");
			} else if (potion.endsWith("(1)")) {
				player.getPacketSender().sendMessage("You have finished your potion.");
			}
			if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
				Consumables.overloadIncrease(player, Skill.ATTACK, 0.38);
				Consumables.overloadIncrease(player, Skill.STRENGTH, 0.38);
				Consumables.overloadIncrease(player, Skill.DEFENCE, 0.38);
				Consumables.overloadIncrease(player, Skill.RANGED, 0.38);
				Consumables.overloadIncrease(player, Skill.MAGIC, 0.38);
			}
			Sounds.sendSound(player, Sound.DRINK_POTION);
			break;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		case 14019:
			player.getPacketSender().sendInterface(60000);
			break;
		case 12927:
		case 12926:
			player.getBlowpipeLoading().handleCheckBlowpipe();
			break;
		case 3962:
			player.getDragonRageLoading().handleCheckDragonrage();
			break;
		case 896:
			player.getMinigunLoading().handleCheckMinigun();
			break;
		case 13208:
			player.getCorruptBandagesLoading().handleCheckCorruptBandages();
			break;
		case 19670:
			if(player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			if(player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(71);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 6500:
			CharmingImp.sendConfig(player);
			break;
		case 4155:
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 103);
			player.setDialogueActionId(65);
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK ? ("You do not have a Slayer task.") : ("Your current task is to kill another "+(player.getSlayer().getAmountToSlay())+" "+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"s."));
			break;
		case 6570:
			if(player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
				player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage("You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
		case 19111:
			if(player.getInventory().contains(19111) && player.getInventory().getAmount(19111) >= 3) {
				player.getInventory().delete(19111, 1).delete(19111, 3).add(6532, 1);
				player.getPacketSender().sendMessage("You have upgraded your TokHaar-Kal cape into a Kryptic TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage("You need at least 3 TokHaar-Kal capes to upgrade your cape.");
			}
			break;
		case 6532:
			if(player.getInventory().contains(6532) && player.getInventory().getAmount(6532) >= 3) {
				player.getInventory().delete(6532, 1).delete(6532, 3).add(6934, 1);
				player.getPacketSender().sendMessage("You have upgraded your Kryptic TokHaar-Kal cape into a Brutal TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage("You need at least 3 Kryptic TokHaar-Kal capes to upgrade your cape.");
			}
			break;
		case 6934:
			if(player.getInventory().contains(6934) && player.getInventory().getAmount(11316) >= 2500) {
				player.getInventory().delete(6934, 1).delete(11316, 2500).add(11318, 1);
				player.getPacketSender().sendMessage("You have upgraded your Brutal TokHaar-Kal cape into an Eternal cape!");
			} else {
				player.getPacketSender().sendMessage("You need a Brutal TokHaar-kal cape and 2500 Elder Monster Essence to upgrade your cape.");
			}
			break;
		case 11318:
			if(player.getInventory().contains(11318) && player.getInventory().getAmount(11320) >= 5000) {
				player.getInventory().delete(11318, 1).delete(11320, 5000).add(11319, 1);
				player.getPacketSender().sendMessage("You have upgraded your Eternal cape into an Immortal cape!");
			} else {
				player.getPacketSender().sendMessage("You need an eternal cape and 5000 Boss fragments to upgrade your cape.");
			}
			break;
			// SLAYER HELMET UPGRADES //
					case 13263: //NORMAL UPGRADE TO LIME
								if(player.getInventory().contains(13263) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
									player.getInventory().delete(13263, 1).delete(11320, 1250).delete(11316, 2500).add(11546, 1);
									player.getPacketSender().sendMessage("You have upgraded your Slayer helmet!");
								} else {
									player.getPacketSender().sendMessage("You need a Slayer helmet and");
									player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Slayer helmet.");
								}
								break;
					case 11546: //LIME UPGRADE TO AQUA
								if(player.getInventory().contains(11546) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
									player.getInventory().delete(11546, 1).delete(11320, 1250).delete(11316, 2500).add(11547, 1);
									player.getPacketSender().sendMessage("You have upgraded your Slayer helmet!");
								} else {
									player.getPacketSender().sendMessage("You need a Lime Slayer helmet and");
									player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Slayer helmet.");
								}
								break;
					case 11547: //AQUA UPGRADE TO CORRUPT
								if(player.getInventory().contains(11547) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
									player.getInventory().delete(11547, 1).delete(11320, 1250).delete(11316, 2500).add(11548, 1);
									player.getPacketSender().sendMessage("You have upgraded your Slayer helmet!");
								} else {
									player.getPacketSender().sendMessage("You need an Aqua Slayer helmet and");
									player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Slayer helmet.");
								}
								break;
					case 11548: //CORRUPT UPGRADE TO BLESSED
								if(player.getInventory().contains(11548) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
									player.getInventory().delete(11548, 1).delete(11320, 3000).delete(11316, 5000).add(11550, 1);
									player.getPacketSender().sendMessage("You have upgraded your Slayer helmet!");
								} else {
									player.getPacketSender().sendMessage("You need a Corrupt Slayer helmet and");
									player.getPacketSender().sendMessage("5000 Elder Monster Essence and 3000 Boss fragments to upgrade your Slayer helmet.");
								}
								break;
					case 11550: //CORRUPT UPGRADE TO BLESSED
								if(player.getInventory().contains(11550) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
									player.getInventory().delete(11550, 1).delete(11320, 5000).delete(11316, 10000).add(11549, 1);
									player.getPacketSender().sendMessage("You have upgraded your Slayer helmet!");
								} else {
									player.getPacketSender().sendMessage("You need a Blessed Slayer helmet and");
									player.getPacketSender().sendMessage("10000 Elder Monster Essence and 5000 Boss fragments to upgrade your Slayer helmet.");
								}
								break;					
								//END//
								//AMULET OF INSANITY//
					case 19886: //DIRE WOLF UPGRADE TO INSANITY
						if(player.getInventory().contains(19886) && player.getInventory().getAmount(11320) >= 15000 && player.getInventory().getAmount(11316) >= 25000) {
							player.getInventory().delete(19886, 1).delete(11320, 1250).delete(11316, 2500).add(11422, 1);
							player.getPacketSender().sendMessage("You have upgraded your Dire Wolf necklace.");
						} else {
							player.getPacketSender().sendMessage("You need a Dire Wolf necklace and");
							player.getPacketSender().sendMessage("25000 Elder Monster Essence and 15000 Boss fragments to upgrade your Barrows gloves.");
						}
						break;
								//END//
								//GLOVES UPGRADE//
					case 7462: //BARROWS UPGRADE TO BLESSED
						if(player.getInventory().contains(7462) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
							player.getInventory().delete(7462, 1).delete(11320, 1250).delete(11316, 2500).add(11552, 1);
							player.getPacketSender().sendMessage("You have upgraded your Barrows gloves");
						} else {
							player.getPacketSender().sendMessage("You need a pair of Barrows gloves and");
							player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Barrows gloves.");
						}
						break;
			case 11552: //BLESSED UPGRADE TO DEATH
						if(player.getInventory().contains(11552) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
							player.getInventory().delete(11552, 1).delete(11320, 2200).delete(11316, 5000).add(11553, 1);
							player.getPacketSender().sendMessage("You have upgraded your Blessed gloves!");
						} else {
							player.getPacketSender().sendMessage("You need a pair of Blessed gloves and");
							player.getPacketSender().sendMessage("5000 Elder Monster Essence and 2200 Boss fragments to upgrade your Blessed gloves.");
						}
						break;			
								//END//
						// RING UPGRADES//
			case 6737: //BERSERKER UPGRADE TO CHAOS
						if(player.getInventory().contains(6737) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
							player.getInventory().delete(6737, 1).delete(11320, 1250).delete(11316, 2500).add(11555, 1);
							player.getPacketSender().sendMessage("You have upgraded your Berserker Ring");
						} else {
							player.getPacketSender().sendMessage("You need a pair of Berserker Ring and");
							player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Berserker Ring.");
						}
						break;
			case 11555: //CHAOS UPGRADE TO DEATH
						if(player.getInventory().contains(11555) && player.getInventory().getAmount(11320) >= 1250 && player.getInventory().getAmount(11316) >= 2500) {
							player.getInventory().delete(11555, 1).delete(11320, 1250).delete(11316, 2500).add(11556, 1);
							player.getPacketSender().sendMessage("You have upgraded your Ring of Chaos!");
						} else {
							player.getPacketSender().sendMessage("You need a Ring of Chaos and");
							player.getPacketSender().sendMessage("2500 Elder Monster Essence and 1250 Boss fragments to upgrade your Ring of Chaos.");
						}
						break;
						//END//
		case 2572:
			if(player.getInventory().contains(2572) && player.getInventory().getAmount(11425) >= 1) {
				final int 
						row = 2572, 
						upgradeMaterial = 11425, 
						potentialResult = 11527;
				
				final int random = Misc.getRandom(0);
				
				player.getInventory()
						
						.delete(upgradeMaterial, 1)
						.refreshItems();
				
				if (random == 0) {
					player.getInventory()
							.add(potentialResult, 1)
							.delete(row, 1)
							.refreshItems();
					player.getPacketSender().sendMessage("You have upgraded your Ring of wealth!");
				} else
					player.getPacketSender().sendMessage("You have failed to upgrade your Ring of wealth!");
			} else {
				player.getPacketSender().sendMessage("You need a Ring of wealth and a Scroll of efficiency to upgrade your Ring of wealth!");
			}
			break;
		case 11527:
			if(player.getInventory().contains(11527) && player.getInventory().getAmount(11425) >= 1) {
				final int 
						row = 11527, 
						upgradeMaterial = 11425, 
						potentialResult = 11529;
				
				final int random = Misc.getRandom(2);
				
				player.getInventory()
						//.delete(row, 1)
						.delete(upgradeMaterial, 1)
						.refreshItems();
				
				if (random == 2) {
					player.getInventory()
					.delete(row, 1)
							.add(potentialResult, 1)
							.refreshItems();
					
					player.getPacketSender().sendMessage("You have upgraded your Ring of wealth [1]!");
				} else
					player.getPacketSender().sendMessage("You have failed to upgrade your Ring of wealth [1]!");
			} else {
				player.getPacketSender().sendMessage("You need a Ring of wealth [1] and a Scroll of efficiency to upgrade your Ring of wealth [1]!");
			}
			break;

		case 11529:
			if(player.getInventory().contains(11529) && player.getInventory().getAmount(11425) >= 1) {
				final int 
						row = 11529, 
						upgradeMaterial = 11425, 
						potentialResult = 11531;
				
				final int random = Misc.getRandom(2);
				
				player.getInventory()
						//.delete(row, 1)
						.delete(upgradeMaterial, 1)
						.refreshItems();
				
				if (random == 2) {
					player.getInventory()
					.delete(row, 1)
							.add(11531, 1)
							.refreshItems();
					player.getPacketSender().sendMessage("You have upgraded your Ring of wealth [2]!");
				} else
					player.getPacketSender().sendMessage("You have failed to upgrade your Ring of wealth [2]!");
			} else {
				player.getPacketSender().sendMessage("You need a Ring of wealth [2] and a Scroll of efficiency to upgrade your Ring of wealth [2]!");
			}
			break;
		case 11531:
			if(player.getInventory().contains(11531) && player.getInventory().getAmount(11425) >= 1) {
				final int 
						row = 11531, 
						upgradeMaterial = 11425, 
						potentialResult = 11533;
				
				final int random = Misc.getRandom(2);
				
				player.getInventory()
						//.delete(row, 1)
						.delete(upgradeMaterial, 1)
						.refreshItems();
				
				if (random == 2) {
					player.getInventory()
					.delete(11531, 1)
							.add(11533, 1)
							.refreshItems();
					player.getPacketSender().sendMessage("You have upgraded your Ring of wealth [3]!");
				} else
					player.getPacketSender().sendMessage("You have failed to upgrade your Ring of wealth [3]!");
			} else {
				player.getPacketSender().sendMessage("You need a Ring of wealth [3] and a Scroll of efficiency to upgrade your Ring of wealth [3]!");
			}
			break;
		case 15262:
			if(!player.getClickDelay().elapsed(1300))
				return;
			int amt = player.getInventory().getAmount(15262);
			if(amt > 0)
				player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
		case 11283: //DFS
			player.getPacketSender().sendMessage("Your Dragonfire shield has "+player.getDfsCharges()+"/20 dragon-fire charges.");
			break;
		case 11613: //dkite
			player.getPacketSender().sendMessage("Your Dragonfire shield has "+player.getDfsCharges()+"/20 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}