package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sreng on 11/16/2016.
 */

public class CategoryTypeDao extends BaseDao<CategoryType> {
    public CategoryTypeDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<CategoryType> getAll(SQLiteDatabase readableDatabase) {
        ArrayList<CategoryType> categoryTypes = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_CATEGORY_TYPES,new String[]{});
        if(cursor.moveToFirst()){
            do{
                categoryTypes.add(buildFromCursor(cursor));
            }while(cursor.moveToNext());
        }
        return null;
    }

    private CategoryType buildFromCursor(Cursor cursor) {
        CategoryType categoryType = new CategoryType();
        categoryType.setId(cursor.getLong(0));
        categoryType.setName(cursor.getString(1));
        categoryType.setCreated(DbUtil.longToDate(cursor.getLong(2)));
        categoryType.setModified(DbUtil.longToDate(cursor.getLong(3)));
        return categoryType;
    }

    @Override
    protected CategoryType create(CategoryType categoryType, SQLiteDatabase writableDatabase) {
        if(categoryType != null){
            Date now = new Date();
            categoryType.setCreated(now);
            categoryType.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", categoryType.getName());
            contentValues.put("created", DbUtil.dateToLong(categoryType.getCreated()));
            contentValues.put("modified", DbUtil.dateToLong(categoryType.getModified()));
            categoryType.setId(writableDatabase.insert(MoneyDbHelper.TABLE_CATEGORY_TYPES, "modified", contentValues));
        }
        return categoryType;
    }

    @Override
    protected void update(CategoryType categoryType, SQLiteDatabase writableDatabase) {
        if(categoryType != null){
            Date now = new Date();
            categoryType.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", categoryType.getName());
            contentValues.put("modified", DbUtil.dateToLong(categoryType.getModified()));
            writableDatabase.update(MoneyDbHelper.TABLE_CATEGORY_TYPES, contentValues, "id=?", new String[]{""+categoryType.getId()});
        }
    }

    @Override
    protected void delete(CategoryType categoryType, SQLiteDatabase writableDatabase) {
        if(categoryType != null){
            deleteById(categoryType.getId(), writableDatabase);
        }
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        writableDatabase.delete(MoneyDbHelper.TABLE_CATEGORY_TYPES, "id=?", new String[]{""+id});
    }

    @Override
    protected CategoryType getById(long id, SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+MoneyDbHelper.TABLE_CATEGORY_TYPES + " WHERE id=?", new String[]{""+id});
        if(cursor.moveToFirst()){
            return buildFromCursor(cursor);
        }
        return null;
    }
}
