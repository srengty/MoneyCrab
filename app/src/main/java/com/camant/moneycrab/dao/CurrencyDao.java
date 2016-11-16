package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sreng on 11/16/2016.
 */

public class CurrencyDao extends BaseDao<com.camant.moneycrab.model.Currency> {
    public CurrencyDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<com.camant.moneycrab.model.Currency> getAll(SQLiteDatabase readableDatabase) {
        Currency currency = new Currency();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+currency.getTableName(),new String[]{});
        ArrayList<Currency> currencies = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                currencies.add(buildFromCursor(cursor));
            }while (cursor.moveToNext());
        }
        return currencies;
    }

    private Currency buildFromCursor(Cursor cursor) {
        Currency currency = new Currency();
        currency.setId(cursor.getLong(0));
        currency.setName(cursor.getString(1));
        currency.setAlt(cursor.getString(2));
        currency.setCreated(DbUtil.longToDate(cursor.getLong(3)));
        currency.setModified(DbUtil.longToDate(cursor.getLong(4)));
        return currency;
    }

    @Override
    protected Currency create(Currency currency, SQLiteDatabase writableDatabase) {
        if(currency != null){
            Date now = new Date();
            currency.setCreated(now);
            currency.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",currency.getName());
            contentValues.put("alt",currency.getAlt());
            contentValues.put("created",DbUtil.dateToLong(currency.getCreated()));
            contentValues.put("modified",DbUtil.dateToLong(currency.getModified()));
            currency.setId(writableDatabase.insert(currency.getTableName(), "modified", contentValues));
            return currency;
        }
        return null;
    }

    @Override
    protected void update(Currency currency, SQLiteDatabase writableDatabase) {
        if(currency != null){
            Date now = new Date();
            currency.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",currency.getName());
            contentValues.put("alt",currency.getAlt());
            contentValues.put("modified",DbUtil.dateToLong(currency.getModified()));
            writableDatabase.update(currency.getTableName(), contentValues, "id=?",new String[]{""+currency.getId()});
        }
    }

    @Override
    protected void delete(Currency currency, SQLiteDatabase writableDatabase) {
        if(currency != null){
            writableDatabase.delete(currency.getTableName(), "id=?", new String[]{""+currency.getId()});
        }
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        if(id > 0){
            writableDatabase.delete("currencies", "id=?", new String[]{""+id});
        }
    }

    @Override
    protected Currency getById(long id, SQLiteDatabase readableDatabase) {
        if(id > 0){
            Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_CURRENCIES+" WHERE id=?",new String[]{""+id});
            if(cursor.moveToFirst()){
                return buildFromCursor(cursor);
            }
        }
        return null;
    }
}
