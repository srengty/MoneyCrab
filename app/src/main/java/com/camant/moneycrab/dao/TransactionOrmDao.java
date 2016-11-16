package com.camant.moneycrab.dao;

import android.content.Context;

import com.camant.moneycrab.model.Transaction;
import com.camant.moneycrab.orm.TransactionOrm;

import java.util.ArrayList;

/**
 * Created by sreng on 11/16/2016.
 */

public class TransactionOrmDao extends TransactionDao {
    private AccountOrmDao accountOrmDao;
    private CategoryOrmDao categoryOrmDao;
    public TransactionOrmDao(Context context) {
        super(context);
        categoryOrmDao = new CategoryOrmDao(context);
        accountOrmDao = new AccountOrmDao(context);
    }
    public ArrayList<TransactionOrm> getAllLazily(){
        ArrayList<Transaction> transactions = getAll();
        ArrayList<TransactionOrm> transactionOrms = new ArrayList<>();
        TransactionOrm transactionOrm;
        for(Transaction t:transactions){
            transactionOrm = new TransactionOrm(t);
            transactionOrm.setAccountOrm(accountOrmDao.getByIdLazily(t.getAccountId()));
            transactionOrm.setCategoryOrm(categoryOrmDao.getByIdLazily(t.getCategoryId()));
            transactionOrms.add(transactionOrm);
        }
        return transactionOrms;
    }
}
