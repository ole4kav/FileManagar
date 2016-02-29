package com.example.home.myproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    File sdDirectory;
    ArrayList<File> files = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else {
            loadFiles();
        }
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "OnItemClickListener", Toast.LENGTH_LONG).show();

            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "OnItemLongClickListener", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void loadFiles() {
        sdDirectory = Environment.getExternalStorageDirectory();
        String [] names = sdDirectory.list();
        if (names!=null){
            for (int i = 0; i <names.length ; i++) {
                String path = sdDirectory.getPath();
                File filenames = new File(path, names[i]);
                files.add(filenames);
            }
        }
        customAdapter = new CustomAdapter(files, this);
        myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(customAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if ((grantResults != null) && (grantResults.length>0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                loadFiles();
            }
        }
    }
}