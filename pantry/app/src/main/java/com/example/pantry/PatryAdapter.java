package com.example.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter{

    private List<com.example.smartpantrymanager.PantryItem> pantryList;
    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onDeleteClick(int itemId);
    }

    public PantryAdapter(List<com.example.smartpantrymanager.PantryItem> pantryList,OnItemDeleteListener deleteListener){
        this.pantryList = pantryList;
        this.deleteListener = deleteListener;
    }
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry_ingredient, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position)
    {
        PantryItem item = pantryList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDetails.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());
        holder.btnDelete.setOnClickListener(v -&gt; {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(item.getId());
            }
        })
        ;
    }

    @Override
    public int getItemCount() { return pantryList.size(); } public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails; Button btnDelete;
        public PantryViewHolder(@NonNull View itemView) {
            super(itemView); tvName = itemView.findViewById(R.id.tvIngredientName);
            tvDetails = itemView.findViewById(R.id.tvIngredientDetails);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }

}