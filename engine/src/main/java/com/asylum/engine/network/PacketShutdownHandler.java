package com.asylum.engine.network;

import com.asylum.common.network.packets.IPacketHandler;
import com.asylum.common.network.packets.PacketShutdown;
import com.asylum.engine.Engine;

/**
 * Created by James on 8/14/2017.
 */
public class PacketShutdownHandler implements IPacketHandler<PacketShutdown> {
    @Override
    public void handle(PacketShutdown Packet) {
        Engine.running=false;
    }
}
