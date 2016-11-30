package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.MoneyDbHelper;

/**
 * Created by sreng on 11/13/2016.
 */

public class Category extends MoneyBase {
    private String name;
    private long ctype;
    private String icon;
    private String alt;
    public Category(){super();}
    public Category(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 2;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    @Override
    protected void readFromParcel(Parcel in) {
        name = in.readString();
        ctype = in.readLong();
        icon = in.readString();
        alt = in.readString();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(ctype);
        parcel.writeString(icon);
        parcel.writeString(alt);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Category Type
     * @return
     */
    public long getCtype() {
        return ctype;
    }

    public void setCtype(long ctype) {
        this.ctype = ctype;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_CATEGORIES);
    }
    @Override
    public String toString() {
        return ""+name;
    }
}
