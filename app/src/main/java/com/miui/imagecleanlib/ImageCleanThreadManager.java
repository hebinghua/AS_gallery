package com.miui.imagecleanlib;

import android.util.Log;
import com.google.common.util.concurrent.Uninterruptibles;
import com.miui.imagecleanlib.ImageCleanTask;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class ImageCleanThreadManager {
    public final BlockingQueue<ImageCleanTaskList> queue = new LinkedBlockingQueue();
    public ImageCleanThreadEngine imageCleanThreadEngine = new ImageCleanThreadEngine(ImageCleanThreadManager$$ExternalSyntheticLambda0.INSTANCE);

    public ImageCleanThreadManager() {
        exec();
    }

    public static /* synthetic */ void lambda$new$0(ImageCleanTask imageCleanTask) {
        Log.v("zman_share", "task done: " + imageCleanTask);
    }

    public void enqueueTaskList(ImageCleanTaskList imageCleanTaskList) {
        Uninterruptibles.putUninterruptibly(this.queue, imageCleanTaskList);
    }

    public final void exec() {
        new Thread() { // from class: com.miui.imagecleanlib.ImageCleanThreadManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                while (true) {
                    ImageCleanTaskList imageCleanTaskList = (ImageCleanTaskList) Uninterruptibles.takeUninterruptibly(ImageCleanThreadManager.this.queue);
                    if (imageCleanTaskList != null) {
                        Log.d("zman_share", "enqueue next list with size: " + imageCleanTaskList.list.size());
                        CountDownLatch countDownLatch = new CountDownLatch(imageCleanTaskList.list.size());
                        imageCleanTaskList.setDoneSignal(countDownLatch);
                        for (int i = 0; i < imageCleanTaskList.list.size(); i++) {
                            ImageCleanThreadManager.this.imageCleanThreadEngine.enqueueTask(imageCleanTaskList.list.get(i));
                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    /* loaded from: classes3.dex */
    public static class ImageCleanTaskList implements ImageCleanTask.TaskListener {
        public CleanImagesListener cleanImagesListener;
        public int finished;
        public List<ImageCleanTask> list;
        public CountDownLatch mDoneSignal;

        public ImageCleanTaskList(List<ImageCleanTask> list, CleanImagesListener cleanImagesListener) {
            this.list = list;
            this.cleanImagesListener = cleanImagesListener;
            for (ImageCleanTask imageCleanTask : list) {
                imageCleanTask.setTaskListener(this);
            }
        }

        public void setDoneSignal(CountDownLatch countDownLatch) {
            this.mDoneSignal = countDownLatch;
        }

        public final synchronized void arrive() {
            CountDownLatch countDownLatch = this.mDoneSignal;
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            int i = this.finished + 1;
            this.finished = i;
            if (i == this.list.size()) {
                String[] strArr = new String[this.list.size()];
                for (int i2 = 0; i2 < this.list.size(); i2++) {
                    strArr[i2] = this.list.get(i2).dstPath;
                }
                this.cleanImagesListener.onProgress(this.finished);
                this.cleanImagesListener.onSuccess(strArr);
            } else {
                this.cleanImagesListener.onProgress(this.finished);
            }
        }

        @Override // com.miui.imagecleanlib.ImageCleanTask.TaskListener
        public void onDone() {
            arrive();
        }
    }
}
