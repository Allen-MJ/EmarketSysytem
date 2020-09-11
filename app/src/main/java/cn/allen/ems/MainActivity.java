package cn.allen.ems;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.widget.ContrlScrollViewPager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.adapter.FragmentAdapter;
import cn.allen.ems.task.TaskFragment;
import cn.allen.ems.user.UserFragment;

public class MainActivity extends AllenBaseActivity {

    @BindView(R.id.pager)
    ContrlScrollViewPager pager;
    @BindView(R.id.bottom)
    BottomNavigationView bottom;
    private FragmentAdapter adapter;
    private List<Fragment> list;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(UserFragment.init());
        list.add(UserFragment.init());
        list.add(TaskFragment.init());
        list.add(UserFragment.init());
        list.add(UserFragment.init());
        adapter = new FragmentAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(adapter);
    }

    @Override
    protected void addEvent() {

    }

}