package cn.allen.ems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.ems.R;
import cn.allen.ems.entry.Address;

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Address> list;

    public AddressAdapter(){
    }

    public void setList(List<Address> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
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

        private AppCompatTextView setinit,name,phone,info,delete,edit,init;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            setinit = itemView.findViewById(R.id.address_item_setinit);
            name = itemView.findViewById(R.id.address_item_name);
            phone = itemView.findViewById(R.id.address_item_phone);
            info = itemView.findViewById(R.id.address_item_info);
            delete = itemView.findViewById(R.id.address_item_delete);
            init = itemView.findViewById(R.id.address_item_init);
            edit = itemView.findViewById(R.id.address_item_edit);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(Address entry,int index){
            if(entry!=null){
                if(entry.isType()){
                    setinit.setVisibility(View.VISIBLE);
                    init.setVisibility(View.GONE);
                }else{
                    setinit.setVisibility(View.GONE);
                    init.setVisibility(View.VISIBLE);
                }
                name.setText(entry.getRecipiment());
                phone.setText(entry.getTelphone());
                info.setText(entry.getArea()+entry.getCity()+entry.getCounty()+entry.getDetailaddress());
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
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.editClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
                init.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.initClick(view,entry);
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
        void itemClick(View v,Address entry);
        void deleteClick(View v,Address entry);
        void editClick(View v,Address entry);
        void initClick(View v,Address entry);
    }
}
