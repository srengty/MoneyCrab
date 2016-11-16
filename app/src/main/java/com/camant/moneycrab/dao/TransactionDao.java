package com.camant.moneycrab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.camant.moneycrab.model.Transaction;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sreng on 11/16/2016.
 */

public class TransactionDao extends BaseDao<Transaction> {
    public TransactionDao(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Transaction> getAll(SQLiteDatabase readableDatabase) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ MoneyDbHelper.TABLE_TRANSACTIONS, new String[]{});
        if(cursor.moveToFirst()){
            do {
                transactions.add(buildFromCursor(cursor));
            }while (cursor.moveToNext());
        }
        return transactions;
    }

    private Transaction buildFromCursor(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getLong(0));
        transaction.setNote(cursor.getString(1));
        transaction.setT_in(cursor.getDouble(2));
        transaction.setT_out(cursor.getDouble(3));
        transaction.setRate(cursor.getDouble(4));
        transaction.setT_date(DbUtil.longToDate(cursor.getLong(5)));
        transaction.setCreated(DbUtil.longToDate(cursor.getLong(6)));
        transaction.setModified(DbUtil.longToDate(cursor.getLong(7)));
        transaction.setCategoryId(cursor.getInt(8));
        transaction.setAccountId(cursor.getInt(9));
        return transaction;
    }

    @Override
    protected Transaction create(Transaction transaction, SQLiteDatabase writableDatabase) {
        if(transaction != null){
            Date now = new Date();
            transaction.setCreated(now);
            transaction.setModified(now);
            transaction.setT_date(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("note", transaction.getNote());
            contentValues.put("t_in", transaction.getT_in());
            contentValues.put("t_out", transaction.getT_out());
            contentValues.put("rate", transaction.getRate());
            contentValues.put("t_date", DbUtil.dateToLong(transaction.getT_date()));
            contentValues.put("created", DbUtil.dateToLong(transaction.getCreated()));
            contentValues.put("modified", DbUtil.dateToLong(transaction.getModified()));
            contentValues.put("category_id", transaction.getCategoryId());
            contentValues.put("account_id", transaction.getAccountId());
            transaction.setId(writableDatabase.insert(MoneyDbHelper.TABLE_TRANSACTIONS, "modified", contentValues));
        }
        return transaction;
    }

    @Override
    protected void update(Transaction transaction, SQLiteDatabase writableDatabase) {
        if(transaction != null){
            Date now = new Date();
            transaction.setModified(now);
            ContentValues contentValues = new ContentValues();
            contentValues.put("note", transaction.getNote());
            contentValues.put("t_in", transaction.getT_in());
            contentValues.put("t_out", transaction.getT_out());
            contentValues.put("rate", transaction.getRate());
            contentValues.put("t_date", DbUtil.dateToLong(transaction.getT_date()));
            contentValues.put("modified", DbUtil.dateToLong(transaction.getModified()));
            contentValues.put("category_id", transaction.getCategoryId());
            contentValues.put("account_id", transaction.getAccountId());
            writableDatabase.update(MoneyDbHelper.TABLE_TRANSACTIONS, contentValues, "id=?", new String[]{""+transaction.getId()});
        }
    }

    @Override
    protected void delete(Transaction transaction, SQLiteDatabase writableDatabase) {
        if(transaction != null) deleteById(transaction.getId(), writableDatabase);
    }

    @Override
    protected void deleteById(long id, SQLiteDatabase writableDatabase) {
        writableDatabase.delete(MoneyDbHelper.TABLE_TRANSACTIONS, "id=?", new String[]{""+id});
    }

    @Override
    protected Transaction getById(long id, SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+MoneyDbHelper.TABLE_TRANSACTIONS+" WHERE id=?",new String[]{""+id});
        Transaction transaction = null;
        if(cursor.moveToFirst()){
            transaction = buildFromCursor(cursor);
        }
        return transaction;
    }
}
