package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.tools.CommonPopupWindow;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.ems.R;
import cn.allen.ems.adapter.TicketAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.Address;
import cn.allen.ems.entry.Order;
import cn.allen.ems.shop.OrderInfoActivity;
import cn.allen.ems.utils.Constants;

public class TicketFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    private SharedPreferences shared;
    private int type, uid;
    private int page = 1;
    private int pagesize = 10;
    private TicketAdapter adapter;
    private List<Order> list, sublist;
    private ActivityHelper actHelper;
    private boolean isRefresh = false;

    public static TicketFragment init(int type) {
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
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
        type = getArguments().getInt("type", 0);
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
        mater.setLoadMore(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        mater.setMaterialRefreshListener(materListener);
        adapter = new TicketAdapter();
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(listener);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    isRefresh = true;
                    page = 1;
                    loadData();
                    break;
                case 101:
                    def = (Address) data.getSerializableExtra(Constants.Choice);
                    name.setText(def == null ? (shared.getString(Constants.User_Default_Address_Uname, "")
                            + "  " + shared.getString(Constants.User_Default_Address_Phone, ""))
                            : (def.getRecipiment() + "  " + def.getTelphone()));
                    adress.setText(def == null ? (shared.getString(Constants.User_Default_Address_Area, "")
                            + shared.getString(Constants.User_Default_Address_City, "")
                            + shared.getString(Constants.User_Default_Address_County, "")
                            + shared.getString(Constants.User_Default_Address_Detailaddress, ""))
                            : (def.getArea() + def.getCity() + def.getCounty() + def.getDetailaddress()));
                    break;
            }
        }
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

    CommonPopupWindow dialog;
    AppCompatTextView name;
    AppCompatTextView adress;
    private Address def;
    private TicketAdapter.OnItemClickListener listener = new TicketAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Order entry) {
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra("order", entry);
            startActivityForResult(intent, 100);
        }

        @Override
        public void rcClick(View v, Order entry) {

        }

        @Override
        public void exClick(View v, Order entry) {
            dialog = new CommonPopupWindow.Builder(getActivity()).setBackGroundLevel(0.5f)
                    .setWidthAndHeight(0, 0, true).setOutsideTouchable(true).setView(R.layout.dialog_default_adress)
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            switch (layoutResId) {
                                case R.layout.dialog_default_adress:
                                    name = view.findViewById(R.id.def_name);
                                    adress = view.findViewById(R.id.def_adress);

                                    name.setText(def == null ? (shared.getString(Constants.User_Default_Address_Uname, "")
                                            + "  " + shared.getString(Constants.User_Default_Address_Phone, ""))
                                            : (def.getRecipiment() + "  " + def.getTelphone()));
                                    adress.setText(def == null ? (shared.getString(Constants.User_Default_Address_Area, "")
                                            + shared.getString(Constants.User_Default_Address_City, "")
                                            + shared.getString(Constants.User_Default_Address_County, "")
                                            + shared.getString(Constants.User_Default_Address_Detailaddress, ""))
                                            : (def.getArea() + def.getCity() + def.getCounty() + def.getDetailaddress()));
                                    AppCompatTextView add = view.findViewById(R.id.add_address);
                                    AppCompatImageView choice = view.findViewById(R.id.set_adress);
                                    AppCompatButton ok = view.findViewById(R.id.ok_bt);
                                    ImageView close = view.findViewById(R.id.iv_close);
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivityForResult(new Intent(getActivity(), AddAddressActivity.class).putExtra(Constants.Choice, true), 101);
                                        }
                                    });
                                    choice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivityForResult(new Intent(getActivity(), AddressActivity.class).putExtra(Constants.Choice, true), 101);
                                        }
                                    });
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            Logger.e("order", entry.getShopname());

                                            actHelper.showProgressDialog("");
                                            perOrder(entry.getShopid());
                                        }
                                    });
                                    break;
                            }
                        }
                    }).create();
            dialog.showAsDropDown(v);
        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getShopByUid(uid, type, page++, pagesize).getList();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void perOrder(int shopId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().preOrder(handler, uid, shopId, def == null ? shared.getString(Constants.User_Default_Address_Uname, "") : def.getRecipiment(), def == null ? shared.getString(Constants.User_Default_Address_Phone, "") : def.getTelphone(), adress.getText().toString());
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
                        if (page == 2) {
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
                    adapter.setList(list);
                    break;
                case 1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    isRefresh = true;
                    page = 1;
                    loadData();
                    currency();
                    break;
                case 120:
                    Intent mIntent = new Intent("update");
                    //发送广播
                    getActivity().sendBroadcast(mIntent);
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    MsgUtils.showMDMessage(getContext(), (String) msg.obj);
                    break;
            }
        }
    };
}
