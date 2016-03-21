package com.example.home.myproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HOME on 21/03/2016.
 */
public class Delete
{
    public static void deleteAllFolder(ArrayList<String> pathToDelete) throws IOException {
        for (int i = 0; i < pathToDelete.size(); i++) {
            File deleteFile = new File(pathToDelete.get(i));
            if (deleteFile.isFile()) {
                deleteFile.delete();
            }
            else {
                String[] children = deleteFile.list();
                for (String thischildren : children) {
                    new File(deleteFile, thischildren).delete();
                }
                if (deleteFile.listFiles().length == 0) {
                    deleteFile.delete();
                }
            }
        }
    }
}
