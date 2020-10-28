package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;

import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.entry.City;
import allen.frame.tools.ChoiceTypeDialog;
import allen.frame.tools.CityUtil;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.NotificationUtil;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Address;
import cn.allen.ems.utils.Constants;

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
    @BindView(R.id.address_isinit)
    SwitchCompat addressIsinit;

    private SharedPreferences shared;
    private int addressid, uid;
    private String recipiment, telphone, area, city, county, detailaddress;
    private boolean type = false;
    private List<City> first, secound, third;
    private Address entry;

    private boolean isChoice = false;

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
        isChoice = getIntent().getBooleanExtra(Constants.Choice,false);
        entry = (Address) getIntent().getSerializableExtra(Constants.Entry_Flag);
        bar.setTitle(entry==null?getTitle():"编辑地址");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared = AllenManager.getInstance().getStoragePreference();
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        uid = shared.getInt(Constants.User_Id, -1);
        if(entry!=null){
            addressid = entry.getId();
            addressPerson.setText(entry.getRecipiment());
            addressPhone.setText(entry.getTelphone());
            addressArea.setText(entry.getArea());
            addressCity.setText(entry.getCity());
            addressContry.setText(entry.getCounty());
            addressInfo.setText(entry.getDetailaddress());
            addressIsinit.setChecked(entry.isType());
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
        addressIsinit.setOnCheckedChangeListener(changeListener);
    }

    private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            type = isChecked;
        }
    };

    private CityUtil.CallBack callBack = new CityUtil.CallBack() {
        @Override
        public void onResponse(List<City> list) {
            first = list;
            handler.sendEmptyMessage(2);
        }
    };

    @OnClick({R.id.address_area, R.id.address_city, R.id.address_contry, R.id.save})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.address_area:
                showProgressDialog("");
                CityUtil.init().loadData(context,"","").newCall(new CityUtil.CallBack() {
                    @Override
                    public void onResponse(List<City> list) {
                        first = list;
                        handler.sendEmptyMessage(2);
                    }
                });
                break;
            case R.id.address_city:
                int slen = secound == null ? 0 : secound.size();
                Logger.e("debug", "size:" + slen);
                if (slen > 0) {
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(context, new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            third = city.getArea();
                            addressContry.setText("");
                        }
                    });
                    dialog.showCityDialog("请选择城市", addressCity, secound);
                }else{
                    area = addressArea.getText().toString().trim();
                    if(StringUtils.empty(area)){
                        MsgUtils.showMDMessage(context,"请选择地区!");
                        return;
                    }
                    showProgressDialog("");
                    CityUtil.init().loadData(context,area,"").newCall(new CityUtil.CallBack() {
                        @Override
                        public void onResponse(List<City> list) {
                            secound = list;
                            handler.sendEmptyMessage(3);
                        }
                    });
                }
                break;
            case R.id.address_contry:
                int tlen = third == null ? 0 : third.size();
                Logger.e("debug", "size:" + tlen);
                if (tlen > 0) {
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(context, null);
                    dialog.showCityDialog("请选择区县", addressContry, third);
                }else{
                    area = addressArea.getText().toString().trim();
                    city = addressCity.getText().toString().trim();
                    if(StringUtils.empty(area)){
                        MsgUtils.showMDMessage(context,"请选择地区!");
                        return;
                    }
                    if(StringUtils.empty(city)){
                        MsgUtils.showMDMessage(context,"请选择城市!");
                        return;
                    }
                    showProgressDialog("");
                    CityUtil.init().loadData(context,area,city).newCall(new CityUtil.CallBack() {
                        @Override
                        public void onResponse(List<City> list) {
                            third = list;
                            handler.sendEmptyMessage(4);
                        }
                    });
                }
                break;
            case R.id.save:
                if (checkIsOk()) {
                    save();
                }
                break;
        }
        view.setEnabled(true);
    }

    private void save() {
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(entry!=null){
                    WebHelper.init().editAddress(handler,addressid,recipiment,telphone,area,city,county,detailaddress,type);
                }else{
                    WebHelper.init().addAddress(handler, uid, recipiment, telphone, area, city, county, detailaddress, type);
                }
            }
        }).start();
    }

    private boolean checkIsOk() {
        recipiment = addressPerson.getText().toString().trim();
        telphone = addressPhone.getText().toString().trim();
        area = addressArea.getText().toString().trim();
        city = addressCity.getText().toString().trim();
        county = addressContry.getText().toString().trim();
        detailaddress = addressInfo.getText().toString().trim();
        if (StringUtils.empty(recipiment)) {
            MsgUtils.showMDMessage(context, "请输入收货人!");
            return false;
        }
        if (StringUtils.empty(telphone)) {
            MsgUtils.showMDMessage(context, "请输入手机号!");
            return false;
        }
        if (StringUtils.empty(area)) {
            MsgUtils.showMDMessage(context, "请选择地区!");
            return false;
        }
        if (StringUtils.empty(city)) {
            MsgUtils.showMDMessage(context, "请选择城市!");
            return false;
        }
        if (StringUtils.empty(county)) {
            MsgUtils.showMDMessage(context, "请选择区县!");
            return false;
        }
        if (StringUtils.empty(detailaddress)) {
            MsgUtils.showMDMessage(context, "请输入详细地址!");
            return false;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if(type){
                        AllenManager.getInstance().getStoragePreference().edit()
                                .putString(Constants.User_Default_Address_Uname,recipiment)
                                .putString(Constants.User_Default_Address_Phone,telphone)
                                .putString(Constants.User_Default_Address_Area,area)
                                .putString(Constants.User_Default_Address_City,city)
                                .putString(Constants.User_Default_Address_County,county)
                                .putString(Constants.User_Default_Address_Detailaddress,detailaddress).apply();
                    }
                    dismissProgressDialog();
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    if(isChoice){
                        Address def = new Address();
                        def.setRecipiment(recipiment);
                        def.setArea(area);
                        def.setCity(city);
                        def.setCounty(county);
                        def.setDetailaddress(detailaddress);
                        def.setTelphone(telphone);
                        setResult(RESULT_OK, getIntent().putExtra(Constants.Choice,def));
                    }else{
                        setResult(RESULT_OK, getIntent());
                    }
                    finish();
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
                case 2:
                    dismissProgressDialog();
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(context, new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            secound = city.getArea();
                            third = null;
                            addressCity.setText("");
                            addressContry.setText("");
                        }
                    });
                    dialog.showCityDialog("请选择地区", addressArea, first);
                    break;
                case 3:
                    dismissProgressDialog();
                    ChoiceTypeDialog sdialog = new ChoiceTypeDialog(context, new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            third = city.getArea();
                            addressContry.setText("");
                        }
                    });
                    sdialog.showCityDialog("请选择城市", addressCity, secound);
                    break;
                case 4:
                    dismissProgressDialog();
                    ChoiceTypeDialog tdialog = new ChoiceTypeDialog(context, null);
                    tdialog.showCityDialog("请选择区县", addressContry, third);
                    break;
            }
        }
    };
}
