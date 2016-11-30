package com.camant.moneycrab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camant.moneycrab.R;
import com.camant.moneycrab.adapter.CategoryIconsAdapter;
import com.camant.moneycrab.dao.AccountOrmDao;
import com.camant.moneycrab.dao.CategoryDao;
import com.camant.moneycrab.dao.CategoryIconDao;
import com.camant.moneycrab.helper.CurrencyHelper;
import com.camant.moneycrab.model.Account;
import com.camant.moneycrab.model.Category;
import com.camant.moneycrab.model.CategoryIcon;
import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.orm.AccountOrm;
import com.camant.moneycrab.util.ViewUtil;
import com.camant.moneycrab.view.Spinner;

import java.util.ArrayList;

public class AccountActivity extends BaseCreateUpdateDeleteActivity {
    private CategoryIconsAdapter categoryIconsAdapter;
    private ArrayList<CategoryIcon> categoryIcons = new ArrayList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private CategoryIconDao categoryIconDao;
    private CurrencyHelper currencyHelper;
    private EditText editTextName, editTextAlt;
    private TextView textViewIcon;
    private Spinner spinner;
    private Currency currency;
    private AccountOrm accountOrm;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if(getIntent().hasExtra("account")){
            moneyBase = getIntent().getParcelableExtra("account");
        }
        if(moneyBase == null){
            Account account = new Account();
            moneyBase = new AccountOrm(account);
        }

        this.accountOrm = (AccountOrm) moneyBase;

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewIcons);
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);*/
        float spans = getResources().getDisplayMetrics().widthPixels/ ViewUtil.dipToPixels(this, 110);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,(int)spans);
        recyclerView.setLayoutManager(gridLayoutManager);

        categoryIconDao = new CategoryIconDao(this);
        categoryIcons.addAll(categoryIconDao.getAll());
        categoryIconsAdapter = new CategoryIconsAdapter(categoryIcons);
        recyclerView.setAdapter(categoryIconsAdapter);

        currencyHelper = new CurrencyHelper(this);
        currencyHelper.loadCurrencies(currencies);
        ArrayAdapter<Currency> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        spinner = (Spinner)findViewById(R.id.spinnerCurrency);
        if(spinner != null){
            spinner.setAdapter(arrayAdapter);
        }
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextAlt = (EditText)findViewById(R.id.editTextAlt);
        textViewIcon = (TextView) findViewById(R.id.textViewIcon);

        if(moneyBase.getId() > 0){
            editTextName.setText(accountOrm.getName());
            editTextAlt.setText(accountOrm.getAlt());
            for(int i=0;i<currencies.size();i++){
                if(currencies.get(i).getId() == accountOrm.getCurrencyId()){
                    spinner.setSelectedItemPosition(i);
                    break;
                }
            }
            for(int i=0;i<categoryIcons.size();i++){
                if(categoryIcons.get(i).getPath().equals(accountOrm.getIcon())){
                    categoryIconsAdapter.setSelectedPos(i);
                    break;
                }
            }

        }
    }
    @Override
    protected void onSave() {
        if(validate()) {
            AccountOrmDao categoryDao = new AccountOrmDao(this);
            accountOrm.setName(editTextName.getText().toString());
            accountOrm.setAlt(editTextAlt.getText().toString());
            accountOrm.setCurrencyId(((Currency)spinner.getSelectedItem()).getId());
            accountOrm.setIcon(categoryIcons.get(categoryIconsAdapter.getSelectedPos()).getPath());
            categoryDao.create(accountOrm);
            Intent data = new Intent();
            data.putExtra("accountOrm", accountOrm);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onUpdate() {
        if(validate()) {
            AccountOrmDao accountOrmDao = new AccountOrmDao(this);
            accountOrm.setName(editTextName.getText().toString());
            accountOrm.setAlt(editTextAlt.getText().toString());
            accountOrm.setCurrencyId(currency.getId());
            accountOrm.setIcon(categoryIcons.get(categoryIconsAdapter.getSelectedPos()).getPath());
            accountOrmDao.update(accountOrm);
            Intent data = new Intent();
            data.putExtra("accountOrm", accountOrm);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public boolean validate(){
        boolean valid = true;
        if(editTextName.getText().toString().trim().length() == 0){
            editTextName.setError(getString(R.string.category_name_required));
            valid = false;
        }else{
            editTextName.setError(null);
        }
        Currency currency = (Currency) spinner.getSelectedItem();
        if(currency == null || currency.getId() == 0){
            spinner.setError(getString(R.string.currency_required));
            valid = false;
        }else{
            spinner.setError(null);
        }

        if(categoryIconsAdapter.getSelectedPos() < 0){
            textViewIcon.setText(getString(R.string.category_icon_required));
            valid = false;
        }else{
            textViewIcon.setText("");
        }

        return valid;
    }

    @Override
    protected void onDelete() {
        AccountOrmDao accountOrmDao = new AccountOrmDao(this);
        accountOrmDao.delete(accountOrm);
        setResult(RESULT_OK);
        finish();
    }
}
