package com.camant.moneycrab.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.camant.moneycrab.util.DbUtil;

import java.util.Date;

/**
 * Created by sreng on 11/13/2016.
 */

public abstract class MoneyBase implements Parcelable {
    protected String tableName;
    private long id;
    private Date created;
    private Date modified;

    protected MoneyBase(){
        initForDb();
    }

    protected MoneyBase(Parcel in) {
        id = in.readLong();
        created = DbUtil.longToDate(in.readLong(), null);
        modified = DbUtil.longToDate(in.readLong(), null);
        readFromParcel(in);
        initForDb();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public static final Creator<MoneyBase> CREATOR = new Creator<MoneyBase>() {
        @Override
        public MoneyBase createFromParcel(Parcel in) {
            return new MoneyBase(in){
                @Override
                protected void readFromParcel(Parcel in) {

                }

                @Override
                protected void storeInParcel(Parcel parcel, int i) {

                }

                @Override
                protected void initForDb() {
                    tableName = "money_base";
                }
            };
        }

        @Override
        public MoneyBase[] newArray(int size) {
            return new MoneyBase[size];
        }
    };
    public String getTableName(){
        return tableName;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(DbUtil.dateToLong(created));
        parcel.writeLong(DbUtil.dateToLong(modified));
        storeInParcel(parcel, i);
    }
    protected abstract void readFromParcel(Parcel in);
    protected abstract void storeInParcel(Parcel parcel, int i);

    protected abstract void initForDb();
}
