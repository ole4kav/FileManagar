package com.example.home.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HOME on 23/02/2016.
 */
 
public class CustomAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
{
    ArrayList<File> files;
    Context context;
    boolean[] isCheckBoxChecked;
    private boolean checkBoxVisibility = false;

    //
    long space;
    //

    public CustomAdapter(ArrayList<File> filess, Context context) {
        this.files = filess;
        this.context = context;
        isCheckBoxChecked = new boolean[files.size()];
    }

    @Override
    public int getCount() {return files.size();}

    @Override
    public File getItem(int position) {return files.get(position);}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;

        if (convertView == null){
            row = LayoutInflater.from(context).inflate(R.layout.row_custom, null);
            CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(this);
        }
        else {
            row = convertView;
        }

        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
        TextView textViewName = (TextView) row.findViewById(R.id.textViewFileName);
        TextView textViewNum = (TextView) row.findViewById(R.id.textViewNumItems);
        TextView textViewDate = (TextView) row.findViewById(R.id.textViewDate);
        CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);

        final File currentFile = getItem(position);
        textViewName.setText(currentFile.getName());

        if (currentFile.isFile()){
            imageView.setImageResource(R.drawable.file);
            textViewNum.setText("");
        }
        else {
            if ((currentFile.listFiles() == null)||(currentFile.listFiles().length == 0)) {
                textViewNum.setText("No items");
                imageView.setImageResource(R.drawable.foldernull);
            }
            else {
                File[] innerFiles =  currentFile.listFiles();
                textViewNum.setText(Integer.toString(innerFiles.length) + " items");
                imageView.setImageResource(R.drawable.folder);
            }
        }
        space = currentFile.getTotalSpace();

        Date lastModified = new Date(currentFile.lastModified());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDateString = formatter.format(lastModified);
        textViewDate.setText(formattedDateString);

        checkBox.setVisibility(checkBoxVisibility ? View.VISIBLE : View.INVISIBLE);

        checkBox.setTag(position);
        checkBox.setChecked(isCheckBoxChecked[position]);


        return row;
    }

    public void setCheckBoxVisibility(){
        checkBoxVisibility = !checkBoxVisibility;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int tagposition = (int) buttonView.getTag();
        isCheckBoxChecked[tagposition] = isChecked;
    }
}






