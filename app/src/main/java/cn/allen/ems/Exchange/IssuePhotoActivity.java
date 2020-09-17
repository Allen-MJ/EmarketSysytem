package cn.allen.ems.Exchange;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.utils.Constants;

public class IssuePhotoActivity extends AllenBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_des)
    AppCompatEditText etDes;
    @BindView(R.id.issue)
    CardView issue;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.regist_bt)
    AppCompatButton registBt;

    private String des="";
    private SharedPreferences shared;
    private int uid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    dismissProgressDialog();
                    setResult(RESULT_OK);
                    finish();
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_issue_photo;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id,-1);
    }

    @Override
    protected void addEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                finish();
                view.setEnabled(true);
            }
        });
    }
    private boolean checkIsOk(){
        des = etDes.getText().toString().trim();
        if(StringUtils.empty(des)){
            MsgUtils.showMDMessage(context,"请输入描述文字!");
            return false;
        }
        return true;
    }
    @OnClick({R.id.issue, R.id.regist_bt})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.issue:
                break;
            case R.id.regist_bt:
                if(checkIsOk()){
                    submit();
                }
                break;
        }
        view.setEnabled(true);
    }

    private void submit(){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().putshowPhoto(handler,uid,des,"");
            }
        }).start();
    }

}