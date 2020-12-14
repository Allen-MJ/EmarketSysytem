package cn.allen.ems.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lake.banner.BannerStyle;
import com.lake.banner.HBanner;
import com.lake.banner.ImageGravityType;
import com.lake.banner.Transformer;
import com.lake.banner.VideoGravityType;
import com.lake.banner.listener.OnBannerListener;
import com.lake.banner.loader.VideoLoader;
import com.lake.banner.loader.ViewItemBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.TimeMeter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.NoticeActivity;
import cn.allen.ems.R;
import cn.allen.ems.ShowPicActivity;
import cn.allen.ems.adapter.ShareAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Drill;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.QrCode;
import cn.allen.ems.entry.VideoTask;
import cn.allen.ems.task.WatchActivity;
import cn.allen.ems.user.UserTicketActivity;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.utils.LoadingDialog;

import static com.lake.banner.BannerConfig.VIDEO;

public class HomeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.play)
    AppCompatImageView play;
    @BindView(R.id.notice)
    AppCompatTextView notice;
    @BindView(R.id.game_rv)
    RecyclerView gameRv;
    @BindView(R.id.time)
    AppCompatTextView time;
    @BindView(R.id.score)
    AppCompatImageView score;
    @BindView(R.id.speed)
    AppCompatTextView speed;
    @BindView(R.id.shared_rv)
    RecyclerView sharedRv;
    @BindView(R.id.speed_time)
    AppCompatTextView speedTime;
    @BindView(R.id.banner)
    HBanner banner;
    @BindView(R.id.image_vol)
    ImageView imageVol;
    @BindView(R.id.game_rule)
    AppCompatTextView gameRule;
    private SharedPreferences shared;
    private boolean isFist = false;
    private int uid;
    private ActivityHelper actHelper;
    private List<Notice> list;
    //    private GameAdapter adapter;
    private CommonAdapter<NineGrid> adapter;
    private List<NineGrid> nineGrids;
    private String nineTaskID;
    private String city = "重庆";
    private List<QrCode> qrCodes;
    private ShareAdapter shareAdapter;
    private Drill drill;
    private VideoTask videoTask;
    private List<VideoTask> bannerList;
    private List<ViewItemBean> bannerViewList = new ArrayList<>();
    private int clickPosition = -1;
    private int surplustime;
    private int muin;
    private int seconds;
    private TimeMeter timeMeter = TimeMeter.getInstance();
    private AudioManager audioManager;

    public static HomeFragment init() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        actHelper = new ActivityHelper(getActivity(), v);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("HomeFragment:", "onDestroyView");
        banner.onPause();
        unbinder.unbind();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        addEvent(view);
    }

    @Override
    public void onResume() {
        Log.e("HomeFragment:", "onResume");
        banner.onResume();
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
        super.onResume();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.e("HomeFragment:", "onViewStateRestored");
        banner.onResume();
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onPause() {
        Log.e("HomeFragment:", "onPause");
        banner.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("HomeFragment:", "onStop");
        banner.onStop();
        super.onStop();
    }



    @Override
    public void onDestroy() {
        Logger.e("HomeFragment:", "onDestroy");
        super.onDestroy();
        timeMeter.stop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    public HBanner getBanner() {
        return banner;
    }

    private void initUI(View view) {
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
//        city = shared.getString(Constants.User_City, "重庆");
        initAdapter();
        initBanner();
        registerBoradcastReceiver();

        GridLayoutManager smanager = new GridLayoutManager(getActivity(), 2);
        sharedRv.setLayoutManager(smanager);
        shareAdapter = new ShareAdapter();
        sharedRv.setAdapter(shareAdapter);
        firstEgg();
        getDrillStatus();
        getNiticeTop5();
        getGameFirst();
        getQrCodes();
        getBannerVideo();
    }

    private void initBanner() {
        banner.setViews(bannerViewList)
                .setBannerAnimation(Transformer.Default)//换场方式
                .setBannerStyle(BannerStyle.CIRCLE_INDICATOR_TITLE)//指示器模式
                .setCache(true)//可以不用设置，默认为true
                .setCachePath(getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "hbanner")
                .setVideoGravity(VideoGravityType.CENTER)//视频布局方式
                .setImageGravity(ImageGravityType.FIT_XY)//图片布局方式
                .setPageBackgroundColor(Color.TRANSPARENT)//设置背景
                .setShowTitle(true)//是否显示标题
                .setViewPagerIsScroll(true)//是否支持手滑
                .isAutoPlay(true)//是否自动播放
                .start();
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        gameRv.setLayoutManager(manager);
//        adapter = new GameAdapter();
        adapter = new CommonAdapter<NineGrid>(getContext(), R.layout.item_ninegrid) {
            @Override
            public void convert(ViewHolder holder, NineGrid entity, int position) {
                AppCompatImageView view = holder.getView(R.id.nine_item_icon);
                if (position == clickPosition) {
                    Glide.with(getActivity()).load(R.mipmap.dankai).into(new GlideDrawableImageViewTarget(view, 1));
                } else {
                    Glide.with(getActivity()).load(R.mipmap.ic_logo_42).into(view);
                }

            }
        };
        gameRv.setAdapter(adapter);
    }

    private void addEvent(View view) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                VideoTask video = bannerList.get(position);
//                Intent videoIntent = new Intent(getActivity(), WatchActivity.class);
//                videoIntent.putExtra(Constants.Video_Flag, video);
//                startActivityForResult(videoIntent, 13);
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra(Constants.Video_Url, video.getVideourl());
                startActivityForResult(intent, 11);
            }
        });
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                float glod = shared.getFloat(Constants.User_Gold, 0);
                boolean canEgg = isFist || nineGrids.get(position).getCurrency() <= glod;
                Logger.e("isFist",isFist+"");
                Logger.e("canEgg",canEgg+"");
                if (!canEgg) {
                    showGifdialog(LoadingDialog.Type_IMG, Constants.Nine_No_Gold);
                } else {
                    if (clickPosition == -1) {
                        clickPosition = position;
                        adapter.notifyItemChanged(position);
                        gameRv.setEnabled(false);
                        showGifdialog(LoadingDialog.Type_GIF, "请稍候...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSmashEgg(nineGrids.get(position).getPalacesid());
                            }
                        }, 2000);

                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        shareAdapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, QrCode entry) {
                Intent intent = new Intent(getActivity(), ShowPicActivity.class);
                intent.putExtra("url", entry.getQrcodeurl());
                intent.putExtra("title", entry.getQrcodename());
                startActivity(intent);

            }
        });
    }

    private LoadingDialog dialog;

    public void showGifdialog(String type, String content) {
        if (type.equals(LoadingDialog.Type_GIF)) {
            dialog = new LoadingDialog(getContext(), content, R.mipmap.ic_logo_73, type, handler);
        } else {
            switch (content) {
                case Constants.Nine_Yes:
                    dialog = new LoadingDialog(getContext(), content, R.mipmap.ic_logo_61, type, handler);
                    break;
                case Constants.Nine_No:
                    dialog = new LoadingDialog(getContext(), content, R.mipmap.ic_logo_71, type, handler);
                    break;
                case Constants.Nine_No_Gold:
                    dialog = new LoadingDialog(getContext(), content, R.mipmap.ic_logo_72, type, handler);
                    break;
            }

        }
        dialog.show();
    }

    public void dismissGifDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void getNiticeTop5() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getTipsList(1, 5).getList();
                handler.sendEmptyMessage(100);
            }
        }).start();
    }

    private void getGameFirst() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nineGrids = WebHelper.init().getNineGame(handler, city);
                handler.sendEmptyMessage(102);
            }
        }).start();
    }

    private void getSmashEgg(int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().smashEgg(handler, uid, id);
            }
        }).start();
    }

    private void getQrCodes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                qrCodes = WebHelper.init().getQrCodeList();
                handler.sendEmptyMessage(103);
            }
        }).start();
    }

    private void getDrill() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                drill = WebHelper.init().beginDrill(handler, uid);
            }
        }).start();
    }

    private void getSpeed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                videoTask = WebHelper.init().quickenDrill(handler);
                if (videoTask != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void getBannerVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bannerList = WebHelper.init().bannerVideo(handler);
                int len = bannerList == null ? 0 : bannerList.size();
                if (len > 0) {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }


    /**
     * 今天是否挖钻
     */
    private void getDrillStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                drill = WebHelper.init().drillStatus(handler, uid);
            }
        }).start();
    }

    private void currency() {
        new Thread() {
            @Override
            public void run() {
                WebHelper.init().currency(handler, uid);
            }
        }.start();
    }

    private void firstEgg() {
        new Thread() {
            @Override
            public void run() {
                WebHelper.init().firstEgg(handler, uid);
            }
        }.start();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            index = index + 1;
            handler.sendEmptyMessage(101);
        }
    };


    TimeMeter.OnTimerLisener timeListener = new TimeMeter.OnTimerLisener() {

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
        }

        @SuppressWarnings("static-access")
        @Override
        public void onInTime(long inTime) {
            // TODO Auto-generated method stub
            time.setText(timeMeter.getCountdown(inTime));
            if (surplustime > 0) {
                muin = (int) ((surplustime - inTime) / 60);
                seconds = (int) ((surplustime - inTime) % 60);
            } else {
                muin = (int) (inTime / 60);
                seconds = (int) (inTime % 60);
            }
        }

        @Override
        public void onEnd() {
            Logger.e("onEnd", "onend");
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    getDrill();
                }
                break;
            case 12:
                int pos = clickPosition;
                clickPosition = -1;
                adapter.notifyItemChanged(pos);
                break;
            case 13:

                break;
        }
    }

    private int index = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.dismissProgressDialog();
                    Intent intent = new Intent(getActivity(), WatchActivity.class);
                    intent.putExtra(Constants.Video_Flag, videoTask);
                    intent.putExtra(Constants.Entry_Flag, drill.getTaskid());
                    startActivityForResult(intent, 11);
                    break;
                case 1:
                    actHelper.dismissProgressDialog();
                    for (VideoTask video : bannerList) {
                        bannerViewList.add(new ViewItemBean(VIDEO, video.getVideoname(), video.getVideourl(), 15 * 1000));
                    }
                    banner.update(bannerViewList);
                    break;
                case 120:
                    Intent mIntent = new Intent("update");
                    //发送广播
                    getActivity().sendBroadcast(mIntent);
                    break;
                case 100:
                    if (list.size() > 0) {
                        notice.setText(list.get(index).getTipcontent());
                        postDelayed(runnable, 3000);
                    } else {
                        notice.setText("暂无公告!");
                    }
                    break;
                case 101:
                    notice.setText(list.get(index % list.size()).getTipcontent());
                    postDelayed(runnable, 3000);
                    break;
                case 102:
