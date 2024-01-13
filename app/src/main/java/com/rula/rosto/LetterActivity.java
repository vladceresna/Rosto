package com.rula.rosto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.rula.etime.EasyTime;
import com.rula.etime.TimeData;

public class LetterActivity extends AppCompatActivity {

    TextView rid, time, title, text;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);

        topAppBar = findViewById(R.id.topAppBar);

        rid = findViewById(R.id.rid);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);

        rid.setText(getIntent().getStringExtra("rid"));
        title.setText(getIntent().getStringExtra("title"));
        text.setText(getIntent().getStringExtra("text"));

        String s = ".";
        TimeData td = new EasyTime().getConvTime(Long.parseLong(getIntent().getStringExtra("time")));
        time.setText(td.M() +s+ td.D() +" "+ td.H() +":"+ td.m());

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reply:
                        startActivity(new Intent(LetterActivity.this, WriteActivity.class).setData(Uri.parse(getIntent().getStringExtra("rid"))));
                        break;
                    case R.id.forward:
                        startActivity(new Intent(LetterActivity.this, WriteActivity.class).setData(Uri.parse("")).putExtra("title", getIntent().getStringExtra("title")).putExtra("text",getIntent().getStringExtra("text")));
                        break;
                }
                return false;
            }
        });

    }
}