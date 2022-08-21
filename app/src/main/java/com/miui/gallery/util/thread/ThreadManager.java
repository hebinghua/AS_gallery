package com.miui.gallery.util.thread;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class ThreadManager extends com.miui.gallery.util.concurrent.ThreadManager {
    public static volatile ThreadPool sComputation;
    public static volatile ThreadPool sInnerPool;
    public static volatile ThreadPool sRightNowPool;
    public static final Object sRightNowPoolLock = new Object();
    public static final Object sInnerPoolLock = new Object();
    public static final Object sComputationPoolLock = new Object();
    public static final RejectedExecutionHandler DEFAULT_REJECT_EXECUTION_HANDLER = new RejectedExecutionHandler() { // from class: com.miui.gallery.util.thread.ThreadManager.2
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            DebugUtil.printThreadPoolStatus(threadPoolExecutor, true);
        }
    };

    public static void execute(int i, Runnable runnable) {
        initThreadPool(i);
        if (i == 31) {
            sRightNowPool.asExecutorService().execute(runnable);
        } else if (i == 47) {
            sInnerPool.asExecutorService().execute(runnable);
        } else if (i == 79) {
            sComputation.asExecutorService().execute(runnable);
        } else if (i == 95) {
            com.miui.gallery.util.concurrent.ThreadManager.getRegionDecodePool().asExecutorService().execute(runnable);
        } else if (i == 111) {
            com.miui.gallery.util.concurrent.ThreadManager.getRequestPool().asExecutorService().execute(runnable);
        } else if (i == 127) {
            com.miui.gallery.util.concurrent.ThreadManager.getTileProviderPool().asExecutorService().execute(runnable);
        } else if (i == 159) {
            com.miui.gallery.util.concurrent.ThreadManager.getDecodePoolForHeif().asExecutorService().execute(runnable);
        } else {
            com.miui.gallery.util.concurrent.ThreadManager.getMiscPool().asExecutorService().execute(runnable);
        }
    }

    public static void initThreadPool(int i) {
        if (sRightNowPool == null || sInnerPool == null || sComputation == null) {
            if (i == 31) {
                if (sRightNowPool != null) {
                    return;
                }
                synchronized (sRightNowPoolLock) {
                    if (sRightNowPool == null) {
                        sRightNowPool = new ThreadPool.Config().setCorePoolSize(ThreadConfig.getSuggestThreadCoreSize()).setMaxPoolSize(64).setThreadKeepAliveTime(1L).setThreadKeepAliveTimeUnit(TimeUnit.SECONDS).setTaskWorkQueue(new LinkedBlockingQueue(10)).setThreadFactory(new ThreadFactory(0, "gallery_Right_Now_")).setRejectedExecutionHandler(new RejectedExecutionHandler() { // from class: com.miui.gallery.util.thread.ThreadManager.1
                            @Override // java.util.concurrent.RejectedExecutionHandler
                            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                                DebugUtil.printThreadPoolStatus(threadPoolExecutor, true);
                                DefaultLogger.e("ThreadManager", "The current thread state is saturated, switch the thread pool to computation!");
                                ThreadManager.execute(79, runnable);
                            }
                        }).build();
                    }
                }
            } else if (i == 47) {
                if (sInnerPool != null) {
                    return;
                }
                synchronized (sInnerPoolLock) {
                    if (sInnerPool == null) {
                        sInnerPool = new ThreadPool.Config().setCorePoolSize(Math.min(2, ThreadConfig.getSuggestThreadCoreSize())).setMaxPoolSize(ThreadConfig.getSuggestThreadMaxSize()).setTaskWorkQueue(new LinkedBlockingQueue(64)).setRejectedExecutionHandler(DEFAULT_REJECT_EXECUTION_HANDLER).setThreadFactory(new ThreadFactory(10, "gallery_Local_")).build();
                    }
                }
            } else if (i != 79 || sComputation != null) {
            } else {
                synchronized (sComputationPoolLock) {
                    if (sComputation == null) {
                        sComputation = new ThreadPool.Config().setCorePoolSize(Math.min(2, ThreadConfig.getSuggestThreadCoreSize())).setMaxPoolSize(64).setThreadKeepAliveTime(1L).setThreadKeepAliveTimeUnit(TimeUnit.SECONDS).setRejectedExecutionHandler(DEFAULT_REJECT_EXECUTION_HANDLER).setTaskWorkQueue(new LinkedBlockingQueue(1)).setThreadFactory(new ThreadFactory(0, "gallery_Computation_")).build();
                    }
                }
            }
        }
    }

    public static Executor getExecutor(int i) {
        initThreadPool(i);
        if (i != 31) {
            if (i == 47) {
                return sInnerPool.asExecutorService();
            }
            if (i == 79) {
                return sComputation.asExecutorService();
            }
            if (i == 95) {
                return com.miui.gallery.util.concurrent.ThreadManager.getRegionDecodePool().asExecutorService();
            }
            if (i == 111) {
                return com.miui.gallery.util.concurrent.ThreadManager.getRequestPool().asExecutorService();
            }
            if (i == 127) {
                return com.miui.gallery.util.concurrent.ThreadManager.getTileProviderPool().asExecutorService();
            }
            if (i == 159) {
                return com.miui.gallery.util.concurrent.ThreadManager.getDecodePoolForHeif().asExecutorService();
            }
            return com.miui.gallery.util.concurrent.ThreadManager.getMiscPool().asExecutorService();
        }
        return sRightNowPool.asExecutorService();
    }

    public static void printAllThreadPoolStatus() {
        DefaultLogger.d("ThreadManager", "-------------------------- current All thread status start");
        printThreadPoolStatus(31);
        printThreadPoolStatus(47);
        printThreadPoolStatus(79);
        printThreadPoolStatus(95);
        printThreadPoolStatus(159);
        printThreadPoolStatus(BaiduSceneResult.BANK_CARD);
        printThreadPoolStatus(111);
        printThreadPoolStatus(BaiduSceneResult.BLACK_WHITE);
        DefaultLogger.d("ThreadManager", "-------------------------- current All thread status end");
    }

    public static void printThreadPoolStatus(int i) {
        ThreadPoolExecutor threadPoolExecutor;
        if (i != 31) {
            if (i != 47) {
                if (i != 79) {
                    if (i == 95) {
                        threadPoolExecutor = (ThreadPoolExecutor) com.miui.gallery.util.concurrent.ThreadManager.getRegionDecodePool().asExecutorService();
                    } else if (i == 111) {
                        threadPoolExecutor = (ThreadPoolExecutor) com.miui.gallery.util.concurrent.ThreadManager.getRequestPool().asExecutorService();
                    } else if (i == 127) {
                        threadPoolExecutor = (ThreadPoolExecutor) com.miui.gallery.util.concurrent.ThreadManager.getTileProviderPool().asExecutorService();
                    } else if (i != 143) {
                        threadPoolExecutor = i != 159 ? null : (ThreadPoolExecutor) com.miui.gallery.util.concurrent.ThreadManager.getDecodePoolForHeif().asExecutorService();
                    } else {
                        threadPoolExecutor = (ThreadPoolExecutor) com.miui.gallery.util.concurrent.ThreadManager.getMiscPool().asExecutorService();
                    }
                } else if (sComputation == null) {
                    return;
                } else {
                    threadPoolExecutor = (ThreadPoolExecutor) sComputation.asExecutorService();
                }
            } else if (sInnerPool == null) {
                return;
            } else {
                threadPoolExecutor = (ThreadPoolExecutor) sInnerPool.asExecutorService();
            }
        } else if (sRightNowPool == null) {
            return;
        } else {
            threadPoolExecutor = (ThreadPoolExecutor) sRightNowPool.asExecutorService();
        }
        DebugUtil.printThreadPoolStatus(threadPoolExecutor, true);
    }

    /* loaded from: classes2.dex */
    public static class ThreadFactory implements java.util.concurrent.ThreadFactory {
        public static final AtomicInteger THREAD_NUMBERING = new AtomicInteger();
        public final String mNamePrefix;
        public int priority;

        public ThreadFactory(int i, String str) {
            this.priority = i;
            this.mNamePrefix = str;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            StringBuilder sb;
            String str;
            if (runnable instanceof GalleryRunnable) {
                return new GalleryThread(runnable, ((GalleryRunnable) runnable).getName(), this.priority);
            }
            if (this.mNamePrefix == null) {
                sb = new StringBuilder();
                str = "gallery_";
            } else {
                sb = new StringBuilder();
                str = this.mNamePrefix;
            }
            sb.append(str);
            sb.append(THREAD_NUMBERING.getAndIncrement());
            return new GalleryThread(runnable, sb.toString(), this.priority);
        }
    }
}
