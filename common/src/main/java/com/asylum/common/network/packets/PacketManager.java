package com.asylum.common.network.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 8/14/2017.
 */
public class PacketManager {
    private static PacketManager instance;
    private Map<Class<? extends IPacket>, IPacketHandler> packetHandlers;

    public synchronized static PacketManager getInstance(){
        if (instance == null)
            instance=new PacketManager();
        return instance;
    }

    private PacketManager(){
        this.packetHandlers = new HashMap<>();
    }

    public void registerHandler(Class<? extends IPacket> packet, IPacketHandler handler ){
        packetHandlers.put(packet,handler);
    }

    public void handlePacket(DataInputStream stream){
        try {
            String packetName = PacketUtils.readString(stream);
            for(Class clazz : packetHandlers.keySet()){
                if (clazz.getName().equals(packetName)) {
                    IPacket packet = (IPacket) clazz.newInstance();
                    packet.deserialize(stream);
                    packetHandlers.get(clazz).handle(packet);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Socket socket, IPacket packet) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        try {

            PacketUtils.writeString(stream,packet.getClass().getName());
            packet.serialize(stream);
            ByteArrayOutputStream finalStream = new ByteArrayOutputStream();
            finalStream.write(bos.toByteArray().length);
            finalStream.write(bos.toByteArray());
            socket.getOutputStream().write(finalStream.toByteArray());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
