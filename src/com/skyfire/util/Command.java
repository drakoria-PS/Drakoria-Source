package com.skyfire.util;

import com.skyfire.world.entity.impl.player.Player;

/**
 * @author Stan van der Bend
 */
public interface Command {

    void execute(final Player player, String command);

}
