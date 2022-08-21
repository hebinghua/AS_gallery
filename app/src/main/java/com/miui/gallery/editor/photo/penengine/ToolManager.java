package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.entity.Eraser;
import com.miui.gallery.editor.photo.penengine.entity.Mark;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.penengine.entity.Pen;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.penengine.entity.Tool;

/* loaded from: classes2.dex */
public class ToolManager {
    public Context mContext;
    public Tool mCurrentSelectTool;
    public Eraser mEraser;
    public Mark mMark;
    public Mosaic mMosaic;
    public Pen mPen;
    public Shape mShape;
    public Text mText = new Text();

    public ToolManager(Context context) {
        this.mContext = context;
        this.mPen = new Pen(context.getResources().getIntArray(R.array.pen_default_size_item), this.mContext.getResources().getIntArray(R.array.pen_default_color_item));
        this.mMark = new Mark(this.mContext.getResources().getIntArray(R.array.mark_pen_default_size_item), this.mContext.getResources().getIntArray(R.array.mark_pen_default_color_item));
        this.mMosaic = new Mosaic(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_mosaic_default_size));
        this.mEraser = new Eraser(this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_eraser_default_size));
        this.mShape = new Shape(this.mContext.getResources().getIntArray(R.array.shape_default_color_item), 0);
        this.mCurrentSelectTool = this.mPen;
    }

    public Tool getCurrentSelectTool() {
        return this.mCurrentSelectTool;
    }

    public void setCurrentSelectTool(Tool tool) {
        this.mCurrentSelectTool = tool;
    }

    public Pen getPen() {
        return this.mPen;
    }

    public Mark getMark() {
        return this.mMark;
    }

    public Mosaic getMosaic() {
        return this.mMosaic;
    }

    public Eraser getEraser() {
        return this.mEraser;
    }

    public Text getText() {
        return this.mText;
    }

    public Shape getShape() {
        return this.mShape;
    }

    public boolean isMarkSelected() {
        return this.mCurrentSelectTool == this.mMark;
    }

    public boolean isMosaicSelected() {
        return this.mCurrentSelectTool == this.mMosaic;
    }

    public boolean isEraserSelected() {
        return this.mCurrentSelectTool == this.mEraser;
    }

    public boolean isTextSelected() {
        return this.mCurrentSelectTool == this.mText;
    }
}
