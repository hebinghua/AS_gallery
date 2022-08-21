package com.miui.gallery.ui.album.picker;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryWrapperItemModel;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.picker.PickAlbumActivity;
import com.miui.gallery.picker.PickAlbumDetailActivity;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.PickerImpl;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickAlbumPageFragment extends PickAlbumPageContract$V<PickAlbumPagePresenter> implements PickerImpl {
    public Picker mPicker;

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.album_picker_page;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        addClickEvent();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment
    public void doOnActivityCreated() {
        ((PickAlbumPagePresenter) getPresenter()).setPicker(this.mPicker);
        ((PickAlbumPagePresenter) getPresenter()).initAll();
    }

    public final void addClickEvent() {
        ((GalleryRecyclerView) this.mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.album.picker.PickAlbumPageFragment.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                EpoxyModel<?> model;
                Object itemData;
                RecyclerView.ViewHolder findContainingViewHolder = PickAlbumPageFragment.this.mRecyclerView.findContainingViewHolder(view);
                if (findContainingViewHolder == null || (model = PickAlbumPageFragment.this.mAdapter.getModel(findContainingViewHolder.getAdapterPosition())) == null) {
                    return false;
                }
                if (model instanceof BaseGalleryWrapperItemModel) {
                    itemData = ((BaseGalleryWrapperItemModel) model).getItemData();
                } else {
                    itemData = ((BaseGalleryItemModel) model).getItemData();
                }
                if (itemData instanceof FourPalaceGridCoverViewBean) {
                    long id = ((FourPalaceGridCoverViewBean) itemData).getId();
                    if (Album.isAIAlbums(id)) {
                        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
                        return PickAlbumPageFragment.this.enterPickAIAlbum();
                    } else if (Album.isOtherAlbums(id)) {
                        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
                        return PickAlbumPageFragment.this.enterPickOtherAlbum();
                    }
                } else if (itemData instanceof CommonAlbumItemViewBean) {
                    FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
                    return PickAlbumPageFragment.this.enterPickAlbum((Album) ((CommonAlbumItemViewBean) itemData).getSource());
                } else if (itemData instanceof MediaGroupTypeViewBean) {
                    FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
                    return PickAlbumPageFragment.this.enterMediaTypeGroupDetail((MediaGroupTypeViewBean) itemData);
                }
                return false;
            }
        });
    }

    public final boolean enterPickAIAlbum() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumActivity.class);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("extra_to_type", 1005);
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        startActivityForResult(intent, 1);
        return true;
    }

    public final boolean enterPickOtherAlbum() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumActivity.class);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("extra_to_type", 1003);
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        startActivityForResult(intent, 1);
        return true;
    }

    public final boolean enterPickAlbum(Album album) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumDetailActivity.class);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("other_share_album", album.isOtherShareAlbum());
        intent.putExtra("owner_share_album", album.isOwnerShareAlbum());
        intent.putExtra("album_id", album.getAlbumId());
        intent.putExtra("album_local_path", album.getLocalPath());
        intent.putExtra("album_server_id", album.getServerId());
        intent.putExtra("screenshot_album", album.isScreenshotsAlbum());
        intent.putExtra("screenrecorder_album", album.isScreenRecorderAlbum());
        intent.putExtra("baby_album", album.isBabyAlbum());
        intent.putExtra("album_name", album.getDisplayedAlbumName());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        intent.putExtra("extra_from_type", this.mPicker.getFromType());
        startActivityForResult(intent, 1);
        return true;
    }

    public final boolean enterMediaTypeGroupDetail(MediaGroupTypeViewBean mediaGroupTypeViewBean) {
        if (TextUtils.isEmpty(mediaGroupTypeViewBean.getGotoLink())) {
            DefaultLogger.e("PickAlbumPageFragment", "enterMediaTypeGroupDetail goto failed,empty gotoLink,beans:[%s]", mediaGroupTypeViewBean);
            return false;
        }
        Uri parse = Uri.parse(mediaGroupTypeViewBean.getGotoLink());
        String queryParameter = parse.getQueryParameter("query");
        String queryParameter2 = parse.getQueryParameter("querySelection");
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumDetailActivity.class);
        intent.putExtra("album_name", queryParameter);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        intent.putExtra("extra_from_type", this.mPicker.getFromType());
        intent.putExtra("album_id", 2147383645L);
        intent.putExtra("photo_selection", queryParameter2);
        startActivityForResult(intent, 1);
        return true;
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public void attach(Picker picker) {
        this.mPicker = picker;
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
        DefaultLogger.d("PickAlbumPageFragment", "Pick result of pre album: %s ", Integer.valueOf(set.size()));
        ArrayList arrayList = new ArrayList();
        for (String str : this.mPicker) {
            if (!set.contains(str)) {
                arrayList.add(str);
            }
        }
        DefaultLogger.d("PickAlbumPageFragment", "Deleted items in pre album : %s ", arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.mPicker.remove((String) it.next());
        }
        this.mPicker.pickAll(Lists.newLinkedList(set));
        DefaultLogger.d("PickAlbumPageFragment", "Pick items in pre album : %s ", Integer.valueOf(this.mPicker.count()));
        if (i2 != -1) {
            return;
        }
        this.mPicker.done(-1);
    }
}
