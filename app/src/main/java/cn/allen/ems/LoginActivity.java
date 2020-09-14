package cn.allen.ems;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.utils.Constants;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

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
    public static final int REQUEST_CAMERA_PERMISSION = 1003;

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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if(checkIsOk(grantResults)){

                }else{
                    finish();
                }
                break;
            }
        }
    }

    private boolean checkIsOk(int[] grantResults){
        boolean isok = true;
        for(int i:grantResults){
            isok = isok && (i == PackageManager.PERMISSION_GRANTED);
        }
        return isok;
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        loginPhone.setText(shared.getString(Constants.User_Phone,""));
        loginPsw.setText("qwe!123");
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE) !=PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) !=PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.READ_PHONE_STATE}, REQUEST_CAMERA_PERMISSION);
            return;
        }
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
                startActivity(new Intent(context,ForgetPswActivity.class));
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
