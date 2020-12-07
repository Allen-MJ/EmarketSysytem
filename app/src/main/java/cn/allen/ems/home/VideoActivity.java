package cn.allen.ems.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.baidu.cloud.media.player.IMediaPlayer;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.TimeMeter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.VideoTask;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.widget.BDCloudVideoView;

public class VideoActivity extends AllenIMBaseActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener {
    @BindView(R.id.watch_video)
    RelativeLayout watchVideo;
    @BindView(R.id.time_close)
    AppCompatTextView timeClose;

    private SharedPreferences shared;
    private int taskid, uid;
    private String url;
    //    private String ak = "1efe1b8bbccc4ea18fdc23f8fd6a2c5e";
    private String ak = "a72d997704aa40a287836019304e1a4a";
    private BDCloudVideoView mVV;
//    private TimeMeter meter;

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
        url = getIntent().getStringExtra(Constants.Video_Url);
        Logger.e("taskid", taskid + "");
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
//        if (meter.isWorking()) {
//            meter.pause();
//        }

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
//        if (!meter.isWorking()) {
//            meter.resume();
//        }

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
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
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

    private AudioManager audioManager;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);//取消静音
//        meter = TimeMeter.getInstance();
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
        mVV.setVideoPath(url);
        mVV.start();
    }

    @Override
    protected void addEvent() {
//        meter.setTimerLisener(new TimeMeter.OnTimerLisener() {
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onInTime(long inTime) {
//                timeClose.setText(inTime + "");
//            }
//
//            @Override
//            public void onEnd() {
////                if (taskid != -1) {
////                    watch();
////                }
//                timeClose.setText("关闭");
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @OnClick(R.id.time_close)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_close:
                setResult(RESULT_OK, getIntent());
                finish();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case -1:
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
//            meter.start();
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
//        mVV.start();
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
