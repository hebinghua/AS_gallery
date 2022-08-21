package com.miui.gallery.discovery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.discovery.BaseMessageOperator;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class RecentDiscoveryMessageOperator extends BaseMessageOperator<RecentSaveParams> {
    public static final Gson sGson = new Gson();

    @Override // com.miui.gallery.discovery.BaseMessageOperator
    public int getMessageType() {
        return 1;
    }

    @Override // com.miui.gallery.discovery.BaseMessageOperator
    public void doWrapMessage(DiscoveryMessage discoveryMessage, String str) {
        RecentMessageDetail fromJson = RecentMessageDetail.fromJson(str);
        if (fromJson != null) {
            checkFileExits(fromJson);
            int unviewMediaCount = fromJson.getUnviewMediaCount();
            if (unviewMediaCount > 0) {
                discoveryMessage.setMessage(GalleryApp.sGetAndroidContext().getResources().getQuantityString(R.plurals.quickly_discovery_message_format, unviewMediaCount, Integer.valueOf(unviewMediaCount)));
            } else {
                discoveryMessage.setMessage(null);
            }
        }
        discoveryMessage.setMessageDetail(fromJson);
    }

    @Override // com.miui.gallery.discovery.BaseMessageOperator
    public boolean doMarkMessageAsRead(Context context, DiscoveryMessage discoveryMessage) {
        discoveryMessage.setConsumed(true);
        if (discoveryMessage.getMessageDetail() instanceof RecentMessageDetail) {
            ((RecentMessageDetail) discoveryMessage.getMessageDetail()).setThumbUrls(null);
            ((RecentMessageDetail) discoveryMessage.getMessageDetail()).setUnviewMediaCount(0);
        } else {
            DefaultLogger.e("RecentDiscoveryMessageOperator", "messageDetail should be instance of RecentMessageDetail");
        }
        return doUpdateMessage(context, discoveryMessage);
    }

    @Override // com.miui.gallery.discovery.BaseMessageOperator
    public boolean doSaveMessage(Context context, RecentSaveParams recentSaveParams) {
        String[] strArr;
        Cursor cursor;
        String[] strArr2;
        int mediaCount = recentSaveParams.getMediaCount();
        List<String> thumbUrls = recentSaveParams.getThumbUrls() != null ? recentSaveParams.getThumbUrls() : new ArrayList<>();
        ContentValues contentValues = new ContentValues();
        long currentTimeMillis = System.currentTimeMillis();
        Cursor queryMessageByType = queryMessageByType(context);
        try {
            try {
                if (queryMessageByType != null) {
                    try {
                        if (queryMessageByType.moveToFirst()) {
                            long j = queryMessageByType.getLong(queryMessageByType.getColumnIndex(j.c));
                            RecentMessageDetail fromJson = RecentMessageDetail.fromJson(queryMessageByType.getString(queryMessageByType.getColumnIndex("extraData")));
                            if (fromJson != null) {
                                mediaCount += fromJson.getUnviewMediaCount();
                                strArr = fromJson.getThumbUrls();
                            } else {
                                fromJson = new RecentMessageDetail();
                                strArr = null;
                            }
                            if (strArr != null) {
                                int i = mediaCount;
                                int length = strArr.length;
                                cursor = queryMessageByType;
                                int i2 = 0;
                                while (i2 < length) {
                                    int i3 = length;
                                    String str = strArr[i2];
                                    if (!thumbUrls.contains(str)) {
                                        strArr2 = strArr;
                                        if (thumbUrls.size() < 3) {
                                            thumbUrls.add(str);
                                        }
                                    } else {
                                        strArr2 = strArr;
                                        i--;
                                    }
                                    i2++;
                                    length = i3;
                                    strArr = strArr2;
                                }
                                mediaCount = i;
                            } else {
                                cursor = queryMessageByType;
                            }
                            fromJson.setUnviewMediaCount(mediaCount);
                            int min = Math.min(thumbUrls.size(), 3);
                            String[] strArr3 = new String[min];
                            for (int i4 = 0; i4 < min; i4++) {
                                strArr3[i4] = thumbUrls.get(i4);
                            }
                            fromJson.setThumbUrls(strArr3);
                            contentValues.put(j.c, Long.valueOf(j));
                            contentValues.put("extraData", fromJson.toJson());
                            contentValues.put("isConsumed", (Integer) 0);
                            contentValues.put("updateTime", Long.valueOf(currentTimeMillis));
                            contentValues.put("actionUri", GalleryContract.RecentAlbum.VIEW_PAGE_URI.toString());
                            boolean run = new BaseMessageOperator.UpdateTask(context, null, contentValues).run();
                            BaseMiscUtil.closeSilently(cursor);
                            return run;
                        }
                    } catch (Exception e) {
                        e = e;
                        DefaultLogger.e("RecentDiscoveryMessageOperator", "Something wrong happened when save message: %s.", e.getMessage());
                        e.printStackTrace();
                        BaseMiscUtil.closeSilently(queryMessageByType);
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        BaseMiscUtil.closeSilently(queryMessageByType);
                        throw th;
                    }
                }
                RecentMessageDetail recentMessageDetail = new RecentMessageDetail();
                recentMessageDetail.setUnviewMediaCount(mediaCount);
                String[] strArr4 = thumbUrls.size() >= 3 ? new String[3] : new String[thumbUrls.size()];
                for (int i5 = 0; i5 < strArr4.length; i5++) {
                    strArr4[i5] = thumbUrls.get(i5);
                }
                recentMessageDetail.setThumbUrls(strArr4);
                contentValues.put("extraData", recentMessageDetail.toJson());
                contentValues.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(getMessageType()));
                contentValues.put("receiveTime", Long.valueOf(currentTimeMillis));
                contentValues.put("updateTime", Long.valueOf(currentTimeMillis));
                contentValues.put("isConsumed", (Integer) 0);
                contentValues.put("actionUri", GalleryContract.RecentAlbum.VIEW_PAGE_URI.toString());
                boolean run2 = new BaseMessageOperator.InsertTask(context, contentValues).run();
                BaseMiscUtil.closeSilently(queryMessageByType);
                return run2;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public final void checkFileExits(RecentMessageDetail recentMessageDetail) {
        if (recentMessageDetail.getThumbUrls() != null) {
            int unviewMediaCount = recentMessageDetail.getUnviewMediaCount();
            ArrayList newArrayList = Lists.newArrayList(recentMessageDetail.getThumbUrls());
            Iterator it = newArrayList.iterator();
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("RecentDiscoveryMessageOperator", "checkFileExits");
            while (it.hasNext()) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile((String) it.next(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile == null || !documentFile.exists()) {
                    it.remove();
                    unviewMediaCount--;
                }
            }
            if (newArrayList.size() > 0) {
                recentMessageDetail.setThumbUrls((String[]) newArrayList.toArray(new String[newArrayList.size()]));
            } else {
                recentMessageDetail.setThumbUrls(null);
            }
            recentMessageDetail.setUnviewMediaCount(unviewMediaCount);
        }
    }

    /* loaded from: classes.dex */
    public static class RecentMessageDetail extends DiscoveryMessage.BaseMessageDetail {
        public String[] thumbUrls;
        public int unviewMediaCount;

        public int getUnviewMediaCount() {
            return this.unviewMediaCount;
        }

        public void setUnviewMediaCount(int i) {
            this.unviewMediaCount = i;
        }

        public String[] getThumbUrls() {
            return this.thumbUrls;
        }

        public void setThumbUrls(String[] strArr) {
            this.thumbUrls = strArr;
        }

        public static RecentMessageDetail fromJson(String str) {
            if (!TextUtils.isEmpty(str)) {
                try {
                    return (RecentMessageDetail) RecentDiscoveryMessageOperator.sGson.fromJson(str, (Class<Object>) RecentMessageDetail.class);
                } catch (Exception e) {
                    DefaultLogger.d("RecentDiscoveryMessageOperator", "Unable to parse extraData json to object: %s", str);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override // com.miui.gallery.model.DiscoveryMessage.BaseMessageDetail
        public String toJson() {
            return RecentDiscoveryMessageOperator.sGson.toJson(this);
        }
    }

    /* loaded from: classes.dex */
    public static class RecentSaveParams {
        public int mMediaCount;
        public List<String> mThumbUrls;

        public RecentSaveParams(int i, List<String> list) {
            this.mMediaCount = i;
            this.mThumbUrls = list;
        }

        public int getMediaCount() {
            return this.mMediaCount;
        }

        public List<String> getThumbUrls() {
            return this.mThumbUrls;
        }
    }
}
