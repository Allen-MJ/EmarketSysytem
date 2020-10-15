package cn.allen.ems.entry;

import java.io.Serializable;

public class Version implements Serializable {
    private int versionsid;
    private String versionsno;
    private String versionscontent;
    private String updatetime;
    private String updateurl;
    private int whetherupdates;

    public Version() {
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionsid=" + versionsid +
                ", versionsno='" + versionsno + '\'' +
                ", versionscontent='" + versionscontent + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateurl='" + updateurl + '\'' +
                ", whetherupdates=" + whetherupdates +
                '}';
    }

    public int getVersionsid() {
        return versionsid;
    }

    public void setVersionsid(int versionsid) {
        this.versionsid = versionsid;
    }

    public String getVersionsno() {
        return versionsno;
    }

    public void setVersionsno(String versionsno) {
        this.versionsno = versionsno;
    }

    public String getVersionscontent() {
        return versionscontent;
    }

    public void setVersionscontent(String versionscontent) {
        this.versionscontent = versionscontent;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public int getWhetherupdates() {
        return whetherupdates;
    }

    public void setWhetherupdates(int whetherupdates) {
        this.whetherupdates = whetherupdates;
    }
}
