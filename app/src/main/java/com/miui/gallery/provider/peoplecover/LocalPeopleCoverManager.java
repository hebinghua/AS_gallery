package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexCrop;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class LocalPeopleCoverManager {
    public static final int[] SCORES = {0, 1, 11, 111, 1111, 11111, 111111, 1111111};
    public static final int[] SCORE_STAGE = {0, 1, 2, 3, 4, 5, 6, 7};
    public static LocalPeopleCoverManager sInstance;
    public ArrayList<BaseStrategy> mCoverStrategies = new ArrayList<>();
    public final ArrayList<String> mCoverIds = new ArrayList<>();
    public int mMaxCoverScore = 0;
    public volatile boolean mLoadPeopleCoverDone = false;

    public LocalPeopleCoverManager() {
        registerCoverStrategy(new CoverImageSizeStrategy(nexCrop.ABSTRACT_DIMENSION), nexCrop.ABSTRACT_DIMENSION);
        registerCoverStrategy(new CoverScaleStrategy(10000), 10000);
        registerCoverStrategy(new CoverCountStrategy(1000), 1000);
        registerCoverStrategy(new CoverCenterStrategy(100), 100);
        registerCoverStrategy(new CoverEyeXStrategy(10), 10);
        registerCoverStrategy(new CoverEyeYStrategy(1), 1);
        registerCoverStrategy(new CoverColorStrategy(1000000), 1000000);
    }

    public static synchronized LocalPeopleCoverManager getInstance() {
        LocalPeopleCoverManager localPeopleCoverManager;
        synchronized (LocalPeopleCoverManager.class) {
            if (sInstance == null) {
                sInstance = new LocalPeopleCoverManager();
            }
            localPeopleCoverManager = sInstance;
        }
        return localPeopleCoverManager;
    }

    public final void registerCoverStrategy(BaseStrategy baseStrategy, int i) {
        this.mCoverStrategies.add(baseStrategy);
        this.mMaxCoverScore += i;
    }

    public void startChooseCoverForAllPeople() {
        SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.PeopleFace.PEOPLE_TAG_URI, new String[]{"serverId", "eTag"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Object>() { // from class: com.miui.gallery.provider.peoplecover.LocalPeopleCoverManager.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Object mo1808handle(Cursor cursor) {
                int doChoosePeopleCover;
                if (cursor != null && cursor.getCount() != 0) {
                    long currentTimeMillis = System.currentTimeMillis();
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        String string2 = cursor.getString(1);
                        PeopleCover peopleCover = (PeopleCover) GalleryEntityManager.getInstance().find(PeopleCover.class, string);
                        if (peopleCover == null || !TextUtils.equals(peopleCover.getServerTag(), string2)) {
                            doChoosePeopleCover = LocalPeopleCoverManager.this.doChoosePeopleCover(string, string2, peopleCover);
                        } else {
                            doChoosePeopleCover = peopleCover.getCoverScore();
                        }
                        LocalPeopleCoverManager.this.generatorScoreStage(doChoosePeopleCover);
                    }
                    DefaultLogger.d("LocalPeopleCoverManager", "finish choose cover for all people, cost time: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    LocalPeopleCoverManager.this.finishChoosePeopleCover();
                }
                return null;
            }
        });
    }

    public final int generatorScoreStage(int i) {
        int length = SCORES.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (i <= SCORES[i2]) {
                return i2;
            }
        }
        return 0;
    }

    public final int doChoosePeopleCover(final String str, final String str2, final PeopleCover peopleCover) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        return ((Integer) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.PeopleFace.ONE_PERSON_URI, BaseStrategy.PROJECTION, (String) null, new String[]{str, "-1"}, "dateTaken DESC ", new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.provider.peoplecover.LocalPeopleCoverManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                int i;
                int i2 = 0;
                if (cursor == null || cursor.getCount() == 0) {
                    return 0;
                }
                if (cursor.moveToFirst()) {
                    i = 0;
                    int i3 = 0;
                    while (true) {
                        Iterator it = LocalPeopleCoverManager.this.mCoverStrategies.iterator();
                        int i4 = 0;
                        while (it.hasNext()) {
                            BaseStrategy baseStrategy = (BaseStrategy) it.next();
                            i4 += baseStrategy.getWeight() * (baseStrategy.isValid(cursor) ? 1 : 0);
                        }
                        if (i4 > i3) {
                            i = cursor.getPosition();
                            if (i4 == LocalPeopleCoverManager.this.mMaxCoverScore) {
                                i2 = i4;
                                break;
                            }
                            i3 = i4;
                        }
                        if (!cursor.moveToNext()) {
                            i2 = i3;
                            break;
                        }
                    }
                } else {
                    i = 0;
                }
                if (i2 != 0) {
                    DefaultLogger.d("LocalPeopleCoverManager", "success choose cover for person: %s, position: %d, cost : %d", str, Integer.valueOf(i), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    cursor.moveToPosition(i);
                    LocalPeopleCoverManager.this.setPeopleCover(str, cursor.getString(10), str2, peopleCover, i2);
                } else {
                    DefaultLogger.d("LocalPeopleCoverManager", "choose no cover for person: %s, cost : %d", str, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    LocalPeopleCoverManager.this.setPeopleCover(str, null, str2, peopleCover, i2);
                }
                return Integer.valueOf(i2);
            }
        })).intValue();
    }

    public synchronized void setPeopleCover(String str, String str2, String str3, PeopleCover peopleCover, int i) {
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        if (peopleCover == null) {
            galleryEntityManager.insert(new PeopleCover(str, str2, str3, i));
        } else {
            peopleCover.setCoverId(str2);
            peopleCover.setServerTag(str3);
            peopleCover.setCoverScore(i);
            galleryEntityManager.update(peopleCover);
        }
    }

    public final void finishChoosePeopleCover() {
        Iterator<BaseStrategy> it = this.mCoverStrategies.iterator();
        while (it.hasNext()) {
            it.next().onFinish();
        }
    }

    public ArrayList getCoverIds() {
        ensureLoadPeopleCoverDone();
        return new ArrayList(this.mCoverIds);
    }

    public final void ensureLoadPeopleCoverDone() {
        if (this.mLoadPeopleCoverDone) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mCoverIds) {
            if (!this.mLoadPeopleCoverDone) {
                loadPeopleCover();
            }
        }
        DefaultLogger.d("LocalPeopleCoverManager", "wait for load cost : %d ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public final synchronized void loadPeopleCover() {
        long currentTimeMillis = System.currentTimeMillis();
        List<PeopleCover> query = GalleryEntityManager.getInstance().query(PeopleCover.class, null, null, null, null);
        if (BaseMiscUtil.isValid(query)) {
            this.mCoverIds.clear();
            for (PeopleCover peopleCover : query) {
                String coverId = peopleCover.getCoverId();
                if (!TextUtils.isEmpty(coverId)) {
                    this.mCoverIds.add(coverId);
                }
            }
            DefaultLogger.d("LocalPeopleCoverManager", "finish load people cover, cost time: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }
        this.mLoadPeopleCoverDone = true;
    }

    public synchronized void onAccountDelete() {
        ArrayList<String> arrayList = this.mCoverIds;
        if (arrayList != null) {
            arrayList.clear();
        }
        this.mLoadPeopleCoverDone = false;
        GalleryEntityManager.getInstance().deleteAll(PeopleCover.class);
        DefaultLogger.d("LocalPeopleCoverManager", "on account delete");
    }
}
