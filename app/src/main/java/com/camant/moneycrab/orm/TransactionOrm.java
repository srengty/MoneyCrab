package com.camant.moneycrab.orm;

import android.os.Parcel;

import com.camant.moneycrab.model.Transaction;

/**
 * Created by sreng on 11/13/2016.
 */

public class TransactionOrm extends Transaction {
    private CategoryOrm categoryOrm;
    private AccountOrm accountOrm;
    public TransactionOrm(Parcel in) {
        super(in);
        categoryOrm = in.readParcelable(CategoryOrm.class.getClassLoader());
        accountOrm = in.readParcelable(AccountOrm.class.getClassLoader());
    }

    public TransactionOrm(Transaction t) {
        setId(t.getId());
        setNote(t.getNote());
        setT_in(t.getT_in());
        setT_out(t.getT_out());
        setRate(t.getRate());
        setT_date(t.getT_date());
        setCreated(t.getCreated());
        setModified(t.getModified());
        setCategoryId(t.getCategoryId());
        setAccountId(t.getAccountId());
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        super.storeInParcel(parcel, i);
        parcel.writeParcelable(categoryOrm, i);
        parcel.writeParcelable(accountOrm, i);
    }

    @Override
    public int describeContents() {
        return 8;
    }

    public static final Creator<TransactionOrm> CREATOR = new Creator<TransactionOrm>() {
        @Override
        public TransactionOrm createFromParcel(Parcel in) {
            return new TransactionOrm(in);
        }

        @Override
        public TransactionOrm[] newArray(int size) {
            return new TransactionOrm[size];
        }
    };

    public CategoryOrm getCategoryOrm() {
        return categoryOrm;
    }

    public void setCategoryOrm(CategoryOrm categoryOrm) {
        this.categoryOrm = categoryOrm;
    }

    public AccountOrm getAccountOrm() {
        return accountOrm;
    }

    public void setAccountOrm(AccountOrm accountOrm) {
        this.accountOrm = accountOrm;
    }
}
