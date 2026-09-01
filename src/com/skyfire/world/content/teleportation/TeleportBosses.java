package com.skyfire.world.content.teleportation;

public class TeleportBosses extends Teleporting  {

	public static enum Bosses {
		TELEPORT_1(new String[] {"@bla@Dire", "@bla@Wolf"}, new int[] {3110, 9518, 0}),
		TELEPORT_2(new String[] {"Godwars", "Dungeon"}, new int[] {2871, 5319, 2}),
		TELEPORT_3(new String[] {"Kalphite", "Queen"}, new int[] {3488, 9516, 0}),
		TELEPORT_4(new String[] {"@yel@Undead", "@yel@King"}, new int[] {3200, 3210, 0}),
		TELEPORT_5(new String[] {"Frost Dragons", "@red@(wilderness)"}, new int[] {2961, 3882, 0}),
		TELEPORT_6(new String[] {"Dagannoth", "Kings"}, new int[] {1912, 4367, 0}),
		TELEPORT_7(new String[] {"@cya@Le'fosh", "@gr1@Weapon Upgrade 2"}, new int[] {2848, 5585, 0}),
		TELEPORT_8(new String[] {"@mag@Chaos Ele", "@red@(wilderness)"}, new int[] {3276, 3915, 0}),
		TELEPORT_9(new String[] {"Corporeal", "Beast"}, new int[] {2886, 4376, 0}),
		TELEPORT_10(new String[] {"@red@Headless", "@red@Beast"}, new int[] {2766, 2799, 0}),
		TELEPORT_11(new String[] {"Barrelchest", "Area"}, new int[] {2973, 9517, 3}),
		TELEPORT_12(new String[] {"Lizardman", "Shaman"}, new int[] {2718, 9811, 1});
		/**
		 * Initializing the teleport names.
		 */
		private String[] teleportName;
		/**
		 * Initializing the teleport coordinates.
		 */
		private int[] teleportCoordinates;
		
		/**
		 * Constructing the enumerator.
		 * @param teleportName
		 * 			The name of the teleport.
		 * @param teleportName2
		 * 			The secondary name of the teleport.
		 * @param teleportCoordinates
		 * 			The coordinates of the teleport.
		 */
		private Bosses(final String[] teleportName, final int[] teleportCoordinates) {
			this.teleportName = teleportName;
			this.teleportCoordinates = teleportCoordinates;
		}
		/**
		 * Setting the teleport name.
		 * @return
		 */
		public String[] getTeleportName() {
			return teleportName;
		}
		/**
		 * Setting the teleport coordinates.
		 * @return
		 */
		public int[] getCoordinates() {
			return teleportCoordinates;
		}
	}

}
