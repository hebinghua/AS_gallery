package com.miui.gallery.magic.widget.portrait;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.widget.DoodleView;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;

/* loaded from: classes2.dex */
public class PortraitForegroundView extends BitmapGestureView {
    public AnimConfig mAlphaHideAnimConfig;
    public AnimConfig mAlphaShowAnimConfig;
    public Matrix mMatrix;
    public Bitmap mOriginBitmap;
    public MattingInvoker.SegmentResult mSegmentResult;
    public IStateStyle mStateStyle;
    public int viewAlpha;

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public PortraitForegroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAlphaShowAnimConfig = new AnimConfig().setEase(18, 1000.0f);
        this.mAlphaHideAnimConfig = new AnimConfig().setEase(18, 1300.0f);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public void updateMatrixWithWidthAndHeight(Matrix matrix, float f, float f2) {
        this.mMatrix = matrix;
        invalidate();
    }

    public void addPersonForegroundToView(MattingInvoker.SegmentResult segmentResult, Bitmap bitmap) {
        this.mSegmentResult = segmentResult;
        this.mOriginBitmap = bitmap;
        if (segmentResult == null || this.mMatrix == null) {
            return;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), this.mOriginBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawColor(0);
        for (int i = 0; i < this.mSegmentResult.getPersonCount(); i++) {
            this.mSegmentResult.drawPerson(this.mOriginBitmap, i, createBitmap, 0, 0, DoodleView.MASK_COLOR);
        }
        setBitmap(createBitmap);
        shinePeople();
    }

    public final void shinePeople() {
        IStateStyle iStateStyle = this.mStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        this.mStateStyle = Folme.useValue(new Object[0]).addListener(new TransitionListener() { // from class: com.miui.gallery.magic.widget.portrait.PortraitForegroundView.1
            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                UpdateInfo findByName = UpdateInfo.findByName(collection, "alpha");
                if (findByName != null) {
                    PortraitForegroundView.this.viewAlpha = findByName.getIntValue();
                    PortraitForegroundView.this.updateViewAlpha(PortraitForegroundView.this.viewAlpha / 179.0f);
                    PortraitForegroundView.this.invalidate();
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (obj.toString().equals("hide")) {
                    PortraitForegroundView.this.hintView();
                }
                PortraitForegroundView.this.invalidate();
            }
        }).set("start").add("alpha", 0).set("show").add("alpha", 179).set("hide").add("alpha", 0).setTo("start").to("show", this.mAlphaShowAnimConfig).then("hide", this.mAlphaHideAnimConfig);
    }

    public final void updateViewAlpha(float f) {
        setAlpha(f);
    }

    public final void hintView() {
        setVisibility(8);
    }
}
