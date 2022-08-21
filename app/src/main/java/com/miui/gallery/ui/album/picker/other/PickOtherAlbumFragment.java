package com.miui.gallery.ui.album.picker.other;

import android.content.Intent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryWrapperItemModel;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.picker.PickAlbumDetailActivity;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.PickerImpl;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickOtherAlbumFragment extends PickOtherAlbumContract$V<PickOtherAlbumPresenter> implements PickerImpl {
    public Picker mPicker;

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment, com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        addClickEvent();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment, com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$V
    public void showOthersAlbumResult(List<CommonAlbumItemViewBean> list) {
        setDatas(list);
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment
    public void doOnActivityCreated() {
        ((PickOtherAlbumPresenter) getPresenter()).setPicker(this.mPicker);
        ((PickOtherAlbumPresenter) getPresenter()).initData();
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public void attach(Picker picker) {
        this.mPicker = picker;
    }

    public final void addClickEvent() {
        ((GalleryRecyclerView) this.mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.album.picker.other.PickOtherAlbumFragment.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                EpoxyModel<?> model;
                Object itemData;
                RecyclerView.ViewHolder findContainingViewHolder = PickOtherAlbumFragment.this.mRecyclerView.findContainingViewHolder(view);
                if (findContainingViewHolder == null || (model = PickOtherAlbumFragment.this.mAdapter.getModel(findContainingViewHolder.getAdapterPosition())) == null) {
                    return false;
                }
                if (model instanceof BaseGalleryWrapperItemModel) {
                    itemData = ((BaseGalleryWrapperItemModel) model).getItemData();
                } else {
                    itemData = ((BaseGalleryItemModel) model).getItemData();
                }
                if (!(itemData instanceof CommonAlbumItemViewBean)) {
                    return false;
                }
                return PickOtherAlbumFragment.this.enterPickAlbum(view, (Album) ((CommonAlbumItemViewBean) itemData).getSource());
            }
        });
    }

    public final boolean enterPickAlbum(View view, Album album) {
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumDetailActivity.class);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("other_share_album", album.isOtherShareAlbum());
        intent.putExtra("owner_share_album", album.isOwnerShareAlbum());
        intent.putExtra("baby_album", album.isBabyAlbum());
        intent.putExtra("album_id", album.getAlbumId());
        intent.putExtra("album_local_path", album.getLocalPath());
        intent.putExtra("screenrecorder_album", album.isScreenRecorderAlbum());
        intent.putExtra("album_name", album.getDisplayedAlbumName());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        startActivityForResult(intent, 1);
        return true;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 0) {
            this.mPicker.cancel();
            return;
        }
        Set set = (Set) intent.getSerializableExtra("internal_key_updated_selection");
        if (set == null) {
            return;
        }
        DefaultLogger.d("PickOtherAlbumFragment", "Pick result of pre album: %s ", Integer.valueOf(set.size()));
        ArrayList arrayList = new ArrayList();
        for (String str : this.mPicker) {
            if (!set.contains(str)) {
                arrayList.add(str);
            }
        }
        DefaultLogger.d("PickOtherAlbumFragment", "Deleted items in pre album : %s ", arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.mPicker.remove((String) it.next());
        }
        this.mPicker.pickAll(Lists.newLinkedList(set));
        DefaultLogger.d("PickOtherAlbumFragment", "Pick items in pre album : %s ", Integer.valueOf(this.mPicker.count()));
        if (i2 != -1) {
            return;
        }
        this.mPicker.done(-1);
    }
}
