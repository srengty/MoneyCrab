package com.camant.moneycrab.orm;

import android.os.Parcel;

import com.camant.moneycrab.model.Category;
import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.model.Transaction;

/**
 * Created by sreng on 11/13/2016.
 */

public class CategoryOrm extends Category {
    private CategoryType categoryType;
    public CategoryOrm(Parcel in) {
        super(in);
        categoryType = in.readParcelable(CategoryType.class.getClassLoader());
    }
    public CategoryOrm(Category category){
        super();
        setId(category.getId());
        setAlt(category.getAlt());
        setName(category.getName());
        setIcon(category.getIcon());
        setCtype(category.getCtype());
        setCreated(category.getCreated());
        setModified(category.getModified());
    }

    @Override
    protected void storeInParcel(Parcel parcel, int i) {
        super.storeInParcel(parcel, i);
        parcel.writeParcelable(categoryType, i);
    }

    @Override
    public int describeContents() {
        return 7;
    }

    public static final Creator<CategoryOrm> CREATOR = new Creator<CategoryOrm>() {
        @Override
        public CategoryOrm createFromParcel(Parcel in) {
            return new CategoryOrm(in);
        }

        @Override
        public CategoryOrm[] newArray(int size) {
            return new CategoryOrm[size];
        }
    };

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
