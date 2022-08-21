package com.miui.gallery.search;

import com.miui.gallery.ui.BaseFragment;

/* loaded from: classes2.dex */
public abstract class SearchFragmentBase extends BaseFragment {
    public SearchFragmentCallback mCallback;

    public void setQueryText(String str, boolean z, boolean z2) {
    }

    public void setSearchFragmentCallback(SearchFragmentCallback searchFragmentCallback) {
        this.mCallback = searchFragmentCallback;
    }

    public SearchFragmentCallback getCallback() {
        return this.mCallback;
    }
}
