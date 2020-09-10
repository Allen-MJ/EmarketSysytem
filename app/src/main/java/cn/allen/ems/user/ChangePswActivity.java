package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.CheckUtils;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.utils.Constants;

public class ChangePswActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.old_psw)
    AppCompatEditText oldPsw;
    @BindView(R.id.new_psw)
    AppCompatEditText newPsw;
    @BindView(R.id.commit_bt)
    AppCompatButton commitBt;
    private SharedPreferences shared;
    private int uid;
    private String oldpsw,newpsw;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_change_psw;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id,-1);
    }

    @Override
    protected void addEvent() {
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                finish();
                view.setEnabled(true);
            }
        });
    }

    @OnClick(R.id.commit_bt)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.commit_bt:
                if(checkIsOk()){
                    changePsw();
                }
                break;
        }
        view.setEnabled(true);
    }

    private boolean checkIsOk(){
        oldpsw = oldPsw.getText().toString().trim();
        newpsw = newPsw.getText().toString().trim();
        if(StringUtils.empty(oldpsw)){
            MsgUtils.showMDMessage(context,"请输入原密码!");
            return false;
        }
        if(StringUtils.empty(newpsw)){
            MsgUtils.showMDMessage(context,"请输入新密码!");
            return false;
        }
        if(!CheckUtils.passWordIsNotEasy(newpsw)){
            MsgUtils.showMDMessage(context,"请输入大小写字母数字字符任意3种组合密码!");
            return false;
        }
        return true;
    }

    private void changePsw(){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().editPassword(handler,uid,oldpsw,newpsw);
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
