package com.vijay.androidutils;

import android.content.Context;
import android.graphics.Bitmap;
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



    public static Bitmap scaleDownBitmap(Context ctx, Bitmap source, int newHeight) {
        final float densityMultiplier = getDensityMultiplier(ctx);

        // Log.v( TAG, "#scaleDownBitmap Original w: " + source.getWidth() + " h: " +
        // source.getHeight() );

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * source.getWidth() / ((double) source.getHeight()));

        // Log.v( TAG, "#scaleDownBitmap Computed w: " + w + " h: " + h );

        Bitmap photo = Bitmap.createScaledBitmap(source, w, h, true);

        // Log.v( TAG, "#scaleDownBitmap Final w: " + w + " h: " + h );

        return photo;
    }

    private static float getDensityMultiplier(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static Bitmap scaleBitmap(Context ctx, Bitmap source, int newHeight) {

        // Log.v( TAG, "#scaleDownBitmap Original w: " + source.getWidth() + " h: " +
        // source.getHeight() );

        int w = (int) (newHeight * source.getWidth() / ((double) source.getHeight()));

        // Log.v( TAG, "#scaleDownBitmap Computed w: " + w + " h: " + newHeight );

        Bitmap photo = Bitmap.createScaledBitmap(source, w, newHeight, true);

        // Log.v( TAG, "#scaleDownBitmap Final w: " + w + " h: " + newHeight );

        return photo;
    }

    public static Bitmap scaleDownBitmap(Context ctx, Uri uri, int newHeight) throws FileNotFoundException, IOException {
        Bitmap original = Media.getBitmap(ctx.getContentResolver(), uri);
        return scaleBitmap(ctx, original, newHeight);
    }
}