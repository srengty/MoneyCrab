package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.orm.AccountOrm;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sreng on 11/15/2016.
 */

public class AccountDao extends BaseDao<Account> {
    public AccountDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Account> getAll(SQLiteDatabase readableDatabase) {
        ArrayList<Account> accounts = new ArrayList<>();
        Account account = new Account();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+account.getTableName(), new String[]{});
        if(cursor.moveToFirst()){
            do{
                account = buildAccountFromCursor(cursor);
                accounts.add(account);
            }while(cursor.moveToNext());
        }
        return accounts;
    }

    @Override
    protected Account create(Account account, SQLiteDatabase writableDatabase) {
        if(account != null){
            Date now = new Date();
            account.setCreated(now);
            account.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",account.getName());
            contentValues.put("currency",account.getCurrencyId());
            contentValues.put("alt",account.getName());
            contentValues.put("created",DbUtil.dateToLong(account.getCreated()));
            contentValues.put("modified",DbUtil.dateToLong(account.getModified()));
            account.setId(writableDatabase.insert(account.getTableName(), "modified", contentValues));
        }
        return account;
    }

    @Override
    protected void update(Account account, SQLiteDatabase writableDatabase) {
        if(account != null){
            Date now = new Date();
            account.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",account.getName());
            contentValues.put("currency",account.getCurrencyId());
            contentValues.put("alt",account.getName());
            contentValues.put("modified",DbUtil.dateToLong(account.getModified()));
            writableDatabase.update(account.getTableName(), contentValues, "id=?",new String[]{""+account.getId()});
        }
    }

    @Override
    protected void delete(Account account, SQLiteDatabase writableDatabase) {
        if(account != null){
            writableDatabase.delete(account.getTableName(),"id=?", new String[]{""+account.getId()});
        }
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        if(id > 0){
            writableDatabase.delete(MoneyDbHelper.TABLE_ACCOUNTS,"id=?", new String[]{""+id});
        }
    }

    @Override
    protected Account getById(long id, SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+MoneyDbHelper.TABLE_ACCOUNTS+" WHERE id=?", new String[]{""+id});
        if(cursor.moveToFirst()){
            return buildAccountFromCursor(cursor);
        }
        return null;
    }
    protected Account buildAccountFromCursor(Cursor cursor){
        Account account = new Account();
        account.setId(cursor.getInt(0));
        account.setName(cursor.getString(1));
        account.setCurrencyId(cursor.getInt(2));
        account.setAlt(cursor.getString(3));
        account.setCreated(DbUtil.longToDate(cursor.getLong(4)));
        account.setModified(DbUtil.longToDate(cursor.getLong(5)));
        return account;
    }
}
