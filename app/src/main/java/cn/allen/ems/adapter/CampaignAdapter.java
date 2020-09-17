package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.Campaign;

public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Campaign> list;

    public CampaignAdapter(){
    }

    public void setList(List<Campaign> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign, parent, false);
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

        private AppCompatTextView phone,score,date;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.campaingn_item_phone);
            score = itemView.findViewById(R.id.campaingn_item_score);
            date = itemView.findViewById(R.id.campaingn_item_date);
        }
        public void bind(Campaign entry){
            if(entry!=null){
                phone.setText(entry.getPhone());
                score.setText("+ "+entry.getReward());
                date.setText(entry.getCreateTime().substring(0,10));
            }
        }
    }
}
