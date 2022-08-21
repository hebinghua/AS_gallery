package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.video.editor.adapter.AdjustAdapter;
import com.miui.gallery.video.editor.manager.FilterAdjustManager;
import com.miui.gallery.video.editor.model.AdjustData;
import com.miui.gallery.video.editor.model.FilterAdjustData;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import com.miui.gallery.widget.seekbar.BasicSeekBar;
import com.miui.gallery.widget.seekbar.BiDirectionSeekBar;
import com.miui.gallery.widget.seekbar.BubbleIndicator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class AdjustView extends LinearLayout {
    public AdjustAdapter mAdapter;
    public HashMap<Integer, String> mAdjustCurrentEffects;
    public IAdjustEffectChangeListener mAdjustEffectChangeListener;
    public List<AdjustData> mDataList;
    public IFilterAdjustHeadViewListener mFilterAdjustHeadViewListener;
    public BubbleIndicator<TextView> mIndicator;
    public BubbleIndicator.Callback<TextView> mIndicatorCallback;
    public boolean mIsAdjustView;
    public boolean mIsTracking;
    public SimpleRecyclerViewNoSpring mRecyclerView;
    public SeekBar mSeekBar;
    public SeekBar.OnSeekBarChangeListener mSeekBarChangeListener;
    public OnItemClickListener onAdjustItemClickListener;

    /* loaded from: classes2.dex */
    public interface IAdjustEffectChangeListener {
        void adjustBrightness(int i);

        void adjustContrast(int i);

        void adjustSaturation(int i);

        void adjustSharpness(int i);

        void adjustVignetteRange(int i);
    }

    /* loaded from: classes2.dex */
    public interface IFilterAdjustHeadViewListener {
        void addFilterViewToHeadBar(View view);

        void addSeekBarToHeadBar(View view);

        void removeAllViewFromHeadBar();
    }

    public AdjustView(Context context) {
        super(context);
        this.mIndicatorCallback = new BubbleIndicator.Callback<TextView>() { // from class: com.miui.gallery.video.editor.widget.AdjustView.1
            @Override // com.miui.gallery.widget.seekbar.BubbleIndicator.Callback
            public void updateProgress(TextView textView, int i) {
                textView.setText(String.valueOf(i));
            }
        };
        this.mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.miui.gallery.video.editor.widget.AdjustView.2
            public int value = 0;

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                this.value = i;
                AdjustView.this.mIsTracking = true;
                AdjustView adjustView = AdjustView.this;
                adjustView.setEffect(this.value, (AdjustData) adjustView.mDataList.get(AdjustView.this.mAdapter.getSelection()));
                if (i == 0) {
                    LinearMotorHelper.performHapticFeedback(seekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                AdjustView.this.mIsTracking = true;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                AdjustView.this.mIsTracking = false;
                AdjustView adjustView = AdjustView.this;
                adjustView.setEffect(this.value, (AdjustData) adjustView.mDataList.get(AdjustView.this.mAdapter.getSelection()));
            }
        };
        this.onAdjustItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.video.editor.widget.AdjustView.3
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                if (AdjustView.this.mAdapter.getSelection() == i) {
                    AdjustView.this.updateHeadBar();
                    return true;
                }
                AdjustView.this.mAdapter.setSelection(i);
                AdjustData adjustData = (AdjustData) AdjustView.this.mDataList.get(i);
                if (adjustData != null && (adjustData instanceof FilterAdjustData)) {
                    AdjustView.this.performItemSelect(adjustData);
                }
                return true;
            }
        };
        init(context);
    }

    public final void init(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.video_editor_adjustview, this);
        initData(context);
        initRecyclerView(context);
    }

    public final void initData(Context context) {
        this.mDataList = new ArrayList();
        List<FilterAdjustData> adjustData = FilterAdjustManager.getAdjustData();
        if (adjustData != null && adjustData.size() > 0) {
            for (FilterAdjustData filterAdjustData : adjustData) {
                if (filterAdjustData != null) {
                    this.mDataList.add(filterAdjustData);
                }
            }
        }
        this.mIndicator = new BubbleIndicator<>((TextView) View.inflate(context, R.layout.seekbar_bubble_indicator_text, null), context.getResources().getDimensionPixelSize(R.dimen.photo_editor_bubble_indicator_offset), this.mIndicatorCallback, this.mSeekBarChangeListener);
    }

    public final void initRecyclerView(Context context) {
        this.mRecyclerView = (SimpleRecyclerViewNoSpring) findViewById(R.id.recycler_view);
        AdjustAdapter adjustAdapter = new AdjustAdapter(context, this.mDataList, new Selectable.Selector(getResources().getColor(R.color.photo_editor_highlight_color_old)));
        this.mAdapter = adjustAdapter;
        this.mRecyclerView.setAdapter(adjustAdapter);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mRecyclerView);
        this.mAdapter.setOnItemClickListener(this.onAdjustItemClickListener);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_item_start);
        this.mRecyclerView.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_item_horizontal_interval), 0, 0));
    }

    public void refreshData() {
        AdjustAdapter adjustAdapter = this.mAdapter;
        if (adjustAdapter != null) {
            adjustAdapter.setSelection(-1);
        }
    }

    public final void setEffect(int i, AdjustData adjustData) {
        if (this.mAdapter.getSelection() == -1 || this.mAdjustEffectChangeListener == null) {
            return;
        }
        if (this.mAdjustCurrentEffects == null) {
            this.mAdjustCurrentEffects = new HashMap<>();
        }
        adjustData.setProgress(i);
        if (!(adjustData instanceof FilterAdjustData)) {
            return;
        }
        FilterAdjustData filterAdjustData = (FilterAdjustData) adjustData;
        int id = filterAdjustData.getId();
        if (!this.mAdjustCurrentEffects.containsKey(Integer.valueOf(id))) {
            this.mAdjustCurrentEffects.put(Integer.valueOf(id), filterAdjustData.getLable());
        }
        if (id == 0) {
            this.mAdjustEffectChangeListener.adjustBrightness(i);
        } else if (id == 1) {
            this.mAdjustEffectChangeListener.adjustContrast(i);
        } else if (id == 2) {
            this.mAdjustEffectChangeListener.adjustSaturation(i);
        } else if (id == 3) {
            this.mAdjustEffectChangeListener.adjustSharpness(i);
        } else if (id == 4) {
            this.mAdjustEffectChangeListener.adjustVignetteRange(i);
        } else {
            throw new IllegalArgumentException("not support adjust id: " + id);
        }
    }

    public final void updateHeadBar() {
        IFilterAdjustHeadViewListener iFilterAdjustHeadViewListener = this.mFilterAdjustHeadViewListener;
        if (iFilterAdjustHeadViewListener == null) {
            return;
        }
        if (this.mIsAdjustView) {
            iFilterAdjustHeadViewListener.addFilterViewToHeadBar(null);
        } else {
            removePreviousSeekBar();
            this.mFilterAdjustHeadViewListener.addSeekBarToHeadBar(this.mSeekBar);
        }
        this.mIsAdjustView = !this.mIsAdjustView;
    }

    public final void performItemSelect(AdjustData adjustData) {
        removePreviousSeekBar();
        addNewSeekBar(adjustData);
    }

    public final void addNewSeekBar(AdjustData adjustData) {
        if (!adjustData.isMid()) {
            BasicSeekBar basicSeekBar = new BasicSeekBar(getContext());
            this.mSeekBar = basicSeekBar;
            basicSeekBar.setMax(adjustData.getMax());
            this.mSeekBar.setProgress(adjustData.progress);
        } else {
            BiDirectionSeekBar biDirectionSeekBar = new BiDirectionSeekBar(getContext());
            biDirectionSeekBar.setMaxValue(adjustData.getMax());
            biDirectionSeekBar.setCurrentValue(adjustData.progress);
            this.mSeekBar = biDirectionSeekBar;
        }
        IFilterAdjustHeadViewListener iFilterAdjustHeadViewListener = this.mFilterAdjustHeadViewListener;
        if (iFilterAdjustHeadViewListener != null) {
            this.mIsAdjustView = true;
            iFilterAdjustHeadViewListener.addSeekBarToHeadBar(this.mSeekBar);
        }
        this.mSeekBar.setOnSeekBarChangeListener(this.mIndicator);
    }

    public final void removePreviousSeekBar() {
        BubbleIndicator<TextView> bubbleIndicator = this.mIndicator;
        if (bubbleIndicator != null && bubbleIndicator.isShowing()) {
            this.mIndicator.dismiss();
        }
        IFilterAdjustHeadViewListener iFilterAdjustHeadViewListener = this.mFilterAdjustHeadViewListener;
        if (iFilterAdjustHeadViewListener != null) {
            iFilterAdjustHeadViewListener.removeAllViewFromHeadBar();
        }
    }

    public void setFilterAdjustHeadViewListener(IFilterAdjustHeadViewListener iFilterAdjustHeadViewListener) {
        this.mFilterAdjustHeadViewListener = iFilterAdjustHeadViewListener;
    }

    public void setAdjustEffectChangeListener(IAdjustEffectChangeListener iAdjustEffectChangeListener) {
        this.mAdjustEffectChangeListener = iAdjustEffectChangeListener;
    }

    public boolean doCancel() {
        if (this.mDataList == null) {
            this.mDataList = new ArrayList();
        }
        this.mDataList.clear();
        List<FilterAdjustData> adjustData = FilterAdjustManager.getAdjustData();
        if (adjustData == null || adjustData.size() <= 0) {
            return true;
        }
        for (FilterAdjustData filterAdjustData : adjustData) {
            if (filterAdjustData != null) {
                this.mDataList.add(filterAdjustData);
                setEffect(filterAdjustData.getProgress(), filterAdjustData);
            }
        }
        return true;
    }

    public List<String> getAdjustCurrentEffect() {
        if (this.mAdjustCurrentEffects == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : this.mAdjustCurrentEffects.values()) {
            if (str != null) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public void clearCurrentEffects() {
        HashMap<Integer, String> hashMap = this.mAdjustCurrentEffects;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public boolean isTracking() {
        return this.mIsTracking;
    }
}
