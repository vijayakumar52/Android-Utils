package com.vijay.androidutils;

import android.content.Context;

import androidx.annotation.RawRes;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by vijay-3593 on 09/12/17.
 */

public class IOUtils {

    public static byte[] readStreamToBytes(InputStream inputStream) {

        if (inputStream == null) {
            throw new NullPointerException("InputStream is null");
        }

        byte[] bytesData = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            bytesData = buffer.toByteArray();

            // Log.d( TAG, "#readStream data: " + data );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (reader != null) {
                try {
                    reader.close();

                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    // finally

        return bytesData;
    }

    public static byte[] streamToByteArray(InputStream aInput) {
        byte[] bytes = new byte[0];
        try {
            bytes = toByteArray(aInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
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

    public static File writeByteArrayToFile(byte[] aInput, String filepath) {
        File file = new File(filepath);
        OutputStream output = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new BufferedOutputStream(new FileOutputStream(file));
            output.write(aInput);
            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return file;
    }

    public static File getFileFromRaw(Context context, String fileName, @RawRes int rawResId) {
        File cascadeDir = context.getDir("tempDir", Context.MODE_PRIVATE);
        File mCascadeFile = new File(cascadeDir, fileName);
        getFileFromRaw(context, mCascadeFile, rawResId);
        return mCascadeFile;
    }

    public static void getFileFromRaw(Context context, File file, @RawRes int rawResId) {
        try {
            InputStream is = context.getResources().openRawResource(rawResId);
            FileOutputStream os = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
