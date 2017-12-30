package com.vijay.androidutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vijay-3593 on 09/12/17.
 */


public class BitmapUtils {

    private final static String TAG = BitmapUtils.class.getSimpleName();

    private static final String ERROR_URI_NULL = "Uri cannot be null";

    public static String toBase64(Bitmap bitmap) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }

        String base64Bitmap = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBitmap = stream.toByteArray();
        base64Bitmap = Base64.encodeToString(imageBitmap, Base64.DEFAULT);

        return base64Bitmap;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable == null) {
            throw new NullPointerException("Drawable to convert should NOT be null");
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 && drawable.getIntrinsicHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public static InputStream bitmapToInputStream(Bitmap bitmap) throws NullPointerException {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream inputstream = new ByteArrayInputStream(baos.toByteArray());

        return inputstream;
    }

    public static Bitmap getDownScaledImage(String filePath, int targetW, int targetH) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bitmapOptions = getBitmapOptions(filePath);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = calculateInSampleSize(photoW, photoH, targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;

        return getBitmap(filePath, bitmapOptions);
    }

    public static Bitmap getDownScaledImage(Context context, int resourceId, int targetW, int targetH) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bitmapOptions = getBitmapOptions(context, resourceId);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = calculateInSampleSize(photoW, photoH, targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;

        return getBitmap(context, resourceId, bitmapOptions);
    }

    public static Bitmap getDownScaledImage(Context context, Uri uri, int targetW, int targetH) {
        Bitmap bitmap = null;
        try {
            // Get the dimensions of the bitmap
            BitmapFactory.Options bitmapOptions = getBitmapOptions(context, uri);
            int photoW = bitmapOptions.outWidth;
            int photoH = bitmapOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = calculateInSampleSize(photoW, photoH, targetW, targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inSampleSize = scaleFactor;

            bitmap = getBitmap(context, uri, bitmapOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getDownScaledImage(byte[] imageByteArray, int targetW, int targetH) {
        BitmapFactory.Options bitmapOptions = getBitmapOptions(imageByteArray);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = calculateInSampleSize(photoW, photoH, targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;

        return getBitmap(imageByteArray, bitmapOptions);
    }


    private static BitmapFactory.Options getBitmapOptions(Context activity, Uri uri) throws FileNotFoundException {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true;
        String scheme = uri.getScheme();
        if ("content".equals(scheme)) { //No i18N
            BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri), null, bitmapOptions);
        } else if ("file".equals(scheme)) { //No i18N
            BitmapFactory.decodeFile(AndroidUtil.getPath(activity, uri), bitmapOptions);
        }
        return bitmapOptions;
    }

    private static BitmapFactory.Options getBitmapOptions(String filePath) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bitmapOptions);
        return bitmapOptions;
    }

    private static BitmapFactory.Options getBitmapOptions(byte[] imageByteArray) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, bitmapOptions);
        return bitmapOptions;
    }

    private static BitmapFactory.Options getBitmapOptions(Context context, int resourceId) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(),
                resourceId, bitmapOptions);
        return bitmapOptions;
    }


    private static Bitmap getBitmap(Context activity, Uri uri, BitmapFactory.Options bitmapOptions) throws FileNotFoundException {
        String scheme = uri.getScheme();
        Bitmap bitmap = null;
        if ("content".equals(scheme)) { //No i18N
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri), null, bitmapOptions);
        } else if ("file".equals(scheme)) { //No i18N
            bitmap = BitmapFactory.decodeFile(AndroidUtil.getPath(activity, uri), bitmapOptions);
        }
        return bitmap;
    }

    private static Bitmap getBitmap(String filePath, BitmapFactory.Options bitmapOptions) {

        return BitmapFactory.decodeFile(filePath, bitmapOptions);
    }

    private static Bitmap getBitmap(byte[] imageByteArray, BitmapFactory.Options bitmapOptions) {

        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, bitmapOptions);
    }

    private static Bitmap getBitmap(Context context, int resourceId, BitmapFactory.Options bitmapOptions) {

        return BitmapFactory.decodeResource(context.getResources(), resourceId, bitmapOptions);
    }


    public static Bitmap getBitmap(Context activity, Uri uri) {
        String scheme = uri.getScheme();
        Bitmap bitmap = null;
        try {
            if ("content".equals(scheme)) { //No i18N
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
            } else if ("file".equals(scheme)) { //No i18N
                bitmap = BitmapFactory.decodeFile(AndroidUtil.getPath(activity, uri));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(int imageWidth, int imageHeight, int reqWidth, int reqHeight) {

        int inSampleSize = 1;
        if (imageWidth > reqHeight || imageHeight > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) imageHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) imageWidth / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getBitmap(String filePath) {

        return BitmapFactory.decodeFile(filePath);
    }

    public static Bitmap getBitmap(byte[] imageByteArray) {

        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }

    public static Bitmap getBitmap(Context context, int resourceId) {

        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }


    public static Bitmap getCroppedImage(Bitmap bitmap, int width, int height) {
        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }

}