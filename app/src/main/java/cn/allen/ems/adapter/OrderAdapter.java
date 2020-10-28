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
import cn.allen.ems.entry.Order;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> list;

    public OrderAdapter(){
    }

    public void setList(List<Order> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
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

        private AppCompatTextView title,date,redeem,count;
        private AppCompatTextView gold,change,diamond;
        private AppCompatImageView icon;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.order_item_title);
            date = itemView.findViewById(R.id.order_item_date);
            redeem = itemView.findViewById(R.id.order_item_redeem);
            count = itemView.findViewById(R.id.order_item_count);
            change = itemView.findViewById(R.id.order_item_change);
            gold = itemView.findViewById(R.id.order_item_gold);
            diamond = itemView.findViewById(R.id.order_item_diamond);
            icon = itemView.findViewById(R.id.order_item_icon);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(Order entry){
            if(entry!=null){
                title.setText(entry.getShopname());
                date.setText("使用时间:"+entry.getUsetimestart().substring(0,7).replaceAll("-",".")+"-"+entry.getUsetimeend().substring(0,7).replaceAll("-","."));
                count.setText("库存剩余:"+entry.getShopstock());
                change.setText(""+entry.getCurrency1());
                gold.setText(""+entry.getCurrency2());
                diamond.setText(""+entry.getCurrency3());
                Glide.with(view.getContext()).load(entry.getShoppicurl()).into(icon);
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
                redeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.orderClick(view,entry);
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
        void itemClick(View v, Order entry);
        void orderClick(View v,Order entry);
    }
}
