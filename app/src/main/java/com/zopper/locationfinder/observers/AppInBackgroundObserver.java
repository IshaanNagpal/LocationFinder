package com.zopper.locationfinder.observers;

import java.util.Observable;

/**
 * Created by ishaan on 4/28/15.
 */
public class AppInBackgroundObserver extends Observable {

    private static AppInBackgroundObserver self;

    private AppInBackgroundObserver(){}

    public static AppInBackgroundObserver getSharedInstance()
    {
        if(self == null)
        {
            self = new AppInBackgroundObserver();
        }
        return self;

    }

    public void raiseNotification(Object dataObject)
    {
        setChanged();
        notifyObservers(dataObject);
    }

}
