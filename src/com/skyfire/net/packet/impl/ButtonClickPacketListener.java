package com.skyfire.net.packet.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.skyfire.model.definitions.NPCDrops;
import com.skyfire.world.content.DropLookup;
import com.skyfire.GameSettings;
import com.skyfire.model.Graphic;
import com.skyfire.model.Item;
import com.skyfire.model.Locations;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.container.impl.Bank;
import com.skyfire.model.container.impl.Bank.BankSearchAttributes;
import com.skyfire.model.definitions.WeaponInterfaces.WeaponInterface;
import com.skyfire.model.input.impl.EnterClanChatToJoin;
import com.skyfire.model.input.impl.EnterSyntaxToBankSearchFor;
import com.skyfire.model.input.impl.PosInput;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.content.Achievements;
import com.skyfire.world.content.BankPin;
import com.skyfire.world.content.BonusManager;
import com.skyfire.world.content.Consumables;
import com.skyfire.world.content.DropLog;
import com.skyfire.world.content.Emotes;
import com.skyfire.world.content.EnergyHandler;
import com.skyfire.world.content.ExperienceLamps;
import com.skyfire.world.content.ItemsKeptOnDeath;
import com.skyfire.world.content.KillsTracker;
import com.skyfire.world.content.LoyaltyProgramme;
import com.skyfire.world.content.MoneyPouch;
import com.skyfire.world.content.PlayerPanel;
import com.skyfire.world.content.PlayersOnlineInterface;
import com.skyfire.world.content.ProfileViewing;
import com.skyfire.world.content.Scoreboards;
import com.skyfire.world.content.Sounds;
import com.skyfire.world.content.StaffList;
import com.skyfire.world.content.StartScreen;
import com.skyfire.world.content.StarterTasks;
import com.skyfire.world.content.WellOfGoodwill;
import com.skyfire.world.content.WellOfWealth;
import com.skyfire.world.content.Sounds.Sound;
import com.skyfire.world.content.books.TestBook;
import com.skyfire.world.content.clan.ClanChat;
import com.skyfire.world.content.clan.ClanChatManager;
import com.skyfire.world.content.combat.magic.Autocasting;
import com.skyfire.world.content.combat.magic.MagicSpells;
import com.skyfire.world.content.combat.prayer.CurseHandler;
import com.skyfire.world.content.combat.prayer.PrayerHandler;
import com.skyfire.world.content.combat.weapon.CombatSpecial;
import com.skyfire.world.content.combat.weapon.FightType;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.content.dialogue.DialogueOptions;
import com.skyfire.world.content.droppreview.AVATAR;
import com.skyfire.world.content.droppreview.BARRELS;
import com.skyfire.world.content.droppreview.BORKS;
import com.skyfire.world.content.droppreview.CERB;
import com.skyfire.world.content.droppreview.CORP;
import com.skyfire.world.content.droppreview.DAGS;
import com.skyfire.world.content.droppreview.GLAC;
import com.skyfire.world.content.droppreview.GWD;
import com.skyfire.world.content.droppreview.KALPH;
import com.skyfire.world.content.droppreview.KBD;
import com.skyfire.world.content.droppreview.LIZARD;
import com.skyfire.world.content.droppreview.NEXX;
import com.skyfire.world.content.droppreview.PHEON;
import com.skyfire.world.content.droppreview.SKOT;
import com.skyfire.world.content.droppreview.SLASHBASH;
import com.skyfire.world.content.droppreview.TDS;
import com.skyfire.world.content.grandexchange.GrandExchange;
import com.skyfire.world.content.minigames.impl.Dueling;
import com.skyfire.world.content.minigames.impl.Nomad;
import com.skyfire.world.content.minigames.impl.PestControl;
import com.skyfire.world.content.minigames.impl.RecipeForDisaster;
import com.skyfire.world.content.minigames.impl.Wassup;
import com.skyfire.world.content.minigames.impl.weapon.WeaponGame;
import com.skyfire.world.content.skill.ChatboxInterfaceSkillAction;
import com.skyfire.world.content.skill.impl.construction.Construction;
import com.skyfire.world.content.skill.impl.crafting.LeatherMaking;
import com.skyfire.world.content.skill.impl.crafting.Tanning;
import com.skyfire.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.skyfire.world.content.skill.impl.dungeoneering.DungeoneeringParty;
import com.skyfire.world.content.skill.impl.dungeoneering.ItemBinding;
import com.skyfire.world.content.skill.impl.fletching.Fletching;
import com.skyfire.world.content.skill.impl.herblore.IngridientsBook;
import com.skyfire.world.content.skill.impl.slayer.Slayer;
import com.skyfire.world.content.skill.impl.smithing.SmithingData;
import com.skyfire.world.content.skill.impl.summoning.PouchMaking;
import com.skyfire.world.content.skill.impl.summoning.SummoningTab;
import com.skyfire.world.content.teleportation.Teleporting;
import com.skyfire.world.content.transportation.TeleportHandler;
import com.skyfire.world.entity.impl.player.Player;
import com.skyfire.world.content.drakoria.DrakoriaPokemon;

