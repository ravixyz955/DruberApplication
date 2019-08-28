package com.example.user.druberapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.druberapplication.R;
import com.example.user.druberapplication.network.model.Employee;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter {
    private static final int TYPE_CALL = 1;
    private static final int TYPE_EMAIL = 2;
    private Context mContext;
    private ArrayList<Employee> employees;

    public EmployeeAdapter(Context context, ArrayList<Employee> employees) {
        this.mContext = context;
        this.employees = employees;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_CALL) {
            view = inflater.inflate(R.layout.item_call, viewGroup, false);
            return new CallViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_email, viewGroup, false);
            return new EmailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_CALL) {
            ((CallViewHolder) viewHolder).setCallDetails(employees.get(position));
        } else {
            ((EmailViewHolder) viewHolder).setEmailDetails(employees.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(employees.get(position).getEmail()))
            return TYPE_CALL;
        else
            return TYPE_EMAIL;
    }

    class CallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;
        private TextView txtAddress;

        CallViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            itemView.setOnClickListener(this);
        }

        public void setCallDetails(Employee employee) {
            txtName.setText(employee.getName());
            txtAddress.setText(employee.getAddress());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Name: " + employees.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;
        private TextView txtAddress;

        EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            itemView.setOnClickListener(this);
        }

        public void setEmailDetails(Employee employee) {
            txtName.setText(employee.getName());
            txtAddress.setText(employee.getAddress());
        }

        @Override
        public void onClick(View view) {
//            employees.remove(getAdapterPosition());
//            notifyItemRemoved(getAdapterPosition());
//            notifyItemRangeChanged(getAdapterPosition(), employees.size());
            Toast.makeText(mContext, "Name: " + employees.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
        }
    }
}