package allen.frame;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.adapter.EmojAdapter;
import allen.frame.entry.Emoj;
import allen.frame.tools.EmotionUtils;
import allen.frame.tools.Logger;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static allen.frame.tools.EmotionUtils.EMOTION_CLASSIC_TYPE;

public class EmojFragment extends Fragment {

    AppCompatEditText allenEmojEt;
    AppCompatImageView emojChange;
    AppCompatImageView emojOther;
    RecyclerView emojRv;
    AppCompatTextView sendBt;
    AppCompatImageView deleteBt;
    RelativeLayout emojLayout;
    private EmojAdapter adapter;
    private List<Emoj> list;
    private boolean isEmoj = false;
    ActivityHelper helper;
    ViewGroup parentContent;
    private final String TAG = "emoj";
    private SharedPreferences shared;
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    public static EmojFragment instance() {
        EmojFragment fragment = new EmojFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allen_emoj_layout, container, false);
        helper = new ActivityHelper(getActivity(),view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shared = helper.getSharedPreferences();
        initView(view);
        addEvent(view);
        initData();
    }

    private void initView(View v){
        allenEmojEt = v.findViewById(R.id.allen_emoj_et);
        emojChange = v.findViewById(R.id.emoj_change);
        emojOther = v.findViewById(R.id.emoj_other);
        emojRv = v.findViewById(R.id.emoj_rv);
        sendBt = v.findViewById(R.id.send_bt);
        deleteBt = v.findViewById(R.id.delete_bt);
        emojLayout = v.findViewById(R.id.emoj_layout);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),8);
        emojRv.setLayoutManager(manager);
        adapter = new EmojAdapter();
        emojRv.setAdapter(adapter);
        parentContent = getActivity().findViewById(android.R.id.content);

        parentContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                parentContent.getWindowVisibleDisplayFrame(r);

                int displayHeight = r.bottom - r.top;
                Logger.e(TAG, "displayHeight:" + displayHeight);

                int parentHeight = parentContent.getHeight();
                Logger.e(TAG, "parentHeight:" + parentHeight);

                int softKeyHeight = parentHeight - displayHeight;
                Logger.e(TAG, "softKeyHeight:" + softKeyHeight);
                /**
                 * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
                 * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
                 * 我们需要减去底部虚拟按键栏的高度（如果有的话）
                 */
                if (Build.VERSION.SDK_INT >= 20) {
                    // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
                    softKeyHeight = softKeyHeight - getSoftButtonsBarHeight();
                }

                if (softKeyHeight < 0) {
                    Log.e("emoj","EmotionKeyboard--Warning: value of softInputHeight is below zero!");
                }
                //存一份到本地
                if (softKeyHeight > 0) {
                    shared.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softKeyHeight).apply();
                }
                lockEmojHeight();
            }
        });
    }

    private void initData(){
        list = new ArrayList<>();
        // 遍历所有的表情的key
        for (String flag : EmotionUtils.getEmojiMap(EMOTION_CLASSIC_TYPE).keySet()) {
            list.add(new Emoj(flag,EmotionUtils.getImgByName(EMOTION_CLASSIC_TYPE,flag)));
        }
        adapter.setList(list);
    }

    private void addEvent(View v){
        emojChange.setOnClickListener(l);
        emojOther.setOnClickListener(l);
        sendBt.setOnClickListener(l);
        deleteBt.setOnClickListener(l);
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.emoj_change){
                isEmoj = !isEmoj;
                emojChange.setImageResource(isEmoj?R.drawable.ic_keyboard:R.drawable.ic_face);
                emojLayout.setVisibility(isEmoj?View.VISIBLE:View.GONE);
                if(isEmoj){
                    helper.hideSoftInputView();
                }else{
                    helper.showSoftInputView(allenEmojEt);
                }
            }else if(v.getId()==R.id.emoj_other){

            }else if(v.getId()==R.id.send_bt){

            }else if(v.getId()==R.id.delete_bt){

            }
        }
    };


    private void lockEmojHeight(){
        allenEmojEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("emoj","KeyBoardHeight:"+getKeyBoardHeight());
                emojLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,getKeyBoardHeight()));
            }
        },200);
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     * @return
     */
    private int getKeyBoardHeight(){
        return shared.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);

    }
}
