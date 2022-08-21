package com.miui.gallery.util.baby;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class BabyFaceFinder implements FutureListener<Boolean> {
    public static final Long TIME_INTERVAL_FOR_CHECK_CANDIDATE_PEOPLE = Long.valueOf((long) CoreConstants.MILLIS_IN_ONE_WEEK);
    public Boolean mFoundBabyAlbums;
    public Future mFuture;
    public BabyAlbumsFoundListener mListener;

    /* loaded from: classes2.dex */
    public interface BabyAlbumsFoundListener {
        void onBabyAlbumsFound(Boolean bool);
    }

    public final boolean accept(String str, int i) {
        return PeopleContactInfo.isBabyRelation(i) && !TextUtils.isEmpty(str);
    }

    public synchronized void setBabyAlbumsFoundListener(BabyAlbumsFoundListener babyAlbumsFoundListener) {
        this.mListener = babyAlbumsFoundListener;
    }

    public synchronized void startFindFace(String str) {
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
        }
        if (System.currentTimeMillis() - GalleryPreferences.Baby.getLastClickPeopleRecommandationTime(str) < TIME_INTERVAL_FOR_CHECK_CANDIDATE_PEOPLE.longValue()) {
            return;
        }
        this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Boolean>() { // from class: com.miui.gallery.util.baby.BabyFaceFinder.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Code restructure failed: missing block: B:12:0x0032, code lost:
                if (r8 != null) goto L19;
             */
            /* JADX WARN: Code restructure failed: missing block: B:18:0x003c, code lost:
                if (r8 != null) goto L19;
             */
            /* JADX WARN: Code restructure failed: missing block: B:19:0x003e, code lost:
                r8.close();
             */
            /* JADX WARN: Code restructure failed: missing block: B:21:0x0043, code lost:
                return java.lang.Boolean.FALSE;
             */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.lang.Boolean mo1807run(com.miui.gallery.concurrent.ThreadPool.JobContext r8) {
                /*
                    r7 = this;
                    r8 = 0
                    android.content.Context r0 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    android.content.ContentResolver r1 = r0.getContentResolver()     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.PeopleFace.PERSONS_URI     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    java.lang.String[] r3 = com.miui.gallery.util.face.PeopleCursorHelper.PROJECTION     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    r4 = 0
                    r5 = 0
                    r6 = 0
                    android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                L14:
                    if (r8 == 0) goto L32
                    boolean r0 = r8.moveToNext()     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    if (r0 == 0) goto L32
                    com.miui.gallery.util.baby.BabyFaceFinder r0 = com.miui.gallery.util.baby.BabyFaceFinder.this     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    java.lang.String r1 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleName(r8)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    int r2 = com.miui.gallery.util.face.PeopleCursorHelper.getRelationType(r8)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    boolean r0 = com.miui.gallery.util.baby.BabyFaceFinder.access$000(r0, r1, r2)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    if (r0 == 0) goto L14
                    java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L3c
                    r8.close()
                    return r0
                L32:
                    if (r8 == 0) goto L41
                    goto L3e
                L35:
                    r0 = move-exception
                    if (r8 == 0) goto L3b
                    r8.close()
                L3b:
                    throw r0
                L3c:
                    if (r8 == 0) goto L41
                L3e:
                    r8.close()
                L41:
                    java.lang.Boolean r8 = java.lang.Boolean.FALSE
                    return r8
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.baby.BabyFaceFinder.AnonymousClass1.mo1807run(com.miui.gallery.concurrent.ThreadPool$JobContext):java.lang.Boolean");
            }
        }, this);
    }

    @Override // com.miui.gallery.concurrent.FutureListener
    public synchronized void onFutureDone(Future<Boolean> future) {
        if (!future.isCancelled()) {
            Boolean bool = future.get();
            this.mFoundBabyAlbums = bool;
            BabyAlbumsFoundListener babyAlbumsFoundListener = this.mListener;
            if (babyAlbumsFoundListener != null) {
                babyAlbumsFoundListener.onBabyAlbumsFound(bool);
            }
        }
        if (future == this.mFuture) {
            this.mFuture = null;
        }
    }
}
