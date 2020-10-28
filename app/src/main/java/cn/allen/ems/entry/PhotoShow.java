package cn.allen.ems.entry;

import java.io.Serializable;

public class PhotoShow implements Serializable {
    private int showid;
    private String showcontent;
    private String showpicurl;
    private String thumbnail;
    private int showtype;
    private int userid;
    private String createtime;
    private int auditstatus;

    public PhotoShow() {
    }

    @Override
    public String toString() {
        return "PhotoShow{" +
                "showid=" + showid +
                ", showcontent='" + showcontent + '\'' +
                ", showpicurl='" + showpicurl + '\'' +
                ", showtype=" + showtype +
                ", userid=" + userid +
                ", createtime='" + createtime + '\'' +
                ", auditstatus=" + auditstatus +
                '}';
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getShowid() {
        return showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
    }

    public String getShowcontent() {
        return showcontent;
    }

    public void setShowcontent(String showcontent) {
        this.showcontent = showcontent;
    }

    public String getShowpicurl() {
        return showpicurl;
    }

    public void setShowpicurl(String showpicurl) {
        this.showpicurl = showpicurl;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(int auditstatus) {
        this.auditstatus = auditstatus;
    }
}
