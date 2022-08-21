package com.miui.gallery.trash;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.miui.gallery.job.workers.TrackedWorker;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TrashRecoverOrPurgeWorker.kt */
/* loaded from: classes2.dex */
public final class TrashRecoverOrPurgeWorker extends TrackedWorker {
    public static final Companion Companion = new Companion(null);

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TrashRecoverOrPurgeWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(workerParams, "workerParams");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0031  */
    @Override // com.miui.gallery.job.workers.TrackedWorker
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public androidx.work.ListenableWorker.Result doWork() {
        /*
            Method dump skipped, instructions count: 317
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.trash.TrashRecoverOrPurgeWorker.doWork():androidx.work.ListenableWorker$Result");
    }

    /* compiled from: TrashRecoverOrPurgeWorker.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
