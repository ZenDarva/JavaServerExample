package com.asylum.common.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by James on 8/14/2017.
 */
public interface IPacket {

    public void serialize(DataOutputStream output) throws IOException;
    public void deserialize(DataInputStream input) throws IOException;
}
