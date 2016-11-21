package com.camant.moneycrab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.camant.moneycrab.R;
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
        return true;
    }
}
