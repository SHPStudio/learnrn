package com.learnrn1;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.learnrn1.db.DaoMaster;
import com.learnrn1.db.DaoSession;
import com.learnrn1.module.CustomModulePackage;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
  private static Context mContext;
  private DaoSession daoSession;

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
              new CustomModulePackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = this;
    SoLoader.init(this, /* native exopackage */ false);
    this.initGreenDao();
  }

  public static Context getContext() {
    return mContext;
  }

  public static ContentResolver getMContentResolver() {
    return mContext.getContentResolver();
  }

  private void initGreenDao() {
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test.db");
    SQLiteDatabase db = helper.getWritableDatabase();
    DaoMaster daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
  }

  public DaoSession getDaoSession() {
    return daoSession;
  }
}
