package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.MessageShow;

public class ShowMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageShow> list;

    public ShowMsgAdapter(){
    }

    public void setList(List<MessageShow> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
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
        objectHolder.bind(list.get(position));
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView title,date,delete;
        private AppCompatImageView icon;
        private View view,picView;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.record_item_title);
            date = itemView.findViewById(R.id.record_item_date);
            delete = itemView.findViewById(R.id.record_item_delete);
            icon = itemView.findViewById(R.id.record_item_icon);
            view = itemView.findViewById(R.id.item_layout);
            picView = itemView.findViewById(R.id.record_item_icon_layout);
        }
        public void bind(MessageShow entry){
            if(entry!=null){
                title.setText(entry.getShowcontent());
                date.setText(entry.getCreatetime().substring(0,10));
                Glide.with(view.getContext()).load(entry.getShowpicurl()).into(icon);
                if(entry.getShowtype()==1){
                    icon.setVisibility(View.VISIBLE);
                }else{
                    icon.setVisibility(View.GONE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.itemClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.deleteClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
            }
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, MessageShow entry);
        void deleteClick(View v, MessageShow entry);
    }
}
