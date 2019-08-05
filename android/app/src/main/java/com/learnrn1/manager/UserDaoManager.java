package com.learnrn1.manager;

import com.learnrn1.MainApplication;
import com.learnrn1.db.DaoSession;
import com.learnrn1.db.UserDao;
import com.learnrn1.model.User;

import java.util.List;

public class UserDaoManager {
    private static UserDaoManager instance = new UserDaoManager();

    private UserDaoManager() {

    }

    private UserDao getUserDao() {
        MainApplication mainApplication = (MainApplication) MainApplication.getContext();
        DaoSession daoSession = mainApplication.getDaoSession();
        return daoSession.getUserDao();
    }

    public static UserDaoManager getInstance() {
        return instance;
    }

    public long insertOrReplace(User user) {
        return getUserDao().insertOrReplace(user);
    }

    public List<User> queryAll() {
        return getUserDao().loadAll();
    }
}
