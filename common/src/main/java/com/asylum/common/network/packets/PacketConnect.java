package com.asylum.common.network.packets;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by James on 8/14/2017.
 */
public class PacketConnect implements IPacket {
    private static Logger LOGGER = LoggerFactory.getLogger(PacketConnect.class);

    public String username;


    @Override
    public void serialize(DataOutputStream output) throws IOException {
        PacketUtils.writeString(output,username);
    }

    @Override
    public void deserialize(DataInputStream input) throws IOException {
        username = PacketUtils.readString(input);
    }

    public PacketConnect(){};
    public PacketConnect(String username){
        this.username=username;
    }


    public static class ConnectHandler implements IPacketHandler<PacketConnect> {

        @Override
        public void handle(PacketConnect packet) {
            LOGGER.info("{} has connected.", packet.username);
        }
    }
}
