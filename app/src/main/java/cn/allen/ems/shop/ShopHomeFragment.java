package cn.allen.ems.shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.widget.ContrlScrollViewPager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.FragmentAdapter;
import cn.allen.ems.utils.Constants;

public class ShopHomeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.pager)
    ContrlScrollViewPager pager;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int uid;
    private ActivityHelper actHelper;
    private String[] titles = new String[]{"商家订单区","兑换区"};
    FragmentAdapter adapter;
    private List<Fragment> list;

    public static ShopHomeFragment init() {
        ShopHomeFragment fragment = new ShopHomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_shop, container, false);
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
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        list = new ArrayList<>();
        list.add(OrderFragment.init());
        list.add(PointFragment.init());
        adapter = new FragmentAdapter(getChildFragmentManager(),list,titles);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
    }

    private void addEvent(View view) {

    }
}
