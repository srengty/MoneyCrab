package com.camant.moneycrab.holder;

import com.camant.moneycrab.orm.CategoryOrm;

/**
 * Created by sreng on 11/19/2016.
 */

public class PieDataHolder {
    private CategoryOrm categoryOrm;
    private Double t_in = 0d;
    private Double t_out = 0d;

    public PieDataHolder(CategoryOrm categoryOrm, double t_in, double t_out) {
        this.categoryOrm = categoryOrm;
        this.t_in = t_in;
        this.t_out = t_out;
    }

    public CategoryOrm getCategoryOrm() {
        return categoryOrm;
    }

    public void setCategoryOrm(CategoryOrm categoryOrm) {
        this.categoryOrm = categoryOrm;
    }

    public Double getT_in() {
        return t_in;
    }

    public void setT_in(double t_in) {
        this.t_in = t_in;
    }

    public Double getT_out() {
        return t_out;
    }

    public void setT_out(double t_out) {
        this.t_out = t_out;
    }
}
