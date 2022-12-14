package com.example.budgetapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.R;
import com.example.budgetapp.data.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

public class WeekSpendingAdapter extends RecyclerView.Adapter<WeekSpendingAdapter.ViewHolder> {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private Context mContext;
    private List<Data> myDataList;
    private String mAuth = "";

    public WeekSpendingAdapter(Context mContext, List<Data> myDataList, String mAuth) {
        this.mContext = mContext;
        this.myDataList = myDataList;
        this.mAuth = mAuth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        databaseReference = firebaseDatabase.getReference("expenses").child(mAuth);
        return new WeekSpendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        Data data = myDataList.get(position);

        holder.item.setText("Item: " + data.getItem());
        holder.amount.setText("Amount: " + df.format(data.getAmount()));
        holder.date.setText("On: " + data.getDate());
        holder.notes.setText("Notes: " + data.getNotes());
        switch (data.getItem()) {
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_food);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Apparel":
                holder.imageView.setImageResource(R.drawable.ic_shirt);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_health);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personalcare);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
            case "Select Item":
                holder.imageView.setImageResource(R.drawable.ic_other);
            default:
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public void setData(List<Data> myDataList) {
        this.myDataList = myDataList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item, amount, notes, date;
        public ImageView imageView;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View mView = inflater.inflate(R.layout.update_layout, null);
            myDialog.setView(mView);

            final AlertDialog dialog = myDialog.create();
            final TextView mItem = mView.findViewById(R.id.itemname);
            final EditText mAmount = mView.findViewById(R.id.amount);
            final EditText mNotes = mView.findViewById(R.id.note);

            Data itemData = myDataList.get(getAdapterPosition());
            mItem.setText(itemData.getItem());
            mAmount.setText(String.valueOf(itemData.getAmount()));
            mAmount.setSelection(String.valueOf(itemData.getAmount()).length());

            mNotes.setText(itemData.getNotes());
            mNotes.setSelection(itemData.getNotes().length());

            Button btnDelete = mView.findViewById(R.id.btndelete);
            Button btnUpdate = mView.findViewById(R.id.btnupdate);

            btnUpdate.setOnClickListener(v -> {
                Data data = new Data();
                data.setNotes(mNotes.getText().toString());
                data.setAmount(Integer.parseInt(mAmount.getText().toString()));
                databaseReference.child(itemData.getId()).updateChildren(data.toMap()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(mContext, "Update Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            });

            btnDelete.setOnClickListener(v -> {
                databaseReference.child(itemData.getId()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            });
            dialog.show();
        }
    }
}