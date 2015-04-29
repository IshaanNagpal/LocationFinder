package com.zopper.locationfinder.observers;

import java.util.Observable;

/**
 * Created by ishaan on 4/28/15.
 */
public class GetLocationRequestObserver extends Observable {

    private static GetLocationRequestObserver self;

    private GetLocationRequestObserver(){}

    public static GetLocationRequestObserver getSharedInstance()
    {
        if(self == null)
        {
            self = new GetLocationRequestObserver();
        }
        return self;

    }

    public void raiseNotification(Object dataObject)
    {
        setChanged();
        notifyObservers(dataObject);
    }

}
