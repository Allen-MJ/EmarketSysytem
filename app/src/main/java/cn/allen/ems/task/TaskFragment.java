package cn.allen.ems.task;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.TaskAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Task;
import cn.allen.ems.utils.Constants;

public class TaskFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.user_change)
    AppCompatTextView userChange;
    @BindView(R.id.user_gold)
    AppCompatTextView userGold;
    @BindView(R.id.user_diamond)
    AppCompatTextView userDiamond;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private List<Task> list;
    private int uid;
    private TaskAdapter adapter;
    private ActivityHelper actHelper;

    public static TaskFragment init() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        actHelper = new ActivityHelper(getActivity(), v);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        addEvent(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 11) {
                userChange.setText("" + shared.getFloat(Constants.User_ChangeScore, 0f));
                userGold.setText("" + shared.getFloat(Constants.User_Gold, 0f));
                userDiamond.setText("" + shared.getFloat(Constants.User_Diamond, 0f));
                actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
                loadData();
            }
        }
    }

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        userChange.setText("" + shared.getFloat(Constants.User_ChangeScore, 0f));
        userGold.setText("" + shared.getFloat(Constants.User_Gold, 0f));
        userDiamond.setText("" + shared.getFloat(Constants.User_Diamond, 0f));
        mater.setLoadMore(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new TaskAdapter();
        rv.setAdapter(adapter);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private void addEvent(View view) {
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

    private TaskAdapter.OnItemClickListener listener = new TaskAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Task entry) {

        }

        @Override
        public void getClick(View v, Task entry) {
            if (entry.getSeencount() >= entry.getWatchcount()) {
                MsgUtils.showMDMessage(getContext(), "您今天该任务已经达到上限！");
            } else {
                startActivityForResult(new Intent(getActivity(), WatchActivity.class).putExtra(Constants.Entry_Flag, entry.getTaskid()), 11);
            }
        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getTaskList(uid);
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
                case 1:
                    userChange.setText("" + shared.getFloat(Constants.User_ChangeScore, 0f));
                    userGold.setText("" + shared.getFloat(Constants.User_Gold, 0f));
                    userDiamond.setText("" + shared.getFloat(Constants.User_Diamond, 0f));
                    break;
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("update")) {
                handler.sendEmptyMessage(1);
            }
        }

    };
}
