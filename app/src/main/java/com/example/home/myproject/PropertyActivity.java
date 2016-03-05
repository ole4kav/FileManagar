package com.example.home.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class PropertyActivity extends AppCompatActivity
{
    File fileProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        Intent startingIntent = getIntent();

        if (startingIntent.hasExtra("FolderName")) {
            ((TextView) findViewById(R.id.textViewFolderName)).setText(startingIntent.getStringExtra("FolderName"));
        }
        if (startingIntent.hasExtra("FolderSize")) {
            ((TextView) findViewById(R.id.textViewFolderSize)).setText(Long.toString(startingIntent.getLongExtra("FolderSize", 0l)));
        }

        if (startingIntent.hasExtra("FoldersNum")) {
            ((TextView) findViewById(R.id.textViewFolderLength)).setText(
                    Integer.toString(startingIntent.getIntExtra("FoldersNum", 0))+ " folders, " +
                            Integer.toString(startingIntent.getIntExtra("FilesNum", 0))+ " files, ");
            (findViewById(R.id.textViewFolderLengthTitle)).setVisibility(View.VISIBLE);
        }

    }
}
