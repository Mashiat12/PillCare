package com.example.medialert.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medialert.Model.History;
import com.example.medialert.Model.Medicine;
import com.example.medialert.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder>{
    private Context context;
    private List<Medicine> historyArrayList;
    private ItemClick itemClick;
    public interface ItemClick{
        public void onClick(int position);
    }



    public HistoryAdapter(Context context, List<Medicine> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_list,parent,false);
        return new MyHolder(view,itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Medicine history=historyArrayList.get(position);
        holder.nameTV.setText(history.getName());
        holder.timeTV.setText(history.getHour()+":"+history.getMin());
        holder.descTV.setText(history.getDescription());
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        TextView nameTV,descTV,timeTV;

        public MyHolder(@NonNull View itemView, ItemClick itemClick) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTV);
            descTV=itemView.findViewById(R.id.DescriptionTV);
            timeTV=itemView.findViewById(R.id.timeTV);
        }
    }
}

