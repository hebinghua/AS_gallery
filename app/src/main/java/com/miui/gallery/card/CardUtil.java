package com.miui.gallery.card;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.card.Card;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miai.api.StatusCode;
import com.xiaomi.milab.videosdk.message.MsgType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class CardUtil {
    public static List<MediaFeatureItem> getCoverMediaItemsByServerIds(final List<Long> list) {
        if (BaseMiscUtil.isValid(list)) {
            Uri build = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build();
            SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), build, MediaFeatureItem.MEDIA_PROJECTION, "(localGroupId!=-1000) AND " + String.format("%s IN ('%s')", "serverId", TextUtils.join("','", list)), (String[]) null, "alias_create_time DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.card.CardUtil.1
                /* JADX WARN: Removed duplicated region for block: B:26:0x0039 A[EDGE_INSN: B:26:0x0039->B:12:0x0039 ?: BREAK  , SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:6:0x0019 A[Catch: all -> 0x004e, TryCatch #0 {all -> 0x004e, blocks: (B:4:0x0012, B:6:0x0019, B:8:0x0020, B:10:0x0034), top: B:24:0x0012 }] */
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> mo1808handle(android.database.Cursor r9) {
                    /*
                        r8 = this;
                        if (r9 == 0) goto L53
                        int r0 = r9.getCount()
                        java.util.List r1 = r1
                        int r1 = r1.size()
                        int r0 = java.lang.Math.max(r0, r1)
                        com.miui.gallery.assistant.model.MediaFeatureItem[] r1 = new com.miui.gallery.assistant.model.MediaFeatureItem[r0]
                    L12:
                        boolean r2 = r9.moveToNext()     // Catch: java.lang.Throwable -> L4e
                        r3 = 0
                        if (r2 == 0) goto L39
                        com.miui.gallery.assistant.model.MediaFeatureItem r2 = new com.miui.gallery.assistant.model.MediaFeatureItem     // Catch: java.lang.Throwable -> L4e
                        r2.<init>(r9)     // Catch: java.lang.Throwable -> L4e
                    L1e:
                        if (r3 >= r0) goto L12
                        java.util.List r4 = r1     // Catch: java.lang.Throwable -> L4e
                        java.lang.Object r4 = r4.get(r3)     // Catch: java.lang.Throwable -> L4e
                        java.lang.Long r4 = (java.lang.Long) r4     // Catch: java.lang.Throwable -> L4e
                        long r4 = r4.longValue()     // Catch: java.lang.Throwable -> L4e
                        long r6 = r2.getServerId()     // Catch: java.lang.Throwable -> L4e
                        int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                        if (r4 != 0) goto L36
                        r1[r3] = r2     // Catch: java.lang.Throwable -> L4e
                    L36:
                        int r3 = r3 + 1
                        goto L1e
                    L39:
                        com.miui.gallery.util.BaseMiscUtil.closeSilently(r9)
                        java.util.ArrayList r9 = new java.util.ArrayList
                        r9.<init>()
                    L41:
                        if (r3 >= r0) goto L4d
                        r2 = r1[r3]
                        if (r2 == 0) goto L4a
                        r9.add(r2)
                    L4a:
                        int r3 = r3 + 1
                        goto L41
                    L4d:
                        return r9
                    L4e:
                        r0 = move-exception
                        com.miui.gallery.util.BaseMiscUtil.closeSilently(r9)
                        throw r0
                    L53:
                        r9 = 0
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.card.CardUtil.AnonymousClass1.mo1808handle(android.database.Cursor):java.util.List");
                }
            });
            return null;
        }
        return null;
    }

    public static List<String> getSha1sByServerIds(List<Long> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build(), CardManager.CONVERT_PROJECTION, String.format("%s IN (%s)", "serverId", TextUtils.join(",", list)), (String[]) null, String.format("%s DESC", "alias_create_time"), new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.card.CardUtil.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<String> mo1808handle(Cursor cursor) {
                ArrayList arrayList = new ArrayList(cursor != null ? cursor.getCount() : 0);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        arrayList.add(cursor.getString(1));
                    }
                }
                return arrayList;
            }
        });
    }

    public static boolean isDuplicated(Card card, CardInfo cardInfo) {
        if (card == null || cardInfo == null) {
            return false;
        }
        Card.CardExtraInfo cardExtraInfo = (Card.CardExtraInfo) GsonUtils.fromJson(cardInfo.getExtraInfo(), (Class<Object>) Card.CardExtraInfo.class);
        if (cardExtraInfo != null && card.getUniqueKey() != null && card.getUniqueKey().equals(cardExtraInfo.uniqueKey)) {
            return true;
        }
        long validStartTime = card.getValidStartTime();
        long validEndTime = card.getValidEndTime();
        long validStartDate = cardInfo.getValidStartDate();
        long validEndDate = cardInfo.getValidEndDate();
        if (card.getScenarioId() != cardInfo.getScenarioId() || validStartTime <= 0 || validEndTime <= 0 || validStartDate <= 0 || validEndDate <= 0) {
            return false;
        }
        return (validStartTime >= validStartDate && validStartTime <= validEndDate) || (validEndTime >= validStartDate && validEndTime <= validEndDate) || (validStartDate >= validStartTime && validStartDate <= validEndTime);
    }

    public static boolean isDuplicated(Card card, Card card2) {
        if (card == null || card2 == null) {
            return false;
        }
        if (card2.getUniqueKey() != null && card.getUniqueKey() != null && card.getUniqueKey().equals(card2.getUniqueKey())) {
            return true;
        }
        long validStartTime = card.getValidStartTime();
        long validEndTime = card.getValidEndTime();
        long validStartTime2 = card2.getValidStartTime();
        long validEndTime2 = card2.getValidEndTime();
        if (card.getScenarioId() != card2.getScenarioId() || validStartTime <= 0 || validEndTime <= 0 || validStartTime2 <= 0 || validEndTime2 <= 0) {
            return false;
        }
        return (validStartTime >= validStartTime2 && validStartTime <= validEndTime2) || (validEndTime >= validStartTime2 && validEndTime <= validEndTime2) || (validStartTime2 >= validStartTime && validStartTime2 <= validEndTime);
    }

    public static void bindMediaFeatures(List<MediaFeatureItem> list) {
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList(list.size());
            for (MediaFeatureItem mediaFeatureItem : list) {
                arrayList.add(Long.valueOf(mediaFeatureItem.getId()));
            }
            List query = GalleryEntityManager.getInstance().query(MediaFeature.class, String.format("%s IN (%s)", "mediaId", TextUtils.join(",", arrayList)), null, "createTime DESC", null);
            if (!BaseMiscUtil.isValid(query)) {
                return;
            }
            for (MediaFeatureItem mediaFeatureItem2 : list) {
                Iterator it = query.iterator();
                while (true) {
                    if (it.hasNext()) {
                        MediaFeature mediaFeature = (MediaFeature) it.next();
                        if (mediaFeature.getMediaId() == mediaFeatureItem2.getId()) {
                            mediaFeatureItem2.setMediaFeature(mediaFeature);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void bindMediaScene(List<MediaFeatureItem> list) {
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList(list.size());
            for (MediaFeatureItem mediaFeatureItem : list) {
                arrayList.add(Long.valueOf(mediaFeatureItem.getId()));
            }
            List query = GalleryEntityManager.getInstance().query(MediaScene.class, String.format("%s IN (%s)", "mediaId", TextUtils.join(",", arrayList)), null, null, null);
            if (!BaseMiscUtil.isValid(query)) {
                return;
            }
            for (MediaFeatureItem mediaFeatureItem2 : list) {
                ArrayList arrayList2 = new ArrayList();
                Iterator it = query.iterator();
                while (it.hasNext()) {
                    MediaScene mediaScene = (MediaScene) it.next();
                    if (mediaScene.getMediaId() == mediaFeatureItem2.getId()) {
                        arrayList2.add(mediaScene);
                        it.remove();
                    }
                }
                if (BaseMiscUtil.isValid(arrayList2)) {
                    mediaFeatureItem2.setMediaSceneResult(arrayList2);
                }
            }
        }
    }

    public static long getLastCardCreateTime() {
        List query = GalleryEntityManager.getInstance().query(Card.class, "ignored = 0", null, "createTime desc", String.format(Locale.US, "%s,%s", 0, 1));
        if (BaseMiscUtil.isValid(query)) {
            return ((Card) query.get(0)).getCreateTime();
        }
        return -1L;
    }

    public static List<MediaFeatureItem> getCardCovers(List<MediaFeatureItem> list) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            Collections.sort(list);
            if (BaseMiscUtil.isValid(list)) {
                for (int i = 0; i < Math.min(list.size(), 5); i++) {
                    arrayList.add(list.get(i));
                }
            }
        }
        return arrayList;
    }

    public static List<MediaFeatureItem> getUnProcessedMediaFeatureItems(List<MediaFeatureItem> list) {
        List<MediaFeatureItem> list2 = null;
        if (BaseMiscUtil.isValid(list)) {
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            List query = galleryEntityManager.query(MediaFeature.class, String.format(MediaFeature.ALL_FEATURE_PROCESSED_SELECTION + " AND mediaId IN ('%s') ", TextUtils.join("','", getImageIdsFromMediaItems(list))), null);
            list2 = new ArrayList<>(list);
            DefaultLogger.d("CardUtil", "unProcessedMediaFeatureItems,size()=%d", Integer.valueOf(list2.size()));
            DefaultLogger.d("CardUtil", "processedList,size()=%d", Integer.valueOf(query.size()));
            if (BaseMiscUtil.isValid(query)) {
                for (MediaFeatureItem mediaFeatureItem : list) {
                    Iterator it = query.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        } else if (TextUtils.equals(((MediaFeature) it.next()).getSha1(), mediaFeatureItem.getSha1())) {
                            list2.remove(mediaFeatureItem);
                            break;
                        }
                    }
                }
            }
            for (MediaFeatureItem mediaFeatureItem2 : list) {
                if (mediaFeatureItem2.isVideo()) {
                    list2.remove(mediaFeatureItem2);
                }
            }
            DefaultLogger.d("CardUtil", "unProcessedMediaFeatureItems,size()=%d", Integer.valueOf(list2.size()));
        }
        return list2;
    }

    public static List<MediaFeatureItem> getUnProcessedMediaSceneItems(List<MediaFeatureItem> list) {
        List<MediaFeatureItem> list2 = null;
        if (BaseMiscUtil.isValid(list)) {
            List query = GalleryEntityManager.getInstance().query(MediaScene.class, String.format("version = 1 AND mediaId IN ('%s') ", TextUtils.join("','", getImageIdsFromMediaItems(list))), null);
            list2 = new ArrayList<>(list);
            if (BaseMiscUtil.isValid(query)) {
                for (MediaFeatureItem mediaFeatureItem : list) {
                    Iterator it = query.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        } else if (((MediaScene) it.next()).getMediaId() == mediaFeatureItem.getMediaId()) {
                            list2.remove(mediaFeatureItem);
                            break;
                        }
                    }
                }
            }
        }
        return list2;
    }

    public static List<String> getSha1sFromImages(List<MediaFeatureItem> list) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            for (MediaFeatureItem mediaFeatureItem : list) {
                arrayList.add(mediaFeatureItem.getMediaSha1());
            }
        }
        return arrayList;
    }

    public static Uri getAlbumUri(String str) {
        return GalleryContract.Common.URI_CARD_ACTION.buildUpon().appendQueryParameter("actionType", str).build();
    }

    public static List<Long> getImageIdsFromMediaItems(List<MediaFeatureItem> list) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            for (MediaFeatureItem mediaFeatureItem : list) {
                arrayList.add(Long.valueOf(mediaFeatureItem.getId()));
            }
        }
        return arrayList;
    }

    public static boolean isCoverMediaPathEmpty(List<MediaFeatureItem> list) {
        DocumentFile documentFile;
        if (!BaseMiscUtil.isValid(list)) {
            return false;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CardUtil", "isCoverMediaPathEmpty");
        for (int i = 0; i < list.size(); i++) {
            MediaFeatureItem mediaFeatureItem = list.get(i);
            DownloadType downloadType = getDownloadType();
            if (downloadType != DownloadType.THUMBNAIL || (documentFile = StorageSolutionProvider.get().getDocumentFile(mediaFeatureItem.getThumbnailPath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)) == null || !documentFile.exists()) {
                if (downloadType == DownloadType.MICRO) {
                    StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                    String microThumbnailPath = mediaFeatureItem.getMicroThumbnailPath();
                    IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
                    DocumentFile documentFile2 = storageStrategyManager.getDocumentFile(microThumbnailPath, permission, appendInvokerTag);
                    if (documentFile2 != null) {
                        if (documentFile2.exists()) {
                            continue;
                        }
                    }
                    DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(mediaFeatureItem.getThumbnailPath(), permission, appendInvokerTag);
                    if (documentFile3 != null && documentFile3.exists()) {
                    }
                }
                DefaultLogger.d("CardUtil", "Image " + mediaFeatureItem.getId() + "has no local image of " + downloadType);
                return true;
            }
        }
        return false;
    }

    public static DownloadType getDownloadType() {
        return DownloadType.THUMBNAIL;
    }

    public static void downloadCoverThumb(List<MediaFeatureItem> list) {
        if (BaseMiscUtil.isValid(list)) {
            for (int i = 0; i < list.size(); i++) {
                MediaFeatureItem mediaFeatureItem = list.get(i);
                DownloadType downloadType = getDownloadType();
                DownloadType downloadType2 = DownloadType.THUMBNAIL;
                if ((downloadType == downloadType2 && TextUtils.isEmpty(mediaFeatureItem.getThumbnailPath())) || (downloadType == DownloadType.MICRO && TextUtils.isEmpty(mediaFeatureItem.getMicroThumbnailPath()))) {
                    String str = BaseImageTask.getFilePath(mediaFeatureItem, downloadType, true).get();
                    if (!TextUtils.isEmpty(str)) {
                        if (downloadType == downloadType2) {
                            mediaFeatureItem.setThumbnailPath(str);
                        } else {
                            mediaFeatureItem.setMicroThumbnailPath(str);
                        }
                    }
                }
            }
        }
    }

    public static int getMovieTemplateFromCard(Card card) {
        if (card == null) {
            return 0;
        }
        int scenarioId = card.getScenarioId();
        if (scenarioId <= 0 && card.getUniqueKey() != null) {
            scenarioId = card.getUniqueKey().getScenarioId();
        }
        int timeType = card.getTimeType();
        if (timeType != 1) {
            if (timeType == 2) {
                return 4;
            }
            if (timeType != 3) {
                if (timeType == 4 || timeType == 5) {
                    return 7;
                }
                if (timeType == 19) {
                    return 1;
                }
                if (timeType == 111 || timeType == 115) {
                    return 3;
                }
                if (scenarioId != 102) {
                    if (scenarioId != 111) {
                        if (scenarioId != 118 && scenarioId != 201) {
                            if (scenarioId != 10199 && scenarioId != 105 && scenarioId != 106) {
                                if (scenarioId != 114) {
                                    if (scenarioId != 115) {
                                        if (scenarioId != 120) {
                                            if (scenarioId != 121) {
                                                if (scenarioId != 10099) {
                                                    if (scenarioId != 10100) {
                                                        switch (scenarioId) {
                                                            case 401:
                                                                return 8;
                                                            case MsgType.MsgEvent.PLAYER_EVENT_EXTRACT_FINISHED /* 402 */:
                                                                return 9;
                                                            case StatusCode.FORBIDDEN /* 403 */:
                                                                break;
                                                            default:
                                                                switch (scenarioId) {
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                            return 4;
                                        }
                                        return 2;
                                    }
                                }
                            }
                            return 7;
                        }
                        return 1;
                    }
                    return 3;
                } else if (card.getUniqueKey() != null) {
                    try {
                        int parseInt = Integer.parseInt(card.getUniqueKey().getPrimaryKey());
                        if (parseInt == 200) {
                            return 6;
                        }
                        if (parseInt == 205) {
                            return 5;
                        }
                    } catch (NumberFormatException e) {
                        DefaultLogger.e("CardUtil", e);
                    }
                }
                return 0;
            }
        }
        return 2;
    }
}
