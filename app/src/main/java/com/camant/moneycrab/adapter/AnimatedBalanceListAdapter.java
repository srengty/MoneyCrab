package com.camant.moneycrab.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.camant.moneycrab.R;
import com.camant.moneycrab.holder.PieDataHolder;
import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.orm.TransactionOrm;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.view.AnimatedExpandableListView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by sreng on 11/15/2016.
 */

public class AnimatedBalanceListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context _context;
    private List<PieDataHolder> _listDataHeader; // header titles
    // child data in format of header title, child title
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DbUtil.DEFAULT_DAY_MONTH_FORMAT, Locale.getDefault());
    private NumberFormat numberFormat = new DecimalFormat(DbUtil.DEFAULT_NUMBER_FORMAT);
    private HashMap<Long, List<MoneyBase>> _listDataChild;

    public AnimatedBalanceListAdapter(Context context, List<PieDataHolder> listDataHeader,
                                      HashMap<Long, List<MoneyBase>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getCategoryOrm().getId())
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TransactionOrm child = (TransactionOrm) getChild(groupPosition, childPosition);

        //if(childPosition>0 && !(child instanceof CategoryType)) {
            //if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.balance_list_item, null);
            //}

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(child.toString());
            txtListChild = (TextView)convertView.findViewById(R.id.textViewDate);
        txtListChild.setText(simpleDateFormat.format(child.getT_date()));
        txtListChild = (TextView)convertView.findViewById(R.id.textViewAmount);
        if(child.getCategoryOrm().getCtype() == MoneyBase.CATEGORY_TYPE_EXPENSES) {
            txtListChild.setText(numberFormat.format(child.getT_out()));
        }else{
            txtListChild.setText(numberFormat.format(child.getT_in()));
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageViewIcon);
        if(child.getCategoryOrm().getCtype() == MoneyBase.CATEGORY_TYPE_EXPENSES) {
            imageView.setImageResource(R.drawable.ic_expense_type);
        }else{
            imageView.setImageResource(R.drawable.ic_income_type);
        }

        /*}else{
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item_alt, null);
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(child.toString());
        }*/
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getCategoryOrm().getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        PieDataHolder headerTitle = (PieDataHolder) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.balance_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getCategoryOrm().getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
