package com.skyfire.world.content.combat.strategy;

import com.skyfire.model.Graphic;
import com.skyfire.model.GraphicHeight;
import com.skyfire.model.Locations;
import com.skyfire.model.Skill;
import com.skyfire.model.CombatIcon;
import com.skyfire.model.Hit;
import com.skyfire.model.Hitmask;
import com.skyfire.engine.task.Task;
import com.skyfire.engine.task.TaskManager;
import com.skyfire.model.Animation;
import com.skyfire.model.Projectile;
import com.skyfire.model.Locations.Location;
import com.skyfire.util.Misc;
import com.skyfire.world.content.combat.CombatContainer;
import com.skyfire.world.content.combat.CombatHitTask;
import com.skyfire.world.content.combat.CombatType;
import com.skyfire.world.entity.impl.Character;
import com.skyfire.world.entity.impl.npc.NPC;
import com.skyfire.world.entity.impl.player.Player;

public class Gang implements CombatStrategy {

	private static final Animation anim = new Animation(451);
	private static final Animation anim2 = new Animation(451);
	private static final Animation anim3 = new Animation(451);
	private static final Graphic gfx1 = new Graphic(1886, 3, GraphicHeight.MIDDLE);
	private static final Graphic gfx2 = new Graphic(1885);


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
		NPC td = (NPC)entity;


		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(td.isChargingAttack()) {
			return true;
		}
		if(Locations.goodDistance(td.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
			td.performAnimation(anim);
			td.performGraphic(gfx1);
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MELEE, true));
			victim.dealDamage(new Hit(Misc.random(400), Hitmask.RED, CombatIcon.NONE));
			

		} else if(Misc.getRandom(10) <= 7) {
			td.performAnimation(anim2);
			td.setChargingAttack(true);
			victim.dealDamage(new Hit(Misc.random(400), Hitmask.RED, CombatIcon.NONE));
			
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.RANGED, true));
			TaskManager.submit(new Task(1, td, false) {
				@Override
				protected void execute() {
					new Projectile(td, victim, 1884, 44, 3, 43, 31, 0).sendProjectile();
					victim.dealDamage(new Hit(Misc.random(500), Hitmask.RED, CombatIcon.NONE));
					
					td.setChargingAttack(false).getCombatBuilder().setAttackTimer(td.getDefinition().getAttackSpeed() - 1);
					stop();
				}
			});
		} else {
			td.performAnimation(anim3);
			victim.performGraphic(gfx2);
			if(Misc.getRandom(10) <= 2) {
				Player p = (Player)victim;
				int lvl = p.getSkillManager().getCurrentLevel(Skill.PRAYER);
				lvl *= 0.9;
				victim.dealDamage(new Hit(Misc.random(400), Hitmask.RED, CombatIcon.NONE));
				p.getSkillManager().setCurrentLevel(Skill.PRAYER, p.getSkillManager().getCurrentLevel(Skill.PRAYER) - lvl <= 0 ?  1 : lvl);
				p.getPacketSender().sendMessage("Avatar of Death has reduced your Prayer level.");
			}
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MAGIC, true));
		}
		return true;
	}


	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 5;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
