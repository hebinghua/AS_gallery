package miuix.pickerwidget.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.ArraySet;
import com.baidu.platform.comapi.UIMsg;
import com.xiaomi.miai.api.StatusCode;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.internal.util.ReflectUtil;
import miuix.pickerwidget.R$attr;
import miuix.pickerwidget.R$color;
import miuix.pickerwidget.R$dimen;
import miuix.pickerwidget.R$id;
import miuix.pickerwidget.R$layout;
import miuix.pickerwidget.R$raw;
import miuix.pickerwidget.R$string;
import miuix.pickerwidget.R$style;
import miuix.pickerwidget.R$styleable;
import miuix.pickerwidget.internal.util.SimpleNumberFormatter;
import miuix.pickerwidget.internal.util.async.WorkerThreads;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class NumberPicker extends LinearLayout {
    public int MARGIN_LABEL_LEFT;
    public int MARGIN_LABEL_TOP;
    public final Scroller mAdjustScroller;
    public BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
    public int mBottomSelectionDividerBottom;
    public ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    public final boolean mComputeMaxWidth;
    public int mCurrentScrollOffset;
    public boolean mDecrementVirtualButtonPressed;
    public float mDisplayedMaxTextWidth;
    public String[] mDisplayedValues;
    public final Scroller mFlingScroller;
    public Formatter mFormatter;
    public final boolean mHasSelectorWheel;
    public IHoverStyle mIHoverStyle;
    public final int mId;
    public boolean mIncrementVirtualButtonPressed;
    public boolean mIngonreMoveEvents;
    public int mInitialScrollOffset;
    public final EditText mInputText;
    public boolean mIsHoverEnter;
    public CharSequence mLabel;
    public Paint mLabelPaint;
    public int mLabelTextColor;
    public int mLabelTextSize;
    public float mLabelTextSizeThreshold;
    public float mLabelTextSizeTrimFactor;
    public long mLastDownEventTime;
    public float mLastDownEventY;
    public float mLastDownOrMoveEventY;
    public int mLastHandledDownDpadKeyCode;
    public int[] mLocation;
    public long mLongPressUpdateInterval;
    public float mMaxFlingSpeedFactor;
    public final int mMaxHeight;
    public int mMaxValue;
    public int mMaxWidth;
    public int mMaximumFlingVelocity;
    public boolean mMeasureBackgroundEnabled;
    public final int mMinHeight;
    public int mMinValue;
    public final int mMinWidth;
    public int mMinimumFlingVelocity;
    public String mModDevice;
    public OnScrollListener mOnScrollListener;
    public OnValueChangeListener mOnValueChangeListener;
    public int mOriginLabelTextSize;
    public final PressedStateHelper mPressedStateHelper;
    public int mPreviousScrollerY;
    public int mScrollState;
    public final int mSelectionDividerHeight;
    public final int mSelectionDividersDistance;
    public int mSelectorElementHeight;
    public final SparseArray<String> mSelectorIndexToStringCache;
    public final int[] mSelectorIndices;
    public int mSelectorTextGapHeight;
    public final Paint mSelectorWheelPaint;
    public SetSelectionCommand mSetSelectionCommand;
    public boolean mShowSoftInputOnTap;
    public SoundPlayHandler mSoundPlayHandler;
    public int mTextColorHilight;
    public int mTextColorHint;
    public int mTextPadding;
    public final int mTextSize;
    public int mTextSizeHilight;
    public int mTextSizeHint;
    public float mTextSizeThreshold;
    public float mTextSizeTrimFactor;
    public int mTopSelectionDividerTop;
    public int mTouchSlop;
    public String mUpdateText;
    public int mValue;
    public VelocityTracker mVelocityTracker;
    public boolean mWrapSelectorWheel;
    public static final int DEFAULT_LAYOUT_RESOURCE_ID = R$layout.miuix_appcompat_number_picker_layout;
    public static final AtomicInteger sIdGenerator = new AtomicInteger(0);
    public static final Formatter TWO_DIGIT_FORMATTER = new NumberFormatter(2);
    public static final int[] PRESSED_STATE_SET = {16842919};
    public static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /* loaded from: classes3.dex */
    public interface Formatter {
        String format(int i);
    }

    /* loaded from: classes3.dex */
    public interface OnScrollListener {
        void onScrollStateChange(NumberPicker numberPicker, int i);
    }

    /* loaded from: classes3.dex */
    public interface OnValueChangeListener {
        void onValueChange(NumberPicker numberPicker, int i, int i2);
    }

    @Override // android.view.View
    public float getBottomFadingEdgeStrength() {
        return 0.9f;
    }

    public final float getTextSize(float f, int i, int i2) {
        return f >= 1.0f ? i2 : (f * (i2 - i)) + i;
    }

    @Override // android.view.View
    public float getTopFadingEdgeStrength() {
        return 0.9f;
    }

    /* loaded from: classes3.dex */
    public static class NumberFormatter implements Formatter {
        public final int iWidth;

        public NumberFormatter() {
            this.iWidth = -1;
        }

        public NumberFormatter(int i) {
            this.iWidth = i;
        }

        @Override // miuix.pickerwidget.widget.NumberPicker.Formatter
        public String format(int i) {
            return SimpleNumberFormatter.format(this.iWidth, i);
        }
    }

    /* loaded from: classes3.dex */
    public static class SoundPlayHandler extends Handler {
        public static final SoundPlayerContainer sPlayerContainer = new SoundPlayerContainer();

        /* loaded from: classes3.dex */
        public static class SoundPlayerContainer {
            public long mPrevPlayTime;
            public Set<Integer> mRefs;
            public int mSoundId;
            public SoundPool mSoundPlayer;

            public SoundPlayerContainer() {
                this.mRefs = new ArraySet();
            }

            public void init(Context context, int i) {
                if (this.mSoundPlayer == null) {
                    SoundPool soundPool = new SoundPool(1, 1, 0);
                    this.mSoundPlayer = soundPool;
                    this.mSoundId = soundPool.load(context, R$raw.number_picker_value_change, 1);
                }
                this.mRefs.add(Integer.valueOf(i));
            }

            public void play() {
                long currentTimeMillis = System.currentTimeMillis();
                SoundPool soundPool = this.mSoundPlayer;
                if (soundPool == null || currentTimeMillis - this.mPrevPlayTime <= 50) {
                    return;
                }
                soundPool.play(this.mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
                this.mPrevPlayTime = currentTimeMillis;
            }

            public void release(int i) {
                SoundPool soundPool;
                if (!this.mRefs.remove(Integer.valueOf(i)) || !this.mRefs.isEmpty() || (soundPool = this.mSoundPlayer) == null) {
                    return;
                }
                soundPool.release();
                this.mSoundPlayer = null;
            }
        }

        public SoundPlayHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 0) {
                sPlayerContainer.init((Context) message.obj, message.arg1);
            } else if (i == 1) {
                sPlayerContainer.play();
            } else if (i != 2) {
            } else {
                sPlayerContainer.release(message.arg1);
            }
        }

        public void init(Context context, int i) {
            Message obtainMessage = obtainMessage(0, i, 0);
            obtainMessage.obj = context;
            sendMessage(obtainMessage);
        }

        public void play() {
            sendMessage(obtainMessage(1));
        }

        public void stop() {
            removeMessages(1);
        }

        public void release(int i) {
            sendMessage(obtainMessage(2, i, 0));
        }
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.numberPickerStyle);
    }

    public NumberPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mId = sIdGenerator.incrementAndGet();
        this.MARGIN_LABEL_LEFT = 1;
        this.MARGIN_LABEL_TOP = 2;
        this.mMaxWidth = StatusCode.BAD_REQUEST;
        this.mLongPressUpdateInterval = 300L;
        this.mSelectorIndexToStringCache = new SparseArray<>();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.mTextSizeTrimFactor = 0.95f;
        this.mLabelTextSizeTrimFactor = 0.8f;
        this.mMaxFlingSpeedFactor = 1.0f;
        this.mLocation = new int[2];
        this.mIsHoverEnter = false;
        this.mMeasureBackgroundEnabled = true;
        float f = getResources().getDisplayMetrics().density;
        this.MARGIN_LABEL_LEFT = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_label_margin_left);
        this.MARGIN_LABEL_TOP = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_label_margin_top);
        parseStyle(attributeSet, i);
        initSoundPlayer();
        this.mHasSelectorWheel = true;
        this.mSelectionDividerHeight = (int) TypedValue.applyDimension(1, 2.0f, getResources().getDisplayMetrics());
        this.mSelectionDividersDistance = (int) (45.0f * f);
        this.mMinHeight = -1;
        this.mMaxHeight = (int) (f * 202.0f);
        this.mMinWidth = -1;
        this.mMaxWidth = -1;
        this.mComputeMaxWidth = true;
        this.mPressedStateHelper = new PressedStateHelper();
        setWillNotDraw(!true);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R$layout.miuix_appcompat_number_picker_layout, (ViewGroup) this, true);
        EditText editText = (EditText) findViewById(R$id.number_picker_input);
        this.mInputText = editText;
        initInputText();
        initThreshHolds();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / 8;
        this.mTextSize = (int) editText.getTextSize();
        this.mSelectorWheelPaint = initSelectorWheelPaint();
        initLabelPaint();
        this.mFlingScroller = new Scroller(getContext(), null, true);
        this.mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5f));
        updateInputTextView();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.mIHoverStyle = Folme.useAt(this).hover();
        setOnHoverListener(new View.OnHoverListener() { // from class: miuix.pickerwidget.widget.NumberPicker.1
            @Override // android.view.View.OnHoverListener
            public boolean onHover(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 9) {
                    NumberPicker.this.mIsHoverEnter = true;
                    NumberPicker.this.mIHoverStyle.setEffect(IHoverStyle.HoverEffect.NORMAL).hoverEnter(new AnimConfig[0]);
                } else if (actionMasked == 10) {
                    NumberPicker numberPicker = NumberPicker.this;
                    if (!NumberPicker.isOnHoverView(numberPicker, numberPicker.mLocation, motionEvent)) {
                        NumberPicker.this.mIsHoverEnter = false;
                        NumberPicker.this.mIHoverStyle.setEffect(IHoverStyle.HoverEffect.NORMAL).hoverExit(new AnimConfig[0]);
                    }
                }
                return false;
            }
        });
        setOnTouchListener(new View.OnTouchListener() { // from class: miuix.pickerwidget.widget.NumberPicker.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 1 || actionMasked == 3) {
                    NumberPicker.this.exitHoverView();
                    return false;
                }
                return false;
            }
        });
    }

    public final void exitHoverView() {
        if (this.mIsHoverEnter) {
            this.mIsHoverEnter = false;
            this.mIHoverStyle.setEffect(IHoverStyle.HoverEffect.NORMAL).hoverExit(new AnimConfig[0]);
        }
    }

    public static boolean isOnHoverView(View view, int[] iArr, MotionEvent motionEvent) {
        if (view != null) {
            view.getLocationOnScreen(iArr);
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            return rawX > iArr[0] && rawX < iArr[0] + view.getWidth() && rawY > iArr[1] && rawY < iArr[1] + view.getHeight();
        }
        return false;
    }

    public void setMeasureBackgroundEnabled(boolean z) {
        this.mMeasureBackgroundEnabled = z;
    }

    public final void parseStyle(AttributeSet attributeSet, int i) {
        Resources resources = getResources();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.NumberPicker, i, R$style.Widget_NumberPicker_DayNight);
        this.mLabel = obtainStyledAttributes.getText(R$styleable.NumberPicker_android_text);
        this.mTextSizeHilight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.NumberPicker_textSizeHighlight, resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_text_size_highlight_normal));
        this.mTextSizeHint = obtainStyledAttributes.getDimensionPixelSize(R$styleable.NumberPicker_textSizeHint, resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_text_size_hint_normal));
        this.mLabelTextSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.NumberPicker_android_labelTextSize, resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_label_text_size));
        this.mTextColorHilight = obtainStyledAttributes.getColor(R$styleable.NumberPicker_android_textColorHighlight, resources.getColor(R$color.miuix_appcompat_number_picker_highlight_color));
        this.mTextColorHint = obtainStyledAttributes.getColor(R$styleable.NumberPicker_android_textColorHint, resources.getColor(R$color.miuix_appcompat_number_picker_hint_color));
        this.mLabelTextColor = obtainStyledAttributes.getColor(R$styleable.NumberPicker_labelTextColor, resources.getColor(R$color.miuix_appcompat_number_picker_label_color));
        this.mTextPadding = obtainStyledAttributes.getDimensionPixelSize(R$styleable.NumberPicker_labelPadding, resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_number_picker_label_padding));
        obtainStyledAttributes.recycle();
        this.mOriginLabelTextSize = this.mLabelTextSize;
    }

    public final void initInputText() {
        this.mInputText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: miuix.pickerwidget.widget.NumberPicker.3
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    NumberPicker.this.mInputText.selectAll();
                    return;
                }
                NumberPicker.this.mInputText.setSelection(0, 0);
                NumberPicker.this.validateInputTextView(view);
            }
        });
        this.mInputText.setFilters(new InputFilter[]{new InputTextFilter()});
        this.mInputText.setRawInputType(2);
        this.mInputText.setImeOptions(6);
        this.mInputText.setVisibility(4);
        this.mInputText.setGravity(8388611);
        this.mInputText.setScaleX(0.0f);
        this.mInputText.setSaveEnabled(false);
        EditText editText = this.mInputText;
        editText.setPadding(this.mTextPadding, editText.getPaddingTop(), this.mTextPadding, this.mInputText.getPaddingRight());
    }

    public final void initThreshHolds() {
        this.mLabelTextSizeThreshold = getContext().getResources().getDimensionPixelSize(R$dimen.miuix_label_text_size_small);
        this.mTextSizeThreshold = getContext().getResources().getDimensionPixelSize(R$dimen.miuix_text_size_small);
    }

    public final Paint initSelectorWheelPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(this.mTextSizeHilight);
        paint.setTypeface(this.mInputText.getTypeface());
        paint.setColor(this.mInputText.getTextColors().getColorForState(LinearLayout.ENABLED_STATE_SET, -1));
        return paint;
    }

    public final void initLabelPaint() {
        Paint paint = new Paint();
        this.mLabelPaint = paint;
        paint.setAntiAlias(true);
        this.mLabelPaint.setFakeBoldText(true);
        this.mLabelPaint.setColor(this.mLabelTextColor);
        this.mLabelPaint.setTextSize(this.mLabelTextSize);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        tryComputeMaxWidth();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mHasSelectorWheel) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth2 = this.mInputText.getMeasuredWidth();
        int measuredHeight2 = this.mInputText.getMeasuredHeight();
        int i5 = (measuredWidth - measuredWidth2) / 2;
        int i6 = (measuredHeight - measuredHeight2) / 2;
        this.mInputText.layout(i5, i6, measuredWidth2 + i5, measuredHeight2 + i6);
        if (z) {
            initializeSelectorWheel();
            initializeFadingEdges();
            int height = getHeight();
            int i7 = this.mSelectionDividersDistance;
            int i8 = this.mSelectionDividerHeight;
            int i9 = ((height - i7) / 2) - i8;
            this.mTopSelectionDividerTop = i9;
            this.mBottomSelectionDividerBottom = i9 + (i8 * 2) + i7;
        }
        trimLabelTextSize((((getRight() - getLeft()) + getPaddingLeft()) - getPaddingRight()) / 2);
        Drawable background = getBackground();
        int i10 = this.mMaxWidth + 20;
        if (!this.mMeasureBackgroundEnabled || !(background instanceof StateListDrawable)) {
            return;
        }
        StateListDrawable stateListDrawable = (StateListDrawable) background;
        if (Build.VERSION.SDK_INT < 29) {
            return;
        }
        int stateCount = stateListDrawable.getStateCount();
        for (int i11 = 0; i11 < stateCount; i11++) {
            Drawable stateDrawable = stateListDrawable.getStateDrawable(i11);
            if (stateDrawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) stateDrawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                for (int i12 = 0; i12 < numberOfLayers; i12++) {
                    Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(layerDrawable.getId(i12));
                    if (findDrawableByLayerId instanceof GradientDrawable) {
                        ((GradientDrawable) findDrawableByLayerId).setSize(getWidth() > i10 ? i10 : getWidth(), getHeight());
                    }
                }
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (!this.mHasSelectorWheel) {
            super.onMeasure(i, i2);
            return;
        }
        super.onMeasure(makeMeasureSpec(i, this.mMaxWidth), makeMeasureSpec(i2, this.mMaxHeight));
        setMeasuredDimension(resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), i), resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), i2));
    }

    public final void initSoundPlayer() {
        if (this.mSoundPlayHandler == null) {
            SoundPlayHandler soundPlayHandler = new SoundPlayHandler(WorkerThreads.aquireWorker("NumberPicker_sound_play"));
            this.mSoundPlayHandler = soundPlayHandler;
            soundPlayHandler.init(getContext().getApplicationContext(), this.mId);
        }
    }

    public final void releaseSoundPlayer() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.release(this.mId);
            this.mSoundPlayHandler = null;
        }
    }

    public final void playSound() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.play();
        }
    }

    public final void stopSoundPlay() {
        SoundPlayHandler soundPlayHandler = this.mSoundPlayHandler;
        if (soundPlayHandler != null) {
            soundPlayHandler.stop();
        }
    }

    public final boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int finalY = scroller.getFinalY() - scroller.getCurrY();
        int i = this.mInitialScrollOffset - ((this.mCurrentScrollOffset + finalY) % this.mSelectorElementHeight);
        if (i != 0) {
            int abs = Math.abs(i);
            int i2 = this.mSelectorElementHeight;
            if (abs > i2 / 2) {
                i = i > 0 ? i - i2 : i + i2;
            }
            scrollBy(0, finalY + i);
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mHasSelectorWheel || !isEnabled() || motionEvent.getActionMasked() != 0) {
            return false;
        }
        removeAllCallbacks();
        this.mInputText.setVisibility(4);
        float y = motionEvent.getY();
        this.mLastDownEventY = y;
        this.mLastDownOrMoveEventY = y;
        this.mLastDownEventTime = motionEvent.getEventTime();
        this.mIngonreMoveEvents = false;
        this.mShowSoftInputOnTap = false;
        float f = this.mLastDownEventY;
        if (f < this.mTopSelectionDividerTop) {
            if (this.mScrollState == 0) {
                this.mPressedStateHelper.buttonPressDelayed(2);
            }
        } else if (f > this.mBottomSelectionDividerBottom && this.mScrollState == 0) {
            this.mPressedStateHelper.buttonPressDelayed(1);
        }
        if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            onScrollStateChange(0);
        } else if (!this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
        } else {
            float f2 = this.mLastDownEventY;
            if (f2 < this.mTopSelectionDividerTop) {
                postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
            } else if (f2 > this.mBottomSelectionDividerBottom) {
                postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
            } else {
                this.mShowSoftInputOnTap = true;
                postBeginSoftInputOnLongPressCommand();
            }
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || !this.mHasSelectorWheel) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2 && !this.mIngonreMoveEvents) {
                float y = motionEvent.getY();
                if (this.mScrollState != 1) {
                    if (((int) Math.abs(y - this.mLastDownEventY)) > this.mTouchSlop) {
                        removeAllCallbacks();
                        onScrollStateChange(1);
                    }
                } else {
                    scrollBy(0, (int) (y - this.mLastDownOrMoveEventY));
                    invalidate();
                }
                this.mLastDownOrMoveEventY = y;
            }
        } else {
            removeBeginSoftInputCommand();
            removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            int yVelocity = (int) velocityTracker.getYVelocity();
            if (Math.abs(yVelocity) >= Math.abs(this.mMaximumFlingVelocity)) {
                yVelocity = (int) (yVelocity * this.mMaxFlingSpeedFactor);
            }
            if (Math.abs(yVelocity) > this.mMinimumFlingVelocity) {
                fling(yVelocity);
                onScrollStateChange(2);
            } else {
                int y2 = (int) motionEvent.getY();
                int abs = (int) Math.abs(y2 - this.mLastDownEventY);
                long eventTime = motionEvent.getEventTime() - this.mLastDownEventTime;
                if (abs <= this.mTouchSlop && eventTime < ViewConfiguration.getLongPressTimeout()) {
                    if (this.mShowSoftInputOnTap) {
                        this.mShowSoftInputOnTap = false;
                    } else {
                        int i = (y2 / this.mSelectorElementHeight) - 1;
                        if (i > 0) {
                            changeValueByOne(true);
                            this.mPressedStateHelper.buttonTapped(1);
                        } else if (i < 0) {
                            changeValueByOne(false);
                            this.mPressedStateHelper.buttonTapped(2);
                        }
                    }
                } else {
                    ensureScrollWheelAdjusted();
                }
                onScrollStateChange(0);
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x004c, code lost:
        requestFocus();
        r5.mLastHandledDownDpadKeyCode = r0;
        removeAllCallbacks();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x005a, code lost:
        if (r5.mFlingScroller.isFinished() == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x005c, code lost:
        if (r0 != 20) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x005e, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0060, code lost:
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0061, code lost:
        changeValueByOne(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0064, code lost:
        return true;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchKeyEvent(android.view.KeyEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getKeyCode()
            r1 = 19
            r2 = 20
            if (r0 == r1) goto L19
            if (r0 == r2) goto L19
            r1 = 23
            if (r0 == r1) goto L15
            r1 = 66
            if (r0 == r1) goto L15
            goto L65
        L15:
            r5.removeAllCallbacks()
            goto L65
        L19:
            boolean r1 = r5.mHasSelectorWheel
            if (r1 != 0) goto L1e
            goto L65
        L1e:
            int r1 = r6.getAction()
            r3 = 1
            if (r1 == 0) goto L30
            if (r1 == r3) goto L28
            goto L65
        L28:
            int r1 = r5.mLastHandledDownDpadKeyCode
            if (r1 != r0) goto L65
            r6 = -1
            r5.mLastHandledDownDpadKeyCode = r6
            return r3
        L30:
            boolean r1 = r5.mWrapSelectorWheel
            if (r1 != 0) goto L42
            if (r0 != r2) goto L37
            goto L42
        L37:
            int r1 = r5.getValue()
            int r4 = r5.getMinValue()
            if (r1 <= r4) goto L65
            goto L4c
        L42:
            int r1 = r5.getValue()
            int r4 = r5.getMaxValue()
            if (r1 >= r4) goto L65
        L4c:
            r5.requestFocus()
            r5.mLastHandledDownDpadKeyCode = r0
            r5.removeAllCallbacks()
            android.widget.Scroller r6 = r5.mFlingScroller
            boolean r6 = r6.isFinished()
            if (r6 == 0) goto L64
            if (r0 != r2) goto L60
            r6 = r3
            goto L61
        L60:
            r6 = 0
        L61:
            r5.changeValueByOne(r6)
        L64:
            return r3
        L65:
            boolean r6 = super.dispatchKeyEvent(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.pickerwidget.widget.NumberPicker.dispatchKeyEvent(android.view.KeyEvent):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }

    @Override // android.view.View
    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        int currY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        scrollBy(0, currY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currY;
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            invalidate();
        }
    }

    @Override // android.view.View
    public void scrollBy(int i, int i2) {
        int[] iArr = this.mSelectorIndices;
        boolean z = this.mWrapSelectorWheel;
        if (!z && i2 > 0 && iArr[1] <= this.mMinValue) {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        } else if (!z && i2 < 0 && iArr[1] >= this.mMaxValue) {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        } else {
            this.mCurrentScrollOffset += i2;
            while (true) {
                int i3 = this.mCurrentScrollOffset;
                if (i3 - this.mInitialScrollOffset <= this.mSelectorTextGapHeight) {
                    break;
                }
                this.mCurrentScrollOffset = i3 - this.mSelectorElementHeight;
                decrementSelectorIndices(iArr);
                setValueInternal(iArr[1], true);
                if (!this.mWrapSelectorWheel && iArr[1] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            while (true) {
                int i4 = this.mCurrentScrollOffset;
                if (i4 - this.mInitialScrollOffset >= (-this.mSelectorTextGapHeight)) {
                    return;
                }
                this.mCurrentScrollOffset = i4 + this.mSelectorElementHeight;
                incrementSelectorIndices(iArr);
                setValueInternal(iArr[1], true);
                if (!this.mWrapSelectorWheel && iArr[1] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
        }
    }

    public void setLabel(String str) {
        CharSequence charSequence = this.mLabel;
        if ((charSequence != null || str == null) && (charSequence == null || charSequence.equals(str))) {
            return;
        }
        this.mLabel = str;
        invalidate();
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setFormatter(Formatter formatter) {
        if (formatter == this.mFormatter) {
            return;
        }
        this.mFormatter = formatter;
        initializeSelectorWheelIndices();
        updateInputTextView();
    }

    public void setValue(int i) {
        setValueInternal(i, false);
    }

    public final void tryComputeMaxWidth() {
        if (!this.mComputeMaxWidth) {
            return;
        }
        float f = -1.0f;
        this.mSelectorWheelPaint.setTextSize(this.mTextSizeHilight);
        String[] strArr = this.mDisplayedValues;
        int i = 0;
        if (strArr == null) {
            float f2 = 0.0f;
            while (i < 9) {
                float measureText = this.mSelectorWheelPaint.measureText(String.valueOf(i));
                if (measureText > f2) {
                    f2 = measureText;
                }
                i++;
            }
            f = (int) (formatNumber(this.mMaxValue).length() * f2);
        } else {
            int length = strArr.length;
            while (i < length) {
                float measureText2 = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i]);
                if (measureText2 > f) {
                    f = measureText2;
                }
                i++;
            }
        }
        this.mDisplayedMaxTextWidth = f;
        float paddingLeft = f + this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight() + getPaddingLeft() + getPaddingRight();
        if (this.mMaxWidth == paddingLeft) {
            return;
        }
        int i2 = this.mMinWidth;
        if (paddingLeft > i2) {
            this.mMaxWidth = (int) paddingLeft;
        } else {
            this.mMaxWidth = i2;
        }
    }

    private float getLabelWidth() {
        if (TextUtils.isEmpty(this.mLabel) || isInternationalBuild()) {
            return 0.0f;
        }
        return this.mLabelPaint.measureText(this.mLabel.toString());
    }

    public void setLabelTextSizeThreshold(float f) {
        this.mLabelTextSizeThreshold = Math.max(f, 0.0f);
    }

    public void setLabelTextSizeTrimFactor(float f) {
        if (f <= 0.0f || f >= 1.0f) {
            return;
        }
        this.mLabelTextSizeTrimFactor = f;
    }

    public void setTextSizeTrimFactor(float f) {
        if (f <= 0.0f || f >= 1.0f) {
            return;
        }
        this.mTextSizeTrimFactor = f;
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean z) {
        boolean z2 = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length;
        if ((!z || z2) && z != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = z;
        }
        refreshWheel();
    }

    public void setOnLongPressUpdateInterval(long j) {
        this.mLongPressUpdateInterval = j;
    }

    public int getValue() {
        return this.mValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int i) {
        if (this.mMinValue == i) {
            return;
        }
        if (i < 0) {
            throw new IllegalArgumentException("minValue must be >= 0");
        }
        this.mMinValue = i;
        if (i > this.mValue) {
            this.mValue = i;
        }
        setWrapSelectorWheel(this.mMaxValue - i > this.mSelectorIndices.length);
        initializeSelectorWheelIndices();
        updateInputTextView();
        tryComputeMaxWidth();
        invalidate();
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int i) {
        if (this.mMaxValue == i) {
            return;
        }
        if (i < 0) {
            throw new IllegalArgumentException("maxValue must be >= 0");
        }
        this.mMaxValue = i;
        if (i < this.mValue) {
            this.mValue = i;
        }
        setWrapSelectorWheel(i - this.mMinValue > this.mSelectorIndices.length);
        initializeSelectorWheelIndices();
        updateInputTextView();
        tryComputeMaxWidth();
        invalidate();
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public void setDisplayedValues(String[] strArr) {
        if (this.mDisplayedValues == strArr) {
            return;
        }
        this.mDisplayedValues = strArr;
        if (strArr != null) {
            this.mInputText.setRawInputType(524289);
        } else {
            this.mInputText.setRawInputType(2);
        }
        updateInputTextView();
        initializeSelectorWheelIndices();
        tryComputeMaxWidth();
    }

    public void setMaxFlingSpeedFactor(float f) {
        if (f >= 0.0f) {
            this.mMaxFlingSpeedFactor = f;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initSoundPlayer();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSoundPlayer();
        removeAllCallbacks();
        WorkerThreads.releaseWorker("NumberPicker_sound_play");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        if (!this.mHasSelectorWheel) {
            super.onDraw(canvas);
            return;
        }
        float right = (((getRight() - getLeft()) + getPaddingLeft()) - getPaddingRight()) / 2;
        float f = this.mInitialScrollOffset + (this.mSelectorElementHeight * 1);
        drawLabelText(canvas, right, f, drawScrollValue(canvas, right, f));
    }

    public final float drawScrollValue(Canvas canvas, float f, float f2) {
        float f3 = this.mCurrentScrollOffset;
        SparseArray<String> sparseArray = this.mSelectorIndexToStringCache;
        for (int i : this.mSelectorIndices) {
            String str = sparseArray.get(i);
            float abs = Math.abs(f2 - f3) / this.mSelectorElementHeight;
            float textSize = getTextSize(abs, this.mTextSizeHilight, this.mTextSizeHint);
            while (true) {
                this.mSelectorWheelPaint.setTextSize(textSize);
                textSize *= this.mTextSizeTrimFactor;
                if (this.mSelectorWheelPaint.measureText(str) <= getWidth() && this.mSelectorWheelPaint.getTextSize() <= this.mTextSizeThreshold) {
                    break;
                }
            }
            this.mSelectorWheelPaint.setColor(getAlphaGradient(abs, this.mTextColorHint, false));
            canvas.drawText(str, f, ((textSize - this.mTextSizeHint) / 2.0f) + f3, this.mSelectorWheelPaint);
            if (abs < 1.0f) {
                this.mSelectorWheelPaint.setColor(getAlphaGradient(abs, this.mTextColorHilight, true));
                canvas.drawText(str, f, ((textSize - this.mTextSizeHint) / 2.0f) + f3, this.mSelectorWheelPaint);
            }
            f3 += this.mSelectorElementHeight;
        }
        return f3;
    }

    public final void drawLabelText(Canvas canvas, float f, float f2, float f3) {
        float min;
        if (TextUtils.isEmpty(this.mLabel) || isInternationalBuild()) {
            return;
        }
        float measureText = this.mLabelPaint.measureText(this.mLabel.toString());
        if (ViewUtils.isLayoutRtl(this)) {
            min = Math.max(((f - (this.mDisplayedMaxTextWidth / 2.0f)) - this.MARGIN_LABEL_LEFT) - measureText, 0.0f);
        } else {
            min = Math.min(f + (this.mDisplayedMaxTextWidth / 2.0f) + this.MARGIN_LABEL_LEFT, getWidth() - measureText);
        }
        canvas.drawText(this.mLabel.toString(), min, (f2 - (this.mTextSizeHilight / 2)) + (this.mLabelTextSize / 2) + this.MARGIN_LABEL_TOP, this.mLabelPaint);
    }

    public final void trimLabelTextSize(float f) {
        if (getLabelWidth() > 0.0f) {
            int i = this.mOriginLabelTextSize;
            this.mLabelTextSize = i;
            this.mLabelPaint.setTextSize(i);
            while ((this.mDisplayedMaxTextWidth / 2.0f) + f + this.MARGIN_LABEL_LEFT + getLabelWidth() > getWidth()) {
                int i2 = this.mLabelTextSize;
                if (i2 <= this.mLabelTextSizeThreshold) {
                    return;
                }
                int i3 = (int) (i2 * this.mLabelTextSizeTrimFactor);
                this.mLabelTextSize = i3;
                this.mLabelPaint.setTextSize(i3);
            }
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initThreshHolds();
    }

    public final int getAlphaGradient(float f, int i, boolean z) {
        float alpha;
        if (f >= 1.0f) {
            return i;
        }
        if (z) {
            alpha = ((-f) * Color.alpha(i)) + Color.alpha(i);
        } else {
            alpha = f * Color.alpha(i);
        }
        return (((int) alpha) << 24) | (i & 16777215);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return NumberPicker.class.getName();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (isEnabled()) {
            accessibilityNodeInfo.setScrollable(true);
            accessibilityNodeInfo.addAction(8192);
            accessibilityNodeInfo.addAction(4096);
            int i = Build.VERSION.SDK_INT;
            if (i >= 24) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
            }
            accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(0, this.mMinValue - 1, this.mMaxValue + 1, this.mValue));
            StringBuilder sb = new StringBuilder();
            String[] strArr = this.mDisplayedValues;
            sb.append(strArr == null ? formatNumber(this.mValue) : strArr[this.mValue - this.mMinValue]);
            sb.append(TextUtils.isEmpty(this.mLabel) ? "" : this.mLabel);
            accessibilityNodeInfo.setContentDescription(sb.toString());
            if (i < 30) {
                return;
            }
            accessibilityNodeInfo.setStateDescription(getContext().getString(R$string.miuix_access_state_desc));
        }
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        if (super.performAccessibilityAction(i, bundle)) {
            return true;
        }
        boolean z = false;
        if (!isEnabled()) {
            return false;
        }
        if (i != 4096 && i != 8192) {
            return false;
        }
        if (i == 4096) {
            z = true;
        }
        changeValueByOne(z);
        return true;
    }

    public final int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            return View.MeasureSpec.makeMeasureSpec(Math.min(size, i2), 1073741824);
        }
        if (mode == 0) {
            return View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
        }
        if (mode == 1073741824) {
            return i;
        }
        throw new IllegalArgumentException("Unknown measure mode: " + mode);
    }

    public final int resolveSizeAndStateRespectingMinSize(int i, int i2, int i3) {
        return i != -1 ? LinearLayout.resolveSizeAndState(Math.max(i, i2), i3, 0) : i2;
    }

    public final void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] iArr = this.mSelectorIndices;
        int value = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int i2 = (i - 1) + value;
            if (this.mWrapSelectorWheel) {
                i2 = getWrappedSelectorIndex(i2);
            }
            iArr[i] = i2;
            ensureCachedScrollSelectorValue(iArr[i]);
        }
    }

    public final void setValueInternal(int i, boolean z) {
        int min;
        if (this.mWrapSelectorWheel) {
            min = getWrappedSelectorIndex(i);
        } else {
            min = Math.min(Math.max(i, this.mMinValue), this.mMaxValue);
        }
        int i2 = this.mValue;
        if (i2 == min) {
            return;
        }
        this.mValue = min;
        updateInputTextView();
        if (z) {
            notifyChange(i2);
        }
        initializeSelectorWheelIndices();
        invalidate();
    }

    public final void changeValueByOne(boolean z) {
        if (!this.mHasSelectorWheel) {
            if (z) {
                setValueInternal(this.mValue + 1, true);
                return;
            } else {
                setValueInternal(this.mValue - 1, true);
                return;
            }
        }
        this.mInputText.setVisibility(4);
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        if (z) {
            this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
        } else {
            this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
        }
        invalidate();
    }

    public final void initializeSelectorWheel() {
        int[] iArr;
        initializeSelectorWheelIndices();
        float bottom = (getBottom() - getTop()) - (this.mSelectorIndices.length * this.mTextSize);
        if (bottom < 0.0f) {
            bottom = 0.0f;
        }
        int length = (int) ((bottom / iArr.length) + 0.5f);
        this.mSelectorTextGapHeight = length;
        this.mSelectorElementHeight = this.mTextSize + length;
        int baseline = (this.mInputText.getBaseline() + this.mInputText.getTop()) - (this.mSelectorElementHeight * 1);
        this.mInitialScrollOffset = baseline;
        this.mCurrentScrollOffset = baseline;
        updateInputTextView();
    }

    public final void initializeFadingEdges() {
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(((getBottom() - getTop()) - this.mTextSize) / 2);
    }

    public final void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }
            onScrollStateChange(0);
        } else if (this.mScrollState == 1) {
        } else {
            updateInputTextView();
        }
    }

    public final void onScrollStateChange(int i) {
        if (this.mScrollState == i) {
            return;
        }
        if (i == 0) {
            String str = this.mUpdateText;
            if (str != null && !str.equals(this.mInputText.getText().toString())) {
                this.mInputText.setText(this.mUpdateText);
            }
            this.mUpdateText = null;
            stopSoundPlay();
        }
        this.mScrollState = i;
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener == null) {
            return;
        }
        onScrollListener.onScrollStateChange(this, i);
    }

    public final void fling(int i) {
        this.mPreviousScrollerY = 0;
        if (i > 0) {
            this.mFlingScroller.fling(0, 0, 0, i, 0, 0, 0, Integer.MAX_VALUE);
        } else {
            this.mFlingScroller.fling(0, Integer.MAX_VALUE, 0, i, 0, 0, 0, Integer.MAX_VALUE);
        }
        invalidate();
    }

    public final int getWrappedSelectorIndex(int i) {
        int i2 = this.mMaxValue;
        if (i > i2) {
            int i3 = this.mMinValue;
            return (i3 + ((i - i2) % (i2 - i3))) - 1;
        }
        int i4 = this.mMinValue;
        return i < i4 ? (i2 - ((i4 - i) % (i2 - i4))) + 1 : i;
    }

    public final void incrementSelectorIndices(int[] iArr) {
        if (iArr.length - 1 >= 0) {
            System.arraycopy(iArr, 1, iArr, 0, iArr.length - 1);
        }
        int i = iArr[iArr.length - 2] + 1;
        if (this.mWrapSelectorWheel && i > this.mMaxValue) {
            i = this.mMinValue;
        }
        iArr[iArr.length - 1] = i;
        ensureCachedScrollSelectorValue(i);
    }

    public final void decrementSelectorIndices(int[] iArr) {
        if (iArr.length - 1 >= 0) {
            System.arraycopy(iArr, 0, iArr, 1, iArr.length - 1);
        }
        int i = iArr[1] - 1;
        if (this.mWrapSelectorWheel && i < this.mMinValue) {
            i = this.mMaxValue;
        }
        iArr[0] = i;
        ensureCachedScrollSelectorValue(i);
    }

    public final void ensureCachedScrollSelectorValue(int i) {
        String str;
        SparseArray<String> sparseArray = this.mSelectorIndexToStringCache;
        if (sparseArray.get(i) != null) {
            return;
        }
        int i2 = this.mMinValue;
        if (i < i2 || i > this.mMaxValue) {
            str = "";
        } else {
            String[] strArr = this.mDisplayedValues;
            if (strArr != null) {
                str = strArr[i - i2];
            } else {
                str = formatNumber(i);
            }
        }
        sparseArray.put(i, str);
    }

    public final String formatNumber(int i) {
        Formatter formatter = this.mFormatter;
        return formatter != null ? formatter.format(i) : SimpleNumberFormatter.format(i);
    }

    public final void validateInputTextView(View view) {
        String valueOf = String.valueOf(((TextView) view).getText());
        if (TextUtils.isEmpty(valueOf)) {
            updateInputTextView();
        } else {
            setValueInternal(getSelectedPos(valueOf), true);
        }
    }

    public final boolean updateInputTextView() {
        String[] strArr = this.mDisplayedValues;
        String formatNumber = strArr == null ? formatNumber(this.mValue) : strArr[this.mValue - this.mMinValue];
        if (!TextUtils.isEmpty(formatNumber)) {
            if (this.mScrollState != 0) {
                this.mUpdateText = formatNumber;
                return true;
            } else if (formatNumber.equals(this.mInputText.getText().toString())) {
                return true;
            } else {
                this.mInputText.setText(formatNumber);
                return true;
            }
        }
        return false;
    }

    public final void notifyChange(int i) {
        sendAccessibilityEvent(4);
        playSound();
        HapticCompat.performHapticFeedback(this, HapticFeedbackConstants.MIUI_MESH_NORMAL);
        OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(this, i, this.mValue);
        }
    }

    public final void postChangeCurrentByOneFromLongPress(boolean z, long j) {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(z);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j);
    }

    public final void removeChangeCurrentByOneFromLongPress() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    public final void postBeginSoftInputOnLongPressCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand == null) {
            this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
        } else {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        postDelayed(this.mBeginSoftInputOnLongPressCommand, ViewConfiguration.getLongPressTimeout());
    }

    public final void removeBeginSoftInputCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
    }

    public final void removeAllCallbacks() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand != null) {
            removeCallbacks(setSelectionCommand);
        }
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    public final int getSelectedPos(String str) {
        try {
            if (this.mDisplayedValues == null) {
                return Integer.parseInt(str);
            }
            for (int i = 0; i < this.mDisplayedValues.length; i++) {
                str = str.toLowerCase();
                if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return this.mMinValue + i;
                }
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return this.mMinValue;
        }
    }

    public final void postSetSelectionCommand(int i, int i2) {
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand == null) {
            this.mSetSelectionCommand = new SetSelectionCommand();
        } else {
            removeCallbacks(setSelectionCommand);
        }
        this.mSetSelectionCommand.mSelectionStart = i;
        this.mSetSelectionCommand.mSelectionEnd = i2;
        post(this.mSetSelectionCommand);
    }

    /* loaded from: classes3.dex */
    public class InputTextFilter extends NumberKeyListener {
        @Override // android.text.method.KeyListener
        public int getInputType() {
            return 1;
        }

        public InputTextFilter() {
        }

        @Override // android.text.method.NumberKeyListener
        public char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        @Override // android.text.method.NumberKeyListener, android.text.InputFilter
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            String valueOf;
            String[] strArr;
            if (NumberPicker.this.mDisplayedValues == null) {
                CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                if (filter == null) {
                    filter = charSequence.subSequence(i, i2);
                }
                String str = String.valueOf(spanned.subSequence(0, i3)) + ((Object) filter) + ((Object) spanned.subSequence(i4, spanned.length()));
                return "".equals(str) ? str : (NumberPicker.this.getSelectedPos(str) > NumberPicker.this.mMaxValue || str.length() > String.valueOf(NumberPicker.this.mMaxValue).length()) ? "" : filter;
            }
            if (TextUtils.isEmpty(String.valueOf(charSequence.subSequence(i, i2)))) {
                return "";
            }
            String str2 = String.valueOf(spanned.subSequence(0, i3)) + ((Object) valueOf) + ((Object) spanned.subSequence(i4, spanned.length()));
            String lowerCase = String.valueOf(str2).toLowerCase();
            for (String str3 : NumberPicker.this.mDisplayedValues) {
                if (str3.toLowerCase().startsWith(lowerCase)) {
                    NumberPicker.this.postSetSelectionCommand(str2.length(), str3.length());
                    return str3.subSequence(i3, str3.length());
                }
            }
            return "";
        }
    }

    public final boolean ensureScrollWheelAdjusted() {
        int i = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (i != 0) {
            this.mPreviousScrollerY = 0;
            int abs = Math.abs(i);
            int i2 = this.mSelectorElementHeight;
            if (abs > i2 / 2) {
                if (i > 0) {
                    i2 = -i2;
                }
                i += i2;
            }
            this.mAdjustScroller.startScroll(0, 0, 0, i, 800);
            invalidate();
            return true;
        }
        return false;
    }

    /* loaded from: classes3.dex */
    public class PressedStateHelper implements Runnable {
        public final int MODE_PRESS = 1;
        public final int MODE_TAPPED = 2;
        public int mManagedButton;
        public int mMode;

        public PressedStateHelper() {
        }

        public void cancel() {
            this.mMode = 0;
            this.mManagedButton = 0;
            NumberPicker.this.removeCallbacks(this);
            if (NumberPicker.this.mIncrementVirtualButtonPressed) {
                NumberPicker.this.mIncrementVirtualButtonPressed = false;
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
            }
            if (NumberPicker.this.mDecrementVirtualButtonPressed) {
                NumberPicker.this.mDecrementVirtualButtonPressed = false;
                NumberPicker numberPicker2 = NumberPicker.this;
                numberPicker2.invalidate(0, 0, numberPicker2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
            }
        }

        public void buttonPressDelayed(int i) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = i;
            NumberPicker.this.postDelayed(this, ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int i) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = i;
            NumberPicker.this.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = this.mMode;
            if (i == 1) {
                int i2 = this.mManagedButton;
                if (i2 == 1) {
                    NumberPicker.this.mIncrementVirtualButtonPressed = true;
                    NumberPicker numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                } else if (i2 != 2) {
                } else {
                    NumberPicker.this.mDecrementVirtualButtonPressed = true;
                    NumberPicker numberPicker2 = NumberPicker.this;
                    numberPicker2.invalidate(0, 0, numberPicker2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                }
            } else if (i != 2) {
            } else {
                int i3 = this.mManagedButton;
                if (i3 == 1) {
                    if (!NumberPicker.this.mIncrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker numberPicker3 = NumberPicker.this;
                    numberPicker3.mIncrementVirtualButtonPressed = !numberPicker3.mIncrementVirtualButtonPressed;
                    NumberPicker numberPicker4 = NumberPicker.this;
                    numberPicker4.invalidate(0, numberPicker4.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                } else if (i3 != 2) {
                } else {
                    if (!NumberPicker.this.mDecrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker numberPicker5 = NumberPicker.this;
                    numberPicker5.mDecrementVirtualButtonPressed = !numberPicker5.mDecrementVirtualButtonPressed;
                    NumberPicker numberPicker6 = NumberPicker.this;
                    numberPicker6.invalidate(0, 0, numberPicker6.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SetSelectionCommand implements Runnable {
        public int mSelectionEnd;
        public int mSelectionStart;

        public SetSelectionCommand() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mSelectionEnd < NumberPicker.this.mInputText.length()) {
                NumberPicker.this.mInputText.setSelection(this.mSelectionStart, this.mSelectionEnd);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        public boolean mIncrement;

        public ChangeCurrentByOneFromLongPressCommand() {
        }

        public final void setStep(boolean z) {
            this.mIncrement = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            NumberPicker.this.changeValueByOne(this.mIncrement);
            NumberPicker numberPicker = NumberPicker.this;
            numberPicker.postDelayed(this, numberPicker.mLongPressUpdateInterval);
        }
    }

    /* loaded from: classes3.dex */
    public static class CustomEditText extends AppCompatEditText {
        public CustomEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.widget.TextView
        public void onEditorAction(int i) {
            super.onEditorAction(i);
            if (i == 6) {
                clearFocus();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class BeginSoftInputOnLongPressCommand implements Runnable {
        public BeginSoftInputOnLongPressCommand() {
        }

        @Override // java.lang.Runnable
        public void run() {
            NumberPicker.this.mIngonreMoveEvents = true;
        }
    }

    public final void refreshWheel() {
        initializeSelectorWheelIndices();
        invalidate();
    }

    public final boolean isInternationalBuild() {
        if (this.mModDevice == null) {
            this.mModDevice = (String) ReflectUtil.callStaticObjectMethod(ReflectUtil.getClass("android.os.SystemProperties"), String.class, CallMethod.METHOD_GET, new Class[]{String.class, String.class}, "ro.product.mod_device", "");
        }
        return this.mModDevice.endsWith("_global");
    }
}
