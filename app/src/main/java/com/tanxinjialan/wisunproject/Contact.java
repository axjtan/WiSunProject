package com.tanxinjialan.wisunproject;

/**
 * Created by snss-snsppo-sat on 6/6/17.
 */

public class Contact {
    private String time_lodged;
    private String last_update;
    private String status;
    private String id;
    private String contact_no;
    private String type;
    private String block_name;
    private String address;
    private String unit_no;
    private String postal_code;
    private float lat;
    private float lng;
    private int case_no;

    public Contact(int case_no, String time_lodged, String last_update, String status, String id, String contact_no, String type, String block_name, String address, String unit_no, String postal_code, float lat, float lng) {
        this.case_no = case_no;
        this.time_lodged = time_lodged;
        this.last_update = last_update;
        this.status = status;
        this.id = id;
        this.contact_no = contact_no;
        this.type = type;
        this.block_name = block_name;
        this.address = address;
        this.unit_no = unit_no;
        this.postal_code = postal_code;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTime_lodged() {
        return time_lodged;
    }

    public String getLast_update() {
        return last_update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getType() {
        return type;
    }

    public String getBlock_name() {
        return block_name;
    }

    public String getAddress() {
        return address;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public int getCase_no() {
        return case_no;
    }
}
