package com.camant.moneycrab.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.camant.moneycrab.R;
import com.camant.moneycrab.fragment.CategoriesListFragment;
import com.camant.moneycrab.fragment.TransactionNoteFragment;
import com.camant.moneycrab.helper.TransactionDataListener;
import com.camant.moneycrab.model.Transaction;
import com.camant.moneycrab.orm.CategoryOrm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransactionPlusActivity extends AppCompatActivity implements View.OnClickListener, TransactionDataListener {
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private EditText editText, editTextAmount;
    SimpleDateFormat sdf;
    private Transaction transaction = new Transaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_plus);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        editText = (EditText) findViewById(R.id.editTextDate);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
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
        Bundle extras = new Bundle();

        TransactionNoteFragment transactionNote = new TransactionNoteFragment();
        transactionNote.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, transactionNote)
                .commit();

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransactionPlusActivity.this, date, myCalendar
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
        transaction.setT_in(Double.valueOf(editTextAmount.getText().toString()));
        transaction.setT_out(0d);
        transaction.setAccountId(1);
        transaction.setCategoryId(categoryOrm.getId());
        transaction.setRate(1.0);
    }

    @Override
    public void onSubmit() {

    }
}
