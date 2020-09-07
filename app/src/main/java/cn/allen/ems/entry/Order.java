package cn.allen.ems.entry;

import java.io.Serializable;

public class Order implements Serializable {
    private int shopid;
    private String shopno;
    private String shopname;
    private String shoppicurl;
    private int shopstock;
    private int shoptype;
    private String usetimestart;
    private String usetimeend;
    private float currency1;
    private float currency2;
    private float currency3;
    private int receivecount;
    private int verificationcount;
    private float denomination;

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "shopid=" + shopid +
                ", shopno='" + shopno + '\'' +
                ", shopname='" + shopname + '\'' +
                ", shoppicurl='" + shoppicurl + '\'' +
                ", shopstock=" + shopstock +
                ", shoptype=" + shoptype +
                ", usetimestart='" + usetimestart + '\'' +
                ", usetimeend='" + usetimeend + '\'' +
                ", currency1=" + currency1 +
                ", currency2=" + currency2 +
                ", currency3=" + currency3 +
                ", receivecount=" + receivecount +
                ", verificationcount=" + verificationcount +
                ", denomination=" + denomination +
                '}';
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoppicurl() {
        return shoppicurl;
    }

    public void setShoppicurl(String shoppicurl) {
        this.shoppicurl = shoppicurl;
    }

    public int getShopstock() {
        return shopstock;
    }

    public void setShopstock(int shopstock) {
        this.shopstock = shopstock;
    }

    public int getShoptype() {
        return shoptype;
    }

    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    public String getUsetimestart() {
        return usetimestart;
    }

    public void setUsetimestart(String usetimestart) {
        this.usetimestart = usetimestart;
    }

    public String getUsetimeend() {
        return usetimeend;
    }

    public void setUsetimeend(String usetimeend) {
        this.usetimeend = usetimeend;
    }

    public float getCurrency1() {
        return currency1;
    }

    public void setCurrency1(float currency1) {
        this.currency1 = currency1;
    }

    public float getCurrency2() {
        return currency2;
    }

    public void setCurrency2(float currency2) {
        this.currency2 = currency2;
    }

    public float getCurrency3() {
        return currency3;
    }

    public void setCurrency3(float currency3) {
        this.currency3 = currency3;
    }

    public int getReceivecount() {
        return receivecount;
    }

    public void setReceivecount(int receivecount) {
        this.receivecount = receivecount;
    }

    public int getVerificationcount() {
        return verificationcount;
    }

    public void setVerificationcount(int verificationcount) {
        this.verificationcount = verificationcount;
    }

    public float getDenomination() {
        return denomination;
    }

    public void setDenomination(float denomination) {
        this.denomination = denomination;
    }
}
