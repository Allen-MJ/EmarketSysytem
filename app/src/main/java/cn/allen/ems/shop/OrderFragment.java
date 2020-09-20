package cn.allen.ems.shop;

import android.annotation.SuppressLint;
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
import allen.frame.entry.City;
import allen.frame.tools.ChoiceTypeDialog;
import allen.frame.tools.CityUtil;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.OrderAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Order;
import cn.allen.ems.utils.Constants;

public class OrderFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    @BindView(R.id.choice_area)
    AppCompatTextView choiceArea;
    @BindView(R.id.choice_city)
    AppCompatTextView choiceCity;
    @BindView(R.id.choice_country)
    AppCompatTextView choiceCountry;
    private SharedPreferences shared;
    private boolean isRefresh = false;
    private int page = 0;
    private int pagesize = 10;
    private int uid;
    private ActivityHelper actHelper;
    private List<Order> list, sublist;
//    private OrderAdapter adapter;
    private CommonAdapter<Order> adapter;
    private String mcity;
    private List<City> first, secound, third;

    public static OrderFragment init() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
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

    private void initUI(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        adapter=new CommonAdapter<Order>(getContext(),R.layout.item_order) {
            @Override
            public void convert(ViewHolder holder, Order entry, int position) {
                holder.setText(R.id.order_item_title,entry.getShopname());
                holder.setText(R.id.order_item_date,"使用时间:"+entry.getUsetimestart().substring(0,7).replaceAll("-",".")+"-"+entry.getUsetimeend().substring(0,7).replaceAll("-","."));
                holder.setText(R.id.order_item_count,"库存剩余:"+entry.getShopstock());
                holder.setText(R.id.order_item_change,""+entry.getCurrency1());
                holder.setText(R.id.order_item_gold,""+entry.getCurrency2());
                holder.setText(R.id.order_item_diamond,""+entry.getCurrency3());
                holder.setImageByUrl(R.id.order_item_icon,entry.getShoppicurl(),R.drawable.mis_default_error);
                holder.setOnClickListener(R.id.order_item_redeem, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void addEvent(View view) {
        mater.setMaterialRefreshListener(materListener);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Logger.e("order"," 11111");
                actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                perOrder(list.get(position).getShopid());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
//        adapter.setOnItemClickListener(listener);
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

//    private OrderAdapter.OnItemClickListener listener = new OrderAdapter.OnItemClickListener() {
//        @Override
//        public void itemClick(View v, Order entry) {
//            Logger.e("order",entry.getShopname());
//            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
//            perOrder(entry.getShopid());
//        }
//    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getMerchantOrder(page++, pagesize, mcity).getList();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void perOrder(int shopId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().preOrder(handler,uid,shopId);
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
                        mater.finishRefresh();
                    } else {
                        if (page == 1) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        mater.finishRefreshLoadMore();
                    }
                    if (list.size() == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "");
                    }
                    actHelper.setCanLoadMore(mater, pagesize, list);
                    adapter.setDatas(list);
                    break;
                case 1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES,"");
                    MsgUtils.showMDMessage(getContext(),(String)msg.obj);
                    loadData();
                    break;
                case 2:
                    actHelper.dismissProgressDialog();
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(getActivity(), new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            secound = city.getArea();
                            third = null;
                            choiceCity.setText("");
                            choiceCountry.setText("");
                            mcity = city.getName();
                            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                            page = 0;
                            loadData();
                        }
                    });
                    dialog.showCityDialog("请选择地区", choiceArea, first);
                    break;
                case 3:
                    actHelper.dismissProgressDialog();
                    ChoiceTypeDialog sdialog = new ChoiceTypeDialog(getActivity(), new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            third = city.getArea();
                            choiceCountry.setText("");
                            mcity = city.getName();
                            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                            page = 0;
                            loadData();
                        }
                    });
                    sdialog.showCityDialog("请选择城市", choiceCity, secound);
                    break;
                case 4:
                    actHelper.dismissProgressDialog();
                    ChoiceTypeDialog tdialog = new ChoiceTypeDialog(getActivity(), new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            mcity = city.getName();
                            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                            page = 0;
                            loadData();
                        }
                    });
                    tdialog.showCityDialog("请选择区县", choiceCountry, third);
                    break;
                case -1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES,"");
                    MsgUtils.showMDMessage(getContext(),(String)msg.obj);
                    break;
            }
        }
    };

    @OnClick({R.id.choice_area, R.id.choice_city, R.id.choice_country})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choice_area:
                actHelper.showProgressDialog("");
                CityUtil.init().loadData(getActivity(),"","").newCall(new CityUtil.CallBack() {
                    @Override
                    public void onResponse(List<City> list) {
                        first = list;
                        handler.sendEmptyMessage(2);
                    }
                });
                break;
            case R.id.choice_city:
                int slen = secound == null ? 0 : secound.size();
                Logger.e("debug", "size:" + slen);
                if (slen > 0) {
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(getActivity(), new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            third = city.getArea();
                            choiceCountry.setText("");
                            mcity = city.getName();
                            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                            page = 0;
                            loadData();
                        }
                    });
                    dialog.showCityDialog("请选择城市", choiceCity, secound);
                }else{
                    String area = choiceArea.getText().toString().trim();
                    if(StringUtils.empty(area)){
                        MsgUtils.showMDMessage(getActivity(),"请选择地区!");
                        return;
                    }
                    actHelper.showProgressDialog("");
                    CityUtil.init().loadData(getActivity(),area,"").newCall(new CityUtil.CallBack() {
                        @Override
                        public void onResponse(List<City> list) {
                            secound = list;
                            handler.sendEmptyMessage(3);
                        }
                    });
                }
                break;
            case R.id.choice_country:
                int tlen = third == null ? 0 : third.size();
                Logger.e("debug", "size:" + tlen);
                if (tlen > 0) {
                    ChoiceTypeDialog dialog = new ChoiceTypeDialog(getActivity(), new ChoiceTypeDialog.OnClickListener() {
                        @Override
                        public void choiceCity(City city) {
                            mcity = city.getName();
                            actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                            page = 0;
                            loadData();
                        }
                    });
                    dialog.showCityDialog("请选择区县", choiceCountry, third);
                }else{
                    String area = choiceArea.getText().toString().trim();
                    String city = choiceCity.getText().toString().trim();
                    if(StringUtils.empty(area)){
                        MsgUtils.showMDMessage(getActivity(),"请选择地区!");
                        return;
                    }
                    if(StringUtils.empty(city)){
                        MsgUtils.showMDMessage(getActivity(),"请选择城市!");
                        return;
                    }
                    actHelper.showProgressDialog("");
                    CityUtil.init().loadData(getActivity(),area,city).newCall(new CityUtil.CallBack() {
                        @Override
                        public void onResponse(List<City> list) {
                            third = list;
                            handler.sendEmptyMessage(4);
                        }
                    });
                }
                break;
        }
    }

}
