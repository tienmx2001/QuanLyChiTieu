package com.example.expensemanagerapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanagerapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//
//
//
//    // TODO: Rename and change types and number of parameters
//    public static DashBoardFragment newInstance(String param1, String param2) {
//        DashBoardFragment fragment = new DashBoardFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    // floatinh button textview...

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private boolean isOpen=false;

    //Animation
    private Animation FadOpen,FadeClose;
    //Dashboard income and expense result;

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    // firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    //recyclerview
    ArrayList<Data> list;
    ArrayList<Data> list1;
    DashboardAdapter dbAdapter;
    ChiTieuAdapter ctAdapter;

    private FirebaseRecyclerAdapter adapter;
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_dash_board,container,false);


        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);
        //connect floating button to layout

        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn= myview.findViewById(R.id.expense_ft_btn);


        //connect floating text
        fab_income_txt= myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        //total income and expense result set..
        totalIncomeResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);
        //recycler
        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);

        //Animation

        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();
                if(isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen=false;

                }
                else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen=true;
                }
            }
        });


        //Tinh tong thu nhap
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum= 0;
                for(DataSnapshot mySnapshot: snapshot.getChildren()){
                    Data data = mySnapshot.getValue(Data.class);
                    totalsum+=data.getAmount();
                    String formattedTotalSum = formatNumberWithCommas(totalsum);
                    totalIncomeResult.setText(formattedTotalSum + "đ");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum= 0;
                for(DataSnapshot mySnapshot: snapshot.getChildren()){
                    Data data = mySnapshot.getValue(Data.class);
                    totalsum+=data.getAmount();
                    String formattedTotalSum = formatNumberWithCommas(totalsum);
                    totalExpenseResult.setText(formattedTotalSum + "đ");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Recycler
        LinearLayoutManager layoutManagerIncome= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        list = new ArrayList<>();
        list1= new ArrayList<>();
        dbAdapter = new DashboardAdapter(getActivity(),list);
        ctAdapter= new ChiTieuAdapter(getActivity(),list1);
        mRecyclerIncome.setAdapter(dbAdapter);
        mRecyclerExpense.setAdapter(ctAdapter);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot mysnapshot:snapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    list.add(data);
                }
                dbAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for(DataSnapshot mysnapshot: snapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    list1.add(data);
                }
                ctAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;
    }
    //floating button animation

    private void ftAnimantion(){
        if(isOpen){
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;

        }
        else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;
        }

    }
    private void addData(){
        //Fab Button income..
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();;
            }
        });
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }
    public void incomeDataInsert(){
        AlertDialog.Builder mydialog= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myviewm);
        AlertDialog dialog = mydialog.create();


        EditText edtAmount = myviewm.findViewById(R.id.ammount_edt);
        EditText edtType = myviewm.findViewById(R.id.type_edt);
        EditText edtNote = myviewm.findViewById(R.id.note_edt);

        Button btnSave = myviewm.findViewById(R.id.btnSave);
        Button btnCancel =  myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type= edtType.getText().toString().trim();
                String amount= edtAmount.getText().toString().trim();
                String note=  edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount)){
                    edtAmount.setError("Required Field..");
                    return;
                }

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Required Field..");
                    return;
                }

                int ourammontint= Integer.parseInt(amount);

                if(TextUtils.isEmpty(note)){
                    edtNote.setError("Required Field..");
                    return;
                }

                String id=mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data= new Data(ourammontint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Thêm thu nhập thành công",Toast.LENGTH_SHORT).show();

                ftAnimantion();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimantion();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void expenseDataInsert(){
        AlertDialog.Builder mydialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  LayoutInflater.from(getActivity());

        View myview= inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);


        EditText amount = myview.findViewById(R.id.ammount_edt);
        EditText type= myview.findViewById(R.id.type_edt);
        EditText note=  myview.findViewById(R.id.note_edt);


        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel =  myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmAmount = amount.getText().toString().trim();
                String tmType = type.getText().toString().trim();
                String tmNote = note.getText().toString().trim();

                if(TextUtils.isEmpty(tmAmount)){
                    amount.setError("Required Field..");
                    return;
                }

                if(TextUtils.isEmpty(tmType)){
                    type.setError("Required Field..");
                    return;
                }
                int inamount =Integer.parseInt(tmAmount);

                String id=mExpenseDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data= new Data(inamount,tmType,tmNote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Thêm chi tiêu thành công",Toast.LENGTH_SHORT).show();


                ftAnimantion();
                dialog.dismiss();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimantion();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public String formatNumberWithCommas(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }

}