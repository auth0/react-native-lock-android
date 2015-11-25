package com.awesomeproject;

import android.app.Application;

import com.auth0.lock.Lock;
import com.auth0.lock.LockProvider;
/*
public class LockApplication extends Application implements LockProvider {

    private Lock lock;

    @Override
    public void onCreate() {
        super.onCreate();
        lock = new Lock.Builder()
                .loadFromApplication(this)
                .closable(true)
                .useEmail(true)
                .useWebView(true)
                .fullscreen(false)
                .build();
    }

    @Override
    public Lock getLock() {
        return lock;
    }
}
*/