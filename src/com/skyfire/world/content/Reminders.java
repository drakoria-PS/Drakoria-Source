package com.skyfire.world.content;

import com.skyfire.util.Misc;
import com.skyfire.util.Stopwatch;
import com.skyfire.world.World;

/*
 * @author Bas
 * www.Arlania.com
 */

public class Reminders {
	
	
    private static final int TIME = 1200000; //7 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"@blu@[Tip #1]@dre@ Join 'Help' CC For Help/Tips!"},
			{"@blu@[Tip #2]@dre@ Do ::benefits To Check out donator Rank Benefits!"},
			{"@blu@[Tip #3]@dre@ Donate to help the grow! ::store"},
			{"@blu@[Tip #4]@dre@ Use the command :: drop (npcname) for drop tables"},
			{"@blu@[Tip #5]@dre@ Use the ::help command to ask staff for help"},
			{"@blu@[Tip #6]@dre@ Remember to vote daily for rewards!"},
			{"@blu@[Tip #7]@dre@ Remember to spread the word and invite your friends to play!"},
			{"@blu@[Tip #8]@dre@ Use ::commands to find a list of commands"},
			{"@blu@[Tip #9]@dre@ Toggle your client settings to your preference in the wrench tab!"},
			{"@blu@[Tip #10]@dre@ Register and post on the forums to keep them active! ::Forum"},
			{"@blu@[Tip #11]@dre@ Donating rsgp? speak to Thon for more info"},
			{"@blu@[Tip #12]@dre@ Want to see something ingame? Suggest it on ::forums"},
			
		
			
			{"@blu@[Tip #13]@dre@ You can view drop tables by typing ::drop monster name"},
			{"@blu@[Tip #14]@dre@ Remember to gamble responsibly!"},
			{"@blu@[Tip #15]@dre@ You can Afk and make gold for days, at home christmas tree"},
			{"@blu@[Tip #16]@dre@ Make sure you check the PoS daily"},
			{"@blu@[Tip #17]@dre@ Did you know we have 2 mini-quests?"},
			{"@blu@[Tip #18]@dre@ Make sure you join our ::discord channel!"},
			{"@blu@[Tip #19]@dre@ You can compete on in-game and website hiscores!"},
			{"@blu@[Tip #20]@dre@ Donating has many useful benefits"},
			{"@blu@[Tip #21]@dre@ Remember to gamble responsibly!"},
			{"@blu@[Tip #22]@dre@ It's a good idea to have a spade when you dig!"},
			{"@blu@[Tip #23]@dre@ We have 3 customs skills on Skyfire!"},
			{"@blu@[Tip #24]@dre@ The longer you play, the higher the rewards"},
			{"@blu@[Tip #25]@dre@ Shooting stars, and Evil trees are good xp!"},
			{"@blu@[Tip #26]@dre@ Voting is a good money maker, and helps us grow!"},
			{"@blu@[Tip #27]@dre@ Your suggestions grow the server! Always share your ideas"},
			{"@blu@[Tip #28]@dre@ A Ring of vigour cuts the cost of special attacks by 10%."},
			{"@blu@[Tip #29]@dre@ Enemies that are your slayer task will sometimes drop Frozen key fragments."},
			{"@blu@[Tip #30]@dre@ You can sometimes make Lunar armour when crafting astral runes"},
			{"@blu@[Tip #31]@dre@ Wilderness resource area gives a bonus when training"},
			{"@blu@[Tip #32]@dre@ Many items can be upgraded with boss fragments"},
			{"@blu@[Tip #33]@dre@ Lots of bosses drop boss fragments"},
			{"@blu@[Tip #34]@dre@ You can toggle many client settings in options"},
			{"@blu@[Tip #35]@dre@ You can change your skin color to red,blue, and many more colors"},
			{"@blu@[Tip #36]@dre@ Dont worry, your character will never log out! Free loyality points"},
			{"@blu@[Tip #37]@dre@ Lots of bosses drop boss fragments"},
			{"@blu@[Tip #38]@dre@ 1m, 100m, and 1b tokens can be bought from the general store"},
			{"@blu@[Tip #39]@dre@ Click the World Map Globe to teleport around Skyfire"},
			{"@blu@[Tip #40]@dre@ Donating with RS3 or OSRS gp is possible, messege Thon"},
			{"@blu@[Tip #41]@dre@ You can train each stat to 2 Billion Xp"},
			{"@blu@[Tip #42]@dre@ Archaoelogy is a good money maker!"},
			{"@blu@[Tip #43]@dre@ The money pouch has no limit to how much it can store!"},
			{"@blu@[Tip #44]@dre@ You can aquire donator status by voting daily!"},
			{"@blu@[Tip #45]@dre@ You can buy donator status with in-game gold via players!"},
			{"@blu@[Tip #46]@dre@ Every donator rank has its own unique zone"},
			{"@blu@[Tip #47]@dre@ Summoning does not increase your combat level!"},
			{"@blu@[Tip #48]@dre@ Doing slayer has many benefits! Start early"},
			{"@blu@[Tip #49]@dre@ Being a friendly to new players will get you rewarded"},
			{"@blu@[Tip #50]@dre@ Dungeoneering has many useful rewards!"},
			{"@blu@[Tip #51]@dre@ Always report players that break Skyfire rules!"},
			
