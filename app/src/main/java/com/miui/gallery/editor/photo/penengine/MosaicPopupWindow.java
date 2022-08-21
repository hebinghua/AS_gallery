package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.PenSeekBar;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.screen.core.ScreenProviderManager;
import com.miui.gallery.editor.photo.screen.mosaic.MosaicAdapter;
import com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider;
import com.miui.gallery.editor.photo.screen.stat.ScreenEditorStatUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MosaicPopupWindow extends ToolPopupWindow {
    public Context mContext;
    public Mosaic mMosaic;
    public MosaicAdapter mMosaicAdapter;
    public MosaicChangeListener mMosaicChangeListener;
    public OnItemClickListener mOnItemClickListener;
    public SimpleRecyclerViewNoSpring mRecyclerView;
    public View mRootView;
    public PenSeekBar mSeekBar;
    public PenSeekBar.OnProgressChangeListener mSizeChangeLister;

    /* loaded from: classes2.dex */
    public interface MosaicChangeListener {
        void onCurrentSelectIndexChange(Mosaic mosaic);

        void onSizeChange(int i);

        void onStartChangeProgress();

        void onStopChangeProgress(Mosaic mosaic, float f);
    }

    public static /* synthetic */ boolean $r8$lambda$pALx11MWVw57O4Cvc_P5gNvljVI(MosaicPopupWindow mosaicPopupWindow, RecyclerView recyclerView, View view, int i) {
        return mosaicPopupWindow.lambda$new$0(recyclerView, view, i);
    }

    public final float sizeToProgress(int i) {
        return (i - 35) / 126.0f;
    }

    public MosaicPopupWindow(Context context, Mosaic mosaic, MosaicChangeListener mosaicChangeListener) {
        super(context);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.penengine.MosaicPopupWindow$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                return MosaicPopupWindow.$r8$lambda$pALx11MWVw57O4Cvc_P5gNvljVI(MosaicPopupWindow.this, recyclerView, view, i);
            }
        };
        this.mSizeChangeLister = new PenSeekBar.OnProgressChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.MosaicPopupWindow.1
            {
                MosaicPopupWindow.this = this;
            }

            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onProgressChange(float f) {
                int progressToSize = MosaicPopupWindow.this.progressToSize(f);
                MosaicPopupWindow.this.mMosaic.setSize(progressToSize);
                if (MosaicPopupWindow.this.mMosaicChangeListener != null) {
                    MosaicPopupWindow.this.mMosaicChangeListener.onSizeChange(progressToSize);
                }
            }

            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onStartTrackingTouch() {
                MosaicPopupWindow.this.mMosaicChangeListener.onStartChangeProgress();
            }

            @Override // com.miui.gallery.editor.photo.penengine.PenSeekBar.OnProgressChangeListener
            public void onStopTrackingTouch(float f) {
                MosaicPopupWindow.this.mMosaicChangeListener.onStopChangeProgress(MosaicPopupWindow.this.mMosaic, f);
                MosaicPopupWindow.this.updateMosaicSeekBarLabel();
                MosaicPopupWindow.this.mSeekBar.announceForAccessibility(MosaicPopupWindow.this.mSeekBar.getContentDescription());
            }
        };
        this.mContext = context;
        this.mMosaic = mosaic;
        this.mMosaicChangeListener = mosaicChangeListener;
        init();
    }

    public final void init() {
        this.mRootView = View.inflate(this.mContext, R.layout.screen_mosaic_pop_window, null);
        int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R.dimen.screen_brush_popwin_width);
        int dimensionPixelOffset2 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.screen_brush_popwin_height);
        setContentView(this.mRootView);
        setWidth(dimensionPixelOffset);
        setHeight(dimensionPixelOffset2);
        initView();
    }

    public final void initView() {
        this.mRecyclerView = (SimpleRecyclerViewNoSpring) this.mRootView.findViewById(R.id.recycler_view);
        PenSeekBar penSeekBar = (PenSeekBar) this.mRootView.findViewById(R.id.mosaic_size_bar);
        this.mSeekBar = penSeekBar;
        penSeekBar.setOnProgressChangeListener(this.mSizeChangeLister);
        this.mRecyclerView = (SimpleRecyclerViewNoSpring) this.mRootView.findViewById(R.id.recycler_view);
        this.mSeekBar.setOnProgressChangeListener(this.mSizeChangeLister);
        ArrayList arrayList = new ArrayList(((ScreenMosaicProvider) ScreenProviderManager.INSTANCE.getProvider(ScreenMosaicProvider.class)).list());
        this.mMosaic.setMosaicDataList(arrayList);
        MosaicAdapter mosaicAdapter = new MosaicAdapter(this.mContext, arrayList, this.mMosaic.getCurrentSelectIndex());
        this.mMosaicAdapter = mosaicAdapter;
        this.mRecyclerView.setAdapter(mosaicAdapter);
        this.mMosaicAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mRecyclerView.addItemDecoration(new BlankDivider(0, 0, (int) this.mContext.getResources().getDimension(R.dimen.screen_mosaic_item_hInterval), 0, 0));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mRecyclerView);
    }

    public /* synthetic */ boolean lambda$new$0(RecyclerView recyclerView, View view, int i) {
        ScrollHelper.onItemClick(recyclerView, i);
        this.mMosaicAdapter.setSelection(i);
        this.mMosaic.setCurrentSelectIndex(i);
        MosaicChangeListener mosaicChangeListener = this.mMosaicChangeListener;
        if (mosaicChangeListener != null) {
            mosaicChangeListener.onCurrentSelectIndexChange(this.mMosaic);
        }
        ScreenEditorStatUtils.statMosaicMenuItemClick(i);
        return true;
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        setDataToView();
        super.showAtLocation(view, i, i2, i3);
    }

    public final void setDataToView() {
        MosaicAdapter mosaicAdapter = this.mMosaicAdapter;
        if (mosaicAdapter != null) {
            mosaicAdapter.setSelection(this.mMosaic.getCurrentSelectIndex());
        }
        this.mSeekBar.setProgress(sizeToProgress(this.mMosaic.getSize()));
        updateMosaicSeekBarLabel();
    }

    public final void updateMosaicSeekBarLabel() {
        this.mSeekBar.setContentDescription(sizeToLabel(this.mMosaic.getSize()));
    }

    public void setMosaic(Mosaic mosaic) {
        this.mMosaic = mosaic;
    }

    public final String sizeToLabel(int i) {
        String string = getContext().getString(R.string.screen_talkback_large);
        if (i < 60) {
            string = getContext().getString(R.string.screen_talkback_small);
        } else if (i < 85) {
            string = getContext().getString(R.string.screen_talkback_a_little_small);
        } else if (i < 110) {
            string = getContext().getString(R.string.screen_talkback_medium);
        } else if (i < 135) {
            string = getContext().getString(R.string.screen_talkback_a_little_large);
        } else if (i <= 161) {
            string = getContext().getString(R.string.screen_talkback_large);
        }
        return getContext().getString(R.string.screen_talkback_mosaic_size_x, string);
    }

    public final int progressToSize(float f) {
        return Math.round(f * 126.0f) + 35;
    }
}
