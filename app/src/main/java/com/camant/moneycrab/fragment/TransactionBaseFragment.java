package com.camant.moneycrab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camant.moneycrab.helper.TransactionDataListener;

/**
 * Created by Institute on 11/18/2016.
 */

public class TransactionBaseFragment extends Fragment {
    protected TransactionDataListener transactionDataListener;

    public TransactionBaseFragment() { }

    public void setTransactionDataListener(TransactionDataListener transactionDataListener) {
        this.transactionDataListener = transactionDataListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
