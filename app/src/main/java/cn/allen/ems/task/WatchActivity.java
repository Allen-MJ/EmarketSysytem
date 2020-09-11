package cn.allen.ems.task;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.VideoView;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.VideoTask;
import cn.allen.ems.utils.Constants;

public class WatchActivity extends AllenIMBaseActivity {
    @BindView(R.id.watch_title)
    AppCompatTextView watchTitle;
    @BindView(R.id.watch_dec)
    AppCompatTextView watchDec;
    @BindView(R.id.watch_video)
    VideoView watchVideo;
    @BindView(R.id.close)
    AppCompatImageView close;

    private SharedPreferences shared;
    private int taskid,uid;
    private VideoTask entry;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_watch;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id,-1);
        taskid = getIntent().getIntExtra(Constants.Entry_Flag,-1);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        getVideoInfo();
    }

    @Override
    protected void addEvent() {

    }

    @OnClick(R.id.close)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.close:
                finish();
                break;
        }
    }

    private void getVideoInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                entry = WebHelper.init().GetTaskVideo(handler,taskid);
                if(entry!=null){
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    watchVideo.setVideoURI(Uri.parse(entry.getVideourl()));
                    watchVideo.requestFocus();
                    watchTitle.setText(entry.getVideoname());
                    break;
                case -1:
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
