package com.miui.gallery.editor.photo.core.imports.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StickerEditorView.java */
@Keep
/* loaded from: classes2.dex */
public class StickerNode implements Parcelable {
    public static final Parcelable.Creator<StickerNode> CREATOR = new Parcelable.Creator<StickerNode>() { // from class: com.miui.gallery.editor.photo.core.imports.sticker.StickerNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public StickerNode mo873createFromParcel(Parcel parcel) {
            return new StickerNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public StickerNode[] mo874newArray(int i) {
            return new StickerNode[i];
        }
    };
    private static final String TAG = "StickerNode";
    public static final int X = 0;
    public static final int Y = 1;
    private RectF mDrawBounds;
    private RectF mImageBounds;
    private Matrix mInvert;
    private ModifyListener mListener;
    private ParcelableMatrix mMatrix;
    private float[] mMatrixValue;
    private Paint mPaint;
    private String mPathName;
    private Matrix mRotate;
    private Bitmap mSticker;
    public String mStickerCateName;
    public long mStickerId;

    /* compiled from: StickerEditorView.java */
    /* loaded from: classes2.dex */
    public interface ModifyListener {
        void onModified();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public StickerNode(Bitmap bitmap, String str, long j, String str2) {
        this.mMatrix = new ParcelableMatrix();
        this.mInvert = new Matrix();
        this.mRotate = new Matrix();
        this.mPaint = new Paint(3);
        this.mMatrixValue = new float[9];
        this.mSticker = bitmap;
        this.mStickerId = j;
        this.mStickerCateName = str2;
        this.mPathName = str;
        this.mImageBounds = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        this.mDrawBounds = new RectF(this.mImageBounds);
    }

    public void init(Matrix matrix, Bitmap bitmap, float f) {
        matrix.getValues(this.mMatrixValue);
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float screenWidth = ScreenUtils.getScreenWidth();
        float[] fArr = this.mMatrixValue;
        float f2 = fArr[0];
        float f3 = fArr[4];
        if (width < screenWidth) {
            f2 *= width / screenWidth;
        }
        if (height < f) {
            f3 *= height / f;
        }
        if (f3 < f2) {
            f2 = f3;
        }
        this.mMatrix.postScale(f2, f2, 0.0f, 0.0f);
        postModify();
    }

