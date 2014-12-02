package com.example.broadcastReceive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;

    private Handler mHandler;
    private MyThread mMyThread;
    private int count = 0;
    private int maxCount = 0;
    private EditText mEditTextCount;
    private LocalBroadcastManager mLocalBroadcastManager;

    // region Activity lifecycle callbacks

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("|MQ", "onCreate()");

        setContentView(R.layout.main);
        mEditTextCount = (EditText)this.findViewById(R.id.edittext_amount);

        injectObjects();

        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(MyService.RECEIVE));
        mMyThread.run();

    }

    private void injectObjects() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mHandler = new MyHandler(this);
        mMyThread = new MyThread(this);
        mBroadcastReceiver = new MyBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("|MQ", "onResume()" + " MyActivity.this: " + MyActivity.this);
        createService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("|BR", "onPause()");
//        mHandler.removeMessages(1);
    }

    // endregion Activity lifecycle callbacks

    // region Layout click listeners

    public void onDirectCallClick(View view) {
        count = 0;
        maxCount = Integer.parseInt(mEditTextCount.getText().toString());
        Trace.beginSection("onDirectCallClick()");
        for(; count<maxCount; count++) {
            handleResult();
        }
        Log.d("cash-app", "onDirectCallClick() DONE"); // NON-NLS
        Trace.endSection();
    }


    public void onServiceExecutorAndGlobalBroadcastClick(View view) {
        count = 0;
        maxCount = Integer.parseInt(mEditTextCount.getText().toString());
        Trace.beginSection("onServiceExecutorAndGlobalBroadcastClick()");
        Intent intent = new Intent(MyService.DO);
        this.startService(intent);
    }

    public void onPostToHandlerClick(View view) {
        count = 0;
        maxCount = Integer.parseInt(mEditTextCount.getText().toString());
        Trace.beginSection("onPostToHandlerClick()");
        mHandler.dispatchMessage(mHandler.obtainMessage());
    }

    public void onServiceGlobalBroadcastClick(View view) {

    }

    public void onServiceLocalBroadcastClick(View view) {

    }

    // endregion Layout click listeners

    public void createService() {
        Log.d("|BR", "createService()");
        Intent intent = new Intent(MyService.CREATE);
        intent.setClassName(MyActivity.this, MyService.class.getName());
        this.startService(intent);
    }

    public void handleResult() {
        Trace.beginSection("handleResult()");

//        Log.d("|BR", "handleResult()");
        Trace.endSection();
    }

    public void handleHandle() {
        Trace.beginSection("handleHandle()");
        handleResult();
        count++;
        if (count < maxCount) {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(), 0);
        } else {
            Log.d("cash-app", "handleHandle() DONE"); // NON-NLS
            Trace.endSection();
        }
        Trace.endSection();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleResult();
            count++;
            if (count < maxCount) {
                Intent nextIntent = new Intent(MyService.DO);
                nextIntent.setClassName(MyActivity.this, MyService.class.getName());
                startService(nextIntent);
            } else {
                Log.d("cash-app", "onReceive() DONE"); // NON-NLS
                Trace.endSection();
            }

        }
    }
}
