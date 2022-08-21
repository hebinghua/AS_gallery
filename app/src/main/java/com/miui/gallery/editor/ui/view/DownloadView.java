package com.miui.gallery.editor.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.editor.R$drawable;
import com.miui.gallery.editor.R$styleable;

/* loaded from: classes2.dex */
public class DownloadView extends RelativeLayout {
    public int mDownloadedDrawableId;
    public int mDownloadingDrawableId;
    public ImageView mImageView;
    public ObjectAnimator mRotationAnimal;
    public int mToDownloadDrawableId;

    public DownloadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(attributeSet);
    }

    public final void initView(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.DownloadView);
        this.mDownloadingDrawableId = obtainStyledAttributes.getResourceId(R$styleable.DownloadView_downloadingDrawable, R$drawable.editor_downloading);
        this.mDownloadedDrawableId = obtainStyledAttributes.getResourceId(R$styleable.DownloadView_downloadedDrawable, R$drawable.editor_downloaded);
        this.mToDownloadDrawableId = obtainStyledAttributes.getResourceId(R$styleable.DownloadView_toDownloadDrawable, R$drawable.editor_to_download);
        obtainStyledAttributes.recycle();
        ImageView imageView = new ImageView(getContext());
        this.mImageView = imageView;
        addView(imageView, -2, -2);
        ((RelativeLayout.LayoutParams) this.mImageView.getLayoutParams()).addRule(13);
    }

    public final void doDownloading() {
        this.mImageView.setImageResource(this.mDownloadingDrawableId);
        rotation();
    }

    @Override // android.view.View
    public void clearAnimation() {
        super.clearAnimation();
        ObjectAnimator objectAnimator = this.mRotationAnimal;
        if (objectAnimator != null) {
            objectAnimator.end();
            this.mRotationAnimal = null;
        }
        this.mImageView.clearAnimation();
        setAlpha(1.0f);
        setScaleX(1.0f);
        setScaleY(1.0f);
    }

    public void setStateImage(int i) {
        clearAnimation();
        if (i != 0) {
            switch (i) {
                case 17:
                    break;
                case 18:
                    setVisibility(0);
                    doDownloading();
                    return;
                case 19:
                case 20:
                    setVisibility(0);
                    this.mImageView.setImageResource(this.mToDownloadDrawableId);
                    return;
                default:
                    return;
            }
        }
        setVisibility(8);
    }

    public final void rotation() {
        ObjectAnimator objectAnimator = this.mRotationAnimal;
        if (objectAnimator != null) {
            objectAnimator.end();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mImageView, MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, 0.0f, 360.0f);
        this.mRotationAnimal = ofFloat;
        ofFloat.setRepeatCount(-1);
        this.mRotationAnimal.setInterpolator(new LinearInterpolator());
        this.mRotationAnimal.setDuration(1000L);
        this.mRotationAnimal.start();
    }
}
