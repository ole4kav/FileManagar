package com.example.home.myproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by HOME on 11/03/2016.
 */

public class Compress {
    private static final int BUFFER_SIZE = 2048;

  public static void zip(String[] filesPaths, String zipFileName) throws IOException {

          BufferedInputStream origin;
          FileOutputStream dest = new FileOutputStream(zipFileName);
          ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(dest));
          byte data[] = new byte[BUFFER_SIZE];

      try  {
          for(int i=0; i < filesPaths.length; i++) {
              FileInputStream fileInputStream = new FileInputStream(filesPaths[i]);
              origin = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
              ZipEntry entry = new ZipEntry(filesPaths[i].substring(filesPaths[i].lastIndexOf("/") + 1));
              outputStream.putNextEntry(entry);

              int count;

              while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                  outputStream.write(data, 0, count);
              }
              origin.close();
          }
          outputStream.close();
      }
      catch(Exception e) {
          e.printStackTrace();
      }
  }
}


