package cn.allen.ems;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import allen.frame.AllenIMBaseActivity;
import allen.frame.widget.PhotoView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowPicActivity extends AllenIMBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.pic)
    PhotoView pic;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    private String url;
    private String title;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_show_pic;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pic.enable();
        pic.enableRotate();
        Glide.with(context).load(url).into(pic);
        tvTitle.setText(title);
    }

    @Override
    protected void addEvent() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
        view.setEnabled(true);
    }

}
