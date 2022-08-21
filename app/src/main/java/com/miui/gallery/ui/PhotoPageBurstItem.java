package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnScaleStateChangeListener;
import com.github.chrisbanes.photoview.OnViewSingleTapListener;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.PhotoPageBurstItem;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.ExifUtil;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class PhotoPageBurstItem extends PhotoPageImageBaseItem {
    public ShapeDrawable mDefaultDrawable;

    public static /* synthetic */ boolean $r8$lambda$dAfdbhZrpE1S2GlhyppT7mr5jfg(PhotoPageBurstItem photoPageBurstItem, View view, float f, float f2) {
        return photoPageBurstItem.lambda$onImageLoadFinish$0(view, f, f2);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean needRegionDecode() {
        return false;
    }

    public PhotoPageBurstItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void onImageLoadFinish(ErrorCode errorCode) {
        super.onImageLoadFinish(errorCode);
        this.mPhotoView.setInterceptTouch(false);
        this.mPhotoView.setOnViewSingleTapListener(new OnViewSingleTapListener() { // from class: com.miui.gallery.ui.PhotoPageBurstItem$$ExternalSyntheticLambda0
            @Override // com.github.chrisbanes.photoview.OnViewSingleTapListener
            public final boolean onViewSingleTap(View view, float f, float f2) {
                return PhotoPageBurstItem.$r8$lambda$dAfdbhZrpE1S2GlhyppT7mr5jfg(PhotoPageBurstItem.this, view, f, f2);
            }
        });
        this.mPhotoView.setScaleMinSpan(getContext().getResources().getDimensionPixelSize(R.dimen.scale_gesture_detector_min_spin));
        this.mPhotoView.setDisableDragDownOut(true);
        this.mPhotoView.setMediumScale(1.75f);
        this.mPhotoView.setMinimumScale(0.2f);
        this.mPhotoView.setOnScaleStateChangeListener(new AnonymousClass1());
    }

    public /* synthetic */ boolean lambda$onImageLoadFinish$0(View view, float f, float f2) {
        View view2;
        PhotoPageItem.CheckManager checkManager = this.mCheckManager;
        if (checkManager == null || (view2 = checkManager.mSelectLayout) == null) {
            return true;
        }
        view2.performClick();
        return true;
    }

    /* renamed from: com.miui.gallery.ui.PhotoPageBurstItem$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnScaleStateChangeListener {
        public static /* synthetic */ void $r8$lambda$fHPqeknWunnfkPr9VdyiIE0hheE(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$onScaleEnd$0();
        }

        public AnonymousClass1() {
            PhotoPageBurstItem.this = r1;
        }

        @Override // com.github.chrisbanes.photoview.OnScaleStateChangeListener
        public void onScaleStart() {
            View view;
            PhotoPageItem.CheckManager checkManager = PhotoPageBurstItem.this.mCheckManager;
            if (checkManager == null || (view = checkManager.mSelectLayout) == null) {
                return;
            }
            Folme.useAt(view).visible().hide(new AnimConfig[0]);
        }

        @Override // com.github.chrisbanes.photoview.OnScaleStateChangeListener
        public void onScaleEnd() {
            PhotoPageBurstItem.this.mPhotoView.setScale(1.0f, true);
            PhotoPageBurstItem.this.mPhotoView.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageBurstItem$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageBurstItem.AnonymousClass1.$r8$lambda$fHPqeknWunnfkPr9VdyiIE0hheE(PhotoPageBurstItem.AnonymousClass1.this);
                }
            }, 400L);
        }

        public /* synthetic */ void lambda$onScaleEnd$0() {
            View view;
            PhotoPageItem.CheckManager checkManager = PhotoPageBurstItem.this.mCheckManager;
            if (checkManager == null || (view = checkManager.mSelectLayout) == null) {
                return;
            }
            Folme.useAt(view).visible().show(new AnimConfig[0]);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void swapItem(BaseDataItem baseDataItem) {
        super.swapItem(baseDataItem);
        this.mPhotoView.setRotatable(false);
        this.mPhotoView.setScaleDragEnable(true);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.CheckManager createCheckManager() {
        return new BurstCheckManager();
    }

    /* loaded from: classes2.dex */
    public class BurstCheckManager extends PhotoPageItem.CheckManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BurstCheckManager() {
            super();
            PhotoPageBurstItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public boolean originChecked() {
            View view = this.mCheckRenderLayout;
            if (view != null) {
                view.setVisibility(4);
                this.mCheckRenderLayout.setImportantForAccessibility(2);
                return true;
            }
            return true;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void startCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            super.startCheck(choiceModeInterfaceArr);
            this.mCheckRoot.setOnClickListener(null);
            this.mCheckRoot.setClickable(false);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionModeChanged(boolean z) {
        super.onActionModeChanged(z);
        this.mPhotoView.setZoomable(true);
        this.mPhotoView.setRotatable(false);
    }

    public final void configDefaultDrawable(BaseDataItem baseDataItem) {
        float min;
        float height;
        int width;
        if (this.mDefaultDrawable != null || baseDataItem == null || baseDataItem.getWidth() <= 0 || baseDataItem.getHeight() <= 0) {
            return;
        }
        if (ExifUtil.isWidthHeightRotated(baseDataItem.getOrientation())) {
            min = Math.min(getResources().getDimensionPixelSize(R.dimen.burst_pager_item_max_width), ((getHeight() * 1.0f) * baseDataItem.getHeight()) / baseDataItem.getWidth());
            height = baseDataItem.getWidth() * min;
            width = baseDataItem.getHeight();
        } else {
            min = Math.min(getResources().getDimensionPixelSize(R.dimen.burst_pager_item_max_width), ((getHeight() * 1.0f) * baseDataItem.getWidth()) / baseDataItem.getHeight());
            height = baseDataItem.getHeight() * min;
            width = baseDataItem.getWidth();
        }
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        this.mDefaultDrawable = shapeDrawable;
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.burst_photo_default_color));
        this.mDefaultDrawable.getPaint().setAntiAlias(true);
        this.mDefaultDrawable.getPaint().setStyle(Paint.Style.FILL);
        this.mDefaultDrawable.setIntrinsicHeight((int) (height / width));
        this.mDefaultDrawable.setIntrinsicWidth((int) min);
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public RequestOptions getRequestOptions(BaseDataItem baseDataItem, boolean z) {
        RequestOptions requestOptions = super.getRequestOptions(baseDataItem, z);
        if (baseDataItem != null) {
            configDefaultDrawable(baseDataItem);
        }
        ShapeDrawable shapeDrawable = this.mDefaultDrawable;
        return shapeDrawable != null ? requestOptions.mo973placeholder(shapeDrawable) : requestOptions;
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        super.doRelease();
    }
}
