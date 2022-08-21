package com.cdv.io;

import android.os.SystemClock;
import android.util.Log;

/* loaded from: classes.dex */
public class NvSyncEvent {
    private boolean m_manualReset;
    private volatile boolean m_signaled = false;

    public NvSyncEvent(boolean z) {
        this.m_manualReset = false;
        this.m_manualReset = z;
    }

    public void setEvent() {
        synchronized (this) {
            if (!this.m_signaled) {
                this.m_signaled = true;
                notifyAll();
            }
        }
    }

    public void resetEvent() {
        synchronized (this) {
            this.m_signaled = false;
        }
    }

    public boolean waitEvent(long j) {
        boolean z;
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i == 0) {
            synchronized (this) {
                z = this.m_signaled;
            }
            return z;
        } else if (i > 0) {
            return waitEventWithTimeout(j);
        } else {
            try {
                synchronized (this) {
                    while (!this.m_signaled) {
                        wait();
                    }
                    if (!this.m_manualReset) {
                        this.m_signaled = false;
                    }
                }
                return true;
            } catch (Exception e) {
                Log.e("SyncEvent", "" + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean waitEventWithTimeout(long j) {
        try {
            synchronized (this) {
                if (this.m_signaled) {
                    if (!this.m_manualReset) {
                        this.m_signaled = false;
                    }
                    return true;
                }
                long elapsedRealtime = SystemClock.elapsedRealtime();
                while (!this.m_signaled) {
                    wait(j);
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    long j2 = elapsedRealtime2 - elapsedRealtime;
                    if (j2 >= j) {
                        return false;
                    }
                    j -= j2;
                    elapsedRealtime = elapsedRealtime2;
                }
                if (!this.m_manualReset) {
                    this.m_signaled = false;
                }
                return true;
            }
        } catch (Exception e) {
            Log.e("SyncEvent", "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
