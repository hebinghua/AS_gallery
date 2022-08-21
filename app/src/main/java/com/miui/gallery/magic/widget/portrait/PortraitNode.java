package com.miui.gallery.magic.widget.portrait;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.baseui.R$drawable;
import com.miui.gallery.magic.ContourHelper;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.widget.DoodleView;
import com.miui.gallery.magic.widget.portrait.BackgroundAdaptationHelper;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class PortraitNode implements Parcelable, Comparable<PortraitNode> {
    public static final Parcelable.Creator<PortraitNode> CREATOR = new Parcelable.Creator<PortraitNode>() { // from class: com.miui.gallery.magic.widget.portrait.PortraitNode.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public PortraitNode mo1079createFromParcel(Parcel parcel) {
            return new PortraitNode(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public PortraitNode[] mo1080newArray(int i) {
            return new PortraitNode[i];
        }
    };
    public boolean isIdle;
    public boolean mBlendMirror;
    public ContourHelper.Configure mConfigure;
    public RectF mDrawBounds;
    public RectF mImageBounds;
    public Matrix mInvert;
    public boolean mIsEmpty;
    public boolean mIsOrigin;
    public ModifyListener mListener;
    public ParcelableMatrix mMatrix;
    public float[] mMatrixValue;
    public Paint mPaint;
    public Bitmap mPersonBitmap;
    public long mPersonId;
    public int mPersonIndex;
    public Matrix mRotate;
    public boolean mUpdate;
    public BackgroundAdaptationHelper.ScaleResult scaleResult;
    public int shotIndex;

    /* loaded from: classes2.dex */
    public interface ModifyListener {
        void onModified();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PortraitNode() {
        this.mIsOrigin = false;
        this.mMatrix = new ParcelableMatrix();
        this.mInvert = new Matrix();
        this.mRotate = new Matrix();
        this.mPaint = new Paint(3);
        this.mMatrixValue = new float[9];
        this.mBlendMirror = false;
    }

    public PortraitNode(Bitmap bitmap, long j) {
        this(bitmap, j, null);
    }

    public PortraitNode(Bitmap bitmap, long j, Rect rect) {
        this.mIsOrigin = false;
        this.mMatrix = new ParcelableMatrix();
        this.mInvert = new Matrix();
        this.mRotate = new Matrix();
        this.mPaint = new Paint(3);
        this.mMatrixValue = new float[9];
        this.mBlendMirror = false;
        this.mPersonBitmap = bitmap;
        this.mPersonId = j;
        this.mImageBounds = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        this.mDrawBounds = new RectF(rect);
    }

    public void setPersonBitmap(Bitmap bitmap) {
        this.mPersonBitmap = bitmap;
    }

    public static PortraitNode copyStickerNode(PortraitNode portraitNode, int i) {
        PortraitNode portraitNode2 = new PortraitNode();
        portraitNode2.mPersonBitmap = portraitNode.mPersonBitmap;
        portraitNode2.mPersonIndex = i;
        portraitNode2.mMatrix = portraitNode.cloneMatrix(portraitNode.mMatrix);
        portraitNode2.mPersonId = System.currentTimeMillis();
        portraitNode2.mImageBounds = new RectF(portraitNode.mImageBounds);
        portraitNode2.mDrawBounds = new RectF(portraitNode.mDrawBounds);
        portraitNode2.mBlendMirror = portraitNode.mBlendMirror;
        portraitNode2.mRotate = portraitNode.cloneMatrix(portraitNode.mRotate);
        portraitNode2.setConfigure(portraitNode.getConfigure());
        portraitNode2.setUpdate(true);
        return portraitNode2;
    }

    public void init(Matrix matrix, Bitmap bitmap) {
        matrix.getValues(this.mMatrixValue);
        float f = this.mMatrixValue[0];
        this.mMatrix.postScale(f, f, 0.0f, 0.0f);
        postModify();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.mPersonBitmap, this.mMatrix, this.mPaint);
    }

    public void drawColor(Canvas canvas, int i, int i2) {
        if (i2 == -1) {
            return;
        }
        Bitmap createBitmap = Bitmap.createBitmap(this.mPersonBitmap.getWidth(), this.mPersonBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap);
        canvas2.drawColor(i);
        Paint paint = new Paint(3);
        paint.setAlpha(i2);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas2.drawBitmap(this.mPersonBitmap, 0.0f, 0.0f, paint);
        canvas.drawBitmap(createBitmap, this.mMatrix, this.mPaint);
    }

    public final void translate(float f, float f2) {
        this.mMatrix.postTranslate(f, f2);
        postModify();
    }

    public final void scale(float f) {
        this.mMatrix.postScale(f, f, this.mDrawBounds.centerX(), this.mDrawBounds.centerY());
        postModify();
    }

    public final void rotate(float f) {
        this.mRotate.postRotate(f);
        this.mMatrix.postRotate(f, this.mDrawBounds.centerX(), this.mDrawBounds.centerY());
        postModify();
    }

    public final void mirror() {
        this.mMatrix.preScale(-1.0f, 1.0f, this.mImageBounds.centerX(), this.mImageBounds.centerY());
        this.mBlendMirror = !this.mBlendMirror;
        postModify();
    }

    public final boolean contains(float[] fArr) {
        return this.mDrawBounds.contains(fArr[0], fArr[1]);
    }

    public final void postModify() {
        this.mMatrix.mapRect(this.mDrawBounds, this.mImageBounds);
        this.mMatrix.invert(this.mInvert);
        ModifyListener modifyListener = this.mListener;
        if (modifyListener != null) {
            modifyListener.onModified();
        }
    }

    public void changePersonBitmap(Bitmap bitmap) {
        this.mPersonBitmap = bitmap;
    }

    public boolean getBlendMirror() {
        return this.mBlendMirror;
    }

    public int getPersonIndex() {
        return this.mPersonIndex;
    }

    public void setPersonIndex(int i) {
        this.mPersonIndex = i;
    }

    public boolean isUpdate() {
        return this.mUpdate;
    }

    public void setUpdate(boolean z) {
        this.mUpdate = z;
    }

    public ContourHelper.Configure getConfigure(StrokeIconItem strokeIconItem, MattingInvoker.BoundingBox boundingBox) {
        ContourHelper.Configure color = new ContourHelper.Configure().setStyle(strokeIconItem.getStyle()).setStrokeWidth(getStrokeWidth(boundingBox, strokeIconItem)).setDistance(getDistance(boundingBox, strokeIconItem)).setRainbow(strokeIconItem.getRainbow()).setOffsetX(-boundingBox.getX()).setOffsetY(-boundingBox.getY()).setColor(strokeIconItem.getColor());
        this.mConfigure = color;
        return color;
    }

    public final float getDistance(MattingInvoker.BoundingBox boundingBox, StrokeIconItem strokeIconItem) {
        return (float) Math.ceil(strokeIconItem.getScreenScale() * strokeIconItem.getDistance() * (boundingBox.getWidth() / strokeIconItem.getScreenW()));
    }

    public final float getStrokeWidth(MattingInvoker.BoundingBox boundingBox, StrokeIconItem strokeIconItem) {
        return (float) Math.ceil(strokeIconItem.getScreenScale() * strokeIconItem.getStrokeWidth() * (boundingBox.getWidth() / strokeIconItem.getScreenW()));
    }

    public ContourHelper.Configure getConfigure() {
        return this.mConfigure;
    }

    public void setConfigure(ContourHelper.Configure configure) {
        this.mConfigure = configure;
    }

    public PortraitNode cloneNode() {
        PortraitNode portraitNode = new PortraitNode();
        RectF rectF = new RectF(this.mDrawBounds);
        RectF rectF2 = new RectF(this.mImageBounds);
        portraitNode.mDrawBounds = rectF;
        portraitNode.mImageBounds = rectF2;
        portraitNode.mIsOrigin = this.mIsOrigin;
        portraitNode.mMatrix = cloneMatrix(this.mMatrix);
        portraitNode.mInvert = cloneMatrix(this.mInvert);
        portraitNode.mRotate = cloneMatrix(this.mRotate);
        portraitNode.scaleResult = cloneScaleResult();
        portraitNode.setPersonIndex(this.mPersonIndex);
        portraitNode.setConfigure(this.mConfigure);
        portraitNode.setPersonBitmap(this.mPersonBitmap);
        portraitNode.setShotIndex(this.shotIndex);
        portraitNode.setUpdate(this.mUpdate);
        portraitNode.setIdle(false);
        portraitNode.mBlendMirror = this.mBlendMirror;
        return portraitNode;
    }

    public final BackgroundAdaptationHelper.ScaleResult cloneScaleResult() {
        BackgroundAdaptationHelper.ScaleResult scaleResult = this.scaleResult;
        if (scaleResult != null) {
            return scaleResult.m1075clone();
        }
        return null;
    }

    public final ParcelableMatrix cloneMatrix(Matrix matrix) {
        ParcelableMatrix parcelableMatrix = new ParcelableMatrix();
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        parcelableMatrix.setValues(fArr);
        return parcelableMatrix;
    }

    public ContourHelper.Configure getConfigure(MattingInvoker.BoundingBox boundingBox) {
        this.mConfigure.setOffsetX(-boundingBox.getX()).setOffsetY(-boundingBox.getY());
        return this.mConfigure;
    }

    public boolean isIdle() {
        return this.isIdle;
    }

    public void setIsEmpty(boolean z) {
        this.mIsEmpty = z;
    }

    public boolean isEmpty() {
        return this.mIsEmpty;
    }

    @Override // java.lang.Comparable
    public int compareTo(PortraitNode portraitNode) {
        return this.shotIndex > portraitNode.getShotIndex() ? 0 : -1;
    }

    public int getShotIndex() {
        return this.shotIndex;
    }

    public void setShotIndex(int i) {
        this.shotIndex = i;
    }

    public BackgroundAdaptationHelper.ScaleResult getScaleResult(BackgroundAdaptationHelper.ScaleResult scaleResult) {
        this.scaleResult = scaleResult;
        return scaleResult;
    }

    public void setScaleResult(BackgroundAdaptationHelper.ScaleResult scaleResult) {
        this.scaleResult = scaleResult;
    }

    public BackgroundAdaptationHelper.ScaleResult getScaleResult() {
        return this.scaleResult;
    }

    public void setIdle(boolean z) {
        this.isIdle = z;
    }

    /* loaded from: classes2.dex */
    public static class Mutator {
        public boolean isShowDis;
        public ScaleDrawable mAdd;
        public Drawable mAddDis;
        public final Matrix mBitmapToCanvas;
        public Drawable mBorder;
        public final Paint mBorderPaint;
        public final Matrix mCanvasToBitmap;
        public ScaleDrawable mDelete;
        public PortraitNode mItem;
        public ScaleDrawable mMatting;
        public ScaleDrawable mMirror;
        public Rect mDrawBounds = new Rect();
        public float[][] mVertices = (float[][]) Array.newInstance(float.class, 4, 2);
        public float[] mReusePoint = new float[2];
        public float[] mReuseVector = new float[2];
        public RectF mReuseRect = new RectF();
        public Matrix mReuseDegree = new Matrix();
        public Matrix mReuseMatrix = new Matrix();
        public ModifyListener mListener = new ModifyListener() { // from class: com.miui.gallery.magic.widget.portrait.PortraitNode$Mutator$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.magic.widget.portrait.PortraitNode.ModifyListener
            public final void onModified() {
                PortraitNode.Mutator.$r8$lambda$8Pya47EHYS_S5D0Iu7vKXvvpsjk(PortraitNode.Mutator.this);
            }
        };

        public static /* synthetic */ void $r8$lambda$8Pya47EHYS_S5D0Iu7vKXvvpsjk(Mutator mutator) {
            mutator.lambda$new$0();
        }

        public Mutator(Context context, Matrix matrix, Matrix matrix2) {
            for (int i = 0; i < 4; i++) {
                this.mVertices[i] = new float[2];
            }
            this.mDelete = getScaleDrawable(context.getResources().getDrawable(R$drawable.common_editor_window_action_btn_delete));
            this.mMirror = getScaleDrawable(context.getResources().getDrawable(R$drawable.common_editor_window_action_btn_mirror));
            this.mMatting = getScaleDrawable(context.getResources().getDrawable(com.miui.gallery.magic.R$drawable.common_editor_window_action_btn_magic));
            this.mAdd = getScaleDrawable(context.getResources().getDrawable(com.miui.gallery.magic.R$drawable.common_editor_window_action_btn_add));
            this.mAddDis = getScaleDrawable(context.getResources().getDrawable(com.miui.gallery.magic.R$drawable.common_editor_window_action_btn_dis));
            this.mBorder = context.getResources().getDrawable(R$drawable.common_editor_window_n);
            this.mBitmapToCanvas = matrix;
            this.mCanvasToBitmap = matrix2;
            Paint paint = new Paint(1);
            this.mBorderPaint = paint;
            paint.setColor(-1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6.0f);
        }

        public final ScaleDrawable getScaleDrawable(Drawable drawable) {
            ScaleDrawable scaleDrawable = new ScaleDrawable(drawable, 17, 0.5f, 0.5f);
            scaleDrawable.setLevel(10000);
            return scaleDrawable;
        }

        public boolean isIdle() {
            return this.mItem == null;
        }

        public PortraitNode getItem() {
            return this.mItem;
        }

        public void bind(PortraitNode portraitNode) {
            PortraitNode portraitNode2 = this.mItem;
            if (portraitNode2 != null) {
                portraitNode2.mListener = null;
            }
            portraitNode.mListener = this.mListener;
            this.mItem = portraitNode;
            portraitNode.setIdle(true);
            lambda$new$0();
        }

        public PortraitNode unbind() {
            PortraitNode portraitNode = this.mItem;
            if (portraitNode != null) {
                portraitNode.setIdle(false);
            }
            this.mItem = null;
            return portraitNode;
        }

        public void draw(Canvas canvas, boolean z, boolean z2, int i) {
            if (this.mItem == null) {
                return;
            }
            canvas.save();
            canvas.concat(this.mBitmapToCanvas);
            this.mItem.draw(canvas);
            this.mItem.drawColor(canvas, DoodleView.MASK_COLOR, i);
            canvas.restore();
            drawBorderLine(canvas);
            if (z2 || z) {
                return;
            }
            this.mMirror.draw(canvas);
            this.mDelete.draw(canvas);
            this.mMatting.draw(canvas);
            this.mAdd.draw(canvas);
            this.mAddDis.draw(canvas);
        }

        public final void drawBorderLine(Canvas canvas) {
            canvas.save();
            canvas.translate(this.mBorder.getBounds().centerX(), this.mBorder.getBounds().centerY());
            canvas.concat(this.mItem.mRotate);
            canvas.translate(-this.mBorder.getBounds().centerX(), -this.mBorder.getBounds().centerY());
            Rect bounds = this.mBorder.getBounds();
            this.mBorderPaint.setColor(-1);
            this.mBorderPaint.setAlpha(255);
            this.mBorderPaint.setStrokeWidth(6.0f);
            canvas.drawRect(bounds, this.mBorderPaint);
            this.mBorderPaint.setStrokeWidth(1.0f);
            this.mBorderPaint.setColor(-16777216);
            this.mBorderPaint.setAlpha(39);
            canvas.drawRect(new Rect(bounds.left + 4, bounds.top + 4, bounds.right - 4, bounds.bottom - 4), this.mBorderPaint);
            canvas.drawRect(new Rect(bounds.left - 4, bounds.top - 4, bounds.right + 4, bounds.bottom + 4), this.mBorderPaint);
            canvas.restore();
        }

        public boolean contains(float f, float f2) {
            if (this.mItem == null) {
                return false;
            }
            float[] fArr = this.mReusePoint;
            fArr[0] = f;
            fArr[1] = f2;
            this.mCanvasToBitmap.mapPoints(fArr);
            return this.mItem.contains(this.mReusePoint);
        }

        public void mirror() {
            this.mItem.mirror();
        }

        public void rotate(float f) {
            this.mItem.rotate(f);
        }

        public void scale(float f) {
            this.mItem.scale(f);
        }

        public void translate(float f, float f2) {
            float[] fArr = this.mReuseVector;
            fArr[0] = f;
            fArr[1] = f2;
            this.mCanvasToBitmap.mapVectors(fArr);
            PortraitNode portraitNode = this.mItem;
            float[] fArr2 = this.mReuseVector;
            portraitNode.translate(fArr2[0], fArr2[1]);
        }

        public boolean isScale(float f, float f2) {
            return this.mMatting.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public boolean isDelete(float f, float f2) {
            return this.mDelete.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public boolean isMirror(float f, float f2) {
            return this.mMirror.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public boolean isAdd(float f, float f2) {
            this.mAddDis.getBounds().contains(Math.round(f), Math.round(f2));
            return this.mAdd.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public void setScaleAlpha(int i) {
            scaleDownDraw(this.mMatting, i);
        }

        public void setDeleteAlpha(int i) {
            scaleDownDraw(this.mDelete, i);
        }

        public final void scaleDownDraw(ScaleDrawable scaleDrawable, int i) {
            if (153 == i) {
                scaleDrawable.setLevel(8000);
            } else {
                scaleDrawable.setLevel(10000);
            }
        }

        public void setMirrorAlpha(int i) {
            scaleDownDraw(this.mMirror, i);
        }

        public void setAddAlpha(int i) {
            if (!this.isShowDis) {
                scaleDownDraw(this.mAdd, i);
                this.mAdd.setAlpha(i);
            }
        }

        public float getRadius() {
            return (float) Math.hypot(this.mMatting.getBounds().centerX() - this.mDrawBounds.centerX(), this.mMatting.getBounds().centerY() - this.mDrawBounds.centerY());
        }

        public Rect getDrawBounds() {
            return this.mDrawBounds;
        }

        /* renamed from: updateDisplayInfo */
        public void lambda$new$0() {
            this.mReuseRect.set(this.mItem.mDrawBounds);
            this.mBitmapToCanvas.mapRect(this.mReuseRect);
            this.mDrawBounds.set(Math.round(this.mReuseRect.left), Math.round(this.mReuseRect.top), Math.round(this.mReuseRect.right), Math.round(this.mReuseRect.bottom));
            this.mItem.mRotate.invert(this.mReuseMatrix);
            this.mReuseMatrix.preTranslate(-this.mItem.mDrawBounds.centerX(), -this.mItem.mDrawBounds.centerY());
            this.mReuseMatrix.postTranslate(this.mItem.mDrawBounds.centerX(), this.mItem.mDrawBounds.centerY());
            this.mReuseMatrix.preConcat(this.mItem.mMatrix);
            this.mReuseMatrix.mapRect(this.mReuseRect, this.mItem.mImageBounds);
            this.mBitmapToCanvas.mapRect(this.mReuseRect);
            this.mBorder.setBounds(Math.round(this.mReuseRect.left), Math.round(this.mReuseRect.top), Math.round(this.mReuseRect.right), Math.round(this.mReuseRect.bottom));
            float[][] fArr = this.mVertices;
            float[] fArr2 = fArr[0];
            RectF rectF = this.mReuseRect;
            float f = rectF.left;
            fArr2[0] = f;
            float[] fArr3 = fArr[0];
            float f2 = rectF.bottom;
            fArr3[1] = f2;
            float[] fArr4 = fArr[1];
            float f3 = rectF.right;
            fArr4[0] = f3;
            float[] fArr5 = fArr[1];
            float f4 = rectF.top;
            fArr5[1] = f4;
            fArr[2][0] = f;
            fArr[2][1] = f4;
            fArr[3][0] = f3;
            fArr[3][1] = f2;
            this.mReuseMatrix.setTranslate(-rectF.centerX(), -this.mReuseRect.centerY());
            this.mReuseMatrix.postConcat(this.mItem.mRotate);
            this.mReuseMatrix.postTranslate(this.mReuseRect.centerX(), this.mReuseRect.centerY());
            for (float[] fArr6 : this.mVertices) {
                this.mReuseMatrix.mapPoints(fArr6);
            }
            ScaleDrawable scaleDrawable = this.mDelete;
            float[][] fArr7 = this.mVertices;
            setBounds(scaleDrawable, fArr7[1][0], fArr7[1][1]);
            ScaleDrawable scaleDrawable2 = this.mMirror;
            float[][] fArr8 = this.mVertices;
            setBounds(scaleDrawable2, fArr8[0][0], fArr8[0][1]);
            ScaleDrawable scaleDrawable3 = this.mMatting;
            float[][] fArr9 = this.mVertices;
            setBounds(scaleDrawable3, fArr9[3][0], fArr9[3][1]);
            ScaleDrawable scaleDrawable4 = this.mAdd;
            float[][] fArr10 = this.mVertices;
            setBounds(scaleDrawable4, fArr10[2][0], fArr10[2][1]);
            Drawable drawable = this.mAddDis;
            float[][] fArr11 = this.mVertices;
            setBounds(drawable, fArr11[2][0], fArr11[2][1]);
        }

        public static void setBounds(Drawable drawable, float f, float f2) {
            float intrinsicWidth = drawable.getIntrinsicWidth() / 2.0f;
            float intrinsicHeight = drawable.getIntrinsicHeight() / 2.0f;
            drawable.setBounds(Math.round(f - intrinsicWidth), Math.round(f2 - intrinsicHeight), Math.round(f + intrinsicWidth), Math.round(f2 + intrinsicHeight));
        }

        public int getPersonIndex() {
            PortraitNode portraitNode = this.mItem;
            if (portraitNode == null) {
                return -1;
            }
            return portraitNode.getPersonIndex();
        }

        public void getFirstMatrix(float[] fArr) {
            PortraitNode portraitNode = this.mItem;
            if (portraitNode == null) {
                return;
            }
            portraitNode.mMatrix.getValues(fArr);
        }

        public void setDisShow(boolean z) {
            this.isShowDis = z;
            if (z) {
                this.mAdd.setAlpha(0);
                this.mAddDis.setAlpha(255);
                return;
            }
            this.mAdd.setAlpha(255);
            this.mAddDis.setAlpha(0);
        }
    }

    /* loaded from: classes2.dex */
    public static class Cache {
        public Bitmap mBackground;
        public Matrix mBitmapToCanvas;
        public Canvas mCanvas;
        public Matrix mCanvasToBitmap;
        public Bitmap mLayer;
        public Rect mRedrawSrc = new Rect();
        public RectF mRedrawDst = new RectF();
        public Paint mPaint = new Paint(3);
        public ArrayList<PortraitNode> mNodes = new ArrayList<>();
        public Rect mCanvasBound = new Rect();
        public float[] mReusePoint = new float[2];

        public Cache(Bitmap bitmap, Matrix matrix, Matrix matrix2) {
            this.mBackground = bitmap;
            this.mCanvasToBitmap = matrix;
            this.mBitmapToCanvas = matrix2;
            this.mLayer = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            this.mCanvas = new Canvas(this.mLayer);
            this.mCanvasBound.set(0, 0, this.mLayer.getWidth(), this.mLayer.getHeight());
        }

        public List<PortraitNode> getNodes() {
            return (List) this.mNodes.clone();
        }

        public void append(PortraitNode portraitNode) {
            if (portraitNode == null) {
                return;
            }
            this.mNodes.add(portraitNode);
            drawLayer(portraitNode);
        }

        public void drawLayer(PortraitNode portraitNode) {
            Canvas canvas = this.mCanvas;
            int saveLayer = canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), this.mCanvas.getHeight(), null, 31);
            portraitNode.draw(this.mCanvas);
            this.mCanvas.restoreToCount(saveLayer);
        }

        public int find(float f, float f2) {
            float[] fArr = this.mReusePoint;
            fArr[0] = f;
            fArr[1] = f2;
            this.mCanvasToBitmap.mapPoints(fArr);
            for (int size = this.mNodes.size() - 1; size >= 0; size--) {
                if (this.mNodes.get(size).contains(this.mReusePoint)) {
                    return size;
                }
            }
            return -1;
        }

        public boolean removeIndexByPerson(int i) {
            for (int size = this.mNodes.size() - 1; size >= 0; size--) {
                if (this.mNodes.get(size).getPersonIndex() == i) {
                    remove(size);
                    return true;
                }
            }
            return false;
        }

        public PortraitNode removePersonIndex(int i) {
            for (int size = this.mNodes.size() - 1; size >= 0; size--) {
                PortraitNode portraitNode = this.mNodes.get(size);
                if (portraitNode.getPersonIndex() == i) {
                    remove(size);
                    return portraitNode;
                }
            }
            return null;
        }

        public PortraitNode remove(int i) {
            PortraitNode remove = this.mNodes.remove(i);
            this.mRedrawSrc.set((int) Math.floor(remove.mDrawBounds.left), (int) Math.floor(remove.mDrawBounds.top), (int) Math.ceil(remove.mDrawBounds.right), (int) Math.ceil(remove.mDrawBounds.bottom));
            this.mRedrawDst.set(this.mRedrawSrc);
            long currentTimeMillis = System.currentTimeMillis();
            reDrawClearCanvas(findIntersects(remove));
            DefaultLogger.d("MagicLogger PortraitNode", "rebuild finish costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return remove;
        }

        public final void reDrawClearCanvas(List<PortraitNode> list) {
            this.mCanvas.save();
            this.mCanvas.clipRect(this.mRedrawSrc);
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            this.mCanvas.drawBitmap(this.mBackground, this.mRedrawSrc, this.mRedrawDst, this.mPaint);
            this.mPaint.setXfermode(null);
            DefaultLogger.d("MagicLogger PortraitEditView", "need rebuild %d items", Integer.valueOf(list.size()));
            for (PortraitNode portraitNode : list) {
                drawLayer(portraitNode);
            }
            this.mCanvas.restore();
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(this.mLayer, this.mBitmapToCanvas, this.mPaint);
        }

        public final List<PortraitNode> findIntersects(PortraitNode portraitNode) {
            ArrayList arrayList = new ArrayList();
            Iterator<PortraitNode> it = this.mNodes.iterator();
            while (it.hasNext()) {
                PortraitNode next = it.next();
                if (RectF.intersects(next.mDrawBounds, portraitNode.mDrawBounds)) {
                    arrayList.add(next);
                }
            }
            return arrayList;
        }

        public void clear() {
            this.mNodes.clear();
            this.mLayer = this.mBackground.copy(Bitmap.Config.ARGB_8888, true);
            this.mCanvas = new Canvas(this.mLayer);
            this.mCanvasBound.set(0, 0, this.mLayer.getWidth(), this.mLayer.getHeight());
        }

        public void drawTransparen(Bitmap bitmap, float[] fArr, Bitmap bitmap2) {
            Bitmap bitmapBackground = MagicFileUtil.getBitmapBackground(bitmap2, bitmap.getWidth(), bitmap.getHeight());
            Bitmap createBitmap = Bitmap.createBitmap(bitmapBackground, 0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint(3);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            Matrix matrix = new Matrix();
            matrix.setValues(fArr);
            this.mCanvas.drawBitmap(createBitmap, matrix, this.mPaint);
            createBitmap.recycle();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mMatrix, i);
    }

    public PortraitNode(Parcel parcel) {
        this.mIsOrigin = false;
        this.mMatrix = new ParcelableMatrix();
        this.mInvert = new Matrix();
        this.mRotate = new Matrix();
        this.mPaint = new Paint(3);
        this.mMatrixValue = new float[9];
        this.mBlendMirror = false;
        this.mMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
    }
}
