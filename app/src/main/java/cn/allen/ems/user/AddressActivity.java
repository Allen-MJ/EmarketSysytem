package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.adapter.AddressAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Address;
import cn.allen.ems.utils.Constants;

public class AddressActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    @BindView(R.id.add_address)
    AppCompatTextView addAddress;
    private boolean isRefresh = false;
    private List<Address> list;
    private int uid;
    private AddressAdapter adapter;
    private boolean isChoice = false;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_address;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                loadData();
            }
        }
    }

    @Override
    protected void initBar() {
        isChoice = getIntent().getBooleanExtra(Constants.Choice,false);
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uid = AllenManager.getInstance().getStoragePreference().getInt(Constants.User_Id, -1);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        mater.setLoadMore(false);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new AddressAdapter();
        rv.setAdapter(adapter);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
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
        mater.setMaterialRefreshListener(materListener);
        adapter.setOnItemClickListener(listener);
    }

    private MaterialRefreshListener materListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = true;
            loadData();
        }
    };

    private AddressAdapter.OnItemClickListener listener = new AddressAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Address entry) {
            if(isChoice){
                setResult(RESULT_OK,getIntent().putExtra(Constants.Choice,entry));
                finish();
            }
        }

        @Override
        public void deleteClick(View v, Address entry) {
            MsgUtils.showMDMessage(context, "确定删除地址?", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    delete(entry.getId());
                }
            }, "", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }

        @Override
        public void editClick(View v, Address entry) {
            startActivityForResult(new Intent(context,AddAddressActivity.class).putExtra(Constants.Entry_Flag,entry),1);
        }

        @Override
        public void initClick(View v, Address entry) {
            MsgUtils.showMDMessage(context, "确定设置为默认地址?", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    init(entry.getId(),entry);
                }
            }, "", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getAddressList(uid);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void delete(int addressid){
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().deleteAddress(handler,addressid);
            }
        }).start();
    }

    private Address def;
    private void init(int addressid,Address entry){
        def = entry;
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().setDefaultAddress(handler,uid,addressid);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    if (isRefresh) {
                        mater.finishRefresh();
                    }
                    adapter.setList(list);
                    break;
                case 2:
                    dismissProgressDialog();
//                    entry.getArea()+entry.getCity()+entry.getCounty()+entry.getDetailaddress()
                    AllenManager.getInstance().getStoragePreference().edit()
                            .putString(Constants.User_Default_Address_Uname,def.getRecipiment())
                            .putString(Constants.User_Default_Address_Phone,def.getTelphone())
                            .putString(Constants.User_Default_Address_Area,def.getArea())
                            .putString(Constants.User_Default_Address_City,def.getCity())
                            .putString(Constants.User_Default_Address_County,def.getCounty())
                            .putString(Constants.User_Default_Address_Detailaddress,def.getDetailaddress()).apply();
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                    loadData();
                    break;
                case 0:
                    dismissProgressDialog();
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                    loadData();
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };

    @OnClick(R.id.add_address)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.add_address:
                startActivityForResult(new Intent(context,AddAddressActivity.class),1);
                break;
        }
        v.setEnabled(true);
    }
}
