package com.asylum.engine.network;

import com.asylum.common.entity.EntityPlayer;
import com.asylum.common.network.packets.IPacket;
import com.asylum.common.network.packets.PacketManager;
import com.asylum.engine.Engine;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by James on 8/14/2017.
 */
public class NetworkManager extends Thread {

    private static NetworkManager instance;
    private List<Socket> clients;
    private Map<Socket, BufferData> bufferMap;
    private ReadWriteLock clientLock = new ReentrantReadWriteLock();


    public static synchronized NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }



    public synchronized void start(int port) {
        Listener.getInstance().start(port);
        clients = new ArrayList<>();
        bufferMap = new HashMap<>();
        super.start();
    }


    @Override
    public void run() {
        while (Engine.running){
            try {
                currentThread().sleep(1000);
                checkInput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkInput() {
        try {
            clientLock.readLock().lock();
            for (Socket socket : clients) {
                InputStream stream = socket.getInputStream();
                if (stream.available() > 0) {
                    BufferData data = getBuffer(socket);
                    if (data.read(stream))
                    {
                        bufferMap.remove(socket);//We're done with that buffer, a new one will be created if needed.
                        PacketManager.getInstance().handlePacket(socket,new DataInputStream(new ByteArrayInputStream(data.data.toByteArray())));
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientLock.readLock().unlock();
        }
    }

    public void addConnection(Socket socket) {
        try {
            clientLock.writeLock().lock();
            clients.add(socket);
        }
        finally {
            clientLock.writeLock().unlock();
        }

    }


    private BufferData getBuffer(Socket socket) throws IOException {
        if (bufferMap.containsKey(socket))
            return bufferMap.get(socket);

        BufferData data  = new BufferData(socket.getInputStream().read());
        return data;
    }


    private class BufferData {
        ByteArrayOutputStream data;
        int size;

        public BufferData(int size){
            data = new ByteArrayOutputStream();
            this.size=size;
        }

        public boolean read(InputStream stream) throws IOException {
            int available = stream.available();
            byte[] in = new byte[Math.min(size, available)];
            stream.read(in);
            data.write(in);
            size -= available;
            if (size <= 0)
                return true;
            return false;
        }
    }

    public void sendPacketToPlayer(EntityPlayer player, IPacket packet) {
        PacketManager.getInstance().sendPacket(player.socket,packet);
    }



}
