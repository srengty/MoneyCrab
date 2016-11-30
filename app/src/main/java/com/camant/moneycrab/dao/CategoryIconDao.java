package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.CategoryIcon;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Institute on 11/30/2016.
 */

public class CategoryIconDao extends BaseDao<CategoryIcon> {
    public CategoryIconDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<CategoryIcon> getAll(SQLiteDatabase readableDatabase) {
        ArrayList<CategoryIcon> categoryIcons = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_ICONS,new String[]{});
        if(cursor.moveToFirst()){
            do{
                categoryIcons.add(buildFromCursor(cursor));
            }while (cursor.moveToNext());
        }
        return categoryIcons;
    }

    private CategoryIcon buildFromCursor(Cursor cursor) {
        CategoryIcon categoryIcon = new CategoryIcon();
        categoryIcon.setId(cursor.getInt(0));
        categoryIcon.setName(cursor.getString(1));
        categoryIcon.setAlt(cursor.getString(2));
        categoryIcon.setPath(cursor.getString(3));
        categoryIcon.setCreated(DbUtil.longToDate(cursor.getLong(4)));
        categoryIcon.setModified(DbUtil.longToDate(cursor.getLong(5)));
        return categoryIcon;
    }

    @Override
    protected CategoryIcon create(CategoryIcon categoryIcon, SQLiteDatabase writableDatabase) {
        Date now = new Date();
        categoryIcon.setCreated(now);
        categoryIcon.setModified(now);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", categoryIcon.getName());
        contentValues.put("alt", categoryIcon.getAlt());
        contentValues.put("path", categoryIcon.getPath());
        contentValues.put("created", DbUtil.dateToLong(categoryIcon.getCreated()));
        contentValues.put("modified", DbUtil.dateToLong(categoryIcon.getCreated()));
        categoryIcon.setId(writableDatabase.insert(categoryIcon.getTableName(), "modified", contentValues));
        return categoryIcon;
    }

    @Override
    protected void update(CategoryIcon categoryIcon, SQLiteDatabase writableDatabase) {
        Date now = new Date();
        categoryIcon.setModified(now);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", categoryIcon.getName());
        contentValues.put("alt", categoryIcon.getAlt());
        contentValues.put("path", categoryIcon.getPath());
        contentValues.put("modified", DbUtil.dateToLong(categoryIcon.getCreated()));
        writableDatabase.update(categoryIcon.getTableName(), contentValues, "id=?", new String[]{""+categoryIcon.getId()});
    }

    @Override
    protected void delete(CategoryIcon categoryIcon, SQLiteDatabase writableDatabase) {
        if(categoryIcon != null){
            deleteById(categoryIcon.getId(), writableDatabase);
        }
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        writableDatabase.delete(MoneyDbHelper.TABLE_ICONS, "id=?", new String[]{""+id});
    }

    @Override
    protected CategoryIcon getById(long id, SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_ICONS+" WHERE id=?",new String[]{""+id});
        if(cursor.moveToFirst()){
            return buildFromCursor(cursor);
        }
        return null;
    }
}
