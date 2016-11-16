package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.MoneyDbHelper;

/**
 * Created by sreng on 11/13/2016.
 */

public class CategoryType extends MoneyBase {
    private String name;
    public CategoryType(){super();}
    public CategoryType(Parcel in) {
        super(in);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        name = in.readString();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
    @Override
    public int describeContents() {
        return 5;
    }

    public static final Creator<CategoryType> CREATOR = new Creator<CategoryType>() {
        @Override
        public CategoryType createFromParcel(Parcel in) {
            return new CategoryType(in);
        }

        @Override
        public CategoryType[] newArray(int size) {
            return new CategoryType[size];
        }
    };

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_CATEGORY_TYPES);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return ""+name;
    }
}
