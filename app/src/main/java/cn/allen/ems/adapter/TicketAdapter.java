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

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> list;

    public TicketAdapter(){
    }

    public void setList(List<Order> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
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

        private AppCompatTextView name,date,address,redeem,count;
        private AppCompatImageView icon,rccode;
        private View view,exview;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ticket_item_name);
            date = itemView.findViewById(R.id.ticket_item_date);
            address = itemView.findViewById(R.id.ticket_item_address);
            count = itemView.findViewById(R.id.ticket_item_count);
            redeem = itemView.findViewById(R.id.ticket_item_exchange);
            rccode = itemView.findViewById(R.id.ticket_item_rccode);
            icon = itemView.findViewById(R.id.ticket_item_icon);
            view = itemView.findViewById(R.id.item_layout);
            exview = itemView.findViewById(R.id.ticket_item_exchange_layout);
        }
        public void bind(Order entry){
            if(entry!=null){
                name.setText(entry.getShopname());
                date.setText("使用时间:"+entry.getUsetimestart().substring(0,7)+"-"+entry.getUsetimeend().substring(0,7));

                address.setText("店铺地址:");
                Glide.with(view.getContext()).load(entry.getShoppicurl()).into(icon);
                if (entry.getShoptype()==2){
                    rccode.setVisibility(View.GONE);
                    count.setText("运单号:"+entry.getWaybill());
                }else {
                    count.setText("兑换时间:"+entry.getFetchtime());
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
                redeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.exClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
                rccode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.rcClick(view,entry);
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
        void rcClick(View v, Order entry);
        void exClick(View v, Order entry);
    }
}
