package com.miui.gallery.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.activity.CleanerPhotoPickActivity;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.PickCleanerPhotoAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.SimpleMultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.gallery.support.MiuiSdkCompat;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class CleanerPhotoPickFragment extends PhotoListFragmentBase<PickCleanerPhotoAdapter> {
    public static final int[] DELETE_COUNT_STAGE = {20, 50, 100, 200, 500, 1000};
    public PickCleanerPhotoAdapter mAdapter;
    public Button mDeleteButton;
    public EditableListViewWrapper mEditableWrapper;
    public List<Long> mScanResultIds;
    public BaseScanner mScanner;
    public Button mSelectButton;
    public int mType;
    public boolean mIsFirstLoadFinish = true;
    public CleanerKeyboardShortcutCallback mShortcutCallback = new CleanerKeyboardShortcutCallback();
    public RecyclerView.AdapterDataObserver mDataSetObserver = new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment.1
        {
            CleanerPhotoPickFragment.this = this;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            CleanerPhotoPickFragment cleanerPhotoPickFragment = CleanerPhotoPickFragment.this;
            if (cleanerPhotoPickFragment.mIsFirstLoadFinish && cleanerPhotoPickFragment.mo1564getAdapter().getItemCount() > 0) {
                CleanerPhotoPickFragment cleanerPhotoPickFragment2 = CleanerPhotoPickFragment.this;
                cleanerPhotoPickFragment2.mIsFirstLoadFinish = false;
                cleanerPhotoPickFragment2.mEditableWrapper.setAllItemsCheckState(true);
            }
            CleanerPhotoPickFragment.this.onItemSelectedChanged();
        }
    };
    public SimpleMultiChoiceModeListener mMultiChoiceModeListener = new SimpleMultiChoiceModeListener() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment.2
        {
            CleanerPhotoPickFragment.this = this;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            CleanerPhotoPickFragment.this.onItemSelectedChanged();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            CleanerPhotoPickFragment.this.onItemSelectedChanged();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void statGroupItemsCheckedStateChanged(boolean z) {
            if (z) {
                CleanerPhotoPickFragment.this.recordSelectGroupEvent();
            }
        }
    };
    public View.OnClickListener mDeleteButtonClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment.3
        {
            CleanerPhotoPickFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CleanerPhotoPickFragment.this.doDelete();
        }
    };
    public View.OnClickListener mSelectListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment.4
        {
            CleanerPhotoPickFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean z = !CleanerPhotoPickFragment.this.mEditableWrapper.isAllItemsChecked();
            CleanerPhotoPickFragment.this.mEditableWrapper.setAllItemsCheckState(z);
            LinearMotorHelper.performHapticFeedback(CleanerPhotoPickFragment.this.mRecyclerView, LinearMotorHelper.HAPTIC_MESH_LIGHT);
            if (!z) {
                CleanerPhotoPickFragment.this.recordCancelSelectAllEvent();
            } else {
                CleanerPhotoPickFragment.this.recordSelectAllEvent();
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$VFFXUsEEGlA7XAq2qOkgz9DsLEM(CleanerPhotoPickFragment cleanerPhotoPickFragment, DeleteMessage deleteMessage, int i, long[] jArr) {
        cleanerPhotoPickFragment.lambda$doDelete$0(deleteMessage, i, jArr);
    }

    /* renamed from: $r8$lambda$rgUPcDmrLh1HeR1KZxCxW-FtVZE */
    public static /* synthetic */ void m1440$r8$lambda$rgUPcDmrLh1HeR1KZxCxWFtVZE(CleanerPhotoPickFragment cleanerPhotoPickFragment, int i) {
        cleanerPhotoPickFragment.lambda$onInflateView$1(i);
    }

    public DeleteMessage getDeleteMessage() {
        return null;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String[] getSelectionArgs() {
        return null;
    }

    public abstract void recordCancelSelectAllEvent();

    public abstract void recordDeleteEvent(int i);

    public abstract void recordSelectAllEvent();

    public abstract void recordSelectGroupEvent();

    public final void doDelete() {
        final DeleteMessage deleteMessage;
        long[] checkedItemIds = this.mEditableWrapper.getCheckedItemIds();
        if (checkedItemIds == null || checkedItemIds.length <= 0 || (deleteMessage = getDeleteMessage()) == null) {
            return;
        }
        recordDeleteEvent(checkedItemIds.length);
        MediaAndAlbumOperations.delete(this.mActivity, "CleanerPhotoPickFragmentDeleteMediaDialogFragment", new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public final void onDeleted(int i, long[] jArr) {
                CleanerPhotoPickFragment.$r8$lambda$VFFXUsEEGlA7XAq2qOkgz9DsLEM(CleanerPhotoPickFragment.this, deleteMessage, i, jArr);
            }
        }, null, -1L, "", deleteMessage.getReason(), checkedItemIds);
    }

    public /* synthetic */ void lambda$doDelete$0(DeleteMessage deleteMessage, int i, long[] jArr) {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null) {
            return;
        }
        ToastUtils.makeText(appCompatActivity, getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
        if (i > 0) {
            SoundUtils.playSoundForOperation(this.mActivity, 0);
        }
        this.mScanner.removeItems(jArr);
        resetScanResult();
        resetCheckState();
        if (this.mScanResultIds.size() <= 0 && !this.mScanner.isLoadingValid()) {
            this.mActivity.finish();
        }
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, SamplingStatHelper.formatValueStage(i, DELETE_COUNT_STAGE));
        SamplingStatHelper.recordCountEvent("cleaner", deleteMessage.getCleanerSubEvent(), hashMap);
    }

    public CleanerPhotoPickFragment(int i) {
        this.mType = i;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        Button button = (Button) onInflateView.findViewById(R.id.delete);
        this.mDeleteButton = button;
        button.setOnClickListener(this.mDeleteButtonClickListener);
        Button selectAllButton = ((CleanerPhotoPickActivity) this.mActivity).getSelectAllButton();
        this.mSelectButton = selectAllButton;
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, selectAllButton, 0);
        this.mSelectButton.setOnClickListener(this.mSelectListener);
        PickCleanerPhotoAdapter mo1564getAdapter = mo1564getAdapter();
        mo1564getAdapter.setCurrentSortBy(SortBy.CREATE_DATE);
        mo1564getAdapter.setAlbumType(AlbumType.NORMAL);
        mo1564getAdapter.registerAdapterDataObserver(this.mDataSetObserver);
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.CleanerPhotoPickFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
            public final void onStateChanged(int i) {
                CleanerPhotoPickFragment.m1440$r8$lambda$rgUPcDmrLh1HeR1KZxCxWFtVZE(CleanerPhotoPickFragment.this, i);
            }
        });
        this.mRecyclerView.setScrollingCalculator(mo1564getAdapter);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        this.mEditableWrapper = new EditableListViewWrapper(this.mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumns);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, gridLayoutManager)));
        this.mEditableWrapper.setLayoutManager(gridLayoutManager);
        this.mEditableWrapper.setAdapter(mo1564getAdapter);
        this.mEditableWrapper.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mEditableWrapper.setMultiChoiceModeListener(this.mMultiChoiceModeListener);
        this.mEditableWrapper.disableScaleImageViewAniWhenInActionMode();
        this.mEditableWrapper.startChoiceMode();
        this.mScanner = ScannerManager.getInstance().getScanner(this.mType);
        resetScanResult();
        updateConfiguration();
        return onInflateView;
    }

    public /* synthetic */ void lambda$onInflateView$1(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration();
    }

    public final void updateConfiguration() {
        mo1564getAdapter().setSpacing(this.mSpacing);
        mo1564getAdapter().setSpanCount(this.mColumns);
        Button button = this.mDeleteButton;
        if (button != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) button.getLayoutParams();
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.cleaner_pick_action_button_margin_end);
            layoutParams.rightMargin = dimensionPixelOffset;
            layoutParams.leftMargin = dimensionPixelOffset;
            this.mDeleteButton.setLayoutParams(layoutParams);
        }
    }

    public void resetCheckState() {
        this.mEditableWrapper.setAllItemsCheckState(false);
    }

    public void resetScanResult() {
        this.mScanResultIds = this.mScanner.getScanResultIds();
    }

    public void onItemSelectedChanged() {
        if (mo1564getAdapter().getItemCount() > 0) {
            this.mDeleteButton.setEnabled(this.mEditableWrapper.getCheckedItemCount() > 0);
            this.mSelectButton.setVisibility(0);
            MiuiSdkCompat.setEditActionModeButton(this.mActivity, this.mSelectButton, this.mEditableWrapper.isAllItemsChecked() ? 1 : 0);
            return;
        }
        this.mDeleteButton.setEnabled(false);
        this.mSelectButton.setVisibility(8);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public PickCleanerPhotoAdapter mo1564getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new PickCleanerPhotoAdapter(this.mActivity, this.mRecyclerView, getLifecycle());
        }
        return this.mAdapter;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public Uri getUri() {
        return GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("media_group_by", String.valueOf(7)).build();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getSelection() {
        return String.format("%s IN (%s)", j.c, TextUtils.join(",", this.mScanResultIds));
    }

    /* loaded from: classes2.dex */
    public static class DeleteMessage {
        public String mCleanerSubEvent;
        public int mReason;

        public DeleteMessage(String str, int i) {
            this.mCleanerSubEvent = str;
            this.mReason = i;
        }

        public String getCleanerSubEvent() {
            return this.mCleanerSubEvent;
        }

        public int getReason() {
            return this.mReason;
        }

        /* loaded from: classes2.dex */
        public static class Builder {
            public String mCleanerSubEvent;
            public int mReason = -1;

            public Builder setCleanerSubEvent(String str) {
                this.mCleanerSubEvent = str;
                return this;
            }

            public Builder setReason(int i) {
                this.mReason = i;
                return this;
            }

            public DeleteMessage build() {
                int i;
                String str = this.mCleanerSubEvent;
                if (str == null || (i = this.mReason) == -1) {
                    throw new IllegalArgumentException();
                }
                return new DeleteMessage(str, i);
            }
        }
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
        if (getDeleteMessage() != null) {
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class CleanerKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public CleanerKeyboardShortcutCallback() {
            CleanerPhotoPickFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            CleanerPhotoPickFragment.this.mEditableWrapper.setAllItemsCheckState(true);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (CleanerPhotoPickFragment.this.getDeleteMessage() != null) {
                CleanerPhotoPickFragment.this.doDelete();
                return true;
            }
            return true;
        }
    }
}
