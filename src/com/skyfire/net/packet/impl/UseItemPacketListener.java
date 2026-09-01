package com.skyfire.net.packet.impl;

import com.skyfire.engine.task.impl.WalkToTask;
import com.skyfire.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.skyfire.model.Animation;
import com.skyfire.model.GameMode;
import com.skyfire.model.GameObject;
import com.skyfire.model.Graphic;
import com.skyfire.model.Item;
import com.skyfire.model.PlayerRights;
import com.skyfire.model.Position;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.definitions.GameObjectDefinition;
import com.skyfire.model.definitions.ItemDefinition;
import com.skyfire.net.packet.Packet;
import com.skyfire.net.packet.PacketListener;
import com.skyfire.util.Misc;
import com.skyfire.util.RandomUtility;
import com.skyfire.world.World;
import com.skyfire.world.clip.region.RegionClipping;
import com.skyfire.world.content.GamblingAction;
import com.skyfire.world.content.ItemForging;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.content.minigames.impl.WarriorsGuild;
import com.skyfire.world.content.minigames.impl.HerculesBay.BarrowsSpawning;
import com.skyfire.world.content.skill.impl.cooking.Cooking;
import com.skyfire.world.content.skill.impl.cooking.CookingData;
import com.skyfire.world.content.skill.impl.crafting.Gems;
import com.skyfire.world.content.skill.impl.crafting.LeatherMaking;
import com.skyfire.world.content.skill.impl.firemaking.Firemaking;
import com.skyfire.world.content.skill.impl.fletching.Fletching;
import com.skyfire.world.content.skill.impl.herblore.Herblore;
import com.skyfire.world.content.skill.impl.herblore.PotionCombinating;
import com.skyfire.world.content.skill.impl.herblore.WeaponPoison;
import com.skyfire.world.content.skill.impl.prayer.BonesOnAltar;
import com.skyfire.world.content.skill.impl.prayer.EnergyOnAltar;
import com.skyfire.world.content.skill.impl.prayer.Prayer;
import com.skyfire.world.content.skill.impl.slayer.SlayerDialogues;
import com.skyfire.world.content.skill.impl.slayer.SlayerTasks;
import com.skyfire.world.content.skill.impl.smithing.EquipmentMaking;
import com.skyfire.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player 'uses' an item on another
 * entity.
 * 
 * @author relex lawl
 */

public class UseItemPacketListener implements PacketListener {

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	// private final static Logger logger =
	// Logger.getLogger(UseItemPacketListener.class);

	private static void useItem(Player player, Packet packet) {
		if (player.isTeleporting() || player.getConstitution() <= 0)
			return;
		int interfaceId = packet.readLEShortA();
		int slot = packet.readShortA();
		int id = packet.readLEShort();
	}

