package cn.allen.ems.entry;

import java.io.Serializable;

public class Campaign implements Serializable {
    private String userName;
    private String phone;
    private String createTime;
    private String Reward;

    public Campaign() {
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime='" + createTime + '\'' +
                ", Reward='" + Reward + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReward() {
        return Reward;
    }

    public void setReward(String reward) {
        Reward = reward;
    }
}
