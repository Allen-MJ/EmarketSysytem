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
import cn.allen.ems.entry.NineGrid;
import cn.allen.ems.entry.Notice;
import cn.allen.ems.entry.QrCode;

public class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QrCode> list;

    public ShareAdapter(){
    }

    public void setList(List<QrCode> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_qrcode, parent, false);
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

        private AppCompatImageView icon;
        private AppCompatTextView name;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.qr_item_icon);
            name = itemView.findViewById(R.id.qr_item_name);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(QrCode entry){
            if(entry!=null){
                Glide.with(view.getContext()).load(entry.getQrcodeurl()).into(icon);
                name.setText(entry.getQrcodename());
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
        void itemClick(View v, QrCode entry);
    }
}
