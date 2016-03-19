package com.example.home.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by HOME on 11/03/2016.
 */

public class Compress
{
    public static void zip(String[] filesPaths, String zipFileName) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);  //create object of FileOutputStream
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream); //create object of ZipOutputStream from FileOutputStream
            for (int i = 0; i < filesPaths.length; i++) {
                File file = new File(filesPaths[i]);
                addDirectory(zipOutputStream,file);
            }
            zipOutputStream.close();   //close the ZipOutputStream
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDirectory(ZipOutputStream zout, File file) {
        if (file.isDirectory()) {    //if the file is directory, call the function recursively
           File[] innerFiles = file.listFiles();
            for (int i = 0; i < innerFiles.length; i++) {
                addDirectory(zout, innerFiles[i]);
            }
        }
        else {
            //we are here means, its file and not directory, so add it to the zip file
            try {
                byte[] buffer = new byte[1024]; //create byte buffer
                FileInputStream fin = new FileInputStream(file);    //create object of FileInputStream
                zout.putNextEntry(new ZipEntry(file.getName()));
                //After creating entry in the zip file, actually write the file.
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                //After writing the file to ZipOutputStream, use void closeEntry() method of ZipOutputStream
                // class to close the current entry and position the stream to write the next entry.
                zout.closeEntry();
                fin.close();    //close the InputStream
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}