	private static void itemOnItem(Player player, Packet packet) {
		int usedWithSlot = packet.readUnsignedShort();
		int itemUsedSlot = packet.readUnsignedShortA();
		if (usedWithSlot < 0 || itemUsedSlot < 0 || itemUsedSlot > player.getInventory().capacity()
				|| usedWithSlot > player.getInventory().capacity())
			return;
		Item usedWith = player.getInventory().getItems()[usedWithSlot];
		Item itemUsedWith = player.getInventory().getItems()[itemUsedSlot];
		if (usedWith.getId() == 6573 || itemUsedWith.getId() == 6573) {
			player.getPacketSender().sendMessage("To make an Amulet of Fury, you need to put an onyx in a furnace.");
			return;
		}
		
		
		if (usedWith.getId() == 12927) {
			player.getBlowpipeLoading().handleLoadBlowpipe(itemUsedWith);
			return;
		}
		if (itemUsedWith.getId() == 12927) {
			player.getBlowpipeLoading().handleLoadBlowpipe(usedWith);
			return;
		}
		
		
		
		if (usedWith.getId() == 12926) {
			player.getBlowpipeLoading().handleLoadBlowpipe(itemUsedWith);
			return;
		}
		if (itemUsedWith.getId() == 12926) {
			player.getBlowpipeLoading().handleLoadBlowpipe(usedWith);
			return;
		}
		if (usedWith.getId() == 3962) {
			player.getDragonRageLoading().handleLoadDragonrage(itemUsedWith);
			return;
		}
		if (itemUsedWith.getId() == 3962) {
			player.getDragonRageLoading().handleLoadDragonrage(usedWith);
			return;
		}
		if (usedWith.getId() == 13208) {
			player.getCorruptBandagesLoading().handleLoadCorruptBandages(itemUsedWith);
			return;
		}
		if (itemUsedWith.getId() == 13208) {
			player.getCorruptBandagesLoading().handleLoadCorruptBandages(usedWith);
			return;
		}
		if (usedWith.getId() == 896) {
			player.getMinigunLoading().handleLoadMinigun(itemUsedWith);
			return;
		}
		if (itemUsedWith.getId() == 896) {
			player.getMinigunLoading().handleLoadMinigun(usedWith);
			return;
		}
		WeaponPoison.execute(player, itemUsedWith.getId(), usedWith.getId());
		
		

		
			
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if (itemUsedWith.getId() == 590 || usedWith.getId() == 590)
			Firemaking.lightFire(player, itemUsedWith.getId() == 590 ? usedWith.getId() : itemUsedWith.getId(), false,
					1);
		if (itemUsedWith.getDefinition().getName().contains("(") && usedWith.getDefinition().getName().contains("("))
			PotionCombinating.combinePotion(player, usedWith.getId(), itemUsedWith.getId());
		if (usedWith.getId() == Herblore.VIAL || itemUsedWith.getId() == Herblore.VIAL) {
			if (Herblore.makeUnfinishedPotion(player, usedWith.getId())
					|| Herblore.makeUnfinishedPotion(player, itemUsedWith.getId()))
				return;
		}
		if (Herblore.finishPotion(player, usedWith.getId(), itemUsedWith.getId())
				|| Herblore.finishPotion(player, itemUsedWith.getId(), usedWith.getId()))
			return;
		if (usedWith.getId() == 946 || itemUsedWith.getId() == 946)
			Fletching.openSelection(player, usedWith.getId() == 946 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 1777 || itemUsedWith.getId() == 1777)
			Fletching.openBowStringSelection(player,
					usedWith.getId() == 1777 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 53 || itemUsedWith.getId() == 53 || usedWith.getId() == 52
				|| itemUsedWith.getId() == 52)
			Fletching.makeArrows(player, usedWith.getId(), itemUsedWith.getId());
		if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755)
			Gems.selectionInterface(player, usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
		if (usedWith.getId() == 1733 || itemUsedWith.getId() == 1733)
			LeatherMaking.craftLeatherDialogue(player, usedWith.getId(), itemUsedWith.getId());
		Herblore.handleSpecialPotion(player, itemUsedWith.getId(), usedWith.getId());
		ItemForging.forgeItem(player, itemUsedWith.getId(), usedWith.getId());
		if (player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendMessage(
					"ItemOnItem - [usedItem, usedWith] : [" + usedWith.getId() + ", " + itemUsedWith + "]");
	}

	@SuppressWarnings("unused")
	private static void itemOnObject(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int interfaceType = packet.readShort();
		final int objectId = packet.readShort();
		final int objectY = packet.readLEShortA();
		final int itemSlot = packet.readLEShort();
		final int objectX = packet.readLEShortA();
		final int itemId = packet.readShort();

		if (itemSlot < 0 || itemSlot > player.getInventory().capacity())
			return;
		final Item item = player.getInventory().getItems()[itemSlot];
		if (item == null)
			return;
		final GameObject gameObject = new GameObject(objectId,
				new Position(objectX, objectY, player.getPosition().getZ()));
		if (objectId > 0 && objectId != 6 && !RegionClipping.objectExists(gameObject)) {
			// player.getPacketSender().sendMessage("An error occured. Error
			// code: "+id).sendMessage("Please report the error to a staff
			// member.");
			return;
		}
		player.setInteractingObject(gameObject);
		player.setWalkToTask(new WalkToTask(player, gameObject.getPosition().copy(), gameObject.getSize(),
				new FinalizedMovementTask() {
					@Override
					public void execute() {
						if (CookingData.forFish(item.getId()) != null && CookingData.isRange(objectId)) {
							player.setPositionToFace(gameObject.getPosition());
							Cooking.selectionInterface(player, CookingData.forFish(item.getId()));
							return;
						}
						if (Prayer.isBone(itemId) && objectId == 409) {
							BonesOnAltar.openInterface(player, itemId);
							return;
						}
						
						
						
						if (itemId == 13653 && objectId == 11546) {
							BonesOnAltar.offerBonesdf(player, itemId);
							return;
						}
						if (itemId == 13653 && objectId == 11548) {
							BonesOnAltar.offerBonesdf(player, itemId);
							return;
						}
						if (itemId == 13653 && objectId == 11547) {
							BonesOnAltar.offerBonesdf(player, itemId);
							return;
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						if (itemId == 13653 && objectId == 28716) {
							BonesOnAltar.offerBonesd(player, itemId);
							return;
						}
						if (itemId == 13653 && objectId == 28718) {
							BonesOnAltar.offerBonesd(player, itemId);
							return;
						}
						if (itemId == 13653 && objectId == 28717) {
							BonesOnAltar.offerBonesd(player, itemId);
							return;
						}
						
						
						
						if (itemId == 13653 && objectId == 3520) {
							EnergyOnAltar.offerBones(player, itemId);
							return;
						}
						
						
						
						
						
						
						
						
						
						
						if (itemId == 13653  && objectId == 218718) {
							if(player.getInventory().getFreeSlots() < 1) {
								player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
								return;
							}
							player.getPacketSender().sendMessage(
									"You sacrafice the fragment to the obelisk...");
						
							player.getSkillManager().addExperience(Skill.WARDING, 5000);
							//player.getInventory().delete(13653, 1);
							player.performAnimation(new Animation(896));
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
							
							
							
							
							
							
							if (Misc.getRandom(5000) == 222) {
								player.getInventory().add(13216, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage hat");
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
							
						
							return;
						}
						if (itemId == 13653  && objectId == 281716) {
							if(player.getInventory().getFreeSlots() < 1) {
								player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
								return;
							}
							player.getPacketSender().sendMessage(
									"You sacrafice the fragment to the obelisk...");
						
							player.getSkillManager().addExperience(Skill.WARDING, 5000);
							//player.getInventory().delete(13653, 1);
							player.performAnimation(new Animation(896));
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
							
							
							
							
							
							
							if (Misc.getRandom(5000) == 222) {
								player.getInventory().add(13216, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage hat");
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
								
								player.getPacketSender().sendMessage("@cya@You were awared a Lilly of the valley");
								player.getPacketSender().sendMessage("@mag@This Item is Super Rare! Sell to Thessilia at home for reward!");
							}
							
							if (Misc.getRandom(2) == 1) {
								player.getInventory().add(526, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@red@You were awared a bones");
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
							return;
						}
						if (itemId == 13653  && objectId == 287117) {
							if(player.getInventory().getFreeSlots() < 1) {
								player.getPacketSender().sendMessage("You need at least 1 free inventory space to loot this.");
								return;
							}
							player.getPacketSender().sendMessage(
									"You sacrafice the fragment to the obelisk...");
						
							player.getSkillManager().addExperience(Skill.WARDING, 5000);
							//player.getInventory().delete(13653, 1);
							player.performAnimation(new Animation(896));
							if (Misc.getRandom(1000) == 37) {
								player.getInventory().add(6914, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@cya@You were awarded a Master wand");
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
							
							
							
							
							
							
							if (Misc.getRandom(5000) == 222) {
								player.getInventory().add(13216, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@cya@You were awarded a Kryptic mage hat");
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
		
							
							if (Misc.getRandom(2) == 1) {
								player.getInventory().add(526, 1).refreshItems();
								//player.getSkillManager().addExperience(Skill.ARCHAOELOGY, 250 + Misc.getRandom(30));
								
								player.getPacketSender().sendMessage("@red@You were awared a bones");
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
							
						
							return;
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						if (player.getFarming().plant(itemId, objectId, objectX, objectY))
							return;
						if (player.getFarming().useItemOnPlant(itemId, objectX, objectY))
							return;
						if (objectId == 15621) { // Warriors guild
							// animator
							if (!BarrowsSpawning.itemOnAnimator(player, item, gameObject))
								player.getPacketSender().sendMessage("Nothing interesting happens..");
				return;
					}
						if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
							if (GameObjectDefinition.forId(objectId) != null) {
								GameObjectDefinition def = GameObjectDefinition.forId(objectId);
								if (def.name != null && def.name.toLowerCase().contains("bank") && def.actions != null
										&& def.actions[0] != null && def.actions[0].toLowerCase().contains("use")) {
									ItemDefinition def1 = ItemDefinition.forId(itemId);
									ItemDefinition def2;
									int newId = def1.isNoted() ? itemId - 1 : itemId + 1;
									def2 = ItemDefinition.forId(newId);
									if (def2 != null && def1.getName().equals(def2.getName())) {
										int amt = player.getInventory().getAmount(itemId);
										if (!def2.isNoted()) {
											if (amt > player.getInventory().getFreeSlots())
												amt = player.getInventory().getFreeSlots();
										}
										if (amt == 0) {
											player.getPacketSender().sendMessage(
													"You do not have enough space in your inventory to do that.");
											return;
										}
										player.getInventory().delete(itemId, amt).add(newId, amt);

									} else {
										player.getPacketSender().sendMessage("You cannot do this with that item.");
									}
									return;
								}
							}
						}
						switch (objectId) {
						case 6189:
							if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 80) {
								player.getPacketSender()
										.sendMessage("You need a Crafting level of at least 80 to make that item.");
								return;
							}
							if (player.getInventory().contains(6573)) {
								if (player.getInventory().contains(1597)) {
									if (player.getInventory().contains(1759)) {
										player.performAnimation(new Animation(896));
										player.getInventory().delete(new Item(1759)).delete(new Item(6573))
												.add(new Item(6585));
										player.getPacketSender().sendMessage(
												"You put the items into the furnace to forge an Amulet of Fury.");
									} else {
										player.getPacketSender().sendMessage("You need some Ball of Wool to do this.");
									}
								} else {
									player.getPacketSender().sendMessage("You need a Necklace mould to do this.");
								}
							}
							break;
						case 7836:
						case 7808:
							if (itemId == 6055) {
								int amt = player.getInventory().getAmount(6055);
								if (amt > 0) {
									player.getInventory().delete(6055, amt);
									player.getPacketSender().sendMessage("You put the weed in the compost bin.");
									//player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
								}
							}
							break;
						case 4306:
							EquipmentMaking.handleAnvil(player);
							// player.getPacketSender().sendMessage("Temporarily
							// Disabled");

							break;
						}
					}
				}));
	}

	private static void itemOnNpc(final Player player, Packet packet) {
		int itemId = packet.readUnsignedShortA();
		int index = packet.readUnsignedShortA(); //OK I WILL BE FURIOUS IF THIS DOESNT WORK, R U SURE? no this is what i had first time HM !
		final int slot = packet.readLEShort();
		int lastItemSelectedInterface = packet.readUnsignedShortA();
		
		Item usedItem = player.getInventory().forSlot(slot);
		switch (index) {
		case 367:
			GamblingAction.handleGambleItem(player, usedItem);
			break;
		}
	}

	@SuppressWarnings("unused")
	private static void itemOnPlayer(Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShortA();
		int targetIndex = packet.readUnsignedShort();
		int itemId = packet.readUnsignedShort();
		int slot = packet.readLEShort();
		if (slot < 0 || slot > player.getInventory().capacity() || targetIndex > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(targetIndex);
		if (target == null)
			return;
		switch (itemId) {
		case 962:
			if (!player.getInventory().contains(962) || player.getRights() == PlayerRights.ADMINISTRATOR)
				return;
			player.setPositionToFace(target.getPosition());
			player.performGraphic(new Graphic(1006));
			player.performAnimation(new Animation(451));
			player.getPacketSender().sendMessage("You pull the Christmas cracker...");
			target.getPacketSender().sendMessage("" + player.getUsername() + " pulls a Christmas cracker on you..");
			player.getInventory().delete(962, 1);
			player.getPacketSender().sendMessage("The cracker explodes and you receive a Party hat!");
			player.getInventory().add(1038 + RandomUtility.getRandom(10), 1);
			target.getPacketSender().sendMessage("" + player.getUsername() + " has received a Party hat!");
			/*
			 * if(RandomUtility.getRandom(1) == 1) {
			 * target.getPacketSender().sendMessage(
			 * "The cracker explodes and you receive a Party hat!");
			 * target.getInventory().add((1038 + RandomUtility.getRandom(5)*2), 1);
			 * player.getPacketSender().sendMessage(""+target.getUsername()+
			 * " has received a Party hat!"); } else {
			 * player.getPacketSender().sendMessage(
			 * "The cracker explodes and you receive a Party hat!");
			 * player.getInventory().add((1038 + RandomUtility.getRandom(5)*2), 1);
			 * target.getPacketSender().sendMessage(""+player.getUsername()+
			 * " has received a Party hat!"); }
			 */
			break;
		case 4155:
			if (player.getSlayer().getDuoPartner() != null) {
				player.getPacketSender().sendMessage("You already have a duo partner.");
				return;
			}
			if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
				player.getPacketSender().sendMessage("You already have a Slayer task. You must reset it first.");
				return;
			}
			Player duoPartner = World.getPlayers().get(targetIndex);
			if (duoPartner != null) {
				if (duoPartner.getSlayer().getDuoPartner() != null) {
					player.getPacketSender().sendMessage("This player already has a duo partner.");
					return;
				}
				if (duoPartner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
					player.getPacketSender().sendMessage("This player already has a Slayer task.");
					return;
				}
				if (duoPartner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
					player.getPacketSender().sendMessage("You do not have the same Slayer master as that player.");
					return;
				}
				if (duoPartner.busy() || duoPartner.getLocation() == Location.WILDERNESS) {
					player.getPacketSender().sendMessage("This player is currently busy.");
					return;
				}
				DialogueManager.start(duoPartner, SlayerDialogues.inviteDuo(duoPartner, player));
				player.getPacketSender()
						.sendMessage("You have invited " + duoPartner.getUsername() + " to join your Slayer duo team.");
			}
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case ITEM_ON_ITEM:
			itemOnItem(player, packet);
			break;
		case USE_ITEM:
			useItem(player, packet);
			break;
		case ITEM_ON_OBJECT:
			itemOnObject(player, packet);
			break;
		case ITEM_ON_GROUND_ITEM:
			// TODO
			break;
		case ITEM_ON_NPC:
			itemOnNpc(player, packet);
			break;
		case ITEM_ON_PLAYER:
			itemOnPlayer(player, packet);
			break;
		}
	}

	public final static int USE_ITEM = 122;

	public final static int ITEM_ON_NPC = 57;

	public final static int ITEM_ON_ITEM = 53;

	public final static int ITEM_ON_OBJECT = 192;

	public final static int ITEM_ON_GROUND_ITEM = 25;

	public static final int ITEM_ON_PLAYER = 14;
}
