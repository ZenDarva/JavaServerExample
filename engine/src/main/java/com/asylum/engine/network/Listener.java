package com.asylum.engine.network;




import com.asylum.engine.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by James on 8/14/2017.
 */
public class Listener extends Thread {

    private ServerSocket socket;
    private static Listener instance;
    private int port;
    static Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    public static synchronized Listener getInstance(){

        if (instance== null) {
            instance = new Listener();
        }
        return instance;
    }


    public synchronized void start(int port) {
        this.port = port;
        try {
            LOGGER.info("Starting server on port {}", port);
            socket = new ServerSocket(port);
            socket.setSoTimeout(1000);
            ;
        } catch (IOException e) {

        }
        super.start();
    }

    @Override
    public void run() {
        while (Engine.running) {

            try {
                Thread.sleep(100);
                checkConnections();
            } catch (InterruptedException e) {
                return;
            }
        }
        LOGGER.info("Shutting down server.");
    }

    private void checkConnections(){
        try {
            Socket newConnection = socket.accept();
            if (newConnection.isConnected()) {
                LOGGER.info("Accepting connection from {}", newConnection.getInetAddress().getHostAddress());
                NetworkManager.getInstance().addConnection(newConnection);
            }
        } catch (IOException e) {
        }
    }
}
