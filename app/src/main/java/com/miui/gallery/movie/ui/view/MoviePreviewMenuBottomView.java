package com.miui.gallery.movie.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MoviePreviewMenuBottomView extends LinearLayout implements View.OnClickListener {
    public TextView mEditView;
    public IMenuBottomViewClickListener mListener;
    public View mPlayArea;
    public ImageView mPlayView;
    public View mPreViewBottomView;
    public TextView mSaveView;

    /* loaded from: classes2.dex */
    public interface IMenuBottomViewClickListener {
        void onEditBtnClick();

        void onPlayAreaClick();

        void onSaveBtnClick();
    }

    public MoviePreviewMenuBottomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPreViewBottomView = findViewById(R$id.ll_movie_preview_bottom_area);
        this.mPlayArea = findViewById(R$id.fl_preview_play_area);
        this.mSaveView = (TextView) findViewById(R$id.tv_movie_save);
        this.mEditView = (TextView) findViewById(R$id.tv_movie_editor);
        this.mPlayView = (ImageView) findViewById(R$id.iv_movie_play);
        this.mPreViewBottomView.setOnClickListener(this);
        this.mSaveView.setOnClickListener(this);
        this.mEditView.setOnClickListener(this);
        this.mPlayArea.setOnClickListener(this);
    }

    public final void init(Context context) {
        LinearLayout.inflate(context, R$layout.movie_fragment_preview_bottom, this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mListener == null) {
            DefaultLogger.d("MoviePreviewMenuBottomView", "the listener is null . ");
            return;
        }
        int id = view.getId();
        if (id == R$id.tv_movie_save) {
            this.mListener.onSaveBtnClick();
        } else if (id == R$id.tv_movie_editor) {
            this.mListener.onEditBtnClick();
        } else if (id != R$id.fl_preview_play_area) {
        } else {
            this.mListener.onPlayAreaClick();
        }
    }

    public void updatePlayBtnState(boolean z) {
        Resources resources;
        int i;
        this.mPlayView.setSelected(z);
        ImageView imageView = this.mPlayView;
        if (z) {
            resources = getResources();
            i = R$string.movie_content_describe_pause;
        } else {
            resources = getResources();
            i = R$string.movie_content_describe_play;
        }
        imageView.setContentDescription(resources.getString(i));
    }

    public void setIMenuBottomViewClickListener(IMenuBottomViewClickListener iMenuBottomViewClickListener) {
        this.mListener = iMenuBottomViewClickListener;
    }

    public void removeListener() {
        this.mListener = null;
    }
}
