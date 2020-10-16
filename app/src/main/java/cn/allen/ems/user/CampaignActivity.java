package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import cn.allen.ems.adapter.CampaignAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Campaign;
import cn.allen.ems.entry.Data;
import cn.allen.ems.entry.Order;
import cn.allen.ems.utils.Constants;

public class CampaignActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.campaign_code)
    AppCompatTextView campaignCode;
    @BindView(R.id.campaign_copy)
    AppCompatTextView campaignCopy;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.campaign_person)
    AppCompatTextView campaignPerson;
    @BindView(R.id.campaign_reward)
    AppCompatTextView campaignReward;
    @BindView(R.id.mater)
    MaterialRefreshLayout mater;
    private SharedPreferences shared;
    private String campaign;
    private CampaignAdapter adapter;

    private boolean isRefresh = false;
    private int page = 0;
    private int pagesize = 20;
    private int uid;
    private List<Campaign> list, sublist;
    private int countPerson,countScore;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_campaign;
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
        campaign = shared.getString(Constants.User_Invitation, "");
        campaignCode.setText(campaign);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new CampaignAdapter();
        rv.setAdapter(adapter);
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
    }

    private MaterialRefreshListener materListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = true;
            page = 0;
            loadData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = false;
            loadData();
        }
    };

    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Data<Campaign> data = WebHelper.init().getMySpread(uid,page++,pagesize);
                sublist = data.getList();
                countPerson = data.getCount();
                countScore = data.getCount2();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @OnClick(R.id.campaign_copy)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.campaign_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", campaign);
                cm.setPrimaryClip(mClipData);
                MsgUtils.showShortToast(context, "复制成功!");
                break;
        }
        v.setEnabled(true);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    campaignPerson.setText(String.valueOf(countPerson));
                    campaignReward.setText(String.valueOf(countScore));
                    if (isRefresh) {
                        list = sublist;
                        mater.finishRefresh();
                    } else {
                        if (page == 1) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        mater.finishRefreshLoadMore();
                    }
                    actHelper.setCanLoadMore(mater, pagesize, list);
                    adapter.setList(list);
                    break;
            }
        }
    };

}
