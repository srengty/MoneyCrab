package com.camant.moneycrab.helper;

import android.content.Context;

import com.camant.moneycrab.dao.CategoryOrmDao;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.orm.CategoryOrm;

import java.util.ArrayList;

/**
 * Created by sreng on 11/29/2016.
 */

public class CategoryHelper extends MoneyBaseHelper {
    private CategoryOrmDao categoryOrmDao;

    public CategoryHelper(Context context) {
        super(context);
        this.categoryOrmDao = new CategoryOrmDao(context);
    }

    public ArrayList<MoneyBase> loadCategories(ArrayList<MoneyBase> categories){
        if(categories == null) categories = new ArrayList<>();
        ArrayList<CategoryOrm> categoryOrms = categoryOrmDao.getAllLazily();
        categories.clear();
        String name = "";
        for(CategoryOrm co:categoryOrms){
            if(!name.equals(co.getCategoryType().getName())){
                categories.add(co.getCategoryType());
                name = co.getCategoryType().getName();
            }
            categories.add(co);
        }
        return categories;
    }
}
