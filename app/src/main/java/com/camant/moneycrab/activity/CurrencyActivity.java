package com.camant.moneycrab.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.camant.moneycrab.R;
import com.camant.moneycrab.dao.CurrencyDao;
import com.camant.moneycrab.helper.ProgressBarHelper;
import com.camant.moneycrab.model.Currency;

public class CurrencyActivity extends MoneyBaseActivity {
    private EditText editTextName, editTextSign;
    private Currency currency = null;
    private MenuItem menuDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSign = (EditText) findViewById(R.id.editTextSign);
        if(getIntent().hasExtra("currency")){
            currency = getIntent().getParcelableExtra("currency");
        }
        if(currency != null && currency.getId() > 0){
            editTextName.setText(currency.getName());
            editTextSign.setText(currency.getAlt());
        }else{
            currency = new Currency();
            if(menuDelete != null){
                menuDelete.setEnabled(false)
                        .setVisible(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_currency, menu);
        menuDelete = menu.findItem(R.id.menuDelete);
        if(currency == null || currency.getId() == 0){
            menuDelete.setEnabled(false).setVisible(false);
            invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.menuSave:
                boolean error = false;
                if(editTextName.getText().toString().trim().length()<=0){
                    editTextName.setError(getString(R.string.currency_name_required));
                    error = true;
                    //AlertDialog alertDialog = ProgressBarHelper.buildAlertDialog(this, getString(R.string.));
                }
                if(editTextSign.getText().toString().trim().length() <= 0){
                    editTextSign.setError(getString(R.string.currency_sign_required));
                    error = true;
                }
                if(!error) {
                    CurrencyDao currencyDao = new CurrencyDao(this);
                    if (currency.getId() > 0) {//update
                        currencyDao.update(currency);
                    } else {
                        currencyDao.create(currency);
                    }
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.menuDelete:
                CurrencyDao currencyDao = new CurrencyDao(this);
                currencyDao.delete(currency);
                setResult(RESULT_OK);
                finish();
                break;
        }
        return true;
    }
}
