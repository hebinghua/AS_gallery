package com.miui.gallery.vlog.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.vlog.R$drawable;
import java.util.List;

/* loaded from: classes2.dex */
public class TextEditorView extends View {
    public boolean canDelete;
    public boolean canEditor;
    public boolean canStyle;
    public Bitmap deleteImgBtn;
    public RectF deleteRectF;
    public Bitmap editorImgBtn;
    public RectF editorRectF;
    public boolean isInnerDrawRect;
    public double mClickMoveDistance;
    public Paint mDelPaint;
    public onDrawRectClickListener mDrawRectClickListener;
    public Paint mEditorPaint;
    public Paint mInRectPaint;
    public int mIndex;
    public List<PointF> mListPointF;
    public OnTouchListener mListener;
    public boolean mMoveOutScreen;
    public long mPrevMillionSecond;
    public Paint mRectPaint;
    public onStickerMuteListenser mStickerMuteListenser;
    public Paint mStylePaint;
    public List<List<PointF>> mSubListPointF;
    public Paint mSubRectPaint;
    public PointF prePointF;
    public Path rectPath;
    public Bitmap styleImgBtn;
    public RectF styleRectF;
    public int subCaptionIndex;
    public int viewMode;

    /* loaded from: classes2.dex */
    public interface OnTouchListener {
        void onBeyondDrawRectClick();

        void onDel();

        void onDrag(PointF pointF, PointF pointF2);

        void onEditorClick();

        void onScaleAndRotate(float f, PointF pointF, float f2);

        void onStyleClick();

        void onTouchDown(PointF pointF);
    }

    /* loaded from: classes2.dex */
    public interface onDrawRectClickListener {
        void onDrawRectClick(int i);
    }

    /* loaded from: classes2.dex */
    public interface onStickerMuteListenser {
    }

    public TextEditorView(Context context) {
        this(context, null);
    }

