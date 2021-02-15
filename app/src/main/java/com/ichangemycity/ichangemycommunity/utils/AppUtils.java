package com.ichangemycity.ichangemycommunity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.ichangemycity.ichangemycommunity.AppConstant;
import com.ichangemycity.ichangemycommunity.AppMain;
import com.ichangemycity.ichangemycommunity.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class AppUtils {

    public static boolean isValidList(List list) {
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    public static boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) AppMain.getAppMainContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Since NetworkInfo is null incase of no current network
        boolean isNetworkConnected = false;
        if (networkInfo != null) {
            isNetworkConnected = networkInfo.isConnected();
        }
        return isNetworkConnected;
    }


    public static String getCurrentDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String currentDate=dateFormat.format(currentTime);
        return currentDate;
    }

    public static String getReadableDate(String dateInString){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");
        try {

            Date date = formatter.parse(dateInString);
            String readableDate=formatterOut.format(date);
            return  readableDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //TODO change this
        return "31 July 2020";
    }

    public static void saveDriverDetails(User user){
        String mobile = user.getPhone();
        SharedPreferenceUtils.putStringValue(AppConstant.PREF_KEY_MOBILE,mobile);
        SharedPreferenceUtils.putStringValue(AppConstant.PREF_KEY_USER_DETAILS,new Gson().toJson(user));
    }

    public static void saveFCMToken(String token) {
        SharedPreferenceUtils.putStringValue(AppConstant.PREF_KEY_DEVICE_ID,token);
    }

}
