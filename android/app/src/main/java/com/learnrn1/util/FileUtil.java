package com.learnrn1.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.InputStream;

public class FileUtil {

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static void installNormal(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(getDefaultDownloadFilePath() + apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.xxxxx.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else {
            intent.setDataAndType(Uri.fromFile(new File(getDefaultDownloadFilePath() + apkPath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static String getDefaultDownloadFilePath() {
        File file = new File( Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "shape");
        if (!file.exists()) {
            file.mkdir();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "shape" + File.separator;
    }
}
