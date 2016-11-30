package com.camant.moneycrab.helper;

import android.content.Context;

import com.camant.moneycrab.dao.CurrencyDao;
import com.camant.moneycrab.model.Currency;

import java.util.ArrayList;

/**
 * Created by sreng on 11/29/2016.
 */

public class CurrencyHelper extends MoneyBaseHelper {
    private CurrencyDao currencyDao;
    public CurrencyHelper(Context context) {
        super(context);
        currencyDao = new CurrencyDao(context);
    }
    public ArrayList<Currency> loadCurrencies(ArrayList<Currency> currencies){
        if(currencies == null) currencies = new ArrayList<>();
        currencies.clear();
        Currency cur = new Currency();
        cur.setName("");
        currencies.add(cur);
        currencies.addAll(currencyDao.getAll());
        return currencies;
    }
}
