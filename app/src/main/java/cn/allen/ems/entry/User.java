package cn.allen.ems.entry;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String userName;
    private String realName;
    private String cardNo;
    private String passWord;
    private String headImageUrl;
    private int sex;
    private String openid;
    private String phone;
    private String city;
    private int enable;
    private int logincount;
    private int createUserId;
    private String createTime;
    private String editTime;
    private String grade;
    private float empiricalvalue;
    private String invitationcode;
    private String invitationcoded;
    private float currency1;
    private float currency2;
    private float currency3;
    private int authentication;
    private String idcardurl1;
    private String idcardurl2;
    private String authenticationdescribe;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", passWord='" + passWord + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", sex=" + sex +
                ", openid='" + openid + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", enable=" + enable +
                ", logincount=" + logincount +
                ", createUserId=" + createUserId +
                ", createTime='" + createTime + '\'' +
                ", editTime='" + editTime + '\'' +
                ", grade='" + grade + '\'' +
                ", empiricalvalue=" + empiricalvalue +
                ", invitationcode='" + invitationcode + '\'' +
                ", invitationcoded='" + invitationcoded + '\'' +
                ", currency1=" + currency1 +
                ", currency2=" + currency2 +
                ", currency3=" + currency3 +
                ", authentication=" + authentication +
                ", idcardurl1='" + idcardurl1 + '\'' +
                ", idcardurl2='" + idcardurl2 + '\'' +
                ", authenticationdescribe='" + authenticationdescribe + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getLogincount() {
        return logincount;
    }

    public void setLogincount(int logincount) {
        this.logincount = logincount;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public float getEmpiricalvalue() {
        return empiricalvalue;
    }

    public void setEmpiricalvalue(float empiricalvalue) {
        this.empiricalvalue = empiricalvalue;
    }

    public String getInvitationcode() {
        return invitationcode;
    }

    public void setInvitationcode(String invitationcode) {
        this.invitationcode = invitationcode;
    }

    public String getInvitationcoded() {
        return invitationcoded;
    }

    public void setInvitationcoded(String invitationcoded) {
        this.invitationcoded = invitationcoded;
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

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public String getIdcardurl1() {
        return idcardurl1;
    }

    public void setIdcardurl1(String idcardurl1) {
        this.idcardurl1 = idcardurl1;
    }

    public String getIdcardurl2() {
        return idcardurl2;
    }

    public void setIdcardurl2(String idcardurl2) {
        this.idcardurl2 = idcardurl2;
    }

    public String getAuthenticationdescribe() {
        return authenticationdescribe;
    }

    public void setAuthenticationdescribe(String authenticationdescribe) {
        this.authenticationdescribe = authenticationdescribe;
    }
}
