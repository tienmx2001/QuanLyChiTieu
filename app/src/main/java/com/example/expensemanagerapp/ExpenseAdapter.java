package com.example.expensemanagerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerapp.Model.Data;
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

public class ExpenseAdapter  extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>{


    Context context;
    ArrayList<Data> list;

    //firebase
    private DatabaseReference mIncomeDatabase;
    private FirebaseAuth mAuth;
    //update editText
    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    //button for update and delete
    private Button btnUpdate;
    private Button btnDelete;
    //data item value
    private String type;
    private String note;
    private int ammount;
    private String post_key;
    public ExpenseAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.expense_reclycler_data,parent,false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        return new ExpenseAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data= list.get(position);
        holder.mType.setText("Danh mục: "+data.getType());
        holder.mNote.setText("Ghi chú: "+data.getNote());
        holder.mDate.setText(data.getDate());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(data.getAmount());
        holder.mAmmount.setText(formattedAmount+"đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Data data = dataSnapshot.getValue(Data.class);
                            if (data != null && data.getType().equals(type) && data.getNote().equals(note) && data.getAmount() == ammount) {
                                post_key = dataSnapshot.getKey();
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                type= data.getType();
                note= data.getNote();
                ammount= data.getAmount();

                updateDataitem();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mType,mNote,mDate,mAmmount;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mType = itemView.findViewById(R.id.type_txt_expense);
            mNote = itemView.findViewById(R.id.note_txt_expense);
            mDate = itemView.findViewById(R.id.date_txt_expense);
            mAmmount = itemView.findViewById(R.id.ammount_txt_expense);


        }
    }

    private void updateDataitem() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View myview = inflater.inflate(R.layout.update_data_item, null);
        mydialog.setView(myview);

        edtAmmount=myview.findViewById(R.id.ammount_edt);
        edtType= myview.findViewById(R.id.type_edt);
        edtNote=  myview.findViewById(R.id.note_edt);

        //set data to editText;
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmmount.setText(String.valueOf(ammount));
        edtAmmount.setSelection(String.valueOf(ammount).length());

        btnUpdate=myview.findViewById(R.id.btn_upd_Update);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String mdammount=String.valueOf(ammount);

                mdammount=  edtAmmount.getText().toString().trim();

                int myAmmount = Integer.parseInt(mdammount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data = new Data(myAmmount,type,note,post_key,mDate);

                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
