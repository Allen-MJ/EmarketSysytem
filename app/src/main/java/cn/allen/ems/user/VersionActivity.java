package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.HashMap;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.AppDialog;
import allen.frame.tools.DownLoadHelper;
import allen.frame.tools.FileIntent;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.UpdateInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.BuildConfig;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Version;
import cn.allen.ems.utils.Constants;

public class VersionActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.version_info)
    AppCompatTextView versionInfo;
    @BindView(R.id.version_no)
    AppCompatTextView versionNo;
    @BindView(R.id.update)
    AppCompatButton update;
    private AppDialog dialog;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_version;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        dialog = new AppDialog(context);
        getVersion();
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
        dialog.setUpdateInterface(updateInterface);
    }

    UpdateInterface updateInterface = new UpdateInterface() {
        public void downLoad(AppDialog dialog) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = DownLoadHelper.getInstall()
                    .downLoadAppFile(dialog, Constants.APPFILE_NAME, map.get("url"));
            handler.sendMessage(msg);
        };
    };

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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if(map!=null&&!map.isEmpty()){
                        versionNo.setText("最新版本：V"+map.get("version"));
                        versionInfo.setText(map.get("info"));
                        update.setVisibility(AllenManager.getInstance().isNewVersion(map.get("version"))?View.VISIBLE:View.GONE);
                    }else{
                        MsgUtils.showMDMessage(context, "数据异常!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        }, false);
                    }
                    break;
                case -1:
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
                        MsgUtils.exitNotOutMDMessage(context, "下载失败,请稍后再试!");
                    }
                    break;
            }
        }
    };

    @OnClick(R.id.update)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.update:
                dialog.showNewVersion(map);
                break;
        }
        view.setEnabled(true);
    }
}
