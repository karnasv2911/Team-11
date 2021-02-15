package com.ichangemycity.ichangemycommunity.model;

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
}
