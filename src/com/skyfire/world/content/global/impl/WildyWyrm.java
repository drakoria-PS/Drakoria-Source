package com.skyfire.world.content.global.impl;

import com.skyfire.model.Position;
import com.skyfire.util.Misc;
import com.skyfire.world.World;
import com.skyfire.world.content.global.GlobalBoss;
import com.skyfire.world.content.global.SpawnLocation;
import com.skyfire.world.entity.impl.player.Player;

/**
 * Created by Stan van der Bend on 16/12/2017.
 * project: runeworld-game-server
 * package: runeworld.world.entity.combat.strategy.global.impl
 */
public class WildyWyrm extends GlobalBoss {

    private final static int NPC_ID = 6313;
    private final static int KEY_ITEM_ID = 19933;

    private final static String RECEIVE_DROP_MESSAGE = "<img=10> <col=008FF3>Abyzou the world boss has been killed. He will be back though!";

    public WildyWyrm() {
        super(NPC_ID);
    }

    @Override
    public GlobalBoss reincarnate() {
        return new WildyWyrm();
    }

    @Override
    protected void handleDrop(Player player) {
        //player.giveItem(KEY_ITEM_ID, 1);
        
    	int[] rewards = 		{537, 989, 2572, 6585, 13047, 4151, 989, 19055, 15220, 15020, 15019, 15018, 11730, 11283, 11235, 15486, 15241, 15332, 13879, 13883, 11230, 11230, 11230, 11230, 15243, 15243, 15243, 15243, 11212, 11212, 11212, 11212, 2, 2, 2, 2, 565, 560, 995, 995, 995, 2435, 2435, 15273, 15273, 15273, 15273, 1514, 1514, 384, 384, 384, 454, 452, 1631, 1619, 3025, 15332, 386};
		int[] rewardsAmount = 	{25, 5, 1, 1, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 20, 65, 50, 30, 15, 5, 10, 15, 20, 20, 20, 20, 20, 100, 250, 500, 1000, 2500, 5000, 5000000, 5000000, 5000000, 10, 20, 100, 50, 25, 250, 100, 250, 100, 250, 1000, 250, 10, 1, 1, 10, 1, 150};
		int rewardPos = Misc.getRandom(rewards.length-1);
		player.getInventory().add(rewards[rewardPos], (int)((rewardsAmount[rewardPos]*0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
		
    	
    	player.sendMessage(RECEIVE_DROP_MESSAGE);
 	
    }

    @Override
    protected SpawnLocation[] spawnLocations() {
        return new SpawnLocation[]{
                new SpawnLocation(new Position(3104, 5539, 0), "Lair")
        };
    }

    @Override
    protected int minutesTillRespawn() {
        return 60;
    }

    @Override
    protected int minutesTillDespawn() {
        return 10;
    }

    @Override
    protected int maximumDrops() {
        return 8;
    }
}
