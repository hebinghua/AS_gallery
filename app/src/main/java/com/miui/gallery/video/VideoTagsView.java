package com.miui.gallery.video;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoTagsView extends View {
    public float mCurrentProgress;
    public boolean mIsRtl;
    public int mRadius;
    public Drawable mTagNormalDrawable;
    public Drawable mTagSelectedDrawable;
    public List<Float> mTags;
    public int mTotalLength;

    public VideoTagsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        boolean z = true;
        if (getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        this.mIsRtl = z;
        this.mRadius = getResources().getDimensionPixelSize(R.dimen.video_tags_radius);
        this.mTagNormalDrawable = getResources().getDrawable(R.drawable.video_tag_dot_normal);
        this.mTagSelectedDrawable = getResources().getDrawable(R.drawable.video_tag_dot_selected);
        int i = this.mRadius;
        Rect rect = new Rect(0, 0, i * 2, i * 2);
        this.mTagNormalDrawable.setBounds(rect);
        this.mTagSelectedDrawable.setBounds(rect);
    }

    public void setTags(List<Float> list) {
        this.mTags = list;
    }

    public void setCurrentProgress(float f) {
        this.mCurrentProgress = f;
        invalidate();
    }

    public void setTotalLength(int i) {
        this.mTotalLength = i;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!BaseMiscUtil.isValid(this.mTags) || this.mTotalLength <= 0) {
            return;
        }
        int width = getWidth();
        int i = width / 2;
        for (Float f : this.mTags) {
            int floatValue = (int) (i - ((this.mCurrentProgress - f.floatValue()) * this.mTotalLength));
            int i2 = this.mRadius;
            if (floatValue >= (-i2) && floatValue <= i2 + width) {
                int save = canvas.save();
                canvas.translate(this.mIsRtl ? (width - floatValue) - this.mRadius : floatValue - this.mRadius, 0.0f);
                if (Math.abs(i - floatValue) <= this.mRadius) {
                    this.mTagSelectedDrawable.draw(canvas);
                } else {
                    this.mTagNormalDrawable.draw(canvas);
                }
                canvas.restoreToCount(save);
            }
        }
    }
}
