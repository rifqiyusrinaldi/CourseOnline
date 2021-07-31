package com.rifqi.courseonline.helper;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileHelper {
    private static final String TAG = "FileHelper";

    public static final int MOVE_SUCCESS = 0;
    public static final int MOVE_FAILED = -1;
    public static final String FILE_SCHEMA = "file://";
    public static final String COURSE_THUMBS_DIR = "/course-thumbs/";
    public static final String PROFILE_THUMBS_DIR = "/profile-thumbs/";

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        try (FileChannel source = new FileInputStream(sourceFile).getChannel();
             FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        }
    }

    public static String getCourseThumbPath(Context context) {
        return context.getFilesDir() + COURSE_THUMBS_DIR;
    }
    public static String getProfileThumbPath(Context context) {
        return context.getFilesDir() + PROFILE_THUMBS_DIR;
    }
}
