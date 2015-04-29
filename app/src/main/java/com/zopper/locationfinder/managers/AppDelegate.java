package com.zopper.locationfinder.managers;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.Handler;

import com.zopper.locationfinder.observers.AppInBackgroundObserver;
import com.zopper.locationfinder.observers.AppInForegroundObserver;

/**
 * Created by ishaan on 4/29/15.
 */
public class AppDelegate extends Application implements ActivityLifecycleCallbacks {

    private boolean appForeground;

    @Override
    public void onCreate()
    {
        appForeground = true;
        instantiateManagers();
        registerActivityLifecycleCallbacks(this);
        super.onCreate();

    }

    private void instantiateManagers() {

        LocationFinderManager.getSharedInstance();
        PollingManager.getSharedInstance();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

        if(!appForeground)
        {
            appForeground = true;
        }else
        {
            appForeground = false;
            AppInForegroundObserver.getSharedInstance().raiseNotification(null);
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if(appForeground)
                {
                    appForeground = false;
                }else
                {
                    appForeground = true;
                    AppInBackgroundObserver.getSharedInstance().raiseNotification(null);
                }
            }
        }, 1000);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
