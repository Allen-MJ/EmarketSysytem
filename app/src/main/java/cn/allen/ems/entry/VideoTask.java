package cn.allen.ems.entry;

import java.io.Serializable;

public class VideoTask implements Serializable {
    private int videoid;
    private String videoname;
    private String videourl;
    private String city;
    private int videotype;
    private int watchtime;
    private String createtime;
    private String effectivetime;

    public VideoTask() {
    }

    @Override
    public String toString() {
        return "VideoTask{" +
                "videoid=" + videoid +
                ", videoname='" + videoname + '\'' +
                ", videourl='" + videourl + '\'' +
                ", city='" + city + '\'' +
                ", videotype=" + videotype +
                ", watchtime=" + watchtime +
                ", createtime='" + createtime + '\'' +
                ", effectivetime='" + effectivetime + '\'' +
                '}';
    }

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getVideotype() {
        return videotype;
    }

    public void setVideotype(int videotype) {
        this.videotype = videotype;
    }

    public int getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(int watchtime) {
        this.watchtime = watchtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEffectivetime() {
        return effectivetime;
    }

    public void setEffectivetime(String effectivetime) {
        this.effectivetime = effectivetime;
    }
}
