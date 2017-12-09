package com.vijay.androidutils;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by vijay-3593 on 09/12/17.
 */

public class AudioUtils {

    private static final String TAG = AudioUtils.class.getSimpleName();

    private static String mFileName;
    private static MediaRecorder mRecorder;

    public static void startRecordingAudio(Context ctx) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // Winamp does not play the
        // audio recorded MP3 audio.
        mFileName = AndroidUtils.getStorageDirectory(ctx, "application-utils/audio").getAbsolutePath() + "/" + new Date().getTime() + ".mp3";
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e) {
            Log.e(TAG, " failed" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, " failed" + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, " failed" + e.getMessage());
        }
    }

    public static Uri stopRecordingAudio(Context ctx) {

        try {
            mRecorder.stop();
            mRecorder.reset();    // set state to idle
            mRecorder.release();
            mRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saveAudio(ctx);
    }


    public static Uri writeAudioToMedia(Context ctx, File audioFile) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
        values.put(MediaStore.Audio.Media.ARTIST, "");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        // Now set some extra features it depend on you
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
        Uri uri2 = ctx.getContentResolver().insert(uri, values);

        if (uri2 == null || TextUtils.isEmpty(uri2.toString())) {
            Log.w(TAG, "Something went wrong while inserting data to content resolver");
        }

        return uri2;
    }

    private static Uri saveAudio(Context ctx) {
        File audioFile = new File(mFileName);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
        values.put(MediaStore.Audio.Media.ARTIST, "");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        // Now set some extra features it depend on you
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
        Uri uri2 = ctx.getContentResolver().insert(uri, values);

        if (uri2 == null || TextUtils.isEmpty(uri2.toString())) {
            Log.w(TAG, "Something went wrong while inserting data to content resolver");
        }

        return uri2;
    }

    public static boolean removeAudio(Context ctx, Uri uri) {

        if (uri == null) {
            throw new NullPointerException("Uri cannot be null");
        }

        return (ctx.getContentResolver().delete(uri, null, null) != 0);
    }
}