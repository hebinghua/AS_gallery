package androidx.work.impl.utils;

import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Scheduler;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkSpecDao;
import java.util.LinkedList;
import java.util.UUID;

/* loaded from: classes.dex */
public abstract class CancelWorkRunnable implements Runnable {
    public final OperationImpl mOperation = new OperationImpl();

    public abstract void runInternal();

    public Operation getOperation() {
        return this.mOperation;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            runInternal();
            this.mOperation.setState(Operation.SUCCESS);
        } catch (Throwable th) {
            this.mOperation.setState(new Operation.State.FAILURE(th));
        }
    }

    public void cancel(WorkManagerImpl workManagerImpl, String workSpecId) {
        iterativelyCancelWorkAndDependents(workManagerImpl.getWorkDatabase(), workSpecId);
        workManagerImpl.getProcessor().stopAndCancelWork(workSpecId);
        for (Scheduler scheduler : workManagerImpl.getSchedulers()) {
            scheduler.cancel(workSpecId);
        }
    }

    public void reschedulePendingWorkers(WorkManagerImpl workManagerImpl) {
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }

    public final void iterativelyCancelWorkAndDependents(WorkDatabase workDatabase, String workSpecId) {
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        DependencyDao dependencyDao = workDatabase.dependencyDao();
        LinkedList linkedList = new LinkedList();
        linkedList.add(workSpecId);
        while (!linkedList.isEmpty()) {
            String str = (String) linkedList.remove();
            WorkInfo.State state = workSpecDao.getState(str);
            if (state != WorkInfo.State.SUCCEEDED && state != WorkInfo.State.FAILED) {
                workSpecDao.setState(WorkInfo.State.CANCELLED, str);
            }
            linkedList.addAll(dependencyDao.getDependentWorkIds(str));
        }
    }

    public static CancelWorkRunnable forId(final UUID id, final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() { // from class: androidx.work.impl.utils.CancelWorkRunnable.1
            @Override // androidx.work.impl.utils.CancelWorkRunnable
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    cancel(WorkManagerImpl.this, id.toString());
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    reschedulePendingWorkers(WorkManagerImpl.this);
                } catch (Throwable th) {
                    workDatabase.endTransaction();
                    throw th;
                }
            }
        };
    }

    public static CancelWorkRunnable forName(final String name, final WorkManagerImpl workManagerImpl, final boolean allowReschedule) {
        return new CancelWorkRunnable() { // from class: androidx.work.impl.utils.CancelWorkRunnable.3
            @Override // androidx.work.impl.utils.CancelWorkRunnable
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String str : workDatabase.workSpecDao().getUnfinishedWorkWithName(name)) {
                        cancel(WorkManagerImpl.this, str);
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    if (!allowReschedule) {
                        return;
                    }
                    reschedulePendingWorkers(WorkManagerImpl.this);
                } catch (Throwable th) {
                    workDatabase.endTransaction();
                    throw th;
                }
            }
        };
    }
}
