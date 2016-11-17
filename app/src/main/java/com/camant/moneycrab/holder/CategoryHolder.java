package com.camant.moneycrab.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.camant.moneycrab.R;
import com.camant.moneycrab.orm.CategoryOrm;

/**
 * Created by sreng on 11/17/2016.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewIcon;
    private TextView textViewName;
    public CategoryHolder(View itemView) {
        super(itemView);
        imageViewIcon = (ImageView)itemView.findViewById(R.id.imageViewIcon);
        textViewName = (TextView)itemView.findViewById(R.id.textViewName);
    }
    public void showData(CategoryOrm categoryOrm){
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
    }
}