//                    adapter.setList(nineGrids);
                    actHelper.dismissProgressDialog();
                    gameRv.setEnabled(true);
                    if (clickPosition != -1) {
                        adapter.notifyItemChanged(clickPosition);
                        clickPosition = -1;
                    } else {
                        adapter.setDatas(nineGrids);
                    }
                    break;
                case 103:
                    shareAdapter.setList(qrCodes);
                    break;
                case 104:
                    nineTaskID = (String) msg.obj;
                    Logger.e("taskID", nineTaskID);
                    break;
                case 105:
                    gameRv.setEnabled(true);
                    if (clickPosition != -1) {
                        adapter.notifyItemChanged(clickPosition);
                        clickPosition = -1;
                    }
                    break;
                case 106:
                    if (clickPosition != -1) {
                        adapter.notifyItemChanged(clickPosition);
                        clickPosition = -1;
                    }
                    Intent videoIntent = new Intent(getContext(), WatchActivity.class);
                    videoIntent.putExtra(Constants.Entry_Flag, Integer.valueOf(nineTaskID));
                    startActivityForResult(videoIntent, 12);
                    break;
                case 107:
                    if (clickPosition != -1) {
                        adapter.notifyItemChanged(clickPosition);
                        clickPosition = -1;
                    }
                    startActivity(new Intent(getActivity(), UserTicketActivity.class));
                    break;
                case 20:
                    actHelper.dismissProgressDialog();
                    String timeString = drill.getSurplustime();
                    String[] my = timeString.split(":");
                    int hour = Integer.parseInt(my[0]);
                    int min = Integer.parseInt(my[1]);
                    int sec = Integer.parseInt(my[2]);
                    surplustime = hour * 3600 + min * 60 + sec;
                    if (surplustime == 0) {
                        MsgUtils.showMDMessage(getContext(), "您今天已经完成挖钻任务,24点后再来!");
                        time.setText("完成");
                    } else {
                        timeMeter.start();
                        timeMeter.setMaxTime(surplustime);
                        timeMeter.setTimerLisener(timeListener);
                        speedTime.setText("");
                    }
