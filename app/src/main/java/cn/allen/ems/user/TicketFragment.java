package cn.allen.ems.user;

import android.annotation.SuppressLint;
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
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.TicketAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Order;
import cn.allen.ems.utils.Constants;

public class TicketFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    private SharedPreferences shared;
    private int type,uid;
    private int page = 1;
    private int pagesize = 10;
    private TicketAdapter adapter;
    private List<Order> list,sublist;
    private ActivityHelper actHelper;
    private boolean isRefresh = false;

    public static TicketFragment init(int type) {
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
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
        type = getArguments().getInt("type",0);
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id,-1);
        mater.setLoadMore(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        mater.setMaterialRefreshListener(materListener);
        adapter = new TicketAdapter();
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(listener);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
        loadData();
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

    private TicketAdapter.OnItemClickListener listener = new TicketAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Order entry) {

        }

        @Override
        public void rcClick(View v, Order entry) {

        }

        @Override
        public void exClick(View v, Order entry) {

        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getShopByUid(uid,type,page++,pagesize).getList();
                handler.sendEmptyMessage(1);
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
                    if(isRefresh){
                        list = sublist;
                        mater.finishRefresh();
                    }else{
                        if(page==2){
                            list = sublist;
                        }else{
                            list.addAll(sublist);
                        }
                        mater.finishRefreshLoadMore();
                    }
                    if(list.size()==0){
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL,"");
                    }
                    actHelper.setCanLoadMore(mater,pagesize,list);
                    adapter.setList(list);
                    break;
            }
        }
    };
}
