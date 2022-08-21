package com.miui.gallery.migrate.migrator;

import android.content.Context;
import com.google.common.collect.Lists;

/* loaded from: classes2.dex */
public class ThirdPartyMigrator extends AbsMigrator {
    public final String mNewPath;
    public final String mOldPath;

    @Override // com.miui.gallery.migrate.migrator.IMigrator
    public boolean doMigrate() {
        return true;
    }

    public ThirdPartyMigrator(Context context, long j, String str, String str2) {
        super(context, j);
        this.mOldPath = str;
        this.mNewPath = str2;
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public boolean check() {
        return super.check();
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public void preMigrate() {
        MigrateUtils.requestPauseScan(Lists.newArrayList(this.mOldPath, this.mNewPath));
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public void postMigrate(boolean z, Throwable th) {
        MigrateUtils.requestResumeScan(Lists.newArrayList(this.mOldPath, this.mNewPath));
    }

    public String toString() {
        return "ThirdPartyMigrator[" + this.mOldPath + "->" + this.mNewPath + "]";
    }
}
