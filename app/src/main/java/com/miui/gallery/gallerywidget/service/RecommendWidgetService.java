package com.miui.gallery.gallerywidget.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.R;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;
import com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper;
import com.miui.gallery.gallerywidget.db.RecommendWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.RecommendWidgetDBManager;
import com.miui.gallery.gallerywidget.ui.RecommendDispatchActivity;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.service.IntentServiceBase;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RecommendWidgetService extends IntentServiceBase {
    public int mReadPicCount;
    public int mRequestWidgetDataCount;
    public long mStoryChangeCardId;
    public String mStoryChangeType;
    public final String[] PIC_COLUMN_LIST = {j.c, "localFile", "thumbnailFile", "sha1", "exifImageWidth", "exifImageLength", "exifOrientation"};
    public final int PIC_COLUMN_INDEX_CLOUD_ID = 0;
    public final int PIC_COLUMN_INDEX_LOCAL_FILE = 1;
    public final int PIC_COLUMN_INDEX_THUMBNAIL_FILE = 2;
    public final int PIC_COLUMN_INDEX_SHA_1 = 3;
    public final int PIC_COLUMN_INDEX_EXIF_IMAGE_WIDTH = 4;
    public final int PIC_COLUMN_INDEX_EXIF_IMAGE_LENGTH = 5;
    public final int PIC_COLUMN_INDEX_EXIF_ORIENTATION = 6;
    public final int REQUEST_WIDGET_DATA_MAX_COUNT = 3;
    public IWidgetProviderConfig.WidgetSize mWidgetSize = IWidgetProviderConfig.WidgetSize.SIZE_2_2;

    @Override // com.miui.gallery.service.IntentServiceBase
    public int getNotificationId() {
        return 12;
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.IntentServiceBase, android.app.IntentService
    public void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        DefaultLogger.d("RecommendWidgetService", "---log---RecommendWidgetService start");
        if (intent == null) {
            return;
        }
        this.mStoryChangeCardId = intent.getLongExtra("card_status_change_card_id", -1L);
        this.mStoryChangeType = intent.getStringExtra("card_status_change_type");
        int[] intArrayExtra = intent.getIntArrayExtra("appWidgetIds");
        this.mWidgetSize = (IWidgetProviderConfig.WidgetSize) intent.getSerializableExtra("gallery_app_widget_size");
        if (intArrayExtra == null) {
            return;
        }
        if (intent.getBooleanExtra("start_widget_service_for_widget_delete", false)) {
            delete(intArrayExtra);
            return;
        }
        for (int i : intArrayExtra) {
            requestUpdatedWidget(i);
        }
    }

    public final void delete(int[] iArr) {
        RecommendWidgetDBManager.getInstance().delete(iArr);
        for (int i : iArr) {
            WidgetStatisticsHelper.statisticsRecommendWidgetDelete(i, this.mWidgetSize);
        }
    }

    public final synchronized void requestUpdatedWidget(int i) {
        boolean z;
        String[] strArr;
        boolean z2 = false;
        this.mRequestWidgetDataCount = 0;
        CardManager.getInstance().loadMoreCard();
        List<Card> loadedCard = CardManager.getInstance().getLoadedCard();
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.appWidgetId = i;
        if (loadedCard != null && loadedCard.size() > 0) {
            RecommendWidgetDBEntity findWidgetEntity = RecommendWidgetDBManager.getInstance().findWidgetEntity(i);
            if (isIgnoreUpdate(findWidgetEntity)) {
                return;
            }
            Long[] lArr = null;
            if (findWidgetEntity != null) {
                boolean isSameDay = DateUtils.isSameDay(findWidgetEntity.getUpdateTime(), Calendar.getInstance().getTimeInMillis());
                Long[] convertStringToLongArray = GalleryWidgetUtils.convertStringToLongArray(findWidgetEntity.getUsedCardIdList());
                strArr = GalleryWidgetUtils.convertStringToStringArray(findWidgetEntity.getUsedPicSha1List());
                z = isSameDay;
                lArr = convertStringToLongArray;
            } else {
                z = false;
                strArr = null;
            }
            ArrayList arrayList = new ArrayList();
            List<String> arrayList2 = new ArrayList<>();
            if (lArr != null) {
                arrayList.addAll(Arrays.asList(lArr));
            }
            if (strArr != null) {
                arrayList2.addAll(Arrays.asList(strArr));
            }
            if (arrayList2.size() > 0) {
                String str = arrayList2.get(arrayList2.size() - 1);
                if (GalleryWidgetUtils.isPictureUpdateFrequent(i, -1, str)) {
                    arrayList2.remove(str);
                }
            }
            requestWidgetData(z, imageEntity, arrayList, arrayList2);
            if (findWidgetEntity == null) {
                z2 = true;
            }
            updateEntityToDB(z2, imageEntity, arrayList, arrayList2);
            if (arrayList2.size() > 0) {
                GalleryWidgetUtils.setPictureUpdateTime(i, -1, arrayList2.get(arrayList2.size() - 1));
            }
        }
        if (TextUtils.isEmpty(imageEntity.sha1)) {
            updateWidgetEmpty(imageEntity.appWidgetId);
        } else {
            String str2 = TextUtils.isEmpty(imageEntity.localFile) ? imageEntity.thumbnailFile : imageEntity.localFile;
            if (TextUtils.isEmpty(str2)) {
                str2 = GalleryWidgetUtils.downloadImage(imageEntity._id, DownloadType.THUMBNAIL);
                DefaultLogger.d("RecommendWidgetService", "---log---GalleryWidgetUtils.downloadImage: %s>", str2);
            }
            if (TextUtils.isEmpty(str2)) {
                updateWidgetEmpty(imageEntity.appWidgetId);
            } else {
                updateWidget(imageEntity);
            }
        }
    }

    public final boolean isIgnoreUpdate(RecommendWidgetDBEntity recommendWidgetDBEntity) {
        Long[] convertStringToLongArray;
        if (recommendWidgetDBEntity != null) {
            if ("story_change_add".equals(this.mStoryChangeType) && !TextUtils.isEmpty(recommendWidgetDBEntity.getPicSha1())) {
                return true;
            }
            return "story_change_delete".equals(this.mStoryChangeType) && (convertStringToLongArray = GalleryWidgetUtils.convertStringToLongArray(recommendWidgetDBEntity.getUsedCardIdList())) != null && convertStringToLongArray.length > 0 && convertStringToLongArray[convertStringToLongArray.length - 1].longValue() != this.mStoryChangeCardId;
        }
        return false;
    }

    public final void requestWidgetData(boolean z, ImageEntity imageEntity, List<Long> list, List<String> list2) {
        Locale locale;
        String str;
        boolean z2;
        String str2;
        boolean z3;
        int i = this.mRequestWidgetDataCount;
        if (i > 3) {
            DefaultLogger.d("RecommendWidgetService", "---log---requestWidgetData mRequestWidgetDataCount > REQUEST_WIDGET_DATA_MAX_COUNT");
            updateWidgetEmpty(imageEntity.appWidgetId);
            return;
        }
        this.mRequestWidgetDataCount = i + 1;
        String format = String.format(Locale.US, "%s,%s", 0, 1);
        String str3 = Card.BASE_UI_CARD_SELECTION;
        Card card = null;
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        if (z) {
            if (list.size() > 0) {
                str2 = str3 + " AND " + String.format(locale, "%s in ('%s')", j.c, list.get(list.size() - 1));
            } else {
                str2 = str3;
            }
            List query = galleryEntityManager.query(Card.class, str2, null, "createTime desc", format);
            if (query.size() == 0) {
                if (list.size() > 0) {
                    list.remove(list.size() - 1);
                }
                List query2 = galleryEntityManager.query(Card.class, str3 + " AND " + String.format(locale, "%s not in ('%s')", j.c, TextUtils.join("','", list)), null, "createTime desc", format);
                if (query2.size() == 0) {
                    list.clear();
                    List query3 = galleryEntityManager.query(Card.class, str3, null, "createTime desc", format);
                    if (query3.size() > 0) {
                        z3 = false;
                        card = (Card) query3.get(0);
                        list.add(Long.valueOf(card.getRowId()));
                    }
                    z2 = false;
                } else {
                    z3 = false;
                    card = (Card) query2.get(0);
                    list.add(Long.valueOf(card.getRowId()));
                }
            } else {
                z3 = false;
                card = (Card) query.get(0);
            }
            z2 = z3;
        } else {
            list2.clear();
            if (list.size() > 0) {
                str = str3 + " AND " + String.format(locale, "%s not in ('%s')", j.c, TextUtils.join("','", list));
            } else {
                str = str3;
            }
            List query4 = galleryEntityManager.query(Card.class, str, null, "createTime desc", format);
            if (query4.size() == 0) {
                list.clear();
                List query5 = galleryEntityManager.query(Card.class, str3, null, "createTime desc", format);
                if (query5.size() > 0) {
                    z2 = false;
                    card = (Card) query5.get(0);
                    list.add(Long.valueOf(card.getRowId()));
                }
                z2 = false;
            } else {
                z2 = false;
                card = (Card) query4.get(0);
                list.add(Long.valueOf(card.getRowId()));
            }
        }
        Card card2 = card;
        if (card2 == null) {
            return;
        }
        ImageEntity findTargetPic = findTargetPic(card2, list2);
        if (!ImageEntity.isEntityValid(findTargetPic)) {
            requestWidgetData(z2, imageEntity, list, list2);
        } else if (!ImageEntity.isEntityValid(findTargetPic)) {
        } else {
            findTargetPic.appWidgetId = imageEntity.appWidgetId;
            findTargetPic.selectedCardId = card2.getRowId();
            findTargetPic.title = card2.getTitle();
            findTargetPic.description = card2.getDescription();
            imageEntity.setEntity(findTargetPic);
        }
    }

    public final ImageEntity findTargetPic(Card card, List<String> list) {
        List<String> selectedMediaSha1s = card.getSelectedMediaSha1s();
        Locale locale = Locale.US;
        ImageEntity imageEntity = getImageEntity(String.format(locale, "%s in ('%s') AND %s not in ('%s') AND serverType = 1 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", "sha1", TextUtils.join("','", selectedMediaSha1s), "sha1", TextUtils.join("','", list)));
        if (!ImageEntity.isEntityValid(imageEntity)) {
            imageEntity = getImageEntity(String.format(locale, "%s in ('%s') AND serverType = 1 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", "sha1", TextUtils.join("','", selectedMediaSha1s)));
            list.clear();
            if (ImageEntity.isEntityValid(imageEntity)) {
                list.add(imageEntity.sha1);
            }
        } else {
            list.add(imageEntity.sha1);
        }
        return imageEntity;
    }

    public final ImageEntity getImageEntity(String str) {
        this.mReadPicCount = 0;
        ImageEntity imageEntity = new ImageEntity();
        Cursor cursor = null;
        try {
            try {
                cursor = GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("cloud").columns(this.PIC_COLUMN_LIST).selection(str, null).orderBy("mixedDateTime desc ").create());
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = this.mReadPicCount + 1;
                        this.mReadPicCount = i;
                        if (i > 10) {
                            return imageEntity;
                        }
                        AnalyticUtils.CropRegionInfo cropRegionInfo = getCropRegionInfo(cursor);
                        if (cropRegionInfo.mCropRightBottomY < 0.0f) {
                            if (!ImageEntity.isEntityValid(imageEntity)) {
                                fillImageEntity(imageEntity, cursor, cropRegionInfo);
                            }
                        } else {
                            float f = cropRegionInfo.mScore;
                            if (!cropRegionInfo.mIsAccept) {
                                if (f > imageEntity.score) {
                                    fillImageEntity(imageEntity, cursor, cropRegionInfo);
                                }
                            } else {
                                ImageEntity imageEntity2 = new ImageEntity();
                                fillImageEntity(imageEntity2, cursor, cropRegionInfo);
                                return imageEntity2;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                DefaultLogger.w("RecommendWidgetService", e);
            }
            return imageEntity;
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    public final AnalyticUtils.CropRegionInfo getCropRegionInfo(Cursor cursor) {
        AnalyticUtils.RegionType regionType;
        long j = cursor.getLong(0);
        int i = cursor.getInt(4);
        int i2 = cursor.getInt(5);
        int i3 = cursor.getInt(6);
        IWidgetProviderConfig.WidgetSize widgetSize = this.mWidgetSize;
        if (widgetSize == IWidgetProviderConfig.WidgetSize.SIZE_4_2) {
            regionType = AnalyticUtils.RegionType.TWO_ONE;
        } else if (widgetSize == IWidgetProviderConfig.WidgetSize.SIZE_2_3) {
            regionType = AnalyticUtils.RegionType.TWO_THREE;
        } else {
            regionType = AnalyticUtils.RegionType.ONE_ONE;
        }
        AnalyticUtils.CropRegionInfo imageCropRegion = AnalyticUtils.getImageCropRegion(j, i, i2, i3, regionType, AnalyticUtils.DataFetcherType.DATA_FETCHER_DB);
        DefaultLogger.d("RecommendWidgetService", "---log---getCropRegionInfo :%f,%f,%f,%f", Float.valueOf(imageCropRegion.mCropLeftTopX), Float.valueOf(imageCropRegion.mCropLeftTopY), Float.valueOf(imageCropRegion.mCropRightBottomX), Float.valueOf(imageCropRegion.mCropRightBottomY));
        return imageCropRegion;
    }

    public final void fillImageEntity(ImageEntity imageEntity, Cursor cursor, AnalyticUtils.CropRegionInfo cropRegionInfo) {
        imageEntity._id = cursor.getLong(0);
        imageEntity.localFile = cursor.getString(1);
        imageEntity.thumbnailFile = cursor.getString(2);
        imageEntity.sha1 = cursor.getString(3);
        imageEntity.width = cursor.getInt(4);
        imageEntity.height = cursor.getInt(5);
        imageEntity.rotation = cursor.getInt(6);
        if (cropRegionInfo != null) {
            imageEntity.score = cropRegionInfo.mScore;
            imageEntity.mCropLeftTopX = cropRegionInfo.mCropLeftTopX;
            imageEntity.mCropLeftTopY = cropRegionInfo.mCropLeftTopY;
            imageEntity.mCropRightBottomX = cropRegionInfo.mCropRightBottomX;
            imageEntity.mCropRightBottomY = cropRegionInfo.mCropRightBottomY;
        }
    }

    public final void updateWidgetEmpty(int i) {
        RemoteViews recommendRemoteViews = WidgetInstallManager.getRecommendRemoteViews(this, this.mWidgetSize);
        recommendRemoteViews.setImageViewResource(R.id.image_view, WidgetInstallManager.getRecommendWidgetReplaceId(this.mWidgetSize));
        recommendRemoteViews.setTextViewText(R.id.text_title, getResources().getString(R.string.widget_recommend_default_title_on_story));
        recommendRemoteViews.setTextViewText(R.id.text_desc, getResources().getString(R.string.widget_recommend_default_description_no_story));
        Intent intent = new Intent(this, RecommendDispatchActivity.class);
        intent.putExtra("gallery_app_widget_id", i);
        recommendRemoteViews.setOnClickPendingIntent(R.id.layout_root, PendingIntent.getActivity(this, i, intent, 201326592));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(i);
        appWidgetOptions.putString("miuiWidgetEventCode", "other");
        appWidgetOptions.putString("miuiWidgetTimestamp", String.valueOf(SystemClock.elapsedRealtime()));
        appWidgetManager.updateAppWidgetOptions(i, appWidgetOptions);
        appWidgetManager.updateAppWidget(i, recommendRemoteViews);
        WidgetStatisticsHelper.statisticsRecommendWidgetStatus(i, this.mWidgetSize, "memories_null");
    }

    public final void updateWidget(ImageEntity imageEntity) {
        if (!ImageEntity.isEntityValid(imageEntity)) {
            updateWidgetEmpty(imageEntity.appWidgetId);
            return;
        }
        int i = imageEntity.appWidgetId;
        String str = TextUtils.isEmpty(imageEntity.localFile) ? imageEntity.thumbnailFile : imageEntity.localFile;
        if (TextUtils.isEmpty(str)) {
            updateWidgetEmpty(imageEntity.appWidgetId);
            return;
        }
        RemoteViews recommendRemoteViews = WidgetInstallManager.getRecommendRemoteViews(this, this.mWidgetSize);
        DefaultLogger.d("RecommendWidgetService", "---log---updateRecommendWidget mWidgetSize=%s,getCropBitmapFitOrientation before", this.mWidgetSize);
        long currentTimeMillis = System.currentTimeMillis();
        Bitmap cropBitmapFitOrientation = GalleryWidgetUtils.getCropBitmapFitOrientation(str, getRegionRect(imageEntity), GalleryWidgetUtils.getGlideOverrideSize(this.mWidgetSize));
        DefaultLogger.d("RecommendWidgetService", "---log---updateRecommendWidget mWidgetSize=%s,getCropBitmapFitOrientation after bitmap=%s,cost=%s", this.mWidgetSize, cropBitmapFitOrientation, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (WidgetInstallManager.isUseSmallLayout()) {
            Pair<Float, Float> smallLayoutSize = WidgetInstallManager.getSmallLayoutSize(this, this.mWidgetSize);
            cropBitmapFitOrientation = GalleryWidgetUtils.getFitWidgetRoundedBitmap(cropBitmapFitOrientation, ((Float) smallLayoutSize.first).floatValue(), ((Float) smallLayoutSize.second).floatValue(), getResources().getDimension(R.dimen.gallery_widget_bg_small_radius));
        }
        recommendRemoteViews.setImageViewBitmap(R.id.image_view, cropBitmapFitOrientation);
        recommendRemoteViews.setTextViewText(R.id.text_title, imageEntity.title);
        recommendRemoteViews.setTextViewText(R.id.text_desc, imageEntity.description);
        Intent intent = new Intent(this, RecommendDispatchActivity.class);
        intent.putExtra("gallery_app_widget_id", i);
        intent.putExtra("selected_card_id", imageEntity.selectedCardId);
        intent.putExtra("selected_pic_path", str);
        recommendRemoteViews.setOnClickPendingIntent(R.id.layout_root, PendingIntent.getActivity(this, i, intent, 201326592));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(i);
        appWidgetOptions.putString("miuiWidgetEventCode", "status1");
        appWidgetOptions.putString("miuiWidgetTimestamp", String.valueOf(SystemClock.elapsedRealtime()));
        appWidgetManager.updateAppWidgetOptions(i, appWidgetOptions);
        appWidgetManager.updateAppWidget(i, recommendRemoteViews);
        WidgetStatisticsHelper.statisticsRecommendWidgetStatus(i, this.mWidgetSize, "memories_ture");
    }

    public final RectF getRegionRect(ImageEntity imageEntity) {
        if (imageEntity != null) {
            float f = imageEntity.mCropLeftTopX;
            if (f >= 0.0f) {
                float f2 = imageEntity.mCropLeftTopY;
                if (f2 >= 0.0f && f < 1.0f && f2 < 1.0f) {
                    float f3 = imageEntity.mCropRightBottomX;
                    if (f3 > 0.0f) {
                        float f4 = imageEntity.mCropRightBottomY;
                        if (f4 > 0.0f && f3 <= 1.0f && f4 <= 1.0f) {
                            return new RectF(imageEntity.mCropLeftTopX, imageEntity.mCropLeftTopY, imageEntity.mCropRightBottomX, imageEntity.mCropRightBottomY);
                        }
                    }
                }
            }
        }
        return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public final void updateEntityToDB(boolean z, ImageEntity imageEntity, List<Long> list, List<String> list2) {
        if (!ImageEntity.isEntityValid(imageEntity)) {
            DefaultLogger.d("RecommendWidgetService", "---log---updateEntityToDB Entity not Valid, delete");
            RecommendWidgetDBManager.getInstance().delete(new int[]{imageEntity.appWidgetId});
            return;
        }
        int i = imageEntity.appWidgetId;
        String convertArrayToString = GalleryWidgetUtils.convertArrayToString(list);
        String convertArrayToString2 = GalleryWidgetUtils.convertArrayToString(list2);
        long j = imageEntity._id;
        String str = imageEntity.sha1;
        int i2 = imageEntity.width;
        int i3 = imageEntity.height;
        String str2 = imageEntity.title;
        RecommendWidgetDBEntity build = new RecommendWidgetDBEntity.Builder().setWidgetId(i).setUsedCardIdList(convertArrayToString).setUsedPicSha1List(convertArrayToString2).setPicId(j).setPicSha1(str).setPicWidth(i2).setPicHeight(i3).setPicRotation(imageEntity.rotation).setTitle(str2).setDescription(imageEntity.description).setUpdateTime(Calendar.getInstance().getTimeInMillis()).build();
        if (z) {
            RecommendWidgetDBManager.getInstance().add(build);
        } else {
            RecommendWidgetDBManager.getInstance().update(build);
        }
    }

    /* loaded from: classes2.dex */
    public static class ImageEntity {
        public long _id;
        public int appWidgetId;
        public String description;
        public int height;
        public String localFile;
        public float mCropLeftTopX;
        public float mCropLeftTopY;
        public float mCropRightBottomX;
        public float mCropRightBottomY;
        public int rotation;
        public float score = -1.0f;
        public long selectedCardId;
        public String sha1;
        public String thumbnailFile;
        public String title;
        public int width;

        public void setEntity(ImageEntity imageEntity) {
            if (imageEntity != null) {
                this.score = imageEntity.score;
                this.appWidgetId = imageEntity.appWidgetId;
                this.sha1 = imageEntity.sha1;
                this.title = imageEntity.title;
                this.description = imageEntity.description;
                this.localFile = imageEntity.localFile;
                this.thumbnailFile = imageEntity.thumbnailFile;
                this._id = imageEntity._id;
                this.selectedCardId = imageEntity.selectedCardId;
                this.width = imageEntity.width;
                this.height = imageEntity.height;
                this.rotation = imageEntity.rotation;
                this.mCropLeftTopX = imageEntity.mCropLeftTopX;
                this.mCropLeftTopY = imageEntity.mCropLeftTopY;
                this.mCropRightBottomX = imageEntity.mCropRightBottomX;
                this.mCropRightBottomY = imageEntity.mCropRightBottomY;
            }
        }

        public static boolean isEntityValid(ImageEntity imageEntity) {
            return imageEntity != null && !TextUtils.isEmpty(imageEntity.sha1);
        }
    }
}
