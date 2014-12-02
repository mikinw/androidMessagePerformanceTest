package com.example.broadcastReceive;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * TODO description of this class is missing
 */
public class MyThread extends Thread {

    private final MyActivity mMyActivity;
    private MyHandler mMyHandler;

    public MyThread(MyActivity myActivity) {
        mMyActivity = myActivity;

        mMyHandler = new MyHandler(myActivity);

    }

    synchronized public void send(int i, int i1) {
        mMyHandler.sendEmptyMessageDelayed(i, i1);
    }

    @Override
    public void run() {
        super.run();

    }
}
