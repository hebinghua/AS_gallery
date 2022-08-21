package com.miui.gallery.editor.photo.screen.text;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.doodle.PaintElementOperationDrawable;
import com.miui.gallery.editor.photo.core.imports.text.TextAppendConfig;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.photo.core.imports.text.TextManager;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.base.ScreenVirtualEditorView;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenTextView extends ScreenVirtualEditorView implements IScreenTextOperation {
    public Paint mAuxiliaryPaint;
    public String mBubbleText;
    public float mCanvasOffsetY;
    public int mCurrentDialogConfigIndex;
    public int mCurrentMenuItemIndex;
    public float mCurrentTargetOffsetY;
    public FragmentManager mFragmentManager;
    public GesListener mGesListener;
    public List<ITextDialogConfig> mITextDialogConfigs;
    public boolean mIsTextChanged;
    public int[] mLocation;
    public boolean mNeedDrawOverlay;
    public ObjectAnimator mOffsetAnimator;
    public PaintElementOperationDrawable mOperationDrawable;
    public RectF mRectFTemp;
    public Rect mRectTemp;
    public String mRevokeText;
    public List<ITextDialogConfig> mSavedITextDialogConfigs;
    public StatusListener mStatusListener;
    public SparseArray<DialogStatusData> mTextConfigDataArray;
    public ScreenTextDrawNode mTextDrawNode;
    public TextEditDialogOld mTextEditDialog;
    public TextWatcher mTextWatcher;
    public ValueAnimator.AnimatorUpdateListener mUpdateListener;

    /* loaded from: classes2.dex */
    public enum TouchAction {
        NONE,
        DELETE,
        SCALE,
        MIRROR,
        EDIT,
        REVERSE_WHITE,
        REVERSE_BLACK
    }

    public static /* synthetic */ void $r8$lambda$1gsrLTMZeDVi_01JWduoKnqjwes(ScreenTextView screenTextView) {
        screenTextView.lambda$addNewItem$0();
    }

    public void onDetachedFromWindow() {
    }

    public ScreenTextView(ScreenEditorView screenEditorView) {
        super(screenEditorView);
        this.mStatusListener = new StatusListener();
        this.mITextDialogConfigs = new ArrayList();
        this.mSavedITextDialogConfigs = new ArrayList();
        this.mTextConfigDataArray = new SparseArray<>();
        this.mCurrentMenuItemIndex = 0;
        this.mCurrentDialogConfigIndex = -1;
        this.mAuxiliaryPaint = new Paint(1);
        this.mGesListener = new GesListener();
        this.mRectFTemp = new RectF();
        this.mCanvasOffsetY = 0.0f;
        this.mCurrentTargetOffsetY = 0.0f;
        this.mLocation = new int[2];
        this.mRectTemp = new Rect();
        this.mTextWatcher = new TextWatcher() { // from class: com.miui.gallery.editor.photo.screen.text.ScreenTextView.1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            {
                ScreenTextView.this = this;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ScreenTextView.this.setItemText(charSequence.toString());
                ScreenTextView.this.mStatusListener.onTextChange();
                DefaultLogger.d("ScreenTextView", "onTextChanged %s", charSequence);
                if (!ScreenTextView.this.mIsTextChanged) {
                    ScreenTextView.this.mIsTextChanged = true;
                    ScreenTextView.this.mEditorView.notifyOperationUpdate();
                }
            }
        };
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.text.ScreenTextView.2
            {
                ScreenTextView.this = this;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenTextView.this.invalidate();
            }
        };
    }

    public final void init() {
        TextEditDialogOld textEditDialogOld = new TextEditDialogOld();
        this.mTextEditDialog = textEditDialogOld;
        textEditDialogOld.setConfigChangeListener(new ConfigListener());
        this.mTextEditDialog.setTextWatch(this.mTextWatcher);
        this.mTextEditDialog.setStatusListener(this.mStatusListener);
        this.mAuxiliaryPaint.setColor(-1);
        this.mAuxiliaryPaint.setStyle(Paint.Style.STROKE);
        this.mAuxiliaryPaint.setStrokeWidth(1.0f);
        this.mEditorView.setFeatureGestureListener(this.mGesListener);
        this.mOperationDrawable = new PaintElementOperationDrawable(this.mContext.getResources());
        Context context = this.mContext;
        if (context instanceof FragmentActivity) {
            this.mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        }
        TextConfig defaultTextConfig = TextManager.getDefaultTextConfig();
        addNewItem(defaultTextConfig);
        performSetDialog(defaultTextConfig, 0);
    }

    @Override // com.miui.gallery.editor.photo.screen.text.IScreenTextOperation
    public void add(TextConfig textConfig, int i) {
        int i2 = this.mCurrentMenuItemIndex;
        if (i == i2) {
            return;
        }
        getDialogStatusData(getTextConfigDataByIndex(i2));
        this.mBubbleText = getItemTextDialogConfig();
        removeLastItem();
        addNewItem(textConfig);
        if (!isItemActivation()) {
            setLastItemActivation();
        }
        performSetDialog(textConfig, i);
        updateDrawNode();
    }

    public void clear() {
        this.mIsTextChanged = false;
        this.mCurrentMenuItemIndex = -1;
        this.mBubbleText = null;
        this.mITextDialogConfigs.clear();
        this.mTextConfigDataArray.clear();
        this.mCurrentDialogConfigIndex = -1;
    }

    public void onItemEdit() {
        if (!this.mNeedDrawOverlay) {
            this.mNeedDrawOverlay = true;
            invalidate();
        } else if (this.mTextEditDialog.isShowing()) {
        } else {
            String itemTextDialogConfig = getItemTextDialogConfig();
            this.mTextEditDialog.setWillEditText(itemTextDialogConfig, this.mContext.getString(R.string.text_append_hint).equals(itemTextDialogConfig));
            DialogStatusData textConfigDataByIndex = getTextConfigDataByIndex(this.mCurrentMenuItemIndex);
            getCurrentItemStatus(textConfigDataByIndex);
            this.mTextEditDialog.setInitializeData(textConfigDataByIndex);
            this.mTextEditDialog.showAllowingStateLoss(this.mFragmentManager, "TextEditDialog");
        }
    }

    public final String getItemTextDialogConfig() {
        return !isIndexValid(this.mCurrentDialogConfigIndex) ? "" : this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex).getText();
    }

    public final void getCurrentItemStatus(DialogStatusData dialogStatusData) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            dialogStatusData.setEmpty();
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        dialogStatusData.color = iTextDialogConfig.getColor();
        dialogStatusData.transparentProgress = iTextDialogConfig.getTextTransparent();
        dialogStatusData.textStyle = iTextDialogConfig.getTextStyle();
        dialogStatusData.textBold = iTextDialogConfig.isBoldText();
        dialogStatusData.textShadow = iTextDialogConfig.isShadow();
        dialogStatusData.textAlignment = iTextDialogConfig.getTextAlignment();
        dialogStatusData.itemPositionX = iTextDialogConfig.getUserLocationX();
        dialogStatusData.itemPositionY = iTextDialogConfig.getUserLocationY();
        dialogStatusData.itemScale = iTextDialogConfig.getUserScaleMultiple();
        dialogStatusData.itemDegree = iTextDialogConfig.getRotateDegrees();
    }

    public final void performSetDialog(TextConfig textConfig, int i) {
        boolean z;
        String str;
        BaseDialogModel baseDialogModel = textConfig.getBaseDialogModel();
        DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(i);
        if (dialogStatusData == null) {
            dialogStatusData = getTextConfigDataByIndex(i);
            if (textConfig.isWatermark()) {
                dialogStatusData.watermarkInitSelf(textConfig.getWatermarkInfo());
            } else {
                dialogStatusData.configSelfByInit(baseDialogModel);
            }
            z = true;
        } else {
            z = false;
        }
        if (!textConfig.isWatermark() && (str = this.mBubbleText) != null) {
            dialogStatusData.text = str;
        }
        enableStatusForCurrentItem(dialogStatusData, z);
        this.mCurrentMenuItemIndex = i;
        setItemDialogModel(baseDialogModel);
    }

    public final void enableStatusForCurrentItem(DialogStatusData dialogStatusData, boolean z) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setDialogWithStatusData(dialogStatusData);
        if (z) {
            iTextDialogConfig.setImageInitDisplayRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect);
        }
        countAndInvalidate(iTextDialogConfig, false, false);
        configOperationPosition(iTextDialogConfig);
    }

    public final void setItemDialogModel(BaseDialogModel baseDialogModel) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setDialogModel(baseDialogModel, this.mContext.getResources());
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationDecoration(iTextDialogConfig);
        configOperationPosition(iTextDialogConfig);
    }

    public final boolean isItemActivation() {
        return this.mCurrentDialogConfigIndex != -1;
    }

    public final DialogStatusData getTextConfigDataByIndex(int i) {
        DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(i);
        if (dialogStatusData == null) {
            DialogStatusData dialogStatusData2 = new DialogStatusData();
            dialogStatusData2.setEmpty();
            this.mTextConfigDataArray.put(i, dialogStatusData2);
            return dialogStatusData2;
        }
        return dialogStatusData;
    }

    public final void getDialogStatusData(DialogStatusData dialogStatusData) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            dialogStatusData.setEmpty();
        } else {
            this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex).getDialogStatusData(dialogStatusData);
        }
    }

    public final void removeLastItem() {
        if (this.mITextDialogConfigs.size() > 0) {
            List<ITextDialogConfig> list = this.mITextDialogConfigs;
            list.remove(list.size() - 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void addNewItem(TextConfig textConfig) {
        TextAppendConfig textAppendConfig;
        if (textConfig == null || !textConfig.isWatermark()) {
            textAppendConfig = new TextAppendConfig(this.mContext);
        } else {
            WatermarkDialog watermarkDialog = new WatermarkDialog(this.mContext.getResources(), textConfig.getWatermarkInfo());
            watermarkDialog.setImageInitDisplayRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect);
            watermarkDialog.setBitmapLoadingListener(new WatermarkDialog.BitmapLoadingListener() { // from class: com.miui.gallery.editor.photo.screen.text.ScreenTextView$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog.BitmapLoadingListener
                public final void onCompleted() {
                    ScreenTextView.$r8$lambda$1gsrLTMZeDVi_01JWduoKnqjwes(ScreenTextView.this);
                }
            });
            watermarkDialog.setPaddingTop(this.mEditorView.getPaddingTop());
            textAppendConfig = watermarkDialog;
        }
        if (!TextUtils.isEmpty(this.mBubbleText)) {
            textAppendConfig.setText(this.mBubbleText);
        }
        this.mITextDialogConfigs.add(textAppendConfig);
        textAppendConfig.setImageInitDisplayRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect);
        countAndInvalidate(textAppendConfig, false, true);
        setLastItemActivation();
    }

    public /* synthetic */ void lambda$addNewItem$0() {
        invalidate();
    }

    public final void setLastItemActivation() {
        setActivation(this.mITextDialogConfigs.size() - 1);
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void clearActivation() {
        this.mEditorView.disableChildHandleMode();
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void onChangeOperation(boolean z) {
        if (z) {
            init();
            addNewDrawNode();
        } else {
            clearActivation();
            doEditorExit();
        }
        invalidate();
    }

    public final void doEditorExit() {
        this.mEditorView.removeRevokedDrawNode(this.mTextDrawNode);
        if (!this.mIsTextChanged) {
            removeLastItem();
            this.mTextDrawNode = null;
            this.mCurrentDialogConfigIndex = -1;
        } else {
            this.mTextDrawNode.setSaved(true);
            addDrawNode(this.mTextDrawNode);
            this.mSavedITextDialogConfigs.addAll(this.mITextDialogConfigs);
        }
        clear();
    }

    public final void addNewDrawNode() {
        ScreenTextDrawNode screenTextDrawNode = new ScreenTextDrawNode();
        this.mTextDrawNode = screenTextDrawNode;
        screenTextDrawNode.setBitmapRects(this.mEditorView.getOriginBitmapRectF(), this.mEditorView.getDisplayInOriginBitmapRectF());
        this.mTextDrawNode.setDisplayToBitmapMatrix(getBitmapGestureParamsHolder().mDisplayToBitmapMatrix);
        updateDrawNode();
    }

    public final void updateDrawNode() {
        if (this.mTextDrawNode == null) {
            return;
        }
        List<ITextDialogConfig> list = this.mITextDialogConfigs;
        this.mTextDrawNode.setTextDialogConfig(list.get(this.mITextDialogConfigs.size() - 1));
    }

    public void drawCurrentNode(Canvas canvas, RectF rectF) {
        ScreenTextDrawNode screenTextDrawNode = this.mTextDrawNode;
        if (screenTextDrawNode != null) {
            screenTextDrawNode.draw(canvas, rectF);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void drawOverlay(Canvas canvas) {
        if (isIndexValid(this.mCurrentDialogConfigIndex) && this.mNeedDrawOverlay) {
            canvas.save();
            canvas.clipRect(getBitmapGestureParamsHolder().mBitmapDisplayRect);
            this.mOperationDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public boolean canRevoke() {
        return this.mIsTextChanged;
    }

    public void doRevoke() {
        this.mIsTextChanged = false;
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        this.mRevokeText = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex).getText();
        setItemText(this.mContext.getString(R.string.text_append_hint));
        this.mEditorView.addRevokedDrawNode(this.mTextDrawNode);
        invalidate();
    }

    public void doRevert() {
        this.mIsTextChanged = true;
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        setItemText(this.mRevokeText);
        invalidate();
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void canvasMatrixChange() {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        configOperationPosition(this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex));
    }

    public void bitmapMatrixChange() {
        for (ITextDialogConfig iTextDialogConfig : this.mITextDialogConfigs) {
            updateTextConfig(iTextDialogConfig);
        }
        ScreenTextDrawNode screenTextDrawNode = this.mTextDrawNode;
        if (screenTextDrawNode != null) {
            screenTextDrawNode.setDisplayToBitmapMatrix(getBitmapGestureParamsHolder().mDisplayToBitmapMatrix);
        }
    }

    public final void updateTextConfig(ITextDialogConfig iTextDialogConfig) {
        if (iTextDialogConfig != null) {
            iTextDialogConfig.setImageInitDisplayRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect);
            countAndInvalidate(iTextDialogConfig, false, true);
            configOperationPosition(iTextDialogConfig);
        }
    }

    public void setItemText(String str) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        if (TextUtils.equals(str, iTextDialogConfig.getText())) {
            return;
        }
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setText(str);
        countAndInvalidate(iTextDialogConfig, false, true);
        configOperationPosition(iTextDialogConfig);
    }

    /* loaded from: classes2.dex */
    public class ConfigListener implements TextEditDialog.ConfigChangeListener {
        public ConfigListener() {
            ScreenTextView.this = r1;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onColorChange(int i) {
            ScreenTextView.this.setItemTextColor(i);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTransparentChange(int i) {
            ScreenTextView.this.setItemTransparent(i / 100.0f);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onShadowChange(boolean z) {
            ScreenTextView.this.setItemShadow(z);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTextAlignChange(AutoLineLayout.TextAlignment textAlignment) {
            ScreenTextView.this.setItemTextAlignment(textAlignment);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onBoldChange(boolean z) {
            ScreenTextView.this.setItemBold(z);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTypefaceChange(TextStyle textStyle) {
            ScreenTextView.this.setItemTypeface(textStyle);
        }
    }

    public final void setItemTextColor(int i) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setColor(i);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public final boolean isIndexValid(int i) {
        return i >= 0 && i < this.mITextDialogConfigs.size();
    }

    public final void setItemTransparent(float f) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setTextTransparent(f);
        countAndInvalidate(iTextDialogConfig, true, false);
    }

    public final void setItemShadow(boolean z) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setShadow(z);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    public final void setItemTextAlignment(AutoLineLayout.TextAlignment textAlignment) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setTextAlignment(textAlignment);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, false);
    }

    public final void setItemBold(boolean z) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setBoldText(z);
        iTextDialogConfig.setActivation(true);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    public final void setItemTypeface(TextStyle textStyle) {
        if (!isIndexValid(this.mCurrentDialogConfigIndex)) {
            return;
        }
        ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(this.mCurrentDialogConfigIndex);
        iTextDialogConfig.setActivation(true);
        iTextDialogConfig.setTextStyle(textStyle);
        countAndInvalidate(iTextDialogConfig, false, true);
    }

    /* loaded from: classes2.dex */
    public class StatusListener implements TextEditDialog.StatusListener {
        public int mDialogBottom;

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onDismiss() {
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onShow() {
        }

        public StatusListener() {
            ScreenTextView.this = r1;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onBottomChange(int i) {
            this.mDialogBottom = i;
            DefaultLogger.d("ScreenTextView", "onBottomChange: %d", Integer.valueOf(i));
            int activationItemBottom = ScreenTextView.this.getActivationItemBottom();
            DefaultLogger.d("ScreenTextView", "text bottom: %d", Integer.valueOf(activationItemBottom));
            if (activationItemBottom > i) {
                ScreenTextView.this.offsetWithAnimator(i - activationItemBottom);
            } else {
                ScreenTextView.this.offsetWithAnimator(0.0f);
            }
        }

        public void onTextChange() {
            onBottomChange(this.mDialogBottom);
        }
    }

    public int getActivationItemBottom() {
        this.mEditorView.getLocationInWindow(this.mLocation);
        int i = this.mLocation[1];
        return this.mCurrentDialogConfigIndex != -1 ? (int) (i + this.mOperationDrawable.findLowerDecorationPosition()) : i;
    }

    public void offsetWithAnimator(float f) {
        if (this.mCurrentDialogConfigIndex == -1 || this.mCurrentTargetOffsetY == f) {
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
        this.mOffsetAnimator.setTarget(this);
        this.mOffsetAnimator.setPropertyName("canvasOffsetY");
        this.mOffsetAnimator.setFloatValues(this.mCanvasOffsetY, f);
        this.mOffsetAnimator.start();
        DefaultLogger.d("ScreenTextView", "start targetOffset : %f", Float.valueOf(f));
    }

    /* loaded from: classes2.dex */
    public class GesListener implements ScreenBaseGestureView.FeatureGesListener {
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
        public TouchAction mTouchAction;

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public GesListener() {
            ScreenTextView.this = r2;
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
            this.mPointTemp1 = new float[2];
            this.mPointTemp2 = new float[2];
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            ScreenTextView.this.mEditorView.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            float[] fArr = this.mPointTemp1;
            float f = fArr[0];
            this.mDownX = f;
            float f2 = fArr[1];
            this.mDownY = f2;
            int findItemByEvent = ScreenTextView.this.findItemByEvent(f, f2);
            this.mDownIndex = findItemByEvent;
            if (findItemByEvent != -1) {
                ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) ScreenTextView.this.mITextDialogConfigs.get(this.mDownIndex);
                this.mDownConfig = iTextDialogConfig;
                iTextDialogConfig.getOutLineRect(this.mRectF);
            } else {
                this.mDownConfig = null;
            }
            ScreenTextView screenTextView = ScreenTextView.this;
            if (screenTextView.isIndexValid(screenTextView.mCurrentDialogConfigIndex)) {
                this.mTouchAction = ScreenTextView.this.findTouchAction(motionEvent.getX(), motionEvent.getY());
                ITextDialogConfig iTextDialogConfig2 = (ITextDialogConfig) ScreenTextView.this.mITextDialogConfigs.get(ScreenTextView.this.mCurrentDialogConfigIndex);
                this.mCurrentConfig = iTextDialogConfig2;
                this.mCenterX = -1.0f;
                this.mNeedInit = true;
                iTextDialogConfig2.getOutLineRect(this.mRectF);
            } else {
                this.mTouchAction = TouchAction.NONE;
                this.mCurrentConfig = null;
            }
            DefaultLogger.d("ScreenTextView", "mTouchAction : %s", this.mTouchAction);
            return true;
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            DefaultLogger.d("ScreenTextView", "click number： %d", Integer.valueOf(this.mDownIndex));
            ScreenTextView.this.mEditorView.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            if (ScreenTextView.this.mCurrentDialogConfigIndex == -1) {
                int i = this.mDownIndex;
                if (i == -1) {
                    return;
                }
                ScreenTextView.this.setActivation(i);
            } else if (this.mCurrentConfig == null) {
            } else {
                switch (AnonymousClass3.$SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[this.mTouchAction.ordinal()]) {
                    case 1:
                        if (!this.mCurrentConfig.isDialogEnable()) {
                            return;
                        }
                        this.mCurrentConfig.toggleMirror();
                        ScreenTextView.this.countAndInvalidate(this.mCurrentConfig, true, true);
                        return;
                    case 2:
                        if (!this.mCurrentConfig.isWatermark()) {
                            return;
                        }
                        this.mCurrentConfig.reverseColor(-16777216);
                        ScreenTextView.this.updateOperationDrawable(false);
                        ScreenTextView.this.invalidate();
                        return;
                    case 3:
                        if (!this.mCurrentConfig.isWatermark()) {
                            return;
                        }
                        this.mCurrentConfig.reverseColor(-1);
                        ScreenTextView.this.updateOperationDrawable(true);
                        ScreenTextView.this.invalidate();
                        return;
                    case 4:
                    case 5:
                        return;
                    case 6:
                        ScreenTextView.this.onItemEdit();
                        return;
                    default:
                        ITextDialogConfig iTextDialogConfig = this.mCurrentConfig;
                        float[] fArr = this.mPointTemp1;
                        if (!iTextDialogConfig.contains(fArr[0], fArr[1])) {
                            if (!ScreenTextView.this.mNeedDrawOverlay) {
                                return;
                            }
                            ScreenTextView.this.mNeedDrawOverlay = false;
                            ScreenTextView.this.clearActivation(true);
                            return;
                        }
                        ScreenTextView.this.onItemEdit();
                        return;
                }
            }
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            ScreenTextView.this.mEditorView.convertPointToViewPortCoordinate(motionEvent, this.mPointTemp1);
            ScreenTextView.this.mEditorView.convertPointToViewPortCoordinate(motionEvent2, this.mPointTemp2);
            float[] fArr = this.mPointTemp2;
            float f3 = fArr[0];
            float f4 = fArr[1];
            float[] fArr2 = this.mPointTemp1;
            float f5 = fArr2[0];
            float f6 = fArr2[1];
            float convertDistanceX = ScreenTextView.this.mEditorView.convertDistanceX(f);
            float convertDistanceY = ScreenTextView.this.mEditorView.convertDistanceY(f2);
            if (AnonymousClass3.$SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[this.mTouchAction.ordinal()] != 5) {
                if (ScreenTextView.this.mCurrentDialogConfigIndex != -1) {
                    doScroll(convertDistanceX, convertDistanceY, this.mCurrentConfig);
                } else {
                    int i = this.mDownIndex;
                    if (i != -1) {
                        ScreenTextView.this.setActivation(i);
                        ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) ScreenTextView.this.mITextDialogConfigs.get(this.mDownIndex);
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
                ScreenTextView.this.mOperationDrawable.setDrawDecoration(false);
                ScreenTextView.this.countAndInvalidate(this.mCurrentConfig, false, false);
            }
            ITextDialogConfig iTextDialogConfig2 = this.mCurrentConfig;
            if (iTextDialogConfig2 != null) {
                ScreenTextView.this.configOperationPosition(iTextDialogConfig2);
            }
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            int findActivationIndex = ScreenTextView.this.findActivationIndex();
            if (findActivationIndex >= 0) {
                ITextDialogConfig iTextDialogConfig = (ITextDialogConfig) ScreenTextView.this.mITextDialogConfigs.get(findActivationIndex);
                iTextDialogConfig.refreshRotateDegree();
                iTextDialogConfig.setDrawOutline(true);
                ScreenTextView.this.countAndInvalidate(iTextDialogConfig, true, false);
            }
            ScreenTextView.this.mOperationDrawable.setDrawDecoration(true);
            ScreenTextView.this.invalidate();
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x0038, code lost:
            if (r4 > 0.0f) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x003a, code lost:
            r4 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x0047, code lost:
            if (r4 < 0.0f) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x0055, code lost:
            if (r5 > 0.0f) goto L11;
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x0057, code lost:
            r5 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x0064, code lost:
            if (r5 < 0.0f) goto L11;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void doScroll(float r4, float r5, com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig r6) {
            /*
                r3 = this;
                if (r6 == 0) goto L8f
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r0 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                android.graphics.RectF r0 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3400(r0)
                r6.getOutLineRect(r0)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r0 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.widget.imageview.BitmapGestureParamsHolder r0 = r0.getBitmapGestureParamsHolder()
                android.graphics.Matrix r0 = r0.mCanvasMatrix
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                android.graphics.RectF r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3400(r1)
                r0.mapRect(r1)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r0 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.home.ScreenEditorView r0 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3500(r0)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                android.graphics.RectF r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3400(r1)
                int r0 = r0.getRectOverScrollStatus(r1)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3600(r1)
                r1 = r0 & 8
                r2 = 0
                if (r1 == 0) goto L3c
                int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r1 <= 0) goto L4a
            L3a:
                r4 = r2
                goto L4a
            L3c:
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3700(r1)
                r1 = r0 & 4
                if (r1 == 0) goto L4a
                int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r1 >= 0) goto L4a
                goto L3a
            L4a:
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3800(r1)
                r1 = r0 & 2
                if (r1 == 0) goto L59
                int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
                if (r0 <= 0) goto L67
            L57:
                r5 = r2
                goto L67
            L59:
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r1 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3900(r1)
                r0 = r0 & 1
                if (r0 == 0) goto L67
                int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
                if (r0 >= 0) goto L67
                goto L57
            L67:
                float r4 = -r4
                r6.appendUserLocationX(r4)
                float r4 = -r5
                r6.appendUserLocationY(r4)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r4 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                r5 = 0
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$2000(r4, r6, r5, r5)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r4 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig r6 = r3.mCurrentConfig
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3100(r4, r6)
                com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig r4 = r3.mCurrentConfig
                r4.setDrawOutline(r5)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r4 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.core.imports.doodle.PaintElementOperationDrawable r4 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$3000(r4)
                r4.setDrawDecoration(r5)
                com.miui.gallery.editor.photo.screen.text.ScreenTextView r4 = com.miui.gallery.editor.photo.screen.text.ScreenTextView.this
                com.miui.gallery.editor.photo.screen.text.ScreenTextView.access$4000(r4)
            L8f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.screen.text.ScreenTextView.GesListener.doScroll(float, float, com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig):void");
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ITextDialogConfig iTextDialogConfig = this.mCurrentConfig;
            if (iTextDialogConfig != null) {
                iTextDialogConfig.setUserScaleMultiple(iTextDialogConfig.getUserScaleMultiple() * scaleGestureDetector.getScaleFactor());
            }
            ScreenTextView.this.invalidate();
            return false;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ScreenTextView screenTextView = ScreenTextView.this;
            if (screenTextView.isIndexValid(screenTextView.mCurrentDialogConfigIndex)) {
                this.mCurrentConfig = (ITextDialogConfig) ScreenTextView.this.mITextDialogConfigs.get(ScreenTextView.this.mCurrentDialogConfigIndex);
                return false;
            }
            this.mCurrentConfig = null;
            return false;
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.screen.text.ScreenTextView$3 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction;

        static {
            int[] iArr = new int[TouchAction.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction = iArr;
            try {
                iArr[TouchAction.MIRROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.REVERSE_WHITE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.REVERSE_BLACK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.DELETE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.SCALE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.EDIT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$text$ScreenTextView$TouchAction[TouchAction.NONE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
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

    public final TouchAction findTouchAction(float f, float f2) {
        int round = Math.round(f);
        int round2 = Math.round(f2);
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.DELETE, this.mRectFTemp);
        float f3 = round;
        float f4 = round2;
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.DELETE;
        }
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.SCALE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.SCALE;
        }
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.MIRROR, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.MIRROR;
        }
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.EDIT, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.EDIT;
        }
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.REVERSE_WHITE, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.REVERSE_WHITE;
        }
        this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.REVERSE_BLACK, this.mRectFTemp);
        if (this.mRectFTemp.contains(f3, f4)) {
            return TouchAction.REVERSE_BLACK;
        }
        return TouchAction.NONE;
    }

    public void setActivation(int i) {
        this.mNeedDrawOverlay = true;
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
        this.mCurrentDialogConfigIndex = i;
        this.mEditorView.enableChildHandleMode();
        invalidate();
    }

    public final void configOperationPosition(ITextDialogConfig iTextDialogConfig) {
        iTextDialogConfig.getOutLineRect(this.mRectFTemp);
        this.mOperationDrawable.configDecorationPositon(this.mRectFTemp, getBitmapGestureParamsHolder().mCanvasMatrix, iTextDialogConfig.getRotateDegrees(), this.mRectFTemp.centerX(), this.mRectFTemp.centerY());
    }

    public final void countAndInvalidate(ITextDialogConfig iTextDialogConfig, boolean z, boolean z2) {
        iTextDialogConfig.countLocation(z2, getBitmapGestureParamsHolder().mBitmapDisplayInitRect.width());
        if (z) {
            iTextDialogConfig.getOutLineRect(this.mRectFTemp);
            this.mRectFTemp.roundOut(this.mRectTemp);
            this.mEditorView.invalidate(this.mRectTemp);
            return;
        }
        invalidate();
    }

    public final void configOperationDecoration(ITextDialogConfig iTextDialogConfig) {
        if (iTextDialogConfig.isWatermark()) {
            this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.EDIT, null, PaintElementOperationDrawable.Action.SCALE, iTextDialogConfig.isReverseColor() ? PaintElementOperationDrawable.Action.REVERSE_BLACK : PaintElementOperationDrawable.Action.REVERSE_WHITE, this.mContext.getResources());
        } else if (!iTextDialogConfig.isDialogEnable()) {
            this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.EDIT, null, PaintElementOperationDrawable.Action.SCALE, null, this.mContext.getResources());
        } else {
            this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.EDIT, null, PaintElementOperationDrawable.Action.SCALE, PaintElementOperationDrawable.Action.MIRROR, this.mContext.getResources());
        }
    }

    public final void updateOperationDrawable(boolean z) {
        PaintElementOperationDrawable.Action action;
        if (z) {
            action = PaintElementOperationDrawable.Action.REVERSE_WHITE;
        } else {
            action = PaintElementOperationDrawable.Action.REVERSE_BLACK;
        }
        this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.EDIT, null, PaintElementOperationDrawable.Action.SCALE, action, this.mContext.getResources());
    }

    public final void clearActivation(boolean z) {
        int i = 0;
        while (i < this.mITextDialogConfigs.size()) {
            ITextDialogConfig iTextDialogConfig = this.mITextDialogConfigs.get(i);
            if (TextUtils.isEmpty(iTextDialogConfig.getText())) {
                this.mITextDialogConfigs.remove(i);
                clear();
                i--;
            } else {
                iTextDialogConfig.setDrawOutline(false);
                iTextDialogConfig.setActivation(false);
            }
            i++;
        }
        this.mEditorView.disableChildHandleMode();
        if (z) {
            invalidate();
        }
    }

    public final int findActivationIndex() {
        for (int i = 0; i < this.mITextDialogConfigs.size(); i++) {
            if (this.mITextDialogConfigs.get(i).isActivation()) {
                return i;
            }
        }
        return -1;
    }
}
