package com.asylum.common.network.packets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 8/14/2017.
 */
public class PacketManager {
    private static PacketManager instance;
    private Map<String, List<PacketData>> packetHandlers;
    private static Logger LOGGER = LoggerFactory.getLogger(PacketManager.class);

    public synchronized static PacketManager getInstance(){
        if (instance == null)
            instance=new PacketManager();
        return instance;
    }

    private PacketManager(){
        this.packetHandlers = new HashMap<>();
    }

    public void registerHandler(String namespace, int id, Class<? extends IPacket> packet, IPacketHandler handler ){
        List<PacketData> handlerList = packetHandlers.get(namespace);
        if (handlerList == null) {
            handlerList = new ArrayList<>();
            packetHandlers.put(namespace,handlerList);
        }
        handlerList.add(new PacketData(packet,handler,id, namespace));

    }

    public void handlePacket(DataInputStream stream){
        try {
            String namespace = PacketUtils.readString(stream);
            int id = stream.readInt();
            List<PacketData> handlerList = packetHandlers.get(namespace);
            if (handlerList == null){
                LOGGER.error("Received unknown packet of namespace {}, with id {}", namespace,id);
                return;
            }
            for (PacketData data : handlerList) {
                if (data.id == id) {
                    IPacket packet = (IPacket) data.packet.newInstance();
                    packet.deserialize(stream);
                    if (data.handler != null) {
                        data.handler.handle(packet);
                    }
                    break;
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
            PacketData data = getPacketData(packet.getClass());
            PacketUtils.writeString(stream, data.namespace);
            stream.writeInt(data.id);
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

    private PacketData getPacketData(Class target){
        for (String str: packetHandlers.keySet()){
            for (PacketData data : packetHandlers.get(str) ){
                if (data.packet == target)
                    return data;
            }
        }
        return null;
    }

    private class PacketData{
        public Class<? extends IPacket> packet;
        public IPacketHandler handler;
        public int id;
        public String namespace;

        public PacketData(Class packet, IPacketHandler handler, int id, String namespace){
            this.packet = packet;
            this.handler = handler;
            this.id = id;
            this.namespace = namespace;
        }
   }

}
