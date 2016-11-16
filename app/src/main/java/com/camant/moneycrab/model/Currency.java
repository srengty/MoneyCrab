package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

/**
 * Created by sreng on 11/13/2016.
 */

public class Currency extends MoneyBase {
    private String name;
    private String alt;
    public Currency(){super();}
    protected Currency(Parcel in) {
        super(in);
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public int describeContents() {
        return 1;
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
    @Override
    protected void readFromParcel(Parcel in) {
        name = in.readString();
        alt = in.readString();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(alt);
    }

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_CURRENCIES);
    }
    @Override
    public String toString() {
        return ""+name;
    }
}
