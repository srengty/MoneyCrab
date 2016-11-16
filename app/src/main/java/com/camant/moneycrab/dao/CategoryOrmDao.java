package com.camant.moneycrab.dao;

import android.content.Context;

import com.camant.moneycrab.model.Category;
import com.camant.moneycrab.orm.CategoryOrm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreng on 11/16/2016.
 */

public class CategoryOrmDao extends CategoryDao {
    private CategoryTypeDao categoryTypeDao;
    public CategoryOrmDao(Context context) {
        super(context);
        categoryTypeDao = new CategoryTypeDao(context);
    }
    public ArrayList<CategoryOrm> getAllLazily(){
        ArrayList<Category> categories = getAll();
        ArrayList<CategoryOrm> categoryOrms = new ArrayList<>();
        CategoryOrm categoryOrm;
        for(Category c:categories){
            categoryOrm = new CategoryOrm(c);
            categoryOrm.setCategoryType(categoryTypeDao.getById(c.getCtype()));
            categoryOrms.add(categoryOrm);
        }
        return categoryOrms;
    }
    public CategoryOrm getByIdLazily(long id){
        CategoryOrm categoryOrm = new CategoryOrm(getById(id));
        categoryOrm.setCategoryType(categoryTypeDao.getById(categoryOrm.getCtype()));
        return categoryOrm;
    }
}
