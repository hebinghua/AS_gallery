package com.miui.imagecleanlib;

/* loaded from: classes3.dex */
public class ImageCleanTask {
    public boolean clearBaseInfo;
    public boolean clearLocation;
    public String dstPath;
    public String srcPath;
    public TaskListener taskListener = null;

    /* loaded from: classes3.dex */
    public interface TaskListener {
        void onDone();
    }

    public ImageCleanTask(String str, String str2, boolean z, boolean z2) {
        this.srcPath = str;
        this.dstPath = str2;
        this.clearLocation = z;
        this.clearBaseInfo = z2;
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public void onDone() {
        TaskListener taskListener = this.taskListener;
        if (taskListener != null) {
            taskListener.onDone();
        }
    }
}
