package com.asylum.common.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by James on 8/14/2017.
 */
public class PacketUtils {

    public static String readString(DataInputStream stream) throws IOException {
        int len = stream.readInt();
        byte[] data = new byte[len];
        stream.read(data);
        return new String(data);
    }

    public static void writeString(DataOutputStream stream, String value) throws IOException {
        stream.writeInt(value.getBytes().length);
        stream.write(value.getBytes());
    }
}
