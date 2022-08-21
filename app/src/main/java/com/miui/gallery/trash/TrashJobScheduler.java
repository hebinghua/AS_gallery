package com.miui.gallery.trash;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TrashJobScheduler.kt */
/* loaded from: classes2.dex */
public final class TrashJobScheduler {
    public static final TrashJobScheduler INSTANCE = new TrashJobScheduler();
    public static String REQUEST_INFO_SERVER_IDS = "serverIds";
    public static String OPERATION = "operation";

    public final String getREQUEST_INFO_SERVER_IDS() {
        return REQUEST_INFO_SERVER_IDS;
    }

    public final String getOPERATION() {
        return OPERATION;
    }

    public static final void schedule(Context context, int i, Set<String> serverIds) {
        Intrinsics.checkNotNullParameter(serverIds, "serverIds");
        if (context == null) {
            return;
        }
        if (i != 2 && i != 1) {
            DefaultLogger.e("TrashJobScheduler", Intrinsics.stringPlus("Unrecognized op: ", Integer.valueOf(i)));
            return;
        }
        String str = i == 2 ? "com.miui.gallery.job.TrashRecover" : "com.miui.gallery.job.TrashPurge";
        for (List list : CollectionsKt___CollectionsKt.chunked(serverIds, 1000)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Schedule ");
            sb.append(i == 2 ? "recover" : "purge");
            sb.append(" for ");
            sb.append(list.size());
            sb.append(" items");
            DefaultLogger.d("TrashJobScheduler", sb.toString());
            TrashJobScheduler trashJobScheduler = INSTANCE;
            Object[] array = list.toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            trashJobScheduler.scheduleOnce(context, i, (String[]) array, str);
        }
    }

    public final void scheduleOnce(Context context, int i, String[] strArr, String str) {
        OneTimeWorkRequest.Builder constraints = new OneTimeWorkRequest.Builder(TrashRecoverOrPurgeWorker.class).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build());
        int i2 = 0;
        Pair[] pairArr = {TuplesKt.to(OPERATION, Integer.valueOf(i)), TuplesKt.to(REQUEST_INFO_SERVER_IDS, strArr)};
        Data.Builder builder = new Data.Builder();
        while (i2 < 2) {
            Pair pair = pairArr[i2];
            i2++;
            builder.put((String) pair.getFirst(), pair.getSecond());
        }
        Data build = builder.build();
        Intrinsics.checkNotNullExpressionValue(build, "dataBuilder.build()");
        OneTimeWorkRequest.Builder inputData = constraints.setInputData(build);
        Intrinsics.checkNotNullExpressionValue(inputData, "OneTimeWorkRequestBuilde…          )\n            )");
        OneTimeWorkRequest.Builder inputMerger = inputData.setInputMerger(OverwritingInputMerger.class);
        Intrinsics.checkNotNullExpressionValue(inputMerger, "setInputMerger(inputMerger.java)");
        OneTimeWorkRequest build2 = inputMerger.build();
        Intrinsics.checkNotNullExpressionValue(build2, "OneTimeWorkRequestBuilde…ass)\n            .build()");
        WorkManager.getInstance(context).enqueueUniqueWork(str, ExistingWorkPolicy.APPEND_OR_REPLACE, build2);
    }
}
