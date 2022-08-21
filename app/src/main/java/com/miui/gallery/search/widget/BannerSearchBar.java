package com.miui.gallery.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.miui.gallery.R;
import com.miui.gallery.search.hint.SearchBarHintAdapter;
import com.miui.gallery.search.widget.bannerView.BannerView;
import com.miui.gallery.search.widget.bannerView.ILoopController;

/* loaded from: classes2.dex */
public class BannerSearchBar extends LinearLayout implements View.OnClickListener {
    public BannerView mBannerView;
    public OnHintClickListener mOnHintClickListener;

    /* loaded from: classes2.dex */
    public interface OnHintClickListener {
        void onHintClicked(BannerSearchBar bannerSearchBar, int i);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public BannerSearchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BannerSearchBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public void setOnHintClickListener(OnHintClickListener onHintClickListener) {
        this.mOnHintClickListener = onHintClickListener;
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.banner_search_bar_layout, this);
        this.mBannerView = (BannerView) findViewById(R.id.banner_view);
        setOnClickListener(this);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isClickable()) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        OnHintClickListener onHintClickListener = this.mOnHintClickListener;
        if (onHintClickListener != null) {
            onHintClickListener.onHintClicked(this, getLooperController().getCurrentPosition());
        }
    }

    public void setHintAdapter(SearchBarHintAdapter searchBarHintAdapter) {
        BannerView bannerView = this.mBannerView;
        if (bannerView == null) {
            throw new RuntimeException("No banner view found");
        }
        bannerView.setAdapter(searchBarHintAdapter);
    }

    public ILoopController getLooperController() {
        BannerView bannerView = this.mBannerView;
        if (bannerView != null) {
            return bannerView;
        }
        throw new RuntimeException("No looper controller found");
    }
}
