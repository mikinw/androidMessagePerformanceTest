androidMessagePerformanceTest
=============================

A small experiment to measure the performance of certain ways of calling functions on Android: direct call, call runnable through Handler, call it via a Service (and sending the result back through broadcast).

I used this tool for the profiling: http://developer.android.com/tools/help/systrace.html

Tests performed:
 * direct call of a function in a loop
 * Message is posted (delayed with 0 delay) to a Handler. The handleMessage() posts a similar Message to itself
 * Start Service with implicit intent, the service runs on an Executor and sends result back with global Broadcast
 * Start Service with implicit intent, the service runs on the UI thread and sends result back with global Broadcast
 * Start Service with explicit intent, the service runs on the UI thread and sends result back with LocalBroadcaster (v4 support lib)

The results (cascading 1000 times):

| test name                                  | Nexus 7  | Samsung Galaxy S3 (average)
|--------------------------------------------|----------|---------------------------
| direct call                                | 52 ms    | 30 ms
| post to handler                            | 166 ms   | 87 ms
| service /w executor and global broadcast   | 2366 ms  | 2700 ms
| service ui thread & global broadcast       | 2247 ms  | 3200 ms
| service explicit intent & local broadcast  | 1150 ms  | 1470 ms

**Notes**: broadcasting things actually is relatively fast, though you shouldn't use it for real time data (of course). Local broadcasting is twice as fast as global broadcast. However localBroadcaster uses a Handler internally (check source), but it resolves the Intent target. That makes the call 7-17 times slower (compared to sending message to a Handler). So if you know the target, use a handler instead. :-)

You can also check out an example run (Samsung Galaxy S3) in the trace.html
