package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NineGrid> list;

    public GameAdapter(){
    }

    public void setList(List<NineGrid> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ninegrid, parent, false);
        v.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ObjectHolder(v);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ObjectHolder objectHolder = (ObjectHolder) holder;
        objectHolder.bind(position);
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatImageView icon;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.nine_item_icon);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(int index){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    if(listener!=null){
                        listener.itemClick(view,index);
                    }
                    view.setEnabled(true);
                }
            });
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, int index);
    }
}
