package com.miui.gallery.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.provider.deprecated.PeopleRecommendMediaSet;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.glide.CloudImageLoader;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.widget.PagerGridLayout;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class RecommendFaceGroupAdapter extends PagerGridLayout.BaseAdapter {
    public static final String[] PROJECTION = {j.c, "sha1", "microthumbfile", "thumbnailFile", "localFile", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "exifOrientation", "serverId", "photo_id", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};
    public CloudImageLoadingListener mCloudLoadingListener;
    public Cursor mCursor;
    public BaseMediaFragment mFragment;
    public BindImageHelper.OnImageLoadingCompleteListener mLoadingCompleteListener;
    public Long mLocalIdOfAlbum;
    public String mServerIdOfAlbum;
    public ArrayList<String> mConfirmToRight = new ArrayList<>();
    public ArrayList<String> mConfirmToWrong = new ArrayList<>();
    public int mVisibleItemCount = 0;

    public abstract int getLayoutId();

    public RecommendFaceGroupAdapter(BaseMediaFragment baseMediaFragment, String str, Long l) {
        this.mFragment = baseMediaFragment;
        this.mServerIdOfAlbum = str;
        this.mLocalIdOfAlbum = l;
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
    public int getCount() {
        Cursor cursor = this.mCursor;
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
    public View getView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        inflate.setTag(new RecommendFaceItem(inflate));
        return inflate;
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
    public void bindData(int i, View view) {
        Cursor cursorByPosition = getCursorByPosition(i);
        String string = cursorByPosition.getString(10);
        long j = cursorByPosition.getLong(11);
        long j2 = cursorByPosition.getLong(12);
        this.mConfirmToRight.add(string);
        RecommendFaceItem recommendFaceItem = (RecommendFaceItem) view.getTag();
        recommendFaceItem.setChecked(true);
        recommendFaceItem.setServerId(string);
        recommendFaceItem.setCloudId(j);
        recommendFaceItem.setFileLength(j2);
        DownloadType downloadType = DownloadType.THUMBNAIL;
        String string2 = cursorByPosition.getString(3);
        if (TextUtils.isEmpty(string2)) {
            string2 = cursorByPosition.getString(4);
        }
        if (TextUtils.isEmpty(string2)) {
            downloadType = DownloadType.MICRO;
            string2 = cursorByPosition.getString(2);
        }
        recommendFaceItem.setFacePath(string2);
        if (TextUtils.isEmpty(string2)) {
            downloadType = DownloadType.MICRO;
            string2 = StorageUtils.getSafePriorMicroThumbnailPath(cursorByPosition.getString(1));
        }
        recommendFaceItem.bindImage(string2, getDownloadUri(cursorByPosition, 11), GlideOptions.peopleFaceOf(new FaceRegionRectF(cursorByPosition.getFloat(5), cursorByPosition.getFloat(6), cursorByPosition.getFloat(7) + cursorByPosition.getFloat(5), cursorByPosition.getFloat(6) + cursorByPosition.getFloat(8), cursorByPosition.getInt(9)), j2), downloadType);
        this.mVisibleItemCount++;
    }

    public final Uri getDownloadUri(Cursor cursor, int i) {
        return CloudUriAdapter.getDownloadUri(cursor.getLong(i));
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
    public void freshView(View view) {
        ((RecommendFaceItem) view.getTag()).refreshIfNeeded();
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
    public void onPageChanged() {
        confirmRecommends(new ArrayList<>(this.mConfirmToRight), new ArrayList<>(this.mConfirmToWrong));
        this.mConfirmToWrong.clear();
        this.mConfirmToRight.clear();
        this.mVisibleItemCount = 0;
    }

    public int getMergeFaceCount() {
        ArrayList<String> arrayList = this.mConfirmToRight;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public final void gotoRecommendCoverPage(String str, ItemViewInfo itemViewInfo, String str2, long j, long j2) {
        ImageLoadParams build = new ImageLoadParams.Builder().setKey(j).setFilePath(str).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(0).setFileLength(j2).fromFace(true).build();
        ArrayList<ItemViewInfo> arrayList = new ArrayList<>(1);
        arrayList.add(itemViewInfo);
        PhotoPageIntent.Builder uri = new PhotoPageIntent.Builder(this.mFragment, InternalPhotoPageActivity.class).setUri(GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI);
        uri.setSelectionArgs(new String[]{"'" + str2 + "'"}).setOrderBy("dateTaken DESC ").setImageLoadParams(build).setRecommendFaceId(str2).setSpecialItemViewInfos(arrayList).setUnfoldBurst(true).setEnterType(EnterTypeUtils.EnterType.FROM_RECOMMEND_FACE_PAGE).build().gotoPhotoPage();
        SamplingStatHelper.recordCountEvent("face", "face_click_recommend_face");
    }

    public final void recommendCheckStatusChanged(String str, boolean z) {
        if (z) {
            this.mConfirmToWrong.remove(str);
            this.mConfirmToRight.add(str);
        } else {
            this.mConfirmToWrong.add(str);
            this.mConfirmToRight.remove(str);
        }
        if (this.mConfirmToWrong.size() == this.mVisibleItemCount) {
            BaseMediaFragment baseMediaFragment = this.mFragment;
            if (baseMediaFragment instanceof FacePageFragment) {
                ((FacePageFragment) baseMediaFragment).changeToNextPage();
            } else {
                ((RecommendFacePageFragment) baseMediaFragment).changeToNextPage();
            }
        }
    }

    public void confirmRecommends(final ArrayList<String> arrayList, final ArrayList<String> arrayList2) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.ui.RecommendFaceGroupAdapter.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                RecommendFaceGroupAdapter.this.confirmFace(arrayList, true);
                RecommendFaceGroupAdapter.this.confirmFace(arrayList2, false);
                return null;
            }
        });
    }

    public final void confirmFace(ArrayList<String> arrayList, boolean z) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = new NormalPeopleFaceMediaSet(this.mLocalIdOfAlbum.longValue(), this.mServerIdOfAlbum, "");
        PeopleRecommendMediaSet.refreshRecommendHistoryToTrue(arrayList, normalPeopleFaceMediaSet);
        if (z) {
            PeopleRecommendMediaSet.addSelectItemsToRecommendedMediaSet(arrayList, normalPeopleFaceMediaSet);
            HashMap hashMap = new HashMap(1);
            hashMap.put(MiStat.Param.COUNT, String.valueOf(arrayList.size()));
            SamplingStatHelper.recordCountEvent("face", "face_confirm_recommend_face", hashMap);
            return;
        }
        PeopleRecommendMediaSet.feedbackIgnoredPeople2Server(arrayList, normalPeopleFaceMediaSet);
    }

    public Cursor swapCursor(Cursor cursor) {
        Cursor cursor2 = this.mCursor;
        if (cursor == cursor2) {
            return null;
        }
        this.mCursor = cursor;
        return cursor2;
    }

    public void setOnLoadingCompleteListener(BindImageHelper.OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
        this.mLoadingCompleteListener = onImageLoadingCompleteListener;
        if (onImageLoadingCompleteListener != null) {
            this.mCloudLoadingListener = new CloudImageLoadingListener() { // from class: com.miui.gallery.ui.RecommendFaceGroupAdapter.2
                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
                    if (RecommendFaceGroupAdapter.this.mLoadingCompleteListener != null) {
                        RecommendFaceGroupAdapter.this.mLoadingCompleteListener.onLoadingFailed();
                    }
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
                    if (RecommendFaceGroupAdapter.this.mLoadingCompleteListener != null) {
                        RecommendFaceGroupAdapter.this.mLoadingCompleteListener.onLoadingComplete();
                    }
                }
            };
        } else {
            this.mCloudLoadingListener = null;
        }
    }

    public Cursor getCursorByPosition(int i) {
        this.mCursor.moveToPosition(i);
        return this.mCursor;
    }

    /* loaded from: classes2.dex */
    public class RecommendFaceItem implements View.OnClickListener, View.OnTouchListener {
        public CheckBox mCheckbox;
        public ImageView mCoverImage;
        public String mFacePath;
        public long mFileLength;
        public long mPhotoCloudId;
        public String mServerId;

        public RecommendFaceItem(View view) {
            this.mCoverImage = (ImageView) view.findViewById(R.id.recommend_face_cover);
            this.mCheckbox = (CheckBox) view.findViewById(R.id.face_check);
            this.mCoverImage.setOnTouchListener(this);
            this.mCoverImage.setClickable(true);
            this.mCheckbox.setOnClickListener(this);
            this.mCheckbox.setChecked(true);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() != R.id.face_check) {
                return;
            }
            RecommendFaceGroupAdapter.this.recommendCheckStatusChanged(this.mServerId, this.mCheckbox.isChecked());
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0014, code lost:
            if (r0 != 3) goto L9;
         */
        @Override // android.view.View.OnTouchListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouch(android.view.View r12, android.view.MotionEvent r13) {
            /*
                r11 = this;
                int r0 = r12.getId()
                r1 = 0
                r2 = 2131363168(0x7f0a0560, float:1.8346137E38)
                if (r0 != r2) goto L44
                int r0 = r13.getAction()
                r2 = 1
                if (r0 == 0) goto L40
                if (r0 == r2) goto L17
                r13 = 3
                if (r0 == r13) goto L3c
                goto L43
            L17:
                float r0 = r13.getRawX()
                float r3 = r13.getX()
                float r0 = r0 - r3
                int r0 = (int) r0
                float r3 = r13.getRawY()
                float r13 = r13.getY()
                float r3 = r3 - r13
                int r13 = (int) r3
                com.miui.gallery.util.photoview.ItemViewInfo r5 = com.miui.gallery.util.photoview.ItemViewInfo.getImageInfo(r0, r13, r12, r1)
                com.miui.gallery.ui.RecommendFaceGroupAdapter r3 = com.miui.gallery.ui.RecommendFaceGroupAdapter.this
                java.lang.String r4 = r11.mFacePath
                java.lang.String r6 = r11.mServerId
                long r7 = r11.mPhotoCloudId
                long r9 = r11.mFileLength
                com.miui.gallery.ui.RecommendFaceGroupAdapter.access$300(r3, r4, r5, r6, r7, r9)
            L3c:
                r12.setPressed(r1)
                goto L43
            L40:
                r12.setPressed(r2)
            L43:
                return r2
            L44:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.RecommendFaceGroupAdapter.RecommendFaceItem.onTouch(android.view.View, android.view.MotionEvent):boolean");
        }

        public void bindImage(String str, Uri uri, RequestOptions requestOptions, DownloadType downloadType) {
            BindImageHelper.bindImage(str, uri, BaseNetworkUtils.isActiveNetworkMetered() ? DownloadType.MICRO : DownloadType.THUMBNAIL, this.mCoverImage, requestOptions, RecommendFaceGroupAdapter.this.mLoadingCompleteListener);
            if (DownloadType.MICRO != downloadType || TextUtils.isEmpty(str) || BaseNetworkUtils.isActiveNetworkMetered()) {
                return;
            }
            CloudImageLoader.getInstance().displayImage(uri, DownloadType.THUMBNAIL, this.mCoverImage, requestOptions, (TransitionOptions<?, Bitmap>) null, RecommendFaceGroupAdapter.this.mCloudLoadingListener, (CloudImageLoadingProgressListener) null, true, false);
        }

        public void refreshIfNeeded() {
            if (!TextUtils.isEmpty(this.mFacePath) || TextUtils.isEmpty(this.mServerId)) {
                return;
            }
            FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
            String queryCoverImageOfOneFace = FaceManager.queryCoverImageOfOneFace(this.mServerId, faceRegionRectFArr);
            this.mFacePath = queryCoverImageOfOneFace;
            if (TextUtils.isEmpty(queryCoverImageOfOneFace)) {
                return;
            }
            Glide.with(this.mCoverImage).mo989load(GalleryModel.of(queryCoverImageOfOneFace)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(faceRegionRectFArr[0], this.mFileLength)).into(this.mCoverImage);
        }

        public void setServerId(String str) {
            this.mServerId = str;
        }

        public void setCloudId(long j) {
            this.mPhotoCloudId = j;
        }

        public void setFacePath(String str) {
            this.mFacePath = str;
        }

        public void setFileLength(long j) {
            this.mFileLength = j;
        }

        public void setChecked(boolean z) {
            this.mCheckbox.setChecked(z);
        }
    }
}
