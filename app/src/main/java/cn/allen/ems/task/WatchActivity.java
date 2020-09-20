package cn.allen.ems.task;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.cloud.media.player.IMediaPlayer;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.VideoTask;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.widget.BDCloudVideoView;

public class WatchActivity extends AllenIMBaseActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener {
    @BindView(R.id.watch_title)
    AppCompatTextView watchTitle;
    @BindView(R.id.watch_dec)
    AppCompatTextView watchDec;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.watch_video)
    RelativeLayout watchVideo;
    @BindView(R.id.top)
    LinearLayoutCompat top;
    @BindView(R.id.bottom)
    LinearLayoutCompat bottom;

    private SharedPreferences shared;
    private int taskid, uid;
    private VideoTask entry;
    //    private String ak = "1efe1b8bbccc4ea18fdc23f8fd6a2c5e";
    private String ak = "a72d997704aa40a287836019304e1a4a";
    private BDCloudVideoView mVV;
    private boolean isFullScreen = false;
    private boolean isEnd = false;

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
        uid = shared.getInt(Constants.User_Id, -1);
        taskid = getIntent().getIntExtra(Constants.Entry_Flag, -1);
        Logger.e("taskid",taskid+"");
    }

    boolean isPausedByOnPause = false;

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.e("vedio", "onPause");

        // 当home键切出，想暂停视频的话，反注释下面的代码。同时要反注释onResume中的代码
        if (mVV.isPlaying()) {
            isPausedByOnPause = true;
            mVV.pause();
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("vedio", "onResume");

        // 当home键切出，暂停了视频此时想回复的话，反注释下面的代码
        if (isPausedByOnPause) {
            isPausedByOnPause = false;
            mVV.start();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("vedio", "onRestart");
        if (mVV != null) {
            mVV.enterForeground();
        }
    }

    @Override
    protected void onStop() {
        Log.e("vedio", "onStop");
        // enterBackground should be invoke before super.onStop()
        if (mVV != null) {
            mVV.enterBackground();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mVV != null) {
            mVV.stopPlayback(); // 释放播放器资源
            mVV.release(); // 释放播放器资源和显示资源
        }
        mVV = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            isFullScreen = false;
            top.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
        } else {
            isFullScreen = true;
            top.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        /**
         * 设置ak
         */
        BDCloudVideoView.setAK(ak);

        mVV = new BDCloudVideoView(this);
        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
        mVV.setOnBufferingUpdateListener(this);
        mVV.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
        watchVideo.addView(mVV, rllp);
        mVV.setLogEnabled(false);
        getVideoInfo();
    }

    @Override
    protected void addEvent() {

    }

    @Override
    public void onBackPressed() {
        if(isEnd){
            setResult(RESULT_OK,getIntent());
            finish();
        }else{
            MsgUtils.showMDMessage(context, "还未看完视频完成任务!", "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            },"取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.close)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                if(isEnd){
                    setResult(RESULT_OK,getIntent());
                    finish();
                }else{
                    MsgUtils.showMDMessage(context, "还未看完视频完成任务!", "退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    },"取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
                break;
        }
    }

    private void getVideoInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                entry = WebHelper.init().GetTaskVideo(handler, taskid);
                if (entry != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void watch(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().seenVideo(handler,uid,taskid);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    watchTitle.setText(entry.getVideoname());
                    mVV.setVideoPath(entry.getVideourl());
                    mVV.start();
                    break;
                case -1:
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
                case 1:
                    MsgUtils.showMDMessage(context, "任务完成!");
                    isEnd = true;
                    break;
            }
        }
    };

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        isEnd = false;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        watch();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }
}
