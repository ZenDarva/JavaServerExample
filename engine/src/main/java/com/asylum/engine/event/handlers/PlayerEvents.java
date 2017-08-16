package com.asylum.engine.event.handlers;

import com.asylum.common.entity.EntityPlayer;
import com.asylum.common.event.events.EventPlayerLogin;
import com.asylum.common.event.events.SubscribeEvent;
import com.asylum.engine.Engine;

/**
 * Created by James on 8/15/2017.
 */
public class PlayerEvents {

    @SubscribeEvent
    public void playerLogin(EventPlayerLogin event){
        EntityPlayer player = new EntityPlayer();
        player.socket=event.socket;
        player.displayName=event.playerName;
        Engine.addPlayer(player);
    }
}
