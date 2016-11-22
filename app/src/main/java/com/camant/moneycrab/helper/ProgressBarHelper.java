package com.camant.moneycrab.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.camant.moneycrab.R;

/**
 * Created by sreng on 11/22/2016.
 */

public class ProgressBarHelper {
    private static ProgressDialog progressDialog;
    public static void showLoadingDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
    }
    public static void hideLoadingDialog(){
        if(progressDialog != null) progressDialog.dismiss();
    }
    public static AlertDialog buildAlertDialog(Context context, String text, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title != null){
            builder.setTitle(title);
        }
        builder.setMessage(text);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}
