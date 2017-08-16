package com.asylum.common.network.packets;

import java.net.Socket;

/**
 * Created by James on 8/14/2017.
 */
public interface IPacketHandler<T extends IPacket> {

    public void handle(T Packet, Socket socket);
}
