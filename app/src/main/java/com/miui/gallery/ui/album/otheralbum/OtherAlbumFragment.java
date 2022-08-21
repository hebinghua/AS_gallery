package com.miui.gallery.ui.album.otheralbum;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout;
import com.miui.gallery.ui.ListGalleryDialogFragment;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes2.dex */
public class OtherAlbumFragment extends OtherAlbumContract$V<OtherAlbumPresenter> {
    public EditableListViewWrapper mAlbumRecycleViewWrapper;
    public ArrayList<ListGalleryDialogFragment.ItemData> mMoveModules;

    public static OtherAlbumFragment newInstance() {
        Bundle bundle = new Bundle();
        OtherAlbumFragment otherAlbumFragment = new OtherAlbumFragment();
        otherAlbumFragment.setArguments(bundle);
        return otherAlbumFragment;
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment, com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        initCheckable();
        ArrayList<ListGalleryDialogFragment.ItemData> arrayList = new ArrayList<>(2);
        this.mMoveModules = arrayList;
        arrayList.add(new ListGalleryDialogFragment.ItemData(R.id.menu_remove_from_other_albums, R.string.other_album_operation_remove_from_other_albums));
        this.mMoveModules.add(new ListGalleryDialogFragment.ItemData(R.id.menu_move_to_rubbish_albums, R.string.other_album_operation_move_to_rubbish_albums));
    }

    public void onItemClick(Album album) {
        if (this.mAlbumRecycleViewWrapper.isInChoiceMode() || this.mAlbumRecycleViewWrapper.isInActionMode() || getSafeActivity() == null) {
            return;
        }
        IntentUtil.gotoAlbumDetailPageFromOtherAlbum(getSafeActivity(), album);
    }

    public final void initCheckable() {
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper((GalleryRecyclerView) this.mRecyclerView);
        this.mAlbumRecycleViewWrapper = editableListViewWrapper;
        editableListViewWrapper.setAdapter(this.mAdapter);
        this.mAlbumRecycleViewWrapper.setEnableContinuousPick(false);
        this.mAlbumRecycleViewWrapper.enableChoiceMode(true);
        this.mAlbumRecycleViewWrapper.enterChoiceModeWithLongClick(true);
        this.mAlbumRecycleViewWrapper.enableActionModeItemAnim(false);
        this.mAlbumRecycleViewWrapper.disableScaleImageViewAniWhenInActionMode();
        this.mAlbumRecycleViewWrapper.setMultiChoiceModeListener(new OtherAlbumPageMultiChoiceModeListener(this, ((OtherAlbumPresenter) getPresenter()).getMultiChoiceDataProvider()));
        this.mAlbumRecycleViewWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                if (j == 2131361959) {
                    return false;
                }
                OtherAlbumFragment.this.onItemClick(i);
                return true;
            }
        });
        this.mAlbumRecycleViewWrapper.setEditableListViewItemAnimHelper(new EditableListViewItemAnimHelper.Builder().withDownAlphaEnlargeAnim().withUpAlphaNarrowAnim().build(), CommonWrapperCheckableGridItemLayout.class.getSimpleName());
        this.mAlbumRecycleViewWrapper.enableChoiceMode(true, new EditableListViewWrapper.OnLongClickCheck() { // from class: com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment.2
            @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.OnLongClickCheck
            public boolean canNext(RecyclerView recyclerView, View view, int i, long j) {
                return (j == 2131363249 || j == 2131361959) ? false : true;
            }
        });
    }

    public final void onItemClick(int i) {
        if (!isInChoiceMode() && getSafeActivity() != null && !getDatas().isEmpty()) {
            Object obj = getDatas().get(i);
            if (obj instanceof CommonAlbumItemViewBean) {
                onItemClick((Album) ((CommonAlbumItemViewBean) obj).getSource());
            } else if (!(obj instanceof CustomViewItemViewBean)) {
            } else {
                IntentUtil.gotoRubbishAlbumPage(getSafeActivity());
            }
        }
    }

    public final boolean isInChoiceMode() {
        EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
        return editableListViewWrapper != null && (editableListViewWrapper.isInChoiceMode() || this.mAlbumRecycleViewWrapper.isInActionMode());
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment
    public void gotoRubbishAlbumPage(View view) {
        if (isInChoiceMode()) {
            return;
        }
        super.gotoRubbishAlbumPage(view);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment
    public void onOperationEnd() {
        super.onOperationEnd();
        this.mAlbumRecycleViewWrapper.stopActionMode();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public void startChoiceMode() {
        ((OtherAlbumPresenter) getPresenter()).onStartChoiceMode();
        setRubbishTipViewVisible(false);
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public void stopChoiceMode() {
        ((OtherAlbumPresenter) getPresenter()).onStopChoiceMode();
        setRubbishTipViewVisible(true);
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public long[] getCheckedItemIds() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemIds();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public int[] getCheckedItemOrderedPositions() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemOrderedPositions();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public int getCheckedCount() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemCount();
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getSafeActivity()).inflate(R.menu.album_list_menu, menu);
        return true;
    }

    @Override // com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$V
    public void showMoveOperations(final Collection<Album> collection, boolean z) {
        ListGalleryDialogFragment listGalleryDialogFragment = new ListGalleryDialogFragment();
        listGalleryDialogFragment.setDatas(this.mMoveModules);
        listGalleryDialogFragment.setOnOperationSelectedListener(new ListGalleryDialogFragment.OnOperationSelectedListener() { // from class: com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment.3
            @Override // com.miui.gallery.ui.ListGalleryDialogFragment.OnOperationSelectedListener
            public boolean onOperationSelected(int i) {
                if (i == R.id.menu_move_to_rubbish_albums) {
                    OtherAlbumFragment.this.doAlbumMoveToRubbishAlbums(collection);
                    return true;
                } else if (i != R.id.menu_remove_from_other_albums) {
                    return false;
                } else {
                    OtherAlbumFragment.this.doAlbumRemoveFromOtherAlbums(collection);
                    return true;
                }
            }
        });
        listGalleryDialogFragment.setItemEnable(R.id.menu_move_to_rubbish_albums, z);
        listGalleryDialogFragment.showAllowingStateLoss(getActivity().getSupportFragmentManager(), "ProduceCreationDialog");
    }
}
