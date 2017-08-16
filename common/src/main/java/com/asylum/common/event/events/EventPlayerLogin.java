package com.asylum.common.event.events;

import java.net.Socket;

/**
 * Created by James on 8/15/2017.
 */
public class EventPlayerLogin extends IEvent {

    public String playerName;
    public Socket socket;
    @Override
    public boolean isCancelable() {
        return true;
    }
}
