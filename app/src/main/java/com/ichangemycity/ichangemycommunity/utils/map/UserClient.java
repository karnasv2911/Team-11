package com.ichangemycity.ichangemycommunity.utils.map;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.model.Survey;
import com.ichangemycity.ichangemycommunity.model.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserClient extends Application {

    private static final String TAG = UserClient.class.getSimpleName();
    private LatLng userLatLng;
    private boolean isInitialBroadcast = true;
    private MapInputContainerEnum mapInputContainerEnum;
    private Map<SurveyDropDownEnum, List<Survey>> surveyMap = new HashMap<>();
    private List<String> surveyDropDownList = new ArrayList<>();
    private Survey survey;
    private User user;

    public MapInputContainerEnum getMapInputContainerEnum() {
        return mapInputContainerEnum;
    }

    // Retrieve user details from db, mocked driver details are fetched below
    public User fetchUser() {
        user = new User("Citizen", true, "Citizen", "citizen@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, null, getCurrentTimeStamp());
        fetchSurveyMap();
        fetchSurveyDropDownList();
        return user;
    }

    private void fetchSurveyMap() {
        List<Survey> category1 = new ArrayList<>();
        category1.add(new Survey("001", 1, "Bangalore", "Kariyammana Agrahara, Bellandur", new LatLng(12.9360, 77.6808), SurveyDropDownEnum.Walkability, "Broken footpath", "60", "20m"));
        category1.add(new Survey("002", 2, "Bangalore", "RMZ Eco Space, Bellandur", new LatLng(12.9275, 77.6810), SurveyDropDownEnum.Walkability, "Obstacles", "40", "35m"));
        category1.add(new Survey("005", 5, "Bangalore", "BTM Layout 2nd Stage", new LatLng(12.9166, 77.6101), SurveyDropDownEnum.Walkability, "Encroachment", "30", "100m"));
        category1.add(new Survey("007", 7, "Bangalore", "New Horizon Gurukul, Kadubeesanahalli", new LatLng(12.9340, 77.6977), SurveyDropDownEnum.Walkability, "Obstacles", "60", "42m"));

        List<Survey> category2 = new ArrayList<>();
        category2.add(new Survey("003", 3, "Bangalore", "RGA Tech Park, Bellandur", new LatLng(12.9009, 77.7072), SurveyDropDownEnum.Visibility, "Luminosity", "50", ""));
        category2.add(new Survey("004", 4, "Bangalore", "Carmelmaram Railway Station, Bellandur", new LatLng(12.9072, 77.7054), SurveyDropDownEnum.Visibility, "Obstacles", "50", ""));

        List<Survey> category3 = new ArrayList<>();
        category3.add(new Survey("006", 6, "Bangalore", "Kadubeesanahalli", new LatLng(12.9394, 77.6952), SurveyDropDownEnum.Drivability, "Speed breakers", "60", ""));
        category3.add(new Survey("008", 8, "Bangalore", "BTM Layout 2nd Stage", new LatLng(12.9083, 77.6051), SurveyDropDownEnum.Drivability, "Obstacles", "60", ""));
        category3.add(new Survey("009", 9, "Bangalore", "Gear School, Bellandur", new LatLng(12.9183, 77.6976), SurveyDropDownEnum.Drivability, "Potholes", "60", ""));

        surveyMap.put(SurveyDropDownEnum.Walkability, category1);
        surveyMap.put(SurveyDropDownEnum.Visibility, category2);
        surveyMap.put(SurveyDropDownEnum.Drivability, category3);
    }

    private void fetchSurveyDropDownList() {
        surveyDropDownList.clear();
        surveyDropDownList.add(0, "View all surveys");
        surveyDropDownList.add(1, SurveyDropDownEnum.Walkability.toString());
        surveyDropDownList.add(2, SurveyDropDownEnum.Visibility.toString());
        surveyDropDownList.add(3, SurveyDropDownEnum.Drivability.toString());
    }

    public User getUserDetails() {
        return user;
    }

    public void setMapInputContainerEnum(MapInputContainerEnum mapInputContainerEnum) {
        this.mapInputContainerEnum = mapInputContainerEnum;
    }

    public String getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sdf.format(timestamp);
    }

    // N/W call Save user location
    public void saveUserLocation(double lat, double lng) {
        fetchUser();
        userLatLng = new LatLng(lat, lng);
        user.setLiveLocation(getLiveAddress(userLatLng));
        user.setTimeStamp(getCurrentTimeStamp());
    }

    private String getLiveAddress(LatLng latLng) {
//        String address = "";
//        Geocoder geocoder = new Geocoder(getApplicationContext());
//        List<Address> addresses = new ArrayList<>();
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//        } catch (IOException e) {
//            Log.d(TAG, "Exception while getting address from LatLng");
//        }
//        if (addresses.size() > 0) {
//            address = addresses.get(0).toString();
//            Log.d(TAG, "address from LatLng: " + address);
//        }
        return getGeoCoder(latLng);
    }

    private Map<String, LatLng> getGeoCoder2() {
        Map<String, LatLng> map = new HashMap<>();
        map.put("Kariyammana Agrahara, Bellandur", new LatLng(12.9360, 77.6808));
        map.put("RMZ Eco Space, Bellandur", new LatLng(12.9275, 77.6810));
        map.put("RGA Tech Park, Bellandur", new LatLng(12.9009, 77.7072));
        map.put("Carmelmaram Railway Station, Bellandur", new LatLng(12.9072, 77.7054));
        map.put("BTM Layout 2nd Stage", new LatLng(12.9166, 77.6101));
        map.put("Kadubeesanahalli", new LatLng(12.9394, 77.6952));
        map.put("New Horizon Gurukul, Kadubeesanahalli", new LatLng(12.9340, 77.6977));
        map.put("BTM Layout 2nd Stage", new LatLng(12.9083, 77.6051));
        map.put("Gear School, Bellandur", new LatLng(12.9183, 77.6976));
        return map;
    }

    private String getGeoCoder(LatLng latLng) {
        Map<LatLng, String> map = new HashMap<>();
        map.put(new LatLng(12.9360, 77.6808), "Kariyammana Agrahara, Bellandur");
        map.put(new LatLng(12.9275, 77.6810), "RMZ Eco Space, Bellandur");
        map.put(new LatLng(12.9009, 77.7072), "RGA Tech Park, Bellandur");
        map.put(new LatLng(12.90733, 77.7058517), "Karmelmaram Railway Station, Bellandur");
        map.put(new LatLng(12.9166, 77.6101), "BTM Layout 2nd Stage");
        map.put(new LatLng(12.9394, 77.6952), "Kadubeesanahalli");
        map.put(new LatLng(12.9340, 77.6977), "New Horizon Gurukul, Kadubeesanahalli");
        map.put(new LatLng(12.9083, 77.6051), "BTM Layout 2nd Stage");
        map.put(new LatLng(12.9183, 77.6976), "Gear School, Bellandur");
        return map.get(latLng);
    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public boolean isInitialLocationBroadcast() {
        return isInitialBroadcast;
    }

    /* Start Driver Trip Summary Screen */
    // N/W call to rate trip
    public void rateTrip(String rating, String comments) {
        // Do Nothing
    }

    public void setInitialLocationBroadcast(boolean flag) {
        isInitialBroadcast = flag;
    }

    public List<String> getSurveyDropDownList() {
        Log.d("", "size: + surveyDropDownList.size()");
        return surveyDropDownList;
    }

    public List<Survey> getSurveyList() {
        List<Survey> surveyList = new ArrayList<>();
        surveyList.addAll(surveyMap.get(SurveyDropDownEnum.Walkability));
        surveyList.addAll(surveyMap.get(SurveyDropDownEnum.Visibility));
        surveyList.addAll(surveyMap.get(SurveyDropDownEnum.Drivability));
        return surveyList;
    }

    public List<Survey> filterSurveyList(String key) {
        if (key.equals("View all surveys")) {
            return getSurveyList();
        }
        return surveyMap.get(SurveyDropDownEnum.valueOf(SurveyDropDownEnum.class, key));
    }

    public void createSurvey() {
        survey = new Survey();
    }

    public void setSurveyCategory(SurveyDropDownEnum category) {
        survey.setCategory(category);
    }

    public void submitSurvey(String subCategory, String start, String end, String qualityScore) {
        survey.setSubCategory(subCategory);
        survey.setQualityScore(qualityScore);
        survey.setDistance("35m");
        surveyMap.get(survey.getCategory()).add(survey);
        Log.d("", "" + surveyMap.size());
    }
}
