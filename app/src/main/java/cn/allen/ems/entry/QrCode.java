package cn.allen.ems.entry;

import java.io.Serializable;

public class QrCode implements Serializable {
    private int id;
    private String qrcodename;
    private String qrcodeurl;
    private String effectivetime;
    private String createtime;
    private int sore;

    public QrCode() {
    }

    @Override
    public String toString() {
        return "QrCode{" +
                "id=" + id +
                ", qrcodename='" + qrcodename + '\'' +
                ", qrcodeurl='" + qrcodeurl + '\'' +
                ", effectivetime='" + effectivetime + '\'' +
                ", createtime='" + createtime + '\'' +
                ", sore=" + sore +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrcodename() {
        return qrcodename;
    }

    public void setQrcodename(String qrcodename) {
        this.qrcodename = qrcodename;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getEffectivetime() {
        return effectivetime;
    }

    public void setEffectivetime(String effectivetime) {
        this.effectivetime = effectivetime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getSore() {
        return sore;
    }

    public void setSore(int sore) {
        this.sore = sore;
    }
}
