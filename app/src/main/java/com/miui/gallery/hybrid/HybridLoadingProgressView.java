package com.miui.gallery.hybrid;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class HybridLoadingProgressView extends LinearLayout {
    public Button mButton;
    public int mErrorResId;
    public HybridOnRefreshListener mOnRefreshListener;
    public ProgressBar mProgressBar;
    public CharSequence mText;
    public int mTextResId;
    public TextView mTextView;

    /* loaded from: classes2.dex */
    public interface HybridOnRefreshListener {
        void onRefresh();
    }

    /* loaded from: classes2.dex */
    public enum HybridLoadingState {
        NETWORK_ERROR(R.string.hybrid_network_unavaliable),
        SERVICE_ERROR(R.string.hybrid_service_unavailiable),
        DATA_ERROR(R.string.hybrid_data_error),
        LOCATION_ERROR(R.string.hybrid_locating_fail),
        OK(17039370);
        
        private final int mDefaultDescription;

        HybridLoadingState(int i) {
            this.mDefaultDescription = i;
        }

        public int getDescription() {
            return this.mDefaultDescription;
        }
    }

    public HybridLoadingProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.hybrid_loading_progress, (ViewGroup) this, true);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress);
        this.mTextView = (TextView) findViewById(R.id.text);
        this.mButton = (Button) findViewById(R.id.button);
    }

    public final void updateStyle(boolean z) {
        if (z) {
            getLayoutParams().height = -2;
            setBackgroundResource(R.drawable.hybrid_loading_view_bg);
            return;
        }
        getLayoutParams().height = -1;
        setBackground(null);
    }

    public void onStartLoading(boolean z) {
        updateStyle(z);
        this.mProgressBar.setVisibility(0);
        this.mTextView.setVisibility(8);
        this.mButton.setVisibility(8);
        showView(this);
    }

    public void onStopLoading(boolean z) {
        updateStyle(z);
        if (z) {
            hideView(this);
            return;
        }
        showView(this);
        this.mProgressBar.setVisibility(8);
        if (this.mTextResId != 0) {
            this.mTextView.setVisibility(0);
            this.mTextView.setText(this.mTextResId);
        } else if (!TextUtils.isEmpty(this.mText)) {
            this.mTextView.setVisibility(0);
            this.mTextView.setText(this.mText);
        }
        this.mTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getContext().getResources().getDrawable(R.drawable.hybrid_loading_list_empty), (Drawable) null, (Drawable) null);
        this.mButton.setVisibility(8);
    }

    public void onInit(boolean z, boolean z2, HybridOnRefreshListener hybridOnRefreshListener) {
        this.mOnRefreshListener = hybridOnRefreshListener;
        updateStyle(z);
        if (z2) {
            setVisibility(0);
            this.mProgressBar.setVisibility(0);
            this.mTextView.setVisibility(8);
            this.mButton.setVisibility(8);
        } else if (z) {
            setVisibility(8);
        } else {
            setVisibility(0);
            this.mProgressBar.setVisibility(8);
            this.mButton.setVisibility(8);
        }
    }

    public final void showView(View view) {
        if (view != null && view.getVisibility() == 8) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.appear));
            view.setVisibility(0);
        }
    }

    public final void hideView(View view) {
        if (view != null && view.getVisibility() == 0) {
            if (view.isShown()) {
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.disappear));
            }
            view.setVisibility(8);
        }
    }

    public void setEmptyText(int i) {
        this.mTextResId = i;
    }

    public void setErrorText(int i) {
        this.mErrorResId = i;
    }

    public void onError(boolean z, final HybridLoadingState hybridLoadingState, String str) {
        if (this.mErrorResId > 0) {
            str = getContext().getResources().getString(this.mErrorResId);
        } else if (TextUtils.isEmpty(str)) {
            str = getContext().getResources().getString(hybridLoadingState.getDescription());
        }
        updateStyle(z);
        if (z) {
            hideView(this);
            ToastUtils.makeText(getContext(), str);
            return;
        }
        showView(this);
        this.mProgressBar.setVisibility(8);
        this.mTextView.setVisibility(0);
        this.mTextView.setText(str);
        this.mButton.setVisibility(0);
        if (hybridLoadingState == HybridLoadingState.NETWORK_ERROR) {
            this.mTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getContext().getResources().getDrawable(R.drawable.hybrid_loading_network_error), (Drawable) null, (Drawable) null);
            this.mButton.setVisibility(0);
            this.mButton.setText(R.string.hybrid_check_network);
        } else {
            this.mTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getContext().getResources().getDrawable(R.drawable.hybrid_loading_list_empty), (Drawable) null, (Drawable) null);
            this.mButton.setText(R.string.hybrid_try_again);
            if (this.mOnRefreshListener == null) {
                this.mButton.setVisibility(8);
            }
        }
        this.mButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.hybrid.HybridLoadingProgressView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (hybridLoadingState != HybridLoadingState.NETWORK_ERROR) {
                    if (HybridLoadingProgressView.this.mOnRefreshListener == null) {
                        return;
                    }
                    HybridLoadingProgressView.this.mOnRefreshListener.onRefresh();
                    return;
                }
                Intent intent = new Intent("android.settings.SETTINGS");
                intent.addFlags(268435456);
                HybridLoadingProgressView.this.getContext().startActivity(intent);
            }
        });
    }

    public void setIndeterminate(boolean z) {
        if (this.mProgressBar.isIndeterminate() == z) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mProgressBar.getLayoutParams();
        if (z) {
            layoutParams.width = -2;
            layoutParams.height = -2;
            layoutParams.gravity = 17;
        } else {
            layoutParams.width = -1;
            layoutParams.height = (int) TypedValue.applyDimension(1, 1.0f, getResources().getDisplayMetrics());
            layoutParams.gravity = 48;
        }
        this.mProgressBar.setLayoutParams(layoutParams);
        this.mProgressBar.setIndeterminate(z);
    }

    public void setProgress(int i) {
        this.mProgressBar.setProgress(i);
    }

    public int getProgress() {
        return this.mProgressBar.getProgress();
    }
}
