package com.miui.imagecleanlib;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public class ImageCleanThreadEngine {
    public TaskEngineListener taskEngineListener;
    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() { // from class: com.miui.imagecleanlib.ImageCleanThreadEngine.1
        public AtomicInteger mThreadCount = new AtomicInteger();

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ImageCleanThreadEngine-" + this.mThreadCount.getAndIncrement());
        }
    });

    /* loaded from: classes3.dex */
    public interface TaskEngineListener {
        void onTaskDone(ImageCleanTask imageCleanTask);
    }

    public ImageCleanThreadEngine(TaskEngineListener taskEngineListener) {
        this.taskEngineListener = taskEngineListener;
    }

    public void enqueueTask(final ImageCleanTask imageCleanTask) {
        this.threadPoolExecutor.execute(new Runnable() { // from class: com.miui.imagecleanlib.ImageCleanThreadEngine.2
            @Override // java.lang.Runnable
            public void run() {
                File file = new File(imageCleanTask.dstPath);
                ImageCleanUtils.copyFile(new File(imageCleanTask.srcPath), file);
                ImageCleanTask imageCleanTask2 = imageCleanTask;
                ImageCleanUtils.clearImageInfo(file, imageCleanTask2.clearLocation, imageCleanTask2.clearBaseInfo);
                imageCleanTask.onDone();
                if (ImageCleanThreadEngine.this.taskEngineListener != null) {
                    ImageCleanThreadEngine.this.taskEngineListener.onTaskDone(imageCleanTask);
                }
            }
        });
    }
}
