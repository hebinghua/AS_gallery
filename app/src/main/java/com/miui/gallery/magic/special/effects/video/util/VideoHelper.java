package com.miui.gallery.magic.special.effects.video.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoHelper {
    public int basicColor = 0;
    public Bitmap dst = null;
    public float time = 0.0f;
    public float ts = 0.0f;
    public int width = 0;
    public int height = 0;
    public List<OpEntry> opList = new ArrayList();
    public OpEntry entry = new OpEntry();
    public Bitmap orginal = null;

    public static VideoHelper create(Bitmap bitmap, float f) {
        if (f <= 0.0f) {
            throw new Error("time must be greater than zero.");
        }
        VideoHelper videoHelper = new VideoHelper();
        videoHelper.orginal = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        videoHelper.dst = bitmap;
        videoHelper.time = f;
        videoHelper.height = bitmap.getHeight();
        int width = bitmap.getWidth();
        videoHelper.width = width;
        videoHelper.ts = width / f;
        return videoHelper;
    }

    public void start(float f, int i) {
        OpEntry opEntry = new OpEntry();
        this.entry = opEntry;
        opEntry.setColor(i);
        this.entry.setStart(f);
        this.opList.add(this.entry);
    }

    public void clearProcess(float f) {
        process(f, true);
    }

    public void process(float f) {
        process(f, false);
    }

    public final void process(float f, boolean z) {
        if (f < this.entry.getStart()) {
            throw new Error("时间前进的方向有问题吧？？？");
        }
        float f2 = this.time;
        if (f > f2) {
            f = f2;
        }
        this.entry.setClear(z);
        this.entry.setEnd(f);
        drawEntry(this.dst, this.entry);
    }

    public Bitmap undo() {
        Bitmap bitmap = this.dst;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.dst.recycle();
        }
        this.dst = this.orginal.copy(Bitmap.Config.ARGB_8888, true);
        if (this.opList.size() <= 1) {
            this.opList.clear();
            return this.dst;
        }
        List<OpEntry> list = this.opList;
        list.remove(list.size() - 1);
        int size = this.opList.size();
        OpEntry[] opEntryArr = new OpEntry[size];
        for (int i = 0; i < size; i++) {
            opEntryArr[i] = this.opList.get(i);
        }
        drawEntry(this.dst, opEntryArr);
        return this.dst;
    }

    public final void drawEntry(Bitmap bitmap, OpEntry... opEntryArr) {
        Canvas canvas = new Canvas(bitmap);
        for (OpEntry opEntry : opEntryArr) {
            Paint paint = new Paint();
            paint.setColor(opEntry.getColor());
            RectF rectByEntry = getRectByEntry(opEntry);
            if (opEntry.isClear()) {
                Rect rect = new Rect((int) rectByEntry.left, (int) rectByEntry.top, (int) rectByEntry.right, (int) rectByEntry.bottom);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawBitmap(this.orginal, rect, rect, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                canvas.drawBitmap(this.orginal, rect, rect, new Paint());
            } else {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                canvas.drawRect(rectByEntry, paint);
            }
        }
    }

    public final RectF getRectByEntry(OpEntry opEntry) {
        if (BaseMiscUtil.isRTLDirection()) {
            return new RectF(Math.abs(this.width - (opEntry.getEnd() * this.ts)), 0.0f, Math.abs(this.width - (opEntry.getStart() * this.ts)), this.height);
        }
        return new RectF(opEntry.getStart() * this.ts, 0.0f, opEntry.getEnd() * this.ts, this.height);
    }

    /* loaded from: classes2.dex */
    public static class OpEntry {
        public float start = -1.0f;
        public float end = -1.0f;
        public int color = 0;
        public boolean isClear = false;

        public boolean isClear() {
            return this.isClear;
        }

        public void setClear(boolean z) {
            this.isClear = z;
        }

        public float getStart() {
            return this.start;
        }

        public void setStart(float f) {
            this.start = f;
        }

        public float getEnd() {
            return this.end;
        }

        public void setEnd(float f) {
            this.end = f;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int i) {
            this.color = i;
        }
    }
}
