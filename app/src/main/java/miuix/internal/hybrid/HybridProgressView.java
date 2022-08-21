package miuix.internal.hybrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import miuix.hybrid.R;

/* loaded from: classes3.dex */
public class HybridProgressView extends ImageView {
    public Rect mBounds;
    public Context mContext;
    public int mCurrentProgress;
    public Handler mHandler;
    public int mIncrement;
    public Rect mReverseBounds;
    public Drawable mReverseDrawable;
    public int mTargetProgress;

    public int getMax() {
        return 100;
    }

    public HybridProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        this.mBounds = new Rect(0, 0, 0, 0);
        this.mCurrentProgress = 0;
        this.mTargetProgress = 0;
        this.mReverseDrawable = this.mContext.getResources().getDrawable(R.drawable.hybrid_progress_reverse);
        this.mHandler = new Handler() { // from class: miuix.internal.hybrid.HybridProgressView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 42) {
                    if (HybridProgressView.this.mCurrentProgress >= HybridProgressView.this.mTargetProgress) {
                        if (HybridProgressView.this.mCurrentProgress > 9500 || HybridProgressView.this.mCurrentProgress < 800) {
                            return;
                        }
                        HybridProgressView.this.mCurrentProgress += 30;
                        HybridProgressView.this.mBounds.right = (HybridProgressView.this.getWidth() * HybridProgressView.this.mCurrentProgress) / 10000;
                        HybridProgressView.this.invalidate();
                        sendMessageDelayed(HybridProgressView.this.mHandler.obtainMessage(42), 40L);
                        return;
                    }
                    HybridProgressView hybridProgressView = HybridProgressView.this;
                    hybridProgressView.mCurrentProgress = Math.min(hybridProgressView.mTargetProgress, HybridProgressView.this.mCurrentProgress + HybridProgressView.this.mIncrement);
                    HybridProgressView.this.mBounds.right = (HybridProgressView.this.getWidth() * HybridProgressView.this.mCurrentProgress) / 10000;
                    HybridProgressView.this.invalidate();
                    sendMessageDelayed(HybridProgressView.this.mHandler.obtainMessage(42), 40L);
                }
            }
        };
        this.mReverseBounds = new Rect(0, 0, 0, 0);
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Rect rect = this.mBounds;
        rect.left = 0;
        rect.right = ((i3 - i) * this.mCurrentProgress) / 10000;
        rect.top = 0;
        rect.bottom = i4 - i2;
    }

    public void setProgress(int i) {
        int i2 = i * 100;
        int i3 = this.mTargetProgress;
        if (i3 <= 800) {
            this.mCurrentProgress = i3;
        }
        this.mTargetProgress = i2;
        this.mIncrement = (i2 - this.mCurrentProgress) / 10;
        this.mHandler.removeMessages(42);
        this.mHandler.sendEmptyMessage(42);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        drawable.setBounds(this.mBounds);
        drawable.draw(canvas);
        float width = getWidth() - ((getWidth() * this.mCurrentProgress) / 10000.0f);
        canvas.save();
        canvas.translate(-width, 0.0f);
        this.mReverseBounds.set(0, 0, getWidth(), getHeight());
        this.mReverseDrawable.setBounds(this.mReverseBounds);
        this.mReverseDrawable.draw(canvas);
        canvas.translate(width, 0.0f);
        canvas.restore();
    }
}
