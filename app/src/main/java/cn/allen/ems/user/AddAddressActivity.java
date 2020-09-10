package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import allen.frame.AllenBaseActivity;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;

public class AddAddressActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.address_person)
    AppCompatEditText addressPerson;
    @BindView(R.id.address_phone)
    AppCompatEditText addressPhone;
    @BindView(R.id.address_area)
    AppCompatTextView addressArea;
    @BindView(R.id.address_city)
    AppCompatTextView addressCity;
    @BindView(R.id.address_contry)
    AppCompatTextView addressContry;
    @BindView(R.id.address_info)
    AppCompatEditText addressInfo;
    @BindView(R.id.save)
    AppCompatTextView save;

    private int addressid;
    private String recipiment,telphone,area,city,county,detailaddress;
    private boolean type = false;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
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

    @OnClick({R.id.address_area, R.id.address_city, R.id.address_contry, R.id.save})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.address_area:
                break;
            case R.id.address_city:
                break;
            case R.id.address_contry:
                break;
            case R.id.save:
                if(checkIsOk()){
                    save();
                }
                break;
        }
        view.setEnabled(true);
    }

    private void save(){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().editAddress(handler,addressid,recipiment,telphone,area,city,county,detailaddress,type);
            }
        }).start();
    }

    private boolean checkIsOk(){
        recipiment = addressPerson.getText().toString().trim();
        telphone = addressPhone.getText().toString().trim();
        area = addressArea.getText().toString().trim();
        city = addressCity.getText().toString().trim();
        county = addressContry.getText().toString().trim();
        detailaddress = addressInfo.getText().toString().trim();
        if(StringUtils.empty(recipiment)){
            MsgUtils.showMDMessage(context,"请输入收货人!");
            return false;
        }
        if(StringUtils.empty(telphone)){
            MsgUtils.showMDMessage(context,"请输入手机号!");
            return false;
        }
        if(StringUtils.empty(area)){
            MsgUtils.showMDMessage(context,"请选择地区!");
            return false;
        }
        if(StringUtils.empty(city)){
            MsgUtils.showMDMessage(context,"请选择城市!");
            return false;
        }
        if(StringUtils.empty(county)){
            MsgUtils.showMDMessage(context,"请选择区县!");
            return false;
        }
        if(StringUtils.empty(detailaddress)){
            MsgUtils.showMDMessage(context,"请输入详细地址!");
            return false;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    dismissProgressDialog();
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    setResult(RESULT_OK,getIntent());
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
