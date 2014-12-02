package com.example.broadcastReceive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * IMPORTANT! Do not consider this class to well designed!
 */
public class MyActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;
    private Handler mHandler;
    private LocalBroadcastManager mLocalBroadcastManager;
    private EditText mEditTextCount;
    private ViewGroup mViewGroup;
    private Intent mIntent;

    private int count = 0;
    private int maxCount = 0;

    // region Activity lifecycle callbacks

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("androidMessagePerformanceTest", "onCreate()");

        setContentView(R.layout.main);
        mEditTextCount = (EditText)findViewById(R.id.edittext_amount);
        mViewGroup = (ViewGroup)findViewById(R.id.layout);

        injectObjects();

        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(MyService.RECEIVE));
        registerReceiver(mBroadcastReceiver, new IntentFilter(MyService.RECEIVE));
    }

    private void injectObjects() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mHandler = new MyHandler();
        mBroadcastReceiver = new MyBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("androidMessagePerformanceTest", "onResume()");

        Intent intent = new Intent(MyService.CREATE);
        intent.setClassName(MyActivity.this, MyService.class.getName());
        this.startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("androidMessagePerformanceTest", "onPause()");
    }

    // endregion Activity lifecycle callbacks

    // region Layout click listeners

    public void onDirectCallClick(View view) {
        igniteProcess();
        Trace.beginSection("onDirectCallClick()");
        for(; count<maxCount; count++) {
            handleResult();
        }
        processEnded("onDirectCallClick()");
    }

    public void onPostToHandlerClick(View view) {
        igniteProcess();
        Trace.beginSection("onPostToHandlerClick()");
        mHandler.dispatchMessage(mHandler.obtainMessage());
    }

    public void onServiceExecutorAndGlobalBroadcastClick(View view) {
        igniteProcess();
        mIntent = new Intent(MyService.DO_EXECUTOR);
        Trace.beginSection("onServiceExecutorAndGlobalBroadcastClick()");
        this.startService(mIntent);
    }

    public void onServiceGlobalBroadcastClick(View view) {
        igniteProcess();
        mIntent = new Intent(MyService.DO_GLOBAL);
        Trace.beginSection("onServiceGlobalBroadcastClick()");
        this.startService(mIntent);

    }

    public void onServiceLocalBroadcastClick(View view) {
        igniteProcess();
        mIntent = new Intent(MyService.DO_LOCAL);
        mIntent.setClass(this, MyService.class);
        Trace.beginSection("onServiceLocalBroadcastClick()");
        this.startService(mIntent);
    }

    // endregion Layout click listeners

    private void processEnded(final String methodName) {
        Log.d("androidMessagePerformanceTest", methodName + " DONE"); // NON-NLS
        Trace.endSection();
        for(int i = 0; i < mViewGroup.getChildCount(); i++) {
            mViewGroup.getChildAt(i).setEnabled(true);
        }
    }

    private void igniteProcess() {
        count = 0;
        maxCount = Integer.parseInt(mEditTextCount.getText().toString());
        for(int i = 0; i < mViewGroup.getChildCount(); i++) {
            mViewGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void handleResult() {
        Trace.beginSection("handleResult()");
// lets do some things here
//        Log.d("|BR", "handleResult()");
        Trace.endSection();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Trace.beginSection("onReceive()");
            handleResult();
            count++;
            if (count < maxCount) {
                startService(mIntent);
            } else {
                processEnded("onReceive()");
            }
            Trace.endSection();
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Trace.beginSection("handleHandle()");
            handleResult();
            count++;
            if (count < maxCount) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 0);
            } else {
                processEnded("handleHandle()");
            }
            Trace.endSection();
        }
    }
}
