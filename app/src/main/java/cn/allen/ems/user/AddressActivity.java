package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
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

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_address;
    }

    @Override
    protected void initBar() {
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

        }

        @Override
        public void deleteClick(View v, Address entry) {

        }

        @Override
        public void editClick(View v, Address entry) {

        }

        @Override
        public void initClick(View v, Address entry) {

        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getAddressList(uid);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    if (isRefresh) {
                        mater.finishRefresh();
                    }
                    adapter.setList(list);
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
