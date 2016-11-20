package com.camant.moneycrab.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.camant.moneycrab.R;
import com.camant.moneycrab.activity.TransactionUpdateActivity;
import com.camant.moneycrab.adapter.AnimatedBalanceListAdapter;
import com.camant.moneycrab.adapter.ExpandableListAdapter;
import com.camant.moneycrab.dao.AccountOrmDao;
import com.camant.moneycrab.dao.CategoryOrmDao;
import com.camant.moneycrab.dao.CurrencyDao;
import com.camant.moneycrab.dao.TransactionDao;
import com.camant.moneycrab.dao.TransactionOrmDao;
import com.camant.moneycrab.holder.PieDataHolder;
import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.orm.AccountOrm;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.orm.TransactionOrm;
import com.camant.moneycrab.renderer.MoneyLegendRenderer;
import com.camant.moneycrab.renderer.MoneyPieChartRenderer;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.view.AnimatedExpandableListView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sreng on 11/6/2016.
 */

public class ScreenSlidePageFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    protected Typeface mTfLight;
    private TextView textViewDate;
    private Date date;
    private int interval = 1;
    private long from, to;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = null;
    private NumberFormat numberFormat = null;
    private ArrayList<TransactionOrm> transactionOrms = new ArrayList<>();
    private View layoutBalance;
    private int balancePosition=0;
    private float keepY = 1;
    private float headerSpace = 0;
    AnimatedBalanceListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    List<PieDataHolder> listDataHeader = new ArrayList<>();
    HashMap<Long, List<MoneyBase>> listDataChild = new HashMap<>();
    TransactionDao transactionDao;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };
    private PieChart mChart;
    private ViewGroup rootView;
    private MoneyPieChartRenderer pieChartRenderer;
    private MoneyLegendRenderer legendRenderer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        date = new Date();
        dateFormat = new SimpleDateFormat(DbUtil.DEFAULT_DATE_FORMAT, Locale.getDefault());
        numberFormat = new DecimalFormat(DbUtil.DEFAULT_NUMBER_FORMAT);
        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.containsKey("date")){
                date = DbUtil.longToDate(bundle.getLong("date"));
            }
            if(bundle.containsKey("interval")){
                interval = bundle.getInt("interval");
            }
        }
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        if(interval == 7){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }else if(interval == 30){
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }else if(interval == 365){
            calendar.set(Calendar.DAY_OF_YEAR, 1);
        }
        from = calendar.getTimeInMillis();
        String title = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        calendar.add(Calendar.MILLISECOND, -1);
        if(interval > 1) {
            title += " - " + dateFormat.format(calendar.getTime());
        }
        to = calendar.getTimeInMillis();
        TransactionOrmDao transactionOrmDao = new TransactionOrmDao(getActivity());
        transactionOrms = transactionOrmDao.getAllLazily(from, to);
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        textViewDate = (TextView) rootView.findViewById(R.id.textViewDate);
        textViewDate.setText(title);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart = (PieChart) rootView.findViewById(R.id.chart1);
        pieChartRenderer = new MoneyPieChartRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        mChart.setRenderer(pieChartRenderer);
        mChart.getLegend().setEnabled(false);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        //mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        //mChart.setDrawEntryLabels(false);
        //mChart.setDrawMarkers(false);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        setDynData();

        //mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        layoutBalance = rootView.findViewById(R.id.layoutBalance);
        rootView.findViewById(R.id.buttonBalance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutBalance.getY() == headerSpace){
                    layoutBalance.animate().translationY(keepY);
                    //keepY = 0;
                }else{
                    keepY = layoutBalance.getY();
                    layoutBalance.animate().translationY(headerSpace-balancePosition);
                    //Toast.makeText(getActivity(),""+keepY+":"+layoutBalance.getY(), Toast.LENGTH_LONG).show();
                }
            }
        });
        TypedValue tv = new TypedValue();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isAdded()) {
                    handleBalance();
                }
            }
        });
        listAdapter = new AnimatedBalanceListAdapter(getActivity(), listDataHeader, listDataChild);
        AnimatedExpandableListView animatedExpandableListView = (AnimatedExpandableListView)rootView.findViewById(R.id.listViewBalance);
        animatedExpandableListView.setAdapter(listAdapter);
        animatedExpandableListView.setOnChildClickListener(this);
        return rootView;
    }

    private void setDynData(){
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        HashMap<Long, PieDataHolder> categoryOrmHashMap = new HashMap<>();
        Double val_in, val_out;
        val_in = 0d;
        val_out = 0d;
        double total = 0d;
        ArrayList<MoneyBase> children = null;
        listDataHeader.clear();
        listDataChild.clear();
        PieDataHolder pieDataHolder;
        for(TransactionOrm t:transactionOrms){
            if(categoryOrmHashMap.containsKey(t.getCategoryId())){
                val_in = categoryOrmHashMap.get(t.getCategoryId()).getT_in();
                val_out = categoryOrmHashMap.get(t.getCategoryId()).getT_out();
                categoryOrmHashMap.put(t.getCategoryId(), new PieDataHolder(t.getCategoryOrm(),
                        val_in + t.getT_in(),
                        val_out + t.getT_out()));
                pieDataHolder = findData(t.getCategoryId());
                if(pieDataHolder != null){
                    pieDataHolder.setT_in(val_in+t.getT_in());
                    pieDataHolder.setT_out(val_out+t.getT_out());
                }
                if(listDataChild.containsKey(t.getCategoryId())){
                    listDataChild.get(t.getCategoryId()).add(t);
                }
            }else{
                categoryOrmHashMap.put(t.getCategoryId(), new PieDataHolder(t.getCategoryOrm(), t.getT_in(), t.getT_out()));
                listDataHeader.add(new PieDataHolder(t.getCategoryOrm(),t.getT_in(),t.getT_out()));
                children = new ArrayList<>();
                children.add(t);
                listDataChild.put(t.getCategoryId(), children);
            }
            if(t.getCategoryOrm().getCtype() == 1) {//expenses
                total -= t.getT_out();
            }else{
                total += t.getT_in();
            }
        }
        PieDataHolder item;
        for(Long key:categoryOrmHashMap.keySet()){
            item = categoryOrmHashMap.get(key);
            if(item.getCategoryOrm().getCtype() == 1) {// expenses
                entries.add(new PieEntry(item.getT_out().floatValue(), item.getCategoryOrm().getName()));
            }else{
                entries.add(new PieEntry(item.getT_in().floatValue(), item.getCategoryOrm().getName()));
            }
        }
        mChart.setCenterText(generateCenterSpannableText(total));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
    private PieDataHolder findData(long catId){
        for(PieDataHolder pieDataHolder:listDataHeader){
            if(pieDataHolder.getCategoryOrm().getId() == catId){
                return pieDataHolder;
            }
        }
        return null;
    }
    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(double value) {

        NumberFormat numberFormat = new DecimalFormat("0.00");

        SpannableString s = new SpannableString(numberFormat.format(value));
        s.setSpan(new RelativeSizeSpan(2.7f), 0, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    private void handleBalance(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //layoutBalance.setBackgroundColor(Color.BLACK);
        if (layoutBalance.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) layoutBalance.getLayoutParams();
            balancePosition = rootView.getHeight() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            p.setMargins(0, balancePosition, 0, 0);
            p.height = balancePosition+25;
            layoutBalance.requestLayout();
            keepY = 0;
            headerSpace = textViewDate.getHeight();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        Intent intent = new Intent(getActivity(), TransactionUpdateActivity.class);
        TransactionOrm transactionOrm = (TransactionOrm)listAdapter.getChild(groupPosition, childPosition);
        intent.putExtra("transaction",transactionOrm);
        startActivityForResult(intent, 2);
        return true;
    }
}
