package com.zopper.locationfinder.observers;

import java.util.Observable;

/**
 * Created by ishaan on 4/28/15.
 */
public class AppInForegroundObserver extends Observable {

    private static AppInForegroundObserver self;

    private AppInForegroundObserver(){}

    public static AppInForegroundObserver getSharedInstance()
    {
        if(self == null)
        {
            self = new AppInForegroundObserver();
        }
        return self;

    }

    public void raiseNotification(Object dataObject)
    {
        setChanged();
        notifyObservers(dataObject);
    }

}
