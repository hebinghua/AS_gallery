package com.miui.gallery.movie.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;

/* loaded from: classes2.dex */
public class MoviePreviewMenuTitle extends LinearLayout implements View.OnClickListener {
    public IMenuTitleClickListener mListener;
    public TextView mLongVideoTitleView;
    public TextView mShortVideoTitleView;

    /* loaded from: classes2.dex */
    public interface IMenuTitleClickListener {
        boolean onLongVideoTitleViewClick();

        boolean onShortVideoTitleViewClick();
    }

    public MoviePreviewMenuTitle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        TextView textView = (TextView) findViewById(R$id.tv_short_video_title);
        this.mShortVideoTitleView = textView;
        textView.setText(getResources().getString(R$string.movie_video_short, 10));
        this.mLongVideoTitleView = (TextView) findViewById(R$id.tv_long_video_title);
        this.mShortVideoTitleView.setOnClickListener(this);
        this.mLongVideoTitleView.setOnClickListener(this);
    }

    public final void init(Context context) {
        LinearLayout.inflate(context, R$layout.movie_fragment_preview_title, this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mListener == null) {
            return;
        }
        int id = view.getId();
        if (id == R$id.tv_short_video_title) {
            if (this.mShortVideoTitleView.isSelected() || !this.mListener.onShortVideoTitleViewClick()) {
                return;
            }
            updateTitleViewState(true);
        } else if (id != R$id.tv_long_video_title || this.mLongVideoTitleView.isSelected() || !this.mListener.onLongVideoTitleViewClick()) {
        } else {
            updateTitleViewState(false);
        }
    }

    public void setListener(IMenuTitleClickListener iMenuTitleClickListener) {
        this.mListener = iMenuTitleClickListener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public void updateTitleViewState(boolean z) {
        this.mLongVideoTitleView.setSelected(!z);
        this.mShortVideoTitleView.setSelected(z);
    }
}
