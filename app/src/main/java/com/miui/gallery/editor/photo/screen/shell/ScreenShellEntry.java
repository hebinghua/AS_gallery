package com.miui.gallery.editor.photo.screen.shell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.miui.gallery.editor.photo.screen.shell.res.ShellResourceFetcher;
import com.miui.gallery.editor.photo.utils.svgparser.SVG;
import com.miui.gallery.editor.photo.utils.svgparser.SVGAndroidRenderer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenShellEntry {
    public Bitmap mShellBitmap;
    public ShellInfoBean mShellInfo;
    public Path mShellSvgPath;
    public Paint mTopBlackViewPaint;
    public RectF mBitmapRect = new RectF();
    public Rect mSrcArea = new Rect();
    public RectF mDstArea = new RectF();
    public RectF mTopBlackViewRect = new RectF();
    public Paint mBitmapPaint = new Paint(3);

    public ScreenShellEntry() {
        Paint paint = new Paint();
        this.mTopBlackViewPaint = paint;
        paint.setColor(-16777216);
    }

    public Bitmap apply(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap((int) this.mShellInfo.getShellWidth(), (int) this.mShellInfo.getShellHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.mBitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        float height = getTopBlackViewRect(this.mBitmapRect).height();
        this.mSrcArea.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = this.mDstArea;
        ShellInfoBean shellInfoBean = this.mShellInfo;
        float shellWidth = shellInfoBean.getShellWidth();
        ShellInfoBean shellInfoBean2 = this.mShellInfo;
        rectF.set(shellInfoBean.leftMargin, shellInfoBean.topMargin + height, shellWidth - shellInfoBean2.rightMargin, shellInfoBean2.getShellHeight() - this.mShellInfo.bottomMargin);
        RectF rectF2 = this.mTopBlackViewRect;
        ShellInfoBean shellInfoBean3 = this.mShellInfo;
        rectF2.left = shellInfoBean3.leftMargin;
        rectF2.top = shellInfoBean3.topMargin;
        ShellInfoBean shellInfoBean4 = this.mShellInfo;
        rectF2.right = canvas.getWidth() - shellInfoBean4.rightMargin;
        this.mTopBlackViewRect.bottom = shellInfoBean4.topMargin + height;
        canvas.save();
        Matrix matrix = new Matrix();
        ShellInfoBean shellInfoBean5 = this.mShellInfo;
        matrix.postTranslate(shellInfoBean5.leftMargin, shellInfoBean5.topMargin);
        if (getShellSvgPath() != null) {
            Path path = new Path();
            path.addPath(getShellSvgPath(), matrix);
            canvas.clipPath(path);
        }
        canvas.drawRect(this.mTopBlackViewRect, this.mTopBlackViewPaint);
        canvas.drawBitmap(bitmap, this.mSrcArea, this.mDstArea, this.mBitmapPaint);
        canvas.restore();
        this.mSrcArea.set(0, 0, this.mShellBitmap.getWidth(), this.mShellBitmap.getHeight());
        this.mDstArea.set(0.0f, 0.0f, this.mShellInfo.getShellWidth(), this.mShellInfo.getShellHeight());
        canvas.drawBitmap(this.mShellBitmap, this.mSrcArea, this.mDstArea, this.mBitmapPaint);
        return createBitmap;
    }

    public void apply(Canvas canvas, RectF rectF) {
        if (this.mShellBitmap == null || this.mShellInfo == null) {
            return;
        }
        float height = getTopBlackViewRect(rectF).height();
        float width = rectF.width() / this.mShellInfo.with;
        this.mSrcArea.set(0, 0, this.mShellBitmap.getWidth(), this.mShellBitmap.getHeight());
        RectF rectF2 = this.mDstArea;
        float f = rectF.left;
        ShellInfoBean shellInfoBean = this.mShellInfo;
        rectF2.set(f - (shellInfoBean.leftMargin * width), (rectF.top - (shellInfoBean.topMargin * width)) - height, rectF.right + (shellInfoBean.rightMargin * width), rectF.bottom + (shellInfoBean.bottomMargin * width));
        canvas.drawBitmap(this.mShellBitmap, this.mSrcArea, this.mDstArea, this.mBitmapPaint);
    }

    public void drawTopBlackView(Canvas canvas, RectF rectF) {
        RectF topBlackViewRect = getTopBlackViewRect(rectF);
        if (topBlackViewRect.height() <= 5.0f || rectF.bottom <= topBlackViewRect.height()) {
            return;
        }
        canvas.drawRect(topBlackViewRect, this.mTopBlackViewPaint);
    }

    public RectF getTopBlackViewRect(RectF rectF) {
        this.mTopBlackViewRect.setEmpty();
        ShellInfoBean shellInfoBean = this.mShellInfo;
        if (shellInfoBean != null) {
            float f = shellInfoBean.with / shellInfoBean.height;
            if (Math.abs((rectF.width() / rectF.height()) - f) > 0.001d) {
                RectF rectF2 = this.mTopBlackViewRect;
                rectF2.left = rectF.left;
                rectF2.top = rectF.bottom - (rectF.width() / f);
                RectF rectF3 = this.mTopBlackViewRect;
                rectF3.right = rectF.right;
                float f2 = rectF.top;
                rectF3.bottom = f2;
                float f3 = rectF3.top;
                if (f3 < 0.0f) {
                    rectF3.bottom = f2 - f3;
                    rectF3.top = 0.0f;
                }
            }
        }
        return this.mTopBlackViewRect;
    }

    public ShellInfoBean getShellInfo() {
        return this.mShellInfo;
    }

    public Path getShellSvgPath() {
        return this.mShellSvgPath;
    }

    public void generateBitmap() {
        File[] listFiles;
        String materialPath = ShellResourceFetcher.getMaterialPath();
        if (!TextUtils.isEmpty(materialPath)) {
            File file = new File(materialPath);
            if (!file.exists()) {
                return;
            }
            for (File file2 : file.listFiles()) {
                String name = file2.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case 13116902:
                        if (name.equals("shell.json")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1801544043:
                        if (name.equals("shell.png")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1801547174:
                        if (name.equals("shell.svg")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        initJson(file2);
                        break;
                    case 1:
                        initPic(file2);
                        break;
                    case 2:
                        initSVG(file2);
                        break;
                }
            }
        }
    }

    public final void initSVG(File file) {
        List<Path> pathListFromSVG = getPathListFromSVG(file);
        if (pathListFromSVG.size() > 0) {
            Path path = pathListFromSVG.get(0);
            this.mShellSvgPath = path;
            RectF theRectOfPath = getTheRectOfPath(path);
            this.mShellSvgPath.offset(-theRectOfPath.left, -theRectOfPath.top);
        }
    }

    public final void initPic(File file) {
        this.mShellBitmap = BitmapFactory.decodeFile(file.getPath());
    }

    public final void initJson(File file) {
        try {
            this.mShellInfo = (ShellInfoBean) new Gson().fromJson(new JsonReader(new FileReader(file)), ShellInfoBean.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.io.ByteArrayOutputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.miui.gallery.editor.photo.utils.svgparser.SVG getSvgFromFile(java.io.File r7) {
        /*
            r6 = this;
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L49 com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L4e java.io.IOException -> L50
            r1.<init>(r7)     // Catch: java.lang.Throwable -> L49 com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L4e java.io.IOException -> L50
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L3f com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L44 java.io.IOException -> L46
            r7.<init>()     // Catch: java.lang.Throwable -> L3f com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L44 java.io.IOException -> L46
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
        Lf:
            int r3 = r1.read(r2)     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            if (r3 <= 0) goto L1a
            r4 = 0
            r7.write(r2, r4, r3)     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            goto Lf
        L1a:
            r7.flush()     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            byte[] r2 = r7.toByteArray()     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            r3.<init>(r2)     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            com.miui.gallery.editor.photo.utils.svgparser.SVG r0 = com.miui.gallery.editor.photo.utils.svgparser.SVG.getFromInputStream(r3)     // Catch: com.miui.gallery.editor.photo.utils.svgparser.SVGParseException -> L3b java.io.IOException -> L3d java.lang.Throwable -> L6b
            r7.close()     // Catch: java.io.IOException -> L2e
            goto L32
        L2e:
            r7 = move-exception
            r7.printStackTrace()
        L32:
            r1.close()     // Catch: java.io.IOException -> L36
            goto L3a
        L36:
            r7 = move-exception
            r7.printStackTrace()
        L3a:
            return r0
        L3b:
            r2 = move-exception
            goto L53
        L3d:
            r2 = move-exception
            goto L53
        L3f:
            r7 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L6c
        L44:
            r2 = move-exception
            goto L47
        L46:
            r2 = move-exception
        L47:
            r7 = r0
            goto L53
        L49:
            r7 = move-exception
            r1 = r0
            r0 = r7
            r7 = r1
            goto L6c
        L4e:
            r2 = move-exception
            goto L51
        L50:
            r2 = move-exception
        L51:
            r7 = r0
            r1 = r7
        L53:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L6b
            if (r7 == 0) goto L60
            r7.close()     // Catch: java.io.IOException -> L5c
            goto L60
        L5c:
            r7 = move-exception
            r7.printStackTrace()
        L60:
            if (r1 == 0) goto L6a
            r1.close()     // Catch: java.io.IOException -> L66
            goto L6a
        L66:
            r7 = move-exception
            r7.printStackTrace()
        L6a:
            return r0
        L6b:
            r0 = move-exception
        L6c:
            if (r7 == 0) goto L76
            r7.close()     // Catch: java.io.IOException -> L72
            goto L76
        L72:
            r7 = move-exception
            r7.printStackTrace()
        L76:
            if (r1 == 0) goto L80
            r1.close()     // Catch: java.io.IOException -> L7c
            goto L80
        L7c:
            r7 = move-exception
            r7.printStackTrace()
        L80:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.screen.shell.ScreenShellEntry.getSvgFromFile(java.io.File):com.miui.gallery.editor.photo.utils.svgparser.SVG");
    }

    public final List<Path> getPathListFromSVG(File file) {
        ArrayList arrayList = new ArrayList();
        SVG svgFromFile = getSvgFromFile(file);
        if (svgFromFile != null) {
            for (SVG.CloseShapeGraphicsElement closeShapeGraphicsElement : svgFromFile.getLayoutElements()) {
                if (closeShapeGraphicsElement instanceof SVG.Path) {
                    arrayList.add(new SVGAndroidRenderer.PathConverter(((SVG.Path) closeShapeGraphicsElement).d).getPath());
                }
            }
        }
        return arrayList;
    }

    public final RectF getTheRectOfPath(Path path) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        return rectF;
    }
}
