package com.asylum.common.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by James on 8/14/2017.
 */
public class PacketMessage implements IPacket {
    public String Message;
    @Override
    public void serialize(DataOutputStream output) throws IOException {

    }

    @Override
    public void deserialize(DataInputStream input) throws IOException {

    }
}
