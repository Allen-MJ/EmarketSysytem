package allen.frame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.adapter.EmojAdapter;
import allen.frame.entry.Emoj;
import allen.frame.tools.EmotionUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
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

    public static EmojFragment instance() {
        EmojFragment fragment = new EmojFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allen_emoj_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

            }else if(v.getId()==R.id.emoj_other){

            }else if(v.getId()==R.id.send_bt){

            }else if(v.getId()==R.id.delete_bt){

            }
        }
    };
}
