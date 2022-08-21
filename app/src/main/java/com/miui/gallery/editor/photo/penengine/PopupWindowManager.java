package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import com.miui.gallery.editor.photo.penengine.BrushPopupWindow;
import com.miui.gallery.editor.photo.penengine.ColorPopupWindow;
import com.miui.gallery.editor.photo.penengine.MosaicPopupWindow;
import com.miui.gallery.editor.photo.penengine.ShapePopupWindow;
import com.miui.gallery.editor.photo.penengine.TextPopupWindow;
import com.miui.gallery.editor.photo.penengine.entity.CommonBrush;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow;

/* loaded from: classes2.dex */
public class PopupWindowManager {
    public BrushPopupWindow mBrushPopupWindow;
    public ColorPopupWindow mColorPopupWindow;
    public Context mContext;
    public PaintSizePopupWindow mMosaicPaintPopupWindow;
    public MosaicPopupWindow mMosaicPopupWindow;
    public ShapePopupWindow mShapePopupWindow;
    public TextPopupWindow mTextPopupWindow;

    public PopupWindowManager(Context context) {
        this.mContext = context;
    }

    public void showColorPickPopupWindow(View view, int i, ColorPopupWindow.OnColorChangeListener onColorChangeListener, boolean z, int i2, int i3) {
        if (this.mColorPopupWindow == null) {
            this.mColorPopupWindow = new ColorPopupWindow.Builder(this.mContext).create();
        }
        this.mColorPopupWindow.setColor(i);
        this.mColorPopupWindow.setPenColorChangeListener(onColorChangeListener);
        this.mColorPopupWindow.setArrowVisible(z ? 0 : 8);
        this.mColorPopupWindow.show(view, i2, i3);
    }

    public void showBrushPopupWindow(View view, CommonBrush commonBrush, BrushPopupWindow.BrushChangeListener brushChangeListener, boolean z, int i, int i2) {
        BrushPopupWindow brushPopupWindow = this.mBrushPopupWindow;
        if (brushPopupWindow == null) {
            this.mBrushPopupWindow = new BrushPopupWindow(this.mContext, commonBrush, brushChangeListener);
        } else {
            brushPopupWindow.setBrush(commonBrush);
        }
        this.mBrushPopupWindow.setArrowVisible(z ? 0 : 8);
        this.mBrushPopupWindow.show(view, i, i2);
    }

    public void showMosicPopupWindow(View view, Mosaic mosaic, MosaicPopupWindow.MosaicChangeListener mosaicChangeListener, boolean z, int i, int i2) {
        MosaicPopupWindow mosaicPopupWindow = this.mMosaicPopupWindow;
        if (mosaicPopupWindow == null) {
            this.mMosaicPopupWindow = new MosaicPopupWindow(this.mContext, mosaic, mosaicChangeListener);
        } else {
            mosaicPopupWindow.setMosaic(mosaic);
        }
        this.mMosaicPopupWindow.setArrowVisible(z ? 0 : 8);
        this.mMosaicPopupWindow.show(view, i, i2);
    }

    public void setMosaicPaintPopupWindowPaintSize(int i) {
        PaintSizePopupWindow paintSizePopupWindow = this.mMosaicPaintPopupWindow;
        if (paintSizePopupWindow != null) {
            paintSizePopupWindow.setPaintSize(i);
        }
    }

    public void dismissMosaicPaintPopupWindow() {
        PaintSizePopupWindow paintSizePopupWindow = this.mMosaicPaintPopupWindow;
        if (paintSizePopupWindow != null) {
            paintSizePopupWindow.dismiss();
        }
    }

    public void showMosaicPaintPopupWindow(View view) {
        if (this.mMosaicPaintPopupWindow == null) {
            this.mMosaicPaintPopupWindow = new PaintSizePopupWindow(this.mContext);
        }
        this.mMosaicPaintPopupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showTextPopupWindow(View view, Text text, TextPopupWindow.TextChangeListener textChangeListener, boolean z, int i, int i2) {
        TextPopupWindow textPopupWindow = this.mTextPopupWindow;
        if (textPopupWindow == null) {
            this.mTextPopupWindow = new TextPopupWindow(this.mContext, text, textChangeListener);
        } else {
            textPopupWindow.setText(text);
        }
        this.mTextPopupWindow.setArrowVisible(z ? 0 : 8);
        this.mTextPopupWindow.show(view, i, i2);
    }

    public void showShapePopupWindow(View view, Shape shape, ShapePopupWindow.ShapeChangeListener shapeChangeListener, boolean z, int i, int i2) {
        ShapePopupWindow shapePopupWindow = this.mShapePopupWindow;
        if (shapePopupWindow == null) {
            this.mShapePopupWindow = new ShapePopupWindow(this.mContext, shape, shapeChangeListener);
        } else {
            shapePopupWindow.setShape(shape);
        }
        this.mShapePopupWindow.setArrowVisible(z ? 0 : 8);
        this.mShapePopupWindow.show(view, i, i2);
    }

    public final void dismissPopupWindowIfShowing(PopupWindow popupWindow) {
        if (popupWindow == null || !popupWindow.isShowing()) {
            return;
        }
        popupWindow.dismiss();
    }

    public void dismissAllShowingPopupWindows() {
        dismissPopupWindowIfShowing(this.mBrushPopupWindow);
        dismissPopupWindowIfShowing(this.mMosaicPopupWindow);
        dismissPopupWindowIfShowing(this.mTextPopupWindow);
        dismissPopupWindowIfShowing(this.mShapePopupWindow);
        dismissPopupWindowIfShowing(this.mMosaicPaintPopupWindow);
    }

    public void resetAllPopupWindows() {
        this.mBrushPopupWindow = null;
        this.mMosaicPopupWindow = null;
        this.mTextPopupWindow = null;
        this.mShapePopupWindow = null;
        this.mMosaicPaintPopupWindow = null;
    }
}
