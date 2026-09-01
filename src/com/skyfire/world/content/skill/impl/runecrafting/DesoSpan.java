package com.skyfire.world.content.skill.impl.runecrafting;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.CombatIcon;
import com.skyfire.model.Graphic;
import com.skyfire.model.GraphicHeight;
import com.skyfire.model.Hit;
import com.skyfire.model.Hitmask;
import com.skyfire.model.Position;
import com.skyfire.model.Projectile;
import com.skyfire.model.Skill;
import com.skyfire.model.movement.MovementQueue;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;

public class DesoSpan {

	private static final Animation SIPHONING_ANIMATION = new Animation(9368);
	private static final int ENERGY_FRAGMENT = 13653;

	enum Energy {
		GREEN_ENERGY(8028, 1, 60, 912, 551, 999),
		YELLOW_ENERGY(8022, 81, 150, 913, 554, 1006),
		FAIRY_ENERYGY(57, 92, 300, 913, 554, 1006);

		Energy(int npcId, int levelReq, int experience, int playerGraphic, int projectileGraphic, int npcGraphic) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.experience = experience;
			this.playerGraphic = playerGraphic;
			this.projectileGraphic = projectileGraphic;
			this.npcGraphic = npcGraphic;
		}

		public int npcId, levelReq, experience;
		public int playerGraphic, projectileGraphic, npcGraphic;

		static Energy forId(int npc) {
			for(Energy e : Energy.values()) {
				if(e.npcId == npc) 
					return e;
			}
			return null;
		}
	}

	public static void spawn() {
		int lastX = 0;
		for(int i = 0; i < 6; i++) {
			int randomX = 2595 + Misc.getRandom(12);
			if(randomX == lastX || randomX == lastX + 1 || randomX == lastX - 1)
				randomX++;
			int randomY = 4772 + Misc.getRandom(8);
			lastX = randomX;
			World.register(new NPC(i <= 3 ? 8028 : 8022, new Position(randomX, randomY)));
		}
	}

	public static void siphon(final Player player, final NPC n) {
		final Energy energyType = Energy.forId(n.getId());
		if(energyType != null) {
			player.getSkillManager().stopSkilling();
			if(player.getPosition().equals(n.getPosition()))
				MovementQueue.stepAway(player);
			player.setEntityInteraction(n);
			if(player.getSkillManager().getCurrentLevel(Skill.DIVINATION) < energyType.levelReq) {
				player.getPacketSender().sendMessage("You need a Divination level of at least "+energyType.levelReq+" to siphon this energy source.");
				return;
			}
			if(!player.getInventory().contains(ENERGY_FRAGMENT) && player.getInventory().getFreeSlots() == 0) {
				player.getPacketSender().sendMessage("You need some free inventory space to do this.");
				return;
			}
			player.performAnimation(SIPHONING_ANIMATION);
			new Projectile(player, n, energyType.projectileGraphic, 15, 44, 43, 31, 0).sendProjectile();			
			int cycle = 2 + Misc.getRandom(2);
			player.setCurrentTask(new Task(cycle, player, false) {
				@Override
				public void execute() {
					if(n.getConstitution() <= 0) {
						player.getPacketSender().sendMessage("This energy source has died out.");
						stop();
						return;
					}
					player.getSkillManager().addExperience(Skill.DIVINATION, energyType.experience + Misc.getRandom(30));
					player.performGraphic(new Graphic(energyType.playerGraphic, GraphicHeight.HIGH));
					n.performGraphic(new Graphic(energyType.npcGraphic, GraphicHeight.HIGH));
					//n.dealDamage(new Hit(Misc.getRandom(12), Hitmask.RED, CombatIcon.MAGIC));
					if(Misc.getRandom(30) == 10) {
						//player.dealDamage(new Hit(1 + Misc.getRandom(48), Hitmask.RED, CombatIcon.DEFLECT));
						//player.getPacketSender().sendMessage("You accidently attempt to siphon too much energy, and get hurt.");
					} else {
						player.getPacketSender().sendMessage("You siphon some energy ..");
						player.getInventory().add(ENERGY_FRAGMENT, 1);
						player.getInventory().add(995, 2000+Misc.random(30));
						
						
						
						
					}	
						
if(player.getSkillManager().getCurrentLevel(Skill.DIVINATION) > 81) {
						
						player.getInventory().add(ENERGY_FRAGMENT, 1);
						player.getInventory().add(995, 1500+Misc.random(30));
					}
					
					if(player.getSkillManager().getCurrentLevel(Skill.DIVINATION) > 93) {
						
						player.getInventory().add(ENERGY_FRAGMENT, 1);
						player.getInventory().add(995, 3000+Misc.random(30));
					}
					
					
					
					
					
					
				if (Misc.getRandom(400) == 68) {
							player.getPacketSender().sendMessage("@red@You find a casket while siphoning!");
							player.getInventory().add(2714, 1+Misc.random(1));
				
				}
						
				
				if (Misc.getRandom(500) == 1) {
							player.getPacketSender().sendMessage("@mag@You find a currupt crystal while training Divination!");
							player.getInventory().add(13209, 1);
				
				}		
						
						
					
			
					if(n.getConstitution() > 0 && player.getConstitution() > 0)
						siphon(player, n);
					stop();
				}
			});
			TaskManager.submit(player.getCurrentTask());
		}
	}

}
