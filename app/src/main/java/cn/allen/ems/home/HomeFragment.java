package cn.allen.ems.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.TimeMeter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.NoticeActivity;
import cn.allen.ems.R;
import cn.allen.ems.adapter.ShareAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Drill;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.QrCode;
import cn.allen.ems.task.WatchActivity;
import cn.allen.ems.utils.Constants;
import cn.allen.ems.utils.LoadingDialog;

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
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int uid;
    private ActivityHelper actHelper;
    private List<Notice> list;
    //    private GameAdapter adapter;
    private CommonAdapter<NineGrid> adapter;
    private List<NineGrid> nineGrids;
    private String city = "重庆";
    private List<QrCode> qrCodes;
    private ShareAdapter shareAdapter;
    private Drill drill;
    private int clickPosition = -1;
    private int surplustime;
    private int muin;
    private int seconds;
    private TimeMeter timeMeter = TimeMeter.getInstance();

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
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeMeter.stop();
    }

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
//        city = shared.getString(Constants.User_City, "重庆");
        initAdapter();

        GridLayoutManager smanager = new GridLayoutManager(getActivity(), 2);
        sharedRv.setLayoutManager(smanager);
        shareAdapter = new ShareAdapter();
        sharedRv.setAdapter(shareAdapter);
        getNiticeTop5();
        getGameFirst();
        getQrCodes();
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
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (clickPosition == -1) {
                    clickPosition = position;
                    adapter.notifyItemChanged(position);
                    gameRv.setEnabled(false);
                    showGifdialog("请稍候...");
                    getSmashEgg(nineGrids.get(position).getPalacesid());
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

            }
        });
    }

    private LoadingDialog dialog;

    public void showGifdialog(String content) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext(), content, R.mipmap.dankai, LoadingDialog.Type_GIF);
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
                list = WebHelper.init().getTipsList(0, 5).getList();
                handler.sendEmptyMessage(100);
            }
        }).start();
    }

    private void getGameFirst() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nineGrids = WebHelper.init().getNineGame(city);
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
                drill = WebHelper.init().quickenDrill(handler, uid, drill.getDrillid(), drill.getQuickentime());
            }
        }).start();
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 11:
                    actHelper.showProgressDialog("挖钻开始...");
                    getSpeed();
                    break;
                case 12:
                    int pos = clickPosition;
                    clickPosition = -1;
                    adapter.notifyItemChanged(pos);
                    break;
            }
        }
    }

    private int index = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
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
                    speed.setText(getString(R.string.game_speed_hint) + "  " + drill.getQuickencount());

                    break;
                case 10:
                    dismissGifDialog();
                    MsgUtils.showMDMessage(getContext(), (String) msg.obj);
                    getGameFirst();

                    break;
                case 11:
                    dismissGifDialog();
                    String message = (String) msg.obj;
                    if (message.equals("金币不足")) {
                        int taskid = msg.arg1;
                        MsgUtils.showMDMessage(getContext(), "金币不足！是否看视频赚取金币？", "是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(), WatchActivity.class);
                                intent.putExtra(Constants.Entry_Flag, taskid);
                                startActivityForResult(intent, 12);
                            }
                        }, "否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int pos=clickPosition;
                                clickPosition=-1;
                                adapter.notifyItemChanged(pos);
                            }
                        });
                    }
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);

                    break;
                case -20:
                case -10:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    break;
            }
        }
    };

    @OnClick({R.id.play, R.id.notice, R.id.speed, R.id.score})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                    return;
                }
                Intent intent = new Intent(getContext(), WatchActivity.class);
                intent.putExtra(Constants.Entry_Flag, drill.getDrillid());
                startActivityForResult(intent, 11);
//                if (drill.getQuickencount()==0){
//                    MsgUtils.showLongToast(getContext(),"您没有加速次数！");
//                    return;
//                }else if (drill.getQuickentime()==0){
//                    MsgUtils.showLongToast(getContext(),"您没有加速时间!");
//                    return;
//                }
//                getSpeed();
                break;
        }
    }

}
