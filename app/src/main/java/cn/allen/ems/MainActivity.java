package cn.allen.ems;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.widget.ContrlScrollViewPager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.adapter.FragmentAdapter;
import cn.allen.ems.shop.ShopHomeFragment;
import cn.allen.ems.task.TaskFragment;
import cn.allen.ems.user.UserFragment;

public class MainActivity extends AllenBaseActivity {

    @BindView(R.id.pager)
    ContrlScrollViewPager pager;
    @BindView(R.id.bottom)
    BottomNavigationView bottom;
    @BindView(R.id.title)
    AppCompatTextView title;
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
        list.add(ShopHomeFragment.init());
        list.add(UserFragment.init());
        adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
    }

    @Override
    protected void addEvent() {
        bottom.setOnNavigationItemSelectedListener(listener);
        bottom.setSelectedItemId(R.id.item_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    pager.setCurrentItem(0);
                    title.setText("");
                    break;
                case R.id.item_show:
                    pager.setCurrentItem(1);
                    title.setText(menuItem.getTitle());
                    break;
                case R.id.item_task:
                    pager.setCurrentItem(2);
                    title.setText(menuItem.getTitle());
                    break;
                case R.id.item_shop:
                    pager.setCurrentItem(3);
                    title.setText(menuItem.getTitle());
                    break;
                case R.id.item_my:
                    pager.setCurrentItem(4);
                    title.setText("个人中心");
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}