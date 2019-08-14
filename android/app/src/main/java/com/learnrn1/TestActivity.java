package com.learnrn1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.learnrn1.util.AppConfig;
import com.learnrn1.util.GitPropertiesUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestActivity extends Activity {

    @OnClick(R.id.button2)
    public void onClick() {
        Toast.makeText(this, AppConfig.shape, Toast.LENGTH_SHORT).show();
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
        intent.setClass(this,Test2Activity.class);
        startActivity(intent);
    }
}
