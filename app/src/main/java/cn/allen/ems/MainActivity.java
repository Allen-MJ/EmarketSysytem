package cn.allen.ems;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.AppDialog;
import allen.frame.tools.DownLoadHelper;
import allen.frame.tools.FileIntent;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.UpdateInterface;
import allen.frame.widget.ContrlScrollViewPager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Version;
import cn.allen.ems.home.HomeFragment;
import cn.allen.ems.show.ExchangeFragment;
import cn.allen.ems.adapter.FragmentAdapter;
import cn.allen.ems.shop.ShopHomeFragment;
import cn.allen.ems.task.TaskFragment;
import cn.allen.ems.user.UserFragment;
import cn.allen.ems.utils.Constants;

public class MainActivity extends AllenBaseActivity {

    @BindView(R.id.pager)
    ContrlScrollViewPager pager;
    @BindView(R.id.bottom)
    BottomNavigationView bottom;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.message)
    AppCompatImageView message;
    @BindView(R.id.city)
    AppCompatTextView city;
    private FragmentAdapter adapter;
    private List<Fragment> list;
    private SharedPreferences shared;
    private int uid;
    private AppDialog dialog;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(shared.getString(Constants.User_Name,""));
        city.setText(shared.getString(Constants.User_City,"重庆"));
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id,-1);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        dialog = new AppDialog(context);
        list = new ArrayList<>();
        list.add(HomeFragment.init());
        list.add(ExchangeFragment.init());
        list.add(TaskFragment.init());
        list.add(ShopHomeFragment.init());
        list.add(UserFragment.init());
        adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
        getVersion();
    }

    @Override
    protected void addEvent() {
        bottom.setOnNavigationItemSelectedListener(listener);
        bottom.setSelectedItemId(R.id.item_home);
        dialog.setUpdateInterface(updateInterface);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    sendBroadcast(new Intent("bannerResume"));
                    pager.setCurrentItem(0);
                    title.setText("");
                    name.setText(shared.getString(Constants.User_Name,""));
                    city.setText(shared.getString(Constants.User_City,"重庆"));
                    name.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    break;
                case R.id.item_show:
                    sendBroadcast(new Intent("bannerPause"));
                    pager.setCurrentItem(1);
                    title.setText(menuItem.getTitle());
                    name.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);

                    break;
                case R.id.item_task:
                    sendBroadcast(new Intent("bannerPause"));
                    pager.setCurrentItem(2);
                    title.setText(menuItem.getTitle());
                    name.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    break;
                case R.id.item_shop:
                    sendBroadcast(new Intent("bannerPause"));
                    pager.setCurrentItem(3);
                    title.setText(menuItem.getTitle());
                    name.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    break;
                case R.id.item_my:
                    sendBroadcast(new Intent("bannerPause"));
                    pager.setCurrentItem(4);
                    title.setText("个人中心");
                    name.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    };

    UpdateInterface updateInterface = new UpdateInterface() {
        public void downLoad(AppDialog dialog) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = DownLoadHelper.getInstall()
                    .downLoadAppFile(dialog, Constants.APPFILE_NAME, map.get("url"));
            handler.sendMessage(msg);
        };
    };

    @OnClick({R.id.message, R.id.city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message:
                startActivity(new Intent(context,NoticeActivity.class));
                break;
            case R.id.city:
                break;
        }
    }

    private HashMap<String,String> map;

    private void getVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                map = WebHelper.init().getVersion();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(map!=null){
                        if(map.isEmpty()){
                        }else if(AllenManager.getInstance().isNewVersion(map.get("version"))){
                            dialog.showNewVersion(map);
                        }
                    }else{
                        MsgUtils.showShortToast(context,"网络连接异常,请稍后再试!");
                    }
                    break;
                case 1:
                    dialog.dismissDownDialog();
                    int result = (Integer) msg.obj;
                    if(result == 0){
                        Uri uri = null;
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", DownLoadHelper.getInstall().getFile());
                            FileIntent.installApk(context, uri);
                        }else{
                            FileIntent.installApk(context, DownLoadHelper.getInstall().getFile());
                        }
                    }else{
                        MsgUtils.showShortToast(context, "下载失败,请稍后再试!");
                    }
                    break;
            }
        }
    };
}