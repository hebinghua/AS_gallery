package ch.qos.logback.core.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class StatusBase implements Status {
    private static final List<Status> EMPTY_LIST = new ArrayList(0);
    public List<Status> childrenList;
    public long date;
    public int level;
    public final String message;
    public final Object origin;
    public Throwable throwable;

    public StatusBase(int i, String str, Object obj) {
        this(i, str, obj, null);
    }

    public StatusBase(int i, String str, Object obj, Throwable th) {
        this.level = i;
        this.message = str;
        this.origin = obj;
        this.throwable = th;
        this.date = System.currentTimeMillis();
    }

    @Override // ch.qos.logback.core.status.Status
    public synchronized void add(Status status) {
        if (status == null) {
            throw new NullPointerException("Null values are not valid Status.");
        }
        if (this.childrenList == null) {
            this.childrenList = new ArrayList();
        }
        this.childrenList.add(status);
    }

    @Override // ch.qos.logback.core.status.Status
    public synchronized boolean hasChildren() {
        boolean z;
        List<Status> list = this.childrenList;
        if (list != null) {
            if (list.size() > 0) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // ch.qos.logback.core.status.Status
    public synchronized Iterator<Status> iterator() {
        List<Status> list = this.childrenList;
        if (list != null) {
            return list.iterator();
        }
        return EMPTY_LIST.iterator();
    }

    @Override // ch.qos.logback.core.status.Status
    public synchronized boolean remove(Status status) {
        List<Status> list = this.childrenList;
        if (list == null) {
            return false;
        }
        return list.remove(status);
    }

    @Override // ch.qos.logback.core.status.Status
    public int getLevel() {
        return this.level;
    }

    @Override // ch.qos.logback.core.status.Status
    public synchronized int getEffectiveLevel() {
        int i;
        i = this.level;
        Iterator<Status> it = iterator();
        while (it.hasNext()) {
            int effectiveLevel = it.next().getEffectiveLevel();
            if (effectiveLevel > i) {
                i = effectiveLevel;
            }
        }
        return i;
    }

    @Override // ch.qos.logback.core.status.Status
    public String getMessage() {
        return this.message;
    }

    @Override // ch.qos.logback.core.status.Status
    public Object getOrigin() {
        return this.origin;
    }

    @Override // ch.qos.logback.core.status.Status
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override // ch.qos.logback.core.status.Status
    public Long getDate() {
        return Long.valueOf(this.date);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int effectiveLevel = getEffectiveLevel();
        if (effectiveLevel == 0) {
            sb.append("INFO");
        } else if (effectiveLevel == 1) {
            sb.append("WARN");
        } else if (effectiveLevel == 2) {
            sb.append("ERROR");
        }
        if (this.origin != null) {
            sb.append(" in ");
            sb.append(this.origin);
            sb.append(" -");
        }
        sb.append(" ");
        sb.append(this.message);
        if (this.throwable != null) {
            sb.append(" ");
            sb.append(this.throwable);
        }
        return sb.toString();
    }

    public int hashCode() {
        int i = (this.level + 31) * 31;
        String str = this.message;
        return i + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StatusBase statusBase = (StatusBase) obj;
        if (this.level != statusBase.level) {
            return false;
        }
        String str = this.message;
        if (str == null) {
            if (statusBase.message != null) {
                return false;
            }
        } else if (!str.equals(statusBase.message)) {
            return false;
        }
        return true;
    }
}
