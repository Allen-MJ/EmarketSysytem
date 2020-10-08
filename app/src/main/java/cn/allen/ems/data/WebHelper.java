package cn.allen.ems.data;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import cn.allen.ems.entry.Address;
import cn.allen.ems.entry.Campaign;
import cn.allen.ems.entry.Data;
import cn.allen.ems.entry.Drill;
import cn.allen.ems.entry.MessageShow;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.Order;
import cn.allen.ems.entry.PhotoShow;
import cn.allen.ems.entry.QrCode;
import cn.allen.ems.entry.Response;
import cn.allen.ems.entry.Task;
import cn.allen.ems.entry.User;
import cn.allen.ems.entry.Vcode;
import cn.allen.ems.entry.VideoTask;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.utils.NullStringEmptyTypeAdapterFactory;

public class WebHelper {

    private WebService service;
    private static WebHelper helper;
    private Gson gson;

    private WebHelper(){
        service = new WebService();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringEmptyTypeAdapterFactory()).create();
    }
    public static WebHelper init(){
        if(helper==null){
            helper = new WebHelper();
        }
        return helper;
    }

    private void saveToLoacal(User user){
        Logger.debug("user",user.toString());
        AllenManager.getInstance().getStoragePreference().edit()
                .putInt(Constants.User_Id,user.getId())
                .putString(Constants.User_CardNo,user.getCardNo())
                .putString(Constants.User_HeadImage_Url,user.getHeadImageUrl())
                .putString(Constants.User_Name,user.getUserName())
                .putString(Constants.User_Phone,user.getPhone())
                .putString(Constants.User_Openid,user.getOpenid())
                .putString(Constants.User_RealName,user.getRealName())
                .putInt(Constants.User_Sex,user.getSex())
                .putInt(Constants.User_LoginCount,user.getLogincount())
//                .putString(Constants.User_PassWord,user.getPassWord())
//                .putString(Constants.User_City,user.getCity())
                .putString(Constants.User_Invitation,user.getInvitationcode())
                .putFloat(Constants.User_ChangeScore,user.getCurrency1())
                .putFloat(Constants.User_Gold,user.getCurrency2())
                .putFloat(Constants.User_Diamond,user.getCurrency3())
                .putFloat(Constants.User_CurEXP,user.getEmpiricalvalue())
                .putFloat(Constants.User_NextEXP,user.getDifferempirical())
                .putString(Constants.User_LastTime,user.getEditTime())
                .putString(Constants.User_RegistTime,user.getCreateTime())
                .putString(Constants.User_Grade,user.getGrade())
                .putString(Constants.User_Id_Front_Url,user.getIdcardurl1())
                .putString(Constants.User_Id_Back_Url,user.getIdcardurl2())
                .putString(Constants.User_Id_Back_Url,user.getIdcardurl2())
                .putString(Constants.User_Auth_Describe,user.getAuthenticationdescribe())
                .putInt(Constants.User_Auth,user.getAuthentication())
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
        Response response = service.getWebservice(Api.Login,objects,Constants.RequestType);
        Message msg = new Message();
        if(response.isSuccess("200")){
            AllenManager.getInstance().getStoragePreference().edit()
                    .putString(Constants.User_PassWord,psw)
                    .apply();
            User user = gson.fromJson(response.getData(),User.class);
            if(user!=null){
                if(user.getEnable()==1){
                    saveToLoacal(user);
                    msg.what = 0;
                    msg.obj = response.getMessage();
                }else{
                    msg.what = -1;
                    msg.obj = "账户已被禁用!";
                }
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
        Response response = service.getWebservice(Api.Register,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.EditUserName,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.SendVCode,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.EditPhone,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.EditPassword,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.GetCustomerServicePhone,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.VerifiedRealName,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.GetAddress,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.AddAddress,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.SetDefaultAddress,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.EditAddress,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.DeleteAddress,objects,Constants.RequestType);
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
    public Data<Notice> getTipsList(int page,int pagesize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pagesize
        };
        Data<Notice> data = new Data<>();
        Response response = service.getWebservice(Api.GetTips,objects,Constants.RequestType);
        if(response.isSuccess("200")){
            try {
                List<Notice> list = new ArrayList<>();
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<Notice>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取推广二维码列表
     * @return
     */
    public List<QrCode> getQrCodeList(){
        Object[] objects = new Object[]{};
        List<QrCode> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetQRCode,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.GetNineGame,objects,Constants.RequestType);
        Logger.e(Api.GetNineGame,response.getData());
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
        Response response = service.getWebservice(Api.SmashEgg,objects,Constants.RequestType);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 10;
        }else if(response.isSuccess("500")){
            msg.what = 11;
            if (response.getMessage().equals("金币不足")){
                try {
                    JSONObject object=new JSONObject(response.getData());
                    int id=object.optInt("taskid");
                    Logger.e("what=11",id+"");
                    msg.arg1=id;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
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
        Response response = service.getWebservice(Api.BeginDrill,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.QuickenDrill,objects,Constants.RequestType);
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

    public Drill drillStatus(Handler handler,int uid){
        Object[] objects = new Object[]{
                "uid",uid
        };
        Response response = service.getWebservice(Api.DrillStatus,objects,Constants.RequestType);
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
                msg.obj = response.getMessage();
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
    public Data<PhotoShow> getshowPhoto(int page,int pageSize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize
        };
        Response response = service.getWebservice(Api.GetshowPhoto,objects,Constants.RequestType);
        List<PhotoShow> list = new ArrayList<>();
        Data<PhotoShow> data = new Data<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<PhotoShow>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * 获取交换区信息
     * @param page 0开始
     * @param pageSize
     * @return
     */
    public Data<MessageShow> getshowMessage(int page, int pageSize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize
        };
        Response response = service.getWebservice(Api.GetshowMessage,objects,Constants.RequestType);
        List<MessageShow> list = new ArrayList<>();
        Data<MessageShow> data = new Data<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<MessageShow>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * 获取我的发布记录
     * @param uid
     * @param page
     * @param pageSize
     * @return
     */
    public Data<MessageShow> getShowMessageByUid(int uid,int page, int pageSize){
        Object[] objects = new Object[]{
                "uid",uid,"page",page,"pagesize",pageSize
        };
        Response response = service.getWebservice(Api.GetShowMessageByUid,objects,Constants.RequestType);
        List<MessageShow> list = new ArrayList<>();
        Data<MessageShow> data = new Data<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<MessageShow>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
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
        Response response = service.getWebservice(Api.PutshowPhoto,objects,Constants.RequestType);
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
        Response response = service.getWebservice(Api.PutshowMessage,objects,Constants.RequestType);
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
     * 获取商家订单区信息
     * @param page
     * @param pageSize
     * @param city
     * @return
     */
    public Data<Order> getMerchantOrder(int page,int pageSize,String city){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize,"city",city
        };
        Response response = service.getWebservice(Api.GetMerchantOrder,objects,Constants.RequestType);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<Order>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取我的商品列表
     * @param uid
     * @param type
     * @return
     */
    public Data<Order> getShopByUid(int uid,int type,int page,int pagesize){
        Object[] objects = new Object[]{
                "uid",uid,"page",page,"pagesize",pagesize
        };
        Response response = service.getWebservice(Api.GetShopByUid,objects,Constants.RequestType);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<Order>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取兑换区商品信息
     * @param page
     * @param pageSize
     * @return
     */
    public Data<Order> getExchange(int page,int pageSize){
        Object[] objects = new Object[]{
                "page",page,"pagesize",pageSize
        };
        Response response = service.getWebservice(Api.GetExchange,objects,Constants.RequestType);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[1].toString(), new TypeToken<List<Order>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 下单
     * @param handler
     * @param uid
     * @param shopid
     */
    public void preOrder(Handler handler,int uid,int shopid){
        Object[] objects = new Object[]{
                "uid",uid,"shopid",shopid
        };
        Response response = service.getWebservice(Api.PreOrder,objects,Constants.RequestType);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 1;
        }else {
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 获取任务列表
     * @param uid
     * @return
     */
    public List<Task> getTaskList(int uid){
        Object[] objects = new Object[]{
                "uid",uid
        };
        List<Task> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetTaskList,objects,Constants.RequestType);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Task>>(){}.getType());
        }
        return list;
    }

    /**
     * 获取视频信息
     * @param handler
     * @param taskid
     * @return
     */
    public VideoTask GetTaskVideo(Handler handler,int taskid){
        Object[] objects = new Object[]{
                "taskid",taskid
        };
        VideoTask task = null;
        Response response = service.getWebservice(Api.GetTaskVideo,objects,Constants.RequestType);
        if(response.isSuccess("200")){
            task = gson.fromJson(response.getData(), VideoTask.class);
        }else{
            Message msg = new Message();
            msg.what = -1;
            msg.obj = response.getMessage();
            handler.sendMessage(msg);
        }
        return task;
    }



    /**
     * 观看视频完成
     * @param handler
     * @param uid
     * @param taskid
     */
    public void seenVideo(Handler handler,int uid,int taskid){
        Object[] objects = new Object[]{
                "uid",uid,"taskid",taskid
        };
        Response response = service.getWebservice(Api.SeenVideo,objects,Constants.RequestType);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 1;
        }else {
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }


    private Object[] getDataFromJson(String json) throws JSONException {
        Object[] data = new Object[2];
        JSONObject object = new JSONObject(json);
        data[0] = object.optInt("m_Item1");
        data[1] = object.getString("m_Item2");
        return data;
    }

    private String[] getData3FromJson(String json) throws JSONException {
        String[] data = new String[2];
        JSONObject object = new JSONObject(json);
        data[0] = object.optString("m_Item1");
        data[1] = object.optString("m_Item2");
        return data;
    }

    /**
     * 获取我的推广列表
     * @param uid
     * @param page
     * @param pageSize
     * @return
     */
    public Data<Campaign> getMySpread(int uid, int page, int pageSize){
        Object[] objects = new Object[]{
                "uid",uid,"page",page,"pagesize",pageSize
        };
        Response response = service.getWebservice(Api.GetMySpread,objects,Constants.RequestType);
        List<Campaign> list = new ArrayList<>();
        Data<Campaign> data = new Data<>();
        if(response.isSuccess("200")){
            try {
                String[] ob = getData3FromJson(response.getData());
                list = gson.fromJson(ob[1], new TypeToken<List<Campaign>>(){}.getType());
                JSONObject object = new JSONObject(ob[0]);
                data.setCount(object.optInt("spreadCount"));
                data.setCount2(object.optInt("totalReward"));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * 发送留言
     * @param handler
     * @param uid
     * @param messageContent
     */
    public String sendWaitMessage(Handler handler,int uid,String messageContent){
        Object[] objects = new Object[]{
                "uid",uid,"messageContent",messageContent
        };
        String mes = "";
        Response response = service.getWebservice(Api.SendWaitMessage,objects,Constants.RequestType);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 1;
            mes = messageContent;
        }else {
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
        return mes;
    }
}
