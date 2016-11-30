package com.camant.moneycrab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.camant.moneycrab.activity.CategoryActivity;
import com.camant.moneycrab.activity.CurrencyActivity;
import com.camant.moneycrab.activity.TransactionMinusActivity;
import com.camant.moneycrab.activity.TransactionPlusActivity;
import com.camant.moneycrab.adapter.ExpandableListAdapter;
import com.camant.moneycrab.anim.ZoomOutPageTransformer;
import com.camant.moneycrab.dao.AccountOrmDao;
import com.camant.moneycrab.dao.CategoryOrmDao;
import com.camant.moneycrab.dao.CurrencyDao;
import com.camant.moneycrab.dao.TransactionDao;
import com.camant.moneycrab.fragment.ScreenSlidePageFragment;
import com.camant.moneycrab.helper.AccountHelper;
import com.camant.moneycrab.helper.CategoryHelper;
import com.camant.moneycrab.helper.CurrencyHelper;
import com.camant.moneycrab.helper.ProgressBarHelper;
import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.model.Transaction;
import com.camant.moneycrab.orm.AccountOrm;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.util.DbUtil;
import com.camant.moneycrab.view.AnimatedExpandableListView;
import com.camant.moneycrab.view.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener, ExpandableListAdapter.OnChildAddListener {
    private static final int TRANSACTION_PLUS = 1;
    private static final int TRANSACTION_MINUS = 2;
    private static final int CURRENCY_REQUEST_CODE = 3;
    private static final int CATEGORY_REQUEST_CODE = 4;
    ExpandableListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<? extends MoneyBase>> listDataChild;
    TransactionDao transactionDao;
    private int dayCount = 1;
    private int interval = 1;
    private Transaction beginTransation, endTransaction;
    private Calendar calendar = Calendar.getInstance();
    private long accountId = 0;
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES = 1;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private int previousGroup = -1;
    private ArrayList<AccountOrm> accounts = new ArrayList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<MoneyBase> categories = new ArrayList<>();
    private AccountHelper accountHelper = new AccountHelper(this);
    private CategoryHelper categoryHelper = new CategoryHelper(this);
    private CurrencyHelper currencyHelper = new CurrencyHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabMinus = (FloatingActionButton) findViewById(R.id.fab);
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TransactionMinusActivity.class);
                intent.putExtra("user",false);
                startActivityForResult(intent, TRANSACTION_PLUS);/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        FloatingActionButton fabPlus = (FloatingActionButton) findViewById(R.id.fabPlus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TransactionPlusActivity.class);
                intent.putExtra("user",false);
                startActivityForResult(intent, TRANSACTION_MINUS);/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        Spinner spinner = (Spinner)navigationViewRight.findViewById(R.id.spinner);
        ArrayAdapter<AccountOrm> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accounts);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemClickListener(this);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        transactionDao = new TransactionDao(this);
        initTransactions();
        RadioGroup radioGroup = (RadioGroup)navigationViewRight.findViewById(R.id.radioGroupDateFilter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    if(checkedRadioButton.getText().toString().equals(getString(R.string.right_drawer_day))){
                        interval = 1;
                    }else if(checkedRadioButton.getText().toString().equals(getString(R.string.right_drawer_week))){
                        interval = 7;
                    }else if(checkedRadioButton.getText().toString().equals(getString(R.string.right_drawer_month))){
                        interval = 30;
                    }else if(checkedRadioButton.getText().toString().equals(getString(R.string.right_drawer_year))){
                        interval = 365;
                    }
                    NUM_PAGES = dayCount / interval;
                    if(NUM_PAGES<1) NUM_PAGES = 1;
                    mPager.setAdapter(mPagerAdapter);
                    mPagerAdapter.notifyDataSetChanged();
                }
            }
        });

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(NUM_PAGES - 1);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // get the listview
        expListView = (AnimatedExpandableListView) navigationView.findViewById(R.id.lvExp);
        expListView.setOnGroupClickListener(this);
        expListView.setOnGroupExpandListener(this);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        listAdapter.setOnChildAddListener(this);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPos, int childPos, long id) {
                MoneyBase moneyBase = (MoneyBase) listAdapter.getChild(groupPos, childPos);
                if(childPos > 0 && !(moneyBase instanceof CategoryType)){
                    if(moneyBase instanceof AccountOrm){
                        //update account
                    }else if(moneyBase instanceof CategoryOrm){
                        //update category
                    }else if(moneyBase instanceof Currency){
                        //update currency
                        ProgressBarHelper.showLoadingDialog(MainActivity.this);
                        Intent intent = new Intent(getBaseContext(), CurrencyActivity.class);
                        intent.putExtra("currency", moneyBase);
                        startActivityForResult(intent, CURRENCY_REQUEST_CODE);
                    }
                }else{

                }
                Toast.makeText(MainActivity.this, childPos+","+moneyBase.getTableName()+": "+moneyBase, Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(getString(R.string.accounts));
        listDataHeader.add(getString(R.string.categories));
        listDataHeader.add(getString(R.string.currencies));

        loadAccounts();

        reloadCategories();

        reloadCurrencies();

        listDataChild.put(listDataHeader.get(0), accounts); // Header, Child data
        listDataChild.put(listDataHeader.get(1), categories);
        listDataChild.put(listDataHeader.get(2), currencies);
    }
    private void loadAccounts(){
        accountHelper.loadAccounts(accounts);
    }
    private void reloadCategories(){
        categoryHelper.loadCategories(categories);
    }
    private void reloadCurrencies(){
        currencyHelper.loadCurrencies(currencies);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this,""+adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onGroupClick(ExpandableListView listView, View view, int groupPosition, long l) {
        if (listView.isGroupExpanded(groupPosition)) {
            expListView.collapseGroupWithAnimation(groupPosition);
        } else {
            expListView.expandGroupWithAnimation(groupPosition);
        }
        return true;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if(groupPosition != previousGroup)
            expListView.collapseGroupWithAnimation(previousGroup);
        previousGroup = groupPosition;
    }

    @Override
    public void onAddClick(Object moneyBase) {
        if(moneyBase instanceof AccountOrm){
            //add new account

        }else if(moneyBase instanceof CategoryType){
            //add new category
            CategoryType categoryType = (CategoryType) moneyBase;
            Toast.makeText(this, ""+categoryType.getName(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), CategoryActivity.class);
            intent.putExtra("category_type", (CategoryType)moneyBase);
            startActivityForResult(intent, CATEGORY_REQUEST_CODE);
        }else if(moneyBase instanceof Currency){
            //add new currency
            ProgressBarHelper.showLoadingDialog(this);
            Intent intent = new Intent(getBaseContext(), CurrencyActivity.class);
            startActivityForResult(intent, CURRENCY_REQUEST_CODE);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequences.
     *//*
     * */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int datePos = position + 1 - NUM_PAGES;
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, datePos * interval);
            Bundle bundle = new Bundle();
            bundle.putLong("date",DbUtil.dateToLong(calendar.getTime()));
            bundle.putInt("interval", interval);
            ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
            screenSlidePageFragment.setArguments(bundle);
            return screenSlidePageFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CURRENCY_REQUEST_CODE){
            ProgressBarHelper.hideLoadingDialog();
            reloadCurrencies();
            listAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "Ok", Toast.LENGTH_LONG).show();
            initTransactions();
            mPager.setAdapter(mPagerAdapter);
            mPagerAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(NUM_PAGES - 1);
        }
    }
    protected void initTransactions(){
        beginTransation = transactionDao.getFirstTransaction();
        endTransaction = transactionDao.getLastTransaction();

        Date now = new Date();
        long begin = DbUtil.dateToLong(now);

        Long days = 0l;
        if(beginTransation != null) {
            days = (DbUtil.dateToLong(now) - DbUtil.dateToLong(beginTransation.getT_date()));
        }
        days = days / (24 * 60 * 60 * 1000);
        dayCount = days.intValue() + 1;
        NUM_PAGES = (dayCount /interval)==0?1:(dayCount /interval);
    }
}
