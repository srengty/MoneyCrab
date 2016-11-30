package com.camant.moneycrab.helper;

import android.content.Context;

import com.camant.moneycrab.dao.AccountOrmDao;
import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.orm.AccountOrm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreng on 11/29/2016.
 */

public class AccountHelper extends MoneyBaseHelper {
    private AccountOrmDao accountOrmDao;
    public AccountHelper(Context context){
        super(context);
        accountOrmDao = new AccountOrmDao(context);
    }
    public ArrayList<AccountOrm> loadAccounts(ArrayList<AccountOrm> accounts){
        if(accounts == null){
            accounts = new ArrayList<>();
        }
        accounts.clear();
        ArrayList<AccountOrm> orms = accountOrmDao.getAllLazily();
        accounts.clear();
        Account a = new Account();
        a.setName("");
        a.setId(0);
        accounts.add(new AccountOrm(a));
        accounts.addAll(orms);
        return accounts;
    }
}
