package com.skyfire.world.content.itemopening;

import com.skyfire.model.PlayerRights;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.content.PlayerPanel;
import com.skyfire.world.content.dialogue.Dialogue;
import com.skyfire.world.content.dialogue.DialogueExpression;
import com.skyfire.world.content.dialogue.DialogueManager;
import com.skyfire.world.content.dialogue.DialogueType;
import com.skyfire.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		PlayerRights rights = null;
		if(player.getAmountDonated() >= 10)
			rights = PlayerRights.SAPPHIRE;
		if(player.getAmountDonated() >= 50)
			rights = PlayerRights.EMERALD;
		if(player.getAmountDonated() >= 200)
			rights = PlayerRights.RUBY;
		if(player.getAmountDonated() >= 500)
			rights = PlayerRights.DIAMOND;
		if(player.getAmountDonated() >= 1000)
			rights = PlayerRights.DRAGONSTONE;
		if(player.getAmountDonated() >= 2000)
			rights = PlayerRights.ONYX;
		if(player.getAmountDonated() >= 5000)
			rights = PlayerRights.ZENYTE;
		if(rights != null && rights != player.getRights()) {
			World.sendMessage("@dre@[PLAYER] @mag@["+player.getUsername()+"] has just become a @bla@["+Misc.formatText(rights.toString().toLowerCase())+"] Donator!@mag@ Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}

	public static boolean handleScroll(Player player, int item) {
		switch(item) {
		case 619:
		case 15355:
		case 15359:
		case 15358:
			int funds = item == 619 ? 1 : item == 15355 ? 10 : item == 15359 ? 25 : item == 15358 ? 50 : -1;
			player.getInventory().delete(item, 1);
			player.incrementAmountDonated(funds);
			player.getPointsHandler().incrementDonationPoints(funds);
			player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
			checkForRankUpdate(player);
			PlayerPanel.refreshPanel(player);
			break;
		}
		return false;
	}
	
	public static Dialogue getTotalFunds(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public int npcId() {
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
