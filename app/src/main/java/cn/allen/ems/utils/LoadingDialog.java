package cn.allen.ems.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;

import cn.allen.ems.R;

public class LoadingDialog extends Dialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    // 显示的图片
    private ImageView mImageView;
    // 提示的文字
    private String mLoadingTip;
    private TextView mLoadingTv, loadingText;
    private AppCompatButton button;
    private AppCompatImageView closeButton;
    private int mResid;
    private LinearLayoutCompat load_layout, load_layout_gif;
    // GIF动态图片
    private ImageView load_gifv;
    // 加载框显示类型
    private String showType;

    public static final String Type_GIF = "GIF";
    public static final String Type_IMG = "IMG";
    private Handler handler;

    /**
     * loading对话框构造方法
     *
     * @param context 上下文
     * @param content 提示信息
     * @param resId   资源
     * @param type    loading图类型 GIF;IMG
     */
    public LoadingDialog(Context context, String content, int resId, String type, Handler handler) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = resId;
        this.showType = type;
        this.handler=handler;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        initView();
        initData();
    }

    /**
     * 页面初始化
     */
    private void initView() {
        mLoadingTv = findViewById(R.id.load_tetv);
        loadingText = findViewById(R.id.load_text);
        mImageView = findViewById(R.id.load_imgv);
        load_layout = findViewById(R.id.load_layout);
        load_layout_gif = findViewById(R.id.load_layout_gif);
        load_gifv = findViewById(R.id.load_gifv);
        button = findViewById(R.id.button);
        closeButton = findViewById(R.id.close_button);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        if (showType.equals(Type_GIF)) {
//            load_gifv.setMovieResource(mResid);
            load_layout.setVisibility(View.GONE);
            load_layout_gif.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mResid).asGif().into(load_gifv);
            loadingText.setText(mLoadingTip);
        } else if (showType.equals(Type_IMG)) {
            load_layout_gif.setVisibility(View.GONE);
            load_layout.setVisibility(View.VISIBLE);
            mLoadingTv.setVisibility(View.GONE);
            Glide.with(mContext).load(mResid).into(mImageView);
            switch (mLoadingTip) {
                case Constants.Nine_Yes:
                    button.setVisibility(View.VISIBLE);
                    button.setText("去看奖品");
                    break;
                case Constants.Nine_No:
                    button.setVisibility(View.GONE);
                    break;
                case Constants.Nine_No_Gold:
                    mLoadingTv.setVisibility(View.VISIBLE);
                    mLoadingTv.setText("金币不足！");
                    button.setText("去赚金币");
                    break;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mLoadingTip) {
                        case Constants.Nine_Yes:
                            dismiss();
                            handler.sendEmptyMessage(107);
                            break;
                        case Constants.Nine_No:
                            break;
                        case Constants.Nine_No_Gold:
                            dismiss();
                            handler.sendEmptyMessage(106);
                            break;
                    }
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.sendEmptyMessage(105);
                    dismiss();
                }
            });

        }
    }

    /**
     * 设置提示信息内容
     *
     * @param str
     */
    public void setContent(String str) {
        mLoadingTv.setText(str);
    }
}
