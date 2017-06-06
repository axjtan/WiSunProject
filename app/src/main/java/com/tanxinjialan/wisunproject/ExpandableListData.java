package com.tanxinjialan.wisunproject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by snss-snsppo-sat on 6/6/17.
 */

public class ExpandableListData {
    public static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> central = new ArrayList<String>();
        central.add("Golden Jasmine, 152B Bishan St 11, #11-271, S572152");
        //central.add("152B Bishan St 11");

        List<String> north = new ArrayList<String>();

        List<String> south = new ArrayList<String>();
        south.add("Skyville @ Dawson, 88 Dawson Rd, #06-41, S142088");
        //south.add("88 Dawson Road Singapore");

        List<String> east = new ArrayList<String>();

        List<String> west = new ArrayList<String>();
        west.add("Clementi Cascadia, 440C Clementi Ave 3, #06-32, S123440");
        //west.add("440C Clementi Ave 3");

        expandableListDetail.put("Central District", central);
        expandableListDetail.put("North District", north);
        expandableListDetail.put("South District", south);
        expandableListDetail.put("East District", east);
        expandableListDetail.put("West District", west);
        return expandableListDetail;
    }
}
