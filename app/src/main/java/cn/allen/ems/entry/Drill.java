package cn.allen.ems.entry;

import java.io.Serializable;

public class Drill implements Serializable {
    private int drillid;
    private int userid;
    private String startdrill;
    private int quickentime;
    private int quickencount;
    private String surplustime;
    private int taskid;

    public Drill() {
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getDrillid() {
        return drillid;
    }

    public void setDrillid(int drillid) {
        this.drillid = drillid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getStartdrill() {
        return startdrill;
    }

    public void setStartdrill(String startdrill) {
        this.startdrill = startdrill;
    }

    public int getQuickentime() {
        return quickentime;
    }

    public void setQuickentime(int quickentime) {
        this.quickentime = quickentime;
    }

    public int getQuickencount() {
        return quickencount;
    }

    public void setQuickencount(int quickencount) {
        this.quickencount = quickencount;
    }

    public String getSurplustime() {
        return surplustime;
    }

    public void setSurplustime(String surplustime) {
        this.surplustime = surplustime;
    }

    @Override
    public String toString() {
        return "Drill{" +
                "drillid=" + drillid +
                ", userid=" + userid +
                ", startdrill='" + startdrill + '\'' +
                ", quickentime=" + quickentime +
                ", quickencount=" + quickencount +
                ", surplustime='" + surplustime + '\'' +
                '}';
    }
}
