package com.example.broadcastReceive;

import android.app.Service;
import android.content.Intent;
import android.os.Trace;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * TODO description of this class is missing
 */
public class MyService extends Service {

    public static final String CREATE = MyService.class.getName() + ".CREATE";
    public static final String RECEIVE = MyService.class.getName() + ".RECEIVE";
    public static final String DO = MyService.class.getName() + ".DO";

    public Executor executor;

    private LocalBroadcastManager mLocalBroadcastManager;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        String action = intent.getAction();

//        Log.d("|BR", "onStartCommand() - 1");
        Trace.beginSection("onStartCommand()");
        if(DO.equals(action)) {
//            executor.execute(new MyRunnable());
            new MyRunnable().run();
//            Log.d("|BR", "onStartCommand() - 2");

        } else {
            Log.d("cash-app", "created"); // NON-NLS
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        }
        Trace.endSection();
        return START_NOT_STICKY;
    }


    /**
     * This service is not bindable.
     * @param intent
     * @return null always
     */
    @Override
    public android.os.IBinder onBind(final Intent intent) {
        return null;
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
//            Log.d("|BR", "run() - 1");
//
//            try {
//                Thread.sleep(1000 * 8);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            Log.d("|BR", "run() - 2");
            Trace.beginSection("service run()");
            Intent intent = new Intent(RECEIVE);
//            intent.putExtra("ASDF", "QWER");
            mLocalBroadcastManager.sendBroadcast(intent);
            Trace.endSection();

//            Log.d("|BR", "run() - 3");

        }
    }
}
