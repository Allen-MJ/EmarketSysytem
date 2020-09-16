package cn.allen.ems.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import cn.allen.ems.adapter.GameAdapter;
import cn.allen.ems.adapter.ShareAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Drill;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.QrCode;
import cn.allen.ems.utils.Constants;

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
    @BindView(R.id.speed_score)
    AppCompatTextView speedScore;
    @BindView(R.id.speed)
    AppCompatTextView speed;
    @BindView(R.id.shared_rv)
    RecyclerView sharedRv;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int uid;
    private ActivityHelper actHelper;
    private List<Notice> list;
    private GameAdapter adapter;
    private List<NineGrid> nineGrids;
    private String city="重庆";
    private List<QrCode> qrCodes;
    private ShareAdapter shareAdapter;
    private Drill drill;

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
        getSpeed();
    }

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        city = shared.getString(Constants.User_City, "重庆");
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        gameRv.setLayoutManager(manager);
        adapter = new GameAdapter();
        gameRv.setAdapter(adapter);
        GridLayoutManager smanager = new GridLayoutManager(getActivity(), 2);
        sharedRv.setLayoutManager(smanager);
        shareAdapter = new ShareAdapter();
        sharedRv.setAdapter(shareAdapter);
        getNiticeTop5();
        getGameFirst();
        getQrCodes();
    }

    private void addEvent(View view) {
        adapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int index) {

            }
        });
        shareAdapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, QrCode entry) {

            }
        });
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

    private void getQrCodes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                qrCodes = WebHelper.init().getQrCodeList();
                handler.sendEmptyMessage(103);
            }
        }).start();
    }

    private void getSpeed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                drill = WebHelper.init().beginDrill(handler,uid);
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
                    adapter.setList(nineGrids);
                    break;
                case 103:
                    shareAdapter.setList(qrCodes);
                    break;
                case 20:
                    time.setText(drill.getSurplustime());
                    speedScore.setText(drill.getQuickentime());
                    speed.setText(getString(R.string.game_speed_hint) +"  "+ drill.getQuickencount());
                    break;
                case -20:

                    break;
            }
        }
    };

    @OnClick({R.id.play, R.id.notice, R.id.speed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play:
                break;
            case R.id.notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.speed:

                break;
        }
    }

}
