package cn.allen.ems.entry;

import java.io.Serializable;

public class Notice implements Serializable {
    private int tipid;
    private String title;
    private String tipcontent;
    private String tipstatus;
    private String createtime;

    public Notice() {
    }

    @Override
    public String toString() {
        return "Notice{" +
                "tipid=" + tipid +
                ", title='" + title + '\'' +
                ", tipcontent='" + tipcontent + '\'' +
                ", tipstatus='" + tipstatus + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public int getTipid() {
        return tipid;
    }

    public void setTipid(int tipid) {
        this.tipid = tipid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTipcontent() {
        return tipcontent;
    }

    public void setTipcontent(String tipcontent) {
        this.tipcontent = tipcontent;
    }

    public String getTipstatus() {
        return tipstatus;
    }

    public void setTipstatus(String tipstatus) {
        this.tipstatus = tipstatus;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
