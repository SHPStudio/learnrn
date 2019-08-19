package com.learnrn1.download;

public interface DownloadListener {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();

    void onError(String message);
}
