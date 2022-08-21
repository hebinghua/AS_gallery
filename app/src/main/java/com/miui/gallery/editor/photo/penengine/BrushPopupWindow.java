package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.PenSeekBar;
import com.miui.gallery.editor.photo.penengine.entity.CommonBrush;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.List;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class BrushPopupWindow extends ToolPopupWindow {
    public TextView mAlphaTv;
    public CommonBrush mBrush;
    public BrushChangeListener mBrushChangeListener;
    public PenSeekBar.OnProgressChangeListener mPenAlphaChangeLister;
    public PenSeekBar mPenSeekBar;
    public View mRootView;
    public View.OnClickListener mStrokeViewListener;
    public List<ConstraintLayout> mStrokeWidthItems;

    /* loaded from: classes2.dex */
    public interface BrushChangeListener {
        void onBrushAlphaChange(CommonBrush commonBrush);

        void onBrushSizeChange(CommonBrush commonBrush);
    }

    public static /* synthetic */ void $r8$lambda$UOzXLAjZ3zehIxvNGZ7epGoYZUQ(BrushPopupWindow brushPopupWindow, View view) {
        brushPopupWindow.lambda$new$0(view);
    }

    public BrushPopupWindow(Context context, CommonBrush commonBrush, BrushChangeListener brushChangeListener) {
        super(context);
        this.mStrokeViewListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.penengine.BrushPopupWindow$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BrushPopupWindow.$r8$lambda$UOzXLAjZ3zehIxvNGZ7epGoYZUQ(BrushPopupWindow.this, view);
            }
        };
        this.mPenAlphaChangeLister = new PenSeekBar.OnProgressChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.BrushPopupWindow.1
            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onStartTrackingTouch() {
            }

            {
                BrushPopupWindow.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onProgressChange(float f) {
                DefaultLogger.d("BrushPopupWindow_", "onProgessChange: " + f);
                BrushPopupWindow.this.updateAlphaTv(f);
                BrushPopupWindow.this.mBrush.setAlpha(f);
                BrushPopupWindow brushPopupWindow = BrushPopupWindow.this;
                brushPopupWindow.onBrushAlphaChange(brushPopupWindow.mBrush);
            }

            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onStopTrackingTouch(float f) {
                BrushPopupWindow.this.updateSeekBarLabel();
                BrushPopupWindow.this.mPenSeekBar.announceForAccessibility(BrushPopupWindow.this.mPenSeekBar.getContentDescription());
            }
        };
        this.mBrush = commonBrush;
        this.mBrushChangeListener = brushChangeListener;
        init();
    }

    public final void init() {
        this.mRootView = View.inflate(getContext(), R.layout.brush_config_layout, null);
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.screen_brush_popwin_width);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(R.dimen.screen_brush_popwin_height);
        setContentView(this.mRootView);
        setWidth(dimensionPixelOffset);
        setHeight(dimensionPixelOffset2);
        initView();
    }

    public final void updateAlphaTv(float f) {
        this.mAlphaTv.setText(convertFloatToPercent(f));
    }

    public final String convertFloatToPercent(float f) {
        return ((int) (f * 100.0f)) + "%";
    }

    public final void onBrushSelectSizeIndexChange(CommonBrush commonBrush) {
        DefaultLogger.d("BrushPopupWindow_", "onBrushSelectSizeIndexChange: ");
        BrushChangeListener brushChangeListener = this.mBrushChangeListener;
        if (brushChangeListener != null) {
            brushChangeListener.onBrushSizeChange(commonBrush);
        }
    }

    public final void onBrushAlphaChange(CommonBrush commonBrush) {
        BrushChangeListener brushChangeListener = this.mBrushChangeListener;
        if (brushChangeListener != null) {
            brushChangeListener.onBrushAlphaChange(commonBrush);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        for (int i = 0; i < this.mStrokeWidthItems.size(); i++) {
            ConstraintLayout constraintLayout = this.mStrokeWidthItems.get(i);
            if (constraintLayout == view) {
                selectStrokeItem(constraintLayout, true);
                this.mBrush.setSelectSizeIndex(i);
                onBrushSelectSizeIndexChange(this.mBrush);
            } else {
                unSelectStrokeItem(constraintLayout, false);
            }
        }
    }

    public final void selectStrokeItem(View view, boolean z) {
        View findViewById = view.findViewById(R.id.brush_stroke_item_select_bg);
        if (z) {
            findViewById.animate().scaleX(1.5f).scaleY(1.5f).setInterpolator(new EaseManager.SpringInterpolator()).setDuration(200L).start();
        } else {
            findViewById.setScaleX(1.5f);
            findViewById.setScaleY(1.5f);
        }
        view.findViewById(R.id.brush_stroke_item_content).setBackgroundColor(this.mBrush.getColor());
    }

    public final void unSelectStrokeItem(View view, boolean z) {
        View findViewById = view.findViewById(R.id.brush_stroke_item_select_bg);
        if (z) {
            findViewById.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new EaseManager.SpringInterpolator()).setDuration(200L).start();
        } else {
            findViewById.setScaleX(1.0f);
            findViewById.setScaleY(1.0f);
        }
        view.findViewById(R.id.brush_stroke_item_content).setBackgroundColor(getContext().getColor(R.color.brush_config_size_item_unselect_color));
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        setDataToView();
        super.showAtLocation(view, i, i2, i3);
    }

    public void setBrush(CommonBrush commonBrush) {
        this.mBrush = commonBrush;
    }

    public final void setDataToView() {
        for (int i = 0; i < this.mStrokeWidthItems.size(); i++) {
            ConstraintLayout constraintLayout = this.mStrokeWidthItems.get(i);
            if (this.mBrush.getSelectSizeIndex() == i) {
                selectStrokeItem(constraintLayout, false);
            } else {
                unSelectStrokeItem(constraintLayout, false);
            }
        }
        this.mPenSeekBar.setColor(this.mBrush.getColor());
        this.mPenSeekBar.setProgress(this.mBrush.getAlpha());
        updateSeekBarLabel();
        updateAlphaTv(this.mBrush.getAlpha());
    }

    public final void updateSeekBarLabel() {
        this.mPenSeekBar.setContentDescription(alphaToLabel(this.mBrush.getAlpha()));
    }

    public final String alphaToLabel(float f) {
        return getContext().getString(R.string.screen_talkback_alpha_x, convertFloatToPercent(f));
    }

    public final void initView() {
        List<ConstraintLayout> asList = Arrays.asList((ConstraintLayout) this.mRootView.findViewById(R.id.item1), (ConstraintLayout) this.mRootView.findViewById(R.id.item2), (ConstraintLayout) this.mRootView.findViewById(R.id.item3), (ConstraintLayout) this.mRootView.findViewById(R.id.item4), (ConstraintLayout) this.mRootView.findViewById(R.id.item5));
        this.mStrokeWidthItems = asList;
        int size = asList.size();
        for (int i = 0; i < size; i++) {
            ConstraintLayout constraintLayout = this.mStrokeWidthItems.get(i);
            View findViewById = constraintLayout.findViewById(R.id.brush_stroke_item_content);
            findViewById.setClipToOutline(true);
            findViewById.setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.editor.photo.penengine.BrushPopupWindow.2
                {
                    BrushPopupWindow.this = this;
                }

                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(new Rect(0, 0, view.getWidth(), view.getHeight()));
                }
            });
            findViewById.setBackgroundColor(getContext().getColor(R.color.brush_config_size_item_unselect_color));
            constraintLayout.setOnClickListener(this.mStrokeViewListener);
        }
        PenSeekBar penSeekBar = (PenSeekBar) this.mRootView.findViewById(R.id.pen_seek_bar);
        this.mPenSeekBar = penSeekBar;
        penSeekBar.setOnProgressChangeListener(this.mPenAlphaChangeLister);
        this.mAlphaTv = (TextView) this.mRootView.findViewById(R.id.alpha_tv);
        updateAlphaTv(this.mBrush.getAlpha());
    }
}
