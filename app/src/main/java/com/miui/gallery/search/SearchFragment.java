package com.miui.gallery.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransaction;
import com.baidu.platform.comapi.UIMsg;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.miui.gallery.R;
import com.miui.gallery.search.navigationpage.NavigationFragment;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.suggestionpage.SuggestionFragment;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.widget.SearchView;
import com.miui.gallery.ui.BaseFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class SearchFragment extends BaseFragment implements SearchFragmentCallback {
    public boolean mIsFirstCreate;
    public boolean mIsLargeScreenWindow;
    public SearchView mSearchView;
    public SearchFragmentBase mTopVisibleFragment;
    public int mTryShowInputCount = 0;
    public SearchFragmentBase[] mFragments = new SearchFragmentBase[2];
    public Runnable mShowInputMethod = new Runnable() { // from class: com.miui.gallery.search.SearchFragment.1
        @Override // java.lang.Runnable
        public void run() {
            SearchFragment.access$008(SearchFragment.this);
            int i = SearchFragment.this.mIsLargeScreenWindow ? UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME : 200;
            if (SearchFragment.this.mSearchView.showInputMethod() || SearchFragment.this.mTryShowInputCount > 5) {
                return;
            }
            ThreadManager.getMainHandler().postDelayed(SearchFragment.this.mShowInputMethod, i);
        }
    };
    public SearchView.SearchViewListener mSearchViewListener = new SearchView.SearchViewListener() { // from class: com.miui.gallery.search.SearchFragment.3
        @Override // com.miui.gallery.search.widget.SearchView.SearchViewListener
        public void onFocusChanged(View view, boolean z) {
        }

        @Override // com.miui.gallery.search.widget.SearchView.SearchViewListener
        public void onCancelSearch(View view) {
            SearchFragment.this.mSearchView.selectAll(false);
            SearchFragment.this.mSearchView.hideInputMethod();
            SearchFragment.this.finish();
        }

        @Override // com.miui.gallery.search.widget.SearchView.SearchViewListener
        public void onQueryTextSubmit(View view, String str) {
            SearchFragment.this.mSearchView.clearFocus();
            if (TextUtils.isEmpty(SearchFragment.this.getQueryText())) {
                SearchFragment.this.setTopFragment(0);
                SearchFragment.this.mSearchView.showInputMethod();
                ToastUtils.makeText(SearchFragment.this.mActivity, (int) R.string.empty_query_text_msg);
                return;
            }
            SearchFragment.this.setTopFragment(1);
            if (!(SearchFragment.this.mTopVisibleFragment instanceof SuggestionFragment)) {
                return;
            }
            SearchFragment.this.mTopVisibleFragment.setQueryText(SearchFragment.this.getQueryText(), true, false);
        }

        @Override // com.miui.gallery.search.widget.SearchView.SearchViewListener
        public void onQueryTextChanged(View view, String str) {
            if (TextUtils.isEmpty(SearchFragment.this.getQueryText())) {
                SearchFragment.this.setTopFragment(0);
                SearchFragment.this.mSearchView.showInputMethod();
                return;
            }
            SearchFragment.this.setTopFragment(1);
            SearchFragment.this.mTopVisibleFragment.setQueryText(SearchFragment.this.getQueryText(), false, false);
        }

        @Override // com.miui.gallery.search.widget.SearchView.SearchViewListener
        public void onStartVoiceAssistant(View view) {
            IntentUtil.startSpeechInput(SearchFragment.this.mActivity, 43, true, SearchConfig.get().getVoiceAssistantSuggestion(SearchFragment.this.mActivity));
            SearchStatUtils.reportEvent("from_search", "start_voice_assistant");
        }
    };

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return MiStat.Event.SEARCH;
    }

    public boolean onBackPressed() {
        return false;
    }

    public static /* synthetic */ int access$008(SearchFragment searchFragment) {
        int i = searchFragment.mTryShowInputCount;
        searchFragment.mTryShowInputCount = i + 1;
        return i;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.search_fragment, viewGroup, false);
        initTopBar();
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        boolean z;
        String str;
        super.onActivityCreated(bundle);
        boolean z2 = true;
        this.mIsLargeScreenWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        Intent intent = this.mActivity.getIntent();
        String str2 = null;
        if (intent.getData() != null) {
            Uri data = intent.getData();
            str2 = data.getQueryParameter("query");
            str = data.getQueryParameter("queryHint");
            z = data.getBooleanQueryParameter("enableShortcut", false);
        } else {
            z = false;
            str = null;
        }
        if (TextUtils.isEmpty(str2)) {
            if (!TextUtils.isEmpty(str)) {
                this.mSearchView.setQueryText(str);
            }
            setTopFragment(0);
        } else {
            updateQueryFromIntent(str2, z);
        }
        this.mSearchView.setVoiceButtonEnabled(IntentUtil.isSpeechInputSupported());
        if (bundle != null && !bundle.getBoolean("state_first_creation", true)) {
            z2 = false;
        }
        this.mIsFirstCreate = z2;
        configSearchView();
        recordQueryArrivedFromIntent(str2, getFromFromIntent(intent));
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            searchView.setSearchViewListener(null);
        }
        SearchStatUtils.onCompleteSerial("from_search");
        ThreadManager.getMainHandler().removeCallbacks(this.mShowInputMethod);
        SearchView searchView2 = this.mSearchView;
        if (searchView2 != null) {
            searchView2.hideInputMethod();
        }
    }

    public void onNewIntent(Intent intent) {
        if (intent.getData() != null) {
            Uri data = intent.getData();
            String queryParameter = data.getQueryParameter("query");
            boolean booleanQueryParameter = data.getBooleanQueryParameter("enableShortcut", false);
            if (TextUtils.isEmpty(queryParameter)) {
                return;
            }
            updateQueryFromIntent(queryParameter, booleanQueryParameter);
            recordQueryArrivedFromIntent(queryParameter, getFromFromIntent(intent));
        }
    }

    public final void initTopBar() {
        this.mSearchView = new SearchView(this.mActivity);
        this.mActivity.getAppCompatActionBar().setDisplayShowCustomEnabled(true);
        this.mActivity.getAppCompatActionBar().setCustomView(this.mSearchView);
        this.mActivity.getAppCompatActionBar().setDisplayShowTitleEnabled(false);
        this.mActivity.getAppCompatActionBar().setBackgroundDrawable(null);
    }

    public final void configSearchView() {
        this.mSearchView.setSearchViewListener(this.mSearchViewListener);
        if (this.mIsFirstCreate) {
            int i = this.mIsLargeScreenWindow ? UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME : 200;
            this.mSearchView.requestFocus();
            this.mSearchView.selectAll(true);
            this.mSearchView.setCursorVisible(true);
            ThreadManager.getMainHandler().postDelayed(this.mShowInputMethod, i);
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        DefaultLogger.d("SearchFragment", "onSaveInstanceState");
        bundle.putBoolean("state_first_creation", false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 41 || i2 != -1 || intent == null) {
            if (i != 43 || i2 != -1) {
                return;
            }
            String extractQueryFromVoiceAssistantResult = extractQueryFromVoiceAssistantResult(intent);
            if (TextUtils.isEmpty(extractQueryFromVoiceAssistantResult)) {
                return;
            }
            updateQueryFromIntent(extractQueryFromVoiceAssistantResult, false);
            SearchStatUtils.reportEvent("from_search", "receive_voice_assistant_result", "query", extractQueryFromVoiceAssistantResult);
            return;
        }
        String string = intent.getExtras().getString("server_id_of_album");
        String string2 = intent.getExtras().getString("origin_album_name");
        String string3 = intent.getExtras().getString("mark_album_name");
        String string4 = intent.getExtras().getString("album_name");
        if (TextUtils.isEmpty(string4)) {
            string4 = string3;
        }
        String string5 = intent.getExtras().getString("mark_relation");
        String queryText = getQueryText();
        if (!TextUtils.isEmpty(string3) && !TextUtils.isEmpty(string4) && !TextUtils.isEmpty(queryText)) {
            String replaceFirst = queryText.replaceFirst(string3, string4);
            SearchConfig.get().getSuggestionConfig().addQueryExtra(string4, string2, string);
            this.mSearchView.setQueryText(replaceFirst);
            this.mSearchViewListener.onQueryTextSubmit(this.mSearchView, replaceFirst);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("peopleServerID", StringUtils.nullToEmpty(string));
        hashMap.put("originName", StringUtils.nullToEmpty(string2));
        hashMap.put("newName", StringUtils.nullToEmpty(string4));
        hashMap.put("markName", StringUtils.nullToEmpty(string3));
        hashMap.put("queryText", StringUtils.nullToEmpty(queryText));
        hashMap.put("markRelation", StringUtils.nullToEmpty(string5));
        SearchStatUtils.reportEvent("from_guide", "suggestion_mark_people", hashMap);
    }

    public final String getQueryText() {
        if (this.mSearchView.getQueryText() == null) {
            return null;
        }
        return this.mSearchView.getQueryText().trim();
    }

    public final void updateQueryFromIntent(final String str, final boolean z) {
        this.mSearchView.setQueryText(str);
        setTopFragment(1);
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.SearchFragment.2
            @Override // java.lang.Runnable
            public void run() {
                if (SearchFragment.this.mTopVisibleFragment instanceof SuggestionFragment) {
                    SearchFragment.this.mTopVisibleFragment.setQueryText(str, true, z);
                }
            }
        });
    }

    public final void recordQueryArrivedFromIntent(String str, String str2) {
        SearchStatUtils.createNewSerial("from_search");
        HashMap hashMap = new HashMap();
        hashMap.put("queryText", str);
        hashMap.put("srcPage", str2);
        SearchStatUtils.cacheEvent(null, "client_enter_search_page", hashMap);
    }

    public final String getFromFromIntent(Intent intent) {
        String stringExtra = intent.getStringExtra("from");
        return (!TextUtils.isEmpty(stringExtra) || intent.getData() == null) ? stringExtra : intent.getData().getQueryParameter("from");
    }

    public final String extractQueryFromVoiceAssistantResult(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("android.speech.extra.RESULTS");
            if (bundleExtra != null) {
                JsonObject jsonObject = (JsonObject) new Gson().fromJson(bundleExtra.getString("nlpIntention"), (Class<Object>) JsonObject.class);
                JsonElement jsonElement = jsonObject.get("search_query");
                if (jsonElement != null && !TextUtils.isEmpty(jsonElement.getAsString())) {
                    return jsonElement.getAsString();
                }
                return jsonObject.get("query").getAsString();
            }
            return intent.getStringArrayListExtra("android.speech.extra.RESULTS").get(0);
        } catch (Exception e) {
            SearchLog.w("SearchFragment", "Error occurred while extracting query from voice assistant result, %s", e);
            return null;
        }
    }

    @Override // com.miui.gallery.search.SearchFragmentCallback
    public void requestIME(final boolean z) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.SearchFragment.4
            @Override // java.lang.Runnable
            public void run() {
                if (z) {
                    SearchFragment.this.mSearchView.showInputMethod();
                } else {
                    SearchFragment.this.mSearchView.hideInputMethod();
                }
            }
        });
    }

    public final boolean isTopFragment(int i) {
        SearchFragmentBase searchFragmentBase = this.mTopVisibleFragment;
        return searchFragmentBase != null && searchFragmentBase == getFragment(i);
    }

    public final void setTopFragment(int i) {
        if (!isTopFragment(i)) {
            FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
            SearchFragmentBase fragment = getFragment(i);
            if (fragment == null) {
                fragment = ensureFragment(i);
            }
            beginTransaction.replace(R.id.search_fragments, fragment, String.valueOf(i));
            beginTransaction.commitAllowingStateLoss();
            this.mTopVisibleFragment = fragment;
        }
    }

    public final SearchFragmentBase getFragment(int i) {
        return this.mFragments[i];
    }

    public final SearchFragmentBase ensureFragment(int i) {
        if (i == 0) {
            SearchFragmentBase[] searchFragmentBaseArr = this.mFragments;
            if (searchFragmentBaseArr[i] == null) {
                searchFragmentBaseArr[i] = new NavigationFragment();
            }
        } else if (i == 1) {
            SearchFragmentBase[] searchFragmentBaseArr2 = this.mFragments;
            if (searchFragmentBaseArr2[i] == null) {
                searchFragmentBaseArr2[i] = new SuggestionFragment();
            }
        }
        this.mFragments[i].setSearchFragmentCallback(this);
        return getFragment(i);
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsLargeScreenWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            searchView.config();
        }
    }
}
