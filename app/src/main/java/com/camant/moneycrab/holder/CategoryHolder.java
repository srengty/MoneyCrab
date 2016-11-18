package com.camant.moneycrab.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.camant.moneycrab.R;
import com.camant.moneycrab.adapter.CategoriesAdapter;
import com.camant.moneycrab.orm.CategoryOrm;

/**
 * Created by sreng on 11/17/2016.
 */

public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView imageViewIcon;
    private TextView textViewName;
    private CategoriesAdapter.OnItemClickListener onItemClickListener;
    public CategoryHolder(View itemView) {
        super(itemView);
        imageViewIcon = (ImageView)itemView.findViewById(R.id.imageViewIcon);
        textViewName = (TextView)itemView.findViewById(R.id.textViewName);
        itemView.setOnClickListener(this);
    }
    public void showData(CategoryOrm categoryOrm, int position, CategoriesAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
        if(imageViewIcon != null){
            Glide.with(itemView.getContext())
                    .load(categoryOrm.getIcon())
                    .placeholder(R.drawable.ic_wallpaper_black_300dp)
                    .error(R.drawable.ic_wallpaper_black_300dp)
                    .into(imageViewIcon);
        }
        if(textViewName != null){
            textViewName.setText(categoryOrm.getName());
        }
        itemView.setTag(1,categoryOrm);
        itemView.setTag(2, position);
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(itemView, (CategoryOrm)itemView.getTag(1),(Integer)itemView.getTag(2));
        }
    }
}
