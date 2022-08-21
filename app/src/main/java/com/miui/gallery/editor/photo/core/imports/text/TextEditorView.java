package com.miui.gallery.editor.photo.core.imports.text;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.text.TextElementOperationDrawable;
import com.miui.gallery.editor.photo.core.imports.text.TextFragment;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.utils.ParcelUtils;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureView;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes2.dex */
public class TextEditorView extends ProtectiveBitmapGestureView {
    public final int KEYBOARD_TAB_INDEX;
    public final int STYLE_TAB_INDEX;
    public Paint mAuxiliaryPaint;
    public CallBack mCallBack;
    public float mCanvasOffsetY;
    public Context mContext;
    public int mCurrentIndex;
    public float mCurrentTargetOffsetY;
    public Disposable mDisposable;
    public List<ITextDialogConfig> mITextDialogConfigs;
    public boolean mIsBoundBox;
    public boolean mIsClipCanvas;
    public boolean mIsMantle;
    public boolean mIsShowGuide;
    public Paint mLinePaint;
    public int[] mLocation;
    public Paint mMantlePaint;
    public ObjectAnimator mOffsetAnimator;
    public TextElementOperationDrawable mOperationDrawable;
    public RectF mRectFTemp;
    public Rect mRectTemp;
    public String mSignaturePath;
    public TextFragment.StatisticLogger mStatisticLogger;
    public Paint mStrokePaint;
    public Stack<ITextDialogConfig> mTextDialogStack;
    public TextEditorListener mTextEditorListener;
    public boolean mToInit;
    public ValueAnimator.AnimatorUpdateListener mUpdateListener;

    /* loaded from: classes2.dex */
    public interface CallBack {
        void onChangeSelection();

        void onDeleteTextDialog();
    }

    /* loaded from: classes2.dex */
    public interface TextEditorListener {
        void onClear();

        default void onItemEdit(int i) {
        }

        void onModify();
    }

    /* loaded from: classes2.dex */
    public enum TouchAction {
        NONE,
        DELETE,
        SCALE,
        MIRROR,
        EDIT,
        REVERSE_WHITE,
        REVERSE_BLACK,
        DATE,
        ADD,
        FONT,
        STYLE
    }

    public static /* synthetic */ void $r8$lambda$CHQMv_gKsPfXw71heDdi_TqYpXU(TextEditorView textEditorView, boolean z, BaseDialogModel baseDialogModel, ITextDialogConfig iTextDialogConfig, Drawable drawable) {
        textEditorView.lambda$loadDrawable$1(z, baseDialogModel, iTextDialogConfig, drawable);
    }

    public static /* synthetic */ void $r8$lambda$Nj8VcOtvmhslWuj_Q702sD2QdLg(TextEditorView textEditorView, boolean z, BaseDialogModel baseDialogModel, ITextDialogConfig iTextDialogConfig, Throwable th) {
        textEditorView.lambda$loadDrawable$2(z, baseDialogModel, iTextDialogConfig, th);
    }

    /* renamed from: $r8$lambda$QPmzHXdhqCDawVAxsSCj-OFH6gc */
    public static /* synthetic */ void m881$r8$lambda$QPmzHXdhqCDawVAxsSCjOFH6gc(TextEditorView textEditorView) {
        textEditorView.lambda$addNewItem$3();
    }

    public static /* synthetic */ void $r8$lambda$VHNMDcJtfZHUOCxi1jsm0J28Tnk(TextEditorView textEditorView, ObservableEmitter observableEmitter) {
        textEditorView.lambda$loadDrawable$0(observableEmitter);
    }

