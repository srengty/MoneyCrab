package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.MoneyDbHelper;

/**
 * Created by sreng on 11/13/2016.
 */

public class Account extends MoneyBase {
    private String name;
    private long currencyId;
    private String alt;
    private String icon;

    public Account() {
        super();
    }

    public Account(Parcel in) {
        super(in);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        name = in.readString();
        currencyId = in.readInt();
        alt = in.readString();
        icon = in.readString();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(currencyId);
        parcel.writeString(alt);
        parcel.writeString(icon);
    }
    @Override
    public int describeContents() {
        return 3;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currency) {
        this.currencyId = currency;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_ACCOUNTS);
    }

    @Override
    public String toString() {
        return name==null?"":name;
    }
}
