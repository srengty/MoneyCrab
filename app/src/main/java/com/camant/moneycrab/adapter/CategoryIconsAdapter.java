package com.camant.moneycrab.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camant.moneycrab.R;
import com.camant.moneycrab.holder.CategoryIconHolder;
import com.camant.moneycrab.model.CategoryIcon;

import java.util.ArrayList;

/**
 * Created by sreng on 11/29/2016.
 */

public class CategoryIconsAdapter extends RecyclerView.Adapter<CategoryIconHolder> {
    private ArrayList<CategoryIcon> categoryIcons;
    private int selectedPos = -1;

    public CategoryIconsAdapter(ArrayList<CategoryIcon> categoryIcons) {
        this.categoryIcons = categoryIcons;
    }

    @Override
    public CategoryIconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_icon_item,parent,false);

        return new CategoryIconHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryIconHolder holder, final int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.applyData(categoryIcons.get(position), position);
        if(selectedPos == position){
            // Here I am just highlighting the background
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.background_high));
        }else{
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.background_medium));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryIcons.size();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        notifyItemChanged(selectedPos);
        this.selectedPos = selectedPos;
        notifyItemChanged(selectedPos);
    }
}
