package com.example.expensemanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_selected,parent,false);
        TextView  tvSeleted = convertView.findViewById(R.id.tvi_selected);

        Category category=this.getItem(position);
        if(category!=null){
            tvSeleted.setText(category.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner,parent,false);
        TextView  tvCategory = convertView.findViewById(R.id.tv_category);

        Category category=this.getItem(position);
        if(category!=null){
            tvCategory.setText(category.getName());
        }

        return convertView;
    }
}
