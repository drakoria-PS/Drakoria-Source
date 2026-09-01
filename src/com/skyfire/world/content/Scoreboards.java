package com.skyfire.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.skyfire.model.Item;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.entity.impl.player.Player;

public class Scoreboards {

	public static Scoreboard HIGHEST_SLAYER_STREAK;
	public static Scoreboard TOP_TRIVIA_ANSWERS;
	public static Scoreboard MOST_TIME_PLAYED;
	public static Scoreboard TOP_DONATOR;
	
	
	
	
	public static Scoreboard TOP_ACHIEVER;
	public static Scoreboard MOST_MONEY_IN_POUCH;
	public static Scoreboard TOP_TOTAL_XP;
	public static Scoreboard NPC_KILLS;
	
	public static Scoreboard MOST_CLUES_COMPLETED;
	public static Scoreboard MOST_VOTES_DONE;
	public static Scoreboard TOP_PRESTIGE_POINTS;

	public static Scoreboard PVM_POINTS;
	public static Scoreboard CHOPS;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static abstract class Scoreboard {

		public Scoreboard(String title, String file) {
			this.title = title;
			//this.file = file;
		}

		public abstract void resort(ArrayList<ScoreboardEntry> entries);

		private String title;
		//private String file;
	}

	public static class ScoreboardEntry {

		public ScoreboardEntry(String player, String[] value) {
			this.player = player;
			this.value = value;
		}

		private String player;
		private String[] value;
	}

