package okhttp3.internal;

/* loaded from: classes3.dex */
public abstract class NamedRunnable implements Runnable {
    public final String name;

    public abstract void execute();

    public NamedRunnable(String str, Object... objArr) {
        this.name = Util.format(str, objArr);
    }

    @Override // java.lang.Runnable
    public final void run() {
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(this.name);
        try {
            execute();
        } finally {
            Thread.currentThread().setName(name);
        }
    }
}
