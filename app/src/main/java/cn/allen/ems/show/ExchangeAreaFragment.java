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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.ShowPicActivity;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.MessageShow;
import cn.allen.ems.entry.PhotoShow;
import cn.allen.ems.home.VideoActivity;
import cn.allen.ems.utils.Constants;

public class ExchangeAreaFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.issue)
    CardView issue;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int uid;
    private ActivityHelper actHelper;
    private List<MessageShow> list, sublist;
    private CommonAdapter<MessageShow> adapter;
    private int page = 1;
    private int pagesize = 10;

    public static ExchangeAreaFragment init() {
        ExchangeAreaFragment fragment = new ExchangeAreaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exchange_area, container, false);
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
            if (requestCode == 100) {
                isRefresh = true;
                page = 1;
                loadData();
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
        adapter = new CommonAdapter<MessageShow>(getContext(), R.layout.exchange_area_item_layout) {
            @Override
            public void convert(ViewHolder holder, MessageShow entity, int position) {
                if (entity.getShowpicurl().contains("Videos")) {
                    holder.setImageByUrl(R.id.iv_photo, entity.getThumbnail(), R.drawable.mis_default_error);
                } else {
                    holder.setImageByUrl(R.id.iv_photo, entity.getShowpicurl(), R.drawable.mis_default_error);
                }
                holder.setText(R.id.tv_photo_text, entity.getShowcontent());
                holder.setText(R.id.tv_date, entity.getCreatetime());
            }
        };
        recyclerview.setAdapter(adapter);
    }

    private void addEvent(View view) {
        refreshLayout.setMaterialRefreshListener(materListener);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MessageShow entry=list.get(position);
                if (entry.getShowpicurl().contains("Videos")) {
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra(Constants.Video_Url, entry.getShowpicurl());
                    startActivityForResult(intent, 11);
                }else {
                    Intent intent = new Intent(getActivity(), ShowPicActivity.class);
                    intent.putExtra("url", entry.getShowpicurl());
                    intent.putExtra("title", "");
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private MaterialRefreshListener materListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = true;
            page = 1;
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
                sublist = WebHelper.init().getshowMessage(page++, pagesize).getList();
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
                        if (page == 2) {
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
                startActivityForResult(new Intent(getActivity(),IssueMessageActivity.class),100);
                break;
        }
        view.setEnabled(true);
    }
}
