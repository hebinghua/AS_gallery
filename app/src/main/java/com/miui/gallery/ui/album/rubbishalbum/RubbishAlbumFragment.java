package com.miui.gallery.ui.album.rubbishalbum;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.eventhook.OnClickEventHook;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.RubbishItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.TimerDialog;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class RubbishAlbumFragment extends RubbishAlbumContract$V<RubbishAlbumPresenter> {
    public boolean isFirstAddEventHook = true;
    public EditableListViewWrapper mAlbumRecycleViewWrapper;
    public Button mNoMediaManagerBtn;

    public static /* synthetic */ void $r8$lambda$2Su958vE6VH3TvIcgPzonyG2TJY(RubbishAlbumFragment rubbishAlbumFragment) {
        rubbishAlbumFragment.lambda$exitActionMode$1();
    }

    public static /* synthetic */ void $r8$lambda$HbIIKlOffoE0t3bmAibw7WKEj0U(RubbishAlbumFragment rubbishAlbumFragment, View view) {
        rubbishAlbumFragment.lambda$installActionBar$0(view);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.rubbish_album_fragment;
    }

    public static RubbishAlbumFragment newInstance() {
        Bundle bundle = new Bundle();
        RubbishAlbumFragment rubbishAlbumFragment = new RubbishAlbumFragment();
        rubbishAlbumFragment.setArguments(bundle);
        return rubbishAlbumFragment;
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public void initView(View view, Bundle bundle, View view2) {
        super.initView(view, bundle, view2);
        initRecyclerViewWrapper();
        initNoMediaManagerBtn(view);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 1, false));
        setEmptyPage(R.id.album_rubbish_empty_view, getDefaultEmptyConfig().setTitle(R.string.empty_album_rubbish_list_title));
        this.mRecyclerView.setClipToPadding(false);
    }

    public final void initRecyclerViewWrapper() {
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper((GalleryRecyclerView) this.mRecyclerView);
        this.mAlbumRecycleViewWrapper = editableListViewWrapper;
        editableListViewWrapper.setEditActionModeButton2Func(1);
        this.mAlbumRecycleViewWrapper.setAdapter(this.mAdapter);
        this.mAlbumRecycleViewWrapper.enableChoiceMode(true);
        this.mAlbumRecycleViewWrapper.enterChoiceModeWithLongClick(false);
        this.mAlbumRecycleViewWrapper.enableActionModeItemAnim(false);
        this.mAlbumRecycleViewWrapper.disableScaleImageViewAniWhenInActionMode();
        this.mAlbumRecycleViewWrapper.setMultiChoiceModeListener(new RubbishAlbumPagePickModeListener(this));
        this.mAlbumRecycleViewWrapper.setInitState(((RubbishAlbumPresenter) getPresenter()).getInitState());
    }

    public final void initNoMediaManagerBtn(View view) {
        Button button = (Button) view.findViewById(R.id.manage);
        this.mNoMediaManagerBtn = button;
        FolmeUtil.setDefaultTouchAnim(button, null, false, false, true);
        this.mNoMediaManagerBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.1
            {
                RubbishAlbumFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TrackController.trackClick("403.39.2.1.11133", AutoTracking.getRef());
                RubbishAlbumFragment.this.mAlbumRecycleViewWrapper.startActionMode();
            }
        });
    }

    public final void installActionBar() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getSafeActivity();
        if (appCompatActivity == null) {
            DefaultLogger.w("RubbishAlbumFragment", "Activity should not be null");
            return;
        }
        ImageButton imageButton = (ImageButton) ActionBarCompat.setCustomEndViewOnly(appCompatActivity, R.layout.action_bar_rubbish_page);
        if (imageButton == null) {
            return;
        }
        FolmeUtil.addAlphaPressAnim(imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RubbishAlbumFragment.$r8$lambda$HbIIKlOffoE0t3bmAibw7WKEj0U(RubbishAlbumFragment.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$installActionBar$0(View view) {
        deleteFromRubbishAlbumsPage(getDatas());
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void showRubbishListResult(List<RubbishItemItemViewBean> list) {
        setDatas(list);
        if (list != null && !list.isEmpty()) {
            if (this.isFirstAddEventHook) {
                addEventHook();
                this.isFirstAddEventHook = false;
            }
            if (this.mAlbumRecycleViewWrapper.isInActionMode()) {
                return;
            }
            this.mNoMediaManagerBtn.setVisibility(0);
            return;
        }
        this.mNoMediaManagerBtn.setVisibility(8);
    }

    public final void addEventHook() {
        this.mAdapter.addEventHook(new OnClickEventHook<CommonWrapperCheckableLinearAlbumItemModel.VH>(CommonWrapperCheckableLinearAlbumItemModel.VH.class) { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.2
            {
                RubbishAlbumFragment.this = this;
            }

            @Override // com.miui.epoxy.eventhook.OnClickEventHook
            public void onClick(View view, CommonWrapperCheckableLinearAlbumItemModel.VH vh, int i, EpoxyModel epoxyModel) {
                int id = view.getId();
                RubbishItemItemViewBean rubbishItemItemViewBean = (RubbishItemItemViewBean) ((CommonWrapperCheckableLinearAlbumItemModel) epoxyModel).getItemData();
                if (id != CommonAlbumItemModel.ViewHolder.getCoverViewId()) {
                    if (id != R.id.btnMoveTo) {
                        return;
                    }
                    TrackController.trackClick("403.39.1.1.11130", AutoTracking.getRef());
                    RubbishAlbumFragment.this.removeFromRubbishAlbumsPage(rubbishItemItemViewBean);
                } else if (RubbishAlbumFragment.this.mAlbumRecycleViewWrapper.isInChoiceMode() || rubbishItemItemViewBean.isManualHide()) {
                } else {
                    IntentUtil.gotoAlbumDetailPageFromRubbishAlbum(RubbishAlbumFragment.this.getSafeActivity(), (Album) rubbishItemItemViewBean.getSource());
                    TrackController.trackClick("403.39.1.1.11131", AutoTracking.getRef());
                }
            }

            @Override // com.miui.epoxy.eventhook.EventHook
            public List<? extends View> onBindMany(CommonWrapperCheckableLinearAlbumItemModel.VH vh) {
                ArrayList arrayList = new ArrayList(2);
                RubbishItemModel.ViewHolder viewHolder = (RubbishItemModel.ViewHolder) vh.getChildViewHolder();
                arrayList.add(viewHolder.mBtnMoveTo);
                arrayList.add(viewHolder.mAlbumCover);
                return arrayList;
            }
        });
        installActionBar();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((RubbishAlbumPresenter) getPresenter()).initData();
    }

    public void deleteFromRubbishAlbumsPage(final List<RubbishItemItemViewBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        TrackController.trackClick("403.39.0.1.11135", AutoTracking.getRef());
        new AlertDialog.Builder(getActivity()).setMessage(R.string.operation_delete_all_albums_from_rubbish_page_tip).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.3
            {
                RubbishAlbumFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).deleteAllAlbumsFromRubbishPage(list);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment, com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onDeleteAlbumsSuccess(long[] jArr, int i, Collection<Album> collection) {
        if (getSafeActivity() != null) {
            ToastUtils.makeTextLong(GalleryApp.sGetAndroidContext(), (int) R.string.delete_album_success);
            getSafeActivity().finish();
        }
    }

    public void removeFromRubbishAlbumsPage(final RubbishItemItemViewBean rubbishItemItemViewBean) {
        DialogInterface.OnClickListener onClickListener;
        if (rubbishItemItemViewBean == null) {
            return;
        }
        AlertDialog.Builder message = new AlertDialog.Builder(getActivity()).setTitle(R.string.operation_remove_from_rubbish_albums).setMessage(rubbishItemItemViewBean.isManualHide() ? R.string.remove_single_manual_hide_album_from_rubbish_page_tip : R.string.remove_single_album_from_rubbish_page_tip);
        if (rubbishItemItemViewBean.isManualHide()) {
            onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.4
                {
                    RubbishAlbumFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).removeFromRubbishAlbums(rubbishItemItemViewBean);
                    LinkedList linkedList = new LinkedList();
                    linkedList.add(rubbishItemItemViewBean);
                    ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).doRemoveNoMediaForRubbishAlbum(linkedList);
                }
            };
        } else {
            onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.5
                {
                    RubbishAlbumFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).removeFromRubbishAlbums(rubbishItemItemViewBean);
                }
            };
        }
        message.setPositiveButton(17039370, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void doAddRemoveNoMediaForRubbishAlbum(List<Integer> list) {
        final LinkedList linkedList = new LinkedList();
        final LinkedList linkedList2 = new LinkedList();
        ((RubbishAlbumPresenter) getPresenter()).classifyCheckState(list, this.mAdapter.getDatas(), linkedList, linkedList2);
        if (linkedList.size() == 0 && linkedList2.size() == 0) {
            exitActionMode();
        } else {
            new TimerDialog.Builder(getActivity()).setTitle(R.string.manual_hide_manage).setMessage(R.string.add_remove_nomedia_dialog_message).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment.6
                {
                    RubbishAlbumFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (linkedList.size() > 0) {
                        ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).doAddNoMediaForRubbishAlbum(linkedList);
                        TrackController.trackClick("403.39.2.1.11134", "403.39.2.1.11133", linkedList.size());
                    }
                    if (linkedList2.size() > 0) {
                        ((RubbishAlbumPresenter) RubbishAlbumFragment.this.getPresenter()).doRemoveNoMediaForRubbishAlbum(linkedList2);
                    }
                }
            }).setNegativeButton(17039360, null).setConfirmTime(5000L).build().show();
        }
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void removeAlbumFromRubbishIsSuccess(long j, RubbishItemItemViewBean rubbishItemItemViewBean) {
        ToastUtils.makeTextLong(GalleryApp.sGetAndroidContext(), (int) R.string.rubbish_album_move_to_other_album_success);
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void startChoiceMode() {
        this.mNoMediaManagerBtn.setVisibility(8);
        ((RubbishAlbumPresenter) getPresenter()).onStartChoiceMode();
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void stopChoiceMode() {
        this.mNoMediaManagerBtn.setVisibility(0);
        ((RubbishAlbumPresenter) getPresenter()).onStopChoiceMode();
    }

    public /* synthetic */ void lambda$exitActionMode$1() {
        this.mAlbumRecycleViewWrapper.stopActionMode();
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void exitActionMode() {
        this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RubbishAlbumFragment.$r8$lambda$2Su958vE6VH3TvIcgPzonyG2TJY(RubbishAlbumFragment.this);
            }
        });
    }

    @Override // com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$V
    public void update(List<BaseViewBean> list) {
        for (BaseViewBean baseViewBean : list) {
            this.mAdapter.notifyDataChanged(baseViewBean);
        }
    }
}
