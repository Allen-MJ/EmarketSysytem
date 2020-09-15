package cn.allen.ems.user;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.widget.ContrlScrollViewPager;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.R;
import cn.allen.ems.adapter.FragmentAdapter;

public class UserTicketActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.pager)
    ContrlScrollViewPager pager;

    private String[] titles = new String[]{"未使用","已使用","已过期"};
    FragmentAdapter adapter;
    private List<Fragment> list;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(TicketFragment.init(0));
        list.add(TicketFragment.init(1));
        list.add(TicketFragment.init(2));
        adapter = new FragmentAdapter(getSupportFragmentManager(),list,titles);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
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
    }
}
