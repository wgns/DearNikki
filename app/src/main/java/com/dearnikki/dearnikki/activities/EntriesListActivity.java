package com.dearnikki.dearnikki.activities;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dearnikki.dearnikki.R;
import com.dearnikki.dearnikki.adapters.EntriesRecyclerAdapter;
import com.dearnikki.dearnikki.model.*;
import com.dearnikki.dearnikki.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EntriesListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewEntries;
    private List<Entry> listEntries;
    private EntriesRecyclerAdapter entriesRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private ImageView imgCompose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_list);
        getSupportActionBar().setTitle("");

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewEntries = (RecyclerView) findViewById(R.id.recyclerViewEntries);
        imgCompose = (ImageView) findViewById(R.id.imgCompose);
    }

    private void initListeners() {
        imgCompose.setOnClickListener(this);
    }

    private void initObjects() {
        listEntries = new ArrayList<>();
        entriesRecyclerAdapter = new EntriesRecyclerAdapter(listEntries ,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewEntries.setLayoutManager(mLayoutManager);
        recyclerViewEntries.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEntries.setHasFixedSize(true);
        recyclerViewEntries.setAdapter(entriesRecyclerAdapter);
        databaseHelper = new DatabaseHelper(this);

        String emailFromIntent = databaseHelper.getUserName(getIntent().getStringExtra("EMAIL"));
        textViewName.setText(emailFromIntent);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listEntries.clear();
                listEntries.addAll(databaseHelper.getAllEntry());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                entriesRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgCompose) {
            Intent intentRegister = new Intent(this, ComposeActivity.class);
            intentRegister.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));
            startActivity(intentRegister);
        }
    }
}