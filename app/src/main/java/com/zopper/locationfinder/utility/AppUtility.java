package com.zopper.locationfinder.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zopper.locationfinder.R;
import com.zopper.locationfinder.models.NotificationModel;

/**
 * Created by ishaan on 4/29/15.
 */
public class AppUtility {



    public static Builder getAlertDialog(final Context context, int dialogTheme, int numberOfButtons, boolean Cancellable, View popUpView, OnClickListener buttonListener, String titleString, String alertDescription, String neutralButton, String positiveButton, String negativeButton) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, dialogTheme);

        if(popUpView != null)
            alertDialogBuilder.setView(popUpView);

        if(titleString != null)
            alertDialogBuilder.setTitle(titleString);

        // set dialog message
        alertDialogBuilder.setCancelable(Cancellable);
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        if(alertDescription != null)
            alertDialogBuilder.setMessage(alertDescription);

        switch (numberOfButtons) {
            case 1: return addNeutralButton(alertDialogBuilder, neutralButton, buttonListener);

            case 2: return addPositiveNegativeButtons(alertDialogBuilder, positiveButton, negativeButton, buttonListener);

            case 3: return addPositiveNegativeNeutralButtons(alertDialogBuilder, neutralButton, positiveButton, negativeButton, buttonListener);

            default: return null;
        }

    }


    private static Builder addNeutralButton(Builder alertDialogBuilder,	String neutralButton, OnClickListener neutralDialogInterfaceListener) {
        alertDialogBuilder.setNeutralButton(neutralButton, neutralDialogInterfaceListener);

        return alertDialogBuilder;
    }


    private static Builder addPositiveNegativeButtons(Builder alertDialogBuilder, String positiveButton, String negativeButton, OnClickListener buttonListener) {

        alertDialogBuilder.setPositiveButton(positiveButton, buttonListener);
        alertDialogBuilder.setNegativeButton(negativeButton, buttonListener);

        return alertDialogBuilder;
    }

    private static Builder addPositiveNegativeNeutralButtons(Builder alertDialogBuilder, String neutralButton, String positiveButton, String negativeButton, OnClickListener buttonListener) {

        alertDialogBuilder.setPositiveButton(positiveButton, buttonListener);
        alertDialogBuilder.setNegativeButton(negativeButton, buttonListener);
        alertDialogBuilder.setNeutralButton(neutralButton, buttonListener);

        return alertDialogBuilder;
    }


    /**
     * This function will show the alert with user preferences of title and message with icon of alert.
     * @param context
     * @param title
     * @param alertMsg
     * @param icon
     */
    public static void showAlert(Context context, String title, String alertMsg,String buttonText, int icon)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(alertMsg);
        alertDialog.setIcon(icon);
        alertDialog.setCancelable(false);
        alertDialog.setButton(-2, buttonText,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }});
        alertDialog.show();
        TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        alertDialog = null;
    }

    /**
     * This function check if the device is connected to any network connection or not.
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * This method will generate a new notification model with specified parameters
     * @param context
     * @param notificationName
     * @param request
     * @param response
     * @return
     */
    public static NotificationModel getNotificationModel(Context context, String notificationName, Object request, Object response)
    {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.context = context;
        notificationModel.notificationName = notificationName;
        notificationModel.requestModel = request;
        notificationModel.responseModel = response;

        return notificationModel;
    }
}
