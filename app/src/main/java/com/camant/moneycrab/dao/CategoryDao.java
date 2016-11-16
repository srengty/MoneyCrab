package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.Category;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sreng on 11/16/2016.
 */

public class CategoryDao extends BaseDao<Category> {
    public CategoryDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Category> getAll(SQLiteDatabase readableDatabase) {
        ArrayList<Category> categories = new ArrayList<>();
        Category category = new Category();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+category.getTableName()+" ORDER BY ctype ASC",new String[]{});
        if(cursor.moveToFirst()){
            do{
                categories.add(buildFromCursor(cursor));
            }while(cursor.moveToNext());
        }
        return  categories;
    }

    private Category buildFromCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(0));
        category.setName(cursor.getString(1));
        category.setCtype(cursor.getInt(2));
        category.setIcon(cursor.getString(3));
        category.setAlt(cursor.getString(4));
        category.setCreated(DbUtil.longToDate(cursor.getLong(5)));
        category.setModified(DbUtil.longToDate(cursor.getLong(6)));
        return category;
    }

    @Override
    protected Category create(Category category, SQLiteDatabase writableDatabase) {
        if(category != null){
            Date now = new Date();
            category.setCreated(now);
            category.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", category.getName());
            contentValues.put("ctype", category.getCtype());
            contentValues.put("icon", category.getIcon());
            contentValues.put("alt", category.getAlt());
            contentValues.put("created", DbUtil.dateToLong(category.getCreated()));
            contentValues.put("modified", DbUtil.dateToLong(category.getModified()));
            category.setId(writableDatabase.insert(category.getTableName(), "modified", contentValues));
        }
        return category;
    }

    @Override
    protected void update(Category category, SQLiteDatabase writableDatabase) {
        if(category != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", category.getName());
            contentValues.put("ctype", category.getCtype());
            contentValues.put("icon", category.getIcon());
            contentValues.put("alt", category.getAlt());
            contentValues.put("modified", DbUtil.dateToLong(category.getModified()));
            writableDatabase.update(category.getTableName(), contentValues, "id=?",new String[]{""+category.getId()});
        }
    }

    @Override
    protected void delete(Category category, SQLiteDatabase writableDatabase) {
        if(category != null){
            writableDatabase.delete(category.getTableName(), "id=?", new String[]{""+category.getId()});
        }
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        writableDatabase.delete("categories", "id=?", new String[]{""+id});
    }

    @Override
    protected Category getById(long id, SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_CATEGORIES+" WHERE id=?",new String[]{""+id});
        if(cursor.moveToFirst()){
            return buildFromCursor(cursor);
        }
        return null;
    }
}
