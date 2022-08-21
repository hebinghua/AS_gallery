package com.miui.gallery.monitor;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class FileLog {
    public int mActiveCache;
    public final LinkedList<String> mCache0;
    public final LinkedList<String> mCache1;
    public final Object mLock;
    public final LogTask mTask;

    public FileLog() {
        this(null);
    }

    public FileLog(String str) {
        this.mLock = new Object();
        LogTask logTask = new LogTask(str);
        this.mTask = logTask;
        this.mCache0 = new LinkedList<>();
        this.mCache1 = new LinkedList<>();
        logTask.setPriority(4);
        logTask.start();
    }

    public void log(String str) {
        LinkedList<String> linkedList;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mLock) {
            linkedList = this.mActiveCache == 0 ? this.mCache0 : this.mCache1;
        }
        linkedList.add(str);
        this.mTask.active();
        Log.d("FileLog", "log cost: " + (System.currentTimeMillis() - currentTimeMillis));
    }

    /* loaded from: classes2.dex */
    public class LogTask extends Thread {
        public volatile boolean mActive = true;
        public final String mLogPath;
        public BufferedWriter mWriter;

        public LogTask(String str) {
            this.mLogPath = str;
        }

        public final String getDefaultLogPath() {
            return new File(Environment.getExternalStorageDirectory(), String.format("blog_%s", new SimpleDateFormat("yyyy_MM_dd").format(new Date()))).getAbsolutePath();
        }

        public final String getLogPath() {
            return TextUtils.isEmpty(this.mLogPath) ? getDefaultLogPath() : this.mLogPath;
        }

        public void active() {
            synchronized (this) {
                notifyAll();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            LinkedList linkedList;
            try {
                this.mWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getLogPath(), true)));
            } catch (IOException e) {
                this.mWriter = null;
                e.printStackTrace();
            }
            if (this.mWriter != null) {
                while (this.mActive) {
                    try {
                        synchronized (FileLog.this.mLock) {
                            linkedList = FileLog.this.mActiveCache == 0 ? FileLog.this.mCache0 : FileLog.this.mCache1;
                            Log.d("FileLog", "write active cache: " + FileLog.this.mActiveCache);
                            FileLog fileLog = FileLog.this;
                            fileLog.mActiveCache = 1 - fileLog.mActiveCache;
                        }
                        Iterator it = linkedList.iterator();
                        while (it.hasNext()) {
                            this.mWriter.write((String) it.next());
                            this.mWriter.write("\n");
                        }
                        this.mWriter.flush();
                        linkedList.clear();
                        LinkedList linkedList2 = FileLog.this.mActiveCache == 0 ? FileLog.this.mCache0 : FileLog.this.mCache1;
                        synchronized (this) {
                            while (linkedList2.size() <= 0) {
                                Log.d("FileLog", "wait cache: " + FileLog.this.mActiveCache);
                                wait();
                            }
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                this.mWriter.close();
                this.mWriter = null;
            }
        }
    }
}
