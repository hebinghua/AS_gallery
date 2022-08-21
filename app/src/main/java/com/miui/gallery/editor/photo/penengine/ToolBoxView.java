package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.photo.penengine.BrushPopupWindow;
import com.miui.gallery.editor.photo.penengine.ColorPickView;
import com.miui.gallery.editor.photo.penengine.ColorPopupWindow;
import com.miui.gallery.editor.photo.penengine.MosaicPopupWindow;
import com.miui.gallery.editor.photo.penengine.ShapePopupWindow;
import com.miui.gallery.editor.photo.penengine.TextPopupWindow;
import com.miui.gallery.editor.photo.penengine.entity.Brush;
import com.miui.gallery.editor.photo.penengine.entity.CommonBrush;
import com.miui.gallery.editor.photo.penengine.entity.IActivable;
import com.miui.gallery.editor.photo.penengine.entity.IColorable;
import com.miui.gallery.editor.photo.penengine.entity.IDefaultColorable;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import com.miui.gallery.editor.photo.screen.stat.ScreenEditorStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class ToolBoxView extends RelativeLayout {
    public BrushPopupWindow.BrushChangeListener mBrushChangeListener;
    public ColorSelectView mCenterColorView;
    public ColorPopupWindow.OnColorChangeListener mColorPickListener;
    public Context mContext;
    public ColorSelectView mCurrentSelectColorView;
    public View mCurrentSelectToolView;
    public ActivableToolView mEraserATV;
    public boolean mLargeScreen;
    public ColorSelectView mLeftColorView;
    public ActivableToolView mMarkATV;
    public ActivableToolView mMosaicATV;
    public MosaicPopupWindow.MosaicChangeListener mMosaicChangeListener;
    public ActivableToolView mPenATV;
    public PopupWindowManager mPopupWindowManager;
    public View.OnClickListener mReDoListener;
    public ImageView mRedoIv;
    public ColorSelectView mRightColorView;
    public View.OnClickListener mSaveBtnListener;
    public ImageView mSaveIv;
    public ShapePopupWindow.ShapeChangeListener mShapeChangeListener;
    public ShapeToolView mShapeIvLeft;
    public ShapeToolView mShapeIvRight;
    public ColorSelectView mSingleColorView;
    public ActivableToolView mTextATV;
    public TextPopupWindow.TextChangeListener mTextChangeListener;
    public ToolChangeListener mToolChangeListener;
    public View.OnClickListener mToolClickListener;
    public ToolManager mToolManager;
    public View.OnClickListener mUnDoListener;
    public ImageView mUndoIv;

    /* loaded from: classes2.dex */
    public interface ToolChangeListener {
        void onBrushAlphaChange(float f);

        void onBrushColorChange(int i);

        void onBrushSizeChange(int i);

        void onMosaicDataChange(MosaicData mosaicData, int i);

        void onMosaicPaintSizeChange(int i);

        void onShapeChange(DoodleData doodleData, int i);

        void onShapeColorChange(int i);

        void onTextChange(TextConfig textConfig, int i);

        void onToolSelect(Tool tool);
    }

    public static /* synthetic */ void $r8$lambda$JPjMkPEvZmxfDzR1ePydYgf0oUc(ToolBoxView toolBoxView, View view) {
        toolBoxView.lambda$findView$2(view);
    }

    /* renamed from: $r8$lambda$UJOaUzaKo7h-Yl2Y0ZTmA55CdrU */
    public static /* synthetic */ void m918$r8$lambda$UJOaUzaKo7hYl2Y0ZTmA55CdrU(ToolBoxView toolBoxView, View view) {
        toolBoxView.lambda$findView$3(view);
    }

    /* renamed from: $r8$lambda$fn0if1oFnv3E1j-7nFj-VLVg4o0 */
    public static /* synthetic */ void m919$r8$lambda$fn0if1oFnv3E1j7nFjVLVg4o0(ToolBoxView toolBoxView, View view) {
        toolBoxView.lambda$findView$1(view);
    }

    public static /* synthetic */ void $r8$lambda$zhd1JLZVRbWgqLW9KfUF5UaysjY(ToolBoxView toolBoxView, View view) {
        toolBoxView.lambda$new$0(view);
    }

    public ToolBoxView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ToolBoxView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBrushChangeListener = new BrushPopupWindow.BrushChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView.1
            {
                ToolBoxView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.BrushPopupWindow.BrushChangeListener
            public void onBrushSizeChange(CommonBrush commonBrush) {
                DefaultLogger.d("MiuiToolBoxView", "size changed is : " + commonBrush.getSelectSizeIndex());
                if (ToolBoxView.this.mToolChangeListener != null) {
                    ToolBoxView.this.mToolChangeListener.onBrushSizeChange(commonBrush.getSize());
                }
            }

            @Override // com.miui.gallery.editor.photo.penengine.BrushPopupWindow.BrushChangeListener
            public void onBrushAlphaChange(CommonBrush commonBrush) {
                DefaultLogger.d("MiuiToolBoxView", "onBrushAlphaChange: " + commonBrush.getAlpha());
                if (ToolBoxView.this.mToolChangeListener != null) {
                    ToolBoxView.this.mToolChangeListener.onBrushAlphaChange(commonBrush.getAlpha());
                }
            }
        };
        this.mMosaicChangeListener = new MosaicPopupWindow.MosaicChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView.2
            {
                ToolBoxView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.MosaicPopupWindow.MosaicChangeListener
            public void onCurrentSelectIndexChange(Mosaic mosaic) {
                ToolBoxView.this.mToolChangeListener.onMosaicDataChange(mosaic.getMosaicData(), mosaic.getCurrentSelectIndex());
            }

            @Override // com.miui.gallery.editor.photo.penengine.MosaicPopupWindow.MosaicChangeListener
            public void onSizeChange(int i2) {
                ToolBoxView.this.mPopupWindowManager.setMosaicPaintPopupWindowPaintSize(i2);
                ToolBoxView.this.mToolChangeListener.onMosaicPaintSizeChange(i2);
            }

            @Override // com.miui.gallery.editor.photo.penengine.MosaicPopupWindow.MosaicChangeListener
            public void onStartChangeProgress() {
                ToolBoxView.this.mPopupWindowManager.showMosaicPaintPopupWindow(ToolBoxView.this);
            }

            @Override // com.miui.gallery.editor.photo.penengine.MosaicPopupWindow.MosaicChangeListener
            public void onStopChangeProgress(Mosaic mosaic, float f) {
                ToolBoxView.this.mPopupWindowManager.dismissMosaicPaintPopupWindow();
                ToolBoxView.this.mToolChangeListener.onMosaicPaintSizeChange(mosaic.getSize());
                ScreenEditorStatUtils.statMosaicSizeChoose(Math.round(f * 100.0f));
            }
        };
        this.mTextChangeListener = new TextPopupWindow.TextChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView.3
            {
                ToolBoxView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.TextPopupWindow.TextChangeListener
            public void onTextChange(TextConfig textConfig, int i2) {
                ToolBoxView.this.mToolChangeListener.onTextChange(textConfig, i2);
            }
        };
        this.mShapeChangeListener = new ShapePopupWindow.ShapeChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView.4
            {
                ToolBoxView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.ShapePopupWindow.ShapeChangeListener
            public void onShapeChange(DoodleData doodleData, int i2) {
                ToolBoxView.this.mToolChangeListener.onShapeChange(doodleData, i2);
            }
        };
        this.mColorPickListener = new ColorPopupWindow.OnColorChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView.5
            {
                ToolBoxView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.ColorPickView.OnColorChangeListener
            public void onColorChange(ColorPickView.ColorChangeEvent colorChangeEvent) {
                DefaultLogger.d("MiuiToolBoxView", "color changed index is : " + colorChangeEvent.getColor());
                Tool currentSelectTool = ToolBoxView.this.mToolManager.getCurrentSelectTool();
                if (currentSelectTool instanceof IDefaultColorable) {
                    IDefaultColorable iDefaultColorable = (IDefaultColorable) currentSelectTool;
                    iDefaultColorable.updateSelectColor(colorChangeEvent.getColor());
                    ToolBoxView.this.mCurrentSelectColorView.setColor(colorChangeEvent.getColor());
                    ToolBoxView.this.onColorChange(iDefaultColorable);
                }
            }
        };
        this.mToolClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolBoxView.$r8$lambda$zhd1JLZVRbWgqLW9KfUF5UaysjY(ToolBoxView.this, view);
            }
        };
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.tool_box_layout, this);
        this.mLargeScreen = Utils.isLargeScreen(context);
        this.mToolManager = new ToolManager(context);
        this.mPopupWindowManager = new PopupWindowManager(context);
        findView();
        initView();
        initialBrush();
        selectTool(this.mPenATV, this.mToolManager.getPen());
    }

    public final void selectTool(View view, Tool tool) {
        updateToolViewSelect(this.mCurrentSelectToolView, false, this.mToolManager.getCurrentSelectTool());
        this.mToolManager.setCurrentSelectTool(tool);
        this.mCurrentSelectToolView = view;
        updateToolViewSelect(view, true, tool);
        if (tool instanceof IDefaultColorable) {
            updateColorView((IDefaultColorable) tool);
        }
        onToolSelect(tool);
    }

    public final void updateColorView(IColorable iColorable) {
        if (this.mLargeScreen) {
            if (!(iColorable instanceof IDefaultColorable)) {
                return;
            }
            updateColorBalls((IDefaultColorable) iColorable);
            return;
        }
        updateSingleColorBall(iColorable);
    }

    public final void initView() {
        updateActivableToolView(this.mPenATV, this.mToolManager.getPen(), this.mToolManager.isMosaicSelected());
        updateActivableToolView(this.mMarkATV, this.mToolManager.getMark(), this.mToolManager.isMarkSelected());
        updateActivableToolView(this.mMosaicATV, this.mToolManager.getMosaic(), this.mToolManager.isMosaicSelected());
        updateActivableToolView(this.mEraserATV, this.mToolManager.getEraser(), this.mToolManager.isEraserSelected());
        updateActivableToolView(this.mTextATV, this.mToolManager.getText(), this.mToolManager.isTextSelected());
        updateOperationViewsVisible(this.mLargeScreen);
        initColorBalls(this.mLargeScreen);
        updateShapeView(this.mLargeScreen);
    }

    public final void updateShapeView(boolean z) {
        if (z) {
            this.mShapeIvLeft.setVisibility(8);
            this.mShapeIvRight.setVisibility(0);
            return;
        }
        this.mShapeIvLeft.setVisibility(0);
        this.mShapeIvRight.setVisibility(8);
    }

    public final void initialBrush() {
        this.mPenATV.setTranslationY(0.0f);
        this.mCurrentSelectToolView = this.mPenATV;
        if (this.mLargeScreen) {
            updateColorBalls(this.mToolManager.getPen());
        } else {
            updateSingleColorBall(this.mToolManager.getPen());
        }
    }

    public final void updateColorBalls(IDefaultColorable iDefaultColorable) {
        this.mLeftColorView.setColor(iDefaultColorable.getColorArray()[0]);
        this.mCenterColorView.setColor(iDefaultColorable.getColorArray()[1]);
        this.mRightColorView.setColor(iDefaultColorable.getColorArray()[2]);
        updateColorBallsCheckStatus(iDefaultColorable);
        updateCurrentSelectColorView(iDefaultColorable);
    }

    public final void updateColorBallsCheckStatus(IDefaultColorable iDefaultColorable) {
        boolean z = false;
        this.mLeftColorView.setCheck(iDefaultColorable.getSelectColorIndex() == 0);
        this.mCenterColorView.setCheck(iDefaultColorable.getSelectColorIndex() == 1);
        ColorSelectView colorSelectView = this.mRightColorView;
        if (iDefaultColorable.getSelectColorIndex() == 2) {
            z = true;
        }
        colorSelectView.setCheck(z);
    }

    public final void updateCurrentSelectColorView(IDefaultColorable iDefaultColorable) {
        if (iDefaultColorable.getSelectColorIndex() == 0) {
            this.mCurrentSelectColorView = this.mLeftColorView;
        } else if (iDefaultColorable.getSelectColorIndex() == 1) {
            this.mCurrentSelectColorView = this.mCenterColorView;
        } else if (iDefaultColorable.getSelectColorIndex() != 2) {
        } else {
            this.mCurrentSelectColorView = this.mRightColorView;
        }
    }

    public final void updateSingleColorBall(IColorable iColorable) {
        this.mSingleColorView.setCheck(true);
        this.mSingleColorView.setColor(iColorable.getColor());
    }

    public final boolean isToolSelected(View view) {
        return this.mCurrentSelectToolView == view;
    }

    public /* synthetic */ void lambda$new$0(View view) {
        int id = view.getId();
        if (id == R.id.brush_normal) {
            CommonBrush pen = this.mToolManager.getPen();
            if (isToolSelected(view)) {
                showBrushPopupWindow(view, pen, this.mBrushChangeListener);
            } else {
                selectTool(view, pen);
            }
        } else if (id == R.id.brush_markpen) {
            CommonBrush mark = this.mToolManager.getMark();
            if (isToolSelected(view)) {
                showBrushPopupWindow(view, mark, this.mBrushChangeListener);
            } else {
                selectTool(view, mark);
            }
        } else if (id == R.id.brush_mosaic) {
            if (isToolSelected(view)) {
                showMosaicPopupWindow(view, this.mToolManager.getMosaic(), this.mMosaicChangeListener);
            } else {
                selectTool(view, this.mToolManager.getMosaic());
            }
        } else if (id == R.id.brush_eraser) {
            if (isToolSelected(view)) {
                return;
            }
            selectTool(view, this.mToolManager.getEraser());
        } else if (id == R.id.brush_text) {
            if (isToolSelected(view)) {
                showTextPopupWindow(view, this.mToolManager.getText(), this.mTextChangeListener);
            } else {
                selectTool(view, this.mToolManager.getText());
            }
        } else if (id == R.id.tool_shape_right || id == R.id.tool_shape_left) {
            if (isToolSelected(view)) {
                showShapePopupWindow(view, this.mToolManager.getShape(), this.mShapeChangeListener);
            } else {
                selectTool(view, this.mToolManager.getShape());
            }
        } else if (id == R.id.brush_color_default_left || id == R.id.brush_color_default_center || id == R.id.brush_color_default_right) {
            Tool currentSelectTool = this.mToolManager.getCurrentSelectTool();
            if (!(currentSelectTool instanceof IDefaultColorable)) {
                return;
            }
            handleColorBallsClick((ColorSelectView) view, (IDefaultColorable) currentSelectTool);
        } else if (id != R.id.color_ball_single) {
        } else {
            Tool currentSelectTool2 = this.mToolManager.getCurrentSelectTool();
            this.mCurrentSelectColorView = this.mSingleColorView;
            if (!(currentSelectTool2 instanceof IColorable)) {
                return;
            }
            showColorPickPopupWindow(view, (IColorable) currentSelectTool2, this.mColorPickListener);
        }
    }

    public final void updateToolViewSelect(View view, boolean z, Tool tool) {
        if (view == null) {
            return;
        }
        view.setSelected(z);
        if (tool instanceof IActivable) {
            ActivableToolView activableToolView = (ActivableToolView) view;
            if (z) {
                activableToolView.performSelectAnim();
            } else {
                activableToolView.performUnselectAnim();
            }
        }
        if (!z || !(tool instanceof IColorable)) {
            return;
        }
        updateColorView((IColorable) tool);
    }

    public final void initColorBalls(boolean z) {
        if (z) {
            this.mSingleColorView.setVisibility(8);
            this.mLeftColorView.setVisibility(0);
            this.mCenterColorView.setVisibility(0);
            this.mRightColorView.setVisibility(0);
            this.mLeftColorView.setContentDescription(getResources().getString(R.string.screen_talkback_color_x, 1));
            this.mCenterColorView.setContentDescription(getResources().getString(R.string.screen_talkback_color_x, 2));
            this.mRightColorView.setContentDescription(getResources().getString(R.string.screen_talkback_color_x, 3));
            return;
        }
        this.mSingleColorView.setVisibility(0);
        this.mSingleColorView.setContentDescription(getResources().getString(R.string.screen_talkback_color_x, 1));
        this.mLeftColorView.setVisibility(8);
        this.mCenterColorView.setVisibility(8);
        this.mRightColorView.setVisibility(8);
    }

    public final void findView() {
        ActivableToolView activableToolView = (ActivableToolView) findViewById(R.id.brush_mosaic);
        this.mMosaicATV = activableToolView;
        activableToolView.setOnClickListener(this.mToolClickListener);
        ActivableToolView activableToolView2 = (ActivableToolView) findViewById(R.id.brush_normal);
        this.mPenATV = activableToolView2;
        activableToolView2.setOnClickListener(this.mToolClickListener);
        ActivableToolView activableToolView3 = (ActivableToolView) findViewById(R.id.brush_markpen);
        this.mMarkATV = activableToolView3;
        activableToolView3.setOnClickListener(this.mToolClickListener);
        ActivableToolView activableToolView4 = (ActivableToolView) findViewById(R.id.brush_eraser);
        this.mEraserATV = activableToolView4;
        activableToolView4.setOnClickListener(this.mToolClickListener);
        ActivableToolView activableToolView5 = (ActivableToolView) findViewById(R.id.brush_text);
        this.mTextATV = activableToolView5;
        activableToolView5.setOnClickListener(this.mToolClickListener);
        ShapeToolView shapeToolView = (ShapeToolView) findViewById(R.id.tool_shape_right);
        this.mShapeIvRight = shapeToolView;
        shapeToolView.setOnClickListener(this.mToolClickListener);
        ShapeToolView shapeToolView2 = (ShapeToolView) findViewById(R.id.tool_shape_left);
        this.mShapeIvLeft = shapeToolView2;
        shapeToolView2.setOnClickListener(this.mToolClickListener);
        ColorSelectView colorSelectView = (ColorSelectView) findViewById(R.id.color_ball_single);
        this.mSingleColorView = colorSelectView;
        colorSelectView.setOnClickListener(this.mToolClickListener);
        Folme.useAt(this.mSingleColorView).touch().handleTouchOf(this.mSingleColorView, new AnimConfig[0]);
        ColorSelectView colorSelectView2 = (ColorSelectView) findViewById(R.id.brush_color_default_left);
        this.mLeftColorView = colorSelectView2;
        colorSelectView2.setOnClickListener(this.mToolClickListener);
        Folme.useAt(this.mLeftColorView).touch().handleTouchOf(this.mLeftColorView, new AnimConfig[0]);
        ColorSelectView colorSelectView3 = (ColorSelectView) findViewById(R.id.brush_color_default_center);
        this.mCenterColorView = colorSelectView3;
        colorSelectView3.setOnClickListener(this.mToolClickListener);
        Folme.useAt(this.mCenterColorView).touch().handleTouchOf(this.mCenterColorView, new AnimConfig[0]);
        ColorSelectView colorSelectView4 = (ColorSelectView) findViewById(R.id.brush_color_default_right);
        this.mRightColorView = colorSelectView4;
        colorSelectView4.setOnClickListener(this.mToolClickListener);
        Folme.useAt(this.mRightColorView).touch().handleTouchOf(this.mRightColorView, new AnimConfig[0]);
        this.mUndoIv = (ImageView) findViewById(R.id.tool_undo_iv);
        this.mRedoIv = (ImageView) findViewById(R.id.tool_redo_iv);
        this.mUndoIv.setEnabled(false);
        this.mUndoIv.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolBoxView.m919$r8$lambda$fn0if1oFnv3E1j7nFjVLVg4o0(ToolBoxView.this, view);
            }
        });
        Folme.useAt(this.mUndoIv).touch().handleTouchOf(this.mUndoIv, new AnimConfig[0]);
        this.mRedoIv.setEnabled(false);
        this.mRedoIv.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolBoxView.$r8$lambda$JPjMkPEvZmxfDzR1ePydYgf0oUc(ToolBoxView.this, view);
            }
        });
        Folme.useAt(this.mRedoIv).touch().handleTouchOf(this.mRedoIv, new AnimConfig[0]);
        ImageView imageView = (ImageView) findViewById(R.id.tool_done_iv);
        this.mSaveIv = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.penengine.ToolBoxView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolBoxView.m918$r8$lambda$UJOaUzaKo7hYl2Y0ZTmA55CdrU(ToolBoxView.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$findView$1(View view) {
        this.mUnDoListener.onClick(view);
    }

    public /* synthetic */ void lambda$findView$2(View view) {
        this.mReDoListener.onClick(view);
    }

    public /* synthetic */ void lambda$findView$3(View view) {
        View.OnClickListener onClickListener = this.mSaveBtnListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public final void updateOperationViewsVisible(boolean z) {
        if (z) {
            this.mUndoIv.setVisibility(0);
            this.mRedoIv.setVisibility(0);
            this.mSaveIv.setVisibility(0);
            return;
        }
        this.mUndoIv.setVisibility(8);
        this.mRedoIv.setVisibility(8);
        this.mSaveIv.setVisibility(8);
    }

    public final void initToolBoxViewDim() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mLeftColorView.getLayoutParams();
        layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_left_color_margin_start));
        this.mLeftColorView.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mSingleColorView.getLayoutParams();
        layoutParams2.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_left_color_margin_start));
        layoutParams2.setMarginEnd(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_color_margin_start));
        this.mSingleColorView.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.mPenATV.getLayoutParams();
        layoutParams3.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_brush_pen_margin_start));
        DefaultLogger.d("MiuiToolBoxView", "initToolBoxViewDim: " + this.mContext.getResources().getDimension(R.dimen.screen_brush_pen_margin_start));
        this.mPenATV.setLayoutParams(layoutParams3);
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) this.mMarkATV.getLayoutParams();
        layoutParams4.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_brush_markpen_margin_start));
        this.mMarkATV.setLayoutParams(layoutParams4);
        LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) this.mMosaicATV.getLayoutParams();
        layoutParams5.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_mosaic_margin_markpen_start));
        this.mMosaicATV.setLayoutParams(layoutParams5);
        LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) this.mEraserATV.getLayoutParams();
        layoutParams6.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_eraser_margin_start));
        this.mEraserATV.setLayoutParams(layoutParams6);
        LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) this.mTextATV.getLayoutParams();
        layoutParams7.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_text_margin_start));
        this.mTextATV.setLayoutParams(layoutParams7);
        RelativeLayout.LayoutParams layoutParams8 = (RelativeLayout.LayoutParams) this.mShapeIvRight.getLayoutParams();
        layoutParams8.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_shape_right_margin_start));
        this.mShapeIvRight.setLayoutParams(layoutParams8);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Log.d("MiuiToolBoxView", "onConfigurationChanged: " + configuration);
        this.mPopupWindowManager.dismissAllShowingPopupWindows();
        this.mPopupWindowManager.resetAllPopupWindows();
        initToolBoxViewDim();
        boolean isLargeScreen = Utils.isLargeScreen(this.mContext);
        updateOperationViewsVisible(isLargeScreen);
        initColorBalls(isLargeScreen);
        updateShapeView(isLargeScreen);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPopupWindowManager.dismissAllShowingPopupWindows();
    }

    public final void handleColorBallsClick(ColorSelectView colorSelectView, IDefaultColorable iDefaultColorable) {
        if (this.mCurrentSelectColorView == colorSelectView) {
            showColorPickPopupWindow(colorSelectView, iDefaultColorable, this.mColorPickListener);
            return;
        }
        int i = 0;
        if (colorSelectView != this.mLeftColorView) {
            if (colorSelectView == this.mCenterColorView) {
                i = 1;
            } else if (colorSelectView == this.mRightColorView) {
                i = 2;
            }
        }
        iDefaultColorable.setSelectColorIndex(i);
        updateColorBallsCheckStatus(iDefaultColorable);
        this.mCurrentSelectColorView = colorSelectView;
        onColorChange(iDefaultColorable);
    }

    public final void onColorChange(IDefaultColorable iDefaultColorable) {
        if (iDefaultColorable instanceof Brush) {
            updateActivableToolView((ActivableToolView) this.mCurrentSelectToolView, (IActivable) ((Brush) iDefaultColorable), true);
            onBrushColorChange(iDefaultColorable.getColor());
        } else if (!(iDefaultColorable instanceof Shape)) {
        } else {
            onShapeColorChange(iDefaultColorable.getColor());
        }
    }

    public final void onBrushColorChange(int i) {
        ToolChangeListener toolChangeListener = this.mToolChangeListener;
        if (toolChangeListener != null) {
            toolChangeListener.onBrushColorChange(i);
        }
    }

    public final void onShapeColorChange(int i) {
        ToolChangeListener toolChangeListener = this.mToolChangeListener;
        if (toolChangeListener != null) {
            toolChangeListener.onShapeColorChange(i);
        }
    }

    public final void updateActivableToolView(ActivableToolView activableToolView, IActivable iActivable, boolean z) {
        updateActivableToolView(activableToolView, iActivable.createFg(this.mContext), iActivable.createBg(this.mContext, z));
    }

    public final void updateActivableToolView(ActivableToolView activableToolView, Drawable drawable, Drawable drawable2) {
        activableToolView.setToolForeground(drawable);
        activableToolView.setToolBackground(drawable2);
    }

    public final void showColorPickPopupWindow(View view, IColorable iColorable, ColorPopupWindow.OnColorChangeListener onColorChangeListener) {
        int i = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_color_popwin_offset_x);
        int i2 = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_color_popwin_offset_y);
        boolean z = this.mLargeScreen;
        this.mPopupWindowManager.showColorPickPopupWindow(!z ? this : view, iColorable.getColor(), onColorChangeListener, z, i, i2);
    }

    public final void showBrushPopupWindow(View view, CommonBrush commonBrush, BrushPopupWindow.BrushChangeListener brushChangeListener) {
        DefaultLogger.d("MiuiToolBoxView", "showBrushPopupWindow: ");
        int i = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_brush_popwin_offset_x);
        int i2 = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_brush_popwin_offset_y);
        boolean z = this.mLargeScreen;
        this.mPopupWindowManager.showBrushPopupWindow(!z ? this : view, commonBrush, brushChangeListener, z, i, i2);
    }

    public final void showMosaicPopupWindow(View view, Mosaic mosaic, MosaicPopupWindow.MosaicChangeListener mosaicChangeListener) {
        int i = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_mosaic_popwin_offset_x);
        int i2 = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_mosaic_popwin_offset_y);
        boolean z = this.mLargeScreen;
        this.mPopupWindowManager.showMosicPopupWindow(!z ? this : view, mosaic, mosaicChangeListener, z, i, i2);
    }

    public final void showTextPopupWindow(View view, Text text, TextPopupWindow.TextChangeListener textChangeListener) {
        int i = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_text_popwin_offset_x);
        int i2 = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_text_popwin_offset_y);
        boolean z = this.mLargeScreen;
        this.mPopupWindowManager.showTextPopupWindow(!z ? this : view, text, textChangeListener, z, i, i2);
    }

    public final void showShapePopupWindow(View view, Shape shape, ShapePopupWindow.ShapeChangeListener shapeChangeListener) {
        int i = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_shape_popwin_offset_x);
        int i2 = -this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_shape_popwin_offset_y);
        boolean z = this.mLargeScreen;
        this.mPopupWindowManager.showShapePopupWindow(!z ? this : view, shape, shapeChangeListener, z, i, i2);
    }

    public void setSaveClickListener(View.OnClickListener onClickListener) {
        this.mSaveBtnListener = onClickListener;
    }

    public void setUndoClickListener(View.OnClickListener onClickListener) {
        this.mUnDoListener = onClickListener;
    }

    public void setRedoClickListener(View.OnClickListener onClickListener) {
        this.mReDoListener = onClickListener;
    }

    public void setUndoEnable(boolean z) {
        this.mUndoIv.setEnabled(z);
    }

    public void setRedoEnable(boolean z) {
        this.mRedoIv.setEnabled(z);
    }

    public final void onToolSelect(Tool tool) {
        ToolChangeListener toolChangeListener = this.mToolChangeListener;
        if (toolChangeListener != null) {
            toolChangeListener.onToolSelect(tool);
        }
    }

    public void setOnToolChangeListener(ToolChangeListener toolChangeListener) {
        this.mToolChangeListener = toolChangeListener;
        toolChangeListener.onToolSelect(this.mToolManager.getCurrentSelectTool());
    }
}
