package com.example.expensemanagerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    ArrayList<Data> list;

    private Spinner spnCategory;
    private CategoryAdapter categoryAdapter;
    //
    private TextView incomTotalSum;
    private TextView expenseTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myview= inflater.inflate(R.layout.fragment_statistics, container, false);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        incomTotalSum=myview.findViewById(R.id.income_set_result);

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);
        expenseTotalSum = myview.findViewById(R.id.expense_set_result);

        list =  new ArrayList<>();

        spnCategory = myview.findViewById(R.id.spn_category);
        categoryAdapter= new CategoryAdapter(getActivity(),R.layout.item_spinner_selected,getListCategory());
        spnCategory.setAdapter(categoryAdapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Integer selectedMonth = Integer.valueOf(position + 1); // Vị trí bắt đầu từ 0, nên cộng thêm 1
                // Gọi phương thức để xử lý dữ liệu dựa trên tháng đã chọn
                if(selectedMonth==11){
                    handleDataForSelectedMonth(selectedMonth);
                }
                else{
                    order1(selectedMonth);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return myview;
    }
    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Tháng 1"));
        list.add(new Category("Tháng 2"));
        list.add(new Category("Tháng 3"));
        list.add(new Category("Tháng 4"));
        list.add(new Category("Tháng 5"));
        list.add(new Category("Tháng 6"));
        list.add(new Category("Tháng 7"));
        list.add(new Category("Tháng 8"));
        list.add(new Category("Tháng 9"));
        list.add(new Category("Tháng 10"));
        list.add(new Category("Tháng 11"));
        list.add(new Category("Tháng 12"));

        return list;
    }
    private void order1(Integer Month){
        String result = "Tháng " + Month + ": " +  "0đ";
        incomTotalSum.setText(result);


        String result1 = "Tháng " + Month + ": " +  "0đ";
        expenseTotalSum.setText(result1);

    }

    private void handleDataForSelectedMonth(Integer selectedMonth) {
//        Date startDate = new Date();
//        Date endDate = new Date();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String startDateStr = dateFormat.format(startDate);
//        String endDateStr = dateFormat.format(endDate);
//        Query query = mIncomeDatabase
//                .orderByChild("date")
//                .startAt(startDateStr)
//                .endAt(endDateStr);
//        Query query = mIncomeDatabase
//                .orderByChild("date")
//                .startAt("1 " + "thg "+selectedMonth + ", 2023" )
//                .endAt("31 " + "thg "+selectedMonth + ", 2023" );

        mIncomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalIncomeForMonth = 0;

                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    totalIncomeForMonth += data.getAmount();
                    // Hiển thị tổng thu nhập của tháng đã chọn
                    String formattedTotalSum = formatNumberWithCommas(totalIncomeForMonth);
                    String result = "Tháng " + selectedMonth + ": " + formattedTotalSum + "đ";
                    incomTotalSum.setText(result);
                }


                // Đưa kết quả hiển thị lên giao diện người dùng


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Query query1 = mExpenseDatabase
//                .orderByChild("date")
//                .startAt("1 " + "thg "+selectedMonth + ", 2023" )
//                .endAt("31 " + "thg "+selectedMonth + ", 2023" );
        mExpenseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalIncomeForMonth = 0;

                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    totalIncomeForMonth += data.getAmount();
                    // Hiển thị tổng thu nhập của tháng đã chọn
                    String formattedTotalSum = formatNumberWithCommas(totalIncomeForMonth);
                    String result = "Tháng " + selectedMonth + ": " + formattedTotalSum + "đ";
                    // Đưa kết quả hiển thị lên giao diện người dùng
                    expenseTotalSum.setText(result);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String formatNumberWithCommas(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }
}