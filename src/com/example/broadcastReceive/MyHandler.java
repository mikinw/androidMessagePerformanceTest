package com.example.broadcastReceive;

import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * TODO description of this class is missing
 */
public class MyHandler extends Handler {
    private final WeakReference<MyActivity> mMyActivity;

    public MyHandler(MyActivity myActivity) {

        mMyActivity = new WeakReference<MyActivity>(myActivity);
    }

    @Override
    public void handleMessage(Message msg) {
//        if (mMyActivity.get() != null) {
//            Log.d("|MQ", "handleMessage()" + " MyActivity.this: " + mMyActivity.get());
//        } else {
//            Log.d("|MQ", "handleMessage()" + " no MyActivity");
//
//        }
        Trace.beginSection("handleMessage()");

        mMyActivity.get().handleHandle();
        Trace.endSection();
    }
}
