package com.miui.gallery.assistant.library;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import com.miui.gallery.job.workers.TrackedWorker;
import com.miui.gallery.util.StaticContext;
import java.io.File;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: DeleteLibraryWorker.kt */
/* loaded from: classes.dex */
public final class DeleteLibraryWorker extends TrackedWorker {
    public static final Companion Companion = new Companion(null);

    public static final void schedule() {
        Companion.schedule();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeleteLibraryWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(workerParams, "workerParams");
    }

    @Override // com.miui.gallery.job.workers.TrackedWorker
    public ListenableWorker.Result doWork() {
        process();
        ListenableWorker.Result success = ListenableWorker.Result.success();
        Intrinsics.checkNotNullExpressionValue(success, "success()");
        return success;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00b3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0087 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean process() {
        /*
            Method dump skipped, instructions count: 315
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.assistant.library.DeleteLibraryWorker.process():boolean");
    }

    public final boolean isDownloadTempFile(File file) {
        if (file != null) {
            String name = file.getName();
            Intrinsics.checkNotNullExpressionValue(name, "child.name");
            if (StringsKt__StringsJVMKt.endsWith$default(name, ".download", false, 2, null)) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: DeleteLibraryWorker.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final void schedule() {
            WorkManager.getInstance(StaticContext.sGetAndroidContext()).enqueueUniqueWork("com.miui.gallery.job.DeleteAlgoLibrary", ExistingWorkPolicy.KEEP, new OneTimeWorkRequest.Builder(DeleteLibraryWorker.class).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(true).build()).build());
        }
    }
}
