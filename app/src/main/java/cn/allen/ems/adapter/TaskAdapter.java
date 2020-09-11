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
import cn.allen.ems.entry.Task;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> list;

    public TaskAdapter(){
    }

    public void setList(List<Task> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
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

        private AppCompatTextView name,getbt,count;
        private AppCompatImageView icon;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.task_item_name);
            getbt = itemView.findViewById(R.id.task_item_get);
            count = itemView.findViewById(R.id.task_item_count);
            icon = itemView.findViewById(R.id.task_item_icon);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(Task entry){
            if(entry!=null){
                name.setText(entry.getTaskdescribe());
                count.setText(entry.getSeencount()+"/"+entry.getWatchcount());
                Glide.with(view.getContext()).load(entry.getTaskpic()).into(icon);
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
                getbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.getClick(view,entry);
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
        void itemClick(View v, Task entry);
        void getClick(View v, Task entry);
    }
}
