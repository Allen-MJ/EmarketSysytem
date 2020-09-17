package cn.allen.ems.entry;

import java.io.Serializable;

public class NineGrid implements Serializable {
    private int palacesid;
    private int shopid;
    private float currency;
    private String city;
    private int isclick=0;

    public NineGrid() {
    }

    @Override
    public String toString() {
        return "NineGrid{" +
                "palacesid=" + palacesid +
                ", shopid=" + shopid +
                ", currency=" + currency +
                ", city='" + city + '\'' +
                '}';
    }

    public int getIsclick() {
        return isclick;
    }

    public void setIsclick(int isclick) {
        this.isclick = isclick;
    }

    public int getPalacesid() {
        return palacesid;
    }

    public void setPalacesid(int palacesid) {
        this.palacesid = palacesid;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public float getCurrency() {
        return currency;
    }

    public void setCurrency(float currency) {
        this.currency = currency;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
