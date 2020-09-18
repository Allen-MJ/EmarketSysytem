package cn.allen.ems.entry;

import java.io.Serializable;

public class TalkMsg implements Serializable {
    private int uid;
    private String msg;
    private String date;
    private String photo;

    public TalkMsg() {
    }

    public TalkMsg(int uid, String msg, String photo) {
        this.uid = uid;
        this.msg = msg;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "TalkMsg{" +
                "uid=" + uid +
                ", msg='" + msg + '\'' +
                ", date='" + date + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
