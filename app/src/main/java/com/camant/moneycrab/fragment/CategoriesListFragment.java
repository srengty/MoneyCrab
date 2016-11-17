package com.camant.moneycrab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camant.moneycrab.R;
import com.camant.moneycrab.dao.CategoryOrmDao;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.view.CategoriesView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesListFragment extends Fragment implements View.OnClickListener {
    private ArrayList<CategoryOrm> categoryOrms = new ArrayList<>();

    public CategoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_list, container, false);
        CategoriesView categoriesView = (CategoriesView)view.findViewById(R.id.categoriesView);
        categoriesView.setData(categoryOrms);
        CategoryOrmDao categoryOrmDao = new CategoryOrmDao(this.getActivity());
        categoryOrms.clear();
        categoryOrms.addAll(categoryOrmDao.getAllLazily());
        categoriesView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