    public TextEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.prePointF = new PointF(0.0f, 0.0f);
        this.deleteRectF = new RectF();
        this.styleRectF = new RectF();
        this.editorRectF = new RectF();
        this.rectPath = new Path();
        this.canStyle = false;
        this.canDelete = false;
        this.isInnerDrawRect = false;
        this.canEditor = false;
        this.mIndex = 0;
        this.viewMode = 0;
        this.styleImgBtn = BitmapFactory.decodeResource(getResources(), R$drawable.vlog_caption_text_style);
        this.editorImgBtn = BitmapFactory.decodeResource(getResources(), R$drawable.vlog_caption_text_editor);
        this.deleteImgBtn = BitmapFactory.decodeResource(getResources(), R$drawable.vlog_caption_text_delete);
        this.mPrevMillionSecond = 0L;
        this.mClickMoveDistance = SearchStatUtils.POW;
        this.mRectPaint = new Paint();
        this.mInRectPaint = new Paint();
        this.mSubRectPaint = new Paint();
        this.mStylePaint = new Paint();
        this.mEditorPaint = new Paint();
        this.mDelPaint = new Paint();
        this.mMoveOutScreen = false;
        this.subCaptionIndex = -1;
        initRectPaint();
        initSubRectPaint();
        initInRectPaint();
        initButtonPaint();
    }

    private void setRectPath(List<PointF> list) {
        this.rectPath.reset();
        this.rectPath.moveTo(list.get(0).x, list.get(0).y);
        this.rectPath.lineTo(list.get(1).x, list.get(1).y);
        this.rectPath.lineTo(list.get(2).x, list.get(2).y);
        this.rectPath.lineTo(list.get(3).x, list.get(3).y);
        this.rectPath.close();
    }

    private void setInRectPath(List<PointF> list) {
        this.rectPath.reset();
        this.rectPath.moveTo(list.get(0).x + 1.1f, list.get(0).y + 1.1f);
        this.rectPath.lineTo(list.get(1).x + 1.1f, list.get(1).y - 1.1f);
        this.rectPath.lineTo(list.get(2).x - 1.1f, list.get(2).y - 1.1f);
        this.rectPath.lineTo(list.get(3).x - 1.1f, list.get(3).y + 1.1f);
        this.rectPath.close();
    }

    public final void initRectPaint() {
        this.mRectPaint.setColor(Color.parseColor("#0F000000"));
        this.mRectPaint.setAntiAlias(true);
        this.mRectPaint.setStrokeWidth(1.1f);
        this.mRectPaint.setStyle(Paint.Style.STROKE);
    }

    public final void initInRectPaint() {
        this.mInRectPaint.setColor(Color.parseColor("#80FFFFFF"));
        this.mInRectPaint.setAntiAlias(true);
        this.mInRectPaint.setStrokeWidth(5.0f);
        this.mInRectPaint.setStyle(Paint.Style.STROKE);
    }

    public final void initButtonPaint() {
        this.mStylePaint.setAntiAlias(true);
        this.mStylePaint.setStrokeWidth(3.025f);
        this.mStylePaint.setStyle(Paint.Style.STROKE);
        this.mEditorPaint.setAntiAlias(true);
        this.mEditorPaint.setStrokeWidth(3.025f);
        this.mEditorPaint.setStyle(Paint.Style.STROKE);
        this.mDelPaint.setAntiAlias(true);
        this.mDelPaint.setStrokeWidth(3.025f);
        this.mDelPaint.setStyle(Paint.Style.STROKE);
    }

    public final void initSubRectPaint() {
        this.mSubRectPaint.setColor(Color.parseColor("#9B9B9B"));
        this.mSubRectPaint.setAntiAlias(true);
        float f = 4;
        this.mSubRectPaint.setStrokeWidth(f);
        this.mSubRectPaint.setStyle(Paint.Style.STROKE);
        this.mSubRectPaint.setPathEffect(new DashPathEffect(new float[]{f, 2}, 0.0f));
    }

    public final int getSubCaptionIndex(int i, int i2) {
        List<List<PointF>> list = this.mSubListPointF;
        if (list == null) {
            return -1;
        }
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (clickPointIsInnerDrawRect(this.mSubListPointF.get(i3), i, i2)) {
                return i3;
            }
        }
        return -1;
    }

    public void setAlignIndex(int i) {
        this.mIndex = i;
        invalidate();
    }

    public void setDrawRect(List<PointF> list, int i) {
        this.mListPointF = list;
        this.viewMode = i;
        invalidate();
    }

    public List<PointF> getDrawRect() {
        return this.mListPointF;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mListener = onTouchListener;
    }

    public void setDrawRectClickListener(onDrawRectClickListener ondrawrectclicklistener) {
        this.mDrawRectClickListener = ondrawrectclicklistener;
    }

    public void setStickerMuteListenser(onStickerMuteListenser onstickermutelistenser) {
        this.mStickerMuteListenser = onstickermutelistenser;
    }

    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<PointF> list = this.mListPointF;
        if (list == null || list.size() != 4) {
            return;
        }
        setRectPath(this.mListPointF);
        canvas.drawPath(this.rectPath, this.mRectPaint);
        setInRectPath(this.mListPointF);
        canvas.drawPath(this.rectPath, this.mInRectPaint);
        if (this.viewMode == 3) {
            return;
        }
        canvas.drawBitmap(this.deleteImgBtn, this.mListPointF.get(0).x - (this.deleteImgBtn.getHeight() / 2), this.mListPointF.get(0).y - (this.deleteImgBtn.getWidth() / 2), this.mDelPaint);
        this.deleteRectF.set(this.mListPointF.get(0).x - (this.deleteImgBtn.getWidth() / 2), this.mListPointF.get(0).y - (this.deleteImgBtn.getHeight() / 2), this.mListPointF.get(0).x + (this.deleteImgBtn.getWidth() / 2), this.mListPointF.get(0).y + (this.deleteImgBtn.getHeight() / 2));
        canvas.drawBitmap(this.editorImgBtn, this.mListPointF.get(3).x - (this.editorImgBtn.getWidth() / 2), this.mListPointF.get(3).y - (this.editorImgBtn.getHeight() / 2), this.mEditorPaint);
        this.editorRectF.set(this.mListPointF.get(3).x - (this.editorImgBtn.getWidth() / 2), this.mListPointF.get(3).y - (this.editorImgBtn.getHeight() / 2), this.mListPointF.get(3).x + (this.editorImgBtn.getWidth() / 2), this.mListPointF.get(3).y + (this.editorImgBtn.getHeight() / 2));
        canvas.drawBitmap(this.styleImgBtn, this.mListPointF.get(2).x - (this.styleImgBtn.getHeight() / 2), this.mListPointF.get(2).y - (this.styleImgBtn.getWidth() / 2), this.mStylePaint);
        this.styleRectF.set(this.mListPointF.get(2).x - (this.styleImgBtn.getWidth() / 2), this.mListPointF.get(2).y - (this.styleImgBtn.getHeight() / 2), this.mListPointF.get(2).x + (this.styleImgBtn.getWidth() / 2), this.mListPointF.get(2).y + (this.styleImgBtn.getHeight() / 2));
    }

    public boolean curPointIsInnerDrawRect(int i, int i2) {
        return clickPointIsInnerDrawRect(this.mListPointF, i, i2);
    }

    public boolean clickPointIsInnerDrawRect(List<PointF> list, int i, int i2) {
        if (list == null || list.size() != 4) {
            return false;
        }
        RectF rectF = new RectF();
        Path path = new Path();
        path.moveTo(list.get(0).x, list.get(0).y);
        path.lineTo(list.get(1).x, list.get(1).y);
        path.lineTo(list.get(2).x, list.get(2).y);
        path.lineTo(list.get(3).x, list.get(3).y);
        path.close();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(i, i2);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnTouchListener onTouchListener;
        OnTouchListener onTouchListener2;
        OnTouchListener onTouchListener3;
        OnTouchListener onTouchListener4;
        OnTouchListener onTouchListener5;
        OnTouchListener onTouchListener6;
        OnTouchListener onTouchListener7;
        OnTouchListener onTouchListener8;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (this.mListPointF != null) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mPrevMillionSecond = System.currentTimeMillis();
                this.canStyle = this.styleRectF.contains(x, y);
                this.canEditor = this.editorRectF.contains(x, y);
                this.canDelete = this.deleteRectF.contains(x, y);
                OnTouchListener onTouchListener9 = this.mListener;
                if (onTouchListener9 != null) {
                    onTouchListener9.onTouchDown(new PointF(x, y));
                }
                List<PointF> list = this.mListPointF;
                if (list != null && list.size() == 4) {
                    this.isInnerDrawRect = curPointIsInnerDrawRect((int) x, (int) y);
                }
                if (this.isInnerDrawRect) {
                    this.subCaptionIndex = getSubCaptionIndex((int) x, (int) y);
                }
                this.prePointF.set(x, y);
                if (this.canStyle) {
                    this.mStylePaint.setAlpha(153);
                    invalidate();
                } else if (this.canEditor) {
                    this.mEditorPaint.setAlpha(153);
                    invalidate();
                } else if (this.canDelete) {
                    this.mDelPaint.setAlpha(153);
                    invalidate();
                }
            } else if (action == 1) {
                this.canStyle = this.styleRectF.contains(x, y);
                this.canEditor = this.editorRectF.contains(x, y);
                boolean contains = this.deleteRectF.contains(x, y);
                this.canDelete = contains;
                if (contains && (onTouchListener8 = this.mListener) != null) {
                    this.isInnerDrawRect = false;
                    onTouchListener8.onDel();
                }
                if (this.canStyle && (onTouchListener7 = this.mListener) != null) {
                    this.isInnerDrawRect = false;
                    onTouchListener7.onStyleClick();
                }
                if (this.canEditor && (onTouchListener6 = this.mListener) != null) {
                    this.isInnerDrawRect = false;
                    onTouchListener6.onEditorClick();
                }
                long currentTimeMillis = System.currentTimeMillis() - this.mPrevMillionSecond;
                if (this.mClickMoveDistance < 10.0d && currentTimeMillis <= 200) {
                    int i = this.viewMode;
                    if (i == 0) {
                        if (this.isInnerDrawRect && !this.canStyle && !this.canEditor && !this.canDelete) {
                            onDrawRectClickListener ondrawrectclicklistener = this.mDrawRectClickListener;
                            if (ondrawrectclicklistener != null) {
                                ondrawrectclicklistener.onDrawRectClick(0);
                            }
                        } else if (!this.canStyle && !this.canEditor && !this.canDelete && (onTouchListener5 = this.mListener) != null) {
                            onTouchListener5.onBeyondDrawRectClick();
                        }
                    } else if (i == 1) {
                        if (!this.isInnerDrawRect && !this.canStyle && !this.canEditor && !this.canDelete && (onTouchListener4 = this.mListener) != null) {
                            onTouchListener4.onBeyondDrawRectClick();
                        }
                    } else if (i == 3) {
                        if (!this.isInnerDrawRect && (onTouchListener3 = this.mListener) != null) {
                            onTouchListener3.onBeyondDrawRectClick();
                        }
                    } else if (i == 2) {
                        if (!this.isInnerDrawRect && (onTouchListener2 = this.mListener) != null) {
                            onTouchListener2.onBeyondDrawRectClick();
                        }
                    } else if (i == 4) {
                        if (this.isInnerDrawRect && !this.canStyle && !this.canEditor) {
                            onDrawRectClickListener ondrawrectclicklistener2 = this.mDrawRectClickListener;
                            if (ondrawrectclicklistener2 != null) {
                                ondrawrectclicklistener2.onDrawRectClick(this.subCaptionIndex);
                            }
                        } else if (!this.canStyle && !this.canEditor && (onTouchListener = this.mListener) != null) {
                            onTouchListener.onBeyondDrawRectClick();
                        }
                    }
                }
                this.canEditor = false;
                this.canStyle = false;
                this.isInnerDrawRect = false;
                this.canDelete = false;
                this.mClickMoveDistance = SearchStatUtils.POW;
                this.mStylePaint.setAlpha(255);
                this.mEditorPaint.setAlpha(255);
                this.mDelPaint.setAlpha(255);
                invalidate();
            } else if (action == 2) {
                this.mClickMoveDistance = Math.sqrt(Math.pow(x - this.prePointF.x, 2.0d) + Math.pow(y - this.prePointF.y, 2.0d));
                if (x <= 100.0f || x >= getWidth() || y >= getHeight() || y <= 20.0f) {
                    this.mMoveOutScreen = true;
                } else if (this.mMoveOutScreen) {
                    this.mMoveOutScreen = false;
                } else {
                    PointF pointF = new PointF();
                    List<PointF> list2 = this.mListPointF;
                    if (list2 != null && list2.size() == 4) {
                        pointF.x = (this.mListPointF.get(0).x + this.mListPointF.get(2).x) / 2.0f;
                        pointF.y = (this.mListPointF.get(0).y + this.mListPointF.get(2).y) / 2.0f;
                    }
                    if (this.mListener != null && this.canStyle) {
                        this.isInnerDrawRect = false;
                        float sqrt = (float) (Math.sqrt(Math.pow(x - pointF.x, 2.0d) + Math.pow(y - pointF.y, 2.0d)) / Math.sqrt(Math.pow(this.prePointF.x - pointF.x, 2.0d) + Math.pow(this.prePointF.y - pointF.y, 2.0d)));
                        double atan2 = Math.atan2(y - pointF.y, x - pointF.x);
                        PointF pointF2 = this.prePointF;
                        this.mListener.onScaleAndRotate(sqrt, new PointF(pointF.x, pointF.y), -((float) ((((float) (atan2 - Math.atan2(pointF2.y - pointF.y, pointF2.x - pointF.x))) * 180.0f) / 3.141592653589793d)));
                    }
                    OnTouchListener onTouchListener10 = this.mListener;
                    if (onTouchListener10 != null && this.isInnerDrawRect) {
                        onTouchListener10.onDrag(this.prePointF, new PointF(x, y));
                    }
                    this.prePointF.set(x, y);
                }
            }
        }
        return true;
    }
}
