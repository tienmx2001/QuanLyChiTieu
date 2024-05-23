package com.example.expensemanagerapp;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    ArrayList<Data> list;

    ExpenseAdapter eAdapter;

    //RecyclerView
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private TextView expenseTotalSum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        expenseTotalSum = myView.findViewById(R.id.expense_txt_result);

        recyclerView = myView.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        eAdapter = new ExpenseAdapter(getActivity(), list);
        recyclerView.setAdapter(eAdapter);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalvalue=0;
                list.clear();
                for(DataSnapshot mysnapshot:snapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    totalvalue+=data.getAmount();
                    list.add(data);
                    String formattedTotalSum = formatNumberWithCommas(totalvalue);
                    expenseTotalSum.setText("Tổng: "+formattedTotalSum + "đ");
                }
                eAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myView;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    public String formatNumberWithCommas(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }
}