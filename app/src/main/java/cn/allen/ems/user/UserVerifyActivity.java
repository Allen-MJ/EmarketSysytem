package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bumptech.glide.Glide;

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
import cn.allen.ems.widget.IDView;

public class UserVerifyActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.verify_name)
    AppCompatEditText verifyName;
    @BindView(R.id.verify_id)
    AppCompatEditText verifyId;
    @BindView(R.id.verify_id_font)
    IDView verifyIdFont;
    @BindView(R.id.verify_id_back)
    IDView verifyIdBack;
    @BindView(R.id.verify_info)
    AppCompatEditText verifyInfo;
    @BindView(R.id.commit_bt)
    AppCompatButton commitBt;
    private String name,idno,idbg1,idbg2,desc;
    private int uid;
    private SharedPreferences shared;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_verify;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared = AllenManager.getInstance().getStoragePreference();
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        uid = shared.getInt(Constants.User_Id,-1);
        Glide.with(context).load(shared.getString(Constants.User_Id_Front_Url,"")).into(verifyIdFont);
        Glide.with(context).load(shared.getString(Constants.User_Id_Back_Url,"")).into(verifyIdBack);
        verifyName.setText(shared.getString(Constants.User_RealName,""));
        verifyId.setText(shared.getString(Constants.User_CardNo,""));
        verifyInfo.setText(shared.getString(Constants.User_Auth_Describe,""));
        if(shared.getInt(Constants.User_Auth,-1)==0){
            commitBt.setVisibility(View.VISIBLE);
        }else if(shared.getInt(Constants.User_Auth,-1)>0&&shared.getInt(Constants.User_Auth,-1)<3){
            if(shared.getInt(Constants.User_Auth,-1)==1){
                MsgUtils.showMDMessage(context,"审核中,等待通知!");
            }else{
                MsgUtils.showMDMessage(context,"已审核!");
            }
            commitBt.setVisibility(View.GONE);
        }else if(shared.getInt(Constants.User_Auth,-1)==3){
            MsgUtils.showMDMessage(context,"审核未通过,请重新上传!");
            commitBt.setVisibility(View.VISIBLE);
        }else{
            MsgUtils.showMDMessage(context,"未知状态,请联系客服!");
            commitBt.setVisibility(View.GONE);
        }
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
        name = verifyName.getText().toString().trim();
        idno = verifyId.getText().toString().trim();
        desc = verifyInfo.getText().toString().trim();
        if(StringUtils.empty(name)){
            MsgUtils.showMDMessage(context,"请输入姓名!");
            return false;
        }
        if(!CheckUtils.IDIsOk(idno)){
            MsgUtils.showMDMessage(context,"请输入合法的身份证号码!");
            return false;
        }
        return true;
    }

    @OnClick({R.id.verify_id_font, R.id.verify_id_back, R.id.commit_bt})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.verify_id_font:
                break;
            case R.id.verify_id_back:
                break;
            case R.id.commit_bt:
                if(checkIsOk()){
                    verify();
                }
                break;
        }
        view.setEnabled(true);
    }

    private void verify(){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().verifiedRealName(handler,uid,name,idno,idbg1,idbg2,desc);
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
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
