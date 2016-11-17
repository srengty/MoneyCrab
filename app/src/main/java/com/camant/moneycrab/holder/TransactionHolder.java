package com.camant.moneycrab.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camant.moneycrab.R;
import com.camant.moneycrab.orm.TransactionOrm;

/**
 * Created by sreng on 11/17/2016.
 */

public class TransactionHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewIcon;
    private TextView textViewName;
    public TransactionHolder(View itemView) {
        super(itemView);
        imageViewIcon = (ImageView)itemView.findViewById(R.id.imageViewIcon);
        textViewName = (TextView)itemView.findViewById(R.id.textViewName);
    }
    public void showData(TransactionOrm transactionOrm){
        if(imageViewIcon != null){

        }
    }
}
