package com.camant.moneycrab.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.camant.moneycrab.R;
import com.camant.moneycrab.dao.TransactionDao;
import com.camant.moneycrab.fragment.TransactionNoteFragment;
import com.camant.moneycrab.helper.TransactionDataListener;
import com.camant.moneycrab.model.MoneyBase;
import com.camant.moneycrab.model.Transaction;
import com.camant.moneycrab.orm.CategoryOrm;
import com.camant.moneycrab.orm.TransactionOrm;
import com.camant.moneycrab.util.DbUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransactionUpdateActivity extends AppCompatActivity implements View.OnClickListener, TransactionDataListener {
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private EditText editText, editTextAmount;
    SimpleDateFormat sdf;
    private TransactionOrm transaction = null;
    private TransactionDao transactionDao = new TransactionDao(this);
    private NumberFormat numberFormat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_update);
        numberFormat = new DecimalFormat(DbUtil.DEFAULT_NUMBER_FORMAT);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(getIntent().getParcelableExtra("transaction") != null){
            transaction = getIntent().getParcelableExtra("transaction");
            myCalendar.setTime(transaction.getT_date());
        }
        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        editText = (EditText) findViewById(R.id.editTextDate);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        //editTextAmount.setForm
        editText.setText(sdf.format(myCalendar.getTime()));
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        if(transaction != null){
            if(transaction.getCategoryOrm().getCtype() == MoneyBase.CATEGORY_TYPE_EXPENSES) {
                editTextAmount.setText(numberFormat.format(transaction.getT_out()));
            }
        }
        Bundle extras = new Bundle();
        //extras.putInt("category_type", 1);
        extras.putParcelable("transaction", transaction);
        TransactionNoteFragment transactionNote = new TransactionNoteFragment();
        transactionNote.setArguments(extras);
        transactionNote.setTransactionDataListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, transactionNote)
                .commit();

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransactionUpdateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void updateLabel() {
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
        }else if(item.getItemId() == R.id.menuSave){
            onNextFieldSet(null);
            onSubmit();
        }else if(item.getItemId() == R.id.menuDelete){
            setResult(RESULT_OK);
            transactionDao.delete(transaction);
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFieldSet(String note) {
        transaction.setNote(note);
    }

    @Override
    public void onNextFieldSet(CategoryOrm categoryOrm) {
        transaction.setT_date(myCalendar.getTime());
        CategoryOrm cat = categoryOrm;
        if(cat == null) {
            cat = transaction.getCategoryOrm();
        }
        if(cat != null){
            transaction.setCategoryId(cat.getId());
            if(cat.getCtype() == MoneyBase.CATEGORY_TYPE_EXPENSES) {
                if (editTextAmount.getText().length() > 0) {
                    transaction.setT_out(Double.valueOf(editTextAmount.getText().toString()));
                } else {
                    transaction.setT_out(0);
                }
                transaction.setT_in(0d);
            }else{
                if (editTextAmount.getText().length() > 0) {
                    transaction.setT_in(Double.valueOf(editTextAmount.getText().toString()));
                } else {
                    transaction.setT_in(0);
                }
                transaction.setT_out(0d);
            }
        }
        transaction.setAccountId(1);
        transaction.setRate(1.0);
    }

    @Override
    public void onSubmit() {
        transactionDao.update(transaction);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_transaction_update, menu);
        return true;
    }
}
