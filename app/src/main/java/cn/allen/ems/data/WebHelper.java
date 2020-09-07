package cn.allen.ems.data;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenManager;
import cn.allen.ems.entry.Address;
import cn.allen.ems.entry.Drill;
import cn.allen.ems.entry.MessageShow;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.PhotoShow;
import cn.allen.ems.entry.QrCode;
import cn.allen.ems.entry.Response;
import cn.allen.ems.entry.User;
import cn.allen.ems.entry.Vcode;
import cn.allen.ems.utils.Constants;

public class WebHelper {

    private WebService service;
    private static WebHelper helper;
    private Gson gson;

    private WebHelper(){
        service = new WebService();
        gson = new Gson();
    }
    public static WebHelper init(){
        if(helper==null){
            helper = new WebHelper();
        }
        return helper;
    }

    private void saveToLoacal(User user){
        AllenManager.getInstance().getStoragePreference().edit()
                .putInt(Constants.User_Id,user.getId())
                .putString(Constants.User_CardNo,user.getCardNo())
                .putString(Constants.User_HeadImage_Url,user.getHeadImageUrl())
                .putString(Constants.User_Name,user.getUserName())
                .putString(Constants.User_Phone,user.getPhone())
                .putString(Constants.User_Openid,user.getOpenid())
                .putString(Constants.User_RealName,user.getRealName())
                .putInt(Constants.User_Sex,user.getSex())
                .putString(Constants.User_PassWord,user.getPassWord())
                .putString(Constants.User_City,user.getCity())
                .apply();
    }

    /**
     * 登录
     * @param handler
     * @param name
     * @param psw
     */
    public void login(Handler handler, String name, String psw){
        Object[] objects = new Object[]{
                "loginName",name,"loginPass",psw
        };
        Response response = service.getWebservice(Api.Login,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            User user = gson.fromJson(response.getData(),User.class);
            if(user!=null){
                saveToLoacal(user);
                msg.what = 0;
                msg.obj = response.getMessage();
            }else{
                msg.what = -1;
                msg.obj = "数据异常!";
            }
        }else{
            msg.what = -1;
            msg.obj = response.getMessage();
        }
        handler.sendMessage(msg);
    }

