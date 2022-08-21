package com.xiaomi.milab.videosdk.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import com.google.gson.Gson;
import com.xiaomi.milab.videosdk.XmsAnimProperties;
import com.xiaomi.milab.videosdk.utils.TextAnim;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class DrawText {
    private float centerPointX;
    private float centerPointY;
    private double fps;
    private int mHeight;
    private ArrayList<TextAnim.Anim> mTextAnimList;
    private int mWidth;
    private float textWidth;
    private Map<TextAnim.Anim, Paint> mPaintMap = new HashMap();
    private Map<TextAnim.Anim, XmsAnimProperties> mAnimPropertiesMap = new HashMap();
    private HashMap<TextAnim.Anim, Bitmap> mBitmapMap = new HashMap<>();
    private HashMap<TextAnim.Anim, Path> mPathMap = new HashMap<>();
    private Map<TextAnim.Anim, Matrix> mMatrixMap = new HashMap();
    private Map<TextAnim.Cue, XmsAnimProperties> mCuePropertiesMap = new HashMap();
    private Map<TextAnim.Cue, CueDiffInfo> cueDiffInfoMap = new HashMap();

    public DrawText(String str, double d) {
        this.fps = d;
        TextAnim textAnim = (TextAnim) new Gson().fromJson(str, (Class<Object>) TextAnim.class);
        this.mTextAnimList = textAnim.animList;
        this.mWidth = textAnim.width;
        this.mHeight = textAnim.height;
        initPaint();
        initPoint();
        initAnim(d);
    }

    /* renamed from: com.xiaomi.milab.videosdk.utils.DrawText$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type;

        static {
            int[] iArr = new int[TextAnim.Type.values().length];
            $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type = iArr;
            try {
                iArr[TextAnim.Type.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.CUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.LINE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.PATH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.PNG.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.OTHER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private void initPaint() {
        Iterator<TextAnim.Anim> it = this.mTextAnimList.iterator();
        while (it.hasNext()) {
            TextAnim.Anim next = it.next();
            switch (AnonymousClass1.$SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.valueOf(next.type).ordinal()]) {
                case 1:
                case 2:
                    TextPaint textPaint = new TextPaint();
                    textPaint.setStyle(Paint.Style.FILL);
                    textPaint.setAntiAlias(true);
                    textPaint.setFilterBitmap(true);
                    textPaint.setLetterSpacing(next.letterSpacing);
                    String str = next.color;
                    if (str != null) {
                        textPaint.setColor(Color.parseColor(str));
                    }
                    textPaint.setTextSize(next.textSize);
                    textPaint.setUnderlineText(next.underline);
                    textPaint.setFakeBoldText(next.bold);
                    textPaint.setStrikeThruText(next.thrutext);
                    textPaint.setTextSkewX(0.0f);
                    this.mPaintMap.put(next, textPaint);
                    break;
                case 3:
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAntiAlias(true);
                    paint.setFilterBitmap(true);
                    String str2 = next.color;
                    if (str2 != null) {
                        paint.setColor(Color.parseColor(str2));
                    }
                    paint.setTextSize(next.textSize);
                    paint.setStrokeWidth(next.textSize);
                    paint.setUnderlineText(next.underline);
                    paint.setFakeBoldText(next.bold);
                    paint.setStrikeThruText(next.thrutext);
                    paint.setTextSkewX(0.0f);
                    this.mPaintMap.put(next, paint);
                    break;
                case 4:
                    TextPaint textPaint2 = new TextPaint();
                    textPaint2.setStyle(Paint.Style.FILL);
                    textPaint2.setAntiAlias(true);
                    textPaint2.setFilterBitmap(true);
                    String str3 = next.color;
                    if (str3 != null) {
                        textPaint2.setColor(Color.parseColor(str3));
                    }
                    textPaint2.setTextSize(next.textSize);
                    textPaint2.setUnderlineText(next.underline);
                    textPaint2.setFakeBoldText(next.bold);
                    textPaint2.setStrikeThruText(next.thrutext);
                    textPaint2.setTextSkewX(0.0f);
                    this.mPaintMap.put(next, textPaint2);
                    break;
                case 5:
                case 6:
                    Paint paint2 = new Paint();
                    paint2.setAntiAlias(true);
                    paint2.setFilterBitmap(true);
                    this.mPaintMap.put(next, paint2);
                    break;
            }
        }
    }

    private void initAnim(double d) {
        Iterator<TextAnim.Anim> it = this.mTextAnimList.iterator();
        while (it.hasNext()) {
            TextAnim.Anim next = it.next();
            XmsAnimProperties xmsAnimProperties = new XmsAnimProperties(d);
            xmsAnimProperties.setString("transX", next.transX);
            xmsAnimProperties.setString("transY", next.transY);
            xmsAnimProperties.setString("scale", next.scale);
            xmsAnimProperties.setString("alpha", next.alpha);
            xmsAnimProperties.setString("rotate", next.rotate);
            if (next.type == TextAnim.Type.CUE.getValue()) {
                List<TextAnim.Cue> list = next.cues;
                if (list != null && list.size() > 0) {
                    for (TextAnim.Cue cue : list) {
                        XmsAnimProperties xmsAnimProperties2 = new XmsAnimProperties(d);
                        String str = cue.evaporate;
                        if (!StringUtils.isEmpty(str)) {
                            xmsAnimProperties2.setString("evaporate", str);
                        }
                        this.mCuePropertiesMap.put(cue, xmsAnimProperties2);
                    }
                }
            } else {
                String str2 = next.evaporate;
                if (!StringUtils.isEmpty(str2)) {
                    xmsAnimProperties.setString("evaporate", str2);
                }
            }
            this.mAnimPropertiesMap.put(next, xmsAnimProperties);
        }
    }

    private void initPoint() {
        this.centerPointX = this.mWidth / 2.0f;
        this.centerPointY = this.mHeight / 2.0f;
        Iterator<TextAnim.Anim> it = this.mTextAnimList.iterator();
        while (it.hasNext()) {
            TextAnim.Anim next = it.next();
            Matrix matrix = new Matrix();
            matrix.reset();
            this.mPaintMap.get(next).setAlpha(0);
            this.mMatrixMap.put(next, matrix);
            int i = AnonymousClass1.$SwitchMap$com$xiaomi$milab$videosdk$utils$TextAnim$Type[TextAnim.Type.valueOf(next.type).ordinal()];
            if (i == 4) {
                Path path = new Path();
                path.reset();
                path.moveTo(next.pathList.get(0).x + next.posX + this.centerPointX, next.pathList.get(0).y + next.posY + this.centerPointY);
                for (int i2 = 0; i2 < next.pathList.size(); i2++) {
                    path.lineTo(next.pathList.get(i2).x + next.posX + this.centerPointX, next.pathList.get(i2).y + next.posY + this.centerPointY);
                }
                path.close();
                this.mPathMap.put(next, path);
            } else if (i == 5) {
                this.mBitmapMap.put(next, Bitmaps.getBitmapFromFile(next.text));
            }
        }
    }

    public void recycleBitmap(Bitmap bitmap) {
        bitmap.recycle();
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x01b7, code lost:
        if (com.xiaomi.milab.videosdk.utils.StringUtils.isEmpty(r5.text) != false) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.graphics.Bitmap renderFrame(int r29, long r30) {
        /*
            Method dump skipped, instructions count: 848
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.milab.videosdk.utils.DrawText.renderFrame(int, long):android.graphics.Bitmap");
    }

    public void release() {
        for (XmsAnimProperties xmsAnimProperties : this.mAnimPropertiesMap.values()) {
            xmsAnimProperties.release();
        }
        this.mAnimPropertiesMap.clear();
        this.mTextAnimList.clear();
        this.mPaintMap.clear();
        this.mBitmapMap.clear();
        this.mMatrixMap.clear();
    }

    private long getPts(long j) {
        return (long) ((j * 1000.0d) / this.fps);
    }

    private TextAnim.Cue getCueByPts(List<TextAnim.Cue> list, long j) {
        for (TextAnim.Cue cue : list) {
            if (cue.start <= j && cue.end > j) {
                return cue;
            }
        }
        return null;
    }

    private TextAnim.Cue getOldCue(List<TextAnim.Cue> list, TextAnim.Cue cue) {
        long j = cue.index - 1;
        if (j < 0) {
            return null;
        }
        for (TextAnim.Cue cue2 : list) {
            if (cue2.index == j) {
                return cue2;
            }
        }
        return null;
    }

    private CueDiffInfo getCueDiffInfo(TextAnim.Cue cue, TextAnim.Cue cue2, TextPaint textPaint) {
        if (this.cueDiffInfoMap.containsKey(cue)) {
            return this.cueDiffInfoMap.get(cue);
        }
        CueDiffInfo cueDiffInfo = new CueDiffInfo(cue.text, cue2.text, textPaint);
        this.cueDiffInfoMap.put(cue, cueDiffInfo);
        return cueDiffInfo;
    }
}
