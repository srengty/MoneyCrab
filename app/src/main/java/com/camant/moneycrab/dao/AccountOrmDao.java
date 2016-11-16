package com.camant.moneycrab.dao;

import android.content.Context;

import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.orm.AccountOrm;

import java.util.ArrayList;

/**
 * Created by sreng on 11/16/2016.
 */

public class AccountOrmDao extends AccountDao {
    private CurrencyDao currencyDao;
    public AccountOrmDao(Context context) {
        super(context);
        currencyDao = new CurrencyDao(context);
    }

    public ArrayList<AccountOrm> getAllLazily() {
        ArrayList<Account> accounts = super.getAll();
        ArrayList<AccountOrm> accountOrms = new ArrayList<>();
        AccountOrm accountOrm;
        for(Account account:accounts){
            accountOrm = new AccountOrm(account);
            accountOrm.setCurrency(currencyDao.getById(account.getCurrencyId()));
            accountOrms.add(accountOrm);
        }
        return accountOrms;
    }
    public AccountOrm getByIdLazily(long id){
        AccountOrm accountOrm = new AccountOrm(getById(id));
        accountOrm.setCurrency(currencyDao.getById(accountOrm.getCurrencyId()));
        return accountOrm;
    }

}