			{"@blu@[Tip #52]@dre@ You can buy 1m, 10m, and 100m tokens from Explorer Jack"},
			{"@blu@[Tip #53]@dre@ You can buy stuff directly from your money pouch!"},
			{"@blu@[Tip #54]@dre@ Remember to double check your trade! Always!"},
			{"@blu@[Tip #55]@dre@ Its a good idea to use a dwarf cannon for slayer"},
			{"@blu@[Tip #56]@dre@ Luring fish from the fountain at home is great money"},
			{"@blu@[Tip #57]@dre@ You can Examine Npc's to see their drop table"},
			{"@blu@[Tip #58]@dre@ Click toolbar on top of client for quick links!"},
			{"@blu@[Tip #59]@dre@ Have no money? Just afk at any of the 2 locations!"},
			{"@blu@[Tip #60]@dre@ You can dice, and stake! Dont get carried away"},
			{"@blu@[Tip #61]@dre@ Youtube rank is earned by making lots of videos!"},
			{"@blu@[Tip #62]@dre@ Dont forget to utilize the Well of Goodwill"},
			{"@blu@[Tip #63]@dre@ Killing bosses in wilderness has increased chance of drops!"},
			{"@blu@[Tip #64]@dre@ Donations deals are always going! Ask Thon for details"},
			{"@blu@[Tip #65]@dre@ Always vote! Its good money, and helps out Skyfire."},
			{"@blu@[Tip #66]@dre@ Explore the client settings to find the right match for you"},
			{"@blu@[Tip #67]@dre@ Right click examine monsters to view all the possible drops"},
			

			{"@blu@[Tip #68]@dre@ Did you know that here untradable items you can sell and trade?"},
			{"@blu@[Tip #69]@dre@ You can train your skills up to 2,000,000,000 XP"},
			{"@blu@[Tip #70]@dre@ You can type ::thread # to quick open forums"},
			{"@blu@[Tip #71]@dre@ Type ::news to see all the news on Skyfire"},
			{"@blu@[Tip #72]@dre@ Type ::benefits to see all the benefits for donating!"},
			{"@blu@[Tip #73]@dre@ Type ::maxhit to see your current max hit!"},
			{"@blu@[Tip #74]@dre@ Type ::answer to answer various trivia questions"},
			{"@blu@[Tip #75]@dre@ Type ::train to quickly teleport to rockcrabs"},
			{"@blu@[Tip #76]@dre@ Examine NPC's to view their drop table"},
			{"@blu@[Tip #77]@dre@ You can type ::forums, ::vote, ::donate, ::discord, and many more!"},
			{"@blu@[Tip #78]@dre@ Utilize the top bar for quick links!"},
			{"@blu@[Tip #79]@dre@ Check your progress by talking to Max at ::home!"},
			{"@blu@[Tip #80]@dre@ 1 Vote ticket can be exchanged for 1m and 1 point"},
			{"@blu@[Tip #81]@dre@ 1 Vote ticket can be exchanged for 30% Xp boost for 10 minutes!"},
			{"@blu@[Tip #82]@dre@ Type ::thread 13 to view all donator benefits"},
			{"@blu@[Tip #83]@dre@ You can click any skill icon to teleport there!"},
			
