package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
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

public class UserNickActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.user_nick)
    AppCompatEditText userNick;
    @BindView(R.id.commit_bt)
    AppCompatButton commitBt;
    private SharedPreferences shared;
    private String nick;
    private int uid;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_change_nick;
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
        userNick.setText(shared.getString(Constants.User_Name, ""));
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

    private boolean checkIsOk(){
        nick = userNick.getText().toString().trim();
        if(StringUtils.empty(nick)){
            MsgUtils.showMDMessage(context,"请输入昵称!");
            return false;
        }
        return true;
    }

    private void changeNick(){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().editName(handler,uid,nick);
            }
        }).start();
    }

    @OnClick(R.id.commit_bt)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.commit_bt:
                if(checkIsOk()){
                    changeNick();
                }
                break;
        }
        view.setEnabled(true);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    finish();
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
