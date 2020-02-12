package com.example.fest19_android.ui.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fest19_android.R;
import com.example.fest19_android.data.model.HealthEntry;

public class HealthAdapter extends ListAdapter<HealthEntry, HealthAdapter.HealthHolder> {
    private final HealthListActivity mParentActivity;
    private final boolean mTwoPane;
    private final HealthViewModel healthViewModel;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HealthEntry item = (HealthEntry) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(HealthDetailFragment.ARG_ITEM_ID, item.id);
                HealthDetailFragment fragment = new HealthDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.health_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, HealthDetailActivity.class);
                intent.putExtra(HealthDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };
    private final View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HealthEntry tag = (HealthEntry) v.getTag();
            assert tag!=null;
            healthViewModel.delete(tag).observe(mParentActivity, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean ok) {
                    if (!ok) Toast.makeText(mParentActivity, "Can't delete when net is off. LOL", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    HealthAdapter(HealthListActivity parent, boolean twoPane, HealthViewModel healthViewModel) {
        super(DIFF_CALLBACK);
        mParentActivity = parent;
        mTwoPane = twoPane;
        this.healthViewModel = healthViewModel;
    }

    private static final DiffUtil.ItemCallback<HealthEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<HealthEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull HealthEntry oldItem, @NonNull HealthEntry newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull HealthEntry oldItem, @NonNull HealthEntry newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public HealthAdapter.HealthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_list_content, parent, false);
        return new HealthAdapter.HealthHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthAdapter.HealthHolder holder, int position) {
        HealthEntry item = getItem(position);
        holder.bindTo(item);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.deleteButton.setTag(item);
        holder.deleteButton.setOnClickListener(deleteListener);
    }

    static class HealthHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;
        final Button deleteButton;

        HealthHolder(@NonNull View itemView) {
            super(itemView);
            mIdView = itemView.findViewById(R.id.id_text);
            mContentView = itemView.findViewById(R.id.content);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }


        void bindTo(HealthEntry item) {
            mIdView.setText(item.id);
            mContentView.setText(item.toString());
        }
    }
}
