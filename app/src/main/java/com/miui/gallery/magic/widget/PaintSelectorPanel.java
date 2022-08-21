package com.miui.gallery.magic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$color;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.widget.MagicBubbleSeekBar;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class PaintSelectorPanel extends LinearLayout {
    public static int PAINT_MAX_SIZE = 100;
    public static int[] colors = {R$color.paint1, R$color.paint2, R$color.paint3, R$color.paint4, R$color.paint5, R$color.paint6, R$color.paint7, R$color.paint8, R$color.paint9, R$color.paint10, R$color.paint11, R$color.paint12, R$color.paint13, R$color.paint14, R$color.paint15, R$color.paint16, R$color.paint17};
    public PaintColorListAdapter mAdapter;
    public ImageView mCloseImage;
    public RecyclerView mColorListView;
    public Context mContext;
    public ImageView mCurColorView;
    public OnPaintSelectorListener mOnPaintSelectorListener;
    public ImageView mSizeImage;
    public MagicBubbleSeekBar mSizeSeekBar;

    /* loaded from: classes2.dex */
    public interface OnPaintSelectorListener {
        void onPaintColorSelected(int i);

        void onPaintSizeSelected(int i);
    }

    public PaintSelectorPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R$layout.ts_magic_paint_panel_selector, this);
        this.mSizeImage = (ImageView) inflate.findViewById(R$id.magic_paint_size_image);
        this.mCloseImage = (ImageView) inflate.findViewById(R$id.magic_paint_color_close);
        this.mSizeSeekBar = (MagicBubbleSeekBar) inflate.findViewById(R$id.magic_paint_size_seek);
        final View findViewById = inflate.findViewById(R$id.magic_paint_seek_body);
        final View findViewById2 = inflate.findViewById(R$id.magic_paint_color);
        this.mSizeSeekBar.setProgressListener(new MagicBubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.magic.widget.PaintSelectorPanel.1
            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onProgressChanged(MagicBubbleSeekBar magicBubbleSeekBar, int i) {
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
                float currentProgress = ((magicBubbleSeekBar.getCurrentProgress() * 1.0f) + 3.0f) / 100.0f;
                if (PaintSelectorPanel.this.mOnPaintSelectorListener != null) {
                    PaintSelectorPanel.this.mOnPaintSelectorListener.onPaintSizeSelected((int) (PaintSelectorPanel.PAINT_MAX_SIZE * currentProgress));
                }
            }
        });
        this.mSizeImage.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.widget.PaintSelectorPanel.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                findViewById.setVisibility(8);
                findViewById2.setVisibility(0);
            }
        });
        this.mCloseImage.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.widget.PaintSelectorPanel.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                findViewById2.setVisibility(8);
                findViewById.setVisibility(0);
            }
        });
        this.mColorListView = (RecyclerView) inflate.findViewById(R$id.recycler_paint_color);
        this.mColorListView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        PaintColorListAdapter paintColorListAdapter = new PaintColorListAdapter(colors);
        this.mAdapter = paintColorListAdapter;
        this.mColorListView.setAdapter(paintColorListAdapter);
    }

    public void setOnPaintSelectorListener(OnPaintSelectorListener onPaintSelectorListener) {
        this.mOnPaintSelectorListener = onPaintSelectorListener;
    }

    public void setProgress(int i) {
        this.mSizeSeekBar.setCurrentProgress(i);
    }

    /* loaded from: classes2.dex */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ItemViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R$id.paint_color_view);
        }
    }

    /* loaded from: classes2.dex */
    public class PaintColorListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        public int[] mColors;
        public int mPosition = 0;

        public PaintColorListAdapter(int[] iArr) {
            this.mColors = iArr;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public ItemViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_paint_item_color, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final ItemViewHolder itemViewHolder, int i) {
            final int i2 = this.mColors[i];
            itemViewHolder.mImageView.setColorFilter(PaintSelectorPanel.this.mContext.getResources().getColor(i2));
            itemViewHolder.mImageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.widget.PaintSelectorPanel.PaintColorListAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (PaintSelectorPanel.this.mCurColorView != null) {
                        PaintSelectorPanel.this.mCurColorView.setSelected(false);
                    }
                    PaintSelectorPanel.this.mCurColorView = itemViewHolder.mImageView;
                    PaintSelectorPanel.this.mCurColorView.setSelected(true);
                    PaintSelectorPanel.this.mSizeImage.setColorFilter(PaintSelectorPanel.this.mContext.getResources().getColor(i2));
                    if (PaintSelectorPanel.this.mOnPaintSelectorListener != null) {
                        PaintSelectorPanel.this.mOnPaintSelectorListener.onPaintColorSelected(PaintSelectorPanel.this.mContext.getResources().getColor(i2));
                    }
                }
            });
            if (this.mPosition == i) {
                if (PaintSelectorPanel.this.mCurColorView != null) {
                    PaintSelectorPanel.this.mCurColorView.setSelected(false);
                }
                PaintSelectorPanel.this.mCurColorView = itemViewHolder.mImageView;
                PaintSelectorPanel.this.mCurColorView.setSelected(true);
                PaintSelectorPanel.this.mSizeImage.setColorFilter(PaintSelectorPanel.this.mContext.getResources().getColor(i2));
                if (PaintSelectorPanel.this.mOnPaintSelectorListener == null) {
                    return;
                }
                PaintSelectorPanel.this.mOnPaintSelectorListener.onPaintColorSelected(PaintSelectorPanel.this.mContext.getResources().getColor(i2));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mColors.length;
        }
    }
}
