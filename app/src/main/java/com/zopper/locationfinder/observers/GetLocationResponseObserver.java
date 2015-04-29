package com.zopper.locationfinder.observers;

import java.util.Observable;

/**
 * Created by ishaan on 4/28/15.
 */
public class GetLocationResponseObserver extends Observable {

    private static GetLocationResponseObserver self;

    private GetLocationResponseObserver(){}

    public static GetLocationResponseObserver getSharedInstance()
    {
        if(self == null)
        {
            self = new GetLocationResponseObserver();
        }
        return self;

    }

    public void raiseNotification(Object dataObject)
    {
        setChanged();
        notifyObservers(dataObject);
    }

}