	public static void init() {
		
		
		
		
		HIGHEST_SLAYER_STREAK = new Scoreboard("Skyfire's Highest Slayer Streak", "slayerstreak.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		

		TOP_TRIVIA_ANSWERS = new Scoreboard("Skyfire's Top Trivia Answers", "toptrivia.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		
		
		

		MOST_TIME_PLAYED = new Scoreboard("Skyfire's Most Play Time", "topplaytime.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		
		
		
		
		TOP_DONATOR = new Scoreboard("Skyfire's Top Donators", "topdonators.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		
		
		
		
		TOP_ACHIEVER = new Scoreboard("Skyfire's Top Achiever", "top-achievers.txt")  {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "integer");
			}
		};
		
		

		MOST_MONEY_IN_POUCH = new Scoreboard("Skyfire's Top Money in Pouch", "money.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		TOP_TOTAL_XP = new Scoreboard("Skyfire's Top Total XP", "totalxp.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		NPC_KILLS = new Scoreboard("Skyfire's Top NPC Kills", "topnpc.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		MOST_CLUES_COMPLETED = new Scoreboard("Skyfire's Top Total Level", "clues.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		MOST_VOTES_DONE = new Scoreboard("Skyfire's Top Voter", "voter.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
		
		
		TOP_PRESTIGE_POINTS = new Scoreboard("Skyfire's Top Prestige", "prestige.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		PVM_POINTS = new Scoreboard("Skyfire's Top PVM Points", "pvm.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		CHOPS = new Scoreboard("Skyfire's Top AFK Chops", "chops.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
		
	}

	public static ArrayList<ScoreboardEntry> getUpdatedList(Scoreboard scoreboard) {
		ArrayList<ScoreboardEntry> entries = new ArrayList<ScoreboardEntry>();
		for(Player p : World.getPlayers()) {
			
			if(p == null)
				
				continue;
			if(scoreboard == Scoreboards.MOST_MONEY_IN_POUCH) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {}
				for (Item item : p.getEquipment().getItems()) {}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {}
				}
				gold += p.getMoneyInPouch();
				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(gold)}));
				//int kc = p.getPlayerKillingAttributes().getPlayerKills();
				//int dc = p.getPlayerKillingAttributes().getPlayerDeaths();
				//double kdr = (double) ((kc < 2 || dc < 2) ? (0) : (kc/dc));
				//entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(kdr), String.valueOf(kc), String.valueOf(dc)}));
			} else if(scoreboard == Scoreboards.NPC_KILLS) {
				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getNpcKills())}));
			} else if(scoreboard == Scoreboards.TOP_TOTAL_XP) {
				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getSkillManager().getTotalExp())}));
			} else if(scoreboard == Scoreboards.TOP_ACHIEVER) {
				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getAchievementPoints())}));
			}
			
			
			
		
			
			
			
			 else if(scoreboard == Scoreboards.TOP_PRESTIGE_POINTS) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getPrestigePoints())}));
				}
			 else if(scoreboard == Scoreboards.MOST_VOTES_DONE) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getVotingPoints())}));
				}
			 else if(scoreboard == Scoreboards.MOST_CLUES_COMPLETED) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getSkillManager().getTotalLevel())}));
				}
			 else if(scoreboard == Scoreboards.TOP_DONATOR) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getAmountDonated())}));
				}
			 else if(scoreboard == Scoreboards.MOST_TIME_PLAYED) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getLoyaltyPoints())}));
				}
			 else if(scoreboard == Scoreboards.TOP_TRIVIA_ANSWERS) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getTriviaPoints())}));
				}
			 else if(scoreboard == Scoreboards.HIGHEST_SLAYER_STREAK) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getSlayer().getTaskStreak())}));
				}
			 else if(scoreboard == Scoreboards.PVM_POINTS) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.pvmPoints)}));
				}
			
			 else if(scoreboard == Scoreboards.CHOPS) {
					entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.chops)}));
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
		scoreboard.resort(entries);
		return entries;
	}



	public static void open(Player player, Scoreboard scoreboard) {
		ArrayList<ScoreboardEntry> entries = getUpdatedList(scoreboard);
		int stringId = 6402;
		for (int i = 0; i <= 50; stringId++, i++) {
			if(i == 10) {
				stringId = 8578;
			}
			ScoreboardEntry entry = i < entries.size() ? entries.get(i) : null;
			String line = "";
			if(entry != null) {
				if(scoreboard == Scoreboards.HIGHEST_SLAYER_STREAK) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Slayer Streak: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.TOP_TRIVIA_ANSWERS) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+" @whi@- @yel@Trivia Answers: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.MOST_TIME_PLAYED) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Loyalty Points: @or1@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.TOP_DONATOR) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Top Donator: @or2@$"+Misc.insertCommasToNumber(entry.value[0]);
				}
				
				
				else if(scoreboard == Scoreboards.TOP_ACHIEVER) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+" @whi@- @yel@Achievement Points: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.MOST_MONEY_IN_POUCH) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Money In Pouch: @or1@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.TOP_TOTAL_XP) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Total XP: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				}
				else if(scoreboard == Scoreboards.NPC_KILLS) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+" @whi@- @yel@Npc Kills: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.MOST_CLUES_COMPLETED) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Total Level: @or1@"+Misc.insertCommasToNumber(entry.value[0]);
				} else if(scoreboard == Scoreboards.MOST_VOTES_DONE) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Voting Points: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				}else if(scoreboard == Scoreboards.TOP_PRESTIGE_POINTS) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@Prestige Points: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				}
				
				else if(scoreboard == Scoreboards.PVM_POINTS) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@PvM Points: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				}
			
				else if(scoreboard == Scoreboards.CHOPS) {
					line = "@whi@Rank @cya@"+(i+1)+"@whi@ - @gr2@"+entry.player.replaceAll("_", " ")+"@whi@ - @yel@AFK Chops: @or2@"+Misc.insertCommasToNumber(entry.value[0]);
				}
				
				
				
				
				
			}
			player.getPacketSender().sendString(stringId, line);
		}
		entries.clear();
		player.getPacketSender().sendInterface(6308).sendString(6400, "Scoreboard - "+scoreboard.title+"").sendString(6399, "").sendString(6401, "Close");
	}

	private static void resort(ArrayList<ScoreboardEntry> entries, String type) {
		Collections.sort(entries, new Comparator<ScoreboardEntry>() {
			@Override
			public int compare(ScoreboardEntry player1, ScoreboardEntry player2) {
				if(type.equals("integer")) {
					int v1 = Integer.parseInt(player1.value[0]);
					int v2 = Integer.parseInt(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else if(type.equals("long")) {
					long v1 = Long.parseLong(player1.value[0]);
					long v2 = Long.parseLong(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else if(type.equals("double")) {
					double v1 = Double.parseDouble(player1.value[0]);
					double v2 = Double.parseDouble(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else {
					return 1;
				}
			}
		});
	}
}
