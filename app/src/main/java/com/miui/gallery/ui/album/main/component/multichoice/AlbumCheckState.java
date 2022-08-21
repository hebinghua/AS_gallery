package com.miui.gallery.ui.album.main.component.multichoice;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonWrapperCheckableGridAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class AlbumCheckState extends EditableListViewWrapper.CheckState {
    public boolean isAllCheckedMode;
    public EpoxyAdapter mAdapter;
    public int mCheckableItemCount;
    public List<Integer> mCheckablePositions = new ArrayList(0);
    public BitSet mOldCheckStatusLists;

    public AlbumCheckState(EpoxyAdapter epoxyAdapter) {
        this.mAdapter = epoxyAdapter;
        epoxyAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.ui.album.main.component.multichoice.AlbumCheckState.1
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                super.onChanged();
                AlbumCheckState.this.refreshCheckableItemPositions();
            }
        });
    }

    public final void refreshCheckableItemPositions() {
        this.mCheckablePositions = new ArrayList(this.mCheckableItemCount);
        boolean z = this.mUserSelectedIdIndex.size() == 0;
        if (!z) {
            this.mOldCheckStatusLists = (BitSet) this.mCheckState.clone();
        }
        this.mCheckState.clear();
        List<EpoxyModel<?>> models = this.mAdapter.getModels();
        for (int i = 0; i < models.size(); i++) {
            EpoxyModel<?> epoxyModel = models.get(i);
            if ((epoxyModel instanceof CommonWrapperCheckableGridAlbumItemModel) || (epoxyModel instanceof CommonWrapperCheckableLinearAlbumItemModel)) {
                this.mCheckablePositions.add(Integer.valueOf(i));
                this.mCheckState.set(i, z ? this.isAllCheckedMode : this.mOldCheckStatusLists.get(i));
            }
        }
        BitSet bitSet = this.mOldCheckStatusLists;
        if (bitSet != null) {
            bitSet.clear();
        }
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public boolean isAllItemsChecked() {
        int checkedCount = getCheckedCount();
        return checkedCount > 0 && checkedCount == this.mCheckablePositions.size();
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public void setAllChecked(boolean z) {
        this.mStatus = EditableListViewWrapper.CheckState.Status.CLEAN;
        this.mCheckMode = z ? EditableListViewWrapper.CheckState.Mode.REVERSE : EditableListViewWrapper.CheckState.Mode.NORMAL;
        this.mUserSelectedIdIndex.clear();
        this.mReverseModeSelectedIdIndex.clear();
        this.mIndex = 0L;
        this.mReverseIndex = 0L;
        this.isAllCheckedMode = z;
        refreshCheckableItemPositions();
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public int getCheckedCount() {
        if (this.mCheckMode == EditableListViewWrapper.CheckState.Mode.NORMAL) {
            return this.mUserSelectedIdIndex.size();
        }
        return getCheckableItemCount() - this.mUserSelectedIdIndex.size();
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public void clear() {
        super.clear();
        this.isAllCheckedMode = false;
        List<Integer> list = this.mCheckablePositions;
        if (list != null) {
            list.clear();
        }
        this.mCheckableItemCount = 0;
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public long[] getCheckedItemIds() {
        int checkedCount = getCheckedCount();
        int i = 0;
        if (checkedCount == 0) {
            return new long[0];
        }
        if (this.mCheckMode == EditableListViewWrapper.CheckState.Mode.NORMAL) {
            long[] jArr = new long[this.mUserSelectedIdIndex.size()];
            long[] jArr2 = new long[this.mUserSelectedIdIndex.size()];
            for (Map.Entry<Long, Long> entry : this.mUserSelectedIdIndex.entrySet()) {
                jArr[i] = entry.getKey().longValue();
                jArr2[i] = entry.getValue().longValue();
                i++;
            }
            return getSortedKeysByIndex(jArr, jArr2);
        }
        long[] jArr3 = new long[checkedCount];
        int i2 = 0;
        for (Integer num : this.mCheckablePositions) {
            long sourceItemId = this.mSource.getSourceItemId(num.intValue());
            if (!this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceItemId)) && !this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(sourceItemId))) {
                jArr3[i2] = sourceItemId;
                i2++;
            }
        }
        if (this.mReverseModeSelectedIdIndex.size() > 0) {
            long[] jArr4 = new long[this.mReverseModeSelectedIdIndex.size()];
            long[] jArr5 = new long[this.mReverseModeSelectedIdIndex.size()];
            int i3 = 0;
            for (Map.Entry<Long, Long> entry2 : this.mReverseModeSelectedIdIndex.entrySet()) {
                jArr4[i3] = entry2.getKey().longValue();
                jArr5[i3] = entry2.getValue().longValue();
                i3++;
            }
            long[] sortedKeysByIndex = getSortedKeysByIndex(jArr4, jArr5);
            while (i < sortedKeysByIndex.length && i2 < checkedCount) {
                jArr3[i2] = sortedKeysByIndex[i];
                i++;
                i2++;
            }
        }
        return jArr3;
    }

    @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
    public int[] getCheckedItemOrderedPositions() {
        int checkedCount = getCheckedCount();
        int i = 0;
        if (checkedCount == 0) {
            return new int[0];
        }
        if (this.mCheckMode == EditableListViewWrapper.CheckState.Mode.NORMAL) {
            int[] iArr = new int[this.mUserSelectedIdIndex.size()];
            long[] jArr = new long[this.mUserSelectedIdIndex.size()];
            int i2 = 0;
            while (i < this.mSource.getSourceItemCount() && i2 < checkedCount) {
                long sourceItemId = this.mSource.getSourceItemId(i);
                if (this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceItemId))) {
                    iArr[i2] = i;
                    jArr[i2] = this.mUserSelectedIdIndex.get(Long.valueOf(sourceItemId)).longValue();
                    i2++;
                }
                i++;
            }
            return getSortedPositionsByIndex(iArr, jArr);
        }
        int[] iArr2 = new int[checkedCount];
        int[] iArr3 = new int[this.mReverseModeSelectedIdIndex.size()];
        long[] jArr2 = new long[this.mReverseModeSelectedIdIndex.size()];
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < this.mCheckablePositions.size(); i5++) {
            int intValue = this.mCheckablePositions.get(i5).intValue();
            long sourceItemId2 = this.mSource.getSourceItemId(intValue);
            if (!this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceItemId2)) && !this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(sourceItemId2))) {
                iArr2[i3] = intValue;
                i3++;
            }
            if (this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(sourceItemId2))) {
                iArr3[i4] = intValue;
                jArr2[i4] = this.mReverseModeSelectedIdIndex.get(Long.valueOf(sourceItemId2)).longValue();
                i4++;
            }
        }
        if (this.mReverseModeSelectedIdIndex.size() > 0) {
            int[] sortedPositionsByIndex = getSortedPositionsByIndex(iArr3, jArr2);
            while (i < sortedPositionsByIndex.length && i3 < checkedCount) {
                iArr2[i3] = sortedPositionsByIndex[i];
                i++;
                i3++;
            }
        }
        return iArr2;
    }

    public final int getCheckableItemCount() {
        List<Integer> list = this.mCheckablePositions;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
