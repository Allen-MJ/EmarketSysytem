package cn.allen.ems.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.CommonPopupWindow;
import allen.frame.tools.EncryptUtils;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Address;
import cn.allen.ems.entry.Order;
import cn.allen.ems.user.AddAddressActivity;
import cn.allen.ems.user.AddressActivity;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.utils.URLImageParser;

public class OrderInfoActivity extends AllenBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_title)
    AppCompatTextView orderTitle;
    @BindView(R.id.order_date)
    AppCompatTextView orderDate;
    @BindView(R.id.order_count)
    AppCompatTextView orderCount;
    @BindView(R.id.order_change)
    AppCompatTextView orderChange;
    @BindView(R.id.order_gold)
    AppCompatTextView orderGold;
    @BindView(R.id.order_diamond)
    AppCompatTextView orderDiamond;
    @BindView(R.id.item_layout)
    CardView itemLayout;
    @BindView(R.id.order_redeem)
    AppCompatTextView orderRedeem;
    @BindView(R.id.order_icon)
    AppCompatImageView orderIcon;
    @BindView(R.id.tv_content)
    AppCompatTextView tvContent;

    private SharedPreferences shared;
    private int uid;
    private Order orderEntry;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_info;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        orderEntry = (Order) getIntent().getSerializableExtra("order");
        orderTitle.setText(orderEntry.getShopname());
        orderDate.setText("使用时间:" + orderEntry.getUsetimestart().substring(0, 7).replaceAll("-", ".") + "-" + orderEntry.getUsetimeend().substring(0, 7).replaceAll("-", "."));
        orderCount.setText("库存剩余:" + orderEntry.getShopstock());
        orderChange.setText("" + orderEntry.getCurrency1());
        orderGold.setText("" + orderEntry.getCurrency2());
        orderDiamond.setText("" + orderEntry.getCurrency3());
        Glide.with(context).load(orderEntry.getShoppicurl()).into(orderIcon);
        String content=orderEntry.getContent()==null?"": EncryptUtils.unescape(orderEntry.getContent());
        Logger.e("image:",content);
        tvContent.setText(Html.fromHtml(content, new URLImageParser(this, tvContent), null));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                def = (Address) data.getSerializableExtra(Constants.Choice);
                name.setText(def == null ? (shared.getString(Constants.User_Default_Address_Uname, "")
                        + "  " + shared.getString(Constants.User_Default_Address_Phone, ""))
                        : (def.getRecipiment() + "  " + def.getTelphone()));
                adress.setText(def == null ? (shared.getString(Constants.User_Default_Address_Area, "")
                        + shared.getString(Constants.User_Default_Address_City, "")
                        + shared.getString(Constants.User_Default_Address_County, "")
                        + shared.getString(Constants.User_Default_Address_Detailaddress, ""))
                        : (def.getArea() + def.getCity() + def.getCounty() + def.getDetailaddress()));
            }
        }
    }

    CommonPopupWindow dialog;
    AppCompatTextView name;
    AppCompatTextView adress;
    private Address def;

    @OnClick({R.id.order_redeem})
    public void onViewClicked(View v) {
        dialog = new CommonPopupWindow.Builder(context).setBackGroundLevel(0.5f)
                .setWidthAndHeight(0, 0, true).setOutsideTouchable(true).setView(R.layout.dialog_default_adress)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.dialog_default_adress:
                                name = view.findViewById(R.id.def_name);
                                adress = view.findViewById(R.id.def_adress);
                                name.setText(def == null ? (shared.getString(Constants.User_Default_Address_Uname, "")
                                        + "  " + shared.getString(Constants.User_Default_Address_Phone, ""))
                                        : (def.getRecipiment() + "  " + def.getTelphone()));
                                adress.setText(def == null ? (shared.getString(Constants.User_Default_Address_Area, "")
                                        + shared.getString(Constants.User_Default_Address_City, "")
                                        + shared.getString(Constants.User_Default_Address_County, "")
                                        + shared.getString(Constants.User_Default_Address_Detailaddress, ""))
                                        : (def.getArea() + def.getCity() + def.getCounty() + def.getDetailaddress()));
                                AppCompatTextView add = view.findViewById(R.id.add_address);
                                AppCompatImageView choice = view.findViewById(R.id.set_adress);
                                AppCompatButton ok = view.findViewById(R.id.ok_bt);
                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra(Constants.Choice, true), 101);
                                    }
                                });
                                choice.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivityForResult(new Intent(context, AddressActivity.class).putExtra(Constants.Choice, true), 101);
                                    }
                                });
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        Logger.e("order", orderEntry.getShopname());
                                        actHelper.showProgressDialog("");
                                        perOrder(orderEntry.getShopid());
                                    }
                                });
                                break;
                        }
                    }
                }).create();
        dialog.showAsDropDown(v);
    }

    private void perOrder(int shopId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().preOrder(handler, uid, shopId, def == null ? shared.getString(Constants.User_Default_Address_Uname, "") : def.getRecipiment(), def == null ? shared.getString(Constants.User_Default_Address_Phone, "") : def.getTelphone(), adress.getText().toString());

            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(context, (String) msg.obj);
                    setResult(RESULT_OK);
                    finish();
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}