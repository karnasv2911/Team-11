package com.ichangemycity.ichangemycommunity.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSurveyCollection {

    public Map<String, List<String>> getCategoryMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Walkability", getSubcategories("Walkability"));
        map.put("Visibility", getSubcategories("Visibility"));
        map.put("Drivability", getSubcategories("Drivability"));
        return map;
    }

    private List<String> getSubcategories(String key) {
        List<String> subCategories = new ArrayList<>();
        subCategories.add("Obstacles");
        switch (key) {
            case "Walkability":
                subCategories.add("Broken footpath");
                subCategories.add("Encroachment");
                break;
            case "Visibility":
                subCategories.add("Luminosity");
                break;
            case "Drivability":
                subCategories.add("Pot holes");
                subCategories.add("Speed breakers");
                break;
        }
        return subCategories;
    }

    public List<Survey> getInitialSurveyMap() {
        List<Survey> surveyList = new ArrayList<>();
        surveyList.add(new Survey("001", 1, "Bangalore", "Kariyammana Agrahara, Bellandur", new LatLng(12.9360, 77.6808), "Walkability", "Broken footpath", "60", "start", "end"));
        surveyList.add(new Survey("002", 2, "Bangalore", "RMZ Eco Space, Bellandur", new LatLng(12.9275, 77.6810), "Walkability", "Obstacles", "40", "start", "end"));
        surveyList.add(new Survey("003", 3, "Bangalore", "RGA Tech Park, Bellandur", new LatLng(12.9009, 77.7072), "Visibility", "Luminosity", "50", "start", "end"));
        surveyList.add(new Survey("004", 4, "Bangalore", "Carmelmaram Railway Station, Bellandur", new LatLng(12.9072, 77.7054), "Visibility", "Obstacles", "50", "start", "end"));
        surveyList.add(new Survey("005", 5, "Bangalore", "BTM Layout 2nd Stage", new LatLng(12.9166, 77.6101), "Walkability", "Encroachment", "30", "start", "end"));
        surveyList.add(new Survey("006", 6, "Bangalore", "Kadubeesanahalli", new LatLng(12.9394, 77.6952), "Drivability", "Speed breakers", "60", "start", "end"));
        surveyList.add(new Survey("007", 7, "Bangalore", "New Horizon Gurukul, Kadubeesanahalli", new LatLng(12.9340, 77.6977), "Walkability", "Obstacles", "60", "start", "end"));
        surveyList.add(new Survey("008", 8, "Bangalore", "BTM Layout 2nd Stage", new LatLng(12.9083, 77.6051), "Drivability", "Obstacles", "60", "start", "end"));
        surveyList.add(new Survey("009", 9, "Bangalore", "Gear School, Bellandur", new LatLng(12.9183, 77.6976), "Drivability", "Potholes", "60", "start", "end"));
        return surveyList;
    }
}