    /**
     * 注册
     * @param handler
     * @param phone
     * @param psw
     * @param username
     * @param invitationcode
     */
    public void register(Handler handler,String phone,String psw,String username,String invitationcode){
        Object[] objects = new Object[]{
                "phone",phone,"loginPass",psw,"username",username,"Invitationcode",invitationcode
        };
        Response response = service.getWebservice(Api.Register,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 修改昵称
     * @param handler
     * @param uid
     * @param nickName
     */
    public void editName(Handler handler,int uid,String nickName){
        Object[] objects = new Object[]{
                "uid",uid,"username",nickName
        };
        Response response = service.getWebservice(Api.EditUserName,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            AllenManager.getInstance().getStoragePreference().edit()
                    .putString(Constants.User_Name,nickName)
                    .apply();
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 发送验证码
     * @param handler
     * @param phone
     */
    public String sendVCode(Handler handler, String phone){
        Object[] objects = new Object[]{
                "phoneNo",phone
        };
        Response response = service.getWebservice(Api.SendVCode,objects,WebService.Post);
        String code="";
        Message msg = new Message();
        if(response.isSuccess("200")){
            Vcode vcode = gson.fromJson(response.getData(),Vcode.class);
            if(vcode!=null){
                msg.what = 0;
                msg.obj = response.getMessage();
                code = vcode.getMessage();
            }else{
                msg.what = -1;
                msg.obj = "数据异常!";
            }
        }else{
            msg.what = -1;
            msg.obj = response.getMessage();
        }
        handler.sendMessage(msg);
        return code;
    }

    /**
     * 修改手机号
     * @param handler
     * @param uid
     * @param phone
     * @param verifycode
     * @param verifyKey
     */
    public void editPhone(Handler handler,int uid, String phone,String verifycode,String verifyKey){
        Object[] objects = new Object[]{
                "uid",uid,"phone",phone,"verifycode",verifycode,"verifyKey",verifyKey
        };
        Response response = service.getWebservice(Api.EditPhone,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            AllenManager.getInstance().getStoragePreference().edit()
                    .putString(Constants.User_Phone,phone)
                    .apply();
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 修改密码
     * @param handler
     * @param uid
     * @param oldpass
     * @param newpass
     */
    public void editPassword(Handler handler,int uid,String oldpass,String newpass){
        Object[] objects = new Object[]{
                "uid",uid,"oldpass",oldpass,"newpass",newpass
        };
        Response response = service.getWebservice(Api.EditPassword,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            AllenManager.getInstance().getStoragePreference().edit()
                    .putString(Constants.User_PassWord,newpass)
                    .apply();
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 获取客户电话
     * @param handler
     * @return
     */
    public String getCustomerServicePhone(Handler handler){
        Object[] objects = new Object[]{};
        Response response = service.getWebservice(Api.GetCustomerServicePhone,objects,WebService.Post);
        Message msg = new Message();
        String phone = "";
        if(response.isSuccess("200")){
            msg.what = 0;
            phone = response.getData();
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
        return phone;
    }

    /**
     * 上传实名认证信息
     * @param handler
     * @param uid
     * @param realname
     * @param cardno
     * @param idcard1Base64 身份证正面照
     * @param idcard2Base64 身份证背面照
     * @param describe
     */
    public void verifiedRealName(Handler handler,int uid,String realname,String cardno,
                                 String idcard1Base64,String idcard2Base64,String describe){
        Object[] objects = new Object[]{
                "uid",uid,"realname",realname,"cardno",cardno,"idcard1Base64",idcard1Base64,
                "idcard2Base64",idcard2Base64,"describe",describe
        };
        Response response = service.getWebservice(Api.VerifiedRealName,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 获取地址列表
     * @param uid
     * @return
     */
    public List<Address> getAddressList(int uid){
        Object[] objects = new Object[]{
                "uid",uid
        };
        List<Address> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetAddress,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Address>>(){}.getType());
        }
        return list;
    }

    /**
     * 添加收货地址
     * @param handler
     * @param uid
     * @param recipiment
     * @param telphone
     * @param area
     * @param city
     * @param county
     * @param detailaddress
     * @param type
     */
    public void addAddress(Handler handler,int uid,String recipiment,String telphone,
                           String area,String city,String county,String detailaddress,boolean type){
        Object[] objects = new Object[]{
                "uid",uid,"recipiment",recipiment,"telphone",telphone,"area",area,
                "city",city,"county",county,"street","","detailaddress",detailaddress,"type",type
        };
        Response response = service.getWebservice(Api.AddAddress,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 设置默认收货地址
     * @param handler
     * @param uid
     * @param addressid
     */
    public void setDefaultAddress(Handler handler,int uid,int addressid){
        Object[] objects = new Object[]{
                "uid",uid,"addressid",addressid
        };
        Response response = service.getWebservice(Api.SetDefaultAddress,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 修改收货地址
     * @param handler
     * @param addressid
     * @param recipiment
     * @param telphone
     * @param area
     * @param city
     * @param county
     * @param detailaddress
     * @param type
     */
    public void editAddress(Handler handler,int addressid,String recipiment,String telphone,
                            String area,String city,String county,String detailaddress,boolean type){
        Object[] objects = new Object[]{
                "addressid",addressid,"recipiment",recipiment,"telphone",telphone,"area",area,
                "city",city,"county",county,"street","","detailaddress",detailaddress,"type",type
        };
        Response response = service.getWebservice(Api.EditAddress,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 删除地址
     * @param handler
     * @param addressid
     */
    public void deleteAddress(Handler handler,int addressid){
        Object[] objects = new Object[]{
                "addressid",addressid
        };
        Response response = service.getWebservice(Api.DeleteAddress,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 获取广告列表
     * @return
     */
    public List<Notice> getTipsList(){
        Object[] objects = new Object[]{};
        List<Notice> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetTips,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Notice>>(){}.getType());
        }
        return list;
    }

    /**
     * 获取推广二维码列表
     * @return
     */
    public List<QrCode> getQrCodeList(){
        Object[] objects = new Object[]{};
        List<QrCode> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetQRCode,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<QrCode>>(){}.getType());
        }
        return list;
    }

    /**
     * 获取九宫格游戏布局
     * @param city
     * @return
     */
    public List<NineGrid> getNineGame(String city){
        Object[] objects = new Object[]{
                "city",city
        };
        List<NineGrid> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetNineGame,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<NineGrid>>(){}.getType());
        }
        return list;
    }

    /**
     * 砸蛋游戏
     * @param handler
     * @param uid
     * @param ngid
     */
    public void smashEgg(Handler handler,int uid,int ngid){
        Object[] objects = new Object[]{
                "uid",uid,"ngid",ngid
        };
        Response response = service.getWebservice(Api.SmashEgg,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 10;
        }else if(response.isSuccess("500")){
            msg.what = 11;
        }else {
            msg.what = -10;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 开始挖钻/查询挖钻情况
     * @param handler
     * @param uid
     * @return
     */
    public Drill beginDrill(Handler handler,int uid){
        Object[] objects = new Object[]{
                "uid",uid
        };
        Response response = service.getWebservice(Api.BeginDrill,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            Drill drill = gson.fromJson(response.getData(),Drill.class);
            if(drill!=null){
                msg.what = 20;
                msg.obj = response.getMessage();
                handler.sendMessage(msg);
                return drill;
            }else{
                msg.what = -20;
                msg.obj = "数据异常!";
                handler.sendMessage(msg);
            }
        }else {
            msg.what = -20;
            msg.obj = response.getMessage();
            handler.sendMessage(msg);
        }
        return null;
    }

    /**
     * 挖钻加速
     * @param handler
     * @param uid
     * @param drillId
     * @param qtime
     * @return
     */
    public Drill quickenDrill(Handler handler,int uid,int drillId,int qtime){
        Object[] objects = new Object[]{
                "uid",uid,"drillId",drillId,"qtime",qtime
        };
        Response response = service.getWebservice(Api.QuickenDrill,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            Drill drill = gson.fromJson(response.getData(),Drill.class);
            if(drill!=null){
                msg.what = 20;
                msg.obj = response.getMessage();
                handler.sendMessage(msg);
                return drill;
            }else{
                msg.what = -20;
                msg.obj = "数据异常!";
                handler.sendMessage(msg);
            }
        }else {
            msg.what = -20;
            msg.obj = response.getMessage();
            handler.sendMessage(msg);
        }
        return null;
    }

    /**
     * 获取照片墙
     * @param page 0开始
     * @param pageSize
     * @return
     */
    public List<PhotoShow> getshowPhoto(int page,int pageSize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize
        };
        List<PhotoShow> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetshowPhoto,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<PhotoShow>>(){}.getType());
        }
        return list;
    }

    /**
     * 获取交换区信息
     * @param page 0开始
     * @param pageSize
     * @return
     */
    public List<MessageShow> getshowMessage(int page, int pageSize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize
        };
        List<MessageShow> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetshowMessage,objects,WebService.Post);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<MessageShow>>(){}.getType());
        }
        return list;
    }

    /**
     * 发布图片墙
     * @param handler
     * @param uid
     * @param content
     * @param photoBase64
     */
    public void putshowPhoto(Handler handler,int uid,String content,String photoBase64){
        Object[] objects = new Object[]{
                "uid",uid,"content",content,"photoBase64",photoBase64
        };
        Response response = service.getWebservice(Api.PutshowPhoto,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else {
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 发布交易物品
     * @param handler
     * @param uid
     * @param content
     */
    public void putshowMessage(Handler handler,int uid,String content){
        Object[] objects = new Object[]{
                "uid",uid,"content",content
        };
        Response response = service.getWebservice(Api.PutshowMessage,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else {
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }




}
