package com.example.home.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HOME on 12/03/2016.
 */
public class Copy
{
    public static void copyFolder(File src, File dest) throws IOException {
        if(src.isDirectory()){  //if directory not exists, create it
            if(!dest.exists()){
                dest.mkdir();
            }
            String files[] = src.list(); //list all the directory contents
            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile,destFile);   //recursive copy
            }
        }
        else{  //if file, then copy it
            //Use bytes stream to support all file types
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
}

