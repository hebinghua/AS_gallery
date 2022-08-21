package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.ScalableTouchDelegate;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.GestureDetector;
import com.miui.gallery.widget.MirrorFunctionHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public abstract class ClickItemTouchListener {
    public final GestureDetectorHelper mGestureDetector;
    public final ItemClickGestureListener mItemClickGestureListener;

    /* loaded from: classes3.dex */
    public interface GestureDetectorHelper {
        boolean onTouchEvent(MotionEvent motionEvent);
    }

    public abstract boolean performItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2);

    public abstract boolean performItemLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z);

    public ClickItemTouchListener(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        ItemClickGestureListener itemClickGestureListener = new ItemClickGestureListener(recyclerView);
        this.mItemClickGestureListener = itemClickGestureListener;
        this.mGestureDetector = new ItemClickGestureDetector(context, itemClickGestureListener);
    }

    public static boolean isDetachedFromWindow(RecyclerView recyclerView) {
        return !recyclerView.isAttachedToWindow();
    }

    public final boolean hasAdapter(RecyclerView recyclerView) {
        return recyclerView.getAdapter() != null;
    }

    public boolean onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (isDetachedFromWindow(recyclerView) || !hasAdapter(recyclerView)) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0 && recyclerView.getScrollState() == 2) {
            return false;
        }
        if (recyclerView.getTag(R.id.ignore_target_view_action_event_disposable) != null && ((Boolean) recyclerView.getTag(R.id.ignore_target_view_action_event_disposable)).booleanValue()) {
            recyclerView.setTag(R.id.ignore_target_view_action_event_disposable, null);
            return false;
        }
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder != null) {
            TouchDelegate touchDelegate = findChildViewUnder.getTouchDelegate();
            if (touchDelegate instanceof ScalableTouchDelegate) {
                if (((ScalableTouchDelegate) touchDelegate).isDelegateTargeted(motionEvent.getX() - (findChildViewUnder.getLeft() + findChildViewUnder.getTranslationX()), motionEvent.getY() - (findChildViewUnder.getTop() + findChildViewUnder.getTranslationY()))) {
                    return false;
                }
            }
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public void setIsClickedItemRecyclable(boolean z) {
        this.mItemClickGestureListener.setIsClickedItemRecyclable(z);
    }

    public void setTakeOverUnhandledLongPress(boolean z) {
        this.mItemClickGestureListener.setTakeOverUnhandledLongPress(z);
    }

    /* loaded from: classes3.dex */
    public static class ItemClickGestureDetector implements GestureDetectorHelper {
        public GestureDetector mGestureDetectorCompat;
        public final ItemClickGestureListener mGestureListener;

        public ItemClickGestureDetector(Context context, ItemClickGestureListener itemClickGestureListener) {
            this.mGestureDetectorCompat = new GestureDetector(context, itemClickGestureListener, new Handler(Looper.getMainLooper()));
            this.mGestureListener = itemClickGestureListener;
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x002b, code lost:
            if (r3 != 3) goto L15;
         */
        @Override // com.miui.gallery.widget.recyclerview.ClickItemTouchListener.GestureDetectorHelper
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouchEvent(android.view.MotionEvent r5) {
            /*
                r4 = this;
                int r0 = r5.getSource()
                r1 = 3
                r2 = 1
                r3 = 8194(0x2002, float:1.1482E-41)
                if (r0 != r3) goto L1f
                int r0 = r5.getDeviceId()
                r3 = -1
                if (r0 != r3) goto L1f
                int r0 = r5.getActionButton()
                if (r0 == r2) goto L1f
                int r0 = r5.getActionMasked()
                if (r0 == r1) goto L1f
                r5 = 0
                return r5
            L1f:
                com.miui.gallery.view.GestureDetector r0 = r4.mGestureDetectorCompat
                boolean r0 = r0.onTouchEvent(r5)
                int r3 = r5.getActionMasked()
                if (r3 == r2) goto L2e
                if (r3 == r1) goto L33
                goto L38
            L2e:
                com.miui.gallery.widget.recyclerview.ClickItemTouchListener$ItemClickGestureListener r1 = r4.mGestureListener
                r1.dispatchSingleTapUpIfNeeded(r5)
            L33:
                com.miui.gallery.widget.recyclerview.ClickItemTouchListener$ItemClickGestureListener r1 = r4.mGestureListener
                r1.dispatchResetPressState(r5)
            L38:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.ClickItemTouchListener.ItemClickGestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
        }
    }

    /* loaded from: classes3.dex */
    public class ItemClickGestureListener extends GestureDetector.SimpleOnGestureListener {
        public final RecyclerView mHostView;
        public boolean mIsClickedItemRecyclable;
        public boolean mIsTapUpConsumed;
        public WeakReference<View> mPressedViewRef;
        public boolean mTakeOverUnhandledLongPress = true;

        public ItemClickGestureListener(RecyclerView recyclerView) {
            this.mHostView = recyclerView;
        }

        public void setTakeOverUnhandledLongPress(boolean z) {
            this.mTakeOverUnhandledLongPress = z;
        }

        public void dispatchSingleTapUpIfNeeded(MotionEvent motionEvent) {
            if (!this.mTakeOverUnhandledLongPress || this.mIsTapUpConsumed) {
                return;
            }
            onSingleTapUp(motionEvent);
        }

        public void dispatchResetPressState(MotionEvent motionEvent) {
            resetPressState();
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mIsTapUpConsumed = false;
            return this.mHostView.findChildViewUnder(motionEvent.getX(), motionEvent.getY()) != null;
        }

        @Override // com.miui.gallery.view.GestureDetector.SimpleOnGestureListener, com.miui.gallery.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
            resetPressState();
            View findChildViewUnder = this.mHostView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (findChildViewUnder != null) {
                findChildViewUnder.setPressed(true);
                if (this.mIsClickedItemRecyclable) {
                    this.mHostView.getChildViewHolder(findChildViewUnder).setIsRecyclable(false);
                }
                this.mPressedViewRef = new WeakReference<>(findChildViewUnder);
            }
        }

        @Override // com.miui.gallery.view.GestureDetector.SimpleOnGestureListener, com.miui.gallery.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            View findChildViewUnder = this.mHostView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (findChildViewUnder == null) {
                return false;
            }
            int childAdapterPosition = this.mHostView.getChildAdapterPosition(findChildViewUnder);
            if (childAdapterPosition == -1) {
                DefaultLogger.i("ClickItemTouchListener", "onSingleTapUp, NO_POSITION for pressed view");
                return false;
            }
            long itemId = this.mHostView.getAdapter().getItemId(childAdapterPosition);
            if (itemId == -1) {
                DefaultLogger.i("ClickItemTouchListener", "onSingleTapUp, NO_ID for pressed view");
                return false;
            }
            boolean performItemClick = ClickItemTouchListener.this.performItemClick(this.mHostView, findChildViewUnder, childAdapterPosition, itemId, motionEvent.getX() - (findChildViewUnder.getLeft() + findChildViewUnder.getTranslationX()), motionEvent.getY() - (findChildViewUnder.getTop() + findChildViewUnder.getTranslationY()));
            if (performItemClick) {
                this.mIsTapUpConsumed = true;
            }
            return performItemClick;
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!this.mIsTapUpConsumed) {
                this.mIsTapUpConsumed = true;
            }
            resetPressState();
            return false;
        }

        @Override // com.miui.gallery.view.GestureDetector.SimpleOnGestureListener, com.miui.gallery.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            View findChildViewUnder;
            if (!ClickItemTouchListener.isDetachedFromWindow(this.mHostView) && (findChildViewUnder = this.mHostView.findChildViewUnder(motionEvent.getX(), motionEvent.getY())) != null) {
                int childAdapterPosition = this.mHostView.getChildAdapterPosition(findChildViewUnder);
                if (childAdapterPosition == -1) {
                    DefaultLogger.i("ClickItemTouchListener", "onLongPress, NO_POSITION for pressed view");
                    return;
                }
                long itemId = this.mHostView.getAdapter().getItemId(childAdapterPosition);
                if (itemId == -1) {
                    DefaultLogger.i("ClickItemTouchListener", "onLongPress, NO_ID for pressed view");
                    return;
                }
                if (!ClickItemTouchListener.this.performItemLongClick(this.mHostView, findChildViewUnder, childAdapterPosition, itemId, motionEvent.getX() - (findChildViewUnder.getLeft() + findChildViewUnder.getTranslationX()), motionEvent.getY() - (findChildViewUnder.getTop() + findChildViewUnder.getTranslationY()), MirrorFunctionHelper.isEventFromMirror(motionEvent))) {
                    return;
                }
                this.mIsTapUpConsumed = true;
            }
        }

        public final void resetPressState() {
            WeakReference<View> weakReference = this.mPressedViewRef;
            if (weakReference != null) {
                View view = weakReference.get();
                if (view != null) {
                    view.setPressed(false);
                    if (this.mIsClickedItemRecyclable) {
                        this.mHostView.getChildViewHolder(view).setIsRecyclable(true);
                    }
                    this.mPressedViewRef.clear();
                }
                this.mPressedViewRef = null;
            }
        }

        public void setIsClickedItemRecyclable(boolean z) {
            this.mIsClickedItemRecyclable = z;
        }
    }
}
