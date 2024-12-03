package com.example.expensemanagerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanagerapp.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    ArrayList<Data> incomeList, expenseList;

    private Spinner spnCategory;
    private CategoryAdapter categoryAdapter;

    private TextView incomTotalSum;
    private TextView expenseTotalSum;

    private RecyclerView recyclerIncome, recyclerExpense;
    private InComeAdapter incomeAdapter;
    private ExpenseAdapter expenseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_statistics, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        incomTotalSum = myview.findViewById(R.id.income_set_result);
        expenseTotalSum = myview.findViewById(R.id.expense_set_result);

        recyclerIncome = myview.findViewById(R.id.recycler_income);
        recyclerExpense = myview.findViewById(R.id.recycler_expense);

        incomeList = new ArrayList<>();
        expenseList = new ArrayList<>();

        incomeAdapter = new InComeAdapter(getContext(), incomeList);
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList);

        recyclerIncome.setAdapter(incomeAdapter);
        recyclerExpense.setAdapter(expenseAdapter);

        recyclerIncome.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExpense.setLayoutManager(new LinearLayoutManager(getContext()));

        spnCategory = myview.findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.item_spinner_selected, getListCategory());
        spnCategory.setAdapter(categoryAdapter);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedMonth = position + 1; // Position starts at 0, so add 1 to get month (1-12)
                loadIncomeData(selectedMonth);
                loadExpenseData(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return myview;
    }

    private void loadIncomeData(int month) {
        Query incomeQuery = mIncomeDatabase.orderByChild("date");
        incomeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalIncome = 0;
                incomeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    String dateString = data.getDate(); // Assume date is in "d 'thg' M, yyyy" format
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d 'thg' M, yyyy", Locale.forLanguageTag("vi-VN"));
                        Date date = dateFormat.parse(dateString);
                        if (new SimpleDateFormat("MM").format(date).equals(String.valueOf(month))) {
                            totalIncome += data.getAmount(); // Sum amounts for the selected month
                            incomeList.add(data); // Add the data to list for displaying in RecyclerView
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                incomTotalSum.setText(new DecimalFormat("#,###.##").format(totalIncome)); // Display formatted total
                incomeAdapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load income data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadExpenseData(int month) {
        Query expenseQuery = mExpenseDatabase.orderByChild("date");
        expenseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalExpense = 0;
                expenseList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    String dateString = data.getDate(); // Assume date is in "d 'thg' M, yyyy" format
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d 'thg' M, yyyy", Locale.forLanguageTag("vi-VN"));
                        Date date = dateFormat.parse(dateString);
                        if (new SimpleDateFormat("MM").format(date).equals(String.valueOf(month))) {
                            totalExpense += data.getAmount(); // Sum amounts for the selected month
                            expenseList.add(data); // Add the data to list for displaying in RecyclerView
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                expenseTotalSum.setText(new DecimalFormat("#,###.##").format(totalExpense)); // Display formatted total
                expenseAdapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load expense data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to return list of months
    private List<Category> getListCategory() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("January"));
        categories.add(new Category("February"));
        categories.add(new Category("March"));
        categories.add(new Category("April"));
        categories.add(new Category("May"));
        categories.add(new Category("June"));
        categories.add(new Category("July"));
        categories.add(new Category("August"));
        categories.add(new Category("September"));
        categories.add(new Category("October"));
        categories.add(new Category("November"));
        categories.add(new Category("December"));
        return categories;
    }
}