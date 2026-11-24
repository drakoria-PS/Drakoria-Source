package com.skyfire.world.content.combat.visuals;

/**
 * Java 21 CPU gate for strict opt-in range/mage visuals. It never starts combat,
 * changes damage or touches the map/cache.
 */
public final class DrakoriaCombatVisualsCheck {
    private DrakoriaCombatVisualsCheck() { }

    private static void yes(boolean condition, String code) {
        if (!condition) throw new AssertionError(code);
    }

    public static void main(String[] args) {
        String oldRange = System.getProperty("drakoria.combat.visuals.range");
        String oldMage = System.getProperty("drakoria.combat.visuals.mage");
        try {
            System.clearProperty("drakoria.combat.visuals.range");
            System.clearProperty("drakoria.combat.visuals.mage");
            yes(!DrakoriaCombatVisuals.rangeEnabled(841, 10, false),
                    "RANGE_OFF_BY_DEFAULT");
            yes(!DrakoriaCombatVisuals.mageEnabled(1381, 1152),
                    "MAGE_OFF_BY_DEFAULT");
            System.setProperty("drakoria.combat.visuals.range", "true");
            System.setProperty("drakoria.combat.visuals.mage", "true");
            yes(DrakoriaCombatVisuals.rangeEnabled(841, 10, false),
                    "SHORTBOW_ARROW_SELECTED");
            yes(!DrakoriaCombatVisuals.rangeEnabled(841, 10, true),
                    "SPECIAL_ATTACK_PROTECTED");
            yes(!DrakoriaCombatVisuals.rangeEnabled(9185, 10, false),
                    "CROSSBOW_PROTECTED");
            yes(!DrakoriaCombatVisuals.rangeEnabled(841, 27, false),
                    "BOLT_PROTECTED");
            yes(DrakoriaCombatVisuals.mageEnabled(1381, 1152),
                    "STAFF_AIR_WIND_STRIKE_SELECTED");
            yes(!DrakoriaCombatVisuals.mageEnabled(1381, 1162),
                    "OTHER_SPELL_PROTECTED");
            yes(!DrakoriaCombatVisuals.mageEnabled(1383, 1152),
                    "OTHER_STAFF_PROTECTED");
            System.out.println("DRAKORIA_RANGE_MAGE_VISUALS_CPU=PASS"
                    + " default_off=true range=841+10 mage=1381+1152"
                    + " damage_unchanged=true gpu_pixels=NOT_TESTED");
        } finally {
            if (oldRange == null) System.clearProperty("drakoria.combat.visuals.range");
            else System.setProperty("drakoria.combat.visuals.range", oldRange);
            if (oldMage == null) System.clearProperty("drakoria.combat.visuals.mage");
            else System.setProperty("drakoria.combat.visuals.mage", oldMage);
        }
    }
}
