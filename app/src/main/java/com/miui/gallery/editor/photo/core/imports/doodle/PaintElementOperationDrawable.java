package com.miui.gallery.editor.photo.core.imports.doodle;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.miui.gallery.R;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class PaintElementOperationDrawable extends Drawable {
    public float mRotateCenterX;
    public float mRotateCenterY;
    public float mRotateDegree;
    public Drawable mWindow;
    public Drawable[] mDecorationDrawables = new Drawable[4];
    public Rect[] mDecorationRects = new Rect[4];
    public RectF mRectFTemp = new RectF();
    public RectF mBound = new RectF();
    public float[] mPoint = new float[2];
    public Matrix mMatrix = new Matrix();
    public Matrix mRotateMatrix = new Matrix();
    public Matrix mCurrentMatrix = new Matrix();
    public boolean mDrawDecoration = true;
    public HashMap<Action, Integer> mActionPosition = new HashMap<>();

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public PaintElementOperationDrawable(Resources resources) {
        this.mWindow = resources.getDrawable(R.drawable.common_editor_window_n_new);
        int i = 0;
        while (true) {
            Rect[] rectArr = this.mDecorationRects;
            if (i < rectArr.length) {
                rectArr[i] = new Rect();
                i++;
            } else {
                return;
            }
        }
    }

    public void configDecorationPositon(RectF rectF, Matrix matrix, float f, float f2, float f3) {
        this.mBound.set(rectF);
        this.mMatrix.reset();
        this.mMatrix.postConcat(matrix);
        this.mRotateMatrix.reset();
        this.mRotateMatrix.postRotate(f, f2, f3);
        this.mRotateDegree = f;
        this.mRotateCenterX = f2;
        this.mRotateCenterY = f3;
    }

    public void configActionPosition(Action action, Action action2, Action action3, Action action4, Resources resources) {
        this.mActionPosition.clear();
        if (action != null) {
            this.mActionPosition.put(action, 0);
            this.mDecorationDrawables[0] = resources.getDrawable(action.icon);
        }
        if (action2 != null) {
            this.mActionPosition.put(action2, 1);
            this.mDecorationDrawables[1] = resources.getDrawable(action2.icon);
        }
        if (action3 != null) {
            this.mActionPosition.put(action3, 2);
            this.mDecorationDrawables[2] = resources.getDrawable(action3.icon);
        }
        if (action4 != null) {
            this.mActionPosition.put(action4, 3);
            this.mDecorationDrawables[3] = resources.getDrawable(action4.icon);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mRectFTemp.set(this.mBound);
        float[] fArr = this.mPoint;
        fArr[0] = this.mRotateCenterX;
        fArr[1] = this.mRotateCenterY;
        this.mMatrix.mapRect(this.mRectFTemp);
        this.mMatrix.mapPoints(this.mPoint);
        Drawable drawable = this.mWindow;
        RectF rectF = this.mRectFTemp;
        drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        canvas.save();
        float f = this.mRotateDegree;
        float[] fArr2 = this.mPoint;
        canvas.rotate(f, fArr2[0], fArr2[1]);
        this.mWindow.draw(canvas);
        canvas.restore();
        for (Map.Entry<Action, Integer> entry : this.mActionPosition.entrySet()) {
            int intValue = entry.getValue().intValue();
            if (intValue == 0) {
                float[] fArr3 = this.mPoint;
                RectF rectF2 = this.mBound;
                fArr3[0] = rectF2.left;
                fArr3[1] = rectF2.top;
            } else if (intValue == 1) {
                float[] fArr4 = this.mPoint;
                RectF rectF3 = this.mBound;
                fArr4[0] = rectF3.right;
                fArr4[1] = rectF3.top;
            } else if (intValue == 2) {
                float[] fArr5 = this.mPoint;
                RectF rectF4 = this.mBound;
                fArr5[0] = rectF4.right;
                fArr5[1] = rectF4.bottom;
            } else if (intValue == 3) {
                float[] fArr6 = this.mPoint;
                RectF rectF5 = this.mBound;
                fArr6[0] = rectF5.left;
                fArr6[1] = rectF5.bottom;
            }
            this.mCurrentMatrix.reset();
            this.mCurrentMatrix.postConcat(this.mMatrix);
            this.mCurrentMatrix.preConcat(this.mRotateMatrix);
            this.mCurrentMatrix.mapPoints(this.mPoint);
            Drawable drawable2 = this.mDecorationDrawables[intValue];
            float intrinsicWidth = drawable2.getIntrinsicWidth() / 2;
            configRectByPointAndSize(this.mDecorationRects[intValue], this.mPoint, intrinsicWidth, intrinsicWidth);
            drawable2.setBounds(this.mDecorationRects[intValue]);
            if (this.mDrawDecoration) {
                drawable2.draw(canvas);
            }
        }
    }

    public static void configRectByPointAndSize(Rect rect, float[] fArr, float f, float f2) {
        if (fArr == null || fArr.length != 2) {
            return;
        }
        rect.set(Math.round(fArr[0] - f), Math.round(fArr[1] - f2), Math.round(fArr[0] + f), Math.round(fArr[1] + f2));
    }

    public void getDecorationRect(Action action, RectF rectF) {
        Integer num = this.mActionPosition.get(action);
        if (num == null) {
            rectF.setEmpty();
        } else {
            rectF.set(this.mDecorationRects[num.intValue()]);
        }
    }

    public float findLowerDecorationPosition() {
        float f = 0.0f;
        if (!this.mActionPosition.isEmpty()) {
            for (Map.Entry<Action, Integer> entry : this.mActionPosition.entrySet()) {
                float f2 = this.mDecorationRects[entry.getValue().intValue()].bottom;
                if (f2 > f) {
                    f = f2;
                }
            }
        }
        return f;
    }

    public void setDrawDecoration(boolean z) {
        this.mDrawDecoration = z;
    }

    /* loaded from: classes2.dex */
    public enum Action {
        EDIT(R.drawable.common_editor_window_action_btn_edit),
        ROTATE(R.drawable.common_editor_window_action_btn_rotate_n),
        SCALE(R.drawable.common_editor_window_action_btn_scale),
        DELETE(R.drawable.common_editor_window_action_btn_delete),
        MIRROR(R.drawable.common_editor_window_action_btn_mirror),
        REVERSE_WHITE(R.drawable.common_editor_window_action_btn_reverse_white),
        REVERSE_BLACK(R.drawable.common_editor_window_action_btn_reverse_black),
        DATE(R.drawable.common_editor_window_action_btn_date),
        ADD(R.drawable.common_editor_window_action_btn_add),
        FONT(R.drawable.common_editor_window_action_btn_font),
        STYLE(R.drawable.common_editor_window_action_btn_style);
        
        public final int icon;

        Action(int i) {
            this.icon = i;
        }
    }
}
