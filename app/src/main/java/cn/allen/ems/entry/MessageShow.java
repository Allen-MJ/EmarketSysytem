package cn.allen.ems.entry;

import java.io.Serializable;

public class MessageShow implements Serializable {
    private int showid;
    private String showcontent;
    private String showpicurl;
    private int showtype;
    private int userid;
    private int auditstatus;
    private String createtime;
    /**
     * username : 毛哥
     * thumbnail : http://yx.chilvwz.com/Files/Photos/2020-11/slt20201116143908452.jpg
     */

    private String username;
    private String thumbnail;

    public MessageShow() {
    }

    @Override
    public String toString() {
        return "MessageShow{" +
                "showid=" + showid +
                ", showcontent='" + showcontent + '\'' +
                ", showpicurl='" + showpicurl + '\'' +
                ", showtype=" + showtype +
                ", userid=" + userid +
                ", auditstatus=" + auditstatus +
                ", createtime='" + createtime + '\'' +
                '}';
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

    public int getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(int auditstatus) {
        this.auditstatus = auditstatus;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
