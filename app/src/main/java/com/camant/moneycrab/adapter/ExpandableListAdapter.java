package com.camant.moneycrab.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camant.moneycrab.R;
import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.view.AnimatedExpandableListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sreng on 11/15/2016.
 */

public class ExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements View.OnClickListener {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<? extends MoneyBase>> _listDataChild;
    private OnChildAddListener onChildAddListener = null;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<? extends MoneyBase>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MoneyBase child = (MoneyBase) getChild(groupPosition, childPosition);

        if(childPosition>0 && !(child instanceof CategoryType)) {
            //if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
            //}

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(child.toString());
        }else{
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item_alt, null);
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAdd);
            imageView.setTag(child);
            imageView.setOnClickListener(this);
            txtListChild.setText(child.toString());
        }
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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

    public void setOnChildAddListener(OnChildAddListener onChildAddListener) {
        this.onChildAddListener = onChildAddListener;
    }

    @Override
    public void onClick(View view) {
        if(view.getTag() != null && onChildAddListener != null){
            onChildAddListener.onAddClick(view.getTag());
        }
    }
    public interface OnChildAddListener{
        void onAddClick(Object o);
    }
}
