package miuix.smooth;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.io.IOException;
import miuix.smooth.internal.SmoothDrawHelper;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class SmoothContainerDrawable extends Drawable implements Drawable.Callback {
    public static final PorterDuffXfermode XFERMODE_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    public ContainerState mContainerState;
    public SmoothDrawHelper mHelper;

    public SmoothContainerDrawable() {
        this.mHelper = new SmoothDrawHelper();
        this.mContainerState = new ContainerState();
    }

    private SmoothContainerDrawable(Resources resources, Resources.Theme theme, ContainerState containerState) {
        this.mHelper = new SmoothDrawHelper();
        this.mContainerState = new ContainerState(containerState, this, resources, theme);
        this.mHelper.setStrokeWidth(containerState.mStrokeWidth);
        this.mHelper.setStrokeColor(containerState.mStrokeColor);
        this.mHelper.setRadii(containerState.mRadii);
        this.mHelper.setRadius(containerState.mRadius);
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws IOException, XmlPullParserException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet, R$styleable.MiuixSmoothContainerDrawable);
        setCornerRadius(obtainAttributes.getDimensionPixelSize(R$styleable.MiuixSmoothContainerDrawable_android_radius, 0));
        int i = R$styleable.MiuixSmoothContainerDrawable_android_topLeftRadius;
        if (obtainAttributes.hasValue(i) || obtainAttributes.hasValue(R$styleable.MiuixSmoothContainerDrawable_android_topRightRadius) || obtainAttributes.hasValue(R$styleable.MiuixSmoothContainerDrawable_android_bottomRightRadius) || obtainAttributes.hasValue(R$styleable.MiuixSmoothContainerDrawable_android_bottomLeftRadius)) {
            float dimensionPixelSize = obtainAttributes.getDimensionPixelSize(i, 0);
            float dimensionPixelSize2 = obtainAttributes.getDimensionPixelSize(R$styleable.MiuixSmoothContainerDrawable_android_topRightRadius, 0);
            float dimensionPixelSize3 = obtainAttributes.getDimensionPixelSize(R$styleable.MiuixSmoothContainerDrawable_android_bottomRightRadius, 0);
            float dimensionPixelSize4 = obtainAttributes.getDimensionPixelSize(R$styleable.MiuixSmoothContainerDrawable_android_bottomLeftRadius, 0);
            setCornerRadii(new float[]{dimensionPixelSize, dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize4});
        }
        setStrokeWidth(obtainAttributes.getDimensionPixelSize(R$styleable.MiuixSmoothContainerDrawable_miuix_strokeWidth, 0));
        setStrokeColor(obtainAttributes.getColor(R$styleable.MiuixSmoothContainerDrawable_miuix_strokeColor, 0));
        setLayerType(obtainAttributes.getInt(R$styleable.MiuixSmoothContainerDrawable_android_layerType, 0));
        obtainAttributes.recycle();
        inflateInnerDrawable(resources, xmlPullParser, attributeSet, theme);
    }

    public final void inflateInnerDrawable(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws IOException, XmlPullParserException {
        int next;
        int depth = xmlPullParser.getDepth() + 1;
        while (true) {
            int next2 = xmlPullParser.next();
            if (next2 != 1) {
                int depth2 = xmlPullParser.getDepth();
                if (depth2 < depth && next2 == 3) {
                    return;
                }
                if (next2 == 2 && depth2 <= depth && xmlPullParser.getName().equals(MapBundleKey.OfflineMapKey.OFFLINE_CHILD)) {
                    do {
                        next = xmlPullParser.next();
                    } while (next == 4);
                    if (next != 2) {
                        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <child> tag requires a 'drawable' attribute or child tag defining a drawable");
                    }
                    ChildDrawable childDrawable = new ChildDrawable();
                    if (Build.VERSION.SDK_INT >= 21) {
                        childDrawable.mDrawable = Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet, theme);
                    } else {
                        childDrawable.mDrawable = Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet);
                    }
                    childDrawable.mDrawable.setCallback(this);
                    this.mContainerState.mChildDrawable = childDrawable;
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mContainerState.mChildDrawable.mDrawable.applyTheme(theme);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        ContainerState containerState = this.mContainerState;
        return (containerState != null && containerState.canApplyTheme()) || super.canApplyTheme();
    }

    public void setChildDrawable(Drawable drawable) {
        if (this.mContainerState != null) {
            ChildDrawable childDrawable = new ChildDrawable();
            childDrawable.mDrawable = drawable;
            drawable.setCallback(this);
            this.mContainerState.mChildDrawable = childDrawable;
        }
    }

    public Drawable getChildDrawable() {
        ContainerState containerState = this.mContainerState;
        if (containerState != null) {
            return containerState.mChildDrawable.mDrawable;
        }
        return null;
    }

    public void setStrokeWidth(int i) {
        ContainerState containerState = this.mContainerState;
        if (containerState.mStrokeWidth != i) {
            containerState.mStrokeWidth = i;
            this.mHelper.setStrokeWidth(i);
            invalidateSelf();
        }
    }

    public void setStrokeColor(int i) {
        ContainerState containerState = this.mContainerState;
        if (containerState.mStrokeColor != i) {
            containerState.mStrokeColor = i;
            this.mHelper.setStrokeColor(i);
            invalidateSelf();
        }
    }

    public void setCornerRadii(float[] fArr) {
        this.mContainerState.mRadii = fArr;
        this.mHelper.setRadii(fArr);
        if (fArr == null) {
            this.mContainerState.mRadius = 0.0f;
            this.mHelper.setRadius(0.0f);
        }
        invalidateSelf();
    }

    public void setCornerRadius(float f) {
        if (Float.isNaN(f)) {
            return;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        ContainerState containerState = this.mContainerState;
        containerState.mRadius = f;
        containerState.mRadii = null;
        this.mHelper.setRadius(f);
        this.mHelper.setRadii(null);
        invalidateSelf();
    }

    public float getCornerRadius() {
        return this.mContainerState.mRadius;
    }

    public void setLayerType(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("Layer type can only be one of: LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE");
        }
        ContainerState containerState = this.mContainerState;
        if (containerState.mLayerType == i) {
            return;
        }
        containerState.mLayerType = i;
        invalidateSelf();
    }

    public int getLayerType() {
        return this.mContainerState.mLayerType;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            outline.setPath(this.mHelper.getSmoothPath(getBoundsInner()));
        } else if (i < 21) {
        } else {
            outline.setRoundRect(getBoundsInner(), getCornerRadius());
        }
    }

    public final TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] iArr) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, iArr);
        }
        return theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mContainerState.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mContainerState.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        this.mContainerState.onBoundsChange(rect);
        this.mHelper.onBoundsChange(rect);
    }

    public final Rect getBoundsInner() {
        return this.mContainerState.getBounds();
    }

    @Override // android.graphics.drawable.Drawable
    public Rect getDirtyBounds() {
        return this.mContainerState.getDirtyBounds();
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int i) {
        this.mContainerState.setChangingConfigurations(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z) {
        this.mContainerState.setDither(z);
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z) {
        this.mContainerState.setFilterBitmap(z);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        return this.mContainerState.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int saveLayer = getLayerType() != 2 ? canvas.saveLayer(getBoundsInner().left, getBoundsInner().top, getBoundsInner().right, getBoundsInner().bottom, null, 31) : -1;
        this.mContainerState.mChildDrawable.mDrawable.draw(canvas);
        this.mHelper.drawMask(canvas, XFERMODE_DST_OUT);
        if (getLayerType() != 2) {
            canvas.restoreToCount(saveLayer);
        }
        this.mHelper.drawStroke(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mContainerState.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mContainerState.setAlpha(i);
        this.mHelper.setAlpha(i);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mContainerState.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mContainerState.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mContainerState;
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.mContainerState.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        return this.mContainerState.onStateChange(iArr);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mContainerState.isStateful();
    }

    /* loaded from: classes3.dex */
    public static class ChildDrawable {
        public Drawable mDrawable;

        public ChildDrawable() {
        }

        public ChildDrawable(ChildDrawable childDrawable, SmoothContainerDrawable smoothContainerDrawable, Resources resources, Resources.Theme theme) {
            Drawable drawable;
            Drawable drawable2 = childDrawable.mDrawable;
            if (drawable2 != null) {
                Drawable.ConstantState constantState = drawable2.getConstantState();
                if (constantState == null) {
                    drawable = drawable2;
                } else if (resources == null) {
                    drawable = constantState.newDrawable();
                } else if (theme == null) {
                    drawable = constantState.newDrawable(resources);
                } else if (Build.VERSION.SDK_INT >= 21) {
                    drawable = constantState.newDrawable(resources, theme);
                } else {
                    drawable = constantState.newDrawable(resources);
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    drawable.setLayoutDirection(drawable2.getLayoutDirection());
                }
                drawable.setBounds(drawable2.getBounds());
                drawable.setLevel(drawable2.getLevel());
                drawable.setCallback(smoothContainerDrawable);
            } else {
                drawable = null;
            }
            this.mDrawable = drawable;
        }
    }

    /* loaded from: classes3.dex */
    public static final class ContainerState extends Drawable.ConstantState {
        public ChildDrawable mChildDrawable;
        public int mLayerType;
        public float[] mRadii;
        public float mRadius;
        public int mStrokeColor;
        public int mStrokeWidth;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return true;
        }

        public ContainerState() {
            this.mLayerType = 0;
            this.mChildDrawable = new ChildDrawable();
        }

        public ContainerState(ContainerState containerState, SmoothContainerDrawable smoothContainerDrawable, Resources resources, Resources.Theme theme) {
            this.mLayerType = 0;
            this.mChildDrawable = new ChildDrawable(containerState.mChildDrawable, smoothContainerDrawable, resources, theme);
            this.mRadius = containerState.mRadius;
            this.mRadii = containerState.mRadii;
            this.mStrokeWidth = containerState.mStrokeWidth;
            this.mStrokeColor = containerState.mStrokeColor;
            this.mLayerType = containerState.mLayerType;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new SmoothContainerDrawable(null, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new SmoothContainerDrawable(resources, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            return new SmoothContainerDrawable(resources, theme, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChildDrawable.mDrawable.getChangingConfigurations();
        }

        public final boolean isStateful() {
            return this.mChildDrawable.mDrawable.isStateful();
        }

        public boolean onStateChange(int[] iArr) {
            return isStateful() && this.mChildDrawable.mDrawable.setState(iArr);
        }

        public int getIntrinsicWidth() {
            return this.mChildDrawable.mDrawable.getIntrinsicWidth();
        }

        public int getIntrinsicHeight() {
            return this.mChildDrawable.mDrawable.getIntrinsicHeight();
        }

        public void onBoundsChange(Rect rect) {
            this.mChildDrawable.mDrawable.setBounds(rect);
        }

        public void jumpToCurrentState() {
            this.mChildDrawable.mDrawable.jumpToCurrentState();
        }

        public int getOpacity() {
            return this.mChildDrawable.mDrawable.getOpacity();
        }

        public void setAlpha(int i) {
            this.mChildDrawable.mDrawable.setAlpha(i);
            this.mChildDrawable.mDrawable.invalidateSelf();
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mChildDrawable.mDrawable.setColorFilter(colorFilter);
        }

        public Rect getBounds() {
            return this.mChildDrawable.mDrawable.getBounds();
        }

        public Rect getDirtyBounds() {
            return this.mChildDrawable.mDrawable.getDirtyBounds();
        }

        public void setChangingConfigurations(int i) {
            this.mChildDrawable.mDrawable.setChangingConfigurations(i);
        }

        public void setDither(boolean z) {
            this.mChildDrawable.mDrawable.setDither(z);
        }

        public void setFilterBitmap(boolean z) {
            this.mChildDrawable.mDrawable.setFilterBitmap(z);
        }

        public int getAlpha() {
            return this.mChildDrawable.mDrawable.getAlpha();
        }

        public boolean getPadding(Rect rect) {
            return this.mChildDrawable.mDrawable.getPadding(rect);
        }
    }
}
