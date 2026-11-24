# Drakoria — Range / Mage Custom Visuals, isolated pilot

Branch: `feature-drakoria-range-mage-vfx-20261124` (based on Source `main`).

## Scope

**Range:** equip normal Shortbow (item ID 841) and Bronze arrows (item ID 882), then attack an NPC normally. The existing real arrow projectile (GFX 10), ammunition usage, player animation and damage remain unchanged. On an accurate hit, a physical impact (existing graphic ID 337) is displayed on the victim.

**Mage:** equip Staff of Air (item ID 1381) and cast Wind Strike (spell ID 1152). The existing casting animation, original wind projectile (GFX 2699), original target impact (GFX 2700), rune use and damage stay intact. One existing magic charging graphic (102) plays on cast, and two extra wind-projectile layers (2699) travel at staggered heights and delays. These extra projectiles are **visual only**, deal no extra damage, and form a primitive wind vortex.

The new wind effect is a **prototype using the existing cache**, not a genuine rotating 3D tornado model. A true custom tornado requires new cached projectile models/animations and an assigned spotanim/graphic ID. Nothing has been added to the cache.

## Activation

The feature is **off by default** and does not change normal combat unless explicitly enabled on the **server JVM**:

```
-Ddrakoria.combat.visuals.range=true
-Ddrakoria.combat.visuals.mage=true
```

Each switch can be enabled independently. Disable by removing the property or setting it to `false`.

No damage, accuracy, hit delays, projectile collision, weapon stats, XP or spell requirements were changed. Classic and all client renderers receive the same normal packets; no Classic rendering code was edited. Test with the normal client and GPU CORE for visual parity.

## Deployment and validation

Run `tools/APPLY-DRAKORIA-RANGE-MAGE-VFX.ps1 -SourceRepo "<local path to Drakoria-Source>"` in PowerShell. The script refuses dirty Git status, fetches/fast-forwards the feature branch, checks Java21, compiles using your existing Gradle 8 installation (not the old Gradle 6.9.4 wrapper), and executes the focused scope gate.

Alternatively, from the feature branch run:

```powershell
gradle --no-daemon classes
javac --release 8 -encoding UTF-8 -cp build/classes/java/main -d build/test-custom-combat-visuals tests/com/skyfire/world/content/combat/visuals/DrakoriaCombatVisualsCheck.java
java -cp "build/test-custom-combat-visuals;build/classes/java/main" com.skyfire.world.content.combat.visuals.DrakoriaCombatVisualsCheck
```

Expected CPU log: `DRAKORIA_RANGE_MAGE_VISUALS_CPU=PASS`.

The corresponding GitHub Actions workflow runs compilation/test on Windows Java 21 with Gradle 8.14.8 when available.

## In-game test scene

- NPC standing 3–5 tiles ahead, same plane, daylight.
- Shortbow 841 + bronze arrows 882: 1 flying arrow per regular attack; one physical impact only if hit is accurate; normal ammo consumption; no extra hits.
- Staff of Air 1381 + Wind Strike 1152: staff charge, 3 wind streams including the original, original spell impact; no additional damage.
- Compare GPU CORE and software renderers from the same camera and location.
- Check no duplicate effects when both properties are omitted/false and no changes to other weapons, spells, PvP combat or NPC attacks.

## Status

- GitHub source integrated: yes, isolated feature branch.
- Java21 focused CPU compilation / full Gradle build: pending execution on full server source.
- RTX3060 in-game visual/runtime validation: pending.
- New rotating tornado asset / true staff-tip emitter: not implemented, requires dedicated cache model and bone/mount data.
