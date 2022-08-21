package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class DownloadView extends LinearLayout {
    public ImageView mImageView;
    public ObjectAnimator mRotationAnimal;
    public TextView mTextView;

    public DownloadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public final void initView(Context context) {
        LinearLayout.inflate(context, R.layout.photo_editor_text_download_view, this);
        this.mTextView = (TextView) findViewById(R.id.text_item);
        this.mImageView = (ImageView) findViewById(R.id.img_item);
    }

    public final void doDownloading() {
        this.mImageView.setImageResource(R.drawable.editor_downloading_old);
        rotation();
    }

    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }

    public final void endDownloading() {
        hide(this.mImageView, new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DownloadView downloadView = DownloadView.this;
                downloadView.show(downloadView.mImageView, new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadView.1.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator2) {
                        super.onAnimationEnd(animator2);
                        DownloadView downloadView2 = DownloadView.this;
                        downloadView2.hide(downloadView2, null);
                        DownloadView.this.mTextView.setText("");
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator2) {
                        super.onAnimationStart(animator2);
                        DownloadView.this.mImageView.setImageResource(R.drawable.editor_downloaded_old);
                    }
                });
            }
        });
    }

    public void clearAnimator() {
        ObjectAnimator objectAnimator = this.mRotationAnimal;
        if (objectAnimator != null) {
            objectAnimator.end();
            this.mRotationAnimal = null;
        }
        this.mImageView.animate().cancel();
    }

    public void setStateImage(int i) {
        clearAnimator();
        if (i != 0) {
            switch (i) {
                case 17:
                    setVisibility(8);
                    return;
                case 18:
                    setVisibility(0);
                    doDownloading();
                    return;
                case 19:
                case 20:
                    setVisibility(0);
                    this.mImageView.setImageResource(R.drawable.editor_download_old);
                    return;
                default:
                    return;
            }
        }
        setVisibility(0);
        endDownloading();
    }

    public final void show(View view, AnimatorListenerAdapter animatorListenerAdapter) {
        view.setAlpha(0.0f);
        view.setScaleX(0.6f);
        view.setScaleY(0.6f);
        view.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(350L).setListener(animatorListenerAdapter).setInterpolator(new CubicEaseOutInterpolator());
    }

    public final void hide(View view, AnimatorListenerAdapter animatorListenerAdapter) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.animate().alpha(0.0f).scaleX(0.6f).scaleY(0.6f).setDuration(250L).setListener(animatorListenerAdapter).setInterpolator(new CubicEaseOutInterpolator());
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
