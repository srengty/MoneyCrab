package com.camant.moneycrab.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;

/**
 * Created by sreng on 11/15/2016.
 */

public abstract class BaseDao<T extends MoneyBase> {
    protected MoneyDbHelper dbHelper;
    public BaseDao(Context context){
        dbHelper = new MoneyDbHelper(context);
    }
    public ArrayList<T> getAll(){
        return getAll(dbHelper.getReadableDatabase());
    }
    public T create(T t){
        return create(t, dbHelper.getWritableDatabase());
    }
    public void update(T t){
        update(t, dbHelper.getWritableDatabase());
    }
    public void delete(T t){
        delete(t, dbHelper.getWritableDatabase());
    }
    public void deleteById(long id){
        deleteById(id, dbHelper.getWritableDatabase());
    }
    public T getById(long id){
        return getById(id, dbHelper.getReadableDatabase());
    }

    protected abstract ArrayList<T> getAll(SQLiteDatabase readableDatabase);
    protected abstract T create(T t, SQLiteDatabase writableDatabase);
    protected abstract void update(T t, SQLiteDatabase writableDatabase);
    protected abstract void delete(T t, SQLiteDatabase writableDatabase);
    protected abstract void deleteById(long id, SQLiteDatabase writableDatabase);
    protected abstract T getById(long id, SQLiteDatabase readableDatabase);
}
