package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.widget.ViewUtils;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class HeaderTransitionRender extends BaseTransitionRender {
    public float mAnimScale;
    public final float mFromScale;
    public WeakReference<View> mHostViewRef;
    public final boolean mIsSticky;
    public final boolean mIsUnitScale;
    public boolean mIsVisible;
    public int mLayerType;
    public Matrix mMatrix;
    public final float mToScale;
    public RecyclerView.ViewHolder mViewHolder;
    public final float mVisibleEnd;
    public final float mVisibleStart;

    public HeaderTransitionRender(Builder builder) {
        super(builder.mFromFrame, builder.mToFrame, builder.mFromAlpha, builder.mToAlpha, builder.mAlphaInterpolator);
        boolean z = false;
        this.mLayerType = 0;
        this.mAnimScale = 1.0f;
        float f = builder.mFromScale;
        this.mFromScale = f;
        float f2 = builder.mToScale;
        this.mToScale = f2;
        this.mViewHolder = builder.mViewHolder;
        this.mIsSticky = builder.mIsSticky;
        if (!(builder.mAlphaInterpolator instanceof NarrowedLinearInterpolator)) {
            this.mVisibleStart = 0.0f;
            this.mVisibleEnd = 1.0f;
        } else {
            this.mVisibleStart = ((NarrowedLinearInterpolator) builder.mAlphaInterpolator).getStart();
            this.mVisibleEnd = ((NarrowedLinearInterpolator) builder.mAlphaInterpolator).getEnd();
        }
        if (f == f2 && f == 1.0f) {
            z = true;
        }
        this.mIsUnitScale = z;
        this.mHostViewRef = new WeakReference<>(builder.mHost);
        this.mMatrix = new Matrix();
    }

    public final RectF determineViewBounds() {
        RectF fromFrame = getFromFrame();
        RectF toFrame = getToFrame();
        return fromFrame.height() > toFrame.height() ? fromFrame : toFrame;
    }

    public final void createAndSetupView() {
        RectF determineViewBounds = determineViewBounds();
        this.mViewHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(Math.round(determineViewBounds.width()), 1073741824), View.MeasureSpec.makeMeasureSpec(Math.round(determineViewBounds.height()), 1073741824));
        View view = this.mViewHolder.itemView;
        view.layout(0, 0, view.getMeasuredWidth(), this.mViewHolder.itemView.getMeasuredHeight());
        this.mLayerType = this.mViewHolder.itemView.getLayerType();
        this.mViewHolder.itemView.setLayerType(2, null);
    }

    public final void restoreView() {
        View view = this.mViewHolder.itemView;
        if (view instanceof ITransitionalView) {
            ((ITransitionalView) view).updateBackgroundAlpha(255);
            ((ITransitionalView) this.mViewHolder.itemView).updateContentAlpha(1.0f);
        }
        this.mViewHolder.itemView.setLayerType(this.mLayerType, null);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public void onPreTransition() {
        if (this.mViewHolder != null) {
            createAndSetupView();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public void onPostTransition() {
        if (this.mViewHolder != null) {
            restoreView();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public boolean skipDraw() {
        return this.mViewHolder == null;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void onPreDraw(RectF rectF, int i, float f) {
        View view = this.mViewHolder.itemView;
        if (view instanceof ITransitionalView) {
            ((ITransitionalView) view).updateBackgroundAlpha(this.mIsVisible ? 255 : 0);
            ((ITransitionalView) this.mViewHolder.itemView).updateContentAlpha(this.mIsVisible ? i / 255.0f : 0.0f);
        }
        updateMatrix(rectF);
    }

    public final void updateMatrix(RectF rectF) {
        this.mMatrix.reset();
        boolean z = this.mHostViewRef.get() != null && ViewUtils.isLayoutRtl(this.mHostViewRef.get());
        RectF temp1 = getTemp1();
        temp1.set(this.mViewHolder.itemView.getLeft(), this.mViewHolder.itemView.getTop(), this.mViewHolder.itemView.getRight(), this.mViewHolder.itemView.getBottom());
        float f = 0.0f;
        if (z) {
            f = rectF.width() - (temp1.width() * this.mAnimScale);
        }
        RectF temp2 = getTemp2();
        temp2.set(temp1);
        BaseMiscUtil.scaleRectF(temp2, this.mAnimScale);
        this.mMatrix.setRectToRect(temp1, temp2, Matrix.ScaleToFit.FILL);
        this.mMatrix.postTranslate(f, (rectF.height() - (temp1.height() * this.mAnimScale)) * 0.5f);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void onDraw(Canvas canvas, RectF rectF) {
        int save = canvas.save();
        canvas.translate(rectF.left, rectF.top);
        canvas.clipRect(0.0f, 0.0f, rectF.width(), rectF.height());
        canvas.concat(this.mMatrix);
        this.mViewHolder.itemView.draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void evaluateAlpha(float f, float f2) {
        int i = this.mFromAlpha;
        int i2 = this.mToAlpha;
        if (i == i2) {
            this.mAnimAlpha = i;
        } else {
            this.mAnimAlpha = MathUtils.clamp(i + ((int) ((i2 - i) * this.mAlphaInterpolator.getInterpolation(f))), 0, 255);
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void evaluateCustomValues(float f) {
        float f2;
        if (this.mIsUnitScale) {
            f2 = 1.0f;
        } else {
            float f3 = this.mFromScale;
            f2 = f3 + ((this.mToScale - f3) * f);
        }
        this.mAnimScale = f2;
        this.mIsVisible = Float.compare(f, this.mVisibleStart) >= 0 && Float.compare(f, this.mVisibleEnd) <= 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public int sortFactor() {
        return this.mIsSticky ? 2 : 1;
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        public Interpolator mAlphaInterpolator;
        public RectF mFromFrame;
        public View mHost;
        public boolean mIsSticky;
        public int mToAlpha;
        public RectF mToFrame;
        public RecyclerView.ViewHolder mViewHolder;
        public int mFromAlpha = 255;
        public float mFromScale = 1.0f;
        public float mToScale = 1.0f;

        public Builder(View view) {
            this.mHost = view;
        }

        public Builder setViewHolder(RecyclerView.ViewHolder viewHolder) {
            this.mViewHolder = viewHolder;
            return this;
        }

        public Builder setFromFrame(RectF rectF) {
            this.mFromFrame = rectF;
            return this;
        }

        public Builder setToFrame(RectF rectF) {
            this.mToFrame = rectF;
            return this;
        }

        public Builder setFromAlpha(int i) {
            this.mFromAlpha = i;
            return this;
        }

        public Builder setToAlpha(int i) {
            this.mToAlpha = i;
            return this;
        }

        public Builder isSticky(boolean z) {
            this.mIsSticky = z;
            return this;
        }

        public Builder setAlphaInterpolator(Interpolator interpolator) {
            this.mAlphaInterpolator = interpolator;
            return this;
        }

        public HeaderTransitionRender build() {
            return new HeaderTransitionRender(this);
        }
    }
}
