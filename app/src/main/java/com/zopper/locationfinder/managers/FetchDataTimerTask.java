package com.zopper.locationfinder.managers;

import android.os.AsyncTask;
import android.util.Log;

import com.zopper.locationfinder.models.NotificationModel;
import com.zopper.locationfinder.observers.GetLocationRequestObserver;

import java.util.TimerTask;

/**
 * Created by ishaan on 4/29/15.
 */
public class FetchDataTimerTask extends TimerTask {

    @Override
    public void run() {

        raiseGetLocationsRequest();
    }

    /**
     * This method sets NotificationModel and raises GetAllDataRequest notification
     */
    private void raiseGetLocationsRequest()
    {
        AsyncTask<Void, Void, Void> getDataAsyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {

                Log.v("LOCATION FINDER", "REFRESH");
                NotificationModel notificationModel = new NotificationModel();
                GetLocationRequestObserver.getSharedInstance().raiseNotification(notificationModel);

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        };

        getDataAsyncTask.execute();
    }
}


