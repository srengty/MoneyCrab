package com.camant.moneycrab.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.camant.moneycrab.R;
import com.camant.moneycrab.adapter.CategoriesAdapter;
import com.camant.moneycrab.orm.CategoryOrm;

import java.util.ArrayList;

/**
 * Created by sreng on 11/17/2016.
 */

public class CategoriesView extends RelativeLayout {
    RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private CategoriesAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<CategoryOrm> categoryOrms = new ArrayList<>();

    /*
    called by creation programmatically
     */
    public CategoriesView(Context context) {
        super(context);
        init(context, null, 0);
    }

    /*
    called by XML
     */
    public CategoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CategoriesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setData(ArrayList<CategoryOrm> categoryOrms){
        this.categoryOrms = categoryOrms;
        if(categoriesAdapter != null){
            categoriesAdapter.setCategoryOrms(categoryOrms);
        }
    }

    /**
     * The layout to be inflated should be merge layout
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.categories_view_layout, this, true);
        recyclerViewCategories = (RecyclerView)findViewById(R.id.recyclerViewCategories);
        if(recyclerViewCategories != null){
            categoriesAdapter = new CategoriesAdapter(categoryOrms);
            recyclerViewCategories.setAdapter(categoriesAdapter);
        }
    }

    public void setOnItemClickListener(CategoriesAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if(categoriesAdapter != null){
            categoriesAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
}
