package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.Notice;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notice> list;

    public NoticeAdapter(){
    }

    public void setList(List<Notice> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
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

        private AppCompatTextView title,content,date;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_item_title);
            content = itemView.findViewById(R.id.notice_item_content);
            date = itemView.findViewById(R.id.notice_item_date);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(Notice entry){
            if(entry!=null){
                title.setText(entry.getTitle());
                content.setText(entry.getTipcontent());
                date.setText(entry.getCreatetime().substring(0,10));
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
            }
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, Notice entry);
    }
}
