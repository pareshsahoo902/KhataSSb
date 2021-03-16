package com.ssb.ssbapp.Utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class FirebaseHelper extends Application {

    private static FirebaseHelper instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public static FirebaseHelper getInstance() {
        return instance;
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

}
