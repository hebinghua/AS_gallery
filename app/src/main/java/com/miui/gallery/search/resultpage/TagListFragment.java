package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.BaseSuggestionAdapter;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.feedback.FeedbackPolicyNoticeFragment;
import com.miui.gallery.search.resultpage.SearchResultFragment;
import com.miui.gallery.ui.SearchFeedbackTaskController;

/* loaded from: classes2.dex */
public class TagListFragment extends SearchResultFragment {
    public SearchFeedbackTaskController mSearchFeedbackTaskController;
    public View mSearchFeedbackTaskIndicator;
    public boolean mUserAgreedPolicy = false;
    public FeedbackPolicyNoticeFragment mPolicyNoticeFragment = null;

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public int getLayoutResource() {
        return R.layout.search_tag_list_fragment;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onInflateView(View view, Bundle bundle, Uri uri) {
        super.onInflateView(view, bundle, uri);
        if (this.mInFeedbackTaskMode) {
            if (this.mSearchFeedbackTaskIndicator == null) {
                this.mSearchFeedbackTaskIndicator = ((ViewStub) view.findViewById(R.id.search_feedback_task_indicator)).inflate();
            }
            if (this.mSearchFeedbackTaskController == null) {
                this.mSearchFeedbackTaskController = new SearchFeedbackTaskController(this, this.mSearchFeedbackTaskIndicator);
            }
            if (!isResumed()) {
                return;
            }
            this.mSearchFeedbackTaskController.onResume();
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SearchFeedbackTaskController searchFeedbackTaskController = this.mSearchFeedbackTaskController;
        if (searchFeedbackTaskController != null) {
            searchFeedbackTaskController.onResume();
        }
        if (!this.mInFeedbackTaskMode || !GalleryPreferences.Search.shouldShowFeedbackPolicy() || this.mUserAgreedPolicy) {
            return;
        }
        FeedbackPolicyNoticeFragment feedbackPolicyNoticeFragment = this.mPolicyNoticeFragment;
        if (feedbackPolicyNoticeFragment != null && feedbackPolicyNoticeFragment.isAdded()) {
            return;
        }
        if (this.mPolicyNoticeFragment == null) {
            FeedbackPolicyNoticeFragment feedbackPolicyNoticeFragment2 = (FeedbackPolicyNoticeFragment) getFragmentManager().findFragmentByTag("PolicyNoticeFragment");
            this.mPolicyNoticeFragment = feedbackPolicyNoticeFragment2;
            if (feedbackPolicyNoticeFragment2 == null) {
                FeedbackPolicyNoticeFragment feedbackPolicyNoticeFragment3 = new FeedbackPolicyNoticeFragment();
                this.mPolicyNoticeFragment = feedbackPolicyNoticeFragment3;
                feedbackPolicyNoticeFragment3.setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() { // from class: com.miui.gallery.search.resultpage.TagListFragment.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TagListFragment.this.mUserAgreedPolicy = true;
                    }
                });
            }
        }
        if (this.mPolicyNoticeFragment.isAdded()) {
            return;
        }
        this.mPolicyNoticeFragment.showAllowingStateLoss(getFragmentManager(), "PolicyNoticeFragment");
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SearchFeedbackTaskController searchFeedbackTaskController = this.mSearchFeedbackTaskController;
        if (searchFeedbackTaskController != null) {
            searchFeedbackTaskController.onPause();
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public QueryInfo.Builder getSectionDataQueryInfoBuilder() {
        QueryInfo.Builder sectionDataQueryInfoBuilder = super.getSectionDataQueryInfoBuilder();
        if (this.mInFeedbackTaskMode) {
            sectionDataQueryInfoBuilder.addParam("filterMode", "feedback");
        }
        return sectionDataQueryInfoBuilder;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (bundle != null) {
            this.mUserAgreedPolicy = bundle.getBoolean("UserAgreedPolicy", false);
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("UserAgreedPolicy", this.mUserAgreedPolicy);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public StatusHandleHelper.AbstractErrorViewAdapter getErrorViewAdapter() {
        if (this.mErrorViewAdapter == null) {
            this.mErrorViewAdapter = new TagListErrorViewAdapter(this.mActivity);
        }
        return this.mErrorViewAdapter;
    }

    /* loaded from: classes2.dex */
    public class TagListErrorViewAdapter extends SearchResultFragment.ErrorViewAdapter {
        public TagListErrorViewAdapter(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public int getIconResForStatus(int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (infoViewPosition == StatusHandleHelper.InfoViewPosition.FULL_SCREEN) {
                return R.drawable.ic_pic_empty_things;
            }
            if (infoViewPosition != StatusHandleHelper.InfoViewPosition.FOOTER) {
                return 0;
            }
            return R.drawable.search_connection_error_icon;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public String getInfoTitleForStatus(int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            boolean z = infoViewPosition == StatusHandleHelper.InfoViewPosition.FULL_SCREEN;
            int i2 = R.string.things_album_empty_title;
            if (i != 1) {
                if (i == 10) {
                    i2 = R.string.search_syncing;
                } else if (i == 13) {
                    i2 = R.string.ai_album_requesting_title;
                } else if (i != 3) {
                    if (i != 4) {
                        if (!z) {
                            i2 = R.string.search_error_and_retry;
                        }
                    } else if (!z) {
                        i2 = R.string.search_backup_title;
                    }
                } else if (!z) {
                    i2 = R.string.search_login_title;
                }
            } else if (!z) {
                i2 = R.string.search_connection_error_and_set;
            }
            return this.mContext.getString(i2);
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment
    public void trackLoadComplete() {
        BaseSuggestionAdapter<SuggestionCursor> baseSuggestionAdapter = this.mResultAdapter;
        TimeMonitor.trackTimeMonitor("403.49.0.1.13785", baseSuggestionAdapter == null ? 0L : baseSuggestionAdapter.getItemCount());
    }
}
