package com.camant.moneycrab.model;

import android.os.Parcel;

import com.camant.moneycrab.util.MoneyDbHelper;

/**
 * Created by sreng on 11/29/2016.
 */

public class CategoryIcon extends MoneyBase {
    private String name;
    private String alt;
    private String path;

    @Override
    protected void readFromParcel(Parcel in) {
        name = in.readString();
        alt = in.readString();
        path = in.readString();
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(alt);
        parcel.writeString(path);
    }

    @Override
    protected void initForDb() {
        setTableName(MoneyDbHelper.TABLE_ICONS);
    }

    public CategoryIcon() {
        super();
    }

    public CategoryIcon(Parcel in) {
        super(in);
    }
    public static final Creator<CategoryIcon> CREATOR = new Creator<CategoryIcon>() {
        @Override
        public CategoryIcon createFromParcel(Parcel in) {
            return new CategoryIcon(in){
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
        public CategoryIcon[] newArray(int size) {
            return new CategoryIcon[size];
        }
    };

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
