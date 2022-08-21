package com.miui.gallery.magic.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.miui.gallery.magic.Contact;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.widget.imageview.BitmapGestureView;

/* loaded from: classes2.dex */
public class PhotoPaper extends BitmapGestureView {
    public float itemPhotoH;
    public float itemPhotoW;
    public int mColor;
    public int mHeight;
    public int mWidth;
    public PhotoStyle p;
    public int spacing;

    public PhotoPaper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.spacing = 15;
    }

    public final Bitmap getPageBitmap(Bitmap bitmap, PhotoStyle photoStyle) {
        this.itemPhotoW = Math.round((this.mWidth - (this.spacing * 5)) / 4);
        float round = Math.round((bitmap.getHeight() / bitmap.getWidth()) * this.itemPhotoW);
        this.itemPhotoH = round;
        int i = this.mHeight;
        boolean z = (round + ((float) this.spacing)) * 2.0f <= ((float) i);
        Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, i, Bitmap.Config.ARGB_8888, true);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) this.itemPhotoW, (int) this.itemPhotoH, true);
        Canvas canvas = new Canvas(createBitmap);
        if (Contact.mPhotoMultiple) {
            int parseColor = Color.parseColor("#F3F7FA");
            if (Color.parseColor("#FFFFFF") == photoStyle.getColor()) {
                canvas.drawColor(parseColor);
            } else {
                canvas.drawColor(-1);
            }
        } else {
            canvas.drawColor(-1);
        }
        int i2 = this.mHeight;
        float f = this.itemPhotoH;
        int i3 = this.spacing;
        int i4 = (int) ((i2 - ((f * 2.0f) + i3)) / 2.0f);
        if (!z) {
            drawOne(canvas, createScaledBitmap, i3, ((int) ((i2 - f) / 2.0f)) - i3);
        } else {
            drawOne(canvas, createScaledBitmap, i3, i4);
            int i5 = this.spacing;
            drawTwo(canvas, createScaledBitmap, i5, (int) (i4 + this.itemPhotoH + i5));
        }
        return createBitmap;
    }

    public final void drawOne(Canvas canvas, Bitmap bitmap, int i, int i2) {
        int i3 = 0;
        while (i3 < 4) {
            int i4 = i3 + 1;
            canvas.drawBitmap(bitmap, (i * i4) + (this.itemPhotoW * i3), i2, (Paint) null);
            i3 = i4;
        }
    }

    public final void drawTwo(Canvas canvas, Bitmap bitmap, int i, int i2) {
        int i3 = 0;
        while (i3 < 4) {
            int i4 = i3 + 1;
            canvas.drawBitmap(bitmap, (i * i4) + (this.itemPhotoW * i3), i2, (Paint) null);
            i3 = i4;
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void setBitmap(Bitmap bitmap, PhotoStyle photoStyle) {
        if (bitmap == null) {
            return;
        }
        this.p = photoStyle;
        this.mWidth = 1500;
        this.mHeight = (int) (((1500 * 1.0d) / 5.0d) * 3.5d);
        this.mColor = photoStyle.getColor();
        setBitmap(getPageBitmap(bitmap, photoStyle));
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        return getPageBitmap(bitmap, this.p);
    }
}
