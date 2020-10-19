package cn.allen.ems.home;

import android.os.Bundle;
import android.view.View;

import allen.frame.AllenBaseActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.R;

public class GameRuleActivity extends AllenBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.rule_ms)
    AppCompatTextView ruleMs;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_game_rule;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        ruleMs.setText("ashdkajlsdlajh");
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
