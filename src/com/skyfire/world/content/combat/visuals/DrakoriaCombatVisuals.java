package com.skyfire.world.content.combat.visuals;

import com.skyfire.model.Graphic;
import com.skyfire.model.GraphicHeight;
import com.skyfire.model.Projectile;
import com.skyfire.model.container.impl.Equipment;
import com.skyfire.world.content.combat.CombatContainer;
import com.skyfire.world.content.combat.CombatType;
import com.skyfire.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.skyfire.world.entity.impl.Character;
import com.skyfire.world.entity.impl.player.Player;

/**
 * Isolated visual-only combat pilot. No new damage, weapon timings or cache IDs.
 * All visual assets are already referenced by the existing combat source.
 */
public final class DrakoriaCombatVisuals {
    public static final int SHORTBOW_ID = 841;
    public static final int STAFF_OF_AIR_ID = 1381;
    public static final int WIND_STRIKE_SPELL_ID = 1152;
    public static final int BRONZE_ARROW_PROJECTILE_ID = 10;
    public static final int WIND_PROJECTILE_ID = 2699;
    public static final int ARROW_IMPACT_GRAPHIC_ID = 337;

    private DrakoriaCombatVisuals() { }

    public static boolean rangeEnabled(int weaponId, int projectileId,
                                       boolean specialActive) {
        return Boolean.parseBoolean(System.getProperty(
                "drakoria.combat.visuals.range", "false"))
                && weaponId == SHORTBOW_ID
                && projectileId == BRONZE_ARROW_PROJECTILE_ID
                && !specialActive;
    }

    public static boolean mageEnabled(int weaponId, int spellId) {
        return Boolean.parseBoolean(System.getProperty(
                "drakoria.combat.visuals.mage", "false"))
                && weaponId == STAFF_OF_AIR_ID
                && spellId == WIND_STRIKE_SPELL_ID;
    }

    /**
     * Leave the existing physical arrow, ammo consumption and damage intact;
     * add a cache-backed physical impact when the arrow actually hits.
     */
    public static CombatContainer rangedContainer(Player player,
            Character victim, AmmunitionData ammo, boolean darkBow) {
        int hits = darkBow ? 2 : 1;
        int weaponId = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
        if (ammo == null || !rangeEnabled(weaponId,
                ammo.getProjectileId(), player.isSpecialActivated())) {
            return new CombatContainer(player, victim, hits, CombatType.RANGED, true);
        }
        return new CombatContainer(player, victim, hits, CombatType.RANGED, true) {
            @Override
            public void onHit(int damage, boolean accurate) {
                if (accurate) {
                    victim.performGraphic(new Graphic(
                            ARROW_IMPACT_GRAPHIC_ID, GraphicHeight.MIDDLE));
                }
            }
        };
    }

    /** Wind aura when the staff begins casting; graphic 102 is already in cache. */
    public static void chargeWindVortex(Character caster, int spellId) {
        if (caster == null || !caster.isPlayer()) return;
        Player player = (Player) caster;
        int weaponId = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
        if (mageEnabled(weaponId, spellId)) {
            caster.performGraphic(new Graphic(102, GraphicHeight.HIGH));
        }
    }

    /**
     * Add two vertically staggered wind streams to the original Wind Strike
     * projectile, approximating a short vortex without modifying spell damage.
     * A true rotating tornado mesh needs its own cache graphic/animation.
     */
    public static void addWindVortex(Character caster, Character victim, int spellId) {
        if (caster == null || victim == null || !caster.isPlayer()) return;
        Player player = (Player) caster;
        int weaponId = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
        if (!mageEnabled(weaponId, spellId)) return;
        new Projectile(caster, victim, WIND_PROJECTILE_ID,
                44, 3, 55, 33, 12).sendProjectile();
        new Projectile(caster, victim, WIND_PROJECTILE_ID,
                50, 3, 34, 27, -12).sendProjectile();
    }
}
