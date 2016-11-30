package com.camant.moneycrab.orm;

import android.os.Parcel;

import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.model.Transaction;

/**
 * Created by sreng on 11/13/2016.
 */

public class AccountOrm extends Account {
    private Currency currency;
    public AccountOrm(Parcel in) {
        super(in);
        if(getCurrencyId()>0) {
            currency = in.readParcelable(Currency.class.getClassLoader());
        }
    }
    public AccountOrm(Account account){
        super();
        setId(account.getId());
        setCurrencyId(account.getCurrencyId());
        setIcon(account.getIcon());
        setName(account.getName());
        setAlt(account.getAlt());
        setModified(account.getModified());
        setCreated(account.getCreated());
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        super.storeInParcel(parcel, i);
        parcel.writeParcelable(currency, i);
    }

    @Override
    public int describeContents() {
        return 6;
    }

    public static final Creator<AccountOrm> CREATOR = new Creator<AccountOrm>() {
        @Override
        public AccountOrm createFromParcel(Parcel in) {
            return new AccountOrm(in);
        }

        @Override
        public AccountOrm[] newArray(int size) {
            return new AccountOrm[size];
        }
    };

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        if(getCurrency() != null) {
            return super.toString() + " (" + getCurrency().getName() + ")";
        }
        return super.toString();
    }
}