    public TextEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.KEYBOARD_TAB_INDEX = 0;
        this.STYLE_TAB_INDEX = 2;
        this.mITextDialogConfigs = new ArrayList();
        this.mMantlePaint = new Paint();
        this.mLinePaint = new Paint();
        this.mStrokePaint = new Paint();
        this.mRectFTemp = new RectF();
        this.mRectTemp = new Rect();
        this.mLocation = new int[2];
        this.mCanvasOffsetY = 0.0f;
        this.mCurrentTargetOffsetY = 0.0f;
        this.mAuxiliaryPaint = new Paint(1);
        this.mCurrentIndex = -1;
        this.mTextDialogStack = new Stack<>();
        this.mToInit = true;
        this.mIsBoundBox = true;
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView.1
            {
                TextEditorView.this = this;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextEditorView.this.invalidate();
            }
        };
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        setBackground(null);
        this.mAuxiliaryPaint.setColor(-1);
        this.mAuxiliaryPaint.setStyle(Paint.Style.STROKE);
        this.mAuxiliaryPaint.setStrokeWidth(1.0f);
        setFeatureGestureListener(new GesListener());
        this.mOperationDrawable = new TextElementOperationDrawable(getResources());
        this.mIsClipCanvas = true;
        setStrokeEnable(false);
        enableChildHandleMode();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void drawChild(Canvas canvas) {
        canvas.save();
        canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
        DefaultLogger.d("TextEditorView", "drawChild size : %d", Integer.valueOf(this.mITextDialogConfigs.size()));
        for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigs) {
            if (!iTextDialogConfig.isActivation()) {
                iTextDialogConfig.draw(canvas);
            }
        }
        canvas.restore();
        if (this.mIsMantle) {
            this.mMantlePaint.setAntiAlias(true);
            this.mMantlePaint.setColor(this.mContext.getResources().getColor(R.color.text_editor_mantle_color));
            this.mMantlePaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), this.mMantlePaint);
        }
        if (this.mIsShowGuide) {
            drawGuide(canvas);
        }
        int i = this.mCurrentIndex;
        if (i != -1) {
            this.mITextDialogConfigs.get(i).getOutLineRect(this.mRectFTemp);
            canvas.save();
            canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
            if (this.mIsClipCanvas) {
                canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
            }
            canvas.translate(0.0f, this.mCanvasOffsetY);
            this.mITextDialogConfigs.get(this.mCurrentIndex).draw(canvas);
            canvas.restore();
            if (!this.mIsBoundBox) {
                return;
            }
            canvas.save();
            if (this.mIsClipCanvas) {
                canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
            }
            canvas.translate(0.0f, this.mCanvasOffsetY);
            this.mOperationDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public final void drawGuide(Canvas canvas) {
        RectF rectF = this.mBitmapGestureParamsHolder.mBitmapDisplayRect;
        float f = rectF.top;
        float f2 = rectF.bottom;
        float f3 = rectF.left;
        float f4 = rectF.right;
        float f5 = f4 - f3;
        float f6 = f2 - f;
        float f7 = f5 / 2.0f;
        float f8 = f5 / 3.0f;
        float f9 = f6 / 2.0f;
        float f10 = f6 / 3.0f;
        this.mLinePaint.setColor(-1);
        this.mLinePaint.setStrokeWidth(1.0f);
        this.mLinePaint.setAlpha(204);
        this.mLinePaint.setPathEffect(null);
        this.mStrokePaint.setColor(-16777216);
        this.mStrokePaint.setStrokeWidth(1.0f);
        this.mStrokePaint.setAlpha(26);
        float f11 = f3 + f8;
        canvas.drawLine(f11, f, f11, f2, this.mLinePaint);
        float f12 = f11 - 1.0f;
        canvas.drawLine(f12, f, f12, f2, this.mStrokePaint);
        float f13 = f11 + 1.0f;
        canvas.drawLine(f13, f, f13, f2, this.mStrokePaint);
        float f14 = (f8 * 2.0f) + f3;
        canvas.drawLine(f14, f, f14, f2, this.mLinePaint);
        float f15 = f14 - 1.0f;
        canvas.drawLine(f15, f, f15, f2, this.mStrokePaint);
        float f16 = f14 + 1.0f;
        canvas.drawLine(f16, f, f16, f2, this.mStrokePaint);
        float f17 = f + f10;
        float f18 = f5 + f3;
        canvas.drawLine(f3, f17, f18, f17, this.mLinePaint);
        float f19 = f17 - 1.0f;
        canvas.drawLine(f3, f19, f18, f19, this.mStrokePaint);
        float f20 = f17 + 1.0f;
        canvas.drawLine(f3, f20, f18, f20, this.mStrokePaint);
        float f21 = f + (f10 * 2.0f);
        canvas.drawLine(f3, f21, f18, f21, this.mLinePaint);
        float f22 = f21 - 1.0f;
        canvas.drawLine(f3, f22, f18, f22, this.mStrokePaint);
        float f23 = f21 + 1.0f;
        canvas.drawLine(f3, f23, f18, f23, this.mStrokePaint);
        this.mLinePaint.setAlpha(204);
        this.mLinePaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 10.0f}, 0.0f));
        float f24 = f3 + f7;
        canvas.drawLine(f24, f, f24, f2, this.mLinePaint);
        float f25 = f + f9;
        canvas.drawLine(f3, f25, f4, f25, this.mLinePaint);
    }

    public final void configOperationPosition(ITextDialogConfig iTextDialogConfig) {
        iTextDialogConfig.getOutLineRect(this.mRectFTemp);
        this.mOperationDrawable.configDecorationPositon(this.mRectFTemp, this.mBitmapGestureParamsHolder.mCanvasMatrix, iTextDialogConfig.getRotateDegrees(), this.mRectFTemp.centerX(), this.mRectFTemp.centerY());
    }

    public final void configOperationDecoration(ITextDialogConfig iTextDialogConfig) {
        if (iTextDialogConfig.isWatermark()) {
            this.mOperationDrawable.configActionPosition(TextElementOperationDrawable.Action.EDIT, null, TextElementOperationDrawable.Action.SCALE, iTextDialogConfig.isReverseColor() ? TextElementOperationDrawable.Action.REVERSE_BLACK : TextElementOperationDrawable.Action.REVERSE_WHITE, getResources());
        } else if (iTextDialogConfig.isSignature()) {
            this.mOperationDrawable.configActionPosition(TextElementOperationDrawable.Action.EDIT, TextElementOperationDrawable.Action.DATE, TextElementOperationDrawable.Action.SCALE, null, getResources());
        } else if (!iTextDialogConfig.isDialogEnable()) {
            this.mOperationDrawable.configActionPosition(TextElementOperationDrawable.Action.ADD, TextElementOperationDrawable.Action.DELETE, TextElementOperationDrawable.Action.SCALE, null, getResources());
        } else if (iTextDialogConfig.getDialogModel().isBubbleModel()) {
            this.mOperationDrawable.configActionPosition(TextElementOperationDrawable.Action.EDIT, null, TextElementOperationDrawable.Action.SCALE, TextElementOperationDrawable.Action.MIRROR, getResources());
        } else {
            this.mOperationDrawable.configActionPosition(null, null, TextElementOperationDrawable.Action.SCALE, iTextDialogConfig.isReverseColor() ? TextElementOperationDrawable.Action.REVERSE_BLACK : TextElementOperationDrawable.Action.REVERSE_WHITE, getResources());
        }
    }

    public final void updateOperationDrawable(boolean z, boolean z2) {
        TextElementOperationDrawable.Action action;
        if (z) {
            action = TextElementOperationDrawable.Action.REVERSE_WHITE;
        } else {
            action = TextElementOperationDrawable.Action.REVERSE_BLACK;
        }
        TextElementOperationDrawable.Action action2 = action;
        if (z2) {
            this.mOperationDrawable.configActionPosition(TextElementOperationDrawable.Action.EDIT, null, TextElementOperationDrawable.Action.SCALE, action2, getResources());
        } else {
            this.mOperationDrawable.configActionPosition(null, null, TextElementOperationDrawable.Action.SCALE, action2, getResources());
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        super.onCanvasMatrixChange();
        int i = this.mCurrentIndex;
        if (i != -1) {
            configOperationPosition(this.mITextDialogConfigs.get(i));
        }
    }

    public final void notifyModify() {
        TextEditorListener textEditorListener = this.mTextEditorListener;
        if (textEditorListener != null) {
            textEditorListener.onModify();
        }
    }

    public final void notifyClear() {
        TextEditorListener textEditorListener = this.mTextEditorListener;
        if (textEditorListener != null) {
            textEditorListener.onClear();
        }
    }

    public final void notifyItemEdit(int i) {
        TextEditorListener textEditorListener = this.mTextEditorListener;
        if (textEditorListener != null) {
            textEditorListener.onItemEdit(i);
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        super.onBitmapMatrixChanged();
        if (this.mITextDialogConfigs.isEmpty() && this.mToInit) {
            addNewItem(null);
            this.mToInit = false;
        }
        for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigs) {
            if (iTextDialogConfig != null) {
                iTextDialogConfig.setImageInitDisplayRect(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
                countAndInvalidate(iTextDialogConfig, false, true);
                configOperationPosition(iTextDialogConfig);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class GesListener implements BitmapGestureView.FeatureGesListener {
        public float mBaseDegrees;
        public float mBaseDistance;
        public float mCenterX;
        public float mCenterY;
        public ITextDialogConfig mCurrentConfig;
        public ITextDialogConfig mDownConfig;
        public int mDownIndex;
        public float mDownX;
        public float mDownY;
        public boolean mNeedInit;
        public float[] mPointTemp1;
        public float[] mPointTemp2;
        public float mPreDegrees;
        public float mPreScale;
        public RectF mRectF;
        public float[] mStartXs;
        public float[] mStartYs;
        public TouchAction mTouchAction;

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public GesListener() {
            TextEditorView.this = r2;
            this.mTouchAction = TouchAction.NONE;
            this.mDownIndex = -1;
            this.mBaseDistance = 0.0f;
            this.mBaseDegrees = 0.0f;
            this.mCenterX = 0.0f;
            this.mCenterY = 0.0f;
            this.mRectF = new RectF();
            this.mNeedInit = false;
            this.mPreScale = 1.0f;
            this.mPreDegrees = 0.0f;
            this.mStartXs = new float[2];
            this.mStartYs = new float[2];
            this.mPointTemp1 = new float[2];
            this.mPointTemp2 = new float[2];
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onMultiPointDown(MotionEvent motionEvent) {
            this.mStartXs[0] = motionEvent.getX(0);
            this.mStartYs[0] = motionEvent.getY(0);
            this.mStartXs[1] = motionEvent.getX(1);
            this.mStartYs[1] = motionEvent.getY(1);
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onMultiPointMove(MotionEvent motionEvent) {
            if (TextEditorView.this.mCurrentIndex == -1) {
                return true;
            }
            float[] fArr = {motionEvent.getX(0), motionEvent.getX(1)};
            float[] fArr2 = {motionEvent.getY(0), motionEvent.getY(1)};
            if (this.mNeedInit) {
                this.mCenterX = this.mRectF.centerX();
                this.mCenterY = this.mRectF.centerY();
                float[] fArr3 = this.mStartXs;
                float[] fArr4 = this.mStartYs;
                this.mBaseDistance = (float) Math.hypot(fArr3[0] - fArr3[1], fArr4[0] - fArr4[1]);
                float[] fArr5 = this.mStartYs;
                float[] fArr6 = this.mStartXs;
                this.mBaseDegrees = (float) Math.atan2(fArr5[0] - fArr5[1], fArr6[0] - fArr6[1]);
                this.mPreScale = this.mCurrentConfig.getUserScaleMultiple();
                this.mPreDegrees = this.mCurrentConfig.getRotateDegrees();
                this.mNeedInit = false;
            }
            double hypot = Math.hypot(fArr[0] - fArr[1], fArr2[0] - fArr2[1]);
            double atan2 = Math.atan2(fArr2[0] - fArr2[1], fArr[0] - fArr[1]);
            float f = (float) (hypot / this.mBaseDistance);
            double degrees = Math.toDegrees(atan2 - this.mBaseDegrees);
            this.mCurrentConfig.setUserScaleMultiple(f * this.mPreScale);
            this.mCurrentConfig.setRotateDegrees(((float) degrees) + this.mPreDegrees);
            this.mCurrentConfig.setDrawOutline(false);
            TextEditorView.this.mOperationDrawable.setDrawDecoration(false);
            TextEditorView.this.countAndInvalidate(this.mCurrentConfig, false, false);
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            TextEditorView.this.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            float[] fArr = this.mPointTemp1;
            float f = fArr[0];
            this.mDownX = f;
            float f2 = fArr[1];
            this.mDownY = f2;
            int findItemByEvent = TextEditorView.this.findItemByEvent(f, f2);
            this.mDownIndex = findItemByEvent;
            if (findItemByEvent != -1) {
                ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) TextEditorView.this.mITextDialogConfigs.get(this.mDownIndex);
                this.mDownConfig = iTextDialogConfig;
                iTextDialogConfig.getOutLineRect(this.mRectF);
            } else {
                this.mDownConfig = null;
            }
            if (TextEditorView.this.mCurrentIndex != -1) {
                this.mTouchAction = TextEditorView.this.findTouchAction(motionEvent.getX(), motionEvent.getY());
                ITextDialogConfig iTextDialogConfig2 = (ITextDialogConfig) TextEditorView.this.mITextDialogConfigs.get(TextEditorView.this.mCurrentIndex);
                this.mCurrentConfig = iTextDialogConfig2;
                this.mCenterX = -1.0f;
                this.mNeedInit = true;
                iTextDialogConfig2.getOutLineRect(this.mRectF);
            } else {
                this.mTouchAction = TouchAction.NONE;
                this.mCurrentConfig = null;
            }
            DefaultLogger.d("TextEditorView", "mTouchAction : %s", this.mTouchAction);
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            DefaultLogger.d("TextEditorView", "click number： %d", Integer.valueOf(this.mDownIndex));
            TextEditorView.this.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            if (TextEditorView.this.mCurrentIndex == -1) {
                int i = this.mDownIndex;
                if (i == -1) {
                    return;
                }
                TextEditorView.this.setActivation(i);
            } else if (this.mCurrentConfig == null) {
            } else {
                switch (AnonymousClass2.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[this.mTouchAction.ordinal()]) {
                    case 1:
                        if (!this.mCurrentConfig.isDialogEnable()) {
                            return;
                        }
                        this.mCurrentConfig.toggleMirror();
                        TextEditorView.this.countAndInvalidate(this.mCurrentConfig, true, true);
                        return;
                    case 2:
                        if (!this.mCurrentConfig.isWatermark() && (!this.mCurrentConfig.isDialogEnable() || this.mCurrentConfig.getDialogModel().isBubbleModel())) {
                            return;
                        }
                        this.mCurrentConfig.reverseColor(-16777216);
                        TextEditorView.this.updateOperationDrawable(false, this.mCurrentConfig.isWatermark());
                        TextEditorView.this.invalidate();
                        return;
                    case 3:
                        if (!this.mCurrentConfig.isWatermark() && (!this.mCurrentConfig.isDialogEnable() || this.mCurrentConfig.getDialogModel().isBubbleModel())) {
                            return;
                        }
                        this.mCurrentConfig.reverseColor(-1);
                        TextEditorView.this.updateOperationDrawable(true, this.mCurrentConfig.isWatermark());
                        TextEditorView.this.invalidate();
                        return;
                    case 4:
                        TextEditorView.this.removeLastOperationItem();
                        return;
                    case 5:
                        if (this.mCurrentConfig.getDialogModel() != null && !this.mCurrentConfig.getDialogModel().isBubbleModel()) {
                            return;
                        }
                        TextEditorView.this.notifyItemEdit(0);
                        return;
                    case 6:
                        return;
                    case 7:
                        TextEditorView.this.toggleSignatureShowDate();
                        return;
                    case 8:
                        TextEditorView.this.copyItem();
                        return;
                    case 9:
                        TextEditorView.this.notifyItemEdit(2);
                        TextEditorView.this.mIsBoundBox = false;
                        return;
                    default:
                        ITextDialogConfig iTextDialogConfig = this.mCurrentConfig;
                        float[] fArr = this.mPointTemp1;
                        if (!iTextDialogConfig.contains(fArr[0], fArr[1])) {
                            TextEditorView.this.clearActivation(false);
                            TextEditorView.this.setActivation(this.mDownIndex);
                            return;
                        } else if (this.mCurrentConfig.getDialogModel() != null && !this.mCurrentConfig.getDialogModel().isBubbleModel()) {
                            return;
                        } else {
                            if (!this.mCurrentConfig.isSignature()) {
                                TextEditorView.this.mIsBoundBox = false;
                            }
                            TextEditorView.this.notifyItemEdit(0);
                            return;
                        }
                }
            }
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!TextEditorView.this.mITextDialogConfigs.isEmpty() && TextEditorView.this.mCurrentIndex != -1) {
                TextEditorView.this.mIsShowGuide = true;
            }
            TextEditorView.this.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            TextEditorView.this.convertPointToViewPortCoordinate(motionEvent2, this.mPointTemp2);
            float[] fArr = this.mPointTemp2;
            float f3 = fArr[0];
            float f4 = fArr[1];
            float[] fArr2 = this.mPointTemp1;
            float f5 = fArr2[0];
            float f6 = fArr2[1];
            float convertDistanceX = TextEditorView.this.convertDistanceX(f);
            float convertDistanceY = TextEditorView.this.convertDistanceY(f2);
            if (AnonymousClass2.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[this.mTouchAction.ordinal()] != 6) {
                if (TextEditorView.this.mCurrentIndex != -1) {
                    doScroll(convertDistanceX, convertDistanceY, this.mCurrentConfig);
                } else {
                    int i = this.mDownIndex;
                    if (i != -1) {
                        TextEditorView.this.setActivation(i);
                        ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) TextEditorView.this.mITextDialogConfigs.get(this.mDownIndex);
                        this.mCurrentConfig = iTextDialogConfig;
                        doScroll(convertDistanceX, convertDistanceY, iTextDialogConfig);
                    }
                }
            } else {
                if (this.mNeedInit) {
                    this.mCenterX = this.mRectF.centerX();
                    float centerY = this.mRectF.centerY();
                    this.mCenterY = centerY;
                    this.mBaseDistance = (float) Math.hypot(this.mCenterX - f5, centerY - f6);
                    this.mBaseDegrees = (float) Math.atan2(f6 - this.mCenterY, f5 - this.mCenterX);
                    this.mPreScale = this.mCurrentConfig.getUserScaleMultiple();
                    this.mPreDegrees = this.mCurrentConfig.getRotateDegrees();
                    this.mNeedInit = false;
                }
                double hypot = Math.hypot(this.mCenterX - f3, this.mCenterY - f4);
                double atan2 = Math.atan2(f4 - this.mCenterY, f3 - this.mCenterX);
                float f7 = (float) (hypot / this.mBaseDistance);
                double degrees = Math.toDegrees(atan2 - this.mBaseDegrees);
                this.mCurrentConfig.setUserScaleMultiple(f7 * this.mPreScale);
                this.mCurrentConfig.setRotateDegrees(((float) degrees) + this.mPreDegrees);
                this.mCurrentConfig.setDrawOutline(false);
                TextEditorView.this.mOperationDrawable.setDrawDecoration(false);
                TextEditorView.this.countAndInvalidate(this.mCurrentConfig, false, false);
            }
            ITextDialogConfig iTextDialogConfig2 = this.mCurrentConfig;
            if (iTextDialogConfig2 != null) {
                TextEditorView.this.configOperationPosition(iTextDialogConfig2);
            }
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            TextEditorView.this.mIsShowGuide = false;
            int findActivationIndex = TextEditorView.this.findActivationIndex();
            if (findActivationIndex >= 0) {
                ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) TextEditorView.this.mITextDialogConfigs.get(findActivationIndex);
                iTextDialogConfig.refreshRotateDegree();
                iTextDialogConfig.setDrawOutline(true);
                TextEditorView.this.countAndInvalidate(iTextDialogConfig, true, false);
                TextEditorView.this.notifyModify();
            }
            TextEditorView.this.mOperationDrawable.setDrawDecoration(true);
            TextEditorView.this.invalidate();
        }

        public final void doScroll(float f, float f2, ITextDialogConfig iTextDialogConfig) {
            if (iTextDialogConfig != null) {
                iTextDialogConfig.getOutLineRect(TextEditorView.this.mRectFTemp);
                TextEditorView.this.mBitmapGestureParamsHolder.mCanvasMatrix.mapRect(TextEditorView.this.mRectFTemp);
                TextEditorView textEditorView = TextEditorView.this;
                int rectOverScrollStatus = textEditorView.getRectOverScrollStatus(textEditorView.mRectFTemp);
                if ((rectOverScrollStatus & 8) == 0 ? !((rectOverScrollStatus & 4) == 0 || f >= 0.0f) : f > 0.0f) {
                    f = 0.0f;
                }
                if ((rectOverScrollStatus & 2) == 0 ? !((rectOverScrollStatus & 1) == 0 || f2 >= 0.0f) : f2 > 0.0f) {
                    f2 = 0.0f;
                }
                iTextDialogConfig.appendUserLocationX(-f);
                iTextDialogConfig.appendUserLocationY(-f2);
                TextEditorView.this.countAndInvalidate(iTextDialogConfig, false, false);
                TextEditorView.this.configOperationPosition(this.mCurrentConfig);
                this.mCurrentConfig.setDrawOutline(false);
                TextEditorView.this.mOperationDrawable.setDrawDecoration(false);
                TextEditorView.this.invalidate();
            }
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ITextDialogConfig iTextDialogConfig = this.mCurrentConfig;
            if (iTextDialogConfig != null) {
                iTextDialogConfig.setUserScaleMultiple(iTextDialogConfig.getUserScaleMultiple() * scaleGestureDetector.getScaleFactor());
            }
            TextEditorView.this.invalidate();
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (TextEditorView.this.mCurrentIndex != -1) {
                this.mCurrentConfig = (ITextDialogConfig) TextEditorView.this.mITextDialogConfigs.get(TextEditorView.this.mCurrentIndex);
                return false;
            }
            this.mCurrentConfig = null;
            return false;
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.TextEditorView$2 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction;

        static {
            int[] iArr = new int[TouchAction.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction = iArr;
            try {
                iArr[TouchAction.MIRROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.REVERSE_WHITE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.REVERSE_BLACK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.DELETE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.EDIT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.SCALE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.DATE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.ADD.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.STYLE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$TextEditorView$TouchAction[TouchAction.NONE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public final void copyItem() {
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        TextAppendConfig textAppendConfig = (TextAppendConfig) ParcelUtils.copy((TextAppendConfig) iTextDialogConfig);
        if (iTextDialogConfig.getTextStyle() != null) {
            textAppendConfig.getTextStyle().setTypeFace(iTextDialogConfig.getTextStyle().getTypeFace());
        }
        textAppendConfig.appendUserLocationX(getResources().getDimensionPixelOffset(R.dimen.copy_text_append_config_offset));
        RectF rectF = new RectF();
        textAppendConfig.getOutLineRect(rectF);
        textAppendConfig.appendUserLocationY(rectF.bottom - rectF.top);
        this.mITextDialogConfigs.add(textAppendConfig);
        countAndInvalidate(textAppendConfig, false, true);
        setLastItemActivation();
        TextFragment.StatisticLogger statisticLogger = this.mStatisticLogger;
        if (statisticLogger != null) {
            statisticLogger.statisticLog("text_copy_click");
        }
    }

    public final void toggleSignatureShowDate() {
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        if (!(iTextDialogConfig instanceof SignatureAppendConfig) || !iTextDialogConfig.isSignature()) {
            return;
        }
        SignatureAppendConfig signatureAppendConfig = (SignatureAppendConfig) iTextDialogConfig;
        signatureAppendConfig.setShowTimeStamp(!signatureAppendConfig.isShowTimeStamp());
        invalidate();
    }

    public final void countAndInvalidate(ITextDialogConfig iTextDialogConfig, boolean z, boolean z2) {
        iTextDialogConfig.countLocation(z2, this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect.width());
        if (z) {
            iTextDialogConfig.getOutLineRect(this.mRectFTemp);
            this.mRectFTemp.roundOut(this.mRectTemp);
            invalidate(this.mRectTemp);
            return;
        }
        invalidate();
    }

    public final TouchAction findTouchAction(float f, float f2) {
        int round = Math.round(f);
        int round2 = Math.round(f2);
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.ADD, this.mRectFTemp);
        float f3 = round;
        float f4 = round2;
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.ADD;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.STYLE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.STYLE;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.DELETE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.DELETE;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.SCALE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.SCALE;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.MIRROR, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.MIRROR;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.EDIT, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.EDIT;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.REVERSE_WHITE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.REVERSE_WHITE;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.REVERSE_BLACK, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.REVERSE_BLACK;
        }
        this.mOperationDrawable.getDecorationRect(TextElementOperationDrawable.Action.DATE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.DATE;
        }
        return TouchAction.NONE;
    }

    public final int findItemByEvent(float f, float f2) {
        int i = -1;
        float f3 = -1.0f;
        for (int i2 = 0; i2 < this.mITextDialogConfigs.size(); i2++) {
            ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(i2);
            if (iTextDialogConfig.contains(f, f2)) {
                iTextDialogConfig.getOutLineRect(this.mRectFTemp);
                float distance = getDistance(this.mRectFTemp, f, f2);
                if (f3 == -1.0f) {
                    i = i2;
                    f3 = distance;
                } else if (distance <= f3) {
                    i = i2;
                }
            }
        }
        return i;
    }

    public final float getDistance(RectF rectF, float f, float f2) {
        return (float) Math.hypot(rectF.centerX() - f, rectF.centerY() - f2);
    }

    public TextEntry export() {
        TextEntry textEntry = new TextEntry(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect, this.mITextDialogConfigs, getResources());
        if (this.mStatisticLogger == null) {
            return textEntry;
        }
        for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigs) {
            if (iTextDialogConfig.isStroke()) {
                this.mStatisticLogger.statisticLog("open_text_stroke");
            }
            if (iTextDialogConfig.isSubstrate()) {
                this.mStatisticLogger.statisticLog("open_text_substrate");
            }
            if (iTextDialogConfig.getSubstrateColors() != null) {
                this.mStatisticLogger.statisticLog("select_substrate_color");
            }
        }
        return textEntry;
    }

    public void setTextEditorListener(TextEditorListener textEditorListener) {
        this.mTextEditorListener = textEditorListener;
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    /* loaded from: classes2.dex */
    public static class TextEntry implements Parcelable {
        public static final Parcelable.Creator<TextEntry> CREATOR = new Parcelable.Creator<TextEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView.TextEntry.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public TextEntry mo882createFromParcel(Parcel parcel) {
                return new TextEntry(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public TextEntry[] mo883newArray(int i) {
                return new TextEntry[i];
            }
        };
        public List<ITextDialogConfig> mITextDialogConfigList;
        public RectF mRectF;
        public Resources mResource;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public TextEntry(RectF rectF, List<ITextDialogConfig> list, Resources resources) {
            RectF rectF2 = new RectF();
            this.mRectF = rectF2;
            rectF2.set(rectF);
            this.mITextDialogConfigList = new ArrayList(list);
            this.mResource = resources;
        }

        public Bitmap apply(Bitmap bitmap) {
            if (!bitmap.isMutable()) {
                bitmap = Bitmaps.copyBitmapAndRecycle(bitmap);
            }
            if (bitmap == null) {
                return null;
            }
            RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.setRectToRect(this.mRectF, rectF, Matrix.ScaleToFit.FILL);
            Canvas canvas = new Canvas(bitmap);
            canvas.concat(matrix);
            for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigList) {
                iTextDialogConfig.setDrawOutline(false);
                iTextDialogConfig.draw(canvas);
            }
            return bitmap;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mRectF, i);
            parcel.writeList(this.mITextDialogConfigList);
        }

        public TextEntry(Parcel parcel) {
            this.mRectF = new RectF();
            this.mRectF = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            ArrayList arrayList = new ArrayList();
            this.mITextDialogConfigList = arrayList;
            parcel.readList(arrayList, ITextDialogConfig.class.getClassLoader());
        }
    }

    public boolean isIndexValid(int i) {
        return i >= 0 && i < this.mITextDialogConfigs.size();
    }

    public boolean isItemActivation() {
        return this.mCurrentIndex != -1;
    }

    public String getItemText() {
        return !isIndexValid(this.mCurrentIndex) ? "" : this.mITextDialogConfigs.get(this.mCurrentIndex).getText();
    }

    public float getItemTransparent() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return 0.0f;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).getTextTransparent();
    }

    public int getItemColor() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return 0;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).getColor();
    }

    public AutoLineLayout.TextAlignment getItemAlignment() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return AutoLineLayout.TextAlignment.LEFT;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).getTextAlignment();
    }

    public void getCurrentItemStatus(DialogStatusData dialogStatusData) {
        ITextDialogConfig lastOperationItem = getLastOperationItem();
        if (lastOperationItem == null) {
            dialogStatusData.setEmpty();
            return;
        }
        dialogStatusData.color = lastOperationItem.getColor();
        dialogStatusData.transparentProgress = lastOperationItem.getTextTransparent();
        dialogStatusData.textStyle = lastOperationItem.getTextStyle();
        dialogStatusData.textBold = lastOperationItem.isBoldText();
        dialogStatusData.textShadow = lastOperationItem.isShadow();
        dialogStatusData.textAlignment = lastOperationItem.getTextAlignment();
        dialogStatusData.itemPositionX = lastOperationItem.getUserLocationX();
        dialogStatusData.itemPositionY = lastOperationItem.getUserLocationY();
        dialogStatusData.itemScale = lastOperationItem.getUserScaleMultiple();
        dialogStatusData.itemDegree = lastOperationItem.getRotateDegrees();
        dialogStatusData.isStroke = lastOperationItem.isStroke();
        dialogStatusData.isSubstrate = lastOperationItem.isSubstrate();
        dialogStatusData.mName = lastOperationItem.getName();
        dialogStatusData.mSubstrateColors = lastOperationItem.getSubstrateColors();
        dialogStatusData.gradientsColor = lastOperationItem.getGradientsColor();
    }

    public void getDialogStatusData(DialogStatusData dialogStatusData) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        this.mITextDialogConfigs.get(this.mCurrentIndex).getDialogStatusData(dialogStatusData);
    }

    public void enableStatusForCurrentItem(DialogStatusData dialogStatusData, boolean z) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setDialogWithStatusData(dialogStatusData);
        if (z) {
            float centerX = this.mBitmapGestureParamsHolder.mDisplayRect.centerX();
            float centerY = this.mBitmapGestureParamsHolder.mDisplayRect.centerY();
            float centerX2 = this.mBitmapGestureParamsHolder.mDisplayInsideRect.centerX();
            float centerY2 = this.mBitmapGestureParamsHolder.mDisplayInsideRect.centerY();
            iTextDialogConfig.appendUserLocationX(centerX2 - centerX);
            iTextDialogConfig.appendUserLocationY(centerY2 - centerY);
        }
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationPosition(iTextDialogConfig);
    }

    public BaseDialogModel getItemDialogModel() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return null;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).getDialogModel();
    }

    public ITextDialogConfig getItemTextDialogConfig() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return null;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex);
    }

    public boolean getItemBold() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return false;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).isBoldText();
    }

    public boolean getItemShadow() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return false;
        }
        return this.mITextDialogConfigs.get(this.mCurrentIndex).isShadow();
    }

    public void setItemText(String str) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        if (TextUtils.equals(str, iTextDialogConfig.getText())) {
            return;
        }
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setText(str);
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationPosition(iTextDialogConfig);
    }

    public void setSignaturePath(String str) {
        this.mSignaturePath = str;
    }

    public void setItemDialogModel(BaseDialogModel baseDialogModel) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        if (iTextDialogConfig.isSignature()) {
            loadDrawable(false, baseDialogModel, iTextDialogConfig);
            return;
        }
        iTextDialogConfig.setDialogModel(baseDialogModel, getResources());
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationDecoration(iTextDialogConfig);
        configOperationPosition(iTextDialogConfig);
    }

    public ITextDialogConfig getLastOperationItem() {
        if (isIndexValid(this.mCurrentIndex)) {
            return this.mITextDialogConfigs.get(this.mCurrentIndex);
        }
        while (!this.mTextDialogStack.empty()) {
            if (this.mITextDialogConfigs.contains(this.mTextDialogStack.peek())) {
                return this.mTextDialogStack.peek();
            }
            this.mTextDialogStack.pop();
        }
        return null;
    }

    public void updateItemSignature(String str, int i) {
        this.mSignaturePath = str;
        ITextDialogConfig lastOperationItem = getLastOperationItem();
        if (lastOperationItem != null && (lastOperationItem instanceof SignatureAppendConfig)) {
            ((SignatureAppendConfig) lastOperationItem).setSignaturePath(str);
            loadDrawable(true, lastOperationItem.getDialogModel(), lastOperationItem);
        }
    }

    public final void loadDrawable(final boolean z, final BaseDialogModel baseDialogModel, final ITextDialogConfig iTextDialogConfig) {
        this.mDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                TextEditorView.$r8$lambda$VHNMDcJtfZHUOCxi1jsm0J28Tnk(TextEditorView.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                TextEditorView.$r8$lambda$CHQMv_gKsPfXw71heDdi_TqYpXU(TextEditorView.this, z, baseDialogModel, iTextDialogConfig, (Drawable) obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                TextEditorView.$r8$lambda$Nj8VcOtvmhslWuj_Q702sD2QdLg(TextEditorView.this, z, baseDialogModel, iTextDialogConfig, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$loadDrawable$0(ObservableEmitter observableEmitter) throws Exception {
        if (TextUtils.isEmpty(this.mSignaturePath)) {
            observableEmitter.onNext(null);
            return;
        }
        observableEmitter.onNext(new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeFile(this.mSignaturePath)));
    }

    public /* synthetic */ void lambda$loadDrawable$2(boolean z, BaseDialogModel baseDialogModel, ITextDialogConfig iTextDialogConfig, Throwable th) throws Exception {
        lambda$loadDrawable$1(z, baseDialogModel, iTextDialogConfig, null);
    }

    /* renamed from: setSignatureDrawable */
    public final void lambda$loadDrawable$1(boolean z, BaseDialogModel baseDialogModel, ITextDialogConfig iTextDialogConfig, Drawable drawable) {
        if (z) {
            iTextDialogConfig.setSignatureDrawable(baseDialogModel, drawable);
            invalidate();
            return;
        }
        iTextDialogConfig.setSignatureDrawable(baseDialogModel, drawable);
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationDecoration(iTextDialogConfig);
        configOperationPosition(iTextDialogConfig);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                this.mDisposable.dispose();
            }
            this.mDisposable = null;
        }
        for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigs) {
            iTextDialogConfig.onDetachedFromWindow();
        }
    }

    public void setItemTextColor(int i) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setColor(i);
        iTextDialogConfig.setGradientsColor(0);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public void setItemTextColor(int i, int i2) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setColor(i);
        iTextDialogConfig.setGradientsColor(i2);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public void setItemTypeface(TextStyle textStyle) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setTextStyle(textStyle);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    public void setItemBold(boolean z) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setBoldText(z);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    public void setItemShadow(boolean z) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setShadow(z);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    public void setItemTextAlignment(AutoLineLayout.TextAlignment textAlignment) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setTextAlignment(textAlignment);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, false);
    }

    public void setItemTransparent(float f) {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setTextTransparent(f);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public void refreshTextDialog() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public void clear() {
        DefaultLogger.d("TextEditorView", "clear");
        this.mITextDialogConfigs.clear();
        this.mCurrentIndex = -1;
        invalidate();
    }

    public final int findActivationIndex() {
        for (int i = 0; i < this.mITextDialogConfigs.size(); i++) {
            if (this.mITextDialogConfigs.get(i).isActivation()) {
                return i;
            }
        }
        return -1;
    }

    public void clearActivation(boolean z) {
        int i = 0;
        while (i < this.mITextDialogConfigs.size()) {
            ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(i);
            if (iTextDialogConfig.getText() == null) {
                this.mITextDialogConfigs.remove(i);
                notifyClear();
                i--;
            } else {
                iTextDialogConfig.setDrawOutline(false);
                iTextDialogConfig.setActivation(false);
            }
            i++;
        }
        this.mCurrentIndex = -1;
        if (z) {
            invalidate();
        }
    }

    public void setActivation(int i) {
        clearActivation(false);
        if (i < 0 || i >= this.mITextDialogConfigs.size() || this.mITextDialogConfigs.get(i).isActivation()) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(i);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setDrawOutline(true);
        countAndInvalidate(iTextDialogConfig, false, false);
        configOperationDecoration(iTextDialogConfig);
        configOperationPosition(iTextDialogConfig);
        this.mTextDialogStack.push(iTextDialogConfig);
        this.mCurrentIndex = i;
        notifySelectionChange();
        invalidate();
    }

    public void notifySelectionChange() {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onChangeSelection();
        }
    }

    public void notifyRemoveItem() {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onDeleteTextDialog();
        }
    }

    public void setLastItemActivation() {
        setActivation(this.mITextDialogConfigs.size() - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addNewItem(TextConfig textConfig) {
        TextAppendConfig textAppendConfig;
        if (textConfig == null || (!textConfig.isWatermark() && !textConfig.isSignature())) {
            textAppendConfig = new TextAppendConfig(this.mContext);
        } else if (textConfig.isSignature()) {
            SignatureAppendConfig signatureAppendConfig = new SignatureAppendConfig(this.mContext);
            signatureAppendConfig.setSignaturePath(textConfig.getSignatureInfo().path);
            textAppendConfig = signatureAppendConfig;
        } else {
            WatermarkDialog watermarkDialog = new WatermarkDialog(getResources(), textConfig.getWatermarkInfo());
            watermarkDialog.setImageInitDisplayRect(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
            watermarkDialog.init();
            watermarkDialog.setBitmapLoadingListener(new WatermarkDialog.BitmapLoadingListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextEditorView$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog.BitmapLoadingListener
                public final void onCompleted() {
                    TextEditorView.m881$r8$lambda$QPmzHXdhqCDawVAxsSCjOFH6gc(TextEditorView.this);
                }
            });
            watermarkDialog.setPaddingTop(getPaddingTop());
            textAppendConfig = watermarkDialog;
        }
        this.mITextDialogConfigs.add(textAppendConfig);
        if (textConfig != null) {
            textAppendConfig.setName(textConfig.name);
        }
        textAppendConfig.setImageInitDisplayRect(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
        countAndInvalidate(textAppendConfig, false, true);
        setLastItemActivation();
    }

    public /* synthetic */ void lambda$addNewItem$3() {
        invalidate();
    }

    public void removeLastOperationItem() {
        if (this.mITextDialogConfigs.isEmpty()) {
            return;
        }
        this.mITextDialogConfigs.remove(getLastOperationItem());
        this.mTextDialogStack.pop();
        this.mCurrentIndex = -1;
        notifyRemoveItem();
    }

    public boolean isEmpty() {
        return this.mITextDialogConfigs.isEmpty();
    }

    public int getActivationItemBottom() {
        getLocationInWindow(this.mLocation);
        int i = this.mLocation[1];
        return this.mCurrentIndex != -1 ? (int) (i + this.mOperationDrawable.findLowerDecorationPosition()) : i;
    }

    private void setCanvasOffsetY(float f) {
        this.mCanvasOffsetY = f;
    }

    private float getOffsetDistanceWithAnimator() {
        if (!isIndexValid(this.mCurrentIndex)) {
            return 0.0f;
        }
        Matrix matrix = new Matrix();
        RectF rectF = new RectF(this.mRectFTemp);
        matrix.postRotate(this.mITextDialogConfigs.get(this.mCurrentIndex).getRotateDegrees(), rectF.centerX(), rectF.centerY());
        matrix.mapRect(rectF);
        return rectF.top;
    }

    public void offsetWithAnimator(float f) {
        if (this.mCurrentIndex == -1 || this.mCurrentTargetOffsetY == f) {
            return;
        }
        ObjectAnimator objectAnimator = this.mOffsetAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        } else {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mOffsetAnimator = objectAnimator2;
            objectAnimator2.addUpdateListener(this.mUpdateListener);
        }
        this.mCurrentTargetOffsetY = f;
        if (f < 0.0f) {
            f = Math.max(f, -Math.max(0.0f, getOffsetDistanceWithAnimator()));
        }
        this.mOffsetAnimator.setTarget(this);
        this.mOffsetAnimator.setPropertyName("canvasOffsetY");
        this.mOffsetAnimator.setFloatValues(this.mCanvasOffsetY, f);
        this.mOffsetAnimator.start();
        DefaultLogger.d("TextEditorView", "start targetOffset : %f", Float.valueOf(f));
    }

    public ITextDialogConfig getTextDialogConfig() {
        return this.mITextDialogConfigs.get(this.mCurrentIndex);
    }

    public void setIsBoundBox(boolean z) {
        this.mIsBoundBox = z;
    }

    public boolean isSubstrate() {
        return this.mITextDialogConfigs.get(this.mCurrentIndex).isSubstrate();
    }

    public void setIsSubstrate(boolean z) {
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setSubstrate(z);
        if (z) {
            iTextDialogConfig.setSubstrateColors(iTextDialogConfig.getGradientsColor() == 0 ? new int[]{iTextDialogConfig.getColor()} : new int[]{iTextDialogConfig.getColor(), iTextDialogConfig.getGradientsColor()});
            int i = -1;
            if (iTextDialogConfig.getColor() == -1) {
                i = -16777216;
            }
            iTextDialogConfig.setColor(i);
            iTextDialogConfig.setGradientsColor(0);
        }
        countAndInvalidate(iTextDialogConfig, false, false);
    }

    public void setSubstrateColors(int... iArr) {
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentIndex);
        iTextDialogConfig.setSubstrateColors(iArr);
        countAndInvalidate(iTextDialogConfig, false, false);
    }

    public void setIsStroke(boolean z) {
        int i = this.mCurrentIndex;
        if (i != -1) {
            ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(i);
            iTextDialogConfig.setIsStroke(z);
            countAndInvalidate(iTextDialogConfig, false, false);
        }
    }

    public void setIsMantle(boolean z) {
        this.mIsMantle = z;
    }

    public void setClipCanvas(boolean z) {
        this.mIsClipCanvas = z;
    }

    public void setStatisticLogger(TextFragment.StatisticLogger statisticLogger) {
        this.mStatisticLogger = statisticLogger;
    }
}
