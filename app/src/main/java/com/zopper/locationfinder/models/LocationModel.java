package com.zopper.locationfinder.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ishaan on 4/28/15.
 */
public class LocationModel implements Parcelable{

    public double latitude;
    public double longitude;

    public LocationModel()
    {
        super();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    public LocationModel(Parcel in) {

        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }



}
