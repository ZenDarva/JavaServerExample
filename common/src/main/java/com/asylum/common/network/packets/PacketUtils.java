package com.asylum.common.network.packets;

import com.sun.javafx.geom.Vec3d;

import java.io.DataInput;
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

    public static void writeIntArray(DataOutputStream stream, int[] array) throws IOException {
        stream.writeInt(array.length);
        for (int i = 0; i < array.length; i++)
            stream.writeInt(array[i]);
    }

    public static int[] readIntArray(DataInputStream stream) throws IOException {
        int[] result = new int[stream.readInt()];
        for (int i = 0; i < result.length; i++)
            result[i]=stream.readInt();
        return result;
    }
}
