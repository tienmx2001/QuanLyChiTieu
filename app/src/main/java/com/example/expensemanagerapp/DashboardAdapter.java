package com.example.expensemanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerapp.Model.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    Context context;
    ArrayList<Data> list;

    public DashboardAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dashbord_income,parent,false);


        return new DashboardAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data= list.get(position);
        holder.mType.setText(data.getType());
        holder.mDate.setText(data.getDate());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(data.getAmount());
        holder.mAmmount.setText(formattedAmount+"đ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mType,mDate,mAmmount;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mType = itemView.findViewById(R.id.type_income_ds);
            mDate = itemView.findViewById(R.id.date_income_ds);
            mAmmount = itemView.findViewById(R.id.ammoun_income_ds);

        }
    }
}
