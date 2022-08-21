package com.miui.gallery.migrate.migrator;

import android.content.Context;

/* loaded from: classes2.dex */
public class EmptyMigrator extends AbsMigrator {
    @Override // com.miui.gallery.migrate.migrator.IMigrator
    public boolean doMigrate() {
        return false;
    }

    public EmptyMigrator(Context context, long j) {
        super(context, j);
    }
}
