package com.example.home.myproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    ArrayList<File> files = new ArrayList<>();
    ArrayList<String> pathToBack = new ArrayList<>();

    CustomAdapter customAdapter;
    ListView myListView;
    File thisDirectory;

    int fileCount;
    int folderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            loadAndSaveToHist(Environment.getExternalStorageDirectory());
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "OnItemClickListener", Toast.LENGTH_LONG).show();
                File dir = files.get(position);
                if (dir.isDirectory()) {
                    loadAndSaveToHist(dir);
                }
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                File filePosition = files.get(position);
                Intent intent = new Intent(MainActivity.this, PropertyActivity.class);
                intent.putExtra("FolderName", filePosition.getName());
                intent.putExtra("FolderSize", filePosition.length()); //getTotalSpace());

                if (filePosition.isDirectory()) {
                    long lengthfolder = folderSize(filePosition);
                    intent.putExtra("FolderSize", lengthfolder);

                    folderType(filePosition);
                    intent.putExtra("FoldersNum", folderCount);
                    intent.putExtra("FilesNum", fileCount);
                }
                startActivity(intent);
                return false;
            }
        });
    }

    public long folderSize(File directory) {
        long length = directory.length();
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file);
            }
        }
        return length;
    }

    public void folderType(File directory) {
        fileCount = 0;
        folderCount = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                fileCount += 1;
            } else {
                folderCount += 1;
            }
        }
    }

    private void loadFiles(File currentDirectory) {
        files.clear();
        String[] names = currentDirectory.list();
        /*if (names!=null){
            for (int i = 0; i <names.length ; i++) {
                String path = currentDirectory.getPath();
                File filenames = new File(path, names[i]);
                files.add(filenames);}
        }*/
        String path;

        if (names != null) {
            for (String filename : names) {
                path = currentDirectory.getPath();
                File filenames = new File(path, filename);
                files.add(filenames);
            }
        }

        ((TextView) findViewById(R.id.textView)).setText(currentDirectory.getPath());

        customAdapter = new CustomAdapter(files, this);
        myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(customAdapter);

        thisDirectory = currentDirectory;
    }

    private void loadAndSaveToHist(File currentDirectory) {
        loadFiles(currentDirectory);
        pathToBack.add(currentDirectory.getPath());
    }

    public void chooseItemBtnClick(View view) {

        customAdapter.setCheckBoxVisibility();
        customAdapter.notifyDataSetChanged();

        for (int i = 0; i < files.size(); i++) {
            customAdapter.isCheckBoxChecked[i] = false;
        }
    }

    public ArrayList<String> chosesFiles (ArrayList<String> choosePaths){
        for (int i = 0; i < files.size(); i++) {
            if (customAdapter.isCheckBoxChecked[i]) {
                choosePaths.add(files.get(i).getPath());
            }
        }
        return choosePaths;
    }

    public void deleteItemBtnClick(View view) {
        ArrayList<String> pathToDelete = new ArrayList<>();
        pathToDelete = chosesFiles(pathToDelete);
        if (pathToDelete.size()>0) {
            try {
                Manager.delete(pathToDelete);
                Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            loadFiles(thisDirectory);
            customAdapter.notifyDataSetChanged();
        }
    }

    public void zipItemBtnClick(View view) {
        ArrayList<String> pathToZip = new ArrayList<>();
        pathToZip = chosesFiles (pathToZip);
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy-HH:mm");
        String nameToZipFile = sdf.format(new Date());

        String[] stringPathToZip = new String[pathToZip.size()];
        stringPathToZip = pathToZip.toArray(stringPathToZip);

        if (pathToZip.size()>0) {
            try {
                Manager.zipCompress(stringPathToZip, thisDirectory.toString() + "/" + nameToZipFile + ".zip");
                Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }
            loadFiles(thisDirectory);
            customAdapter.notifyDataSetChanged();
        }
    }

    public void copyItemBtnClick(View view) {
        ArrayList<String> pathToCopy = new ArrayList<>();
        pathToCopy = chosesFiles(pathToCopy);
        String sdCard = thisDirectory.toString();

        if (pathToCopy.size()>0){
            try {
                Manager.copy(pathToCopy, sdCard);
                Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG).show();
            }
            catch  (IOException e) {
                e.printStackTrace();
            }
            loadFiles(thisDirectory);
            customAdapter.notifyDataSetChanged();
        }
    }

   @Override
    public void onBackPressed() {
        if (pathToBack.size() > 1) {
            int index = (pathToBack.size() - 1);
            pathToBack.remove(index);

            int indexNew = (pathToBack.size() - 1);
            File lastDir = new File(pathToBack.get(indexNew));

            loadFiles(lastDir);
        }
        else {
            this.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if ((grantResults != null) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                loadAndSaveToHist(Environment.getExternalStorageDirectory());
            }
        }
    }

    public void searchItemBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}