package com.skyfire.world.content.combat.strategy.impl.gwd2;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.CombatIcon;
import com.skyfire.model.Graphic;
import com.skyfire.model.Hit;
import com.skyfire.model.Hitmask;
import com.skyfire.model.Locations;
import com.skyfire.model.Position;
import com.skyfire.model.Projectile;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.util.Misc;
import com.skyfire.world.content.combat.CombatContainer;
import com.skyfire.world.content.combat.CombatHitTask;
import com.skyfire.world.content.combat.CombatType;
import com.skyfire.world.content.combat.strategy.CombatStrategy;
import com.skyfire.world.entity.impl.Character;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;

public class ArmadylAbyzou implements CombatStrategy {
	
	/** @author Lord Lewis **/

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC cB = (NPC)entity;
		if(cB.isChargingAttack() || cB.getConstitution() <= 0) {
			return true;
		}
		Player target = (Player)victim;
		boolean stomp = false;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			
			
		}
		if (stomp) {
			//cB.performAnimation(attack_anim);
			//cB.performGraphic(attack_graphic);
		}

		for (Player t : Misc.getCombinedPlayerList(target)) {
			
			if (Locations.goodDistance(t.getPosition(), cB.getPosition(), 1)) {
				stomp = true;
				cB.getCombatBuilder().setVictim(t);
				new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.MAGIC, true)).handleAttack();
				
				new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.MAGIC, true)).handleAttack();
			}
		}
		if (stomp) {
	
		}
		int attackStyle = Misc.getRandom(4);
		NPC etalk = (NPC)entity;
		etalk.forceChat("You cant escape me!");
		new Projectile(cB, target, 660, 44, 3, 43, 43, 0).sendProjectile();
		if (attackStyle == 0 || attackStyle == 1) { // melee
			int distanceX = target.getPosition().getX() - cB.getPosition().getX();
			int distanceY = target.getPosition().getY() - cB.getPosition().getY();
			if (distanceX > 4 || distanceX < -1 || distanceY > 4 || distanceY < -1)
				attackStyle = 4;
			else {

				cB.performAnimation(new Animation(attackStyle == 0 ? 64 : 64));
				if(target.getLocation() == Location.WILDERNESS)
					cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 1, CombatType.MELEE, true));
				return true;
			}
		} else if (attackStyle == 2) { // powerfull mage spiky ball
			NPC atalk = (NPC)entity;
			atalk.forceChat("Time to die!!!");
			//cB.performAnimation(attack_anim);
			new Projectile(cB, target, 660, 44, 3, 43, 43, 0).sendProjectile();
			new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
			cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 2, CombatType.RANGED, true));
			new Projectile(cB, target, 660, 44, 3, 43, 43, 0).sendProjectile();
			new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
		} else if (attackStyle == 3) { // translucent ball of energy
			//cB.performAnimation(attack_anim);
				cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 2, CombatType.MAGIC, true));
			new Projectile(cB, target, 2122, 44, 3, 43, 43, 0).sendProjectile();
			new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					int skill = Misc.getRandom(4);
					Skill skillT = Skill.forId(skill);
					Player player = (Player) target;
					int lvl = player.getSkillManager().getCurrentLevel(skillT);
					lvl -= 1 + Misc.getRandom(10);
					player.getSkillManager().setCurrentLevel(skillT, player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ?  1 : lvl);
					target.getPacketSender().sendMessage("Your " + skillT.getFormatName() +" has been slighly drained!");
					stop();
				}
			});
		}
		
		if(attackStyle == 4) {
			
			NPC talk = (NPC)entity;
			talk.forceChat("You dare run from me!");
			new Projectile(cB, target, 660, 44, 3, 43, 43, 0).sendProjectile();
			new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
			//cB.performAnimation(attack_anim);
			for (Player t : Misc.getCombinedPlayerList(target)) {
				new Projectile(cB, target, 660, 44, 3, 43, 43, 0).sendProjectile();
				new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
				if(t == null || t.getLocation() != Location.WILDERNESS)
					continue;
				new Projectile(cB, target, 665, 44, 3, 43, 43, 0).sendProjectile();
				new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
			}
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						if(t == null || t.getLocation() != Location.WILDERNESS)
							continue;
						cB.getCombatBuilder().setVictim(t);
						new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.RANGED, true)).handleAttack();
					}
					stop();
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 16;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}