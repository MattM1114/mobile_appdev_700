package com.example.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    /** Lets the Activity decide what happens when a row is tapped or its Delete button is pressed. */
    public interface OnItemActionListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> pantryList;
    private final OnItemActionListener listener;

    public PantryAdapter(List<PantryItem> pantryList, OnItemActionListener listener) {
        this.pantryList = pantryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry_ingredient, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        final PantryItem item = pantryList.get(position);

        holder.tvName.setText(item.getName());

        StringBuilder details = new StringBuilder("Quantity: " + formatQuantity(item.getQuantity()));
        if (item.getUnit() != null && !item.getUnit().isEmpty()) {
            details.append(" ").append(item.getUnit());
        }
        if (item.getExpiryDate() != null) {
            details.append("\nExpires: ").append(item.getExpiryDate());
        }
        holder.tvDetails.setText(details.toString());

        // Tapping the row edits the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    // Shows 2 instead of 2.0, but keeps 1.5 as 1.5
    private String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDetails;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvDetails = itemView.findViewById(R.id.tvIngredientDetails);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}