    public void initForParcelable() {
        this.mSticker = BitmapFactory.decodeFile(this.mPathName);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.mSticker, this.mMatrix, this.mPaint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void translate(float f, float f2) {
        this.mMatrix.postTranslate(f, f2);
        postModify();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scale(float f) {
        this.mMatrix.postScale(f, f, this.mDrawBounds.centerX(), this.mDrawBounds.centerY());
        postModify();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rotate(Matrix matrix) {
        this.mRotate.postConcat(matrix);
        this.mMatrix.postTranslate(-this.mDrawBounds.centerX(), -this.mDrawBounds.centerY());
        this.mMatrix.postConcat(matrix);
        this.mMatrix.postTranslate(this.mDrawBounds.centerX(), this.mDrawBounds.centerY());
        postModify();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mirror() {
        this.mMatrix.preScale(-1.0f, 1.0f, this.mImageBounds.centerX(), this.mImageBounds.centerY());
        postModify();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean contains(float[] fArr) {
        float f = fArr[0];
        float f2 = fArr[1];
        this.mInvert.mapPoints(fArr);
        boolean contains = this.mImageBounds.contains(fArr[0], fArr[1]);
        fArr[0] = f;
        fArr[1] = f2;
        return contains;
    }

    private void postModify() {
        this.mMatrix.mapRect(this.mDrawBounds, this.mImageBounds);
        this.mMatrix.invert(this.mInvert);
        ModifyListener modifyListener = this.mListener;
        if (modifyListener != null) {
            modifyListener.onModified();
        }
    }

    /* compiled from: StickerEditorView.java */
    /* loaded from: classes2.dex */
    public static class Mutator {
        public final Matrix mBitmapToCanvas;
        public Drawable mBorder;
        public final Matrix mCanvasToBitmap;
        public Drawable mDelete;
        public StickerNode mItem;
        public Drawable mMirror;
        public Drawable mScale;
        public Rect mDrawBounds = new Rect();
        public float[][] mVertices = (float[][]) Array.newInstance(float.class, 4, 2);
        public float[] mReusePoint = new float[2];
        public float[] mReuseVector = new float[2];
        public RectF mReuseRect = new RectF();
        public Matrix mReuseDegree = new Matrix();
        public Matrix mReuseMatrix = new Matrix();
        public ModifyListener mListener = new ModifyListener() { // from class: com.miui.gallery.editor.photo.core.imports.sticker.StickerNode.Mutator.1
            @Override // com.miui.gallery.editor.photo.core.imports.sticker.StickerNode.ModifyListener
            public void onModified() {
                Mutator.this.updateDisplayInfo();
            }
        };

        public Mutator(Context context, Matrix matrix, Matrix matrix2) {
            for (int i = 0; i < 4; i++) {
                this.mVertices[i] = new float[2];
            }
            this.mDelete = context.getResources().getDrawable(R.drawable.common_editor_window_action_btn_delete);
            this.mMirror = context.getResources().getDrawable(R.drawable.common_editor_window_action_btn_mirror);
            this.mScale = context.getResources().getDrawable(R.drawable.common_editor_window_action_btn_scale);
            this.mBorder = context.getResources().getDrawable(R.drawable.common_editor_window_n);
            this.mBitmapToCanvas = matrix;
            this.mCanvasToBitmap = matrix2;
        }

        public boolean isIdle() {
            return this.mItem == null;
        }

        public void bind(StickerNode stickerNode) {
            StickerNode stickerNode2 = this.mItem;
            if (stickerNode2 != null) {
                stickerNode2.mListener = null;
            }
            stickerNode.mListener = this.mListener;
            this.mItem = stickerNode;
            updateDisplayInfo();
        }

        public StickerNode unbind() {
            StickerNode stickerNode = this.mItem;
            this.mItem = null;
            return stickerNode;
        }

        public void draw(Canvas canvas, boolean z, boolean z2) {
            if (this.mItem == null) {
                return;
            }
            canvas.save();
            canvas.concat(this.mBitmapToCanvas);
            this.mItem.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(this.mBorder.getBounds().centerX(), this.mBorder.getBounds().centerY());
            canvas.concat(this.mItem.mRotate);
            canvas.translate(-this.mBorder.getBounds().centerX(), -this.mBorder.getBounds().centerY());
            this.mBorder.draw(canvas);
            canvas.restore();
            if (!z) {
                this.mMirror.draw(canvas);
                this.mDelete.draw(canvas);
                this.mScale.draw(canvas);
            }
            if (!z2) {
                return;
            }
            this.mScale.draw(canvas);
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

        public void rotate(float f, float f2) {
            this.mReuseDegree.setSinCos(f, f2);
            this.mItem.rotate(this.mReuseDegree);
        }

        public void scale(float f) {
            this.mItem.scale(f);
        }

        public void translate(float f, float f2) {
            if (this.mItem == null) {
                return;
            }
            float[] fArr = this.mReuseVector;
            fArr[0] = f;
            fArr[1] = f2;
            this.mCanvasToBitmap.mapVectors(fArr);
            StickerNode stickerNode = this.mItem;
            float[] fArr2 = this.mReuseVector;
            stickerNode.translate(fArr2[0], fArr2[1]);
        }

        public boolean isScale(float f, float f2) {
            return this.mScale.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public boolean isDelete(float f, float f2) {
            return this.mDelete.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public boolean isMirror(float f, float f2) {
            return this.mMirror.getBounds().contains(Math.round(f), Math.round(f2));
        }

        public void setScaleAlpha(int i) {
            this.mScale.setAlpha(i);
        }

        public void setDeleteAlpha(int i) {
            this.mDelete.setAlpha(i);
        }

        public void setMirrorAlpha(int i) {
            this.mMirror.setAlpha(i);
        }

        public float getRadius() {
            return (float) Math.hypot(this.mScale.getBounds().centerX() - this.mDrawBounds.centerX(), this.mScale.getBounds().centerY() - this.mDrawBounds.centerY());
        }

        public float[] getVertex(int i) {
            if (i > 4) {
                throw new IllegalArgumentException("error params");
            }
            return this.mVertices[i];
        }

        public Rect getDrawBounds() {
            return this.mDrawBounds;
        }

        public RectF getBorderBounds() {
            return this.mReuseRect;
        }

        public void updateDisplayInfo() {
            StickerNode stickerNode = this.mItem;
            if (stickerNode == null || stickerNode.mDrawBounds == null) {
                return;
            }
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
            Drawable drawable = this.mDelete;
            float[][] fArr7 = this.mVertices;
            setBounds(drawable, fArr7[1][0], fArr7[1][1]);
            Drawable drawable2 = this.mMirror;
            float[][] fArr8 = this.mVertices;
            setBounds(drawable2, fArr8[0][0], fArr8[0][1]);
            Drawable drawable3 = this.mScale;
            float[][] fArr9 = this.mVertices;
            setBounds(drawable3, fArr9[3][0], fArr9[3][1]);
        }

        public static void setBounds(Drawable drawable, float f, float f2) {
            float intrinsicWidth = drawable.getIntrinsicWidth() / 2.0f;
            float intrinsicHeight = drawable.getIntrinsicHeight() / 2.0f;
            drawable.setBounds(Math.round(f - intrinsicWidth), Math.round(f2 - intrinsicHeight), Math.round(f + intrinsicWidth), Math.round(f2 + intrinsicHeight));
        }
    }

    /* compiled from: StickerEditorView.java */
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
        public ArrayList<StickerNode> mNodes = new ArrayList<>();
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

        public List<StickerNode> getNodes() {
            return (List) this.mNodes.clone();
        }

        public void append(StickerNode stickerNode) {
            if (stickerNode != null) {
                this.mNodes.add(stickerNode);
                stickerNode.draw(this.mCanvas);
            }
        }

        public boolean isEmpty() {
            return this.mNodes.isEmpty();
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

        public StickerNode remove(int i) {
            StickerNode remove = this.mNodes.remove(i);
            this.mRedrawSrc.set((int) Math.floor(remove.mDrawBounds.left), (int) Math.floor(remove.mDrawBounds.top), (int) Math.ceil(remove.mDrawBounds.right), (int) Math.ceil(remove.mDrawBounds.bottom));
            this.mRedrawDst.set(this.mRedrawSrc);
            long currentTimeMillis = System.currentTimeMillis();
            this.mCanvas.save();
            this.mCanvas.clipRect(this.mRedrawSrc);
            this.mCanvas.drawBitmap(this.mBackground, this.mRedrawSrc, this.mRedrawDst, this.mPaint);
            List<StickerNode> findIntersects = findIntersects(remove);
            DefaultLogger.d("StickerEditorView", "need rebuild %d items", Integer.valueOf(findIntersects.size()));
            for (StickerNode stickerNode : findIntersects) {
                stickerNode.draw(this.mCanvas);
            }
            this.mCanvas.restore();
            DefaultLogger.d(StickerNode.TAG, "rebuild finish costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return remove;
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(this.mLayer, this.mBitmapToCanvas, this.mPaint);
        }

        public final List<StickerNode> findIntersects(StickerNode stickerNode) {
            ArrayList arrayList = new ArrayList();
            Iterator<StickerNode> it = this.mNodes.iterator();
            while (it.hasNext()) {
                StickerNode next = it.next();
                if (RectF.intersects(next.mDrawBounds, stickerNode.mDrawBounds)) {
                    arrayList.add(next);
                }
            }
            return arrayList;
        }

        public void clear() {
            this.mNodes.clear();
            this.mLayer = this.mBackground.copy(Bitmap.Config.ARGB_8888, true);
            this.mCanvas = new Canvas(this.mLayer);
        }

        public void release() {
            this.mNodes.clear();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mMatrix, i);
        parcel.writeString(this.mPathName);
    }

    public StickerNode(Parcel parcel) {
        this.mMatrix = new ParcelableMatrix();
        this.mInvert = new Matrix();
        this.mRotate = new Matrix();
        this.mPaint = new Paint(3);
        this.mMatrixValue = new float[9];
        this.mMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mPathName = parcel.readString();
        initForParcelable();
    }
}
