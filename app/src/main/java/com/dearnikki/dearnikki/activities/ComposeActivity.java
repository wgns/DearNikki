package com.dearnikki.dearnikki.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.dearnikki.dearnikki.R;
import com.dearnikki.dearnikki.helpers.InputValidation;
import com.dearnikki.dearnikki.model.Entry;
import com.dearnikki.dearnikki.sql.DatabaseHelper;

import java.util.*;
import java.text.*;

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;

    private AppCompatButton appCompatButtonSave;

    private TextInputLayout textInputLayoutTitle;
    private TextInputLayout textInputLayoutContent;

    private TextInputEditText textInputEditTextTitle;
    private TextInputEditText textInputEditTextContent;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

        if (getIntent().getStringExtra("TITLE") != null) {
            textInputEditTextTitle.setText(getIntent().getStringExtra("TITLE"));
            textInputEditTextContent.setText(getIntent().getStringExtra("CONTENT"));
        }
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.textInputLayoutTitle);
        textInputLayoutContent = (TextInputLayout) findViewById(R.id.textInputLayoutContent);

        textInputEditTextTitle = (TextInputEditText) findViewById(R.id.textInputEditTextTitle);
        textInputEditTextContent = (TextInputEditText) findViewById(R.id.textInputEditTextContent);

        appCompatButtonSave = (AppCompatButton) findViewById(R.id.appCompatButtonSave);
    }

    private void initListeners() {
        appCompatButtonSave.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);
        entry = new Entry();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.appCompatButtonSave) {
            postDataToSQLite();
            finish();

            Intent intent = new Intent(this, EntriesListActivity.class);
            intent.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));

            startActivity(intent);
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextTitle, textInputLayoutTitle, "Enter Title")) {
            return;
        } if (!inputValidation.isInputEditTextFilled(textInputEditTextContent, textInputLayoutContent, "Enter content")) {
            return;
        }

        entry.setTitle(textInputEditTextTitle.getText().toString().trim());
        entry.setContent(textInputEditTextContent.getText().toString().trim());
        entry.setDate();
        entry.setEmail(getIntent().getStringExtra("EMAIL"));

        databaseHelper.addEntry(entry);

        // Snack Bar to show success message that record saved successfully
        Snackbar.make(nestedScrollView, "Diary Entry added", Snackbar.LENGTH_LONG).show();
        emptyInputEditText();
    }

    private void emptyInputEditText() {
        textInputEditTextTitle.setText(null);
        textInputEditTextContent.setText(null);
    }
}