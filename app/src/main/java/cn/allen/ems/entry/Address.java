package cn.allen.ems.entry;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private String userid;
    private String recipiment;
    private String telphone;
    private String area;
    private String city;
    private String county;
    private String street;
    private String detailaddress;
    private boolean type;
    private String createtime;

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", recipiment='" + recipiment + '\'' +
                ", telphone='" + telphone + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", street='" + street + '\'' +
                ", detailaddress='" + detailaddress + '\'' +
                ", type=" + type +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRecipiment() {
        return recipiment;
    }

    public void setRecipiment(String recipiment) {
        this.recipiment = recipiment;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
