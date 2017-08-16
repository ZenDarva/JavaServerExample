package com.asylum.engine.network;

import com.asylum.common.network.packets.IPacketHandler;
import com.asylum.common.network.packets.PacketShutdown;
import com.asylum.engine.Engine;

import java.net.Socket;

/**
 * Created by James on 8/14/2017.
 */
public class PacketShutdownHandler implements IPacketHandler<PacketShutdown> {
    @Override
    public void handle(PacketShutdown Packet, Socket socket) {
        Engine.running=false;
    }
}
