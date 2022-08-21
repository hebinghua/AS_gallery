package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Strings;
import com.miui.gallery.Config$PlaceholderDrawable;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.album.main.base.config.PhotoIconConfig;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.widget.ScalableImageView;
import com.miui.gallery.widget.editwrapper.PickAnimationHelper$BackgroundImageViewable;
import com.miui.gallery.widget.editwrapper.PickAnimationHelper$ShowNumberWhenPicking;
import com.miui.gallery.widget.recyclerview.transition.TransitionalAnchor;

/* loaded from: classes2.dex */
public class MicroThumbGridItem extends FrameLayout implements CheckableView, PickAnimationHelper$BackgroundImageViewable, PickAnimationHelper$ShowNumberWhenPicking, TransitionalAnchor {
    public boolean isOpenCheckBoxAnim;
    public View mBottomIndicatorContainer;
    public ViewStub mBottomIndicatorStub;
    public CheckBox mCheckBox;
    public View mCheckBoxContainer;
    public ValueAnimator mCheckBoxEnterAnimator;
    public final Animator.AnimatorListener mCheckBoxEnterListener;
    public ValueAnimator mCheckBoxExitAnimator;
    public Animator.AnimatorListener mCheckBoxExitListener;
    public View mFavoriteIndicator;
    public FullyDrawnCounter mFullyDrawnCounter;
    public ImageView mImageView;
    public int mInflateFlags;
    public boolean mIsSimilarBestImage;
    public ViewStub mItemStub;
    public long mLastSyncId;
    public ImageView mMask;
    public TextView mPickingNumberIndicator;
    public ValueAnimator mSimilarBestEnterAnimator;
    public ValueAnimator mSimilarBestExitAnimator;
    public Animator.AnimatorListener mSimilarBestExitListener;
    public ImageView mSimilarBestMark;
    public ImageView mSyncIndicator;
    public ValueAnimator mSyncIndicatorEnterAnimator;
    public Animator.AnimatorListener mSyncIndicatorEnterListener;
    public ValueAnimator mSyncIndicatorExitAnimator;
    public Animator.AnimatorListener mSyncIndicatorExitListener;
    public int mSyncState;
    public View mTopIndicatorContainer;
    public TextView mTypeIndicator;

    public static boolean displaySyncableState(int i) {
        return (i & 2) != 0;
    }

    public static boolean displaySyncedState(int i) {
        return (i & 8) != 0;
    }

    public static boolean displaySyncingState(int i) {
        return (i & 4) != 0;
    }

    public static boolean displayUnSyncableState(int i) {
        return (i & 1) != 0;
    }

    public boolean isSquareItem() {
        return true;
    }

