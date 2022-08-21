package com.miui.gallery.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.search.feedback.SearchFeedbackHelper;
import com.miui.gallery.search.utils.SearchLog;
import miuix.appcompat.app.Fragment;

/* loaded from: classes2.dex */
public class SearchFeedbackTaskController {
    public Fragment mFragment;
    public View mIndicator;
    public TextView mProgress;
    public boolean mResumed;
    public int mTaskStatus = -1;

    public SearchFeedbackTaskController(Fragment fragment, View view) {
        this.mFragment = fragment;
        this.mIndicator = view;
        this.mProgress = (TextView) view.findViewById(R.id.feedback_task_progress);
        this.mIndicator.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SearchFeedbackTaskController.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }
        });
        hideIndicator();
    }

    public void onResume() {
        this.mResumed = true;
        new FeedbackInfoQueryTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void onPause() {
        this.mResumed = false;
    }

    public final void hideIndicator() {
        this.mIndicator.setVisibility(8);
    }

    public final void updateIndicatorUI(int i, int i2) {
        if (this.mTaskStatus < 0) {
            hideIndicator();
            return;
        }
        this.mIndicator.setVisibility(0);
        int i3 = this.mTaskStatus;
        if (i3 == 0) {
            this.mProgress.setText(String.format(this.mFragment.getString(R.string.search_feedback_task_progress), Integer.valueOf(i2), Integer.valueOf(i)));
        } else if (i3 == 1) {
            this.mProgress.setText(R.string.search_feedback_task_complete);
        } else {
            hideIndicator();
        }
    }

    public final int getFeedbackTaskStatus(int i, int i2) {
        if (i > 0) {
            return i2 >= i ? 1 : 0;
        }
        SearchLog.w("SearchFeedbackTaskController", "Something wrong may happened, invalid feedback task num");
        return -1;
    }

    /* loaded from: classes2.dex */
    public class FeedbackInfoQueryTask extends AsyncTask<Void, Void, Bundle> {
        public FeedbackInfoQueryTask() {
        }

        @Override // android.os.AsyncTask
        public Bundle doInBackground(Void... voidArr) {
            try {
                Bundle feedbackTaskInfo = SearchFeedbackHelper.getFeedbackTaskInfo();
                if (feedbackTaskInfo != null && feedbackTaskInfo != Bundle.EMPTY) {
                    feedbackTaskInfo.putInt("task_status", SearchFeedbackTaskController.this.getFeedbackTaskStatus(feedbackTaskInfo.getInt("need_handle_image_num", 0), feedbackTaskInfo.getInt("finish_image_num", 0)));
                    return feedbackTaskInfo;
                }
                return null;
            } catch (Exception unused) {
                SearchLog.e("SearchFeedbackTaskController", "getFeedbackTaskInfo failed");
                return null;
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bundle bundle) {
            SearchLog.e("SearchFeedbackTaskController", "On get bundle %s, resumed %s", bundle, String.valueOf(SearchFeedbackTaskController.this.mResumed));
            if (!SearchFeedbackTaskController.this.mResumed || bundle == null) {
                if (bundle != null) {
                    return;
                }
                SearchFeedbackTaskController.this.hideIndicator();
                return;
            }
            int i = bundle.getInt("need_handle_image_num", 0);
            int i2 = bundle.getInt("finish_image_num", 0);
            SearchFeedbackTaskController.this.mTaskStatus = bundle.getInt("task_status", -1);
            SearchFeedbackTaskController.this.updateIndicatorUI(i, i2);
        }
    }
}
