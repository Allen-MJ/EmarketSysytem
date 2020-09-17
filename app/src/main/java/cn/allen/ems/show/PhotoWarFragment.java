package cn.allen.ems.show;

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
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.PhotoShow;
import cn.allen.ems.utils.Constants;

public class PhotoWarFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.issue)
    CardView issue;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int page = 0;
    private int pagesize = 10;
    private int uid;
    private ActivityHelper actHelper;
    private List<PhotoShow> list, sublist;
    private CommonAdapter<PhotoShow> adapter;

    public static PhotoWarFragment init() {
        PhotoWarFragment fragment = new PhotoWarFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_war, container, false);
        actHelper = new ActivityHelper(getActivity(), v);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        addEvent(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 11) {
            }
        }
    }

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        initAdapter();
        loadData();
    }

    private void initAdapter() {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(staggeredGridLayoutManager);
        adapter = new CommonAdapter<PhotoShow>(getContext(), R.layout.photo_wall_item_layout) {
            @Override
            public void convert(ViewHolder holder, PhotoShow entity, int position) {
                holder.setImageByUrl(R.id.iv_photo, entity.getShowpicurl(), R.drawable.mis_default_error);
                holder.setText(R.id.tv_photo_text, entity.getShowcontent());
            }
        };
        recyclerview.setAdapter(adapter);
    }

    private void addEvent(View view) {
        refreshLayout.setMaterialRefreshListener(materListener);
    }

    private MaterialRefreshListener materListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = true;
            page = 0;
            loadData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = false;
            loadData();
        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getshowPhoto(page++, pagesize).getList();
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
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    if (isRefresh) {
                        list = sublist;
                        refreshLayout.finishRefresh();
                    } else {
                        if (page == 1) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        refreshLayout.finishRefreshLoadMore();
                    }
                    if (list.size() == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "");
                    }
                    actHelper.setCanLoadMore(refreshLayout, pagesize, list);
                    adapter.setDatas(list);
                    break;
            }
        }
    };

    @OnClick(R.id.issue)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.issue:
                break;
        }
        view.setEnabled(true);
    }
}