/**
 * This packet listener manages a button that the player has clicked upon.
 * 
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int id = packet.readUnsignedShort();

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendMessage("Clicked button: " + id);
		}

		if (checkHandlers(player, id))
			return;
        if( id >= -27885 && id <= -27719) {

            int base_button = -27885;
            int modified_button = (base_button - id);
            int index = Math.abs(modified_button);

            List<Integer> key = player.getAttribute("DROP_KEY");
            if(!player.getClickDelay().elapsed(600)) {
                return;
            }
            player.getClickDelay().reset();
            if (key == null || key.isEmpty()) {
             DropLookup.resetInterface(player);
                return;
            }
            if(index >= key.size()) {
             DropLookup.resetInterface(player);
                return;
            }
            DropLookup.display(player, NPCDrops.forId(key.get(index)));
        }
		if (id >= 32623 && id <= 32722) {
			player.getPlayerOwnedShopManager().handleButton(id);
		}

		if (id >= 32410 && id <= 32460) {
			StaffList.handleButton(player, id);
		}
		switch (id) {

			case 60120:
				player.setAttribute("DRAKORIA_TRAINER", DrakoriaPokemon.Trainer.BROCK);
				break;

			case 60160:
				DrakoriaPokemon.Trainer trainer = player.getAttribute("DRAKORIA_TRAINER");

				if (trainer == null) {
					player.sendMessage("@red@Please select a trainer first.");
					return;
				}

				player.sendMessage("@yel@Pokemon Zone @bla@» @cya@Your journey to @yel@" + trainer.name() + "@cya@ begins now!");

				TeleportHandler.teleportPlayer(
						player,
						new Position(
								trainer.getX(),
								trainer.getY(),
								trainer.getZ()
						),
						player.getSpellbook().getTeleportType()
				);
				break;

		case 26113:
			player.dropLogOrder = !player.dropLogOrder;
			if (player.dropLogOrder) {
				player.getPA().sendFrame126(26113, "Oldest to Newest");
			} else {
				player.getPA().sendFrame126(26113, "Newest to Oldest");
			}
			break;
		
			
		case -29031:
			ProfileViewing.rate(player, true);
			break;
		case -29028:
			ProfileViewing.rate(player, false);
			break;
		case -27454:
		case -27534:
		case 5384:
		case 12729:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case -17631:
			KBD.closeInterface(player);
			break;

		case -11438:
			player.getPlayerOwnedShopManager().openEditor();
			break;

		case -17629:
			if (player.getLocation() == Location.KING_BLACK_DRAGON) {
				KBD.nextItem(player);
			}
			if (player.getLocation() == Location.SLASH_BASH) {
				SLASHBASH.nextItem(player);
			}
			if (player.getLocation() == Location.TORM_DEMONS) {
				TDS.nextItem(player);
			}
			if (player.getLocation() == Location.CORPOREAL_BEAST) {
				CORP.nextItem(player);
			}
			if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
				DAGS.nextItem(player);
			}
			if (player.getLocation() == Location.BANDOS_AVATAR) {
				AVATAR.nextItem(player);
			}
			if (player.getLocation() == Location.KALPHITE_QUEEN) {
				KALPH.nextItem(player);
			}
			if (player.getLocation() == Location.PHOENIX) {
				PHEON.nextItem(player);
			}
			if (player.getLocation() == Location.GLACORS) {
				GLAC.nextItem(player);
			}
			if (player.getLocation() == Location.SKOTIZO) {
				SKOT.nextItem(player);
			}
			if (player.getLocation() == Location.CERBERUS) {
				CERB.nextItem(player);
			}
			if (player.getLocation() == Location.NEX) {
				NEXX.nextItem(player);
			}
			if (player.getLocation() == Location.GODWARS_DUNGEON) {
				GWD.nextItem(player);
			}
			if (player.getLocation() == Location.BORK) {
				BORKS.nextItem(player);
			}
			if (player.getLocation() == Location.LIZARDMAN) {
				LIZARD.nextItem(player);
			}
			if (player.getLocation() == Location.BARRELCHESTS) {
				BARRELS.nextItem(player);
			}
			break;

		case -17630:
			if (player.getLocation() == Location.KING_BLACK_DRAGON) {
				KBD.previousItem(player);
			}
			if (player.getLocation() == Location.SLASH_BASH) {
				SLASHBASH.previousItem(player);
			}
			if (player.getLocation() == Location.TORM_DEMONS) {
				TDS.previousItem(player);
			}
			if (player.getLocation() == Location.CORPOREAL_BEAST) {
				CORP.previousItem(player);
			}
			if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
				DAGS.previousItem(player);
			}
			if (player.getLocation() == Location.BANDOS_AVATAR) {
				AVATAR.previousItem(player);
			}
			if (player.getLocation() == Location.KALPHITE_QUEEN) {
				KALPH.previousItem(player);
			}
			if (player.getLocation() == Location.PHOENIX) {
				PHEON.previousItem(player);
			}
			if (player.getLocation() == Location.GLACORS) {
				GLAC.previousItem(player);
			}
			if (player.getLocation() == Location.SKOTIZO) {
				SKOT.previousItem(player);
			}
			if (player.getLocation() == Location.CERBERUS) {
				CERB.previousItem(player);
			}
			if (player.getLocation() == Location.NEX) {
				NEXX.previousItem(player);
			}
			if (player.getLocation() == Location.GODWARS_DUNGEON) {
				GWD.previousItem(player);
			}
			if (player.getLocation() == Location.BORK) {
				BORKS.previousItem(player);
			}
			if (player.getLocation() == Location.LIZARDMAN) {
				LIZARD.previousItem(player);
			}
			if (player.getLocation() == Location.BARRELCHESTS) {
				BARRELS.previousItem(player);
			}
			break;

//		case -26373:
//			DropLog.open(player);
//			break;
		case 1036:
			EnergyHandler.rest(player);
			break;
//		case -26376:
			// PlayersOnlineInterface.showInterface(player);
//			break;
		case -26376:
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 46343);
			StaffList.updateInterface(player);
			break;
		case 32388:
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639); // 26600
			break;
		case -26359:
			player.setExperienceLocked(!player.experienceLocked());
			player.sendMessage("Your experience is now: "+(player.experienceLocked() ? "locked" : "unlocked"));
			break;
		case -26360:
			player.sendMessage("You have donated a total of: $" + player.getAmountDonated() +"!");
			break;
		case 27229:
			DungeoneeringParty.create(player);
			break;
		case 3229:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			player.sendMessage("Common Costs 50 Skyfire Points.");
			break;
		case 3218:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			player.sendMessage("Uncommon Package Costs 100 Skyfire Points.");
			player.getClickDelay().reset();
			break;
		case 3215:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			player.sendMessage("Extreme Package Costs 200 Skyfire Points.");
			player.getClickDelay().reset();
			break;
			
			
			
        case -27928:
            player.setInputHandling(new NpcSearch());
            player.getPacketSender().sendEnterInputPrompt("Enter an Npc name, or part of it.");
            break;
			
			
			
			
			
			
			
		case 3221:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			player.sendMessage("Rare Package Costs 150 Skyfire Points.");
			player.getClickDelay().reset();
			break;
		case 3235:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			player.sendMessage("Legendary Package Costs 250 Skyfire Points.");
			player.getClickDelay().reset();
			break;
		case 3204:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getImmortalXPoints() >= 150) {
				 player.getInventory().add(15371, 1);
				 player.incrementImmortalXPoints(150);
				 PlayerPanel.refreshPanel(player);
				 player.getClickDelay().reset();
			}
			break;
		case 3206:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getImmortalXPoints() >= 200) {
				 player.getInventory().add(15372, 1);
				 player.incrementImmortalXPoints(200);
				 PlayerPanel.refreshPanel(player);
				 player.getClickDelay().reset();
			} 
			break;
		case 3260:
			player.getPacketSender().sendString(1, "www.Skyfire317.com/forums");
		     player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
			break;
		case 3208:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getImmortalXPoints() >= 100) {
				 player.getInventory().add(15370, 1);
				 player.incrementImmortalXPoints(100);
				 PlayerPanel.refreshPanel(player);
				 player.getClickDelay().reset();
			} 
			break;
		case 3225:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getImmortalXPoints() >= 50) {
				 player.getInventory().add(15369, 1);
				 player.incrementImmortalXPoints(50);
				 PlayerPanel.refreshPanel(player);
				 player.getClickDelay().reset();
			} 
			break;
			
			
			
			

			
		case 3240:
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getImmortalXPoints() >= 250) {
				 player.getInventory().add(15373, 1);
				 player.incrementImmortalXPoints(250);
				 PlayerPanel.refreshPanel(player);
				 player.getClickDelay().reset();
			} 
			break;
		case 26226:
		case 26229:
			if (Dungeoneering.doingDungeoneering(player)) {
				DialogueManager.start(player, 114);
				player.setDialogueActionId(71);
			} else {
				Dungeoneering.leave(player, false, true);
			}
			break;
		case 26244:
		case 26247:
			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
				if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
						.equals(player.getUsername())) {
					DialogueManager.start(player, id == 26247 ? 106 : 105);
					player.setDialogueActionId(id == 26247 ? 68 : 67);
				} else {
					player.getPacketSender().sendMessage("Only the party owner can change this setting.");
				}
			}
			break;
		case -12307:
			if (!StarterTasks.claimReward(player)) {
				player.sendMessage("@red@You cannot claim the reward untill all tasks are complete.");
				return;
			}
			player.sendMessage("Enjoy your reward");
			break;
		case 28180:
			player.getPacketSender().sendInterfaceRemoval();
			if (player.getSummoning().getFamiliar() != null) {
				player.getPacketSender()
						.sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
				player.getPacketSender().sendMessage("You must dismiss your familiar before joining the dungeon");
				return;
			}

			TeleportHandler.teleportPlayer(player, new Position(3450, 3715), player.getSpellbook().getTeleportType());
			break;
		case 14176:
			player.setUntradeableDropItem(null);
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 14175:
			player.getPacketSender().sendInterfaceRemoval();
			if (player.getUntradeableDropItem() != null
					&& player.getInventory().contains(player.getUntradeableDropItem().getId())) {
				ItemBinding.unbindItem(player, player.getUntradeableDropItem().getId());
				player.getInventory().delete(player.getUntradeableDropItem());
				player.getPacketSender().sendMessage("Your item vanishes as it hits the floor.");
				Sounds.sendSound(player, Sound.DROP_ITEM);
			}
			player.setUntradeableDropItem(null);
			break;
		case 1013:
			player.getSkillManager().setTotalGainedExp(0);
			break;
		case -26369:
			if (WellOfGoodwill.isActive()) {
				player.getPacketSender().sendMessage(
						"<img=10> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
								+ WellOfGoodwill.getMinutesRemaining() + " minutes.");
			} else {
				player.getPacketSender()
						.sendMessage("<img=10> <col=008FB2>The Well of Goodwill needs another "
								+ Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount())
								+ " coins before becoming full.");
			}
			break;
		case -26368:
			
			break;
			
			
			

			
			
			
			
			
			
			
			
			
			
			
		case -26374:
			KillsTracker.open(player);
			break;
		case -26373:
			DropLog.open(player);
			break;
		case -13627:
			Scoreboards.open(player, Scoreboards.HIGHEST_SLAYER_STREAK);
			break;
		case -13628:
			Scoreboards.open(player, Scoreboards.TOP_TRIVIA_ANSWERS);
			break;
		case -13629:
			Scoreboards.open(player, Scoreboards.MOST_TIME_PLAYED);
			break;
		case -13630:
			Scoreboards.open(player, Scoreboards.TOP_DONATOR);
			break;
		case -13631:
			Scoreboards.open(player, Scoreboards.TOP_ACHIEVER);
			break;
		case -13633:
			Scoreboards.open(player, Scoreboards.MOST_MONEY_IN_POUCH);
			break;
		case -13634:
			Scoreboards.open(player, Scoreboards.TOP_TOTAL_XP);
			break;
		case -13635:
			Scoreboards.open(player, Scoreboards.NPC_KILLS);
			break;
		case -13624:
			Scoreboards.open(player, Scoreboards.MOST_CLUES_COMPLETED);
			break;
		case -13625:
			Scoreboards.open(player, Scoreboards.MOST_VOTES_DONE);
			break;
		case -13622:
			Scoreboards.open(player, Scoreboards.PVM_POINTS);
			break;
		case -13626:
			Scoreboards.open(player, Scoreboards.TOP_PRESTIGE_POINTS);
			break;
		case -13623:
			Scoreboards.open(player, Scoreboards.CHOPS);
			break;
		case -30281:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 26614:
			DropLog.open(player);
			break;

		//case -10531:
			//if (player.isKillsTrackerOpen()) {
				//player.setKillsTrackerOpen(false);
				//player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
			//	PlayerPanel.refreshPanel(player);
			//}
		//	break;

		case 28177:
			player.getPacketSender()
			.sendMessage( "@red@Start killing bosses to train Boss Hunting skill!");
			player.getPacketSender()
			.sendMessage( "@blu@Trade Valentina at home for the rewards!");
			
			break;
		case -30282:
			KillsTracker.openBoss(player);
			break;
		case -10283:
			KillsTracker.open(player);
			break;

		case 11014:
			Teleporting.openTab(player, -4928);
			break;
//		case -26333:
//			player.getPacketSender().sendString(1, "www.runeunity.org/forum");
//			player.getPacketSender().sendMessage("Attempting to open: www.runeunity.org/forum");
//			break;
		case -26332:
			player.getPacketSender().sendString(1, "Skyfire.net/forums");
			player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
			break;
		case -26331:
			player.getPacketSender().sendString(1, "Skyfire.net/forums");
			player.getPacketSender().sendMessage("Attempting to open: www.Skyfire..net/forums");
			break;
		case -26330:
			player.getPacketSender().sendString(1, "www.Skyfire.net/forums");
			player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
			break;
		case -26329:
			player.getPacketSender().sendString(1, "www.Skyfire.net/forums");
			player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
			break;
		case -26328:
			player.getPacketSender().sendString(1, "www.Skyfire.net/forums");
			player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
			break;
		case -26335:
			RecipeForDisaster.openQuestLog(player);
			break;
		case -26334:
			Nomad.openQuestLog(player);
			break;
		case -26375:
		
			Wassup.openQuestLog(player);
			break;
		case 350:
			player.getPacketSender()
					.sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
					.sendTab(GameSettings.MAGIC_TAB).sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
			break;
		case 12162:
			DialogueManager.start(player, 61);
			player.setDialogueActionId(28);
			break;
		case 29335:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			DialogueManager.start(player, 60);
			player.setDialogueActionId(27);
			break;
		case 29455:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			ClanChatManager.toggleLootShare(player);
			break;
		case 8658:
			DialogueManager.start(player, 55);
			player.setDialogueActionId(26);
			break;
		case 11001:
			TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),
					player.getSpellbook().getTeleportType());
			break;
		case 8667:
			TeleportHandler.teleportPlayer(player, new Position(2742, 3443), player.getSpellbook().getTeleportType());
			break;
		case 8672:
			TeleportHandler.teleportPlayer(player, new Position(3216, 3415), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage(
					"<img=10> To get started with Archaoelogy, buy a spade and start to dig anywhere for loot.");
			
			player.getPacketSender().sendMessage(
					"<img=10> You can dig anywhere for XP, but certain areas such as wilderness give more");
			player.getPacketSender().sendMessage(
					"<img=10> loot and xp! The deeper you adventure in wild! The greater the reward!");
			
			
			break;
		case 8861:
			TeleportHandler.teleportPlayer(player, new Position(2914, 3450), player.getSpellbook().getTeleportType());
			break;
		case 8656:
			player.setDialogueActionId(47);
			DialogueManager.start(player, 86);
			break;
		case 8659:
			TeleportHandler.teleportPlayer(player, new Position(3024, 9741), player.getSpellbook().getTeleportType());
			break;
		case 8664:
			TeleportHandler.teleportPlayer(player, new Position(3220, 3426), player.getSpellbook().getTeleportType());
			break;
		case 8666:
			TeleportHandler.teleportPlayer(player, new Position(3085, 3496), player.getSpellbook().getTeleportType());
			break;
			

		/*
		 * Teleporting Called Below
		 */

		case -4914:
		case -4911:
		case -4908:
		case -4905:
		case -4899:
		case -4896:
		case -4893:
		case -4890:
		case -4845:
		case -4839:
		case -4842:
			Teleporting.teleport(player, id);
			break;
			
		case -4902:
			if(player.getSummoning().getFamiliar() != null) {
				player.getPacketSender().sendMessage("You must dismiss your familiar before teleporting to the arena!");
		} else {
			Teleporting.teleport(player, id);
		}
		break;

		
		
		  case 10003:

              Teleporting.openTab(player, -4934);
              break;
		
		
		
		
		
		
		
		
		
		
		
		//case 10003:
			//Teleporting.openTab(player, -4934);
			//break;
		case -4934:
			Teleporting.openTab(player, -4934);
			break;
		case -4931:
			Teleporting.openTab(player, -4931);
			break;
		case -4928:
			Teleporting.openTab(player, -4928);
			break;
		case -4925:
			Teleporting.openTab(player, -4925);
			break;
		case -4922:
			Teleporting.openTab(player, -4922);
			break;
		case -4919:
			Teleporting.openTab(player, -4919);
			break;

		/*
		 * End Teleporting
		 */
			
		/*case -4845:
			WeaponGame.addToLobby(player);
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("Weapon Game is currently under development!");

			break;*/
		case 8671:
			TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook().getTeleportType());
			break;

		case 8670:
			TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook().getTeleportType());
			break;
		case 8668:
			TeleportHandler.teleportPlayer(player, new Position(2709, 3437), player.getSpellbook().getTeleportType());
			break;
		case 8665:
			TeleportHandler.teleportPlayer(player, new Position(3079, 3495), player.getSpellbook().getTeleportType());
			break;
		case 8662:
			TeleportHandler.teleportPlayer(player, new Position(2345, 3698), player.getSpellbook().getTeleportType());
			break;
		case 13928:
			TeleportHandler.teleportPlayer(player, new Position(2209, 5346), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage(
					"<img=10> To get started with Warding, Use the fragments from Divination on any 3 Obelisk");
			
			break;
		case 28179:
			TeleportHandler.teleportPlayer(player, new Position(2209, 5348), player.getSpellbook().getTeleportType());
			break;
		case 28178:
			TeleportHandler.teleportPlayer(player, new Position(3123, 3225), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage(
					"<img=10> To get started with Divination, harvest energy from wisps for rewards");
			
			break;
		case 1159: // Bones to Bananas
		case 15877:// Bones to peaches
		case 30306:
			MagicSpells.handleMagicSpells(player, id);
			break;
		case 10001:
			if (player.getInterfaceId() == -1) {
				Consumables.handleHealAction(player);
			} else {
				player.getPacketSender().sendMessage("You cannot heal yourself right now.");
			}
			break;
		case 18025:
			if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
			}
			break;
		case 18018:
			if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
			}
			break;
		case 10000:
		case 950:
			if (player.getInterfaceId() < 0)
				player.getPacketSender().sendInterface(40030);
			else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case 3546:
		case 3420:
			if (System.currentTimeMillis() - player.getTrading().lastAction <= 300)
				return;
			player.getTrading().lastAction = System.currentTimeMillis();
			if (player.getTrading().inTrade()) {
				player.getTrading().acceptTrade(id == 3546 ? 2 : 1);
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
			break;
		case 10162:
		case -18269:
		case 11729:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 841:
			IngridientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
			//TestBook.readBook(player, player.getCurrentBookPage() + 2, true);
			break;
		case 839:
			IngridientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
			//TestBook.readBook(player, player.getCurrentBookPage() - 2, true);
			break;
		case 14922:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			break;
		case 14921:
			player.getPacketSender().sendMessage("Please visit the forums and ask for help in the support section.");
			break;
		case 5294:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
			DialogueManager.start(player,
					DialogueManager.getDialogues().get(player.getBankPinAttributes().hasBankPin() ? 12 : 9));
			break;
		case 27653:
			if (!player.busy() && !player.getCombatBuilder().isBeingAttacked()
					&& !Dungeoneering.doingDungeoneering(player)) {
				player.getSkillManager().stopSkilling();
				player.getPriceChecker().open();
			} else {
				player.getPacketSender().sendMessage("You cannot open this right now.");
			}
			break;
		case 2735:
		case 1511:
			if (player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().toInventory();
				player.getPacketSender().sendInterfaceRemoval();
			} else {
				player.getPacketSender().sendMessage("You do not have a familiar who can hold items.");
			}
			break;
		case -11501:
		case -11504:
		case -11498:
		case -11507:
		case 1020:
		case 1021:
		case 1019:
		case 1018:
			if (id == 1020 || id == -11504)
				SummoningTab.renewFamiliar(player);
			else if (id == 1019 || id == -11501)
				SummoningTab.callFollower(player);
			else if (id == 1021 || id == -11498)
				SummoningTab.handleDismiss(player, false);
			else if (id == -11507)
				player.getSummoning().store();
			else if (id == 1018)
				player.getSummoning().toInventory();
			break;
		case 11004:
			player.getPacketSender().sendTab(GameSettings.SKILLS_TAB);
			DialogueManager.sendStatement(player, "Simply press on the skill you want to train in the skills tab.");
			player.setDialogueActionId(-1);
			break;
		case 8654:
		case 8657:
		case 8655:
		case 8663:
		case 8669:
		case 8660:
		case 11008:
			Teleporting.openTab(player, -4934);
			break;
		case 11017:
			Teleporting.openTab(player, -4931);
			break;
		case 11011:
			Teleporting.openTab(player, -4919);
			break;
		case 11020:
			Teleporting.openTab(player, -4922);
			break;
		case 2799:
		case 2798:
		case 1747:
		case 1748:
		case 8890:
		case 8886:
		case 8875:
		case 8871:
		case 8894:
			ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
			break;
		case 14873:
		case 14874:
		case 14875:
		case 14876:
		case 14877:
		case 14878:
		case 14879:
		case 14880:
		case 14881:
		case 14882:
			BankPin.clickedButton(player, id);
			break;
		case 27005:
		case 22012:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
			break;
		case 27023:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			if (player.getSummoning().getBeastOfBurden() == null) {
				player.getPacketSender().sendMessage("You do not have a familiar which can hold items.");
				return;
			}
			Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
			break;
		case 22008:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setNoteWithdrawal(!player.withdrawAsNote());
			break;
		case 21000:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setSwapMode(false);
			player.getPacketSender().sendConfig(304, 0).sendMessage("This feature is coming soon!");
			// player.setSwapMode(!player.swapMode());
			break;
		case 27009:
			MoneyPouch.toBank(player);
			break;
		case 27014:
		case 27015:
		case 27016:
		case 27017:
		case 27018:
		case 27019:
		case 27020:
		case 27021:
		case 27022:
			if (!player.isBanking())
				return;
			if (player.getBankSearchingAttribtues().isSearchingBank())
				BankSearchAttributes.stopSearch(player, true);
			int bankId = id - 27014;
			boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
			if (!empty || bankId == 0) {
				player.setCurrentBankTab(bankId);
				player.getPacketSender().sendString(5385, "scrollreset");
				player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab()));
				player.getPacketSender().sendString(27000, "1");
				player.getBank(bankId).open();
			} else
				player.getPacketSender().sendMessage("To reincarnate a new tab, please drag an item here.");
			break;
		case 22004:
			if (!player.isBanking())
				return;
			if (!player.getBankSearchingAttribtues().isSearchingBank()) {
				player.getBankSearchingAttribtues().setSearchingBank(true);
				player.setInputHandling(new EnterSyntaxToBankSearchFor());
				player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
			} else {
				BankSearchAttributes.stopSearch(player, true);
			}
			break;
		case 32602:
			player.setInputHandling(new PosInput());
			player.getPacketSender().sendEnterInputPrompt("What/Who would you like to search for?");
			break;
		case 22845:
		case 24115:
		case 24010:
		case 24041:
		case 150:
			player.setAutoRetaliate(!player.isAutoRetaliate());
			break;
		case 29332:
			ClanChat clan = player.getCurrentClanChat();
			if (clan == null) {
				player.getPacketSender().sendMessage("You are not in a clanchat channel.");
				return;
			}
			ClanChatManager.leave(player, false);
			player.setClanChatName(null);
			break;
		case 29329:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.setInputHandling(new EnterClanChatToJoin());
			player.getPacketSender().sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
			break;
		case 19158:
		case 152:
			if (player.getRunEnergy() <= 1) {
				player.getPacketSender().sendMessage("You do not have enough energy to do this.");
				player.setRunning(false);
			} else
				player.setRunning(!player.isRunning());
			player.getPacketSender().sendRunStatus();
			break;

		case -282:
			DropLog.openRare(player);
			break;

		case 27658:
			player.setExperienceLocked(!player.experienceLocked());
			String type = player.experienceLocked() ? "locked" : "unlocked";
			player.getPacketSender().sendMessage("Your experience is now " + type + ".");
			PlayerPanel.refreshPanel(player);
			break;
		case 27651:
		case 21341:
			if (player.getInterfaceId() == -1) {
				player.getSkillManager().stopSkilling();
				BonusManager.update(player);
				player.getPacketSender().sendInterface(21172);
			} else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case 27654:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.getSkillManager().stopSkilling();
			ItemsKeptOnDeath.sendInterface(player);
			break;
			
		case -10531:
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
			PlayerPanel.refreshPanel(player);
			//player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 26600); // 26600
			break;
			
		case -26386:
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 45000); // 26600
			break;
		case 26701:
			
			if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 1) {
				
				
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You offer your 10m scrolls, and you are teleported to Avatar of Death!");
				
				TeleportHandler.teleportPlayer(player, new Position(2915, 9830), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@blu@Welcome to Avatar of death lair! Good luck!");
				player.getInventory().delete(7776, 1);
				TeleportHandler.teleportPlayer(player, new Position(2915, 9830), player.getSpellbook().getTeleportType());
				
			}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x1 10m@red@ Token to be teleported to Avatar of death");
				}
			
			
	
	
			break;
			
			
			
			
			
			
			
		case 26705:
	
			
			
			
	if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 5) {
				
				
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You offer your 50m in scrolls, and you are teleported to the Chrono");
				TeleportHandler.teleportPlayer(player, new Position(3226, 3366), player.getSpellbook().getTeleportType());
				player.getInventory().delete(7776, 5);
				TeleportHandler.teleportPlayer(player, new Position(3226, 3366), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@red@Welcome to Chrono, he will destroy you...");
		
				
		
				
				
		
				
				}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x5 10m@red@ Token to be teleported to Chrono");
				}
			
			
			
			
			break;
			
			
			
		case 26706:
	
			
			
			
			
			
			
			
			
			
			
	if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 7) {
				
				
				player.getPacketSender().sendMessage("You offer your 70m in scrolls, and you are teleported to the Elven Gang");
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				TeleportHandler.teleportPlayer(player, new Position(2525, 3167), player.getSpellbook().getTeleportType());
				player.getInventory().delete(7776, 7);
				TeleportHandler.teleportPlayer(player, new Position(2525, 3167), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@dre@Welcome to Elven Gangs Hide out! They run the place here!");
		
				
			
				
				
		
				
				}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x7 10m@red@ Token to be teleported to the Elven Gang");
				}
			
			
			
			
			
			
			
			
			
			break;
			
			
			
			
		case 26704:

			
			
			
	if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 3) {
				
				
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You offer your 300m in scrolls, and you are teleported to the Mad King");
				TeleportHandler.teleportPlayer(player, new Position(2606, 3870), player.getSpellbook().getTeleportType());
				player.getInventory().delete(7776, 3);
				TeleportHandler.teleportPlayer(player, new Position(2606, 3870), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@blu@Welcome to the Mad Kings Land, he gets anything he wants!");
		
				
				
		
				
				}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x3 10m@red@ Token to be teleported to the Mad King");
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			break;
			
			
			
	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		case 26702:
		
			
			
			
			
			
			

			if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 1) {
				
				
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You offer your 10m scroll, and you are teleported to the Necromancer God!");
				TeleportHandler.teleportPlayer(player, new Position(3279, 3512), player.getSpellbook().getTeleportType());
				player.getInventory().delete(7776, 1);
				TeleportHandler.teleportPlayer(player, new Position(3279, 3512), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@mag@Welcome to Necromancer God, Good luck!");
		
				
				}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x1 10m@red@ Token to be teleported to Necromancer God");
				}
			
			
			
			
			
			
			
			
			
			
			
			
			break;
			
			
			
		case 26703:
		
			
			
			
			
			
			
			
			
			

			if(player.getInventory().contains(7776) && player.getInventory().getAmount(7776) >= 2) {
				
			
				
					//player.getInventory().add(995, 1000000);
					//player.getClickDelay().reset();
				player.getPacketSender().sendMessage("You offer your 20m in scrolls, and you are teleported to the Lurer");
				TeleportHandler.teleportPlayer(player, new Position(2507, 4712), player.getSpellbook().getTeleportType());
				player.getInventory().delete(7776, 2);
				TeleportHandler.teleportPlayer(player, new Position(2507, 4712), player.getSpellbook().getTeleportType());
				
				player.getPacketSender()
				.sendMessage("@gre@Welcome to the Lurers Lair, may the gods be on your...side");
		
				
				
		
				
				}	else {
					player.getPacketSender().sendMessage("@red@You must pay @dre@x2 10m@red@ Token to be teleported to the Lurer");
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			break;
			
			
			
			
		case 2458: // Logout
			player.performGraphic(new Graphic(2036));
			
			if (player.logout()) {
				World.getPlayers().remove(player);
			}
			break;
		case 29138:
		case 29038:
		case 29063:
		case 29113:
		case 29163:
		case 29188:
		case 29213:
		case 29238:
		case 30007:
		case 48023:
		case 33033:
		case 30108:
		case 7473:
		case 7562:
		case 7487:
		case 7788:
		case 8481:
		case 7612:
		case 7587:
		case 7662:
		case 7462:
		case 7548:
		case 7687:
		case 7537:
		case 12322:
		case 7637:
		case 12311:
		case -24530:
			CombatSpecial.activate(player);
			break;
		case 1772: // shortbow & longbow
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_ACCURATE);
			}
			break;
		case 1771:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_RAPID);
			}
			break;
		case 1770:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_LONGRANGE);
			}
			break;
		case 2282: // dagger & sword
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_STAB);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_STAB);
			}
			break;
		case 2285:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_LUNGE);
			}
			break;
		case 2284:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_SLASH);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_SLASH);
			}
			break;
		case 2283:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_BLOCK);
			}
			break;
		case 2429: // scimitar & longsword
			if (player.getWeapon() == WeaponInterface.SCIMITAR|| player.getWeapon() == WeaponInterface.FASTSPEED|| player.getWeapon() == WeaponInterface.FASTSPEED1|| player.getWeapon() == WeaponInterface.FASTSPEED2|| player.getWeapon() == WeaponInterface.FASTSPEED3) {
				player.setFightType(FightType.SCIMITAR_CHOP);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_CHOP);
			}
			break;
		case 2432:
			if (player.getWeapon() == WeaponInterface.SCIMITAR|| player.getWeapon() == WeaponInterface.FASTSPEED|| player.getWeapon() == WeaponInterface.FASTSPEED1|| player.getWeapon() == WeaponInterface.FASTSPEED2|| player.getWeapon() == WeaponInterface.FASTSPEED3) {
				player.setFightType(FightType.SCIMITAR_SLASH);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_SLASH);
			}
			break;
		case 2431:
			if (player.getWeapon() == WeaponInterface.SCIMITAR|| player.getWeapon() == WeaponInterface.FASTSPEED|| player.getWeapon() == WeaponInterface.FASTSPEED1|| player.getWeapon() == WeaponInterface.FASTSPEED2|| player.getWeapon() == WeaponInterface.FASTSPEED3) {
				player.setFightType(FightType.SCIMITAR_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_LUNGE);
			}
			break;
		case 2430:
			if (player.getWeapon() == WeaponInterface.SCIMITAR || player.getWeapon() == WeaponInterface.FASTSPEED|| player.getWeapon() == WeaponInterface.FASTSPEED1|| player.getWeapon() == WeaponInterface.FASTSPEED2|| player.getWeapon() == WeaponInterface.FASTSPEED3) {
				player.setFightType(FightType.SCIMITAR_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_BLOCK);
			}
			break;
		case 3802: // mace
			player.setFightType(FightType.MACE_POUND);
			break;
		case 3805:
			player.setFightType(FightType.MACE_PUMMEL);
			break;
		case 3804:
			player.setFightType(FightType.MACE_SPIKE);
			break;
		case 3803:
			player.setFightType(FightType.MACE_BLOCK);
			break;
		case 4454: // knife, thrownaxe, dart & javelin
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_ACCURATE);
			}
			break;
		case 4453:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_RAPID);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_RAPID);
			}
			break;
		case 4452:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_LONGRANGE);
			}
			break;
		case 4685: // spear
			player.setFightType(FightType.SPEAR_LUNGE);
			break;
		case 4688:
			player.setFightType(FightType.SPEAR_SWIPE);
			break;
		case 4687:
			player.setFightType(FightType.SPEAR_POUND);
			break;
		case 4686:
			player.setFightType(FightType.SPEAR_BLOCK);
			break;
		case 4711: // 2h sword
			player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
			break;
		case 4714:
			player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
			break;
		case 4713:
			player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
			break;
		case 4712:
			player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
			break;
		case 5576: // pickaxe
			player.setFightType(FightType.PICKAXE_SPIKE);
			break;
		case 5579:
			player.setFightType(FightType.PICKAXE_IMPALE);
			break;
		case 5578:
			player.setFightType(FightType.PICKAXE_SMASH);
			break;
		case 5577:
			player.setFightType(FightType.PICKAXE_BLOCK);
			break;
		case 7768: // claws
			player.setFightType(FightType.CLAWS_CHOP);
			break;
		case 7771:
			player.setFightType(FightType.CLAWS_SLASH);
			break;
		case 7770:
			player.setFightType(FightType.CLAWS_LUNGE);
			break;
		case 7769:
			player.setFightType(FightType.CLAWS_BLOCK);
			break;
		case 8466: // halberd
			player.setFightType(FightType.HALBERD_JAB);
			break;
		case 8468:
			player.setFightType(FightType.HALBERD_SWIPE);
			break;
		case 8467:
			player.setFightType(FightType.HALBERD_FEND);
			break;
		case 5862: // unarmed
			player.setFightType(FightType.UNARMED_PUNCH);
			break;
		case 5861:
			player.setFightType(FightType.UNARMED_KICK);
			break;
		case 5860:
			player.setFightType(FightType.UNARMED_BLOCK);
			break;
		case 12298: // whip
			player.setFightType(FightType.WHIP_FLICK);
			break;
		case 12297:
			player.setFightType(FightType.WHIP_LASH);
			break;
		case 12296:
			player.setFightType(FightType.WHIP_DEFLECT);
			break;
		case 336: // staff
			player.setFightType(FightType.STAFF_BASH);
			break;
		case 335:
			player.setFightType(FightType.STAFF_POUND);
			break;
		case 334:
			player.setFightType(FightType.STAFF_FOCUS);
			break;
		case 433: // warhammer
			player.setFightType(FightType.WARHAMMER_POUND);
			break;
		case 432:
			player.setFightType(FightType.WARHAMMER_PUMMEL);
			break;
		case 431:
			player.setFightType(FightType.WARHAMMER_BLOCK);
			break;
		case 782: // scythe
			player.setFightType(FightType.SCYTHE_REAP);
			break;
		case 784:
			player.setFightType(FightType.SCYTHE_CHOP);
			break;
		case 785:
			player.setFightType(FightType.SCYTHE_JAB);
			break;
		case 783:
			player.setFightType(FightType.SCYTHE_BLOCK);
			break;
		case 1704: // battle axe
			player.setFightType(FightType.BATTLEAXE_CHOP);
			break;
		case 1707:
			player.setFightType(FightType.BATTLEAXE_HACK);
			break;
		case 1706:
			player.setFightType(FightType.BATTLEAXE_SMASH);
			break;
		case 1705:
			player.setFightType(FightType.BATTLEAXE_BLOCK);
			break;
		}
	}

	private boolean checkHandlers(Player player, int id) {
		if (Construction.handleButtonClick(id, player)) {
			return true;
		}
		switch (id) {
		case 2494:
		case 2495:
		case 2496:
		case 2497:
		case 2498:
		case 2471:
		case 2472:
		case 2473:
		case 2461:
		case 2462:
		case 2482:
		case 2483:
		case 2484:
		case 2485:
			DialogueOptions.handle(player, id);
			return true;
		}
		if (player.isPlayerLocked() && id != 2458 && id != -12780 && id != -12779 && id != -12778 && id != -29767) {
			   player.getPacketSender().sendMessage("Attempting to open: www.Skyfire.net/forums");
				
				
			return true;
		}
		if (Achievements.handleButton(player, id)) {
			return true;
		}
		if (Sounds.handleButton(player, id)) {
			return true;
		}
		if (PrayerHandler.isButton(id)) {
			PrayerHandler.togglePrayerWithActionButton(player, id);
			return true;
		}
		if (CurseHandler.isButton(player, id)) {
			return true;
		}
		if (StartScreen.handleButton(player, id)) {
			return true;
		}
		if (Autocasting.handleAutocast(player, id)) {
			return true;
		}
		if (SmithingData.handleButtons(player, id)) {
			return true;
		}
		if (PouchMaking.pouchInterface(player, id)) {
			return true;
		}
		if (LoyaltyProgramme.handleButton(player, id)) {
			return true;
		}
		if (Fletching.fletchingButton(player, id)) {
			return true;
		}
		if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
			return true;
		}
		if (Emotes.doEmote(player, id)) {
			return true;
		}
		if (PestControl.handleInterface(player, id)) {
			return true;
		}
		if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
			return true;
		}
		if (Slayer.handleRewardsInterface(player, id)) {
			return true;
		}
		if (ExperienceLamps.handleButton(player, id)) {
			return true;
		}
		if (PlayersOnlineInterface.handleButton(player, id)) {
			return true;
		}
		if (GrandExchange.handleButton(player, id)) {
			return true;
		}
		if (ClanChatManager.handleClanChatSetupButton(player, id)) {
			return true;
		}
		return false;
	}

	public static final int OPCODE = 185;
}
