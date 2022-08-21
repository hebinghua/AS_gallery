package com.miui.gallery.job;

import android.content.Context;
import androidx.work.WorkManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.annotation.Annotation;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: GalleryJobScheduler.kt */
/* loaded from: classes2.dex */
public final class GalleryJobScheduler {
    public static final GalleryJobScheduler INSTANCE = new GalleryJobScheduler();

    public static /* synthetic */ boolean $r8$lambda$Lfq5ECHoyHKO_4R0lmmDgXUk2VY(Context context) {
        return m997migrateFromJobScheduler$lambda10(context);
    }

    public final void scheduleAll(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        migrateFromJobScheduler(context);
        WorkManager workManager = WorkManager.getInstance(context);
        for (Class cls : PeriodicWorkerProviders.getInstance().getAll().values()) {
            if (!IPeriodicWorkerProvider.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException((cls + " must inherit from " + ((Object) Reflection.getOrCreateKotlinClass(IPeriodicWorkerProvider.class).getSimpleName())).toString());
            }
            boolean z = true;
            if (!(!cls.isLocalClass() && !cls.isAnonymousClass())) {
                throw new IllegalArgumentException((cls + " must not be local class or anonymous class").toString());
            } else if (!cls.isAnnotationPresent(PeriodicWorkerProvider.class)) {
                throw new IllegalArgumentException((cls + " must be annotated with " + ((Object) Reflection.getOrCreateKotlinClass(PeriodicWorkerProvider.class).getSimpleName())).toString());
            } else {
                Annotation annotation = cls.getAnnotation(PeriodicWorkerProvider.class);
                Objects.requireNonNull(annotation, "null cannot be cast to non-null type com.miui.gallery.job.PeriodicWorkerProvider");
                PeriodicWorkerProvider periodicWorkerProvider = (PeriodicWorkerProvider) annotation;
                DefaultLogger.d("GalleryJobScheduler", "Schedule worker [" + ((Object) cls.getSimpleName()) + ']');
                Object newInstance = cls.newInstance();
                Objects.requireNonNull(newInstance, "null cannot be cast to non-null type com.miui.gallery.job.IPeriodicWorkerProvider");
                IPeriodicWorkerProvider iPeriodicWorkerProvider = (IPeriodicWorkerProvider) newInstance;
                if (periodicWorkerProvider.unique()) {
                    if (periodicWorkerProvider.uniqueName().length() <= 0) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalArgumentException((cls + " is unique, unique name must not be empty").toString());
                    }
                    workManager.enqueueUniquePeriodicWork(periodicWorkerProvider.uniqueName(), periodicWorkerProvider.existWorkPolicy(), iPeriodicWorkerProvider.getWorkRequest());
                } else {
                    workManager.enqueue(iPeriodicWorkerProvider.getWorkRequest());
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:62:0x006c, code lost:
        if ((r2.length == 0) != false) goto L25;
     */
    /* JADX WARN: Removed duplicated region for block: B:52:0x003f  */
    /* renamed from: migrateFromJobScheduler$lambda-10 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final boolean m997migrateFromJobScheduler$lambda10(android.content.Context r7) {
        /*
            java.lang.String r0 = "$context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.Class<android.app.job.JobScheduler> r0 = android.app.job.JobScheduler.class
            java.lang.Object r0 = androidx.core.content.ContextCompat.getSystemService(r7, r0)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            android.app.job.JobScheduler r0 = (android.app.job.JobScheduler) r0
            com.miui.gallery.job.JobSchedulerConst r1 = com.miui.gallery.job.JobSchedulerConst.INSTANCE
            int r2 = r1.getTRASH_REQUEST_JOB_PURGE()
            android.app.job.JobInfo r2 = r0.getPendingJob(r2)
            java.lang.String r3 = "GalleryJobScheduler"
            r4 = 0
            r5 = 1
            if (r2 != 0) goto L21
            goto L4b
        L21:
            android.os.PersistableBundle r2 = r2.getExtras()
            com.miui.gallery.trash.TrashJobScheduler r6 = com.miui.gallery.trash.TrashJobScheduler.INSTANCE
            java.lang.String r6 = r6.getREQUEST_INFO_SERVER_IDS()
            java.lang.String[] r2 = r2.getStringArray(r6)
            if (r2 == 0) goto L3c
            int r6 = r2.length
            if (r6 != 0) goto L36
            r6 = r5
            goto L37
        L36:
            r6 = r4
        L37:
            if (r6 == 0) goto L3a
            goto L3c
        L3a:
            r6 = r4
            goto L3d
        L3c:
            r6 = r5
        L3d:
            if (r6 != 0) goto L4b
            java.lang.String r6 = "Migrate trash purge task"
            com.miui.gallery.util.logger.DefaultLogger.i(r3, r6)
            java.util.Set r2 = kotlin.collections.ArraysKt___ArraysKt.toSet(r2)
            com.miui.gallery.trash.TrashJobScheduler.schedule(r7, r5, r2)
        L4b:
            int r2 = r1.getTRASH_REQUEST_JOB_RECOVERY()
            android.app.job.JobInfo r2 = r0.getPendingJob(r2)
            if (r2 != 0) goto L56
            goto L7e
        L56:
            android.os.PersistableBundle r2 = r2.getExtras()
            com.miui.gallery.trash.TrashJobScheduler r6 = com.miui.gallery.trash.TrashJobScheduler.INSTANCE
            java.lang.String r6 = r6.getREQUEST_INFO_SERVER_IDS()
            java.lang.String[] r2 = r2.getStringArray(r6)
            if (r2 == 0) goto L6e
            int r6 = r2.length
            if (r6 != 0) goto L6b
            r6 = r5
            goto L6c
        L6b:
            r6 = r4
        L6c:
            if (r6 == 0) goto L6f
        L6e:
            r4 = r5
        L6f:
            if (r4 != 0) goto L7e
            java.lang.String r4 = "Migrate trash recover task"
            com.miui.gallery.util.logger.DefaultLogger.i(r3, r4)
            r4 = 2
            java.util.Set r2 = kotlin.collections.ArraysKt___ArraysKt.toSet(r2)
            com.miui.gallery.trash.TrashJobScheduler.schedule(r7, r4, r2)
        L7e:
            java.util.List r7 = r1.getDEPRECATED_JOB_IDS()
            java.util.Iterator r7 = r7.iterator()
        L86:
            boolean r1 = r7.hasNext()
            if (r1 == 0) goto La7
            java.lang.Object r1 = r7.next()
            java.lang.Number r1 = (java.lang.Number) r1
            int r1 = r1.intValue()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            java.lang.String r4 = "Cancel deprecated job: "
            java.lang.String r2 = kotlin.jvm.internal.Intrinsics.stringPlus(r4, r2)
            com.miui.gallery.util.logger.DefaultLogger.i(r3, r2)
            r0.cancel(r1)
            goto L86
        La7:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.job.GalleryJobScheduler.m997migrateFromJobScheduler$lambda10(android.content.Context):boolean");
    }

    public final void migrateFromJobScheduler(final Context context) {
        GalleryPreferences.runOnce(GalleryPreferences.PrefKeys.JOB_SCHEDULER_CLEANUP, new GalleryPreferences.OneshotAction() { // from class: com.miui.gallery.job.GalleryJobScheduler$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.preference.GalleryPreferences.OneshotAction
            public final boolean exec() {
                return GalleryJobScheduler.$r8$lambda$Lfq5ECHoyHKO_4R0lmmDgXUk2VY(context);
            }
        });
    }
}
