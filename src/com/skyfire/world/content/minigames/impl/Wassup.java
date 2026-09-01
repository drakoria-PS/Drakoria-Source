package com.skyfire.world.content.minigames.impl;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Item;
import com.skyfire.model.Position;
import com.skyfire.model.RegionInstance;
import com.skyfire.model.Locations.Location;
import com.skyfire.model.RegionInstance.RegionInstanceType;
import com.skyfire.model.container.impl.Shop;
import com.skyfire.model.input.impl.EnterAmountToBuyFromShop;
import com.skyfire.model.input.impl.EnterAmountToSellToShop;
import com.skyfire.world.World;
import com.skyfire.world.content.PlayerPanel;
import com.skyfire.world.content.combat.prayer.CurseHandler;
import com.skyfire.world.content.combat.prayer.PrayerHandler;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;

/**
 * @author Gabriel Hannason
 * Wrote this quickly!!
 * Handles the RFD quest
 */
public class Wassup {

	public static void enter(Player player) {}
	
	public static void leave(Player player) {
		Location.RECIPE_FOR_DISASTER.leave(player);
	}

	public static void spawnWave(final Player p, final int wave) {}

	public static void handleNPCDeath(final Player player, NPC n) {}

	public static String getQuestTabPrefix(Player player) {
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
			return "@yel@";
		}
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
			return "@gre@";
		}
		return "@red@";
	}
	
	public static void openQuestLog(Player p) {
		for(int i = 8145; i < 8196; i++)
			p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, ""+questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for(int i = 8147; i < 8147+questIntro.length; i++) {
			p.getPacketSender().sendString(i, "@dre@"+questIntro[questIntroIndex]);
			questIntroIndex++;
		}
		int questGuideIndex = 0;
		for(int i = 8147+questIntro.length; i < 8147+questIntro.length+questGuide.length; i++) {
			if(!p.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(questGuideIndex))
				p.getPacketSender().sendString(i, ""+questGuide[questGuideIndex]);
			else
				p.getPacketSender().sendString(i, "@str@"+questGuide[questGuideIndex]+"");
			if(questGuideIndex == 2) {
				if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() > 0 && !p.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(questGuideIndex))
					p.getPacketSender().sendString(i, "@yel@"+questGuide[questGuideIndex]);
				if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
					p.getPacketSender().sendString(i, "@str@"+questGuide[questGuideIndex]+"");
			}
			questGuideIndex++;
		}
		if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			p.getPacketSender().sendString(8147+questIntro.length+questGuide.length, "@dre@Quest complete!");
	}

	public static void openRFDShop(final Player player) {}


	private static final Position spawnPos = new Position(1900, 5354);
	private static final String questTitle = "@mag@Monster Drop table";
	private static final String[] questIntro ={
		"~~~~~~", 
		"~~~~",
		"~~~",
	};
	private static final String[] questGuide ={
		"View drop table is easy, type @dre@::drop monster name",
		"for example",
		"@dre@::drop undead king",
		"or",
		"@dre@::drop zulrah",
		"Or you can Examine any attackable NPC for drop table",
		"@dre@~~~~",
		"@dre@~~~~~~"
	};
}
