/* $Id$ */
package com.vijay.androidutils.network;

public interface NetworkProgressView {

    void onStarted();

    void onProgress();

    void onFinished();

    boolean dismissDialogOnSuccess();
}
