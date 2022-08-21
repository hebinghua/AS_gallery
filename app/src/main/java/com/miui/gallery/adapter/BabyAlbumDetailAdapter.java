package com.miui.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.FastScrollerStringCapsuleModel;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.deprecated.ThumbnailInfo;
import com.miui.gallery.share.AlbumShareOperations;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.Path;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.BabyAlbumDetailFaceHeaderItem;
import com.miui.gallery.ui.BabyAlbumDetailGridHeaderItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.ui.pictures.cluster.TimelineCluster;
import com.miui.gallery.util.CropUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.baby.FindFace2CreateBabyAlbum;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;

/* loaded from: classes.dex */
public class BabyAlbumDetailAdapter extends AlbumDetailAdapter {
    public FastScrollerStringCapsuleModel mCapsuleModel;
    public FaceHeaderHelper mFaceHeaderHelper;
    public int mFirstHeadHeight;

    public BabyAlbumDetailAdapter(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
        this.mFaceHeaderHelper = new FaceHeaderHelper();
        init();
    }

    public final void init() {
        this.mFirstHeadHeight = this.mContext.getResources().getDimensionPixelOffset(R.dimen.baby_album_page_header_first_height);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        BabyAlbumDetailGridHeaderItem babyAlbumDetailGridHeaderItem = new BabyAlbumDetailGridHeaderItem(viewGroup.getContext(), null);
        babyAlbumDetailGridHeaderItem.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        return new BaseViewHolder(babyAlbumDetailGridHeaderItem);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public void doBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, PictureViewMode pictureViewMode) {
        String formatRelativeDate;
        TimelineCluster timelineCluster = this.mClusterAdapter.getTimelineCluster(pictureViewMode);
        String str = null;
        String groupLabel = !PictureViewMode.isYearMode(pictureViewMode) ? timelineCluster.getGroupLabel(i, this.mShowTimeLine) : null;
        long j = mo1558getItem(timelineCluster.getGroupStartPosition(i, this.mShowTimeLine)).getLong(5);
        if (PictureViewMode.isYearMode(pictureViewMode)) {
            formatRelativeDate = GalleryDateUtils.formatRelativeOnlyYear(j);
        } else if (PictureViewMode.isMonthMode(pictureViewMode)) {
            formatRelativeDate = GalleryDateUtils.formatRelativeMonth(j);
            groupLabel = simplifiedLocation(groupLabel);
        } else {
            formatRelativeDate = GalleryDateUtils.formatRelativeDate(j);
        }
        BabyAlbumDetailGridHeaderItem babyAlbumDetailGridHeaderItem = (BabyAlbumDetailGridHeaderItem) baseViewHolder.itemView;
        if (!PictureViewMode.isYearMode(pictureViewMode)) {
            str = this.mFaceHeaderHelper.getAge(j);
        }
        babyAlbumDetailGridHeaderItem.bindData(formatRelativeDate, groupLabel, str, getHeaderHeight(i));
    }

