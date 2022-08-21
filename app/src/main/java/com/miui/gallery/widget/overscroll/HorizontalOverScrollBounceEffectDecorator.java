package com.miui.gallery.widget.overscroll;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase;

/* loaded from: classes2.dex */
public class HorizontalOverScrollBounceEffectDecorator extends OverScrollBounceEffectDecoratorBase {
    public static void setOverScrollEffect(RecyclerView recyclerView) {
        new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
    }

    public static void setOverScrollEffect(RecyclerView recyclerView, ItemTouchHelper.Callback callback) {
        new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView, callback));
    }

    public static void setOverScrollEffect(HorizontalScrollView horizontalScrollView) {
        new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(horizontalScrollView));
    }

    /* loaded from: classes2.dex */
    public static class MotionAttributesHorizontal extends OverScrollBounceEffectDecoratorBase.MotionAttributes {
        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.MotionAttributes
        public boolean init(View view, MotionEvent motionEvent) {
            boolean z = false;
            if (motionEvent.getHistorySize() == 0) {
                return false;
            }
            float y = motionEvent.getY(0) - motionEvent.getHistoricalY(0, 0);
            float x = motionEvent.getX(0) - motionEvent.getHistoricalX(0, 0);
            if (Math.abs(x) < Math.abs(y)) {
                return false;
            }
            this.mAbsOffset = view.getTranslationX();
            this.mDeltaOffset = x;
            if (x > 0.0f) {
                z = true;
            }
            this.mDir = z;
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static class AnimationAttributesHorizontal extends OverScrollBounceEffectDecoratorBase.AnimationAttributes {
        public AnimationAttributesHorizontal() {
            this.mProperty = View.TRANSLATION_X;
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.AnimationAttributes
        public void init(View view) {
            this.mAbsOffset = view.getTranslationX();
            this.mMaxOffset = view.getWidth();
        }
    }

    public HorizontalOverScrollBounceEffectDecorator(IOverScrollInterface$IOverScrollDecoratorAdapter iOverScrollInterface$IOverScrollDecoratorAdapter) {
        this(iOverScrollInterface$IOverScrollDecoratorAdapter, 3.0f, 1.0f, -2.0f);
    }

    public HorizontalOverScrollBounceEffectDecorator(IOverScrollInterface$IOverScrollDecoratorAdapter iOverScrollInterface$IOverScrollDecoratorAdapter, float f, float f2, float f3) {
        super(iOverScrollInterface$IOverScrollDecoratorAdapter, f3, f, f2);
    }

    @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase
    public OverScrollBounceEffectDecoratorBase.MotionAttributes createMotionAttributes() {
        return new MotionAttributesHorizontal();
    }

    @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase
    public OverScrollBounceEffectDecoratorBase.AnimationAttributes createAnimationAttributes() {
        return new AnimationAttributesHorizontal();
    }

    @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase
    public void translateView(View view, float f) {
        view.setTranslationX(f);
    }

    @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase
    public void translateViewAndEvent(View view, float f, MotionEvent motionEvent) {
        view.setTranslationX(f);
        motionEvent.offsetLocation(f - motionEvent.getX(0), 0.0f);
    }
}
