package com.miui.gallery.db.sqlite3;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GallerySQLiteOpenHelperFactory.kt */
/* loaded from: classes.dex */
public final class GallerySQLiteOpenHelperFactory implements SupportSQLiteOpenHelper.Factory {
    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Factory
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        Context context = configuration.context;
        Intrinsics.checkNotNullExpressionValue(context, "configuration.context");
        String str = configuration.name;
        SupportSQLiteOpenHelper.Callback callback = configuration.callback;
        Intrinsics.checkNotNullExpressionValue(callback, "configuration.callback");
        return new GallerySQLiteOpenHelper(context, str, callback, configuration.useNoBackupDirectory, configuration.allowDataLossOnRecovery);
    }
}
