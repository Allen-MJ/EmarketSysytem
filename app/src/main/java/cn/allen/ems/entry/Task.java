package cn.allen.ems.entry;

import java.io.Serializable;

public class Task implements Serializable {
    private int taskid;
    private String taskname;
    private String taskdescribe;
    private int watchcount;
    private String taskpic;
    private String createtime;
    private boolean enable;
    private float currency1;
    private float currency2;
    private float currency3;
    private int quickentime;
    private int shopid;
    private int seencount;

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskid=" + taskid +
                ", taskname='" + taskname + '\'' +
                ", taskdescribe='" + taskdescribe + '\'' +
                ", watchcount=" + watchcount +
                ", taskpic='" + taskpic + '\'' +
                ", createtime='" + createtime + '\'' +
                ", enable=" + enable +
                ", currency1=" + currency1 +
                ", currency2=" + currency2 +
                ", currency3=" + currency3 +
                ", quickentime=" + quickentime +
                ", shopid=" + shopid +
                ", seencount=" + seencount +
                '}';
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskdescribe() {
        return taskdescribe;
    }

    public void setTaskdescribe(String taskdescribe) {
        this.taskdescribe = taskdescribe;
    }

    public int getWatchcount() {
        return watchcount;
    }

    public void setWatchcount(int watchcount) {
        this.watchcount = watchcount;
    }

    public String getTaskpic() {
        return taskpic;
    }

    public void setTaskpic(String taskpic) {
        this.taskpic = taskpic;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public int getQuickentime() {
        return quickentime;
    }

    public void setQuickentime(int quickentime) {
        this.quickentime = quickentime;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public int getSeencount() {
        return seencount;
    }

    public void setSeencount(int seencount) {
        this.seencount = seencount;
    }
}
