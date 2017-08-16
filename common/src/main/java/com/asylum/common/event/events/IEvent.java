package com.asylum.common.event.events;

import javax.naming.OperationNotSupportedException;

/**
 * Created by James on 8/15/2017.
 */
public abstract class IEvent {

    private boolean canceled = false;

    public abstract boolean isCancelable();

    public void setCanceled(boolean canceled) throws OperationNotSupportedException {
        if (isCancelable() == false)
        {
            throw new OperationNotSupportedException();
        }
        this.canceled = canceled;
    }
    public boolean isCanceled(){
        return canceled;
    }

}