    public void setFaceHeader(BabyInfo babyInfo, ThumbnailInfo thumbnailInfo, String str, String str2, BabyAlbumDetailFaceHeaderItem babyAlbumDetailFaceHeaderItem, View view, View.OnClickListener onClickListener, final boolean z) {
        setBabyInfoAndThumbnailInfo(babyInfo, thumbnailInfo, str2);
        FaceHeaderHelper faceHeaderHelper = this.mFaceHeaderHelper;
        faceHeaderHelper.mFaceHeaderItem = babyAlbumDetailFaceHeaderItem;
        faceHeaderHelper.setFaceImage();
        this.mFaceHeaderHelper.setAlbumName(str);
        this.mFaceHeaderHelper.setBirthday();
        this.mFaceHeaderHelper.mFaceHeaderItem.setOnFaceClickListener(new View.OnClickListener() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                BabyAlbumDetailAdapter.this.gotoBabyInfoSettingPage(z);
                SamplingStatHelper.recordCountEvent("baby", "baby_edit_baby_info");
            }
        });
        this.mFaceHeaderHelper.mFaceHeaderItem.setOnBackgroundClickListener(onClickListener);
    }

    public void gotoBabyInfoSettingPage(boolean z) {
        this.mFaceHeaderHelper.gotoBabyInfoSettingPage(z);
    }

    public void setAlbumId(long j) {
        this.mFaceHeaderHelper.mBabyAlbumLocalId = j;
    }

    public void setPeopleServerId(String str) {
        this.mFaceHeaderHelper.mBabyAlbumPeopleServerId = str;
    }

    public void firstBindHeaderPic(Cursor cursor) {
        if (!this.mFaceHeaderHelper.bindBackgroundByThumbnail()) {
            this.mFaceHeaderHelper.bindBackgroundByCursor(cursor);
        }
    }

    public void rebindHeaderPic(String str) {
        this.mFaceHeaderHelper.bindBackgroundPicByPath(str);
    }

    public void resetBabyInfoAndThumbnailInfo(BabyInfo babyInfo, ThumbnailInfo thumbnailInfo) {
        setBabyInfoAndThumbnailInfo(babyInfo, thumbnailInfo, null);
        this.mFaceHeaderHelper.setFaceImage();
        this.mFaceHeaderHelper.setBirthday();
        notifyDataSetChanged();
    }

    public Bitmap getFaceImageOfFaceHeaderItem() {
        int dimension = (int) this.mContext.getResources().getDimension(R.dimen.baby_timeline_quick_start_icon_size);
        return CropUtil.cropImage(this.mFaceHeaderHelper.getFaceImage(), dimension, dimension, false);
    }

    public final void setBabyInfoAndThumbnailInfo(BabyInfo babyInfo, ThumbnailInfo thumbnailInfo, String str) {
        if (babyInfo != null) {
            this.mFaceHeaderHelper.mBabyInfo = babyInfo;
        }
        if (thumbnailInfo != null) {
            this.mFaceHeaderHelper.mThumbnailInfo = thumbnailInfo;
        }
        if (!TextUtils.isEmpty(str)) {
            this.mFaceHeaderHelper.mSharerInfoStr = str;
        }
        this.mFaceHeaderHelper.setBirthdayYearMonthDay();
    }

    @Override // com.miui.gallery.adapter.GroupedMediaAdapter
    public int getHeaderHeight(int i) {
        if (i == 0) {
            return this.mFirstHeadHeight;
        }
        return super.getHeaderHeight(i);
    }

    @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public FastScrollerCapsuleContentProvider getCapsuleContent(int i) {
        if (this.mCapsuleModel == null) {
            this.mCapsuleModel = new FastScrollerStringCapsuleModel();
        }
        Cursor mo1558getItem = mo1558getItem(i);
        if (mo1558getItem != null && mo1558getItem.getCount() > 0) {
            this.mCapsuleModel.setContent(this.mFaceHeaderHelper.getAge(mo1558getItem.getLong(5)));
        }
        return this.mCapsuleModel;
    }

    /* loaded from: classes.dex */
    public class FaceHeaderHelper {
        public String TAG = "FaceHeaderHelper";
        public String mAlbumName;
        public long mBabyAlbumLocalId;
        public String mBabyAlbumPeopleServerId;
        public BabyInfo mBabyInfo;
        public int mBirthdayDay;
        public int mBirthdayMonth;
        public int mBirthdayYear;
        public BabyAlbumDetailFaceHeaderItem mFaceHeaderItem;
        public String mSharerInfoStr;
        public ThumbnailInfo mThumbnailInfo;

        public FaceHeaderHelper() {
        }

        public final void setAlbumName(String str) {
            this.mAlbumName = str;
            this.mFaceHeaderItem.setAlbumName(str);
        }

        public final void gotoBabyInfoSettingPage(boolean z) {
            FindFace2CreateBabyAlbum.gotoBabyAlbumInfoPage((Activity) BabyAlbumDetailAdapter.this.mContext, this.mThumbnailInfo, this.mBabyInfo, this.mBabyAlbumLocalId, this.mAlbumName, getFacePath(), z);
        }

        public final void setBirthdayYearMonthDay() {
            int[] splitBirthDay = BabyInfo.splitBirthDay(this.mBabyInfo);
            if (splitBirthDay != null) {
                this.mBirthdayYear = splitBirthDay[0];
                this.mBirthdayMonth = splitBirthDay[1];
                this.mBirthdayDay = splitBirthDay[2];
            }
        }

        public final void setBirthday() {
            String age = getAge(System.currentTimeMillis());
            if (TextUtils.isEmpty(age)) {
                return;
            }
            this.mFaceHeaderItem.setAge(age);
        }

        public final String getAge(long j) {
            int i;
            int[] age;
            int i2;
            String str;
            String country = GalleryApp.sGetAndroidContext().getResources().getConfiguration().locale.getCountry();
            if (("cn".equalsIgnoreCase(country) || "en".equalsIgnoreCase(country) || "us".equalsIgnoreCase(country)) && (i = this.mBirthdayDay) != 0 && (i2 = (age = BabyInfo.getAge(j, this.mBirthdayYear, this.mBirthdayMonth, i))[0]) >= 0) {
                int i3 = age[1];
                int i4 = age[2];
                if (!"cn".equalsIgnoreCase(country)) {
                    Resources resources = GalleryApp.sGetAndroidContext().getResources();
                    String quantityString = resources.getQuantityString(R.plurals.age_year, i2, Integer.valueOf(i2));
                    String quantityString2 = resources.getQuantityString(R.plurals.age_day, i4, Integer.valueOf(i4));
                    String quantityString3 = resources.getQuantityString(R.plurals.age_month, i3, Integer.valueOf(i3));
                    if (i2 != 0 && i3 != 0 && i4 != 0) {
                        return quantityString + " " + quantityString3 + " " + quantityString2;
                    } else if (i2 != 0 && i3 != 0 && i4 == 0) {
                        return quantityString + " " + quantityString3;
                    } else if (i2 != 0 && i3 == 0 && i4 == 0) {
                        return quantityString;
                    } else {
                        if (i2 == 0 && i3 == 0 && i4 != 0) {
                            if (i4 == 0) {
                                str = (i4 + 1) + " day";
                            } else {
                                str = (i4 + 1) + " days";
                            }
                            return str;
                        } else if (i2 == 0 && i3 != 0 && i4 == 0) {
                            return quantityString3;
                        } else {
                            if (i2 != 0 && i3 == 0 && i4 != 0) {
                                return quantityString + " " + quantityString2;
                            } else if (i2 != 0 || i3 == 0 || i4 == 0) {
                                if (i2 != 0 || i3 != 0 || i4 != 0) {
                                    return null;
                                }
                                return " first day";
                            } else {
                                return quantityString3 + " " + quantityString2;
                            }
                        }
                    }
                } else if (i2 != 0 && i3 != 0 && i4 != 0) {
                    return i2 + "岁" + i3 + "个月" + i4 + "天";
                } else if (i2 != 0 && i3 != 0 && i4 == 0) {
                    return i2 + "岁" + i3 + "个月";
                } else if (i2 != 0 && i3 == 0 && i4 == 0) {
                    return i2 + "岁生日";
                } else if (i2 == 0 && i3 == 0 && i4 != 0) {
                    return "第" + (i4 + 1) + "天";
                } else if (i2 == 0 && i3 != 0 && i4 == 0) {
                    return i3 + "个月";
                } else if (i2 != 0 && i3 == 0 && i4 != 0) {
                    return i2 + "岁零" + i4 + "天";
                } else if (i2 != 0 || i3 == 0 || i4 == 0) {
                    if (i2 != 0 || i3 != 0 || i4 != 0) {
                        return null;
                    }
                    return "出生第一天";
                } else {
                    return i3 + "个月" + i4 + "天";
                }
            }
            return "";
        }

        public Bitmap getFaceImage() {
            return this.mFaceHeaderItem.getHeadFacePic();
        }

        public String getFacePath() {
            return this.mFaceHeaderItem.getFacePath();
        }

        public void setFaceImage() {
            final ThumbnailInfo thumbnailInfo = this.mThumbnailInfo;
            if (!BabyAlbumDetailAdapter.this.isOtherShareAlbum()) {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Pair<String, FaceRegionRectF>>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public Pair<String, FaceRegionRectF> mo1807run(ThreadPool.JobContext jobContext) {
                        ThumbnailInfo thumbnailInfo2 = thumbnailInfo;
                        String faceInfo = thumbnailInfo2 != null ? thumbnailInfo2.getFaceInfo(BabyAlbumDetailAdapter.this.isOtherShareAlbum()) : null;
                        if (!TextUtils.isEmpty(faceInfo)) {
                            return new Pair<>(faceInfo, null);
                        }
                        FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
                        return new Pair<>(FaceManager.queryCoverImageOfOnePerson(FaceHeaderHelper.this.mBabyAlbumPeopleServerId, -1L, faceRegionRectFArr), faceRegionRectFArr[0]);
                    }
                }, new FutureListener<Pair<String, FaceRegionRectF>>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.2
                    @Override // com.miui.gallery.concurrent.FutureListener
                    public void onFutureDone(Future<Pair<String, FaceRegionRectF>> future) {
                        final Pair<String, FaceRegionRectF> pair = future.get();
                        if (TextUtils.isEmpty((CharSequence) pair.first)) {
                            DefaultLogger.e(FaceHeaderHelper.this.TAG, "pathPair.first is empty");
                        } else {
                            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Pair pair2 = pair;
                                    Object obj = pair2.second;
                                    if (obj != null) {
                                        FaceHeaderHelper.this.bindFaceImageByRect((String) pair2.first, (FaceRegionRectF) obj);
                                        return;
                                    }
                                    AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                                    FaceHeaderHelper.this.bindFaceImageFromPath((String) pair2.first, thumbnailInfo);
                                }
                            });
                        }
                    }
                });
            } else {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.3
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public String mo1807run(ThreadPool.JobContext jobContext) {
                        if (TextUtils.isEmpty(FaceHeaderHelper.this.mBabyAlbumPeopleServerId)) {
                            return thumbnailInfo.getFaceInfo(BabyAlbumDetailAdapter.this.isOtherShareAlbum());
                        }
                        FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
                        String queryCoverImageOfOnePerson = FaceManager.queryCoverImageOfOnePerson(FaceHeaderHelper.this.mBabyAlbumPeopleServerId, -1L, faceRegionRectFArr);
                        thumbnailInfo.setmFaceRegion(faceRegionRectFArr[0]);
                        return queryCoverImageOfOnePerson;
                    }
                }, new FutureListener<String>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.4
                    @Override // com.miui.gallery.concurrent.FutureListener
                    public void onFutureDone(Future<String> future) {
                        final String str = future.get();
                        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (TextUtils.isEmpty(str)) {
                                    if (TextUtils.isEmpty(FaceHeaderHelper.this.mSharerInfoStr)) {
                                        FaceHeaderHelper.this.setFaceImageFromByFirstGetSharerInfo();
                                        return;
                                    }
                                    FaceHeaderHelper faceHeaderHelper = FaceHeaderHelper.this;
                                    faceHeaderHelper.setFaceImageFromSharerInfo(faceHeaderHelper.mSharerInfoStr);
                                    return;
                                }
                                AnonymousClass4 anonymousClass4 = AnonymousClass4.this;
                                FaceHeaderHelper.this.bindFaceImageFromPath(str, thumbnailInfo);
                            }
                        });
                    }
                });
            }
        }

        public final void bindFaceImageFromPath(String str, ThumbnailInfo thumbnailInfo) {
            if (thumbnailInfo.getFaceRegion() != null) {
                this.mFaceHeaderItem.bindHeadFacePic(str, thumbnailInfo.getFaceRegion());
            } else {
                this.mFaceHeaderItem.bindHeadFacePicFromNet(str, thumbnailInfo.getFaceRegionFromFaceInfo());
            }
        }

        public final void bindFaceImageByRect(String str, FaceRegionRectF faceRegionRectF) {
            this.mFaceHeaderItem.bindHeadFacePic(str, faceRegionRectF);
        }

        /* renamed from: com.miui.gallery.adapter.BabyAlbumDetailAdapter$FaceHeaderHelper$5  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass5 implements AlbumShareUIManager.OnCompletionListener<Path, String> {
            public AnonymousClass5() {
            }

            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public void onCompletion(Path path, String str, int i, boolean z) {
                if (z) {
                    Log.i(FaceHeaderHelper.this.TAG, "updateInvitationAsync cancelled");
                } else if (i != 0) {
                    String str2 = FaceHeaderHelper.this.TAG;
                    Log.i(str2, "updateInvitationAsync error, errorCode=" + i);
                } else {
                    ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.5.1
                        @Override // com.miui.gallery.concurrent.ThreadPool.Job
                        /* renamed from: run  reason: collision with other method in class */
                        public String mo1807run(ThreadPool.JobContext jobContext) {
                            return FaceManager.querySharerInfoOfBabyAlbum(String.valueOf(FaceHeaderHelper.this.mBabyAlbumLocalId));
                        }
                    }, new FutureListener<String>() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.5.2
                        @Override // com.miui.gallery.concurrent.FutureListener
                        public void onFutureDone(Future<String> future) {
                            final String str3 = future.get();
                            if (!TextUtils.isEmpty(str3)) {
                                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.adapter.BabyAlbumDetailAdapter.FaceHeaderHelper.5.2.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        FaceHeaderHelper.this.setFaceImageFromSharerInfo(str3);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }

        public final void setFaceImageFromByFirstGetSharerInfo() {
            AlbumShareUIManager.updateInvitationAsync(new Path(this.mBabyAlbumLocalId, true, true), new AnonymousClass5());
        }

        public final void setFaceImageFromSharerInfo(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            AlbumShareOperations.SharerInfo parseSharerInfo = AlbumShareOperations.parseSharerInfo(str);
            if (parseSharerInfo == null || TextUtils.isEmpty(parseSharerInfo.mThumbnailUrl)) {
                String str2 = this.TAG;
                Log.e(str2, "illegal SharerInfo: " + str);
            } else if (TextUtils.isEmpty(parseSharerInfo.mThumbnailUrl)) {
            } else {
                this.mFaceHeaderItem.bindHeadFacePicFromNet(parseSharerInfo.mThumbnailUrl, parseSharerInfo.mFaceRelativePos);
            }
        }

        public final void setBackgroundByPathOrUri(String str, Uri uri) {
            BabyAlbumDetailAdapter.this.mFaceHeaderHelper.mFaceHeaderItem.bindHeaderBackgroundPic(str, uri, GlideOptions.bigPhotoOf());
        }

        public final boolean bindBackgroundByThumbnail() {
            ThumbnailInfo thumbnailInfo = this.mThumbnailInfo;
            if (thumbnailInfo != null) {
                String bgPath = thumbnailInfo.getBgPath();
                if (TextUtils.isEmpty(bgPath)) {
                    return false;
                }
                setBackgroundByPathOrUri(bgPath, null);
                return true;
            }
            return false;
        }

        public final void bindBackgroundByCursor(Cursor cursor) {
            if (cursor == null || cursor.isAfterLast()) {
                return;
            }
            String string = cursor.getString(1);
            String string2 = cursor.getString(2);
            String string3 = cursor.getString(14);
            if (!TextUtils.isEmpty(string2)) {
                string = string2;
            } else if (!TextUtils.isEmpty(string3)) {
                string = string3;
            }
            setBackgroundByPathOrUri(string, BaseMediaAdapter.getDownloadUri(cursor, 12, 0));
        }

        public final void bindBackgroundPicByPath(String str) {
            setBackgroundByPathOrUri(str, null);
        }
    }
}
