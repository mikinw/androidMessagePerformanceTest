package com.example.broadcastReceive;

import android.app.Service;
import android.content.Intent;
import android.os.Trace;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyService extends Service {

    public static final String CREATE = MyService.class.getName() + ".CREATE";
    public static final String RECEIVE = MyService.class.getName() + ".RECEIVE";
    public static final String DO_EXECUTOR = MyService.class.getName() + ".DO_EXECUTOR";
    public static final String DO_GLOBAL = MyService.class.getName() + ".DO_GLOBAL";
    public static final String DO_LOCAL = MyService.class.getName() + ".DO_LOCAL";

    private Executor executor;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        String action = intent.getAction();

        Trace.beginSection("onStartCommand() " + " action: " + action);
        if(DO_LOCAL.equals(action)) {
            new MyLocalRunnable().run();
        } else if(DO_GLOBAL.equals(action)) {
            new MyGlobalRunnable().run();
        } else if(DO_EXECUTOR.equals(action)) {
            executor.execute(new MyGlobalRunnable());
        }
        Trace.endSection();
        return START_NOT_STICKY;
    }

    @Override
    public android.os.IBinder onBind(final Intent intent) {
        return null;
    }

    private class MyLocalRunnable implements Runnable {

        @Override
        public void run() {
            Trace.beginSection("MyLocalRunnable run()");
            Intent intent = new Intent(RECEIVE);
            mLocalBroadcastManager.sendBroadcast(intent);
            Trace.endSection();
        }
    }

    private class MyGlobalRunnable implements Runnable {

        @Override
        public void run() {
            Trace.beginSection("MyGlobalRunnable run()");
            Intent intent = new Intent(RECEIVE);
            sendBroadcast(intent);
            Trace.endSection();
        }
    }
}
