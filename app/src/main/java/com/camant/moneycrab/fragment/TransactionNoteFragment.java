package com.camant.moneycrab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.camant.moneycrab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionNoteFragment extends TransactionBaseFragment {
    private EditText editText;

    public TransactionNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_note, container, false);
        editText = (EditText)view.findViewById(R.id.editTextNote);
        Button button = (Button)view.findViewById(R.id.buttonChooseCategory);
        final CategoriesListFragment categoriesListFragment = new CategoriesListFragment();
        categoriesListFragment.setTransactionDataListener(this.transactionDataListener);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transactionDataListener != null){
                    transactionDataListener.onFieldSet(editText.getText().toString());
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, categoriesListFragment)
                        .addToBackStack("TransactionPlus")
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                        .commit();
            }
        });
        return view;
    }

}
