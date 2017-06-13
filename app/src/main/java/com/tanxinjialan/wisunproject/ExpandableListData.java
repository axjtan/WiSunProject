package com.tanxinjialan.wisunproject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by snss-snsppo-sat on 6/6/17.
 */

class ExpandableListData {
    private static List<String> central = new ArrayList<String>();
    private static List<String> north = new ArrayList<String>();
    private static List<String> south = new ArrayList<String>();
    private static List<String> east = new ArrayList<String>();
    private static List<String> west = new ArrayList<String>();

    public static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        expandableListDetail.put("Central District", central);
        expandableListDetail.put("North District", north);
        expandableListDetail.put("South District", south);
        expandableListDetail.put("East District", east);
        expandableListDetail.put("West District", west);
        return expandableListDetail;
    }

    public static void addData(String district, String block_name, String address, String unit_no, String postal_code) {
        //Log.i("Test",district);
        String full_address = block_name + ", " + address + ", " + unit_no + ", " + postal_code;

        switch (district) {
            case "Central":
                central.add(full_address);
                break;
            case "North":
                north.add(full_address);
                break;
            case "South":
                south.add(full_address);
                break;
            case "East":
                east.add(full_address);
                break;
            case "West":
                west.add(full_address);
                break;
            default:
                break;

        }
    }

    public static void removeData(String district, int childPosition) {
        switch (district) {
            case "Central":
                central.remove(childPosition);
                break;
            case "North":
                north.remove(childPosition);
                break;
            case "South":
                south.remove(childPosition);
                break;
            case "East":
                east.remove(childPosition);
                break;
            case "West":
                west.remove(childPosition);
                break;
            default:
                break;

        }

    }

}
