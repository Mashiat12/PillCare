package com.example.medialert.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medialert.Model.SellMedicine;
import com.example.medialert.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class SellMedicineListAdapter extends RecyclerView.Adapter<SellMedicineListAdapter.SellMedicineListAdapterHolder>{
    private Context context;
    private List<SellMedicine> historyArrayList;
    private ItemClick itemClick;
    private CallButtonClick callButtonClick;
    public interface ItemClick{
        public void onClick(int position);
    }
    public interface CallButtonClick{
        public void onClick(int position);
    }




    public SellMedicineListAdapter(Context context, List<SellMedicine> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    public void setCallButtonClick(CallButtonClick callButtonClick) {
        this.callButtonClick = callButtonClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public SellMedicineListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sell_medicine_list,parent,false);
        return new SellMedicineListAdapterHolder(view,itemClick,callButtonClick);
    }

    @Override
    public void onBindViewHolder(@NonNull SellMedicineListAdapterHolder holder, int position) {
        SellMedicine history=historyArrayList.get(position);
        holder.nameTV.setText("Medicine Name: "+history.getName());
        holder.priceTV.setText("Price: "+history.getPrice()+" Tk");
        holder.descTV.setText("Description: "+history.getDescription());
        holder.timeTV.setText(new SimpleDateFormat("dd-MMM-yy hh:mm:ss").format(history.getCreatedDate()));
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


    public class SellMedicineListAdapterHolder extends RecyclerView.ViewHolder{
        TextView nameTV,descTV,timeTV,priceTV,callTV;

        public SellMedicineListAdapterHolder(@NonNull View itemView, ItemClick itemClick, CallButtonClick callButtonClick) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.medicineNameId);
            descTV=itemView.findViewById(R.id.descriptionId);
            priceTV=itemView.findViewById(R.id.amountTV);
            timeTV=itemView.findViewById(R.id.timeTV);
            callTV=itemView.findViewById(R.id.callId);
            itemView.setOnClickListener(view -> {
                itemClick.onClick(getAdapterPosition());
            });
            callTV.setOnClickListener(view -> {
                callButtonClick.onClick(getAdapterPosition());
            });
        }
    }
}

