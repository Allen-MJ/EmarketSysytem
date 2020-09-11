package cn.allen.ems.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.utils.Constants;

public class CampaignActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.campaign_code)
    AppCompatTextView campaignCode;
    @BindView(R.id.campaign_copy)
    AppCompatTextView campaignCopy;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.campaign_person)
    AppCompatTextView campaignPerson;
    @BindView(R.id.campaign_reward)
    AppCompatTextView campaignReward;
    private SharedPreferences shared;
    private String campaign;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_campaign;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared = AllenManager.getInstance().getStoragePreference();
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        campaign = shared.getString(Constants.User_Invitation, "");
        campaignCode.setText(campaign);
    }

    @Override
    protected void addEvent() {
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                finish();
                view.setEnabled(true);
            }
        });
    }

    @OnClick(R.id.campaign_copy)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.campaign_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", campaign);
                cm.setPrimaryClip(mClipData);
                MsgUtils.showShortToast(context,"复制成功!");
                break;
        }
        v.setEnabled(true);
    }

}
