package com.miui.gallery.widget;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.biz.story.StoryAlbumAdapter;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.card.model.BaseMedia;
import com.miui.gallery.card.ui.detail.SlideShowController;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.glide.CloudImageLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class CardSlideView extends FrameLayout implements ICardView, SlideShowController.SlideShowNextListener {
    public List<MediaFeatureItem> mCoverItems;
    public boolean mIsPaused;
    public String mLocalPath;
    public List<String> mSelectedMediaSha1s;
    public boolean mShouldUpdateViews;
    public Future mSlideFuture;
    public SlideShowController mSlideShowController;
    public SlideShowView mSlideView;

    public CardSlideView(Context context) {
        this(context, null);
    }

    public CardSlideView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CardSlideView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    public final void initViews(Context context) {
        LinearLayout.inflate(context, R.layout.card_slide_layout, this);
        SlideShowView slideShowView = (SlideShowView) findViewById(R.id.slide);
        this.mSlideView = slideShowView;
        slideShowView.setSlideDuration(3500);
        SlideShowController slideShowController = new SlideShowController(this.mSlideView, 3500);
        this.mSlideShowController = slideShowController;
        slideShowController.setSlideShowNextListener(this);
    }

    public SlideShowView getSlideShowView() {
        return this.mSlideView;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateViews();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.card_cover_height_large), 1073741824));
    }

    public void update(List<String> list, List<MediaFeatureItem> list2) {
        if (!isSelectMediaChanged(list, this.mSelectedMediaSha1s)) {
            return;
        }
        this.mShouldUpdateViews = true;
        this.mSelectedMediaSha1s = list;
        this.mCoverItems = list2;
        requestLayout();
    }

    public boolean isSelectMediaChanged(List<String> list, List<String> list2) {
        if (!BaseMiscUtil.isValid(list) || !BaseMiscUtil.isValid(list2) || list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.equals(list.get(i), list2.get(i))) {
                return true;
            }
        }
        return false;
    }

    public final void doSlideShow() {
        Future future = this.mSlideFuture;
        if (future != null) {
            future.cancel();
        }
        if (this.mSlideShowController.getCount() == 0 && BaseMiscUtil.isValid(this.mCoverItems)) {
            updateMedias(this.mCoverItems);
        }
        this.mSlideFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<List<MediaInfo>>() { // from class: com.miui.gallery.widget.CardSlideView.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public List<MediaInfo> mo1807run(ThreadPool.JobContext jobContext) {
                Uri build = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build();
                return (List) SafeDBUtil.safeQuery(CardSlideView.this.getContext(), build, StoryAlbumAdapter.PROJECTION, "(localGroupId!=-1000) AND " + String.format("%s IN ('%s')", "sha1", TextUtils.join("','", CardSlideView.this.mSelectedMediaSha1s)), (String[]) null, "alias_create_time DESC ", new SafeDBUtil.QueryHandler<List<MediaInfo>>() { // from class: com.miui.gallery.widget.CardSlideView.1.1
                    /* JADX WARN: Removed duplicated region for block: B:12:0x0029 A[RETURN] */
                    /* JADX WARN: Removed duplicated region for block: B:14:0x002b A[RETURN] */
                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle  reason: collision with other method in class */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public java.util.List<com.miui.gallery.biz.story.data.MediaInfo> mo1808handle(android.database.Cursor r5) {
                        /*
                            r4 = this;
                            java.util.ArrayList r0 = new java.util.ArrayList
                            r0.<init>()
                            r1 = 0
                            if (r5 == 0) goto L27
                            boolean r2 = r5.moveToFirst()
                            if (r2 == 0) goto L27
                        Le:
                            com.miui.gallery.biz.story.data.MediaInfo r2 = new com.miui.gallery.biz.story.data.MediaInfo
                            r2.<init>(r5)
                            java.lang.String r3 = r2.getUri()
                            boolean r3 = android.text.TextUtils.isEmpty(r3)
                            if (r3 != 0) goto L1e
                            r1 = 1
                        L1e:
                            r0.add(r2)
                            boolean r2 = r5.moveToNext()
                            if (r2 != 0) goto Le
                        L27:
                            if (r1 != 0) goto L2b
                            r5 = 0
                            return r5
                        L2b:
                            return r0
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.CardSlideView.AnonymousClass1.C00921.mo1808handle(android.database.Cursor):java.util.List");
                    }
                });
            }
        }, new FutureHandler<List<MediaInfo>>() { // from class: com.miui.gallery.widget.CardSlideView.2
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<List<MediaInfo>> future2) {
                if (future2 == null || !BaseMiscUtil.isValid(future2.get())) {
                    CardSlideView.this.checkAndDownload();
                } else if (future2.isCancelled()) {
                } else {
                    CardSlideView.this.updateMedias(future2.get());
                }
            }
        });
    }

    public final void checkAndDownload() {
        if (BaseMiscUtil.isValid(this.mCoverItems)) {
            int size = this.mCoverItems.size();
            ArrayList arrayList = new ArrayList(size);
            ArrayList arrayList2 = new ArrayList(size);
            ArrayList arrayList3 = new ArrayList(size);
            Iterator<MediaFeatureItem> it = this.mCoverItems.iterator();
            boolean z = false;
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MediaFeatureItem next = it.next();
                if (!TextUtils.isEmpty(next.getUri())) {
                    z = true;
                    break;
                }
                arrayList.add(CloudUriAdapter.getDownloadUri(next.getId()));
                arrayList2.add(DownloadType.THUMBNAIL);
                arrayList3.add(new CoverCloudImageLoadingListener(i));
                i++;
            }
            if (z || !BaseMiscUtil.isValid(arrayList)) {
                return;
            }
            CloudImageLoader.getInstance().loadImages(arrayList, arrayList2, arrayList3, null);
        }
    }

    @Override // com.miui.gallery.widget.ICardView
    public int getCurrentIndex() {
        SlideShowController slideShowController = this.mSlideShowController;
        if (slideShowController == null) {
            return 0;
        }
        return slideShowController.getShowIndex();
    }

    @Override // com.miui.gallery.widget.ICardView
    public void setLoadIndex(int i) {
        this.mSlideShowController.setLoadIndex(i);
    }

    @Override // com.miui.gallery.widget.ICardView
    public String getCurrentLocalPath() {
        return this.mLocalPath;
    }

    @Override // com.miui.gallery.card.ui.detail.SlideShowController.SlideShowNextListener
    public void onNext(String str) {
        this.mLocalPath = str;
    }

    /* loaded from: classes2.dex */
    public class CoverCloudImageLoadingListener implements CloudImageLoadingListener {
        public int mIndex;

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
        }

        public CoverCloudImageLoadingListener(int i) {
            this.mIndex = i;
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
            if (!BaseMiscUtil.isValid(CardSlideView.this.mCoverItems) || this.mIndex >= CardSlideView.this.mCoverItems.size()) {
                return;
            }
            MediaFeatureItem mediaFeatureItem = (MediaFeatureItem) CardSlideView.this.mCoverItems.get(this.mIndex);
            if (mediaFeatureItem.getId() != ContentUris.parseId(uri)) {
                return;
            }
            switch (AnonymousClass4.$SwitchMap$com$miui$gallery$sdk$download$DownloadType[downloadType.ordinal()]) {
                case 1:
                case 2:
                    mediaFeatureItem.setMicroThumbnailPath(str);
                    break;
                case 3:
                case 4:
                    mediaFeatureItem.setThumbnailPath(str);
                    break;
                case 5:
                case 6:
                case 7:
                    mediaFeatureItem.setOriginPath(str);
                    break;
            }
            CardSlideView cardSlideView = CardSlideView.this;
            cardSlideView.updateMedias(cardSlideView.mCoverItems);
        }
    }

    /* renamed from: com.miui.gallery.widget.CardSlideView$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass4 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$sdk$download$DownloadType;

        static {
            int[] iArr = new int[DownloadType.values().length];
            $SwitchMap$com$miui$gallery$sdk$download$DownloadType = iArr;
            try {
                iArr[DownloadType.MICRO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.MICRO_BATCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.THUMBNAIL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.THUMBNAIL_BATCH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN_BATCH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN_FORCE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public final void updateMedias(List<? extends BaseMedia> list) {
        this.mSlideShowController.updateMedias(list, false);
        if (this.mIsPaused) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.widget.CardSlideView.3
                @Override // java.lang.Runnable
                public void run() {
                    CardSlideView.this.mSlideShowController.pause();
                }
            });
        }
    }

    public void onDestroy() {
        Future future = this.mSlideFuture;
        if (future != null) {
            future.cancel();
            this.mSlideFuture = null;
        }
        SlideShowController slideShowController = this.mSlideShowController;
        if (slideShowController != null) {
            slideShowController.destroy();
        }
    }

    public void onResume() {
        if (this.mIsPaused) {
            this.mSlideShowController.resume();
            this.mIsPaused = false;
        }
    }

    public void onPause() {
        this.mSlideShowController.pause();
        this.mIsPaused = true;
    }

    public final void updateViews() {
        if (!BaseMiscUtil.isValid(this.mSelectedMediaSha1s) || !this.mShouldUpdateViews) {
            return;
        }
        this.mShouldUpdateViews = false;
        doSlideShow();
    }
}
