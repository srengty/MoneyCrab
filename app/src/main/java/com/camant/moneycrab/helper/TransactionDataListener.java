package com.camant.moneycrab.helper;

import com.camant.moneycrab.orm.CategoryOrm;

import java.util.Date;

/**
 * Created by Institute on 11/18/2016.
 */

public interface TransactionDataListener {
    void onFieldSet(String note);
    void onNextFieldSet(CategoryOrm categoryOrm);
    void onSubmit();
}
