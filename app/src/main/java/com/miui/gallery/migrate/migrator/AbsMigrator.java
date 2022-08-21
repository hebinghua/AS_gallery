package com.miui.gallery.migrate.migrator;

import android.content.Context;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class AbsMigrator implements IMigrator {
    public final Context mContext;
    public final long mVersion;

    public boolean check() {
        return true;
    }

    public void postMigrate(boolean z, Throwable th) {
    }

    public void preMigrate() {
    }

    public AbsMigrator(Context context, long j) {
        this.mContext = context;
        this.mVersion = j;
    }

    public final boolean migrate() {
        if (!check()) {
            DefaultLogger.d("AbsMigrator", "%s check failed, don not need migrate.", toString());
            return true;
        }
        preMigrate();
        try {
            boolean doMigrate = doMigrate();
            try {
                postMigrate(doMigrate, null);
            } catch (Exception e) {
                DefaultLogger.e("AbsMigrator", e);
            }
            return doMigrate;
        } catch (Throwable th) {
            try {
                postMigrate(true, th);
                return false;
            } catch (Exception e2) {
                DefaultLogger.e("AbsMigrator", e2);
                return false;
            }
        }
    }

    public long getVersion() {
        return this.mVersion;
    }
}
