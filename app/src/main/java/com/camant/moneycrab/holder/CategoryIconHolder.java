package com.camant.moneycrab.holder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camant.moneycrab.R;
import com.camant.moneycrab.model.CategoryIcon;

import java.io.IOException;

/**
 * Created by sreng on 11/29/2016.
 */

public class CategoryIconHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewIcon;
    private TextView textViewName;
    public CategoryIconHolder(final View itemView) {
        super(itemView);
        imageViewIcon = (ImageView)itemView.findViewById(R.id.imageViewIcon);
        textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        if(imageViewIcon != null){
            imageViewIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.callOnClick();
                }
            });
        }
    }

    public void applyData(CategoryIcon categoryIcon, int position) {
        if(categoryIcon == null){
            Log.e(getClass().getSimpleName(), "Position "+position+" is null!");
            return;
        }
        if(imageViewIcon != null){
            try {
                imageViewIcon.setImageDrawable(
                        Drawable.createFromStream(
                                itemView.getContext().getAssets().open("icons/"+categoryIcon.getPath()),
                                categoryIcon.getName()));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(),Log.getStackTraceString(e));
            }
        }
        if(textViewName != null){
            textViewName.setText(categoryIcon.getName());
        }
    }
}
