package com.camant.moneycrab.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.camant.moneycrab.R;
import com.camant.moneycrab.dao.CurrencyDao;
import com.camant.moneycrab.model.MoneyBase;

/**
 * Created by sreng on 11/28/2016.
 */

public class BaseCreateUpdateDeleteActivity extends AppCompatActivity {
    protected MenuItem menuDelete;
    protected MoneyBase moneyBase;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_money_base, menu);
        menuDelete = menu.findItem(R.id.menuDelete);
        if(moneyBase == null || moneyBase.getId() <= 0){
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
                if(moneyBase.getId() > 0){
                    onUpdate();
                }else {
                    onSave();
                }
                break;
            case R.id.menuDelete:
                onDelete();
                break;
        }
        return true;
    }
    protected void onSave(){

    }
    protected void onUpdate(){

    }
    protected void onDelete(){

    }
}
