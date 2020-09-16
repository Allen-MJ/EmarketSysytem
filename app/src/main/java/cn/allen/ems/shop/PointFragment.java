package cn.allen.ems.shop;

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
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.OrderAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Order;

public class PointFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int page = 0;
    private int pagesize = 10;
    private int uid;
    private ActivityHelper actHelper;
    private List<Order> list,sublist;
    private OrderAdapter adapter;

    public static PointFragment init() {
        PointFragment fragment = new PointFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_point, container, false);
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

    private void initUI(View view) {
        adapter = new OrderAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
        loadData();
    }

    private void addEvent(View view){
        mater.setMaterialRefreshListener(materListener);
        adapter.setOnItemClickListener(listener);
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

    private OrderAdapter.OnItemClickListener listener = new OrderAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Order entry) {

        }
    };

    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getExchange(page++,pagesize).getList();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    if(isRefresh){
                        list = sublist;
                        mater.finishRefresh();
                    }else{
                        if(page==1){
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
