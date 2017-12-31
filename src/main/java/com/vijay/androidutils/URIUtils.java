package com.vijay.androidutils;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresPermission;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class URIUtils {

    public static String getFileNameFromContentUri(Context activity, Uri uri) {
        String filename = "";
        Cursor returnCursor = activity.getContentResolver().query(uri, null, null, null, null);
        returnCursor.moveToFirst();
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        filename = returnCursor.getString(nameIndex);
        returnCursor.close();
        return filename;

    }

   /* @RequiresPermission(
            allOf = {Manifest.permission.READ_EXTERNAL_STORAGE}
    )*/
    public static File getFileFromUri(Context context, Uri uri) throws Exception {
        File file = null;
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            String filePath = uri.getPath();
            file = new File(filePath);
        } else if ("content".equals(scheme)) {
            String fileName = URIUtils.getFileNameFromContentUri(context, uri);
            byte[] response = IOUtils.streamToByteArray(context.getContentResolver().openInputStream(uri));
            file = IOUtils.writeByteArrayToFile(response, new File(context.getFilesDir(), fileName).getPath());
        }
        return file;
    }
}
