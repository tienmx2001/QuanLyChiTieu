package com.example.expensemanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerapp.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChiTieuAdapter extends RecyclerView.Adapter<ChiTieuAdapter.MyViewHolder>{
    Context context;
    ArrayList<Data> list;

    public ChiTieuAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dashboard_expense,parent,false);

        return new ChiTieuAdapter.MyViewHolder(v);
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
        TextView mType,mNote,mDate,mAmmount;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mType = itemView.findViewById(R.id.type_expense_ds);
            mDate = itemView.findViewById(R.id.date_expense_ds);
            mAmmount = itemView.findViewById(R.id.ammoun_expense_ds);


        }
    }
}
