package com.zopper.locationfinder.managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.zopper.locationfinder.models.LocationModel;
import com.zopper.locationfinder.models.NotificationModel;
import com.zopper.locationfinder.observers.GetLocationRequestObserver;
import com.zopper.locationfinder.observers.GetLocationResponseObserver;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ishaan on 4/28/15.
 */
public class LocationFinderManager implements LocationListener, Observer
{
    public static LocationFinderManager instance;

    LocationManager locationManager;
    Context context;

    public static LocationFinderManager getSharedInstance()
    {
        if(instance == null)
            instance = new LocationFinderManager();

        return instance;
    }

    /**
     * This is the default constructor
     */
    public LocationFinderManager()
    {
        super();

        addObservers();
    }

    /**
     * This method add observers for the notifications to be listened
     */
    private void addObservers() {

        GetLocationRequestObserver.getSharedInstance().addObserver(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        sendUpdatedLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     * This method receives callback for the registered notifications
     * @param observable
     * @param data
     */
    @Override
    public void update(Observable observable, Object data) {

        if(observable instanceof GetLocationRequestObserver)
            onGetLocationRequest(data);
    }


    /**
     * This method will get the current location.
     * @param data
     */
    private void onGetLocationRequest(Object data) {

        NotificationModel notificationModel = (NotificationModel)data;

        if(notificationModel.context == null)
            return;

        locationManager = (LocationManager) notificationModel.context.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        if(getLocationInstantly() != null)
            sendUpdatedLocation(getLocationInstantly());
    }


    private Location getLocationInstantly()
    {
        if(locationManager != null) {
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        return null;
    }

    private void sendUpdatedLocation(Location location) {

        LocationModel locationModel = new LocationModel();

        locationModel.latitude = location.getLatitude();
        locationModel.longitude = location.getLongitude();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.responseModel = locationModel;

        GetLocationResponseObserver.getSharedInstance().raiseNotification(notificationModel);
    }

}