//                    speed.setText(getString(R.string.game_speed_hint) + "  " + drill.getQuickencount());

                    break;
                case 10:
                    dismissGifDialog();
                    showGifdialog(LoadingDialog.Type_IMG, Constants.Nine_Yes);
                    getGameFirst();
                    if (isFist) {
                        firstEgg();
                    }

                    break;
                case 11:
                    dismissGifDialog();
                    String message = (String) msg.obj;
                    currency();
                    if (message.equals("金币不足")) {
                        int taskid = msg.arg1;
                        showGifdialog(LoadingDialog.Type_IMG, Constants.Nine_No_Gold);
                    } else {

                        showGifdialog(LoadingDialog.Type_IMG, Constants.Nine_No);
                    }
                    if (isFist) {
                        firstEgg();
                    }
                    break;
                case 12:
                    isFist = (boolean) msg.obj;
                    break;
                case -20:
                    actHelper.dismissProgressDialog();
                    break;
                case -10:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.play, R.id.notice, R.id.speed, R.id.score, R.id.image_vol, R.id.game_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_vol:
                boolean muteFlag = audioManager.isStreamMute(AudioManager.STREAM_MUSIC);//获取当前音乐多媒体是否静音
                if (muteFlag) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);//取消静音
                    imageVol.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_volume_up_24));
                } else {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
                    imageVol.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_volume_off_24));
                }
                break;
            case R.id.score:
                getDrill();
                break;
            case R.id.play:
                break;
            case R.id.notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.speed:
                if (drill == null) {
                    MsgUtils.showLongToast(getContext(), "您还没有开始挖砖!请开始后再加速!");
                } else {
                    actHelper.showProgressDialog("挖钻加速...");
                    getSpeed();
                }
                break;
            case R.id.game_rule:
                startActivity(new Intent(getActivity(), GameRuleActivity.class));
                break;
        }
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("bannerPause");
        myIntentFilter.addAction("bannerResume");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("bannerPause")) {
                banner.onPause();
            }else if (intent.getAction().equals("bannerResume")){
                banner.onResume();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//设为静音
            }
        }
    };

}
