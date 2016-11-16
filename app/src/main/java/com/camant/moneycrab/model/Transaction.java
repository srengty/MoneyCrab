package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.util.MoneyDbHelper;

import java.util.Date;

/**
 * Created by sreng on 11/13/2016.
 */

public class Transaction extends MoneyBase {
    private String note;
    private double t_in;
    private double t_out;
    private double rate;
    private Date t_date;
    private int categoryId;
    private int accountId;
    public Transaction(){super();}
    public Transaction(Parcel in) {
        super(in);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        note = in.readString();
        t_in = in.readDouble();
        t_out = in.readDouble();
        rate = in.readDouble();
        t_date = DbUtil.longToDate(in.readLong());
        categoryId = in.readInt();
        accountId = in.readInt();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(note);
        parcel.writeDouble(t_in);
        parcel.writeDouble(t_out);
        parcel.writeDouble(rate);
        parcel.writeLong(DbUtil.dateToLong(t_date));
        parcel.writeInt(categoryId);
        parcel.writeInt(accountId);
    }
    @Override
    public int describeContents() {
        return 4;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getT_in() {
        return t_in;
    }

    public void setT_in(double t_in) {
        this.t_in = t_in;
    }

    public double getT_out() {
        return t_out;
    }

    public void setT_out(double t_out) {
        this.t_out = t_out;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getT_date() {
        return t_date;
    }

    public void setT_date(Date t_date) {
        this.t_date = t_date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_TRANSACTIONS);
    }
    @Override
    public String toString() {
        return ""+note;
    }
}
