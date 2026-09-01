package com.skyfire.world.content.drakoria;

public class DrakoriaPokemon {

    public enum Trainer {

        BROCK(1999, 1930, 4724, 0),
        MISTY(2000, 1952, 4724, 0),
        LT_SURGE(2001, 1973, 4724, 0),
        ERIKA(2002, 1930, 4704, 0),
        KOGA(2003, 1952, 4704, 0),
        SABRINA(2004, 1973, 4704, 0),
        BLAINE(2005, 1930, 4684, 0),
        GIOVANNI(2006, 1952, 4684, 0);

        private final int npcId;
        private final int x;
        private final int y;
        private final int z;

        Trainer(int npcId, int x, int y, int z) {
            this.npcId = npcId;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getNpcId() {
            return npcId;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }
}
