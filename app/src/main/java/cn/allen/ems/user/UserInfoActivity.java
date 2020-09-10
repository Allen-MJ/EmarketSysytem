package cn.allen.ems.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.widget.CircleImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.utils.Constants;

public class UserInfoActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.info_photo)
    CircleImageView infoPhoto;
    @BindView(R.id.info_nick)
    AppCompatTextView infoNick;
    @BindView(R.id.info_phone)
    AppCompatTextView infoPhone;
    @BindView(R.id.info_last_time)
    AppCompatTextView infoLastTime;
    @BindView(R.id.info_regist_time)
    AppCompatTextView infoRegistTime;
    @BindView(R.id.info_psw)
    AppCompatTextView infoPsw;
    private SharedPreferences shared;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user_info;
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
        Glide.with(context).load(shared.getString(Constants.User_HeadImage_Url,"")).into(infoPhoto);
        infoNick.setText(shared.getString(Constants.User_Name,""));
        infoPhone.setText(shared.getString(Constants.User_Phone,""));
        infoLastTime.setText(shared.getString(Constants.User_LastTime,""));;
        infoRegistTime.setText(shared.getString(Constants.User_RegistTime,""));;
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

    @OnClick({R.id.info_nick, R.id.info_phone, R.id.info_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info_nick:
                break;
            case R.id.info_phone:
                break;
            case R.id.info_psw:
                startActivity(new Intent(context,ChangePswActivity.class));
                break;
        }
    }
}
