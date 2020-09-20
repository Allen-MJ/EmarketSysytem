package cn.allen.ems;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import allen.frame.AllenIMBaseActivity;
import allen.frame.tools.CheckUtils;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.data.WebHelper;

public class RegistActivity extends AllenIMBaseActivity {
    @BindView(R.id.regist_phone)
    AppCompatEditText registPhone;
    @BindView(R.id.regist_psw)
    AppCompatEditText registPsw;
    @BindView(R.id.regist_nick)
    AppCompatEditText registNick;
    @BindView(R.id.regist_invitation)
    AppCompatEditText registInvitation;
    @BindView(R.id.regist_bt)
    AppCompatButton registBt;

    private String phone,psw,username,invitationcode;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void addEvent() {

    }

    private boolean checkIsOk(){
        phone = registPhone.getText().toString().trim();
        username = registNick.getText().toString().trim();
        psw = registPsw.getText().toString().trim();
        invitationcode = registInvitation.getText().toString().trim();
        if(!CheckUtils.phoneIsOk(phone)){
            MsgUtils.showMDMessage(context,"请输入正确的手机号!");
            return false;
        }
        if(StringUtils.empty(psw)){
            MsgUtils.showMDMessage(context,"请输入用户密码!");
            return false;
        }
//        if(!CheckUtils.passWordIsNotEasy(psw)){
//            MsgUtils.showMDMessage(context,"用户密码过于简单!");
//            return false;
//        }
        if(StringUtils.empty(username)){
            MsgUtils.showMDMessage(context,"请输入用户昵称!");
            return false;
        }
        /*if(StringUtils.empty(invitationcode)){
            MsgUtils.showMDMessage(context,"请输入邀请码!");
            return false;
        }*/
        return true;
    }

    @OnClick(R.id.regist_bt)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.regist_bt:
                if(checkIsOk()){
                    showProgressDialog("正在注册!请稍候...");
                    regist();
                }
                break;
        }
        v.setEnabled(true);
    }

    private void regist(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().register(handler,phone,psw,username,invitationcode);
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            finish();
                        }
                    },false);
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
