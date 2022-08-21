package com.miui.gallery.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.baseui.R$dimen;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class StartEndSmoothScrollerController extends SmoothScrollerController {
    public float mMinDistance;

    public StartEndSmoothScrollerController(Context context) {
        super(context);
        init(context);
    }

    public final void init(Context context) {
        this.mMinDistance = context.getResources().getDimension(R$dimen.scroll_control_item_min_scroll_distance);
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 155.0f / displayMetrics.densityDpi;
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
    public void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
        int rightDecorationWidth;
        int bottomDecorationHeight;
        if (getLayoutManager() == null) {
            return;
        }
        int calculateDxToMakeVisible = calculateDxToMakeVisible(view, getHorizontalSnapPreference());
        int calculateDyToMakeVisible = calculateDyToMakeVisible(view, getVerticalSnapPreference());
        if (calculateDxToMakeVisible > 0) {
            rightDecorationWidth = calculateDxToMakeVisible - getLayoutManager().getLeftDecorationWidth(view);
        } else {
            rightDecorationWidth = calculateDxToMakeVisible + getLayoutManager().getRightDecorationWidth(view);
        }
        if (calculateDyToMakeVisible > 0) {
            bottomDecorationHeight = calculateDyToMakeVisible - getLayoutManager().getTopDecorationHeight(view);
        } else {
            bottomDecorationHeight = calculateDyToMakeVisible + getLayoutManager().getBottomDecorationHeight(view);
        }
        if (((int) Math.sqrt((rightDecorationWidth * rightDecorationWidth) + (bottomDecorationHeight * bottomDecorationHeight))) <= this.mMinDistance) {
            return;
        }
        action.update(-rightDecorationWidth, -bottomDecorationHeight, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME, new CubicEaseOutInterpolator());
    }
}
