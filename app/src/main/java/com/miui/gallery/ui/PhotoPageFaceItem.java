package com.miui.gallery.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.activity.facebaby.FacePageActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.data.CacheOfAllFacesInOnePhoto;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.renameface.FaceAlbumHandlerBase;
import com.miui.gallery.ui.renameface.FaceAlbumRenameHandler;
import com.miui.gallery.ui.renameface.InputFaceNameFragment;
import com.miui.gallery.ui.renameface.RemoveFromFaceAlbumHandler;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.PhotoPageDataCache;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PhotoPageFaceItem extends PhotoPageItem implements CacheOfAllFacesInOnePhoto.PhotoViewProvider {
    public FaceHandler mFaceHandler;
    public FaceHighLightManager mFaceHighLightManager;
    public FaceNameLabelsManager mFaceNameLablesManager;

    public PhotoPageFaceItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFaceNameLablesManager = new FaceNameLabelsManager();
        this.mFaceHighLightManager = new FaceHighLightManager();
        this.mFaceHandler = new FaceHandler();
    }

    public final void bindFaceAnymore(BaseDataItem baseDataItem) {
        CloudItem cloudItem = (CloudItem) baseDataItem;
        ArrayList<PeopleFace> requestFacesOfThePhoto = ((this.mFaceHighLightManager.isVisible() || this.mFaceNameLablesManager.isVisible()) && baseDataItem != null && !TextUtils.isEmpty(cloudItem.getServerId())) ? CacheOfAllFacesInOnePhoto.getInstance().requestFacesOfThePhoto(this, cloudItem.getServerId()) : null;
        this.mFaceNameLablesManager.bindViewAndData(cloudItem, requestFacesOfThePhoto);
        this.mFaceHighLightManager.bindViewAndData(cloudItem, requestFacesOfThePhoto);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnMatrixChanged(RectF rectF) {
        super.doOnMatrixChanged(rectF);
        FaceNameLabelsManager faceNameLabelsManager = this.mFaceNameLablesManager;
        if (faceNameLabelsManager != null) {
            faceNameLabelsManager.onMatrixChanged(rectF);
        }
        FaceHighLightManager faceHighLightManager = this.mFaceHighLightManager;
        if (faceHighLightManager != null) {
            faceHighLightManager.onMatrixChanged();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void swapItem(BaseDataItem baseDataItem) {
        super.swapItem(baseDataItem);
        bindFaceAnymore(baseDataItem);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mFaceNameLablesManager.isVisible()) {
            this.mFaceNameLablesManager.onLayout(z, i, i2, i3, i4);
        }
        if (this.mFaceHighLightManager.isVisible()) {
            this.mFaceHighLightManager.onLayout(z, i, i2, i3, i4);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mFaceHighLightManager.isVisible()) {
            this.mFaceHighLightManager.onDraw(canvas);
        }
    }

    @Override // com.miui.gallery.data.CacheOfAllFacesInOnePhoto.PhotoViewProvider
    public void onInvalidated() {
        FaceHandler faceHandler = this.mFaceHandler;
        if (faceHandler != null) {
            faceHandler.notifyChanged();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        FaceHighLightManager faceHighLightManager = this.mFaceHighLightManager;
        if (faceHighLightManager != null) {
            faceHighLightManager.onDestroy();
        }
        super.doRelease();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mFaceHighLightManager.isVisible()) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animExit(ItemViewInfo itemViewInfo, PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        if (this.mFaceHighLightManager.isVisible() && !this.mFaceHighLightManager.isExiting()) {
            this.mFaceHighLightManager.onExiting();
        }
        super.animExit(itemViewInfo, photoTransitionListener);
    }

    public final boolean isCheckInAction() {
        return this.mCheckManager.inAction();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static RectF getFaceRect(float f, float f2, PeopleFace peopleFace, int i) {
        double d = peopleFace.faceXScale;
        double d2 = peopleFace.faceYScale;
        double d3 = peopleFace.faceWScale;
        double d4 = peopleFace.faceHScale;
        boolean z = true;
        boolean z2 = false;
        switch (i) {
            case 1:
                z = false;
                z2 = z;
                break;
            case 2:
                z2 = z;
                break;
            case 3:
                z = false;
                d = (1.0d - d) - d3;
                d2 = (1.0d - d2) - d4;
                z2 = z;
                break;
            case 4:
                d = (1.0d - d) - d3;
                d2 = (1.0d - d2) - d4;
                z2 = z;
                break;
            case 5:
                d = (1.0d - d) - d3;
                z2 = z;
                d3 = d4;
                d4 = d3;
                double d5 = d;
                d = d2;
                d2 = d5;
                break;
            case 6:
                z = false;
                d2 = (1.0d - d2) - d4;
                z2 = z;
                d3 = d4;
                d4 = d3;
                double d52 = d;
                d = d2;
                d2 = d52;
                break;
            case 7:
                d2 = (1.0d - d2) - d4;
                z2 = z;
                d3 = d4;
                d4 = d3;
                double d522 = d;
                d = d2;
                d2 = d522;
                break;
            case 8:
                z = false;
                d = (1.0d - d) - d3;
                z2 = z;
                d3 = d4;
                d4 = d3;
                double d5222 = d;
                d = d2;
                d2 = d5222;
                break;
        }
        if (z2) {
            d = (1.0d - d) - d3;
        }
        double d6 = f;
        float f3 = (float) (d * d6);
        double d7 = f2;
        float f4 = (float) (d2 * d7);
        return new RectF(f3, f4, ((float) (d6 * d3)) + f3, ((float) (d7 * d4)) + f4);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionBarVisibleChanged(Boolean bool, int i) {
        super.onActionBarVisibleChanged(bool, i);
        bindFaceAnymore(this.mDataItem);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActivityResult(int i, int i2, Intent intent) {
        PeopleContactInfo peopleContactInfo = null;
        if (i == 16) {
            if (intent != null && i2 == -1) {
                peopleContactInfo = InputFaceNameFragment.getContactInfo(getContext(), intent);
            }
            FaceAlbumRenameHandler faceAlbumRenameHandler = this.mFaceHandler.mFaceAlbumRenameHandler;
            if (faceAlbumRenameHandler != null) {
                faceAlbumRenameHandler.finishWhenGetContact(peopleContactInfo);
            }
        } else if (i == 17) {
            if (intent != null && i2 == -1) {
                peopleContactInfo = InputFaceNameFragment.getContactInfo(getContext(), intent);
            }
            RemoveFromFaceAlbumHandler removeFromFaceAlbumHandler = this.mFaceHandler.mRemoveFromFaceAlbumHandler;
            if (removeFromFaceAlbumHandler != null) {
                removeFromFaceAlbumHandler.finishWhenGetContact(peopleContactInfo);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    /* loaded from: classes2.dex */
    public class FaceHandler {
        public PeopleFace mCurrentFace;
        public FaceAlbumRenameHandler mFaceAlbumRenameHandler;
        public RemoveFromFaceAlbumHandler mRemoveFromFaceAlbumHandler;
        public FaceAlbumHandlerBase.FaceAlbumHandlerListener mRemoveFromFaceAlbumListener;

        public boolean shouldShow() {
            return false;
        }

        public FaceHandler() {
            this.mRemoveFromFaceAlbumListener = new FaceAlbumHandlerBase.FaceAlbumHandlerListener() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceHandler.3
                @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceAlbumHandlerListener
                public void onGetFolderItem(FaceAlbumHandlerBase.FaceFolderItem faceFolderItem) {
                    doRemove(faceFolderItem);
                }

                public final void doRemove(FaceAlbumHandlerBase.FaceFolderItem faceFolderItem) {
                    NormalPeopleFaceMediaSet.doMoveFacesToAnother(faceFolderItem, new long[]{Long.parseLong(FaceHandler.this.mCurrentFace._id)});
                    CacheOfAllFacesInOnePhoto.getInstance().clearCache();
                    PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener = PhotoPageFaceItem.this.mPhotoPageInteractionListener;
                    if (photoPageInteractionListener != null) {
                        photoPageInteractionListener.notifyDataChanged();
                    }
                }
            };
        }

        public void notifyChanged() {
            if (PhotoPageFaceItem.this.isAttachedToWindow()) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PhotoPageFaceItem photoPageFaceItem = PhotoPageFaceItem.this;
                        photoPageFaceItem.bindFaceAnymore(photoPageFaceItem.mDataItem);
                    }
                });
            }
        }

        public String getRecommendFaceId() {
            return PhotoPageDataCache.getInstance().getString("recommend_face_id", null, null);
        }

        public void exit() {
            ((Activity) PhotoPageFaceItem.this.getContext()).onBackPressed();
        }

        public void renameFace(PeopleFace peopleFace) {
            FaceAlbumRenameHandler faceAlbumRenameHandler = new FaceAlbumRenameHandler((FragmentActivity) PhotoPageFaceItem.this.getContext(), new NormalPeopleFaceMediaSet(Long.parseLong(peopleFace.localGroupId), peopleFace.groupId, peopleFace.peopleName), new FaceAlbumRenameHandler.ConfirmListener() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceHandler.2
                @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.ConfirmListener
                public void onConfirm(String str, boolean z) {
                    CacheOfAllFacesInOnePhoto.getInstance().clearCache();
                    PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener = PhotoPageFaceItem.this.mPhotoPageInteractionListener;
                    if (photoPageInteractionListener != null) {
                        photoPageInteractionListener.notifyDataChanged();
                    }
                }
            }, !PeopleContactInfo.isUnKnownRelation(peopleFace.relationType));
            this.mFaceAlbumRenameHandler = faceAlbumRenameHandler;
            faceAlbumRenameHandler.show();
        }

        public void removeFace(PeopleFace peopleFace) {
            RemoveFromFaceAlbumHandler removeFromFaceAlbumHandler = new RemoveFromFaceAlbumHandler((FragmentActivity) PhotoPageFaceItem.this.getContext(), new NormalPeopleFaceMediaSet(Long.parseLong(peopleFace.localGroupId), peopleFace.groupId, peopleFace.peopleName), this.mRemoveFromFaceAlbumListener);
            this.mRemoveFromFaceAlbumHandler = removeFromFaceAlbumHandler;
            this.mCurrentFace = peopleFace;
            removeFromFaceAlbumHandler.show();
        }
    }

    /* loaded from: classes2.dex */
    public class FaceNameLabelsManager {
        public ArrayList<View> mLables;
        public ArrayList<PeopleFace> mLastFaces;
        public int mOrientation;

        public FaceNameLabelsManager() {
            this.mLables = new ArrayList<>();
            this.mLastFaces = new ArrayList<>();
            this.mOrientation = 0;
        }

        public final void bindViewAndData(final CloudItem cloudItem, ArrayList<PeopleFace> arrayList) {
            if (!isVisible()) {
                removeFacelabel();
            } else if (cloudItem != null && !TextUtils.isEmpty(cloudItem.getServerId())) {
                if (arrayList == null || notChangedFaces(arrayList)) {
                    return;
                }
                this.mOrientation = cloudItem.getOrientation();
                removeFacelabel();
                this.mLastFaces = (ArrayList) arrayList.clone();
                Iterator<PeopleFace> it = arrayList.iterator();
                while (it.hasNext()) {
                    final PeopleFace next = it.next();
                    TextView textView = new TextView(PhotoPageFaceItem.this.mPhotoView.getContext());
                    textView.setText(!TextUtils.isEmpty(next.peopleName) ? next.peopleName : PhotoPageFaceItem.this.mPhotoView.getContext().getString(R.string.who_is_this));
                    textView.setTag(R.id.face_info, next);
                    textView.setGravity(17);
                    textView.setTextSize(0, PhotoPageFaceItem.this.mPhotoView.getContext().getResources().getDimensionPixelSize(R.dimen.face_label_font_size));
                    textView.setTextColor(PhotoPageFaceItem.this.getResources().getColor(R.color.white_80_transparent));
                    textView.setBackgroundResource(R.drawable.face_tag);
                    PhotoPageFaceItem.this.addView(textView, new RelativeLayout.LayoutParams(-2, -2));
                    textView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceNameLabelsManager.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(next.peopleName)) {
                                if (PhotoPageFaceItem.this.mFaceHandler == null) {
                                    return;
                                }
                                PhotoPageFaceItem.this.mFaceHandler.renameFace(next);
                                return;
                            }
                            FaceNameLabelsManager.this.showMenuDialog((PeopleFace) view.getTag(R.id.face_info), cloudItem);
                        }
                    });
                    this.mLables.add(textView);
                }
            } else {
                removeFacelabel();
            }
        }

        public final void onMatrixChanged(RectF rectF) {
            if (isVisible()) {
                onLayout(true, (int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                PhotoPageFaceItem.this.invalidate();
                return;
            }
            removeFacelabel();
        }

        public final boolean notChangedFaces(ArrayList<PeopleFace> arrayList) {
            if (arrayList.size() == this.mLastFaces.size()) {
                int i = 0;
                while (i < arrayList.size() && arrayList.get(i).equalMainInfoWith(this.mLastFaces.get(i))) {
                    i++;
                }
                if (i == arrayList.size()) {
                    return true;
                }
            }
            return false;
        }

        public final void removeFacelabel() {
            if (this.mLables.size() > 0) {
                Iterator<View> it = this.mLables.iterator();
                while (it.hasNext()) {
                    PhotoPageFaceItem.this.removeView(it.next());
                }
                this.mLables.clear();
            }
            this.mLastFaces.clear();
        }

        public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
            RectF drawableSize = PhotoPageFaceItem.this.mPhotoView.getDrawableSize();
            if (drawableSize.width() == 0.0f || drawableSize.height() == 0.0f) {
                return;
            }
            Iterator<View> it = this.mLables.iterator();
            while (it.hasNext()) {
                View next = it.next();
                RectF faceRect = PhotoPageFaceItem.getFaceRect(drawableSize.width(), drawableSize.height(), (PeopleFace) next.getTag(R.id.face_info), this.mOrientation);
                PhotoPageFaceItem.this.mPhotoView.getAbsoluteRect(faceRect);
                int measuredWidth = ((((int) faceRect.left) + ((int) faceRect.right)) / 2) - (next.getMeasuredWidth() / 2);
                int measuredHeight = (((int) faceRect.top) - next.getMeasuredHeight()) - 20;
                next.layout(measuredWidth, measuredHeight, next.getMeasuredWidth() + measuredWidth, next.getMeasuredHeight() + measuredHeight);
            }
        }

        public final void showMenuDialog(final PeopleFace peopleFace, BaseDataItem baseDataItem) {
            new AlertDialog.Builder(PhotoPageFaceItem.this.mPhotoView.getContext()).setSingleChoiceItems(new String[]{PhotoPageFaceItem.this.mPhotoView.getContext().getString(R.string.view_face_album), PhotoPageFaceItem.this.mPhotoView.getContext().getString(R.string.operation_remove_from_face_album, peopleFace.peopleName)}, -1, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceNameLabelsManager.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (i == 0) {
                        FaceNameLabelsManager.this.viewAlbum(peopleFace);
                    } else if (i == 1) {
                        if (PhotoPageFaceItem.this.mFaceHandler == null) {
                            return;
                        }
                        PhotoPageFaceItem.this.mFaceHandler.removeFace(peopleFace);
                    } else {
                        throw new IllegalStateException("unknown item clicked: " + i);
                    }
                }
            }).create().show();
        }

        public final void viewAlbum(PeopleFace peopleFace) {
            Intent intent = new Intent();
            String str = peopleFace.groupId;
            String str2 = peopleFace.localGroupId;
            intent.putExtra("server_id_of_album", str);
            intent.putExtra("local_id_of_album", str2);
            intent.putExtra("album_name", peopleFace.peopleName);
            intent.setClass(PhotoPageFaceItem.this.mPhotoView.getContext(), FacePageActivity.class);
            PhotoPageFaceItem.this.mPhotoView.getContext().startActivity(intent);
        }

        public final boolean isVisible() {
            return PhotoPageFaceItem.this.mFaceHandler != null && PhotoPageFaceItem.this.mFaceHandler.shouldShow() && !PhotoPageFaceItem.this.isCheckInAction();
        }
    }

    /* loaded from: classes2.dex */
    public class FaceHighLightManager implements View.OnClickListener {
        public boolean isAnimatorStarted;
        public boolean isExiting;
        public ValueAnimator mAnimator;
        public float mCurrentAlpha;
        public int mCurrentRadius;
        public int mCurrentX;
        public int mCurrentY;
        public float mExitAlpha;
        public int mOrientation;
        public Paint mPaint;
        public PeopleFace mPeopleFace;
        public int mStartRadius;
        public int mStartX;
        public int mStartY;
        public int mTargetRadius;
        public int mTargetX;
        public int mTargetY;
        public PorterDuffXfermode mXfermode;

        public FaceHighLightManager() {
            this.mOrientation = 0;
            this.mExitAlpha = 1.0f;
            this.isAnimatorStarted = false;
            this.isExiting = false;
            this.mPaint = new Paint();
            this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        }

        public static /* synthetic */ float access$1240(FaceHighLightManager faceHighLightManager, float f) {
            float f2 = faceHighLightManager.mCurrentAlpha / f;
            faceHighLightManager.mCurrentAlpha = f2;
            return f2;
        }

        public void bindViewAndData(CloudItem cloudItem, ArrayList<PeopleFace> arrayList) {
            if (!isVisible() || this.isAnimatorStarted) {
                return;
            }
            PhotoPageFaceItem.this.setOnClickListener(this);
            if (cloudItem == null || TextUtils.isEmpty(cloudItem.getServerId()) || arrayList == null) {
                return;
            }
            this.mOrientation = cloudItem.getOrientation();
            Iterator<PeopleFace> it = arrayList.iterator();
            while (it.hasNext()) {
                PeopleFace next = it.next();
                if (PhotoPageFaceItem.this.mFaceHandler.getRecommendFaceId().equals(next.serverId)) {
                    this.mPeopleFace = next;
                    initTargetValues();
                    PhotoPageFaceItem.this.invalidate();
                    return;
                }
            }
        }

        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            if (!z || this.isAnimatorStarted) {
                return;
            }
            int width = PhotoPageFaceItem.this.getWidth();
            int height = PhotoPageFaceItem.this.getHeight();
            int sqrt = (int) (Math.sqrt(Math.pow(width, 2.0d) + Math.pow(height, 2.0d)) / 2.0d);
            int i5 = width / 2;
            this.mStartX = i5;
            this.mCurrentX = i5;
            int i6 = height / 2;
            this.mStartY = i6;
            this.mCurrentY = i6;
            this.mStartRadius = sqrt;
            this.mCurrentRadius = sqrt;
        }

        public final void initTargetValues() {
            if (this.mPeopleFace == null) {
                return;
            }
            RectF drawableSize = PhotoPageFaceItem.this.mPhotoView.getDrawableSize();
            if (drawableSize.width() == 0.0f || drawableSize.height() == 0.0f) {
                return;
            }
            RectF faceRect = PhotoPageFaceItem.getFaceRect(drawableSize.width(), drawableSize.height(), this.mPeopleFace, this.mOrientation);
            PhotoPageFaceItem.this.mPhotoView.getBaseMatrix().mapRect(faceRect);
            this.mTargetRadius = (int) Math.max(faceRect.width(), faceRect.height());
            this.mTargetX = (int) faceRect.centerX();
            this.mTargetY = (int) faceRect.centerY();
        }

        public final void onMatrixChanged() {
            if (isVisible()) {
                initTargetValues();
                PhotoPageFaceItem.this.invalidate();
            }
        }

        public void onDraw(Canvas canvas) {
            if (this.mPeopleFace == null || this.mTargetRadius <= 0) {
                return;
            }
            if (!this.isAnimatorStarted) {
                ValueAnimator valueAnimator = new ValueAnimator();
                this.mAnimator = valueAnimator;
                valueAnimator.setFloatValues(this.mCurrentAlpha, 0.5f);
                this.mAnimator.setDuration(400L);
                this.mAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
                this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.ui.PhotoPageFaceItem.FaceHighLightManager.1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        FaceHighLightManager.this.mCurrentAlpha = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                        float f = FaceHighLightManager.this.mCurrentAlpha / 0.5f;
                        if (FaceHighLightManager.this.isExiting()) {
                            FaceHighLightManager.access$1240(FaceHighLightManager.this, 2.0f);
                            FaceHighLightManager.this.mExitAlpha = ((double) f) > 0.875d ? 1.0f : f / 0.875f;
                        } else {
                            FaceHighLightManager faceHighLightManager = FaceHighLightManager.this;
                            faceHighLightManager.mCurrentX = (int) (((faceHighLightManager.mTargetX - FaceHighLightManager.this.mStartX) * f) + FaceHighLightManager.this.mStartX);
                            FaceHighLightManager faceHighLightManager2 = FaceHighLightManager.this;
                            faceHighLightManager2.mCurrentY = (int) (((faceHighLightManager2.mTargetY - FaceHighLightManager.this.mStartY) * f) + FaceHighLightManager.this.mStartY);
                            FaceHighLightManager faceHighLightManager3 = FaceHighLightManager.this;
                            faceHighLightManager3.mCurrentRadius = (int) ((f * (faceHighLightManager3.mTargetRadius - FaceHighLightManager.this.mStartRadius)) + FaceHighLightManager.this.mStartRadius);
                        }
                        PhotoPageFaceItem.this.invalidate();
                    }
                });
                this.mAnimator.start();
                this.isAnimatorStarted = true;
            }
            int saveLayerAlpha = canvas.saveLayerAlpha(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), (int) (this.mCurrentAlpha * 255.0f), 31);
            if (isExiting()) {
                canvas.setMatrix(PhotoPageFaceItem.this.mPhotoView.getSuppMatrix());
                Drawable drawable = PhotoPageFaceItem.this.mPhotoView.getDrawable();
                if (drawable != null) {
                    drawable.setAlpha((int) (this.mExitAlpha * 255.0f));
                }
            }
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-16777216);
            canvas.drawPaint(this.mPaint);
            this.mPaint.setXfermode(this.mXfermode);
            canvas.drawCircle(this.mCurrentX, this.mCurrentY, this.mCurrentRadius, this.mPaint);
            this.mPaint.setXfermode(null);
            canvas.restoreToCount(saveLayerAlpha);
        }

        public boolean isVisible() {
            return (PhotoPageFaceItem.this.mFaceHandler == null || PhotoPageFaceItem.this.mFaceHandler.getRecommendFaceId() == null) ? false : true;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (PhotoPageFaceItem.this.mFaceHandler != null) {
                PhotoPageFaceItem.this.mFaceHandler.exit();
            }
        }

        public boolean isExiting() {
            return this.isExiting;
        }

        public boolean onExiting() {
            ValueAnimator valueAnimator;
            if (this.isAnimatorStarted && (valueAnimator = this.mAnimator) != null && !this.isExiting) {
                if (valueAnimator.isStarted()) {
                    this.mAnimator.pause();
                }
                this.mAnimator.setDuration(200L);
                this.mAnimator.reverse();
                this.mAnimator.resume();
                this.isExiting = true;
            }
            return this.isExiting;
        }

        public void onDestroy() {
            this.mTargetRadius = 0;
            this.mPeopleFace = null;
            this.isAnimatorStarted = false;
            this.isExiting = false;
            this.mExitAlpha = 1.0f;
            ValueAnimator valueAnimator = this.mAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                return;
            }
            this.mAnimator.end();
        }
    }
}
