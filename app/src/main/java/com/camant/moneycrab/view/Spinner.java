package com.camant.moneycrab.view;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.camant.moneycrab.R;

/**
 * Created by sreng on 11/14/2016.
 */

public class Spinner extends Button implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PopupWindow popupWindow;
    private BaseAdapter adapter;
    private ListView listView;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Object selectedItem;
    public Spinner(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        this.setOnClickListener(this);
        setAllCaps(false);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_spinner);
        drawable.setBounds(new Rect(0, 0, 40, 40));
        setCompoundDrawables(null,null,drawable,null);

        View view = LayoutInflater.from(context).inflate(R.layout.spinner,null,false);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        //popupWindow.setContentView(view);
        listView = (ListView)popupWindow.getContentView().findViewById(R.id.listView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(listView != null){
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        popupWindow.setWidth(this.getMeasuredWidth());
        popupWindow.showAsDropDown(this);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
        if(adapter != null){
            initialSelection();
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    initialSelection();
                }

                @Override
                public void onInvalidated() {
                    super.onInvalidated();
                    initialSelection();
                }
            });
        }

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapter != null) {
            this.setText("" + adapter.getItem(i));
        }
        selectedItem = adapter.getItem(i);
        popupWindow.dismiss();
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(adapterView, view, i, l);
        }
    }
    private void initialSelection(){
        if(adapter != null && adapter.getCount()>0){
            setText("" + adapter.getItem(0));
            selectedItem = adapter.getItem(0);
        }
    }

    public Object getSelectedItem() {
        return selectedItem;
    }
    public void setSelectedItemPosition(int position){
        if(adapter != null && adapter.getCount()>position && position >= 0){
            setText("" + adapter.getItem(position));
            selectedItem = adapter.getItem(position);
        }
    }
}
