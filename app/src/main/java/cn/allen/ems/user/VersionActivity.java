package cn.allen.ems.user;

import android.os.Bundle;
import android.view.View;

import allen.frame.AllenBaseActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.R;

public class VersionActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.version_info)
    AppCompatTextView versionInfo;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_version;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        versionInfo.setText("版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息版本信息");
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
}
