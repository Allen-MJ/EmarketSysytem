package cn.allen.ems.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import allen.frame.AllenManager;
import allen.frame.tools.StringUtils;
import allen.frame.widget.CircleImageView;
import allen.frame.widget.ExpView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.ems.LoginActivity;
import cn.allen.ems.R;
import cn.allen.ems.utils.Constants;

public class UserFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.user_photo)
    CircleImageView userPhoto;
    @BindView(R.id.user_name)
    AppCompatTextView userName;
    @BindView(R.id.user_count)
    AppCompatTextView userCount;
    @BindView(R.id.user_level)
    AppCompatTextView userLevel;
    @BindView(R.id.user_change)
    AppCompatTextView userChange;
    @BindView(R.id.user_gold)
    AppCompatTextView userGold;
    @BindView(R.id.user_diamond)
    AppCompatTextView userDiamond;
    @BindView(R.id.user_campaign_code)
    AppCompatTextView userCampaignCode;
    @BindView(R.id.user_campaign)
    LinearLayoutCompat userCampaign;
    @BindView(R.id.user_my_shop)
    AppCompatTextView userMyShop;
    @BindView(R.id.user_my_record)
    AppCompatTextView userMyRecord;
    @BindView(R.id.user_my_real)
    AppCompatTextView userMyReal;
    @BindView(R.id.user_my_info)
    AppCompatTextView userMyInfo;
    @BindView(R.id.user_my_address)
    AppCompatTextView userMyAddress;
    @BindView(R.id.user_about_us)
    AppCompatTextView userAboutUs;
    @BindView(R.id.user_version)
    AppCompatTextView userVersion;
    @BindView(R.id.exit)
    AppCompatButton exit;
    @BindView(R.id.user_level_cur)
    AppCompatTextView userLevelCur;
    @BindView(R.id.user_level_next)
    AppCompatTextView userLevelNext;
    @BindView(R.id.user_exp)
    ExpView userExp;
    private SharedPreferences shared;

    public static UserFragment init() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
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
        shared = AllenManager.getInstance().getStoragePreference();
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity()).load(shared.getString(Constants.User_HeadImage_Url, "")).into(userPhoto);
        userName.setText(shared.getString(Constants.User_Name, ""));
        userCount.setText("登入:" + shared.getInt(Constants.User_LoginCount, 0) + "次");
        String level = shared.getString(Constants.User_Grade, "");
        userLevel.setText(level);
        userLevelCur.setText(StringUtils.empty(level) ? "0" : level);
        userLevelNext.setText(StringUtils.empty(level) ? "1" : "" + (Integer.parseInt(level) + 1));
        float cur = shared.getFloat(Constants.User_CurEXP,0f);
        float next = shared.getFloat(Constants.User_NextEXP,0f);
        userExp.setPercent(cur/(cur+next));
        userCampaignCode.setText("邀请码:" + shared.getString(Constants.User_Invitation, ""));
        userChange.setText("" + shared.getFloat(Constants.User_ChangeScore, 0f));
        userGold.setText("" + shared.getFloat(Constants.User_Gold, 0f));
        userDiamond.setText("" + shared.getFloat(Constants.User_Diamond, 0f));
        registerBoradcastReceiver();
    }
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @OnClick({R.id.user_campaign, R.id.user_my_shop, R.id.user_my_record, R.id.user_my_real, R.id.user_my_info, R.id.user_my_address, R.id.user_about_us, R.id.user_version, R.id.exit})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.user_campaign:
                startActivity(new Intent(getActivity(), CampaignActivity.class));
                break;
            case R.id.user_my_shop:
                startActivity(new Intent(getActivity(), UserTicketActivity.class));
                break;
            case R.id.user_my_record:
                startActivity(new Intent(getActivity(), UserRecordActivity.class));
                break;
            case R.id.user_my_real:
                startActivity(new Intent(getActivity(), UserVerifyActivity.class));
                break;
            case R.id.user_my_info:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.user_my_address:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.user_about_us:
                startActivity(new Intent(getActivity(), UserTalkActivity.class));
                break;
            case R.id.user_version:
                startActivity(new Intent(getActivity(), VersionActivity.class));
                break;
            case R.id.exit:
                AllenManager.getInstance().back2Activity(LoginActivity.class);
                break;
        }
        view.setEnabled(true);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("update")) {
                userChange.setText("" + shared.getFloat(Constants.User_ChangeScore, 0f));
                userGold.setText("" + shared.getFloat(Constants.User_Gold, 0f));
                userDiamond.setText("" + shared.getFloat(Constants.User_Diamond, 0f));
            }
        }

    };
}
