package com.miui.gallery.scanner.provider.resolver;

import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.scanner.core.ScanContracts$Mode;
import com.miui.gallery.sdk.download.DownloadType;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: IScanMethodResolver.kt */
/* loaded from: classes2.dex */
public final class IScanMethodResolver$currentMode$2 extends Lambda implements Function0<ScanContracts$Mode> {
    public static final IScanMethodResolver$currentMode$2 INSTANCE = new IScanMethodResolver$currentMode$2();

    /* compiled from: IScanMethodResolver.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[DownloadType.values().length];
            iArr[DownloadType.THUMBNAIL.ordinal()] = 1;
            iArr[DownloadType.ORIGIN.ordinal()] = 2;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public IScanMethodResolver$currentMode$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final ScanContracts$Mode mo1738invoke() {
        String str;
        if (!BatchDownloadManager.getInstance().checkCondition()) {
            return ScanContracts$Mode.PERFORMANCE;
        }
        SupportSQLiteDatabase readableDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
        DownloadType downloadType = GalleryPreferences.Sync.getDownloadType();
        int i = downloadType == null ? -1 : WhenMappings.$EnumSwitchMapping$0[downloadType.ordinal()];
        if (i == 1) {
            str = "thumbnailFile IS NULL AND (localFlag = 0 AND serverStatus = 'custom')";
        } else if (i != 2) {
            return ScanContracts$Mode.PERFORMANCE;
        } else {
            str = "localFile IS NULL AND (localFlag = 0 AND serverStatus = 'custom')";
        }
        Cursor query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").limit("1").selection(str, null).create());
        try {
            if (query.getCount() > 0) {
                ScanContracts$Mode scanContracts$Mode = ScanContracts$Mode.POWER_SAVE;
                CloseableKt.closeFinally(query, null);
                return scanContracts$Mode;
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(query, null);
            query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").limit("1000").create());
            try {
                if (query.getCount() >= 1000) {
                    CloseableKt.closeFinally(query, null);
                    return ScanContracts$Mode.PERFORMANCE;
                }
                ScanContracts$Mode scanContracts$Mode2 = ScanContracts$Mode.POWER_SAVE;
                CloseableKt.closeFinally(query, null);
                return scanContracts$Mode2;
            } finally {
            }
        } finally {
            try {
                throw th;
            } finally {
            }
        }
    }
}
