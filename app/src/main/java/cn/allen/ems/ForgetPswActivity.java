package cn.allen.ems;

import android.os.Bundle;
import android.view.View;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPswActivity extends AllenIMBaseActivity {
    @BindView(R.id.regist_phone)
    AppCompatEditText registPhone;
    @BindView(R.id.regist_nick)
    AppCompatEditText registNick;
    @BindView(R.id.apply_bt)
    AppCompatButton applyBt;
    @BindView(R.id.back_bt)
    AppCompatButton backBt;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_forgetpsw;
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

    @OnClick({R.id.apply_bt, R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.apply_bt:
                break;
            case R.id.back_bt:
                AllenManager.getInstance().back2Activity(LoginActivity.class);
                break;
        }
    }
}
