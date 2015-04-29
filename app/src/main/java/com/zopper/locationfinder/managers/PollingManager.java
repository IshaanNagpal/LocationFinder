package com.zopper.locationfinder.managers;

import com.zopper.locationfinder.constants.AppConstants;
import com.zopper.locationfinder.observers.AppInBackgroundObserver;
import com.zopper.locationfinder.observers.AppInForegroundObserver;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

/**
 * Created by ishaan on 4/29/15.
 */
public class PollingManager implements Observer{

    public static PollingManager instance;
    private long timerDuration;
    private Timer timer;
    /**
     * This method creates shared instance for policy manger
     * @return instance
     */
    public static PollingManager getSharedInstance()
    {
        if(instance == null)
            instance = new PollingManager();

        return instance;
    }

    private PollingManager()
    {
        addObservers();
    }

    private void addObservers() {

        AppInForegroundObserver.getSharedInstance().addObserver(this);
        AppInBackgroundObserver.getSharedInstance().addObserver(this);
    }


    @Override
    public void update(Observable observable, Object o) {

        if(observable instanceof  AppInForegroundObserver)
            onAppIsInForeground();

        else if(observable instanceof AppInBackgroundObserver)
            onAppIsInBackground();
    }

    private void onAppIsInBackground() {

        stopPolling();
    }

    private void stopPolling() {

        stopGetLocationsTimer();
    }


    private void onAppIsInForeground() {

        if(timerDuration == 0)
            timerDuration = AppConstants.GET_LOCATIONS_TIME_INTERVAL;

        startGetLocationsTimer();
    }

    /**
     * This method will start a timer that will run after specified interval and fetch messages for the loaded conversation
     * @param
     */
    private void startGetLocationsTimer()
    {
        stopGetLocationsTimer();

        timer = new Timer();

        FetchDataTimerTask getAllDataTimerTask = new FetchDataTimerTask();

        timer.schedule(getAllDataTimerTask, 0, timerDuration);
    }

    /**
     * This method will stop the running timer
     */
    private void stopGetLocationsTimer()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

}
