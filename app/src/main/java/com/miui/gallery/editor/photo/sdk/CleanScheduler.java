package com.miui.gallery.editor.photo.sdk;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CleanScheduler.kt */
/* loaded from: classes2.dex */
public final class CleanScheduler {
    public static final CleanScheduler INSTANCE = new CleanScheduler();

    public static final void schedule(Context context, String tag, String... path) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(path, "path");
        if (context == null) {
            throw new IllegalArgumentException("Required value was null.".toString());
        }
        int i = 0;
        if (path.length == 0) {
            DefaultLogger.w("CleanScheduler", "path should not be empty");
            return;
        }
        String arrays = Arrays.toString(path);
        Intrinsics.checkNotNullExpressionValue(arrays, "toString(this)");
        DefaultLogger.d("CleanScheduler", Intrinsics.stringPlus("Received file: ", arrays));
        WorkManager workManager = WorkManager.getInstance(context);
        List<WorkInfo> list = workManager.getWorkInfos(WorkQuery.Builder.fromUniqueWorkNames(CollectionsKt__CollectionsJVMKt.listOf("com.miui.gallery.job.CleanFile")).addTags(CollectionsKt__CollectionsJVMKt.listOf(tag)).addStates(CollectionsKt__CollectionsKt.listOf((Object[]) new WorkInfo.State[]{WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING, WorkInfo.State.BLOCKED})).build()).get();
        if (list == null || list.isEmpty()) {
            OneTimeWorkRequest.Builder constraints = new OneTimeWorkRequest.Builder(CleanWorker.class).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(true).build());
            Object[] copyOf = Arrays.copyOf(path, path.length);
            Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, size)");
            Pair[] pairArr = {TuplesKt.to("extra_file_paths", copyOf)};
            Data.Builder builder = new Data.Builder();
            while (i < 1) {
                Pair pair = pairArr[i];
                i++;
                builder.put((String) pair.getFirst(), pair.getSecond());
            }
            Data build = builder.build();
            Intrinsics.checkNotNullExpressionValue(build, "dataBuilder.build()");
            OneTimeWorkRequest.Builder addTag = constraints.setInputData(build).addTag(tag);
            Intrinsics.checkNotNullExpressionValue(addTag, "OneTimeWorkRequestBuilde…             .addTag(tag)");
            OneTimeWorkRequest.Builder inputMerger = addTag.setInputMerger(OverwritingInputMerger.class);
            Intrinsics.checkNotNullExpressionValue(inputMerger, "setInputMerger(inputMerger.java)");
            OneTimeWorkRequest build2 = inputMerger.build();
            Intrinsics.checkNotNullExpressionValue(build2, "OneTimeWorkRequestBuilde…                 .build()");
            workManager.enqueueUniqueWork("com.miui.gallery.job.CleanFile", ExistingWorkPolicy.APPEND_OR_REPLACE, build2);
            return;
        }
        DefaultLogger.d("CleanScheduler", "Same worker with tag [" + tag + "] already scheduled");
    }
}
