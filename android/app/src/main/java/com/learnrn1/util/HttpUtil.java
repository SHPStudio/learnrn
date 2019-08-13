package com.learnrn1.util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private OkHttpClient mOkHttpClient;
    private static HttpUtil mInstance;
    private final static long CONNECT_TIMEOUT = 60;//超时时间，秒
    private final static long READ_TIMEOUT = 60;//读取时间，秒
    private final static long WRITE_TIMEOUT = 60;//写入时间，秒

    /**
     * @param url        下载链接
     * @param startIndex 下载起始位置
     * @param endIndex   结束为止
     * @param callback   回调
     * @throws IOException
     */
    public void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) throws IOException {
        // 创建一个Request
        // 设置分段下载的头信息。 Range:做分段数据请求,断点续传指示下载的区间。格式: Range bytes=0-1024或者bytes:0-1024
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(url)
                .build();
        doAsync(request, callback);
    }

    public void getContentLength(String url, Callback callback) throws IOException {
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        doAsync(request, callback);
    }

    public void downloadFileAsyn(String url, Callback callback) throws IOException {
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        doAsync(request, callback);
    }

    /**
     * 异步请求
     */
    private void doAsync(Request request, Callback callback) throws IOException {
        //创建请求会话
        Call call = mOkHttpClient.newCall(request);
        //同步执行会话请求
        call.enqueue(callback);
    }

    /**
     * 同步请求
     */
    private Response doSync(Request request) throws IOException {

        //创建请求会话
        Call call = mOkHttpClient.newCall(request);
        //同步执行会话请求
        return call.execute();
    }


    /**
     * @return HttpUtil实例对象
     */
    public static HttpUtil getInstance() {
        if (null == mInstance) {
            synchronized (HttpUtil.class) {
                if (null == mInstance) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 构造方法,配置OkHttpClient
     */
    private HttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS );
        mOkHttpClient.setWriteTimeout(READ_TIMEOUT, TimeUnit.SECONDS );
        mOkHttpClient.setReadTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.getDispatcher().setMaxRequestsPerHost(SystemUtil.getSuitaleProcessors());
    }
}
