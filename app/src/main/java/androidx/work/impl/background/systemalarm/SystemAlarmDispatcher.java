package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Processor;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.SerialExecutor;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.impl.utils.WorkTimer;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SystemAlarmDispatcher implements ExecutionListener {
    public static final String TAG = Logger.tagWithPrefix("SystemAlarmDispatcher");
    public final CommandHandler mCommandHandler;
    public CommandsCompletedListener mCompletedListener;
    public final Context mContext;
    public Intent mCurrentIntent;
    public final List<Intent> mIntents;
    public final Handler mMainHandler;
    public final Processor mProcessor;
    public final TaskExecutor mTaskExecutor;
    public final WorkManagerImpl mWorkManager;
    public final WorkTimer mWorkTimer;

    /* loaded from: classes.dex */
    public interface CommandsCompletedListener {
        void onAllCommandsCompleted();
    }

    public SystemAlarmDispatcher(Context context) {
        this(context, null, null);
    }

    public SystemAlarmDispatcher(Context context, Processor processor, WorkManagerImpl workManager) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mCommandHandler = new CommandHandler(applicationContext);
        this.mWorkTimer = new WorkTimer();
        workManager = workManager == null ? WorkManagerImpl.getInstance(context) : workManager;
        this.mWorkManager = workManager;
        processor = processor == null ? workManager.getProcessor() : processor;
        this.mProcessor = processor;
        this.mTaskExecutor = workManager.getWorkTaskExecutor();
        processor.addExecutionListener(this);
        this.mIntents = new ArrayList();
        this.mCurrentIntent = null;
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void onDestroy() {
        Logger.get().debug(TAG, "Destroying SystemAlarmDispatcher", new Throwable[0]);
        this.mProcessor.removeExecutionListener(this);
        this.mWorkTimer.onDestroy();
        this.mCompletedListener = null;
    }

    @Override // androidx.work.impl.ExecutionListener
    public void onExecuted(String workSpecId, boolean needsReschedule) {
        postOnMainThread(new AddRunnable(this, CommandHandler.createExecutionCompletedIntent(this.mContext, workSpecId, needsReschedule), 0));
    }

    public boolean add(final Intent intent, final int startId) {
        Logger logger = Logger.get();
        String str = TAG;
        boolean z = false;
        logger.debug(str, String.format("Adding command %s (%s)", intent, Integer.valueOf(startId)), new Throwable[0]);
        assertMainThread();
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            Logger.get().warning(str, "Unknown command. Ignoring", new Throwable[0]);
            return false;
        } else if ("ACTION_CONSTRAINTS_CHANGED".equals(action) && hasIntentWithAction("ACTION_CONSTRAINTS_CHANGED")) {
            return false;
        } else {
            intent.putExtra("KEY_START_ID", startId);
            synchronized (this.mIntents) {
                if (!this.mIntents.isEmpty()) {
                    z = true;
                }
                this.mIntents.add(intent);
                if (!z) {
                    processCommand();
                }
            }
            return true;
        }
    }

    public void setCompletedListener(CommandsCompletedListener listener) {
        if (this.mCompletedListener != null) {
            Logger.get().error(TAG, "A completion listener for SystemAlarmDispatcher already exists.", new Throwable[0]);
        } else {
            this.mCompletedListener = listener;
        }
    }

    public Processor getProcessor() {
        return this.mProcessor;
    }

    public WorkTimer getWorkTimer() {
        return this.mWorkTimer;
    }

    public WorkManagerImpl getWorkManager() {
        return this.mWorkManager;
    }

    public TaskExecutor getTaskExecutor() {
        return this.mTaskExecutor;
    }

    public void postOnMainThread(Runnable runnable) {
        this.mMainHandler.post(runnable);
    }

    public void dequeueAndCheckForCompletion() {
        Logger logger = Logger.get();
        String str = TAG;
        logger.debug(str, "Checking if commands are complete.", new Throwable[0]);
        assertMainThread();
        synchronized (this.mIntents) {
            if (this.mCurrentIntent != null) {
                Logger.get().debug(str, String.format("Removing command %s", this.mCurrentIntent), new Throwable[0]);
                if (!this.mIntents.remove(0).equals(this.mCurrentIntent)) {
                    throw new IllegalStateException("Dequeue-d command is not the first.");
                }
                this.mCurrentIntent = null;
            }
            SerialExecutor backgroundExecutor = this.mTaskExecutor.getBackgroundExecutor();
            if (!this.mCommandHandler.hasPendingCommands() && this.mIntents.isEmpty() && !backgroundExecutor.hasPendingTasks()) {
                Logger.get().debug(str, "No more commands & intents.", new Throwable[0]);
                CommandsCompletedListener commandsCompletedListener = this.mCompletedListener;
                if (commandsCompletedListener != null) {
                    commandsCompletedListener.onAllCommandsCompleted();
                }
            } else if (!this.mIntents.isEmpty()) {
                processCommand();
            }
        }
    }

    public final void processCommand() {
        assertMainThread();
        PowerManager.WakeLock newWakeLock = WakeLocks.newWakeLock(this.mContext, "ProcessCommand");
        try {
            newWakeLock.acquire();
            this.mWorkManager.getWorkTaskExecutor().executeOnBackgroundThread(new Runnable() { // from class: androidx.work.impl.background.systemalarm.SystemAlarmDispatcher.1
                @Override // java.lang.Runnable
                public void run() {
                    SystemAlarmDispatcher systemAlarmDispatcher;
                    DequeueAndCheckForCompletion dequeueAndCheckForCompletion;
                    synchronized (SystemAlarmDispatcher.this.mIntents) {
                        SystemAlarmDispatcher systemAlarmDispatcher2 = SystemAlarmDispatcher.this;
                        systemAlarmDispatcher2.mCurrentIntent = systemAlarmDispatcher2.mIntents.get(0);
                    }
                    Intent intent = SystemAlarmDispatcher.this.mCurrentIntent;
                    if (intent != null) {
                        String action = intent.getAction();
                        int intExtra = SystemAlarmDispatcher.this.mCurrentIntent.getIntExtra("KEY_START_ID", 0);
                        Logger logger = Logger.get();
                        String str = SystemAlarmDispatcher.TAG;
                        logger.debug(str, String.format("Processing command %s, %s", SystemAlarmDispatcher.this.mCurrentIntent, Integer.valueOf(intExtra)), new Throwable[0]);
                        PowerManager.WakeLock newWakeLock2 = WakeLocks.newWakeLock(SystemAlarmDispatcher.this.mContext, String.format("%s (%s)", action, Integer.valueOf(intExtra)));
                        try {
                            Logger.get().debug(str, String.format("Acquiring operation wake lock (%s) %s", action, newWakeLock2), new Throwable[0]);
                            newWakeLock2.acquire();
                            SystemAlarmDispatcher systemAlarmDispatcher3 = SystemAlarmDispatcher.this;
                            systemAlarmDispatcher3.mCommandHandler.onHandleIntent(systemAlarmDispatcher3.mCurrentIntent, intExtra, systemAlarmDispatcher3);
                            Logger.get().debug(str, String.format("Releasing operation wake lock (%s) %s", action, newWakeLock2), new Throwable[0]);
                            newWakeLock2.release();
                            systemAlarmDispatcher = SystemAlarmDispatcher.this;
                            dequeueAndCheckForCompletion = new DequeueAndCheckForCompletion(systemAlarmDispatcher);
                        } catch (Throwable th) {
                            try {
                                Logger logger2 = Logger.get();
                                String str2 = SystemAlarmDispatcher.TAG;
                                logger2.error(str2, "Unexpected error in onHandleIntent", th);
                                Logger.get().debug(str2, String.format("Releasing operation wake lock (%s) %s", action, newWakeLock2), new Throwable[0]);
                                newWakeLock2.release();
                                systemAlarmDispatcher = SystemAlarmDispatcher.this;
                                dequeueAndCheckForCompletion = new DequeueAndCheckForCompletion(systemAlarmDispatcher);
                            } catch (Throwable th2) {
                                Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", action, newWakeLock2), new Throwable[0]);
                                newWakeLock2.release();
                                SystemAlarmDispatcher systemAlarmDispatcher4 = SystemAlarmDispatcher.this;
                                systemAlarmDispatcher4.postOnMainThread(new DequeueAndCheckForCompletion(systemAlarmDispatcher4));
                                throw th2;
                            }
                        }
                        systemAlarmDispatcher.postOnMainThread(dequeueAndCheckForCompletion);
                    }
                }
            });
        } finally {
            newWakeLock.release();
        }
    }

    public final boolean hasIntentWithAction(String action) {
        assertMainThread();
        synchronized (this.mIntents) {
            for (Intent intent : this.mIntents) {
                if (action.equals(intent.getAction())) {
                    return true;
                }
            }
            return false;
        }
    }

    public final void assertMainThread() {
        if (this.mMainHandler.getLooper().getThread() == Thread.currentThread()) {
            return;
        }
        throw new IllegalStateException("Needs to be invoked on the main thread.");
    }

    /* loaded from: classes.dex */
    public static class DequeueAndCheckForCompletion implements Runnable {
        public final SystemAlarmDispatcher mDispatcher;

        public DequeueAndCheckForCompletion(SystemAlarmDispatcher dispatcher) {
            this.mDispatcher = dispatcher;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mDispatcher.dequeueAndCheckForCompletion();
        }
    }

    /* loaded from: classes.dex */
    public static class AddRunnable implements Runnable {
        public final SystemAlarmDispatcher mDispatcher;
        public final Intent mIntent;
        public final int mStartId;

        public AddRunnable(SystemAlarmDispatcher dispatcher, Intent intent, int startId) {
            this.mDispatcher = dispatcher;
            this.mIntent = intent;
            this.mStartId = startId;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mDispatcher.add(this.mIntent, this.mStartId);
        }
    }
}
