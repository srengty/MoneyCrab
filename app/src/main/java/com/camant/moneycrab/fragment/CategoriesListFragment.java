package com.camant.moneycrab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camant.moneycrab.R;
import com.camant.moneycrab.adapter.CategoriesAdapter;
import com.camant.moneycrab.dao.CategoryOrmDao;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.orm.TransactionOrm;
import com.camant.moneycrab.view.CategoriesView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesListFragment extends TransactionBaseFragment implements View.OnClickListener, CategoriesAdapter.OnItemClickListener {
    private ArrayList<CategoryOrm> categoryOrms = new ArrayList<>();
    private TransactionOrm transactionOrm;

    public CategoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_list, container, false);
        CategoryOrmDao categoryOrmDao = new CategoryOrmDao(this.getActivity());
        categoryOrms.clear();
        if(getArguments() == null || !getArguments().containsKey("category_type")) {
            categoryOrms.addAll(categoryOrmDao.getAllLazily());
        }else{
            categoryOrms.addAll(categoryOrmDao.getAllLazily(getArguments().getInt("category_type")));
        }
        if(getArguments() != null && getArguments().containsKey("transaction")){
            transactionOrm = getArguments().getParcelable("transaction");
        }
        if(transactionOrm != null){
//
        }
        CategoriesView categoriesView = (CategoriesView)view.findViewById(R.id.categoriesView);
        categoriesView.setData(categoryOrms);
        categoriesView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(View view, CategoryOrm categoryOrm, int position) {
        if(transactionDataListener != null){
            transactionDataListener.onNextFieldSet(categoryOrm);
            transactionDataListener.onSubmit();
        }
    }
}
