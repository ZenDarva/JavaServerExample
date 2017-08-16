package com.asylum.engine;


import com.asylum.common.entity.EntityPlayer;
import com.asylum.common.event.EventManager;
import com.asylum.common.network.packets.PacketConnect;
import com.asylum.common.network.packets.PacketManager;

import com.asylum.common.network.packets.PacketShutdown;
import com.asylum.engine.event.handlers.PlayerEvents;
import com.asylum.engine.network.Listener;
import com.asylum.engine.network.NetworkManager;
import com.asylum.engine.network.PacketShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by James on 8/14/2017.
 */
public class Engine {
    static Logger LOGGER = LoggerFactory.getLogger(Engine.class);
    static String propertyFilePath = "/config/properties.cfg";
    private static List<EntityPlayer> players;
    public static ReadWriteLock playersLock;
    public static volatile boolean running = true;

    public static void main(String args[]) throws IOException {
        Properties props =  new Properties();
//        File file = new File(System.getProperties().get("user.dir") + propertyFilePath);
//        if (!file.exists()) {
//            LOGGER.error("Missing config file.");
//        }
//        props.load(new FileInputStream(file));

        players = new ArrayList<>();
        NetworkManager.getInstance().start(9000);
        PacketManager.getInstance().registerHandler("vanilla",1,PacketConnect.class, new PacketConnect.ConnectHandler());
        PacketManager.getInstance().registerHandler("vanilla",2,PacketShutdown.class, new PacketShutdownHandler());
        EventManager.getInstance().registerHandler(new PlayerEvents());

        while(running){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Listener.getInstance().interrupt();
    }

    public static void addPlayer(EntityPlayer player){
        playersLock.writeLock().lock();
        try{
            players.add(player);
        }
        finally{
            playersLock.writeLock().unlock();
        }
    }
    public static List<EntityPlayer> getPlayers(){
        playersLock.readLock().lock();
        try {
            return Collections.unmodifiableList(players);
        }
        finally{
            playersLock.readLock().unlock();
        }
    }

    public EntityPlayer getPlayerByName(String name) {
        playersLock.readLock().lock();
        try {
            for (EntityPlayer player : players){
                if (player.displayName.equals(name))
                        return player;
            }
        }
        finally{
            playersLock.readLock().unlock();
        }
        return null;
    }
}
