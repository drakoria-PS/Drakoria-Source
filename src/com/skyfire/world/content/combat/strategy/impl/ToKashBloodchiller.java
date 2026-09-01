package com.skyfire.world.content.combat.strategy.impl;

import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Graphic;
import com.skyfire.model.Locations;
import com.skyfire.model.Projectile;
import com.skyfire.model.Skill;
import com.skyfire.model.Locations.Location;
import com.skyfire.util.Misc;
import com.skyfire.world.content.combat.CombatContainer;
import com.skyfire.world.content.combat.CombatHitTask;
import com.skyfire.world.content.combat.CombatType;
import com.skyfire.world.content.combat.strategy.CombatStrategy;
import com.skyfire.world.entity.Entity;
import com.skyfire.world.entity.impl.Character;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;
/**
 * @author Lewis
 */

public class ToKashBloodchiller implements CombatStrategy {

	/**
	 * Animations
	 */

	public Animation handClap = new Animation(14383);
	
	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC monster = (NPC) entity;
		if (monster.isChargingAttack() || monster.getConstitution() <= 0) {
			return true;
		}
		Player target = (Player)victim;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			
		}
		switch(Misc.inclusiveRandom(1, 4)) {
			/**
			 * Regular attack
			 */
			case 1:
				monster.performAnimation(handClap);
				monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 1, 0, CombatType.MAGIC, true));
				break;
		/**
		 * hand clap attack 
		 */
			case 3:
				monster.performAnimation(handClap);
				for (Player t : Misc.getCombinedPlayerList(target)) {
					if(t == null || t.getLocation() != Location.THREEBOSSES)
						continue;
				}
				TaskManager.submit(new Task(1, victim, false) {
					@Override
					public void execute() {
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if(t == null || t.getLocation() != Location.THREEBOSSES)
								continue;
							monster.getCombatBuilder().setVictim(t);
							new Projectile(monster, victim, 364, 14, 3, 43, 43, 0).sendProjectile();
							new CombatHitTask(monster.getCombatBuilder(), new CombatContainer(monster, t, 1, CombatType.MAGIC, true)).handleAttack();
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
		return 20;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}