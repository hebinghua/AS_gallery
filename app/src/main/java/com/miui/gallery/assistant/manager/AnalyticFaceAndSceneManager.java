package com.miui.gallery.assistant.manager;

import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.AnalyticFaceAndSceneRequest;
import com.miui.gallery.assistant.manager.request.AnalyticSceneRequest;
import com.miui.gallery.assistant.manager.request.param.AnalyticFaceAndSceneParam;
import com.miui.gallery.assistant.manager.request.param.AnalyticSceneParam;
import com.miui.gallery.assistant.manager.result.AnalyticFaceAndSceneResult;
import com.miui.gallery.assistant.manager.result.AnalyticSceneResult;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AnalyticFaceAndSceneManager {
    public static boolean sIsSupportVideo;
    public static final String[] sSupportVideoDevice;

    public AnalyticFaceAndSceneManager() {
    }

    /* loaded from: classes.dex */
    public static final class AnalyticFaceAndSceneHolder {
        public static final AnalyticFaceAndSceneManager INSTANCE = new AnalyticFaceAndSceneManager();
    }

    public static AnalyticFaceAndSceneManager getInstance() {
        return AnalyticFaceAndSceneHolder.INSTANCE;
    }

    static {
        String[] strArr = {"cmi", "umi", "lmi", "lmiin", "lmiinpro", "lmipro", "polaris", "dipper", "equuleus", "perseus", "ursa", "andromeda", "cepheus", "crux", "raphael", "raphaelin", "beryllium", "vangogh", "andromeda", "beryllium", "sagit", "chiron", "chiron", "monet", "monet", "picasso", "picassoin", "phoenix", "phoenix", "davinci", "tucana", "cas", "apollo", "gauguin", "gauguinpro", "star", "cetus", "venus", "surya", "sweet", "haydn", "haydnin", "mojito", "rainbow", "sunny", "aliothin", "alioth", "renoir", "mars", "enuma", "elish", "nabu", "argo", "thyme", "odin", "zeus", "cupid", "ingres", "taoyao", "thor", "loki", "unicorn", "mayfly", "lime", "citrus", "lemon", "pomelo", "diting"};
        sSupportVideoDevice = strArr;
        sIsSupportVideo = false;
        for (String str : strArr) {
            if (str.equalsIgnoreCase(Build.DEVICE)) {
                sIsSupportVideo = true;
            }
        }
    }

    public static boolean isDeviceSupportVideo() {
        return sIsSupportVideo;
    }

    public List<List<MediaScene>> getSceneTagListByPathInBacth(final List<String> list, final boolean z, final boolean z2) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        String pathBatchSelectionStr = getPathBatchSelectionStr(list);
        return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, MediaFeatureItem.PROJECTION, String.format(Locale.US, "(localFile in (%s) OR thumbnailFile in (%s)) AND(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", pathBatchSelectionStr, pathBatchSelectionStr), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<List<MediaScene>>>() { // from class: com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.1
            /* JADX WARN: Removed duplicated region for block: B:17:0x0048  */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.util.List<java.util.List<com.miui.gallery.assistant.model.MediaScene>> mo1808handle(android.database.Cursor r12) {
                /*
                    r11 = this;
                    java.util.ArrayList r0 = new java.util.ArrayList
                    java.util.List r1 = r2
                    int r1 = r1.size()
                    r0.<init>(r1)
                    java.util.HashMap r1 = new java.util.HashMap
                    r1.<init>()
                    if (r12 == 0) goto L3c
                    boolean r2 = r12.moveToFirst()
                    if (r2 == 0) goto L3c
                L18:
                    com.miui.gallery.assistant.model.MediaFeatureItem r2 = new com.miui.gallery.assistant.model.MediaFeatureItem
                    r2.<init>(r12)
                    r3 = 4
                    java.lang.String r3 = r12.getString(r3)
                    boolean r4 = android.text.TextUtils.isEmpty(r3)
                    if (r4 == 0) goto L2d
                    r3 = 3
                    java.lang.String r3 = r12.getString(r3)
                L2d:
                    boolean r4 = android.text.TextUtils.isEmpty(r3)
                    if (r4 != 0) goto L36
                    r1.put(r3, r2)
                L36:
                    boolean r2 = r12.moveToNext()
                    if (r2 != 0) goto L18
                L3c:
                    java.util.List r12 = r2
                    java.util.Iterator r12 = r12.iterator()
                L42:
                    boolean r2 = r12.hasNext()
                    if (r2 == 0) goto L8d
                    java.lang.Object r2 = r12.next()
                    r6 = r2
                    java.lang.String r6 = (java.lang.String) r6
                    boolean r2 = android.text.TextUtils.isEmpty(r6)
                    r3 = 0
                    if (r2 == 0) goto L5a
                    r0.add(r3)
                    goto L42
                L5a:
                    java.io.File r2 = new java.io.File
                    r2.<init>(r6)
                    boolean r4 = r2.exists()
                    if (r4 != 0) goto L69
                    r0.add(r3)
                    goto L42
                L69:
                    java.lang.Object r3 = r1.get(r6)
                    com.miui.gallery.assistant.model.MediaFeatureItem r3 = (com.miui.gallery.assistant.model.MediaFeatureItem) r3
                    if (r3 != 0) goto L89
                    com.miui.gallery.assistant.model.MediaFeatureItem r10 = new com.miui.gallery.assistant.model.MediaFeatureItem
                    r4 = -1
                    com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager r3 = com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.this
                    boolean r3 = com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.access$200(r3, r6)
                    if (r3 == 0) goto L7f
                    r3 = 2
                    goto L80
                L7f:
                    r3 = 1
                L80:
                    r7 = r3
                    long r8 = r2.length()
                    r3 = r10
                    r3.<init>(r4, r6, r7, r8)
                L89:
                    r0.add(r3)
                    goto L42
                L8d:
                    com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager r12 = com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.this
                    boolean r1 = r3
                    boolean r2 = r4
                    java.util.List r12 = r12.getSceneTagListInBatch(r0, r1, r2)
                    return r12
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.AnonymousClass1.mo1808handle(android.database.Cursor):java.util.List");
            }
        });
    }

    public String getPathBatchSelectionStr(List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                if (z) {
                    z = false;
                } else {
                    sb.append(",");
                }
                sb.append("'");
                sb.append(str);
                sb.append("'");
            }
        }
        return sb.toString();
    }

    public List<MediaScene> getSceneTagListByPath(final String str, final boolean z, final boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        final File file = new File(str);
        return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, MediaFeatureItem.PROJECTION, String.format(Locale.US, "(localFile = '%s' OR thumbnailFile = '%s') AND(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", str, str), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<MediaScene>>() { // from class: com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<MediaScene> mo1808handle(Cursor cursor) {
                MediaFeatureItem mediaFeatureItem;
                if (cursor != null && cursor.moveToFirst()) {
                    mediaFeatureItem = new MediaFeatureItem(cursor);
                } else {
                    String str2 = str;
                    mediaFeatureItem = new MediaFeatureItem(-1L, str2, AnalyticFaceAndSceneManager.this.isVideoFile(str2) ? 2 : 1, file.length());
                }
                return AnalyticFaceAndSceneManager.this.getSceneTagList(mediaFeatureItem, z, z2);
            }
        });
    }

    public List<List<MediaScene>> getSceneTagListInBatch(List<MediaFeatureItem> list, boolean z, boolean z2) {
        if (BaseMiscUtil.isValid(list)) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            boolean z3 = true;
            boolean z4 = true;
            for (MediaFeatureItem mediaFeatureItem : list) {
                if (mediaFeatureItem != null) {
                    if (mediaFeatureItem.getMediaId() > 0) {
                        if (z3) {
                            z3 = false;
                        } else {
                            sb2.append(",");
                        }
                        sb2.append(mediaFeatureItem.getMediaId());
                    } else if (!TextUtils.isEmpty(mediaFeatureItem.getImagePath())) {
                        if (z4) {
                            z4 = false;
                        } else {
                            sb.append(",");
                        }
                        sb.append("'");
                        sb.append(mediaFeatureItem.getImagePath());
                        sb.append("'");
                    }
                }
            }
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            String format = String.format("(%s IN (%s) OR %s IN (%s)) AND %s = %d", "mediaId", sb2.toString(), "mediaPath", sb.toString(), "version", 1);
            if (!z2) {
                format = String.format("(%s) AND %s=%d", format, "isQuickResult", 0);
            }
            List<MediaScene> query = galleryEntityManager.query(MediaScene.class, format, null);
            ArrayList arrayList = new ArrayList(list.size());
            for (MediaFeatureItem mediaFeatureItem2 : list) {
                ArrayList arrayList2 = new ArrayList();
                for (MediaScene mediaScene : query) {
                    if (mediaFeatureItem2.getMediaId() >= 0 && mediaFeatureItem2.getMediaId() == mediaScene.getMediaId()) {
                        arrayList2.add(mediaScene);
                    } else if (mediaFeatureItem2.getMediaId() < 0 && TextUtils.equals(mediaFeatureItem2.getImagePath(), mediaScene.getPath()) && mediaFeatureItem2.getFileSize() == mediaScene.getFileSize()) {
                        arrayList2.add(mediaScene);
                    }
                }
                if (BaseMiscUtil.isValid(arrayList2)) {
                    arrayList.add(arrayList2);
                } else if (z) {
                    arrayList.add(null);
                } else {
                    AnalyticSceneResult analyticSceneTagSync = analyticSceneTagSync(mediaFeatureItem2, z2);
                    if (analyticSceneTagSync != null && analyticSceneTagSync.getResultCode() == 0) {
                        arrayList.add(analyticSceneTagSync.getResult());
                    } else {
                        arrayList.add(null);
                    }
                }
            }
            return arrayList;
        }
        return null;
    }

    public List<MediaScene> getSceneTagList(MediaFeatureItem mediaFeatureItem, boolean z, boolean z2) {
        String format;
        AnalyticSceneResult analyticSceneTagSync;
        if (mediaFeatureItem == null) {
            return null;
        }
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        if (mediaFeatureItem.getMediaId() > 0) {
            format = String.format("%s=%d AND %s=%d", "mediaId", Long.valueOf(mediaFeatureItem.getMediaId()), "version", 1);
        } else {
            format = String.format("%s=%d AND %s='%s' AND %s=%d", "fileSize", Long.valueOf(mediaFeatureItem.getFileSize()), "mediaPath", mediaFeatureItem.getImagePath(), "version", 1);
        }
        if (!z2) {
            format = String.format("(%s) AND %s=%d", format, "isQuickResult", 0);
        }
        List<MediaScene> query = galleryEntityManager.query(MediaScene.class, format, null);
        if (BaseMiscUtil.isValid(query)) {
            return query;
        }
        if (!z && (analyticSceneTagSync = analyticSceneTagSync(mediaFeatureItem, z2)) != null && analyticSceneTagSync.getResultCode() == 0) {
            return analyticSceneTagSync.getResult();
        }
        return null;
    }

    public AnalyticSceneResult analyticSceneTagSync(MediaFeatureItem mediaFeatureItem, boolean z) {
        if (mediaFeatureItem == null) {
            return new AnalyticSceneResult(3);
        }
        if (CloudControlStrategyHelper.getMediaFeatureCalculationDisable()) {
            return new AnalyticSceneResult(7);
        }
        if ((mediaFeatureItem.getSpecialTypeFlags() & 262144) != 0 || (mediaFeatureItem.getSpecialTypeFlags() & 4194304) != 0) {
            return new AnalyticSceneResult(3);
        }
        if (mediaFeatureItem.getDuration() > 480) {
            return new AnalyticSceneResult(3);
        }
        return new AnalyticSceneRequest(AlgorithmRequest.Priority.PRIORITY_NORMAL, new AnalyticSceneParam(mediaFeatureItem, true, false, z)).executeSync();
    }

    public AnalyticFaceAndSceneResult analyticFaceAndSceneSync(List<MediaFeatureItem> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return new AnalyticFaceAndSceneResult(3);
        }
        if (CloudControlStrategyHelper.getMediaFeatureCalculationDisable()) {
            return new AnalyticFaceAndSceneResult(7);
        }
        return new AnalyticFaceAndSceneRequest(AlgorithmRequest.Priority.PRIORITY_NORMAL, new AnalyticFaceAndSceneParam(list, true, false, false)).executeSync();
    }

    public final boolean isVideoFile(String str) {
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        if ("*/*".equals(mimeType)) {
            mimeType = BaseFileMimeUtil.getMimeTypeByParseFile(str);
        }
        return BaseFileMimeUtil.isVideoFromMimeType(mimeType);
    }
}
