package cn.allen.ems;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.utils.Constants;

public class LoginActivity extends AllenIMBaseActivity {
    @BindView(R.id.login_phone)
    AppCompatEditText loginPhone;
    @BindView(R.id.login_psw)
    AppCompatEditText loginPsw;
    @BindView(R.id.login_bt)
    AppCompatButton loginBt;
    @BindView(R.id.regist_bt)
    AppCompatButton registBt;
    @BindView(R.id.forget_psw)
    AppCompatTextView forgetPsw;
    private SharedPreferences shared;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        shared = AllenManager.getInstance().getStoragePreference();
        Logger.init().setDebug(true).setHttp(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        loginPhone.setText(shared.getString(Constants.User_Phone,""));
    }

    @Override
    protected void addEvent() {

    }

    @OnClick({R.id.login_bt, R.id.regist_bt, R.id.forget_psw})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.login_bt:
                if(checkIsOk()){
                    showProgressDialog("");
                    login();
                }
                break;
            case R.id.regist_bt:
                startActivity(new Intent(context,RegistActivity.class));
                break;
            case R.id.forget_psw:
                test();
                break;
        }
        view.setEnabled(true);
    }

    private String phone,psw;
    private boolean checkIsOk(){
        phone = loginPhone.getText().toString().trim();
        psw = loginPsw.getText().toString().trim();
        if(StringUtils.empty(phone)){
            MsgUtils.showMDMessage(context,"请输入用户账号!");
            return false;
        }
        if(StringUtils.empty(psw)){
            MsgUtils.showMDMessage(context,"请输入用户密码!");
            return false;
        }
        return true;
    }

    private void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().login(handler,phone,psw);
            }
        }).start();
    }

    private void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().getCustomerServicePhone(handler);
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
                    startActivity(new Intent(context,MainActivity.class));
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