			{"@blu@[Tip #84]@dre@ Type ::thread 12 to download the latest client"},
			{"@blu@[Tip #85]@dre@ Type ::thread 14 to troubleshoot your client if needed"},
			{"@blu@[Tip #86]@dre@ Type ::thread 17 to view the staff application"},
			{"@blu@[Tip #87]@dre@ Type ::thread 5 to play the 1-Word story of forums!"},
			{"@blu@[Tip #88]@dre@ Play trivia for good rewards!"},
			{"@blu@[Tip #89]@dre@ Type ::thread 13 for donating benefits"},
			{"@blu@[Tip #90]@dre@ All skills you can grind till 2b xp!"},
			{"@blu@[Tip #91]@dre@ Gather divination fragments, and use them for Warding skill"},
			{"@blu@[Tip #92]@dre@ Please remember to join our ::discord for giveaways!"},
			{"@blu@[Tip #93]@dre@ Please remember to register on ::forums! To keep up to date"},
			
			{"@blu@[Tip #94]@dre@ Did you know that max skill level is 168?"},
			{"@blu@[Tip #95]@dre@ You can train your skill up to level 168!"},
			{"@blu@[Tip #96]@dre@ Don't you love leveling? Well you can up to 168!"},
			{"@blu@[Tip #97]@dre@ Max combat level is 255!"},
			{"@blu@[Tip #98]@dre@ You can train your combat level to level 255!"},
			{"@blu@[Tip #99]@dre@ Keep your combat high! It goes up to level 255!"},
			{"@blu@[Tip #100]@dre@ You can increase your combat level with over 10 skills!"},
			{"@blu@[Tip #101]@dre@ Luck also increases your combat level!"},
			{"@blu@[Tip #102]@dre@ Warding has multiple zones to train in!"},
			{"@blu@[Tip #103]@dre@ The max total level is 4,200!"},
			{"@blu@[Tip #104]@dre@ You can check the in-game hiscores bottom right tab!"},
			{"@blu@[Tip #105]@dre@ Elder bosses is a great way to upgrade gear!"},
			{"@blu@[Tip #106]@dre@ You can chop the AFK tree at home for extra gold"},
			{"@blu@[Tip #107]@dre@ You gain XP for PvMing for every monster killed!"},
			{"@blu@[Tip #108]@dre@ You can buy powerful auras from Valentina at ::home"},
			{"@blu@[Tip #109]@dre@ Auras increase your character power by alot!"},
			{"@blu@[Tip #110]@dre@ Divination has many zones to gather from"},
			{"@blu@[Tip #111]@dre@ Luck increases your odds in better loot from everything!"},
			{"@blu@[Tip #112]@dre@ The Big Bang is a extremely powerful weapon! It can destroy anything!"},
			{"@blu@[Tip #113]@dre@ It's suggested to buy the Money Orb for low levels from Donator Store!"},
			{"@blu@[Tip #114]@dre@ The Blasta Cannon is a very powerful ranged weapon!"},
			{"@blu@[Tip #115]@dre@ Pick up your nex set from donator store! Discounted!"},
			{"@blu@[Tip #116]@dre@ Make sure to upgrade all your weapons that you can!"},
			{"@blu@[Tip #117]@dre@ Want fast XP? You can always buy a lamp from donator store!"},
			{"@blu@[Tip #118]@dre@ Answer trivia questions for easy points to make bank!"},
			{"@blu@[Tip #119]@dre@ Have a suggestion? Please post it on forums or discord!"},
			{"@blu@[Tip #120]@dre@ Donator Ranks: Sapphire,Emerald,Ruby,Diamond,Dragonstone,Onyx,Zenyte"},
			{"@blu@[Tip #121]@dre@ Compete in hiscores for best ranks! There are always rewards!"},
			{"@blu@[Tip #122]@dre@ Refer to icons above NPC to see what they do!"},
			{"@blu@[Tip #123]@dre@ You can check all NPC's killed on server in quest tab"},
			{"@blu@[Tip #124]@dre@ It's advised to rush to get a aura if you dont have one"},
			{"@blu@[Tip #125]@dre@ Train your prayer via the first warding floor!"},
			{"@blu@[Tip #126]@dre@ Warding is a really good money making skill!"},
			{"@blu@[Tip #127]@dre@ Did you know every item is tradable?"},
			{"@blu@[Tip #128]@dre@ Voting will give you easy Luck XP!"},
			{"@blu@[Tip #129]@dre@ Vote as much as you can for free Luck XP!"},
			{"@blu@[Tip #130]@dre@ Have extra OSGP or RS3 gold? We can accept it for a donation!"},
			{"@blu@[Tip #131]@dre@ You should have a very fast weapon to attempt Elder bosses!"},
			{"@blu@[Tip #132]@dre@ The Blood chest at ::home gives very good rewards!"},
			{"@blu@[Tip #133]@dre@ Use the rare Forsaken key at ::home for legendary rewards!"},	
			{"@blu@[Tip #134]@dre@ Please remember to register on ::forums! To keep up to date"},
			
			
			
