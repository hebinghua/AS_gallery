package com.miui.gallery.picker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.google.common.collect.Lists;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.LocationListAdapter;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.resultpage.LocationListFragment;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public class PickLocationListFragment extends LocationListFragment implements PickerImpl {
    public Picker mPicker;

    @Override // com.miui.gallery.search.resultpage.LocationListFragment
    public boolean isMapAlbumAvailable(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        return false;
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public void attach(Picker picker) {
        this.mPicker = picker;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment
    public OnActionClickListener getOnActionClickListener(Context context) {
        return new OnActionClickListener() { // from class: com.miui.gallery.picker.PickLocationListFragment.1
            @Override // com.miui.gallery.search.core.display.OnActionClickListener
            public void onClick(View view, int i, Object obj, Bundle bundle) {
                PickLocationListFragment.this.enterLocationSearchImageResult(view, Uri.parse(((ListSuggestionCursor) obj).getCurrent().getIntentActionURI()));
            }
        };
    }

    @Override // com.miui.gallery.search.resultpage.LocationListFragment, com.miui.gallery.search.resultpage.SearchResultFragment
    public void configAdapter() {
        super.configAdapter();
        ((LocationListAdapter) this.mResultAdapter).setMapAlbumAvailable(false);
    }

    public final void enterLocationSearchImageResult(View view, Uri uri) {
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
        Intent intent = new Intent();
        intent.setClass(getActivity(), PickSearchAlbumDetailActivity.class);
        intent.setData(uri);
        intent.putExtra("extra_filter_media_type", this.mPicker.getFilterMimeTypes());
        intent.putExtra("pick-upper-bound", this.mPicker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", this.mPicker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(this.mPicker));
        intent.putExtra("picker_result_type", this.mPicker.getResultType().ordinal());
        intent.putExtra("ai_album", true);
        startActivityForResult(intent, 1);
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
        DefaultLogger.d("PickLocationListFragment", "Pick result of pre album: %s ", Integer.valueOf(set.size()));
        ArrayList arrayList = new ArrayList();
        for (String str : this.mPicker) {
            if (!set.contains(str)) {
                arrayList.add(str);
            }
        }
        DefaultLogger.d("PickLocationListFragment", "Deleted items in pre album : %s ", arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.mPicker.remove((String) it.next());
        }
        this.mPicker.pickAll(Lists.newLinkedList(set));
        DefaultLogger.d("PickLocationListFragment", "Pick items in pre album : %s ", Integer.valueOf(this.mPicker.count()));
        if (i2 != -1) {
            return;
        }
        this.mPicker.done(-1);
    }
}
