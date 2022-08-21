package com.miui.gallery.editor.photo.widgets.seekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ColorPicker extends SeekBar {
    public static int[] COLORS = {-16747777, -384518, -53457, -35801, -16384, -13964758, -15998977};
    public static int MAX_LEVEL = 10000;
    public Delegator mDelegator;
    public CircleDrawable mThumbDrawable;

    public ColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        this.mThumbDrawable = new CircleDrawable(getContext().getResources().getDimensionPixelSize(R.dimen.editor_seek_bar_progress_thumb_start), getResources());
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.custom_seekbar_thumb_size);
        this.mThumbDrawable.setIntrinsicWidth(dimensionPixelSize);
        this.mThumbDrawable.setIntrinsicHeight(dimensionPixelSize);
        setThumb(this.mThumbDrawable);
        setLayerType(2, null);
    }

    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        super.setProgressDrawable(drawable);
        if (drawable instanceof BitmapDrawable) {
            this.mDelegator = new BitmapDelegator((BitmapDrawable) drawable);
        } else if (!(drawable instanceof ColorGradientDrawable)) {
        } else {
            this.mDelegator = new GradientDelegator((ColorGradientDrawable) drawable);
        }
    }

    public int getColor() {
        return this.mDelegator.getColor();
    }

    public void setThumbColor(int i) {
        this.mThumbDrawable.setColor(i);
    }

    public int findProgressByColor(int i) {
        return (this.mDelegator.findLevel(i) * getMax()) / MAX_LEVEL;
    }

    /* loaded from: classes2.dex */
    public static abstract class Delegator<D extends Drawable> {
        public D mDrawable;

        public abstract int findLevel(int i);

        public abstract int getColor();

        public Delegator(D d) {
            this.mDrawable = d;
        }
    }

    /* loaded from: classes2.dex */
    public static class BitmapDelegator extends Delegator<BitmapDrawable> {
        public Bitmap mBitmap;

        public BitmapDelegator(BitmapDrawable bitmapDrawable) {
            super(bitmapDrawable);
            this.mBitmap = bitmapDrawable.getBitmap();
        }

        @Override // com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.Delegator
        public int getColor() {
            Bitmap bitmap = this.mBitmap;
            return bitmap.getPixel((int) ((this.mBitmap.getWidth() / ColorPicker.MAX_LEVEL) * ((BitmapDrawable) this.mDrawable).getLevel()), bitmap.getHeight() / 2);
        }

        @Override // com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.Delegator
        public int findLevel(int i) {
            for (int i2 = 0; i2 < this.mBitmap.getWidth(); i2++) {
                Bitmap bitmap = this.mBitmap;
                if (bitmap.getPixel(i2, bitmap.getHeight() / 2) == i) {
                    return (int) ((i2 / this.mBitmap.getWidth()) * ColorPicker.MAX_LEVEL);
                }
            }
            return -1;
        }
    }

    /* loaded from: classes2.dex */
    public static class GradientDelegator extends Delegator<ColorGradientDrawable> {
        public int[] mColors;

        public final float getValueProgress(int i, int i2, int i3) {
            if (i3 == i2) {
                return 0.0f;
            }
            return (i - i2) / (i3 - i2);
        }

        public final boolean isValueInside(int i, int i2, int i3) {
            return (i >= i2 && i <= i3) || (i >= i3 && i <= i2);
        }

        public GradientDelegator(ColorGradientDrawable colorGradientDrawable) {
            super(colorGradientDrawable);
            this.mColors = colorGradientDrawable.getColors();
        }

        @Override // com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.Delegator
        public int getColor() {
            int level = ((ColorGradientDrawable) this.mDrawable).getLevel();
            if (level == ColorPicker.MAX_LEVEL) {
                return -1;
            }
            if (level == 0) {
                return -16777216;
            }
            float f = level / ColorPicker.MAX_LEVEL;
            if (f <= 0.0f) {
                return this.mColors[0];
            }
            if (f >= 1.0f) {
                int[] iArr = this.mColors;
                return iArr[iArr.length - 1];
            }
            int[] iArr2 = this.mColors;
            float length = f * (iArr2.length - 1);
            int i = (int) length;
            float f2 = length - i;
            int i2 = iArr2[i];
            int i3 = iArr2[i + 1];
            return Color.argb(ave(Color.alpha(i2), Color.alpha(i3), f2), ave(Color.red(i2), Color.red(i3), f2), ave(Color.green(i2), Color.green(i3), f2), ave(Color.blue(i2), Color.blue(i3), f2));
        }

        public final int ave(int i, int i2, float f) {
            return i + Math.round(f * (i2 - i));
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x0090, code lost:
            if (r8 != r2) goto L27;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x0092, code lost:
            return 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x0094, code lost:
            r2 = r16.mColors;
            r1 = findColorProgress(r17, r2[r8], r2[r8 + 1]);
            r3 = r16.mColors;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00b8, code lost:
            return (int) (((r8 / (r3.length - 1)) + (r1 * (1.0f / (r3.length - 1)))) * com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.MAX_LEVEL);
         */
        @Override // com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.Delegator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int findLevel(int r17) {
            /*
                r16 = this;
                r0 = r16
                r1 = r17
                r2 = 0
                r3 = -16777216(0xffffffffff000000, float:-1.7014118E38)
                if (r1 != r3) goto La
                return r2
            La:
                r3 = -1
                if (r1 != r3) goto L12
                int r1 = com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.access$000()
                return r1
            L12:
                int r4 = android.graphics.Color.alpha(r17)
                int r5 = android.graphics.Color.red(r17)
                int r6 = android.graphics.Color.green(r17)
                int r7 = android.graphics.Color.blue(r17)
                r8 = r2
            L23:
                int[] r9 = r0.mColors
                int r10 = r9.length
                int r10 = r10 + (-1)
                if (r8 >= r10) goto L8e
                r10 = r9[r8]
                int r11 = r8 + 1
                r12 = r9[r11]
                if (r1 != r10) goto L40
                float r1 = (float) r8
                int r2 = r9.length
                int r2 = r2 + (-1)
                float r2 = (float) r2
                float r1 = r1 / r2
                int r2 = com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.access$000()
                float r2 = (float) r2
                float r1 = r1 * r2
                int r1 = (int) r1
                return r1
            L40:
                if (r1 != r12) goto L50
                float r1 = (float) r11
                int r2 = r9.length
                int r2 = r2 + (-1)
                float r2 = (float) r2
                float r1 = r1 / r2
                int r2 = com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.access$000()
                float r2 = (float) r2
                float r1 = r1 * r2
                int r1 = (int) r1
                return r1
            L50:
                int r9 = android.graphics.Color.alpha(r10)
                int r13 = android.graphics.Color.red(r10)
                int r14 = android.graphics.Color.green(r10)
                int r10 = android.graphics.Color.blue(r10)
                int r15 = android.graphics.Color.alpha(r12)
                int r2 = android.graphics.Color.red(r12)
                int r3 = android.graphics.Color.green(r12)
                int r12 = android.graphics.Color.blue(r12)
                boolean r9 = r0.isValueInside(r4, r9, r15)
                if (r9 == 0) goto L8a
                boolean r2 = r0.isValueInside(r5, r13, r2)
                if (r2 == 0) goto L8a
                boolean r2 = r0.isValueInside(r6, r14, r3)
                if (r2 == 0) goto L8a
                boolean r2 = r0.isValueInside(r7, r10, r12)
                if (r2 == 0) goto L8a
                r2 = -1
                goto L90
            L8a:
                r8 = r11
                r2 = 0
                r3 = -1
                goto L23
            L8e:
                r2 = -1
                r8 = -1
            L90:
                if (r8 != r2) goto L94
                r2 = 0
                return r2
            L94:
                int[] r2 = r0.mColors
                r3 = r2[r8]
                int r4 = r8 + 1
                r2 = r2[r4]
                float r1 = r0.findColorProgress(r1, r3, r2)
                float r2 = (float) r8
                int[] r3 = r0.mColors
                int r4 = r3.length
                int r4 = r4 + (-1)
                float r4 = (float) r4
                float r2 = r2 / r4
                r4 = 1065353216(0x3f800000, float:1.0)
                int r3 = r3.length
                int r3 = r3 + (-1)
                float r3 = (float) r3
                float r4 = r4 / r3
                float r1 = r1 * r4
                float r2 = r2 + r1
                int r1 = com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.access$000()
                float r1 = (float) r1
                float r2 = r2 * r1
                int r1 = (int) r2
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker.GradientDelegator.findLevel(int):int");
        }

        public final float findColorProgress(int i, int i2, int i3) {
            float valueProgress = getValueProgress(Color.alpha(i), Color.alpha(i2), Color.alpha(i3));
            float valueProgress2 = getValueProgress(Color.red(i), Color.red(i2), Color.red(i3));
            float valueProgress3 = getValueProgress(Color.green(i), Color.green(i2), Color.green(i3));
            return Math.max(Math.max(Math.max(valueProgress, valueProgress2), valueProgress3), getValueProgress(Color.blue(i), Color.blue(i2), Color.blue(i3)));
        }
    }
}
