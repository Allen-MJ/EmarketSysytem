package cn.allen.ems;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenBaseActivity;
import allen.frame.entry.City;
import allen.frame.tools.ChoiceTypeDialog;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.adapter.NoticeAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Notice;

public class NoticeActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;

    private SharedPreferences shared;
    private boolean isRefresh = false;
    private List<Notice> list,sublist;
    private NoticeAdapter adapter;
    private int page = 1;
    private int pagesize = 10;
    private int uid;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_record;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new NoticeAdapter();
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
            page = 1;
            loadData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = false;
            loadData();
        }
    };

    private NoticeAdapter.OnItemClickListener listener = new NoticeAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Notice entry) {

        }
    };

    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getTipsList(page++,pagesize).getList();
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
                        list = sublist;
                        mater.finishRefresh();
                    } else {
                        if (page == 2) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        mater.finishRefreshLoadMore();
                    }
                    if (list.size() == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "");
                    }
                    actHelper.setCanLoadMore(mater, pagesize, list);
                    adapter.setList(list);
                    break;
            }
        }
    };
}
