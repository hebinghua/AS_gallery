package ch.qos.logback.core;

import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.spi.LogbackLock;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BasicStatusManager implements StatusManager {
    public static final int MAX_HEADER_COUNT = 150;
    public static final int TAIL_SIZE = 150;
    public int count = 0;
    public final List<Status> statusList = new ArrayList();
    public final CyclicBuffer<Status> tailBuffer = new CyclicBuffer<>(150);
    public final LogbackLock statusListLock = new LogbackLock();
    public int level = 0;
    public final List<StatusListener> statusListenerList = new ArrayList();
    public final LogbackLock statusListenerListLock = new LogbackLock();

    @Override // ch.qos.logback.core.status.StatusManager
    public void add(Status status) {
        fireStatusAddEvent(status);
        this.count++;
        if (status.getLevel() > this.level) {
            this.level = status.getLevel();
        }
        synchronized (this.statusListLock) {
            if (this.statusList.size() < 150) {
                this.statusList.add(status);
            } else {
                this.tailBuffer.add(status);
            }
        }
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public List<Status> getCopyOfStatusList() {
        ArrayList arrayList;
        synchronized (this.statusListLock) {
            arrayList = new ArrayList(this.statusList);
            arrayList.addAll(this.tailBuffer.asList());
        }
        return arrayList;
    }

    private void fireStatusAddEvent(Status status) {
        synchronized (this.statusListenerListLock) {
            for (StatusListener statusListener : this.statusListenerList) {
                statusListener.addStatusEvent(status);
            }
        }
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public void clear() {
        synchronized (this.statusListLock) {
            this.count = 0;
            this.statusList.clear();
            this.tailBuffer.clear();
        }
    }

    public int getLevel() {
        return this.level;
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public int getCount() {
        return this.count;
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public boolean add(StatusListener statusListener) {
        synchronized (this.statusListenerListLock) {
            if (!(statusListener instanceof OnConsoleStatusListener) || !checkForPresence(this.statusListenerList, statusListener.getClass())) {
                this.statusListenerList.add(statusListener);
                return true;
            }
            return false;
        }
    }

    private boolean checkForPresence(List<StatusListener> list, Class<?> cls) {
        for (StatusListener statusListener : list) {
            if (statusListener.getClass() == cls) {
                return true;
            }
        }
        return false;
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public boolean addUniquely(StatusListener statusListener, Object obj) {
        for (StatusListener statusListener2 : getCopyOfStatusListenerList()) {
            if (statusListener2.getClass().isInstance(statusListener)) {
                add(new WarnStatus("A previous listener of type [" + statusListener2.getClass() + "] has been already registered. Skipping double registration.", obj));
                return false;
            }
        }
        add(statusListener);
        return true;
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public void remove(StatusListener statusListener) {
        synchronized (this.statusListenerListLock) {
            this.statusListenerList.remove(statusListener);
        }
    }

    @Override // ch.qos.logback.core.status.StatusManager
    public List<StatusListener> getCopyOfStatusListenerList() {
        ArrayList arrayList;
        synchronized (this.statusListenerListLock) {
            arrayList = new ArrayList(this.statusListenerList);
        }
        return arrayList;
    }
}
