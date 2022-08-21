package com.miui.gallery.ui.album.picker.ai;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.google.common.collect.Lists;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.eventhook.OnClickEventHook;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.picker.PickAlbumActivity;
import com.miui.gallery.picker.PickFaceAlbumActivity;
import com.miui.gallery.picker.PickPeopleActivity;
import com.miui.gallery.picker.PickSearchAlbumDetailActivity;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.PickerImpl;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment;
import com.miui.gallery.ui.album.aialbum.AIAlbumPagePresenter;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.c.b;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickAIAlbumFragment extends AIAlbumPageFragment implements PickerImpl {
    public Picker mPicker;

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment, com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public boolean isMapAlbumAvailable() {
        return false;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment, com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public void showPageDatas(List<BaseViewBean> list, List<EpoxyModel<?>> list2) {
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        setDatasAndModels(list, list2, false);
        addClickEvent();
    }

    public final void addClickEvent() {
        if (this.isFirstInitItemClickListener) {
            this.mAdapter.addEventHook(new OnClickEventHook<EpoxyViewHolder>(EpoxyViewHolder.class) { // from class: com.miui.gallery.ui.album.picker.ai.PickAIAlbumFragment.1
                @Override // com.miui.epoxy.eventhook.OnClickEventHook
                public void onClick(View view, EpoxyViewHolder epoxyViewHolder, int i, EpoxyModel epoxyModel) {
                    Object itemData = ((BaseGalleryItemModel) epoxyModel).getItemData();
                    if (itemData instanceof PeopleFaceAlbumViewBean) {
                        PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = (PeopleFaceAlbumViewBean) itemData;
                        if (peopleFaceAlbumViewBean.getFaceRectF() == null || peopleFaceAlbumViewBean.isMoreStyle()) {
                            PickAIAlbumFragment.this.gotoAllPeople();
                        } else {
                            PickAIAlbumFragment.this.gotoFace(view, peopleFaceAlbumViewBean);
                        }
                    } else if (!(itemData instanceof LocationAndTagsAlbumItemViewBean)) {
                    } else {
                        Uri locationOrTagsIntentUri = ((AIAlbumPagePresenter) PickAIAlbumFragment.this.getPresenter()).getLocationOrTagsIntentUri(itemData);
                        Uri uri = GalleryContract.Search.URI_LOCATION_LIST_PAGE;
                        if (uri.equals(locationOrTagsIntentUri)) {
                            PickAIAlbumFragment.this.gotoSearchList(1006, uri);
                            return;
                        }
                        Uri uri2 = GalleryContract.Search.URI_TAG_LIST_PAGE;
                        if (uri2.equals(locationOrTagsIntentUri)) {
                            PickAIAlbumFragment.this.gotoSearchList(b.g, uri2);
                        } else {
                            PickAIAlbumFragment.this.gotoSearchResult(view, locationOrTagsIntentUri);
                        }
                    }
                }

                @Override // com.miui.epoxy.eventhook.EventHook
                public View onBind(EpoxyViewHolder epoxyViewHolder) {
                    return epoxyViewHolder.itemView;
                }
            });
            this.isFirstInitItemClickListener = false;
        }
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public void attach(Picker picker) {
        this.mPicker = picker;
    }

    public final void gotoAllPeople() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickPeopleActivity.class);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("album_name", ResourceUtils.getString(R.string.album_name_people));
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        startActivityForResult(intent, 1);
    }

    public final void gotoSearchList(int i, Uri uri) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickAlbumActivity.class);
        intent.setData(uri);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("extra_to_type", i);
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        startActivityForResult(intent, 3);
    }

    public final void gotoFace(View view, PeopleFaceAlbumViewBean peopleFaceAlbumViewBean) {
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickFaceAlbumActivity.class);
        intent.putExtra("server_id_of_album", peopleFaceAlbumViewBean.getPeopleServerId());
        intent.putExtra("local_id_of_album", String.valueOf(peopleFaceAlbumViewBean.getPeopleLocalId()));
        intent.putExtra("album_name", peopleFaceAlbumViewBean.getTitle());
        intent.putExtra("relationType", peopleFaceAlbumViewBean.getRelationType());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        intent.putExtra("ai_album", true);
        startActivityForResult(intent, 2);
    }

    public final void gotoSearchResult(View view, Uri uri) {
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
        Intent intent = new Intent();
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null && !lastPathSegment.equals("result")) {
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            intent.setPackage(GalleryApp.sGetAndroidContext().getPackageName());
            getSafeActivity().startActivity(intent);
            return;
        }
        intent.setClass(getActivity(), PickSearchAlbumDetailActivity.class);
        intent.setData(uri);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        intent.putExtra("ai_album", true);
        startActivityForResult(intent, 4);
    }

    @Override // androidx.fragment.app.Fragment
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
        DefaultLogger.d("PickAIAlbumFragment", "Pick result of pre album: %s ", Integer.valueOf(set.size()));
        ArrayList arrayList = new ArrayList();
        for (String str : this.mPicker) {
            if (!set.contains(str)) {
                arrayList.add(str);
            }
        }
        DefaultLogger.d("PickAIAlbumFragment", "Deleted items in pre album : %s ", arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.mPicker.remove((String) it.next());
        }
        this.mPicker.pickAll(Lists.newLinkedList(set));
        DefaultLogger.d("PickAIAlbumFragment", "Pick items in pre album : %s ", Integer.valueOf(this.mPicker.count()));
        if (i2 != -1) {
            return;
        }
        this.mPicker.done(-1);
    }
}
