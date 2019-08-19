package com.learnrn1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.learnrn1.aop.MyLog;
import com.learnrn1.download.AppDownLoad;
import com.learnrn1.download.DownloadListener;
import com.learnrn1.manager.SystemAlarmManager;
import com.learnrn1.util.AppConfig;
import com.learnrn1.util.GitPropertiesUtil;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestActivity extends Activity {
    private long firstPressTime;

    @OnClick(R.id.button2)
    public void onClick() {
        Toast.makeText(this, AppConfig.shape, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button3)
    public void onClickDownload() {
        SystemAlarmManager.getInstance().setProcessAlarm("应用下载", "正在下载中....", 0f, this, false);
        AppDownLoad appDownLoad = new AppDownLoad(AppConfig.downloadAppUrl, new DownloadListener() {
            @Override
            public void onFinished() {
                SystemAlarmManager.getInstance().setProcessAlarm("应用下载", "下载完成", 100f, TestActivity.this, true);
            }

            @Override
            public void onProgress(float progress) {
                SystemAlarmManager.getInstance().setProcessAlarm("应用下载", "正在下载中....", progress, TestActivity.this, false);
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }
        });
        try {
            appDownLoad.downLoad();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GitPropertiesUtil.getProperties();
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick2() {
        Intent intent = new Intent();
        intent.setClass(this,Test3Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressTime < 2000) {
            super.onBackPressed();
        }else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressTime = System.currentTimeMillis();
        }
    }
}
