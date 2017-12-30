/* $Id$ */
package com.vijay.androidutils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class AndroidUtil {
	
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
    	return "com.android.externalstorage.documents".equals(uri.getAuthority());// No I18N
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
    	return "com.android.providers.downloads.documents".equals(uri.getAuthority());// No I18N
    }
    public static int webunitsToDP(float in, int unit, Activity activity){
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        float float_ml = ((in/9)*((rect.width())/dm.xdpi));
        return (int) TypedValue.applyDimension(unit,float_ml,activity.getResources().getDisplayMetrics());
	}

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
    	return "com.android.providers.media.documents".equals(uri.getAuthority());// No I18N
    }
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */

    public static String getPath(final Context context, final Uri uri) {

    	final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

    	// DocumentProvider
    	if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
    		// ExternalStorageProvider
    		if (isExternalStorageDocument(uri)) {
    			final String docId = DocumentsContract.getDocumentId(uri);
    			final String[] split = docId.split(":");// No I18N
    			final String type = split[0];

    			if ("primary".equalsIgnoreCase(type)) {// No I18N
    				return Environment.getExternalStorageDirectory() + "/" + split[1];// No I18N
    			}

    			// TODO handle non-primary volumes
    		}
    		// DownloadsProvider
    		else if (isDownloadsDocument(uri)) {

    			final String id = DocumentsContract.getDocumentId(uri);
    			final Uri contentUri = ContentUris.withAppendedId(
    					Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));// No I18N

    			return getDataColumn(context, contentUri, null, null);
    		}
    		// MediaProvider
    		else if (isMediaDocument(uri)) {
    			final String docId = DocumentsContract.getDocumentId(uri);
    			final String[] split = docId.split(":");// No I18N
    			final String type = split[0];

    			Uri contentUri = null;
    			if ("image".equals(type)) {// No I18N
    				contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    			} else if ("video".equals(type)) {// No I18N
    				contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    			} else if ("audio".equals(type)) {// No I18N
    				contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    			}

    			final String selection = "_id=?";// No I18N
    			final String[] selectionArgs = new String[] {
    					split[1]
    			};

    			return getDataColumn(context, contentUri, selection, selectionArgs);
    		}
    	}
    	// MediaStore (and general)
    	else if ("content".equalsIgnoreCase(uri.getScheme())) {// No I18N
    		return getDataColumn(context, uri, null, null);
    	}
    	// File
    	else if ("file".equalsIgnoreCase(uri.getScheme())) {// No I18N
    		return uri.getPath();
    	}

    	return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
    		String[] selectionArgs) {

    	Cursor cursor = null;
    	final String column = "_data";// No I18N
    	final String[] projection = {
    			column
    	};

    	try {
    		cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
    				null);
    		if (cursor != null && cursor.moveToFirst()) {
    			final int column_index = cursor.getColumnIndexOrThrow(column);
    			return cursor.getString(column_index);
    		}
    	} finally {
    		if (cursor != null)
    			{
    				cursor.close();
    			}
    	}
    	return null;
    }

}
