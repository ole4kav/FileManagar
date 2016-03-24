package com.example.home.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by HOME on 21/03/2016.
 */
public class Manager
{
    public static void zipCompress(String[] filesPaths, String zipFileName) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);  //create object of FileOutputStream
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream); //create object of ZipOutputStream from FileOutputStream
            for (String fpath : filesPaths) {
                File file = new File(fpath);
                addDirectory(zipOutputStream,file);
            }
            zipOutputStream.close();   //close the ZipOutputStream
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void addDirectory(ZipOutputStream zout, File file) {
        if (file.isDirectory()) {    //if the file is directory, call the function recursively
            File[] innerFiles = file.listFiles();
            for (File innerf : innerFiles) {
                addDirectory(zout, innerf);
            }
        }
        else {  //we are here means, its file and not directory, so add it to the zip file
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



    public static void copy(ArrayList<String> pathToCopy, String sdCard) throws IOException {
        for (int i = 0; i < pathToCopy.size(); i++) {
            try {
                File sourceLocation = new File(pathToCopy.get(i));   //the file to be copied
                String nameFile = sourceLocation.getName();
                File targetLocation = new File(sdCard + "/NewFolder/" + nameFile);  //target location
                copyFolder(sourceLocation, targetLocation);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {  //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String files[] = src.list(); //list all the directory contents
            for (String file : files) { //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);   //recursive copy
            }
        }
        else {       //if file, then copy it
            File parentDes = dest.getParentFile();
            if (!parentDes.exists()) {  //if directory not exists, create it
                parentDes.mkdirs();
            }
            ////Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }


    public static void delete(ArrayList<String> pathToDelete) throws IOException {
        for (int i = 0; i < pathToDelete.size(); i++) {
            File deleteFile = new File(pathToDelete.get(i));
            if (deleteFile.isFile()) {
                deleteFile.delete();
            }
            else {
                String[] children = deleteFile.list();
                for (String thisChildren : children) {
                    new File(deleteFile, thisChildren).delete();
                }
                if (deleteFile.listFiles().length == 0) {
                    deleteFile.delete();
                }
            }
        }
    }
}
