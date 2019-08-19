package com.learnrn1.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.common.base.Strings;
import com.learnrn1.util.FileUtil;
import com.learnrn1.util.HttpUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppDownLoad extends Handler {
    private String defaultDownloadFilePath;
    private String url;
    private String fileName;
    private DownloadListener downloadListener;
    private HttpUtil httpUtil;
    private long total;
    private long process;
    private String message;
    private final static int PROCESS = 1;
    private final static int ERROR = 2;
    private final static int FINISH = 3;
    private final static int PAUSE = 4;


    public AppDownLoad(String url, DownloadListener downloadListener) {
        this.init(url, null, downloadListener);
    }

    public AppDownLoad(String url, String fileName, DownloadListener downloadListener) {
        this.init(url, fileName, downloadListener);
    }

    private void init(String url, String fileName, DownloadListener downloadListener) {
        this.url = url;
        this.downloadListener = downloadListener;
        this.fileName = fileName;
        this.httpUtil = HttpUtil.getInstance();
        this.getDefaultDownloadFilePath();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PROCESS:
                this.downloadListener.onProgress(process * 1.0f / total);
                break;
            case ERROR:
                this.downloadListener.onError(this.message);
                break;
            case FINISH:
                this.downloadListener.onFinished();
                break;
            case PAUSE:
                this.downloadListener.onPause();
                break;
        }
    }

    public void downLoad() throws IOException {
        httpUtil.downloadFileAsyn(this.url, new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("下载异常:" + e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    message = response.message();
                    sendEmptyMessage(ERROR);
                    return;
                }
                total = response.body().contentLength();
                String finalFileName = Strings.isNullOrEmpty(fileName) ? FileUtil.getFileNameFromUrl(url) : fileName;
                try(InputStream inputStream = response.body().byteStream(); FileOutputStream fileOutputStream = new FileOutputStream(new File(defaultDownloadFilePath + finalFileName))) {
                    byte[] buffer = new byte[1024 << 2];
                    int length = -1;
                    long total = 0;
                    while ((length = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                        total += length;
                        process = total;
                        sendEmptyMessage(PROCESS);
                    }
                }
                sendEmptyMessage(FINISH);
            }
        });
    }

    private void getDefaultDownloadFilePath() {
        File file = new File( Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "shape");
        if (!file.exists()) {
            file.mkdir();
        }
        if (TextUtils.isEmpty(defaultDownloadFilePath)) {
            defaultDownloadFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "shape" + File.separator;
        }
    }
}
