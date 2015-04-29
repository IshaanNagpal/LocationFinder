package com.zopper.locationfinder.modules;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zopper.locationfinder.R;
import com.zopper.locationfinder.models.LocationModel;
import com.zopper.locationfinder.models.NotificationModel;
import com.zopper.locationfinder.observers.GetLocationRequestObserver;
import com.zopper.locationfinder.observers.GetLocationResponseObserver;
import com.zopper.locationfinder.utility.AppUtility;

import java.util.Observable;
import java.util.Observer;

import static android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED;


public class LocationFinderActivity extends ActionBarActivity implements Observer{

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_finder);

        addObservers();
    }

    /**
     * Method to add observers to listen notifications
     */
    private void addObservers() {
        GetLocationResponseObserver.getSharedInstance().addObserver(this);
    }

    @Override
    protected void onResume() {

        if(isLocationServicesTurnedOn() && isHighAccuracyMode())
            raiseNotificationToGetLocation();

        else
            showAlert(getString(R.string.location_disabled), getString(R.string.enable_gps), getString(R.string.cancel), getString(R.string.please_turn_on));

        checkForInternetConnection();

        super.onResume();
    }

    /**
     * This method checks for internet connection availablity and shows alert if no internet connection found
     */
    private void checkForInternetConnection() {

        if(!AppUtility.isNetworkConnected(this))
            showNoInternetConnectionAlert();
    }

    /**
     * This method shows an alert for no internet connectivity
     */
    private void showNoInternetConnectionAlert() {
        AppUtility.showAlert(this, getString(R.string.connection_error), getString(R.string.check_your_internet), getString(R.string.ok), R.drawable.ic_launcher);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_finder, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method set the latitude and longitude values on view.
     * @param locationModel
     */
    private void setCurrentLatLongOnView(LocationModel locationModel)
    {
        setTextOnTextView(R.id.latitude_textview, "Lat : " + locationModel.latitude+"");
        setTextOnTextView(R.id.longitude_textview, "Long : " + locationModel.longitude+"");
    }

    /**
     * This method sets the text on textview
     * @param resId
     * @param text
     */
    private void setTextOnTextView(int resId, String text)
    {
        TextView textView = (TextView)findViewById(resId);
        textView.setText(text);
    }

    /**
     * It is a callback method for the button defined in layout
     * @param view
     */
    public void onUpdateButtonClicked(View view)
    {
        raiseNotificationToGetLocation();
    }




    /**
     * This method will show an alert on the screen
     * @param title
     * @param positiveButtonText
     * @param negativeButtonText
     * @param message
     */
    private void showAlert(String title, String positiveButtonText, String negativeButtonText, String message)
    {
        DialogInterface.OnClickListener buttonListener = getButtonListenerForAlert();

        Builder alertDialogBuilder = AppUtility.getAlertDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, 2, false, null, buttonListener, title, message, null, positiveButtonText, negativeButtonText);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /**
     * This method creates a dialog listeners for the alert dialog buttons
     * @return
     */
    private android.content.DialogInterface.OnClickListener getButtonListenerForAlert() {

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:
                        openGPSSettings();
                        break;

                    case AlertDialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        finish();
                        break;

                    case AlertDialog.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    default: dialog.dismiss();
                        break;
                }
            }
        };

        return listener;
    }

    /**
     * This method will open the location settings in the settings app.
     */
    private void openGPSSettings()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }


    public boolean isHighAccuracyMode()
    {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(),Settings.Secure.LOCATION_MODE);

            } catch (SettingNotFoundException e) {
                e.printStackTrace();
            }

            return (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);

        }else{
            locationProviders = Settings.Secure.getString(getContentResolver(), LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    private boolean isLocationServicesTurnedOn()
    {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if(locationManager==null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}
        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        if(!gps_enabled && !network_enabled)
            return false;

        else
            return true;
    }

    @Override
    public void update(Observable observable, Object data) {

        if(observable instanceof GetLocationResponseObserver)
            onLocationResponse(data);
    }

    private void onLocationResponse(Object data) {

        NotificationModel notificationModel = (NotificationModel)data;
        LocationModel locationModel = (LocationModel) notificationModel.responseModel;

        setCurrentLatLongOnView(locationModel);
    }

    private void raiseNotificationToGetLocation() {

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.context = this;
        GetLocationRequestObserver.getSharedInstance().raiseNotification(notificationModel);
    }


    @Override
    protected void onDestroy() {

        deleteObservers();
        super.onDestroy();
    }

    private void deleteObservers() {
        GetLocationResponseObserver.getSharedInstance().deleteObserver(this);
    }
}
