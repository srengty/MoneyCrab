package com.camant.moneycrab.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camant.moneycrab.R;
import com.camant.moneycrab.adapter.CategoryIconsAdapter;
import com.camant.moneycrab.dao.CategoryDao;
import com.camant.moneycrab.dao.CategoryIconDao;
import com.camant.moneycrab.helper.CurrencyHelper;
import com.camant.moneycrab.model.Category;
import com.camant.moneycrab.model.CategoryIcon;
import com.camant.moneycrab.model.CategoryType;
import com.camant.moneycrab.model.Currency;
import com.camant.moneycrab.orm.AccountOrm;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.util.ViewUtil;
import com.camant.moneycrab.view.Spinner;

import java.util.ArrayList;

public class CategoryActivity extends BaseCreateUpdateDeleteActivity {
    private CategoryIconsAdapter categoryIconsAdapter;
    private ArrayList<CategoryIcon> categoryIcons = new ArrayList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private CategoryIconDao categoryIconDao;
    private CurrencyHelper currencyHelper;
    private EditText editTextName, editTextAlt;
    private TextView textViewIcon;
    private CategoryType categoryType;
    private Category category;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if(getIntent().hasExtra("category_type")){
            categoryType = getIntent().getParcelableExtra("category_type");
        }
        if(getIntent().hasExtra("category")){
            moneyBase = getIntent().getParcelableExtra("category");
        }

        if(categoryType == null){
            Toast.makeText(this, getString(R.string.category_type_not_found), Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if(moneyBase == null){
            moneyBase = new Category();
        }

        this.category = (Category) moneyBase;
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
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextAlt = (EditText)findViewById(R.id.editTextAlt);
        textViewIcon = (TextView) findViewById(R.id.textViewIcon);

        if(moneyBase.getId() > 0){
            editTextName.setText(category.getName());
            editTextAlt.setText(category.getAlt());
            for(int i=0;i<categoryIcons.size();i++){
                if(categoryIcons.get(i).getPath().equals(category.getIcon())){
                    categoryIconsAdapter.setSelectedPos(i);
                    break;
                }
            }

        }
    }

    @Override
    protected void onSave() {
        if(validate()) {
            CategoryDao categoryDao = new CategoryDao(this);
            Category category = new Category();
            category.setName(editTextName.getText().toString());
            category.setAlt(editTextAlt.getText().toString());
            category.setCtype(categoryType.getId());
            category.setIcon(categoryIcons.get(categoryIconsAdapter.getSelectedPos()).getPath());
            categoryDao.create(category);
            Intent data = new Intent();
            data.putExtra("category", category);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onUpdate() {
        if(validate()) {
            CategoryDao categoryDao = new CategoryDao(this);
            category.setName(editTextName.getText().toString());
            category.setAlt(editTextAlt.getText().toString());
            category.setCtype(categoryType.getId());
            category.setIcon(categoryIcons.get(categoryIconsAdapter.getSelectedPos()).getPath());
            categoryDao.update(category);
            Intent data = new Intent();
            data.putExtra("category", category);
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
        CategoryDao categoryDao = new CategoryDao(this);
        categoryDao.delete(category);
        setResult(RESULT_OK);
        finish();
    }
}