			{"@blu@[Tip #135]@dre@ When a certain donator logs in, you can see it!"},
			{"@blu@[Tip #136]@dre@ You can click the emote tab for quick chat options!"},
			{"@blu@[Tip #137]@dre@ Click Dance on the Emote tab, see what happens!"},
			{"@blu@[Tip #138]@dre@ You can Auto Chat by clicking emotes! Try it!"},
			{"@blu@[Tip #139]@dre@ Check your current play time on top of the client window!"},
			{"@blu@[Tip #140]@dre@ You can upgrade alot of weapons and accesories!"},	
			{"@blu@[Tip #141]@dre@ Elder bosses cost money to teleport to! Make sure your ready"},
			
			
			
			
			{"@blu@[Tip #142]@dre@ Skyfire has many customs maps!"},
			{"@blu@[Tip #143]@dre@ How high of a total level can you get? It's up to 4,200!"},
			{"@blu@[Tip #144]@dre@ Enter the Skyfire portal at home for a new place to explore!"},
			{"@blu@[Tip #145]@dre@ Hardcore Ironman deaths are broadcasted server wide!"},
			{"@blu@[Tip #146]@dre@ Want a real challenge? Try Hardcore ironman"},
			{"@blu@[Tip #147]@dre@ Turn Anti-Aliasing on in settings to smooth out graphics!"},	
			{"@blu@[Tip #148]@dre@ Make sure to sell items on the Players Owned Shops at ::home!"},
			
			
			
			{"@blu@[Tip #149]@dre@ Dont break the rules! Everything is logged"},
			{"@blu@[Tip #150]@dre@ The started sword is actually a pretty good weapon!"},
			{"@blu@[Tip #151]@dre@ Please help out new players! You will be rewarded!"},
			{"@blu@[Tip #152]@dre@ If you bring a friend! Both get rewards!"},
			{"@blu@[Tip #153]@dre@ Check out our youtube channel!"},
			{"@blu@[Tip #154]@dre@ Apply for Graphics Artis on forums!"},	
			{"@blu@[Tip #155]@dre@ Think you can be a Mod? or even Admin? Apply then!"},
			
			
			{"@blu@[Tip #156]@dre@ Start training! Top ranked players get rewarded!"},
			{"@blu@[Tip #157]@dre@ You can always get free donator by voting :)"},
			{"@blu@[Tip #158]@dre@ You can view the achievements tab in the quest tab!"},
			{"@blu@[Tip #159]@dre@ If you bring a friend! Both get rewards!"},
			{"@blu@[Tip #160]@dre@ Check out our youtube channel!"},
			{"@blu@[Tip #161]@dre@ Apply for Veteran status after 6 Month Playtime!"},	
			{"@blu@[Tip #162]@dre@ Think you can be a Advisor? Apply on forums!!"},
			
			
			
			
			
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
				
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			World.sendMessage(currentMessage);
			World.savePlayers();
					
				}
				
			World.savePlayers();
			}
		

          }

}