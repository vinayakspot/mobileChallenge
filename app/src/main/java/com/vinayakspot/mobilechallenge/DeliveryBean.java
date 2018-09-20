package com.vinayakspot.mobilechallenge;


public class DeliveryBean {

    public String id;
    public String desc;
    String img;
    String lat;
    String lng;
    String add;

    public DeliveryBean(String id, String desc, String img, String lat, String lng, String add) {
        this.id = id;
        this.desc = desc;
        this.img = img;
        this.lat = lat;
        this.lng = lng;
        this.add = add;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getAdd() {
        return add;
    }
}
