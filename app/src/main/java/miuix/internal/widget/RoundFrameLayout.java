package miuix.internal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.Arrays;
import miuix.appcompat.R$dimen;

/* loaded from: classes3.dex */
public class RoundFrameLayout extends FrameLayout {
    public Region mAreaRegion;
    public Path mClipOutPath;
    public Path mClipPath;
    public boolean mEnableSmoothRound;
    public RectF mLayer;
    public Paint mPaint;
    public float[] mRadii;

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mEnableSmoothRound = false;
        init();
    }

    public final void init() {
        float dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_immersion_menu_background_radius);
        this.mRadii = new float[]{dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize};
        this.mLayer = new RectF();
        this.mClipPath = new Path();
        this.mClipOutPath = new Path();
        this.mAreaRegion = new Region();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(-1);
        this.mPaint.setAntiAlias(true);
    }

    public void setRadius(float f) {
        setRadius(new float[]{f, f, f, f, f, f, f, f});
    }

    public void setRadius(float[] fArr) {
        if (!Arrays.equals(this.mRadii, fArr)) {
            this.mRadii = fArr;
            invalidate();
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mLayer.set(0.0f, 0.0f, i, i2);
        refreshRegion();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mEnableSmoothRound) {
            int saveLayer = canvas.saveLayer(this.mLayer, null, 31);
            super.dispatchDraw(canvas);
            onClipDraw(canvas);
            canvas.restoreToCount(saveLayer);
            return;
        }
        onClipDraw(canvas);
        super.dispatchDraw(canvas);
    }

    public final void refreshRegion() {
        if (this.mRadii == null) {
            return;
        }
        RectF rectF = new RectF();
        rectF.left = getPaddingLeft();
        rectF.top = getPaddingTop();
        rectF.right = ((int) this.mLayer.width()) - getPaddingRight();
        rectF.bottom = ((int) this.mLayer.height()) - getPaddingBottom();
        this.mClipPath.reset();
        this.mClipPath.addRoundRect(rectF, this.mRadii, Path.Direction.CW);
        this.mAreaRegion.setPath(this.mClipPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        this.mClipOutPath.reset();
        this.mClipOutPath.addRect(0.0f, 0.0f, (int) this.mLayer.width(), (int) this.mLayer.height(), Path.Direction.CW);
        this.mClipOutPath.op(this.mClipPath, Path.Op.DIFFERENCE);
    }

    public void onClipDraw(Canvas canvas) {
        if (this.mRadii == null) {
            return;
        }
        if (this.mEnableSmoothRound) {
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(this.mClipOutPath, this.mPaint);
            return;
        }
        canvas.clipPath(this.mClipPath);
    }
}
