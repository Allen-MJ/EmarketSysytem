package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import allen.frame.widget.CircleImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.Address;
import cn.allen.ems.entry.TalkMsg;

public class TalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TalkMsg> list;

    private int uid;
    public TalkAdapter(int uid){
        this.uid = uid;
    }

    public void setList(List<TalkMsg> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void addMsg(TalkMsg entry){
        if(list==null){
            list = new ArrayList<>();
        }
        list.add(entry);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_talk, parent, false);
        v.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ObjectHolder(v);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ObjectHolder objectHolder = (ObjectHolder) holder;
        objectHolder.bind(list.get(position),position);
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView lcontent,rcontent;
        private CircleImageView licon,ricon;
        private View lview,rview;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            lcontent = itemView.findViewById(R.id.talk_item_l_content);
            rcontent = itemView.findViewById(R.id.talk_item_r_content);
            licon = itemView.findViewById(R.id.talk_item_l_icon);
            ricon = itemView.findViewById(R.id.talk_item_r_icon);
            lview = itemView.findViewById(R.id.talk_item_l_layout);
            rview = itemView.findViewById(R.id.talk_item_r_layout);
        }
        public void bind(TalkMsg entry,int index){
            if(entry!=null){
                if(uid==entry.getUid()){
                    lview.setVisibility(View.GONE);
                    rview.setVisibility(View.VISIBLE);
                    rcontent.setText(entry.getMsg());
                    Glide.with(lview.getContext()).load(entry.getPhoto()).placeholder(R.drawable.photo).error(R.drawable.photo).into(ricon);
                }else{
                    lview.setVisibility(View.VISIBLE);
                    rview.setVisibility(View.GONE);
                    lcontent.setText(entry.getMsg());
                    Glide.with(lview.getContext()).load(entry.getPhoto()).placeholder(R.drawable.photo).error(R.drawable.photo).into(licon);
                }
            }
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, Address entry);
    }
}
