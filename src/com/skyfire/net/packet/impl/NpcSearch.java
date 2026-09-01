package com.skyfire.net.packet.impl;

import com.skyfire.world.content.DropLookup;
import com.skyfire.world.entity.impl.player.Player;
import com.skyfire.model.input.Input;

public class NpcSearch extends Input {

    @Override
    public void handleSyntax(Player player, final String syntax) {
        if(syntax.length() < 3) {
            player.sendMessage("You must type atleast 3 letters");
            return;
        }
        if(!player.getClickDelay().elapsed(600)) {
            return;
        }
        player.getClickDelay().reset();
        DropLookup.search(player, syntax, DropLookup.SearchPolicy.NPC);
    }
}