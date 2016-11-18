package com.camant.moneycrab.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.camant.moneycrab.R;
import com.camant.moneycrab.holder.CategoryHolder;
import com.camant.moneycrab.orm.CategoryOrm;

import java.util.ArrayList;

/**
 * Created by sreng on 11/17/2016.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private ArrayList<CategoryOrm> categoryOrms = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public CategoriesAdapter(ArrayList<CategoryOrm> categoryOrms){
        this.categoryOrms = categoryOrms;
    }
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_view_layout, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.showData(categoryOrms.get(position), position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return categoryOrms.size();
    }

    public void setCategoryOrms(ArrayList<CategoryOrm> categoryOrms) {
        this.categoryOrms = categoryOrms;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, CategoryOrm categoryOrm, int position);
    }
}
