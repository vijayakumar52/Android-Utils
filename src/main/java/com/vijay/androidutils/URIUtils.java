package com.vijay.androidutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

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
        String filename="";
        Cursor returnCursor = activity.getContentResolver().query(uri, null, null, null, null);
        returnCursor.moveToFirst();
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        filename=returnCursor.getString(nameIndex);
        returnCursor.close();
        return filename;

    }

    public static byte[] streamToByteArray(InputStream aInput){
        byte[] bytes = new byte[0];
        try {
            bytes = toByteArray(aInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public static File writeByteArrayToFile(byte[] aInput, String filepath){
        File file=new File(filepath);
        OutputStream output = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            output = new BufferedOutputStream(new FileOutputStream(file));
            output.write(aInput);
            output.close();
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return file;
    }

}
