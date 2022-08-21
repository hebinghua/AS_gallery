package com.miui.gallery.editor.photo.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.IPositionChangeData;
import com.miui.gallery.editor.photo.core.imports.frame.FrameRenderData;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.XmpHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes2.dex */
public class XmpExtraManager {
    public boolean mIsMoveWatermaskEnable;
    public boolean mIsRemoveWatermarkEnable;
    public WaterMaskWrapper mMaskWrapper = new WaterMaskWrapper();
    public int mOriginHeight;
    public int mOriginRotationDegree;
    public int mOriginWidth;
    public SubImage mSubImage;

    public void decodeXmpData(InputStream inputStream, int i, int i2, int i3) {
        this.mOriginWidth = i;
        this.mOriginHeight = i2;
        this.mOriginRotationDegree = i3;
        XMPMeta extractXMPMeta = XmpHelper.extractXMPMeta(inputStream);
        if (extractXMPMeta != null) {
            extraXmpMetaData(extractXMPMeta);
        }
    }

    public void checkSubImage(InputStream inputStream) {
        boolean z = true;
        try {
            try {
                if (this.mSubImage != null) {
                    int available = inputStream.available();
                    int i = this.mSubImage.offset;
                    if (available < i) {
                        this.mSubImage = null;
                    } else {
                        inputStream.skip(available - i);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(inputStream, null, options);
                        String str = options.outMimeType;
                        if (str == null || !BaseFileMimeUtil.isImageFromMimeType(str)) {
                            this.mSubImage = null;
                        }
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("XmpExtraManager", e);
            }
            if (this.mSubImage == null) {
                z = false;
            }
            this.mIsRemoveWatermarkEnable = z;
            if (z) {
                DefaultLogger.i("XmpExtraManager", "SubImage is exist");
            } else {
                DefaultLogger.i("XmpExtraManager", "SubImage is not exist");
            }
        } finally {
            IoUtils.close("XmpExtraManager", inputStream);
        }
    }

    public boolean isMoveWatermaskEnable() {
        return this.mIsMoveWatermaskEnable;
    }

    public boolean isRemoveWatermarkEnable() {
        return this.mIsRemoveWatermarkEnable;
    }

    public boolean isRemoveWatermarkShow(Bitmap bitmap, List<RenderData> list) {
        boolean z = false;
        if (!isRemoveWatermarkEnable()) {
            return false;
        }
        RectF watermarkRect = getWatermarkRect(bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        float f = 1.0f;
        for (RenderData renderData : list) {
            if (renderData instanceof IPositionChangeData) {
                ((IPositionChangeData) renderData).refreshTargetAreaPosition(rectF, watermarkRect);
                f = calculateIntersectPercent(watermarkRect, rectF);
                DefaultLogger.d("XmpExtraManager", "intersectPercent:%f", Float.valueOf(f));
            }
        }
        if (f > 0.4f) {
            z = true;
        }
        this.mIsRemoveWatermarkEnable = z;
        return z;
    }

    public final float calculateIntersectPercent(RectF rectF, RectF rectF2) {
        RectF rectF3 = new RectF(rectF);
        if (rectF3.intersect(rectF2)) {
            return ((rectF3.height() * rectF3.width()) / rectF.width()) / rectF.height();
        }
        return 0.0f;
    }

    public void initDeviceMask(InputStream inputStream) {
        initWaterMask(inputStream, this.mMaskWrapper.getDeviceMask());
    }

    public void initTimeMask(InputStream inputStream) {
        initWaterMask(inputStream, this.mMaskWrapper.getTimeMask());
    }

    public final void initWaterMask(InputStream inputStream, WaterMaskData waterMaskData) {
        int available;
        if (!isRemoveWatermarkEnable()) {
            this.mIsMoveWatermaskEnable = false;
            return;
        }
        try {
            if (waterMaskData == null) {
                return;
            }
            try {
                available = inputStream.available();
            } catch (Exception e) {
                DefaultLogger.e("XmpExtraManager", e);
            }
            if (available < waterMaskData.getSubImage().offset) {
                this.mIsMoveWatermaskEnable = false;
                return;
            }
            inputStream.skip(available - waterMaskData.getSubImage().offset);
            BitmapFactory.Options options = new BitmapFactory.Options();
            waterMaskData.getSubImage().renderBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            String str = options.outMimeType;
            if (str == null || !BaseFileMimeUtil.isImageFromMimeType(str)) {
                this.mIsMoveWatermaskEnable = false;
                return;
            }
            IoUtils.close("XmpExtraManager", inputStream);
            this.mIsMoveWatermaskEnable = true;
            DefaultLogger.i("XmpExtraManager", "Watermask is exist, type is " + waterMaskData.getMaskType());
            if (waterMaskData.getSubImage().renderBitmap.getWidth() != waterMaskData.getSubImage().width || waterMaskData.getSubImage().renderBitmap.getHeight() != waterMaskData.getSubImage().height) {
                RectF rectF = new RectF(0.0f, 0.0f, waterMaskData.getSubImage().width, waterMaskData.getSubImage().height);
                RectF rectF2 = new RectF(0.0f, 0.0f, waterMaskData.getSubImage().renderBitmap.getWidth(), waterMaskData.getSubImage().renderBitmap.getHeight());
                Matrix matrix = new Matrix();
                matrix.setRectToRect(rectF2, rectF, Matrix.ScaleToFit.CENTER);
                waterMaskData.getSubImage().renderBitmap = Bitmap.createBitmap(waterMaskData.getSubImage().renderBitmap, 0, 0, waterMaskData.getSubImage().renderBitmap.getWidth(), waterMaskData.getSubImage().renderBitmap.getHeight(), matrix, true);
            }
            RectF rectF3 = new RectF(0.0f, 0.0f, this.mOriginWidth, this.mOriginHeight);
            Matrix matrix2 = new Matrix();
            matrix2.postRotate(this.mOriginRotationDegree);
            int i = (this.mOriginRotationDegree + 360) % 360;
            if (i == 90) {
                matrix2.postTranslate(this.mOriginHeight, 0.0f);
            } else if (i == 180) {
                matrix2.postTranslate(this.mOriginWidth, this.mOriginHeight);
            } else if (i == 270) {
                matrix2.postTranslate(0.0f, this.mOriginWidth);
            }
            matrix2.mapRect(rectF3);
            this.mMaskWrapper.originHeight = (int) rectF3.height();
            this.mMaskWrapper.originWidth = (int) rectF3.width();
            RectF rectF4 = new RectF(0.0f, 0.0f, waterMaskData.getSubImage().renderBitmap.getWidth(), waterMaskData.getSubImage().renderBitmap.getHeight());
            rectF4.offset(waterMaskData.getMaskType() == 1 ? waterMaskData.getSubImage().paddingX : (this.mMaskWrapper.originWidth - waterMaskData.getSubImage().renderBitmap.getWidth()) - waterMaskData.getSubImage().paddingX, (this.mMaskWrapper.originHeight - waterMaskData.getSubImage().renderBitmap.getHeight()) - waterMaskData.getSubImage().paddingY);
            waterMaskData.getRecord().maskRect.set(rectF4);
        } finally {
            IoUtils.close("XmpExtraManager", inputStream);
        }
    }

    public void updateMaskInfo(RenderData renderData) {
        WaterMaskWrapper waterMaskWrapper = this.mMaskWrapper;
        if (waterMaskWrapper == null || renderData == null || !(renderData instanceof FrameRenderData)) {
            return;
        }
        if (waterMaskWrapper.getDeviceMask() != null) {
            this.mMaskWrapper.getDeviceMask().getRecord().framed = true;
        }
        if (this.mMaskWrapper.getTimeMask() == null) {
            return;
        }
        this.mMaskWrapper.getTimeMask().getRecord().framed = true;
    }

    public void sweepImage(Bitmap bitmap, InputStream inputStream) {
        SubImage subImage = this.mSubImage;
        if (subImage == null) {
            return;
        }
        try {
            if (subImage.renderBitmap == null) {
                try {
                    inputStream.skip(inputStream.available() - this.mSubImage.offset);
                    this.mSubImage.renderBitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    DefaultLogger.e("XmpExtraManager", e);
                }
            }
            if (this.mSubImage.renderBitmap == null) {
                return;
            }
            RectF rectF = new RectF(0.0f, 0.0f, this.mSubImage.renderBitmap.getWidth(), this.mSubImage.renderBitmap.getHeight());
            SubImage subImage2 = this.mSubImage;
            RectF rectF2 = new RectF(0.0f, 0.0f, subImage2.width, subImage2.height);
            RectF rectF3 = new RectF(0.0f, 0.0f, this.mOriginWidth, this.mOriginHeight);
            RectF rectF4 = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.FILL);
            Matrix matrix2 = new Matrix();
            matrix2.postRotate(this.mOriginRotationDegree);
            int i = (this.mOriginRotationDegree + 360) % 360;
            if (i == 90) {
                matrix2.postTranslate(this.mOriginHeight, 0.0f);
            } else if (i == 180) {
                matrix2.postTranslate(this.mOriginWidth, this.mOriginHeight);
            } else if (i == 270) {
                matrix2.postTranslate(0.0f, this.mOriginWidth);
            }
            matrix2.mapRect(rectF3);
            Matrix matrix3 = new Matrix();
            int i2 = this.mOriginRotationDegree;
            SubImage subImage3 = this.mSubImage;
            int i3 = subImage3.rotation;
            if (i3 != -1) {
                i2 = i3;
            }
            matrix3.postTranslate(subImage3.paddingX, subImage3.paddingY);
            matrix3.postRotate(i2);
            int i4 = (i2 + 360) % 360;
            if (i4 == 90) {
                matrix3.postTranslate(rectF3.width(), 0.0f);
            } else if (i4 == 180) {
                matrix3.postTranslate(rectF3.width(), rectF3.height());
            } else if (i4 == 270) {
                matrix3.postTranslate(0.0f, rectF3.height());
            }
            Matrix matrix4 = new Matrix();
            matrix4.setRectToRect(rectF3, rectF4, Matrix.ScaleToFit.FILL);
            Matrix matrix5 = new Matrix();
            matrix5.postConcat(matrix);
            matrix5.postConcat(matrix3);
            matrix5.postConcat(matrix4);
            new Canvas(bitmap).drawBitmap(this.mSubImage.renderBitmap, matrix5, new Paint(3));
        } finally {
            IoUtils.close("XmpExtraManager", inputStream);
        }
    }

    public void saveWaterMask(Bitmap bitmap) {
        if (this.mMaskWrapper.getDeviceMask() != null && this.mMaskWrapper.getDeviceMask().getSubImage().renderBitmap != null) {
            saveWaterMask(bitmap, this.mMaskWrapper.getDeviceMask());
        }
        if (this.mMaskWrapper.getTimeMask() == null || this.mMaskWrapper.getTimeMask().getSubImage().renderBitmap == null) {
            return;
        }
        saveWaterMask(bitmap, this.mMaskWrapper.getTimeMask());
    }

    public WaterMaskWrapper getWaterMaskWrapper() {
        return this.mMaskWrapper;
    }

    public final void saveWaterMask(Bitmap bitmap, WaterMaskData waterMaskData) {
        Matrix matrix = new Matrix();
        RectF rectF = new RectF();
        RectF rectF2 = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        rectF.set(waterMaskData.getRecord().displayRect);
        matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        RectF rectF3 = new RectF();
        matrix.mapRect(rectF3, waterMaskData.getRecord().maskBitmapRect);
        Matrix matrix2 = new Matrix();
        matrix2.setRectToRect(new RectF(0.0f, 0.0f, waterMaskData.getSubImage().renderBitmap.getWidth(), waterMaskData.getSubImage().renderBitmap.getHeight()), rectF3, Matrix.ScaleToFit.CENTER);
        new Canvas(bitmap).drawBitmap(waterMaskData.getSubImage().renderBitmap, matrix2, new Paint(3));
    }

    public RectF getWatermarkRect(int i, int i2) {
        RectF rectF = new RectF(0.0f, 0.0f, this.mOriginWidth, this.mOriginHeight);
        SubImage subImage = this.mSubImage;
        int i3 = subImage.paddingX;
        int i4 = subImage.paddingY;
        RectF rectF2 = new RectF(i3, i4, i3 + subImage.width, i4 + subImage.height);
        Matrix matrix = new Matrix();
        matrix.postRotate(this.mOriginRotationDegree);
        matrix.mapRect(rectF);
        matrix.mapRect(rectF2);
        matrix.setRectToRect(rectF, new RectF(0.0f, 0.0f, i, i2), Matrix.ScaleToFit.FILL);
        matrix.mapRect(rectF2);
        DefaultLogger.d("XmpExtraManager", "watermarkRect :%s", rectF2.toString());
        return rectF2;
    }

    public boolean waterChanged() {
        if (this.mMaskWrapper.getDeviceMask() == null && this.mMaskWrapper.getTimeMask() == null) {
            return false;
        }
        if (this.mMaskWrapper.getDeviceMask() != null ? this.mMaskWrapper.getDeviceMask().getRecord().moved : false) {
            return true;
        }
        if (this.mMaskWrapper.getTimeMask() == null) {
            return false;
        }
        return this.mMaskWrapper.getTimeMask().getRecord().moved;
    }

    public final void extraXmpMetaData(XMPMeta xMPMeta) {
        try {
            extraXmlMetaData(xMPMeta.getPropertyString("http://ns.xiaomi.com/photos/1.0/camera/", "XMPMeta"));
        } catch (XMPException e) {
            e.printStackTrace();
        }
    }

    public final void extraSubImageData(XmlPullParser xmlPullParser) {
        this.mSubImage = new SubImage();
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            attributeName.hashCode();
            char c = 65535;
            switch (attributeName.hashCode()) {
                case -1221029593:
                    if (attributeName.equals(nexExportFormat.TAG_FORMAT_HEIGHT)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1106363674:
                    if (attributeName.equals("length")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1019779949:
                    if (attributeName.equals("offset")) {
                        c = 2;
                        break;
                    }
                    break;
                case -40300674:
                    if (attributeName.equals(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION)) {
                        c = 3;
                        break;
                    }
                    break;
                case 113126854:
                    if (attributeName.equals(nexExportFormat.TAG_FORMAT_WIDTH)) {
                        c = 4;
                        break;
                    }
                    break;
                case 773277319:
                    if (attributeName.equals("paddingx")) {
                        c = 5;
                        break;
                    }
                    break;
                case 773277320:
                    if (attributeName.equals("paddingy")) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.mSubImage.height = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 1:
                    this.mSubImage.length = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 2:
                    this.mSubImage.offset = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 3:
                    this.mSubImage.rotation = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 4:
                    this.mSubImage.width = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 5:
                    this.mSubImage.paddingX = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 6:
                    this.mSubImage.paddingY = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
            }
        }
    }

    public final void extraWaterMaskData(XmlPullParser xmlPullParser, WaterMaskData waterMaskData) {
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            attributeName.hashCode();
            char c = 65535;
            switch (attributeName.hashCode()) {
                case -1221029593:
                    if (attributeName.equals(nexExportFormat.TAG_FORMAT_HEIGHT)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1106363674:
                    if (attributeName.equals("length")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1019779949:
                    if (attributeName.equals("offset")) {
                        c = 2;
                        break;
                    }
                    break;
                case 113126854:
                    if (attributeName.equals(nexExportFormat.TAG_FORMAT_WIDTH)) {
                        c = 3;
                        break;
                    }
                    break;
                case 773277319:
                    if (attributeName.equals("paddingx")) {
                        c = 4;
                        break;
                    }
                    break;
                case 773277320:
                    if (attributeName.equals("paddingy")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    waterMaskData.getSubImage().height = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 1:
                    waterMaskData.getSubImage().length = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 2:
                    waterMaskData.getSubImage().offset = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 3:
                    waterMaskData.getSubImage().width = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 4:
                    waterMaskData.getSubImage().paddingX = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
                case 5:
                    waterMaskData.getSubImage().paddingY = Integer.parseInt(xmlPullParser.getAttributeValue(i));
                    break;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0063, code lost:
        if (r3 == 1) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0065, code lost:
        if (r3 == 2) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0068, code lost:
        r7 = new com.miui.gallery.editor.photo.app.WaterMaskData(2);
        r6.mMaskWrapper.setTimeMask(r7);
        extraWaterMaskData(r0, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0076, code lost:
        r7 = new com.miui.gallery.editor.photo.app.WaterMaskData(1);
        r6.mMaskWrapper.setDeviceMask(r7);
        extraWaterMaskData(r0, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0087, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0087, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0087, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void extraXmlMetaData(java.lang.String r7) {
        /*
            r6 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 == 0) goto L7
            return
        L7:
            java.lang.String r0 = "XmpExtraManager"
            com.miui.gallery.util.logger.DefaultLogger.i(r0, r7)
            org.xmlpull.v1.XmlPullParser r0 = android.util.Xml.newPullParser()
            java.io.StringReader r1 = new java.io.StringReader     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r1.<init>(r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r0.setInput(r1)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            int r7 = r0.getEventType()     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
        L1c:
            r1 = 1
            if (r7 == r1) goto L95
            r2 = 2
            if (r7 == r2) goto L24
            goto L87
        L24:
            java.lang.String r7 = r0.getName()     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            boolean r3 = android.text.TextUtils.isEmpty(r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            if (r3 == 0) goto L2f
            goto L87
        L2f:
            r3 = -1
            int r4 = r7.hashCode()     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r5 = -2070555877(0xffffffff8495d31b, float:-3.52236E-36)
            if (r4 == r5) goto L58
            r5 = -186848201(0xfffffffff4dcec37, float:-1.4002641E32)
            if (r4 == r5) goto L4e
            r5 = 1390871846(0x52e70526, float:4.96111911E11)
            if (r4 == r5) goto L44
            goto L61
        L44:
            java.lang.String r4 = "lenswatermark"
            boolean r7 = r7.equals(r4)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            if (r7 == 0) goto L61
            r3 = r1
            goto L61
        L4e:
            java.lang.String r4 = "timewatermark"
            boolean r7 = r7.equals(r4)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            if (r7 == 0) goto L61
            r3 = r2
            goto L61
        L58:
            java.lang.String r4 = "subimage"
            boolean r7 = r7.equals(r4)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            if (r7 == 0) goto L61
            r3 = 0
        L61:
            if (r3 == 0) goto L84
            if (r3 == r1) goto L76
            if (r3 == r2) goto L68
            goto L87
        L68:
            com.miui.gallery.editor.photo.app.WaterMaskData r7 = new com.miui.gallery.editor.photo.app.WaterMaskData     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r7.<init>(r2)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            com.miui.gallery.editor.photo.app.WaterMaskWrapper r1 = r6.mMaskWrapper     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r1.setTimeMask(r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r6.extraWaterMaskData(r0, r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            goto L87
        L76:
            com.miui.gallery.editor.photo.app.WaterMaskData r7 = new com.miui.gallery.editor.photo.app.WaterMaskData     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r7.<init>(r1)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            com.miui.gallery.editor.photo.app.WaterMaskWrapper r1 = r6.mMaskWrapper     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r1.setDeviceMask(r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            r6.extraWaterMaskData(r0, r7)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            goto L87
        L84:
            r6.extraSubImageData(r0)     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
        L87:
            int r7 = r0.next()     // Catch: java.lang.Exception -> L8c org.xmlpull.v1.XmlPullParserException -> L91
            goto L1c
        L8c:
            r7 = move-exception
            r7.printStackTrace()
            goto L95
        L91:
            r7 = move-exception
            r7.printStackTrace()
        L95:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.app.XmpExtraManager.extraXmlMetaData(java.lang.String):void");
    }
}
