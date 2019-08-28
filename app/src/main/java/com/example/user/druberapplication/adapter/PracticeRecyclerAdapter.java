package com.example.user.druberapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.user.druberapplication.R;
import com.example.user.druberapplication.network.model.Test;
import com.example.user.druberapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PracticeRecyclerAdapter extends RecyclerView.Adapter<PracticeRecyclerAdapter.ItemViewHolder> implements Filterable {
    private ArrayList<Test> tests, searchTests;
    private Context mContext;
    private Filter searchTestsFilter;

    {
        searchTestsFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Test> testsList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    testsList.addAll(searchTests);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (Test searchTest : searchTests) {
                        if (searchTest.getName().contains(filterPattern))
                            testsList.add(searchTest);
                    }
                }
                FilterResults results = new FilterResults();
                results.values = testsList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tests.clear();
//                ArrayList<Test> values = (ArrayList<Test>) filterResults.values;
//                tests.addAll(values);
                tests.addAll(new ArrayList<>());
                notifyDataSetChanged();
            }
        };
    }

    public PracticeRecyclerAdapter(Context context, ArrayList<Test> tests) {
        this.tests = tests;
        this.searchTests = new ArrayList<>(tests);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.practicerecycler_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        holder.name.setText(tests.get(i).getName());
        holder.age.setText(String.valueOf(tests.get(i).getAge()));
        Drawable drawable = ContextCompat.getDrawable(mContext, tests.get(i).getImage());
        holder.circleImageView.setImageDrawable(drawable);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof String) {
                holder.name.setText(payloads.get(0).toString());
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
        notifyDataSetChanged();
    }

    public void addNewItem(Test tests) {
        this.tests.add(tests);
        notifyItemInserted(this.tests.size());
        Utils.showToast(mContext, "notifyItemInserted called");
    }

    public void addNewItemsRange(ArrayList<Test> tests) {
        final int positionStart = this.tests.size() + 1;
        this.tests.addAll(tests);
        notifyItemRangeChanged(positionStart, tests.size());
        Utils.showToast(mContext, "notifyItemRangeChanged called");
    }

    @Override
    public Filter getFilter() {
        return searchTestsFilter;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, age;
        CircleImageView circleImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            circleImageView = itemView.findViewById(R.id.circle_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle(R.string.itemchangedelete).setMessage(R.string.message).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (tests.size() > 0) {
                        tests.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        Utils.showToast(mContext, "notifyItemRemoved called");
                    }
                }
            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Utils.showToast(mContext, "notifyItemChanged called");
                    notifyItemChanged(getAdapterPosition(), "NewName");
                }
            }).show();
        }
    }
}