    public MicroThumbGridItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSyncState = Integer.MAX_VALUE;
        this.mLastSyncId = -1L;
        this.isOpenCheckBoxAnim = true;
        this.mCheckBoxEnterListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.MicroThumbGridItem.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                MicroThumbGridItem microThumbGridItem = MicroThumbGridItem.this;
                microThumbGridItem.bindFavoriteIndicator(microThumbGridItem.mFavoriteIndicator != null && MicroThumbGridItem.this.mFavoriteIndicator.getVisibility() == 0);
            }
        };
        this.mCheckBoxExitListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.MicroThumbGridItem.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MicroThumbGridItem.this.mCheckBoxContainer.setVisibility(8);
                MicroThumbGridItem.this.mCheckBoxContainer.setAlpha(1.0f);
            }
        };
        this.mSimilarBestExitListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.MicroThumbGridItem.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MicroThumbGridItem.this.mSimilarBestMark.setVisibility(8);
                MicroThumbGridItem.this.mSimilarBestMark.setAlpha(1.0f);
            }
        };
        this.mSyncIndicatorEnterListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.MicroThumbGridItem.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (MicroThumbGridItem.this.mSyncIndicator != null) {
                    MicroThumbGridItem.this.mSyncIndicator.clearAnimation();
                }
            }
        };
        this.mSyncIndicatorExitListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.MicroThumbGridItem.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (MicroThumbGridItem.this.mSyncIndicator != null) {
                    MicroThumbGridItem.this.mSyncIndicator.clearAnimation();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MicroThumbGridItem.this.mSyncIndicator.setAlpha(1.0f);
            }
        };
        ScalableImageView scalableImageView = new ScalableImageView(getContext());
        this.mImageView = scalableImageView;
        scalableImageView.setId(R.id.micro_thumb);
        this.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.mImageView.setDuplicateParentStateEnabled(false);
        this.mImageView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mImageView);
        ViewStub viewStub = new ViewStub(getContext(), (int) R.layout.micro_thumbnail_grid_item_stub);
        this.mItemStub = viewStub;
        viewStub.setId(R.id.item_stub);
        this.mItemStub.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mItemStub);
        ViewStub viewStub2 = new ViewStub(getContext(), (int) R.layout.bottom_indicator_stub);
        this.mBottomIndicatorStub = viewStub2;
        viewStub2.setId(R.id.bottom_indicator_stub);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 87;
        this.mBottomIndicatorStub.setLayoutParams(layoutParams);
        addView(this.mBottomIndicatorStub);
    }

    public void setImageForeground(int i) {
        ImageView imageView = this.mImageView;
        imageView.setForeground(imageView.getResources().getDrawable(i));
    }

    @Override // com.miui.gallery.widget.editwrapper.PickAnimationHelper$BackgroundImageViewable
    public ImageView getBackgroundImageView() {
        return this.mImageView;
    }

    @Override // com.miui.gallery.widget.editwrapper.PickAnimationHelper$ShowNumberWhenPicking
    public ImageView getBackgroundMask() {
        ensureStubInflate();
        return this.mMask;
    }

    @Override // com.miui.gallery.widget.editwrapper.PickAnimationHelper$ShowNumberWhenPicking
    public TextView getShowNumberTextView() {
        ensureStubInflate();
        return this.mPickingNumberIndicator;
    }

    @Override // com.miui.gallery.ui.Checkable
    public void setCheckable(boolean z) {
        initCheckBoxAnimIfNeed();
        if (z) {
            ensureStubInflate();
            if (this.mCheckBoxContainer.getVisibility() == 0) {
                return;
            }
            this.mCheckBoxContainer.setVisibility(0);
            ValueAnimator valueAnimator = this.mCheckBoxEnterAnimator;
            if (valueAnimator != null && !valueAnimator.isRunning()) {
                this.mCheckBoxEnterAnimator.setTarget(this.mCheckBox);
                this.mCheckBoxEnterAnimator.addListener(this.mCheckBoxEnterListener);
                this.mCheckBoxEnterAnimator.start();
            }
        } else {
            View view = this.mCheckBoxContainer;
            if (view != null) {
                if (view.getVisibility() == 8 || this.mCheckBoxContainer.getAlpha() == 0.0f) {
                    return;
                }
                ValueAnimator valueAnimator2 = this.mCheckBoxExitAnimator;
                if (valueAnimator2 != null && !valueAnimator2.isRunning()) {
                    this.mCheckBoxExitAnimator.setTarget(this.mCheckBoxContainer);
                    this.mCheckBoxExitAnimator.addListener(this.mCheckBoxExitListener);
                    this.mCheckBoxExitAnimator.start();
                }
            }
        }
        setSimilarMarkEnable(z);
    }

    public final void initCheckBoxAnimIfNeed() {
        if (this.isOpenCheckBoxAnim) {
            if (this.mCheckBoxEnterAnimator == null) {
                this.mCheckBoxEnterAnimator = PhotoIconConfig.getPhotoIconConfig().getEnterAnimator();
            }
            if (this.mCheckBoxExitAnimator == null) {
                this.mCheckBoxExitAnimator = PhotoIconConfig.getPhotoIconConfig().getExitAnimator();
            }
            if (this.mSimilarBestEnterAnimator == null) {
                this.mSimilarBestEnterAnimator = PhotoIconConfig.getPhotoIconConfig().getEnterAnimator();
            }
            if (this.mSimilarBestExitAnimator == null) {
                this.mSimilarBestExitAnimator = PhotoIconConfig.getPhotoIconConfig().getExitAnimator();
            }
            if (this.mSyncIndicatorEnterAnimator == null) {
                this.mSyncIndicatorEnterAnimator = PhotoIconConfig.getPhotoIconConfig().getEnterAnimator();
            }
            if (this.mSyncIndicatorExitAnimator != null) {
                return;
            }
            this.mSyncIndicatorExitAnimator = PhotoIconConfig.getPhotoIconConfig().getExitAnimator();
        }
    }

    public void setIsOpenCheckBoxAnim(boolean z) {
        this.isOpenCheckBoxAnim = z;
        if (!z) {
            this.mCheckBoxExitAnimator = null;
            this.mCheckBoxEnterAnimator = null;
        }
    }

    public void setSimilarMarkEnable(boolean z) {
        if (z && this.mIsSimilarBestImage) {
            ensureStubInflate();
            if (this.mSimilarBestEnterAnimator.isRunning() || this.mSimilarBestMark.getVisibility() == 0) {
                return;
            }
            this.mSimilarBestMark.setVisibility(0);
            this.mSimilarBestEnterAnimator.setTarget(this.mSimilarBestMark);
            this.mSimilarBestEnterAnimator.start();
            return;
        }
        ImageView imageView = this.mSimilarBestMark;
        if (imageView == null || imageView.getVisibility() == 8 || this.mSimilarBestMark.getAlpha() == 0.0f || this.mSimilarBestExitAnimator.isRunning()) {
            return;
        }
        this.mSimilarBestExitAnimator.setTarget(this.mSimilarBestMark);
        this.mSimilarBestExitAnimator.addListener(this.mSimilarBestExitListener);
        this.mSimilarBestExitAnimator.start();
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (z) {
            ensureStubInflate();
            this.mCheckBox.setChecked(true);
        } else if (this.mCheckBox == null) {
        } else {
            ValueAnimator valueAnimator = this.mCheckBoxExitAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return;
            }
            this.mCheckBox.setChecked(false);
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        CheckBox checkBox = this.mCheckBox;
        if (checkBox == null) {
            return false;
        }
        return checkBox.isChecked();
    }

    @Override // com.miui.gallery.ui.CheckableView
    public void setCheckableListener(View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            View view = this.mCheckBoxContainer;
            if (view == null) {
                return;
            }
            view.setOnClickListener(null);
            return;
        }
        ensureStubInflate();
        this.mCheckBoxContainer.setOnClickListener(onClickListener);
    }

    @Override // android.widget.Checkable
    public void toggle() {
        ensureStubInflate();
        this.mCheckBox.toggle();
    }

    public CheckBox getCheckBox() {
        ensureStubInflate();
        return this.mCheckBox;
    }

    public void setIsSimilarBestImage(boolean z) {
        this.mIsSimilarBestImage = z;
        if (isInCheckMode()) {
            initCheckBoxAnimIfNeed();
            setSimilarMarkEnable(true);
        }
    }

    public void bindImage(String str, Uri uri, RequestOptions requestOptions) {
        bindImage(-1L, str, uri, DownloadType.MICRO, requestOptions, (RequestOptions) null);
    }

    public void bindImage(String str, Uri uri, RequestOptions requestOptions, RequestOptions requestOptions2) {
        bindImage(-1L, str, uri, DownloadType.MICRO, requestOptions, requestOptions2);
    }

    public void bindImage(long j, String str, Uri uri, RequestOptions requestOptions, RequestOptions requestOptions2, String str2) {
        bindImage(j, str, uri, DownloadType.MICRO, requestOptions, requestOptions2, str2);
    }

    public void bindImage(String str, Uri uri, DownloadType downloadType, RequestOptions requestOptions) {
        bindImage(-1L, str, uri, downloadType, requestOptions, (RequestOptions) null);
    }

    public void bindImage(long j, String str, Uri uri, DownloadType downloadType, RequestOptions requestOptions, RequestOptions requestOptions2) {
        BindImageHelper.bindImage(str, uri, downloadType, this.mImageView, requestOptions, requestOptions2);
    }

    public void bindImage(long j, String str, Uri uri, DownloadType downloadType, RequestOptions requestOptions, RequestOptions requestOptions2, String str2) {
        bindImage(j, str, uri, downloadType, requestOptions, requestOptions2, str2, true);
    }

    public void bindImage(long j, String str, Uri uri, DownloadType downloadType, RequestOptions requestOptions, RequestOptions requestOptions2, String str2, boolean z) {
        BindImageHelper.bindImage(str, uri, downloadType, this.mImageView, requestOptions, requestOptions2, str2, z);
    }

    public boolean setFullyDrawnCounter(FullyDrawnCounter fullyDrawnCounter) {
        if (this.mFullyDrawnCounter != fullyDrawnCounter) {
            this.mFullyDrawnCounter = fullyDrawnCounter;
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mFullyDrawnCounter != null) {
            Drawable drawable = this.mImageView.getDrawable();
            if (!(drawable instanceof BitmapDrawable) || (drawable instanceof Config$PlaceholderDrawable) || ((BitmapDrawable) drawable).getBitmap().getWidth() <= Config$ThumbConfig.get().sTinyTargetSize.getWidth()) {
                return;
            }
            this.mFullyDrawnCounter.arrive();
            this.mFullyDrawnCounter = null;
        }
    }

    public void recycle() {
        BindImageHelper.cancel(this.mImageView);
        FullyDrawnCounter fullyDrawnCounter = this.mFullyDrawnCounter;
        if (fullyDrawnCounter != null) {
            fullyDrawnCounter.arrive();
            this.mFullyDrawnCounter = null;
        }
    }

    public void bindIndicator(String str, long j, long j2) {
        int i;
        String str2 = "";
        int i2 = 0;
        if (BaseFileMimeUtil.isGifFromMimeType(str)) {
            i = R.drawable.type_indicator_gif;
        } else if (BaseFileMimeUtil.isRawFromMimeType(str)) {
            i = R.drawable.type_indicator_video_raw;
        } else if (BaseFileMimeUtil.isVideoFromMimeType(str)) {
            i = SpecialTypeMediaUtils.tryGetHFRIndicatorResId(j2);
            str2 = FormatUtil.formatVideoDuration(j);
            if (i == 0) {
                i = R.drawable.type_indicator_video;
            }
        } else if (SpecialTypeMediaUtils.isRefocusSupported(getContext(), j2)) {
            i = R.drawable.type_indicator_refocus;
        } else if (SpecialTypeMediaUtils.isMotionPhoto(getContext(), j2)) {
            i = R.drawable.type_indicator_motion_photo;
        } else if (SpecialTypeMediaUtils.isTimeBurstPhoto(j2)) {
            i = R.drawable.type_indicator_time_burst_photo;
        } else if (SpecialTypeMediaUtils.isBurstPhoto(j2)) {
            i = R.drawable.type_indicator_burst_photo;
        } else if (SpecialTypeMediaUtils.isDocPhoto(getContext(), j2)) {
            i = R.drawable.type_indicator_doc_photo;
        } else {
            str2 = null;
            i2 = 8;
            i = 0;
        }
        updateTypeIndicator(i2, str2, i);
    }

    public void bindFavoriteIndicator(boolean z) {
        updateFavoriteIndicatorVisibility((!z || isInCheckMode()) ? 8 : 0);
    }

    public void clearIndicator() {
        TextView textView = this.mTypeIndicator;
        if (textView != null) {
            textView.setVisibility(8);
        }
        View view = this.mFavoriteIndicator;
        if (view != null) {
            view.setVisibility(8);
        }
        updateBottomIndicatorBg();
        ImageView imageView = this.mSyncIndicator;
        if (imageView != null) {
            imageView.setVisibility(8);
        }
        updateTopIndicatorBg();
        setIsSimilarBestImage(false);
    }

    public void clearSimIndicatior() {
        setIsSimilarBestImage(false);
    }

    public final boolean isInCheckMode() {
        ValueAnimator valueAnimator;
        View view = this.mCheckBoxContainer;
        return view != null && view.getVisibility() == 0 && ((valueAnimator = this.mCheckBoxExitAnimator) == null || !valueAnimator.isRunning());
    }

    public final boolean needShowSyncIndicator(SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene) {
        if (syncStateDisplay$DisplayScene == SyncStateDisplay$DisplayScene.SCENE_ALWAYS) {
            return true;
        }
        return syncStateDisplay$DisplayScene == SyncStateDisplay$DisplayScene.SCENE_IN_CHECK_MODE && isInCheckMode();
    }

    public final boolean needShowSyncIndicator(int i, int i2) {
        if (i == 0 || i == 1) {
            return displaySyncedState(i2);
        }
        if (i == 2) {
            return displaySyncingState(i2);
        }
        if (i == 3) {
            return displaySyncableState(i2);
        }
        if (i == 4) {
            return displayUnSyncableState(i2);
        }
        return false;
    }

    public final boolean needRefreshSyncIndicator(long j, int i, int i2) {
        if (this.mLastSyncId != j) {
            this.mSyncState = Integer.MAX_VALUE;
            return true;
        } else if (this.mSyncState != i) {
            return true;
        } else {
            ImageView imageView = this.mSyncIndicator;
            return needShowSyncIndicator(i, i2) == ((imageView == null ? 8 : imageView.getVisibility()) != 0);
        }
    }

    public void bindSyncIndicator(long j, int i, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene) {
        bindSyncIndicator(j, i, syncStateDisplay$DisplayScene, 7);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0067, code lost:
        if (displaySyncableState(r12) != false) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void bindSyncIndicator(long r8, int r10, com.miui.gallery.adapter.SyncStateDisplay$DisplayScene r11, int r12) {
        /*
            r7 = this;
            boolean r11 = r7.needShowSyncIndicator(r11)
            r0 = 8
            if (r11 != 0) goto L1b
            r7.setSyncIndicatorVisibility(r0)
            android.widget.ImageView r8 = r7.mSyncIndicator
            if (r8 == 0) goto L1a
            android.view.animation.Animation r8 = r8.getAnimation()
            if (r8 == 0) goto L1a
            android.widget.ImageView r8 = r7.mSyncIndicator
            r8.clearAnimation()
        L1a:
            return
        L1b:
            boolean r11 = r7.needRefreshSyncIndicator(r8, r10, r12)
            if (r11 != 0) goto L22
            return
        L22:
            r11 = 2131233147(0x7f08097b, float:1.8082423E38)
            r1 = 2131233146(0x7f08097a, float:1.8082421E38)
            r2 = 2
            r3 = 0
            r4 = 0
            if (r10 == 0) goto L82
            r5 = 1
            if (r10 == r5) goto L82
            if (r10 == r2) goto L6a
            r1 = 3
            if (r10 == r1) goto L63
            r11 = 4
            if (r10 == r11) goto L59
            r11 = 2147483647(0x7fffffff, float:NaN)
            if (r10 != r11) goto L42
        L3d:
            r11 = r3
            r0 = r11
        L3f:
            r12 = r4
            goto Lab
        L42:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r11 = "unknow status: "
            r9.append(r11)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r8.<init>(r9)
            throw r8
        L59:
            boolean r11 = displayUnSyncableState(r12)
            if (r11 == 0) goto L80
            r11 = 2131233148(0x7f08097c, float:1.8082425E38)
            goto L89
        L63:
            boolean r12 = displaySyncableState(r12)
            if (r12 == 0) goto L80
            goto L89
        L6a:
            boolean r12 = displaySyncingState(r12)
            if (r12 == 0) goto L80
            android.content.Context r12 = r7.getContext()
            r0 = 2130772025(0x7f010039, float:1.7147157E38)
            android.view.animation.Animation r12 = android.view.animation.AnimationUtils.loadAnimation(r12, r0)
            r0 = r3
            r6 = r4
            r4 = r12
            r12 = r6
            goto Lab
        L80:
            r11 = r3
            goto L3f
        L82:
            boolean r11 = displaySyncedState(r12)
            if (r11 == 0) goto L8b
            r11 = r1
        L89:
            r0 = r3
            goto L3f
        L8b:
            int r11 = r7.mSyncState
            if (r11 == r2) goto L90
            goto L80
        L90:
            boolean r11 = displaySyncingState(r12)
            if (r11 == 0) goto L3d
            android.content.Context r11 = r7.getContext()
            r12 = 2130772024(0x7f010038, float:1.7147155E38)
            android.view.animation.Animation r4 = android.view.animation.AnimationUtils.loadAnimation(r11, r12)
            android.content.Context r11 = r7.getContext()
            android.view.animation.Animation r11 = android.view.animation.AnimationUtils.loadAnimation(r11, r12)
            r12 = r11
            r11 = r1
        Lab:
            r7.mLastSyncId = r8
            r7.mSyncState = r10
            r7.updateSyncIndicator(r0, r11, r4, r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.MicroThumbGridItem.bindSyncIndicator(long, int, com.miui.gallery.adapter.SyncStateDisplay$DisplayScene, int):void");
    }

    private void setSyncIndicatorVisibility(int i) {
        initCheckBoxAnimIfNeed();
        if (i == 0) {
            ensureStubInflate();
            this.mSyncIndicator.setVisibility(0);
            this.mSyncIndicatorEnterAnimator.setTarget(this.mSyncIndicator);
            this.mSyncIndicatorEnterAnimator.addListener(this.mSyncIndicatorEnterListener);
            this.mSyncIndicatorEnterAnimator.start();
        } else {
            ImageView imageView = this.mSyncIndicator;
            if (imageView != null) {
                if (imageView.getVisibility() == 8 || this.mSyncIndicator.getAlpha() == 0.0f || this.mSyncIndicatorExitAnimator.isRunning()) {
                    return;
                }
                this.mSyncIndicator.setVisibility(8);
                this.mSyncIndicatorExitAnimator.setTarget(this.mSyncIndicator);
                this.mSyncIndicatorExitAnimator.addListener(this.mSyncIndicatorExitListener);
                this.mSyncIndicatorExitAnimator.start();
            }
        }
        updateTopIndicatorBg();
    }

    public void updateTopIndicatorBg() {
        ImageView imageView = this.mSyncIndicator;
        if (imageView != null && imageView.getVisibility() == 0 && this.mSyncIndicator.getDrawable() != null) {
            ensureStubInflate();
            this.mTopIndicatorContainer.setVisibility(0);
            return;
        }
        View view = this.mTopIndicatorContainer;
        if (view == null) {
            return;
        }
        view.setVisibility(8);
    }

    public final void updateSyncIndicator(int i, int i2, Animation animation, Animation animation2) {
        if (this.mSyncIndicator != null || i2 != 0) {
            ensureStubInflate();
            this.mSyncIndicator.setImageResource(i2);
        }
        setSyncIndicatorVisibility(i);
        if (animation != null) {
            ensureStubInflate();
            this.mSyncIndicator.startAnimation(animation);
        } else {
            ImageView imageView = this.mSyncIndicator;
            if (imageView != null) {
                imageView.clearAnimation();
            }
        }
        if (animation2 != null) {
            ensureStubInflate();
            this.mTopIndicatorContainer.startAnimation(animation2);
            return;
        }
        View view = this.mTopIndicatorContainer;
        if (view == null) {
            return;
        }
        view.clearAnimation();
    }

    private void setTypeIndicatorVisibility(int i) {
        if (i == 0) {
            this.mTypeIndicator.setVisibility(0);
        } else {
            TextView textView = this.mTypeIndicator;
            if (textView != null) {
                textView.setVisibility(8);
            }
        }
        updateBottomIndicatorBg();
    }

    public void updateFavoriteIndicatorVisibility(int i) {
        if (this.mFavoriteIndicator != null || i == 0) {
            ensureBottomIndicatorInflated();
            if (i == 0) {
                this.mFavoriteIndicator.setVisibility(0);
            } else {
                View view = this.mFavoriteIndicator;
                if (view != null) {
                    view.setVisibility(8);
                }
            }
            updateBottomIndicatorBg();
        }
    }

    public void updateBottomIndicatorBg() {
        View view;
        TextView textView = this.mTypeIndicator;
        if ((textView != null && textView.getVisibility() == 0) || ((view = this.mFavoriteIndicator) != null && view.getVisibility() == 0)) {
            this.mBottomIndicatorContainer.setVisibility(0);
            return;
        }
        View view2 = this.mBottomIndicatorContainer;
        if (view2 == null) {
            return;
        }
        view2.setVisibility(8);
    }

    public void updateTypeIndicator(int i, String str, int i2) {
        if (this.mTypeIndicator != null || i == 0) {
            ensureBottomIndicatorInflated();
            if (i != 0 && i == this.mTypeIndicator.getVisibility()) {
                return;
            }
            String nullToEmpty = Strings.nullToEmpty(str);
            if (!TextUtils.equals(this.mTypeIndicator.getText(), nullToEmpty)) {
                this.mTypeIndicator.setText(nullToEmpty);
            }
            Drawable drawable = i2 != 0 ? getContext().getDrawable(i2) : null;
            if (this.mTypeIndicator.getCompoundDrawablesRelative()[0] != drawable) {
                this.mTypeIndicator.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            setTypeIndicatorVisibility(i);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (isSquareItem()) {
            super.onMeasure(i, i);
        } else {
            super.onMeasure(i, i2);
        }
    }

    public final boolean checkInflated(int i) {
        return (this.mInflateFlags & i) == i;
    }

    public final void markInflated(int i, boolean z) {
        if (z) {
            this.mInflateFlags = i | this.mInflateFlags;
            return;
        }
        this.mInflateFlags = (~i) & this.mInflateFlags;
    }

    public final void ensureBottomIndicatorInflated() {
        if (checkInflated(1)) {
            return;
        }
        View inflate = this.mBottomIndicatorStub.inflate();
        this.mTypeIndicator = (TextView) inflate.findViewById(R.id.type_indicator);
        this.mFavoriteIndicator = inflate.findViewById(R.id.favorites_indicator);
        this.mBottomIndicatorContainer = inflate.findViewById(R.id.bottom_indicator_container);
        markInflated(1, true);
    }

    public final void ensureStubInflate() {
        if (checkInflated(2)) {
            return;
        }
        View inflate = this.mItemStub.inflate();
        this.mMask = (ImageView) inflate.findViewById(R.id.mask);
        this.mCheckBox = (CheckBox) inflate.findViewById(16908289);
        this.mCheckBoxContainer = inflate.findViewById(R.id.checkbox_container);
        this.mSyncIndicator = (ImageView) inflate.findViewById(R.id.sync_indicator);
        this.mTopIndicatorContainer = inflate.findViewById(R.id.top_indicator_container);
        this.mPickingNumberIndicator = (TextView) inflate.findViewById(R.id.pick_num_indicator);
        this.mSimilarBestMark = (ImageView) inflate.findViewById(R.id.similar_best_mark);
        markInflated(2, true);
    }
}
