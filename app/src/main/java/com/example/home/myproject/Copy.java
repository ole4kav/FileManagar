package com.example.home.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by HOME on 12/03/2016.
 */
public class Copy
{
    public static void copyAllFolder(ArrayList<String> pathToCopy, String sdCard) throws IOException {
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

    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {  //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String files[] = src.list(); //list all the directory contents
            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);   //recursive copy
            }
        }
        else {       //if file, then copy it
            //if directory not exists, create it
            File parentDes = dest.getParentFile();
            if (!parentDes.exists()) {
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
}

    /*private static void copyDirectory(File sourceLocation, File targetLocation) {
        if (!targetLocation.exists()) {
            targetLocation.mkdirs();
        }
        String files[] = sourceLocation.list(); //list all the directory contents
        for (String file : files) {
            //construct the sourceLocation and dest file structure
            File srcFile = new File(sourceLocation, file);
            File destFile = new File(targetLocation, file);

            if (srcFile.isDirectory()) {
                copyFolder(srcFile, destFile);   //recursive copy
            }
        }
    }*/


/*
public class Copy
    {
    public static void copyFolder(File src, File dest) throws IOException {
        if(src.isDirectory()){  //if directory not exists, create it
            if(!dest.exists()){
                dest.mkdirs();
            }
            String files[] = src.list(); //list all the directory contents
            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile,destFile);   //recursive copy
            }
        }
        else{       //if file, then copy it
            //if directory not exists, create it
            File parentDes = dest.getParentFile();
            if (!parentDes.exists()){
                parentDes.mkdirs();
            }
            ////Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
}*/

