package com.asylum.client;

import com.asylum.common.network.packets.PacketConnect;
import com.asylum.common.network.packets.PacketManager;
import com.asylum.common.network.packets.PacketShutdown;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by James on 8/14/2017.
 */
public class ClientMain {

    public static void main(String args[]) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost",9000);
        //socket.connect(new InetSocketAddress("localhost",9000));
        PacketConnect connect = new PacketConnect("darva");
        PacketManager.getInstance().sendPacket(socket, connect);
        Thread.sleep(20000);
        PacketShutdown shutdown = new PacketShutdown();
        PacketManager.getInstance().sendPacket(socket,shutdown);
        Thread.sleep(20000);
    }
}
