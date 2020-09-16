package cn.allen.ems.Exchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.widget.ContrlScrollViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.FragmentAdapter;
import cn.allen.ems.adapter.TaskAdapter;
import cn.allen.ems.entry.Task;
import cn.allen.ems.shop.OrderFragment;
import cn.allen.ems.shop.PointFragment;
import cn.allen.ems.utils.Constants;

public class ExchangeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.pager)
    ContrlScrollViewPager pager;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int uid;
    private ActivityHelper actHelper;
    private String[] titles = new String[]{"照片墙","物品交换区"};
    FragmentAdapter adapter;
    private List<Fragment> list;

    public static ExchangeFragment init() {
        ExchangeFragment fragment = new ExchangeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exchange, container, false);
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
            }
        }
    }

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        list = new ArrayList<>();
        list.add(PhotoWarFragment.init());
        list.add(ExchangeAreaFragment.init());
        adapter = new FragmentAdapter(getChildFragmentManager(),list,titles);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
    }

    private void addEvent(View view) {
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };
}
