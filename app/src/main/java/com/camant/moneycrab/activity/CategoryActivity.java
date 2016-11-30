package com.camant.moneycrab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.camant.moneycrab.R;
import com.camant.moneycrab.orm.CategoryOrm;

public class CategoryActivity extends BaseCreateUpdateDeleteActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewIcons);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onSave() {
        finish();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        finish();
    }

    @Override
    protected void onDelete() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
