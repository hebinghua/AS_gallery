package com.miui.gallery.util.baby;

import android.util.SparseArray;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class BabyAlbumRecommendationFinder {
    public volatile Future<RecommendationDatas> mFuture;
    public String mPeopleId;
    public volatile RecommendationDatas mRecommandationDatas;
    public volatile RecommendationFoundListener mRecommandationFoundListener;

    /* loaded from: classes2.dex */
    public interface RecommendationFoundListener {
        void onRecommendationFound(RecommendationDatas recommendationDatas);
    }

    /* loaded from: classes2.dex */
    public static final class RecommendationDatas {
        public ArrayList<Long> ids;
        public String peopleLocalId;
        public String peopleServerId;
        public int totalFaceCountInFaceAlbum;

        public boolean hasNewRecommendation() {
            ArrayList<Long> arrayList = this.ids;
            return arrayList != null && arrayList.size() > 0;
        }

        public int getRecommendationSize() {
            ArrayList<Long> arrayList = this.ids;
            if (arrayList != null) {
                return arrayList.size();
            }
            return 0;
        }
    }

    public BabyAlbumRecommendationFinder(String str) {
        this.mPeopleId = str;
    }

    public void setRecommendationFoundListener(RecommendationFoundListener recommendationFoundListener) {
        this.mRecommandationFoundListener = recommendationFoundListener;
    }

    public void findRecommendation(final SparseArray<Boolean> sparseArray, final String str) {
        if (this.mFuture != null) {
            return;
        }
        this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<RecommendationDatas>() { // from class: com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Code restructure failed: missing block: B:18:0x008e, code lost:
                if (r0 != null) goto L29;
             */
            /* JADX WARN: Code restructure failed: missing block: B:24:0x009e, code lost:
                if (r0 != null) goto L29;
             */
            /* JADX WARN: Code restructure failed: missing block: B:25:0x00a0, code lost:
                r11.totalFaceCountInFaceAlbum = r0.getCount();
                r0.close();
             */
            /* JADX WARN: Code restructure failed: missing block: B:26:0x00a9, code lost:
                return r11;
             */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.RecommendationDatas mo1807run(com.miui.gallery.concurrent.ThreadPool.JobContext r11) {
                /*
                    r10 = this;
                    com.miui.gallery.util.baby.BabyAlbumRecommendationFinder$RecommendationDatas r11 = new com.miui.gallery.util.baby.BabyAlbumRecommendationFinder$RecommendationDatas
                    r11.<init>()
                    com.miui.gallery.util.baby.BabyAlbumRecommendationFinder r0 = com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.this
                    java.lang.String r0 = com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.access$000(r0)
                    r11.peopleServerId = r0
                    com.miui.gallery.util.baby.BabyAlbumRecommendationFinder r0 = com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.this
                    java.lang.String r0 = com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.access$000(r0)
                    long r0 = com.miui.gallery.provider.FaceManager.getPeopleLocalIdByServerId(r0)
                    java.lang.String r0 = java.lang.String.valueOf(r0)
                    r11.peopleLocalId = r0
                    r0 = 0
                    android.content.Context r1 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    android.content.ContentResolver r2 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    android.net.Uri r3 = com.miui.gallery.provider.GalleryContract.PeopleFace.ONE_PERSON_URI     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.String r1 = "_id"
                    java.lang.String r4 = "title"
                    java.lang.String r5 = "mixedDateTime"
                    java.lang.String[] r4 = new java.lang.String[]{r1, r4, r5}     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r5 = 0
                    r1 = 2
                    java.lang.String[] r6 = new java.lang.String[r1]     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.String r7 = r11.peopleServerId     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r8 = 0
                    r6[r8] = r7     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.String r7 = r11.peopleLocalId     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r9 = 1
                    r6[r9] = r7     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r7 = 0
                    android.database.Cursor r0 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                L45:
                    if (r0 == 0) goto L8e
                    boolean r2 = r0.moveToNext()     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    if (r2 == 0) goto L8e
                    java.lang.String r2 = r0.getString(r9)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    if (r2 != 0) goto L45
                    android.util.SparseArray r2 = r2     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.String r3 = r0.getString(r9)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    int r3 = r3.hashCode()     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.Object r2 = r2.get(r3)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    if (r2 != 0) goto L45
                    long r2 = r0.getLong(r1)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.String r4 = r3     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    long r4 = com.miui.gallery.preference.GalleryPreferences.Baby.getLastClickBabyPhotosRecommandationTime(r4)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                    if (r2 <= 0) goto L45
                    java.util.ArrayList<java.lang.Long> r2 = r11.ids     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    if (r2 != 0) goto L80
                    java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r2.<init>()     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r11.ids = r2     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                L80:
                    java.util.ArrayList<java.lang.Long> r2 = r11.ids     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    long r3 = r0.getLong(r8)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    r2.add(r3)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L9e
                    goto L45
                L8e:
                    if (r0 == 0) goto La9
                    goto La0
                L91:
                    r1 = move-exception
                    if (r0 == 0) goto L9d
                    int r2 = r0.getCount()
                    r11.totalFaceCountInFaceAlbum = r2
                    r0.close()
                L9d:
                    throw r1
                L9e:
                    if (r0 == 0) goto La9
                La0:
                    int r1 = r0.getCount()
                    r11.totalFaceCountInFaceAlbum = r1
                    r0.close()
                La9:
                    return r11
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.AnonymousClass1.mo1807run(com.miui.gallery.concurrent.ThreadPool$JobContext):com.miui.gallery.util.baby.BabyAlbumRecommendationFinder$RecommendationDatas");
            }
        }, new FutureListener<RecommendationDatas>() { // from class: com.miui.gallery.util.baby.BabyAlbumRecommendationFinder.2
            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<RecommendationDatas> future) {
                if (!future.isCancelled()) {
                    BabyAlbumRecommendationFinder.this.mRecommandationDatas = future.get();
                    RecommendationFoundListener recommendationFoundListener = BabyAlbumRecommendationFinder.this.mRecommandationFoundListener;
                    if (recommendationFoundListener != null) {
                        recommendationFoundListener.onRecommendationFound(BabyAlbumRecommendationFinder.this.mRecommandationDatas);
                    }
                }
                if (future == BabyAlbumRecommendationFinder.this.mFuture) {
                    BabyAlbumRecommendationFinder.this.mFuture = null;
                }
            }
        });
    }
}
